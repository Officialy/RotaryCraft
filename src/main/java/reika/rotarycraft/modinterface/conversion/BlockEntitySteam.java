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
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.CombinedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import net.neoforged.neoforge.transfer.transaction.Transaction;

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
	private final ResourceHandler<FluidResource> steamHandler = new SteamResourceHandler();
	private final class SteamResourceHandler extends SnapshotJournal<Integer>
			implements ResourceHandler<FluidResource> {
		@Override public int size() { return 1; }
		@Override public FluidResource getResource(int index) {
			if (index != 0) throw new IndexOutOfBoundsException(index);
			return storedEnergy > 0 ? FluidResource.of(RotaryFluids.STEAM.get()) : FluidResource.EMPTY;
		}
		@Override public long getAmountAsLong(int index) {
			if (index != 0) throw new IndexOutOfBoundsException(index);
			return storedEnergy;
		}
		@Override public long getCapacityAsLong(int index, FluidResource resource) {
			if (index != 0) throw new IndexOutOfBoundsException(index);
			return resource.isEmpty() || isValid(index, resource) ? CAPACITY : 0;
		}
		@Override public boolean isValid(int index, FluidResource resource) {
			if (index != 0) throw new IndexOutOfBoundsException(index);
			return resource.equals(FluidResource.of(RotaryFluids.STEAM.get()));
		}
		@Override public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
			TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
			if (!isValid(index, resource) || amount == 0) return 0;
			int accepted = addEnergy(amount, false);
			if (accepted > 0) {
				updateSnapshots(transaction);
				addEnergy(accepted, true);
			}
			return accepted;
		}
		@Override public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
			TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
			if (index != 0) throw new IndexOutOfBoundsException(index);
			return 0;
		}
		@Override protected Integer createSnapshot() { return storedEnergy; }
		@Override protected void revertToSnapshot(Integer snapshot) { storedEnergy = snapshot; }
		@Override protected void onRootCommit(Integer originalState) { setChanged(); }
	}
	private final ResourceHandler<FluidResource> combinedFluidHandler =
			new CombinedResourceHandler<>(super.getFluidHandler(null), steamHandler);

	@Override
	public ResourceHandler<FluidResource> getFluidHandler(Direction side) {
		return side == null || side == this.getBlockState().getValue(BlockRotaryCraftMachine.FACING)
				? combinedFluidHandler : super.getFluidHandler(side);
	}

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
	    super.updateBlockEntity();
		this.getIOSides(world, pos, world.getBlockState(pos).getValue(BlockRotaryCraftMachine.FACING));
		write = this.getFacing().getOpposite();

		if (this.getTicksExisted() == 0)
			ReikaWorldHelper.causeAdjacentUpdates(world, pos);

		this.updateSpeed();
		this.basicPowerReceiver();
	}

	private void getSteam(Level world, BlockPos pos) {
		if (world.isClientSide() || storedEnergy >= this.getMaxStorage()) return;
		Direction inlet = this.getBlockState().getValue(BlockRotaryCraftMachine.FACING);
		var source = world.getCapability(Capabilities.Fluid.BLOCK, pos.relative(inlet), inlet.getOpposite());
		if (source == null) return;
		try (Transaction transaction = Transaction.openRoot()) {
			var moved = ResourceHandlerUtil.moveFirst(source, steamHandler,
					resource -> resource.equals(FluidResource.of(RotaryFluids.STEAM.get())),
					25, transaction);
			if (moved != null) transaction.commit();
		}
	}

	private int addEnergy(int amount, boolean doAdd) {
		int max = this.getMaxStorage()-storedEnergy;
		int add = Math.min(max, amount);
		if (doAdd)
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
	public boolean canFill(Direction from, Fluid fluid) {
		return super.canFill(from, fluid) || from == this.getFacing() && fluid.equals(RotaryFluids.STEAM.get());
	}

	@Override
	public boolean isValidSupplier(BlockEntity te) {
		return te != null && level != null && level.getCapability(Capabilities.Fluid.BLOCK,
				te.getBlockPos(), this.getBlockState().getValue(BlockRotaryCraftMachine.FACING).getOpposite()) != null;
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
