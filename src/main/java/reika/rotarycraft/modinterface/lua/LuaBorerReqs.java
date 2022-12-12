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
//public class LuaBorerReqs extends LuaMethod {
//
//	public LuaBorerReqs() {
//		super("getRequirements", BlockEntityBorer.class);
//	}
//
//	@Override
//	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
//		BlockEntityBorer io = (BlockEntityBorer)te;
//		Object[] o = new Object[3];
//		o[0] = io.getRequiredTorque();
//		o[1] = io.getRequiredPower();
//		o[2] = io.isJammed();
//		return o;
//	}
//
//	@Override
//	public String getDocumentation() {
//		return "Returns the current mining requirements.\nArgs: None\nReturns: [Required Torque, Required Power, isJammed]";
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
