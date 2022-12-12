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
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.base.ItemChargedTool;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//
//public class ItemChargedGrafter extends ItemChargedTool {
//
//    public ItemChargedGrafter() {
//    }
//
//    @Override
//    public float getSaplingModifier(ItemStack stack, Level world, Player player, BlockPos pos) {
//        return this.getChanceFromCharge(stack.getItemDamage());
//    }
//
//    /*
//    @Override
//    public boolean onBlockDestroyed(ItemStack is, Level world, Block blockID, BlockPos pos, LivingEntity e)
//    {
//        if (is.getItemDamage() > 0 && e instanceof Player) {
//            Player ep = (Player)e;
//            Block b = blockID;
//            if (b.defaultBlockState().getMaterial() == Material.leaves) {
//                is.setItemDamage(is.getItemDamage()-1);
//                int r = 3;
//                for (int i = -r; i <= r; i++) {
//                    for (int j = -r; j <= r; j++) {
//                        for (int k = -r; k <= r; k++) {
//                            int dx = x+i;
//                            int dy = y+j;
//                            int dz = z+k;
//                            Block b2 = world.getBlock(dx, dy, dz);
//                            if (b2 != null && b2.defaultBlockState().getMaterial() == Material.leaves) {
//                                b2.dropBlockAsItem(world, dx, dy, dz, world.getBlockMetadata(dx, dy, dz), 0);
//                                b2.removedByPlayer(world, ep, dx, dy, dz);
//                                is.setItemDamage(is.getItemDamage()-1);
//                            }
//                        }
//                    }
//                }
//                e.setCurrentItemOrArmor(0, is);
//                return true;
//            }
//        }
//        return false;
//    }
//     */
//    private float getChanceFromCharge(int charge) {
//        if (charge < 8)
//            return charge;
//        if (charge > 4096)
//            return 100;
//        return (int) (10 * ReikaMathLibrary.logbase(charge, 2)) - 20;
//    }
//
//    @Override
//    public float getDigSpeed(ItemStack is, Block b) {
//        if (b == null)
//            return 0;
//        if (ForestryHandler.BlockEntry.LEAF.getBlock() == b)
//            return 30;
//        return super.getDigSpeed(is, b, meta);
//    }
//
//    @Override
//    public ItemStack onItemRightClick(ItemStack is, Level world, Player ep) {
//        return is;
//    }
//
//}
