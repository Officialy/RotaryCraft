/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.api.power;


/**
 * If your machine merges two shafts, implement this to avoid being the source of a loop exploit or StackOverflow
 */
public interface ShaftMerger {

    void onPowerLooped(PowerTracker pwr);

    void fail();

}
