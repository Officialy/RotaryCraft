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
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.common.NeoForge;
import reika.dragonapi.libraries.mathsci.ReikaEngLibrary;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.mathsci.ReikaPhysicsHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.api.interfaces.ComplexIO;
import reika.rotarycraft.api.power.PowerGenerator;
import reika.rotarycraft.api.power.PowerTracker;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.api.power.ShaftPowerEmitter;
import reika.rotarycraft.api.event.FlywheelFailureEvent;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.TorqueUsage;
import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import reika.rotarycraft.base.blockentity.BlockEntityTransmissionMachine;
import reika.rotarycraft.registry.*;

import java.util.Collection;

public class BlockEntityFlywheel extends BlockEntityTransmissionMachine implements SimpleProvider, PowerGenerator, ShaftMerger {

    public static final int MINTORQUERATIO = 4;
    public boolean failed = false;
    private int decayTime;
    private int maxtorque;
    private int soundtick = 0;

    private int oppTorque = 0;
    private int updateticks = 0;

    private Flywheels type = Flywheels.WOOD;

    public BlockEntityFlywheel(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.FLYWHEEL.get(), pos, state);
    }

    public int getOppTorque(BlockEntityFlywheel reading) {
        if (reading.getTypeOrdinal().ordinal() < this.getTypeOrdinal().ordinal())
            return this.getMinTorque() * MINTORQUERATIO;
        return omega < omegain - omegain / 20 ? torque + oppTorque : oppTorque;
    }

    public int getMinTorque() {
        return this.getTypeOrdinal().getMinTorque();
    }

    public void testFailure() {
        double factor = 0.25 * Math.sqrt(omega);
        switch (this.getTypeOrdinal().ordinal()) {
            case 1:
                factor /= 2.5;
            case 3:
                factor *= 1.25;
        }
        factor *= ReikaMathLibrary.doubpow(omega / 65536D, 1.5); //to reduce damage
        double energy = ReikaEngLibrary.rotenergy(this.getDensity(), 0.25, omega, 0.75);
        //ReikaJavaLibrary.pConsole(ReikaPhysicsHelper.getExplosionFromEnergy(energy/factor)+"  fails: "+ReikaEngLibrary.mat_rotfailure(this.getDensity(), 0.75, omega, 100*this.getStrength()));
        if (ReikaEngLibrary.mat_rotfailure(this.getDensity(), 0.75, omega, 100 * this.getStrength()))
            this.fail(level, worldPosition, energy / factor);
    }

    private void fail(Level world, BlockPos pos, double e) {
        failed = true;
        float f = ReikaPhysicsHelper.getExplosionFromEnergy(e);
        if (!world.isClientSide)
            world.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), f, ConfigRegistry.BLOCKDAMAGE.getState() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE);
        NeoForge.EVENT_BUS.post(new FlywheelFailureEvent(this, f));
    }

    private double getDensity() {
        return this.getTypeOrdinal().density;
    }

    public Flywheels getTypeOrdinal() {
        return type;
    }

    private double getStrength() {
        return this.getTypeOrdinal().tensileStrength;
    }

//    public int frictionLosses(int speed) {
//        int fric = RotaryConfig.friction;
//        return (speed * fric);
//    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.HSLA_FLYWHEEL.get(); //todo other flywheels
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.loadType();
//        this.getIOSides(world, pos);
        if (failed) {
            omega = 0;
            torque = 0;
            power = 0;
            return;
        }
        if (read != null && write != null)
            this.process(world, pos);
        power = (long) omega * torque;
        this.testFailure();
        this.playSounds();

        this.basicPowerReceiver();
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    private void playSounds() {
        if (omega == 0) {
            soundtick = 20000;
            return;
        }
        float pitch = (float) Math.sqrt(omega / 1024F);
        if (pitch == 0)
            pitch = 0.01F;
        soundtick++;
        if (soundtick < -3F / (pitch * pitch) + 69 / (pitch))
            return;
        soundtick = 0;
        SoundRegistry.FLYWHEEL.playSoundAtBlock(level, worldPosition, RotaryAux.isMuffled(this) ? 0.3F : 1, pitch);
    }

    private void loadType() {
        maxtorque = this.getTypeOrdinal().maxTorque;
        decayTime = this.getTypeOrdinal().decayTime;
    }

    public void getIOSides(Level world, BlockPos pos, int metadata) {
        switch (metadata % 4) {
            case 0:
                read = Direction.WEST;
                break;
            case 1:
                read = Direction.EAST;
                break;
            case 2:
                read = Direction.NORTH;
                break;
            case 3:
                read = Direction.SOUTH;
                break;
        }
        write = read.getOpposite();
    }

    public void process(Level world, BlockPos pos) {
        omegain = 0;
        tickcount++;
        boolean isCentered = pos.getX() == worldPosition.getX() && pos.getY() == worldPosition.getY() && pos.getZ() == worldPosition.getZ();
        int dx = pos.getX() + read.getStepX();
        int dy = pos.getY() + read.getStepY();
        int dz = pos.getZ() + read.getStepZ();
        MachineRegistry m = isCentered ? this.getMachine(read) : MachineRegistry.getMachine(world, dx, dy, dz);
        BlockEntity te = isCentered ? getAdjacentBlockEntity(read) : world.getBlockEntity(new BlockPos(dx, dy, dz));
        if (this.isProvider(te)) {
            if (m == MachineRegistry.WOOD_SHAFT || m ==
                    MachineRegistry.STONE_SHAFT || m ==
                    MachineRegistry.HSLA_SHAFT || m ==
                    MachineRegistry.TUNGSTEN_SHAFT || m ==
                    MachineRegistry.DIAMOND_SHAFT || m ==
                    MachineRegistry.BEDROCK_SHAFT) {
                BlockEntityShaft devicein = (BlockEntityShaft) te;
                if (devicein.isCross()) {
                    omegain = this.readFromCross(devicein, false);
                    torquein = this.readFromCross(devicein, true);
                } else if (devicein.isWritingTo(this)) {
                    omegain = devicein.omega;
                    torquein = devicein.torque;
                }
            }
            if (te instanceof SimpleProvider) {
                this.copyStandardPower(te);
            }
            if (te instanceof ComplexIO pwr) {
                Direction dir = this.getInputDirection().getOpposite();
                omegain = pwr.getSpeedToSide(dir);
                torquein = pwr.getTorqueToSide(dir);
            }
            if (te instanceof ShaftPowerEmitter sp) {
                if (sp.isEmitting() && sp.canWriteTo(read.getOpposite())) {
                    torquein = sp.getTorque();
                    omegain = sp.getOmega();
                }
            }
            if (m == MachineRegistry.SPLITTER) {
                BlockEntitySplitter devicein = (BlockEntitySplitter) te;
                if (devicein.isSplitting()) {
                    int om = omega;
                    int tq = torque;
                    this.readFromSplitter(world, pos, devicein);
                    omegain = omega;
                    torquein = torque;
                    omega = om;
                    torque = tq;
                    return;
                } else if (devicein.isWritingTo(this)) {
                    omegain = devicein.omega;
                    torquein = devicein.torque;
                }
            }
        } /*else if (te instanceof WorldRift) {
            WorldRift sr = (WorldRift) te;
            WorldLocation loc = sr.getLinkTarget();
            if (loc != null)
                this.process(loc.getWorld(), loc.pos.getX(), loc.pos.getY(), loc.pos.getZ());
        }*/ else {
            //omega = 0;
            //torque = 0;
            //power = 0;
            //return;
            //why was this here?!
        }
        double r = 0.75;  //this calculates the flywheel datas. You already assumed r=0.75 in previous formulas, so I used that. I set t=0.4 from the model in-game
        double t = 0.25; //thickness is closer to 0.25m for the actual main disk
        double mass = (t * r * r * Math.PI) * this.getDensity();
        double iner = mass * r * r / 2; //standard inertial moment formula for a cylinder with its rotor on the central axis
        updateticks = 0;
        if (torquein >= this.getMinTorque()) {
            oppTorque = TorqueUsage.getTorque(this);
            if (oppTorque <= torquein) {
                if (omega <= omegain) {
                    if ((torquein - oppTorque) / iner < 1 && (torquein - oppTorque) > 0) { //making up for the fact that numbers are integers
                        int i = 1;
                        while ((((double) torquein - oppTorque) / iner * i) < 1) {
                            i++;
                        }
                        updateticks = i;
                        if (tickcount % updateticks == 0) {
                            omega++;
                            tickcount = 0;
                        }
                    } else {
                        omega += (torquein - oppTorque) / iner; //increasing omega, following the formula torque=inertia*ang.acceleration
                    }
                    if (omega > omegain)
                        omega = omegain; //to prevent oscillations once reached the input value
                } else {
                    if ((torquein + oppTorque) / iner < 1) { //same as before, but to reduce omega if it's greater than the input omega
                        int i = 1;
                        while ((((double) torquein + oppTorque) / iner * i) < 1) {
                            i++;
                        }
                        updateticks = i;
                        if (tickcount % updateticks == 0) {
                            omega--;
                            tickcount = 0;
                        }
                    } else
                        omega = (int) Math.max(0, omega - (torquein + oppTorque) / iner);
                }
                torque = Math.min(torquein, maxtorque);
            } else {
                if ((oppTorque - torquein) / iner < 1) { //this applies the same formula to reduce omega in the case the input is smaller than the required output
                    int i = 1;
                    while (((oppTorque - torquein) / iner * i) < 1) {
                        i++;
                    }
                    updateticks = i;
                    if (tickcount % updateticks == 0) {
                        omega--;
                        tickcount = 0;
                    }
                } else
                    omega = (int) Math.max(0, omega - (oppTorque - torquein) / iner);
                if (omega < 0)
                    omega = 0;
            }
        } else {
            if (omega == 0) {
                torque = 0;
                tickcount = 0;
            } else { //same as before, but without input
                oppTorque = TorqueUsage.getTorque(this);
                if (oppTorque / iner < 1 && oppTorque > 0) {
                    int i = 1;
                    while ((oppTorque / iner * i) < 1) {
                        i++;
                    }
                    updateticks = i;
                    if (tickcount % updateticks == 0) {
                        omega--;
                    }
                } else
                    omega = (int) Math.max(0, omega - oppTorque / iner);
                if (omega < 0)
                    omega = 0;
            }
        }
        if (omega <= 0)
            torque = 0;
    }

    private void decrSpeed() {
        if (omega > 0 && tickcount >= decayTime) {
            omega--;
            tickcount = 0;
        }
    }

    public int readFromCross(BlockEntityShaft cross, boolean isTorque) {
        if (cross.isWritingTo(this)) {
            if (isTorque)
                return cross.readtorque[0];
            return cross.readomega[0];
        } else if (cross.isWritingTo2(this)) {
            if (isTorque)
                return cross.readtorque[1];
            return cross.readomega[1];
        } else
            return 0; //not its output
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putBoolean("failed", failed);

        tag.putInt("typeIdx", type.ordinal());
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        failed = tag.getBoolean("failed");

        type = Flywheels.list[tag.getInt("typeIdx")];
    }

    @Override
    protected String getTEName() {
        return null;
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
        return MachineRegistry.FLYWHEEL;
    }

    //    @Override
    public int getRedstoneOverride() {
        return 15 * omega / this.getTypeOrdinal().maxSpeed;
    }

    @Override
    public void onEMP() {
    }

    @Override
    public PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
        /*return new PowerSourceList().addSource(this);*/
        PowerSourceList pwr = new PowerSourceList();
        pwr.addAll(PowerSourceList.getAllFrom(level, read, new BlockPos(worldPosition.getX() + read.getStepX(), worldPosition.getY() + read.getStepY(), worldPosition.getZ() + read.getStepZ()), this, caller));
        if (pwr.isEmpty())
            pwr.addSource(this);
        return pwr;
    }

    @Override
    public long getMaxPower() {
        return (long) maxtorque * omega;
    }

    @Override
    public long getCurrentPower() {
        return power;
    }

    @Override
    public BlockPos getEmittingPos(BlockPos pos) {
        return new BlockPos(pos.getX() + write.getStepX(), pos.getY() + write.getStepY(), pos.getZ() + write.getStepZ());
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public void onPowerLooped(PowerTracker pwr) {
        omega = torque = 0;
        power = 0;
    }

    @Override
    public void getAllOutputs(Collection<BlockEntity> c, Direction dir) {
        if (dir == read)
            c.add(getAdjacentBlockEntity(write));
    }

    @Override
    public void fail() {
        this.fail(level, worldPosition, ReikaPhysicsHelper.getEnergyFromExplosion(4));
    }

//    public void setMaterialFromItem(ItemStack is) {
//        type = Flywheels.getMaterialFromFlywheelItem(is);
//        if (level != null)
//            this.syncAllData(true);
//    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }
}
