/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.transmission;

import java.util.Collection;


import reika.dragonapi.interfaces.blockentity.BreakAction;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.SoundRegistry;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

import reika.dragonapi.instantiable.StepTimer;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.api.power.PowerGenerator;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import reika.rotarycraft.auxiliary.interfaces.TransmissionReceiver;

import reika.rotarycraft.registry.MachineRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

public class BlockEntityBeltHub extends BlockEntityPowerReceiver implements PowerGenerator, SimpleProvider, TransmissionReceiver, BreakAction {

    public BlockEntityBeltHub(BlockPos pos, BlockState state) {
        this(RotaryBlockEntities.BELT.get(), pos, state);
    }

    protected BlockEntityBeltHub(net.minecraft.world.level.block.entity.BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /** The hub shaft axis (from FACING); the belt must run perpendicular to it. */
    public final Direction.Axis getHubAxis() {
        BlockState state = this.getBlockState();
        Direction d = state.hasProperty(BlockRotaryCraftMachine.FACING) ? state.getValue(BlockRotaryCraftMachine.FACING) : Direction.EAST;
        return d.getAxis();
    }

    private Direction getFacingDir() {
        BlockState state = this.getBlockState();
        return state.hasProperty(BlockRotaryCraftMachine.FACING) ? state.getValue(BlockRotaryCraftMachine.FACING) : Direction.EAST;
    }

    private final StepTimer sound = new StepTimer(26);
    public boolean isEmitting;
    private int wetTimer = 0;
    private boolean isSlippingTorque;
    private boolean isSlippingOmega;
    private BlockPos otherEnd = null;

    public boolean isSplitting() {
        //return this.getBlockMetadata() >= 6;
        return false;
    }

    @Override
    public final void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        read = this.getFacingDir();

        sound.update();
        //isEmitting = true;
        if (isEmitting) {
            write = read;
            if (this.isSplitting())
                write2 = read.getOpposite();
            else
                write2 = null;
            read = null;
            this.copyPower();
        } else {
            if (this.isSplitting())
                write = read.getOpposite();
            else
                write = null;
            this.getPower(false);
            if (this.isSplitting()) {
                power /= 2;
                torque /= 2;
            }
        }

        if (power > 0)
            this.playSound(world, pos);

        if (!world.isClientSide() && world.isRaining() && world.canSeeSky(pos.above()) && world.getGameTime() % 1024 == 0)
            this.makeWet();

        if (wetTimer > 0 && power > 0)
            wetTimer--;
    }

    private void playSound(Level world, BlockPos pos) {
        if (sound.checkCap()) {
            SoundRegistry.BELT.playSoundAtBlock(world, pos, 0.6F, 1F);
        }
    }

    public final boolean areInSamePlane(BlockEntityBeltHub belt) {
        return this.getHubAxis() == belt.getHubAxis();
    }

    public final void reset() {
        otherEnd = null;
    }

    public final void resetOther() {
        if (otherEnd == null)
            return;
        MachineRegistry m = MachineRegistry.getMachine(level, new BlockPos(otherEnd.getX(), otherEnd.getY(), otherEnd.getZ()));
        if (m == this.getMachine()) {
            BlockEntityBeltHub te = (BlockEntityBeltHub) level.getBlockEntity(otherEnd);
            te.reset();
        }
    }

    private boolean canConnect(Level world, BlockPos pos) {
        int dx = pos.getX() - worldPosition.getX();
        int dy = pos.getY() - worldPosition.getY();
        int dz = pos.getZ() - worldPosition.getZ();

        //ReikaJavaLibrary.pConsole(isEmitting ? Arrays.toString(source) : Arrays.toString(target));

        if (!ReikaMathLibrary.nBoolsAreTrue(1, dx != 0, dy != 0, dz != 0))
            return false;

        Direction dir = null;

        if (dx > 0)
            dir = Direction.EAST;
        if (dx < 0)
            dir = Direction.WEST;
        if (dy > 0)
            dir = Direction.UP;
        if (dy < 0)
            dir = Direction.DOWN;
        if (dz > 0)
            dir = Direction.SOUTH;
        if (dz < 0)
            dir = Direction.NORTH;

        if (dir == null)
            return false;
        if (!this.isValidDirection(dir))
            return false;

        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof BlockEntityBeltHub) {
            BlockEntityBeltHub tb = (BlockEntityBeltHub) te;
            if (tb.isEmitting == isEmitting)
                return false;
            if (!this.areInSamePlane(tb))
                return false;
            for (int i = 1; i < Math.abs(dx + dy + dz); i++) {
                int xi = pos.getX() + dir.getStepX() * i;
                int yi = pos.getY() + dir.getStepY() * i;
                int zi = pos.getZ() + dir.getStepZ() * i;
                if (!ReikaWorldHelper.softBlocks(level, new BlockPos(xi, yi, zi))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public final boolean hasValidConnection() {
        if (otherEnd == null)
            return false;
        MachineRegistry m = MachineRegistry.getMachine(level, new BlockPos(otherEnd.getX(), otherEnd.getY(), otherEnd.getZ()));
        return m == this.getMachine() && this.canConnect(level, new BlockPos(otherEnd.getX(), otherEnd.getY(), otherEnd.getZ()));
    }

    public final boolean tryConnect(Level world, BlockPos pos) {
        if (otherEnd != null)
            return false;
        if (worldPosition.getX() == pos.getX() && worldPosition.getY() == pos.getY() && worldPosition.getZ() == pos.getZ())
            return false;
        if (!this.canConnect(world, pos))
            return false;
        otherEnd = pos;
        return true;
    }

    public int getMaxTorque() {
        return 8192;
    }

    public int getMaxSmoothSpeed() {
        return 8192;
    }

    public int getTorque(int input) {
        int max = this.isWet() ? this.getMaxTorque() / 4 : this.getMaxTorque();
        isSlippingTorque = input > max;
        int torque = Math.min(input, max);
        return this.isSplitting() ? torque / 2 : torque;
    }

    public int getOmega(int input) {
        int s = this.isWet() ? this.getMaxSmoothSpeed() / 4 : this.getMaxSmoothSpeed();
        isSlippingOmega = input > s;
        int speed = input <= s ? input : (int) (s + Math.sqrt(input - s));
        return speed;
    }

    private void copyPower() {
        if (this.hasValidConnection()) {
            BlockEntityBeltHub tile = (BlockEntityBeltHub) level.getBlockEntity(otherEnd);
            omega = this.getOmega(tile.omega);
            torque = this.getTorque(tile.torque);
            power = (long) omega * (long) torque;
        } else {
            if (omega > 0)
                omega = (int) (omega * 0.98);
            else
                torque = 0;
            power = (long) omega * (long) torque;
        }
    }

    public final Direction getBeltDirection() {
        if (otherEnd == null)
            return null;
        int dx;
        int dy;
        int dz;

        dx = worldPosition.getX() - otherEnd.getX();
        dy = worldPosition.getY() - otherEnd.getY();
        dz = worldPosition.getZ() - otherEnd.getZ();

        Direction dir = null;
        if (dx < 0)
            dir = Direction.EAST;
        if (dx > 0)
            dir = Direction.WEST;
        if (dy < 0)
            dir = Direction.UP;
        if (dy > 0)
            dir = Direction.DOWN;
        if (dz < 0)
            dir = Direction.SOUTH;
        if (dz > 0)
            dir = Direction.NORTH;
        return dir;
    }

    public final int getDistanceToTarget() {
        return otherEnd == null ? -1 : otherEnd.distManhattan(this.getBlockPos());
    }

    public final boolean isValidDirection(Direction dir) {
        // The belt runs perpendicular to the hub shaft axis.
        return dir.getAxis() != this.getHubAxis();
    }

    @Override
    public final boolean canProvidePower() {
        return isEmitting || this.isSplitting();
    }

    @Override
    public final PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
        if (isEmitting) {
            BlockEntityBeltHub tile = (BlockEntityBeltHub) level.getBlockEntity(otherEnd);
            return tile != null ? tile.getPowerSources(io, caller) : new PowerSourceList();
        } else {
            return PowerSourceList.getAllFrom(level, read, new BlockPos(worldPosition.getX() + read.getStepX(), worldPosition.getY() + read.getStepY(), worldPosition.getZ() + read.getStepZ()), this, caller);
        }
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
//        if (!this.isInWorld()) {
//            phi = 0;
//            return;
//        }
        phi += (float) ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.BELT;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public final long getMaxPower() {
        return this.isSplitting() ? power / 2 : power;
    }

    @Override
    public final long getCurrentPower() {
        return this.isSplitting() ? power / 2 : power;
    }

    public final boolean isWet() {
        return wetTimer > 0;
    }

    public final void makeWet() {
        wetTimer = Math.min(wetTimer + 3600, 18000); //3 to 15 min
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putBoolean("emit", isEmitting);

        if (otherEnd != null)
            NBT.putLong("endpoint", otherEnd.asLong());

        NBT.putInt("wet", wetTimer);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        isEmitting = NBT.getBooleanOr("emit", false);

        otherEnd = NBT.getLong("endpoint").isPresent() ? BlockPos.of(NBT.getLongOr("endpoint", 0L)) : null;

        wetTimer = NBT.getIntOr("wet", 0);
    }


    public final BlockPos getConnection() {
        return otherEnd;
    }

    @Override
    public BlockPos getEmittingPos(BlockPos pos) {
        return write != null ? worldPosition.relative(write) : worldPosition;
    }

    public int[] getBeltColor() {
        return new int[]{192, 120, 70};
    }

    public ItemStack getBeltItem() {
        return RotaryItems.BELT.get().getDefaultInstance();
    }

    public boolean isEmitting() {
        return isEmitting;
    }

    @Override
    public final void breakBlock() {
        if (!level.isClientSide()) {
            int num = this.getDistanceToTarget() - 1;
            num = Math.min(num, this.getBeltItem().getMaxStackSize());
            if (!this.hasValidConnection())
                num = 0;
            for (int i = 0; i < num; i++) {
                ReikaItemHelper.dropItem(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, this.getBeltItem());
            }
            this.resetOther();
        }
    }

    @Override
    public final void getOutputs(Collection<BlockEntity> c, Direction dir) {
        if (otherEnd != null) {
            BlockEntity te = level.getBlockEntity(otherEnd);
            if (te instanceof BlockEntityBeltHub) {
                BlockEntityBeltHub belt = (BlockEntityBeltHub) te;
                if (belt.write != null)
                    c.add(belt.getAdjacentBlockEntity(belt.write));
                if (belt.write2 != null)
                    c.add(belt.getAdjacentBlockEntity(belt.write2));
            }
        }
    }

    public boolean isSlipping() {
        return isSlippingOmega || isSlippingTorque;
    }

    @Override
    protected String getTEName() {
        return "belthub";
    }

    @Override
    public net.minecraft.world.level.block.Block getBlockEntityBlockID() {
        return RotaryBlocks.BELT.get();
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
