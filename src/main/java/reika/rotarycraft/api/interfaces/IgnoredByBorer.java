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
 * This interface - intended to be implemented by blocks, <i>not</i> TileEntities - allows for some custom control over how the Borer
 * interacts with your block.<br>Note that this is mainly to fix bugs like gas blocks jamming the machine; this API does not and will
 * not provide a way to make the block unbreakable by the Borer.<br>That can only be done by making the block truly unbreakable (with a
 * negative hardness).<br>Note that if your block extends BlockLiquid or BlockFluidBase, fluid handling code takes over and implementing
 * this interface will have no effect. Similarly, if isAirBlock() returns true, the Borer will ignore its hardness no matter what.
 */
public interface IgnoredByBorer {

    /**
     * If this block returns true, the Borer will completely ignore the hardness of the block when calculating required torque and power.
     * The return value of this method has no bearing on the drops; if the getDrops() method returns a nonempty list, those items
     * will still drop upon block destruction, or the block itself will drop on a silk-touch-enchanted Borer (unless isAirBlock()
     * returns true).<br>
     * Arguments: Level, dimension, pos, metadata
     */
    boolean ignoreHardness(Level world, int dimID, BlockPos pos);

}
