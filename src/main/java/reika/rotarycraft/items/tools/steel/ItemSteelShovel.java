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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;

import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.RotaryItems;

public class ItemSteelShovel extends ShovelItem {

    private int index;

    public ItemSteelShovel() {
        super(Tiers.IRON, 1, 1, new Properties().durability(600));
        //this.setHarvestLevel("shovel", Tiers.IRON.getLevel());
    }

    /*@Override
    public float func_150893_a(ItemStack par1ItemStack, Block par2Block) {
        float amt = super.func_150893_a(par1ItemStack, par2Block);
        return amt > 1 ? amt * 1.2F : 1;
    }*/

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        if (ConfigRegistry.HSLAHARVEST.getState() && state.getDestroySpeed(level, pos) < 20 && this.getDestroySpeed(this.getDefaultInstance(), state) > 1) {
            return true;
        } else
            return Items.IRON_SHOVEL.canAttackBlock(state, level, pos, player);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState block) {
        float amt = super.getDestroySpeed(stack, block);
        return amt > 1 ? amt * 1.2F : 1;
    }

    /**
     * Return whether this item is repairable in an anvil.
     *
     * @param tool the {@code ItemStack} tool
     * @param item the {@code ItemStack} being used to repair the item
     */
    @Override
    public boolean isValidRepairItem(ItemStack tool, ItemStack item) {
        return tool.getItem() == this && ReikaItemHelper.matchStacks(item, RotaryItems.HSLA_STEEL_INGOT);

    }

}
