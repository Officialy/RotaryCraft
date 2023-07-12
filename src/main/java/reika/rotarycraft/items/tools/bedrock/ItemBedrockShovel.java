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

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import reika.dragonapi.instantiable.data.collections.ChancedOutputList;
import reika.dragonapi.instantiable.data.maps.BlockMap;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.ConfigRegistry;

import java.util.Collection;


public class ItemBedrockShovel extends ShovelItem {

    private static final BlockMap<ChancedOutputList> extraDrops = new BlockMap();

    static {
        addDrop(Blocks.GRASS, Items.WHEAT_SEEDS.getDefaultInstance(), 10);
        addDrop(Blocks.GRASS, Items.CLAY_BALL.getDefaultInstance(), 5);
        addDrop(Blocks.GRASS, Blocks.MYCELIUM, 0.5F);
        addDrop(Blocks.GRASS, Items.PUMPKIN_SEEDS.getDefaultInstance(), 5);
        addDrop(Blocks.GRASS, Items.MELON_SEEDS.getDefaultInstance(), 5);

        addDrop(Blocks.DIRT, Items.WHEAT_SEEDS.getDefaultInstance(), 10);
        addDrop(Blocks.DIRT, Items.GLOWSTONE_DUST.getDefaultInstance(), 2);
        addDrop(Blocks.DIRT, Items.NETHER_WART.getDefaultInstance(), 0.5F);
        addDrop(Blocks.DIRT, Items.EMERALD.getDefaultInstance(), 0.05F);
        addDrop(Blocks.DIRT, Items.DIAMOND.getDefaultInstance(), 0.05F);

        addDrop(Blocks.SAND, Items.GUNPOWDER.getDefaultInstance(), 2);

        addDrop(Blocks.CLAY, Items.BONE.getDefaultInstance(), 5);
        addDrop(Blocks.CLAY, Blocks.SOUL_SAND, 2);
        addDrop(Blocks.CLAY, Items.GOLD_NUGGET.getDefaultInstance(), 4);

        addDrop(Blocks.SOUL_SAND, Items.BLAZE_POWDER.getDefaultInstance(), 4);
        addDrop(Blocks.SOUL_SAND, Items.NETHER_WART.getDefaultInstance(), 5);
        addDrop(Blocks.SOUL_SAND, Items.QUARTZ.getDefaultInstance(), 2);

//        if (ModList.MAGICBEES.isLoaded()) {
//            ItemStack is = ReikaItemHelper.lookupItem("magicbees:miscResources:3");
//            if (is != null)
//                addDrop(Blocks.SOUL_SAND, is, 1);
//        }
    }

    private int index;

    public ItemBedrockShovel() {
        super(Tiers.GOLD, 4, -2.8F, new Item.Properties().durability(0).setNoRepair());
        // this.blocksEffectiveAgainst = par4ArrayOfBlock;
        //efficiencyOnProperMaterial = 20F;
        // this.efficiencyOnProperMaterial = par3ToolMaterial.getEfficiencyOnProperMaterial();
    }

    private static void addDrop(Block b, Block i, float chance) {
        addDrop(b, new ItemStack(i), chance);
    }

    public static void addDrop(Block b, ItemStack is, float chance) {
        ChancedOutputList co = extraDrops.get(b);
        if (co == null) {
            co = new ChancedOutputList(false);
            extraDrops.put(b, co);
        }
        co.addItem(is, chance);
    }

//    public int getHarvestLevel(ItemStack stack, String toolClass) {
//        return toolClass == null || toolClass.toLowerCase(Locale.ENGLISH).contains("shovel") || toolClass.toLowerCase(Locale.ENGLISH).contains("spade") ? Integer.MAX_VALUE : super.getHarvestLevel(stack, toolClass);
//    }

    @Override
    public boolean canAttackBlock(BlockState b, Level pLevel, BlockPos pPos, Player pPlayer) {
        return b.getMaterial() != Material.STONE && b.getMaterial() != Material.METAL;
    }

    @Override
    public int getEnchantmentValue() {
        return Items.IRON_SHOVEL.getEnchantmentValue();//(Items.IRON_SHOVEL.getDefaultInstance());
    }

    @Override
    public boolean onBlockStartBreak(ItemStack is, BlockPos pos, Player ep) {
        if (ConfigRegistry.FAKEBEDROCK.getState() || !ReikaPlayerAPI.isFake(ep)) {
            ChancedOutputList co = extraDrops.get(ep.level.getBlockState(pos).getBlock());
            if (co != null) {
                double mult = Math.sqrt(1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, ep)); //ep was is??
                Collection<ItemStack> c = co.calculate(mult);
                for (ItemStack drop : c) {
                    ReikaItemHelper.dropItem(ep.level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
                }
            }
        }
        return false;
    }

    @Override
    public float getDestroySpeed(ItemStack i, BlockState b) {
        if (b == null)
            return 0;
        if (b.getMaterial() == Material.GRASS)
            return 24F;
        if (b.getMaterial() == Material.DIRT)
            return 24F;
        if (b.getMaterial() == Material.SAND)
            return 24F;
        //if (ModList.TINKERER.isLoaded() && b == TinkerBlockHandler.getInstance().gravelOreID)
        //    return 36F;
        //if (field_150914_c.contains(b))
        //    return 24F;
        return 1F;
    }

    public boolean isAcceleratedOn(BlockState b) {
        return /*field_150914_c.contains(b) || */b.getMaterial() == Material.GRASS || b.getMaterial() == Material.DIRT || b.getMaterial() == Material.SAND;
    }

}
