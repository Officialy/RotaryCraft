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

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

// 26.1: PickaxeItem collapsed into plain Item + Item.Properties.pickaxe(...). The basic
// pickaxe behaviour (huge destroy speed against obsidian/blastpane/spawner/etc., below) is
// functional. The legacy 1.7.10 features still missing here, each gated on a removed hook:
//   - silk-touch enforcement on use (was Item#onBlockStartBreak): wire via BlockEvent.BreakEvent.
//   - spawner pickup with NBT preservation (was Item#breakBlock with spawner data copy):
//     wire via BlockDropsEvent, copying the SpawnerBlockEntity NBT onto the dropped stack.
//   - item self-destruct after losing all enchantments (was Item#inventoryTick): wire via
//     PlayerTickEvent.Post on the server side, checking the held stack.
// Each is a self-contained add-on; the pickaxe is usable as-is in the meantime.
public final class ItemBedrockPickaxe extends Item {

    public ItemBedrockPickaxe() {
        super(RotaryItems.itemProperties().stacksTo(1).pickaxe(ToolMaterial.NETHERITE, 5, 12));
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState b) {
        if (b == null)
            return 0;
        if (b.getBlock() == Blocks.OBSIDIAN)
            return 48F;
        if (b.getBlock() == RotaryBlocks.BLASTPANE.get())
            return 32F;
        if (b.getBlock() == RotaryBlocks.BLASTGLASS.get())
            return 48F;
        if (b.getBlock() == Blocks.SPAWNER)
            return 18F;
        if (b.getBlock() == Blocks.DRAGON_EGG)
            return 6F;
        if (b.getBlock() == Blocks.GLOWSTONE)
            return 8F;
        if (b.getBlock() == Blocks.PISTON || b.getBlock() == Blocks.STICKY_PISTON)
            return 8F;
        if (b.getBlock() == Blocks.LEVER || b.getBlock() == Blocks.STONE_BUTTON
                || b.getBlock() == Blocks.STONE_PRESSURE_PLATE
                || b.getBlock() == Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE
                || b.getBlock() == Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)
            return 18F;
        if (b.getBlock() == Blocks.REDSTONE_LAMP)
            return 10F;
        if (b.getBlock() == Blocks.IRON_DOOR)
            return 18F;
        if (b.getBlock() == Blocks.BOOKSHELF)
            return 8F;
        if (b.getBlock().getClass().getSimpleName().equalsIgnoreCase("BlockConduitFacade")
                || b.getBlock().getClass().getSimpleName().equalsIgnoreCase("BlockConduitBundle"))
            return 24F;
        if (b.getBlock() instanceof DropExperienceBlock)
            return 24F;
        if (((ItemBedrockShovel) RotaryItems.BEDROCK_ALLOY_SHOVEL.get()).isAcceleratedOn(b))
            return 6F;
        if (b.getBlock().defaultMapColor() == MapColor.STONE
                || b.getBlock().defaultMapColor() == MapColor.METAL
                || b.getBlock().defaultMapColor() == MapColor.COLOR_PURPLE)
            return 12F;
        if (b.getBlock().defaultMapColor() == MapColor.NONE)
            return 12F;
        if (b.getBlock().defaultMapColor() == MapColor.ICE)
            return 12F;
        if (b.getBlock() instanceof BlockBasicMachine)
            return 12F;
        return 1F;
    }
}
