///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.base.blocks.entity.transmission;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.CreativeModeTab;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.LevelAccessor;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntityTicker;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//
//import net.minecraft.world.level.storage.loot.LootParams;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.auxiliary.RotaryAux;
//import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
//import reika.rotarycraft.blockentities.level.BlockEntityFloodlight;
//import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//import javax.annotation.Nullable;
//import java.util.ArrayList;
//import java.util.List;
//
//public class BlockAdvGear extends BlockRotaryCraftMachine {
//
//    public BlockAdvGear(Properties prop) {
//        super(prop);
//        ////this.blockIndexInTexture = 8;
//    }
//
//    @Override
//    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
//        return pLevel.isClientSide() ? null : ((pLevel1, pPos, pState1, pBlockEntity) -> {
//            ((BlockEntityAdvancedGear) pBlockEntity).updateEntity(pLevel1, pPos);
//        });
//    }
//
//    @Override
//    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
//        return new BlockEntityAdvancedGear(pPos, pState);
//
//    }
//
//
//    private boolean canHarvest(Level world, Player ep, BlockPos pos) {
//        return RotaryAux.canHarvestSteelMachine(ep);
//    }
//
//    @Override
//    public void destroy(LevelAccessor p_49860_, BlockPos p_49861_, BlockState p_49862_) {
//        super.destroy(p_49860_, p_49861_, p_49862_);
//    }
//
//    @Override
//    public void playerDestroy(Level world, Player player, BlockPos pos, BlockState p_49830_, @Nullable BlockEntity p_49831_, ItemStack p_49832_) {
//        if (this.canHarvest(world, player, pos))
//            this.harvestBlock(world, player, pos);
//
//        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
//    }
//
//    //    @Override
//    public final void harvestBlock(Level world, Player ep, BlockPos pos) {
//        if (!this.canHarvest(world, ep, pos))
//            return;
//        BlockEntityAdvancedGear te = (BlockEntityAdvancedGear) world.getBlockEntity(pos);
//        if (te != null) {
//            ItemStack is = null;//todo MachineRegistry.ADVANCEDGEARS.getCraftedMetadataProduct(te.getBlockMetadata() / 4);
//            if (te.getGearType().storesEnergy()) {
//                long e = te.getEnergy();
//                if (is.getTag() == null)
//                    is.getOrCreateTag();
//                is.getTag().putLong("energy", e);
//                is.getTag().putBoolean("bedrock", te.isBedrockCoil());
//            }
//            if (te.getGearType().isLubricated()) {
//                int lube = te.getLubricant();
//                is.getOrCreateTag().putInt("lube", lube);
//            }
//            if (te.isUnHarvestable()) {
//                is = ReikaItemHelper.getSizedItemStack(RotaryItems.HSLA_STEEL_SCRAP.get().getDefaultInstance(), 2 + DragonAPI.rand.nextInt(12));
//            }
//            ReikaItemHelper.dropItem(world, new BlockPos(pos.getX() + DragonAPI.rand.nextDouble(), pos.getY() + DragonAPI.rand.nextDouble(), pos.getZ() + DragonAPI.rand.nextDouble()), is);
//        }
//    }
//
///*    @Override
//    public void setBlockBoundsBasedOnState(BlockGetter iba, BlockPos pos) {
//        this.setFullBlockBounds();
//        if (iba.getBlockMetadata(x, y, z) >= 8)
//            maxY = 0.875;
//    }*/
//
//
//
//}
