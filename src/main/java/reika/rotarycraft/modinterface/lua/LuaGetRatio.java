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
//import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;
//import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear.GearType;
//
//public class LuaGetRatio extends LuaMethod {
//
//	public LuaGetRatio() {
//		super("getRatio", BlockEntityAdvancedGear.class);
//	}
//
//	@Override
//	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
//		BlockEntityAdvancedGear adv = (BlockEntityAdvancedGear) te;
//		return adv.getGearType() == GearType.CVT ? new Object[]{adv.getRatio()} : null;
//	}
//
//	@Override
//	public String getDocumentation() {
//		return "Returns the CVT ratio.\nArgs: None\nReturns: Ratio";
//	}
//
//	@Override
//	public String getArgsAsString() {
//		return "";
//	}
//
//	@Override
//	public ReturnType getReturnType() {
//		return ReturnType.INTEGER;
//	}
//
//}
