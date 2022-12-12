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
//import reika.rotarycraft.blockentities.surveying.TileEntityGPR;
//
//public class LuaShiftPlane extends LuaMethod {
//
//	public LuaShiftPlane() {
//		super("shiftPlane", BlockEntityGPR.class);
//	}
//
//	@Override
//	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
//		BlockEntityGPR tg = (BlockEntityGPR)te;
//		int dir = (int)Math.signum((Double)args[0]);
//		tg.shiftInt(dir);
//		return null;
//	}
//
//	@Override
//	public String getDocumentation() {
//		return "Returns the block at a given position.\nArgs: shift direction +/- 1\nReturns: Nothing";
//	}
//
//	@Override
//	public String getArgsAsString() {
//		return "";
//	}
//
//	@Override
//	public ReturnType getReturnType() {
//		return ReturnType.VOID;
//	}
//
//}
