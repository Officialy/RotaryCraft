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
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.eventbus.api.Event;
import reika.dragonapi.base.BlockTieredResource;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.ItemRotaryShears;
import reika.rotarycraft.registry.ConfigRegistry;

import java.util.HashSet;
import java.util.List;

public class ItemBedrockShears extends ItemRotaryShears {

    private static final HashSet<Block> noDrops = new HashSet<>();

    static {
        noDrops.add(Blocks.SUGAR_CANE);
        noDrops.add(Blocks.MELON_STEM);
        noDrops.add(Blocks.PUMPKIN_STEM);
    }

    public ItemBedrockShears() {
        super(new Properties().setNoRepair().stacksTo(1));
    }

    public static Event.Result getHarvestResult(Block b, Player player, Level level, BlockPos pos) {
        if (b instanceof BlockTieredResource) {
            return Event.Result.DENY;
        } else if (noDrops.contains(b)) {
            return Event.Result.DENY;
            //} else if (b.canSilkHarvest(level, player, pos)) {
            //    return Event.Result.ALLOW;
        } else if (b == Blocks.COBWEB) {
            return Event.Result.ALLOW;
            // else if (ModList.CHROMATICRAFT.isLoaded() && b instanceof BlockDyeLeaf) {
            //    return Event.Result.DEFAULT; --todo chromaticraft
        } else if (level.getBlockEntity(pos) instanceof BlockEntity) {
            return Event.Result.DENY;
        } else if (b instanceof IForgeShearable) {
            ((IForgeShearable) b).onSheared(player, player.getUseItem(), level, pos, 0);
            return Event.Result.ALLOW;
        } else if (b.defaultBlockState().getMaterial() == Material.PLANT || b.defaultBlockState().getMaterial() == Material.LEAVES) {
            return Event.Result.ALLOW;
        } else {
            return Event.Result.DEFAULT;
        }
    }

//    public static int getDroppedMeta(Block id, int meta) {
//        if (id == Blocks.LEAVES || id == Blocks.leaves2)
//            return meta & 3;
//        if (ModList.CHROMATICRAFT.isLoaded() && id == ChromatiAPI.getAPI().trees().getRainbowLeaf())
//            return 0;
//        if (id == Blocks.VINE)
//            return 0;
//        if (id == Blocks.WATERLILY)
//            return 0;
//        if (id == Blocks.SAPLING)
//            return meta & 3;
//        if (id.getClass().getName().equals("vazkii.botania.common.block.BlockModDoubleFlower")) {
//            meta &= 7;
//            if (id == ForgeRegistries.BLOCKS.getValue(new ResourceLocation((ModList.BOTANIA.modid, "doubleFlower2"))
//                ;//meta += 8;
//            return meta;
//        }
//        if (id instanceof BlockDoublePlant)
//            return meta % BlockDoublePlant.field_149892_a.length;
//        ModWoodList wood = ModWoodList.getModWoodFromLeaf(id, meta);
//        if (wood != null) {
//            return wood.getLeafMetadatas().get(0);
//        }
//        return meta;
//    }

//    /**
//     * Called when item is crafted/smelted. Used only by maps so far.
//     *
//     * @param pStack
//     * @param pLevel
//     * @param pPlayer
//     */
//    @Override
//    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
//        RotaryAdvancements.BEDROCKTOOLS.triggerAchievement(ep);
//    }

    /**
     * Called before a block is broken. Return true to prevent default block
     * harvesting.
     * <p>
     * Note: In SMP, this is called on both client and server sides!
     *
     * @param itemstack The current ItemStack
     * @param pos       Block's position in world
     * @param player    The Player that is wielding the item
     * @return True to prevent harvesting, false to continue as normal
     */
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        if (player.level.isClientSide())
            return false;
        else {
            Block b = player.level.getBlockState(pos).getBlock();
            boolean drop = false;
            boolean flag = false;
            Event.Result res = getHarvestResult(b, player, player.level, pos);
            switch (res) {
                case ALLOW -> drop = flag = true;
                case DEFAULT -> flag = super.onBlockStartBreak(itemstack, pos, player);
                case DENY -> drop = flag = false;
            }
            if (drop) {
                ItemStack block = new ItemStack(b, 1);
                ReikaItemHelper.dropItem(player.level, new BlockPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), block);
                player.level.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
            }
            return flag;
        }
    }

    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     *
     * @param stack
     * @param player
     * @param entity
     * @param hand
     */
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, InteractionHand hand) {
        if (entity.level.isClientSide())
            return InteractionResult.FAIL;
        if (entity instanceof IForgeShearable) {
            IForgeShearable target = (IForgeShearable) entity;
            int x = Mth.floor(entity.getX());
            int y = Mth.floor(entity.getY());
            int z = Mth.floor(entity.getZ());
            if (target.isShearable(stack, entity.level, new BlockPos(x, y, z))) {
                int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, player);
                List<ItemStack> drops = target.onSheared(player, stack, entity.level, new BlockPos(x, y, z), fortune);

                if (ConfigRegistry.FAKEBEDROCK.getState() || !ReikaPlayerAPI.isFake(player)) {
                    for (ItemStack is : drops) {
                        int amount = is.getCount();
                        is.setCount(amount * 2);
                    }
                }
                ReikaItemHelper.dropItems(entity.level, new BlockPos(x + 0.5, y + 0.8, z + 0.5), drops);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        float f = 0.75F;
        if (pState != null) {
            if (pState.getBlock() instanceof IForgeShearable) {
                f = 8F;
            } else if (pState.getMaterial() == Material.PLANT) {
                f = 8F;
            } else if (pState.getMaterial() == Material.WEB || pState.getBlock() == Blocks.COBWEB) {
                f = 40F;
            } else if (pState.getMaterial() == Material.CLOTH_DECORATION || pState.getMaterial() == Material.WOOL) {
                f = 16;
            }
        }
        return f;
    }
}
