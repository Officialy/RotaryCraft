package reika.rotarycraft.blockentities.transmission;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.ModList;
import reika.dragonapi.interfaces.blockentity.GuiController;
import reika.dragonapi.libraries.java.ReikaArrayHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.api.interfaces.ComplexIO;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.api.power.ShaftPowerEmitter;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import reika.rotarycraft.base.blockentity.BlockEntityTransmissionMachine;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

import java.util.Collection;

//@Strippable(value = {"mrtjp.projectred.api.IBundledTile"})
public class BlockEntityDistributionClutch extends BlockEntityTransmissionMachine implements ComplexIO, GuiController {//, IBundledTile {

    private int[] requestedTorques = new int[4];
    private int[] outputTorques = new int[4];
    private boolean[] enabledSides = new boolean[4];

    private ControlMode control = ControlMode.GUI;

    public BlockEntityDistributionClutch(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.DISTRIBUTION_CLUTCH.get(), pos, state);
    }

    @Override
    public PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
        if (read == null)
            return new PowerSourceList();
        return PowerSourceList.getAllFrom(level, read, new BlockPos(worldPosition.getX() + read.getStepX(), worldPosition.getY() + read.getStepY(), worldPosition.getZ() + read.getStepZ()), this, caller);
    }

    @Override
    public void getAllOutputs(Collection<BlockEntity> c, Direction dir) {
        if (dir == read) {
            for (int i = 2; i < 6; i++) {
                Direction dir2 = Direction.values()[i];
                if (this.isOutputtingToSide(dir2)) {
                    c.add(getAdjacentBlockEntity(dir2));
                }
            }
        }
    }

    public boolean isOutputtingToSide(Direction dir) {
        return dir != read && enabledSides[dir.ordinal() - 2] && (requestedTorques[dir.ordinal() - 2] > 0 || outputTorques[dir.ordinal() - 2] > 0);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    //    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
//        if (!this.isisInWorld()) {
//            phi = 0;
//            return;
//        }
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.DISTRIBCLUTCH;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.DISTRIBUTION_CLUTCH.get();
    }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getIOSides(world, pos, getBlockState().getValue(BlockRotaryCraftMachine.FACING));
        if (!RotaryAux.getPowerOnClient && world.isClientSide)
            return;
        this.updateControl(world, pos);
        this.intakePower(world, pos);
        omega = omegain;
        this.distributePower(world, pos);
    }

    private void getIOSides(Level world, BlockPos pos, Direction direction) {
        switch (direction.ordinal()) {
            case 0 -> read = Direction.EAST;
            case 1 -> read = Direction.WEST;
            case 2 -> read = Direction.SOUTH;
            case 3 -> read = Direction.NORTH;
        }
        write = read.getOpposite();
    }

    private void updateControl(Level world, BlockPos pos) {
        enabledSides[write.ordinal() - 2] = true;
        enabledSides[read.ordinal() - 2] = false;
        if (control == ControlMode.REDSTONE) {
            int redstone = world.getBestNeighborSignal(pos);
            enabledSides[Direction.NORTH.ordinal() - 2] = (redstone & 1) != 0;
            enabledSides[Direction.EAST.ordinal() - 2] = (redstone & 2) != 0;
            enabledSides[Direction.SOUTH.ordinal() - 2] = (redstone & 4) != 0;
            enabledSides[Direction.WEST.ordinal() - 2] = (redstone & 8) != 0;
        } //else if (control == ControlMode.BUNDLEDREDSTONE) {
        //  enabledSides[Direction.NORTH.ordinal() - 2] = this.getBundledInput(world, pos, ReikaDyeHelper.RED) != 0;
        //  enabledSides[Direction.EAST.ordinal() - 2] = this.getBundledInput(world, pos, ReikaDyeHelper.YELLOW) != 0;
        //  enabledSides[Direction.SOUTH.ordinal() - 2] = this.getBundledInput(world, pos, ReikaDyeHelper.BLUE) != 0;
        //  enabledSides[Direction.WEST.ordinal() - 2] = this.getBundledInput(world, pos, ReikaDyeHelper.GREEN) != 0;
        //}
    }

    private void distributePower(Level world, BlockPos pos) {
        int leftover = torquein;
        for (int i = 2; i < 6; i++) {
            Direction dir = Direction.values()[i];
            outputTorques[dir.ordinal() - 2] = 0;
            //ReikaJavaLibrary.pConsole("Resetting output on "+dir, Dist.DEDICATED_SERVER, xCoord == 514);
            if (this.isOutputtingToSide(dir)) {
                int amt = this.calculateOutputTorque(dir, leftover);
                leftover -= amt;
                outputTorques[dir.ordinal() - 2] = amt;
                //ReikaJavaLibrary.pConsole("Setting output to "+amt+" on "+dir, Dist.DEDICATED_SERVER, xCoord == 514);
            }
        }
        outputTorques[write.ordinal() - 2] = leftover;
        //ReikaJavaLibrary.pConsole("Setting output to "+leftover+" on "+write, Dist.DEDICATED_SERVER, xCoord == 514);
        for (int i = 2; i < 6; i++) {
            Direction dir = Direction.values()[i];
            if (getAdjacentBlockEntity(dir) != null) {
                int speed = this.getSpeedToSide(dir);
                int torque = this.getTorqueToSide(dir);
                this.writeToPowerReceiver(dir, speed, torque);
            }
        }
        //ReikaJavaLibrary.pConsole("write "+System.identityHashCode(this)+" @ "+System.nanoTime()+": "+torquein+" > "+Arrays.toString(enabledSides)+" > "+Arrays.toString(outputTorques)+" & "+this.getTorqueToSide(Direction.WEST), Dist.DEDICATED_SERVER, x == 514);
    }

    private void intakePower(Level world, BlockPos pos) {
        if (!RotaryAux.getPowerOnClient && world.isClientSide)
            return;
        omegain = torquein = 0;
        boolean isCentered = pos.getX() == worldPosition.getX() && pos.getY() == worldPosition.getY() && pos.getZ() == worldPosition.getZ();
        int dx = pos.getX() + read.getStepX();
        int dy = pos.getY() + read.getStepY();
        int dz = pos.getZ() + read.getStepZ();
        MachineRegistry m = isCentered ? this.getMachine(read) : MachineRegistry.getMachine(world, new BlockPos(dx, dy, dz));
        BlockEntity te = isCentered ? getAdjacentBlockEntity(read) : world.getBlockEntity(new BlockPos(dx, dy, dz));
        //ReikaJavaLibrary.pConsole(System.nanoTime(), Dist.DEDICATED_SERVER, x == 514);
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
                //ReikaJavaLibrary.pConsole(System.identityHashCode(te), Dist.DEDICATED_SERVER, xCoord == 513);
                //ReikaJavaLibrary.pConsole("read "+System.identityHashCode(te)+" @ "+System.nanoTime()+": "+((BlockEntityDistributionClutch)te).torquein+" > "+Arrays.toString(((BlockEntityDistributionClutch)te).enabledSides)+" > "+Arrays.toString(((BlockEntityDistributionClutch)te).outputTorques)+" & "+((BlockEntityDistributionClutch)te).getTorqueToSide(dir), Dist.DEDICATED_SERVER, x == 513);
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
        } /*else if (te instanceof WorldRift) {
            WorldRift sr = (WorldRift) te;
            WorldLocation loc = sr.getLinkTarget();
            if (loc != null)
                this.intakePower(loc.getWorld(), loc.xCoord, loc.yCoord, loc.zCoord, meta);
        }*/ else {
            omega = torque = 0;
        }
		/*
		 		if (this.isProvider(te)) {
			this.processTileSimply(te, m, xCoord, yCoord, zCoord);
		}
		else if (te instanceof SpaceRift) {
			SpaceRift sr = (SpaceRift)te;
			WorldLocation loc = sr.getLinkTarget();
			BlockEntity other = sr.getBlockEntityFrom(read);
			this.processTileSimply(other, MachineRegistry.getMachine(loc.move(read, 1)), loc.xCoord, loc.yCoord, loc.zCoord);
		}
		else {
			omega = torque = 0;
		}
		 */
    }

    private int calculateOutputTorque(Direction dir, int available) {
        return Math.min(requestedTorques[dir.ordinal() - 2], available);
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);

        tag.putIntArray("output", outputTorques);
        tag.putIntArray("req", requestedTorques);
        tag.putInt("sides", ReikaArrayHelper.booleanToBitflags(enabledSides));

        tag.putInt("control", control.ordinal());

        //ReikaJavaLibrary.pConsole("out "+NBT+" leaves "+Arrays.toString(outputTorques), xCoord == 514);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);

        //ReikaJavaLibrary.pConsole("in "+NBT+" gives "+Arrays.toString(outputTorques), xCoord == 514);

        requestedTorques = tag.getIntArray("req");
        outputTorques = tag.getIntArray("output");
        enabledSides = ReikaArrayHelper.booleanFromBitflags(tag.getInt("sides"), 4);

        control = ControlMode.list[tag.getInt("control")];
    }

    @Override
    protected String getTEName() {
        return "distributionclutch";
    }

    @Override
    public int getSpeedToSide(Direction dir) {
        return this.isOutputtingToSide(dir) ? omega : 0;
    }

    @Override
    public int getTorqueToSide(Direction dir) {
        return outputTorques[dir.ordinal() - 2];
    }

    public void setSideEnabled(Direction dir, boolean flag) {
        enabledSides[dir.ordinal() - 2] = flag;
//        this.syncAllData(false);
    }

    public boolean isSideEnabled(Direction dir) {
        return enabledSides[dir.ordinal() - 2];
    }

    public int getTorqueRequest(Direction dir) {
        return requestedTorques[dir.ordinal() - 2];
    }

    public void setTorqueRequest(Direction dir, int amt) {
        requestedTorques[dir.ordinal() - 2] = amt;
//        this.syncAllData(false);
    }

    public void setTorqueRequests(int[] vals) {
        for (int i = 0; i < 4; i++) {
            requestedTorques[i] = vals[i];
        }
//        this.syncAllData(false);
    }

    public int getInputTorque() {
        return torquein;
    }

    //    @Override
    public byte[] getBundledSignal(int dir) {
        return new byte[16];
    }

    //    @Override
    public boolean canConnectBundled(int side) {
        return control == ControlMode.BUNDLEDREDSTONE;
    }

    /*@ModDependent(ModList.PROJRED)
    private int getBundledInput(Level world, BlockPos pos, ReikaDyeHelper color) {
        int ret = 0;
        for (int i = 0; i < 6; i++) {
            byte[] data = ProjectRedAPI.transmissionAPI.getBundledInput(world, pos, i);
            int at = data != null ? data[color.ordinal()] & 255 : 0;
            if (at > ret)
                ret = at;
        }
        return ret;
    }*/

    public void stepMode() {
        control = control.next();
        while (!control.isValid()) {
            control = control.next();
        }
        enabledSides = new boolean[4];
//        this.triggerBlockUpdate();
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    public enum ControlMode {
        GUI,
        REDSTONE,
        BUNDLEDREDSTONE,
        COMPUTER;

        private static final ControlMode[] list = values();

        public ControlMode next() {
            return this.ordinal() == list.length - 1 ? list[0] : list[this.ordinal() + 1];
        }

        public boolean isValid() {
            return switch (this) {
                case BUNDLEDREDSTONE -> ModList.PROJRED.isLoaded();
                case COMPUTER -> ModList.COMPUTERCRAFT.isLoaded() || ModList.OPENCOMPUTERS.isLoaded();
                default -> true;
            };
        }
    }

}
