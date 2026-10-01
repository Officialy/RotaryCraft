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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidStack;

import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.java.ReikaRandomHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.HiddenInventorySlot;
import reika.rotarycraft.auxiliary.recipemanagers.ExtractorRecipe;
import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidReceiver;
import reika.rotarycraft.gui.container.machine.inventory.ContainerExtractor;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.DurationRegistry;
import reika.rotarycraft.registry.ExtractOres;
import reika.rotarycraft.registry.ExtractorBonus;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.PowerReceivers;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.RotaryRecipeTypes;

public class BlockEntityExtractor extends InventoriedPowerLiquidReceiver implements ConditionalOperation, HiddenInventorySlot {

    public static final int oreCopy = 50; //50% chance of doubling -> 1.5^4 = 5.1
    public static final int oreCopyNether = 80; //80% chance of doubling -> 1.8^4 = 10.5
    public static final int oreCopyRare = 90; //90% chance of doubling -> 1.9^4 = 13.1

    public static final int DRILL_LIFE = 4096;
    public static final int CAPACITY = 16000;

    /**
     * The number of ticks that each of the four stages has been processing for
     */
    private int[] extractorCookTime = new int[4];

    private int drillTime = ConfigRegistry.EXTRACTORMAINTAIN.getState() ? 0 : DRILL_LIFE;
    private boolean bedrock = false;

    public boolean idle = false;

    public BlockEntityExtractor(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.EXTRACTOR.get(), pos, state);
    }

    public boolean upgrade() {
        if (bedrock)
            return false;
        bedrock = true;
        if (!itemHandler.getStackInSlot(9).isEmpty())
            ReikaItemHelper.dropItem(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, itemHandler.getStackInSlot(9));
        itemHandler.setStackInSlot(9, new ItemStack(RotaryItems.BEDROCK_DRILL.get()));
        return true;
    }

    public boolean isBedrock() {
        return bedrock;
    }

    public int getCookTime(int stage) {
        return extractorCookTime[stage];
    }

    public void setCookTime(int stage, int time) {
        extractorCookTime[stage] = time;
    }

    public void testIdle() {
        boolean works = false;
        for (int i = 0; i < 4; i++) {
            if (this.canProcess(i))
                works = true;
        }
        idle = !works;
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return i == 7 || i == 8;
    }

    @Override
    public boolean validatesInputs() {
        return true;
    }

    private int getSmeltNumber(int stage, ExtractOres ore, ItemStack is, ExtractorRecipe recipe) {
        // Raw-material rates take precedence over the bedrock drill, preserving ore's advantage.
        if (recipe.getDuplicationChance().isPresent())
            return ReikaRandomHelper.doWithChance(recipe.getDuplicationChance().get()) ? 2 : 1;
        if (bedrock && stage == 0)
            return 2;
        if (recipe.getOreDuplicationChance().isPresent())
            return ReikaRandomHelper.doWithChance(recipe.getOreDuplicationChance().get()) ? 2 : 1;
        if (ore != null) {
            if (ore.isRare()) {
                return ReikaRandomHelper.doWithChance(oreCopyRare / 100D) ? 2 : 1;
            }
            // nether ores are richer; in vanilla terms that's the netherrack-ground ore
            // blocks fed into the first stage (nether gold, nether quartz)
            if (stage == 0 && is.is(Tags.Items.ORES_IN_GROUND_NETHERRACK)) {
                return ReikaRandomHelper.doWithChance(oreCopyNether / 100D) ? 2 : 1;
            }
        }
        return ReikaRandomHelper.doWithChance(oreCopy / 100D) ? 2 : 1;
    }

    private void throughPut() {
        for (int i = 1; i < 4; i++) {
            ItemStack out = itemHandler.getStackInSlot(i + 3);
            if (!out.isEmpty()) {
                ItemStack in = itemHandler.getStackInSlot(i);
                if (in.isEmpty()) {
                    itemHandler.setStackInSlot(i, out);
                    itemHandler.setStackInSlot(i + 3, ItemStack.EMPTY);
                } else if (in.getCount() < in.getMaxStackSize()) {
                    if (ItemStack.isSameItemSameComponents(in, out)) {
                        int amt = Math.min(out.getCount(), in.getMaxStackSize() - in.getCount());
                        amt = Math.min(amt, this.getNumberConsecutiveOperations(i));
                        if (amt > 0) {
                            in.setCount(in.getCount() + amt);
                            itemHandler.setStackInSlot(i, in);
                            ReikaInventoryHelper.decrStack(i + 3, itemHandler, amt);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getContainerSize() {
        return 10;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        extractorCookTime = NBT.getIntArray("CookTime").orElse(new int[4]);
        if (extractorCookTime.length != 4)
            extractorCookTime = new int[4];
        drillTime = NBT.getIntOr("drill", 0);
        bedrock = NBT.getBooleanOr("bedrock", false);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putIntArray("CookTime", extractorCookTime);
        NBT.putInt("drill", drillTime);
        NBT.putBoolean("bedrock", bedrock);
    }

    public int getCookProgressScaled(int par1, int i) {
        int time = this.getOperationTime(i);
        if (time <= 0)
            time = 1;
        return (extractorCookTime[i] * par1) / 2 / time;
    }

    public int getDrillLifeScaled(int a) {
        return bedrock ? a : drillTime * a / DRILL_LIFE;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getPowerBelow();
        this.testIdle();
        if (world.isClientSide())
            return;
        this.throughPut();
        if (!bedrock) {
            if (ConfigRegistry.EXTRACTORMAINTAIN.getState()) {
                if (drillTime <= 0 && itemHandler.getStackInSlot(9).is(RotaryItems.HSLA_DRILL.get())) {
                    ReikaInventoryHelper.decrStack(9, itemHandler);
                    drillTime = DRILL_LIFE;
                }
            } else {
                drillTime = DRILL_LIFE;
                itemHandler.setStackInSlot(9, ItemStack.EMPTY);
            }
        }
        for (int i = 0; i < 4; i++) {
            boolean flag1 = false;

            int n = this.getNumberConsecutiveOperations(i);
            for (int k = 0; k < n; k++)
                flag1 |= this.doOperation(n > 1, i);

            if (flag1)
                this.setChanged();
        }
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    private boolean doOperation(boolean multiple, int i) {
        if (this.canProcess(i)) {
            extractorCookTime[i]++;
            int time = this.getOperationTime(i);
            if (extractorCookTime[i] >= time) {
                extractorCookTime[i] = 0;
                this.processItem(i);
            }
            return true;
        } else {
            extractorCookTime[i] = 0;
            return false;
        }
    }

    private boolean canProcess(int i) {
        PowerReceivers p = PowerReceivers.EXTRACTOR;
        if (power < p.getMinPower(i) || omega < p.getMinSpeed(i) || torque < p.getMinTorque(i))
            return false;

        if (i == 0 && !bedrock && drillTime <= 0 && ConfigRegistry.EXTRACTORMAINTAIN.getState())
            return false;

        if ((i == 1 || i == 2) && tank.getFluidAmount() < 125)
            return false;

        ItemStack in = itemHandler.getStackInSlot(i);
        if (in.isEmpty())
            return false;
        ExtractorRecipe recipe = this.getExtractionRecipe(i, in);
        if (recipe == null)
            return false;
        if (i == 3) {
            var bonus = this.getBonus(recipe, in);
            if (bonus != null && !this.canFit(8, bonus.output().create())) return false;
        }
        ItemStack itemstack = recipe.getOutput();
        int maximumCopies = recipe.getDuplicationChance().map(chance -> chance > 0 ? 2 : 1)
                .orElse(bedrock && i == 0 || recipe.getOreDuplicationChance().orElse(.5) > 0 ? 2 : 1);
        itemstack.setCount(itemstack.getCount() * maximumCopies);
        return this.canFit(i + 4, itemstack);
    }

    private boolean canFit(int slot, ItemStack itemstack) {
        ItemStack out = itemHandler.getStackInSlot(slot);
        int limit = Math.min(this.getInventoryStackLimit(), itemstack.getMaxStackSize());
        if (itemstack.getCount() > limit) return false;
        if (out.isEmpty())
            return true;
        if (!ItemStack.isSameItemSameComponents(out, itemstack))
            return false;
        return out.getCount() + itemstack.getCount() <= limit;
    }

    public ExtractorRecipe getExtractionRecipe(int stage, ItemStack in) {
        var recipes = reika.rotarycraft.modinterface.jei.RotaryRecipeSync.getRecipes(level);
        if (recipes == null || in.isEmpty())
            return null;
        return recipes.byType(RotaryRecipeTypes.EXTRACTOR.get()).stream()
                .filter(h -> h.value().matches(stage, in))
                .sorted(java.util.Comparator.<RecipeHolder<ExtractorRecipe>>comparingInt(h -> h.value().getPriority()).reversed()
                        .thenComparing(h -> h.id().identifier().toString()))
                .map(RecipeHolder::value).findFirst().orElse(null);
    }

    private void processItem(int i) {
        ItemStack in = itemHandler.getStackInSlot(i);
        ExtractorRecipe recipe = this.getExtractionRecipe(i, in);
        if (recipe == null)
            return;
        ItemStack itemstack = recipe.getOutput();
        ExtractOres ore = i == 0 ? ExtractOres.getByOreBlock(in) : ExtractOres.getByStageItem(in);
        int num = this.getSmeltNumber(i, ore, in, recipe);
        ItemStack out = itemHandler.getStackInSlot(i + 4);
        if (out.isEmpty()) {
            ItemStack make = itemstack.copy();
            make.setCount(make.getCount() * num);
            itemHandler.setStackInSlot(i + 4, make);
        } else if (ItemStack.isSameItemSameComponents(out, itemstack)) {
            out.setCount(out.getCount() + itemstack.getCount() * num);
            itemHandler.setStackInSlot(i + 4, out);
        }

        if (i == 0 && !bedrock && drillTime > 0 && ConfigRegistry.EXTRACTORMAINTAIN.getState()) {
            drillTime--;
        }
        if (i == 3) {
            this.bonusItems(recipe, in);
        }

        in.shrink(1);
        itemHandler.setStackInSlot(i, in);
        if (i == 1 || i == 2)
            tank.removeLiquid(125);
    }

    private reika.rotarycraft.auxiliary.recipemanagers.ExtractorBonusOutput getBonus(ExtractorRecipe recipe, ItemStack in) {
        if (!recipe.getBonuses().isEmpty()) return recipe.getAvailableBonus().orElse(null);
        ExtractorBonus legacy = ExtractorBonus.getBonusForIngredient(in);
        return legacy == null ? null : new reika.rotarycraft.auxiliary.recipemanagers.ExtractorBonusOutput(
                net.minecraft.world.item.ItemStackTemplate.fromNonEmptyStack(legacy.getBonusItem()), legacy.getProbability(), java.util.Optional.empty());
    }

    private void bonusItems(ExtractorRecipe recipe, ItemStack is) {
        var e = this.getBonus(recipe, is);
        if (e != null && ReikaRandomHelper.doWithChance(e.chance())) {
            ItemStack bonus = e.output().create();
            ItemStack slot = itemHandler.getStackInSlot(8);
            if (slot.isEmpty())
                itemHandler.setStackInSlot(8, bonus);
            else if (ItemStack.isSameItemSameComponents(slot, bonus) && slot.getCount() + bonus.getCount() <= slot.getMaxStackSize()) {
                slot.setCount(slot.getCount() + bonus.getCount());
                itemHandler.setStackInSlot(8, slot);
            }
        }
    }

    @Override
    public boolean hasModelTransparency() {
        return true;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.EXTRACTOR;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.EXTRACTOR.get();
    }

    @Override
    protected String getTEName() {
        return "extractor";
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        if (slot > 3 && slot < 9)
            return false;
        if (slot == 0)
            return this.getExtractionRecipe(0, is) != null;
        if (slot >= 1 && slot <= 3)
            return this.getExtractionRecipe(slot, is) != null;
        if (slot == 9)
            return !bedrock && ConfigRegistry.EXTRACTORMAINTAIN.getState() && is.is(RotaryItems.HSLA_DRILL.get());
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        int bits = 0b1111;
        for (int i = 0; i < 4; i++) {
            if (this.canProcess(i))
                bits &= ~(1 << i);
        }
        return bits;
    }

    @Override
    public Fluid getInputFluid() {
        return Fluids.WATER;
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public boolean canReceiveFrom(Direction dir) {
        return dir.getStepY() == 0;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe();
    }



    public int getOperationTime(int stage) {
        return DurationRegistry.EXTRACTOR.getOperationTime(omega, stage);
    }

    public int getNumberConsecutiveOperations(int stage) {
        return DurationRegistry.EXTRACTOR.getNumberOperations(omega, stage);
    }

    @Override
    public boolean areConditionsMet() {
        return !tank.isEmpty() && !ReikaInventoryHelper.isEmptyFrom(itemHandler, 0, 4);
    }

    @Override
    public String getOperationalStatus() {
        return tank.isEmpty() ? "No Water" : this.areConditionsMet() ? "Operational" : "No Items";
    }

    @Override
    public boolean isSlotHidden(int slot) {
        return slot == 9;
    }

    @Override
    public int[] getHiddenSlots() {
        return new int[]{9};
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return true;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Extractor");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ContainerExtractor(id, inv, this);
    }
}
