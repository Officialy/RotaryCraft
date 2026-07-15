/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.storage;

import java.util.ArrayList;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.libraries.ReikaNBTHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.interfaces.NBTMachine;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryFluids;

/**
 * The Gas Tank compresses a fluid into a huge internal buffer. Its usable capacity scales with the
 * input torque (roughly ten-fold per two doublings of torque) and is eight times larger for gases
 * than liquids, so a well-powered tank stores an enormous volume of gas. Fluid moves in and out
 * through the standard RotaryCraft pipe system (input from the sides, output from the top). The
 * held fluid + amount survives being broken via the item's NBT (NBTMachine). 1.7.10-faithful; the
 * legacy overpressure-explosion (from filling past capacity as torque drops) is left commented as
 * in the source.
 */
public class BlockEntityFluidCompressor extends BlockEntityPowerReceiver implements PipeConnector, NBTMachine {

    private static final ArrayList<FluidStack> creativeFluids = new ArrayList<>();

    private final HybridTank tank = new HybridTank("gastank", 1000000000);

    public static void initCreativeFluids() {
        creativeFluids.clear();
        // MOD-PORT: hydrofluoric acid / uranium hexafluoride / CO2 / deuterium / tritium are
        // ReactorCraft fluids and are added when that mod is present.
        addCreativeFluid(new FluidStack(RotaryFluids.AMMONIA.get(), -1));
        addCreativeFluid(new FluidStack(RotaryFluids.CHLORINE.get(), -1));
        addCreativeFluid(new FluidStack(RotaryFluids.OXYGEN.get(), -1));
    }

    private static void addCreativeFluid(FluidStack fluid) {
        if (fluid != null)
            creativeFluids.add(fluid);
    }

    public BlockEntityFluidCompressor(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.GASTANK.get(), pos, state);
    }

    public int getCapacity() {
        return tank.isEmpty() ? 0 : this.getCapacity(tank.getActualFluid().getFluid());
    }

    public int getCapacity(Fluid f) {
        if (power < MINPOWER || torque < MINTORQUE)
            return 0;
        int log2 = (int) (ReikaMathLibrary.logbase(torque, 2) / 2);
        long pow = ReikaMathLibrary.longpow(10, log2);
        int factor = f.getFluidType().isLighterThanAir() ? 8 : 1; //gases compress ~8x denser
        long frac = factor * (pow / 40);
        return (int) Math.min(frac, tank.getCapacity());
    }

    public FluidStack getFluid() {
        return tank.getActualFluid();
    }

    public boolean isEmpty() {
        return tank.isEmpty();
    }

    public int getFluidLevel() {
        return tank.getFluidLevel();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getPowerBelow();
        /* MOD-PORT / legacy: when the usable capacity drops below the stored amount (torque
         * falls), the tank overpressures and eventually explodes. Left commented as in source. */
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.GASTANK;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        int cap = this.getCapacity();
        return cap > 0 ? 15 * tank.getFluidLevel() / cap : 0;
    }

    @Override
    protected String getTEName() {
        return "gastank";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.GASTANK.get();
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return true;
    }

    // ==== PipeConnector: fluid I/O through RotaryCraft pipes ====

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe() || m == MachineRegistry.HOSE || m == MachineRegistry.FUELLINE || m == MachineRegistry.VALVE;
    }

    @Override
    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return this.canConnectToPipe(p);
    }

    @Override
    public Flow getFlowForSide(Direction side) {
        return side == Direction.UP ? Flow.OUTPUT : Flow.INPUT;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        if (from == Direction.UP || resource.isEmpty())
            return 0; //input from the sides only
        int toadd = Math.min(resource.getAmount(), this.getCapacity(resource.getFluid()) - tank.getFluidLevel());
        if (toadd <= 0)
            return 0;
        return tank.fill(new FluidStack(resource.getFluid(), toadd), action);
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        if (from != Direction.UP)
            return FluidStack.EMPTY; //output from the top only
        return tank.drain(maxDrain, doDrain);
    }

    // ==== persistence ====

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

    // ==== NBTMachine: the held fluid survives break/replace on the item ====

    @Override
    public CompoundTag getTagsToWriteToStack() {
        if (this.isEmpty())
            return null;
        CompoundTag NBT = new CompoundTag();
        ReikaNBTHelper.writeFluidToNBT(NBT, this.getFluid());
        NBT.putInt("lvl", this.getFluidLevel());
        return NBT;
    }

    @Override
    public void setDataFromItemStackTag(CompoundTag NBT) {
        if (NBT == null) {
            tank.empty();
            return;
        }
        FluidStack f = ReikaNBTHelper.getFluidFromNBT(NBT);
        int level = NBT.getIntOr("lvl", 0);
        if (f != null && level > 0)
            tank.setContents(level, f.getFluid());
    }

    @Override
    public ArrayList<CompoundTag> getCreativeModeVariants() {
        ArrayList<CompoundTag> li = new ArrayList<>();
        li.add(null);
        for (FluidStack creativeFluid : creativeFluids) {
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("lvl", 1000000000);
            ReikaNBTHelper.writeFluidToNBT(nbt, creativeFluid);
            li.add(nbt);
        }
        return li;
    }

    @Override
    public ArrayList<String> getDisplayTags(CompoundTag nbt) {
        ArrayList<String> li = new ArrayList<>();
        FluidStack f = ReikaNBTHelper.getFluidFromNBT(nbt);
        if (f != null) {
            int amt = nbt.getIntOr("lvl", 0);
            if (amt > 0)
                li.add("Contents: " + (amt / 1000) + "B of " + f.getHoverName().getString());
        }
        return li;
    }
}
