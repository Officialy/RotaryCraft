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
//public class LuaSetSpeed extends LuaMethod {
//
//	public LuaSetSpeed() {
//		super("setSpeed", BlockEntityAdvancedGear.class);
//	}
//
//	@Override
//	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
//		BlockEntityAdvancedGear adv = (BlockEntityAdvancedGear) te;
//		if (adv.getGearType() == GearType.COIL) {
//			int s = ((Double)args[0]).intValue();
//			adv.setReleaseOmega(s);
//		}
//		return null;
//	}
//
//	@Override
//	public String getDocumentation() {
//		return "Sets the coil speed.\nArgs: Desired Speed\nReturns: Nothing";
//	}
//
//	@Override
//	public String getArgsAsString() {
//		return "int speed";
//	}
//
//	@Override
//	public ReturnType getReturnType() {
//		return ReturnType.VOID;
//	}
//
//}
