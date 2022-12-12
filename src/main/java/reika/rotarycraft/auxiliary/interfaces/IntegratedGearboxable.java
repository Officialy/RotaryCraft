/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary.interfaces;


import reika.dragonapi.interfaces.blockentity.BreakAction;
import net.minecraft.world.item.ItemStack;

public interface IntegratedGearboxable extends BreakAction {

    boolean applyIntegratedGear(ItemStack is);

    /**
     * Return positive for torque gearing, and either 0 for no gearing
     */
    int getIntegratedGear();

}
