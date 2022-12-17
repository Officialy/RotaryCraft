///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface.conversion;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;import net.minecraft.util.Mth;
//import net.minecraft.world.World;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.common.util.Direction;
//
//import reika.dragonapi.ModList;
//import reika.dragonapi.asm.APIStripper.Strippable;
//import reika.dragonapi.asm.DependentMethodStripper.ModDependent;
//import reika.dragonapi.libraries.io.ReikaSoundHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaParticleHelper;
//import reika.dragonapi.libraries.rendering.ReikaColorAPI;
//import reika.dragonapi.modinteract.ItemHandlers.IC2Handler;
//import reika.dragonapi.modinteract.Power.ReikaEUHelper;
//import reika.dragonapi.modinteract.power.ReikaEUHelper;
//import reika.rotarycraft.api.Power.PowerGenerator;
//import reika.rotarycraft.api.power.PowerGenerator;
//import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
//import reika.rotarycraft.base.blockentity.EnergyToPowerBase;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.SoundRegistry;
//
//import ic2.api.energy.event.EnergyTileLoadEvent;
//import ic2.api.energy.event.EnergyTileUnloadEvent;
//import ic2.api.energy.tile.IEnergyConductor;
//import ic2.api.energy.tile.IEnergySink;
//import ic2.api.energy.tile.IEnergySource;
//import ic2.api.tile.IEnergyStorage;
//
////@Strippable(value = {"universalelectricity.api.electricity.IVoltageInput", "universalelectricity.api.energy.IEnergyInterface"})
//@Strippable(value = {"ic2.api.energy.tile.IEnergySink"})
//public class TileEntityElectricMotor extends EnergyToPowerBase implements PowerGenerator, SimpleProvider, IEnergySink {
//
//	public static int CAPACITY = 90000;
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
//		return MachineRegistry.ELECTRICMOTOR;
//	}
//
//	@Override
//	public boolean hasModelTransparency() {
//		return false;
//	}
//
//	@Override
//	public int getRedstoneOverride() {
//		return 0;
//	}
//
//	@Override
//	public void updateEntity(Level world, BlockPos pos) {
//		super.updateEntity();
//		tickcount++;
//		this.getIOSides(world, pos);
//		read = this.getFacing();
//		write = read.getOpposite();
//
//		this.updateSpeed();
//		if (!world.isClientSide()) {
//			tickcount++;
//			if (power > 0) {
//				if (tickcount >= 294) {
//					tickcount = 0;
//					SoundRegistry.ELECTRIC.playSoundAtBlock(world, pos, this.isMuffled() ? 0.08F : 0.2F, 0.51F);
//				}
//			}
//		}
//
//		this.basicPowerReceiver();
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
//	public int getMaxStorage() {
//		return CAPACITY;
//	}
//
//	@Override
//	protected int getIdealConsumedUnitsPerTick() {
//		return Mth.ceil(power/ ReikaEUHelper.getWattsPerEU());
//	}
//
//	@Override
//	public String getUnitDisplay() {
//		return "EU";
//	}
//
//	@Override
//	public int getPowerColor() {
//		return ReikaColorAPI.RGBtoHex(255, 220, 0);
//	}
//
//	@Override
//	public boolean acceptsEnergyFrom(BlockEntity emitter, Direction dir) {
//		return (dir == this.getFacing() || dir == Direction.DOWN) && this.isValidSupplier(emitter);
//	}
//
//	@Override
//	public double getDemandedEnergy() {
//		return this.getMaxStorage()-this.getStoredPower();
//	}
//
//	@Override
//	public int getSinkTier() {
//		//ReikaJavaLibrary.pConsole(ReikaEUHelper.getIC2TierFromPower(this.getTierPower(this.getTier())));
//		return IC2Handler.getInstance().isIC2Classic() ? this.getScaledTier() : 5;//this.getScaledTier();
//	}
//
//	private int getScaledTier() {
//		return 1+ReikaEUHelper.getIC2TierFromPower(this.getTierPower());
//	}
//
//	@Override
//	public double injectEnergy(Direction directionFrom, double amount, double voltage) {
//		int tier = ReikaEUHelper.getIC2TierFromEUVoltage(voltage);
//		int tier1 = this.getScaledTier();
//		//if (tier != tier1) {
//		//	this.onWrongVoltage(tier, tier1);
//		//	//ReikaJavaLibrary.pConsole(tier+":"+tier1);
//		//	return 0;
//		//}
//		double add = Math.min(amount, this.getMaxStorage()-storedEnergy);
//		storedEnergy += add;
//		return amount-add;
//	}
//
//	private void onWrongVoltage(int tier, int correct) {
//		int over = tier-correct;
//		if (over > 2 && TileEntityGenerator.hardModeEU) {
//			level.explode(null, xCoord+0.5, yCoord+0.5, zCoord+0.5, 9F, true, true);
//		}
//		else if (over > 1) {
//			ReikaParticleHelper.SMOKE.spawnAroundBlock(level, worldPosition, 12);
//			ReikaSoundHelper.playSoundAtBlock(level, worldPosition, "random.fizz");
//		}
//		else if (over == 1) {
//			if (rand.nextInt(5) == 0) {
//				ReikaParticleHelper.SMOKE.spawnAroundBlock(level, worldPosition, 12);
//				ReikaSoundHelper.playSoundAtBlock(level, worldPosition, "random.fizz");
//			}
//			if (rand.nextInt(15) == 0)
//				SoundRegistry.ELECTRIC.playSoundAtBlock(level, worldPosition, 0.2F, 2F);
//		}
//	}
//
//	@Override
//	@ModDependent(ModList.IC2)
//	public boolean isValidSupplier(BlockEntity te) {
//		return te instanceof IEnergySource || te instanceof IEnergyConductor || te instanceof IEnergyStorage;
//	}
//
//	@Override
//	public void onFirstTick(Level world, int x, int y, int z) {
//		this.getIOSides(world, x, y, z, this.getBlockMetadata());
//		if (!world.isClientSide() && ModList.IC2.isLoaded())
//			this.addTileToNet();
//	}
//
//	@ModDependent(ModList.IC2)
//	private void addTileToNet() {
//		MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
//	}
//
//	@Override
//	protected void onInvalidateOrUnload(Level world, int x, int y, int z, boolean invalidate) {
//		if (!world.isClientSide() && ModList.IC2.isLoaded())
//			this.removeTileFromNet();
//	}
//
//	@ModDependent(ModList.IC2)
//	private void removeTileFromNet() {
//		MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
//	}
//}
