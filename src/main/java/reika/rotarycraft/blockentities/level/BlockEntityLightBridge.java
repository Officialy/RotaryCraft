/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.level;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.interfaces.blockentity.BreakAction;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.base.blockentity.BlockEntityBeamMachine;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.PowerReceivers;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * The Light Bridge extends a walkable bridge of solid light blocks straight out in front of itself,
 * as long as it is powered and the space directly above it is brightly lit (light level ≥ 13 — so a
 * sunlit or otherwise well-lit emitter). The bridge grows one block per tick up to a power-scaled
 * range and retracts the moment the light or power is lost. 1.7.10-faithful; the public
 * {@code LightBridgePowerLossEvent} is not fired (API-PORT: the event class is not in this build).
 */
public class BlockEntityLightBridge extends BlockEntityBeamMachine implements RangedEffect, BreakAction {

    public static final int DISTANCE_LIMIT = Math.max(64, ConfigRegistry.BRIDGERANGE.getValue());

    private int animtick = 0;

    public BlockEntityLightBridge(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.LIGHT_BRIDGE.get(), pos, state);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        animtick++;
        this.getIOSides(world, pos, getBlockState().getValue(BlockRotaryCraftMachine.FACING));
        this.getPower(false);
        power = (long) omega * (long) torque;
        if (!world.isClientSide())
            this.makeBeam(world, pos);
    }

    @Override
    protected void makeBeam(Level world, BlockPos pos) {
        int range = this.getRange();
        // Needs to be brightly lit directly above (sun/light ≥ 13), like the original.
        if (range > 0 && world.getMaxLocalRawBrightness(pos.above()) >= 13) {
            boolean blocked = false;
            for (int i = 1; i <= range && i <= animtick && !blocked; i++) {
                BlockPos p = pos.relative(facing, i);
                Block b = world.getBlockState(p).getBlock();
                if (this.isOverwritable(world, p, b)) {
                    world.setBlock(p, RotaryBlocks.BRIDGE.get().defaultBlockState(), 3);
                } else {
                    animtick--; //hit an obstruction: stop growing here
                    blocked = true;
                }
            }
        } else {
            // API-PORT: legacy posted MinecraftForge.EVENT_BUS.post(new LightBridgePowerLossEvent(this)).
            this.lightsOut(world, pos);
        }
    }

    private boolean isOverwritable(Level world, BlockPos p, Block b) {
        return world.getBlockState(p).isAir() || ReikaWorldHelper.softBlocks(world, p)
                || b == Blocks.LIGHT || b == RotaryBlocks.BEAM.get() || b == RotaryBlocks.BRIDGE.get();
    }

    private void lightsOut(Level world, BlockPos pos) {
        animtick = 0;
        for (int i = 1; i < this.getMaxRange(); i++) {
            BlockPos p = pos.relative(facing, i);
            if (world.getBlockState(p).is(RotaryBlocks.BRIDGE.get()))
                world.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    @Override
    public int getRange() {
        long min = PowerReceivers.LIGHTBRIDGE.getMinPower();
        return (int) Math.min(DISTANCE_LIMIT, power * DISTANCE_LIMIT / min);
    }

    @Override
    public int getMaxRange() {
        return DISTANCE_LIMIT;
    }

    @Override
    public void breakBlock() {
        if (level != null)
            this.lightsOut(level, worldPosition);
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.LIGHTBRIDGE;
    }

    @Override
    protected String getTEName() {
        return "lightbridge";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.LIGHT_BRIDGE.get();
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        animtick = NBT.getIntOr("tick", 0);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("tick", animtick);
    }
}
