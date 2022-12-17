///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface;
//
//import java.util.Collection;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.block.entity.BlockEntity;//import net.minecraft.world.World;
//import net.minecraft.world.level.Explosion;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.level.material.Fluids;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.common.util.Direction;
//import net.minecraftforge.fluids.Fluid;
//import net.minecraftforge.fluids.FluidRegistry;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.fluids.FluidTankInfo;
//import net.minecraftforge.fluids.IFluidHandler;
//
//import reika.dragonapi.asm.APIStripper.Strippable;
//import reika.dragonapi.instantiable.HybridTank;
//import reika.dragonapi.instantiable.StepTimer;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaParticleHelper;
//import reika.dragonapi.libraries.World.ReikaWorldHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaParticleHelper;
//import reika.dragonapi.modinteract.AtmosphereHandler;
//import net.minecraftforge.fluids.capability.IFluidHandler;
//import reika.dragonapi.instantiable.HybridTank;
//import reika.dragonapi.instantiable.StepTimer;
//import reika.rotarycraft.api.Power.PowerGenerator;
//import reika.rotarycraft.api.Power.ShaftMerger;
//import reika.rotarycraft.auxiliary.PowerSourceList;
//import reika.rotarycraft.auxiliary.RotaryAux;
//import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
//import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
//import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
//import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
//import reika.rotarycraft.api.power.ShaftMerger;
//import reika.rotarycraft.auxiliary.PowerSourceList;
//import reika.rotarycraft.auxiliary.RotaryAux;
//import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
//import reika.rotarycraft.base.blockentity.BlockEntityPiping;
//import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;
//import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
//import reika.rotarycraft.registry.EngineType.EngineClass;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryFluids;
//import reika.rotarycraft.registry.SoundRegistry;
//import reika.rotarycraft.blockentities.Auxiliary.BlockEntityEngineController;
//
//import buildcraft.api.transport.IPipeConnection;
//import buildcraft.api.transport.IPipeTile.PipeType;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//import reika.rotarycraft.api.power.PowerGenerator;
//import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
//import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
//import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
//import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;
//import reika.rotarycraft.registry.MachineRegistry;
//
//@Strippable(value = {"buildcraft.api.transport.IPipeConnection"})
//public class BlockEntityFuelEngine extends BlockEntityIOMachine implements IFluidHandler, PipeConnector, SimpleProvider, PowerGenerator, IPipeConnection, TemperatureTE {
//
//	public static final int GEN_OMEGA = 256;
//	public static final int GEN_TORQUE = 2048;
//
//	private int temperature;
//
//	public static final int CAPACITY = 24000;
//	public static final int MAXTEMP = 750;
//
//	private final HybridTank tank = new HybridTank("fuelengine", CAPACITY);
//	private final HybridTank watertank = new HybridTank("waterfuelengine", CAPACITY);
//	private final HybridTank lubetank = new HybridTank("lubefuelengine", CAPACITY);
//
//	private StepTimer fuelTimer = new StepTimer(36); //30 min a bucket
//	private StepTimer soundTick = new StepTimer(40);
//	private StepTimer tempTimer = new StepTimer(20);
//
//	private boolean canEmitPower(Level world, int x, int y, int z) {
//		if (tank.isEmpty())
//			return false;
//		if (AtmosphereHandler.isNoAtmo(world, x, y+1, z, blockType, true))
//			return false;
//		if (lubetank.isEmpty())
//			return false;
//		if (this.hasECU()) {
//			BlockEntityEngineController te = this.getECU();
//			return te.canProducePower();
//		}
//		return true;
//	}
//
//	private boolean hasECU() {
//		return this.getMachine(isFlipped ? Direction.UP : Direction.DOWN) == MachineRegistry.ECU;
//	}
//
//	private BlockEntityEngineController getECU() {
//		return (BlockEntityEngineController)this.getAdjacentTileEntity(isFlipped ? Direction.UP : Direction.DOWN);
//	}
//	private void updateSpeed(int maxspeed, boolean revup) {
//		if (revup) {
//			if (omega < maxspeed) {
//				//ReikaJavaLibrary.pConsole(omega+"->"+(omega+2*(int)(ReikaMathLibrary.logbase(maxspeed, 2))), Dist.DEDICATED_SERVER);
//				omega += 4*(int) ReikaMathLibrary.logbase(maxspeed, 2);
//				tank.removeLiquid(1); //more fuel burn while spinning up
//				if (omega > maxspeed)
//					omega = maxspeed;
//			}
//		}
//		else {
//			if (omega > 0) {
//				//ReikaJavaLibrary.pConsole(omega+"->"+(omega-omega/128-1), Dist.DEDICATED_SERVER);
//				omega -= omega/256+1;
//				//soundtick = 2000;
//			}
//		}
//	}
//
//	private int getFuelDuration(Level world, BlockPos pos) {
//		if (this.hasECU()) {
//			BlockEntityEngineController te = this.getECU();
//			return 36*te.getFuelMultiplier(EngineClass.PISTON);
//		}
//		return 36;
//	}
//
//	@Override
//	public void updateEntity(Level world, BlockPos pos) {
//		super.updateEntity();
//		this.getIOSides(world, pos);
//		fuelTimer.setCap(this.getFuelDuration(world, pos));
//		int genomega = this.getGenOmega();
//		tempTimer.update();
//		if (tempTimer.checkCap()) {
//			this.updateTemperature(world, pos);
//		}
//		if (this.canEmitPower(world, x, y, z)) {
//			fuelTimer.update();
//			if (fuelTimer.checkCap()) {
//				tank.removeLiquid(this.getConsumedFuel());
//			}
//			torque = GEN_TORQUE;
//			if (this.hasECU()) {
//				BlockEntityEngineController te = this.getECU();
//				genomega *= te.getSpeedMultiplier();
//			}
//		}
//		else {
//			genomega = 0;
//			if (omega == 0) {
//				torque = 0;
//			}
//		}
//		this.updateSpeed(genomega, genomega >= omega);
//		power = omega*torque;
//		soundTick.update();
//		if (power > 0) {
//			this.makeSmoke(world, pos);
//			if (soundTick.checkCap()) {
//				SoundRegistry.DIESEL.playSoundAtBlock(world, pos, RotaryAux.isMuffled(this) ? 0.3F : 1F, 0.4F);
//			}
//			if (world.getGameTime()%32 == 0)
//				lubetank.removeLiquid(1);
//		}
//	}
//
//	private int getConsumedFuel() {
//		return 4; //was 2
//	}
//
//	private int getGenOmega() {
//		return temperature <= 450 ? GEN_OMEGA : Math.max(16, GEN_OMEGA+450-temperature);
//	}
//
//	private void makeSmoke(Level world, int x, int y, int z, int meta) {
//		if (isFlipped)
//			y -= 0.5;
//		switch(meta) {
//			case 0:
//				ReikaParticleHelper.SMOKE.spawnAt(world, x+0.6875, y+0.9375, z+0.0625);
//				ReikaParticleHelper.SMOKE.spawnAt(world, x+0.6875, y+0.9375, z+0.9375);
//				break;
//			case 1:
//				ReikaParticleHelper.SMOKE.spawnAt(world, x+0.3175, y+0.9375, z+0.0625);
//				ReikaParticleHelper.SMOKE.spawnAt(world, x+0.3175, y+0.9375, z+0.9375);
//				break;
//			case 2:
//				ReikaParticleHelper.SMOKE.spawnAt(world, x+0.0625, y+0.9375, z+0.6875);
//				ReikaParticleHelper.SMOKE.spawnAt(world, x+0.9375, y+0.9375, z+0.6875);
//				break;
//			case 3:
//				ReikaParticleHelper.SMOKE.spawnAt(world, x+0.0625, y+0.9375, z+0.3175);
//				ReikaParticleHelper.SMOKE.spawnAt(world, x+0.9375, y+0.9375, z+0.3175);
//				break;
//		}
//	}
//
//	private void getIOSides(Level world, int x, int y, int z, int meta) {
//		switch(meta) {
//			case 0:
//				write = Direction.WEST;
//				break;
//			case 1:
//				write = Direction.EAST;
//				break;
//			case 2:
//				write = Direction.NORTH;
//				break;
//			case 3:
//				write = Direction.SOUTH;
//				break;
//		}
//	}
//
//	@Override
//	public boolean canProvidePower() {
//		return !tank.isEmpty();
//	}
//
//	@Override
//	public PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
//		return new PowerSourceList().addSource(this);
//	}
//
//	@Override
//	protected void animateWithTick(Level world, BlockPos pos) {
//		if (!this.isInWorld()) {
//			phi = 0;
//			return;
//		}
//		phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega+1, 2), 1.05);
//	}
//
//	@Override
//	public MachineRegistry getMachine() {
//		return MachineRegistry.FUELENGINE;
//	}
//
//	@Override
//	public boolean hasModelTransparency() {
//		return false;
//	}
//
//	@Override
//	public int getRedstoneOverride() {
//		return 15*tank.getLevel()/tank.getCapacity();
//	}
//
//	@Override
//	public int fill(Direction from, FluidStack resource, FluidAction doFill) {
//		Fluid f = resource.getFluid();
//		if (this.canFill(from, f)) {
//			if (f.equals(RotaryFluids.LUBRICANT.get()))
//				return lubetank.fill(resource, doFill);
//			else if (f.equals(FluidRegistry.getFluid("fuel")))
//				return tank.fill(resource, doFill);
//			else if (f.equals(Fluids.WATER))
//				return watertank.fill(resource, doFill);
//		}
//		return 0;
//	}
//
//	@Override
//	public FluidStack drain(Direction from, FluidStack resource, boolean doDrain) {
//		return null;
//	}
//
//	@Override
//	public FluidStack drain(Direction from, int maxDrain, boolean doDrain) {
//		return null;
//	}
//
//	@Override
//	public boolean canFill(Direction from, Fluid f) {
//		return switch (from) {
//			case UP -> isFlipped && f.equals(FluidRegistry.getFluid("fuel"));
//			case DOWN -> !isFlipped && f.equals(FluidRegistry.getFluid("fuel"));
//			case EAST, NORTH, SOUTH, WEST -> f.equals(RotaryFluids.LUBRICANT.get()) || f.equals(Fluids.WATER);
//			default -> false;
//		};
//	}
//
//	@Override
//	public boolean canDrain(Direction from, Fluid fluid) {
//		return false;
//	}
//
//	@Override
//	public FluidTankInfo[] getTankInfo(Direction from) {
//		return new FluidTankInfo[]{tank.getInfo(), watertank.getInfo(), lubetank.getInfo()};
//	}
//
//	@Override
//	public long getMaxPower() {
//		return power;
//	}
//
//	@Override
//	public long getCurrentPower() {
//		return power;
//	}
//
//	@Override
//	protected void writeSyncTag(CompoundTag NBT)
//	{
//		super.writeSyncTag(NBT);
//
//		tank.saveAdditional(NBT);
//		watertank.saveAdditional(NBT);
//		lubetank.saveAdditional(NBT);
//
//		NBT.putInt("temp", temperature);
//	}
//
//	@Override
//	protected void readSyncTag(CompoundTag NBT)
//	{
//		super.readSyncTag(NBT);
//
//		tank.load(NBT);
//		watertank.load(NBT);
//		lubetank.load(NBT);
//
//		temperature = NBT.getInt("temp");
//	}
//
//	@Override
//	public ConnectOverride overridePipeConnection(PipeType type, Direction with) {
//		return type == PipeType.FLUID && with != Direction.DOWN ? ConnectOverride.CONNECT : ConnectOverride.DEFAULT;
//	}
//
//	public int getFuelLevel() {
//		return tank.getLevel();
//	}
//
//	public int getWaterLevel() {
//		return watertank.getLevel();
//	}
//
//	public int getLubeLevel() {
//		return lubetank.getLevel();
//	}
//
//	public void addFuel(int amt) {
//		tank.addLiquid(amt, FluidRegistry.getFluid("fuel"));
//	}
//
//	public void removeFuel(int amt) {
//		tank.removeLiquid(amt);
//	}
//
//	public void addWater(int amt) {
//		watertank.addLiquid(amt, Fluids.WATER);
//	}
//
//	public void addLube(int amt) {
//		lubetank.addLiquid(amt, RotaryFluids.LUBRICANT.get());
//	}
//
//	@Override
//	public int getEmittingX() {
//		return xCoord+write.offsetX;
//	}
//
//	@Override
//	public int getEmittingY() {
//		return yCoord+write.offsetY;
//	}
//
//	@Override
//	public int getEmittingZ() {
//		return zCoord+write.offsetZ;
//	}
//
//	@Override
//	public void updateTemperature(Level world, BlockPos pos) {
//		int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
//		int dT = temperature-Tamb;
//
//		if (dT > 0) {
//			temperature--;
//			int c = (temperature-Tamb)/100;
//			if (!watertank.isEmpty()) {
//				temperature -= c;
//				watertank.removeLiquid(20);
//			}
//		}
//
//		if (power > 0) {
//			temperature += 5;
//		}
//
//		if (temperature > MAXTEMP)
//			this.overheat(world, pos);
//	}
//
//	@Override
//	public void addTemperature(int temp) {
//		temperature += temp;
//	}
//
//	@Override
//	public int getTemperature() {
//		return temperature;
//	}
//
//	@Override
//	public int getThermalDamage() {
//		return 0;
//	}
//
//	@Override
//	public void overheat(Level world, int x, int y, int z) {
//		world.setBlockToAir(x, y, z);
//		if (!world.isClientSide()) {
//			world.explode(null, x+0.5, y+0.5, z+0.5, 4, true, Level.ExplosionInteraction.BLOCK);
//			world.explode(null, x+0.5, y+0.5, z+0.5, 8, true, Level.ExplosionInteraction.BLOCK);
//		}
//	}
//
//	@Override
//	public boolean canBeCooledWithFins() {
//		return false;
//	}
//
//	@Override
//	public boolean allowHeatExtraction() {
//		return false;
//	}
//
//	@Override
//	public boolean allowExternalHeating() {
//		return false;
//	}
//
//	public void setTemperature(int temp) {
//		temperature = temp;
//	}
//
//	@Override
//	public void getAllOutputs(Collection<BlockEntity> c, Direction dir) {
//		c.add(this.getAdjacentBlockEntity(write));
//	}
//
//	@SideOnly(Dist.CLIENT)
//	public int getFuelScaled(int a) {
//		return tank.getLevel() * a / tank.getCapacity();
//	}
//
//	@SideOnly(Dist.CLIENT)
//	public int getWaterScaled(int a) {
//		return watertank.getLevel() * a / watertank.getCapacity();
//	}
//
//	@SideOnly(Dist.CLIENT)
//	public int getLubricantScaled(int a) {
//		return lubetank.getLevel() * a / lubetank.getCapacity();
//	}
//
//	@SideOnly(Dist.CLIENT)
//	public int getTemperatureScaled(int a) {
//		return temperature * a / MAXTEMP;
//	}
//
//	public int getFuelDuration() {
//		return tank.getLevel()*fuelTimer.getCap()/this.getConsumedFuel()/20;
//	}
//
//	@Override
//	public int getMaxTemperature() {
//		return MAXTEMP;
//	}
//
//	@Override
//	public final boolean canConnectToPipe(MachineRegistry m) {
//		return m.isStandardPipe() || m == MachineRegistry.HOSE || m == MachineRegistry.FUELLINE;
//	}
//
//	@Override
//	public final boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
//		return this.canConnectToPipe(p) && this.getFlowForSide(side) == BlockEntityPiping.Flow.INPUT;
//	}
//
//	@Override
//	public BlockEntityPiping.Flow getFlowForSide(Direction side) {
//		return side == (isFlipped ? Direction.DOWN : Direction.UP) ? BlockEntityPiping.Flow.NONE : BlockEntityPiping.Flow.INPUT;
//	}
//
//}
