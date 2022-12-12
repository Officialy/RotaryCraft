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
 * Implement this on a block class to make it recognized as bedrock by the bedrock breaker. Used for things like worldgen-altering mods or mods
 * like ExNBridge which add alternative bedrock-type materials.
 **/
public interface SurrogateBedrock {

    boolean isBedrock(Level world, BlockPos pos);

    /**
     * Returns a float from zero to one to multiply against the bedrock dust yield. Values are clamped such that at least one dust is always dropped
     * and so that values never exceed that of vanilla bedrock (1-3 dust per block depending on difficulty settings).
     */
    //Currently unused
    float getYield(Level world, BlockPos pos);

}
