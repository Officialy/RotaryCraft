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
 * All TileEntities with a pressure implement this, possibly indirectly.
 */
public interface PressureTile extends BasicMachine {

    /**
     * For fetching the pressure for logic or display
     */
    int getPressure();

    /**
     * For overpressure tests. Not all tiles actually fail; some just clamp the pressure.
     */
    int getMaxPressure();

}
