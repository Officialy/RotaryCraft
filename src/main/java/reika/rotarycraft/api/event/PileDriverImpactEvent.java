/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.api.event;

import net.minecraft.core.BlockPos;
import reika.dragonapi.instantiable.event.TileEntityEvent;
import reika.rotarycraft.blockentities.level.BlockEntityPileDriver;

/** Original non-cancellable notification, posted before the impact changes any block. */
public final class PileDriverImpactEvent extends TileEntityEvent {
    public final int centerX, centerY, centerZ;
    public PileDriverImpactEvent(BlockEntityPileDriver tile, BlockPos center) {
        super(tile); centerX = center.getX(); centerY = center.getY(); centerZ = center.getZ();
    }
}
