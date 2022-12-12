/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base;

import reika.dragonapi.libraries.io.ReikaChatHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;

public abstract class ItemChargedArmor extends ItemRotaryArmor {

    public ItemChargedArmor(ArmorMaterial par2, EquipmentSlot slot, Properties properties) {
        super(par2, slot, properties);
        //this.setNoRepair();
    }
/*
	protected final void warnCharge(ItemStack is) {
		if (RotaryConfig.COMMON.CLEARCHAT.get() && is.getItemDamage() <= 32)
			ReikaChatHelper.clearChat();
		if (is.getItemDamage() == 2) {
			ReikaChatHelper.write("Armor charge is very low (2 kJ)!");
		}
		if (is.getItemDamage() == 4) {
			ReikaChatHelper.write("Armor charge is low (4 kJ)!");
		}
		if (is.getItemDamage() == 16) {
			ReikaChatHelper.write("Armor charge is low (16 kJ)!");
		}
		if (is.getItemDamage() == 32) {
			ReikaChatHelper.write("Armor charge is low (32 kJ)!");
		}
	}*/

    protected final void noCharge() {
        //if (RotaryConfig.COMMON.CLEARCHAT.getState())
        //	ReikaChatHelper.clearChat();
        ReikaChatHelper.write("Armor charge is depleted!");
    }

}
