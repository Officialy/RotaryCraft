/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary.interfaces;

import net.minecraft.core.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.registry.MachineRegistry;

/**
 * To declare a machine output-only, return 0 for addFluid for all cases.
 * To declare a machine input-only, return 0 for removeFluid for all cases.
 */
public interface PipeConnector {

    boolean canConnectToPipe(MachineRegistry m);

    /**
     * Side is relative to the piping block (so DOWN means the block is below the pipe); p is the pipe type
     */
    boolean canConnectToPipeOnSide(MachineRegistry m, Direction side);

    int fill(Direction from, FluidStack resource, IFluidHandler.FluidAction action);

    FluidStack drain(Direction from, int maxDrain, boolean doDrain);

    BlockEntityPiping.Flow getFlowForSide(Direction side);
}
