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
 * Implement this to make your block capable of generating RC power.
 */
public interface ShaftPowerEmitter extends ShaftMachine, PowerGenerator {

    /**
     * Side to write to
     */
    boolean canWriteTo(Direction dir);

    /**
     * Whether your machine is emitting power right now
     */
    boolean isEmitting();

}