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

/**
 * Use this on TileEntities to make them have special behavior if hit with a discharge from the Van De Graff generator.
 */
public interface Shockable {

    /**
     * Called when the VDG hits the BlockEntity with a bolt. Args: Charge in the bolt, distance of hit
     */
    void onDischarge(int charge, double range);

    /**
     * The minimum charge the VDG must attain before attempting to hit the BlockEntity
     */
    int getMinDischarge();

    /**
     * Can this BlockEntity accept discharges from a range greater than one meter (block)
     */
    boolean canDischargeLongRange();

    /**
     * Where in the block to aim the bolt tip (X-axis). Normal range 0-1.
     */
    BlockPos getAimPos();

}
