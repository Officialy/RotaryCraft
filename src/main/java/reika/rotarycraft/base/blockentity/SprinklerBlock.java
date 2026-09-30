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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.interfaces.blockentity.HasFluidResourceHandler;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.blockentities.piping.BlockEntityPipe;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.SoundRegistry;

public abstract class SprinklerBlock extends RotaryCraftBlockEntity implements PipeConnector, RangedEffect, HasFluidResourceHandler {

    private final StepTimer soundTimer = new StepTimer(40);
    private int liquid;
    private int pressure;
    private final ResourceHandler<FluidResource> fluidHandler = new WaterHandler();

    private final class WaterHandler extends SnapshotJournal<Integer> implements ResourceHandler<FluidResource> {
        @Override public int size() { return 1; }
        @Override public FluidResource getResource(int index) {
            if (index != 0) throw new IndexOutOfBoundsException(index);
            return liquid > 0 ? FluidResource.of(Fluids.WATER) : FluidResource.EMPTY;
        }
        @Override public long getAmountAsLong(int index) {
            if (index != 0) throw new IndexOutOfBoundsException(index);
            return liquid;
        }
        @Override public long getCapacityAsLong(int index, FluidResource resource) {
            if (index != 0) throw new IndexOutOfBoundsException(index);
            return resource.isEmpty() || isValid(index, resource) ? getCapacity() : 0;
        }
        @Override public boolean isValid(int index, FluidResource resource) {
            if (index != 0) throw new IndexOutOfBoundsException(index);
            return resource.equals(FluidResource.of(Fluids.WATER));
        }
        @Override public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
            TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
            if (!isValid(index, resource) || amount == 0) return 0;
            int accepted = Math.min(amount, Math.max(0, getCapacity() - liquid));
            if (accepted > 0) {
                updateSnapshots(transaction);
                liquid += accepted;
            }
            return accepted;
        }
        @Override public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
            TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
            if (index != 0) throw new IndexOutOfBoundsException(index);
            return 0;
        }
        @Override protected Integer createSnapshot() { return liquid; }
        @Override protected void revertToSnapshot(Integer snapshot) { liquid = snapshot; }
        @Override protected void onRootCommit(Integer originalState) { setChanged(); }
    }

    @Override
    public ResourceHandler<FluidResource> getFluidHandler(Direction side) {
        return side == null || side == this.getPipeDirection() ? fluidHandler : null;
    }

    public SprinklerBlock(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    private void getLiq(Level world, BlockPos pos) {
        if (world.isClientSide()) return;
        Direction dir = this.getPipeDirection();
        BlockPos neighborPos = pos.relative(dir);
        MachineRegistry m = MachineRegistry.getMachine(world, neighborPos);
        if (m != null && m.isStandardPipe()) {
            BlockEntityPipe tile = (BlockEntityPipe) world.getBlockEntity(neighborPos);
            if (tile != null && tile.contains(Fluids.WATER) && tile.getFluidLevel() > 0) {
                if (liquid < this.getCapacity()) {
                    int toremove = tile.getFluidLevel() / 4 + 1;
                    int toadd = Math.min(toremove, this.getCapacity() - liquid);
                    var source = world.getCapability(Capabilities.Fluid.BLOCK, neighborPos, dir.getOpposite());
                    if (source != null) ResourceHandlerUtil.move(source, fluidHandler,
                            resource -> resource.equals(FluidResource.of(Fluids.WATER)), toadd, null);
                }
                pressure = tile.getPressure();
            }
        }
        if (liquid > this.getCapacity())
            liquid = this.getCapacity();
    }

    public abstract int getCapacity();

    public abstract int getWaterConsumption();

    public abstract Direction getPipeDirection();

    //@Override
    public final void tick(Level world, BlockPos pos) {
        super.updateEntity();
        this.getLiq(world, pos);

        if (this.canPerformEffects()) { //&& !AtmosphereHandler.isNoAtmo(world, new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()), getType(), false)) { todo atmosphere checking
            this.performEffects(world, pos);
            soundTimer.update();
            if (soundTimer.checkCap()) {
                SoundRegistry.SPRINKLER.playSoundAtBlock(world, pos, 1, 1);
            }
            liquid -= this.getWaterConsumption();
        }

        this.doAnimations();
    }

    protected void doAnimations() {

    }

    public final boolean canPerformEffects() {
        return this.getRange() > 0 && liquid >= this.getWaterConsumption();
    }

    protected abstract void performEffects(Level world, BlockPos pos);

    public final int getWater() {
        return liquid;
    }

    public final int getPressure() {
        return pressure;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putInt("press", pressure);
        NBT.putInt("lvl", liquid);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        pressure = NBT.getIntOr("press", 0);
        liquid = NBT.getIntOr("lvl", 0);

        if (liquid < 0)
            liquid = 0;
        if (liquid > this.getCapacity())
            liquid = this.getCapacity();
        if (pressure < 0)
            pressure = 0;
    }

    @Override
    public final int getRange() {
        int val = 0;
        if (pressure <= 0)
            return 0;
        val = pressure / 80;
        if (val > this.getMaxRange())
            val = this.getMaxRange();
        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", val));
        return val;
    }

    @Override
    public final int getMaxRange() {
        return 8;
    }

    @Override
    public final boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe();
    }

    @Override
    public final boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return side == this.getPipeDirection() && p.isStandardPipe();
    }


    public boolean canFill(Direction side, Fluid f) {
        return f.equals(Fluids.WATER) && side == this.getPipeDirection();
    }

    @Override
    public final Flow getFlowForSide(Direction side) {
        return side == this.getPipeDirection() ? Flow.INPUT : Flow.NONE;
    }

}
