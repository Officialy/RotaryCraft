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
//import net.minecraft.core.Direction;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.level.material.Fluids;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.fluids.capability.IFluidHandler;
//
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.instantiable.HybridTank;
//import reika.dragonapi.instantiable.TemperatureEffect.TemperatureCallback;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
//import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
//import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
//import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//import reika.rotarycraft.registry.*;
//
//public class BlockEntityPulseFurnace extends InventoriedPowerReceiver implements TemperatureTE, PipeConnector, IFluidHandler, DiscreteFunction, ConditionalOperation, TemperatureCallback {
//
//    public static final int CAPACITY = 3000;
//    public static final int MAXFUEL = 8000;
//    public static final int MAXTEMP = 1000; //1370C = melting steel, 800C = 90% strength loss
//    public static final int SMELTTICKS_BASE = 100;
//    private final HybridTank fuel = new HybridTank("fuel", MAXFUEL);
//    private final HybridTank water = new HybridTank("water", CAPACITY);
//    private final HybridTank accel = new HybridTank("accel", MAXFUEL);
//    public int pulseFurnaceCookTime;
//    public boolean idle = false;
//    public int temperature;    //damage player, start fires, etc
//    public int smelttick = 0;
//    private int tickcount2 = 0;
//    private int soundtick = 2000;
//
//    public BlockEntityPulseFurnace(BlockPos pos, BlockState state) {
//        super(RotaryBlockEntities.PULSE_JET_FURNACE.get(), pos, state);
//    }
//
//
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return i == 2;
//    }
//
//    public void testIdle() {
//        idle = (this.getRecipe() == null && omega > MINSPEED);
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 3;
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        pulseFurnaceCookTime = NBT.getShort("CookTime");
//
//        water.readFromNBT(NBT);
//        fuel.readFromNBT(NBT);
//        accel.readFromNBT(NBT);
//
//        temperature = NBT.getInt("temp");
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putShort("CookTime", (short) pulseFurnaceCookTime);
//
//        water.writeToNBT(NBT);
//        fuel.writeToNBT(NBT);
//        accel.writeToNBT(NBT);
//
//        NBT.putInt("temp", temperature);
//    }
//
//    /**
//     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
//     * cooked
//     */
//    public int getCookProgressScaled(int par1) {
//        return Math.min(par1, (pulseFurnaceCookTime * par1) / this.getOperationTime());
//    }
//
//    public int getFuelScaled(int par1) {
//        return (fuel.getLevel() * par1) / MAXFUEL;
//    }
//
//    public int getTempScaled(int par1) {
//        return (temperature * par1) / MAXTEMP;
//    }
//
//    public int getWaterScaled(int par1) {
//        return (water.getLevel() * par1) / CAPACITY;
//    }
//
//    public int getFireScaled(int par1) {
//        return Math.min(par1, (smelttick * par1) / this.getSmeltingDuration());
//    }
//
//    public int getAccelerantScaled(int a) {
//        return accel.getLevel() * a / accel.getCapacity();
//    }
//
//    private void getFuel(Level world, BlockPos pos) {
//        if (tickcount2 >= 100) {
//            fuel.removeLiquid(100);
//            tickcount2 = 0;
//        }
//    }
//
//    private void heatAmbient(Level world, BlockPos pos) {
//        if (fuel.getLevel() > 0 && this.canHeatUp())
//            temperature += Math.max((MAXTEMP - temperature) / 8, 4);
//
//        int dT = 2;
//        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
//
//        if (Tamb < -40) {
//            dT = 8;
//        } else if (Tamb < -5) {
//            dT = 6;
//        } else if (Tamb < 5) {
//            dT = 4;
//        }
//
//        if (Tamb >= 300 && this.canHeatUp()) {
//            dT = -1;
//        } else if (Tamb >= temperature) {
//            dT = 0;
//        } else if (Tamb > 30) {
//            dT = 1;
//        }
//
//        if (water.getLevel() > 0) {
//            if (DragonAPI.rand.nextInt(3) == 0) {
//                int rem = (temperature * 2 / MAXTEMP) * 50;
//                if (Tamb >= 180)
//                    rem *= 2;
//                if (Tamb >= 90)
//                    rem *= 2;
//                if (rem > 0)
//                    water.removeLiquid(rem);
//            }
//            if (Tamb >= 300)
//                temperature -= temperature / 256;
//            else
//                temperature -= temperature / 64;
//        }
//
//        if (dT != 0) {
//            if (dT > 0)
//                temperature = Math.max(Tamb, temperature - dT);
//            else
//                temperature -= dT;
//        }
//    }
//
//    private boolean canHeatUp() {
//        return power >= MINPOWER && omega >= MINSPEED && !fuel.isEmpty();
//    }
//
//    public void smeltHeat() {
//        //	this.temperature += 10*melttemp/980;	//980 kJ per degree kelvin
//    }
//
//    public void updateTemperature(Level world, BlockPos pos) {
//        this.heatAmbient(world, pos);
//
//        if (temperature > 915) {
//            RotaryCraft.LOGGER.warn("WARNING: " + this + " is reaching very high temperature!");
//            world.addParticle(ParticleTypes.LAVA, pos.getX() + DragonAPI.rand.nextFloat(), pos.getY() + DragonAPI.rand.nextFloat(), pos.getZ() + DragonAPI.rand.nextFloat(), 0, 0, 0);
//        }
//        ReikaWorldHelper.temperatureEnvironment(world, pos, temperature, this);
//        if (temperature > MAXTEMP) {
//            this.overheat(world, pos);
//        }
//
//    }
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.testIdle();
//        soundtick++;
//        if (tickcount >= 20 && !world.isClientSide) {
//            this.updateTemperature(world, pos);
//            tickcount = 0;
//        }
//        if (soundtick >= 18 && this.canHeatUp()) {
//            soundtick = 0;
//            SoundRegistry.PULSEJET.playSoundAtBlock(world, pos, 1, 1);
//        }
//        RecipesPulseFurnace.PulseJetRecipe recipe = this.getRecipe();
//        if (recipe != null) {
//            this.getFuel(world, pos);
//        }
//
//        tickcount++;
//        tickcount2++;
//
//        if (!world.isClientSide) {
//            int tick = 1;
//            if (!fuel.isEmpty() && power > 0 && omega >= MINSPEED && accel.getLevel() > 10) {
//                tick = 4;
//                if (recipe != null || temperature >= 800) {
//                    accel.removeLiquid(10);
//                    if (DragonAPI.rand.nextInt(4) == 0)
//                        temperature += 1;
//                }
//            }
//
//            //ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.format("%d  %d  %d", this.power, this.omega, this.torque));
//            int duration = this.getSmeltingDuration();
//
//            if (recipe != null)
//                smelttick += tick;
//            else
//                smelttick = 0;
//
//            if (recipe != null && smelttick >= duration) {
//                pulseFurnaceCookTime += tick;
//                //ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.format("%d", ReikaMathLibrary.extrema(2, 600-this.omega, "max")));
//                if (pulseFurnaceCookTime >= this.getOperationTime()) {
//                    pulseFurnaceCookTime = 0;
//                    this.smeltItem(recipe);
//                    smelttick = 0;
//                    //flag2 = false;
//                }
//            } else
//                pulseFurnaceCookTime = 0;
//        }
//    }
//
//    @Override
//    protected void animateWithTick(Level level, BlockPos blockPos) {
//
//    }
//
//    private int getSmeltingDuration() {
//        if (temperature >= 980) //8x speed if about to fail
//            return SMELTTICKS_BASE / 8;
//        else if (temperature >= 950) //4x speed if running uncooled and very hot
//            return SMELTTICKS_BASE / 4;
//        else if (temperature >= 900) //2x speed if running uncooled
//            return SMELTTICKS_BASE / 2;
//        else
//            return SMELTTICKS_BASE;
//    }
//
//    /**
//     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
//     */
//    private RecipesPulseFurnace.PulseJetRecipe getRecipe() {
//        this.getPowerBelow();
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", power));
//        if (power <= 0 || omega < MINSPEED)
//            return null;
//        if (itemHandler.getStackInSlot(0).isEmpty())
//            return null;
//        if (fuel.isEmpty())
//            return null;
//
//        RecipesPulseFurnace.PulseJetRecipe rec = RecipesPulseFurnace.getRecipes().getSmeltingResult(itemHandler.getStackInSlot(0));
//        if (rec == null || rec.requiredTemperature > temperature)
//            return null;
//
//        if (!itemHandler.getStackInSlot(2).isPresent() && !ReikaItemHelper.areStacksCombinable(inv[2], rec.getOutput(), this.getInventoryStackLimit()))
//            return null;
//
//        return rec;
//    }
//
//    /**
//     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
//     */
//    private void smeltItem(RecipesPulseFurnace.PulseJetRecipe rec) {
//        this.smeltHeat();
//        ReikaInventoryHelper.addOrSetStack(rec.getOutput(), inv, 2);
//        ReikaInventoryHelper.decrStack(0, inv);
//    }/*
//
//	private void smeltScrap() {
//		int size = 1;
//		if (itemHandler.getStackInSlot(0).getItem == RotaryItems.HSLA_STEEL_SCRAP.itemID && itemHandler.getStackInSlot(0).getItemDamage() == RotaryItems.HSLA_STEEL_SCRAP.getItemDamage())
//			size = 9;
//		itemHandler.getStackInSlot(0).getCount() -= size;
//		ItemStack i = this.getCraftedScrapIngot();
//		ReikaInventoryHelper.addOrSetStack(i.itemID, 1, i.getItemDamage(), inv, 2);
//		if (itemHandler.getStackInSlot(0).getCount() <= 0)
//			itemHandler.getStackInSlot(0) = null;
//		RotaryAchievements.RECYCLE.triggerAchievement(this.getPlacer());
//	}*/
//
//    private ItemStack getCraftedScrapIngot() {
//        if (ReikaItemHelper.matchStacks(itemHandler.getStackInSlot(0), RotaryItems.HSLA_STEEL_SCRAP))
//            return RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance();
//        if (ReikaItemHelper.matchStacks(itemHandler.getStackInSlot(0), RotaryItems.IRON_SCRAP))
//            return new ItemStack(Items.IRON_INGOT);
//        return null;
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
//        return MachineRegistry.PULSEJET;
//    }
//
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        if (slot != 0)
//            return false;
//        return RecipesPulseFurnace.getRecipes().getSmeltingResult(is) != null;
//    }
//
//    @Override
//    public int getThermalDamage() {
//        return (temperature) / 100;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return this.getRecipe() == null ? 15 : 0;
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m == MachineRegistry.FUELLINE || m.isStandardPipe();
//    }
//
//    @Override
//    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
//        return side.getStepY() == 0;
//    }
//
//    @Override
//    public int fill(Direction from, FluidStack resource, FluidAction doFill) {
//        Fluid fluid = resource.getFluid();
//        if (!this.canFill(from, fluid))
//            return 0;
//        if (fluid.equals(Fluids.WATER)) {
//            return water.fill(resource, doFill);
//        }
//        if (fluid.equals(RotaryFluids.JET_FUEL)) {
//            return fuel.fill(resource, doFill);
//        }
//        if (fluid.equals(RotaryFluids.OXYGEN)) {
//            return accel.fill(resource, doFill);
//        }
//  /*   todo   if (fluid.equals(Fluids.getFluid("oxygen"))) {
//            return accel.fill(resource, doFill);
//        }*/
//        return 0;
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
//    public void overheat(Level world, BlockPos pos) {
//        ReikaWorldHelper.overheat(world, pos.getX(), pos.getY(), pos.getZ(), RotaryItems.HSLA_STEEL_SCRAP.get().getDefaultInstance(), 0, 17, true, 1.5F, false, ConfigRegistry.BLOCKDAMAGE.getState(), 12F);
//    }
//
//    public int getAccelerant() {
//        return accel.getLevel();
//    }
//
//    public FluidStack getAccelerantType() {
//        return accel.getActualFluid();
//    }
//
//    public int getAccelerantCapacity() {
//        return accel.getCapacity();
//    }
//
//    @Override
//    public FluidStack drain(Direction from, int maxDrain, boolean doDrain) {
//        return null;
//    }
//
//    public boolean canFill(Direction from, Fluid fluid) {
//        if (fluid.equals(Fluids.WATER)) {
//            return from.getStepY() == 0;
//        }
//        if (fluid.equals(RotaryFluids.JET_FUEL)) {
//            return from.getStepY() == 0;
//        }
//        if (fluid.equals(RotaryFluids.OXYGEN)) {
//            return from.getStepY() == 0;
//        }
//      /*  if (fluid.equals(Fluids.getFluid("oxygen"))) {
//            return from.getStepY() == 0;
//        }*/
//        return false;
//    }
//
//    public int getWater() {
//        return water.getLevel();
//    }
//
//    public int getFuel() {
//        return fuel.getLevel();
//    }
//
//    @Override
//    public Flow getFlowForSide(Direction side) {
//        return side.getStepY() == 0 ? Flow.INPUT : Flow.NONE;
//    }
//
//    public void addFuel(int amt) {
//        fuel.addLiquid(amt, RotaryFluids.JET_FUEL.get());
//    }
//
//    public void addWater(int amt) {
//        water.addLiquid(amt, Fluids.WATER);
//    }
//
//    @Override
//    public int getOperationTime() {
//        return 20;
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return this.getRecipe() != null && !fuel.isEmpty();
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return fuel.isEmpty() ? "No Fuel" : this.areConditionsMet() ? "Operational" : "Invalid or Missing Items";
//    }
//
//    public void removeFuel(int amt) {
//        fuel.removeLiquid(amt);
//    }
//
//    @Override
//    public boolean canBeCooledWithFins() {
//        return true;
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
//    public boolean allowExternalHeating() {
//        return false;
//    }
//
//    @Override
//    public int getMaxTemperature() {
//        return MAXTEMP;
//    }
//
//    @Override
//    public void onApplyTemperature(Level world, BlockPos pos, int temperature) {
//        if (world.getBlockState(pos).getBlock() == Blocks.FIRE)
//            RotaryAdvancements.PULSEFIRE.triggerAchievement(this.getPlacer());
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
//    public int getTanks() {
//        return 0;
//    }
//
//    
//    @Override
//    public FluidStack getFluidInTank(int tank) {
//        return null;
//    }
//
//    @Override
//    public int getTankCapacity(int tank) {
//        return 0;
//    }
//
//    @Override
//    public boolean isFluidValid(int tank,  FluidStack stack) {
//        return false;
//    }
//
//    @Override
//    public int fill(FluidStack resource, FluidAction action) {
//        return 0;
//    }
//
//    
//    @Override
//    public FluidStack drain(FluidStack resource, FluidAction action) {
//        return null;
//    }
//
//    
//    @Override
//    public FluidStack drain(int maxDrain, FluidAction action) {
//        return null;
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
