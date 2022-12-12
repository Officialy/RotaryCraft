///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.production;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.Fluids;
//import org.jetbrains.annotations.NotNull;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.instantiable.StepTimer;
//import reika.dragonapi.interfaces.blockentity.XPProducer;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.java.ReikaArrayHelper;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.auxiliary.RotaryAux;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
//import reika.rotarycraft.auxiliary.interfaces.FrictionHeatable;
//import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
//import reika.rotarycraft.base.blockentity.InventoriedRCBlockEntity;
//import reika.rotarycraft.registry.DifficultyEffects;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryBlocks;
//import reika.rotarycraft.registry.RotaryItems;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class BlockEntityBlastFurnace extends InventoriedRCBlockEntity implements TemperatureTE, XPProducer, FrictionHeatable, DiscreteFunction, ConditionalOperation {
//
//    public static final int SMELTTEMP = 600;
//    public static final int BEDROCKTEMP = 1450;//1400;//1000;//1150;
//    public static final int MAXTEMP = 2000;
//    public static final float SMELT_XP = 0.6F;
//    public static final int CENTER_ADDITIVE = 0;
//    public static final int LOWER_ADDITIVE = 11;
//    public static final int UPPER_ADDITIVE = 14;
//    public static final int PATTERN_SLOT = 15;
//    public static final int OUTPUT_CENTER = 10;
//    public static final int OUTPUT_UPPER = 12;
//    public static final int OUTPUT_LOWER = 13;
//    private final StepTimer tempTimer = new StepTimer(20);
//    public int smeltTime = 0;
//    public boolean[] lockedSlots = new boolean[inv.length];
//    public boolean leaveLastItem;
//    private int temperature;
//    private float xp;
//    private BlastFurnacePattern pattern;
//
//    public BlockEntityBlastFurnace(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        super(type, pos, state);
//    }
//
//    @Override
//    protected int getActiveTexture() {
//        return this.getRecipe() != null || this.getCrafting() != null ? 1 : 0;
//    }
//
//    private BlastCrafting getCrafting() {
//        ItemStack[] center = new ItemStack[9];
//        System.arraycopy(inv, 1, center, 0, 9);
//        BlastCrafting c = RecipesBlastFurnace.getRecipes().getCrafting(center, temperature);
//
//        if (c != null && leaveLastItem) {
//            for (int i = 1; i <= 9; i++) {
//                if (inv[i] != null && inv[i].getCount() == 1)
//                    return null;
//            }
//        }
//
//        return c;
//    }
//
//    private BlastRecipe getRecipe() {
//        ItemStack[] center = new ItemStack[9];
//        System.arraycopy(inv, 1, center, 0, 9);
//        BlastRecipe rec = RecipesBlastFurnace.getRecipes().getRecipe(inv[CENTER_ADDITIVE], inv[LOWER_ADDITIVE], inv[UPPER_ADDITIVE], center, temperature);
//
//        if (rec == null)
//            return null;
//
//        if (rec.requiresEmptyOutput()) {
//            if (inv[10] != null || inv[13] != null || inv[12] != null)
//                return null;
//        }
//
//        ItemStack out = rec.outputItem();
//        int num = this.getProducedFor(rec);
//        out = ReikaItemHelper.getSizedItemStack(out, num);
//        if (!this.checkCanMakeItem(out))
//            return null;
//
//        if (leaveLastItem) {
//            for (int i = 1; i <= 9; i++) {
//                if (rec.usesSlot(i - 1) && inv[i] != null && inv[i].getCount() == 1)
//                    return null;
//            }
//        }
//
//        return rec;
//    }
//
//    private boolean checkCanMakeItem(ItemStack out) {
//        return this.canAdd(out, 10) || this.canAdd(out, 13) || this.canAdd(out, 12);
//    }
//
//    private boolean canAdd(ItemStack is, int slot) {
//        if (inv[slot] == null)
//            return true;
//        else {
//            ItemStack in = inv[slot];
//            return ReikaItemHelper.areStacksCombinable(is, in, this.getInventoryStackLimit());
//        }
//    }
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        tempTimer.update();
//        if (tempTimer.checkCap()) {
//            this.updateTemperature(world, pos);
//        }
//
//        BlastRecipe rec = this.getRecipe();
//        BlastCrafting bc = this.getCrafting();
//        if (bc != null) {
//            pattern = bc;
//            if (bc.speed <= 1 || level.getDayTime() % bc.speed == 0)
//                smeltTime++;
//            if (smeltTime >= this.getOperationTime()) {
//                this.craft(bc);
//            }
//        } else if (rec != null) {
//            pattern = rec;
//            smeltTime++;
//            if (smeltTime >= this.getOperationTime()) {
//                this.make(rec);
//            }
//        } else {
//            pattern = null;
//            smeltTime = 0;
//            return;
//        }
//    }
//
//    @Override
//    protected void animateWithTick(Level level, BlockPos blockPos) {
//
//    }
//
//    private void craft(BlastCrafting bc) {
//        smeltTime = 0;
//        if (level.isClientSide)
//            return;
//
//        ItemStack out = bc.outputItem();
//
//        if (!ReikaInventoryHelper.addOrSetStack(out, inv, 10))
//            if (!ReikaInventoryHelper.addOrSetStack(out, inv, 12))
//                if (!ReikaInventoryHelper.addOrSetStack(out, inv, 13))
//                    if (!this.checkSpreadFit(out, out.getCount()))
//                        return;
//
//        xp += out.getCount() * bc.xp;
//        if (this.getPlacer() != null) {
//            out.onCraftedBy(level, this.getPlacer(), out.getCount());
//        }
//
//        for (int i = 1; i < 10; i++) {
//            if (inv[i] != null)
//                ReikaInventoryHelper.decrStack(i, inv);
//        }
//    }
//
//    private void make(BlastRecipe rec) {
//        smeltTime = 0;
//        if (level.isClientSide)
//            return;
//
//        int num = this.getProducedFor(rec);
//        int made = num;
//        ItemStack out = rec.outputItem();
//
//        if (rec.bonusYield > 0) {
//            double chance = DifficultyEffects.BONUSSTEEL.getDouble() * (ReikaMathLibrary.intpow(1.005, num * num) - 1);
//            if (ReikaRandomHelper.doWithChance(chance)) {
//                num *= 1 + DragonAPI.rand.nextFloat() * rec.bonusYield;
//            }
//        }
//
//        if (ReikaItemHelper.matchStacks(out, RotaryBlocks.HSLA_STEEL_BLOCK)) {
//            if (!ReikaInventoryHelper.addOrSetStack(ReikaItemHelper.getSizedItemStack(RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance(), 2 * num), inv, 10))
//                if (!ReikaInventoryHelper.addOrSetStack(ReikaItemHelper.getSizedItemStack(RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance(), 2 * num), inv, 12))
//                    if (!ReikaInventoryHelper.addOrSetStack(ReikaItemHelper.getSizedItemStack(RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance(), 2 * num), inv, 13))
//                        return;
//            if (!ReikaInventoryHelper.addOrSetStack(new ItemStack(Items.COAL, 3 * num, 1), inv, 10))
//                if (!ReikaInventoryHelper.addOrSetStack(new ItemStack(Items.COAL, 3 * num, 1), inv, 12))
//                    if (!ReikaInventoryHelper.addOrSetStack(new ItemStack(Items.COAL, 3 * num, 1), inv, 13))
//                        return;
//            if (!ReikaInventoryHelper.addOrSetStack(new ItemStack(Items.IRON_INGOT, 5 * num, 0), inv, 10))
//                if (!ReikaInventoryHelper.addOrSetStack(new ItemStack(Items.IRON_INGOT, 5 * num, 0), inv, 12))
//                    if (!ReikaInventoryHelper.addOrSetStack(new ItemStack(Items.IRON_INGOT, 5 * num, 0), inv, 13))
//                        return;
//        } else {
//            if (!ReikaInventoryHelper.addOrSetStack(out.getItem(), num, out.getItemDamage(), inv, 10))
//                if (!ReikaInventoryHelper.addOrSetStack(out.getItem(), num, out.getItemDamage(), inv, 12))
//                    if (!ReikaInventoryHelper.addOrSetStack(out.getItem(), num, out.getItemDamage(), inv, 13))
//                        if (!this.checkSpreadFit(out, num))
//                            return;
//        }
//
//        xp += rec.xp * num;
//
//        for (int i = 0; i < rec.primary.numberToUse; i++) {
//            if (ReikaRandomHelper.doWithChance(this.getConsumptionFactor(rec.primary.chanceToUse, made)))
//                ReikaInventoryHelper.decrStack(0, inv);
//        }
//        for (int i = 0; i < rec.secondary.numberToUse; i++) {
//            if (ReikaRandomHelper.doWithChance(this.getConsumptionFactor(rec.secondary.chanceToUse, made)))
//                ReikaInventoryHelper.decrStack(11, inv);
//        }
//        for (int i = 0; i < rec.tertiary.numberToUse; i++) {
//            if (ReikaRandomHelper.doWithChance(this.getConsumptionFactor(rec.tertiary.chanceToUse, made)))
//                ReikaInventoryHelper.decrStack(14, inv);
//        }
//
//        for (int i = 1; i < 10; i++) {
//            if (inv[i] != null)
//                ReikaInventoryHelper.decrStack(i, inv);
//        }
//        RotaryAdvancements a = this.getAchievement(rec);
//        if (a != null)
//            a.triggerAchievement(this.getPlacer());
//
//    }
//
//    private float getConsumptionFactor(float base, int made) {
//        return Mth.clamp(base * made * DifficultyEffects.BLASTCONSUME.getChance(), base, 1);
//    }
//
//    private int getProducedFor(BlastRecipe rec) {
//        int num = 0;
//        for (int i = 1; i < 10; i++) {
//            if (inv[i] != null) {
//                if (rec.isValidMainItem(inv[i]))
//                    num++;
//            }
//        }
//        return rec.getNumberProduced(num);
//    }
//
//    private RotaryAdvancements getAchievement(BlastRecipe rec) {
//        if (rec.isValidMainItem(RotaryItems.HSLA_STEEL_SCRAP))
//            return RotaryAdvancements.RECYCLE;
//        if (ReikaItemHelper.matchStacks(rec.outputItem(), RotaryItems.HSLA_STEEL_INGOT))
//            return RotaryAdvancements.MAKESTEEL;
//        if (ReikaItemHelper.matchStacks(rec.outputItem(), RotaryBlocks.HSLA_STEEL_BLOCK))
//            return RotaryAdvancements.FAILSTEEL;
//        return null;
//    }
//
//    public int getTemperatureScaled(int p1) {
//        return ((p1 * temperature) / MAXTEMP);
//    }
//
//    public void dropXP() {
//        ReikaWorldHelper.splitAndSpawnXP(level, xCoord + DragonAPI.rand.nextFloat(), yCoord + 1.25F, zCoord + DragonAPI.rand.nextFloat(), (int) xp);
//        xp = 0;
//    }
//
//    public float getXP() {
//        return xp;
//    }
//
//    public void clearXP() {
//        xp = 0;
//    }
//
//    private boolean checkSpreadFit(ItemStack is, int num) {
//        int maxfit = 0;
//        int f1 = is.getMaxStackSize() - inv[10].getCount();
//        int f2 = is.getMaxStackSize() - inv[12].getCount();
//        int f3 = is.getMaxStackSize() - inv[13].getCount();
//        maxfit = f1 + f2 + f3;
//        if (num > maxfit)
//            return false;
//        if (f1 > num) {
//            inv[10].getCount() += num;
//            return true;
//        } else {
//            inv[10].getCount() = inv[10].getMaxStackSize();
//            num -= f1;
//        }
//        if (f2 > num) {
//            inv[12].getCount() += num;
//            return true;
//        } else {
//            inv[12].getCount() = inv[12].getMaxStackSize();
//            num -= f2;
//        }
//        if (f3 > num) {
//            inv[12].getCount() += num;
//            return true;
//        } else {
//            inv[13].getCount() = inv[13].getMaxStackSize();
//            num -= f3;
//        }
//        return true;
//    }
//
//    public int getOperationTime() {
//        int time = 2 * ((1500 - (temperature - SMELTTEMP)) / 12); //1500 was MAXTEMP
//        if (time < 1)
//            return 1;
//        return time;
//    }
//
//    public int getCookScaled(int p1) {
//        return ((p1 * smeltTime) / this.getOperationTime());
//    }
//
//    public void updateTemperature(Level world, BlockPos pos) {
//        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
//
//        if (RotaryAux.isNextToWater(world, pos)) {
//            Tamb /= 2;
//        }
//        Direction iceside = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.ICE);
//        if (iceside == null)
//            iceside = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.PACKED_ICE);
//        if (iceside != null) {
//            if (Tamb > 0)
//                Tamb /= 4;
//            ReikaWorldHelper.changeAdjBlock(world, pos, iceside, Fluids.FLOWING_WATER.getFlowing().defaultFluidState().createLegacyBlock(), 0);
//        }
//        int Tadd = 0;
//        if (RotaryAux.isNextToFire(world, pos)) {
//            Tadd += Tamb >= 100 ? 100 : 200;
//        }
//        if (RotaryAux.isNextToLava(world, pos)) {
//            Tadd += Tamb >= 100 ? 400 : 600;
//        }
//        Tamb += Tadd;
//
//        if (temperature > Tamb)
//            temperature--;
//        if (temperature > Tamb * 2)
//            temperature--;
//        if (temperature < Tamb)
//            temperature++;
//        if (temperature * 2 < Tamb)
//            temperature++;
//        if (temperature > MAXTEMP)
//            temperature = MAXTEMP;
//        if (temperature > 100) {
//            Direction side = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.SNOW);
//            if (side == null)
//                side = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.SNOW);
//            if (side != null)
//                ReikaWorldHelper.changeAdjBlock(world, pos, side, Blocks.AIR, 0);
//            side = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.ICE);
//            if (side != null)
//                ReikaWorldHelper.changeAdjBlock(world, pos, side, Fluids.FLOWING_WATER.getFlowing().defaultFluidState().createLegacyBlock(), 0);
//        }
//    }
//
//    public int getContainerSize() {
//        return 16;
//    }
//
//    @Override
//    public int getInventoryStackLimit() {
//        return 64;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putInt("melt", smeltTime);
//        NBT.putInt("temp", temperature);
//        NBT.putFloat("exp", xp);
//
//        NBT.putInt("locks", ReikaArrayHelper.booleanToBitflags(lockedSlots));
//        NBT.putBoolean("last", leaveLastItem);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        smeltTime = NBT.getInt("melt");
//        temperature = NBT.getInt("temp");
//        xp = NBT.getFloat("exp");
//
//        lockedSlots = ReikaArrayHelper.booleanFromBitflags(NBT.getInt("locks"), inv.length);
//        leaveLastItem = NBT.getBoolean("last");
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int i, ItemStack is) {
//        return is != null && this.getSlotForItem(i, is);
//    }
//
//    private boolean getSlotForItem(int slot, ItemStack is) {
//        ItemStack patt = inv[PATTERN_SLOT];
//        if (RotaryItems.CRAFTPATTERN.matchItem(patt) && slot >= 1 && slot <= 9) {
//            return ItemCraftPattern.checkPatternForMatch(this, RecipeMode.BLASTFURN, slot, slot - 1, is, patt);
//        }
//        //ReikaJavaLibrary.pConsole(slot+": "+lockedSlots[slot]);
//        if (lockedSlots[slot])
//            return false;
//        HashSet<Integer> slots = ReikaInventoryHelper.getSlotsBetweenWithItemStack(is, this, 1, 9, false);
//        if (!slots.isEmpty()) {
//            return slots.contains(slot);
//        }
//
//        Set<Integer> types = RecipesBlastFurnace.getRecipes().getInputTypesForItem(is);
//        if (slot == CENTER_ADDITIVE)
//            return types.contains(1);
//        if (slot == LOWER_ADDITIVE)
//            return types.contains(2);
//        if (slot == UPPER_ADDITIVE)
//            return types.contains(3);
//        if (slot >= 1 && slot <= 9)
//            return types.contains(0); //check this last, since there are fewer variants that go in additives
//        return false;
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
//        return MachineRegistry.BLASTFURNACE;
//    }
//
//    @Override
//    public int getThermalDamage() {
//        return 0;
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return i == 10 || i == 12 || i == 13;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return this.getRecipe() == null ? 15 : 0;
//    }
//
//    @Override
//    public void addTemperature(int temp) {
//        temperature += temp;
//    }
//
//    @Override
//    public int getTemperature() {
//        return temperature;
//    }
//
//    public void setTemperature(int temp) {
//        temperature = temp;
//    }
//
//    @Override
//    public void overheat(Level world, BlockPos pos) {
//
//    }
//
//    @Override
//    public void onEMP() {
//    }
//
//    @Override
//    public int getMaxTemperature() {
//        return MAXTEMP;
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return this.getRecipe() != null;
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "Insufficient Temperature or Invalid or Missing Items";
//    }
//
//    @Override
//    public boolean canBeCooledWithFins() {
//        return false;
//    }
//
//    @Override
//    public boolean allowHeatExtraction() {
//        return true;
//    }
//
//    @Override
//    public int getAmbientTemperature() {
//        return 0;
//    }
//
//    @Override
//    public void resetAmbientTemperatureTimer() {
//        tempTimer.reset();
//    }
//
//    @Override
//    public float getMultiplier() {
//        return 1;
//    }
//
//    @Override
//    public void onOverheat(Level world, BlockPos pos) {
//
//    }
//
//    @Override
//    public boolean canBeFrictionHeated() {
//        return true;
//    }
//
//    @Override
//    public boolean allowExternalHeating() {
//        return true;
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
//    @NotNull
//    @Override
//    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
//        return null;
//    }
//
//    @NotNull
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
//    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
//        return false;
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
