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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

/**
 * Implement this to make your crop have special compatibility with fans that goes beyond the raw harvesting already implemented.
 */
@Deprecated
public interface BlowableCrop {

    boolean isReadyToHarvest(Level world, BlockPos pos);

    /**
     * Usually sets the metadata to zero.
     */
    void setPostHarvest(Level world, BlockPos pos);

    /**
     * Similar to and often returns the value of world.getBlock(pos).getDrops(world, pos, world.getBlockMetadata(), 0)
     */
    ArrayList<ItemStack> getHarvestProducts(Level world, BlockPos pos);

    /**
     * A simple multiplier. Higher numbers harvest faster.
     */
    float getHarvestingSpeed();

}
