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
import net.minecraft.world.level.BlockGetter;

/**
 * Implement this on a block class that is supposed to act as an environmental heat source or sink (eg ice, lava, water, plasma)
 */
public interface EnvironmentalHeatSource {

    /**
     * Returns the general type of thermal source the object is. Informs the approximate temperature.
     */
    SourceType getSourceType(BlockGetter getter, BlockPos pos);

    /**
     * If this returns false, the temperature effects do not apply.
     */
    boolean isActive(BlockGetter getter, BlockPos pos);

    enum SourceType {
        /**
         * Icy cold objects. Around -10C.
         */
        ICY(-10),

        /**
         * Cool water. Around 15C.
         */
        WATER(15),

        /**
         * Fire temperature; around 300C.
         */
        FIRE(300),

        /**
         * Lava-hot. Around 1200C.
         */
        LAVA(1200),

        /**
         * Ambient; effectively a no-op.
         */
        AMBIENT(25),

        /**
         * Cryogenic, 77K (liquid N2) or less.
         */
        CRYO(77 - 273),

        /**
         * Stellar-level temperatures, seen only in star cores, fusion reactors, and nuclear explosions.
         */
        SOLAR(150000000);

        public final int approxTemperature;

        SourceType(int t) {
            approxTemperature = t;
        }

        public boolean isHot() {
            return approxTemperature > 30;
        }

        public boolean isCold() {
            return approxTemperature <= 0;
        }
    }

}
