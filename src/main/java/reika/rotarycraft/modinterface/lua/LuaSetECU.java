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
//import reika.rotarycraft.blockentities.Auxiliary.BlockEntityEngineController;
//
//public class LuaSetECU extends LuaMethod {
//
//	public LuaSetECU() {
//		super("setECU", BlockEntityEngineController.class);
//	}
//
//	@Override
//	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
//		BlockEntityEngineController ecu = (BlockEntityEngineController) te;
//		int index = ((Double)args[0]).intValue();
//		ecu.setSetting(index);
//		return null;
//	}
//
//	@Override
//	public String getDocumentation() {
//		return "Sets the ECU setting.\nArgs: Setting Ordinal\nReturns: Nothing";
//	}
//
//	@Override
//	public String getArgsAsString() {
//		return "int settingOrdinal";
//	}
//
//	@Override
//	public ReturnType getReturnType() {
//		return ReturnType.VOID;
//	}
//
//}
