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
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.registry.MachineRegistry;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.libraries.java.ReikaStringParser;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;

import java.util.Locale;

//@Strippable(value = {"buildcraft.api.transport.IPipeConnection"})
public abstract class PoweredLiquidInOut extends PoweredLiquidBase implements PipeConnector {//, IPipeConnection {

    protected final HybridTank tank = new HybridTank(ReikaStringParser.stripSpaces(this.getName().toLowerCase(Locale.ENGLISH)), this.getCapacity());

    public PoweredLiquidInOut(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

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

    //@Override
    public final boolean canFill(Direction from, Fluid fluid) {
        return this.canReceiveFrom(from) && this.isValidFluid(fluid);
    }

    public boolean isValidFluid(Fluid f) {
        return f != null && f.equals(this.getInputFluid());
    }

    public abstract boolean canReceiveFrom(Direction from);

    //    @Override
    public final boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        //return this.canReceiveFrom(side.getOpposite()) && this.canConnectToPipe(p);
        return true;
    }

//    public final ConnectOverride overridePipeConnection(PipeType type, Direction side) {
//        return type == PipeType.FLUID ? (this.canReceiveFrom(side) ? ConnectOverride.CONNECT : ConnectOverride.DISCONNECT) : ConnectOverride.DEFAULT;
//    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        tank.readFromNBT(tag);
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

}
