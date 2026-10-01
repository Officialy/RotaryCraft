/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities;

import java.util.Collection;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.auxiliary.ChunkManager;
import reika.dragonapi.interfaces.blockentity.ChunkLoadingTile;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/** V33a speed-gated chunk loader. Ticket ownership belongs to this block position. */
public class BlockEntityChunkLoader extends BlockEntityPowerReceiver implements ChunkLoadingTile, RangedEffect {
    public static final int BASE_RADIUS = 0;
    public static final int FALLOFF = 524288;
    private int lastRadius = -2;

    public BlockEntityChunkLoader(BlockPos pos, BlockState state) { super(RotaryBlockEntities.CHUNK_LOADER.get(), pos, state); }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        if (world.isClientSide()) return;
        getPowerBelow();
        int radius = isActive() ? getLoadingRadius() : -1;
        if (radius != lastRadius || radius >= 0 && !ChunkManager.instance.isLoaded(this)) {
            // The manager reconciles the delta, including contraction after a torque change.
            if (radius >= 0) ChunkManager.instance.loadChunks(this);
            else ChunkManager.instance.unloadChunks(this);
            lastRadius = radius;
            setChanged();
            syncAllData(false);
        }
    }

    public boolean isActive() { return ConfigRegistry.ALLOWCHUNKLOADER.getState() && omega >= MINSPEED; }
    public int getLoadingRadius() {
        // Subtract before division, as in V33a, but retain long power to avoid integer overflow.
        return (int)Math.clamp(BASE_RADIUS + (power - MINSPEED) / FALLOFF, 0L, getMaxRange() / 16L);
    }
    @Override public Collection<ChunkPos> getChunksToLoad() {
        return isActive() ? ChunkManager.getChunkSquare(worldPosition.getX(), worldPosition.getZ(), getLoadingRadius()) : List.of();
    }
    @Override public void breakBlock() {
        if (level != null && !level.isClientSide()) {
            ChunkManager.instance.unloadChunks(this);
            lastRadius = -1;
        }
    }
    @Override protected void animateWithTick(Level world, BlockPos pos) {
        if (world != null && omega > 0) phi = (float)((phi - 0.25 * Math.log(omega + 1D) / Math.log(2)) % 360);
    }
    @Override public int getRange() { return getLoadingRadius() * 16; }
    @Override public int getMaxRange() { return Math.max(0, ConfigRegistry.CHUNKLOADERSIZE.getValue()) * 16; }
    @Override public boolean hasAnInventory() { return false; }
    @Override public boolean hasATank() { return false; }
    @Override public boolean hasModelTransparency() { return false; }
    @Override public int getRedstoneOverride() { return 0; }
    @Override public MachineRegistry getMachine() { return MachineRegistry.CHUNKLOADER; }
    @Override public net.minecraft.world.level.block.Block getBlockEntityBlockID() { return RotaryBlocks.CHUNK_LOADER.get(); }
    @Override protected String getTEName() { return "Chunk Loader"; }
}
