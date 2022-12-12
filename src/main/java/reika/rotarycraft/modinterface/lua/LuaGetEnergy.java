///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface.lua;
//
//import net.minecraft.world.level.block.entity.BlockEntity;
//import reika.dragonapi.modinteract.lua.LuaMethod;
//
//public class LuaGetEnergy extends LuaMethod {
//
//	public LuaGetEnergy() {
//		super("getEnergy", BlockEntityAdvancedGear.class);
//	}
//
//	@Override
//	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
//		BlockEntityAdvancedGear adv = (BlockEntityAdvancedGear) te;
//		return adv.getGearType().storesEnergy() ? new Object[]{adv.getEnergy()/20, adv.getMaxStorageCapacity()} : null;
//	}
//
//	@Override
//	public String getDocumentation() {
//		return "Returns the stored energy.\nArgs: None\nReturns: [Energy,Capacity]";
//	}
//
//	@Override
//	public String getArgsAsString() {
//		return "";
//	}
//
//	@Override
//	public ReturnType getReturnType() {
//		return ReturnType.ARRAY;
//	}
//
//}