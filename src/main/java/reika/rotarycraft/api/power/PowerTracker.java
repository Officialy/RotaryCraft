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

public interface PowerTracker {

    long getMaxGennablePower();

    long getRealMaxPower();

    boolean isLooping();

    boolean contains(PowerGenerator te);

    boolean calledFrom(ShaftMerger sm);

    boolean passesThrough(ShaftMerger sm);

    boolean isEngineSpam();

}
