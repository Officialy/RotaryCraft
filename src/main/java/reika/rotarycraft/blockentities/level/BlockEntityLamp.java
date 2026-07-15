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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.instantiable.data.blockstruct.BlockArray;
import reika.dragonapi.base.OneSlotMachine;
import reika.dragonapi.interfaces.blockentity.BreakAction;
import reika.dragonapi.interfaces.blockentity.InertIInv;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntitySpringPowered;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * The Lamp is a spring-powered area light: while wound (a charged coil in its single slot) and not
 * redstone-suppressed, it fills an octahedral volume around itself with vanilla invisible light
 * blocks (radius {@value #MAXRANGE}), lighting a large space with no ongoing power draw beyond the
 * coil slowly unwinding. Cutting power (redstone signal or a dead coil) removes the placed lights.
 */
public class BlockEntityLamp extends BlockEntitySpringPowered implements InertIInv, RangedEffect, OneSlotMachine, BreakAction {

    public static final int MAXRANGE = 12;

    private final BlockArray light = new BlockArray();
    private boolean canlight;
    private int unwindTick;

    public BlockEntityLamp(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.LAMP.get(), pos, state);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.LAMP;
    }

    @Override
    protected String getTEName() {
        return "lamp";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.LAMP.get();
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
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateEntity(); // 26.1 lifecycle (ticksExisted++, onFirstTick)

        boolean red = world.hasNeighborSignal(pos);
        if (red)
            canlight = false;
        else
            this.updateCoil();

        if (!canlight) {
            this.goDark(world);
            return;
        }

        if (light.isEmpty()) {
            this.buildLightVolume(world, pos);
            return; //next tick actually places the lights
        }

        for (int n = 0; n < light.getSize(); n++) {
            BlockPos c = light.getNthBlock(n);
            if (world.getBlockState(c).isAir())
                world.setBlock(c, Blocks.LIGHT.defaultBlockState(), 3);
            world.sendBlockUpdated(c, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    private void buildLightVolume(Level world, BlockPos pos) {
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        int r = this.getRange();
        // Cardinal rays out to the full range.
        for (int i = 1; i <= r; i++) {
            this.tryAdd(world, x + i, y, z);
            this.tryAdd(world, x - i, y, z);
            this.tryAdd(world, x, y + i, z);
            this.tryAdd(world, x, y - i, z);
            this.tryAdd(world, x, y, z + i);
            this.tryAdd(world, x, y, z - i);
        }
        // Diagonal fill (every other step, out to 80% range) so the volume reads as a sphere.
        for (int d = 2; d <= r * 0.8; d += 2) {
            for (int sy = -1; sy <= 1; sy++) {
                if (sy == 0) {
                    this.tryAdd(world, x + d, y, z + d);
                    this.tryAdd(world, x - d, y, z + d);
                    this.tryAdd(world, x + d, y, z - d);
                    this.tryAdd(world, x - d, y, z - d);
                } else {
                    int dy = sy * d;
                    this.tryAdd(world, x + d, y + dy, z + d);
                    this.tryAdd(world, x - d, y + dy, z + d);
                    this.tryAdd(world, x + d, y + dy, z - d);
                    this.tryAdd(world, x - d, y + dy, z - d);
                }
            }
        }
    }

    private void tryAdd(Level world, int x, int y, int z) {
        BlockPos p = new BlockPos(x, y, z);
        if (this.canEditAt(world, p))
            light.addBlockCoordinate(p);
    }

    public boolean canEditAt(Level world, BlockPos pos) {
        return world.getBlockState(pos).isAir();
    }

    private void goDark(Level world) {
        for (int n = 0; n < light.getSize(); n++) {
            BlockPos c = light.getNthBlock(n);
            if (world.getBlockState(c).is(Blocks.LIGHT)) {
                world.setBlock(c, Blocks.AIR.defaultBlockState(), 3);
                world.sendBlockUpdated(c, this.getBlockState(), this.getBlockState(), 3);
            }
        }
    }

    private void updateCoil() {
        if (!this.hasCoil()) {
            canlight = false;
            return;
        }
        unwindTick++;
        if (unwindTick > this.getUnwindTime()) {
            itemHandler.setStackInSlot(this.getCoilSlot(), this.getDecrementedCharged());
            unwindTick = 0;
        }
        canlight = true;
    }

    @Override
    public int getRange() {
        return this.getMaxRange();
    }

    @Override
    public int getMaxRange() {
        return MAXRANGE;
    }

    @Override
    public int getBaseDischargeTime() {
        return 120;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    // ==== Container: the single coil slot, backed by itemHandler (like BlockEntitySmokeDetector) ====

    @Override
    public boolean isEmpty() {
        return itemHandler.getStackInSlot(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot == 0 ? itemHandler.getStackInSlot(0) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return slot == 0 ? itemHandler.extractItem(0, amount, false) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot != 0)
            return ItemStack.EMPTY;
        ItemStack stack = itemHandler.getStackInSlot(0);
        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot == 0)
            itemHandler.setStackInSlot(0, stack);
    }

    @Override
    public void clearContent() {
        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
    }

    @Override
    public boolean stillValid(Player player) {
        return !isRemoved() && player.distanceToSqr(
                worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64;
    }

    @Override
    public void breakBlock() {
        if (level != null) {
            for (int k = 0; k < light.getSize(); k++) {
                BlockPos c = light.getNthBlock(k);
                if (level.getBlockState(c).is(Blocks.LIGHT))
                    level.setBlock(c, Blocks.AIR.defaultBlockState(), 3);
            }
        }
        light.clear();
    }
}
