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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.exception.WTFException;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.dragonapi.libraries.java.ReikaArrayHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.ComplexIO;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.api.power.ShaftPowerEmitter;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import reika.rotarycraft.auxiliary.interfaces.TransmissionReceiver;
import reika.rotarycraft.blockentities.transmission.BlockEntityShaft;
import reika.rotarycraft.blockentities.transmission.BlockEntitySplitter;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.PowerReceivers;

import java.util.Collection;

public abstract class BlockEntityPowerReceiver extends BlockEntityIOMachine {

    public final long MINPOWER;
    public final int MINTORQUE;
    public final int MINSPEED;
    private final long[][] powerin = new long[6][3]; //stores P, T, omega
    public PowerReceivers machine;
    private long prevpower;

    public BlockEntityPowerReceiver(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
//        ReikaJavaLibrary.pConsole(this.getClass() + " goes to " + this.getMachineIndex());
        machine = PowerReceivers.getEnumFromMachineIndex(this.getMachineIndex());
        if (machine == null) {
            MINPOWER = 0;
            MINSPEED = 0;
            MINTORQUE = 0;
            return;//throw new RuntimeException("Machine "+this.getName()+" in "+this.getClass()+" has no enum! Case?");
        }
        if (!machine.hasMultiValuedPower()) {
            MINPOWER = machine.getMinPower();
            MINSPEED = machine.getMinSpeed();
            MINTORQUE = machine.getMinTorque();
        } else {
            MINPOWER = 0;
            MINSPEED = 0;
            MINTORQUE = 0;
        }
    }

    public final long getScaledOmega(int a) {
        return MINSPEED > 0 ? Math.min(a, a * omega / MINSPEED) : (omega > 0 ? a : 0);
    }

    public final long getScaledTorque(int a) {
        return MINTORQUE > 0 ? Math.min(a, a * torque / MINTORQUE) : (torque > 0 ? a : 0);
    }

    public final long getScaledPower(int a) {
        return MINPOWER > 0 ? Math.min(a, a * power / MINPOWER) : (power > 0 ? a : 0);
    }

    @Override
    public void updateBlockEntity() {
        super.updateBlockEntity();
        if (MINPOWER == -1) {
            RotaryCraft.LOGGER.error(this.getName() + " has not registered its power!");
            ReikaChatHelper.write(this.getName() + " has not registered its power!");
        }
    }

    private void clear() {
        for (int i = 0; i < powerin.length; i++)
            for (int j = 0; j < 3; j++)
                powerin[i][j] = 0;
    }

    public long[] returnHighest() {
        long[] val = new long[3];
        for (long[] longs : powerin) {
            if (longs[0] > val[0]) { //decide based on max power
                System.arraycopy(longs, 0, val, 0, 3);
            }
        }
        return val;
    }

    private void readFromCross(BlockEntityShaft cross, int slot) {
        //ReikaChatHelper.writeInt(slot+400);
        if (cross.isWritingTo(this)) {
            //ReikaChatHelper.writeInt(cross.readomega[0]);
            powerin[slot][2] = cross.readomega[0];
            powerin[slot][1] = cross.readtorque[0];
            powerin[slot][0] = powerin[slot][1] * powerin[slot][2];
        } else if (cross.isWritingTo2(this)) {
            powerin[slot][2] = cross.readomega[1];
            powerin[slot][1] = cross.readtorque[1];
            powerin[slot][0] = powerin[slot][1] * powerin[slot][2];
        } else
            return; //not its output
    }

    protected void readFromSplitter(Level world, BlockPos pos, BlockEntitySplitter spl, int slot) { //Complex enough to deserve its own function
        int ratio = spl.getRatioFromMode();
        if (ratio == 0)
            return;
        boolean favorbent = false;
        if (ratio < 0) {
            favorbent = true;
            ratio = -ratio;
        }
        if (pos == spl.getWritePos() && pos == spl.getWritePos2()) { //We are the inline
            powerin[slot][2] = spl.omega; //omega always constant
            if (ratio == 1) { //Even split, favorbent irrelevant
                powerin[slot][1] = spl.torque / 2;
                powerin[slot][0] = spl.omega * spl.torque / 2;
                return;
            }
            if (favorbent) {
                powerin[slot][1] = spl.torque / ratio;
            } else {
                powerin[slot][1] = (int) (spl.torque * ((ratio - 1D) / (ratio)));
            }
            powerin[slot][0] = powerin[slot][1] * powerin[slot][2];
        } else if (pos == spl.getWritePos2()) { //We are the bend
            powerin[slot][2] = spl.omega; //omega always constant
            if (ratio == 1) { //Even split, favorbent irrelevant
                powerin[slot][1] = spl.torque / 2;
                powerin[slot][0] = spl.omega * spl.torque / 2;
                return;
            }
            if (favorbent) {
                powerin[slot][1] = (int) (spl.torque * ((ratio - 1D) / (ratio)));
            } else {
                powerin[slot][1] = spl.torque / ratio;
            }
            powerin[slot][0] = powerin[slot][1] * powerin[slot][2];
        } else { //We are not one of its write-to blocks
            powerin[slot][0] = 0;
            powerin[slot][1] = 0;
            powerin[slot][2] = 0;
        }
    }

    protected final void getPower(boolean doubleSided) {
        this.getPower(level, worldPosition, doubleSided);
    }

    private void getPower(Level world, BlockPos pos, boolean doubleSided) {
        if (level.isClientSide() && !RotaryAux.getPowerOnClient)
            return;
        this.clear();
        boolean isCentered = worldPosition.getX() == pos.getX() && worldPosition.getY() == pos.getY() && worldPosition.getZ() == pos.getZ();
        if (read == null){
            //RotaryCraft.LOGGER.error("BlockEntity "+pos+" has no read direction!" + " The block at this position will not function."); //todo comment out on release? or make it debug only?
            return;
        }
        int dx = pos.getX() + read.getStepX();
        int dy = pos.getY() + read.getStepY();
        int dz = pos.getZ() + read.getStepZ();
        MachineRegistry m = isCentered ? this.getMachine(read) : MachineRegistry.getMachine(world, new BlockPos(dx, dy, dz));
        BlockEntity te = isCentered ? getAdjacentBlockEntity(read) : world.getBlockEntity(new BlockPos(dx, dy, dz));
        if (this.isProvider(te)) {
            if (m == MachineRegistry.SHAFT) {
                BlockEntityShaft devicein = (BlockEntityShaft) te;
                if (devicein.isCross()) {
                    this.readFromCross(devicein, 0);
                    torquein = (int) powerin[0][1];
                    omegain = (int) powerin[0][2];
                } else if (devicein.isWritingTo(this)) {
                    torquein = devicein.torque;
                    omegain = devicein.omega;
                }
            }
            if (te instanceof ComplexIO) {
                ComplexIO pwr = (ComplexIO) te;
                Direction dir = read.getOpposite();
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
                    this.readFromSplitter(world, pos, devicein, 0);
                    torquein = (int) powerin[0][1];
                    omegain = (int) powerin[0][2];
                } else if (devicein.isWritingTo(this)) {
                    torquein = devicein.torque;
                    omegain = devicein.omega;
                } else {
                    torquein = omegain = 0;
                }
            }
            powerin[0][0] = (long) torquein * (long) omegain;
            powerin[0][1] = torquein;
            powerin[0][2] = omegain;
        } else {
            torquein = 0;
            omegain = 0;
        }

        if (!doubleSided) {
            torque = torquein;
            omega = omegain;
            power = (long) omega * (long) torque;
            if (power != prevpower) {
                prevpower = power;
            }
            return;
        }
        torquein = 0;
        omegain = 0;

        dx = pos.getX() + read2.getStepX();
        dy = pos.getY() + read2.getStepY();
        dz = pos.getZ() + read2.getStepZ();
        m = isCentered ? this.getMachine(read2) : MachineRegistry.getMachine(world, new BlockPos(dx, dy, dz));
        te = isCentered ? getAdjacentBlockEntity(read2) : world.getBlockEntity(new BlockPos(dx, dy, dz));
        if (this.isProvider(te)) {
            if (m == MachineRegistry.SHAFT) {
                BlockEntityShaft devicein = (BlockEntityShaft) te;
                if (devicein.isCross()) {
                    this.readFromCross(devicein, 1);
                    torquein = (int) powerin[1][1];
                    omegain = (int) powerin[1][2];
                }
                if (devicein.isWritingTo(this)) {
                    torquein = devicein.torque;
                    omegain = devicein.omega;
                }
            }
            if (te instanceof ComplexIO) {
                ComplexIO pwr = (ComplexIO) te;
                Direction dir = read2.getOpposite();
                omegain = pwr.getSpeedToSide(dir);
                torquein = pwr.getTorqueToSide(dir);
                //ReikaJavaLibrary.pConsole(omegain, doublesided && this.getSide() == Dist.DEDICATED_SERVER);
            }
            if (te instanceof SimpleProvider) {
                this.copyStandardPower(te);
            }
            if (te instanceof ShaftPowerEmitter) {
                ShaftPowerEmitter sp = (ShaftPowerEmitter) te;
                if (sp.isEmitting() && sp.canWriteTo(read2.getOpposite())) {
                    torquein = sp.getTorque();
                    omegain = sp.getOmega();
                }
            }
            if (m == MachineRegistry.SPLITTER) {
                BlockEntitySplitter devicein = (BlockEntitySplitter) te;
                if (devicein.isSplitting()) {
                    this.readFromSplitter(world, pos, devicein, 1);
                    torquein = (int) powerin[1][1];
                    omegain = (int) powerin[1][2];
                } else if (devicein.isWritingTo(this)) {
                    torquein = devicein.torque;
                    omegain = devicein.omega;
                } else {
                    torquein = omegain = 0;
                }
            }
            powerin[1][0] = (long) torquein * (long) omegain;
            powerin[1][1] = torquein;
            powerin[1][2] = omegain;
        } else {
            torquein = 0;
            omegain = 0;
        }
        long[] powers = this.returnHighest();
        torque = (int) powers[1];
        omega = (int) powers[2];
        power = (long) torque * (long) omega;
        if (power != prevpower) {
            prevpower = power;
        }
    }

    protected final void getOffsetPower4Sided(int stepx, int stepy, int stepz, boolean vertical) {
        this.getOffsetPower4Sided(level, worldPosition, stepx, stepy, stepz, vertical);
    }

    private void getOffsetPower4Sided(Level world, BlockPos pos, int stepx, int stepy, int stepz, boolean vertical) {
        if (level.isClientSide && !RotaryAux.getPowerOnClient)
            return;
        this.setPointingOffset(new BlockPos(stepx, stepy, stepz));

        boolean isCentered = worldPosition.getX() == pos.getX() && worldPosition.getY() == pos.getY() && worldPosition.getZ() == pos.getZ();

        isOmniSided = vertical;
        if (!isOmniSided) {
            read = Direction.EAST;
            read2 = Direction.WEST;
            read3 = Direction.SOUTH;
            read4 = Direction.NORTH;
        }
        for (int i = vertical ? 0 : 2; i < 6; i++) {
            Direction in = Direction.values()[i];
            int x1 = worldPosition.getX() + stepx + in.getStepX();
            int y1 = worldPosition.getY() + stepy + in.getStepY();
            int z1 = worldPosition.getZ() + stepz + in.getStepZ();

            MachineRegistry id1 = MachineRegistry.getMachine(world, new BlockPos(x1, y1, z1));
            BlockEntity te1 = world.getBlockEntity(new BlockPos(x1, y1, z1));

            if (this.isProvider(te1)) {
                if (id1 == MachineRegistry.SHAFT) {
                    BlockEntityShaft devicein = (BlockEntityShaft) te1;
                    if (devicein.isCross()) {
                        this.readFromCross(devicein, 0);
                        torquein = (int) powerin[0][1];
                        omegain = (int) powerin[0][2];
                    } else if (devicein.isWritingTo(this)) {
                        torquein = devicein.torque;
                        omegain = devicein.omega;
                    }
                }
                if (te1 instanceof ComplexIO) {
                    ComplexIO pwr = (ComplexIO) te1;
                    Direction dir = in.getOpposite();
                    omegain = pwr.getSpeedToSide(dir);
                    torquein = pwr.getTorqueToSide(dir);
                }
                if (te1 instanceof SimpleProvider) {
                    this.copyStandardPower(te1);
                }
                if (te1 instanceof ShaftPowerEmitter) {
                    ShaftPowerEmitter sp = (ShaftPowerEmitter) te1;
                    if (sp.isEmitting() && sp.canWriteTo(in.getOpposite())) {
                        torquein = sp.getTorque();
                        omegain = sp.getOmega();
                    }
                }
                if (id1 == MachineRegistry.SPLITTER) {
                    BlockEntitySplitter devicein = (BlockEntitySplitter) te1;
                    if (devicein.isSplitting()) {
                        this.readFromSplitter(world, pos, devicein, 0);
                        torquein = (int) powerin[0][1];
                        omegain = (int) powerin[0][2];
                    } else if (devicein.isWritingTo(this)) {
                        torquein = devicein.torque;
                        omegain = devicein.omega;
                    } else {
                        torquein = omegain = 0;
                    }
                }
            }
            powerin[i][0] = torquein * omegain;
            powerin[i][1] = torquein;
            powerin[i][2] = omegain;
            torquein = 0;
            omegain = 0;
        }

        long[] powers = this.returnHighest();
        torque = (int) powers[1];
        omega = (int) powers[2];
        power = (long) torque * (long) omega;
        if (power != prevpower) {
            prevpower = power;
        }
    }

    protected final void getPowerBelow() {
        read = Direction.DOWN;
        this.getPower(false);
    }

    protected final void getPowerAbove() {
        read = Direction.UP;
        this.getPower(false);
    }

    protected final void getSummativeSidedPower() {
        isOmniSided = true;
        if (level.isClientSide && !RotaryAux.getPowerOnClient)
            return;
        int x = worldPosition.getX();
        int y = worldPosition.getY();
        int z = worldPosition.getZ();
        long[][] powers = new long[2][6];
        if (this.getMachine().getMinY(this) == 0) {
            this.getPowerBelow();
            powers[0][0] = omega;
            powers[1][0] = torque;
        }
        if (this.getMachine().getMaxY(this) == 1) {
            this.getPowerAbove();
            powers[0][1] = omega;
            powers[1][1] = torque;
        }
        read = Direction.EAST;
        if (this.getMachine().getMaxX(this) == 1) {
            this.getPower(false);
            powers[0][2] = omega;
            powers[1][2] = torque;
        }
        read = Direction.WEST;
        if (this.getMachine().getMinX(this) == 0) {
            this.getPower(false);
            powers[0][3] = omega;
            powers[1][3] = torque;
        }
        read = Direction.SOUTH;
        if (this.getMachine().getMaxZ(this) == 1) {
            this.getPower(false);
            powers[0][4] = omega;
            powers[1][4] = torque;
        }
        read = Direction.NORTH;
        if (this.getMachine().getMinZ(this) == 0) {
            this.getPower(false);
            powers[0][5] = omega;
            powers[1][5] = torque;
        }
        read = null;
        torque = 0;
        omega = 0;
        power = 0;
        boolean unequal = false;
        unequal = !ReikaArrayHelper.allNonZerosEqual(powers[0]);
        if (unequal) {
            level.addParticle(ParticleTypes.CRIT, x + DragonAPI.rand.nextFloat(), y + DragonAPI.rand.nextFloat(), z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat() / 2F, DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat() / 2F);
            if (DragonAPI.rand.nextInt(5) == 0)
                level.playLocalSound(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, SoundEvents.BLAZE_HURT, SoundSource.AMBIENT, 1F, 1F, false);
            if (power != prevpower) {
                prevpower = power;
            }
            return;
        }
        int i = 0;
        while (powers[0][i] == 0 && i < 5) {
            i++;
        }
        omega = (int) powers[0][i];
        torque = (int) ReikaArrayHelper.sumArray(powers[1]);
        power = (long) omega * (long) torque;
        if (power != prevpower) {
            prevpower = power;
        }
    }

    @Override
    public boolean canProvidePower() {
        return false;
    }

    @Override
    public PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
        PowerSourceList pwr = new PowerSourceList();
        if (isOmniSided) {
            for (int i = 0; i < 6; i++) {
                Direction dir = Direction.values()[i];
                pwr.addAll(PowerSourceList.getAllFrom(level, dir, new BlockPos(worldPosition.getX() + dir.getStepX() + this.getPointingOffsetX(), worldPosition.getY() + dir.getStepY() + this.getPointingOffsetY(), worldPosition.getZ() + dir.getStepZ() + this.getPointingOffsetZ()), this, caller));
            }
        } else {
            if (read != null) {
                pwr.addAll(PowerSourceList.getAllFrom(level, read, new BlockPos(worldPosition.getX() + read.getStepX() + this.getPointingOffsetX(), worldPosition.getY() + read.getStepY() + this.getPointingOffsetY(), worldPosition.getZ() + read.getStepZ() + this.getPointingOffsetZ()), this, caller));
            }
            if (read2 != null) {
                pwr.addAll(PowerSourceList.getAllFrom(level, read2, new BlockPos(worldPosition.getX() + read2.getStepX() + this.getPointingOffsetX(), worldPosition.getY() + read2.getStepY() + this.getPointingOffsetY(), worldPosition.getZ() + read2.getStepZ() + this.getPointingOffsetZ()), this, caller));
            }
            if (read3 != null) {
                pwr.addAll(PowerSourceList.getAllFrom(level, read3, new BlockPos(worldPosition.getX() + read3.getStepX() + this.getPointingOffsetX(), worldPosition.getY() + read3.getStepY() + this.getPointingOffsetY(), worldPosition.getZ() + read3.getStepZ() + this.getPointingOffsetZ()), this, caller));
            }
            if (read4 != null) {
                pwr.addAll(PowerSourceList.getAllFrom(level, read4, new BlockPos(worldPosition.getX() + read4.getStepX() + this.getPointingOffsetX(), worldPosition.getY() + read4.getStepY() + this.getPointingOffsetY(), worldPosition.getZ() + read4.getStepZ() + this.getPointingOffsetZ()), this, caller));
            }
        }
        return pwr;
    }

    @Override
    public final void getAllOutputs(Collection<BlockEntity> c, Direction dir) {
        if (this instanceof TransmissionReceiver) {
            ((TransmissionReceiver) this).getOutputs(c, dir);
        }
    }
}
