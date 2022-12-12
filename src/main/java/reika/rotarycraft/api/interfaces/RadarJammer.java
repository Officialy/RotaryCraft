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
 * An interface for entities to allow them to jam the mob radar. Typically used for Eldritch abominations and the like.
 */
public interface RadarJammer {

    /**
     * As long as the entity is in range and this returns true, the GUI displays only static.
     */
    boolean jamRadar(Level world, BlockPos radarPos);

}
