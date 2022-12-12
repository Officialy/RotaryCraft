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
 * A base class for the shaft power interfaces. Do not implement this directly; doing so is harmless but useless.
 */
public interface ShaftMachine {

    /**
     * For fetching the current rotational speed. This can be called from
     * both client and server, so ensure that the TE is prepared for that.
     */
    int getOmega();

    /**
     * For fetching the current torque. This can be called from
     * both client and server, so ensure that the TE is prepared for that.
     */
    int getTorque();

    /**
     * For fetching the current power value. This can be called from
     * both client and server, so ensure that the TE is prepared for that.
     */
    long getPower();

    /**
     * For when to write it to chat or the like. This can be called from
     * both client and server, so ensure that the TE is prepared for that.
     */
    String getName();

    /**
     * Analogous to BlockEntityIOMachine's "iotick". Used to control I/O render opacity.
     */
    int getIORenderAlpha();

    /**
     * Analogous to BlockEntityIOMachine's "iotick". Used to control I/O render opacity.
     * This one is called by tools.
     */
    void setIORenderAlpha(int io);

}
