/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools.steel;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import reika.rotarycraft.base.ItemRotaryArmor;
import reika.rotarycraft.registry.Materials;
import reika.rotarycraft.registry.RotaryItems;

public class ItemSteelArmor extends ItemRotaryArmor {

    public ItemSteelArmor(Type slot, Properties properties) {
        super(Materials.HSLA_STEEL, slot, properties);
    }

    @Override
    public boolean providesProtection() {
        return true;
    }

    @Override
    public boolean canBeDamaged() {
        return true;
    }

    @Override
    public boolean isValidRepairItem(ItemStack tool, ItemStack item) {
        return tool.getItem() == this && item.getItem() == RotaryItems.HSLA_STEEL_INGOT.get();
    }
}
