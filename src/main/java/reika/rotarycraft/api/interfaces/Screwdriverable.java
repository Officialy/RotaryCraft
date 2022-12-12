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
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

/**
 * Implement this to make your block able to have the screwdriver used on it. Overrides normal IWrench behavior if your block also acts on that.
 */
public interface Screwdriverable {

    /**
     * Return true to prevent further processing.
     */
    boolean onShiftRightClick(Level world, BlockPos pos, Direction side);

    /**
     * Return true to prevent further processing.
     */
    boolean onRightClick(Level world, BlockPos pos, Direction side);

}
