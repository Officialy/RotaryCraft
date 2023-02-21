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
//import cpw.mods.fml.common.registry.GameRegistry;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.registries.ForgeRegistries;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.ModList;
//import reika.dragonapi.instantiable.StepTimer;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.auxiliary.RotaryAux;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.MultiOperational;
//import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
//import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidReceiver;
//import reika.rotarycraft.registry.DurationRegistry;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//import reika.rotarycraft.registry.SoundRegistry;
//
//public class BlockEntityCrystallizer extends InventoriedPowerLiquidReceiver implements TemperatureTE, MultiOperational, ConditionalOperation {
//
//    private final StepTimer timer = new StepTimer(400);
//    private final StepTimer tempTimer = new StepTimer(20);
//    private final StepTimer sound = new StepTimer(45);
//    public int freezeTick;
//    private int temperature;
//
//    public static int getFreezingPoint(FluidStack fs) {
//        return -273 + (int) (0.9 * fs.getFluid().getTemperature(fs));
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return i == 0;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getIOSidesDefault(world, pos);
//        this.getPower(false);
//
//        //ReikaJavaLibrary.pConsole((omega-MINSPEED)+":"+dur);
//        timer.setCap(this.getOperationTime());
//
//        tempTimer.update();
//        if (tempTimer.checkCap())
//            this.updateTemperature(world, pos);
//
//        if (!level.isClientSide) {
//            int n = this.getNumberConsecutiveOperations();
//            for (int i = 0; i < n; i++)
//                this.doOperation(n > 1);
//
//            freezeTick = timer.getTick();
//        }
//
//        sound.update();
//        if (omega > 0) {
//            if (sound.checkCap())
//                SoundRegistry.FAN.playSoundAtBlock(world, pos, RotaryAux.isMuffled(this) ? 0.1F : 0.4F, 0.6F);
//        }
//    }
//
//    private void doOperation(boolean multiple) {
//        if (!tank.isEmpty()) {
//            ItemStack toMake = RecipesCrystallizer.getRecipes().getFreezingResult(tank.getFluid());
//            //ReikaJavaLibrary.pConsole(timer.getTick()+"/"+timer.getCap()+":"+toMake);
//            if (this.canOperate(toMake)) {
//                timer.update();
//                if (multiple || timer.checkCap()) {
//                    this.make(toMake);
//                }
//            } else
//                timer.reset();
//        } else
//            timer.reset();
//    }
//
//    private void make(ItemStack toMake) {
//        ReikaInventoryHelper.addOrSetStack(toMake, inv, 0);
//        int amt = RecipesCrystallizer.getRecipes().getRecipeConsumption(toMake);
//        tank.removeLiquid(amt);
//    }
//
//    private boolean canOperate(ItemStack toMake) {
//        if (toMake == null)
//            return false;
//        if (power < MINPOWER || omega < MINSPEED)
//            return false;
//        if (temperature > this.getFreezingPoint())
//            return false;
//        if (itemHandler.getStackInSlot(0).isEmpty())
//            return true;
//        return ReikaItemHelper.areStacksCombinable(toMake, itemHandler.getStackInSlot(0), this.getInventoryStackLimit());
//    }
//
//    public int getFreezingPoint() {
//        return !tank.isEmpty() ? getFreezingPoint(tank.getFluid()) : 0;
//    }
//
//    public int getProgressScaled(int s) {
//        return s * freezeTick / timer.getCap();
//    }
//
//    public int getLiquidScaled(int s) {
//        return s * tank.getLevel() / tank.getCapacity();
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 2;
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return true;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return slot == 1 && ReikaItemHelper.matchStacks(is, RotaryItems.dryice);
//    }
//
//    @Override
//    public Fluid getInputFluid() {
//        return null;
//    }
//
//    @Override
//    public boolean isValidFluid(Fluid f) {
//        return RecipesCrystallizer.getRecipes().isValidFluid(f);
//    }
//
//    @Override
//    public boolean canReceiveFrom(Direction from) {
//        return true;
//    }
//
//    @Override
//    public int getCapacity() {
//        return 8000;
//    }
//
//    @Override
//    protected void animateWithTick(Level world, BlockPos pos) {
//        if (!this.isInWorld()) {
//            phi = 0;
//            return;
//        }
//        if (power < MINPOWER || omega < MINSPEED)
//            return;
//        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.CRYSTALLIZER;
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
//    public void updateTemperature(Level world, BlockPos pos) {
//        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
//        if (ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.SNOW) != null)
//            Tamb -= 5;
//        if (RotaryAux.isNextToWater(world, pos))
//            Tamb -= 15;
//        if (RotaryAux.isNextToIce(world, pos))
//            Tamb -= 30;
//
//        ItemStack cryo = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ModList.THERMALFOUNDATION.getModid(), "dustCryotheum")).getDefaultInstance();//GameRegistry.findItemStack(ModList.THERMALFOUNDATION.modid, "dustCryotheum", 1);
//        if (ReikaItemHelper.matchStacks(RotaryItems.DRY_ICE.get().getDefaultInstance(), itemHandler.getStackInSlot(1)) || (cryo != null && ReikaItemHelper.matchStacks(cryo, itemHandler.getStackInSlot(1)))) {
//            Tamb -= 40;
//            if (temperature > Tamb + 4 || DragonAPI.rand.nextInt(20) == 0)
//                ReikaInventoryHelper.decrStack(1, inv);
//        }
//
//        int dT = Tamb - temperature;
//
//        temperature += Math.abs(dT) < 4 ? Math.signum(dT) : dT / 4;
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
//    public int getThermalDamage() {
//        return 0;
//    }
//
//    @Override
//    public void overheat(Level world, BlockPos pos) {
//
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        temperature = NBT.getInt("temp");
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        NBT.putInt("temp", temperature);
//    }
//
//    @Override
//    public int getOperationTime() {
//        return DurationRegistry.CRYSTALLIZER.getOperationTime(Math.max(0, omega - MINSPEED));
//    }
//
//    @Override
//    public int getNumberConsecutiveOperations() {
//        return DurationRegistry.CRYSTALLIZER.getNumberOperations(Math.max(0, omega - MINSPEED));
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return !tank.isEmpty();
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "No Liquid";
//    }
//
//    @Override
//    public boolean canBeCooledWithFins() {
//        return true;
//    }
//
//    @Override
//    public boolean allowHeatExtraction() {
//        return false;
//    }
//
//    @Override
//    public boolean allowExternalHeating() {
//        return true;
//    }
//
//    @Override
//    public int getMaxTemperature() {
//        return 1000;
//    }
//
//}
