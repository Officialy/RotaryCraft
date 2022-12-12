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
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.instantiable.data.immutable.BlockKey;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.api.interfaces.ImmovableBlock;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;

import reika.rotarycraft.registry.*;

public class BlockEntityLineBuilder extends InventoriedPowerReceiver implements RangedEffect, ConditionalOperation, DiscreteFunction {

    private final StepTimer timer = new StepTimer(40);
    private final boolean isOut = false;
    private Direction dir;

    public BlockEntityLineBuilder(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.LINE_BUILDER.get(), pos, state);
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (power >= MINPOWER && torque >= MINTORQUE)
            phi = 1 - timer.getFraction() - 0.01F;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.LINEBUILDER;
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
    protected String getTEName() {
        return null;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
//        this.getIOSides(world, pos);
        this.getPower(false);

        if (power < MINPOWER || torque < MINTORQUE)
            return;

        timer.setCap(this.getOperationTime());
        timer.update();

        if (timer.checkCap()) {
            if (!world.isClientSide) {
                this.shiftBlocks(world, pos);
                phi = 0.5F;
            }
        }
    }

    private void getIOSides(Level world, BlockPos pos, Direction dir) {
        switch (dir) {
            case EAST -> {
                read = Direction.EAST;
                dir = Direction.WEST;
            }
            case WEST -> {
                read = Direction.WEST;
                dir = Direction.EAST;
            }
            case SOUTH -> {
                read = Direction.SOUTH;
                dir = Direction.NORTH;
            }
            case NORTH -> {
                read = Direction.NORTH;
                dir = Direction.SOUTH;
            }
            case UP -> {    //moving up
                read = Direction.DOWN;
                dir = Direction.UP;
            }
            case DOWN -> {    //moving down
                read = Direction.UP;
                dir = Direction.DOWN;
            }
        }
    }

    private boolean shiftBlocks(Level world, BlockPos pos) {
        SoundRegistry.LINEBUILDER.playSoundAtBlock(world, pos);
        if (!this.canShift(world, pos))
            return false;
        BlockKey is = this.getNextBlockToAdd();
        if (is == null)
            return false;
        int r = this.getLineLength();
        for (int i = r; i > 0; i--) {
            int rx = pos.getX() + dir.getStepX() * i;
            int ry = pos.getY() + dir.getStepY() * i;
            int rz = pos.getZ() + dir.getStepZ() * i;
            int rx2 = pos.getX() + dir.getStepX() * (i + 1);
            int ry2 = pos.getY() + dir.getStepY() * (i + 1);
            int rz2 = pos.getZ() + dir.getStepZ() * (i + 1);
            Block b = world.getBlockState(new BlockPos(rx, ry, rz)).getBlock();
            world.setBlock(new BlockPos(rx2, ry2, rz2), b.defaultBlockState(), 3);
            world.setBlock(new BlockPos(rx, ry, rz), Blocks.AIR.defaultBlockState(), 0);
        }
        int rx = pos.getX() + dir.getStepX();
        int ry = pos.getY() + dir.getStepY();
        int rz = pos.getZ() + dir.getStepZ();
        is.place(world, new BlockPos(rx, ry, rz));
        SoundRegistry.LINEBUILDER.playSoundAtBlock(world, new BlockPos(rx, ry, rz));
        return true;
    }

    public boolean canShift(Level world, BlockPos pos) {
        int r = this.getLineLength() + 1;
        int rx = pos.getX() + dir.getStepX() * r;
        int ry = pos.getY() + dir.getStepY() * r;
        int rz = pos.getZ() + dir.getStepZ() * r;
        boolean softend = ReikaWorldHelper.softBlocks(world, new BlockPos(rx, ry, rz));
        return softend && r <= this.getMaxRange() && r > 0;
    }

    public BlockKey getNextBlockToAdd() {
        ItemStack is = ReikaInventoryHelper.getNextBlockInInventory(inv, true);
        if (is == null)
            return null;
        return ReikaItemHelper.getWorldBlockFromItem(is);
    }

    @Override
    public int getRange() {
        int range = this.getLineLength();
        return range;
    }

    public int getLineLength() {
        int len = 0;
        int i = 1;
        int rx = worldPosition.getX() + dir.getStepX() * i;
        int ry = worldPosition.getY() + dir.getStepY() * i;
        int rz = worldPosition.getZ() + dir.getStepZ() * i;
        Block id = level.getBlockState(new BlockPos(rx, ry, rz)).getBlock();
        if (id == Blocks.BEDROCK)
            return Integer.MIN_VALUE;
//        if (!level.isClientSide && !ReikaPlayerAPI.playerCanBreakAt((ServerLevel) level, rx, ry, rz, this.getServerPlacer()))
//            return Integer.MIN_VALUE;
        int maxr = this.getMaxRange();
        BlockEntity te = level.getBlockEntity(new BlockPos(rx, ry, rz));
        if (te != null)
            return Integer.MIN_VALUE;
        while (!ReikaWorldHelper.softBlocks(level, new BlockPos(rx, ry, rz)) && i <= maxr) {
            i++;
            rx = worldPosition.getX() + dir.getStepX() * i;
            ry = worldPosition.getY() + dir.getStepY() * i;
            rz = worldPosition.getZ() + dir.getStepZ() * i;
            id = level.getBlockState(new BlockPos(rx, ry, rz)).getBlock();
            if (id == Blocks.BEDROCK)
                return Integer.MIN_VALUE;
            if (id instanceof ImmovableBlock) {
                ImmovableBlock im = (ImmovableBlock) id;
                if (!im.canBePushed(level, new BlockPos(rx, ry, rz), i, torque, power))
                    return Integer.MIN_VALUE;
            }
//            if (!level.isClientSide && !ReikaPlayerAPI.playerCanBreakAt((ServerLevel) level, rx, ry, rz, this.getServerPlacer()))
//                return Integer.MIN_VALUE;
            BlockEntity tile = level.getBlockEntity(new BlockPos(rx, ry, rz));
            if (tile != null)
                return Integer.MIN_VALUE;
        }
        return i - 1;
    }

    @Override
    public int getMaxRange() {
        return Math.max(64, ConfigRegistry.LINEBUILDER.getValue());
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    @Override
    public boolean areConditionsMet() {
        return !ReikaInventoryHelper.isEmpty(inv);
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Blocks";
    }

    @Override
    public int getOperationTime() {
        int time = DurationRegistry.RAM.getOperationTime(omega);
        return Math.max(3, time);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return null;
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return null;
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {

    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }

    @Override
    public void clearContent() {

    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }
}
