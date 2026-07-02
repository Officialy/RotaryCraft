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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.api.interfaces.ComplexIO;
import reika.rotarycraft.api.power.PowerTracker;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.api.power.ShaftPowerEmitter;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.NBTMachine;
import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;
import reika.rotarycraft.base.blockentity.BlockEntityTransmissionMachine;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

import java.util.ArrayList;
import java.util.Collection;

public class BlockEntitySplitter extends BlockEntityTransmissionMachine implements ShaftMerger, NBTMachine {

    //public int[] writeinline = new int[2]; //xz coords
    //public int[] writebend = new int[2]; //xz coords

    public boolean failed;
    public int torqueOut1;
    public int torqueOut2;
    private int torquein2;
    private int omegain2;
    private int splitmode = 1;
    private boolean processingSecondary;
    private boolean bedrock;
    private int overloadTick = 0;
    private int pow2;

    /**
     * 1.21.5 port fix: in 1.7 the splitter's I/O orientation (0-7 = merge, 8-15 = split) was
     * stored in block metadata. The port previously tried to read it as
     * {@code getUpdateTag(...).getIntOr("ioside", 0)}, but nothing ever wrote that key, so the
     * splitter was permanently stuck at ioside 0 (merge, EAST+NORTH → WEST) and
     * {@link #isSplitting} returned false forever. We now store {@code ioside} as a real
     * field, persist it through {@link #writeSyncTag} / {@link #readSyncTag}, and derive the
     * split-mode boolean from the high bit ({@code ioside >= 8}). On first tick we initialise
     * {@code ioside} from the BlockState's FACING so a fresh placement at least faces a
     * sensible direction.
     */
    private int ioside = 0;
    private boolean iosideInitialised = false;

    public BlockEntitySplitter(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.SPLITTER.get(), pos, state);
    }

    public int getRatioFromMode() {
        return splitmode;
    }

    public void setMode(int mode) {
        splitmode = mode;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.SPLITTER.get();
    }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        if (!iosideInitialised) {
            initIosideFromFacing();
            iosideInitialised = true;
        }
        this.getIOSides(world, pos, ioside);

        if (failed) {
            omega = torque = 0;
        } else {
            this.transferPower(world, pos, true, true);
        }
        power = (long) omega * (long) torque;
    }

    /**
     * Derives an initial {@link #ioside} from the block's FACING so a freshly placed splitter
     * has a usable orientation without the player having to set one. Legacy splitter has 8
     * merge configurations and 8 split configurations encoded into the 0-15 range; we pick the
     * merge case whose write-direction matches FACING:
     * <ul>
     *   <li>FACING WEST  → ioside 0 (read EAST + read2 NORTH → write WEST)</li>
     *   <li>FACING NORTH → ioside 1 (read EAST + read2 SOUTH → write NORTH)</li>
     *   <li>FACING EAST  → ioside 2 (read SOUTH + read2 WEST → write EAST)</li>
     *   <li>FACING SOUTH → ioside 3 (read NORTH + read2 WEST → write SOUTH)</li>
     *   <li>UP / DOWN    → fallback to 0 (splitter is a horizontal-only device legacy-wise)</li>
     * </ul>
     * Adding 8 to the chosen value flips it from merge → split with the same orientation.
     * {@link #setSplitting(boolean)} adjusts that flag and re-derives {@link #ioside}.
     */
    /** 26.1: public wrapper for {@link BlockSplitter#setPlacedBy} to call right after placement,
     *  so the first sync packet carries the FACING-derived ioside instead of the default 0. */
    public void initIosideFromFacingPublic() {
        initIosideFromFacing();
        iosideInitialised = true;
    }

    private void initIosideFromFacing() {
        // Defaults to merge orientation (cases 0-3); the player can flip to split (cases 8-11)
        // via {@link #setSplitting} or {@link #setIoside}.
        BlockState state = this.getBlockState();
        if (state == null || !state.hasProperty(BlockRotaryCraftMachine.FACING)) {
            return; // keep current ioside (0 by default).
        }
        ioside = switch (state.getValue(BlockRotaryCraftMachine.FACING)) {
            case WEST  -> 0;
            case NORTH -> 1;
            case EAST  -> 2;
            case SOUTH -> 3;
            default    -> 0; // UP/DOWN: legacy splitter is horizontal-only.
        };
    }

    public int getIoside() {
        return ioside;
    }

    public void setIoside(int side) {
        if (side < 0 || side >= 16) return;
        ioside = side;
        iosideInitialised = true;
        setChanged();
    }

    /** Toggle between merge and split modes while preserving orientation. */
    public void setSplitting(boolean split) {
        // ioside layout: low 3 bits = orientation, bit 3 (value 8) = split-mode flag.
        ioside = (ioside & 7) | (split ? 8 : 0);
        iosideInitialised = true;
        setChanged();
    }

    public void getIOSides(Level world, BlockPos pos, int dir) {
        switch (dir) {
            case 0 -> {//-z and +x -> -x
                read = Direction.EAST;
                read2 = Direction.NORTH;
                write = Direction.WEST;
                write2 = null;
            }
            case 1 -> { //+z and +x -> -z
                read = Direction.EAST;
                read2 = Direction.SOUTH;
                write = Direction.NORTH;
                write2 = null;
            }
            case 2 -> { //+z and -x -> +x
                read = Direction.SOUTH;
                read2 = Direction.WEST;
                write = Direction.EAST;
                write2 = null;
            }
            case 3 -> { //-z and -x -> +z
                read = Direction.NORTH;
                read2 = Direction.WEST;
                write = Direction.SOUTH;
                write2 = null;
            }
            case 4 -> {//+z and +x -> -x
                read = Direction.EAST;
                read2 = Direction.SOUTH;
                write = Direction.WEST;
                write2 = null;
            }
            case 5 -> { //+z and -x -> -z
                read = Direction.WEST;
                read2 = Direction.SOUTH;
                write = Direction.NORTH;
                write2 = null;
            }
            case 6 -> { //-z and -x -> +x
                read = Direction.NORTH;
                read2 = Direction.WEST;
                write = Direction.EAST;
                write2 = null;
            }
            case 7 -> { //-z and +x -> +z
                read = Direction.NORTH;
                read2 = Direction.EAST;
                write = Direction.SOUTH;
                write2 = null;
            }
            //---------------------------SPLITTER-----------------------------------
            case 8 -> {//-z and +x <- -x
                read2 = null;
                read = Direction.WEST;
                write = Direction.EAST;
                write2 = Direction.NORTH;
            }
            case 9 -> { //+z and +x <- -z
                read = Direction.NORTH;
                read2 = null;
                write = Direction.SOUTH;
                write2 = Direction.EAST;
            }
            case 10 -> { //+z and -x <- +x
                read = Direction.EAST;
                read2 = null;
                write = Direction.WEST;
                write2 = Direction.SOUTH;
            }
            case 11 -> { //-z and -x <- +z
                read = Direction.SOUTH;
                read2 = null;
                write = Direction.NORTH;
                write2 = Direction.WEST;
            }
            case 12 -> {//+z and +x <- -x
                read = Direction.WEST;
                read2 = null;
                write = Direction.EAST;
                write2 = Direction.SOUTH;
            }
            case 13 -> { //+z and -x <- -z
                read = Direction.NORTH;
                read2 = null;
                write = Direction.SOUTH;
                write2 = Direction.WEST;
            }
            case 14 -> { //-z and -x <- +x
                read = Direction.EAST;
                read2 = null;
                write = Direction.WEST;
                write2 = Direction.NORTH;
            }
            case 15 -> { //-z and +x <- +z
                read = Direction.SOUTH;
                read2 = null;
                write = Direction.NORTH;
                write2 = Direction.EAST;
            }
        }
		/*
		if (write != null) {
			writeinline[0] = xCoord+write.getStepX();
			writeinline[1] = zCoord+write.getStepZ();
		}
		else {
			writeinline[0] = Integer.MIN_VALUE;
			writeinline[1] = Integer.MIN_VALUE;
		}
		if (write2 != null) {
			writebend[0] = xCoord+write2.getStepX();
			writebend[1] = zCoord+write2.getStepZ();
		}
		else {
			writebend[0] = Integer.MIN_VALUE;
			writebend[1] = Integer.MIN_VALUE;
		}
		 */
        //ReikaWorldHelper.legacySetBlockWithNotify(this.level, this.writex, this.yCoord, this.writez, 44);
        //ReikaWorldHelper.legacySetBlockWithNotify(this.level, this.writex2, this.yCoord, this.writez2, 79);
        //ReikaWorldHelper.legacySetBlockWithNotify(this.level, this.readx, this.yCoord, this.readz, 20);
    }

    @Override
    protected void readFromCross(BlockEntityShaft cross) {
        if (cross.isWritingTo(this)) {
            omega = cross.readomega[0];
            torque = cross.readtorque[0];
        } else if (cross.isWritingTo2(this)) {
            omega = cross.readomega[1];
            torque = cross.readtorque[1];
        } else {
        } //not its output
    }

    private void mergeReadFromCross(BlockEntityShaft cross, int side) {
        if (cross.isWritingTo(this)) {
            if (side == 0) {
                omegain = cross.readomega[0];
                torquein = cross.readtorque[0];
            }
            if (side == 1) {
                omegain2 = cross.readomega[0];
                torquein2 = cross.readtorque[0];
            }
        } else if (cross.isWritingTo2(this)) {
            if (side == 0) {
                omegain = cross.readomega[1];
                torquein = cross.readtorque[1];
            }
            if (side == 1) {
                omegain2 = cross.readomega[1];
                torquein2 = cross.readtorque[1];
            }
        } else {
        } //not its output
    }

    protected void transferPower(Level world, BlockPos pos, boolean check1, boolean check2) {
        if (getLevel().isClientSide() && !RotaryAux.getPowerOnClient)
            return;
        if (check1 && check2)
            omegain = torquein = 0;
        boolean isCentered = pos.getX() == worldPosition.getX() && pos.getZ() == worldPosition.getY() && pos.getZ() == worldPosition.getZ();
        if (!this.isSplitting()) {
            int dx = pos.getX() + read.getStepX();
            int dy = pos.getY() + read.getStepY();
            int dz = pos.getZ() + read.getStepZ();
            int dx2 = pos.getX() + read2.getStepX();
            int dy2 = pos.getY() + read2.getStepY();
            int dz2 = pos.getZ() + read2.getStepZ();
            MachineRegistry m = isCentered ? this.getMachine(read) : MachineRegistry.getMachine(world, new BlockPos(dx, dy, dz));
            BlockEntity te = isCentered ? getAdjacentBlockEntity(read) : world.getBlockEntity(new BlockPos(dx, dy, dz));
            MachineRegistry m2 = isCentered ? this.getMachine(read2) : MachineRegistry.getMachine(world, new BlockPos(dx2, dy2, dz2));
            BlockEntity te2 = isCentered ? getAdjacentBlockEntity(read2) : world.getBlockEntity(new BlockPos(dx2, dy2, dz2));
            if (check1) {
                if (this.isProvider(te)) {
                    if (m == MachineRegistry.WOOD_SHAFT || m ==
                    MachineRegistry.STONE_SHAFT || m ==
                    MachineRegistry.HSLA_SHAFT || m ==
                    MachineRegistry.TUNGSTEN_SHAFT || m ==
                    MachineRegistry.DIAMOND_SHAFT || m ==
                    MachineRegistry.BEDROCK_SHAFT) {
                        BlockEntityShaft devicein = (BlockEntityShaft) te;
                        if (devicein.isCross()) {
                            this.mergeReadFromCross(devicein, 0);
                            //  return;
                        } else if (devicein.isWritingTo(this)) {
                            torquein = devicein.torque;
                            omegain = devicein.omega;
                        }
                    }
                    if (te instanceof SimpleProvider) {
                        BlockEntityIOMachine devicein = (BlockEntityIOMachine) te;
                        if (devicein.isWritingTo(this)) {
                            torquein = devicein.torque;
                            omegain = devicein.omega;
                        } else if (devicein.isWritingTo2(this)) {
                            torquein = devicein.torque;
                            omegain = devicein.omega;
                        } else
                            torquein = omegain = 0;
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
                        } else
                            torquein = omegain = 0;
                    }
                    if (m == MachineRegistry.SPLITTER) {
                        BlockEntitySplitter devicein = (BlockEntitySplitter) te;
                        if (devicein.isSplitting()) {
                            processingSecondary = false;
                            this.readFromSplitter(world, pos, devicein);
                        } else if (devicein.isWritingTo(this)) {
                            torquein = devicein.torque;
                            omegain = devicein.omega;
                        }
                    }
//                } else if (te instanceof WorldRift) {
//                    WorldRift sr = (WorldRift) te;
//                    WorldLocation loc = sr.getLinkTarget();
//                    if (loc != null)
//                        this.transferPower(loc.getWorld(), loc.xCoord, loc.yCoord, loc.zCoord, meta, true, false);
                } else {
                    omegain = 0;
                    torquein = 0;
                }
            }
            if (check2) {
                if (this.isProvider(te2)) {
                    if (m2 == MachineRegistry.WOOD_SHAFT || m2 == MachineRegistry.STONE_SHAFT || m2 == MachineRegistry.HSLA_SHAFT || m2 == MachineRegistry.TUNGSTEN_SHAFT || m2 == MachineRegistry.DIAMOND_SHAFT || m2 == MachineRegistry.BEDROCK_SHAFT) {
                        BlockEntityShaft devicein2 = (BlockEntityShaft) te2;
                        if (devicein2.isCross()) {
                            this.mergeReadFromCross(devicein2, 1);
                            // ReikaChatHelper.writeInt(this.omegain2);
                            // return;
                        } else if (devicein2.isWritingTo(this)) {
                            torquein2 = devicein2.torque;
                            omegain2 = devicein2.omega;
                        }
                    }
                    if (te2 instanceof SimpleProvider) {
                        BlockEntityIOMachine devicein = (BlockEntityIOMachine) te2;
                        if (devicein.isWritingTo(this)) {
                            torquein2 = devicein.torque;
                            omegain2 = devicein.omega;
                        } else
                            torquein2 = omegain2 = 0;
                    }
                    if (te2 instanceof ComplexIO pwr) {
                        Direction dir = this.getInputDirection().getOpposite();
                        omegain2 = pwr.getSpeedToSide(dir);
                        torquein2 = pwr.getTorqueToSide(dir);
                    }
                    if (te2 instanceof ShaftPowerEmitter sp) {
                        if (sp.isEmitting() && sp.canWriteTo(read2.getOpposite())) {
                            torquein2 = sp.getTorque();
                            omegain2 = sp.getOmega();
                        } else
                            torquein2 = omegain2 = 0;
                    }
                    if (m2 == MachineRegistry.SPLITTER) {
                        BlockEntitySplitter devicein2 = (BlockEntitySplitter) te2;
                        if (devicein2.isSplitting()) {
                            processingSecondary = true;
                            this.readFromSplitter(world, pos, devicein2);
                        } else if (devicein2.isWritingTo(this)) {
                            torquein2 = devicein2.torque;
                            omegain2 = devicein2.omega;
                        }
                    }
//                } else if (te2 instanceof WorldRift) {
//                    WorldRift sr = (WorldRift) te2;
//                    WorldLocation loc = sr.getLinkTarget();
//                    if (loc != null)
//                        this.transferPower(loc.getWorld(), loc.xCoord, loc.yCoord, loc.zCoord, meta, false, true);
                } else {
                    omegain2 = 0;
                    torquein2 = 0;
                }
            }
            if (!check1 || !check2)
                return;

            PowerSourceList in1 = null;
            PowerSourceList in2 = null;
            if (read != null && read2 != null) {
                //ReikaJavaLibrary.pConsole("====================", Dist.DEDICATED_SERVER, xCoord == -1011);
                in1 = PowerSourceList.getAllFrom(world, read, new BlockPos(worldPosition.getX() + read.getStepX(), worldPosition.getY() + read.getStepY(), worldPosition.getZ() + read.getStepZ()), this, this);
                //ReikaJavaLibrary.pConsole("--------------", Dist.DEDICATED_SERVER, xCoord == -1011);
                in2 = PowerSourceList.getAllFrom(world, read2, new BlockPos(worldPosition.getX() + read2.getStepX(), worldPosition.getY() + read2.getStepY(), worldPosition.getZ() + read2.getStepZ()), this, this);
                //ReikaJavaLibrary.pConsole("==================", Dist.DEDICATED_SERVER, xCoord == -1011);

                if (this.isLoopingPower(in1, in2)) {
                    omega = torque = 0;
                    power = 0;
                    this.fail();
                }
            }

            boolean overload = false;
            if (!this.canCombine(in1, in2, torquein, torquein2)) {
                overloadTick++;
                overload = true;
                if (overloadTick > 20) {
                    this.fail();
                    overloadTick = 0;
                } else {
                    world.addParticle(ParticleTypes.CRIT, pos.getX() + DragonAPI.rand.nextFloat(), pos.getY() + DragonAPI.rand.nextFloat(), pos.getZ() + DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat());
                    world.playSound(null /*todo null player bad*/, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLAZE_HURT, SoundSource.MASTER, 0.1F, 1F);
                }
            } else if (omegain == omegain2) {
                omega = omegain;
                torque = torquein + torquein2;
            } else {
                omega = omegain == 0 || omegain2 == 0 ? Math.max(omegain, omegain2) : DragonAPI.rand.nextInt(Math.max(1 + omegain, 1 + omegain2));
                if (omegain == 0 || omegain2 == 0) {
                    if (omega == omegain)
                        torque = torquein;
                    else
                        torque = torquein2;
                } else {
                    torque = DragonAPI.rand.nextInt(Math.min(1 + torquein, 1 + torquein2));
                }
                if (omegain != 0 && omegain2 != 0) {
                    world.addParticle(ParticleTypes.CRIT, pos.getX() + DragonAPI.rand.nextFloat(), pos.getY() + DragonAPI.rand.nextFloat(), pos.getZ() + DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat());
                    world.playSound(null /*todo null player bad?*/,pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLAZE_HURT, SoundSource.MASTER, 0.1F, 1F);
                }
            }
            if (!overload)
                overloadTick = 0;
            this.basicPowerReceiver();
        } else {
            //--------------------------------------------------------------------------------------------
            //------################-------Splitter mode (dmg >= 8)-------##########################------
            //--------------------------------------------------------------------------------------------
            int dx = worldPosition.getX() + read.getStepX();
            int dy = worldPosition.getY() + read.getStepY();
            int dz = worldPosition.getZ() + read.getStepZ();
            MachineRegistry m = isCentered ? this.getMachine(read) : MachineRegistry.getMachine(world, new BlockPos(dx, dy, dz));
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
                        this.readFromCross(devicein);
                        //ReikaChatHelper.writeInt(this.torque);
                        return;
                    }
                    if (devicein.isWritingTo(this)) {
                        torque = devicein.torque;
                        omega = devicein.omega;
                    } else
                        torque = omega = 0;
                }
                if (te instanceof SimpleProvider) {
                    BlockEntityIOMachine devicein = (BlockEntityIOMachine) te;
                    if (devicein.isWritingTo(this)) {
                        torque = devicein.torque;
                        omega = devicein.omega;
                    } else
                        torque = omega = 0;
                }
                if (te instanceof ShaftPowerEmitter sp) {
                    if (sp.isEmitting() && sp.canWriteTo(read.getOpposite())) {
                        torque = sp.getTorque();
                        omega = sp.getOmega();
                    } else
                        torque = omega = 0;
                }

                if (te instanceof ComplexIO pwr) {
                    Direction dir = this.getInputDirection().getOpposite();
                    omega = pwr.getSpeedToSide(dir);
                    torque = pwr.getTorqueToSide(dir);
                }
                if (m == MachineRegistry.SPLITTER) {
                    BlockEntitySplitter devicein = (BlockEntitySplitter) te;
                    if (devicein.isSplitting()) {
                        processingSecondary = false;
                        this.readFromSplitter(world, pos, devicein);
                        torque = torquein;
                        omega = omegain;
                    } else if (devicein.isWritingTo(this)) {
                        torque = devicein.torque;
                        omega = devicein.omega;
                    } else
                        torque = omega = 0;
                }
//            } else if (te instanceof WorldRift) {
//                WorldRift sr = (WorldRift) te;
//                WorldLocation loc = sr.getLinkTarget();
//                if (loc != null)
//                    this.transferPower(loc.getWorld(), loc.xCoord, loc.yCoord, loc.zCoord, meta, false, false);
            } else {
                omega = 0;
                torque = 0;
            }
            power = (long) torque * (long) omega;
            this.writeToReceiver();
            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d * %d = %d", this.omega, this.torque, this.power));
        }
    }

    private boolean canCombine(PowerSourceList in1, PowerSourceList in2, int t1, int t2) {
        PowerSourceList combo = PowerSourceList.combine(in1, in2, this);
        if (combo.isLooping())
            return false;
        if (t1 == 0 || t2 == 0)
            return true;
        return bedrock || !combo.isEngineSpam();
    }

    public void fail() {
        level.explode(null, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, 4, Level.ExplosionInteraction.NONE);
        failed = true;
    }

    public boolean isSplitting() {
        // ioside 0-7 = merge, 8-15 = split. No separate field needed.
        return ioside >= 8;
    }

    public boolean isBedrock() {
        return bedrock;
    }

    private boolean isLoopingPower(PowerSourceList in1, PowerSourceList in2) {
		/*
		if (torquein*omegain != 0 && in1.getRealMaxPower() == 0) {
			omegain = omegain2;
			torquein = torquein2;
			return true;
		}
		if (torquein2*omegain2 != 0 && in2.getRealMaxPower() == 0) {
			omegain2 = omegain;
			torquein2 = torquein;
			return true;

		}*/
        int st = torquein + torquein2;
        int so = omegain + omegain2;
        //ReikaJavaLibrary.pConsole(in1, Dist.DEDICATED_SERVER, xCoord == -1011);
        return st > 0 && so > 0 && (in1.passesThrough(this) || in2.passesThrough(this));
    }

    private void writeToReceiver() {
        int t1 = 0;
        int t2 = 0;
        int y = worldPosition.getY();
        Level world = level;

        int ratio = this.getRatioFromMode();
        if (ratio == 0)
            return;
        boolean favorbent = false;
        if (ratio < 0) {
            favorbent = true;
            ratio = -ratio;
        }
        if (ratio == 1) { //Even split, favorbent irrelevant
            t1 = torque / 2;
        } else if (favorbent) {
            t1 = torque / ratio;
        } else {
            t1 = (int) (torque * ((ratio - 1D) / (ratio)));
        }
        if (ratio == 1) { //Even split, favorbent irrelevant
            t2 = torque / 2;
        } else if (favorbent) {
            t2 = (int) (torque * ((ratio - 1D) / (ratio)));
        } else {
            t2 = torque / ratio;
        }
        this.writeToPowerReceiver(write, omega, t1);
        this.writeToPowerReceiver(write2, omega, t2);

        torqueOut1 = t1;
        torqueOut2 = t2;
    }

    @Override
    protected void readFromSplitter(Level world, BlockPos pos, BlockEntitySplitter spl) { //Complex enough to deserve its own function
        this.assignOmega(spl.omega); //omegain always constant
        int ratio = spl.getRatioFromMode();
        if (ratio == 0)
            return;
        boolean favorbent = false;
        if (ratio < 0) {
            favorbent = true;
            ratio = -ratio;
        }
        if (worldPosition == spl.getWritePos() && worldPosition == spl.getWritePos2()) { //We are the inline
            if (ratio == 1) { //Even split, favorbent irrelevant
                this.assignTorque(spl.torque / 2);
                return;
            }
            if (favorbent) {
                this.assignTorque(spl.torque / ratio);
            } else {
                this.assignTorque((int) (spl.torque * ((ratio - 1D) / (ratio))));
            }
        } else if (worldPosition == spl.getWritePos() && worldPosition == spl.getWritePos2()) { //We are the bend
            this.assignOmega(spl.omega); //omegain always constant
            if (ratio == 1) { //Even split, favorbent irrelevant
                this.assignTorque(spl.torque / 2);
                return;
            }
            if (favorbent) {
                this.assignTorque((int) (spl.torque * ((ratio - 1D) / (ratio))));
            } else {
                this.assignTorque(spl.torque / ratio);
            }
        } else { //We are not one of its write-to blocks
            this.assignTorque(0);
            this.assignOmega(0);
        }
    }

    private void assignTorque(int val) {
        if (processingSecondary)
            torquein2 = val;
        else
            torquein = val;
    }

    private void assignOmega(int val) {
        if (processingSecondary)
            omegain2 = val;
        else
            omegain = val;
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putInt("mode", splitmode);
        tag.putBoolean("fail", failed);
        tag.putBoolean("bedrock", bedrock);
        // 1.21.5 port: persist ioside + split-mode so the splitter's read/write configuration
        // survives chunk reload and is visible to the {@link LuaSetJunction}-style API.
        tag.putInt("ioside", ioside);
        tag.putBoolean("iosideInit", iosideInitialised);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        splitmode = tag.getIntOr("mode", 0);
        failed = tag.getBooleanOr("fail", false);
        bedrock = tag.getBooleanOr("bedrock", false);
        ioside = tag.getIntOr("ioside", 0);
        iosideInitialised = tag.getBooleanOr("iosideInit", false);
    }

    @Override
    protected String getTEName() {
        return "splitter";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }


    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.hasLevel()) { //todo isinlevel
            phi = 0;
            return;
        }
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.SPLITTER;
    }

    @Override
    public PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
        PowerSourceList pwr = new PowerSourceList();
        if (caller == null)
            caller = this;
        if (!this.isSplitting()) { //merge
            if (read != null) {
                PowerSourceList in = PowerSourceList.getAllFrom(getLevel(), read, new BlockPos(worldPosition.getX() + read.getStepX(), worldPosition.getY() + read.getStepY(), worldPosition.getZ() + read.getStepZ()), this, caller);
                pwr.addAll(in);
            }
            if (read2 != null) {
                PowerSourceList in = PowerSourceList.getAllFrom(getLevel(), read2, new BlockPos(worldPosition.getX() + read.getStepX(), worldPosition.getY() + read.getStepY(), worldPosition.getZ() + read.getStepZ()), this, caller);
                pwr.addAll(in);
            }
            return pwr;
        } else {
            return PowerSourceList.getAllFrom(getLevel(), read, new BlockPos(worldPosition.getX() + read.getStepX(), worldPosition.getY() + read.getStepY(), worldPosition.getZ() + read.getStepZ()), this, caller);
        }
    }

    public String getRatioForDisplay() {
        return switch (splitmode) {
            case 32 -> "31:1 Inline";
            case -32 -> "1:31 Bend";
            case 16 -> "15:1 Inline";
            case -16 -> "1:15 Bend";
            case 8 -> "7:1 Inline";
            case -8 -> "1:7 Bend";
            case 4 -> "3:1 Inline";
            case -4 -> "1:3 Bend";
            case 1 -> "1:1 Even";
            default -> "ERROR";
        };
    }

    @Override
    public BlockPos getWritePos() {
        return new BlockPos(write != null ? worldPosition.getX() + write.getStepX() : Integer.MIN_VALUE, worldPosition.getY(), write != null ? worldPosition.getZ() + write.getStepZ() : Integer.MIN_VALUE);
    }

    @Override
    public BlockPos getWritePos2() {
        return new BlockPos(write2 != null ? worldPosition.getX() + write2.getStepX() : Integer.MIN_VALUE, worldPosition.getY(), write2 != null ? worldPosition.getZ() + write2.getStepZ() : Integer.MIN_VALUE);
    }


    @Override
    public CompoundTag getTagsToWriteToStack() {
        CompoundTag NBT = new CompoundTag();
        NBT.putBoolean("bedrock", bedrock);
        return NBT;
    }

    @Override
    public void setDataFromItemStackTag(CompoundTag NBT) {
        bedrock = NBT != null && NBT.getBooleanOr("bedrock", false);
    }

    @Override
    public ArrayList<CompoundTag> getCreativeModeVariants() {
        ArrayList<CompoundTag> li = new ArrayList<>();
        li.add(new CompoundTag());
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("bedrock", true);
        li.add(nbt);
        return li;
    }

    @Override
    public ArrayList<String> getDisplayTags(CompoundTag NBT) {
        ArrayList<String> li = new ArrayList<>();
        li.add(NBT != null && NBT.getBooleanOr("bedrock", false) ? "Bedrock" : "Steel");
        return li;
    }

    public void setBedrock() {
        bedrock = true;
    }

    public int getInputTorque1() {
        return torquein;
    }

    public int getInputTorque2() {
        return torquein2;
    }

    public int getInputSpeed1() {
        return omegain;
    }

    public int getInputSpeed2() {
        return omegain2;
    }

    @Override
    public void onPowerLooped(PowerTracker pwr) {
        if (power > 0)
            this.fail();
    }

    @Override
    public final void getAllOutputs(Collection<BlockEntity> c, Direction dir) {
        c.add(getAdjacentBlockEntity(write));
        if (this.isSplitting()) {
            c.add(getAdjacentBlockEntity(write2));
        }
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
