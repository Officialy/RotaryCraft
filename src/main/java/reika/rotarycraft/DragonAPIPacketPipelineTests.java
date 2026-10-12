package reika.rotarycraft;

import java.io.DataInputStream;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import reika.dragonapi.auxiliary.PacketTypes;
import reika.dragonapi.interfaces.PacketHandler;
import reika.dragonapi.libraries.io.CustomNetworkBridge;
import reika.dragonapi.libraries.io.CustomNetworkBridge.DataPacketPayload;
import reika.dragonapi.libraries.io.PacketPipeline;
import reika.dragonapi.libraries.io.ReikaPacketHelper.DataPacket;
import reika.dragonapi.libraries.io.ReikaPacketHelper.PacketObj;

/** Native levels/players and the production decode-dispatch path, with captured connection replies. */
final class DragonAPIPacketPipelineTests {
    private static PacketPipeline registeredPipe;
    private static final Identifier REGISTERED_COUNTER = Identifier.fromNamespaceAndPath("rotarycraft","pipeline_registered_audit/counter");

    @EventBusSubscriber(modid=RotaryCraft.MODID)
    public static final class RegistrationHooks {
        @SubscribeEvent
        public static void registerPayloads(RegisterPayloadHandlersEvent event) {
            if (!java.util.Arrays.asList(System.getProperty("neoforge.enabledGameTestNamespaces","").split(",")).contains(RotaryCraft.MODID)) return;
            registeredPipe = new PacketPipeline(RotaryCraft.getInstance(),"pipeline_registered_audit",(p,w,e) -> { throw new AssertionError("Codec-registration fixture must not dispatch gameplay"); });
            registeredPipe.registerPacket(REGISTERED_COUNTER,CounterPacket.class,(packet,buf) -> buf.writeInt(packet.value),buf -> new CounterPacket(buf.readInt()));
            bridge(registeredPipe).registerAll(event);
        }
    }

    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        RotaryGameTests.register(event,env,"dragonapi_pipeline_reply",40,DragonAPIPacketPipelineTests::reply);
        RotaryGameTests.register(event,env,"dragonapi_pipeline_dispatch_failure",40,DragonAPIPacketPipelineTests::failure);
        RotaryGameTests.register(event,env,"dragonapi_pipeline_custom_codec",40,DragonAPIPacketPipelineTests::custom);
        RotaryGameTests.register(event,env,"dragonapi_pipeline_native_registration",40,DragonAPIPacketPipelineTests::registration);
    }

    private static ServerPlayer player(GameTestHelper h) {
        var profile = new com.mojang.authlib.GameProfile(java.util.UUID.randomUUID(),"pipeline-audit");
        var cookie = net.minecraft.server.network.CommonListenerCookie.createInitial(profile,false);
        return new ServerPlayer(h.getLevel().getServer(),h.getLevel(),profile,cookie.clientInformation());
    }
    private static IPayloadContext context(ServerPlayer player, List<CustomPacketPayload> replies) {
        return (IPayloadContext)Proxy.newProxyInstance(IPayloadContext.class.getClassLoader(),new Class[]{IPayloadContext.class},
                (proxy,method,args) -> switch(method.getName()) {
                    case "flow" -> PacketFlow.SERVERBOUND;
                    case "player" -> player;
                    case "reply" -> { replies.add((CustomPacketPayload)args[0]); yield null; }
                    case "enqueueWork" -> { ((Runnable)args[0]).run(); yield CompletableFuture.completedFuture(null); }
                    default -> throw new AssertionError("Unexpected context access: "+method.getName());
                });
    }
    private static DataPacket packet(PacketPipeline pipe, PacketTypes type, int value) {
        var packet = new DataPacket(new byte[]{0,0,0,(byte)value}); packet.init(type,pipe); return packet;
    }
    private static CustomNetworkBridge bridge(PacketPipeline pipe) {
        try {
            var field=PacketPipeline.class.getDeclaredField("bridge"); field.setAccessible(true);
            return (CustomNetworkBridge)field.get(pipe);
        } catch (ReflectiveOperationException e) { throw new IllegalStateException(e); }
    }
    private static DataPacketPayload payload(PacketPipeline pipe, PacketObj packet) {
        return (DataPacketPayload)((ClientboundCustomPayloadPacket)pipe.getMinecraftPacket(packet,PacketFlow.CLIENTBOUND)).payload();
    }
    private static void deliver(PacketPipeline pipe, DataPacketPayload payload, IPayloadContext context) {
        try {
            var method=CustomNetworkBridge.class.getDeclaredMethod("handleIncoming",DataPacketPayload.class,IPayloadContext.class);
            method.setAccessible(true); method.invoke(bridge(pipe),payload,context);
        } catch (ReflectiveOperationException e) { throw new IllegalStateException(e); }
    }
    private static void reply(GameTestHelper h) {
        var player=player(h); var replies=new ArrayList<CustomPacketPayload>();
        AtomicReference<PacketPipeline> reference=new AtomicReference<>(); AtomicInteger handled=new AtomicInteger();
        PacketHandler handler=(packet,world,sender) -> {
            h.assertTrue(world == h.getLevel() && sender == player && packet.getHandler() == reference.get().getHandler(),"native sender and channel binding");
            handled.incrementAndGet();
            reference.get().replyToPacket(packet(reference.get(),PacketTypes.RAW,42));
            h.assertTrue(reference.get().getMinecraftPacket(packet) instanceof ClientboundCustomPayloadPacket,"server receive context infers clientbound reply");
        };
        var pipe=new PacketPipeline(RotaryCraft.getInstance(),"pipeline_reply_audit",handler); reference.set(pipe);
        deliver(pipe,payload(pipe,packet(pipe,PacketTypes.RAW,41)),context(player,replies));
        h.assertTrue(handled.get()==1 && replies.size()==1,"one receive and one reply to captured connection");
        var received=(DataPacketPayload)replies.getFirst();
        FriendlyByteBuf buffer=new FriendlyByteBuf(io.netty.buffer.Unpooled.wrappedBuffer(received.data()));
        try { h.assertTrue(DataPacket.decode(buffer,handler).getDataIn().readInt()==42,"reply retains native bridge bytes"); }
        catch(java.io.IOException e) { throw new IllegalStateException(e); } finally { buffer.release(); }
        try { pipe.replyToPacket(packet(pipe,PacketTypes.RAW,1)); h.fail("reply context leaked out of dispatch"); }
        catch(IllegalStateException expected) { }
        h.succeed();
    }
    private static void failure(GameTestHelper h) {
        AtomicInteger handled=new AtomicInteger(); var replies=new ArrayList<CustomPacketPayload>();
        var pipe=new PacketPipeline(RotaryCraft.getInstance(),"pipeline_failure_audit",(p,w,e) -> { handled.incrementAndGet(); throw new IllegalArgumentException("fixture handler failure"); });
        var context=context(player(h),replies);
        deliver(pipe,payload(pipe,packet(pipe,PacketTypes.RAW,1)),context);
        h.assertTrue(handled.get()==1 && replies.isEmpty(),"failed handler is contained without invented replies");
        try { pipe.replyToPacket(packet(pipe,PacketTypes.RAW,1)); h.fail("failed dispatch leaked reply context"); }
        catch(IllegalStateException expected) { }
        deliver(pipe,payload(pipe,packet(pipe,PacketTypes.SOUND,1)),context);
        deliver(pipe,new DataPacketPayload(new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath("rotarycraft","foreign")),new byte[]{0}),context);
        h.assertTrue(handled.get()==1,"client-only and foreign payloads never reach server handler"); h.succeed();
    }
    private static void custom(GameTestHelper h) {
        AtomicReference<PacketPipeline> reference=new AtomicReference<>(); AtomicInteger handled=new AtomicInteger();
        var replies=new ArrayList<CustomPacketPayload>();
        var pipe=new PacketPipeline(RotaryCraft.getInstance(),"pipeline_codec_audit",(packet,world,player) -> {
            h.assertTrue(packet instanceof CounterPacket && ((CounterPacket)packet).value==73 && packet.getHandler()==reference.get().getHandler(),"registered decoder and authoritative handler used");
            handled.incrementAndGet();
            var response=new CounterPacket(74); response.init(PacketTypes.RAW,reference.get()); reference.get().replyToPacket(response);
        }); reference.set(pipe);
        Identifier id=Identifier.fromNamespaceAndPath("rotarycraft","pipeline_codec_audit/counter");
        pipe.registerPacket(id,CounterPacket.class,(packet,buf) -> buf.writeInt(packet.value),buf -> new CounterPacket(buf.readInt()));
        var input=new CounterPacket(73); input.init(PacketTypes.RAW,pipe);
        var payload=payload(pipe,input); h.assertTrue(payload.type().id().equals(id),"registered native payload identity");
        deliver(pipe,payload,context(player(h),replies));
        h.assertTrue(handled.get()==1 && replies.size()==1 && replies.getFirst().type().id().equals(id),"registered receive and reply retain schema identity");
        var bytes=((DataPacketPayload)replies.getFirst()).data();
        h.assertTrue(bytes.length==4 && bytes[3]==74,"custom encoder supplied reply bytes"); h.succeed();
    }
    private static void registration(GameTestHelper h) {
        h.assertTrue(registeredPipe != null,"fixture registered during the actual NeoForge payload event");
        h.assertTrue(NetworkRegistry.getCodec(REGISTERED_COUNTER,ConnectionProtocol.PLAY,PacketFlow.CLIENTBOUND) != null
                && NetworkRegistry.getCodec(REGISTERED_COUNTER,ConnectionProtocol.PLAY,PacketFlow.SERVERBOUND) != null,"registered native codec available in both directions");
        var packet=new CounterPacket(91); packet.init(PacketTypes.RAW,registeredPipe);
        FriendlyByteBuf serverWire=new FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
        RegistryFriendlyByteBuf clientWire=new RegistryFriendlyByteBuf(io.netty.buffer.Unpooled.buffer(),h.getLevel().registryAccess());
        try {
            var outgoing=(ServerboundCustomPayloadPacket)registeredPipe.getMinecraftPacket(packet,PacketFlow.SERVERBOUND);
            ServerboundCustomPayloadPacket.STREAM_CODEC.encode(serverWire,outgoing);
            var received=ServerboundCustomPayloadPacket.STREAM_CODEC.decode(serverWire);
            h.assertTrue(received.payload() instanceof DataPacketPayload && received.payload().type().id().equals(REGISTERED_COUNTER)
                    && ((DataPacketPayload)received.payload()).data()[3]==91 && !serverWire.isReadable(),"native serverbound packet codec preserves registered data");
            var response=(ClientboundCustomPayloadPacket)registeredPipe.getMinecraftPacket(packet,PacketFlow.CLIENTBOUND);
            ClientboundCustomPayloadPacket.GAMEPLAY_STREAM_CODEC.encode(clientWire,response);
            var client=ClientboundCustomPayloadPacket.GAMEPLAY_STREAM_CODEC.decode(clientWire);
            h.assertTrue(client.payload() instanceof DataPacketPayload && client.payload().type().id().equals(REGISTERED_COUNTER)
                    && ((DataPacketPayload)client.payload()).data()[3]==91 && !clientWire.isReadable(),"native clientbound packet codec preserves registered data");
        } finally { serverWire.release(); clientWire.release(); }
        try { registeredPipe.registerPacket(CounterPacket.class,(p,b) -> {},b -> new CounterPacket(0)); h.fail("late packet registration accepted"); }
        catch(IllegalStateException expected) { }
        h.succeed();
    }
    private static final class CounterPacket extends PacketObj {
        final int value;
        CounterPacket(int value) { this.value=value; type=PacketTypes.RAW; }
        @Override public void encode(FriendlyByteBuf buffer) { throw new AssertionError("Registered encoder must run"); }
        @Override public DataInputStream getDataIn() { return new DataInputStream(new java.io.ByteArrayInputStream(new byte[0])); }
        @Override protected String getDataAsString() { return Integer.toString(value); }
    }
}
