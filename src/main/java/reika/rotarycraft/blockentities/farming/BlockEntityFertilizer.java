/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.farming;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.MachineEnchantmentHandler;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.EnchantableMachine;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidReceiver;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

/**
 * Fertilizer: sprays a water + fertilizer mist that force-growth-ticks random blocks in a
 * torque-scaled radius (up to 32). Consumes water constantly and fertilizer items occasionally.
 * Enchantable: Efficiency = more ticks per cycle, Fortune = more consecutive growth ticks per
 * block, Power = +range, Aqua Affinity = less water, Unbreaking = fertilizer lasts longer -- all
 * the legacy filters.
 *
 * <p>26.2 port notes: the legacy forced BlockTickEvent/UpdateFlags mechanism is replaced by
 * {@code randomTick} on grow-capable blocks (BonemealableBlock/crops/saplings/canes/cacti/stems/
 * vines -- the legacy fertilizables list, tag-driven where possible); redstone components are
 * excluded exactly as legacy. The FERTILIZER client packet for growth sparkles is replaced by
 * server-spawned HAPPY_VILLAGER particles. Forestry fertilizer interop is gated out.</p>
 */
public class BlockEntityFertilizer extends InventoriedPowerLiquidReceiver implements RangedEffect,
        ConditionalOperation, EnchantableMachine {

    public static final int CAPACITY = 6000;

    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler()
            .addFilter(Enchantments.EFFICIENCY).addFilter(Enchantments.AQUA_AFFINITY)
            .addFilter(Enchantments.FORTUNE).addFilter(Enchantments.UNBREAKING)
            .addFilter(Enchantments.POWER);

    private int firstValidSlot = -1;

    public BlockEntityFertilizer(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.FERTILIZER.get(), pos, state);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getPowerBelow();

        if (world.isClientSide())
            return;

        this.checkFertilizer();
        int n = this.getUpdatesPerTick();
        for (int i = 0; i < n && this.hasFertilizer(); i++)
            this.tickBlock(world, pos);
    }

    private int getUpdatesPerTick() {
        if (power < MINPOWER)
            return 0;
        return 4 * ReikaMathLibrary.logbase2(Math.max(1, omega)) + enchantments.getEnchantment(Enchantments.EFFICIENCY) * 3;
    }

    private int getConsecutiveUpdates() {
        if (omega < 1048576)
            return 1;
        return 1 + ReikaMathLibrary.logbase2(omega / 1048576);
    }

    private void tickBlock(Level world, BlockPos pos) {
        int r = this.getRange();
        if (r <= 0)
            return;
        int dx = pos.getX() + rand.nextInt(2 * r + 1) - r;
        int dy = pos.getY() + rand.nextInt(2 * r + 1) - r;
        // The mist falls: never tick more than one block above the sprayer (legacy).
        while (dy > pos.getY() + 1)
            dy = pos.getY() + rand.nextInt(2 * r + 1) - r;
        int dz = pos.getZ() + rand.nextInt(2 * r + 1) - r;

        BlockPos p = new BlockPos(dx, dy, dz);
        BlockState state = world.getBlockState(p);
        double dd = ReikaMathLibrary.py3d(dx - pos.getX(), dy - pos.getY(), dz - pos.getZ());
        if (!state.isAir() && dd <= r && this.canTick(state)) {
            if (this.isGrowable(state) && world instanceof ServerLevel sl) {
                int n = this.getConsecutiveUpdates() + enchantments.getEnchantment(Enchantments.FORTUNE);
                for (int i = 0; i < n; i++)
                    state.randomTick(sl, p, sl.getRandom());
                sl.sendParticles(ParticleTypes.HAPPY_VILLAGER, dx + 0.5, dy + 0.5, dz + 0.5, 4, 0.3, 0.3, 0.3, 0);
                if (rand.nextInt(5) == 0)
                    this.consumeItem();
            }
        }
    }

    /** Redstone components must never be force-ticked (legacy exclusion). */
    private boolean canTick(BlockState state) {
        Block b = state.getBlock();
        return !(b instanceof DiodeBlock || b instanceof RedStoneWireBlock);
    }

    /** The legacy fertilizables set (crops, saplings, canes, cacti, stems, vines, mycelium), tag-driven. */
    private boolean isGrowable(BlockState state) {
        Block b = state.getBlock();
        if (b instanceof BonemealableBlock)
            return true;
        // 26.2 removed the SAPLINGS block tag; BonemealableBlock covers saplings.
        if (state.is(BlockTags.CROPS))
            return true;
        return b == Blocks.SUGAR_CANE || b == Blocks.CACTUS || b == Blocks.VINE || b == Blocks.MYCELIUM
                || b == Blocks.MELON_STEM || b == Blocks.PUMPKIN_STEM;
    }

    private void consumeItem() {
        tank.removeLiquid(Math.max(1, 5 - enchantments.getEnchantment(Enchantments.AQUA_AFFINITY) / 2));
        if (rand.nextInt(4 + enchantments.getEnchantment(Enchantments.UNBREAKING)) == 0 && firstValidSlot >= 0) {
            itemHandler.extractItem(firstValidSlot, 1, false);
            if (itemHandler.getStackInSlot(firstValidSlot).isEmpty())
                this.checkFertilizer();
        }
    }

    public boolean isValidFertilizer(ItemStack is) {
        if (is.getItem() == Items.BONE_MEAL)
            return true;
        if (is.getItem() == RotaryItems.COMPOST.get())
            return true;
        // FORESTRY-PORT: forestry fertilizer.
        return false;
    }

    public boolean hasFertilizer() {
        return firstValidSlot >= 0;
    }

    private void checkFertilizer() {
        firstValidSlot = -1;
        if (tank.isEmpty())
            return;
        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack is = itemHandler.getStackInSlot(i);
            if (!is.isEmpty() && this.isValidFertilizer(is)) {
                firstValidSlot = i;
                return;
            }
        }
    }

    @Override
    public int getRange() {
        if (torque <= 0)
            return 0;
        int r = 2 * (int) ReikaMathLibrary.logbase(torque, 2) + enchantments.getEnchantment(Enchantments.POWER);
        return Math.min(r, this.getMaxRange());
    }

    @Override
    public int getMaxRange() {
        return 32;
    }

    @Override
    public int getContainerSize() {
        return 18;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return this.isValidFertilizer(is);
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    /** Exposes the fertilizer inventory for the container (base handler is public but named). */
    public reika.dragonapi.instantiable.storage.ManagedItemHandler getFertilizerHandler() {
        return itemHandler;
    }

    /** Public tank level for the GUI. */
    public int getTankLevel() {
        return tank.getFluidLevel();
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe();
    }

    @Override
    public Fluid getInputFluid() {
        return Fluids.WATER;
    }

    @Override
    public boolean canReceiveFrom(Direction from) {
        return from != Direction.DOWN;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction action) {
        return FluidStack.EMPTY;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        if (!this.canReceiveFrom(from) || !this.isValidFluid(resource.getFluid()))
            return 0;
        return tank.fill(resource, action);
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public boolean areConditionsMet() {
        return !tank.isEmpty() && this.hasFertilizer();
    }

    @Override
    public String getOperationalStatus() {
        return tank.isEmpty() ? "No Water" : this.areConditionsMet() ? "Operational" : "No Fertilizer Items";
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        phi += (float) ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.FERTILIZER;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.FERTILIZER.get();
    }

    @Override
    protected String getTEName() {
        return "fertilizer";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return this.hasFertilizer() ? 0 : 15;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.put("enchants", enchantments.saveAdditional());
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        enchantments.load(NBT.getListOrEmpty("enchants"));
    }

    @Override
    public MachineEnchantmentHandler getEnchantmentHandler() {
        return enchantments;
    }

}
