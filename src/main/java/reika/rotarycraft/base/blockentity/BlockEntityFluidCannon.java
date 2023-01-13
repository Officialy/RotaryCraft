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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.libraries.java.ReikaStringParser;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.Locale;

public abstract class BlockEntityFluidCannon extends BlockEntityAimedCannon implements IFluidHandler, PipeConnector { //, IPipeConnection {

    protected final HybridTank tank = new HybridTank(ReikaStringParser.stripSpaces(this.getName().toLowerCase(Locale.ENGLISH)), this.getCapacity());

    public BlockEntityFluidCannon(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public final boolean hasAmmo() {
        return !tank.isEmpty();
    }

    protected int getCapacity() {
        return 1000;
    }

    public final int getFluidLevel() {
        return tank.getFluidLevel();
    }

    public final Fluid getContainedFluid() {
        return tank.getActualFluid().getFluid();
    }

    public final void addLiquid(Fluid f, int amt) {
        if (this.isValidFluid(f))
            tank.addLiquid(amt, f);
    }

    public final boolean canFill(Direction from, Fluid fluid) {
        return this.canReceiveFrom(from) && this.isValidFluid(fluid);
    }

    public abstract boolean isValidFluid(Fluid f);

    @Override
    public final int fill(Direction from, FluidStack resource, FluidAction doFill) {
        if (!this.canFill(from, resource.getFluid()))
            return 0;
        return tank.fill(resource, doFill);
    }

    public abstract boolean canReceiveFrom(Direction from);

    //@Override
    public final boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        //return this.canReceiveFrom(side.getOpposite()) && this.canConnectToPipe(p);
        return true; //todo fix this later
    }

//    public final ConnectOverride overridePipeConnection(PipeType type, Direction side) {
//        return type == PipeType.FLUID ? (this.canReceiveFrom(side) ? ConnectOverride.CONNECT : ConnectOverride.DISCONNECT) : ConnectOverride.DEFAULT;
//    }

    @Override
    public final Flow getFlowForSide(Direction side) {
        return this.canReceiveFrom(side) ? Flow.INPUT : Flow.NONE;
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        tank.readFromNBT(tag);
        super.readSyncTag(tag);
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tank.writeToNBT(tag);
    }

    public final boolean isEmpty() {
        return tank.isEmpty();
    }

    public final boolean isFull() {
        return tank.isFull();
    }

    @Override
    protected final double randomOffset() {
        return 0;
    }

    @Override
    public final int getOperationTime() {
        return 0;
    }

}
