/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.modinterface.conversion;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.modinteract.power.ReikaRailCraftHelper;
import reika.rotarycraft.auxiliary.interfaces.RCToModConverter;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.base.blockentity.PoweredLiquidIO;
import reika.rotarycraft.registry.*;

public class BlockEntityBoiler extends PoweredLiquidIO implements TemperatureTE, RCToModConverter {

    private int temperature;
    private long storedEnergy;

    public static final int CAPACITY = 27000;
    public static final int MAXTEMP = 500;

    private final StepTimer timer = new StepTimer(20);

    public BlockEntityBoiler(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.FRICTION_BOILER.get(), pos, state);
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.BOILER;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        if (output.isFull())
            return 15;
        if (input.isEmpty())
            return 15;
        return 0;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.FRICTION_BOILER.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        this.getPowerBelow();

        timer.update();
        if (timer.checkCap())
            this.updateTemperature(world, pos);
        if (this.acceptEnergy())
            storedEnergy += power * 200 * ConfigRegistry.getConverterEfficiency();

        //ReikaJavaLibrary.pConsole(storedEnergy/ReikaRailCraftHelper.getSteamBucketEnergy(this.getWaterTemp()), Dist.DEDICATED_SERVER);

        //ReikaJavaLibrary.pConsoleSideOnly(this.getSteam()+":"+storedEnergy+"/"+ReikaRailCraftHelper.getSteamBucketEnergy(), Dist.DEDICATED_SERVER);
        if (!world.isClientSide()) {
            int space = output.getRemainingSpace();
            if (space > 0) {
                int mB = Math.min(space, Math.min(input.getFluidLevel(), ReikaRailCraftHelper.getAmountConvertibleSteam(this.getInitTemp(), storedEnergy)));
                if (mB > 0)
                    this.makeSteam(mB);
            }
        }

        BlockEntity te = world.getBlockEntity(pos.above());
        if (te instanceof IFluidHandler ic) {
            if (output.getFluid() != null) {
                int amt = ic.fill(output.getFluid(), IFluidHandler.FluidAction.EXECUTE); //direction = down
                if (amt > 0)
                    output.removeLiquid(amt);
            }
        }
    }

    private boolean acceptEnergy() {
        return temperature > 100 && !input.isEmpty() && !output.isFull();
    }

    private void makeSteam(int mB) {
        input.removeLiquid(mB);
        output.addLiquid(this.getProducedSteam(mB), RotaryFluids.STEAM.get()/*FluidRegistry.getFluid("steam")*/);
        storedEnergy -= ReikaRailCraftHelper.getSteamEnergy(this.getInitTemp(), mB);
    }

    private int getProducedSteam(int mB) {
        return 8 * mB;
    }

    private int getInitTemp() {
        return ReikaWorldHelper.getAmbientTemperatureAt(level, worldPosition);
    }

    public int getSteam() {
        return output.getFluid() != null ? output.getFluid().getAmount() : 0;
    }

    public int getWater() {
        return input.getFluidLevel();
    }

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
        if (power > 0) {
            temperature += 0.3125 * ReikaMathLibrary.logbase(power, 2);
        }
        if (temperature > Tamb) {
            temperature -= (temperature - Tamb) / 40;
        } else {
            temperature += (temperature - Tamb) / 40;
        }
        if (temperature - Tamb <= 40 && temperature > Tamb)
            temperature--;
        if (temperature > MAXTEMP) {
            temperature = MAXTEMP;
            this.overheat(world, pos);
        }
        if (temperature > 50) {
            Direction side = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.SNOW);
            if (side != null)
                ReikaWorldHelper.changeAdjBlock(world, pos, side, Blocks.AIR.defaultBlockState());
            side = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.ICE);
            if (side != null)
                ReikaWorldHelper.changeAdjBlock(world, pos, side, Fluids.FLOWING_WATER.getFlowing().defaultFluidState().createLegacyBlock());
        }
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
    public int getThermalDamage() {
        return 0;
    }

    @Override
    public void overheat(Level world, BlockPos pos) {
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 6, Level.ExplosionInteraction.BLOCK);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("temp", temperature);
        NBT.putLong("energy", storedEnergy);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        temperature = NBT.getInt("temp");
        storedEnergy = NBT.getLong("energy");
    }

    @Override
    protected String getTEName() {
        return "friction_boiler";
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe();
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        return 0;
    }


    @Override
    public Fluid getInputFluid() {
        return Fluids.WATER;
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public boolean canReceiveFrom(Direction from) {
        return from.getStepY() == 0;
    }

    @Override
    public boolean canOutputTo(Direction to) {
        return to == Direction.UP;
    }

    @Override
    public boolean canIntakeFromPipe(MachineRegistry p) {
        return p.isStandardPipe();
    }

    @Override
    public boolean canOutputToPipe(MachineRegistry p) {
        return p.isStandardPipe();
    }

    @Override
    public boolean canBeCooledWithFins() {
        return false;
    }

    public void setTemperature(int temp) {
        temperature = temp;
    }

    @Override
    public boolean allowExternalHeating() {
        return false;
    }

    @Override
    public boolean allowHeatExtraction() {
        return false;
    }

    @Override
    public int getAmbientTemperature() {
        return 0;
    }

    @Override
    public int getMaxTemperature() {
        return MAXTEMP;
    }

    @Override
    public int getGeneratedUnitsPerTick() {
        return this.getProducedSteam(this.getWater());
    }

    @Override
    public String getUnitDisplay() {
        return "Steam";
    }
    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }
}
