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
//import java.util.ArrayList;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;import net.minecraft.world.World;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.common.util.Direction;
//import net.minecraftforge.fluids.Fluid;
//import net.minecraftforge.fluids.FluidRegistry;
//
//import reika.dragonapi.ModList;
//import reika.dragonapi.ASM.APIStripper.Strippable;
//import reika.dragonapi.ASM.DependentMethodStripper.ModDependent;
//import reika.dragonapi.libraries.io.ReikaSoundHelper;
//import reika.dragonapi.libraries.Java.ReikaRandomHelper;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.World.ReikaWorldHelper;
//import reika.dragonapi.modinteract.Power.ReikaEUHelper;
//import reika.dragonapi.modinteract.power.ReikaEUHelper;
//import reika.rotarycraft.RotaryConfig;
//import reika.rotarycraft.auxiliary.interfaces.NBTMachine;
//import reika.rotarycraft.auxiliary.interfaces.RCToModConverter;
//import reika.rotarycraft.auxiliary.interfaces.UpgradeableMachine;
//import reika.rotarycraft.base.blockentity.PoweredLiquidReceiver;
//import reika.rotarycraft.Items.Tools.ItemEngineUpgrade.Upgrades;
//import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
//import reika.rotarycraft.registry.ConfigRegistry;
//import reika.rotarycraft.registry.ItemRegistry;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import ic2.api.energy.event.EnergyTileLoadEvent;
//import ic2.api.energy.event.EnergyTileUnloadEvent;
//import ic2.api.energy.tile.IEnergySource;
//import reika.rotarycraft.registry.RotaryFluids;
//import reika.rotarycraft.registry.RotaryItems;
//
////@Strippable(value = {"universalelectricity.api.electricity.IVoltageOutput", "universalelectricity.api.energy.IEnergyInterface"})
//@Strippable(value = {"ic2.api.energy.tile.IEnergySource"})
//public class TileEntityGenerator extends PoweredLiquidReceiver implements IEnergySource, RCToModConverter, UpgradeableMachine, NBTMachine {
//
//	//public static final int OUTPUT_VOLTAGE = 24000;
//	//public static final float POWER_FACTOR = 0.875F;
//
//	private Direction facingDir;
//
//	public static final boolean hardModeEU = RotaryConfig.COMMON.HARDEU.get();
//
//	private boolean upgraded;
//
//	public Direction getFacing() {
//		return facingDir != null ? facingDir : Direction.EAST;
//	}
//
//	@Override
//	protected void animateWithTick(Level world, BlockPos pos) {
//
//	}
//
//	@Override
//	public MachineRegistry getMachine() {
//		return MachineRegistry.GENERATOR;
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
//		this.getIOSides(world, pos, getBlockState().getValue(getFacingProperty()));
//		if (hardModeEU) {
//			if (tank.isEmpty()) {
//				omega = torque = 0;
//				power = 0;
//				return;
//			}
//			else {
//				if (power > 0) {
//					tank.removeLiquid(1);
//
//					int t = upgraded ? 4096 : 1024;
//					if (torque > t) {
//						if (ReikaRandomHelper.doWithChance((torque-t)/20000D)) {
//							if (rand.nextInt(upgraded ? 16 : 4) == 0) {
//								this.delete();
//								world.explode(null, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 3, true, true);
//							}
//							else {
//								ReikaSoundHelper.playSoundAtBlock(world, pos, "random.fizz");
//							}
//						}
//					}
//				}
//			}
//		}
//
//		this.getPower(false);
//
//		if ((world.getGameTime()&31) == 0)
//			ReikaWorldHelper.causeAdjacentUpdates(world, pos);
//	}
//
//	private void getIOSides(Level world, BlockPos pos, Direction dir) {
//		switch (dir) {
//			case NORTH -> facingDir = Direction.NORTH;
//			case WEST -> facingDir = Direction.WEST;
//			case SOUTH -> facingDir = Direction.SOUTH;
//			case EAST -> facingDir = Direction.EAST;
//		}
//		read = facingDir;
//		write = read.getOpposite();
//	}
//
//	@Override
//	public boolean emitsEnergyTo(BlockEntity receiver, Direction direction) {
//		return direction == this.getFacing().getOpposite();
//	}
//
//	@Override
//	public double getOfferedEnergy() {
//		return (power-this.getPowerLoss())/ ReikaEUHelper.getWattsPerEU()*ConfigRegistry.getConverterEfficiency();
//	}
//
//	private double getPowerLoss() {
//		int t = upgraded ? 512 : 128;
//		return torque > t ? ReikaMathLibrary.intpow2(torque-t, 2)*(upgraded ? 0.0625 : 0.125) : 0;
//	}
//
//	@Override
//	public void onFirstTick(Level world, BlockPos pos) {
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
//
//	@Override
//	public int getSourceTier() {
//		return ReikaEUHelper.getIC2TierFromPower(this.getOfferedEnergy());
//	}
//
//	@Override
//	public boolean canConnectToPipe(MachineRegistry m) {
//		return m.isStandardPipe();
//	}
//
//	@Override
//	public boolean isValidFluid(Fluid f) {
//		return f != null && (f.equals(FluidRegistry.getFluid("ic2coolant")) || f.equals(RotaryFluids.LIQUID_NITROGEN.get()));
//	}
//
//	@Override
//	public Fluid getInputFluid() {
//		return null;
//	}
//
//	@Override
//	public boolean canReceiveFrom(Direction from) {
//		return from == Direction.DOWN;
//	}
//
//	@Override
//	public int getCapacity() {
//		return 6000;
//	}
//
//	@Override
//	public void upgrade(ItemStack is) {
//		upgraded = true;
//	}
//
//	public boolean isUpgraded() {
//		return upgraded;
//	}
//
//	@Override
//	public boolean canUpgradeWith(ItemStack item) {
//		return !upgraded && RotaryItems.UPGRADE.matchItem(item) && item.getItemDamage() == Upgrades.FLUX.ordinal();
//	}
//
//	public final void setDataFromItemStackTag(CompoundTag nbt) {
//		if (nbt != null) {
//			upgraded = nbt.getBoolean("upgrade");
//		}
//	}
//
//	public final CompoundTag getTagsToWriteToStack() {
//		CompoundTag nbt = new CompoundTag();
//		nbt.putBoolean("upgrade", upgraded);
//		return nbt;
//	}
//
//	@Override
//	protected void writeSyncTag(CompoundTag NBT) {
//		super.writeSyncTag(NBT);
//
//		NBT.putBoolean("upgrade", upgraded);
//	}
//
//	@Override
//	protected void readSyncTag(CompoundTag NBT) {
//		super.readSyncTag(NBT);
//
//		upgraded = NBT.getBoolean("upgrade");
//	}
//
//	@Override
//	public ArrayList<CompoundTag> getCreativeModeVariants() {
//		return new ArrayList<>();
//	}
//
//	@Override
//	public ArrayList<String> getDisplayTags(CompoundTag NBT) {
//		return new ArrayList<>();
//	}
//
//	@Override
//	public int getGeneratedUnitsPerTick() {
//		return (int)this.getOfferedEnergy();
//	}
//
//	@Override
//	public String getUnitDisplay() {
//		return "EU";
//	}
//}
