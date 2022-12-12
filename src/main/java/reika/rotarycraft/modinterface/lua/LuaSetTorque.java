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
//public class LuaSetTorque extends LuaMethod {
//
//	public LuaSetTorque() {
//		super("setTorque", BlockEntityAdvancedGear.class);
//	}
//
//	@Override
//	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
//		BlockEntityAdvancedGear adv = (BlockEntityAdvancedGear) te;
//		if (adv.getGearType() == GearType.COIL) {
//			int tq = ((Double)args[0]).intValue();
//			adv.setReleaseTorque(tq);
//		}
//		return null;
//	}
//
//	@Override
//	public String getDocumentation() {
//		return "Sets the coil torque.\nArgs: Desired Torque\nReturns: Nothing";
//	}
//
//	@Override
//	public String getArgsAsString() {
//		return "int torque";
//	}
//
//	@Override
//	public ReturnType getReturnType() {
//		return ReturnType.VOID;
//	}
//
//}
