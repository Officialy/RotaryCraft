/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.transmission;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.interfaces.blockentity.BreakAction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import reika.rotarycraft.auxiliary.ShaftPowerBus;
import reika.rotarycraft.auxiliary.interfaces.TransmissionReceiver;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.base.blockentity.PoweredLiquidReceiver;
import reika.rotarycraft.registry.DifficultyEffects;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.Collection;

public class BlockEntityBusController extends PoweredLiquidReceiver implements TransmissionReceiver, BreakAction {

    public BlockEntityBusController(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.BUSCONTROLLER.get(), pos, state);
    }

    private ShaftPowerBus bus = new ShaftPowerBus(this);

    private final StepTimer timer = new StepTimer(100);


    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    protected String getTEName() {
        return "buscontroller";
    }

    @Override
    public net.minecraft.world.level.block.Block getBlockEntityBlockID() {
        return reika.rotarycraft.registry.RotaryBlocks.BUSCONTROLLER.get();
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
    public MachineRegistry getMachine() {
        return MachineRegistry.BUSCONTROLLER;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        // Power reads from the FACING side (legacy 4-way metadata).
        BlockState state = this.getBlockState();
        read = state.hasProperty(BlockRotaryCraftMachine.FACING) ? state.getValue(BlockRotaryCraftMachine.FACING) : Direction.EAST;
        this.getPower(false);

        timer.update();

        if (DragonAPI.debugtest)
            tank.addLiquid(5, RotaryFluids.LUBRICANT.get());

        if (tank.isEmpty()) {
            torque = 0;
            omega = 0;
        } else {
            if (power > 0 && timer.checkCap())
                tank.removeLiquid(this.getLubricantUsed());
        }

        power = (long) torque * (long) omega;
        if (tickcount % 10 == 0)
            bus.update();
        //ReikaJavaLibrary.pConsole(bus.getInputPower()+":"+bus.getTotalOutputSides(), Dist.DEDICATED_SERVER);
    }

    private int getLubricantUsed() {
        return Math.max(1, (int) (DifficultyEffects.LUBEUSAGE.getChance() * 2 * bus.getSize() + bus.getTotalOutputSides()));
    }

    public ShaftPowerBus getBus() {
        return bus;
    }

    private void clear() {
        bus.clear();
        bus = null;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        tank.readFromNBT(NBT);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        tank.writeToNBT(NBT);
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m == MachineRegistry.HOSE || m == MachineRegistry.BEDPIPE;
    }

    @Override
    public Fluid getInputFluid() {
        return RotaryFluids.LUBRICANT.get();
    }

    @Override
    public boolean canReceiveFrom(Direction from) {
        return from.getStepY() != 0;
    }

    @Override
    public int getCapacity() {
        return 8000;
    }

    // Pure consumer: lubricant only flows in.
    @Override
    public net.neoforged.neoforge.fluids.FluidStack drainPipe(Direction from, int maxDrain, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction action) {
        return net.neoforged.neoforge.fluids.FluidStack.EMPTY;
    }

    @Override
    public int fillPipe(Direction from, net.neoforged.neoforge.fluids.FluidStack resource, net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction action) {
        if (!this.canReceiveFrom(from) || !this.isValidFluid(resource.getFluid()))
            return 0;
        return tank.fill(resource, action);
    }

    @Override
    public void breakBlock() {
        this.clear();
    }

    @Override
    public void getOutputs(Collection<BlockEntity> c, Direction dir) {
        Collection<BlockEntityPowerBus> c2 = bus.getBlocks();
        for (BlockEntityPowerBus te : c2) {
            te.getAllOutputs(c, dir);
        }
    }

}
