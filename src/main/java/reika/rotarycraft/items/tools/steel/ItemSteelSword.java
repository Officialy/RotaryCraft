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

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.RotaryItems;

public class ItemSteelSword extends SwordItem {

    public ItemSteelSword() {
        super(Tiers.IRON, 5, 2, new Properties());
    }

    @Override
    public boolean isValidRepairItem(ItemStack tool, ItemStack item) {
        //return tool.getItem() == this && item;
        return tool.getItem() == this && ReikaItemHelper.matchStacks(item, RotaryItems.HSLA_STEEL_INGOT.get());
    }

}
