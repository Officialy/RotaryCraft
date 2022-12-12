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
 * Implement this on an entity to control how it can be deflected, i.e. blown around, by a fan.
 */
public interface CustomFanEntity {

    /**
     * The required power to be able to blow this entity.
     */
    long getBlowPower();

    /**
     * The maximum speed-vector change. A magnitude, not an angle.
     */
    double getMaxDeflection();

}
