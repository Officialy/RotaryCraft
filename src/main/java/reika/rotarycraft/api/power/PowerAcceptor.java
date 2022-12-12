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
 * Anything that receives power implements this, usually indirectly. Do not implement this directly.
 */
public interface PowerAcceptor extends ShaftMachine {

    /**
     * Can the machine receive power from this side. Usually either "dir == facing" for 1D machines and "true" for omnidirectional machines.
     */
    boolean canReadFrom(Direction dir);

    /**
     * Whether your machine is able to receive power right now. If this returns false, no other power code is called.
     */
    boolean isReceiving();

    /**
     * The minimum torque the machine requires to operate. Also controls flywheel deceleration; a value of zero means flywheels driving it
     * never decelerate, yielding infinite energy. To have no effective minimum, pick a value of one; a torque of zero yields zero power.
     * Pick something reasonable, preferably as realistic as possible.
     */
    int getMinTorque(int available);

}
