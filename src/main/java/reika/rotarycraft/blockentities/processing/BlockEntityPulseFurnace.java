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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.TemperatureEffect.TemperatureCallback;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.auxiliary.recipemanagers.PulseFurnaceRecipe;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.gui.container.machine.inventory.ContainerPulseFurnace;
import reika.rotarycraft.registry.*;

public class BlockEntityPulseFurnace extends InventoriedPowerReceiver implements TemperatureTE, PipeConnector, DiscreteFunction, ConditionalOperation, TemperatureCallback {

    public static final int CAPACITY = 3000;
    public static final int MAXFUEL = 8000;
    public static final int MAXTEMP = 1000; //1370C = melting steel, 800C = 90% strength loss
    public static final int SMELTTICKS_BASE = 100;

    private final HybridTank fuel = new HybridTank("fuel", MAXFUEL);
    private final HybridTank water = new HybridTank("water", CAPACITY);
    private final HybridTank accel = new HybridTank("accel", MAXFUEL);

    public int pulseFurnaceCookTime;
    public boolean idle = false;
    public int temperature;
    public int smelttick = 0;
    private int tickcount2 = 0;
    private int soundtick = 2000;

    public BlockEntityPulseFurnace(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.PULSE_JET_FURNACE.get(), pos, state);
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return i == 2;
    }

    public void testIdle() {
        idle = (this.getRecipe() == null && omega > MINSPEED);
    }

    @Override
    public int getContainerSize() {
        return 3;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        pulseFurnaceCookTime = NBT.getShortOr("CookTime", (short) 0);
        water.readFromNBT(NBT);
        fuel.readFromNBT(NBT);
        accel.readFromNBT(NBT);
        temperature = NBT.getIntOr("temp", 0);
    }

    @Override
    protected String getTEName() {
        return "pulsejetfurnace";
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putShort("CookTime", (short) pulseFurnaceCookTime);
        water.writeToNBT(NBT);
        fuel.writeToNBT(NBT);
        accel.writeToNBT(NBT);
        NBT.putInt("temp", temperature);
    }

    public int getCookProgressScaled(int par1) {
        return Math.min(par1, (pulseFurnaceCookTime * par1) / this.getOperationTime());
    }

    public int getFuelScaled(int par1) {
        return (fuel.getFluidLevel() * par1) / MAXFUEL;
    }

    public int getTempScaled(int par1) {
        return (temperature * par1) / MAXTEMP;
    }

    public int getWaterScaled(int par1) {
        return (water.getFluidLevel() * par1) / CAPACITY;
    }

    public int getFireScaled(int par1) {
        return Math.min(par1, (smelttick * par1) / this.getSmeltingDuration());
    }

    public int getAccelerantScaled(int a) {
        return accel.getFluidLevel() * a / accel.getCapacity();
    }

    private void getFuel(Level world, BlockPos pos) {
        if (tickcount2 >= 100) {
            fuel.removeLiquid(100);
            tickcount2 = 0;
        }
    }

    private void heatAmbient(Level world, BlockPos pos) {
        if (fuel.getFluidLevel() > 0 && this.canHeatUp())
            temperature += Math.max((MAXTEMP - temperature) / 8, 4);

        int dT = 2;
        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);

        if (Tamb < -40) {
            dT = 8;
        } else if (Tamb < -5) {
            dT = 6;
        } else if (Tamb < 5) {
            dT = 4;
        }

        if (Tamb >= 300 && this.canHeatUp()) {
            dT = -1;
        } else if (Tamb >= temperature) {
            dT = 0;
        } else if (Tamb > 30) {
            dT = 1;
        }

        if (water.getFluidLevel() > 0) {
            if (DragonAPI.rand.nextInt(3) == 0) {
                int rem = (temperature * 2 / MAXTEMP) * 50;
                if (Tamb >= 180)
                    rem *= 2;
                if (Tamb >= 90)
                    rem *= 2;
                if (rem > 0)
                    water.removeLiquid(rem);
            }
            if (Tamb >= 300)
                temperature -= temperature / 256;
            else
                temperature -= temperature / 64;
        }

        if (dT != 0) {
            if (dT > 0)
                temperature = Math.max(Tamb, temperature - dT);
            else
                temperature -= dT;
        }
    }

    private boolean canHeatUp() {
        return power >= MINPOWER && omega >= MINSPEED && !fuel.isEmpty();
    }

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
        this.heatAmbient(world, pos);

        if (temperature > 915) {
            world.addParticle(ParticleTypes.LAVA, pos.getX() + DragonAPI.rand.nextFloat(), pos.getY() + DragonAPI.rand.nextFloat(), pos.getZ() + DragonAPI.rand.nextFloat(), 0, 0, 0);
        }
        ReikaWorldHelper.temperatureEnvironment(world, pos, temperature, this);
        if (temperature > MAXTEMP) {
            this.overheat(world, pos);
        }
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.PULSE_JET_FURNACE.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.testIdle();
        soundtick++;
        if (tickcount >= 20 && !world.isClientSide()) {
            this.updateTemperature(world, pos);
            tickcount = 0;
        }
        if (soundtick >= 18 && this.canHeatUp()) {
            soundtick = 0;
            SoundRegistry.PULSEJET.playSoundAtBlock(world, pos, 1, 1);
        }
        PulseFurnaceRecipe recipe = this.getRecipe();
        if (recipe != null) {
            this.getFuel(world, pos);
        }

        tickcount++;
        tickcount2++;

        if (!world.isClientSide()) {
            int tick = 1;
            if (!fuel.isEmpty() && power > 0 && omega >= MINSPEED && accel.getFluidLevel() > 10) {
                tick = 4;
                if (recipe != null || temperature >= 800) {
                    accel.removeLiquid(10);
                    if (DragonAPI.rand.nextInt(4) == 0)
                        temperature += 1;
                }
            }

            int duration = this.getSmeltingDuration();

            if (recipe != null)
                smelttick += tick;
            else
                smelttick = 0;

            if (recipe != null && smelttick >= duration) {
                pulseFurnaceCookTime += tick;
                if (pulseFurnaceCookTime >= this.getOperationTime()) {
                    pulseFurnaceCookTime = 0;
                    this.smeltItem(recipe);
                    smelttick = 0;
                }
            } else
                pulseFurnaceCookTime = 0;
        }
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    private int getSmeltingDuration() {
        if (temperature >= 980) //8x speed if about to fail
            return SMELTTICKS_BASE / 8;
        else if (temperature >= 950) //4x speed if running uncooled and very hot
            return SMELTTICKS_BASE / 4;
        else if (temperature >= 900) //2x speed if running uncooled
            return SMELTTICKS_BASE / 2;
        else
            return SMELTTICKS_BASE;
    }

    /**
     * The pulse-furnace recipe currently smeltable, or null. Requires power, fuel, a matching
     * {@link PulseFurnaceRecipe} whose temperature requirement is met, and room in the output slot.
     */
    private PulseFurnaceRecipe getRecipe() {
        this.getPowerBelow();
        if (power <= 0 || omega < MINSPEED)
            return null;
        ItemStack in = itemHandler.getStackInSlot(0);
        if (in.isEmpty() || fuel.isEmpty())
            return null;
        if (level == null || level.getServer() == null)
            return null;

        SingleRecipeInput input = new SingleRecipeInput(in);
        PulseFurnaceRecipe rec = level.getServer().getRecipeManager()
                .getRecipeFor(RotaryRecipeTypes.PULSE_FURNACE.get(), input, level)
                .map(h -> h.value())
                .orElse(null);
        if (rec == null || rec.requiredTemperature() > temperature)
            return null;

        ItemStack out = itemHandler.getStackInSlot(2);
        if (!out.isEmpty() && !ReikaItemHelper.areStacksCombinable(out, rec.getOutput(), this.getInventoryStackLimit()))
            return null;

        return rec;
    }

    /** Turn one input item into the recipe's output. */
    private void smeltItem(PulseFurnaceRecipe rec) {
        ReikaInventoryHelper.addOrSetStack(rec.getOutput(), itemHandler, 2);
        ReikaInventoryHelper.decrStack(0, itemHandler);
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.PULSEJET;
    }

    public boolean isItemValidForSlot(int slot, ItemStack is) {
        if (slot != 0 || level == null || level.getServer() == null)
            return false;
        SingleRecipeInput input = new SingleRecipeInput(is);
        return level.getServer().getRecipeManager()
                .getRecipeFor(RotaryRecipeTypes.PULSE_FURNACE.get(), input, level).isPresent();
    }

    @Override
    public int getThermalDamage() {
        return temperature / 100;
    }

    @Override
    public int getRedstoneOverride() {
        return this.getRecipe() == null ? 15 : 0;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m == MachineRegistry.FUELLINE || m.isStandardPipe();
    }

    @Override
    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return side.getStepY() == 0;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction doFill) {
        if (from.getStepY() != 0)
            return 0;
        Fluid f = resource.getFluid();
        if (f.isSame(Fluids.WATER))
            return water.fill(resource, doFill);
        if (f.isSame(RotaryFluids.JET_FUEL.get()))
            return fuel.fill(resource, doFill);
        if (f.isSame(RotaryFluids.OXYGEN.get()))
            return accel.fill(resource, doFill);
        return 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        return FluidStack.EMPTY;
    }

    @Override
    public void onEMP() {
    }

    @Override
    public void addTemperature(int temp) {
        temperature += temp;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    @Override
    public int getMaxTemperature() {
        return MAXTEMP;
    }

    @Override
    public int getAmbientTemperature() {
        return 0;
    }

    @Override
    public boolean canBeCooledWithFins() {
        return true;
    }

    @Override
    public boolean allowHeatExtraction() {
        return true;
    }

    @Override
    public boolean allowExternalHeating() {
        return false;
    }

    public void setTemperature(int temp) {
        temperature = temp;
    }

    @Override
    public void overheat(Level world, BlockPos pos) {
        ReikaWorldHelper.overheat(world, pos.getX(), pos.getY(), pos.getZ(), RotaryItems.HSLA_STEEL_SCRAP.get().getDefaultInstance(), 0, 17, true, 1.5F, false, ConfigRegistry.BLOCKDAMAGE.getState(), 12F);
    }

    public int getAccelerant() {
        return accel.getFluidLevel();
    }

    public FluidStack getAccelerantType() {
        return accel.getActualFluid();
    }

    public int getAccelerantCapacity() {
        return accel.getCapacity();
    }

    public int getWater() {
        return water.getFluidLevel();
    }

    public int getFuel() {
        return fuel.getFluidLevel();
    }

    @Override
    public Flow getFlowForSide(Direction side) {
        return side.getStepY() == 0 ? Flow.INPUT : Flow.NONE;
    }

    public void addFuel(int amt) {
        fuel.addLiquid(amt, RotaryFluids.JET_FUEL.get());
    }

    public void addWater(int amt) {
        water.addLiquid(amt, Fluids.WATER);
    }

    public void removeFuel(int amt) {
        fuel.removeLiquid(amt);
    }

    @Override
    public int getOperationTime() {
        return 20;
    }

    @Override
    public boolean areConditionsMet() {
        return this.getRecipe() != null && !fuel.isEmpty();
    }

    @Override
    public String getOperationalStatus() {
        return fuel.isEmpty() ? "No Fuel" : this.areConditionsMet() ? "Operational" : "Invalid or Missing Items";
    }

    // The 1.7.10 version triggered the PULSEFIRE achievement here; achievements (and getPlacer)
    // aren't ported, and the fire-starting itself is handled by temperatureEnvironment above.
    @Override
    public void onApplyTemperature(Level world, BlockPos pos, int temperature) {
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ContainerPulseFurnace(id, inv, this);
    }
}
