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
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;

/** Original non-cancellable notification, posted once before a kinetic impact changes terrain. */
public final class RailgunImpactEvent extends Event {
    public final int blockX, blockY, blockZ, projectileMass;
    public final Level world;
    public RailgunImpactEvent(Level world, BlockPos position, int tier) {
        this.world = world; blockX = position.getX(); blockY = position.getY(); blockZ = position.getZ();
        projectileMass = 1 << Math.clamp(tier, 0, 15);
    }
    public RailgunImpactEvent(Level world, int x, int y, int z, int tier) { this(world, new BlockPos(x, y, z), tier); }
}
