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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.instantiable.data.immutable.WorldLocation;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.ComplexIO;
import reika.rotarycraft.api.power.ShaftPowerEmitter;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import reika.rotarycraft.base.blockentity.BlockEntity1DTransmitter;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

public class BlockEntityMonitor extends BlockEntity1DTransmitter {

    public BlockEntityMonitor(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.DYNAMOMETER.get(), pos, state);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.DYNAMOMETER.get();
    }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        power = (long) omega * (long) torque;

        this.getIOSides(world, pos, false);
        this.transferPower(world, pos);

        this.basicPowerReceiver();
    }

    @Override
    protected void transferPower(Level world, BlockPos pos) {
        if (!RotaryAux.getPowerOnClient && world.isClientSide) {
            return;
        }
        omegain = torquein = 0;
        boolean isCentered = pos.getX() == worldPosition.getX() && pos.getY() == worldPosition.getY() && pos.getZ() == worldPosition.getZ();
        int dx = pos.getZ() + read.getStepX();
        int dy = pos.getY() + read.getStepY();
        int dz = pos.getZ() + read.getStepZ();

        MachineRegistry m = isCentered ? this.getMachine(read) : MachineRegistry.getMachine(world, dx, dy, dz);
        BlockEntity te = isCentered ? getAdjacentBlockEntity(read) : world.getBlockEntity(new BlockPos(dx, dy, dz));
//        RotaryCraft.LOGGER.info(te);
        if (this.isProvider(te)) {
            if (m == MachineRegistry.WOOD_SHAFT || m ==
                    MachineRegistry.STONE_SHAFT || m ==
                    MachineRegistry.HSLA_SHAFT || m ==
                    MachineRegistry.TUNGSTEN_SHAFT || m ==
                    MachineRegistry.DIAMOND_SHAFT || m ==
                    MachineRegistry.BEDROCK_SHAFT) {
                BlockEntityShaft devicein = (BlockEntityShaft) te;
                if (devicein.isCross()) {
                    this.readFromCross(devicein);
                    return;
                } else if (devicein.isWritingToCoordinate(pos)) {
                    torquein = devicein.torque;
                    omegain = devicein.omega;
                }
            }
            if (te instanceof ComplexIO) {
                ComplexIO pwr = (ComplexIO) te;
                Direction dir = this.getInputDirection().getOpposite();
                omegain = pwr.getSpeedToSide(dir);
                torquein = pwr.getTorqueToSide(dir);
            }
            if (te instanceof SimpleProvider) {
                this.copyStandardPower(te);
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
                } else if (devicein.isWritingToCoordinate(pos)) {
                    torquein = devicein.torque;
                    omegain = devicein.omega;
                }
            }
            omega = omegain;
            torque = torquein;
        }/* else if (te instanceof WorldRift) {
            WorldRift sr = (WorldRift) te;
            WorldLocation loc = sr.getLinkTarget();
            if (loc != null)
                this.transferPower(loc.getWorld(), loc.xCoord, loc.yCoord, loc.zCoord, meta);
        } else {
            omega = torque = 0;
        }

        if (this.isProvider(te)) {
            this.processTileSimply(te, m, xCoord, yCoord, zCoord);
        } else if (te instanceof SpaceRift) {
            SpaceRift sr = (SpaceRift) te;
            WorldLocation loc = sr.getLinkTarget();
            BlockEntity other = sr.getBlockEntityFrom(read);
            this.processTileSimply(other, MachineRegistry.getMachine(loc.move(read, 1)), loc.xCoord, loc.yCoord, loc.zCoord);
        } else {
            omega = torque = 0;
        }*/

    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.25);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.DYNAMOMETER;
    }

    public int getRedstoneOverride() {
        return power > 0 ? Math.max(1, Math.min(15, (int) (15L * power / 1048576))) : 0;
    }

    @Override
    protected String getTEName() {
        return "dynamometer";
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
