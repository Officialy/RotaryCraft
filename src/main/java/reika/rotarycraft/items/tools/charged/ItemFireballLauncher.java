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
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResultHolder;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.entity.projectile.LargeFireball;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.Vec3;
//import reika.dragonapi.instantiable.data.immutable.DecimalPosition;
//import reika.dragonapi.libraries.mathsci.ReikaVectorHelper;
//import reika.rotarycraft.base.ItemChargedTool;
//
//public class ItemFireballLauncher extends ItemChargedTool {
//
//    public ItemFireballLauncher() {
//        super();
//    }
//
//    public void fire(ItemStack is, Level world, Player ep, float charge) {
//        DecimalPosition look = ReikaVectorHelper.getPlayerLookCoords(ep, 2);
//        LargeFireball ef = new LargeFireball(world, ep, look.xCoord, look.yCoord + 1, look.zCoord);
//        Vec3 lookv = ep.getLookVec();
//        ef.motionX = lookv.xCoord / 5;
//        ef.motionY = lookv.yCoord / 5;
//        ef.motionZ = lookv.zCoord / 5;
//        ef.accelerationX = ef.motionX;
//        ef.accelerationY = ef.motionY;
//        ef.accelerationZ = ef.motionZ;
//        ef.field_92057_e = (int) (charge);
//        ef.getY() = ep.getY() + 1;
//        if (!world.isClientSide) {
//            world.playSoundAtEntity(ep, "mob.ghast.fireball", 1, 1);
//            world.addFreshEntity(ef);
//        }
//        if (!ep.isCreative() && par5Random.nextInt(3) == 0)
//            ReikaInventoryHelper.findAndDecrStack(Items.FIRE_CHARGE, -1, ep.getInventory());
//        int decr = (int) (charge / 2F);
//        if (decr <= 0)
//            decr = 1;
//        ep.setCurrentItemOrArmor(0, new ItemStack(is.getItem(), is.getCount(), is.getItemDamage() - decr));
//    }
//
//    @Override
//    public void onPlayerStoppedUsing(ItemStack is, Level world, Player ep, int ticksLeft) {
//        float power = (is.getMaxItemUseDuration() - ticksLeft) / 20F;
//        float charge = 0;
//        if (ep.isCreative()) {
//            power *= 2;
//            if (ep.isShiftKeyDown())
//                power *= 2;
//        }
//        if (power < 0.1F) {
//            charge = 0;
//        } else if (power < 0.25F) {
//            charge = 1;
//        } else if (power < 0.5F) {
//            charge = 2;
//        } else if (power < 1F) {
//            charge = 3;
//        } else if (power < 2F) {
//            charge = 4;
//        } else if (power < 3F) {
//            charge = 5;
//        } else if (power < 5F) {
//            charge = 6;
//        } else if (power < 8F) {
//            charge = 7;
//        } else {
//            charge = 8;
//        }
//        //ReikaChatHelper.write(power+"  ->  "+charge);
//        this.fire(is, world, ep, charge);
//    }
//
//    @Override
//    public ItemStack onEaten(ItemStack is, Level world, Player ep) {
//        return is;
//    }
//
//    @Override
//    public int getMaxItemUseDuration(ItemStack is) {
//        return 72000;
//    }
//
//    /**
//     * returns the action that specifies what animation to play when the items is being used
//     */
//    @Override
//    public EnumAction getItemUseAction(ItemStack is) {
//        return EnumAction.bow;
//    }
//
//    /**
//     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, Player
//     */
//    @Override
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//        if (is.getItemDamage() <= 0) {
//            this.noCharge();
//            return is;
//        }
//        this.warnCharge(is);
//        if (!ReikaPlayerAPI.playerHasOrIsCreative(ep, Items.fire_charge))
//            return is;
//        ep.setItemInUse(is, this.getMaxItemUseDuration(is));
//        return is;
//    }
//
//    @Override
//    public void onUsingTick(ItemStack is, Player ep, int count) {
//        float power = (is.getMaxItemUseDuration() - count) / 20F;
//        if (ep.capabilities.isCreative()) {
//            power *= 2;
//            if (ep.isShiftKeyDown())
//                power *= 2;
//        }
//        if (power < 0.1F) {
//            texture = defaulttex;
//        } else if (power < 0.25F) {
//            texture = defaulttex + 1;
//        } else if (power < 0.5F) {
//            texture = defaulttex + 2;
//        } else if (power < 1F) {
//            texture = defaulttex + 3;
//        } else if (power < 2F) {
//            texture = defaulttex + 4;
//        } else if (power < 3F) {
//            texture = defaulttex + 5;
//        } else if (power < 5F) {
//            texture = defaulttex + 6;
//        } else if (power < 8F) {
//            texture = defaulttex + 7;
//        } else {
//            texture = defaulttex + 8;
//        }
//    }
//}
