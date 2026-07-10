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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.instantiable.data.blockstruct.TreeReader;
import reika.dragonapi.instantiable.data.immutable.BlockKey;
import reika.dragonapi.libraries.ReikaEnchantmentHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.MachineEnchantmentHandler;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DamagingContact;
import reika.rotarycraft.auxiliary.interfaces.EnchantableMachine;
import reika.rotarycraft.auxiliary.interfaces.MultiOperational;
import reika.rotarycraft.auxiliary.interfaces.Wettable;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.registry.DurationRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

/**
 * Woodcutter: fells the whole tree in front of it (3x3 intake at machine level, scanned by
 * {@link TreeReader}), leaves-first and top-down, replanting the sapling from slot 0 (free with
 * Infinity). Fortune boosts drops, Efficiency speeds cutting. Drops route: saplings restock slot 0,
 * everything else tries the chest below, then spills at the machine.
 *
 * <p>26.2 port notes: tree identity is tag-driven through TreeReader (legacy TreeType/ModWoodList
 * gone) so any correctly-tagged tree works; the slime-tree jam and TwilightForest/dye-tree special
 * cases are gated out (mods not ported -- {@code wet()}/jam kept for the Wettable contract). The
 * legacy non-INSTACUT falling-block cosmetic mode is not ported; cutting always drops items. The
 * legacy InertIInv/ProcessingMachine markers are not ported (no GUI; slot 0 is machine-managed).</p>
 */
public class BlockEntityWoodcutter extends InventoriedPowerReceiver implements EnchantableMachine,
        DamagingContact, Wettable, MultiOperational, ConditionalOperation {

    private static final int MAX_JAM = 20;

    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler()
            .addFilter(Enchantments.INFINITY).addFilter(Enchantments.FORTUNE).addFilter(Enchantments.EFFICIENCY);

    private final TreeReader tree = new TreeReader();
    private TreeReader treeCopy = new TreeReader();

    private boolean cuttingTree;

    private int jam = 0;
    private int jamColor = -1;

    private long lastSoundTick = -1;

    public BlockEntityWoodcutter(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.WOODCUTTER.get(), pos, state);
    }

    public int getJamColor() {
        return jamColor;
    }

    @Override
    public void wet() {
        jam--;
        if (jam <= 0) {
            jam = 0;
            jamColor = -1;
        }
    }

    public boolean isJammed() {
        return jam > MAX_JAM;
    }

    /** The horizontal direction the blade faces (tree side); power reads from behind. */
    private Direction getFacing() {
        BlockState state = this.getBlockState();
        Direction d = state.hasProperty(BlockRotaryCraftMachine.FACING) ? state.getValue(BlockRotaryCraftMachine.FACING) : Direction.NORTH;
        return d.getAxis().isHorizontal() ? d : Direction.NORTH;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;

        tree.setWorld(world);
        treeCopy.setWorld(world);

        Direction facing = this.getFacing();
        read = facing.getOpposite();
        this.getPower(false);

        if (power < MINPOWER || torque < MINTORQUE)
            return;

        if (this.isJammed())
            return;

        if (world.isClientSide())
            return;

        BlockPos edit = pos.relative(facing);
        if (tree.isEmpty() && this.hasWood()) {
            tree.reset();
            Block log = TreeReader.getTreeLog(world, edit);
            if (log == null)
                log = TreeReader.getTreeLog(world, edit.relative(facing));

            if (log != null) {
                tree.setTree(log);
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        tree.addTree(world, edit.offset(i, 0, j));
                    }
                }
            }

            if (!tree.isEmpty())
                cuttingTree = true;

            this.checkAndMatchInventory();

            tree.sortBlocksByHeight(false);
            tree.reverseBlockOrder();
            tree.sortBlocksByDistance(pos);
            tree.sort(new LeafPrioritizer(world));
            treeCopy = (TreeReader) tree.copy();
        }

        // Clear tree matter growing over the machine itself.
        BlockPos above = pos.above();
        BlockState aboveState = world.getBlockState(above);
        if (!aboveState.isAir() && (aboveState.is(BlockTags.LOGS) || aboveState.is(BlockTags.LEAVES))) {
            for (ItemStack is : this.getDrops(world, above))
                ReikaItemHelper.dropItem(world, pos.getX() + 0.5, pos.getY() - 0.25, pos.getZ() + 0.5, is);
            world.setBlockAndUpdate(above, Blocks.AIR.defaultBlockState());
        }

        if (tree.isEmpty())
            return;

        if (tickcount < this.getOperationTime())
            return;
        tickcount = 0;

        if (!cuttingTree && !tree.isValidTree()) {
            tree.reset();
            tree.clear();
            return;
        }

        for (int i = 0; i < this.getNumberConsecutiveOperations(); i++) {
            BlockPos c = tree.getNextAndMoveOn();
            this.cutCoord(world, pos, c);
            if (tree.isEmpty()) {
                cuttingTree = false;
                break;
            }
        }
    }

    private void cutCoord(Level world, BlockPos machine, BlockPos c) {
        BlockState state = world.getBlockState(c);
        if (state.isAir())
            return;

        boolean wasLog = tree.getTreeLog() != null && state.is(tree.getTreeLog());
        this.cutBlock(world, machine, c, state);

        // Replant on the ground row (machine level) where the trunk stood.
        if (c.getY() == machine.getY() && wasLog) {
            BlockKey plant = this.getPlantedSapling();
            if (plant != null) {
                BlockState sap = plant.blockID.getBlock().defaultBlockState();
                if (world.getBlockState(c).isAir() && sap.canSurvive(world, c)) {
                    if (!itemHandler.getStackInSlot(0).isEmpty() && !enchantments.hasEnchantment(Enchantments.INFINITY))
                        itemHandler.extractItem(0, 1, false);
                    world.setBlockAndUpdate(c, sap);
                }
            }
        }
    }

    private void cutBlock(Level world, BlockPos machine, BlockPos c, BlockState state) {
        this.dropBlocks(world, machine, c, state);
        world.setBlockAndUpdate(c, Blocks.AIR.defaultBlockState());

        if (lastSoundTick != this.getTicksExisted()) {
            boolean leaf = state.is(BlockTags.LEAVES);
            world.playSound(null, machine, leaf ? SoundEvents.GRASS_BREAK : SoundEvents.WOOD_BREAK,
                    SoundSource.BLOCKS, 0.5F + rand.nextFloat() * 0.5F, 1F);
            lastSoundTick = this.getTicksExisted();
        }
    }

    /** Loot-table drops at this machine's Fortune level (a virtual fortune-enchanted axe). */
    private List<ItemStack> getDrops(Level world, BlockPos c) {
        BlockState state = world.getBlockState(c);
        if (!(world instanceof ServerLevel sl))
            return new ArrayList<>();
        ItemStack tool = new ItemStack(Items.DIAMOND_AXE);
        int fortune = enchantments.getEnchantment(Enchantments.FORTUNE);
        if (fortune > 0)
            tool.enchant(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE), fortune);
        List<ItemStack> ret = new ArrayList<>(Block.getDrops(state, sl, c, world.getBlockEntity(c), null, tool));

        // Legacy oak apple bonus, fortune-scaled.
        if (tree.getTreeLog() == Blocks.OAK_LOG) {
            int n = 0;
            if (fortune >= 5) n = 4;
            else if (fortune >= 3) n = 6;
            else if (fortune >= 2) n = 10;
            else if (fortune >= 1) n = 20;
            if (n > 0 && rand.nextInt(n) == 0)
                ret.add(new ItemStack(Items.APPLE));
        }
        return ret;
    }

    private void dropBlocks(Level world, BlockPos machine, BlockPos c, BlockState state) {
        BlockKey sapling = tree.getSapling();

        List<ItemStack> drops = this.getDrops(world, c);
        if (tree.getTreeLog() != null && state.is(tree.getTreeLog())) {
            if (rand.nextInt(3) == 0)
                drops.add(new ItemStack(RotaryItems.SAWDUST.get(), 1 + rand.nextInt(4)));
        }

        for (ItemStack todrop : drops) {
            boolean isSapling = sapling != null && todrop.getItem() == sapling.blockID.getBlock().asItem();
            if (isSapling && !enchantments.hasEnchantment(Enchantments.INFINITY)) {
                ItemStack slot = itemHandler.getStackInSlot(0);
                if (slot.isEmpty()) {
                    itemHandler.setStackInSlot(0, todrop.copy());
                    continue;
                }
                else if (ItemStack.isSameItemSameComponents(slot, todrop) && slot.getCount() < slot.getMaxStackSize()) {
                    int add = Math.min(todrop.getCount(), slot.getMaxStackSize() - slot.getCount());
                    slot.grow(add);
                    todrop.shrink(add);
                    if (todrop.isEmpty())
                        continue;
                }
            }
            this.chestCheck(todrop);
            if (!todrop.isEmpty())
                ReikaItemHelper.dropItem(world, machine.getX() + 0.5, machine.getY() - 0.25, machine.getZ() + 0.5, todrop);
        }
    }

    /** Feed drops into an inventory below the machine, collating stacks. */
    private void chestCheck(ItemStack is) {
        if (level == null)
            return;
        BlockEntity te = level.getBlockEntity(worldPosition.below());
        if (te instanceof Container ii) {
            int max = Math.min(ii.getMaxStackSize(), is.getMaxStackSize());
            for (int i = 0; i < ii.getContainerSize() && !is.isEmpty(); i++) {
                ItemStack in = ii.getItem(i);
                if (!in.isEmpty() && ItemStack.isSameItemSameComponents(is, in) && in.getCount() < max) {
                    int add = Math.min(max - in.getCount(), is.getCount());
                    in.grow(add);
                    is.shrink(add);
                }
            }
            for (int i = 0; i < ii.getContainerSize() && !is.isEmpty(); i++) {
                if (ii.getItem(i).isEmpty() && ii.canPlaceItem(i, is)) {
                    ii.setItem(i, is.copy());
                    is.setCount(0);
                }
            }
            ii.setChanged();
        }
    }

    /** Slot 0 only holds this tree's sapling; anything else is pushed to the chest below. */
    private void checkAndMatchInventory() {
        BlockKey sapling = tree.getSapling();
        ItemStack slot = itemHandler.getStackInSlot(0);
        if (slot.isEmpty())
            return;
        if (sapling == null || slot.getItem() != sapling.blockID.getBlock().asItem()) {
            ItemStack is = slot.copy();
            itemHandler.setStackInSlot(0, ItemStack.EMPTY);
            this.chestCheck(is);
            if (!is.isEmpty())
                ReikaItemHelper.dropItem(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1, worldPosition.getZ() + 0.5, is);
        }
    }

    public BlockKey getPlantedSapling() {
        if (!this.shouldPlantSapling())
            return null;
        return treeCopy.getSapling();
    }

    private boolean shouldPlantSapling() {
        if (enchantments.hasEnchantment(Enchantments.INFINITY))
            return true;
        BlockKey sapling = treeCopy.getSapling();
        ItemStack slot = itemHandler.getStackInSlot(0);
        return sapling != null && !slot.isEmpty() && slot.getItem() == sapling.blockID.getBlock().asItem();
    }

    /** Any scannable log in the 3x3 intake in front of the blade. */
    private boolean hasWood() {
        if (level == null)
            return false;
        BlockPos edit = worldPosition.relative(this.getFacing());
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (TreeReader.getTreeLog(level, edit.offset(i, 0, j)) != null)
                    return true;
            }
        }
        return false;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        if (power < MINPOWER || torque < MINTORQUE || this.isJammed())
            return;
        phi += (float) ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.WOODCUTTER;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.WOODCUTTER.get();
    }

    @Override
    protected String getTEName() {
        return "woodcutter";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return this.hasWood() ? 0 : 15;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return false;
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putInt("jam", jam);
        NBT.putInt("jamc", jamColor);

        NBT.putBoolean("cutting", cuttingTree);

        NBT.put("enchants", enchantments.saveAdditional());
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        jam = NBT.getIntOr("jam", 0);
        jamColor = NBT.getIntOr("jamc", -1);

        cuttingTree = NBT.getBooleanOr("cutting", false);

        enchantments.load(NBT.getListOrEmpty("enchants"));
    }

    @Override
    public int getOperationTime() {
        int base = DurationRegistry.WOODCUTTER.getOperationTime(omega);
        float ench = ReikaEnchantmentHelper.getEfficiencyMultiplier(enchantments.getEnchantment(Enchantments.EFFICIENCY));
        return (int) (base / ench);
    }

    @Override
    public int getNumberConsecutiveOperations() {
        return DurationRegistry.WOODCUTTER.getNumberOperations(omega);
    }

    @Override
    public boolean areConditionsMet() {
        return this.hasWood();
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Tree";
    }

    @Override
    public int getContactDamage() {
        return 4;
    }

    @Override
    public boolean canDealDamage() {
        return power >= MINPOWER && torque >= MINTORQUE;
    }

    @Override
    public DamageSource getDamageType() {
        return RotaryCraft.grind.get(level);
    }

    @Override
    public MachineEnchantmentHandler getEnchantmentHandler() {
        return enchantments;
    }

    public boolean hasWork() {
        return this.areConditionsMet();
    }

    /** Cut leaves before logs (legacy LeafPrioritizer), tag-driven. */
    private static class LeafPrioritizer implements Comparator<BlockPos> {

        private final Level world;

        private LeafPrioritizer(Level world) {
            this.world = world;
        }

        @Override
        public int compare(BlockPos b1, BlockPos b2) {
            boolean l1 = world.getBlockState(b1).is(BlockTags.LEAVES);
            boolean l2 = world.getBlockState(b2).is(BlockTags.LEAVES);
            if (l1 == l2)
                return 0;
            return l1 ? -1 : 1;
        }

    }
}
