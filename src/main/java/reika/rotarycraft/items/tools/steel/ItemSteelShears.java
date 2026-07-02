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
        // 1.21.5: defaultDurability → durability.
        super(RotaryItems.itemProperties().durability(600));
    }

    // 1.21.5: Item.isValidRepairItem replaced by Properties.repairable(...); kept as helper.
    public boolean isValidRepairItem(ItemStack tool, ItemStack item) {
        return tool.getItem() == this && ReikaItemHelper.matchStacks(item, RotaryItems.HSLA_STEEL_INGOT);
    }

}
