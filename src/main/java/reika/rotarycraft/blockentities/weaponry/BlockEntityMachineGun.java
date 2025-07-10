///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.weaponry;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.AABB;
//
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.ModList;
//import reika.dragonapi.libraries.ReikaEntityHelper;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.ReikaPlayerAPI;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.auxiliary.MachineEnchantmentHandler;
//import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
//import reika.rotarycraft.auxiliary.interfaces.EnchantableMachine;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.List;
//
//public class BlockEntityMachineGun extends InventoriedPowerReceiver implements RangedEffect, EnchantableMachine, DiscreteFunction {
//
//    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler().addFilter(Enchantment.infinity).addFilter(Enchantment.power);
//
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        tickcount++;
//        this.getIOSides(world, pos);
//        this.getPower(false);
//
//        if (power < MINPOWER || torque < MINTORQUE)
//            return;
//
//        if (DragonAPI.debugtest) {
//            ReikaInventoryHelper.addToIInv(Items.ARROW, this);
//        }
//
//        //ReikaJavaLibrary.pConsole(tickcount+"/"+this.getFireRate()+":"+ReikaInventoryHelper.checkForItem(Items.arrow.itemID, inv));
//
//        if (tickcount >= this.getOperationTime() && ReikaInventoryHelper.checkForItem(Items.ARROW, inv)) {
//            AABB box = this.drawAABB(world, pos);
//            List<LivingEntity> li = world.getEntitiesOfClass(LivingEntity.class, box);
//            if (li.size() > 0 && !ReikaEntityHelper.allAreDead(li, false) && !this.isReikaOnly(li)) {
//                this.fire(world, pos);
//            }
//            tickcount = 0;
//        }
//    }
//
//    private boolean isReikaOnly(List<LivingEntity> li) {
//        if (li.size() != 1)
//            return false;
//        LivingEntity e = li.get(0);
//        if (!(e instanceof Player))
//            return false;
//        if (ReikaPlayerAPI.isReika((Player) e)) {
//            return !((Player) e).capabilities.isCreative();
//        }
//        return false;
//    }
//
//    public void getIOSides(Level world, BlockPos pos, int metadata) {
//        switch (metadata) {
//            case 1:
//                read = Direction.WEST;
//                break;
//            case 0:
//                read = Direction.EAST;
//                break;
//            case 2:
//                read = Direction.NORTH;
//                break;
//            case 3:
//                read = Direction.SOUTH;
//                break;
//        }
//    }
//
//
//    private int getArrowSlot() {
//        return ReikaInventoryHelper.locateIDInInventory(Items.arrow, this);
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int i, ItemStack is) {
//        return is.getItem() == Items.arrow;
//    }
//
//    public int getContainerSize() {
//        return 27;
//    }
//
//    private double getFirePower() {
//        return enchantments.getEnchantment(Enchantment.power) * 0.5 + ReikaMathLibrary.logbase(torque + 1, 2);
//    }
//
//    public int getOperationTime() {
//        return Math.max(16 - (int) ReikaMathLibrary.logbase(omega + 1, 2), 4);
//    }
//
//    private void fire(Level world, BlockPos pos) {
//        double vx = 0;
//        double vz = 0;
//        double v = this.getFirePower();
//        if (ModList.CHROMATICRAFT.isLoaded()) {
//            v *= ChromatiAPI.adjacency.getFactorSimple(level, pos, "PINK");
//        }
//        switch (meta) {
//            case 1:
//                x++;
//                vx = v;
//                break;
//            case 0:
//                x--;
//                vx = -v;
//                break;
//            case 2:
//                z++;
//                vz = v;
//                break;
//            case 3:
//                z--;
//                vz = -v;
//                break;
//        }
//        EntityArrow ar = new EntityArrow(world);
//        ar.setLocationAndAngles(x + 0.5, y + 0.8, z + 0.5, 0, 0);
//        ar.motionX = vx;
//        ar.motionZ = vz;
//        if (!world.isClientSide) {
//            ar.velocityChanged = true;
//            world.addFreshEntity(ar);
//        }
//        if (!enchantments.hasEnchantment(Enchantment.infinity))
//            ReikaInventoryHelper.decrStack(this.getArrowSlot(), inv);
//        ReikaSoundHelper.playSoundAtBlock(world, pos, "DragonAPI.rand.bow", 1, 1);
//    }
//
//    private AABB drawAABB(Level world, BlockPos pos) {
//        double d = 0.1;
//        AABB box = AABB.getBoundingBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).contract(d, d, d);
//        int r = this.getRange();
//        switch (meta) {
//            case 1 -> {
//                box.offset(1, 0, 0);
//                box.maxX += Math.min(ReikaWorldHelper.getFreeDistance(world, pos, Direction.EAST, r), r);
//            }
//            case 0 -> {
//                box.offset(-1, 0, 0);
//                box.minX -= Math.min(ReikaWorldHelper.getFreeDistance(world, pos, Direction.WEST, r), r);
//            }
//            case 2 -> {
//                box.offset(0, 0, 1);
//                box.maxZ += Math.min(ReikaWorldHelper.getFreeDistance(world, pos, Direction.SOUTH, r), r);
//            }
//            case 3 -> {
//                box.offset(0, 0, -1);
//                box.minZ -= Math.min(ReikaWorldHelper.getFreeDistance(world, pos, Direction.NORTH, r), r);
//            }
//        }
//
//        return box;
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.ARROWGUN;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public int getRange() {
//        return this.getMaxRange();
//    }
//
//    @Override
//    public int getMaxRange() {
//        return 10 + 2 * (int) ReikaMathLibrary.logbase(torque + 1, 2);
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return Container.calcRedstoneFromInventory(this);
//    }
//
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return false;
//    }
//
//    @Override
//    public MachineEnchantmentHandler getEnchantmentHandler() {
//        return enchantments;
//    }
//
//    @Override
//    public void saveAdditional(CompoundTag NBT) {
//        super.saveAdditional(NBT);
//
//        NBT.put("enchants", enchantments.saveAdditional());
//    }
//
//    @Override
//    public void load(CompoundTag NBT) {
//        super.load(NBT);
//
//        enchantments.load(NBT.getTagList("enchants", NBTTypes.COMPOUND.ID));
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 0;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return false;
//    }
//
//    @Override
//    public ItemStack getItem(int pIndex) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItem(int pIndex, int pCount) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItemNoUpdate(int pIndex) {
//        return null;
//    }
//
//    @Override
//    public void setItem(int pIndex, ItemStack pStack) {
//
//    }
//
//    @Override
//    public boolean stillValid(Player pPlayer) {
//        return false;
//    }
//
//    @Override
//    public void clearContent() {
//
//    }
//
//    @Override
//    public int getSlots() {
//        return 0;
//    }
//
//    
//    @Override
//    public ItemStack insertItem(int slot,  ItemStack stack, boolean simulate) {
//        return null;
//    }
//
//    
//    @Override
//    public ItemStack extractItem(int slot, int amount, boolean simulate) {
//        return null;
//    }
//
//    @Override
//    public int getSlotLimit(int slot) {
//        return 0;
//    }
//
//    @Override
//    public boolean isItemValid(int slot,  ItemStack stack) {
//        return false;
//    }
//
//    @Override
//    public String getName() {
//        return null;
//    }
//
//    @Override
//    public boolean hasAnInventory() {
//        return false;
//    }
//
//    @Override
//    public boolean hasATank() {
//        return false;
//    }
//}
