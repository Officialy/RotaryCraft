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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import reika.rotarycraft.auxiliary.RotaryAux;

public class BlockFlywheel extends Block {

    public BlockFlywheel(Properties p_49795_) {
        super(p_49795_);
    }

    private boolean canHarvest(Level world, Player ep, BlockPos pos) {
        return RotaryAux.canHarvestSteelMachine(ep);
    }
//
//    @Override
//    public void harvestBlock(Level world, Player ep, BlockPos pos) {
//        if (!this.canHarvest(world, ep, pos))
//            return;
//        BlockEntityFlywheel fly = (BlockEntityFlywheel) world.getBlockEntity(pos);
//        if (fly != null) {
//            if (fly.failed) {
//                ItemStack todrop = RotaryItems.MOUNT.get().getDefaultInstance();    //drop mount
//                ItemEntity item = new ItemEntity(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, todrop);
//                item.delayBeforeCanPickup = 10;
//                if (!world.isClientSide())
//                    world.addFreshEntity(item);
//            } else {
//                int metadata = fly.getBlockMetadata();
//                ItemStack todrop = fly.getTypeOrdinal().getFlywheelItem();
//                if (fly.isUnHarvestable()) {
//                    todrop = ReikaItemHelper.getSizedItemStack(RotaryItems.HSLA_STEEL_SCRAP, 2 + par5Random.nextInt(12));
//                }
//                ItemEntity item = new ItemEntity(world, x + 0.5F, y + 0.5F, z + 0.5F, todrop);
//                item.delayBeforeCanPickup = 10;
//                if (!world.isClientSide())
//                    world.addFreshEntity(item);
//            }
//        }
//    }
//
//    @Override
//    public void setBlockBoundsBasedOnState(BlockGetter par1BlockGetter, BlockPos pos) {
//        this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
//        float minx = (float) minX;
//        float maxx = (float) maxX;
//        float miny = (float) minY;
//        float maxy = (float) maxY;
//        float minz = (float) minZ;
//        float maxz = (float) maxZ;
//
//        maxy -= 0.125F;
//
//        this.setBlockBounds(minx, miny, minz, maxx, maxy, maxz);
//    }
//
//    @Override
//    public final ArrayList<ItemStack> getDrops(Level world, BlockPos pos, int metadata, int fortune) {
//        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
//        BlockEntityFlywheel fly = (BlockEntityFlywheel) world.getBlockEntity(x, y, z);
//        if (fly != null) {
//            ret.add(fly.getTypeOrdinal().getFlywheelItem());
//        } else {
//            ret.add(RotaryItems.FLYWHEEL.getStackOfMetadata(metadata / 4));
//        }
//        return ret;
//    }
//
//    @Override
//    @ModDependent(ModList.WAILA)
//    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
//        return ((BlockEntityFlywheel) accessor.getBlockEntity()).getTypeOrdinal().getFlywheelItem();
//    }
}
