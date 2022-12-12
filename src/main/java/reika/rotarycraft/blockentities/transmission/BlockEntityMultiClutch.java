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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.interfaces.blockentity.GuiController;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.ComplexIO;
import reika.rotarycraft.api.power.ShaftPowerEmitter;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import reika.rotarycraft.base.blockentity.BlockEntity1DTransmitter;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

public class BlockEntityMultiClutch extends BlockEntity1DTransmitter implements GuiController {

    private int redLevel;

    private int[] control = new int[16];

    public BlockEntityMultiClutch(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.MULTI_CLUTCH.get(), pos, state);
    }

    @Override
    protected void transferPower(Level world, BlockPos pos) {
        omegain = torquein = 0;
        boolean isCentered = worldPosition.getX() == pos.getX() && worldPosition.getY() == pos.getY() && worldPosition.getZ() == worldPosition.getZ();
        int dx = pos.getX() + read.getStepX();
        int dy = pos.getY() + read.getStepY();
        int dz = pos.getZ() + read.getStepZ();
        MachineRegistry m = isCentered ? this.getMachine(read) : MachineRegistry.getMachine(world, dx, dy, dz);
        BlockEntity te = isCentered ? getAdjacentBlockEntity(read) : world.getBlockEntity(new BlockPos(dx, dy, dz));
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
        } /*else if (te instanceof WorldRift) {
            WorldRift sr = (WorldRift) te;
            WorldLocation loc = sr.getLinkTarget();
            if (loc != null)
                this.transferPower(loc.getWorld(), loc.xCoord, loc.yCoord, loc.zCoord, meta);
        }*/ else {
            omega = 0;
            torque = 0;
            power = 0;
            return;
        }
    }

//    protected void animateWithTick(Level world, BlockPos pos) {
//        if (!this.isInWorld()) {
//            phi = 0;
//            return;
//        }
//        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
//    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.MULTICLUTCH;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        if (world.isClientSide)
            return;
        redLevel = world.getBestNeighborSignal(pos);
        this.getIOSides(world, pos);
        this.transferPower(world, pos);

        this.basicPowerReceiver();
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    private void getIOSides(Level world, BlockPos pos) {
//        switch (meta) {
//            case 0:
//                read = Direction.EAST;
//                break;
//            case 1:
//                read = Direction.WEST;
//                break;
//            case 2:
//                read = Direction.SOUTH;
//                break;
//            case 3:
//                read = Direction.NORTH;
//                break;
//        }

        switch (control[redLevel]) {
            case 0 -> write = Direction.DOWN;
            case 1 -> write = Direction.UP;
            case 2 -> write = Direction.NORTH;
            case 3 -> write = Direction.SOUTH;
            case 4 -> write = Direction.WEST;
            case 5 -> write = Direction.EAST;
        }
    }

    public void setSideOfState(int state, int side) {
        if (side >= 6) {
            RotaryCraft.LOGGER.error("INVALID SIDE " + side + "!");
        }
        if (state >= 16) {
            RotaryCraft.LOGGER.error("INVALID STATE " + state + "!");
        }
        control[state] = side;
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);

        tag.putIntArray("set", control);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);

        control = tag.getIntArray("set");
    }

    @Override
    protected String getTEName() {
        return "multiclutch";
    }

    public int getSideOfState(int state) {
        return control[state];
    }

    public int getNextSideForState(int state) {
        if (control[state] == 5)
            return 0;
        else
            return control[state] + 1;
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
