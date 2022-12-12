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
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.base.ItemRotaryShears;
import reika.rotarycraft.registry.RotaryItems;

public class ItemSteelShears extends ItemRotaryShears {

    public ItemSteelShears() {
        super(new Item.Properties().defaultDurability(600));
    }

    /**
     * Return whether this item is repairable in an anvil.
     *
     * @param tool
     * @param item
     */
    @Override
    public boolean isValidRepairItem(ItemStack tool, ItemStack item) {
        return tool.getItem() == this && ReikaItemHelper.matchStacks(item, RotaryItems.HSLA_STEEL_INGOT);

    }

}
