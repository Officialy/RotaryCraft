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
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.Tag;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraft.world.item.enchantment.Enchantments;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.level.material.Fluids;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.modregistry.ModWoodList;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.api.Interfaces.BlowableCrop;
//import reika.rotarycraft.auxiliary.MachineEnchantmentHandler;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.EnchantableMachine;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidReceiver;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//import java.util.ArrayList;
//
//public class BlockEntityFertilizer extends InventoriedPowerLiquidReceiver implements RangedEffect, ConditionalOperation, EnchantableMachine {
//
//    private static final ArrayList<Block> fertilizables = new ArrayList<>();
//
//    static {
////        addFertilizable(Blocks.SAPLING);
//        addFertilizable(Blocks.CACTUS);
//        addFertilizable(Blocks.SUGAR_CANE);
//        addFertilizable(Blocks.MYCELIUM);
//        addFertilizable(Blocks.MELON_STEM);
//        addFertilizable(Blocks.PUMPKIN_STEM);
//        addFertilizable(Blocks.VINE);
//    }
//
//    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler().addFilter(Enchantment.efficiency).addFilter(Enchantment.aquaAffinity).addFilter(Enchantment.fortune).addFilter(Enchantment.unbreaking).addFilter(Enchantment.power);
//    private int firstValidSlot;
//
//    private static void addFertilizable(Block b) {
//        fertilizables.add(b);
//    }
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
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.FERTILIZER;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return this.hasFertilizer() ? 0 : 15;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getPowerBelow();
//
//        if (DragonAPI.debugtest) {
//            tank.addLiquid(100, Fluids.WATER);
//            inv[0] = new ItemStack(Items.dye, 64, 15);
//        }
//
//        if (!world.isClientSide) {
//            this.checkFertilizer();
//            int n = this.getUpdatesPerTick();
//            for (int i = 0; i < n && this.hasFertilizer(); i++)
//                this.tickBlock(world, pos);
//        }
//    }
//
//    private int getUpdatesPerTick() {
//        if (power < MINPOWER)
//            return 0;
//        return 4 * ReikaMathLibrary.logbase2(omega) + enchantments.getEnchantment(Enchantment.efficiency) * 3;
//    }
//
//    private int getConsecutiveUpdates() {
//        if (omega < 1048576)
//            return 1;
//        return 1 + ReikaMathLibrary.logbase2(omega / 1048576);
//    }
//
//    private void tickBlock(Level world, BlockPos pos) {
//        int r = this.getRange();
//        int dx = ReikaRandomHelper.getRandomPlusMinus(x, r);
//        int dy = ReikaRandomHelper.getRandomPlusMinus(y, r);
//        while (dy > y + 1)
//            dy = ReikaRandomHelper.getRandomPlusMinus(y, r);
//        int dz = ReikaRandomHelper.getRandomPlusMinus(z, r);
//        Block id = world.getBlockState(new BlockPos(dx, dy, dz)).getBlock();
//        int ddx = dx - x;
//        int ddy = dy - y;
//        int ddz = dz - z;
//        double dd = ReikaMathLibrary.py3d(ddx, ddy, ddz);
//        if (id != Blocks.AIR && dd <= this.getRange() && this.canTick(world, dx, dy, dz)) {
//            int n = this.getConsecutiveUpdates() + enchantments.getEnchantment(Enchantments.BLOCK_FORTUNE);
//            for (int i = 0; i < n; i++) {
//                id.updateTick(world, dx, dy, dz, DragonAPI.rand);
//                BlockTickEvent.fire(world, dx, dy, dz, id, UpdateFlags.FORCED.flag);
//            }
//            world.markBlockForUpdate(dx, dy, dz);
//            if (this.didSomething(world, dx, dy, dz)) {
//                ReikaPacketHelper.sendUpdatePacket(RotaryCraft.packetChannel, PacketRegistry.FERTILIZER.ordinal(), dx, dy, dz, new PacketTarget.RadiusTarget(world, dx, dy, dz, 32));
//                if (ReikaRandomHelper.doWithChance(20))
//                    this.consumeItem();
//            } else if (id == Blocks.GRASS) {
//                ReikaPacketHelper.sendUpdatePacket(RotaryCraft.packetChannel, PacketRegistry.FERTILIZER.ordinal(), dx, dy, dz, new PacketTarget.RadiusTarget(world, dx, dy, dz, 32));
//            }
//        }
//    }
//
//    private boolean canTick(Level world, int dx, int dy, int dz) {
//        Block b = world.getBlock(dx, dy, dz);
//        return !(b instanceof BlockRedstoneDiode || b instanceof BlockRedstoneComparator || b instanceof BlockRedstoneWire);
//    }
//
//    private void consumeItem() {
//        tank.removeLiquid(Math.max(1, 5 - enchantments.getEnchantment(Enchantments.AQUA_AFFINITY) / 2));
//        if (DragonAPI.rand.nextInt(4 + enchantments.getEnchantment(Enchantments.UNBREAKING)) == 0) {
//            int in = inv[firstValidSlot].getCount();
//            ReikaInventoryHelper.decrStack(firstValidSlot, inv);
//            if (in == 1) {
//                this.checkFertilizer();
//            }
//        }
//    }
//
//    private boolean didSomething(Level world, BlockPos pos) {
//        Block id = world.getBlock(pos);
//        ReikaCropHelper crop = ReikaCropHelper.getCrop(id);
//        ModCrop mod = ModCropList.getModCrop(id, meta);
//        ModWoodList sapling = ModWoodList.getModWoodFromSapling(id, meta);
//        boolean fert = fertilizables.contains(id);
//        if (crop != null)
//            return true;
//        if (mod != null)
//            return true;
//        if (sapling != null)
//            return true;
//        if (fert)
//            return true;
//        return id instanceof BlowableCrop;
//    }
//
//    @Override
//    public int getRange() {
//        if (torque <= 0)
//            return 0;
//        int r = 2 * (int) ReikaMathLibrary.logbase(torque, 2) + enchantments.getEnchantment(Enchantment.power);
//        if (r > this.getMaxRange())
//            return this.getMaxRange();
//        return r;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return 32;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 18;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return this.isValidFertilizer(is);
//    }
//
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return false;
//    }
//
//    public boolean isValidFertilizer(ItemStack is) {
//        if (ReikaItemHelper.matchStacks(is, Items.BONE_MEAL))
//            return true;
//        if (ReikaItemHelper.matchStacks(is, RotaryItems.COMPOST))
//            return true;
//        return is.getItem() == ForestryHandler.ItemEntry.FERTILIZER.getItem();
//    }
//
//    public boolean hasFertilizer() {
//        return firstValidSlot >= 0;
//    }
//
//    private void checkFertilizer() {
//        firstValidSlot = -1;
//        if (tank.isEmpty())
//            return;
//        for (int i = 0; i < inv.length; i++) {
//            if (inv[i] != null) {
//                if (this.isValidFertilizer(inv[i])) {
//                    firstValidSlot = i;
//                    return;
//                }
//            }
//        }
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m.isStandardPipe();
//    }
//
//    @Override
//    public Fluid getInputFluid() {
//        return Fluids.WATER;
//    }
//
//    @Override
//    public boolean canReceiveFrom(Direction from) {
//        return from != Direction.DOWN;
//    }
//
//    @Override
//    public int getCapacity() {
//        return 6000;
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return !tank.isEmpty() && this.hasFertilizer();
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return tank.isEmpty() ? "No Water" : this.areConditionsMet() ? "Operational" : "No Fertilizer Items";
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.put("enchants", enchantments.saveAdditional());
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        enchantments.load(NBT.getList("enchants", Tag.TAG_COMPOUND));
//    }
//
//    @Override
//    public MachineEnchantmentHandler getEnchantmentHandler() {
//        return enchantments;
//    }
//}
