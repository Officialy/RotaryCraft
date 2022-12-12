///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.items.tools.charged;
//
//import reika.dragonapi.libraries.ReikaPlayerAPI;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.base.ItemChargedTool;
//import net.minecraft.util.HitResult;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.HitResult;
//
//import java.util.List;
//
//public class ItemVacuum extends ItemChargedTool {
//
//    public ItemVacuum() {
//        super();
//    }
//
//    @Override
//    public ItemStack onItemRightClick(ItemStack is, Level world, Player ep) {
//        if (is.getItemDamage() <= 0) {
//            this.noCharge();
//            return is;
//        }
//        this.warnCharge(is);
//        if (ep.isShiftKeyDown()) {
//            this.empty(is, world, ep);
//            return new ItemStack(is.getItem(), is.getCount(), is.getItemDamage() - 2);
//        }
//        AABB range = new AABB(ep.getX() - 8, ep.getY() - 8, ep.getZ() - 8, ep.getX() + 8, ep.getY() + 8, ep.getZ() + 8);
//        List inrange = world.getEntities(ItemEntity.class, range);
//        for (int i = 0; i < inrange.size(); i++) {
//            ItemEntity ent = (ItemEntity) inrange.get(i);
//            ItemStack is2 = ent.getEntityItem();
//            if (ReikaInventoryHelper.canAcceptMoreOf(is2, ep.inventory)) {
//                double dx = (ep.getX() - ent.getX());
//                double dy = (ep.getY() - ent.getY());
//                double dz = (ep.posZ - ent.posZ);
//                double ddt = ReikaMathLibrary.py3d(dx, dy, dz);
//                ent.motionX += dx / ddt / ddt / 2;
//                ent.motionY += dy / ddt / ddt / 2;
//                ent.motionZ += dz / ddt / ddt / 2;
//                if (ent.getY() < ep.getY())
//                    ent.motionY += 0.1;
//                if (!world.isClientSide)
//                    ent.velocityChanged = true;
//            }
//        }
//        List inbox2 = world.getEntities(EntityXPOrb.class, range);
//        for (int i = 0; i < inbox2.size(); i++) {
//            EntityXPOrb ent = (EntityXPOrb) inbox2.get(i);
//            double dx = (ep.getY - ent.getY);
//            double dy = (ep.getY() - ent.getY());
//            double dz = (ep.posZ - ent.posZ);
//            double ddt = ReikaMathLibrary.py3d(dx, dy, dz);
//            ent.motionX += dx / ddt / ddt / 2;
//            ent.motionY += dy / ddt / ddt / 2;
//            ent.motionZ += dz / ddt / ddt / 2;
//            if (ent.getY() < ep.getY())
//                ent.motionY += 0.1;
//            if (!world.isClientSide)
//                ent.velocityChanged = true;
//        }
//        return new ItemStack(is.getItem(), is.getCount(), is.getItemDamage() - 1);
//    }
//
//    private void empty(ItemStack is, Level world, Player ep) {
//        HitResult mov = ReikaPlayerAPI.getLookedAtBlock(ep, 5, false);
//        //ReikaChatHelper.write(mov.blockX+", "+mov.blockY+", "+mov.blockZ);
//        if (mov == null)
//            return;
//        int x = mov.blockX;
//        int y = mov.blockY;
//        int z = mov.blockZ;
//        Block b = world.getBlockState(pos).getBlock();
//        BlockEntity tile = world.getBlockEntity(pos);
//        if (tile == null || !(tile instanceof Container))
//            return;
//        Container ii = (Container) tile;
//        ReikaInventoryHelper.spillAndEmptyInventory(world, pos, ii);
//    }
//
//}
