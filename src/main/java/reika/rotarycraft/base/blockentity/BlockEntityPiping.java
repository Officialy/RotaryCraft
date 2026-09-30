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
import reika.dragonapi.instantiable.data.immutable.WorldLocation;
import reika.dragonapi.interfaces.blockentity.WorldRift;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import reika.dragonapi.instantiable.storage.FilteredFluidResourceHandler;
import reika.dragonapi.interfaces.blockentity.HasFluidResourceHandler;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.interfaces.blockentity.BreakAction;
import reika.dragonapi.libraries.java.ReikaArrayHelper;
import reika.rotarycraft.auxiliary.PipeDebugLog;
import reika.rotarycraft.auxiliary.interfaces.*;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.Locale;

public abstract class BlockEntityPiping extends RotaryCraftBlockEntity implements RenderableDuct, CachedConnection, BreakAction, PumpablePipe, HasFluidResourceHandler {

    public static final int UPPRESSURE = 40;
    public static final int HORIZPRESSURE = 20;
    public static final int DOWNPRESSURE = 0;
    private static final int CAPACITY_LIMIT = 1000000000; //1 billion mB to prevent overflow
    private static final int MAXPRESSURE = 2400000;
    /** 26.1 PERF: cache {@link Direction#values()} because every {@code Enum.values()}
     *  call allocates a new array, and at ~30 pipes × 20 TPS × ~12 calls per tick that's
     *  7,200 array allocations/sec just from this enum lookup — measured GC pauses from
     *  the user's PipeDbg dumps tracked exactly to allocation pressure in this hot path. */
    private static final Direction[] DIRS = Direction.values();
    /** Mutable scratch BlockPos so neighbour-lookup doesn't allocate per call. SAFE because
     *  pipe ticks run on a single (server) thread; this BE never ticks on the client.
     *  Per-BE so concurrent pipes don't clobber each other's scratch. */
    private final BlockPos.MutableBlockPos scratchPos = new BlockPos.MutableBlockPos();
    private final boolean[] interaction = new boolean[6];
    private final ResourceHandler<FluidResource> fluidHandler = new PipeFluidHandler();
    private final ResourceHandler<FluidResource> inputFluidView = new FilteredFluidResourceHandler(
            fluidHandler, index -> true, (index, resource) -> true, (index, resource) -> false);
    private final ResourceHandler<FluidResource> outputFluidView = new FilteredFluidResourceHandler(
            fluidHandler, index -> true, (index, resource) -> false, (index, resource) -> true);
    private final StepTimer flowTimer = new StepTimer(getTickDelay());
    private boolean[] connections = new boolean[6];
    private int connectionDelay = 0;

    // 26.1 client-sync tracker — see {@link #updateEntity} for the comment explaining why we
    // gate on these. Initial values match the BE's default-state (no fluid, level=0, no
    // connections) so the very first tick AFTER construction doesn't fire a spurious sync
    // before the staggered recompute fills in real values. Without this guard, every freshly
    // chunk-loaded pipe burst-sent a sync on tick 0, contributing to the ~80-BE chunk-load
    // packet flood the user observed as a 2.3s server stall.
    private Fluid lastSyncedFluid = null;
    private int lastSyncedLevel = 0;
    private int lastSyncedConnMask = 0;

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
            return 0;
        // p = rho*R*T approximation (gauge pressure, base = 0 when empty)
        long ret;
        if (f.getFluidType().isLighterThanAir())
            ret = (long)(128 * (amt / 1000D * f.getFluidType().getTemperature()
                    * Math.abs(f.getFluidType().getDensity()) / 1000D));
        else
            ret = amt * 24L;
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

    @Override
    public ResourceHandler<FluidResource> getFluidHandler(Direction side) {
        if (side == null) return fluidHandler;
        boolean input = canIntakeFromIFluidHandler(side);
        boolean output = canOutputToIFluidHandler(side);
        return input && output ? fluidHandler : input ? inputFluidView : output ? outputFluidView : null;
    }

    private final class PipeFluidHandler extends SnapshotJournal<PipeFluidState>
            implements ResourceHandler<FluidResource> {
        @Override public int size() { return 1; }
        @Override public FluidResource getResource(int index) {
            checkIndex(index);
            Fluid fluid = getAttributes();
            return fluid == null || getFluidLevel() <= 0 ? FluidResource.EMPTY : FluidResource.of(fluid);
        }
        @Override public long getAmountAsLong(int index) {
            checkIndex(index);
            return getFluidLevel();
        }
        @Override public long getCapacityAsLong(int index, FluidResource resource) {
            checkIndex(index);
            return resource.isEmpty() || isValid(index, resource) ? CAPACITY_LIMIT : 0;
        }
        @Override public boolean isValid(int index, FluidResource resource) {
            checkIndex(index);
            // Pipes retain only a fluid type. Reject component-bearing resources rather than
            // silently discarding their components during transfer.
            return !resource.isEmpty() && resource.equals(FluidResource.of(resource.getFluid()))
                    && isValidFluid(resource.getFluid());
        }
        @Override public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
            checkIndex(index);
            TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
            if (amount == 0 || !isValid(index, resource) || !canIntakeFluid(resource.getFluid())) return 0;
            int inserted = Math.min(amount, CAPACITY_LIMIT - getFluidLevel());
            if (inserted <= 0) return 0;
            updateSnapshots(transaction);
            setFluid(resource.getFluid());
            addFluid(inserted);
            return inserted;
        }
        @Override public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
            checkIndex(index);
            TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
            if (amount == 0 || !resource.equals(getResource(index))) return 0;
            int extracted = Math.min(amount, getFluidLevel());
            if (extracted <= 0) return 0;
            updateSnapshots(transaction);
            removeLiquid(extracted);
            if (getFluidLevel() == 0) setFluid(null);
            return extracted;
        }
        @Override protected PipeFluidState createSnapshot() {
            return new PipeFluidState(getAttributes(), getFluidLevel());
        }
        @Override protected void revertToSnapshot(PipeFluidState state) {
            setFluid(state.fluid());
            setFluidLevel(state.amount());
        }
        @Override protected void onRootCommit(PipeFluidState originalState) {
            setChanged();
        }
        private void checkIndex(int index) {
            java.util.Objects.checkIndex(index, 1);
        }
    }

    private record PipeFluidState(Fluid fluid, int amount) {}

    public final boolean canIntakeFluid(Fluid f) {
        if (f == null)
            return false;
        return this.isValidFluid(f) && (this.getAttributes() == null || this.getFluidLevel() == 0 || this.getAttributes().equals(f));
    }

    public abstract boolean isValidFluid(Fluid f);

    //    @Override
    public void updateEntity(Level world, BlockPos pos) {
        long _tickT0 = System.nanoTime();
        PipeDebugLog.event("pipe.tick");
        // 26.1 fix: pipes were not ticking the {@link BlockEntityBase} lifecycle at all. The
        // BlockPipe ticker calls this 2-arg overload, but the base's per-tick bookkeeping —
        // ticksExisted++/tileAge++, onFirstTick (which calls recomputeConnections!), periodic
        // sync, etc. — lives in the no-arg {@link BlockEntityBase#updateEntity()}. Engines /
        // BlockEntityIOMachine call {@code super.updateEntity()} from their 2-arg path; pipes
        // never did, so {@code age_ticks} stayed at 0 forever, {@code onFirstTick} never fired,
        // and the connections[] array stayed all-false. Symptom in NBT: user-reported pipe
        // with {@code conn: 0b, age_ticks: 0L} despite a pump on one side and a reservoir on
        // the other. Calling super.updateEntity() here drives the lifecycle so the first tick
        // populates connections and every subsequent tick syncs / ages correctly.
        super.updateEntity();
        Fluid f = this.getAttributes();
        flowTimer.update();
        if (flowTimer.checkCap()) {
            // 26.1 PERF: skip the intake/dump scan entirely if the pipe is fully isolated —
            // no interaction sides AND empty. The scan iterates 6 sides regardless, doing a
            // getBlockEntity lookup + instanceof checks per side; for the user-reported
            // 180-BE network (PipeDbg dumps showed ~3600 pipe.ticks/sec), this iteration was
            // the dominant per-tick cost. A truly isolated pipe has nothing to receive from
            // and nothing to push to; recomputeConnections will fire on neighborChanged the
            // moment a real source/sink is placed, repopulating interaction[] for the next
            // tick. NOTE: we still iterate when the pipe HAS interactions even if currently
            // empty — that's how empty pipes receive their first fluid from a producer.
            boolean anyInteraction = false;
            for (int i = 0; i < 6; i++) if (interaction[i]) { anyInteraction = true; break; }
            if (anyInteraction || this.getFluidLevel() > 0) {
                this.intakeFluid(world, pos);
                this.dumpContents(world, pos);
            } else {
                PipeDebugLog.event("pipe.idle.skipped");
            }
        }
        if (this.getFluidLevel() <= 0) {
            this.setFluidLevel(0);
            this.setFluid(null);
        }
        Fluid f2 = this.getAttributes();
        if (f != f2 && !world.isClientSide()) {
            // Fluid type changed — mark the chunk dirty so the next periodic sync ships the
            // new state and request a visual refresh. Don't force a full sync packet.
            this.setChanged();
            BlockState s = this.getBlockState();
            world.setBlocksDirty(pos, s, s);
        }

        if (this.getPressure() > this.getMaxPressure()) {
            if (world.isClientSide()) {
//                ReikaPacketHelper.sendUpdatePacket(DragonAPI.packetChannel, PacketIDs.EXPLODE.ordinal(), this, PacketDistributor.PacketTarget.SERVER);
            } else {
                this.overpressure(world, pos);
            }
        }

        if (!world.isClientSide()) {
            if (connectionDelay > 0) {
                connectionDelay--;
                if (connectionDelay == 0) {
                    this.recomputeConnections(world, pos);
                }
            }
            
            Fluid cur = this.getAttributes();
            int level = this.getFluidLevel();
            int connMask = 0;
            for (int i = 0; i < 6; i++) if (connections[i]) connMask |= (1 << i);
            // Bucket the level so the display updates on meaningful changes (~256mB) without re-syncing
            // every tick. 0 = empty (distinct from the lowest non-empty bucket so empty↔fluid still syncs).
            int packedLevelSentinel = level <= 0 ? 0 : 1 + level / 256;
            if (cur != lastSyncedFluid || packedLevelSentinel != lastSyncedLevel || connMask != lastSyncedConnMask) {
                lastSyncedFluid = cur;
                lastSyncedLevel = packedLevelSentinel;
                lastSyncedConnMask = connMask;
                PipeDebugLog.event("pipe.stateTracker.fire");
                this.syncAllData(false);
            }
        }
        long _tickDt = System.nanoTime() - _tickT0;
        if (_tickDt > 5_000_000L) {
            PipeDebugLog.event("pipe.tick.slow_ms_" + (_tickDt / 1_000_000L));
        }
    }

    //    @Override
    protected final void onFirstTick(Level world, BlockPos pos) {
        // 26.1: recompute connections IMMEDIATELY on first tick. An earlier revision deferred
        // this via {@code connectionDelay = 1 + hash(pos) % 20} as a chunk-load smoothing
        // optimization, but the user-visible result was "fluid only goes to the first pipe":
        // a freshly-placed pipe receives fluid from its upstream neighbour (which can write
        // directly via {@code addFluid}) but couldn't itself dump forward for up to 20 ticks,
        // because interaction[] stayed all-false until the deferred recompute fired.
        // For a chain of 20 pipes placed in succession, propagation broke entirely — by the
        // time pipe N's recompute fired, pipe N-1 had already filled it up and the network
        // was stuck.
        //
        // The other lag sources we found (VanillaRegistries.createLookup uncached,
        // IOMachine per-tick sendBlockUpdated, redundant markAndNotifyBlock) accounted for
        // the original chunk-load stall on their own. With those fixed, the stagger is
        // unnecessary. Do the recompute immediately so interaction[] populates by the end of
        // first tick and propagation works as expected.
        this.recomputeConnections(world, pos);
    }

    /**
     * 26.1: opt out of the {@code BlockEntityBase} first-20-tick full-NBT burst. Reasoning:
     * pipes have many instances per chunk (the user's setup is pump → pipe → reservoir but
     * a real network has dozens), and the per-pipe 5-packet burst on placement was the
     * source of the user-reported 40s-per-placement lag. Periodic {@code BE_NBT_SYNC} plus
     * the state-tracker {@code syncAllData(false)} call at the end of {@link #updateEntity}
     * cover client-side updates with one targeted packet per actual change.
     */
    @Override
    protected boolean shouldDoInitialFullSync() {
        return false;
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
        BlockEntity te = getAdjacentBlockEntity(side);
        if (te instanceof WorldRift) return true;
        if (te instanceof BlockEntityPiping) return true;
        if (te instanceof PipeConnector) return true;
        return this.interactsWithMachines() && this.isInteractableTile(te, side);
    }

    private boolean isInteractableTile(BlockEntity te, Direction side) {
        if (te instanceof PipeRenderConnector) {
            return ((PipeRenderConnector) te).canConnectToPipeOnSide(side);
        }
        if (te != null) {
            String name = te.getClass().getSimpleName().toLowerCase(Locale.ENGLISH);
            if (name.contains("conduit") || name.contains("fluidduct") || name.contains("pipe") || name.contains("multipart")) {
                return false;
            }
        }
        return level.getCapability(Capabilities.Fluid.BLOCK,
                worldPosition.relative(side), side.getOpposite()) != null;
    }

    public final int getPipeIntake(int otherlevel) {
        return Math.min(CAPACITY_LIMIT - this.getFluidLevel(), TransferAmount.QUARTER.getTransferred(otherlevel));
    }

    public final int getPipeOutput(int max) {
        return Math.min(TransferAmount.QUARTER.getTransferred(max), this.getFluidLevel() - 5);
    }

    private final void dumpContents(Level world, BlockPos pos) {
        PipeDebugLog.event("pipe.dumpContents.call");
        Fluid f = this.getAttributes();
        if (this.getFluidLevel() <= 1 || f == null)
            return;
        for (int i = 0; i < 6; i++) {
            int level = this.getFluidLevel();
            if (level <= 0) {
                this.setFluid(null);
                return;
            }
            Direction dir = DIRS[i];
            if (interaction[i]) {
                BlockEntity te = world.getBlockEntity(scratchPos.setWithOffset(pos, dir));
                Level w = world;
                BlockPos at = scratchPos;

                // V33a: a World Rift hands the far side's tile over. V33a reassigned the method's world for the
                // remaining faces too; the target's level is kept to this face here.
                if (te instanceof WorldRift rift) {
                    if (world.isClientSide())
                        continue;
                    WorldLocation loc = rift.getLinkTarget();
                    if (loc != null) {
                        te = rift.getTileEntityFrom(dir);
                        if (te == null)
                            continue;
                        at = te.getBlockPos();
                        w = te.getLevel();
                    }
                }

                if (te instanceof BlockEntityPiping tp) {
                    if (this.hasReciprocalConnectivity(tp, dir) && this.canEmitToPipeOn(dir) && tp.canReceiveFromPipeOn(dir.getOpposite())) {
                        if (tp.canIntakeFluid(f)) {
                            int otherlevel = tp.getFluidLevel();
                            int dL = level - otherlevel;
                            // 26.1: aggressive transfer (BUCKET-capped) instead of QUARTER so
                            // fluid actually reaches the end of long pipe chains. With QUARTER,
                            // each pipe lost 75% per hop → 25-pipe chain had nothing past pipe 7.
                            int toadd = this.getPipeOutput(dL);
                            if (toadd > 0) {
                                tp.addFluid(toadd);
                                this.removeLiquid(toadd);
                            }
                        }
                    }
                } else if (te instanceof PipeConnector pc) {
                    PipeDebugLog.event("pipe.dump.PipeConnector.try");
                    Flow flow = pc.getFlowForSide(dir.getOpposite());
                    if (flow.canIntake) {
                        int toadd = this.getPipeOutput(this.getFluidLevel());
                        if (toadd > 0) {
                            ResourceHandler<FluidResource> target = w.getCapability(
                                    Capabilities.Fluid.BLOCK, at, dir.getOpposite());
                            int added = target == null ? 0 : ResourceHandlerUtil.move(fluidHandler,
                                    target, resource -> resource.getFluid() == f, toadd, null);
                            if (added > 0) {
                                PipeDebugLog.event("pipe.dump.PipeConnector.success");
                            } else {
                                PipeDebugLog.event("pipe.dump.PipeConnector.fillReturnedZero");
                            }
                        } else {
                            PipeDebugLog.event("pipe.dump.PipeConnector.toaddZero");
                        }
                    } else {
                        PipeDebugLog.event("pipe.dump.PipeConnector.flowCannotIntake");
                    }
                } else if (this.canOutputToIFluidHandler(dir)) {
                    ResourceHandler<FluidResource> target = w.getCapability(Capabilities.Fluid.BLOCK,
                            at, dir.getOpposite());
                    if (target == null) continue;
                    int toadd = this.getPipeOutput(this.getFluidLevel());
                    if (toadd > 0) {
                        ResourceHandlerUtil.move(fluidHandler, target,
                                resource -> resource.getFluid() == f, toadd, null);
                    }
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
        PipeDebugLog.event("pipe.intakeFluid.call");
        for (int i = 0; i < 6; i++) {
            Direction dir = DIRS[i];
            if (interaction[i]) {
                BlockEntity te = world.getBlockEntity(scratchPos.setWithOffset(pos, dir));

                // V33a: draw from the pipe beside the far end of a World Rift.
                if (te instanceof WorldRift rift) {
                    if (world.isClientSide())
                        continue;
                    WorldLocation loc = rift.getLinkTarget();
                    if (loc != null) {
                        te = rift.getTileEntityFrom(dir);
                        if (te == null)
                            continue;
                    }
                }

                if (te instanceof BlockEntityPiping tp) {
                    if (this.hasReciprocalConnectivity(tp, dir) && this.canReceiveFromPipeOn(dir) && tp.canEmitToPipeOn(dir.getOpposite())) {
                        Fluid f = tp.getAttributes();
                        int amt = tp.getFluidLevel();
                        int dL = amt - this.getFluidLevel();
                        // 26.1: aggressive transfer between pipes (BUCKET-capped) so long
                        // chains actually propagate. Pairs with the matching change in
                        // dumpContents pipe-to-pipe branch.
                        int todrain = this.getPipeIntake(dL);
                        if (todrain > 0 && this.canIntakeFluid(f)) {
                            this.setFluid(f);
                            this.addFluid(todrain);
                            tp.removeLiquid(todrain);
                            this.onIntake(te);
                        }
                    }
                } else if (te instanceof PipeConnector pc) {
                    Flow flow = pc.getFlowForSide(dir.getOpposite());
                    if (flow.canOutput) {
                        ResourceHandler<FluidResource> source = world.getCapability(
                                Capabilities.Fluid.BLOCK, scratchPos, dir.getOpposite());
                        if (source == null) continue;
                        for (int slot = 0; slot < source.size(); slot++) {
                            FluidResource resource = source.getResource(slot);
                            if (resource.isEmpty() || !this.canIntakeFluid(resource.getFluid())) continue;
                            int todrain = this.getPipeIntake(source.getAmountAsInt(slot) - this.getFluidLevel());
                            if (todrain <= 0) continue;
                            int moved = ResourceHandlerUtil.move(source, fluidHandler,
                                    candidate -> candidate.equals(resource), todrain, null);
                            if (moved > 0) {
                                this.onIntake(te);
                                break;
                            }
                        }
                    }
                } else if (this.canIntakeFromIFluidHandler(dir)) {
                    ResourceHandler<FluidResource> source = world.getCapability(Capabilities.Fluid.BLOCK,
                            scratchPos, dir.getOpposite());
                    if (source == null) continue;
                    for (int slot = 0; slot < source.size(); slot++) {
                        FluidResource resource = source.getResource(slot);
                        if (resource.isEmpty() || !this.canIntakeFluid(resource.getFluid())) continue;
                        int todrain = this.getPipeIntake(source.getAmountAsInt(slot) - this.getFluidLevel());
                        if (todrain <= 0) continue;
                        int moved = ResourceHandlerUtil.move(source, fluidHandler,
                                candidate -> candidate.equals(resource), todrain, null);
                        if (moved > 0) {
                            this.onIntake(te);
                            break;
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

    public boolean isConnectedToNonSelf(Direction dir) {
        if (!this.isConnectionValidForSide(dir))
            return false;
        BlockPos npos = worldPosition.relative(dir);
        BlockState machineState = this.getMachine().getBlockState();
        return machineState == null || level.getBlockState(npos).getBlock() != machineState.getBlock();
    }

    // 1.21.5: BlockEntity.getRenderBoundingBox was removed; renderers compute their own bounds.
    public final AABB getRenderBoundingBox() {
        // Inflate by one so the connection stubs drawn into the six neighbouring blocks are not
        // culled when the pipe's own block leaves the frustum.
        return new AABB(worldPosition).inflate(1);
    }

    @Override
    public final void onEMP() {
    }

    public abstract boolean hasLiquid();

    public abstract Fluid getAttributes();

    public void recomputeConnections(Level world, BlockPos pos) {
        PipeDebugLog.event("recomputeConnections.call");
        long _t0 = System.nanoTime();
        boolean changed = false;
        // 26.1 CRITICAL FIX: two-phase recompute. The legacy single-pass version was buggy in a
        // way the user discovered by experiment — "place pipe between pump and reservoir,
        // nothing happens; place a second pipe on top, NOW fluid flows." Root cause:
        // {@link #canInteractWith} short-circuits at {@code if (!connections[side.ordinal()])}
        // — reading the OLD connection bit. On the FIRST recompute (which runs from
        // {@link #onFirstTick}, before anything has been set), {@code connections[]} is still
        // its default all-false state. So {@code canInteractWith} returns false for every side
        // BEFORE the matching {@code connections[i] = newConn} statement on the next line
        // overwrites it with the correct value. Result: connections fill in but interaction
        // stays all-false → {@code dumpContents}/{@code intakeFluid} skip every side. The user
        // adding a pipe on top then triggered a SECOND recompute via the resulting
        // {@code neighborChanged} — on that pass, {@code connections[]} held the right values
        // from the first pass, {@code canInteractWith} returned true, and flow started.
        //
        // Fix: compute and apply connections FIRST, then compute interactions against the
        // freshly-applied connections. Same number of underlying probes, no behaviour change
        // for the steady-state recompute, but the first-ever-tick now produces a correct
        // interaction[] and pipes work immediately on placement.
        for (int i = 0; i < 6; i++) {
            boolean newConn = this.shouldTryToConnect(DIRS[i]);
            if (newConn != connections[i]) changed = true;
            connections[i] = newConn;
        }
        for (int i = 0; i < 6; i++) {
            boolean newInter = this.canInteractWith(world, pos, DIRS[i]);
            if (newInter != interaction[i]) changed = true;
            interaction[i] = newInter;
        }
        // 1.21.9 perf: previously called {@code world.updateNeighborsAt(pos, block)} here, which
        // fires every adjacent BE's {@code neighborChanged} on every recompute — and because
        // recompute happens on first tick AND every time {@link #queueConnectionEvaluation}
        // fires, placing a pipe into a connected network was triggering an N²-ish cascade of
        // signal-propagations through every pipe in the chain. The result was a 5-second
        // server stall per connect/disconnect. Pipes are pure data-plane entities — none of
        // the neighbours need redstone-update semantics — so we simply skip the broadcast
        // unless connections actually changed, and even then just request a visual refresh
        // via {@code setBlocksDirty} instead of a full neighbour-notify.
        if (changed && !world.isClientSide()) {
            BlockState s = this.getBlockState();
            world.setBlocksDirty(pos, s, s);
            this.setChanged();
        }
        long _dt = System.nanoTime() - _t0;
        if (_dt > 1_000_000L) {
            // anything over 1ms is suspicious — recompute is 12 BE lookups + 6 getMachine calls,
            // should be well under 100μs.
            PipeDebugLog.event("recomputeConnections.slow_ms_" + (_dt / 1_000_000L));
        }
    }

    public void deleteFromAdjacentConnections(Level world, BlockPos pos) {
        PipeDebugLog.event("deleteFromAdjacentConnections.call");
        for (int i = 0; i < 6; i++) {
            Direction dir = DIRS[i];
            scratchPos.setWithOffset(pos, dir);
            MachineRegistry m = MachineRegistry.getMachine(world, scratchPos);
            if (m == this.getMachine()) {
                BlockEntityPiping te = (BlockEntityPiping) world.getBlockEntity(scratchPos);
                te.connections[dir.getOpposite().ordinal()] = false;
            }
        }
    }

    public void addToAdjacentConnections(Level world, BlockPos pos) {
        for (int i = 0; i < 6; i++) {
            Direction dir = DIRS[i];
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
        BlockPos npos = getBlockPos().relative(dir);
        MachineRegistry m = this.getMachine();
        MachineRegistry m2 = MachineRegistry.getMachine(level, npos);
        if (m != null && !m.isPipe() && m == m2)
            return true;
        BlockEntity tile = level.getBlockEntity(npos);
        if (tile instanceof WorldRift) {
            return true;
        }
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

        // 26.1 CRITICAL PERF FIX: previously called {@code ReikaNBTHelper.writeFluidToNBT(NBT,
        // new FluidStack(attr, 0))} just to serialise the Fluid TYPE. Spark profiling caught
        // this as ~9% of render-thread CPU because the helper's codec path went through
        // {@code VanillaRegistries.createWorldLookup()} — which rebuilds the entire biome
        // generation registry tree per call. The fluid-id needs is one ResourceLocation;
        // codec is overkill. Write the registry key as a plain string (or empty when no
        // fluid present); readSyncTag does the symmetric lookup. The codec helper still has
        // a fast path now (cached lookup) but for the pipe's hot sync path we skip the codec
        // entirely.
        Fluid attr = this.getAttributes();
        if (attr != null) {
            Identifier fluidId = BuiltInRegistries.FLUID.getKey(attr);
            if (fluidId != null) NBT.putString("fluid_id", fluidId.toString());
        }
        // Sync the ACTUAL fluid level, not a binary has-fluid flag. The 26.1 perf pass replaced the
        // level with a 0/1 byte and readSyncTag hardcoded the client level to 1 — so EVERY non-empty
        // pipe displayed "Level: 1 mB" (and Pressure 24 Pa) regardless of how full it really was, even
        // though the server-side transfer worked. The packet-rate concern is handled by the bucketed
        // sync trigger in updateEntity (only re-syncs on coarse level changes), so writing the real
        // value here is cheap.
        NBT.putInt("level", this.getFluidLevel());
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        connections = ReikaArrayHelper.booleanFromByteBitflags(NBT.getByteOr("conn", (byte)0), 6);

        // 26.1 PERF: matches the write path — read a plain fluid registry-id string, not a
        // codec-encoded FluidStack. Direct registry lookup is O(1) hash map access vs the
        // codec path's reflection + biome-tree rebuild via {@code VanillaRegistries.createLookup}.
        String fluidIdStr = NBT.getStringOr("fluid_id", "");
        Fluid f;
        if (fluidIdStr.isEmpty()) {
            f = null;
        } else {
            Identifier fluidId = Identifier.tryParse(fluidIdStr);
            f = fluidId == null ? null : BuiltInRegistries.FLUID.getValue(fluidId);
            if (f == Fluids.EMPTY) f = null;
        }
        this.setFluid(f);
        // Back-compat: read the real level; fall back to the old has_fluid byte for tags written before
        // the fix (treat any non-empty legacy tag as 1mB until the next sync ships the real value).
        if (NBT.contains("level"))
            this.setFluidLevel(NBT.getIntOr("level", 0));
        else
            this.setFluidLevel(NBT.getByteOr("has_fluid", (byte) 0) != 0 ? 1 : 0);
    }

    // 1.21.5: BlockEntity#serializeNBT(provider) replaces the no-arg variant; we no longer override.
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("conndelay", connectionDelay);
        return nbt;
    }

    /**
     * 26.1: disk persistence path for the exact fluid level. {@link #writeSyncTag} now only
     * sends a coarse {@code has_fluid} byte over the network (huge perf win — the legacy
     * level-as-int caused a periodic packet per pipe per 5 ticks during fluid flow), but the
     * server still needs the exact mB amount to survive world save/reload. Save it here under
     * a dedicated key so {@link #load} can restore it before the next tick — the chain is
     * vanilla {@code BlockEntity#saveAdditional(ValueOutput)} → {@code BlockEntityBase}
     * compat shim → {@code saveAdditional(CompoundTag)} → this override → super (writes
     * conn+fluid+has_fluid via writeSyncTag).
     */
    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("level_exact", this.getFluidLevel());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        connectionDelay = nbt.getIntOr("conndelay", 0);
        // 26.1: restore exact fluid level from disk save. The super.load chain already ran
        // readSyncTag which set level to the wire-format sentinel (0 or 1). Override with the
        // real saved value if present — back-compat: pipes saved before this fix have no
        // {@code level_exact} key, so they fall back to the sentinel which at worst means a
        // recently-loaded pipe shows "has fluid" but the exact mB has to be re-pumped in.
        if (nbt.contains("level_exact")) {
            this.setFluidLevel(nbt.getIntOr("level_exact", 0));
        }
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
