/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.piping;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import reika.rotarycraft.auxiliary.interfaces.PumpablePipe;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryBlocks;

public class BlockEntityFuelLine extends BlockEntityPiping implements PumpablePipe {

    private int fuel = 0;
    private Fluid fluid;

    public BlockEntityFuelLine(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.FUEL_LINE.get(), pos, state);
    }

    private boolean isAcceptableFuel(Fluid f) {
        if (f == null) return false;
        if (f.isSame(RotaryFluids.JET_FUEL.get()) || f.isSame(RotaryFluids.ETHANOL.get()))
            return true;
        Identifier id = BuiltInRegistries.FLUID.getKey(f);
        if (id == null) return false;
        String path = id.getPath();
        if (id.getNamespace().equals("reactorcraft"))
            return path.equals("lifbe_fuel") || path.equals("lifbe_fuel_preheat");
        return path.equals("bioethanol") || path.equals("ethanol") || path.equals("fuel")
                || path.equals("rocket_fuel") || path.equals("turbofuel");
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.FUELLINE;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m, Direction dir) {
        return m == MachineRegistry.FUELLINE || m == MachineRegistry.VALVE || m == MachineRegistry.SEPARATION || m == MachineRegistry.BYPASS || m == MachineRegistry.SUCTION;
    }

    @Override
    public boolean hasLiquid() {
        return fuel > 0;
    }

    @Override
    public Fluid getAttributes() {
        return fluid;
    }

    @Override
    public int getFluidLevel() {
        return fuel;
    }

    @Override
    protected void setFluidLevel(int amt) {
        fuel = amt;
    }

    @Override
    protected void setFluid(Fluid f) {
        fluid = f;
    }

    @Override
    protected boolean interactsWithMachines() {
        return true;
    }

    @Override
    protected void onIntake(BlockEntity te) {

    }

    @Override
    public boolean isValidFluid(Fluid f) {
        return this.isAcceptableFuel(f);
    }

    @Override
    public boolean canReceiveFromPipeOn(Direction side) {
        return true;
    }

    @Override
    public boolean canEmitToPipeOn(Direction side) {
        return true;
    }

    @Override
    public Block getPipeBlockType() {
        return Blocks.OBSIDIAN;
    }

    @Override
    public boolean isConnectedToNonSelf(Direction dir) {
        return super.isConnectedToNonSelf(dir);
    }

    @Override
    public boolean canIntakeFromIFluidHandler(Direction side) {
        return side.getStepY() != 0;
    }

    @Override
    public boolean canOutputToIFluidHandler(Direction side) {
        return side.getStepY() == 0;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.FUEL_LINE.get();
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected String getTEName() {
        return "fuelline";
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }
	/*
	@Override
	public boolean canTransferTo(PumpablePipe p, Direction dir) {
		if (p instanceof BlockEntityFuelLine) {
			Fluid f = ((BlockEntityFuelLine)p).fluid;
			return f != null ? f.equals(fluid) : true;
		}
		return false;
	}

	@Override
	public void transferFrom(PumpablePipe from, int amt) {
		((BlockEntityFuelLine)from).fuel -= amt;
		fluid = ((BlockEntityFuelLine)from).fluid;
		fuel += amt;
	}
	 */
}
