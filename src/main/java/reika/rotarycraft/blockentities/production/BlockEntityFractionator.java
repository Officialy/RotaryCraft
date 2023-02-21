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
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import reika.dragonapi.instantiable.Interpolation;
//import reika.dragonapi.instantiable.data.KeyedItemStack;
//import reika.dragonapi.instantiable.data.WeightedRandom;
//import reika.dragonapi.instantiable.math.MovingAverage;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.MultiOperational;
//import reika.rotarycraft.auxiliary.interfaces.PressureTE;
//import reika.rotarycraft.registry.DifficultyEffects;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//
//public class BlockEntityFractionator extends InventoriedPoweredLiquidIO implements MultiOperational, ConditionalOperation, PressureTE {
//
//    public static final int CAPACITY = 240000;
//    public static final int MINTIME = 10;
//    private static final HashMap<KeyedItemStack, Float> ingredients = new HashMap<>();
//    private static final Interpolation yield = new Interpolation(false);
//    public static int MAXPRESSURE = 1000; //10atm
//
//    static {
//        ingredients.put(key(new ItemStack(Items.BLAZE_POWDER)), 1.5F);
//        ingredients.put(key(RotaryItems.COALDUST), 1F);
//        ingredients.put(key(new ItemStack(Items.MAGMA_CREAM)), 0.75F);
//        ingredients.put(key(new ItemStack(Items.PINK_DYE)), 0.5F);
//        ingredients.put(key(RotaryItems.NETHERRACKDUST), 2F);
//        ingredients.put(key(RotaryItems.TAR), 1.5F);
//
//        yield.addPoint(0, 0.01);
//        yield.addPoint(100, 0.05); //~ atm
//        yield.addPoint(180, 0.1); // min torque
//        yield.addPoint(500, 0.4); // microturbine is just a bit more than this
//        yield.addPoint(720, 1); //~4MW if minimal-speed gearing, the breakeven point
//        yield.addPoint(850, 1.5); //~6MW
//        yield.addPoint(1000, 2.5); //max
//    }
//
//    public int mixTime;
//    public boolean idle = false;
//    private int pressure;
//    private MovingAverage torqueInput = new MovingAverage(20);
//
//    private static KeyedItemStack key(Item i) {
//        return key(new ItemStack(i));
//    }
//
//    private static KeyedItemStack key(ItemStack is) {
//        return new KeyedItemStack(is).setSimpleHash(true).setIgnoreMetadata(!is.getHasSubtypes());
//    }
//
//	/*
//	private static final ItemStack[] ingredients =
//		{
//		new ItemStack(Items.blaze_powder), new ItemStack(Items.coal),
//		RotaryItems.netherrackdust, RotaryItems.tar,
//		RotaryItems.ETHANOL.get(), new ItemStack(Items.magma_cream)
//		};
//	 */
//
//    public static boolean isJetFuelIngredient(ItemStack is) {
//        return ingredients.containsKey(key(is));
//    }
//
//    public static Collection<KeyedItemStack> getIngredients() {
//        return Collections.unmodifiableCollection(ingredients.keySet());
//    }
//
//    public void testIdle() {
//        idle = !this.hasAllIngredients();
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return false;
//    }
//
//    public int getFuelScaled(int par1) {
//        return (output.getLevel() * par1) / this.getOutputCapacity();
//    }
//
//    public int getEthanolScaled(int par1) {
//        return (input.getLevel() * par1) / this.getInputCapacity();
//    }
//
//    public int getPressureScaled(int par1) {
//        return (pressure * par1) / MAXPRESSURE;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//
//        this.getPowerBelow();
//        power = (long) omega * (long) torque;
//
//        torqueInput.addValue(torque);
//        if (!world.isClientSide && this.tickcount % 20 == 0)
//            this.updatePressure(world, pos);
//        this.testIdle();
//        if (power < MINPOWER || omega < MINSPEED) {
//            mixTime = 0;
//            return;
//        }
//        //int operationtime = ReikaMathLibrary.extrema(BASETIME-this.omega, MINTIME, "max");
//
//        int n = this.getNumberConsecutiveOperations();
//        for (int i = 0; i < n; i++)
//            this.doOperation(n > 1);
//    }
//
//    public void updatePressure(Level world, BlockPos pos) {
//        int local = pressure;
//        double Pamb = ReikaWorldHelper.getAmbientPressureAt(world, pos, false);
//        if (Pamb > 101.3) {
//            Pamb = 101.3 + 0.02 * (Pamb - 101.3);
//        }
//        int dp = local - (int) Pamb;
//        int sub = (int) (Math.signum(dp) * Math.max(1, Math.abs(dp / 16)));
//
//        int avg = (int) torqueInput.getAverage();
//
//        if (avg <= 0)
//            sub *= 8;//4;
//
//        local -= sub;
//
//        if (avg > 0)
//            local += 1.8 * Math.sqrt(avg);
//
//        if (local > MAXPRESSURE) {
//            this.overpressure(world, pos);
//            local = MAXPRESSURE;
//        }
//
//        //Gain pressure slowly, but lose it quickly
//        if (pressure < local) { //ascending
//            pressure += Math.max(1, Math.min(ReikaRandomHelper.getRandomPlusMinus(6, 13), (local - pressure) / 4));
//        } else {
//            pressure = local;
//        }
//    }
//
//    private void doOperation(boolean multiple) {
//        if (this.process()) {
//            mixTime++;
//            //ReikaChatHelper.writeInt(this.operationTime(this.omega, 0));
//            if (multiple || mixTime >= this.getOperationTime()) {
//                mixTime = 0;
//                this.make();
//            }
//        } else {
//            mixTime = 0;
//        }
//    }
//
//    private void make() {
//        RotaryAdvancements.JETFUEL.triggerAchievement(this.getPlacer());
//        if (!level.isClientSide) {
//            float consume = ingredients.size() * DifficultyEffects.CONSUMEFRAC.getChance();
//            WeightedRandom<Integer> wr = new WeightedRandom();
//            for (int i = 0; i < ingredients.size(); i++) {
//                ItemStack is = itemHandler.getStackInSlot(i);
//                KeyedItemStack ks = key(is);
//                float wt = ingredients.get(ks);
//                wr.addEntry(i, wt);
//            }
//            while (consume > 0) {
//                int slot = wr.getRandomEntry();
//                if (consume >= 1 || ReikaRandomHelper.doWithChance(consume))
//                    ReikaInventoryHelper.decrStack(slot, inv);
//                wr.remove(slot);
//                consume -= 1;
//            }
//        }
//        float amt = 1000 * DifficultyEffects.CONSUMEFRAC.getChance();
//        input.removeLiquid((int) amt);
//        output.addLiquid((int) (this.getYieldRatio() * DifficultyEffects.PRODUCEFRAC.getInt()), RotaryFluids.JET_FUEL);
//    }
//
//    public float getYieldRatio() {
//        return (float) yield.getValue(pressure);
//    }
//
//    private boolean process() {
//        //ReikaJavaLibrary.pConsole(tank.getLevel()+":"+(DifficultyEffects.PRODUCEFRAC.getMaxAmount()*1000)+":"+CAPACITY);
//        if (output.getLevel() + (DifficultyEffects.PRODUCEFRAC.getMaxAmount()) >= CAPACITY)
//            return false;
//        if (inv[ingredients.size()] == null)
//            return false;
//        if (inv[ingredients.size()].getItem() != Items.GHAST_TEAR) //need a ghast tear as fuel solvent
//            return false;
//        return this.hasAllIngredients();
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.valueOf(allitems));
//    }
//
//    private boolean hasAllIngredients() {
//        HashSet<KeyedItemStack> check = new HashSet(ingredients.keySet());
//        for (int i = 0; i < ingredients.size(); i++) {
//            if (itemHandler.getStackInSlot(i) == null)
//                return false;
//            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d  %d", ingredients[i.getItem, ingredients[i].getItemDamage()));
//            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", i)+String.valueOf(this.haveIngredient(ingredients[i.getItem, ingredients[i].getItemDamage())));
//            KeyedItemStack ks = key(itemHandler.getStackInSlot(i));
//            if (!check.contains(ks))
//                return false;
//            check.remove(ks);
//        }
//        return input.getLevel() >= 1000 * DifficultyEffects.CONSUMEFRAC.getChance();
//    }
//
//    public int getContainerSize() {
//        return ingredients.size() + 1 + 1;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        NBT.putInt("mix", mixTime);
//
//        NBT.putInt("press", pressure);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        mixTime = NBT.getInt("mix");
//
//        pressure = NBT.getInt("press");
//    }
//
//    @Override
//    public void saveAdditional(CompoundTag NBT) {
//        super.saveAdditional(NBT);
//
//        CompoundTag tag = new CompoundTag();
//        torqueInput.saveAdditional(tag);
//        NBT.put("torquevals", tag);
//    }
//
//    @Override
//    public void load(CompoundTag NBT) {
//        super.load(NBT);
//
//        if (NBT.contains("torquevals"))
//            torqueInput = MovingAverage.load(NBT.getCompound("torquevals"));
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
//        return MachineRegistry.FRACTIONATOR;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        if (slot == ingredients.size() + 1)
//            return is.getItem() == Items.BUCKET;
//        if (slot == ingredients.size())
//            return is.getItem() == Items.GHAST_TEAR;
//        if (!isJetFuelIngredient(is))
//            return false;
//        HashSet<Integer> slots = ReikaInventoryHelper.getSlotsBetweenWithItemStack(is, this, 0, ingredients.size() - 1, false);
//        return slots.isEmpty() || slots.contains(slot);
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return (int) (this.getYieldRatio() / yield.getFinalValue() * 15);
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m == MachineRegistry.FUELLINE || m == MachineRegistry.BEDPIPE;
//    }
//
//    public int getFuelLevel() {
//        return output.getLevel();
//    }
//
//    @Override
//    public boolean canOutputTo(Direction to) {
//        return to == Direction.UP;
//    }
//
//    @Override
//    public int getInputCapacity() {
//        return 16000;
//    }
//
//    @Override
//    public int getCapacity() {
//        return CAPACITY;
//    }
//
//    @Override
//    public int getOperationTime() {
//        return DurationRegistry.FRACTIONATOR.getOperationTime(omega);
//    }
//
//    @Override
//    public int getNumberConsecutiveOperations() {
//        return DurationRegistry.FRACTIONATOR.getNumberOperations(omega);
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        int nslots = ReikaInventoryHelper.countEmptySlots(inv);
//        return nslots == 0 || (nslots == 1 && inv[7] == null);
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "Missing Ingredients";
//    }
//
//    @Override
//    public int getPressure() {
//        return pressure;
//    }
//
//    @Override
//    public int getMaxPressure() {
//        return MAXPRESSURE;
//    }
//
//    @Override
//    public void addPressure(int press) {
//        pressure += press;
//    }
//
//    @Override
//    public void overpressure(Level world, BlockPos pos) {
//
//    }
//
//    @Override
//    public Fluid getInputFluid() {
//        return RotaryCraft.ethanolFluid;
//    }
//
//    @Override
//    public boolean canReceiveFrom(Direction from) {
//        return from.getStepY() == 0;
//    }
//
//    @Override
//    public boolean canIntakeFromPipe(MachineRegistry p) {
//        return this.canOutputToPipe(p);
//    }
//
//    @Override
//    public boolean canOutputToPipe(MachineRegistry p) {
//        return p == MachineRegistry.FUELLINE || p == MachineRegistry.BEDPIPE;
//    }
//}
