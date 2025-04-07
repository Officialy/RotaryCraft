/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blocks.entity.transmission;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import reika.dragonapi.auxiliary.trackers.KeyWatcher;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityGearbox;
import reika.rotarycraft.items.tools.ItemDebug;
import reika.rotarycraft.items.tools.ItemMeter;
import reika.rotarycraft.items.tools.ItemScrewdriver;
import reika.rotarycraft.registry.GearboxTypes;
import reika.rotarycraft.registry.MaterialRegistry;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.RotaryMenus;

public class BlockGearbox extends BlockBasicMachine {

    public final GearboxTypes type;

    public BlockGearbox(GearboxTypes type, Properties properties) {
        super(properties.noOcclusion());
        this.type = type;
    }

    //
//    @Override
//    public int getFlammability(BlockGetter world, BlockPos pos, Direction face) {
//        BlockEntityGearbox tg = (BlockEntityGearbox) world.getBlockEntity(pos);
//        if (tg == null)
//            return 0;
//        if (tg.getGearboxType().material.isFlammable())
//            return 60;
//        return 0;
//    }
//
    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        BlockEntityGearbox gbx = (BlockEntityGearbox) level.getBlockEntity(pos);
        if (gbx == null)
            return 0;
        MaterialRegistry type = gbx.getGearboxType().material;
        return switch (type) {
            case WOOD -> 5F;
            case STONE -> 10F;
            case STEEL -> 15F;
            case TUNGSTEN, DIAMOND -> 30F;
            case BEDROCK -> 90F;
        };
    }


//    @Override
//    public float getPlayerRelativeBlockHardness(Player ep, Level world, BlockPos pos) {
//        BlockEntityGearbox gbx = (BlockEntityGearbox) world.getBlockEntity(pos);
//        if (gbx == null)
//            return 0.01F;
//        int mult = 1;
//        if (ep.getInventory().getSelected() != null) {
//            if (ep.getInventory().getSelected().getItem() == RotaryItems.BEDPICK.get())
//                mult = 2;
//        }
//        if (this.canHarvest(world, ep, pos))
//            return mult * 0.2F / (gbx.getGearboxType().ordinal() + 1);
//        return 0.01F / (gbx.getGearboxType().ordinal() + 1);
//    }

//    @Override
//    public boolean removedByPlayer(Level world, Player player, BlockPos pos, boolean harv) {
//        if (this.canHarvest(world, player, pos))
//            this.harvestBlock(world, player, pos);
//        return world.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
//    }

    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        if (!(entity instanceof Player)) {
            return false;
        }
        return canHarvest((Level) level, (Player) entity, pos);
    }

    public boolean canHarvest(Level world, Player player, BlockPos pos) {
        if (player.isCreative())
            return false;
        BlockEntityGearbox gbx = (BlockEntityGearbox) world.getBlockEntity(pos);
        if (gbx == null)
            return false;
        MaterialRegistry type = gbx.getGearboxType().material;
        return type.isHarvestablePickaxe(player.getInventory().getSelected());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityGearbox(type, pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : ((pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityGearbox) pBlockEntity).updateEntity(pLevel1, pPos);
        });
    }

    //    @Override
//    public void harvestBlock(Level world, Player ep, BlockPos pos) {
//        if (!this.canHarvest(world, ep, pos))
//            return;
//        BlockEntityGearbox gbx = (BlockEntityGearbox) world.getBlockEntity(pos);
//        if (gbx != null) {
//            ItemStack todrop = gbx.getGearboxType().getGearboxItem(gbx.getRatio());
//            ReikaNBTHelper.combineNBT(todrop.getTag(), gbx.getTagsToWriteToStack());
//            if (gbx.isUnHarvestable()) {
//                todrop = ReikaItemHelper.getSizedItemStack(RotaryItems.HSLA_STEEL_SCRAP, 2 + par5Random.nextInt(12));
//            }
//            ReikaItemHelper.dropItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, todrop);
//        }
//    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity entity = level.getBlockEntity(pos);
        if (entity instanceof BlockEntityGearbox && !level.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            serverPlayer.connection.send(new ClientboundOpenScreenPacket(0, RotaryMenus.GEARBOX.get(), Component.literal("Gearbox")));
            return InteractionResult.SUCCESS;
        }

        BlockEntityGearbox tile = (BlockEntityGearbox) level.getBlockEntity(pos);
        //if (ep.isShiftKeyDown()) {
        if (player.getMainHandItem() != null && KeyWatcher.instance.isKeyDown(player, KeyWatcher.Key.LCTRL) && player.getMainHandItem().getItem() == Items.BUCKET) {
            tile.clearLubricant();
            return InteractionResult.SUCCESS;
        }
        //}

        if (player.getMainHandItem() != null && (player.getMainHandItem().getItem() instanceof ItemScrewdriver || player.getMainHandItem().getItem() instanceof ItemMeter || player.getMainHandItem().getItem() instanceof ItemDebug)) {
            return InteractionResult.FAIL;
        }
        if (tile != null) {
            ItemStack fix = tile.getGearboxType().getPart(GearboxTypes.GearPart.GEAR);
            ItemStack held = player.getMainHandItem();
            if (held != null) {
                if ((ReikaItemHelper.matchStacks(fix, held))) {
                    boolean flag = tile.repair(1 + 20 * tile.getRandom().nextInt(18 - tile.getRatio()));
                    if (flag && !player.isCreative()) {
                        int num = held.getCount();
                        if (num > 1)
                            player.getInventory().setItem(player.getInventory().selected, ReikaItemHelper.getSizedItemStack(fix, num - 1));
                        else
                            player.getInventory().setItem(player.getInventory().selected, null);
                    }
                    return InteractionResult.SUCCESS;
                } else if (ReikaItemHelper.matchStacks(held, RotaryItems.LUBE_BUCKET)) {
                    if (tile.getGearboxType().needsLubricant()) {
                        int amt = 1000 * held.getCount();
                        if (tile.canTakeLubricant(amt)) {
                            tile.addLubricant(amt);
                            if (!player.isCreative())
                                player.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET, held.getCount()));
                        }
                    }
                    return InteractionResult.SUCCESS;
                } else if (GearboxTypes.GearPart.BEARING.isItemOfType(held)) {
                    GearboxTypes material = GearboxTypes.getMaterialFromCraftingItem(held);
                    if (tile.getGearboxType().acceptsBearingUpgrade(material) && tile.getBearingTier() != material) {
                        if (tile.getBearingTier() != tile.getGearboxType())
                            ReikaItemHelper.dropItem(level, 0.5, 0.5, 0.5, tile.getBearingTier().getPart(GearboxTypes.GearPart.BEARING));
                        tile.setBearingTier(material);
                        if (!player.isCreative()) {
                            int num = held.getCount();
                            if (num > 1)
                                player.getInventory().setItem(player.getInventory().selected, ReikaItemHelper.getSizedItemStack(held, num - 1));
                            else
                                player.getInventory().setItem(player.getInventory().selected, null);
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return super.use(state, level, pos, player, hand, hit);
    }

//    @Override
//    public void setBlockBoundsBasedOnState(BlockGetter par1BlockGetter, BlockPos pos) {
//        this.setFullBlockBounds();
//    }

    /*@Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        ArrayList<ItemStack> ret = new ArrayList<>();
        BlockEntityGearbox gbx = (BlockEntityGearbox) builder.getLevel().getBlockEntity(pos);
        ItemStack is = gbx.getGearboxType().getGearboxItem(gbx.getRatio());
        ReikaNBTHelper.combineNBT(is.getTag(), gbx.getTagsToWriteToStack());
        ret.add(is);
        return ret;
    }*/

}
