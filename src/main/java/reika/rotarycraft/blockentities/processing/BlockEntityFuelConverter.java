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
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.common.util.EnumHelper;
//import reika.dragonapi.instantiable.ItemMatch;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.auxiliary.RotaryItems;
//import reika.rotarycraft.registry.DifficultyEffects;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//
//
//public class BlockEntityFuelConverter extends InventoriedPoweredLiquidIO {
//
//    public static final int CAPACITY = 5 * FluidContainerRegistry.BUCKET_VOLUME;
//
//    public static Conversions getConversionByItem(ItemStack is) {
//        for (int i = 0; i < Conversions.list.length; i++) {
//            Conversions c = Conversions.list[i];
//            if (c.isValidItem(is)) {
//                return c;
//            }
//        }
//        return null;
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
//        return MachineRegistry.FUELENHANCER;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return true;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        if (input.isEmpty())
//            return 15;
//        if (output.isFull())
//            return 15;
//        return 0;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        tickcount++;
//        this.getPowerBelow();
//
//        //ReikaJavaLibrary.pConsole(input+":"+output);
//
//        //ReikaJavaLibrary.pConsoleOnlyIn("BC: "+this.getBCFuel()+"    JET: "+this.getJetFuel(), Dist.CLIENT);
//
//        if (power < MINPOWER)
//            return;
//        if (omega < MINSPEED)
//            return;
//        if (world.isClientSide)
//            return;
//
//        Conversions c = this.getConversion();
//        if (c != null && this.getInputLevel() >= c.fluidRatio * c.speedFactor && this.hasItems(c) && output.canTakeIn(c.speedFactor)) {
//            input.removeLiquid(c.fluidRatio * c.speedFactor);
//            output.addLiquid(c.speedFactor, c.output);
//            this.consumeItems(c);
//        }
//    }
//
//    private Conversions getConversion() {
//        return !input.isEmpty() ? Conversions.conversionMap.get(input.getActualFluid().getName()) : null;
//    }
//
//    private boolean hasItems(Conversions c) {
//        for (int i = 0; i < c.ingredients.length; i++) {
//            if (!ReikaInventoryHelper.checkForItemStack(c.ingredients[i], inv)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private void consumeItems(Conversions c) {
//        for (int i = 0; i < c.ingredients.length; i++) {
//            if (ReikaRandomHelper.doWithChance(c.itemConsumptionChance))
//                ReikaInventoryHelper.decrStack(ReikaInventoryHelper.locateInInventory(c.ingredients[i], inv), inv);
//        }
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 9;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return getConversionByItem(is) != null;
//    }
//
//    public double getLiquidModelOffset(boolean in) {
//        return in ? 10 / 16D : 1 / 16D;
//    }
//
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return false;
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m == MachineRegistry.FUELLINE || m.isStandardPipe();
//    }
//
//    public Fluid getInputFluidType() {
//        return input.getActualFluid();
//    }
//
//    public Fluid getOutputFluidType() {
//        return output.getActualFluid();
//    }
//
//    @Override
//    public Fluid getInputFluid() {
//        return null;
//    }
//
//    @Override
//    public boolean isValidFluid(Fluid f) {
//        return Conversions.conversionMap.get(f.getName()) != null;
//    }
//
//    @Override
//    public boolean canOutputTo(Direction to) {
//        return to.getStepY() == 0;
//    }
//
//    @Override
//    public boolean canReceiveFrom(Direction from) {
//        return from == Direction.UP;
//    }
//
//    @Override
//    public int getCapacity() {
//        return CAPACITY;
//    }
//
//    @Override
//    public boolean canIntakeFromPipe(MachineRegistry p) {
//        return p.isStandardPipe();
//    }
//
//    @Override
//    public boolean canOutputToPipe(MachineRegistry p) {
//        return p == MachineRegistry.FUELLINE;
//    }
//
//    public enum Conversions {
//        BCFUEL("fuel", "rc jet fuel", 2, 4, DifficultyEffects.CONSUMEFRAC.getChance() / 32D / 100D * 8, new ItemMatch(Items.blaze_powder), new ItemMatch(RotaryItems.netherrackdust), new ItemMatch(RotaryItems.tar), new ItemMatch(Items.magma_cream)),
//        KEROSENE("kerosene", "rc jet fuel", 2, 4, DifficultyEffects.CONSUMEFRAC.getChance() / 32D / 100D * 8, new ItemMatch(Items.blaze_powder), new ItemMatch(RotaryItems.netherrackdust), new ItemMatch(RotaryItems.tar), new ItemMatch(Items.magma_cream)),
//        ;
//
//        private static final HashMap<String, Conversions> conversionMap = new HashMap();
//        private static final HashMap<String, Conversions> conversionOutputMap = new HashMap();
//        public static Conversions[] list = values();
//
//        static {
//            for (int i = 0; i < list.length; i++) {
//                Conversions c = list[i];
//                if (c.input != null && c.output != null) {
//                    conversionMap.put(c.input.getName(), c);
//                    conversionOutputMap.put(c.output.getName(), c);
//                }
//            }
//        }
//
//        public final Fluid input;
//        public final Fluid output;
//        public final int speedFactor;
//        public final int fluidRatio;
//        public final double itemConsumptionChance;
//        private final ItemMatch[] ingredients;
//
//        Conversions(String in, String out, int sp, int r, double f, ItemMatch... items) {
//            input = Fluids.getFluid(in);
//            output = Fluids.getFluid(out);
//
//            speedFactor = sp;
//            fluidRatio = r;
//
//            itemConsumptionChance = f;
//
//            ingredients = items;
//        }
//
//        public static void addRecipe(String name, String in, String out, int speed, int fluidRatio, double itemConsumeChance, ItemMatch... items) {
//            Conversions c = EnumHelper.addEnum(Conversions.class, name.toUpperCase(), new Class[]{String.class, String.class, int.class, int.class, double.class, ItemMatch[].class}, new Object[]{in, out, speed, fluidRatio, itemConsumeChance, items});
//            conversionMap.put(in, c);
//            conversionOutputMap.put(out, c);
//            list = values();
//        }
//
//        public static Collection<Conversions> getByInput(ItemStack is) {
//            Collection<Conversions> li = new ArrayList<>();
//            for (Conversions c : conversionMap.values()) {
//                if (c.isValidItem(is))
//                    li.add(c);
//            }
//            return li;
//        }
//
//        public static Collection<Conversions> getByInput(Fluid f) {
//            Collection<Conversions> li = new ArrayList<>();
//            for (Conversions c : conversionMap.values()) {
//                if (c.input == f)
//                    li.add(c);
//            }
//            return li;
//        }
//
//        public static Collection<Conversions> getByOutput(Fluid f) {
//            Collection<Conversions> li = new ArrayList<>();
//            for (Conversions c : conversionMap.values()) {
//                if (c.output == f)
//                    li.add(c);
//            }
//            return li;
//        }
//
//        public boolean isValid() {
//            return input != null && output != null;
//        }
//
//        public boolean isValidItem(ItemStack is) {
//            for (int i = 0; i < ingredients.length; i++) {
//                if (ingredients[i].match(is))
//                    return true;
//            }
//            return false;
//        }
//
//
//        public Collection<ItemStack> getIngredientsForDisplay() {
//            Collection<ItemStack> c = new ArrayList<>();
//            for (ItemMatch m : ingredients) {
//                c.add(m.getCycledItem());
//            }
//            return c;
//        }
//    }
//
//}
