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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.api.power.PowerGenerator;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.base.blockentity.EnergyToPowerBase;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.piping.BlockEntityPipe;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryFluids;

//@Strippable(value = {"buildcraft.api.transport.IPipeConnection"})
public class BlockEntitySteam extends EnergyToPowerBase implements PowerGenerator, SimpleProvider, /*IPipeConnection,*/ PipeConnector {

	public static final int CAPACITY = 300000;

	public BlockEntitySteam(BlockPos pos, BlockState state) {
		super(RotaryBlockEntities.STEAM_TURBINE.get(), pos, state);
	}

	//private final HybridTank steam = new HybridTank("steamturb", CAPACITY);

	@Override
	protected double getRelativeEfficiency() {
		return 0.5;
	}

	@Override
	protected void animateWithTick(Level world, BlockPos pos) {
		if (!this.isInWorld()) {
			phi = 0;
			return;
		}
		phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega+1, 4), 1.0);
	}

	@Override
	public MachineRegistry getMachine() {
		return MachineRegistry.STEAMTURBINE;
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
	protected String getTEName() {
		return null;
	}

	@Override
	public Block getBlockEntityBlockID() {
		return null;
	}

	@Override
	public void updateEntity(Level world, BlockPos pos) {
		this.getIOSides(world, pos, world.getBlockState(pos).getValue(BlockRotaryCraftMachine.FACING));
		write = this.getFacing().getOpposite();

		if (this.getTicksExisted() == 0)
			ReikaWorldHelper.causeAdjacentUpdates(world, pos);

		this.updateSpeed();
		this.basicPowerReceiver();
	}

	private void getSteam(Level world, BlockPos pos) {
		int drain = 25;
		if (storedEnergy <= this.getMaxStorage()-drain) {
			BlockEntity te = world.getBlockEntity(pos);
			if (te instanceof IFluidHandler ic) {
				FluidStack liq = ic.drain(drain, FluidAction.EXECUTE);
				if (liq != null && liq.getAmount() > 0 && liq.getFluid().equals(Fluids.WATER))
					//steam.addLiquid(liq.amount, FluidRegistry.getFluid("steam"));
					this.addEnergy(liq.getAmount(), FluidAction.EXECUTE);
			}
		}
	}

	private int addEnergy(int amount, FluidAction doAdd) {
		int max = this.getMaxStorage()-storedEnergy;
		int add = Math.min(max, amount);
		if (doAdd.execute())
			storedEnergy += add;
		return add;
	}

//	@Override
//	public ConnectOverride overridePipeConnection(PipeType type, Direction dir) {
//		return dir == this.getFacing().getOpposite() && type == PipeType.FLUID ? ConnectOverride.CONNECT : ConnectOverride.DISCONNECT;
//	}

	@Override
	public BlockPos getEmittingPos(BlockPos pos) {
		return new BlockPos(worldPosition.getX()+write.getStepX(), worldPosition.getY()+write.getStepY(), worldPosition.getZ()+write.getStepZ());
	}

	@Override
	public boolean canConnectToPipe(MachineRegistry m) {
		return m.isStandardPipe() || super.canConnectToPipe(m);
	}

	@Override
	public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
		return this.canConnectToPipe(p) && side == this.getFacing() || super.canConnectToPipeOnSide(p, side);
	}

	@Override
	public BlockEntityPiping.Flow getFlowForSide(Direction side) {
		return side == this.getFacing() ? BlockEntityPiping.Flow.INPUT : super.getFlowForSide(side);
	}

	@Override
	public int fillPipe(Direction from, FluidStack resource, FluidAction doFill) {
		if (super.canFill(from, resource.getFluid()))
			return super.fillPipe(from, resource, doFill);
		return this.canFill(from, resource.getFluid()) ? this.addEnergy(resource.getAmount(), doFill) : 0;
	}

	@Override
	public FluidStack drainPipe(Direction from, int maxDrain, FluidAction doDrain) {
		return null;
	}
	@Override
	public boolean canFill(Direction from, Fluid fluid) {
		return super.canFill(from, fluid) || from == this.getFacing() && fluid.equals(RotaryFluids.STEAM.get());
	}

	@Override
	public boolean isValidSupplier(BlockEntity te) {
		return te instanceof IFluidHandler || te instanceof BlockEntityPipe;
	}

	@Override
	public int getMaxStorage() {
		return CAPACITY;
	}

	@Override
	protected int getIdealConsumedUnitsPerTick() {
		return Mth.ceil(Math.sqrt(power));
	}

	@Override
	public String getUnitDisplay() {
		return "mB";
	}

	@Override
	public int getPowerColor() {
		return 0xffffff;
	}

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public @NotNull FluidStack getFluidInTank(int tank) {
		return new FluidStack(RotaryFluids.STEAM.get(), 0);
	}

	@Override
	public int getTankCapacity(int tank) {
		return 24000;
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return false;
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		return 0;
	}

	@Override
	public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
		return null;
	}

	@Override
	public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
		return null;
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
	public int getAmbientTemperature() {
		return 0;
	}
}
