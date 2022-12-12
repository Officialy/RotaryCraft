/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blockentity;


import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.interfaces.blockentity.GuiController;
import reika.dragonapi.libraries.rendering.ReikaColorAPI;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import reika.rotarycraft.registry.ConfigRegistry;


public abstract class BlockEntityProtectionDome extends BlockEntityPowerReceiver implements RangedEffect, GuiController {

    public int setRange;
    private int rgb;

    public BlockEntityProtectionDome(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract ParticleOptions getParticleType();

    public abstract int getFallOff();

    public abstract int getRangeBoost();

    protected void setColor(int r, int g, int b) {
        rgb = ReikaColorAPI.RGBtoHex(Math.min(255, r), Math.min(255, g), Math.min(255, b));
    }

    public final int getDomeColor() {
        return rgb;
    }

    protected final boolean isClear(Level world, BlockPos pos) {
        for (int i = 1; i <= setRange; i++) {
            Block id = world.getBlockState(new BlockPos(pos.getX(), pos.getY() + i, pos.getZ())).getBlock();
            if (id != Blocks.AIR && id.getLightEmission(this.getBlockState(), world, pos.above(i)) > 0) //getLightOpacity
                return false;
        }
        return true;
    }

    public final int getMaxRange() {
        if (!this.isClear(level, worldPosition))
            return 0;
        if (power < MINPOWER)
            return 0;
        int range = 2 + (int) ((power - MINPOWER) / this.getFallOff()) + this.getRangeBoost();
        int max = Math.max(64, ConfigRegistry.FORCERANGE.getValue());
        if (range > max)
            return max;
        return range;
    }

    @Override
    public final AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putInt("setRange", setRange);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        setRange = tag.getInt("setRange");
    }

    protected final void spawnParticles(Level world, BlockPos pos) {
        for (int i = 0; i < 4; i++) {
            world.addParticle(this.getParticleType(), pos.getX() + DragonAPI.rand.nextDouble(), pos.getY() + DragonAPI.rand.nextDouble() - 0.5, pos.getZ() + DragonAPI.rand.nextDouble(), DragonAPI.rand.nextDouble() - 0.5, DragonAPI.rand.nextDouble(), DragonAPI.rand.nextDouble() - 0.5);
        }
    }

    protected final AABB getRangedBox() {
        int r = this.getRange();
        return new AABB(worldPosition, new BlockPos(worldPosition.getX() + 1, worldPosition.getY() + 1, worldPosition.getZ() + 1)).expandTowards(r, r, r);
    }

    @Override
    public final boolean hasModelTransparency() {
        return false;
    }

    @Override
    public final int getRange() {
        return Math.min(setRange, this.getMaxRange());
    }

    //@Override
    public int getRedstoneOverride() {
        return 0;
    }

}
