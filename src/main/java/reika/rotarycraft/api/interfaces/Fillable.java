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

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.fluids.FluidStack;

/**
 * For items that use fuel or some other fluid and can be filled in the filling station.
 */
public interface Fillable {

    /**
     * Return true if the item can currently accept this fluid.
     */
    boolean isValidFluid(FluidStack f, ItemStack is);

    /**
     * The max amount of fluid (in millibuckets) the item can take.
     */
    int getCapacity(ItemStack is);

    /**
     * The current fluid level in millibuckets.
     */
    int getCurrentFillLevel(ItemStack is);

    /**
     * This adds fluid to the item and returns how much was successfully added.
     */
    int addFluid(ItemStack is);

    boolean isFull(ItemStack is);

    Fluid getCurrentFluid(ItemStack is);

}
