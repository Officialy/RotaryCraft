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
//import net.minecraft.block.Block;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.init.Blocks;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntity;import net.minecraft.world.World;
//import net.minecraftforge.common.util.Direction;
//
//import net.minecraftforge.energy.IEnergyStorage;
//import reika.dragonapi.asm.APIStripper.Strippable;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.World.ReikaWorldHelper;
//import reika.dragonapi.modinteract.Power.ReikaRFHelper;
//import reika.dragonapi.modinteract.power.ReikaRFHelper;
//import reika.rotarycraft.auxiliary.interfaces.NBTMachine;
//import reika.rotarycraft.auxiliary.interfaces.RCToModConverter;
//import reika.rotarycraft.auxiliary.interfaces.UpgradeableMachine;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//import reika.rotarycraft.Items.Tools.ItemEngineUpgrade.Upgrades;
//import reika.rotarycraft.registry.ConfigRegistry;
//import reika.rotarycraft.registry.ItemRegistry;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import cofh.api.energy.IEnergyStorage;
//import cofh.api.energy.IEnergyStorage;
//
//public class TileEntityDynamo extends BlockEntityPowerReceiver implements IEnergyStorage, RCToModConverter, UpgradeableMachine, NBTMachine {
//
//	private Direction facingDir;
//
//	private boolean upgraded;
//
//	public static final int MAXTORQUE = 1024;
//	public static final int MAXTORQUE_UPGRADE = 2048;
//	public static final int MAXOMEGA = 8192;
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
//		return MachineRegistry.DYNAMO;
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
//		this.getIOSides(world, pos);
//		this.getPower(false);
//
//		if ((world.getGameTime()&31) == 0)
//			ReikaWorldHelper.causeAdjacentUpdates(world, x, y, z);
//
//		int writex = x+write.offsetX;
//		int writey = y+write.offsetY;
//		int writez = z+write.offsetZ;
//		if (power > 0) {
//			Block b = world.getBlock(writex, writey, writez);
//			if (b != Blocks.air) {
//				int metadata = world.getBlockMetadata(writex, writey, writez);
//				if (b.hasTileEntity(metadata)) {
//					BlockEntity tile = world.getBlockEntity(writex, writey, writez);
//					if (tile instanceof IEnergyStorage) {
//						IEnergyStorage rc = (IEnergyStorage)tile;
//						if (rc.canConnectEnergy(facingDir.getOpposite())) {
//							int rf = this.getGenRF();
//							float used = rc.receiveEnergy(facingDir.getOpposite(), rf, false);
//						}
//					}
//					else if (tile instanceof IEnergyStorage) {
//						IEnergyStorage rc = (IEnergyStorage)tile;
//						if (rc.canConnectEnergy(facingDir.getOpposite())) {
//							int rf = this.getGenRF();
//							float used = rc.receiveEnergy(facingDir.getOpposite(), rf, false);
//						}
//					}
//				}
//			}
//		}
//	}
//
//	public int getGenRF() {
//		int tq = Math.min(torque, upgraded ? MAXTORQUE_UPGRADE : MAXTORQUE);
//		int om = Math.min(omega, MAXOMEGA);
//		long pwr = (long)tq*(long)om;
//		return (int)(pwr/ ReikaRFHelper.getWattsPerRF()*ConfigRegistry.getConverterEfficiency());
//	}
//
//	@Override
//	public int getGeneratedUnitsPerTick() {
//		return this.getGenRF();
//	}
//
//	@Override
//	public String getUnitDisplay() {
//		return "RF";
//	}
//
//	private void getIOSides(Level world, int x, int y, int z, int meta) {
//		switch (meta) {
//			case 2 -> facingDir = Direction.SOUTH;
//			case 3 -> facingDir = Direction.EAST;
//			case 4 -> facingDir = Direction.NORTH;
//			case 5 -> facingDir = Direction.WEST;
//			case 1 -> facingDir = Direction.DOWN;
//			case 0 -> facingDir = Direction.UP;
//		}
//		write = facingDir;
//		read = write.getOpposite();
//	}
//
//	@Override
//	public int receiveEnergy(Direction from, int maxReceive, boolean simulate) {
//		return 0;
//	}
//
//	@Override
//	public int extractEnergy(Direction from, int maxExtract, boolean simulate) {
//		if (this.canConnectEnergy(from)) {
//			int rf = (int)(power/ReikaRFHelper.getWattsPerRF());
//			//return rf;
//		}
//		return 0;
//	}
//
//	@Override
//	public boolean canConnectEnergy(Direction from) {
//		return from == facingDir;
//	}
//
//	private Direction getFacing() {
//		return facingDir != null ? facingDir : Direction.EAST;
//	}
//
//	@Override
//	public int getEnergyStored(Direction from) {
//		return 0;
//	}
//
//	@Override
//	public int getMaxEnergyStored(Direction from) {
//		return 0;
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
//		return !upgraded && ItemRegistry.UPGRADE.matchItem(item) && item.getItemDamage() == Upgrades.FLUX.ordinal();
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
//	public ArrayList<CompoundTag> getCreativeModeVariants() {
//		return new ArrayList<>();
//	}
//
//	@Override
//	public ArrayList<String> getDisplayTags(CompoundTag NBT) {
//		return new ArrayList<>();
//	}
//
//}
