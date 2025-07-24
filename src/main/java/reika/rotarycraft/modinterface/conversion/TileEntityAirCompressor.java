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
//import net.minecraft.block.Block;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.init.Blocks;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntity;import net.minecraft.world.World;
//import net.neoforged.common.util.Direction;
//
//import reika.dragonapi.ModList;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.modinteract.Power.ReikaPneumaticHelper;
//import reika.dragonapi.modinteract.power.ReikaPneumaticHelper;
//import reika.rotarycraft.RotaryConfig;
//import reika.rotarycraft.auxiliary.interfaces.PressureTE;
//import reika.rotarycraft.auxiliary.interfaces.RCToModConverter;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//import reika.rotarycraft.registry.ConfigRegistry;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.SoundRegistry;
//
//import pneumaticCraft.api.tileentity.IAirHandler;
//import pneumaticCraft.api.tileentity.IPneumaticMachine;
//
//public class TileEntityAirCompressor extends BlockEntityPowerReceiver implements PressureTE, RCToModConverter {
//
//	private int pressure;
//
//	private boolean isOut;
//
//	public static final int MAXPRESSURE = 1000;
//
//	private Direction facingDir;
//
//	@Override
//	protected void animateWithTick(Level world, BlockPos pos) {
//		if (omega <= 1 || !this.hasOutputTile()) {
//			if (phi > 0) {
//				double speed = 0.0125;
//				if (isOut)
//					phi += speed;
//				else
//					phi -= speed;
//				if (phi <= 0)
//					isOut = true;
//				if (phi >= 0.5)
//					isOut = false;
//			}
//			return;
//		}
//		double speed = ReikaMathLibrary.logbase(omega, 2)*0.025/6D;
//		if (speed > 0.125)
//			speed = 0.125;
//
//		if (isOut) {
//			phi += speed;
//			if (phi <= 0.095)
//				this.playSound(world, x, y, z);
//		}
//		else
//			phi -= speed;
//		if (phi <= 0) {
//			isOut = true;
//			phi = 0;
//		}
//		if (phi >= 0.5) {
//			isOut = false;
//		}
//	}
//
//	private void playSound(Level world, BlockPos pos) {
//		int p = (int)(ReikaMathLibrary.logbase(omega, 2)/8);
//		float v = 0.5F*this.getSoundVolume(world, pos);
//		SoundRegistry.AIRCOMP.playSoundAtBlock(world, pos, v, p);
//	}
//
//	private float getSoundVolume(Level world, int x, int y, int z) {
//		Direction dir = facingDir;
//		if (dir == null)
//			return 1F;
//		Direction dir2 = dir.getOpposite();
//		for (int i = 0; i < 6; i++) {
//			Direction side = dirs[i];
//			if (side != dir && side != dir2) {
//				int dx = x+side.offsetX;
//				int dy = y+side.offsetY;
//				int dz = z+side.offsetZ;
//				Block id = world.getBlockState(new BlockPos(dx, dy, dz)).getBlock();
//				if (id != Blocks.WOOL)
//					return 1;
//			}
//		}
//		return 0.2625F;
//	}
//
//	private boolean hasOutputTile() {
//		BlockEntity te = this.getAdjacentTileEntity(write);
//		return ModList.PNEUMATICRAFT.isLoaded() && te instanceof IPneumaticMachine;
//	}
//
//	@Override
//	public MachineRegistry getMachine() {
//		return MachineRegistry.COMPRESSOR;
//	}
//
//	@Override
//	public boolean hasModelTransparency() {
//		return false;
//	}
//
//	@Override
//	public int getRedstoneOverride() {
//		return 15*pressure/MAXPRESSURE;
//	}
//
//	@Override
//	public void updateEntity(Level world, BlockPos pos) {
//		super.updateEntity();
//		tickcount++;
//		this.getIOSides(world, pos);
//		this.getPower(false);
//
//		if (power > 0 && ModList.PNEUMATICRAFT.isLoaded()) {
//			BlockEntity tile = this.getAdjacentTileEntity(write);
//			if (tile instanceof IPneumaticMachine) {
//				IPneumaticMachine rc = (IPneumaticMachine)tile;
//				IAirHandler a = rc.getAirHandler();
//				if (a == null)
//					return;
//				int air = this.getGenAir();
//				a.addAir(air, write.getOpposite());
//			}
//		}
//
//		if (tickcount < 20)
//			return;
//		tickcount = 0;
//
//		this.updatePressure(world, pos);
//	}
//
//	private void getIOSides(Level world, int x, int y, int z, int meta) {
//		switch (meta) {
//			case 0 -> facingDir = Direction.DOWN;
//			case 1 -> facingDir = Direction.UP;
//			case 2 -> facingDir = Direction.NORTH;
//			case 3 -> facingDir = Direction.WEST;
//			case 4 -> facingDir = Direction.SOUTH;
//			case 5 -> facingDir = Direction.EAST;
//		}
//		read = facingDir;
//		write = read.getOpposite();
//	}
//
//	public boolean isPipeConnected(Direction with) {
//		return switch (this.getBlockMetadata()) {
//			case 4 -> with == Direction.NORTH;
//			case 5 -> with == Direction.WEST;
//			case 2 -> with == Direction.SOUTH;
//			case 3 -> with == Direction.EAST;
//			case 0 -> with == Direction.UP;
//			case 1 -> with == Direction.DOWN;
//			default -> false;
//		};
//	}
//
//	@Override
//	protected void readSyncTag(CompoundTag NBT) {
//		super.readSyncTag(NBT);
//		pressure = NBT.getInt("pressure");
//	}
//
//	@Override
//	protected void writeSyncTag(CompoundTag NBT) {
//		super.writeSyncTag(NBT);
//		NBT.putInt("pressure", pressure);
//	}
//
//	@Override
//	public void updatePressure(Level world, int x, int y, int z, int meta) {
//		int Pamb = 101;
//
//		if (pressure > Pamb)
//			this.addPressure((Pamb-pressure)/50);
//
//		this.addPressure((int)Math.sqrt(power)/64);
//
//		if (pressure > MAXPRESSURE)
//			this.overpressure(world, x, y, z);
//	}
//
//	@Override
//	public void addPressure(int press) {
//		pressure += press;
//	}
//
//	@Override
//	public int getPressure() {
//		return pressure;
//	}
//
//	@Override
//	public void overpressure(Level world, int x, int y, int z) {
//		pressure = MAXPRESSURE;
//		world.explode(null, x+0.5, y+0.5, z+0.5, 4F+rand.nextFloat()*2, RotaryConfig.COMMON.BLOCKDAMAGE.get());
//
//		for (int i = 0; i < 6; i++)
//			world.explode(null, x+0.5-1+rand.nextDouble()*2, y+0.5-1+rand.nextDouble()*2, z+0.5-1+rand.nextDouble()*2, 3F, RotaryConfig.COMMON.BLOCKDAMAGE.get());
//	}
//	/*
//	@Override
//	public ConnectOverride overridePipeConnection(PipeType type, Direction with) {
//		return this.isPipeConnected(with) ? ConnectOverride.CONNECT : ConnectOverride.DISCONNECT;
//	}*/
//
//	public boolean canEmitPowerFrom(Direction side) {
//		return this.isPipeConnected(side);
//	}
//
//	public int getGenAir() {
//		return (int)(power/ ReikaPneumaticHelper.getWattsPerAir()*ConfigRegistry.getConverterEfficiency());
//	}
//
//	@Override
//	public int getMaxPressure() {
//		return MAXPRESSURE;
//	}
//
//	@Override
//	public int getGeneratedUnitsPerTick() {
//		return this.getGenAir();
//	}
//
//	@Override
//	public String getUnitDisplay() {
//		return "mL";
//	}
//
//}
