/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools.bedrock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.item.ItemStack;
import reika.rotarycraft.base.ItemRotaryShears;

import java.util.HashSet;

// 1.21.5 NOTE: IForgeShearable, Event.Result, and onBlockStartBreak have been removed
// in NeoForge. Shearing now goes through a NeoForge capability (IShearable) and the
// EnchantmentHelper API takes Holder<Enchantment>/ResourceKey<Enchantment>.
// The full shear-everything behaviour will need to be rewritten against the new capability
// system; for now we keep only the destroy-speed override so the item is still usable.
public class ItemBedrockShears extends ItemRotaryShears {

    private static final HashSet<Block> noDrops = new HashSet<>();

    static {
        noDrops.add(Blocks.SUGAR_CANE);
        noDrops.add(Blocks.MELON_STEM);
        noDrops.add(Blocks.PUMPKIN_STEM);
    }

    public ItemBedrockShears() {
        super(reika.rotarycraft.registry.RotaryItems.itemProperties().stacksTo(1));
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        float f = 0.75F;
        if (pState != null) {
            if (pState.getBlock().defaultMapColor() == MapColor.PLANT) {
                f = 8F;
            } else if (pState.getBlock() == Blocks.COBWEB) {
                f = 40F;
            } else if (pState.getBlock().defaultMapColor() == MapColor.WOOL) {
                f = 16;
            }
        }
        return f;
    }
}
