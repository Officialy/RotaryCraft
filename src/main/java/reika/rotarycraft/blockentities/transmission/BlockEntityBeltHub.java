///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.transmission;
//
//import java.util.Collection;
//
//import org.jetbrains.annotations.NotNull;
//import reika.dragonapi.interfaces.blockentity.Connectable;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.registry.RotaryItems;
//import reika.rotarycraft.registry.SoundRegistry;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//
//import reika.dragonapi.instantiable.StepTimer;
//import reika.rotarycraft.api.Power.PowerGenerator;
//import reika.rotarycraft.api.Power.ShaftMerger;
//import reika.rotarycraft.auxiliary.PowerSourceList;
//import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
//import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
//import reika.rotarycraft.auxiliary.interfaces.TransmissionReceiver;
//
//import reika.rotarycraft.registry.MachineRegistry;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.phys.AABB;
//
//public class BlockEntityBeltHub extends BlockEntityPowerReceiver implements PowerGenerator, SimpleProvider, TransmissionReceiver, Connectable<BlockPos> {
//
//    private final StepTimer sound = new StepTimer(26);
//    public boolean isEmitting;
//    private int wetTimer = 0;
//    private boolean isSlippingTorque;
//    private boolean isSlippingOmega;
//    private BlockPos otherEnd = null;
//
//    public boolean isSplitting() {
//        //return this.getBlockMetadata() >= 6;
//        return false;
//    }
//
//    //@Override
//    public final void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        //this.getIOSidesDefault(world, pos);
//
//        sound.update();
//        //isEmitting = true;
//        if (isEmitting) {
//            write = read;
//            if (this.isSplitting())
//                write2 = read.getOpposite();
//            else
//                write2 = null;
//            read = null;
//            this.copyPower();
//        } else {
//            if (this.isSplitting())
//                write = read.getOpposite();
//            else
//                write = null;
//            this.getPower(false);
//            if (this.isSplitting()) {
//                power /= 2;
//                torque /= 2;
//            }
//        }
//
//        if (power > 0)
//            this.playSound(world, pos);
//
//        if (world.isRaining() && world.canLightningStrikeAt(pos.above()) && world.getDayTime() % 1024 == 0)
//            this.makeWet();
//
//        if (wetTimer > 0 && power > 0)
//            wetTimer--;
//    }
//
//    private void playSound(Level world, BlockPos pos) {
//        if (sound.checkCap()) {
//            SoundRegistry.BELT.playSoundAtBlock(world, pos, 0.6F, 1F);
//        }
//    }
//
//    public final boolean areInSamePlane(BlockEntityBeltHub belt) {
//        int meta = this.getBlockMetadata() % 6;
//        int meta2 = belt.getBlockMetadata() % 6;
//        if (meta == 0 || meta == 1)
//            return meta2 == 0 || meta2 == 1;
//        if (meta == 2 || meta == 3)
//            return meta2 == 2 || meta2 == 3;
//        if (meta == 4 || meta == 5)
//            return meta2 == 4 || meta2 == 5;
//        return false;
//    }
//
//    public final void reset() {
//        otherEnd = null;
//    }
//
//    public final void resetOther() {
//        if (otherEnd == null)
//            return;
//        MachineRegistry m = MachineRegistry.getMachine(level, new BlockPos(otherEnd.getX(), otherEnd.getY(), otherEnd.getZ()));
//        if (m == this.getMachine()) {
//            BlockEntityBeltHub te = (BlockEntityBeltHub) level.getBlockEntity(otherEnd);
//            te.reset();
//        }
//    }
//
//    private boolean canConnect(Level world, BlockPos pos) {
//        int dx = pos.getX() - worldPosition.getX();
//        int dy = pos.getX() - worldPosition.getY();
//        int dz = pos.getZ() - worldPosition.getZ();
//
//        //ReikaJavaLibrary.pConsole(isEmitting ? Arrays.toString(source) : Arrays.toString(target));
//
//        if (!ReikaMathLibrary.nBoolsAreTrue(1, dx != 0, dy != 0, dz != 0))
//            return false;
//
//        Direction dir = Direction.UNKNOWN;
//
//        if (dx > 0)
//            dir = Direction.EAST;
//        if (dx < 0)
//            dir = Direction.WEST;
//        if (dy > 0)
//            dir = Direction.UP;
//        if (dy < 0)
//            dir = Direction.DOWN;
//        if (dz > 0)
//            dir = Direction.SOUTH;
//        if (dz < 0)
//            dir = Direction.NORTH;
//
//        if (dir == null)
//            return false;
//        if (!this.isValidDirection(dir))
//            return false;
//
//        BlockEntity te = world.getBlockEntity(pos);
//        if (te instanceof BlockEntityBeltHub) {
//            BlockEntityBeltHub tb = (BlockEntityBeltHub) te;
//            if (tb.isEmitting == isEmitting)
//                return false;
//            if (!this.areInSamePlane(tb))
//                return false;
//            for (int i = 1; i < Math.abs(dx + dy + dz); i++) {
//                int xi = pos.getX() + dir.getStepX() * i;
//                int yi = pos.getY() + dir.getStepY() * i;
//                int zi = pos.getZ() + dir.getStepZ() * i;
//                if (!ReikaWorldHelper.softBlocks(level, new BlockPos(xi, yi, zi))) {
//                    return false;
//                }
//            }
//            return true;
//        }
//        return false;
//    }
//
//    public final boolean hasValidConnection() {
//        if (otherEnd == null)
//            return false;
//        MachineRegistry m = MachineRegistry.getMachine(level, new BlockPos(otherEnd.getX(), otherEnd.getY(), otherEnd.getZ()));
//        return m == this.getMachine() && this.canConnect(level, new BlockPos(otherEnd.getX(), otherEnd.getY(), otherEnd.getZ()));
//    }
//
//    public final boolean tryConnect(Level world, BlockPos pos) {
//        if (otherEnd != null)
//            return false;
//        if (worldPosition.getX() == pos.getX() && worldPosition.getY() == pos.getY() && worldPosition.getZ() == pos.getZ())
//            return false;
//        if (!this.canConnect(world, pos))
//            return false;
//        otherEnd = pos;
//        return true;
//    }
//
//    public int getMaxTorque() {
//        return 8192;
//    }
//
//    public int getMaxSmoothSpeed() {
//        return 8192;
//    }
//
//    public int getTorque(int input) {
//        int max = this.isWet() ? this.getMaxTorque() / 4 : this.getMaxTorque();
//        isSlippingTorque = input > max;
//        int torque = Math.min(input, max);
//        return this.isSplitting() ? torque / 2 : torque;
//    }
//
//    public int getOmega(int input) {
//        int s = this.isWet() ? this.getMaxSmoothSpeed() / 4 : this.getMaxSmoothSpeed();
//        isSlippingOmega = input > s;
//        int speed = input <= s ? input : (int) (s + Math.sqrt(input - s));
//        return speed;
//    }
//
//    private void copyPower() {
//        if (this.hasValidConnection()) {
//            BlockEntityBeltHub tile = (BlockEntityBeltHub) level.getBlockEntity(otherEnd);
//            omega = this.getOmega(tile.omega);
//            torque = this.getTorque(tile.torque);
//            power = (long) omega * (long) torque;
//        } else {
//            if (omega > 0)
//                omega *= 0.98;
//            else
//                torque = 0;
//            power = (long) omega * (long) torque;
//        }
//    }
//
//    public final Direction getBeltDirection() {
//        if (otherEnd == null)
//            return Direction.UNKNOWN;
//        int dx;
//        int dy;
//        int dz;
//
//        dx = worldPosition.getX() - otherEnd.getX();
//        dy = worldPosition.getY() - otherEnd.getY();
//        dz = worldPosition.getZ() - otherEnd.getZ();
//
//        Direction dir = Direction.UNKNOWN;
//        if (dx < 0)
//            dir = Direction.EAST;
//        if (dx > 0)
//            dir = Direction.WEST;
//        if (dy < 0)
//            dir = Direction.UP;
//        if (dy > 0)
//            dir = Direction.DOWN;
//        if (dz < 0)
//            dir = Direction.SOUTH;
//        if (dz > 0)
//            dir = Direction.NORTH;
//        return dir;
//    }
//
//    public final int getDistanceToTarget() {
//        return otherEnd == null ? -1 : otherEnd.getTaxicabDistanceTo(getPos());
//    }
//
//    public final boolean isValidDirection(Direction dir) {
//        switch (this.getBlockMetadata() % 6) {
//            case 0:
//            case 1:
//                return dir.getStepX() == 0;
//            case 2:
//            case 3:
//                return dir.getStepZ() == 0;
//            case 4:
//            case 5:
//                return dir.getStepY() == 0;
//        }
//        return false;
//    }
//
//    @Override
//    public final boolean canProvidePower() {
//        return isEmitting || this.isSplitting();
//    }
//
//    @Override
//    public final PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
//        if (isEmitting) {
//            BlockEntityBeltHub tile = (BlockEntityBeltHub) level.getBlockEntity(otherEnd);
//            return tile != null ? tile.getPowerSources(io, caller) : new PowerSourceList();
//        } else {
//            return PowerSourceList.getAllFrom(level, read, new BlockPos(worldPosition.getX() + read.getStepX(), worldPosition.getY() + read.getStepY(), worldPosition.getZ() + read.getStepZ()), this, caller);
//        }
//    }
//
//    //@Override
//    protected void animateWithTick(Level world, BlockPos pos) {
////        if (!this.isInWorld()) {
////            phi = 0;
////            return;
////        }
//        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.BELT;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public final long getMaxPower() {
//        return this.isSplitting() ? power / 2 : power;
//    }
//
//    @Override
//    public final long getCurrentPower() {
//        return this.isSplitting() ? power / 2 : power;
//    }
//
//    public final boolean isWet() {
//        return wetTimer > 0;
//    }
//
//    public final void makeWet() {
//        wetTimer = Math.min(wetTimer + 3600, 18000); //3 to 15 min
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        NBT.putBoolean("emit", isEmitting);
//
//        if (otherEnd != null)
//            otherEnd.saveAdditional("endpoint", NBT);
//
//        NBT.putInt("wet", wetTimer);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        isEmitting = NBT.getBoolean("emit");
//
//        otherEnd = BlockPos.load("endpoint", NBT);
//
//        wetTimer = NBT.getInt("wet");
//    }
//
//    @Override
//    public final AABB getRenderBoundingBox() {/*
//		AABB box = ReikaAABBHelper.getBlockAABB(xCoord, yCoord, zCoord);
//		Direction dir = this.getBeltDirection();
//		int a = this.getDistanceToTarget();
//		box.maxX += a*dir.getStepX();
//		box.maxX -= a*dir.getStepX();
//		box.maxY += a*dir.getStepY();
//		box.maxY -= a*dir.getStepY();
//		box.maxZ += a*dir.getStepZ();
//		box.maxZ -= a*dir.getStepZ();*/
//        //return AABB.getBoundingBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
//        return INFINITE_EXTENT_AABB;
//        //int a = this.getDistanceToTarget();
//        //return ReikaAABBHelper.getBlockAABB(xCoord, yCoord, zCoord).expand(a, a, a);
//    }
//
//    public final BlockPos getConnection() {
//        return otherEnd;
//    }
//
//    @Override
//    public BlockPos getEmittingPos(BlockPos pos) {
//        return new BlockPos(worldPosition.getX() + write.getStepX(), worldPosition.getY() + write.getStepY(), worldPosition.getZ() + write.getStepZ());
//    }
//
//    public int[] getBeltColor() {
//        return new int[]{192, 120, 70};
//    }
//
//    public ItemStack getBeltItem() {
//        return RotaryItems.BELT.get().getDefaultInstance();
//    }
//
//    @Override
//    public boolean isEmitting() {
//        return isEmitting;
//    }
//
//    @Override
//    public final void breakBlock() {
//        if (!level.isClientSide) {
//            int num = this.getDistanceToTarget() - 1;
//            num = Math.min(num, RotaryItems.BELT.get().getMaxStackSize());
//            if (!this.hasValidConnection())
//                num = 0;
//            for (int i = 0; i < num; i++) {
//                ReikaItemHelper.dropItem(level, new BlockPos(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5), this.getBeltItem());
//            }
//            this.resetOther();
//        }
//    }
//
//    @Override
//    public final void getOutputs(Collection<BlockEntity> c, Direction dir) {
//        if (otherEnd != null) {
//            BlockEntity te = level.getBlockEntity(otherEnd);
//            if (te instanceof BlockEntityBeltHub) {
//                BlockEntityBeltHub belt = (BlockEntityBeltHub) te;
//                c.add(getAdjacentBlockEntity(belt.write));
//                c.add(getAdjacentBlockEntity(belt.write2));
//            }
//        }
//    }
//
//    public boolean isSlipping() {
//        return isSlippingOmega || isSlippingTorque;
//    }
//
//    @Override
//    public int getSlots() {
//        return 0;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack getStackInSlot(int slot) {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack extractItem(int slot, int amount, boolean simulate) {
//        return null;
//    }
//
//    @Override
//    public int getSlotLimit(int slot) {
//        return 0;
//    }
//
//    @Override
//    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
//        return false;
//    }
//
//    @Override
//    public String getName() {
//        return null;
//    }
//
//    @Override
//    public boolean hasAnInventory() {
//        return false;
//    }
//
//    @Override
//    public boolean hasATank() {
//        return false;
//    }
//}
