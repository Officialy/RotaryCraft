/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.libraries.ReikaEnchantmentHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.auxiliary.MachineEnchantmentHandler;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.EnchantableMachine;
import reika.rotarycraft.auxiliary.interfaces.MultiOperational;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.registry.DurationRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * Drop Processor: "mines" block items fed into slot 0, producing their loot-table drops in slot 1
 * (multi-item results queue in an overflow buffer and dispense as slot 1 empties). Fortune raises
 * drops, Efficiency speeds processing.
 *
 * <p>26.2 port notes: the legacy handler registry survives (DropProcessing / BlockDropProcessing /
 * NoProcessing) but the mod-loot handlers -- IC2 scrap boxes, Thaumcraft loot bags, Forestry crated
 * items, Mystcraft notebooks/folders, GregTech crates, hard ores -- are gated out (mods not
 * ported). Block drops come from the modern loot-table path with a virtual fortune-enchanted
 * pickaxe at the machine's own position.</p>
 */
public class BlockEntityDropProcessor extends InventoriedPowerReceiver implements MultiOperational,
        EnchantableMachine, ConditionalOperation {

    // MOD-PORT: ScrapBoxProcessing (IC2), ThaumBagProcessing, CratedItemProcessing (Forestry),
    // MystNotebookProcessing/MystFolderProcessing, GregCrateProcessing, HardOreProcessing.
    private static final List<DropProcessing> dropHandlers = new ArrayList<>();
    private static final NoProcessing INVALID = new NoProcessing();

    static {
        new BlockDropProcessing().register();
    }

    private final ArrayList<ItemStack> overflow = new ArrayList<>();

    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler()
            .addFilter(Enchantments.FORTUNE).addFilter(Enchantments.EFFICIENCY);

    public int dropProcessTime;
    public int overflowCount;

    public BlockEntityDropProcessor(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.DROPS.get(), pos, state);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        if (world.isClientSide())
            return;

        this.getPower(false);

        boolean flag1 = false;
        if (!overflow.isEmpty() && itemHandler.getStackInSlot(1).isEmpty()) {
            itemHandler.setStackInSlot(1, overflow.remove(0));
        }
        else if (power >= MINPOWER && torque >= MINTORQUE) {
            int n = this.getNumberConsecutiveOperations();
            for (int i = 0; i < n; i++)
                flag1 |= this.doOperation(world, pos, n > 1);
        }
        else {
            dropProcessTime = 0;
        }

        overflowCount = overflow.size();

        if (flag1)
            this.setChanged();
    }

    private boolean doOperation(Level world, BlockPos pos, boolean multiple) {
        ItemStack in = itemHandler.getStackInSlot(0);
        if (in.isEmpty()) {
            dropProcessTime = 0;
            return false;
        }
        if (!itemHandler.getStackInSlot(1).isEmpty() && !this.canContinueProcessingWithOutput()) {
            dropProcessTime = Math.max(0, Math.min(dropProcessTime, this.getOperationTime() - 1));
            return false;
        }
        if (!overflow.isEmpty()) {
            dropProcessTime = 0;
            return false;
        }
        if (!isProcessable(in)) {
            dropProcessTime = 0;
            return false;
        }
        dropProcessTime++;
        if (multiple || dropProcessTime >= this.getOperationTime()) {
            dropProcessTime = 0;
            this.processItem(world, pos);
        }
        return true;
    }

    private boolean canContinueProcessingWithOutput() {
        DropProcessing h = getHandler(itemHandler.getStackInSlot(0));
        if (h.allowsStacking()) {
            Collection<ItemStack> c = this.runHandler(h, itemHandler.getStackInSlot(0));
            if (c.size() != 1)
                return false;
            ItemStack is = c.iterator().next();
            ItemStack out = itemHandler.getStackInSlot(1);
            return ItemStack.isSameItemSameComponents(is, out)
                    && is.getCount() + out.getCount() <= Math.min(this.getInventoryStackLimit(), is.getMaxStackSize());
        }
        return false;
    }

    private Collection<ItemStack> runHandler(DropProcessing dp, ItemStack in) {
        try {
            int fortune = enchantments.getEnchantment(Enchantments.FORTUNE);
            Player ep = this.getPlacer();
            return dp.generateItems(level, worldPosition, fortune, ep, rand, in);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static DropProcessing getHandler(ItemStack is) {
        if (is.isEmpty())
            return INVALID;
        for (DropProcessing d : dropHandlers) {
            if (d.isValidItem(is))
                return d;
        }
        return INVALID;
    }

    public static boolean isProcessable(ItemStack is) {
        return getHandler(is) != INVALID;
    }

    private void processItem(Level world, BlockPos pos) {
        DropProcessing dp = getHandler(itemHandler.getStackInSlot(0));
        Collection<ItemStack> c = this.runHandler(dp, itemHandler.getStackInSlot(0));
        ArrayList<ItemStack> li = ReikaItemHelper.collateItemList(c);
        if (!li.isEmpty()) {
            ItemStack first = li.remove(0);
            ItemStack out = itemHandler.getStackInSlot(1);
            if (out.isEmpty())
                itemHandler.setStackInSlot(1, first);
            else if (ItemStack.isSameItemSameComponents(out, first))
                out.grow(first.getCount());
            else
                overflow.add(first);
            overflow.addAll(li);
        }
        itemHandler.extractItem(0, 1, false);
    }

    /** Progress out of {@code par} for the GUI arrow. */
    public int getCookScaled(int par) {
        int time = this.getOperationTime();
        return time > 0 ? Math.min(par, dropProcessTime * par / time) : 0;
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return slot == 0 && isProcessable(is);
    }

    public boolean canExtractItem(int slot, ItemStack is, int side) {
        return slot > 0;
    }

    /** Exposes the inventory for the container (base handler is protected). */
    public reika.dragonapi.instantiable.storage.ManagedItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.DROPS;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.DROPS.get();
    }

    @Override
    protected String getTEName() {
        return "dropprocessor";
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putShort("CookTime", (short) dropProcessTime);
        NBT.putInt("overflowCount", overflowCount);
        NBT.put("enchants", enchantments.saveAdditional());

        // 26.1: ItemStack.save(CompoundTag) is gone; encode via ItemStack.CODEC + RegistryOps.
        var regAcc = level == null ? RegistryAccess.EMPTY : level.registryAccess();
        var ops = regAcc.createSerializationContext(NbtOps.INSTANCE);
        ListTag li = new ListTag();
        for (ItemStack is : overflow)
            ItemStack.CODEC.encodeStart(ops, is).result().ifPresent(li::add);
        NBT.put("extra", li);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        dropProcessTime = NBT.getShortOr("CookTime", (short) 0);
        overflowCount = NBT.getIntOr("overflowCount", 0);
        enchantments.load(NBT.getListOrEmpty("enchants"));

        overflow.clear();
        var regAcc = level == null ? RegistryAccess.EMPTY : level.registryAccess();
        var ops = regAcc.createSerializationContext(NbtOps.INSTANCE);
        ListTag li = NBT.getListOrEmpty("extra");
        for (int i = 0; i < li.size(); i++) {
            ItemStack is = ItemStack.CODEC.parse(ops, li.getCompoundOrEmpty(i)).result().orElse(ItemStack.EMPTY);
            if (!is.isEmpty())
                overflow.add(is);
        }
    }

    @Override
    public int getOperationTime() {
        int base = DurationRegistry.DROPS.getOperationTime(omega);
        float ench = ReikaEnchantmentHelper.getEfficiencyMultiplier(enchantments.getEnchantment(Enchantments.EFFICIENCY));
        return (int) (base / ench);
    }

    @Override
    public int getNumberConsecutiveOperations() {
        return DurationRegistry.DROPS.getNumberOperations(omega);
    }

    @Override
    public boolean areConditionsMet() {
        return !itemHandler.getStackInSlot(0).isEmpty() && ReikaItemHelper.isBlock(itemHandler.getStackInSlot(0))
                && itemHandler.getStackInSlot(1).isEmpty() && overflow.isEmpty();
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "Missing Items/Full Output";
    }

    @Override
    public MachineEnchantmentHandler getEnchantmentHandler() {
        return enchantments;
    }

    // --- Handler framework (legacy DropProcessing) --------------------------

    public static abstract class DropProcessing {

        protected abstract Collection<ItemStack> generateItems(Level world, BlockPos pos, int fortune, Player ep, Random rand, ItemStack src) throws Exception;

        protected abstract boolean allowsStacking();

        public abstract boolean isValidItem(ItemStack is);

        protected abstract boolean isLoadable();

        protected final void register() {
            if (this.isLoadable())
                dropHandlers.add(this);
        }

    }

    private static class NoProcessing extends DropProcessing {

        @Override
        protected Collection<ItemStack> generateItems(Level world, BlockPos pos, int fortune, Player ep, Random rand, ItemStack src) {
            return new ArrayList<>();
        }

        @Override
        protected boolean allowsStacking() {
            return false;
        }

        @Override
        public boolean isValidItem(ItemStack is) {
            return false;
        }

        @Override
        protected boolean isLoadable() {
            return true;
        }

    }

    /** Block items -> their loot-table drops (a virtual fortune-enchanted pickaxe). */
    private static class BlockDropProcessing extends DropProcessing {

        @Override
        protected Collection<ItemStack> generateItems(Level world, BlockPos pos, int fortune, Player ep, Random rand, ItemStack src) {
            if (!(world instanceof ServerLevel sl))
                return new ArrayList<>();
            Block b = Block.byItem(src.getItem());
            if (b == Blocks.AIR)
                return new ArrayList<>();
            ItemStack tool = new ItemStack(Items.DIAMOND_PICKAXE);
            if (fortune > 0)
                tool.enchant(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), fortune);
            return new ArrayList<>(Block.getDrops(b.defaultBlockState(), sl, pos, null, ep, tool));
        }

        @Override
        protected boolean allowsStacking() {
            return false;
        }

        @Override
        public boolean isValidItem(ItemStack is) {
            return ReikaItemHelper.isBlock(is);
        }

        @Override
        protected boolean isLoadable() {
            return true;
        }

    }

}
