/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.api.power;

import net.minecraft.core.BlockPos;

/**
 * Implement this if your TE "spawns" power.
 */
public interface PowerGenerator {

    /**
     * The maximum amount of power your machine can ever generate, in watts.
     * If there is no cap, use Long.MAX_VALUE.
     */
    long getMaxPower();

    /**
     * The current power your machine is producing, in watts.
     */
    long getCurrentPower();

    /* These three are again RC bridge methods. */
    BlockPos getEmittingPos(BlockPos pos);

}
