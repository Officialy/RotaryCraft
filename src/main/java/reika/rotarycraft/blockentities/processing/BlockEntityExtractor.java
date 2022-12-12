///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.processing;
//
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.interfaces.registry.OreType;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.java.ReikaArrayHelper;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.level.ReikaBlockHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.auxiliary.CustomExtractLoader;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.HiddenInventorySlot;
//import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidReceiver;
//
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//
//public class BlockEntityExtractor extends InventoriedPowerLiquidReceiver implements ConditionalOperation, HiddenInventorySlot {
//
//    public static final int oreCopy = 50; //50% chance of doubling -> 1.5^4 = 5.1
//    public static final int oreCopyNether = 80; //80% chance of doubling -> 1.8^4 = 10.5
//    public static final int oreCopyRare = 90; //90% chance of doubling -> 1.9^4 = 13.1
//    public static final int DRILL_LIFE = 4096;
//    public static final int CAPACITY = 16000;
//    public boolean idle = false;
//    /**
//     * The number of ticks that the current item has been cooking for
//     */
//    private int[] extractorCookTime = new int[4];
//    private int drillTime = RotaryConfig.COMMON.EXTRACTORMAINTAIN.get() ? 0 : DRILL_LIFE;
//    private boolean bedrock = false;
//
//    public boolean upgrade() {
//        if (bedrock)
//            return false;
//        bedrock = true;
//        if (inv[9] != null)
//            ReikaItemHelper.dropItem(level, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, inv[9]);
//        inv[9] = RotaryItems.bedrockdrill.copy();
//        return true;
//    }
//
//    public boolean isBedrock() {
//        return bedrock;
//    }
//
//    public int getCookTime(int stage) {
//        return extractorCookTime[stage];
//    }
//
//
//    public void setCookTime(int stage, int time) {
//        extractorCookTime[stage] = time;
//    }
//
//    public void testIdle() {
//		/*
//		for (int i = 0; i < 4; i++)
//			if (power < machine.getMinPower(i))
//				return;
//		 */
//        boolean works = false;
//        for (int i = 0; i < 4; i++) {
//            if (this.canProcess(i))
//                works = true;
//        }
//        idle = !works;
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        //if (i >= 9)
//        //	return false;
//        return i == 7 || i == 8;
//    }
//
//    @Override
//    public boolean validatesInputs() {
//        return true;
//    }
//
//    private int getSmeltNumber(int stage, OreType ore, ItemStack is) {
//        if (bedrock && stage == 0)
//            return 2;
//        //ReikaJavaLibrary.pConsole(RotaryConfig.getDifficulty());
//        if (ore != null) {
//            if (ore.getRarity() == OreRarity.RARE) {
//                if (ReikaRandomHelper.doWithChance(oreCopyRare / 100D))
//                    return 2;
//                else
//                    return 1;
//            }
//            boolean nether = ore instanceof ModOreList && ((ModOreList) ore).isNetherOres();
//            if (is.getItemDamage() == 1 && (ore == ModOreList.FORCE || ore == ModOreList.MIMICHITE))
//                nether = true;
//            if (ReikaItemHelper.matchStackWithBlock(is, MagicCropHandler.getInstance().netherOreID))
//                nether = true;
//
//            if (nether) { //.isNetherOres()
//                if (ReikaRandomHelper.doWithChance(oreCopyNether / 100D))
//                    return 2;
//                else
//                    return 1;
//            }
//        }
//        return ReikaRandomHelper.doWithChance(oreCopy / 100D) ? 2 : 1;
//    }
//
//    private void throughPut() {
//        for (int i = 1; i < 4; i++) {
//            if (inv[i + 3] != null) {
//                if (inv[i] == null) {
//                    inv[i] = inv[i + 3];
//                    inv[i + 3] = null;
//                } else if (inv[i].getCount() < inv[i].getMaxStackSize()) {
//                    if (ReikaItemHelper.matchStacks(inv[i], inv[i + 3])) {
//                        int amt = Math.min(inv[i + 3].getCount(), inv[i].getMaxStackSize() - inv[i].getCount());
//                        amt = Math.min(amt, this.getNumberConsecutiveOperations(i));
//                        if (amt > 0) {
//                            inv[i].getCount() += amt;
//                            ReikaInventoryHelper.decrStack(i + 3, inv, amt);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    public int getContainerSize() {
//        return 10;
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        extractorCookTime = NBT.getIntArray("CookTime");
//        drillTime = NBT.getInt("drill");
//        bedrock = NBT.getBoolean("bedrock");
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putIntArray("CookTime", extractorCookTime);
//        NBT.putInt("drill", drillTime);
//        NBT.putBoolean("bedrock", bedrock);
//    }
//
//    public int getCookProgressScaled(int par1, int i) {
//        int j = i + 1;
//        int time = -1;
//        switch (j) {
//            case 1:
//                time = 30 * (30 - (int) (2 * ReikaMathLibrary.logbase(omega, 2)));
//                break;
//            case 2:
//                time = (800 - (int) (40 * ReikaMathLibrary.logbase(omega, 2))) / 2;
//                break;
//            case 3:
//                time = 600 - (int) (30 * ReikaMathLibrary.logbase(omega, 2));
//                break;
//            case 4:
//                time = 1200 - (int) (80 * ReikaMathLibrary.logbase(omega, 2));
//                break;
//        }
//        if (time == -1)
//            return 0;
//        if (time <= 0)
//            time = 1;
//        return (extractorCookTime[i] * par1) / 2 / time;
//    }
//
//    public int getDrillLifeScaled(int a) {
//        return bedrock ? a : drillTime * a / DRILL_LIFE;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getPowerBelow();
//        if (DragonAPI.debugtest)
//            tank.addLiquid(1000, Fluids.WATER);
//        this.testIdle();
//        if (world.isClientSide)
//            return;
//        this.throughPut();
//        if (!bedrock) {
//            if (RotaryConfig.COMMON.EXTRACTORMAINTAIN.getState()) {
//                if (drillTime <= 0 && inv[9] != null && ReikaItemHelper.matchStacks(inv[9], RotaryItems.drill)) {
//                    ReikaInventoryHelper.decrStack(9, inv);
//                    drillTime = DRILL_LIFE;
//                }
//            } else {
//                drillTime = DRILL_LIFE;
//                inv[9] = null;
//            }
//        }
//        boolean[] tickPer = new boolean[4];
//        for (int i = 0; i < 4; i++) {
//            boolean flag1 = false;
//
//            int n = this.getNumberConsecutiveOperations(i);
//            for (int k = 0; k < n; k++)
//                flag1 |= this.doOperation(n > 1, i, tickPer);
//
//            if (flag1)
//                this.markDirty();
//        }
//        if (ReikaArrayHelper.isAllTrue(tickPer))
//            RotaryAdvancements.INSANITY.triggerAchievement(this.getPlacer());
//    }
//
//    private boolean doOperation(boolean multiple, int i, boolean[] tickPer) {
//        if (this.canProcess(i)) {
//            extractorCookTime[i]++;
//            int time = this.getOperationTime(i);
//            if (time <= 1)
//                tickPer[i] = true;
//            if (extractorCookTime[i] >= time) {
//                extractorCookTime[i] = 0;
//                this.processItem(i);
//            }
//            return true;
//        } else {
//            extractorCookTime[i] = 0;
//            return false;
//        }
//    }
//
//    private boolean canProcess(int i) {
//        if (power < machine.getMinPower(i) || omega < machine.getMinSpeed(i) || torque < machine.getMinTorque(i))
//            return false;
//
//        if (i == 0 && !bedrock && drillTime <= 0 && RotaryConfig.COMMON.EXTRACTORMAINTAIN.getState())
//            return false;
//
//        if ((i == 1 || i == 2) && tank.isEmpty())
//            return false;
//
//        if (inv[i] == null)
//            return false;
//        if (inv[i + 4] != null && inv[i + 4].getCount() + 1 >= inv[i + 4].getMaxStackSize())
//            return false;
//        if (inv[8] != null) {
//            if (inv[8].getCount() + 1 > inv[8].getMaxStackSize())
//                return false;
//            if (inv[3] != null) {
//                ExtractorBonus bonus = ExtractorBonus.getBonusForIngredient(inv[3]);
//                if (bonus != null) {
//                    ItemStack out = bonus.getBonusItem();
//                    if (!ReikaItemHelper.matchStacks(out, inv[8]))
//                        return false;
//                }
//            }
//        }
//        OreType ore = this.getOreType(inv[i]);
//        if (ore == null)
//            return false;
//
//        ItemStack itemstack = RecipesExtractor.getRecipes().getExtractionResult(inv[i]);
//        if (itemstack == null) {
//            return false;
//        }
//        if (inv[i + 4] == null)
//            return true;
//        if (!inv[i + 4].isItemEqual(itemstack))
//            return false;
//        if (inv[i + 4].getCount() < this.getInventoryStackLimit() && inv[i + 4].getCount() < inv[i + 4].getMaxStackSize())
//            return true;
//        return inv[i + 4].getCount() < itemstack.getMaxStackSize();
//    }
//
//    private void processItem(int i) {
//        ItemStack itemstack = RecipesExtractor.getRecipes().getExtractionResult(inv[i]);
//        //ReikaJavaLibrary.pConsole("sSmelt :"+(inv[i+4] == null)+"   - "+ReikaItemHelper.matchStacks(inv[i+4], itemstack));
//        //ReikaOreHelper ore = i == 0 ? ReikaOreHelper.getFromVanillaOre(inv[i].getItem()) : this.getVanillaOreByItem(inv[i]);
//        OreType ore = this.getOreType(inv[i]);
//        //ReikaJavaLibrary.pConsole(ore, Dist.DEDICATED_SERVER);
//        int num = this.getSmeltNumber(i, ore, inv[i]);
//        if (inv[i + 4] == null) {
//            inv[i + 4] = itemstack.copy();
//            inv[i + 4].getCount() *= num;
//        } else if (ReikaItemHelper.matchStacks(inv[i + 4], itemstack))
//            inv[i + 4].getCount() += num;
//
//        if (i == 0 && !bedrock && drillTime > 0 && RotaryConfig.COMMON.EXTRACTORMAINTAIN.getState()) {
//            drillTime--;
//        }
//        if (i == 3) {
//            this.bonusItems(inv[i]);
//            RotaryAdvancements.EXTRACTOR.triggerAchievement(this.getPlacer());
//            if (ore.getRarity() == OreRarity.RARE)
//                RotaryAdvancements.RAREEXTRACT.triggerAchievement(this.getPlacer());
//        }
//
//        inv[i].getCount()--;
//        if (i == 1 || i == 2)
//            tank.removeLiquid(125);
//
//        if (inv[i].getCount() <= 0)
//            inv[i] = null;
//
//    }
//
//    private OreType getOreType(ItemStack is) {
//        if (is.getItem() == RotaryItems.EXTRACTS.get()) {
//            return ReikaOreHelper.oreList[is.getItemDamage() % ReikaOreHelper.oreList.length];
//        } else if (is.getItem() == RotaryItems.MODEXTRACTS.get()) {
//            return ExtractorModOres.getOreFromExtract(is);
//        } else if (is.getItem() == RotaryItems.CUSTOMEXTRACT.get()) {
//            return ItemCustomModOre.getExtractType(is);
//        } else {
//            OreType ore = ReikaOreHelper.getFromVanillaOre(is);
//            if (ore != null)
//                return ore;
//            ore = ReikaOreHelper.getEntryByOreDict(is);
//            if (ore != null)
//                return ore;
//            ore = ModOreList.getModOreFromOre(is);
//            if (ore != null)
//                return ore;
//            ore = CustomExtractLoader.instance.getEntryFromOreBlock(is);
//            return ore;
//        }
//    }
//
//    /*
//    private ReikaOreHelper getVanillaOreByItem(ItemStack is) {
//        return ReikaOreHelper.oreList[is.getItemDamage()%ReikaOreHelper.oreList.length];
//    }
//     */
//    private void bonusItems(ItemStack is) {
//        ExtractorBonus e = ExtractorBonus.getBonusForIngredient(is);
//        if (e != null && e.doBonus()) {
//            ReikaInventoryHelper.addOrSetStack(e.getBonusItem(), inv, 8);
//        }
//    }
//
//    /*
//    private boolean isValidModOre(ItemStack is) {
//        return ExtractorModOres.isModOreIngredient(is) || ModOreList.isModOre(is);
//    }
//
//    private boolean processModOre(int i) {
//        if (this.isValidModOre(inv[i])) {
//            ModOreList m = ModOreList.getEntryFromDamage(inv[i].getItemDamage()/4);
//            if (ModOreList.isModOre(inv[i]) && i == 0) {
//                m = ModOreList.getModOreFromOre(inv[0]);
//                ItemStack is = ExtractorModOres.getDustProduct(m);
//                if (ReikaInventoryHelper.addOrSetStack(is.getItem(), this.getSmeltNumber(i, m, inv[0]), is.getItemDamage(), inv, i+4)) {
//                    ReikaInventoryHelper.decrStack(i, inv);
//                }
//                return true;
//            }
//            else if (ExtractorModOres.isModOreIngredient(inv[i])) {
//                if (ExtractorModOres.isDust(m, inv[i].getItemDamage()) && i == 1) {
//                    ItemStack is = ExtractorModOres.getSlurryProduct(m);
//                    if (ReikaInventoryHelper.addOrSetStack(is.getItem(), this.getSmeltNumber(i, m, inv[i]), is.getItemDamage(), inv, i+4)) {
//                        ReikaInventoryHelper.decrStack(i, inv);
//                        tank.removeLiquid(1000/8);
//                    }
//                    return true;
//                }
//                if (ExtractorModOres.isSlurry(m, inv[i].getItemDamage()) && i == 2) {
//                    ItemStack is = ExtractorModOres.getSolutionProduct(m);
//                    if (ReikaInventoryHelper.addOrSetStack(is.getItem(), this.getSmeltNumber(i, m, inv[i]), is.getItemDamage(), inv, i+4)) {
//                        ReikaInventoryHelper.decrStack(i, inv);
//                        tank.removeLiquid(1000/8);
//                    }
//                    return true;
//                }
//                if (ExtractorModOres.isSolution(m, inv[i].getItemDamage()) && i == 3) {
//                    ItemStack is = ExtractorModOres.getFlakeProduct(m);
//                    if (ReikaInventoryHelper.addOrSetStack(is.getItem(), this.getSmeltNumber(i, m, inv[i]), is.getItemDamage(), inv, i+4)) {
//                        ReikaInventoryHelper.decrStack(i, inv);
//                        this.bonusItems(inv[i]);
//                        RotaryAchievements.EXTRACTOR.triggerAchievement(this.getPlacer());
//                        if (m.getRarity() == OreRarity.RARE)
//                            RotaryAchievements.RAREEXTRACT.triggerAchievement(this.getPlacer());
//                    }
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//     */
//    @Override
//    public boolean hasModelTransparency() {
//        return true;
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.EXTRACTOR;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        if (slot > 3 && slot < 9)
//            return false;
//        if (slot == 0)
//            return ReikaBlockHelper.isOre(is) || CustomExtractLoader.instance.getEntryFromOreBlock(is) != null;
//        if (RotaryItems.EXTRACTS.matchItem(is)) {
//            return slot == 1 + is.getItemDamage() / 8;
//        } else if (RotaryItems.MODEXTRACTS.matchItem(is)) {
//            return slot == 1 + is.getItemDamage() % 4;
//        } else if (RotaryItems.CUSTOMEXTRACT.matchItem(is)) {
//            return slot == 1 + is.getItemDamage() % 4;
//        }
//        if (slot == 9)
//            return !bedrock && RotaryConfig.COMMON.EXTRACTORMAINTAIN.get() && ReikaItemHelper.matchStacks(is, RotaryItems.drill);
//        return false;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        int bits = 0b1111;
//        for (int i = 0; i < 4; i++) {
//            if (this.canProcess(i))
//                bits &= ~(1 << i);
//        }
//        return bits;
//    }
//
//    @Override
//    public Fluid getInputFluid() {
//        return Fluids.WATER;
//    }
//
//    @Override
//    public int getCapacity() {
//        return CAPACITY;
//    }
//
//    @Override
//    public boolean canReceiveFrom(Direction dir) {
//        return dir.getStepY() == 0;
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m.isStandardPipe();
//    }
//
//    public int getOperationTime(int stage) {
//        return DurationRegistry.EXTRACTOR.getOperationTime(omega, stage);
//    }
//
//    public int getNumberConsecutiveOperations(int stage) {
//        return DurationRegistry.EXTRACTOR.getNumberOperations(omega, stage);
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return !tank.isEmpty() && !ReikaInventoryHelper.isEmpty(inv, 0, 4);
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return tank.isEmpty() ? "No Water" : this.areConditionsMet() ? "Operational" : "No Items";
//    }
//
//    @Override
//    public boolean isSlotHidden(int slot) {
//        return slot == 9;
//    }
//
//    @Override
//    public int[] getHiddenSlots() {
//        return new int[]{9};
//    }
//}
