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

import net.minecraft.core.Direction;

/**
 * Implementing this gives you extremely basic boolean sensitivity to RC power. Used for things like switches and detectors, and unsuitable
 * for actual machines due to the total lack of sensitivity to the amount of power or its direction.
 */
public interface SimpleShaftPowerReceiver {

    void setPowered(boolean power);

    boolean canReadFrom(Direction dir);

}
