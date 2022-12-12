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
//import reika.dragonapi.instantiable.data.Immutable.BlockKey;
//import reika.dragonapi.instantiable.data.immutable.BlockKey;
//import reika.dragonapi.modinteract.lua.LuaMethod;
//import reika.rotarycraft.blockentities.surveying.TileEntityGPR;
//
//public class LuaGetBlockAtPos extends LuaMethod {
//
//	public LuaGetBlockAtPos() {
//		super("getBlockAtPos", TileEntityGPR.class);
//	}
//
//	@Override
//	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
//		TileEntityGPR tg = (TileEntityGPR)te;
//		int dw = ((Double)args[0]).intValue();
//		int dh = ((Double)args[1]).intValue();
//		/*
//		int dh = te.yCoord+((Double)args[1]).intValue();
//		int[] xz = tg.getHorizontalInterval();
//		int[] yy = tg.getVerticalInterval();
//		BlockVector bv = tg.getLookDirection();
//		dw += te.xCoord*Math.abs(bv.direction.offsetX);
//		dw += te.zCoord*Math.abs(bv.direction.offsetZ);
//		if (dw < xz[0] || dw > xz[1] || dh < yy[0] || dh > yy[1]) //out of bounds
//			return null;
//		 */
//		if (Math.abs(dw) > tg.getRange() || dh < 0 || dh > tg.MAX_HEIGHT)
//			return null;
//		BlockKey bk = tg.getBlock(dw, dh);
//		return new Object[]{bk.blockID};
//	}
//
//	@Override
//	public String getDocumentation() {
//		return "Returns the block at a given position.\nArgs: horizontal offset, relative depth\nReturns: Block ID, metadata";
//	}
//
//	@Override
//	public String getArgsAsString() {
//		return "int offset, int depth";
//	}
//
//	@Override
//	public ReturnType getReturnType() {
//		return ReturnType.ARRAY;
//	}
//
//}
