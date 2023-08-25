///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.farming;
//
//import forestry.api.lepidopterology.IEntityButterfly;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.monster.EntityBlaze;
//import net.minecraft.entity.monster.EntityGhast;
//import net.minecraft.entity.monster.EntityMagmaCube;
//import net.minecraft.entity.monster.Slime;
//import net.minecraft.entity.passive.EntitySquid;
//import net.minecraft.entity.passive.EntityWaterMob;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.AABB;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.interfaces.blockentity.MobAttractor;
//import reika.dragonapi.interfaces.blockentity.MobRepellent;
//import reika.dragonapi.modinteract.Bees.ReikaBeeHelper;
//import reika.dragonapi.modinteract.ItemHandlers.ForestryHandler;
//import reika.dragonapi.ModList;
//import reika.dragonapi.instantiable.AI.AITaskAvoidMachine;
//import reika.dragonapi.instantiable.AI.AITaskSeekMachine;
//import reika.dragonapi.instantiable.data.blockstruct.AbstractSearch.PassablePropagation;
//import reika.dragonapi.instantiable.data.blockstruct.AbstractSearch.PropagationCondition;
//import reika.dragonapi.instantiable.EntityPathSpline;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.List;
//
//public class BlockEntityBaitBox extends InventoriedPowerReceiver implements RangedEffect, ConditionalOperation, MobAttractor, MobRepellent {
//
//    public static final int FALLOFF = 4096; //4 kW per extra meter
//    private static final PropagationCondition butterflyPathRule = new PropagationCondition() {
//
//        @Override
//        public boolean isValidLocation(Level world, BlockPos pos, BlockPos from) {
//            return PassablePropagation.instance.isValidLocation(world, pos, from) || world.getBlock(pos).isLeaves(world, pos);
//        }
//
//    };
//    private EntityPathSpline pathfinder;
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return true;
//    }
//
//    @Override
//    protected void onFirstTick(Level world, BlockPos pos) {
//        if (ModList.FORESTRY.isLoaded() && !world.isClientSide)
//            pathfinder = new EntityPathSpline(this);
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        tickcount++;
//        this.getSummativeSidedPower();
//        if (power < MINPOWER)
//            return;
//        int range = this.getRange();
//        AABB box = this.getBox(pos, range);
//        List<LivingEntity> inbox = world.getEntities(LivingEntity.class, box);
//        if (!inbox.isEmpty() && (world.getDayTime() & 3) == 0) {
//            for (LivingEntity ent : inbox) {
//                if (this.canRepel(ent)) {
//                    this.applyEffect(world, pos, ent, false);
//                } else if (this.canAttract(ent)) {
//                    this.applyEffect(world, pos, ent, true);
//                }
//                if (ModList.FORESTRY.isLoaded() && pathfinder != null && ent instanceof IEntityButterfly) {
//                    pathfinder.addEntity(ent, butterflyPathRule);
//                }
//            }
//        }
//        if (ModList.FORESTRY.isLoaded() && ReikaInventoryHelper.checkForItem(ForestryHandler.ItemEntry.POLLEN.getItem(), inv)) {
//            if (pathfinder != null)
//                pathfinder.removeDeadEntities(world);
//            ReikaBeeHelper.attractButterflies(world, x + 0.5, y + 1.5, z + 0.5, range, pathfinder);
//            ReikaBeeHelper.collectButterflies(world, ReikaAABBHelper.getBlockAABB(this).expand(4, 2, 4), null);
//        }
//        if (DragonAPI.rand.nextInt(20) == 0)
//            this.doEggIncubation(world, pos);
//        tickcount = 0;
//    }
//
//    private void doEggIncubation(Level world, BlockPos pos) {
//        int slot = ReikaInventoryHelper.locateIDInInventory(Items.egg, this);
//        if (slot >= 0) {
//            if (ReikaWorldHelper.checkForAdjMaterial(world, pos, Material.fire) != null) {
//                if (ReikaWorldHelper.checkForAdjMaterial(world, pos, Material.lava) == null) {
//                    EntityChicken ec = new EntityChicken(world);
//                    ec.setLocationAndAngles(x + DragonAPI.rand.nextDouble(), y + DragonAPI.rand.nextDouble() + 0.5, z + DragonAPI.rand.nextDouble(), DragonAPI.rand.nextFloat() * 90, DragonAPI.rand.nextFloat() * 360);
//                    if (!world.isClientSide)
//                        world.addFreshEntity(ec);
//                } else {
//                    ReikaInventoryHelper.addToIInv(new ItemStack(Items.cooked_chicken), this, true);
//                }
//                ReikaInventoryHelper.decrStack(slot, inv);
//            }
//        }
//    }
//
//    private int maxMobs() { //Omega + config file
//        return Math.max(24, RotaryConfig.COMMON.BAITMOBS.get());
//    }
//
//    public int getMaxRange() {
//        return Math.max(24, RotaryConfig.COMMON.BAITRANGE.get());
//    }
//
//    private void silverfishStone(Level world, BlockPos pos) {
//        for (int i = x - 5; i <= x + 5; i++) {
//            for (int j = y - 5; j <= y + 5; j++) {
//                for (int k = z - 5; z <= z + 5; k++) {
//                    if (world.getBlock(i, j, k) == Blocks.monster_egg) {
//                        world.setBlockToAir(i, j, k);
//                        world.playLocalSound(i + 0.5, j + 0.5, k + 0.5, "step.stone", 0.5F + 0.5F * DragonAPI.rand.nextFloat(), 0.8F + 0.2F * DragonAPI.rand.nextFloat());
//                    }
//                }
//            }
//        }
//    }
//
//    private void dropHeldItem(Level world, BlockPos pos, LivingEntity ent) {
//        ItemStack held = ent.getHeldItem();
//        ent.setCurrentItemOrArmor(0, null);
//        if (held != null && !world.isClientSide) {
//            ItemEntity ei = new ItemEntity(world, ent.getY, ent.getY() + ent.getEyeHeight(), ent.posZ, held);
//            ei.motionX = -0.2F + 0.4F * DragonAPI.rand.nextFloat();
//            ei.motionZ = -0.2F + 0.4F * DragonAPI.rand.nextFloat();
//            ei.motionY = 0.4F * DragonAPI.rand.nextFloat();
//            ei.delayBeforeCanPickup = 200;
//            world.addFreshEntity(ei);
//        }
//    }
//
//    public boolean canRepel(LivingEntity ent) {
//        return power >= MINPOWER && this.getRange() >= ent.getDistance(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) && MobBait.hasRepelItem(ent, inv);
//    }
//
//    public boolean canAttract(LivingEntity ent) {
//        return power >= MINPOWER && this.getRange() >= ent.getDistance(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) && MobBait.hasAttractItem(ent, inv);
//    }
//
//    private void applyEffect(Level world, BlockPos pos, LivingEntity ent, boolean attract) {
//        if (world.isClientSide)
//            return;
//        if (ent instanceof EntityTameable && ((EntityTameable) ent).isSitting()) {
//            return;
//        }
//
//        long time = level.getDayTime();
//        if (time - ent.getEntityData().getLong("baitbox") < 20)
//            return;
//        ent.getEntityData().putLong("baitbox", time);
//
//        if (attract) {
//            ReikaEntityHelper.addAITask(ent, new AITaskSeekMachine(ent, 1, this), -1000000);
//        } else {
//            ReikaEntityHelper.addAITask(ent, new AITaskAvoidMachine(ent, 1, this), -1000000);
//            this.dropHeldItem(world, pos, ent);
//        }
//
//        if (ent instanceof EntityBlaze || ent instanceof Slime || ent instanceof EntityMagmaCube || ent instanceof EntityGhast || ent instanceof EntitySquid) {
//            if (attract) {
//                if (!(ent instanceof Slime) || !ent.onGround) {
//                    ent.motionX = 0.02 * (x - ent.getY);
//                    if (!(ent instanceof EntityWaterMob) || ent.isInWater())
//                        ent.motionY = 0.02 * (y - ent.getY());
//                    ent.motionZ = 0.02 * (z - ent.posZ);
//                }
//            } else {
//                if (!(ent instanceof Slime) || !ent.onGround) {
//                    ent.motionX = -0.02 * (x - ent.getY);
//                    if (!(ent instanceof EntityWaterMob) || ent.isInWater())
//                        ent.motionY = -0.02 * (y - ent.getY());
//                    ent.motionZ = -0.02 * (z - ent.posZ);
//                }
//            }
//            if (ent instanceof EntityBlaze) {
//                ent.motionX *= 4;
//                ent.motionZ *= 4;
//            }
//            float var1 = (float) ReikaMathLibrary.py3d(ent.motionX, 0, ent.motionZ);
//            ent.renderYawOffset += (-((float) Math.atan2(ent.motionX, ent.motionZ)) * 180.0F / (float) Math.PI - ent.renderYawOffset) * 0.1F;
//            ent.rotationYaw = ent.renderYawOffset;
//            if (!world.isClientSide)
//                ent.velocityChanged = true;
//        }
//        if (ent instanceof EntityBat) {
//            if (attract) {
//                ent.motionX = 0.1 * (x - ent.getY);
//                ent.motionY = 0.1 * (y - ent.getY());
//                ent.motionZ = 0.1 * (z - ent.posZ);
//            } else {
//                ent.motionX = -0.1 * (x - ent.getY);
//                ent.motionY = -0.1 * (y - ent.getY());
//                ent.motionZ = -0.1 * (z - ent.posZ);
//            }
//            float var1 = (float) ReikaMathLibrary.py3d(ent.motionX, 0, ent.motionZ);
//            ent.renderYawOffset += (-((float) Math.atan2(ent.motionX, ent.motionZ)) * 180.0F / (float) Math.PI - ent.renderYawOffset) * 0.1F;
//            ent.rotationYaw = ent.renderYawOffset;
//            if (!world.isClientSide)
//                ent.velocityChanged = true;
//        }
//    }
//
//    public int getRange() {
//        int range = 8 + (int) ((power - MINPOWER) / FALLOFF);
//        if (range > this.getMaxRange())
//            return this.getMaxRange();
//        return range;
//    }
//
//    private AABB getBox(BlockPos pos, int range) {
//        AABB box = AABB.getBoundingBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expand(range, range, range);
//        return box;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 27;
//    }
//
//    public int getLeftoverSlots() {
//        int slots = itemHandler.getSlots();
//        while (slots >= 9)
//            slots -= 9;
//        return slots;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.BAITBOX;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return true;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        if (ReikaInventoryHelper.isEmpty(inv))
//            return 15;
//        for (int i = 0; i < itemHandler.getSlots(); i++) {
//            if (MobBait.isValidItem(itemHandler.getStackInSlot(i)))
//                return 0;
//        }
//        return 15;
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return !ReikaInventoryHelper.isEmpty(inv);
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "No Items";
//    }
//}
