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
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import reika.dragonapi.instantiable.data.immutable.DecimalPosition;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.level.ReikaBlockHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaVectorHelper;
//import reika.dragonapi.libraries.registry.ReikaParticleHelper;
//import reika.rotarycraft.base.ItemChargedTool;
//import reika.rotarycraft.registry.SoundRegistry;
//
//import java.util.List;
//
//public class ItemStunGun extends ItemChargedTool implements EnchantableItem {
//
//    public ItemStunGun() {
//        super();
//    }
//
//    /**
//     * ItemStack sensitive version of getItemEnchantability
//     *
//     * @param stack The ItemStack
//     * @return the item echantability value
//     */
//    @Override
//    public int getItemEnchantability(ItemStack stack) {
//        return Items.IRON_SWORD.getItemEnchantability(stack);
//
//    }
//
//    @Override
//    public ItemStack onItemRightClick(ItemStack is, Level world, Player ep) {
//        if (ep.isShiftKeyDown()) {
//            return is;
//        }
//
//        //todo this.noCharge();
//
//        this.warnCharge(is);
//        //if (!world.isClientSide) {
//        DecimalPosition part = ReikaVectorHelper.getPlayerLookCoords(ep, 1);
//        for (int i = 0; i < 12; i++) {
//            double px = ReikaRandomHelper.getRandomPlusMinus(part.xCoord, 0.3);
//            double py = ReikaRandomHelper.getRandomPlusMinus(part.yCoord, 0.3);
//            double pz = ReikaRandomHelper.getRandomPlusMinus(part.zCoord, 0.3);
//            double vx = ReikaRandomHelper.getRandomPlusMinus(0, 0.5);
//            double vy = ReikaRandomHelper.getRandomPlusMinus(0, 0.5);
//            double vz = ReikaRandomHelper.getRandomPlusMinus(0, 0.5);
//            ReikaParticleHelper.ENCHANTMENT.spawnAt(world, px, py, pz, vx, vy, vz);
//        }
//        //}
//        Vec3 norm = ep.getLookVec();
//        SoundRegistry.KNOCKBACK.playSound(world, ep.getY + norm.xCoord, ep.getY() + norm.yCoord, ep.posZ + norm.zCoord, 2, 2F);
//        for (float i = 1; i <= 5; i += 0.5) {
//            DecimalPosition look = ReikaVectorHelper.getPlayerLookCoords(ep, i);
//            //ReikaChatHelper.writeString(String.format("%.3f", look.xCoord)+" "+String.format("%.3f", look.yCoord)+" "+String.format("%.3f", look.zCoord));
//            AABB fov = look.getAABB(0.5);
//            List infov = world.getEntities(LivingEntity.class, fov);
//            for (int k = 0; k < infov.size(); k++) {
//                LivingEntity ent = (LivingEntity) infov.get(k);
//                if (!(ent instanceof Player) && ep.canEntityBeSeen(ent)) {
//                    for (int f = 0; i < 64; i++)
//                        world.addParticle("magicCrit", ent.getY - 0.5 + par5Random.nextFloat(), ent.getY() - 0.5 + par5Random.nextFloat(), ent.posZ - 0.5 + par5Random.nextFloat(), -0.5 + par5Random.nextFloat(), -0.5 + par5Random.nextFloat(), -0.5 + par5Random.nextFloat());
//                    ReikaEntityHelper.knockbackEntity(ep, ent, 2 * (1 + ReikaEnchantmentHelper.getEnchantmentLevel(Enchantment.knockback, is) * 0.25));
//                }
//            }
//            if (infov.size() > 0 && !(infov.size() == 1 && infov.get(0) instanceof Player))
//                return new ItemStack(is.getItem(), is.getCount(), is.getItemDamage() - 1);
//        }
//        is = is.copy();
//        is.setItemDamage(is.getItemDamage() - 1);
//        return is;
//    }
//
//    @Override
//    public boolean onItemUse(ItemStack is, Player ep, Level world, BlockPos pos, int side, float a, float b, float c) {
//        if (!ep.isShiftKeyDown())
//            return false;
//        if (is.getItemDamage() <= 0)
//            return false;
//        if (is.getItemDamage() < 8192 && !ep.capabilities.isCreative())
//            return false;
//        if (!world.isClientSide) {
//            //ReikaChatHelper.write(mov);
//            //ReikaChatHelper.writeBlockAtCoords(world, pos);
//            Block id = world.getBlock(pos);
//             =world.getBlockMetadata(pos);
//            Material mat = ReikaWorldHelper.defaultBlockState().getMaterial(world, pos);
//            int fortune = ReikaEnchantmentHelper.getEnchantmentLevel(Enchantment.fortune, is);
//            if (id != Blocks.AIR && !(id instanceof BlockLiquid || id instanceof BlockFluidBase) && (id == Blocks.COBWEB || id == Blocks.red_mushroom ||
//                    id == Blocks.GRAVEL || id == Blocks.monster_egg || id == Blocks.brown_mushroom ||
//                    id == Blocks.waterlily || id == Blocks.flower_pot ||
//                    ReikaBlockHelper.isOre(id, meta) || (ReikaWorldHelper.softBlocks(world, pos) && id != Blocks.SNOW))) {
//                for (int k = 0; k < 64; k++)
//                    world.addParticle("magicCrit", x + par5Random.nextFloat(), y + par5Random.nextFloat(), z + par5Random.nextFloat(), -0.5 + par5Random.nextFloat(), -0.5 + par5Random.nextFloat(), -0.5 + par5Random.nextFloat());
//                ReikaWorldHelper.recursiveBreak(world, pos, id, -1, fortune);
//                ep.setCurrentItemOrArmor(0, new ItemStack(is.getItem(), is.getCount(), is.getItemDamage() - 2));
//            }
//            int leafrange = 4;
//            if (mat == Material.glass || mat == Material.ice || mat == Material.leaves || id == Blocks.SAND || id == Blocks.SNOW || id == Blocks.ICE) {
//                for (int k = 0; k < 64; k++)
//                    world.addParticle("magicCrit", x + par5Random.nextFloat(), y + par5Random.nextFloat(), z + par5Random.nextFloat(), -0.5 + par5Random.nextFloat(), -0.5 + par5Random.nextFloat(), -0.5 + par5Random.nextFloat());
//                ReikaWorldHelper.recursiveBreakWithinSphere(world, pos, id, -1, pos, leafrange, fortune);
//                ep.setCurrentItemOrArmor(0, new ItemStack(is.getItem(), is.getCount(), is.getItemDamage() - 2));
//            }
//        }
//        DecimalPosition part = ReikaVectorHelper.getPlayerLookCoords(ep, 1);
//        for (int i = 0; i < 12; i++) {
//            double px = ReikaRandomHelper.getRandomPlusMinus(part.xCoord, 0.3);
//            double py = ReikaRandomHelper.getRandomPlusMinus(part.yCoord, 0.3);
//            double pz = ReikaRandomHelper.getRandomPlusMinus(part.zCoord, 0.3);
//            double vx = ReikaRandomHelper.getRandomPlusMinus(0, 0.5);
//            double vy = ReikaRandomHelper.getRandomPlusMinus(0, 0.5);
//            double vz = ReikaRandomHelper.getRandomPlusMinus(0, 0.5);
//            ReikaParticleHelper.ENCHANTMENT.spawnAt(world, px, py, pz, vx, vy, vz);
//        }
//        //}
//        Vec3 norm = ep.getLookVec();
//        SoundRegistry.KNOCKBACK.playSound(world, ep.getY + norm.xCoord, ep.getY() + norm.yCoord, ep.posZ + norm.zCoord, 2, 2F);
//        return true;
//    }
//
//    @Override
//    public Result getEnchantValidity(Enchantment e, ItemStack is) {
//        return e == Enchantment.fortune || e == Enchantment.knockback ? Result.ALLOW : Result.DENY;
//    }
//
//}
