package reika.rotarycraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import reika.dragonapi.APIPacketHandler;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.auxiliary.PacketTypes;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.io.PacketTarget;
import reika.dragonapi.libraries.io.CustomNetworkBridge.DataPacketPayload;
import reika.dragonapi.libraries.io.PacketPipeline;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.dragonapi.libraries.io.ReikaPacketHelper.DataPacket;
import reika.dragonapi.libraries.io.ReikaPacketHelper.PacketObj;
import reika.dragonapi.libraries.io.TankSyncData;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.gui.container.machine.ReservoirContainer;
import reika.rotarycraft.registry.RotaryBlocks;

/** Real levels/menus/fluids and native bridge frames; transport calls are captured, not remote sockets. */
final class DragonAPITankSyncTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        RotaryGameTests.register(event,env,"dragonapi_tank_recipients",40,DragonAPITankSyncTests::recipients);
        RotaryGameTests.register(event,env,"dragonapi_tank_menu_integrated",40,DragonAPITankSyncTests::menu);
        RotaryGameTests.register(event,env,"dragonapi_tank_base_refresh",40,DragonAPITankSyncTests::base);
        RotaryGameTests.register(event,env,"dragonapi_tank_fluid_state",40,DragonAPITankSyncTests::fluids);
        RotaryGameTests.register(event,env,"dragonapi_tank_direction_thread",40,DragonAPITankSyncTests::guards);
        RotaryGameTests.register(event,env,"reservoir_render_color_components",40,DragonAPITankSyncTests::colorComponents);
        RotaryGameTests.register(event,env,"reservoir_render_color_world",40,DragonAPITankSyncTests::colorWorld);
    }
    private static BlockEntityReservoir tile(GameTestHelper h) {
        BlockPos pos=h.absolutePos(new BlockPos(4,1,4));h.getLevel().setBlock(pos,RotaryBlocks.RESERVOIR.get().defaultBlockState(),3);
        var tile=(BlockEntityReservoir)h.getLevel().getBlockEntity(pos);TankSyncData.tank(tile,"tank").setContents(1000,Fluids.WATER);return tile;
    }
    private static ServerPlayer player(GameTestHelper h,BlockPos pos) {
        var player=DragonAPIAuditTests.player(h);player.setPos(pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5);return player;
    }
    private record Sent(UUID player,TankSyncData data) {}
    private static final class Capture extends PacketPipeline {
        final List<Sent> sent=new ArrayList<>(); ResourceKey<Level> dimension; double radius,x,y,z;
        Capture(String channel){super(RotaryCraft.getInstance(),channel,new APIPacketHandler());}
        @Override public void sendToPlayer(PacketObj packet,ServerPlayer player) {
            var payload=(DataPacketPayload)((ClientboundCustomPayloadPacket)getMinecraftPacket(packet,PacketFlow.CLIENTBOUND)).payload();
            FriendlyByteBuf buf=new FriendlyByteBuf(io.netty.buffer.Unpooled.wrappedBuffer(payload.data()));
            try {
                var decoded=DataPacket.decode(buf,getHandler());
                if(decoded.getType()==PacketTypes.TANK)sent.add(new Sent(player.getUUID(),TankSyncData.read(decoded.getDataIn())));
            }catch(java.io.IOException error){throw new IllegalStateException(error);}finally{buf.release();}
        }
        @Override public void sendToAllAround(PacketObj packet,ResourceKey<Level> dim,double x,double y,double z,double radius) {
            this.dimension=dim;this.radius=radius;this.x=x;this.y=y;this.z=z;
            var level=net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer().getLevel(dim);
            for(var player:level.players())if(player.distanceToSqr(x,y,z)<=radius*radius)sendToPlayer(packet,player);
        }
    }
    @SuppressWarnings("unchecked") private static void withPipe(String channel,Capture capture,Runnable action) {
        try {
            var field=ReikaPacketHelper.class.getDeclaredField("pipelines");field.setAccessible(true);
            var map=(Map<String,PacketPipeline>)field.get(null);var previous=map.put(channel,capture);
            try{action.run();}finally{if(previous==null)map.remove(channel);else map.put(channel,previous);}
        }catch(ReflectiveOperationException error){throw new IllegalStateException(error);}
    }
    private static void recipients(GameTestHelper h) {
        var tile=tile(h);var near=player(h,tile.getBlockPos());var far=player(h,tile.getBlockPos().east(30));
        String channel="tank-recipients-audit";var capture=new Capture(channel);
        withPipe(channel,capture,()->{
            ReikaPacketHelper.sendTankSyncPacket(channel,tile,"tank");
            h.assertTrue(capture.radius==24&&capture.dimension.equals(h.getLevel().dimension())&&capture.x==tile.getBlockPos().getX()+0.5,"original 24-block same-dimension center retained");
            h.assertTrue(capture.sent.stream().anyMatch(s->s.player().equals(near.getUUID()))&&capture.sent.stream().noneMatch(s->s.player().equals(far.getUUID())),"near player selected, distant player excluded");
            var detached=capture.sent.getFirst().data();TankSyncData.tank(tile,"tank").empty();
            h.assertTrue(detached.amount()==1000&&detached.fluid().equals("minecraft:water"),"encoded snapshot detached from subsequent tank changes");
            capture.sent.clear();ReikaPacketHelper.sendTankSyncPacket(channel,tile,"tank",new PacketTarget.PlayerTarget(far));
            h.assertTrue(capture.sent.size()==1&&capture.sent.getFirst().player().equals(far.getUUID())&&capture.sent.getFirst().data().amount()==0,"explicit recipient and empty state retained");
        });h.succeed();
    }
    private static void menu(GameTestHelper h) {
        var tile=tile(h);var owner=player(h,tile.getBlockPos());var observer=player(h,tile.getBlockPos().east(1));
        var capture=new Capture(RotaryCraft.packetChannel);
        withPipe(RotaryCraft.packetChannel,capture,()->new ReservoirContainer(7,owner.getInventory(),tile).broadcastChanges());
        h.assertTrue(capture.sent.size()==1&&capture.sent.getFirst().player().equals(owner.getUUID()),"real reservoir menu updates its viewer on the logical server, not a nearby observer");
        h.assertTrue(capture.sent.getFirst().data().amount()==1000,"menu frame carries the live tank level");h.succeed();
    }
    private static void base(GameTestHelper h) {
        var tile=tile(h);var player=player(h,tile.getBlockPos());var capture=new Capture(DragonAPI.packetChannel);
        withPipe(DragonAPI.packetChannel,capture,tile::syncTankData);
        h.assertTrue(capture.sent.stream().anyMatch(s->s.player().equals(player.getUUID())&&s.data().field().equals("tank")&&s.data().amount()==1000),"real explicit base refresh sends discovered private tank fields");h.succeed();
    }
    private static void fluids(GameTestHelper h) {
        var tank=new HybridTank("audit",1000);TankSyncData.apply(tank,800,"minecraft:water");
        h.assertTrue(tank.getFluidAmount()==800&&tank.getFluid().getFluid()==Fluids.WATER,"empty tank filled with named fluid");
        TankSyncData.apply(tank,2000,"minecraft:lava");h.assertTrue(tank.getFluidAmount()==1000&&tank.getFluid().getFluid()==Fluids.LAVA,"switch and capacity clamp");
        for(String name:List.of("missing:fluid","invalid fluid id","minecraft:empty")) {
            try{TankSyncData.apply(tank,200,name);h.fail("invalid named fluid accepted");}catch(IllegalArgumentException expected){}
            h.assertTrue(tank.getFluidAmount()==1000&&tank.getFluid().getFluid()==Fluids.LAVA,"invalid id preserves prior fluid and amount");
        }
        CompoundTag data=new CompoundTag();data.putString("source","component-aware");
        var stack=new FluidStack(Fluids.WATER,400);stack.set(DataComponents.CUSTOM_DATA,CustomData.of(data));tank.setFluid(stack);
        TankSyncData.apply(tank,900,"minecraft:water");TankSyncData.apply(tank,200,"");
        h.assertTrue(tank.getFluidAmount()==200&&CustomData.of(data).equals(tank.getFluid().get(DataComponents.CUSTOM_DATA)),"same-fluid and legacy level-only updates preserve existing components");
        TankSyncData.apply(tank,0,"");h.assertTrue(tank.isEmpty(),"empty update clears tank");
        try{TankSyncData.apply(tank,200,"");h.fail("level-only update invented fluid");}catch(IllegalArgumentException expected){}
        h.succeed();
    }
    private static void colorComponents(GameTestHelper h) {
        var detached=new BlockEntityReservoir(BlockPos.ZERO,RotaryBlocks.RESERVOIR.get().defaultBlockState());
        var data=new CompoundTag(); data.putInt("renderColor",0x123456);
        var fluid=new FluidStack(Fluids.WATER,400); fluid.set(DataComponents.CUSTOM_DATA,CustomData.of(data));
        TankSyncData.tank(detached,"tank").setFluid(fluid);
        h.assertTrue(detached.getFluidRenderColor(0xabcdef)==0x123456,"component color must survive outside world rendering");
        TankSyncData.tank(detached,"tank").setFluid(new FluidStack(Fluids.WATER,400));
        h.assertTrue(detached.getFluidRenderColor(0xabcdef)==0xabcdef,"registered model tint must replace invented fallback colors");
        detached.setEmpty();
        h.assertTrue(detached.getFluidRenderColor(0xabcdef)==0xffffff,"empty fluid retains original white color");
        h.succeed();
    }
    private static void colorWorld(GameTestHelper h) {
        var reservoir=tile(h); var data=new CompoundTag(); data.putInt("renderColor",0x123456);
        var fluid=new FluidStack(Fluids.WATER,400); fluid.set(DataComponents.CUSTOM_DATA,CustomData.of(data));
        TankSyncData.tank(reservoir,"tank").setFluid(fluid);
        h.assertTrue(reservoir.getFluidRenderColor(0xabcdef)==0xabcdef,"in-world placeable fluid uses supplied native biome tint before component override");
        h.assertTrue(CustomData.of(data).equals(reservoir.getFluid().get(DataComponents.CUSTOM_DATA)),"reading visual color must not consume component data");
        h.succeed();
    }
    private static void guards(GameTestHelper h) {
        var tile=tile(h);var player=player(h,tile.getBlockPos());String channel="tank-guards-audit";var capture=new Capture(channel);
        withPipe(channel,capture,()->{
            boolean rejected=java.util.concurrent.CompletableFuture.supplyAsync(()->{
                try{ReikaPacketHelper.sendTankSyncPacket(channel,tile,"tank");return false;}catch(IllegalStateException expected){return true;}
            }).join();h.assertTrue(rejected&&capture.sent.isEmpty(),"off-thread tank access rejected before snapshot/send");
            ReikaPacketHelper.updateBlockEntityTankData(h.getLevel(),tile.getBlockPos().getX(),tile.getBlockPos().getY(),tile.getBlockPos().getZ(),"tank",0,"");
            var packet=new DataPacket(new TankSyncData("tank",tile.getBlockPos(),0,"").encode());packet.init(PacketTypes.TANK,capture);
            new APIPacketHandler().handleData(packet,h.getLevel(),player);
            h.assertTrue(TankSyncData.tank(tile,"tank").getFluidAmount()==1000,"receiver entry points cannot mutate a logical server tank");
            try{ReikaPacketHelper.sendTankSyncPacket(channel,tile,"tank",PacketTarget.server);h.fail("serverbound TANK allowed");}catch(IllegalArgumentException expected){}
            try{ReikaPacketHelper.sendTankSyncPacket(channel,tile,"missing");h.fail("missing tank field encoded");}catch(IllegalArgumentException expected){}
            h.getLevel().setBlock(tile.getBlockPos(),net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(),3);
            ReikaPacketHelper.sendTankSyncPacket(channel,tile,"tank");h.assertTrue(capture.sent.isEmpty(),"removed entity cannot send stale tank state");
        });h.succeed();
    }
}
