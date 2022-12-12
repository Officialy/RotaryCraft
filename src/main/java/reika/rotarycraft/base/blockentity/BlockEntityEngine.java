/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.ParallelTicker;
import reika.dragonapi.interfaces.blockentity.PartialInventory;
import reika.dragonapi.interfaces.blockentity.PartialTank;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.mathsci.ReikaTimeHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.power.PowerGenerator;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.*;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.items.tools.ItemIntegratedGearbox;
import reika.rotarycraft.registry.*;

import java.util.Collection;

public abstract class BlockEntityEngine extends BlockEntityInventoryIOMachine implements TemperatureTE, SimpleProvider, PipeConnector, PowerGenerator, PartialInventory, PartialTank, IntegratedGearboxable {

    /**
     * Water capacity
     */
    public static final int CAPACITY = 60 * 1000;

    /**
     * Fuel capacity
     */
    public static final int FUELCAP = 240 * 1000;
    public static final int LUBECAP = 24 * 1000;
    public static int temperature;
    protected final ParallelTicker timer = new ParallelTicker().addTicker("fuel").addTicker("sound").addTicker("temperature", ReikaTimeHelper.SECOND.getDuration());
    protected final HybridTank water = new HybridTank("enginewater", CAPACITY);
    protected final HybridTank lubricant = new HybridTank("enginelube", LUBECAP);
    protected final HybridTank fuel = new HybridTank("enginefuel", FUELCAP);
    protected final HybridTank air = new HybridTank("engineoxygen", 1000);
    /**
     * For timing control
     */
    public int soundtick = 2000;
    protected EngineType type = EngineType.DC;
    protected int backx;
    protected int backz;
    protected long lastpower = 0;
    private boolean isOn;
    private int integratedGear = 0;

    public BlockEntityEngine(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    static int getIntegratedGearTorque(int torque, int gear) {
        if (gear != 0) {
            return gear > 0 ? gear * torque : -torque / gear;
        }
        return torque;
    }

    static int getIntegratedGearSpeed(int speed, int gear) {
        if (gear != 0) {
            return gear < 0 ? -gear * speed : speed / gear;
        }
        return speed;
    }

    public static boolean isAirFluid(Fluid f) {
        return f.equals(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("air"))) || f.equals(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("oxygen"))) || f.equals(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("rc_oxygen")));
    }

    public final EngineType getEngineType() {
        return type != null ? type : EngineType.DC;
    }

    protected int getConsumedFuel() {
        return 10;
    }

    public final int getWaterScaled(int par1) {
        return water.getLevel() * par1 / CAPACITY;
    }

    public int getMaxTemperature() {
        return 1000;
    }

    public final int getTempScaled(int par1) {
        return temperature * par1 / getMaxTemperature();
    }

    /**
     * In seconds
     */
    public int getFuelDuration() {
        if (!type.burnsFuel())
            return -1;
        int fuel = getFuelLevel();
        float burnprogress = 0;
        if (fuel > 0)
            burnprogress = 1F - timer.getPortionOfCap("fuel") / fuel;
        float factor = type.getFuelUnitDuration() / (float) timer.getCapOf("fuel"); //to compensate for 4x burn during spinup
        if (factor <= 0)
            return 0;
        return (int) ((fuel * type.getFuelUnitDuration() * (burnprogress)) * 5 / factor / 1000 * 10D / getConsumedFuel());
    }

    @Override
    public final boolean isUseableByPlayer(Player ep) {
        return type.hasGui() && super.isUseableByPlayer(ep);
    }

    public int getFuelLevel() {
        return 0;
    }

    public final void setType(Block block) {
        if (RotaryBlocks.DC_ENGINE.get() == block) {
            type = EngineType.DC;
        } else if (RotaryBlocks.AC_ENGINE.get() == block) {
            type = EngineType.AC;
        } else if (RotaryBlocks.STEAM_ENGINE.get() == block) {
            type = EngineType.STEAM;
        } else if (RotaryBlocks.GAS_ENGINE.get() == block) {
            type = EngineType.GAS;
        } else if (RotaryBlocks.WIND_ENGINE.get() == block) {
            type = EngineType.WIND;
        } else if (RotaryBlocks.PERFORMANCE_ENGINE.get() == block) {
            type = EngineType.SPORT;
        } else if (RotaryBlocks.MICROTURBINE.get() == block) {
            type = EngineType.MICRO;
        }
    }

    public final int getInventoryStackLimit() {
        return type.allowInventoryStacking() ? 64 : 1;
    }

    public final int getContainerSize() {
        return 2;
    }

    public boolean hasTemperature() {
        return type.isCooled();
    }

    public final int getFuelScaled(int par1) {
        return this.getFuelLevel() * par1 / FUELCAP;
    }

    protected abstract void consumeFuel();

    protected abstract void internalizeFuel();

    protected abstract boolean getRequirements(Level world, BlockPos pos);

    protected final boolean hasAir(Level world, BlockPos pos) {
        if (!air.isEmpty()) {
            air.removeLiquid(2);
            return true;
        }
        return !this.isDrowned(world, pos);
        //return !AtmosphereHandler.isNoAtmo(world, x - this.getWriteDirection().getStepX(), y, z - this.getWriteDirection().getStepZ(), blockType, true);
    }

    public final boolean isDrowned(Level world, BlockPos pos) {
        boolean flag = false;
        for (int i = 0; i < 6; i++) {
            Direction dir = Direction.values()[i];
            int dx = pos.getX() + dir.getStepX();
            int dy = pos.getY() + dir.getStepY();
            int dz = pos.getZ() + dir.getStepZ();
            Block id = world.getBlockState(new BlockPos(dx, dy, dz)).getBlock();
            boolean fluid = id instanceof LiquidBlock;
            flag = flag || fluid;
            if (id == Blocks.AIR)
                return false;
            if (!fluid)
                if (ReikaWorldHelper.softBlocks(world, new BlockPos(dx, dy, dz)))
                    return false;
        }
        return flag && true;
    }

    public void updateTemperature(Level world, BlockPos pos) {
        //Biome biome = world.getBiomeGenForCoords(x, z);
        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
        temperature = Math.max(temperature, Tamb);
//        ReikaChatHelper.writeInt(temperature);
        if (temperature > Tamb && !isOn) {
            this.offlineCooldown(world, pos, Tamb);
        }
    }

    protected void offlineCooldown(Level world, BlockPos pos, int Tamb) {
        temperature -= Math.max(1, (temperature - Tamb) / 256);
    }

    public final boolean isOn() {
        return isOn;
    }

    public void overheat(Level world, BlockPos pos) {

    }

    private void setPowerData(Level world, BlockPos pos) {
        int speed = getIntegratedGearSpeed(this.getMaxSpeed(world, pos), integratedGear);
        this.updateSpeed(speed, speed >= omega && (omega > 0 || this.canStart()));
        torque = getIntegratedGearTorque(this.getGenTorque(world, pos), integratedGear);
    }

    protected boolean canStart() {
        return true;
    }

    protected int getMaxSpeed(Level world, BlockPos pos) {
        return type.getSpeed();
    }

    protected int getGenTorque(Level world, BlockPos pos) {
        return type.getTorque();
    }

    protected abstract void affectSurroundings(Level world, BlockPos pos);

    protected final int getSoundLength() {
        return this.getSoundLength(1);
    }

    protected int getSoundLength(float factor) {
        if (factor == 2.5F && type.carNoise())
            factor = 1.81F;
        if (factor == 2.5F && type.turbineNoise()) {
            factor = 2F;
        }
//        if (type.jetNoise()) {
//            factor += 0.0125F;
//        }
        return (int) (type.getSoundLength() * factor);
    }

    private void initialize(Level world, BlockPos pos) {
        setType(world.getBlockState(pos).getBlock());
        timer.setCap("sound", this.getSoundLength());

        if (timer.checkCap("temperature")) {
            this.updateTemperature(world, pos);
        }

        boolean on = !type.isAirBreathing() || this.hasAir(world, pos);

        if (this.getRequirements(world, pos) && on) {
//            RotaryCraft.LOGGER.info("Engine requirements met");
            isOn = true;
            this.setPowerData(world, pos);
        } else {
            isOn = false;
            this.updateSpeed(0, false);
            if (omega == 0)
                torque = 0;
            if (soundtick == 0 && omega == 0)
                soundtick = 2000;
        }
    }

//    private BlockEntityEngineController getECU() {
//        return (BlockEntityEngineController) getAdjacentBlockEntity(isFlipped ? Direction.UP : Direction.DOWN);
//    }

    private void updateSpeed(int maxspeed, boolean revup) {
//        if (this.hasECU() && this.canBeThrottled()) {
//            BlockEntityEngineController te = this.getECU();
//            if (te != null) {
//                maxspeed *= te.getSpeedMultiplier();
//            }
//            if (omega > maxspeed)
//                revup = false;
//        }
        if (revup) {
            if (omega < maxspeed) {
                ReikaJavaLibrary.pConsole(omega+"->"+(omega+2*(int)(ReikaMathLibrary.logbase(maxspeed, 2))), Dist.DEDICATED_SERVER);
                omega += 4 * ReikaMathLibrary.logbase(maxspeed + 1, 2);
                timer.setCap("fuel", Math.max(type.getFuelUnitDuration() / 4, 1)); //4x fuel burn while spinning up
                if (omega > maxspeed)
                    omega = maxspeed;
            }
        } else {
            if (omega > 0) {
                ReikaJavaLibrary.pConsole(omega+"->"+(omega-omega/128-1), Dist.DEDICATED_SERVER);
                omega -= omega / 256 + 1;
            }
        }
    }

    private boolean hasECU() {
//        return this.getMachine(isFlipped ? Direction.UP : Direction.DOWN) == MachineRegistry.ECU;
        return false;
    }

    protected abstract void playSounds(Level world, BlockPos pos, float pitchMultiplier, float vol);

    protected final boolean isMuffled(Level world, BlockPos pos) {
        if (world.getBlockState(new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ())).getMaterial() == Material.CLOTH_DECORATION) {// || this.getMachine(Direction.UP) == MachineRegistry.ECU) {
            if (world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).getMaterial() == Material.CLOTH_DECORATION)// || this.getMachine(Direction.DOWN) == MachineRegistry.ECU)
                return true;
        }
        for (int i = 0; i < 6; i++) {
            Direction dir = Direction.values()[i];
            if (dir != Direction.DOWN) {
                int dx = pos.getX() + dir.getStepX();
                int dy = pos.getY() + dir.getStepY();
                int dz = pos.getZ() + dir.getStepZ();
                if ((dir != write.getOpposite() && dir != write) || dir == Direction.UP) {
                    Block b = world.getBlockState(new BlockPos(dx, dy, dz)).getBlock();
                    if (b.defaultBlockState().getMaterial() != Material.CLOTH_DECORATION)
                        return false;
                }
            }
        }
        return true;
    }

    @Override
    public final void updateEntity(Level world, BlockPos pos) {
        super.updateEntity();
        super.updateBlockEntity();
        RotaryCraft.LOGGER.debug("power = " + power);
        this.getIOSides(world, pos.getX(), pos.getY(), pos.getZ(), getBlockState().getValue(BlockRotaryCraftMachine.FACING));
        timer.updateTicker("temperature");
        if (this.isShutdown()) {
            omega = torque = 0;
            power = 0;
            RotaryCraft.LOGGER.debug("shutdown");
        } else {
            if (!level.isClientSide() || RotaryAux.getPowerOnClient) {
                timer.setCap("fuel", type.getFuelUnitDuration());
                RotaryCraft.LOGGER.debug("initializing");
                this.initialize(world, pos);
            }
            power = (long) torque * (long) omega;
            if (power > 0)
                this.affectSurroundings(world, pos);
        }

        float pitch = 1F;
        float soundfactor = 1F;
        if (type.isECUControllable() && this.hasECU()) {
//            BlockEntityEngineController te = this.getECU();
//            if (te != null) {
//                if (te.canProducePower()) {
//                    if (this.canBeThrottled()) {
//                        int fueltime = type.getFuelUnitDuration();
//                        if (omega >= type.getSpeed() * te.getSpeedMultiplier()) {
//                            //omega = (int)(omega*te.getSpeedMultiplier());
//                            int max = (int) (type.getSpeed() * te.getSpeedMultiplier());
//                            //this.updateSpeed(max, omega < max);
//                        } else {
//                            fueltime = Math.max(type.getFuelUnitDuration() / 4, 1);
//                        }
//                        timer.setCap("fuel", fueltime);
//                        int fuelcap = timer.getCapOf("fuel");
//                        fuelcap = fuelcap * te.getFuelMultiplier(type.type);
//                        timer.setCap("fuel", fuelcap);
//                        pitch = te.getSoundStretch();
//                        soundfactor = 1F / te.getSoundStretch();
//                        int soundcap = timer.getCapOf("sound");
//                        soundcap = (int) (soundcap * soundfactor);
//                        timer.setCap("sound", soundcap);
//                        int tempcap = timer.getCapOf("temperature");
//                        tempcap *= soundfactor;
//                        timer.setCap("temperature", tempcap);
//                    }
//                } else {
//                    //this.updateSpeed(0, false);
//                    this.resetPower();
//                    soundtick = 0;
//                }
//            }
        }

        this.resetPower(); //temp once above is fixed
        soundtick = 0; //temp once above is fixed

        this.basicPowerReceiver();

        this.internalizeFuel();
        if (power > 0) {
            RotaryCraft.LOGGER.debug("power > 0");
            timer.updateTicker("fuel");
            if (type.burnsFuel() && timer.checkCap("fuel") && this.canConsumeFuel())
                this.consumeFuel();
        }

        if (power > 0) {
            this.playSounds(world, pos, pitch, 1);
        } else if (soundtick < this.getSoundLength(soundfactor))
            soundtick = 2000;

        lastpower = power;
    }

    protected void resetPower() {
        if (omega == 0)
            torque = 0;
        power = (long) omega * (long) torque;
        soundtick = 2000;
        lastpower = power;
    }

    public final void getIOSides(Level world, int x, int y, int z, Direction dir) {
        switch (dir) {
            case WEST -> {
                write = Direction.WEST;
                backx = x + 1;
                backz = z;
            }
            case EAST -> {
                write = Direction.EAST;
                backx = x - 1;
                backz = z;
            }
            case NORTH -> {
                write = Direction.NORTH;
                backx = x;
                backz = z + 1;
            }
            case SOUTH -> {
                write = Direction.SOUTH;
                backx = x;
                backz = z - 1;
            }
        }
    }
    protected boolean canConsumeFuel() {
        return this.getFuelLevel() > 0;
    }

    protected boolean canBeThrottled() {
        return true;
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);

        tag.putInt("type", type.ordinal());

        if (this.hasTemperature())
            tag.putInt("temperature", temperature);

        if (type.needsWater())
            water.writeToNBT(tag);
        if (type.isEthanolFueled() || type.isJetFueled())
            fuel.writeToNBT(tag);

        if (type.requiresLubricant())
            lubricant.writeToNBT(tag);

        if (type.burnsFuel()) {
            tag.putInt("fueltimer", timer.getCapOf("fuel"));
        }

        tag.putInt("gear", integratedGear);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);

        type = EngineType.setType(tag.getInt("type"));

        if (this.hasTemperature())
            temperature = tag.getInt("temperature");

        if (type.requiresLubricant())
            lubricant.readFromNBT(tag);

        if (type.needsWater())
            water.readFromNBT(tag);
        if (type.isEthanolFueled() || type.isJetFueled())
            fuel.readFromNBT(tag);

        if (tag.contains("fueltimer")) {
            timer.setCap("fuel", tag.getInt("fueltimer"));
        }

        integratedGear = tag.getInt("gear");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putFloat("phi", phi);
        timer.saveAdditional(nbt, "engine");
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        timer.load(tag, "engine");
        phi = tag.getFloat("phi");

        if (omega > type.getSpeed())
            omega = type.getSpeed();
        if (torque > type.getTorque())
            torque = type.getTorque();
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack is) {
        if (!type.isValidFuel(is))
            return false;
        if (i >= type.getContainerSize())
            return false;
        return switch (type) {
            case GAS, AC -> true;
            case SPORT -> (i == 0 && is.getItem() == RotaryItems.ETHANOL.get()) || (i == 1 && type.isAdditive(is));
            default -> false;
        };
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    //    @Override
    public final boolean canExtractItem(int i, ItemStack itemstack, int j) {
        if (type == EngineType.AC) {
            if (ReikaItemHelper.matchStacks(itemstack, RotaryItems.HSLA_SHAFT_CORE) || ReikaItemHelper.matchStacks(itemstack, RotaryItems.TUNGSTEN_ALLOY_SHAFT_CORE)) {
                if (itemstack.getTag() == null)
                    return true;
                return itemstack.getTag().getInt("magnet") == 0;
            }
            return false;
        }
        if (type == EngineType.STEAM) {
            return itemstack.getItem() == Items.BUCKET;
        }
        return false;
    }

    @Override
    protected final void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        double pow = 1.05;
        double mult = 1;
/*        if (type == EngineType.JET)
            pow = 1.1;
        if (type == EngineType.HYDRO) {
            BlockEntityHydroEngine te = (BlockEntityHydroEngine) this;
            if (te.failed) {
                phi += 16;
                return;
            }
            mult = 256F / type.getSpeed();
        }*/
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase((int) (mult * omega + 1), 2), pow);
        this.setChanged();
//        RotaryCraft.LOGGER.info("phi: " + phi);
    }

    @Override
    public final boolean canProvidePower() {
        return true;
    }

    @Override
    public final MachineRegistry getMachine() {
        return MachineRegistry.ENGINE;
    }

    @Override
    public final int getThermalDamage() {
        if (type.canHurtPlayer() && this.hasTemperature())
            return (temperature) / 100;
        return 0;
    }

    @Override
    public final int getRedstoneOverride() {
        if (type.burnsFuel()) {
            if (type.isEthanolFueled())
                return 15 * fuel.getLevel() / FUELCAP;
            if (type.isJetFueled())
                return 15 * fuel.getLevel() / FUELCAP;
            else
                return 15 * water.getLevel() / FUELCAP;
        }
        return 0;
    }

    public final int getFuelCapacity() {
        if (type.isEthanolFueled())
            return FUELCAP;
        if (type.isJetFueled())
            return FUELCAP;
        if (type == EngineType.STEAM)
            return CAPACITY;
        return 0;
    }

    /**
     * In seconds
     */
    public final int getFullTankDuration() {
        if (!type.burnsFuel())
            return -1;
        return this.getFuelCapacity() * type.getFuelUnitDuration() * 5;
    }

    @Override
    public final boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe() || m == MachineRegistry.FUELLINE || m == MachineRegistry.HOSE;
    }

    @Override
    public final boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        if (type == null)
            return false;
        if (type.isJetFueled() || type.isEthanolFueled())
            if ((p == MachineRegistry.FUELLINE || p == MachineRegistry.BEDPIPE) && side == (isFlipped ? Direction.UP : Direction.DOWN))
                return true;
        if (type.isWaterPiped() && p.isStandardPipe()) {
            switch (side) {
                case EAST:
//                    return this.getBlockMetadata() == 0;
                case SOUTH:
//                    return this.getBlockMetadata() == 2;
                case WEST:
//                    return this.getBlockMetadata() == 1;
                case NORTH:
//                    return this.getBlockMetadata() == 3;
                default:
                    return false;
            }
        }
        if (type.requiresLubricant() && (p == MachineRegistry.HOSE || p == MachineRegistry.BEDPIPE)) {
        //ReikaJavaLibrary.pConsole(this.getBlockMetadata()+":"+side.name());
            switch (side) {
                case EAST:
//                    return this.getBlockMetadata() == 0;
                case SOUTH:
//                    return this.getBlockMetadata() == 2;
                case WEST:
//                    return this.getBlockMetadata() == 1;
                case NORTH:
//                    return this.getBlockMetadata() == 3;
                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public final void addTemperature(int temp) {
        temperature += temp;
    }

    @Override
    public final int getTemperature() {
        return temperature;
    }

    public final void setTemperature(int temp) {
        temperature = temp;
    }

    public boolean allowExternalHeating() {
        return false;
    }

    @Override
    public boolean allowHeatExtraction() {
        return this.canBeCooledWithFins();
    }

    @Override
    public boolean canBeCooledWithFins() {
        return false;
    }

    @Override
    public double heatEnergyPerDegree() {
        return 2 * super.heatEnergyPerDegree();
    }

    @Override
    public final void onEMP() {
        if (type.isEMPImmune())
            return;
        else
            super.onEMP();
    }

    @Override
    public PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
        PowerSourceList psl = new PowerSourceList();
        if (power > 0)
            psl.addSource(this);
        return psl;
    }

    @Override
    public final void getAllOutputs(Collection<BlockEntity> c, Direction dir) {
        c.add(getAdjacentBlockEntity(write));
    }

    public final long getMaxPower() {
        if (type == null)
            return 0;
        return type.getPower();
    }

    public final long getCurrentPower() {
        return power;
    }

/*    @Override
    public final int fill(Direction from, FluidStack resource, FluidAction doFill) {
        Fluid f = resource.getFluid();
        if (!this.canFill(from, f))
            return 0;
        if (f.equals(Fluids.WATER)) {
            return water.fill(resource, doFill);
        } else if (f.equals(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("rc_lubricant")))) {
            return lubricant.fill(resource, doFill);
        } else if (isAirFluid(f)) {
            return air.fill(resource, doFill);
        } else {
            return fuel.fill(resource, doFill);
        }
    }*/

    @Override
    public final FluidStack drain(Direction from, int maxDrain, boolean doDrain) {
        return null;
    }

    //        @Override
    public final boolean canFill(Direction from, Fluid fluid) {
        if (isAirFluid(fluid)) {
            return type.isAirBreathing() && from == this.getFuelInputDirection();
        }
//        if (!type.canReceiveFluid(fluid))
//            return false;
        if (fluid.equals(Fluids.WATER)) {
            int dx = worldPosition.getX() + from.getStepX();
            int dy = worldPosition.getY() + from.getStepY();
            int dz = worldPosition.getZ() + from.getStepZ();
            return dx == backx && dy == worldPosition.getY() && dz == backz;
        } else if (fluid.equals(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("rc lubricant")))) {
            int dx = worldPosition.getX() + from.getStepX();
            int dy = worldPosition.getY() + from.getStepY();
            int dz = worldPosition.getZ() + from.getStepZ();
            return dx == backx && dy == worldPosition.getY() && dz == backz;
        } else if (fluid.equals(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("rc jet fuel")))) {
            return from == this.getFuelInputDirection();
        } else if (fluid.equals(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("rc ethanol")))) {
            return from == this.getFuelInputDirection();
        } else if (fluid.equals(ForgeRegistries.FLUIDS.getValue(new ResourceLocation("bioethanol")))) {
            return from == this.getFuelInputDirection();
        }
        return false;
    }

    private Direction getFuelInputDirection() {
        return isFlipped ? Direction.UP : Direction.DOWN;
    }

//    @Override
//    public final IFluidTank[] getTankIfo(Direction from) {
//        return new IFluidTank[]{water.getInfo(), fuel.getInfo(), lubricant.getInfo()};
//    }

    public final void addFuel(int amt) {
        fuel.addLiquid(amt, type.getFuelType());
    }

    public final void addLubricant(int amt) {
        lubricant.addLiquid(amt, RotaryFluids.LUBRICANT.get());
    }

    public final void removeLubricant(int amt) {
        lubricant.removeLiquid(amt);
    }

    public final int getLube() {
        return lubricant.getLevel();
    }

    public final void setLube(int amt) {
        lubricant.setContents(amt, RotaryFluids.LUBRICANT.get());
    }

    public final void subtractFuel(int amt) {
        fuel.removeLiquid(amt);
    }

    public final void addWater(int amt) {
        water.addLiquid(amt, Fluids.WATER);
    }

    public final int getWater() {
        return water.getLevel();
    }

    @Override
    public final Flow getFlowForSide(Direction side) {
        return Flow.INPUT;
    }

    @Override
    public final boolean hasInventory() {
        return type.hasInventory();
    }

    @Override
    public final boolean hasTank() {
        return type.usesFluids();
    }

    @Override
    public BlockPos getEmittingPos(BlockPos pos) {
        return new BlockPos(pos.getX() + write.getStepX(), pos.getY() + write.getStepY(), pos.getZ() + write.getStepZ());
    }

    public boolean isBroken() {
        return false;
    }

    public void breakBlock() {
        if (integratedGear != 0) {
            ItemStack is = ItemIntegratedGearbox.getIntegratedGearItem(integratedGear, null);
            ReikaItemHelper.dropItem(level, new BlockPos(worldPosition.getX() + DragonAPI.rand.nextDouble(), worldPosition.getY() + DragonAPI.rand.nextDouble(), worldPosition.getZ() + DragonAPI.rand.nextDouble()), is);
        }
    }

    public final boolean applyIntegratedGear(ItemStack is) {
        if (is == null || !(RotaryItems.INTEGRATED_GEARBOX.get() == is.getItem()))
            return false;
        if (integratedGear != 0)
            return false;
        if (omega > 0 || power > 0)
            return false;
        integratedGear = ItemIntegratedGearbox.getRatioFromIntegratedGearItem(is, true);
//        this.syncAllData(true);
        return integratedGear != 0;
    }

    public final int getIntegratedGear() {
        return integratedGear;
    }

}
