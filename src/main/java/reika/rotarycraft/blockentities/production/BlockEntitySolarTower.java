/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.production;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.fluids.FluidStack;
import net.neoforged.fluids.capability.IFluidHandler;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.instantiable.data.blockstruct.BlockArray;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.api.power.PowerGenerator;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.auxiliary.SolarPlant;
import reika.rotarycraft.auxiliary.interfaces.*;
import reika.rotarycraft.auxiliary.interfaces.SodiumSolarUpgrades.SodiumSolarOutput;
import reika.rotarycraft.auxiliary.interfaces.SodiumSolarUpgrades.SodiumSolarReceiver;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;

import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryFluids;

import java.util.Collection;
import java.util.List;

public class BlockEntitySolarTower extends BlockEntityIOMachine implements MultiBlockMachine, SimpleProvider, PipeConnector, PowerGenerator, SolarPlantBlock {

    public static final int GENOMEGA = 512;
    public static final int GENOMEGA_SODIUM = 4096;
    public static final int MAXTORQUE = 16384;
    public static final int MAXTORQUE_SODIUM = 65536;
    private final BlockArray solarBlocks = new BlockArray();
    private final StepTimer mirrorTimer = new StepTimer(100);
    private final HybridTank tank = new HybridTank("solar", 4000);
    private float overallBrightness;
    private int size;
    private int topLocation = -1;
    private int temperature;
    private int currentConsumption;
    private SolarPlant plant;

    public BlockEntitySolarTower(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.SOLAR_TOWER.get(), pos, state);
    }

    public void searchForPlant(Level world, BlockPos pos) {
        if (plant != null)
            return;
        plant = SolarPlant.build(world, pos);
        size = -1;
    }

    public SolarPlant getPlant() {
        return plant;
    }

    public void setPlant(SolarPlant p) {
        plant = p;
        size = -1;
    }

    @Override
    public boolean canProvidePower() {
//        return this.isMultiBlock(level, xCoord, yCoord, zCoord) && this.getMultiBlockPosition(level, xCoord, yCoord, zCoord)[1] == 0;
        return true;
    }


    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.SOLARTOWER;
    }

    @Override
    public void updateBlockEntity() {
        super.updateBlockEntity();
        this.searchForPlant(level, worldPosition);
        topLocation = this.getTopOfTower();

        if (!level.isClientSide) {
            int temp = (int) (5 * size * overallBrightness);
            for (int i = -3; i <= 3; i++) {
                for (int j = -3; j <= 3; j++) {
                    if (ConfigRegistry.BLOCKDAMAGE.getState()) {
                        ReikaWorldHelper.temperatureEnvironment(level, new BlockPos(worldPosition.getX() + i, worldPosition.getY() + 1, worldPosition.getZ() + j), Math.min(temp, 1750));
                        if (temp >= 1500) {
                            this.setRemoved(); //this.delete();
                            level.setBlock(worldPosition, Blocks.LAVA.defaultBlockState(), 0);
                        }
                    }
                }
            }
            if (temp > 400) {
                AABB above = new AABB(worldPosition.getX() - 3, worldPosition.getY() + 1, worldPosition.getZ() - 3, worldPosition.getX() + 4, worldPosition.getY() + 2, worldPosition.getZ() + 4);
                List<LivingEntity> in = level.getEntitiesOfClass(LivingEntity.class, above);
                for (LivingEntity e : in) {
                    if (!e.hasEffect(MobEffects.FIRE_RESISTANCE))
                        e.setSecondsOnFire(3);
                }
            }
        }
        if (level.getBlockState(worldPosition.below()).getBlock() == Blocks.AIR || MachineRegistry.getMachine(level, worldPosition.below()) != this.getMachine()) {
            //ReikaJavaLibrary.pConsole("TOWER: "+this.getTowerHeight()+";  SIZE: "+this.getArraySize());
            BlockPos c = plant.getPrimaryTower();
            if (c != null && worldPosition.equals(new BlockPos(worldPosition.getX(), 0, worldPosition.getZ()))) //c.to2D()
                this.generatePower(level, worldPosition);
            else
                power = omega = torque = 0;
        } else {
            write = null;
        }
        if (level.getBlockState(worldPosition.above()).getBlock() != Blocks.AIR && !(level.getBlockEntity(worldPosition.above()) instanceof SodiumSolarUpgrades)) {
            if (MachineRegistry.getMachine(level, worldPosition.above()) == this.getMachine())
                temperature = ((BlockEntitySolarTower) level.getBlockEntity(worldPosition.above())).temperature;
            return;
        }

        BlockEntity top = level.getBlockEntity(new BlockPos(worldPosition.getX(), topLocation + 1, worldPosition.getZ()));
        if (top instanceof SodiumSolarReceiver ss) {
            if (ss.isActive()) {
                ss.tick(size, overallBrightness);
                temperature = ss.getTemperature();
            }
        }

        if (plant != null) {
            if (this.isTopOfTower()) {
                mirrorTimer.update();
                if (mirrorTimer.checkCap() || size == -1) {
                    overallBrightness = plant.getOverallBrightness(level);
                    size = plant.mirrorCount();
                }
            } else {
                overallBrightness = this.getArrayOverallBrightness();
                size = this.getArraySize();
            }
        } else {
            size = -1;
            overallBrightness = 0;
        }

        if (write != null) {
            this.basicPowerReceiver();
        }
    }

    private void generatePower(Level world, BlockPos pos) {
        this.getTowerWater(world, pos);
        int amt = this.getConsumedFluid();
        write = Direction.DOWN;
        //omega = 1*ReikaMathLibrary.extrema(ReikaMathLibrary.ceil2exp(this.getTowerHeight()), 8, "min")*(this.getArraySize()+1);
        boolean water = tank.getActualFluid().getFluid() == Fluids.WATER;
        omega = water ? GENOMEGA : GENOMEGA_SODIUM;
        torque = this.getGenTorque(world, pos);
        if (this.getArraySize() <= 0 || torque == 0 || tank.getFluidLevel() < amt || (!water && temperature < 800)) {
            omega = 0;
            torque = 0;
        }
        power = (long) omega * (long) torque;
        if (tank.getActualFluid().getFluid() == RotaryFluids.SODIUM.get()) {
            amt = (int) Math.max(1, amt * power / ((double) GENOMEGA_SODIUM * MAXTORQUE_SODIUM));
        }
        currentConsumption = amt;
        if (power > 0 && tank.getFluidLevel() > 0 && amt > 0) {
            if (!water) {
                BlockEntity te = getAdjacentBlockEntity(Direction.DOWN);
                if (te instanceof SodiumSolarOutput ss) {
                    if (ss.isActive()) {
                        amt = ss.receiveSodium(amt);
                    }
                }
            }
            if (amt > 0) {
                tank.removeLiquid(amt);
            }
        }
    }

    private int getGenTorque(Level world, BlockPos pos) {
        if (tank.isEmpty() || plant == null)
            return 0;
        boolean water = tank.getActualFluid().getFluid() == Fluids.WATER;
        int cap = water ? MAXTORQUE : MAXTORQUE_SODIUM;
        float f = water ? 1 : 1.75F;
        float p = water ? 1 : 1.5F;
        return Math.min(cap, (int) (f * this.getArrayOverallBrightness() * plant.getTowerMultiplier() * (Math.pow(this.getArraySize() + 1, p))));
    }

    public int getConsumedFluid() {
        boolean sodium = tank.getActualFluid().getFluid() == RotaryFluids.SODIUM.get();
        int p = ReikaMathLibrary.logbase2(power);
        if (sodium)
            p = Math.max(1, p);
        int base = 10 + (sodium ? 64 : 16) * p;
        int rnd = 10;
        if (base >= 1000)
            rnd = 1000;
        else if (base >= 100)
            rnd = 100;
        int ret = ReikaMathLibrary.roundUpToX(rnd, base);
        if (sodium)
            ret *= 1 / 128D;//0.00390625; //1/256
        return ret;
    }

    public int getCurrentConsumption() {
        return currentConsumption;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public boolean isMultiBlock(Level world, BlockPos pos) {
        return false;
    }

    @Override
    public int[] getMultiBlockPosition(Level world, BlockPos pos) {
        return null;
    }

    @Override
    public int[] getMultiBlockSize(Level world, BlockPos pos) {
        return null;
    }

    public int getArraySize() {
        BlockEntity tile = level.getBlockEntity(new BlockPos(worldPosition.getX(), topLocation, worldPosition.getZ()));
        if (tile == null)
            return 0;
        return ((BlockEntitySolarTower) tile).size;
    }

    public float getArrayOverallBrightness() {
        BlockEntity tile = level.getBlockEntity(new BlockPos(worldPosition.getX(), topLocation, worldPosition.getZ()));
        if (tile == null)
            return 0;
        return ((BlockEntitySolarTower) tile).overallBrightness;
    }

    public int getTopOfTower() {
        int y = worldPosition.getY();
        while (MachineRegistry.getMachine(level, new BlockPos(worldPosition.getX(), y, worldPosition.getZ())) == MachineRegistry.SOLARTOWER) {
            y++;
        }
        return y - 1;
    }

    public int getBottomOfTower() {
        int y = worldPosition.getY();
        while (MachineRegistry.getMachine(level, new BlockPos(worldPosition.getX(), y, worldPosition.getZ())) == MachineRegistry.SOLARTOWER) {
            y--;
        }
        return y + 1;
    }

    public boolean isTopOfTower() {
        return worldPosition.getY() == this.getTopOfTower();
    }

    private void getTowerWater(Level world, BlockPos pos) {
        int lvl = tank.getFluidLevel();
        Fluid f = tank.getActualFluid().getFluid();
        int cy = pos.getY() + 1;
        while (MachineRegistry.getMachine(world, new BlockPos(pos.getX(), cy, pos.getZ())) == MachineRegistry.SOLARTOWER) {
            BlockEntitySolarTower tile = (BlockEntitySolarTower) world.getBlockEntity(new BlockPos(pos.getX(), cy, pos.getZ()));
            Fluid f2 = tile.tank.getActualFluid().getFluid();
            if (f == null && f2 != null)
                f = f2;
            if (f.equals(f2)) {
                lvl += tile.tank.getFluidLevel();
                tile.tank.empty();
            }
            cy++;
        }
        tank.setContents(lvl, f);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    public void updateEntity(Level level, BlockPos blockPos) {

    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        tank.writeToNBT(NBT);

        NBT.putInt("temp", temperature);
        NBT.putInt("flow", currentConsumption);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        tank.readFromNBT(NBT);

        temperature = NBT.getInt("temp");
        currentConsumption = NBT.getInt("flow");
    }

    @Override
    protected String getTEName() {
        return "solartower";
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe();
    }

    @Override
    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return true;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        return null;
    }


    @Override
    public void onEMP() {
    }

    @Override
    public PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
        return new PowerSourceList().addSource(this);
    }

    public long getMaxPower() {
        return (long) torque * omega;
    }

    public long getCurrentPower() {
        return power;
    }


    private boolean canUseSodium() {
        int y = this.getTopOfTower() + 1;
        BlockEntity te = level.getBlockEntity(new BlockPos(worldPosition.getX(), y, worldPosition.getZ()));
        if (te instanceof SodiumSolarReceiver ss) {
            return ss.isActive();
        }
        return false;
    }

    //@Override
    //public FluidTankInfo[] getTankInfo(Direction from) {
    //    return new FluidTankInfo[]{tank.getInfo()};
    //}

    @Override
    public Flow getFlowForSide(Direction side) {
        return Flow.INPUT;
    }

    public int getEmittingX() {
        return worldPosition.getX() + write.getStepX();
    }

    public int getEmittingY() {
        return worldPosition.getY() + write.getStepY();
    }

    public int getEmittingZ() {
        return worldPosition.getZ() + write.getStepZ();
    }

    @Override
    public BlockPos getEmittingPos(BlockPos pos) {
        return new BlockPos(getEmittingX(), getEmittingY(), getEmittingZ());
    }

    @Override
    public void getAllOutputs(Collection<BlockEntity> c, Direction dir) {
        c.add(getAdjacentBlockEntity(write));
    }

    @Override
    public void breakBlock() {
        plant.invalidate(level);
    }

    //@Override
    public boolean canFill(Fluid fluid) {
        return fluid.equals(Fluids.WATER) || (fluid.equals(RotaryFluids.SODIUM) && this.canUseSodium());
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return false;
    }
}
