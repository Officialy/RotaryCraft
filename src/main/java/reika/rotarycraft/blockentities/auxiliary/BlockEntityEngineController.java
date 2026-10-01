/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.auxiliary;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.storage.FilteredFluidResourceHandler;
import reika.dragonapi.instantiable.storage.HybridTankResourceHandler;
import reika.dragonapi.interfaces.blockentity.HasFluidResourceHandler;
import reika.dragonapi.libraries.java.ReikaStringParser;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
import reika.rotarycraft.registry.EngineType;
import reika.rotarycraft.modinterface.TileEntityFuelEngine;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryFluids;

public class BlockEntityEngineController extends RotaryCraftBlockEntity implements PipeConnector, HasFluidResourceHandler {

    public static final int FUELCAP = 3000;

    private final HybridTank tank = new HybridTank("ecu", FUELCAP);
    private final ResourceHandler<FluidResource> fluidHandler = new HybridTankResourceHandler(
            new HybridTank[] {tank}, (index, resource) -> isSupportedFluid(resource.getFluid()),
            (index, resource) -> isSupportedFluid(resource.getFluid()), this::setChanged);

    @Override
    public ResourceHandler<FluidResource> getFluidHandler(Direction side) {
        if (side == null) return fluidHandler;
        return new FilteredFluidResourceHandler(fluidHandler, index -> true,
                (index, resource) -> this.canFill(side, resource.getFluid()),
                (index, resource) -> side.getAxis().isVertical()
                        && (this.getAdjacentBlockEntity(side) instanceof BlockEntityEngine
                        || this.getAdjacentBlockEntity(side) instanceof TileEntityFuelEngine));
    }

    private static boolean isSupportedFluid(Fluid fluid) {
        return fluid == RotaryFluids.JET_FUEL.get() || fluid == RotaryFluids.ETHANOL.get()
                || BlockEntityEngine.isAirFluid(fluid) || TileEntityFuelEngine.isValidFuel(fluid);
    }

    public BlockEntityEngineController(BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
        super(reika.rotarycraft.registry.RotaryBlockEntities.ECU.get(), pos, state);
    }

    public boolean redstoneMode;
    private int redstoneTick = 0;
    private int prevRedstone;

    private EngineSettings setting = EngineSettings.FULL;

    public BlockEntityEngineController(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static EngineSettings[] getSettingList() {
        EngineSettings[] arr = new EngineSettings[EngineSettings.list.length];
        System.arraycopy(EngineSettings.list, 0, arr, 0, arr.length);
        return arr;
    }

    public static String getSettingsAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < EngineSettings.list.length; i++) {
            EngineSettings set = EngineSettings.list[i];
            sb.append(String.format("%s: %.2f%% Speed, %dx Fuel Efficiency", ReikaStringParser.capFirstChar(set.name()), set.getSpeedDecimal(), set.getEfficiencyFactor()));
            if (i < EngineSettings.list.length - 1)
                sb.append("\n");
        }
        return sb.toString();
    }

    public boolean consumeFuel() {
        return setting.fuelFactor != 0;
    }

    public boolean canProducePower() {
        return setting.speedFactor != 0;
    }

    public boolean playSound() {
        return this.canProducePower();
    }

    public float getSpeedMultiplier() {
        if (this.canProducePower())
            return 1F / setting.speedFactor;
        return 0;
    }

    public int getSpeedFactor() {
        if (this.canProducePower())
            return setting.speedFactor;
        return 0;
    }

    public int getFuelMultiplier(EngineType.EngineClass e) {
        int base = setting.fuelFactor;
        if (e == EngineType.EngineClass.TURBINE)
            base /= 8;
        return Math.max(1, base);
    }

    public float getSoundStretch() {
        switch (setting) {
            case FULL:
                return 1F;
            case LOW:
                return 0.6F;
            case MEDIUM:
                return 0.8F;
            case SHUTDOWN:
                return 0F;
            case STANDBY:
                return 0.4F;
            default:
                return 1F;
        }
    }

    /** Display name of the current throttle setting for the right-click feedback. */
    public String getSettingName() {
        return setting.name().charAt(0) + setting.name().substring(1).toLowerCase(java.util.Locale.ENGLISH)
                + " (" + (int) setting.getSpeedDecimal() + "% speed)";
    }

    public void increment() {
        int l = EngineSettings.list.length;
        int o = setting.ordinal();
        o++;
        if (o >= l)
            o = 0;
        setting = EngineSettings.list[o];
    }

    public void setSetting(int ordinal) {
        int o = Math.max(0, Math.min(ordinal, EngineSettings.list.length - 1));
        setting = EngineSettings.list[o];
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateEntity();
        if (redstoneTick > 0)
            redstoneTick--;
        int power = redstoneTick == 0 ? world.getBestNeighborSignal(pos) : prevRedstone;
        if (prevRedstone != power)
            redstoneTick = 60;
        prevRedstone = power;
        //ReikaJavaLibrary.pConsole(prevRedstone+":"+this.canProducePower(), Dist.DEDICATED_SERVER);

        if (redstoneMode) {
            setting = power == 15 ? EngineSettings.SHUTDOWN : EngineSettings.list[4 - power / 3];
        }
        //ReikaJavaLibrary.pConsole(tank);
        if (tank.isEmpty())
            return;

        if (world.getBlockEntity(pos.above()) instanceof BlockEntityEngine engUp)
            if (this.transferToEngine(engUp, false))
                return;
        if (world.getBlockEntity(pos.above()) instanceof TileEntityFuelEngine engineUp && transferToFuelEngine(engineUp, false)) return;

        if (world.getBlockEntity(pos.below()) instanceof BlockEntityEngine engDown)
            if (this.transferToEngine(engDown, true))
                return;
        if (world.getBlockEntity(pos.below()) instanceof TileEntityFuelEngine engineDown && transferToFuelEngine(engineDown, true)) return;
    }

    private boolean transferToFuelEngine(TileEntityFuelEngine engine, boolean flip) {
        if (engine.isFlipped != flip || tank.isEmpty() || !TileEntityFuelEngine.isValidFuel(tank.getActualFluid().getFluid())) return false;
        Direction side = flip ? Direction.DOWN : Direction.UP;
        var target = level.getCapability(Capabilities.Fluid.BLOCK, worldPosition.relative(side), side.getOpposite());
        if (target == null) return false;
        var resource = FluidResource.of(tank.getFluid());
        return ResourceHandlerUtil.move(fluidHandler, target, resource::equals, tank.getFluidLevel() / 4 + 1, null) > 0;
    }

    private boolean transferToEngine(BlockEntityEngine te, boolean flip) {
        if (te.isFlipped != flip)
            return false;
        FluidStack liq = tank.getFluid();
        if (liq.isEmpty())
            return false;
        Direction side = flip ? Direction.DOWN : Direction.UP;
        ResourceHandler<FluidResource> target = level.getCapability(Capabilities.Fluid.BLOCK,
                worldPosition.relative(side), side.getOpposite());
        if (target == null) return false;
        if (BlockEntityEngine.isAirFluid(liq.getFluid())) {
            FluidResource resource = FluidResource.of(liq);
            return ResourceHandlerUtil.move(fluidHandler, target, resource::equals,
                    liq.getAmount() / 4 + 1, null) > 0;
        } else {
            Fluid f = te.getEngineType().getFuelType();
            if (f == null || !f.isSame(liq.getFluid()))
                return false;
            if (te.getFuelLevel() + liq.getAmount() > BlockEntityEngine.FUELCAP)
                return false;
            int amt = liq.getAmount() / 4 + 1;
            FluidResource resource = FluidResource.of(liq);
            return ResourceHandlerUtil.move(fluidHandler, target, resource::equals, amt, null) > 0;
        }
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }


    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        tank.writeToNBT(NBT);

        NBT.putInt("lvl", setting.ordinal());

        NBT.putBoolean("redstone", redstoneMode);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        tank.readFromNBT(NBT);

        setting = EngineSettings.list[NBT.getIntOr("lvl", 0)];

        redstoneMode = NBT.getBooleanOr("redstone", false);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.ECU;
    }

    @Override
    protected String getTEName() {
        return "ecu";
    }

    @Override
    public net.minecraft.world.level.block.Block getBlockEntityBlockID() {
        return reika.rotarycraft.registry.RotaryBlocks.ECU.get();
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return true;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m == MachineRegistry.FUELLINE || m == MachineRegistry.BEDPIPE;
    }

    @Override
    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return true;
    }




    public boolean canFill(Direction from, Fluid fluid) {
        //if (fluid.equals(Fluids.LAVA)) Why was THIS here???
        //	return true;
        BlockEntity te = getAdjacentBlockEntity(from);
        if (te instanceof BlockEntityEngine) {
            BlockEntityEngine eng = (BlockEntityEngine) te;
            return eng.getEngineType() != EngineType.STEAM && eng.getEngineType().burnsFuel() && fluid.isSame(eng.getEngineType().getFuelType());
        } else if (te instanceof TileEntityFuelEngine) {
            return TileEntityFuelEngine.isValidFuel(fluid);
        }
        if (fluid.isSame(RotaryFluids.JET_FUEL.get()))
            return true;
        if (fluid.isSame(RotaryFluids.ETHANOL.get()))
            return true;
        if (TileEntityFuelEngine.isValidFuel(fluid)) return true;
        if (fluid.isSame(RotaryFluids.OXYGEN.get()))
            return true;
        return false;//fluid.equals(Fluids.getFluid("oxygen"));
    }



//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from) {
//        return new FluidTankInfo[]{tank.getInfo()};
//    }

    @Override
    public Flow getFlowForSide(Direction side) {
        return Flow.DUAL;
    }


    




    





    private enum EngineSettings {
        SHUTDOWN(0, 0),
        STANDBY(16, 64),
        LOW(4, 8),
        MEDIUM(2, 2),
        FULL(1, 1);

        public static final EngineSettings[] list = values();
        public final int speedFactor;
        public final int fuelFactor;

        EngineSettings(int speed, int fuel) {
            speedFactor = speed;
            fuelFactor = fuel;
        }

        public double getSpeedDecimal() {
            if (this == SHUTDOWN)
                return 0;
            return 100D / speedFactor;
        }

        public int getEfficiencyFactor() {
            if (this == SHUTDOWN)
                return 0;
            return fuelFactor / speedFactor;
        }
    }
}
