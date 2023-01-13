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
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.libraries.java.ReikaStringParser;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.Locale;

//@Strippable(value = {"buildcraft.api.transport.IPipeConnection"})
public abstract class PoweredLiquidReceiver extends PoweredLiquidBase implements PipeConnector {//, IPipeConnection {

    protected final HybridTank tank = new HybridTank(ReikaStringParser.stripSpaces(this.getName().toLowerCase(Locale.ENGLISH)), this.getCapacity());

    public PoweredLiquidReceiver(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public abstract Fluid getInputFluid();

//    @Override
//    public final FluidTankInfo[] getTankInfo(Direction from) {
//        return new FluidTankInfo[]{tank.getInfo()};
//    }

    public final int getLiquidLevel() {
        return tank.getFluidLevel();
    }
    public final FluidStack getContainedFluid() {
        return tank.getActualFluid();
    }

    public final void addLiquid(int amt) {
        tank.addLiquid(amt, this.getInputFluid());
    }

    //    @Override
    public final boolean canFill(Direction from, Fluid fluid) {
        return this.canReceiveFrom(from) && this.isValidFluid(fluid);
    }

    public boolean isValidFluid(Fluid f) {
        return f != null && f.equals(this.getInputFluid());
    }


    public abstract boolean canReceiveFrom(Direction from);

    @Override
    public final boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return this.canReceiveFrom(side.getOpposite()) && this.canConnectToPipe(p);
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
        tank.writeToNBT(tag);
        super.writeSyncTag(tag);
    }

    public final boolean isEmpty() {
        return tank.isEmpty();
    }

    public final boolean isFull() {
        return tank.isFull();
    }

}
