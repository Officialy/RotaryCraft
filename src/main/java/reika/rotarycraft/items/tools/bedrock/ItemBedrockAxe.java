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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import reika.dragonapi.auxiliary.ProgressiveRecursiveBreaker;
import reika.dragonapi.instantiable.data.blockstruct.TreeReader;
import reika.dragonapi.interfaces.registry.TreeType;
import reika.dragonapi.libraries.ReikaEnchantmentHelper;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.dragonapi.libraries.io.ReikaSoundHelper;
import reika.dragonapi.libraries.registry.ReikaTreeHelper;
import reika.dragonapi.modregistry.ModWoodList;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.ConfigRegistry;

import java.util.HashMap;


public class ItemBedrockAxe extends AxeItem {

    public ItemBedrockAxe() {
        super(Tiers.GOLD, 6, -3.0F, new Item.Properties().durability(-1).setNoRepair().stacksTo(1));
    }

//    public int getHarvestLevel(ItemStack stack, String toolClass) {
//        return toolClass == null || (toolClass.toLowerCase(Locale.ENGLISH).contains("axe") && !toolClass.toLowerCase(Locale.ENGLISH).contains("pick")) ? Integer.MAX_VALUE : super.getHarvestLevel(stack, toolClass);
//    }

    /**
     * Called as the item is being used by an entity.
     *
     * @param level
     * @param entity
     * @param is
     * @param pRemainingUseDuration
     */
    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack is, int pRemainingUseDuration) {
        this.forceNoSilkTouch(is);
    }

    private void forceNoSilkTouch(ItemStack is) {
        if (ReikaEnchantmentHelper.hasEnchantment(Enchantments.SILK_TOUCH, is)) {
            HashMap<Enchantment, Integer> map = ReikaEnchantmentHelper.getEnchantments(is);
            is.save(null);
            map.remove(Enchantments.SILK_TOUCH);
            ReikaEnchantmentHelper.applyEnchantments(is, map);
        }
    }

    @Override
    public boolean canAttackBlock(BlockState b, Level pLevel, BlockPos pPos, Player pPlayer) {
        return b.getMapColor(pLevel, pPos) == MapColor.WOOD || b.getMapColor(pLevel, pPos) == MapColor.PLANT;// || b.getMaterial().isToolNotRequired();
    }

    @Override
    public int getEnchantmentValue() {
        return Items.IRON_AXE.getEnchantmentValue();//1.19 (Items.IRON_AXE.getDefaultInstance());
    }

    @Override
    public float getDestroySpeed(ItemStack is, BlockState b) {
        if (b == null)
            return 0;
//        if (b == TwilightForestHandler.BlockEntry.TOWERWOOD.getBlock())
//            return 30F;
//        if (b == TwilightForestHandler.BlockEntry.TOWERMACHINE.getBlock())
//            return 24F;
        if (b.getBlock().defaultMapColor() == MapColor.WOOD) {
            return 20F;
        }
//        if (field_150914_c.contains(b))
//            return 20F;
        return 1F;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack is, BlockPos pos, Player ep) {
        Level level = ep.level();
        Block id = level.getBlockState(pos).getBlock();
        TreeType type = ReikaTreeHelper.getTree(id);
        TreeReader tree = new TreeReader();

        if (ep.isCrouching())
            return false;
        if (!ConfigRegistry.FAKEBEDROCK.getState() && ReikaPlayerAPI.isFake(ep))
            return false;

        if (type == null)
            type = ModWoodList.getModWood(id);
        int fortune = ReikaEnchantmentHelper.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, is);
        tree.setWorld(level);
        tree.setTree(type);
        tree.addTree(level, pos);
        if (!tree.isRainbowTree() && !tree.isDyeTree())
            tree.clear();
//        if (id == TwilightForestHandler.BlockEntry.ROOT.getBlock()) {
//            int r = 2;
//            for (int i = -r; i <= r; i++) {
//                for (int j = -r; j <= r; j++) {
//                    for (int k = -r; k <= r; k++) {
//                        Block id2 = Level.getBlockState(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k)).getBlock();
//                        if (id2 == id) {
//                            id.dropBlockAsItem(Level, pos.getX() + i, pos.getY() + j, pos.getZ() + k, fortune);
//                            ep.level.playSound(ep, pos.getX() + i, pos.getY() + j, pos.getZ() + k, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 0, 0); //id
//                            Level.setBlockToAir(pos.getX() + i, pos.getY() + j, pos.getZ() + k);
//                        }
//                    }
//                }
//            }
//            return true;
//        } else
        if (id == Blocks.RED_MUSHROOM_BLOCK || id == Blocks.BROWN_MUSHROOM_BLOCK) {
            int r = 3;
            for (int i = -r; i <= r; i++) {
                for (int j = -r; j <= r; j++) {
                    for (int k = -r; k <= r; k++) {
                        Block id2 = level.getBlockState(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k)).getBlock();
                        if (id2 == Blocks.RED_MUSHROOM_BLOCK || id2 == Blocks.BROWN_MUSHROOM_BLOCK) {
                            //todo mushroom block item dropping id.dropBlockAsItem(level, pos.getX() + i, pos.getY() + j, pos.getZ() + k, fortune);
                            ReikaSoundHelper.playBreakSound(level, pos.getX() + i, pos.getY() + j, pos.getZ() + k, id, 0.25F, 1);
                            level.setBlock(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k), Blocks.AIR.defaultBlockState(), 0);
                        }
                    }
                }
            }
            return true;
//        } else if (ModList.FORESTRY.isLoaded() && ForestryHandler.getInstance().isLog(id)) {
//            this.cutEntireForestryTree(is, level, pos, ep, id);
//            return true;
        } else if (!tree.isEmpty() && tree.isValidTree()) {
            this.cutEntireTree(is, level, tree, pos, ep);
            return true;
        } else if (!level.isClientSide()) {
            if (type != null) {
                ProgressiveRecursiveBreaker.ProgressiveBreaker b = ProgressiveRecursiveBreaker.instance.getTreeBreaker(level, pos, type);
                b.player = ep;
                b.fortune = fortune;
//                if (ModList.CHROMATICRAFT.isLoaded() && this.hasAutoCollect(is))
//                    b.dropInventory = ep.getInventory();
                ProgressiveRecursiveBreaker.instance.addBlockPos(level, b);
                return true;
            }
        }
        return false;
    }

//    @ModDependent(ModList.CHROMATICRAFT)
//    private boolean hasAutoCollect(ItemStack is) {
//        return ReikaEnchantmentHelper.hasEnchantment(ChromaEnchants.AUTOCOLLECT.getEnchantment(), is);
//    }

    private void cutEntireTree(ItemStack is, Level level, TreeReader tree, BlockPos pos, Player ep) {
        int fortune = ReikaEnchantmentHelper.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, is);
        boolean rainbow = tree.isRainbowTree();
        /*if (rainbow) {
            ArrayList<ItemStack> items = tree.getAllDroppedItems(level, fortune, ep);
            if (ModList.CHROMATICRAFT.isLoaded() && this.hasAutoCollect(is)) {
                Iterator<ItemStack> it = items.iterator();
                while (it.hasNext()) {
                    ItemStack in = it.next();
                    if (MinecraftForge.EVENT_BUS.post(new EntityItemPickupEvent(ep, new ItemEntity(level, dx + 0.5, dy + 0.5, dz + 0.5, is))))
                        it.remove();
                    else if (ReikaInventoryHelper.addToIInv(in, ep.getInventory()))
                        it.remove();
                }
            }
            ReikaItemHelper.dropItems(level, new BlockPos(dx, dy, dz), items);
        }*/
        for (int i = 0; i < tree.getSize(); i++) {
            BlockPos nth = tree.getNthBlock(i);
            Block b = level.getBlockState(nth).getBlock();
            if (b != null) {
                ReikaSoundHelper.playBreakSound(level, pos.getX(), pos.getY(), pos.getZ(), b, 0.75F, 1);
               /* if (!rainbow) {
                    if (ModList.CHROMATICRAFT.isLoaded() && this.hasAutoCollect(is)) {
                        this.setCollectionPlayer(ep);
                    }
                    b.dropBlockAsItem(level, pos, fortune);
                    if (ModList.CHROMATICRAFT.isLoaded()) {
                        this.setCollectionPlayer(null);
                    }
                }*/
                RotaryCraft.LOGGER.info("Dropping " + b + " at " + nth);
                level.setBlock(nth, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }

    //    @ModDependent(ModList.CHROMATICRAFT)
    private void setCollectionPlayer(Player ep) {
//        ChromaticEventManager.instance.collectItemPlayer = ep;
    }
/*
    private void cutEntireForestryTree(ItemStack is, Level level, BlockPos pos, Player ep, Block id) {
        if (level.isClientSide())
            return;
        int fortune = ReikaEnchantmentHelper.getEnchantmentLevel(Enchantment.fortune, is);
        ProgressiveBreaker b = ProgressiveRecursiveBreaker.instance.addCoordinateWithReturn(level, pos, 64);
        //b.addBlock(new BlockKey(id, meta));
        for (int i = 0; i < 16; i++) {
            b.addBlock(new BlockKey(id, i));
            b.addBlock(new BlockKey(ForestryHandler.BlockEntry.LEAF.getBlock(), i));
        }
        b.player = ep;
        b.fortune = fortune;
        if (ModList.CHROMATICRAFT.isLoaded() && this.hasAutoCollect(is))
            b.dropInventory = ep.getInventory();
    }*/


    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     *
     * @param stack  the ItemStack used to attack
     * @param target the Entity being hit
     * @param player the player attacking
     */
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity player) {
        if (target.getClass().getSimpleName().equals("EntityEnt")) {
            DamageSource src = player instanceof Player ? player.damageSources().playerAttack((Player) player) : player.damageSources().generic();
            target.setHealth(1);
            target.hurt(src, Integer.MAX_VALUE);
            return true;
        } else {
            return super.hurtEnemy(stack, target, player);
        }
    }
}
