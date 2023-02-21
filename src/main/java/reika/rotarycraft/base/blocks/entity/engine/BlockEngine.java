/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blocks.entity.engine;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BlockEngine extends Block {


    public BlockEngine(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    protected ImmutableMap<BlockState, VoxelShape> getShapeForEachState(Function<BlockState, VoxelShape> shape) {
//        float minx = (float) minX;
//        float maxx = (float) maxX;
//        float miny = (float) minY;
//        float maxy = (float) maxY;
//        float minz = (float) minZ;
//        float maxz = (float) maxZ;
//
//        BlockEntityEngine tile = (BlockEntityEngine) par1BlockGetter.getBlockEntity(pos);
//        if (tile == null)
//            return;
//
//        switch (tile.getEngineType()) {
//            case EngineType.DC:
//                maxy -= 0.1875F;
//                break;/*
//		case WIND:
//			maxy = 1.5F;
//			miny = -0.5F;
//			switch(tile.getBlockMetadata()) {
//			case 0:
//				minz = -0.5F;
//				maxz = 1.5F;
//				maxx = 1.1875F;
//			break;
//			case 1:
//				minz = -0.5F;
//				maxz = 1.5F;
//				minx = -0.1875F;
//			break;
//			case 2:
//				maxx = 1.5F;
//				minx = -0.5F;
//				maxz = 1.1875F;
//			break;
//			case 3:
//				maxx = 1.5F;
//				minx = -0.5F;
//				minz = -0.1875F;
//			break;
//			}
//			break;*/
//            case EngineType.STEAM:
//                maxy -= 0.125F;
//                break;
//            case EngineType.GAS:
//                maxy -= 0.0625F;
//                break;/*
//		case HYDRO:
//			maxy = 1.5F;
//			miny = -0.5F;
//			if (tile.getBlockMetadata() < 2) {
//				minz = -0.5F;
//				maxz = 1.5F;
//			}
//			else {
//				maxx = 1.5F;
//				minx = -0.5F;
//			}
//			break;*/
//            case EngineType.MICRO:
//                maxy -= 0.125F;
//                break;
//            case EngineType.JET:
//                maxy -= 0.125F;
//                break;
//            default:
//                break;
//        }
//
//        this.setBlockBounds(minx, miny, minz, maxx, maxy, maxz);
        return super.getShapeForEachState(shape);
    }

//    @Override
//    public boolean removedByPlayer(Level world, Player player, BlockPos pos, boolean harv) {
//        if (this.canHarvest(world, player, pos))
//            this.harvestBlock(world, player, pos, 0);
//        return world.setBlockToAir(pos);
//    }

    private boolean canHarvest(Level world, Player ep, BlockPos pos) {
        return RotaryAux.canHarvestSteelMachine(ep);
    }

    //    @Override
    public void harvestBlock(Level world, Player ep, BlockPos pos) {
        if (!this.canHarvest(world, ep, pos))
            return;
        BlockEntityEngine eng = (BlockEntityEngine) world.getBlockEntity(pos);
//        if (eng != null) {
//            if (eng.getEngineType() == EngineType.JET && ((BlockEntityJetEngine) eng).FOD >= 8) {
//                ItemStack todrop = ReikaItemHelper.getSizedItemStack(RotaryItems.HSLA_STEEL_GEAR.get().getDefaultInstance(), 1 + DragonAPI.rand.nextInt(5));    //drop gears
//                ItemEntity item = new ItemEntity(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, todrop);
//                item.setPickUpDelay(10);
//                if (!world.isClientSide())
//                    world.addFreshEntity(item);
//                todrop = ReikaItemHelper.getSizedItemStack(RotaryItems.HSLA_STEEL_SCRAP.get().getDefaultInstance(), 16 + DragonAPI.rand.nextInt(17));    //drop scrap
//                item = new ItemEntity(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, todrop);
//                item.setPickUpDelay(10);
//                if (!world.isClientSide() && !ep.isCreative())
//                    world.addFreshEntity(item);
//            } else {
//                ItemStack todrop = eng.getEngineType().getCraftedProduct(); //drop engine item
//                if (eng.getEngineType() == EngineType.JET) {
//                    BlockEntityJetEngine tj = (BlockEntityJetEngine) eng;
//                    if (tj.FOD > 0) {
//                        todrop.getOrCreateTag().putInt("damage", tj.FOD);
//                    }
//                } else if (eng.getEngineType() == EngineType.HYDRO) {
//                    todrop.getOrCreateTag().putBoolean("bed", ((BlockEntityHydroEngine) eng).isBedrock());
//                }
//                if (eng.isUnHarvestable()) {
//                    todrop = ReikaItemHelper.getSizedItemStack(RotaryItems.HSLA_STEEL_SCRAP, 2 + par5Random.nextInt(12));
//                }
//                ItemEntity item = new ItemEntity(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, todrop);
//                item.setPickUpDelay(10);
//
//                if (!world.isClientSide() && !ep.isCreative())
//                    world.addFreshEntity(item);
//                for (int i = 0; i < eng.getContainerSize(); i++) {
//                    if (eng.getStackInSlot(i) != null) {
//                        todrop = eng.getStackInSlot(i);
//                        item = new ItemEntity(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, todrop);
//                        item.setPickUpDelay(10);
//                        if (!world.isClientSide() && !ep.isCreative())
//                            world.addFreshEntity(item);
//                    }
//                }
//            }
//        }
    }

//    @Override
//    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
//        ArrayList<ItemStack> ret = new ArrayList<>();
//        BlockEntityEngine tile = (BlockEntityEngine) builder.getLevel().getBlockEntity(worldposition);
//        if (tile == null)
//            return ret;
//        ItemStack is = tile.getEngineType().getCraftedProduct();
//        ret.add(is);
//        if (tile.getEngineType() == EngineType.JET) {
//            BlockEntityJetEngine tj = (BlockEntityJetEngine) tile;
//            is.put(new CompoundTag());
//            is.getTag().putInt("damage", tj.FOD);
//        }
//        return ret;
//    }

}
