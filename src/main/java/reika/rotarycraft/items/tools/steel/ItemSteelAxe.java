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

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;

import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.RotaryItems;

public class ItemSteelAxe extends AxeItem {

    public ItemSteelAxe() {
        super(ToolMaterial.IRON, 5.0F, -3.0F, RotaryItems.itemProperties().durability(600));
        //this.setHarvestLevel("axe", ToolMaterial.IRON.getHarvestLevel());
    }

    // 1.21.5: Item.canAttackBlock was removed; harvestability is now block-side via Tool component.
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return ConfigRegistry.HSLAHARVEST.getState() && state.getDestroySpeed(level, pos) < 20 && this.getDestroySpeed(this.getDefaultInstance(), state) > 1;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState block) {
        float amt = super.getDestroySpeed(stack, block);
        //if (ReikaBlockHelper.isLeaf(block, pos))
        //    amt *= 6;
        return amt > 1 ? amt * 1.2F : 1;
    }

    // 1.21.5: Item.isValidRepairItem replaced by Properties.repairable(...); kept as a helper.
    public boolean isValidRepairItem(ItemStack tool, ItemStack item) {
        return tool.getItem() == this && ReikaItemHelper.matchStacks(item, RotaryItems.HSLA_STEEL_INGOT);
    }

}
