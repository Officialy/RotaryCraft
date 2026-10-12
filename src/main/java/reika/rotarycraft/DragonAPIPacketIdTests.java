package reika.rotarycraft;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HexFormat;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.APIPacketHandler.PacketIDs;
import reika.dragonapi.auxiliary.PacketTypes;
import reika.dragonapi.interfaces.PacketId;
import reika.dragonapi.libraries.io.ReikaPacketHelper.DataPacket;
import reika.rotarycraft.registry.PacketRegistry;

/** Published identity goldens and real native frame/decoder checks; no packet transport is re-registered. */
final class DragonAPIPacketIdTests {
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        if (net.neoforged.fml.ModList.get().isLoaded("reactorcraft")
                && net.neoforged.fml.ModList.get().isLoaded("electricraft")
                && net.neoforged.fml.ModList.get().isLoaded("chromaticraft"))
            RotaryGameTests.register(event,env,"dragonapi_packet_ids_published_tables",40,DragonAPIPacketIdTests::tables);
        RotaryGameTests.register(event,env,"dragonapi_packet_ids_frames_and_rejection",40,DragonAPIPacketIdTests::frames);
    }
    private static String fingerprint(String name) throws Exception {
        Object[] values=Class.forName(name).getEnumConstants();
        String canonical=Arrays.stream(values).map(value -> ((Enum<?>)value).name()+"="+((PacketId)value).wireId()+"\n")
                .sorted().collect(java.util.stream.Collectors.joining());
        return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(canonical.getBytes(StandardCharsets.UTF_8)));
    }
    private static void tables(GameTestHelper h) {
        try {
            h.assertTrue(fingerprint("reika.dragonapi.auxiliary.PacketTypes").equals("c98875ca4d11a214b2cd4a7c596441adc7e1557ff5e4ed0f27e3e6e18918c138"),"published ID table: reika.dragonapi.auxiliary.PacketTypes");
            h.assertTrue(fingerprint("reika.dragonapi.APIPacketHandler$PacketIDs").equals("f60db17f2344fa9cebd16cb6d27af212ea8a1f680abea0892ad76fece96566e6"),"published ID table: reika.dragonapi.APIPacketHandler$PacketIDs");
            h.assertTrue(fingerprint("reika.dragonapi.libraries.io.ReikaPacketHelper$PacketableData").equals("76428ab0ea5d5f25ab260d3d3537c6cba5ee9cc61f84bfa88cbe8a2a1b90d730"),"published ID table: reika.dragonapi.libraries.io.ReikaPacketHelper$PacketableData");
            h.assertTrue(fingerprint("reika.rotarycraft.registry.PacketRegistry").equals("0659acd7414f87daa699a2cd03fe86540ef54b36604c62a7a6c23216bc05855d"),"published ID table: reika.rotarycraft.registry.PacketRegistry");
            h.assertTrue(fingerprint("reika.reactorcraft.registry.ReactorPackets").equals("23abfdc785333094020c5dd5e1a22c1206b5127294e7c565a1cd4e49b963d374"),"published ID table: reika.reactorcraft.registry.ReactorPackets");
            h.assertTrue(fingerprint("reika.electricraft.registry.ElectriPackets").equals("cbe8bd2319ec316710ae86d4dc3a8a570e8d1ecb20e825aff266a74f74a7d25c"),"published ID table: reika.electricraft.registry.ElectriPackets");
            h.assertTrue(fingerprint("reika.chromaticraft.registry.ChromaPackets").equals("2f7c9540ceb46a27bc394e29a4f85be54df43636468d1453f9eb774861af0f1e"),"published ID table: reika.chromaticraft.registry.ChromaPackets");
            for(var type:PacketTypes.values())h.assertTrue(PacketTypes.getPacketType(type.wireId())==type,"category lookup uses explicit ID");
            for(var id:PacketIDs.values())h.assertTrue(PacketIDs.getEnum(id.wireId())==id,"DragonAPI control lookup");
            for(var id:PacketRegistry.values())h.assertTrue(PacketRegistry.getEnum(id.wireId())==id,"Rotary control lookup");
            verifyLookup(h,"reika.reactorcraft.registry.ReactorPackets","getEnum");
            verifyLookup(h,"reika.electricraft.registry.ElectriPackets","getEnum");
            verifyLookup(h,"reika.chromaticraft.registry.ChromaPackets","getPacket");
        }catch(Exception error){throw new IllegalStateException(error);}h.succeed();
    }
    private static void verifyLookup(GameTestHelper h,String name,String method) throws Exception {
        Class<?> type=Class.forName(name);var lookup=type.getMethod(method,int.class);
        for(Object id:type.getEnumConstants())
            h.assertTrue(lookup.invoke(null,((PacketId)id).wireId())==id,"explicit ID lookup: "+name);
        for(int id:new int[]{-1,Integer.MAX_VALUE}) {
            try{lookup.invoke(null,id);h.fail("unknown ID accepted: "+name);}
            catch(java.lang.reflect.InvocationTargetException error) {
                if(!(error.getCause() instanceof IllegalArgumentException))throw error;
            }
        }
    }
    private static void frames(GameTestHelper h) {
        h.assertTrue(reika.dragonapi.libraries.io.CustomNetworkBridge.PROTOCOL_VERSION.equals("2.1.0"),"byte-identical migration retains the negotiated protocol version");
        FriendlyByteBuf bytes=new FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
        var handler=new reika.dragonapi.APIPacketHandler();
        var pipe=new reika.dragonapi.libraries.io.PacketPipeline(RotaryCraft.getInstance(),"stable-id-audit",handler);
        try {
            for(var type:PacketTypes.values()) {
                bytes.clear();var packet=new DataPacket(new byte[0]);packet.init(type,pipe);packet.encode(bytes);
                h.assertTrue(bytes.getUnsignedByte(0)==type.wireId(),"native category encoder uses explicit ID");
                h.assertTrue(DataPacket.decode(bytes,handler).getType()==type,"native category decoder round trip");
            }
            bytes.clear();bytes.writeBytes(new byte[]{7,4,12,34,56,78});
            var old=DataPacket.decode(bytes,handler);h.assertTrue(old.getType()==PacketTypes.RAW&&old.getDataIn().readInt()==0x0C22384E,"independent protocol-2.1 golden frame remains readable");
            bytes.clear();bytes.writeByte(255);bytes.writeVarInt(0);
            try{DataPacket.decode(bytes,handler);h.fail("unknown category accepted");}catch(IllegalArgumentException expected){}
            for(int id:new int[]{-1,Integer.MAX_VALUE}) {
                try{PacketRegistry.getEnum(id);h.fail("invalid Rotary ID accepted");}catch(IllegalArgumentException expected){}
            }
        }catch(java.io.IOException error){throw new IllegalStateException(error);}finally{bytes.release();}h.succeed();
    }
}
