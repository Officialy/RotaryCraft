package reika.rotarycraft.blockentities.production;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.registry.RotaryBlockEntities;

/** The progressively-ground bedrock block left by the bedrock breaker. Wears from 0..15 before
 * yielding bedrock dust; tracks the drill direction so the slice thins toward the breaker. */
public class BlockEntityBedrockSlice extends BlockEntity {

    public static final int MAX_WEAR = 15;

    private int wear = 0;
    public float dustYield = 1;
    private Direction machineDirection = Direction.EAST;

    public BlockEntityBedrockSlice(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.BEDROCK_SLICE.get(), pos, state);
    }

    public int getWear() {
        return wear;
    }

    public void incrementWear() {
        wear = Math.min(MAX_WEAR, wear + 1);
        this.sync();
    }

    public Direction getMachineDirection() {
        return machineDirection;
    }

    public void setDirection(Direction dir) {
        machineDirection = dir;
        this.sync();
    }

    private void sync() {
        setChanged();
        if (level != null && !level.isClientSide())
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    @Override
    protected void loadAdditional(net.minecraft.world.level.storage.ValueInput input) {
        super.loadAdditional(input);
        wear = input.getIntOr("wear", 0);
        dustYield = input.getFloatOr("yield", 1);
        machineDirection = Direction.from3DDataValue(input.getIntOr("dir", Direction.EAST.get3DDataValue()));
    }

    @Override
    protected void saveAdditional(net.minecraft.world.level.storage.ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("wear", wear);
        output.putFloat("yield", dustYield);
        output.putInt("dir", machineDirection.get3DDataValue());
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveCustomOnly(provider);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
