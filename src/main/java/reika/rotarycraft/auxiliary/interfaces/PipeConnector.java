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
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.registry.MachineRegistry;

/**
 * Marks a machine that RotaryCraft/ReactorCraft pipes connect to and describes that connection.
 *
 * <p>Fluid itself moves through the machine's {@code Capabilities.Fluid.BLOCK} handler
 * ({@link reika.dragonapi.interfaces.blockentity.HasFluidResourceHandler}); the 1.7.10
 * {@code fill}/{@code drain} pair this interface used to carry is replaced by that handler.
 * To declare a machine output-only, give the pipe-facing view no insertable tank; to declare it
 * input-only, give that view no extractable tank.
 */
public interface PipeConnector {

    boolean canConnectToPipe(MachineRegistry m);

    /**
     * Side is relative to the piping block (so DOWN means the block is below the pipe); p is the pipe type
     * Example of DOWN direction:
     * [PIPE]
     * [BLOCK]
     */
    boolean canConnectToPipeOnSide(MachineRegistry m, Direction side);

    BlockEntityPiping.Flow getFlowForSide(Direction side);
}
