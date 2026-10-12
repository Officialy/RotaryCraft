package reika.rotarycraft;

import java.io.*;
import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.APIPacketHandler;
import reika.dragonapi.APIPacketHandler.PacketIDs;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.auxiliary.PacketTypes;
import reika.dragonapi.command.BiomeMapCommand;
import reika.dragonapi.libraries.ReikaEntityHelper;
import reika.dragonapi.libraries.io.CustomNetworkBridge.DataPacketPayload;
import reika.dragonapi.libraries.io.DimensionPacketData;
import reika.dragonapi.libraries.io.PacketPipeline;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.dragonapi.libraries.io.ReikaPacketHelper.DataPacket;
import reika.dragonapi.libraries.io.ReikaPacketHelper.PacketObj;
import reika.dragonapi.libraries.level.ReikaWorldHelper;

/** Native worlds, entities and production bridge frames; captured sends are not remote sockets. */
final class DragonAPIDimensionPacketTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        RotaryGameTests.register(event, env, "dragonapi_dimension_entity_verification", 40, DragonAPIDimensionPacketTests::entities);
        RotaryGameTests.register(event, env, "dragonapi_dimension_biome_stream", 40, DragonAPIDimensionPacketTests::stream);
        RotaryGameTests.register(event, env, "dragonapi_dimension_biome_palettes", 40, DragonAPIDimensionPacketTests::palettes);
        RotaryGameTests.register(event, env, "dragonapi_dimension_map_sessions", 40, DragonAPIDimensionPacketTests::sessions);
    }
    private static final class Capture extends PacketPipeline {
        final List<DimensionPacketData> sent = new ArrayList<>();
        Capture() { super(RotaryCraft.getInstance(), "dimension-audit", new APIPacketHandler()); }
        @Override public void sendToPlayer(PacketObj packet, ServerPlayer player) {
            var payload = (DataPacketPayload)((ClientboundCustomPayloadPacket)getMinecraftPacket(packet, PacketFlow.CLIENTBOUND)).payload();
            var bytes = new FriendlyByteBuf(io.netty.buffer.Unpooled.wrappedBuffer(payload.data()));
            try {
                var decoded = DataPacket.decode(bytes, getHandler());
                if (decoded.getType() != PacketTypes.RAW) throw new AssertionError("Dimension schema is RAW");
                var input = decoded.getDataIn();
                sent.add(DimensionPacketData.read(PacketIDs.getEnum(input.readInt()), input));
            } catch (IOException error) { throw new IllegalStateException(error); } finally { bytes.release(); }
        }
    }
    @SuppressWarnings("unchecked") private static void withPipe(Capture pipe, Runnable action) {
        try {
            var field = ReikaPacketHelper.class.getDeclaredField("pipelines"); field.setAccessible(true);
            var map = (Map<String, PacketPipeline>)field.get(null); var old = map.put(DragonAPI.packetChannel, pipe);
            try { action.run(); } finally { map.put(DragonAPI.packetChannel, old); }
        } catch (ReflectiveOperationException error) { throw new IllegalStateException(error); }
    }
    private static void deliver(Capture pipe, GameTestHelper h, ServerPlayer player, byte[] bytes, PacketTypes type) {
        var packet = new DataPacket(bytes); packet.init(type, pipe);
        pipe.getHandler().handleData(packet, h.getLevel(), player);
    }
    private static void entities(GameTestHelper h) {
        var player = DragonAPIAuditTests.player(h); var pipe = new Capture();
        int hash = ReikaEntityHelper.verificationClassHash(player);
        withPipe(pipe, () -> {
            deliver(pipe,h,player,new DimensionPacketData(PacketIDs.ENTITYVERIFY,h.getLevel().dimension(),"",new int[]{player.getId(),hash}).encode(),PacketTypes.RAW);
            h.assertTrue(pipe.sent.isEmpty(), "existing entity and matching class are verified");
            deliver(pipe,h,player,new DimensionPacketData(PacketIDs.ENTITYVERIFY,Level.NETHER,"",new int[]{player.getId(),hash+1}).encode(),PacketTypes.RAW);
            h.assertTrue(pipe.sent.isEmpty(), "different-dimension request is ignored");
            var request = new DimensionPacketData(PacketIDs.ENTITYVERIFY,h.getLevel().dimension(),"",new int[]{player.getId(),hash+1});
            deliver(pipe,h,player,request.encode(),PacketTypes.RAW);
            h.assertTrue(pipe.sent.size()==1 && pipe.sent.getFirst().control()==PacketIDs.ENTITYVERIFYFAIL
                    && pipe.sent.getFirst().dimension().equals(h.getLevel().dimension()) && pipe.sent.getFirst().values()[1]==hash+1,
                    "class mismatch reply retains dimension and requested class");
            pipe.sent.clear();
            deliver(pipe,h,player,new DimensionPacketData(PacketIDs.ENTITYVERIFY,h.getLevel().dimension(),"",new int[]{Integer.MAX_VALUE,hash}).encode(),PacketTypes.RAW);
            h.assertTrue(pipe.sent.size()==1,"missing entity receives one failure reply"); pipe.sent.clear();
            for (byte[] bad : List.of(Arrays.copyOf(request.encode(),request.encode().length-1),Arrays.copyOf(request.encode(),request.encode().length+1),new byte[]{0,0,0,42,0,0,0,1,0,0,0,0,0,0,0,2}))
                deliver(pipe,h,player,bad,PacketTypes.RAW);
            deliver(pipe,h,player,request.encode(),PacketTypes.DATA);
            deliver(pipe,h,player,new DimensionPacketData(PacketIDs.ENTITYVERIFYFAIL,h.getLevel().dimension(),"",new int[]{player.getId(),hash}).encode(),PacketTypes.RAW);
            h.assertTrue(pipe.sent.isEmpty() && !player.isRemoved(),"bad schemas and wrong directions cannot reply or remove server entities");
            h.assertTrue(!ReikaEntityHelper.applyEntityVerificationFailure(h.getLevel(),h.getLevel().dimension(),player.getId(),hash),"public removal helper also rejects server levels");
        }); h.succeed();
    }
    private static void stream(GameTestHelper h) {
        var pipe = new Capture();
        var profile = new com.mojang.authlib.GameProfile(UUID.randomUUID(), "biome-nether-audit");
        var cookie = net.minecraft.server.network.CommonListenerCookie.createInitial(profile, false);
        var player = new ServerPlayer(h.getLevel().getServer(), h.getLevel().getServer().getLevel(Level.NETHER), profile, cookie.clientInformation());
        withPipe(pipe, () -> BiomeMapCommand.triggerBiomeMap(player,0,0,1,1,0,
                () -> player.setServerLevel(h.getLevel())));
        h.assertTrue(pipe.sent.size()==3,"small map has start, padded points and end");
        h.assertTrue(pipe.sent.getFirst().control()==PacketIDs.BIOMEPNGSTART && pipe.sent.getLast().control()==PacketIDs.BIOMEPNGEND,"map stream order retained");
        int hash = pipe.sent.getFirst().values()[0];
        for(var packet:pipe.sent)h.assertTrue(packet.dimension().equals(Level.NETHER) && packet.values()[0]==hash,"every frame retains source dimension/session");
        deliver(pipe,h,player,pipe.sent.getFirst().encode(),PacketTypes.RAW);
        h.assertTrue(maps().isEmpty(),"server rejects clientbound map start"); h.succeed();
    }
    private static void palettes(GameTestHelper h) {
        var level=h.getLevel(); var pos=h.absolutePos(new BlockPos(4,1,4)); var chunk=level.getChunkAt(pos);
        var sections=chunk.getSections(); var originals=Arrays.stream(sections).map(s->s.getBiomes().copy()).toList();
        try {
            h.assertTrue(ReikaWorldHelper.applyBiomeColumn(level,pos.getX(),pos.getZ(),Biomes.DESERT.identifier()),"loaded registered biome applies");
            for(var section:sections) {
                h.assertTrue(section.getNoiseBiome((pos.getX()>>2)&3,0,(pos.getZ()>>2)&3).is(Biomes.DESERT),"every section including first/last updates");
                h.assertTrue(section.getNoiseBiome(((pos.getX()>>2)+1)&3,0,(pos.getZ()>>2)&3).equals(originals.get(Arrays.asList(sections).indexOf(section)).get(((pos.getX()>>2)+1)&3,0,(pos.getZ()>>2)&3)),"neighbor quart is preserved");
            }
            h.assertTrue(!ReikaWorldHelper.applyBiomeColumn(level,pos.getX(),pos.getZ(),net.minecraft.resources.Identifier.parse("dragonapi:missing")),"unknown biome rejects");
            h.assertTrue(!ReikaWorldHelper.applyBiomeColumn(level,30000000,30000000,Biomes.DESERT.identifier()),"unloaded target is not generated");
        } finally {
            for(int i=0;i<sections.length;i++) {
                var before=originals.get(i);
                sections[i].fillBiomesFromNoise((x,y,z)->before.get(x&3,y&3,z&3),(pos.getX()>>4)<<2,(level.getMinSectionY()+i)<<2,(pos.getZ()>>4)<<2);
            }
        } h.succeed();
    }
    @SuppressWarnings("unchecked") private static Map<Integer,Object> maps() {
        try {var field=BiomeMapCommand.class.getDeclaredField("activeMaps");field.setAccessible(true);return (Map<Integer,Object>)field.get(null);}
        catch(ReflectiveOperationException error){throw new IllegalStateException(error);}
    }
    private static void sessions(GameTestHelper h) {
        var level=h.getLevel(); BiomeMapCommand.clearClientMaps();
        try {
            BiomeMapCommand.startCollecting(level,71,"audit",Level.NETHER,0,0,1,1,0,false);
            h.assertTrue(maps().isEmpty(),"wrong source dimension cannot start a map");
            BiomeMapCommand.startCollecting(level,71,"audit",level.dimension(),0,0,1,1,0,false);
            int biome=level.registryAccess().lookupOrThrow(Registries.BIOME).getId(level.registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.DESERT).value());
            BiomeMapCommand.addBiomePoint(level,71,0,0,biome);
            h.assertTrue(maps().size()==1,"same-level point is accepted");
            var other=level.getServer().getLevel(Level.NETHER);
            BiomeMapCommand.addBiomePoint(other,71,0,0,biome);
            h.assertTrue(maps().isEmpty(),"delayed points discard a session after level change");
            BiomeMapCommand.startCollecting(level,72,"audit",level.dimension(),0,0,1,1,0,false);
            BiomeMapCommand.finishCollectingAndMakeImage(other,72);
            h.assertTrue(maps().isEmpty(),"wrong-level end drops session without exporting");
        } finally {BiomeMapCommand.clearClientMaps();} h.succeed();
    }
}
