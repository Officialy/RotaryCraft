package reika.rotarycraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.DragonOptions;
import reika.dragonapi.base.BlockEntityBase;
import reika.dragonapi.instantiable.data.immutable.WorldLocation;
import reika.dragonapi.instantiable.io.SyncPacket;
import reika.dragonapi.io.CompoundSyncPacket;
import reika.dragonapi.libraries.io.CustomNetworkBridge.DataPacketPayload;
import reika.rotarycraft.registry.RotaryBlocks;

/** Real tile identities/levels/players and captured native payload batches. */
final class DragonAPICompoundSyncTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        RotaryGameTests.register(event,env,"dragonapi_compound_coalescing",60,DragonAPICompoundSyncTests::coalescing);
        RotaryGameTests.register(event,env,"dragonapi_compound_retries",60,DragonAPICompoundSyncTests::retry);
        RotaryGameTests.register(event,env,"dragonapi_compound_stale_dimension",60,DragonAPICompoundSyncTests::stale);
        RotaryGameTests.register(event,env,"dragonapi_compound_snapshot_codec",60,DragonAPICompoundSyncTests::codec);
        RotaryGameTests.register(event,env,"dragonapi_compound_mode_transition",60,DragonAPICompoundSyncTests::mode);
        RotaryGameTests.register(event,env,"dragonapi_compound_thread_guard",60,DragonAPICompoundSyncTests::thread);
    }
    private static BlockEntityBase tile(GameTestHelper h, int x) {
        BlockPos pos=h.absolutePos(new BlockPos(x,1,4));
        h.getLevel().setBlock(pos,RotaryBlocks.DC_ENGINE.get().defaultBlockState(),3);
        return (BlockEntityBase)h.getLevel().getBlockEntity(pos);
    }
    private static ServerPlayer player(GameTestHelper h, BlockPos pos) {
        var p=DragonAPIAuditTests.player(h); p.setPos(pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5); return p;
    }
    private static CompoundTag state(int value, boolean recipe) {
        CompoundTag tag=new CompoundTag(); tag.putInt("value",value); if(recipe)tag.putString("recipe","active");return tag;
    }
    private static CompoundTag patch(CustomPacketPayload payload) {
        var input=new FriendlyByteBuf(io.netty.buffer.Unpooled.wrappedBuffer(((DataPacketPayload)payload).data()));
        try {
            hType(input); input.readBlockPos(); input.readVarInt();
            return java.util.Objects.requireNonNull(input.readNbt());
        } finally { input.release(); }
    }
    private static void hType(FriendlyByteBuf input) {
        if(input.readUnsignedByte()!=reika.dragonapi.auxiliary.PacketTypes.BE_NBT_SYNC.wireId())throw new AssertionError("existing sync category");
        int size=input.readVarInt();if(size!=input.readableBytes())throw new AssertionError("existing sync framing");
    }
    private static final class Capture extends CompoundSyncPacket {
        final UUID receiver; final ArrayList<List<CustomPacketPayload>> batches=new ArrayList<>();
        boolean fail; Runnable duringSend;
        Capture(ServerPlayer player){receiver=player.getUUID();}
        @Override protected boolean supports(ServerPlayer player,CustomPacketPayload payload){return player.getUUID().equals(receiver);}
        @Override protected void send(ServerPlayer player,List<CustomPacketPayload> payloads){
            if(fail)throw new IllegalStateException("fixture sender failure");
            batches.add(List.copyOf(payloads)); if(duringSend!=null){var action=duringSend;duringSend=null;action.run();}
        }
    }
    private static void coalescing(GameTestHelper h){
        var first=tile(h,3);var second=tile(h,5);var player=player(h,first.getBlockPos());var queue=new Capture(player);
        var original=state(1,true);queue.setData(first,false,original,8);original.putInt("value",99);
        queue.setData(second,false,state(7,false),8);queue.flush(h.getLevel());
        h.assertTrue(queue.batches.size()==1&&queue.batches.getFirst().size()==2,"two updates in one native payload batch");
        h.assertTrue(patch(queue.batches.getFirst().getFirst()).getIntOr("value",0)==1,"queued tag detached from caller");
        h.assertTrue(queue.isEmpty(),"consumed updates leave an empty queue");
        queue.setData(first,false,state(2,true),8);queue.setData(first,false,state(2,false),8);
        var detached=queue.snapshot().getFirst().patch();detached.putInt("value",88);
        queue.flush(h.getLevel());var delta=patch(queue.batches.getLast().getFirst());
        h.assertTrue(delta.getIntOr("value",0)==2&&delta.contains(SyncPacket.REMOVED_KEYS),"coalesced change and deletion relative to consumed baseline");
        queue.setData(first,false,state(2,false),0);queue.flush(h.getLevel());
        h.assertTrue(queue.batches.size()==2,"unchanged state sends nothing");
        queue.setData(first,false,state(3,false),0);queue.flush(h.getLevel());
        h.assertTrue(queue.batches.size()==2,"radius excludes even the same-block offset player");h.succeed();
    }
    private static void retry(GameTestHelper h){
        var tile=tile(h,4);var queue=new Capture(player(h,tile.getBlockPos()));queue.setData(tile,false,state(1,true));queue.fail=true;
        try{queue.flush(h.getLevel());h.fail("sender failure lost");}catch(IllegalStateException expected){}
        h.assertTrue(!queue.isEmpty(),"failed send preserves pending state");queue.fail=false;queue.flush(h.getLevel());
        h.assertTrue(queue.isEmpty(),"successful retry consumes state");
        queue.setData(tile,false,state(2,false));queue.duringSend=()->queue.setData(tile,true,state(3,false));queue.flush(h.getLevel());
        h.assertTrue(!queue.isEmpty(),"reentrant enqueue survives flush");queue.flush(h.getLevel());
        h.assertTrue(patch(queue.batches.getLast().getFirst()).getIntOr("value",0)==3,"newer queued revision delivered");h.succeed();
    }
    private static void stale(GameTestHelper h){
        var tile=tile(h,4);var queue=new Capture(player(h,tile.getBlockPos()));queue.setData(tile,false,state(1,true));
        var other=new net.minecraft.world.level.block.entity.ChestBlockEntity(tile.getBlockPos(),Blocks.CHEST.defaultBlockState());
        other.setLevel(h.getLevel().getServer().getLevel(Level.NETHER));queue.setData(other,false,state(5,true));
        h.getLevel().setBlock(tile.getBlockPos(),Blocks.AIR.defaultBlockState(),3);
        h.getLevel().setBlock(tile.getBlockPos(),RotaryBlocks.DC_ENGINE.get().defaultBlockState(),3);queue.flush(h.getLevel());
        h.assertTrue(queue.batches.isEmpty()&&!queue.isEmpty(new WorldLocation(other)),"stale entity discarded without flushing another dimension");
        queue.forgetDimension(Level.NETHER);h.assertTrue(queue.isEmpty()&&queue.hasNoData(),"dimension cleanup clears baselines");h.succeed();
    }
    private static void codec(GameTestHelper h){
        var tile=tile(h,4);var input=CompoundSyncPacket.fromChanges(tile,state(8,true));
        FriendlyByteBuf buffer=new FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
        try{
            input.write(buffer);var decoded=new CompoundSyncPacket();decoded.readPacketData(buffer);
            CompoundTag tag=new CompoundTag();tag.putString("unsent","kept");decoded.readForSync(tile,tag);
            h.assertTrue(tag.getIntOr("value",0)==8&&tag.getStringOr("unsent","").equals("kept"),"snapshot merge and identity");
            buffer.clear();buffer.writeVarInt(99);
            try{decoded.readPacketData(buffer);h.fail("bad version accepted");}catch(IllegalArgumentException expected){}
            CompoundTag retained=new CompoundTag();decoded.readForSync(tile,retained);h.assertTrue(retained.getIntOr("value",0)==8,"failed decode is atomic");
            buffer.clear();buffer.writeVarInt(1);buffer.writeVarInt(1);buffer.writeResourceKey(Level.OVERWORLD);
            buffer.writeInt(10);buffer.writeInt(-50000);buffer.writeInt(20);buffer.writeIdentifier(net.minecraft.resources.Identifier.fromNamespaceAndPath("minecraft","chest"));buffer.writeNbt(state(9,false));
            var tall=new CompoundSyncPacket();tall.readPacketData(buffer);buffer.clear();tall.write(buffer);buffer.readVarInt();buffer.readVarInt();buffer.readResourceKey(net.minecraft.core.registries.Registries.DIMENSION);buffer.readInt();
            h.assertTrue(buffer.readInt()==-50000,"standalone snapshot retains full signed Y");
            buffer.clear();buffer.writeInt(17);int before=buffer.writerIndex();
            CompoundTag large=new CompoundTag();large.putByteArray("large",new byte[1_048_577]);
            try{CompoundSyncPacket.fromChanges(tile,large).write(buffer);h.fail("oversize snapshot accepted");}catch(IllegalArgumentException expected){}
            h.assertTrue(buffer.writerIndex()==before&&buffer.readInt()==17,"failed snapshot write preserves caller buffer");
        }finally{buffer.release();}h.succeed();
    }
    @SuppressWarnings("unchecked") private static void mode(GameTestHelper h){
        var tile=tile(h,4);var location=new WorldLocation(tile);
        try{
            var values=reika.dragonapi.instantiable.io.ControlledConfig.class.getDeclaredField("modernValues");values.setAccessible(true);
            var map=(Map<Integer,net.neoforged.neoforge.common.ModConfigSpec.ConfigValue<?>>)values.get(DragonAPI.config);
            var value=(net.neoforged.neoforge.common.ModConfigSpec.ConfigValue<Object>)map.get(DragonOptions.COMPOUNDSYNC.ordinal());var old=value.get();
            var method=BlockEntityBase.class.getDeclaredMethod("sendSyncPacket");method.setAccessible(true);
            try{
                value.set(true);method.invoke(tile);
                h.assertTrue(!CompoundSyncPacket.instance.isEmpty(location),"real block-entity periodic path queues compound updates");
                value.set(false);method.invoke(tile);h.assertTrue(CompoundSyncPacket.instance.isEmpty(location),"turning batching off cancels old queued updates");
                var field=BlockEntityBase.class.getDeclaredField("syncTag");field.setAccessible(true);
                h.assertTrue(!((SyncPacket)field.get(tile)).getChangesForSend().isEmpty(),"mode transition forces authoritative per-tile state");
            }finally{value.set(old);CompoundSyncPacket.instance.forget(location);}
        }catch(ReflectiveOperationException e){throw new IllegalStateException(e);}h.succeed();
    }
    private static void thread(GameTestHelper h){
        var tile=tile(h,4);var queue=new CompoundSyncPacket();
        var rejected=java.util.concurrent.CompletableFuture.supplyAsync(()->{
            try{queue.setData(tile,false,state(1,true));return false;}catch(IllegalStateException expected){return true;}
        });
        h.assertTrue(rejected.join()&&queue.isEmpty(),"off-thread world queue rejected without mutations");
        queue.primeBaseline(tile,state(1,true));queue.setData(tile,true,state(1,false));
        h.assertTrue(queue.snapshot().getFirst().patch().contains(SyncPacket.REMOVED_KEYS),"transition baseline preserves deletion before first batch");queue.clear();h.succeed();
    }
}
