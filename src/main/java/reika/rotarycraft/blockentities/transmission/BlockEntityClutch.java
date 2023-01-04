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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.api.interfaces.ComplexIO;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.api.power.ShaftPowerEmitter;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import reika.rotarycraft.base.blockentity.BlockEntity1DTransmitter;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

import java.util.Collection;

public class BlockEntityClutch extends BlockEntity1DTransmitter {

    public boolean needsRedstone = true;

    public BlockEntityClutch(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.CLUTCH.get(), pos, state);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
//        this.getIOSides(world, pos, true);
        this.transferPower(world, pos);
        power = (long) omega * (long) torque;

        this.basicPowerReceiver();
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected void transferPower(Level world, BlockPos pos) {
        omegain = torquein = 0;
        boolean isCentered = pos.getX() == worldPosition.getX() && pos.getY() == worldPosition.getY() && pos.getZ() == worldPosition.getZ();
        int dx = pos.getX() + read.getStepX();
        int dy = pos.getY() + read.getStepY();
        int dz = pos.getZ() + read.getStepZ();
        MachineRegistry m = isCentered ? this.getMachine(read) : MachineRegistry.getMachine(world, dx, dy, dz);
        BlockEntity te = isCentered ? getAdjacentBlockEntity(read) : world.getBlockEntity(new BlockPos(dx, dy, dz));
        if (this.isOutputEnabled()) {
            if (this.isProvider(te)) {
                if (m == MachineRegistry.SHAFT) {
                    BlockEntityShaft devicein = (BlockEntityShaft) te;
                    if (devicein.isCross()) {
                        this.readFromCross(devicein);
                        return;
                    } else if (devicein.isWritingTo(this)) {
                        torquein = devicein.torque;
                        omegain = devicein.omega;
                    }
                }
                if (te instanceof SimpleProvider) {
                    this.copyStandardPower(te);
                }
                if (te instanceof ComplexIO) {
                    ComplexIO pwr = (ComplexIO) te;
                    Direction dir = this.getInputDirection().getOpposite();
                    omegain = pwr.getSpeedToSide(dir);
                    torquein = pwr.getTorqueToSide(dir);
                }
                if (te instanceof ShaftPowerEmitter) {
                    ShaftPowerEmitter sp = (ShaftPowerEmitter) te;
                    if (sp.isEmitting() && sp.canWriteTo(read.getOpposite())) {
                        torquein = sp.getTorque();
                        omegain = sp.getOmega();
                    }
                }
                if (m == MachineRegistry.SPLITTER) {
                    BlockEntitySplitter devicein = (BlockEntitySplitter) te;
                    if (devicein.isSplitting()) {
                        this.readFromSplitter(world, pos, devicein);
                        torquein = torque;
                        omegain = omega;
                        return;
                    } else if (devicein.isWritingTo(this)) {
                        torquein = devicein.torque;
                        omegain = devicein.omega;
                    }
                }
                omega = omegain;
                torque = torquein;
            } //else if (te instanceof WorldRift) {
//                WorldRift sr = (WorldRift) te;
//                WorldLocation loc = sr.getLinkTarget();
//                if (loc != null)
//                    this.transferPower(loc.getWorld(), loc.xCoord, loc.yCoord, loc.zCoord, meta);
            //    }
            else {
                omega = 0;
                torque = 0;
                power = 0;
                return;
            }
        } else {
            omega = torque = 0;
        }
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

//    @Override
//    protected void animateWithTick(Level world, BlockPos pos) {
//        if (!this.isInWorld()) {
//            phi = 0;
//            return;
//        }
//        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
//    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.CLUTCH;
    }

    public boolean isOutputEnabled() {
        return this.getLevel().hasNeighborSignal(worldPosition) == needsRedstone;
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);

        tag.putBoolean("redstone", needsRedstone);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);

        needsRedstone = tag.getBoolean("redstone");
    }

    @Override
    protected String getTEName() {
        return "clutch";
    }

    @Override
    public PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
        return this.isOutputEnabled() ? super.getPowerSources(io, caller) : new PowerSourceList();
    }

    @Override
    public void getAllOutputs(Collection<BlockEntity> c, Direction dir) {
        if (this.isOutputEnabled())
            super.getAllOutputs(c, dir);
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
