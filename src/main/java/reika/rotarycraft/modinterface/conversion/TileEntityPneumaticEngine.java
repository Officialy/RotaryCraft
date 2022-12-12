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
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;import net.minecraft.world.World;
//import net.minecraftforge.common.util.Direction;
//
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.DragonAPICore;
//import reika.dragonapi.ModList;
//import reika.dragonapi.ASM.APIStripper.Strippable;
//import reika.dragonapi.ASM.DependentMethodStripper.ModDependent;
//import reika.dragonapi.instantiable.StepTimer;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.rendering.ReikaColorAPI;
//import reika.dragonapi.modinteract.Power.ReikaPneumaticHelper;
//import reika.rotarycraft.base.blockentity.EnergyToPowerBase;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.SoundRegistry;
//
//import pneumaticCraft.api.tileentity.AirHandlerSupplier;
//import pneumaticCraft.api.tileentity.IAirHandler;
//import pneumaticCraft.api.tileentity.IPneumaticMachine;
//
//@Strippable(value={"pneumaticCraft.api.tileentity.IPneumaticMachine"})
//public class TileEntityPneumaticEngine extends EnergyToPowerBase implements IPneumaticMachine {
//
//	private IAirHandler air;
//	private static final int maxAir = 30000;
//
//	private StepTimer sound = new StepTimer(72);
//
//	public TileEntityPneumaticEngine() {
//		super();
//		if (ModList.PNEUMATICRAFT.isLoaded()) {
//			air = AirHandlerSupplier.getAirHandler(10, 12, maxAir);
//		}
//		sound.setTick(sound.getCap());
//	}
//
//	@Override
//	public boolean isValidSupplier(BlockEntity te) {
//		if (te == null)
//			return false;
//		if (te.getClass().getSimpleName().startsWith("pneumaticCraft.common.tileentity"))
//			return true;
//		return false;
//	}
//
//	@Override
//	protected int getIdealConsumedUnitsPerTick() {
//		return this.getAirPerTick();
//	}
//
//	private int getAirPerTick() {
//		return ReikaPneumaticHelper.getWattsPerAir();
//	}
//
//	@Override
//	public int getMaxStorage() {
//		return maxAir;
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
//		return MachineRegistry.PNEUENGINE;
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
//
//		if (!ModList.PNEUMATICRAFT.isLoaded())
//			return;
//
//		if (DragonAPI.debugtest) {
//			air.addAir(5, this.getConnection());
//		}
//		this.getIOSides(world, pos);
//
//		air.updateEntityI();
//
//		storedEnergy = air.getCurrentAir(this.getConnection());
//		if (storedEnergy < 0) {
//			storedEnergy = 0;
//		}
//
//		this.updateSpeed();
//		if (!world.isClientSide()) {
//			sound.update();
//
//			if (power > 0) {
//				if (sound.checkCap())
//					SoundRegistry.PNEUMATIC.playSoundAtBlock(world, pos, this.isMuffled() ? 0.3F : 1.2F, 1);
//			}
//		}
//
//		this.basicPowerReceiver();
//	}
//
//	@Override
//	protected void usePower(float mult) {
//		int amt = (int)(this.getAirPerTick()*mult);
//		if (ModList.PNEUMATICRAFT.isLoaded())
//			air.addAir(-amt, this.getConnection()); //drain amt energy
//	}
//
//	public boolean isConnectedTo(Direction with) {
//		return with == this.getConnection();
//	}
//
//	private Direction getConnection() {
//		switch(this.getBlockMetadata()) {
//			case 0:
//				return Direction.NORTH;
//			case 1:
//				return Direction.WEST;
//			case 2:
//				return Direction.SOUTH;
//			case 3:
//				return Direction.EAST;
//			default:
//				return Direction.UNKNOWN;
//		}
//	}
//
//	@Override
//	protected void writeSyncTag(CompoundTag NBT)
//	{
//		super.writeSyncTag(NBT);
//		if (ModList.PNEUMATICRAFT.isLoaded())
//			air.writeToNBTI(NBT);
//	}
//
//	@Override
//	protected void readSyncTag(CompoundTag NBT)
//	{
//		super.readSyncTag(NBT);
//		if (ModList.PNEUMATICRAFT.isLoaded())
//			air.readFromNBTI(NBT);
//	}
//
//	@Override
//	public void validate() {
//		super.validate();
//		if (ModList.PNEUMATICRAFT.isLoaded())
//			air.validateI(this);
//	}
//
//	@Override
//	public String getUnitDisplay() {
//		return "mL";
//	}
//
//	@Override
//	public int getPowerColor() {
//		return ReikaColorAPI.RGBtoHex(50, 170, 255);
//	}
//
//	@Override
//	@ModDependent(ModList.PNEUMATICRAFT)
//	public IAirHandler getAirHandler() {
//		return air;
//	}
//
//	//@Override
//	//public int getMaxSpeedBase(int tier) {
//	//	return 7+4*tier;
//	//}
//
//}
