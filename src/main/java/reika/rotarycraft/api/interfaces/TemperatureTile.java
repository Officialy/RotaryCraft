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
 * All TileEntities with a temperature implement this, possibly indirectly.
 */
public interface TemperatureTile extends BasicMachine {

    /**
     * For fetching the temperature for logic or display
     */
    int getTemperature();

    /**
     * For overheating tests. Not all tiles actually fail on overheat; some just clamp the temperature.
     */
    int getMaxTemperature();

}
