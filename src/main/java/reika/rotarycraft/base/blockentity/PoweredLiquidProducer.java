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
import org.jetbrains.annotations.NotNull;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.libraries.java.ReikaStringParser;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.Locale;

//@Strippable(value = {"buildcraft.api.transport.IPipeConnection"})
public abstract class PoweredLiquidProducer extends PoweredLiquidBase implements IFluidHandler, PipeConnector {//}, IPipeConnection {

    protected final HybridTank tank = new HybridTank(ReikaStringParser.stripSpaces(this.getTEName().toLowerCase(Locale.ENGLISH)), this.getCapacity());

    public PoweredLiquidProducer(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        return this.canDrain(resource.getFluid()) ? tank.drain(resource.getAmount(), action) : null;
    }

    public final boolean canDrain(Fluid fluid) {
//        return this.canOutputTo(from) && ReikaFluidHelper.isFluidDrainableFromTank(fluid, tank);
        return true;
    }

//    @Override
//    public final FluidTankInfo[] getTankInfo(Direction from) {
//        return new FluidTankInfo[]{tank.getInfo()};
//    }

    public final int getFluidLevel() {
        return tank.getFluidLevel();
    }

    public Fluid getContainedFluid() {
        return tank.getActualFluid().getFluid();
    }

    public final void removeLiquid(int amt) {
        tank.removeLiquid(amt);
    }

    public abstract boolean canOutputTo(Direction to);

    @Override
    public final boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return this.canOutputTo(side) && this.canConnectToPipe(p);
    }

    @Override
    public final Flow getFlowForSide(Direction side) {
        return this.canOutputTo(side) ? Flow.OUTPUT : Flow.NONE;
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

}
