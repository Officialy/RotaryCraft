///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.level;
//
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.potion.Potion;
//import net.minecraft.potion.MobEffect;
//import net.minecraft.world.WorldServer;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.effect.MobEffects;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.level.material.Fluids;
//
//import net.minecraft.world.phys.AABB;
//import reika.dragonapi.modregistry.ModWoodList;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.ReikaPlayerAPI;
//import reika.dragonapi.libraries.ReikaPotionHelper;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.libraries.registry.ReikaParticleHelper;
//import reika.dragonapi.modregistry.ModWoodList;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidReceiver;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.PacketRegistry;
//
//import java.util.List;
//
//public class BlockEntityDefoliator extends InventoriedPowerLiquidReceiver implements RangedEffect {
//
//    public static final int CAPACITY = 4000;
//
//    @Override
//    protected void animateWithTick(Level world, BlockPos pos) {
//        if (!this.isInWorld()) {
//            phi = 0;
//            return;
//        }
//        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
//    }
//
//    public int getPoisonScaled(int i) {
//        return tank.getLevel() * i / CAPACITY;
//    }
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getPowerBelow();
//
//        if (world.isClientSide)
//            return;
//
//        this.consumePotions();
//
//        if (!tank.isEmpty()) {
//            int r = this.getRange();
//            int n = this.getNumberPasses();
//            for (int i = 0; i < n && !tank.isEmpty(); i++) {
//                int rx = ReikaRandomHelper.getRandomPlusMinus(x, r);
//                int ry = ReikaRandomHelper.getRandomPlusMinus(y, r);
//                int rz = ReikaRandomHelper.getRandomPlusMinus(z, r);
//                this.decay(world, rx, ry, rz);
//            }
//        }
//    }
//
//    private int getNumberPasses() {
//        if (power < MINPOWER)
//            return 0;
//        return 2 * (int) Math.sqrt(omega);
//    }
//
//    private void consumePotions() {
//        if (!itemHandler.getStackInSlot(0).isEmpty() && tank.canTakeIn(1000)) {
//            if (this.isItemValidForSlot(0, itemHandler.getStackInSlot(0))) {
//                tank.addLiquid(1000, Fluids.getFluid("poison"));
//                ReikaInventoryHelper.decrStack(0, inv);
//                ReikaInventoryHelper.addOrSetStack(Items.GLASS_BOTTLE, 1, inv, 1);
//            }
//        }
//    }
//
//    private void decay(Level world, BlockPos pos) {
//        Block id = world.getBlockState(pos).getBlock();
//        if (!world.isClientSide && !ReikaPlayerAPI.playerCanBreakAt((WorldServer) level, pos, id, meta, this.getServerPlacer()))
//            return;
//        boolean flag = false;
//        if (id != Blocks.AIR) {
//            Material mat = id.defaultBlockState().getMaterial();
//            if (mat == Material.LEAVES) {
//                flag = true;
//            } else if (mat == Material.PLANT || mat == Material.VINE || mat == Material.CACTUS) {
//                flag = true;
//            } else if (id == Blocks.LOG) {
//                flag = true;
//            } else if (id == Blocks.log2) {
//                flag = true;
//            } else if (id == Blocks.sapling) {
//                flag = true;
//            } else if (ModWoodList.isModWood(id)) {
//                flag = true;
//            } else if (ModWoodList.isModLeaf(id)) {
//                flag = true;
//            } else if (ModWoodList.isModSapling(id)) {
//                flag = true;
//            }
//
//            if (flag) {
//                ReikaSoundHelper.playBreakSound(world, pos, id);
//                List<ItemStack> li = id.getDrops(world, pos, meta, 0);
//                world.setBlockToAir(pos);
//                ReikaItemHelper.dropItems(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, li);
//
//                AABB box = ReikaAABBHelper.getBlockAABB(pos).expand(3, 3, 3);
//                List<LivingEntity> li2 = world.getEntities(LivingEntity.class, box);
//                for (LivingEntity e : li2) {
//                    e.addEffect(new MobEffectInstance(MobEffects.POISON, 50, 3));
//                    e.hurt(DamageSource.GENERIC, 0.5F);
//                }
//
//                if (world.hasChunksAt(pos, pos))
//                    ReikaPacketHelper.sendDataPacketWithRadius(RotaryCraft.packetChannel, PacketRegistry.DEFOLIATOR.ordinal(), world, pos, 64);
//                tank.removeLiquid(1);
//            }
//        }
//    }
//
//    public void onBlockBreak(Level world, BlockPos pos) {
//        int r = 3;
//        for (int i = -r; i <= r; i++)
//            for (int j = -r; j <= r; j++)
//                for (int k = -r; k <= r; k++)
//                    ReikaParticleHelper.spawnColoredParticlesWithOutset(world, x + i, y + j, z + k, 0, 20, 0, 1, 2);
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.DEFOLIATOR;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return itemstack.getItem() == Items.glass_bottle;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 2;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return is.getItem() == Items.potionitem && ReikaPotionHelper.getPotionDamageValue(Potion.poison) == is.getItemDamage();
//    }
//
//    @Override
//    public int getRange() {
//        int r = (int) (8 * ReikaMathLibrary.logbase(torque, 2));
//        return r > this.getMaxRange() ? this.getMaxRange() : r;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return 128;
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m.isStandardPipe();
//    }
//
//    @Override
//    public Fluid getInputFluid() {
//        return null;
//    }
//
//    @Override
//    public boolean isValidFluid(Fluid f) {
//        return f == Fluids.getFluid("poison") || f == Fluids.getFluid("rc chlorine");
//    }
//
//    @Override
//    public boolean canReceiveFrom(Direction from) {
//        return from.getStepY() == 0;
//    }
//
//    @Override
//    public int getCapacity() {
//        return CAPACITY;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 0;
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
//}
