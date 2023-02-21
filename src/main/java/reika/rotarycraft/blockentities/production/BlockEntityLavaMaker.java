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
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.fluids.capability.IFluidHandler;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.instantiable.StepTimer;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.auxiliary.RotaryAux;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
//import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.SoundRegistry;
//
//public class BlockEntityLavaMaker extends InventoriedPowerLiquidProducer implements IFluidHandler, PipeConnector, TemperatureTE, ConditionalOperation {
//
//    public static final int CAPACITY = 64000;
//
//    public static final int MAXTEMP = 1800;
//    private final StepTimer timer = new StepTimer(20);
//    private int temperature;
//    private long energy;
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getPowerBelow();
//
//        timer.update();
//        if (timer.checkCap())
//            this.updateTemperature(world, pos);
//        if (temperature > this.getMeltingTemperature())
//            energy += power;
//        else
//            energy *= 0.85;
//
//        //ReikaJavaLibrary.pConsole(this.getMeltingTemperature()+":"+energy/20F/MELT_ENERGY, Dist.DEDICATED_SERVER);
//
//        tickcount++;
//        if (omega > 0 && power > 0) {
//            if (tickcount > 98) {
//                SoundRegistry.FRICTION.playSoundAtBlock(world, pos, RotaryAux.isMuffled(this) ? 0.1F : 0.5F, 0.5F);
//                tickcount = 0;
//            }
//            world.addParticle("crit", x + DragonAPI.rand.nextDouble(), y, z + DragonAPI.rand.nextDouble(), -0.2 + 0.4 * DragonAPI.rand.nextDouble(), 0.4 * DragonAPI.rand.nextDouble(), -0.2 + 0.4 * DragonAPI.rand.nextDouble());
//        }
//
//
//        for (int i = 0; i < itemHandler.getSlots(); i++) {
//            ItemStack is = itemHandler.getStackInSlot(i);
//            if (is != null) {
//                FluidStack fs = RecipesLavaMaker.getRecipes().getMelting(is);
//                long melt_energy = RecipesLavaMaker.getRecipes().getMeltingEnergy(is);
//                //ReikaJavaLibrary.pConsole(energy/20L+":"+melt_energy, Dist.DEDICATED_SERVER);
//                if (fs != null) {
//                    if (this.canMake(fs) && energy >= melt_energy * 20) {
//                        tank.addLiquid(fs.amount, fs.getFluid());
//                        ReikaInventoryHelper.decrStack(i, inv);
//                        energy -= melt_energy * 20;
//                        return;
//                    }
//                }
//            }
//        }
//    }
//
//    private int getMeltingTemperature() {
//        for (int i = 0; i < itemHandler.getSlots(); i++) {
//            ItemStack is = itemHandler.getStackInSlot(i);
//            if (is != null) {
//                int temp = RecipesLavaMaker.getRecipes().getMeltTemperature(is);
//                if (temp > Integer.MIN_VALUE) {
//                    return temp;
//                }
//            }
//        }
//        return Integer.MAX_VALUE;
//    }
//
//    private boolean canMake(FluidStack liq) {
//        if (level.isClientSide)
//            return false;
//        if (tank.isEmpty())
//            return true;
//        if (!tank.getActualFluid().equals(liq.getFluid()))
//            return false;
//        return tank.getLevel() + liq.amount <= tank.getCapacity();
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return false;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 9;
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m.isStandardPipe() || m == MachineRegistry.FUELLINE;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return RecipesLavaMaker.getRecipes().isValidFuel(is);
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
//        return MachineRegistry.LAVAMAKER;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        if (tank.isFull())
//            return 15;
//        if (!this.canMake())
//            return 15;
//        return 0;
//    }
//
//    private boolean canMake() {
//        for (int i = 0; i < itemHandler.getSlots(); i++) {
//            ItemStack is = itemHandler.getStackInSlot(i);
//            if (is != null) {
//                FluidStack fs = RecipesLavaMaker.getRecipes().getMelting(is);
//                if (fs != null)
//                    if (this.canMake(fs))
//                        return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        tank.load(NBT);
//
//        energy = NBT.getLong("e");
//        temperature = NBT.getInt("temp");
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        tank.saveAdditional(NBT);
//
//        NBT.putLong("e", energy);
//        NBT.putInt("temp", temperature);
//    }
//
//    public boolean isEmpty() {
//        return tank.isEmpty();
//    }
//
//    public boolean hasStone() {
//        return !ReikaInventoryHelper.isEmpty(inv);
//    }
//
//    public void setEmpty() {
//        tank.empty();
//    }
//
//    public void removeLava(int amt) {
//        tank.removeLiquid(amt);
//    }
//
//    @Override
//    public boolean canOutputTo(Direction to) {
//        return to.getStepY() == 0;
//    }
//
//    @Override
//    public int getCapacity() {
//        return CAPACITY;
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return !ReikaInventoryHelper.isEmpty(inv);
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "No Items";
//    }
//
//    @Override
//    public void updateTemperature(Level world, BlockPos pos) {
//        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
//        if (power > 0) {
//            temperature += ReikaMathLibrary.logbase(power, 2);
//        }
//        if (temperature > Tamb) {
//            temperature -= (temperature - Tamb) / 64;
//        } else {
//            temperature += (temperature - Tamb) / 64;
//        }
//        if (temperature - Tamb <= 64 && temperature > Tamb)
//            temperature--;
//        if (temperature > MAXTEMP) {
//            temperature = MAXTEMP;
//            this.overheat(world, pos);
//        }
//        if (temperature > 50) {
//            Direction side = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.SNOW);
//            if (side != null)
//                ReikaWorldHelper.changeAdjBlock(world, pos, side, Blocks.AIR, 0);
//            side = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.ICE);
//            if (side != null)
//                ReikaWorldHelper.changeAdjBlock(world, pos, side, Blocks.flowing_water, 0);
//        }
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
//        return false;
//    }
//
//    @Override
//    public int getMaxTemperature() {
//        return MAXTEMP;
//    }
//
//}
