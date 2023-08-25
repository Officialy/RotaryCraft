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
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;

import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.RotaryItems;

import java.util.Locale;

public class ItemSteelPick extends PickaxeItem {

    public ItemSteelPick() {
        super(Tiers.IRON, 2, 2, new Properties().durability(600));

        //this.setHarvestLevel("pickaxe", Tiers.IRON.getLevel());
        //this.setHarvestLevel("pick", Tiers.IRON.getLevel());
    }

    /*@Override
    public float func_150893_a(ItemStack par1ItemStack, Block par2Block) {
        float amt = super.func_150893_a(par1ItemStack, par2Block);
        return amt > 1 ? amt * 1.2F : 1;
    }*/

    @Override
    public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        //if (RotaryConfig.COMMON.HSLAHARVEST.get() && pState.blockHardness < 20 && this.getDestroySpeed(this.getDefaultInstance(), pState) > 1) {
        //    return true;
        //} else
        return Items.IRON_PICKAXE.canAttackBlock(pState, pLevel, pPos, pPlayer);
    }

    public int getHarvestLevel(ItemStack stack, String toolClass) {
        if (ConfigRegistry.HSLAHARVEST.getState() && toolClass.toLowerCase(Locale.ENGLISH).contains("pick")) {
            return 5;
        } else {
            //return super.getHarvestLevel(stack, toolClass);
        }
        return 4;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState block) {
        if (block.getBlock().defaultMapColor() == MapColor.NONE)
            return 8F;
        float amt = super.getDestroySpeed(stack, block);
        return amt > 1 ? amt * 1.2F : 1;
    }

    public Class getTextureReferenceClass() {
        return RotaryCraft.class;
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
