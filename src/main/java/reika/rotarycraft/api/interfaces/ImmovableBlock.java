/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.api.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Implement this on your block class to make the Block Ram unable to push your block.
 */
public interface ImmovableBlock {

    /**
     * Return true to allow the Block Ram to push the block.
     * Args: Level, pos, distance between block ram and block, block ram torque, block ram power
     * Torque ranges from 1 to a few million (or more if the player likes wasting power), whereas power ranges from 1024 or so to 67 million or so.
     * Consult RotaryCraft to determine average and expected power values.
     */
    boolean canBePushed(Level world, BlockPos pos, int distance, int torque, long power);

}
