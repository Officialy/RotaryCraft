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

/**
 * Use this for entities that do not extend {@link net.minecraft.world.entity.FlyingMob} to mark them as flying-type entities.
 * Implementing this will, among other things, make the RotaryCraft Anti-Air gun target them.
 */
public interface FlyingMob {

    /**
     * Does the mob try to seek and harm players. Similar to {@code instanceof IMob}, but with more control on a per-entity basis.
     */
    boolean isHostile();

    /**
     * Whether the fly mode is active; always true for things like Ghasts; intermittently true for things like Blazes (Blazes only when aggro)
     */
    boolean isCurrentlyFlying();

}
