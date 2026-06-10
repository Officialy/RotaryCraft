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

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.RotaryItems;

public class ItemSteelSword extends Item {

    public ItemSteelSword() {
        super(reika.rotarycraft.registry.RotaryItems.itemProperties().sword(ToolMaterial.IRON, 5, 2));
    }

    public boolean isValidRepairItem(ItemStack tool, ItemStack item) {
        //return tool.getItem() == this && item;
        return tool.getItem() == this && ReikaItemHelper.matchStacks(item, RotaryItems.HSLA_STEEL_INGOT.get());
    }

}
