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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.storage.FilteredFluidResourceHandler;
import reika.dragonapi.instantiable.storage.HybridTankResourceHandler;
import reika.dragonapi.interfaces.blockentity.HasFluidResourceHandler;
import reika.dragonapi.libraries.ReikaFluidHelper;
import reika.dragonapi.libraries.java.ReikaStringParser;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.Locale;

//@Strippable(value = {"buildcraft.api.transport.IPipeConnection"})
public abstract class PoweredLiquidIO extends PoweredLiquidBase implements PipeConnector, HasFluidResourceHandler {//}, IPipeConnection {

    protected final HybridTank output = new HybridTank(ReikaStringParser.stripSpaces(this.getName().toLowerCase(Locale.ENGLISH) + "out"), this.getOutputCapacity());
    protected final HybridTank input = new HybridTank(ReikaStringParser.stripSpaces(this.getName().toLowerCase(Locale.ENGLISH) + "in"), this.getInputCapacity());
    private final ResourceHandler<FluidResource> fluidHandler = new HybridTankResourceHandler(
            new HybridTank[]{input, output},
            (index, resource) -> index == 0 && isValidFluid(resource.getFluid()),
            (index, resource) -> index == 1, this::setChanged);
    private final ResourceHandler<FluidResource> inputFluidView = new FilteredFluidResourceHandler(
            fluidHandler, index -> index == 0,
            (index, resource) -> true, (index, resource) -> false);
    private final ResourceHandler<FluidResource> outputFluidView = new FilteredFluidResourceHandler(
            fluidHandler, index -> index == 1,
            (index, resource) -> false, (index, resource) -> true);

    public PoweredLiquidIO(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

//    @Override
//    public final FluidStack drain(Direction from, FluidStack resource, boolean doDrain) {
//        return this.canDrain(from, resource.getFluid()) ? output.drain(resource.getAmount(), doDrain) : null;
//    }

    public abstract Fluid getInputFluid();


//        @Override
    public final boolean canDrain(Direction from, FluidStack fluid) {
        return this.canOutputTo(from) && ReikaFluidHelper.isFluidDrainableFromTank(fluid, output);
    }

//    @Override
//    public final FluidTankInfo[] getTankInfo(Direction from) {
//        return new FluidTankInfo[]{input.getInfo(), output.getInfo()};
//    }

    public final int getInputLevel() {
        return input.getFluidLevel();
    }

    public final int getOutputLevel() {
        return output.getFluidLevel();
    }

    public final FluidStack getFluidInInput() {
        return input.getActualFluid();
    }

    public final FluidStack getFluidInOutput() {
        return output.getActualFluid();
    }

    public final void removeLiquid(int amt) {
        output.removeLiquid(amt);
    }

    public final void addLiquid(int amt) {
        input.addLiquid(amt, this.getInputFluid());
    }

//        @Override
    public final boolean canFill(Direction from, Fluid fluid) {
        return this.canReceiveFrom(from) && this.isValidFluid(fluid);
    }

    public boolean isValidFluid(Fluid f) {
        return f.equals(this.getInputFluid());
    }

    public int getInputCapacity() {
        return this.getCapacity();
    }

    public int getOutputCapacity() {
        return this.getCapacity();
    }

    public abstract boolean canOutputTo(Direction to);

    public abstract boolean canReceiveFrom(Direction from);

    @Override
    public ResourceHandler<FluidResource> getFluidHandler(Direction side) {
        if (side == null) return fluidHandler;
        if (canOutputTo(side)) return outputFluidView;
        return canReceiveFrom(side) ? inputFluidView : null;
    }

    @Override
    public final boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return (this.canReceiveFrom(side) && this.canIntakeFromPipe(p)) || (this.canOutputTo(side) && this.canOutputToPipe(p)) && this.canConnectToPipe(p);
    }

    public abstract boolean canIntakeFromPipe(MachineRegistry p);

    public abstract boolean canOutputToPipe(MachineRegistry p);

//    public final ConnectOverride overridePipeConnection(PipeType type, Direction side) {
//        return type == PipeType.FLUID ? ((this.canOutputTo(side) || this.canReceiveFrom(side)) ? ConnectOverride.CONNECT : ConnectOverride.DISCONNECT) : ConnectOverride.DEFAULT;
//    }

    @Override
    public final Flow getFlowForSide(Direction side) {
        if (this.canOutputTo(side))
            return Flow.OUTPUT;
        else if (this.canReceiveFrom(side))
            return Flow.INPUT;
        return Flow.NONE;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        input.readFromNBT(NBT);
        output.readFromNBT(NBT);
        super.readSyncTag(NBT);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        input.writeToNBT(NBT);
        output.writeToNBT(NBT);
        super.writeSyncTag(NBT);
    }

}
