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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.instantiable.data.immutable.WorldLocation;
import reika.rotarycraft.api.IOMachine;
import reika.rotarycraft.api.power.AdvancedShaftPowerReceiver;
import reika.rotarycraft.api.power.ShaftPowerEmitter;
import reika.rotarycraft.api.power.ShaftPowerReceiver;
import reika.rotarycraft.api.power.SimpleShaftPowerReceiver;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
import reika.rotarycraft.blockentities.transmission.BlockEntityShaft;

import java.util.ArrayList;
import java.util.Collection;

public abstract class BlockEntityIOMachine extends RotaryCraftBlockEntity implements IOMachine, PowerSourceTracker {

    public int iotick = 512;
    //public for now
    public long power = 0;
    public int torque = 0;
    public int omega = 0;
    public boolean isOmniSided = false;
    protected Direction write;
    protected Direction write2;
    protected Direction read;
    protected Direction read2;
    protected Direction read3;
    protected Direction read4;
    protected int torquein;
    protected int omegain;
    private int pointoffsetx = 0;
    private int pointoffsety = 0;
    private int pointoffsetz = 0;
    private boolean superCalled = false;

    public BlockEntityIOMachine(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public void updateBlockEntity() {
        updateEntity();
        if (iotick > 0) {
            iotick -= 8;
        }
        superCalled = true;
        level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), 2);
    }

    @Override
    protected void onDataSync(boolean fullNBT) {
        this.recursiveSyncPower(fullNBT, new ArrayList<>());
    }

    private void recursiveSyncPower(boolean fullNBT, Collection<BlockEntityIOMachine> li) {
        li.add(this);
        BlockEntity te = this.getWriteBlockEntity();
        if (te instanceof BlockEntityIOMachine && !li.contains(te)) {
            ((BlockEntityIOMachine) te).recursiveSyncPower(fullNBT, li);
        }
        BlockEntity te2 = this.getWriteBlockEntity2();
        if (te2 instanceof BlockEntityIOMachine && !li.contains(te2)) {
            ((BlockEntityIOMachine) te2).recursiveSyncPower(fullNBT, li);
        }

        BlockEntity tea = this.getReadBlockEntity();
        //ReikaJavaLibrary.pConsole(li.contains(tea), tea instanceof BlockEntityMonitor);
        if (tea instanceof BlockEntityIOMachine && !li.contains(tea)) {
            ((BlockEntityIOMachine) tea).recursiveSyncPower(fullNBT, li);
        }
        BlockEntity teb = this.getReadBlockEntity2();
        if (teb instanceof BlockEntityIOMachine && !li.contains(teb)) {
            ((BlockEntityIOMachine) teb).recursiveSyncPower(fullNBT, li);
        }
        BlockEntity tec = this.getReadBlockEntity3();
        if (tec instanceof BlockEntityIOMachine && !li.contains(tec)) {
            ((BlockEntityIOMachine) tec).recursiveSyncPower(fullNBT, li);
        }
        BlockEntity ted = this.getReadBlockEntity4();
        if (ted instanceof BlockEntityIOMachine && !li.contains(ted)) {
            ((BlockEntityIOMachine) ted).recursiveSyncPower(fullNBT, li);
        }
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putInt("torque", torque);
        tag.putInt("omega", omega);
        tag.putLong("power", power);
        tag.putInt("io", iotick);

        tag.putInt("read1", read != null ? read.ordinal() : -1);
        tag.putInt("read2", read2 != null ? read2.ordinal() : -1);
        tag.putInt("read3", read3 != null ? read3.ordinal() : -1);
        tag.putInt("read4", read4 != null ? read4.ordinal() : -1);

        tag.putInt("write1", write != null ? write.ordinal() : -1);
        tag.putInt("write2", write2 != null ? write2.ordinal() : -1);

        tag.putBoolean("omni", isOmniSided);

        tag.putInt("pox", pointoffsetx);
        tag.putInt("poy", pointoffsety);
        tag.putInt("poz", pointoffsetz);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        torque = tag.getInt("torque");
        omega = tag.getInt("omega");
        power = tag.getLong("power");
        iotick = tag.getInt("io");

        if (torque < 0 || torque == Double.POSITIVE_INFINITY || Double.isNaN(torque))
            torque = 0;
        if (omega < 0 || omega == Double.POSITIVE_INFINITY || Double.isNaN(omega))
            omega = 0;

        int r1 = tag.getInt("read1");
        int r2 = tag.getInt("read2");
        int r3 = tag.getInt("read3");
        int r4 = tag.getInt("read4");
        this.read = r1 != -1 ? dirs[r1] : null;
        this.read2 = r2 != -1 ? dirs[r2] : null;
        this.read3 = r3 != -1 ? dirs[r3] : null;
        this.read4 = r4 != -1 ? dirs[r4] : null;

        int w1 = tag.getInt("write1");
        int w2 = tag.getInt("write2");
        write = w1 != -1 ? dirs[w1] : null;
        write2 = w2 != -1 ? dirs[w2] : null;

        isOmniSided = tag.getBoolean("omni");

        pointoffsetx = tag.getInt("pox");
        pointoffsety = tag.getInt("poy");
        pointoffsetz = tag.getInt("poz");
    }

    public final Direction getReadDirection() {
        return read;
    }

    public final Direction getReadDirection2() {
        return read2;
    }

    public final Direction getReadDirection3() {
        return read3;
    }

    public final Direction getReadDirection4() {
        return read4;
    }

    public final Direction getWriteDirection() {
        return write;
    }

    public final Direction getWriteDirection2() {
        return write2;
    }

    protected boolean isProvider(BlockEntity te) {
        if (te instanceof ShaftPowerEmitter)
            return true;
        if (!(te instanceof BlockEntityIOMachine))
            return false;
        return ((BlockEntityIOMachine) te).canProvidePower();
    }

    public final BlockEntity getReadBlockEntity() {
        return read != null ? getAdjacentBlockEntity(read) : null;
    }

    public final BlockEntity getReadBlockEntity2() {
        return read2 != null ? getAdjacentBlockEntity(read2) : null;
    }

    public final BlockEntity getReadBlockEntity3() {
        return read3 != null ? getAdjacentBlockEntity(read3) : null;
    }

    public final BlockEntity getReadBlockEntity4() {
        return read4 != null ? getAdjacentBlockEntity(read4) : null;
    }

    public final BlockEntity getWriteBlockEntity() {
        return write != null ? getAdjacentBlockEntity(write) : null;
    }

    public final BlockEntity getWriteBlockEntity2() {
        return write2 != null ? getAdjacentBlockEntity(write2) : null;
    }

    public final WorldLocation getReadLocation() {
        return read != null ? this.getAdjacentLocation(read) : null;
    }

    public final WorldLocation getReadLocation2() {
        return read2 != null ? this.getAdjacentLocation(read2) : null;
    }

    public final WorldLocation getReadLocation3() {
        return read3 != null ? this.getAdjacentLocation(read3) : null;
    }

    public final WorldLocation getReadLocation4() {
        return read4 != null ? this.getAdjacentLocation(read4) : null;
    }

    public final WorldLocation getWriteLocation() {
        return write != null ? this.getAdjacentLocation(write) : null;
    }

    public final WorldLocation getWriteLocation2() {
        return write2 != null ? this.getAdjacentLocation(write2) : null;
    }

    public abstract boolean canProvidePower();

    public final int getPointingOffsetX() {
        return pointoffsetx;
    }

    public final int getPointingOffsetY() {
        return pointoffsety;
    }

    public final int getPointingOffsetZ() {
        return pointoffsetz;
    }

    protected final void setPointingOffset(BlockPos pos) {
        pointoffsetx = pos.getX();
        pointoffsety = pos.getY();
        pointoffsetz = pos.getZ();
    }

    public final boolean isWritingToCoordinate(BlockPos pos) {
        if (write == null)
            return false;
        boolean wx = pos.getX() + write.getStepX() == pos.getX();
        boolean wy = pos.getY() + write.getStepY() == pos.getY();
        boolean wz = pos.getZ() + write.getStepZ() == pos.getZ();
        return wx && wy && wz;
    }

    public final boolean isWritingToCoordinate2(BlockPos pos) {
        if (write2 == null)
            return false;
        boolean wx = pos.getX() + write.getStepX() == pos.getX();
        boolean wy = pos.getY() + write.getStepY() == pos.getY();
        boolean wz = pos.getZ() + write.getStepZ() == pos.getZ();
        return wx && wy && wz;
    }

    protected final boolean matchTile(PowerSourceTracker te, Direction dir) {
        if (dir == null)
            return false;
        ResourceKey<Level> dim = level.dimension();
        int tx = worldPosition.getX() + te.getIoOffsetPos().getX();
        int ty = worldPosition.getY() + te.getIoOffsetPos().getY();
        int tz = worldPosition.getZ() + te.getIoOffsetPos().getZ();
        BlockEntity adjacentBlockEntity = this.getAdjacentBlockEntity(dir);
        /*while (adjacentBlockEntity instanceof WorldRift) {
            adjacentBlockEntity = ((WorldRift) adjacentBlockEntity).getTileEntityFrom(dir);
        }*/
        if (adjacentBlockEntity == null)
            return false;
//        RotaryCraft.LOGGER.info(adjacentBlockEntity.getBlockPos().getZ() + " " + tz);
        return !adjacentBlockEntity.isRemoved()
                && adjacentBlockEntity.getLevel().dimension() == dim
                && adjacentBlockEntity.getBlockPos().getX() == tx
                && adjacentBlockEntity.getBlockPos().getY() == ty
                && adjacentBlockEntity.getBlockPos().getZ() == tz;
    }

    public boolean isWritingTo(PowerSourceTracker te) {
        if (write == null || !(te instanceof BlockEntity))
            return false;
        BlockPos targetPos = ((BlockEntity) te).getBlockPos();
        BlockPos writePos = this.worldPosition.relative(write);
        return writePos.equals(targetPos);
    }

    public final boolean isWritingTo2(PowerSourceTracker te) {
        if (write2 == null || !(te instanceof BlockEntity))
            return false;
        BlockPos targetPos = ((BlockEntity) te).getBlockPos();
        BlockPos writePos = this.worldPosition.relative(write2);
        return writePos.equals(targetPos);
    }

    public final boolean isReadingFrom(PowerSourceTracker te) {
        return this.matchTile(te, read);
    }

    public final boolean isReadingFrom2(PowerSourceTracker te) {
        return this.matchTile(te, read2);
    }

    public final boolean isReadingFrom3(PowerSourceTracker te) {
        return this.matchTile(te, read3);
    }

    public final boolean isReadingFrom4(PowerSourceTracker te) {
        return this.matchTile(te, read4);
    }

    protected final void copyStandardPower(BlockEntity te) {
        this.copyStandardPower((BlockEntityIOMachine) te);
    }

    protected final void copyStandardPower(BlockEntityIOMachine te) {
        if (te instanceof BlockEntityShaft) {
            return;
        }
//        RotaryCraft.LOGGER.info(!te.isWritingTo(this) && !te.isWritingTo2(this));
        if (!te.isWritingTo(this) && !te.isWritingTo2(this)) {
            omegain = 0;
            torquein = 0;
            return;
        }
        torquein = te.torque;
        omegain = te.omega;
    }

    private void setPower(BlockEntity te, Direction from, int om, int tq) {
        if (te instanceof SimpleShaftPowerReceiver) {
            if (this.isBlacklistedReceiver(te)) {
                if (omega > 0 && torque > 0)
                    this.affectBlacklistedReceiver(te);
            } else {
                SimpleShaftPowerReceiver sp = (SimpleShaftPowerReceiver) te;
                sp.setPowered(sp.canReadFrom(from.getOpposite()) && tq > 0 && om > 0);
            }
        } else if (te instanceof ShaftPowerReceiver) {
            if (this.isBlacklistedReceiver(te)) {
                if (omega > 0 && torque > 0)
                    this.affectBlacklistedReceiver(te);
            } else {
                ShaftPowerReceiver sp = (ShaftPowerReceiver) te;
                if (sp.isReceiving() && sp.canReadFrom(from.getOpposite())) {
                    sp.setOmega(om);
                    sp.setTorque(tq);
                    sp.setPower((long) om * (long) tq);
                } else {
                    sp.setOmega(0);
                    sp.setTorque(0);
                    sp.setPower(0);
                }
            }
        } else if (te instanceof AdvancedShaftPowerReceiver) {
            if (this.isBlacklistedReceiver(te)) {
                if (omega > 0 && torque > 0)
                    this.affectBlacklistedReceiver(te);
            } else {
                AdvancedShaftPowerReceiver sp = (AdvancedShaftPowerReceiver) te;
                if (sp.canReadFrom(from.getOpposite())) {
                    sp.addPower(tq, om, (long) tq * (long) om, from.getOpposite());
                }
            }
        }
    }

    protected final void basicPowerReceiver() {
        this.writeToReceiver(write);
    }

    private void writeToReceiver(Direction dir) {
        this.writeToReceiver(dir, omega, torque);
    }

    private void writeToReceiver(Direction dir, int om, int tq) {
        BlockEntity te = getAdjacentBlockEntity(dir);
        this.setPower(te, dir, om, tq);
    }

    protected void writeToPowerReceiver(Direction dir, int om, int tq) {
        this.writeToReceiver(dir, om, tq);
    }

    private void writePowerToReciever(ShaftPowerReceiver sp) {

    }

    private boolean isBlacklistedReceiver(BlockEntity te) {
        return RotaryAux.isBlacklistedIOMachine(te);
    }

    private void affectBlacklistedReceiver(BlockEntity te) {
        if (te.getLevel() != null) {
            te.getLevel().setBlock(new BlockPos(te.getBlockPos().getX(), te.getBlockPos().getY(), te.getBlockPos().getZ()), Blocks.AIR.defaultBlockState(), 0);
            te.getLevel().explode(null, te.getBlockPos().getX(), te.getBlockPos().getY(), te.getBlockPos().getZ(), 3, Level.ExplosionInteraction.BLOCK);
        }
    }


    public final Direction getInputDirection() {
        return read;
    }

    @Override
    public BlockPos getIoOffsetPos() {
        return new BlockPos(pointoffsetx, pointoffsety, pointoffsetz);
    }

    @Override
    public BlockPos getWritePos() {
        return new BlockPos(
                write != null ? worldPosition.getX() + write.getStepX() : Integer.MIN_VALUE,
                write != null ? worldPosition.getY() + write.getStepY() : Integer.MIN_VALUE,
                write != null ? worldPosition.getZ() + write.getStepZ() : Integer.MIN_VALUE);
    }

    @Override
    public BlockPos getWritePos2() {
        return new BlockPos(
                write2 != null ? worldPosition.getX() + write2.getStepX() : Integer.MIN_VALUE,
                write2 != null ? worldPosition.getY() + write2.getStepY() : Integer.MIN_VALUE,
                write2 != null ? worldPosition.getZ() + write2.getStepZ() : Integer.MIN_VALUE);
    }

    public boolean canTransmitPower() {
        return true;
    }

    public void onRotate() {

    }
}
