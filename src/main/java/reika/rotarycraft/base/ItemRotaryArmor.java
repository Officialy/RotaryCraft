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

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import reika.dragonapi.interfaces.item.UnbreakableArmor;

public abstract class ItemRotaryArmor extends ArmorItem implements UnbreakableArmor {

    public ItemRotaryArmor(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
        super(material, slot, properties);
    }

    public abstract boolean providesProtection();

    public abstract boolean canBeDamaged();

    @Override
    public int getDefense() {
        if (!providesProtection()) return 0;
        return 5;
    }

    @Override
    public float getToughness() {
        if (!providesProtection()) return 0;
        return 5;
    }

}
