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

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

/**
 * Implement this to make the spawner controller able to take control of your spawner. This entails total control over the spawn cycle,
 * including delay, timing, and total shutdown regardless of spawn conditions or player proximity. Contrary to what seems to be commonly
 * believed, you do <b>NOT</b> need to extend BlockEntityMobSpawner to use this.
 */
public interface ControllableSpawner {

    int getCurrentSpawnDelay();

    /**
     * Set the cooldown. Can be zero, but not negative.
     */
    void setCurrentSpawnDelay(int delay);

    /**
     * Run a full spawn cycle, spawning mobs, particles, any any other operations.
     * This may be called up to every tick, and is always called from both client and server. Return the list of spawned entities; do NOT return null.
     */
    Collection<Entity> performSpawnCycle();

    /**
     * Set the spawner inactive, so that it will not spawn or do anything for the next tick or few.
     * This is called every tick if the controller is set to disable the spawner, so a vanilla-style spawner implementation
     * would set the countdown up a few ticks, to prevent a spawn before the controller allows it.
     */
    void setInactive();

    /**
     * The class object of the entity spawned by this spawner.
     */
    Class<? extends LivingEntity> getSpawningEntityClass();

}
