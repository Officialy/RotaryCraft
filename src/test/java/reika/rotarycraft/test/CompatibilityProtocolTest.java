package reika.rotarycraft.test;

import io.netty.buffer.Unpooled;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.junit.jupiter.api.Test;
import reika.dragonapi.auxiliary.PacketTypes;
import reika.dragonapi.instantiable.io.SyncPacket;
import reika.dragonapi.interfaces.PacketHandler;
import reika.dragonapi.libraries.io.PacketValidation;
import reika.dragonapi.libraries.io.ReikaPacketHelper.DataPacket;

import static org.junit.jupiter.api.Assertions.*;

class CompatibilityProtocolTest {
    @Test
    void packetBodyCannotAllocateItsDeclaredLengthBeyondReceivedBytes() {
        for (int length : new int[] {-1, Integer.MAX_VALUE, 8}) {
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            try {
                buffer.writeByte(PacketTypes.RAW.ordinal());
                buffer.writeVarInt(length);
                buffer.writeByte(1);
                assertThrows(IllegalArgumentException.class, () -> DataPacket.decode(buffer, (packet, level, player) -> {}));
            } finally { buffer.release(); }
        }
    }

    @Test
    void decodedPacketIsBoundToTheChannelHandler() {
        PacketHandler handler = (packet, level, player) -> {};
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            buffer.writeByte(PacketTypes.RAW.ordinal());
            buffer.writeVarInt(2);
            buffer.writeBytes(new byte[] {17, 23});
            DataPacket packet = DataPacket.decode(buffer, handler);
            assertSame(handler, packet.getHandler());
            assertArrayEquals(new byte[] {17, 23}, packet.getBytes());
            assertEquals(0, buffer.readableBytes());
        } finally { buffer.release(); }
    }

    @Test
    void prefixedArrayRejectsNegativeHugeAndTruncatedCounts() throws Exception {
        for (int count : new int[] {-1, Integer.MAX_VALUE, 4097, 3}) {
            var bytes = new ByteArrayOutputStream();
            var output = new DataOutputStream(bytes);
            output.writeInt(count);
            output.writeInt(1);
            output.writeInt(2);
            assertThrows(java.io.IOException.class, () -> PacketValidation.readIntCount(new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()))));
        }
    }

    @Test
    void deltaSyncTransmitsDeletionsAndSnapshotsMutableTags() {
        SyncPacket packet = new SyncPacket();
        CompoundTag state = new CompoundTag();
        CompoundTag cpu = new CompoundTag();
        cpu.putInt("x", 10);
        state.put("cpu", cpu);
        packet.setData(null, false, state);
        cpu.putInt("x", 11);
        packet.setData(null, false, state);
        assertEquals(11, packet.getChangesForSend().getCompoundOrEmpty("cpu").getIntOr("x", -1));
        state.remove("cpu");
        packet.setData(null, false, state);
        assertEquals("cpu", packet.getChangesForSend().getListOrEmpty(SyncPacket.REMOVED_KEYS).get(0).asString().orElseThrow());
        packet.setData(null, false, state);
        assertTrue(packet.isEmpty(), "a deletion must only be sent once");
    }

    @Test
    void synchronizationPayloadsAreClientboundOnly() {
        assertTrue(PacketTypes.SYNC.isClientboundOnly());
        assertTrue(PacketTypes.TANK.isClientboundOnly());
        assertTrue(PacketTypes.BE_NBT_SYNC.isClientboundOnly());
        assertThrows(IllegalArgumentException.class, () -> PacketTypes.getPacketType(255));
    }
}
