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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.interfaces.blockentity.BreakAction;
import reika.dragonapi.libraries.ReikaNBTHelper;
import reika.dragonapi.libraries.java.ReikaArrayHelper;
import reika.rotarycraft.auxiliary.interfaces.*;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

public abstract class BlockEntityPiping extends RotaryCraftBlockEntity implements RenderableDuct, CachedConnection, BreakAction, PumpablePipe {

    public static final int UPPRESSURE = 40;
    public static final int HORIZPRESSURE = 20;
    public static final int DOWNPRESSURE = 0;
    private static final HashSet<Class> nonInteractableClasses = new HashSet();
    private static final HashSet<Class> interactableClasses = new HashSet();
    private static final int CAPACITY_LIMIT = 1000000000; //1 billion mB to prevent overflow
    private static final int MAXPRESSURE = 2400000;
    private final boolean[] interaction = new boolean[6];
    private final StepTimer flowTimer = new StepTimer(getTickDelay());
    private boolean[] connections = new boolean[6];
    private int connectionDelay = 0;

    public BlockEntityPiping(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static int getTickDelay() {
        int cfg = Math.max(ConfigRegistry.FLOWSPEED.getValue(), 1);
        if (cfg > 5)
            cfg = 5;
        return 6 - cfg;
    }

    public final int getPressure() {
        Fluid f = this.getAttributes();
        int amt = this.getFluidLevel();
        if (f == null || amt <= 0)
            return 101300;
        //p = rho*R*T approximation
        long ret;
    if (f.getFluidType().isLighterThanAir())
      ret =
          101300
              + (128
                  * (int)
                      (amt
                          / 1000D
                          * f.getFluidType().getTemperature()
                          * Math.abs(f.getFluidType().getDensity())
                          / 1000D));
    else ret = 101300 + amt * 24L;
        return (int) Math.min(Integer.MAX_VALUE, ret);
    }

    public int getMaxPressure() {
        return MAXPRESSURE;
    }

    private void overpressure(Level world, BlockPos pos) {
        Fluid f = this.getAttributes();
        if (f.defaultFluidState().canBeReplacedWith(world, pos, f, null)) {//.canBePlacedInWorld()) {
            world.setBlock(pos, f.defaultFluidState().createLegacyBlock(), 0);
        } else {
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
        }
        //world.markBlockForUpdate(pos);
        //world.notifyBlockOfNeighborChange(pos, f.getBlock());
    }

    public abstract int getFluidLevel();

    protected abstract void setFluidLevel(int amt);

    public abstract boolean canConnectToPipe(MachineRegistry p, Direction side);

    protected abstract void setFluid(Fluid f);

    protected abstract boolean interactsWithMachines();

    protected abstract void onIntake(BlockEntity te);

    public abstract boolean canReceiveFromPipeOn(Direction side);

    public abstract boolean canEmitToPipeOn(Direction side);

    public abstract boolean canIntakeFromIFluidHandler(Direction side);

    public abstract boolean canOutputToIFluidHandler(Direction side);

    public final boolean canIntakeFluid(Fluid f) {
        if (f == null)
            return false;
        return this.isValidFluid(f) && (this.getAttributes() == null || this.getFluidLevel() == 0 || this.getAttributes().equals(f));
    }

    public abstract boolean isValidFluid(Fluid f);

    //    @Override
    public void updateEntity(Level world, BlockPos pos) {
//        if (this.tickcount < 5) {
//            this.syncAllData(true);
//            world.markBlockForUpdate(pos);
//        }
        Fluid f = this.getAttributes();
        //if (!world.isClientSide) {
        flowTimer.update();
        if (flowTimer.checkCap()) {
            this.intakeFluid(world, pos);
            this.dumpContents(world, pos);
        }
        //}
        if (this.getFluidLevel() <= 0) {
            this.setFluidLevel(0);
            this.setFluid(null);
        }
        Fluid f2 = this.getAttributes();
//        if (f != f2) {
//            this.syncAllData(true);
//            world.markBlockForUpdate(pos);
//        }

        if (this.getPressure() > this.getMaxPressure()) {
            if (world.isClientSide) {
//                ReikaPacketHelper.sendUpdatePacket(DragonAPI.packetChannel, PacketIDs.EXPLODE.ordinal(), this, PacketDistributor.PacketTarget.SERVER);
            } else {
                this.overpressure(world, pos);
            }
        }

        if (!world.isClientSide) {
            if (connectionDelay > 0) {
                connectionDelay--;
                if (connectionDelay == 0) {
                    this.recomputeConnections(world, pos);
                }
            }
        }
    }

    //    @Override
    protected final void onFirstTick(Level world, BlockPos pos) {
        this.recomputeConnections(world, pos);
    }

    //    @Override
    public int getPacketDelay() {
//        return 4 * super.getPacketDelay();
        return 4;
    }

    protected void queueConnectionEvaluation(int delay) {
        connectionDelay = 2;
    }

    protected final boolean canInteractWith(Level world, BlockPos pos, Direction side) {
        if (!connections[side.ordinal()])
            return false;
        int dx = pos.getX() + side.getStepX();
        int dy = pos.getY() + side.getStepY();
        int dz = pos.getZ() + side.getStepZ();
        Block id = world.getBlockState(new BlockPos(dx, dy, dz)).getBlock();
        if (id == Blocks.AIR)
            return false;
        MachineRegistry m = MachineRegistry.getMachine(world, new BlockPos(dx, dy, dz));
//        if (m != null && m.isPipe())
//            return this.hasReciprocalConnectivity((BlockEntityPiping) getAdjacentBlockEntity(level, pos, side), side);
        BlockEntity te = getAdjacentBlockEntity(side);
//        if (te instanceof WorldRift) {
//            return true;
//        }
        return this.interactsWithMachines() && this.isInteractableTile(te, side);
    }

    private boolean isInteractableTile(BlockEntity te, Direction side) {
        if (te == null)
            return false;
        if (te instanceof PipeRenderConnector) {
            return ((PipeRenderConnector) te).canConnectToPipeOnSide(side);
        }
        if (te instanceof IFluidHandler) {
            Class c = te.getClass();
            if (interactableClasses.contains(c))
                return true;
            if (nonInteractableClasses.contains(c))
                return false;
            String name = c.getSimpleName().toLowerCase(Locale.ENGLISH);
            if (name.contains("conduit") || name.contains("fluidduct") || name.contains("pipe") || name.contains("multipart")) {
                nonInteractableClasses.add(c);
                return false;
            }
            interactableClasses.add(c);
            return true;
        }
        return false;
    }

    public final int getPipeIntake(int otherlevel) {
        return Math.min(CAPACITY_LIMIT - this.getFluidLevel(), TransferAmount.QUARTER.getTransferred(otherlevel));
    }

    public final int getPipeOutput(int max) {
        return Math.min(TransferAmount.QUARTER.getTransferred(max), this.getFluidLevel() - 5);
    }

    private final void dumpContents(Level world, BlockPos pos) {
        Fluid f = this.getAttributes();
        if (this.getFluidLevel() <= 1 || f == null)
            return;
        for (int i = 0; i < 6; i++) {
            int level = this.getFluidLevel();
            if (level <= 0) {
                this.setFluid(null);
                return;
            }
            Direction dir = Direction.values()[i];
            if (interaction[i]) {
                int dx = pos.getX() + dir.getStepX();
                int dy = pos.getY() + dir.getStepY();
                int dz = pos.getZ() + dir.getStepZ();
                BlockEntity te = world.getBlockEntity(new BlockPos(dx, dy, dz));

                if (te instanceof BlockEntityPiping tp) {
                    if (this.hasReciprocalConnectivity(tp, dir) && this.canEmitToPipeOn(dir) && tp.canReceiveFromPipeOn(dir.getOpposite())) {
                        //ReikaJavaLibrary.pConsole(dir, this.getSide() == Dist.DEDICATED_SERVER && this instanceof BlockEntitySeparatorPipe);
                        if (tp.canIntakeFluid(f)) {
                            int otherlevel = tp.getFluidLevel();
                            int dL = level - otherlevel;
                            int toadd = this.getPipeOutput(dL);
                            if (toadd > 0) {
                                tp.addFluid(toadd);
                                this.removeLiquid(toadd);
                            }
                        }
                    }
                } else if (te instanceof PipeConnector pc) {
                    Flow flow = pc.getFlowForSide(dir.getOpposite());
                    if (flow.canIntake) {
                        int toadd = this.getPipeOutput(this.getFluidLevel());
                        //int toadd = pc.getFluidRemoval().getTransferred(this.getLiquidLevel());
                        if (toadd > 0) {
                            FluidStack fs = new FluidStack(f, toadd);
                            int added = pc.fillPipe(dir.getOpposite(), fs, IFluidHandler.FluidAction.EXECUTE);
                            //ReikaJavaLibrary.pConsole(added, Dist.DEDICATED_SERVER);
                            if (added > 0) {
                                //ReikaJavaLibrary.pConsole(toadd+":"+added+":"+this.getLiquidLevel(), Dist.DEDICATED_SERVER);
                                this.removeLiquid(added);
                            }
                        }
                    }
                } else if (te instanceof IFluidHandler fl && this.canOutputToIFluidHandler(dir)) {
                    //                    if (fl.isFluidValid(dir.getOpposite(), f)) {
//                        int toadd = this.getPipeOutput(this.getFluidLevel());
//                        if (toadd > 0) {
//                            int added = fl.fill(new FluidStack(f, toadd), IFluidHandler.FluidAction.EXECUTE);
//                            if (added > 0) {
//                                this.removeLiquid(added);
//                            }
//                        }
//                    }
                }
            }
        }
    }

    public final void removeLiquid(int toremove) {
        this.setFluidLevel(this.getFluidLevel() - toremove);
    }

    public final void addFluid(int toadd) {
        this.setFluidLevel(this.getFluidLevel() + toadd);
    }

    private void intakeFluid(Level world, BlockPos pos) {
        for (int i = 0; i < 6; i++) {
            Direction dir = Direction.values()[i];
            if (interaction[i]) {
                int dx = pos.getX() + dir.getStepX();
                int dy = pos.getY() + dir.getStepY();
                int dz = pos.getZ() + dir.getStepZ();
                BlockEntity te = world.getBlockEntity(new BlockPos(dx, dy, dz));

//                if (te instanceof WorldRift) {
//                    if (world.isClientSide)
//                        continue;
//                    WorldLocation loc = ((WorldRift) te).getLinkTarget();
//                    if (loc != null) {
//                        te = ((WorldRift) te).getBlockEntityFrom(dir);
//                        if (te == null)
//                            continue;
//                        dx = te.getBlockPos().getX();
//                        dy = te.getBlockPos().getY();
//                        dz = te.getBlockPos().getZ();
//                        world = te.getLevel();
//                    }
//                }

                if (te instanceof BlockEntityPiping tp) {
                    if (this.hasReciprocalConnectivity(tp, dir) && this.canReceiveFromPipeOn(dir) && tp.canEmitToPipeOn(dir.getOpposite())) {
                        Fluid f = tp.getAttributes();
                        int amt = tp.getFluidLevel();
                        int dL = amt - this.getFluidLevel();
                        int todrain = this.getPipeIntake(dL);
                        if (todrain > 0 && this.canIntakeFluid(f)) {
                            this.setFluid(f);
                            this.addFluid(todrain);
                            tp.removeLiquid(todrain);
                            //ReikaJavaLibrary.pConsole("Transferring "+todrain+", have "+this.getFluidLevel(), Dist.DEDICATED_SERVER, this.getAdjacentBlockEntity(Direction.UP) == null);
                            this.onIntake(te);
                        }
                    }
                } else if (te instanceof PipeConnector pc) {
                    Flow flow = pc.getFlowForSide(dir.getOpposite());
                    if (flow.canOutput) {
                        FluidStack fs = pc.drainPipe(dir.getOpposite(), Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                        if (fs != null) {
                            int level = this.getFluidLevel();
                            int todrain = this.getPipeIntake(fs.getAmount() - level);
                            if (todrain > 0) {
                                if (this.canIntakeFluid(fs.getFluid())) {
                                    this.addFluid(todrain);
                                    this.setFluid(fs.getFluid());
                                    pc.drainPipe(dir.getOpposite(), todrain, IFluidHandler.FluidAction.EXECUTE);
                                    this.onIntake(te);
                                    //ReikaJavaLibrary.pConsole("Transferring "+todrain+", have "+this.getFluidLevel(), Dist.DEDICATED_SERVER);
                                }
                            }
                        }
                    }
                } else if (te instanceof IFluidHandler fl && this.canIntakeFromIFluidHandler(dir)) {
                    FluidStack fs = fl.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE);
                    //ReikaJavaLibrary.pConsole(fs);
                    if (fs != null) {
                        int level = this.getFluidLevel();
                        int todrain = this.getPipeIntake(fs.getAmount() - level);
                        if (todrain > 0) {
                            if (this.canIntakeFluid(fs.getFluid())) {
                                this.setFluid(fs.getFluid());
                                this.onIntake(te);
                                int drained = fl.drain(todrain, IFluidHandler.FluidAction.EXECUTE).getAmount();
                                this.addFluid(drained);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public final boolean hasModelTransparency() {
        return false;
    }

    /**
     * Direction is relative to the piping block (so DOWN means the block is below the pipe)
     */
    public boolean isConnectionValidForSide(Direction dir) {
//        if (dir.getStepX() == 0 && MinecraftForgeClient.getRenderPass() != 1)
//            dir = dir.getOpposite();
        return connections[dir.ordinal()];
    }

    public boolean isConnectedDirectly(Direction dir) {
        return connections[dir.ordinal()];
    }

    @Override
    public final AABB getRenderBoundingBox() {
        return new AABB(worldPosition, new BlockPos(worldPosition.getX() + 1, worldPosition.getY() + 1, worldPosition.getZ() + 1));
    }

    @Override
    public final void onEMP() {
    }

    public abstract boolean hasLiquid();

    public abstract Fluid getAttributes();

    public void recomputeConnections(Level world, BlockPos pos) {
        for (int i = 0; i < 6; i++) {
            connections[i] = this.shouldTryToConnect(Direction.values()[i]);
            interaction[i] = this.canInteractWith(world, pos, Direction.values()[i]);
//            world.func_147479_m(x + dirs[i].getStepX(), y + dirs[i].getStepY(), z + dirs[i].getStepZ());
        }
//        this.syncAllData(true);
//        world.markBlockForUpdate(pos);
        world.blockUpdated(pos, this.getBlockState().getBlock());
    }

    public void deleteFromAdjacentConnections(Level world, BlockPos pos) {
        for (int i = 0; i < 6; i++) {
            Direction dir = Direction.values()[i];
            int dx = pos.getX() + dir.getStepX();
            int dy = pos.getX() + dir.getStepY();
            int dz = pos.getX() + dir.getStepZ();
            MachineRegistry m = MachineRegistry.getMachine(world, new BlockPos(dx, dy, dz));
            if (m == this.getMachine()) {
                BlockEntityPiping te = (BlockEntityPiping) world.getBlockEntity(new BlockPos(dx, dy, dz));
                te.connections[dir.getOpposite().ordinal()] = false;
//                world.func_147479_m(new BlockPos(dx, dy, dz));
            }
        }
    }

    public void addToAdjacentConnections(Level world, BlockPos pos) {
        for (int i = 0; i < 6; i++) {
            Direction dir = Direction.values()[i];
            int dx = pos.getX() + dir.getStepX();
            int dy = pos.getY() + dir.getStepY();
            int dz = pos.getZ() + dir.getStepZ();
            MachineRegistry m = MachineRegistry.getMachine(world, new BlockPos(dx, dy, dz));
            if (m == this.getMachine()) {
                BlockEntityPiping te = (BlockEntityPiping) world.getBlockEntity(new BlockPos(dx, dy, dz));
                te.connections[dir.getOpposite().ordinal()] = true;
//                world.func_147479_m(dx, dy, dz);
            }
        }
    }

    public boolean shouldTryToConnect(Direction dir) {
        int x = getBlockPos().getX() + dir.getStepX();
        int y = getBlockPos().getY() + dir.getStepY();
        int z = getBlockPos().getZ() + dir.getStepZ();
        MachineRegistry m = this.getMachine();
        MachineRegistry m2 = MachineRegistry.getMachine(level, getBlockPos());
//        if (m != null && !m.isPipe() && m == m2)
//            return true;
        BlockEntity tile = level.getBlockEntity(getBlockPos());
        /*if (tile instanceof WorldRift) {
            return true;
        }*/
        if (tile instanceof BlockEntityPiping)
            return this.hasReciprocalConnectivity((BlockEntityPiping) tile, dir);
        else if (tile instanceof PipeConnector pc) {
            return pc.canConnectToPipe(this.getMachine()) && pc.canConnectToPipeOnSide(this.getMachine(), dir.getOpposite());
        } else return this.interactsWithMachines() && this.isInteractableTile(tile, dir);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putByte("conn", ReikaArrayHelper.booleanToByteBitflags(connections));

        ReikaNBTHelper.writeFluidToNBT(NBT, new FluidStack(this.getAttributes(), 0));
        NBT.putInt("level", this.getFluidLevel());
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        boolean update = false;

        boolean[] old = new boolean[connections.length];
        System.arraycopy(connections, 0, old, 0, old.length);

        connections = ReikaArrayHelper.booleanFromByteBitflags(NBT.getByte("conn"), 6);

        update = !Arrays.equals(old, connections);

        Fluid f = ReikaNBTHelper.getFluidFromNBT(NBT).getFluid();
        update = update || f != this.getAttributes();
        this.setFluid(f);
        this.setFluidLevel(NBT.getInt("level"));

//        if (level != null && update)
//            level.markBlockForUpdate(getBlockPos());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("conndelay", connectionDelay);
        return nbt;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        connectionDelay = nbt.getInt("conndelay");
    }

    @Override
    public final boolean isFluidPipe() {
        return true;
    }

    public void breakBlock() {
        this.deleteFromAdjacentConnections(level, getBlockPos());
    }

    @Override
    public boolean canTransferTo(PumpablePipe p, Direction dir) {
        return p instanceof BlockEntityPiping && this.hasReciprocalConnectivity((BlockEntityPiping) p, dir);
    }

    public final boolean hasReciprocalConnectivity(BlockEntityPiping te, Direction dir) {
        return te != null && this.canConnectToPipe(te.getMachine(), dir) && te.canConnectToPipe(this.getMachine(), dir.getOpposite());
    }

    @Override
    public final void transferFrom(PumpablePipe from, int amt) {
        BlockEntityPiping te = (BlockEntityPiping) from;
        this.setFluidLevel(this.getFluidLevel() + amt);
        this.setFluid(te.getAttributes());
        {
            te.setFluidLevel(te.getFluidLevel() - amt);
            if (te.getFluidLevel() == 0)
                te.setFluid(null);
        }
    }

    public void onPlacedAgainst(Direction dir) {

    }

    public final boolean allowExternalHeating() {
        return false;
    }

    public final boolean allowHeatExtraction() {
        return false;
    }

    public final boolean canBeCooledWithFins() {
        return false;
    }

    @Override
    public final double heatEnergyPerDegree() {
        double base = super.heatEnergyPerDegree();
        if (this.getAttributes() != null) {
            base += this.getAttributes().getFluidType().getDensity();
        }
        return base;
    }

    public enum TransferAmount {
        UNITY(),
        BUCKET(),
        QUARTER(),
        FORCEDQUARTER(),
        ALL();

        public int getTransferred(int max) {
            if (max <= 0)
                return 0;
            return switch (this) {
                case ALL -> max;
                case FORCEDQUARTER -> max / 4 + 1;
                case QUARTER -> max / 4;
                case UNITY -> 1;
                case BUCKET -> Math.min(max, 1000);
            };
        }
    }

    public enum Flow {
        INPUT(true, false),
        OUTPUT(false, true),
        DUAL(true, true),
        NONE(false, false);

        public static final Flow[] list = values();
        public final boolean canIntake;
        public final boolean canOutput;

        Flow(boolean in, boolean out) {
            canIntake = in;
            canOutput = out;
        }
    }
}
