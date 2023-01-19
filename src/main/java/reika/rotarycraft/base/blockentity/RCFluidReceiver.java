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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.registry.MachineRegistry;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.libraries.java.ReikaStringParser;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Locale;

//@Strippable(value = {"buildcraft.api.transport.IPipeConnection"})
public abstract class RCFluidReceiver extends RotaryCraftBlockEntity implements IFluidHandler, PipeConnector { //, IPipeConnection {

    protected final HybridTank tank = new HybridTank(ReikaStringParser.stripSpaces(this.getName().toLowerCase(Locale.ENGLISH)), this.getCapacity());

    public RCFluidReceiver(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract int getCapacity();

    public abstract Fluid getInputFluid();

    public final int getFluidLevel() {
        return tank.getFluidLevel();
    }

    public final Fluid getContainedFluid() {
        return tank.getActualFluid().getFluid();
    }

    public final void addLiquid(int amt) {
        tank.addLiquid(amt, this.getInputFluid());
    }

    public final boolean canFill(Fluid fluid) {
        return this.isValidFluid(fluid);
    }

    public boolean isValidFluid(Fluid f) {
        return f != null && f.equals(this.getInputFluid());
    }

    @Override
    public MachineRegistry getMachine() {
        return null;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getTanks() {
        return 0;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return null;
    }

    @Override
    public int getTankCapacity(int tank) {
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {

        if (!this.canFill(resource.getFluid()))
            return 0;
        return tank.fill(resource, action);
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return false;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, FluidAction action) {
        return 0;
    }

    public abstract boolean canReceiveFrom(Direction from);

    @Override
    public final boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return this.canReceiveFrom(side.getOpposite()) && this.canConnectToPipe(p);
    }
//
//    public final ConnectOverride overridePipeConnection(PipeType type, Direction side) {
//        return type == PipeType.FLUID ? (this.canReceiveFrom(side) ? ConnectOverride.CONNECT : ConnectOverride.DISCONNECT) : ConnectOverride.DEFAULT;
//    }

    @Override
    public final Flow getFlowForSide(Direction side) {
        return this.canReceiveFrom(side) ? Flow.INPUT : Flow.NONE;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        tank.readFromNBT(NBT);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        tank.writeToNBT(NBT);
    }

    public final boolean isEmpty() {
        return tank.isEmpty();
    }

    public final boolean isFull() {
        return tank.isFull();
    }

}
