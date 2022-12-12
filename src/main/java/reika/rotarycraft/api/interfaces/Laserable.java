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
 * Make a TE, Block, or Entity implement this to have the heat ray give it special effects.
 */
public interface Laserable {

    /**
     * This is called every tick the TE/Entity/Block is in the beam of the Heat Ray.
     * Args: Heat Ray power input, distance from heat ray
     */
    void whenInBeam(Level world, BlockPos pos, long power, int range);

    /**
     * Whether the object blocks the beam. Returning true shields everything behind it.
     */
    boolean blockBeam(Level world, BlockPos pos, long power);

}
