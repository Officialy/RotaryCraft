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

import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import reika.dragonapi.interfaces.item.UnbreakableArmor;

// 1.21.5: ArmorItem was removed; armor is a plain Item with Properties.humanoidArmor(material, type).
// Defense/toughness now come from the ArmorMaterial, so the old getDefense/getToughness overrides are gone.
public abstract class ItemRotaryArmor extends Item implements UnbreakableArmor {

    public ItemRotaryArmor(ArmorMaterial material, ArmorType slot, Properties properties) {
        super(properties.humanoidArmor(material, slot));
    }

    public abstract boolean providesProtection();

    public abstract boolean canBeDamaged();

}
