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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;
import reika.dragonapi.ModList;
import reika.dragonapi.libraries.ReikaEnchantmentHelper;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blocks.BlockBasicMachine;

import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

import java.util.ArrayList;

public final class ItemBedrockPickaxe extends PickaxeItem {

    private final ArrayList<Enchantment> forbiddenEnchants = new ArrayList<>();

    public ItemBedrockPickaxe() {
        super(Tiers.NETHERITE, 5, 12, new Item.Properties().setNoRepair().stacksTo(1)); //was emerald

        //this.setHarvestLevel("pickaxe", Integer.MAX_VALUE);
        //this.setHarvestLevel("pick", Integer.MAX_VALUE);
    }

    /**
     * Called when item is crafted/smelted. Used only by maps so far.
     *
     * @param pStack
     * @param pLevel
     * @param pPlayer
     */
    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        //RotaryAchievements.BEDROCKTOOLS.triggerAchievement(ep);
    }

    // To make un-unenchantable
    @Override
    public void inventoryTick(ItemStack is, Level level, Entity entity, int slot, boolean par5) {
        this.forceSilkTouch(is, level, entity, slot);
    }

    private void forceSilkTouch(ItemStack is, Level Level, Entity entity, int slot) {
        if (!ReikaEnchantmentHelper.hasEnchantment(Enchantments.SILK_TOUCH, is) || ReikaEnchantmentHelper.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, is) < 5) {
            if (entity instanceof Player ep) {
                entity.playSound(SoundEvents.STONE_BREAK, 1, 1);
                ep.getInventory().setItem(slot, ItemStack.EMPTY);
                ep.hurt(ep.damageSources().generic(), 10);
                ReikaChatHelper.sendChatToPlayer(ep, "The dulled tool has broken.");
                is = null;
            }
        }
    }

    @Override
    public boolean canAttackBlock(BlockState p_41441_, Level p_41442_, BlockPos p_41443_, Player p_41444_) {
        return true;
    }


//    public int getHarvestLevel(ItemStack stack, String toolClass) {
//        return toolClass == null || toolClass.toLowerCase(Locale.ENGLISH).contains("pick") ? Integer.MAX_VALUE : super.getHarvestLevel(stack, toolClass);
//    }

    @Override
    public boolean onBlockStartBreak(ItemStack is, BlockPos pos, Player ep) {
        if (ep.isCreative())
            return false;
        Level level = ep.level();
        Block id = level.getBlockState(pos).getBlock();
        ItemStack block = new ItemStack(id, 1);

//        if (RotaryConfig.COMMON.MODORES.getState()) {
//            if (ModList.THAUMCRAFT.isLoaded() && ThaumOreHandler.getInstance().isThaumOre(block)) {
//                this.dropDirectBlock(block, Level, pos);
//                return true;
//            }
//            if (ModList.DARTCRAFT.isLoaded() && DartOreHandler.getInstance().isDartOre(block)) {
//                this.dropDirectBlock(block, Level, pos);
//                return true;
//            }
//            if (ModList.TRANSITIONAL.isLoaded() && TransitionalOreHandler.getInstance().isMagmaniteOre(block)) {
//                this.dropDirectBlock(block, Level, pos);
//                return true;
//            }
//            if (ModList.MAGICCROPS.isLoaded() && MagicCropHandler.getInstance().isEssenceOre(id)) {
//                this.dropDirectBlock(block, Level, pos);
//                return true;
//            }
//            if (ModList.HEXCRAFT.isLoaded() && HexcraftHandler.getActiveHandler().isMonolith(id)) {
//                this.dropDirectBlock(block, Level, pos);
//                return true;
//            }
//        }
//
//        if (ModList.THAUMCRAFT.isLoaded() && ThaumItemHelper.isCrystalCluster(id)) {
//            this.dropDirectBlock(block, Level, pos);
//            return true;
//        }
        if (ConfigRegistry.FAKEBEDROCK.getState() || !ReikaPlayerAPI.isFake(ep)) {
            if (ConfigRegistry.BEDPICKSPAWNERS.getState() && id == Blocks.SPAWNER) {
                SpawnerBlockEntity spw = (SpawnerBlockEntity) level.getBlockEntity(pos);
                Entity entity = spw.getSpawner().getSpawnerEntity();
                if (ConfigRegistry.SPAWNERLEAK.getState() && !ReikaPlayerAPI.isFake(ep))
                    level.addFreshEntity(entity); //todo spawn multiple entities, up to 25
                //12 + level.getRandom().nextInt(25));
                ItemStack item = Items.SPAWNER.getDefaultInstance(); //RotaryItems.SPAWNER.get(); -- This wont be needed now :)
//                ReikaSpawnerHelper.addMobNBTToItem(item, spw);
                item.save(spw.getUpdateTag()); // todo check if this works
//                if (ReikaSpawnerHelper.hasCustomLogic(spw) && this.shouldKeepSpawnerLogic(is))
//                    ReikaSpawnerHelper.setSpawnerItemNBT(item, spw.func_145881_a(), true, true);
                ReikaItemHelper.dropItem(level, pos.getX() + level.getRandom().nextDouble(), pos.getY() + level.getRandom().nextDouble(), pos.getZ() + level.getRandom().nextDouble(), item);
                //Level.setBlockToAir(x, y, z);
                //Level.playLocalSound(x+0.5, y+0.5, z+0.5, "dig.stone", 1F, 1.25F);
                if (FMLLoader.getDist() == Dist.CLIENT) {
                    //ReikaRenderHelper.spawnDropParticles(Level, x, y, z, Blocks.mob_spawner, meta);
                }
                return false;
            }
        }

//        if (id == Blocks.INFESTED_STONE) {
//            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1F, 0.85F, false);
//            level.setBlockToAir(x, y, z);
//            if (FMLLoader.getDist() == Dist.CLIENT) {
//                ReikaRenderHelper.spawnDropParticles(level, pos, Blocks.MONSTER_EGG);
//            }
//            ItemStack drop = new ItemStack(ReikaBlockHelper.getSilverfishImitatedBlock(meta), 1, 0);
//            ReikaItemHelper.dropItem(level, level.random.nextDouble(), y + itemRand.nextDouble(), z + itemRand.nextDouble(), drop);
//            Silverfish si = new Silverfish(EntityType.SILVERFISH, level);
//            si.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
//            si.setHealth(0);
//            if (level.isClientSide())
//                level.addFreshEntity(si);
//            level.playSoundAtEntity(si, "mob.silverfish.kill", 0.5F, 1);
//            ReikaWorldHelper.splitAndSpawnXP(level, x + 0.5F, y + 0.125F, z + 0.5F, si.experienceValue);
//            return true;
//        } else if (id.getClass().getSimpleName().equalsIgnoreCase("BlockHellfish")) {
//            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "dig.stone", 1F, 0.85F);
//            level.setBlockToAir(pos);
//            if (FMLLoader.getDist() == Dist.CLIENT) {
//                ReikaRenderHelper.spawnDropParticles(level, pos, Blocks.NETHERRACK);
//            }
//            ReikaItemHelper.dropItem(level, new BlockPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), new ItemStack(Blocks.NETHERRACK));
//            AABB box = ReikaAABBHelper.getBlockAABB(pos);
//            List<LivingEntity> li = level.getEntitiesOfClass(LivingEntity.class, box);
//            for (LivingEntity e : li) {
//                if (e instanceof Silverfish) {
//                    level.playSoundAtEntity(e, "mob.silverfish.kill", 0.5F, 1);
//                    ReikaWorldHelper.splitAndSpawnXP(level, pos.getX() + 0.5F, pos.getY() + 0.125F, pos.getZ() + 0.5F, e.experienceValue);
//                }
//            }
//        }
        return false;
    }

    private boolean shouldKeepSpawnerLogic(ItemStack is) {
        return ModList.CHROMATICRAFT.isLoaded();//todo  && this.hasDataKeep(is);
    }

//    @ModDependent(ModList.CHROMATICRAFT)
//    private boolean hasDataKeep(ItemStack is) {
//        return ReikaEnchantmentHelper.hasEnchantment(ChromaEnchants.DATAKEEP.getEnchantment(), is);
//    }

    private void dropDirectBlock(ItemStack block, Level level, BlockPos pos) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
        level.playLocalSound(pos.getX() + 0.5, pos.getX() + 0.5, pos.getZ() + 0.5, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1F, 0.85F, false);
        if (FMLLoader.getDist() == Dist.CLIENT) {
            //ReikaRenderHelper.spawnDropParticles(level, pos, Block.byItem(block.getItem()));
        }
        ReikaItemHelper.dropItem(level, pos.getX() + level.getRandom().nextDouble(), pos.getY() + level.getRandom().nextDouble(), pos.getZ() + level.getRandom().nextDouble(), block);
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
//        if (b == MachineRegistry.SHAFT.getBlockState())
//            return 32F;
//        if (b == MachineRegistry.GEARBOX.getBlockState())
//            return 32F;
//        if (ModList.GEOSTRATA.isLoaded() && RockGetter.isGeoStrataRock(b.getBlock()))
//            return 35F; todo geo compat
        if (b.getBlock() == Blocks.SPAWNER)
            return 18F;
        if (b.getBlock() == Blocks.DRAGON_EGG)
            return 6F;
        if (b.getBlock() == Blocks.GLOWSTONE)
            return 8F;
        if (b.getBlock() == Blocks.PISTON)
            return 8F;
        if (b.getBlock() == Blocks.STICKY_PISTON)
            return 8F;
        if (b.getBlock() == Blocks.LEVER)
            return 18F;
        if (b.getBlock() == Blocks.STONE_BUTTON)
            return 18F;
        if (b.getBlock() == Blocks.STONE_PRESSURE_PLATE)
            return 18F;
        if (b.getBlock() == Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE)
            return 18F;
        if (b.getBlock() == Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE)
            return 18F;
        if (b.getBlock() == Blocks.REDSTONE_LAMP)
            return 10F;
        if (b.getBlock() == Blocks.IRON_DOOR)
            return 18F;
        if (b.getBlock() == Blocks.BOOKSHELF)
            return 8F;
//        if (ModList.CHROMATICRAFT.isLoaded() && b.getBlock() instanceof CrystalBlock)
//            return 24F;
//        if (ModList.CHROMATICRAFT.isLoaded() && b.getBlock() instanceof BlockBedrockCrack)
//            return 30F;
//
//        if (ThaumItemHelper.BlockEntry.TOTEMNODE.match(b.getBlock(), meta))
//            return 96F;
//        if (ThaumItemHelper.isTotemBlock(b.getBlock(), meta))
//            return 48F;
//        //if (b.getBlock() == MekanismHandler.getInstance().cableID)
//        //	return 20F;
//        if (b.getBlock() == MFRHandler.getInstance().cableID)
//            return 15F;
//        if (b.getBlock() == OpenBlockHandler.getInstance().tankID)
//            return 20F;
//        //if (b.getBlock() == ThermalHandler.getInstance().ductID)
//        //	return 48F;
//        if (b.getBlock() == MystCraftHandler.getInstance().crystalID)
//            return 20F;
//        if (b.getBlock() == TwilightForestHandler.BlockEntry.MAZESTONE.getBlock())
//            return 90F;
//        if (b.getBlock() == TwilightForestHandler.BlockEntry.DEADROCK.getBlock())
//            return 90F;
//        if (ModOreList.getModOreFromOre(b.getBlock()) == ModOreList.MIMICHITE)
//            return 64F;
//        if (ModList.HEXCRAFT.isLoaded() && HexcraftHandler.getActiveHandler().isMonolith(b.getBlock()))
//            return 64;
        if (b.getBlock().getClass().getSimpleName().equalsIgnoreCase("BlockConduitFacade") || b.getBlock().getClass().getSimpleName().equalsIgnoreCase("BlockConduitBundle"))
            return 24F;

        if (b.getBlock() instanceof DropExperienceBlock)
            return 24F;

//        if (field_150914_c.contains(b.getBlock()))
//            return 12F;
        if (((ItemBedrockShovel) RotaryItems.BEDROCK_ALLOY_SHOVEL.get()).isAcceleratedOn(b))
            return 6F;

        if (b.getBlock().defaultMapColor() == MapColor.STONE || b.getBlock().defaultMapColor() == MapColor.METAL || b.getBlock().defaultMapColor() == MapColor.COLOR_PURPLE)
            return 12F;
        if (b.getBlock().defaultMapColor() == MapColor.NONE) //might be glass map colour? or air, fuck this material removal bs for now
            return 12F;
        if (b.getBlock().defaultMapColor() == MapColor.ICE)
            return 12F;
        if (b.getBlock() instanceof BlockBasicMachine)
            return 12F;
        return 1F;
    }

    @Override
    public int getEnchantmentValue() {
        return ConfigRegistry.PREENCHANT.getState() ? 0 : Items.IRON_PICKAXE.getEnchantmentValue();//(Items.IRON_PICKAXE.getDefaultInstance());
    }

    /**
     * Called by the default implemetation of EntityItem's onUpdate method, allowing
     * for cleaner control over the update of the item without having to write a
     * subclass.
     *
     * @param stack
     * @param entity The entity Item
     * @return Return true to skip any further update code.
     */
    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        ItemStack is = entity.getItem();
        if (!ReikaEnchantmentHelper.hasEnchantment(Enchantments.SILK_TOUCH, is) || ReikaEnchantmentHelper.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, is) < 5) {
            entity.playSound(SoundEvents.ITEM_BREAK, 1, 1);
            entity.kill();
        }
        return false;
    }
}
