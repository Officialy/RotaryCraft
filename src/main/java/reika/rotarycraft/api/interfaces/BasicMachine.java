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
 * All RC/ReC/ElC machines implement this.
 */
public interface BasicMachine {

    /**
     * For display purposes
     */
    String getName();

    /**
     * A more accurate way of checking if the machine has an inventory than just instanceof Container.
     */
    boolean hasAnInventory();

    /**
     * A more accurate way of checking if the machine interfaces with pipes than just checking if it has the IFluidHandler capability.
     */
    boolean hasATank();

}
