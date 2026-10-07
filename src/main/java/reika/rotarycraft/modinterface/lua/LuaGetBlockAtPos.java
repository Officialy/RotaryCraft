///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
package reika.rotarycraft.modinterface.lua;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;

import reika.dragonapi.instantiable.data.immutable.BlockKey;
import reika.dragonapi.modinteract.lua.LuaMethod;
import reika.rotarycraft.blockentities.surveying.BlockEntityGPR;

public class LuaGetBlockAtPos extends LuaMethod {

	public LuaGetBlockAtPos() {
		super("getBlockAtPos", BlockEntityGPR.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		BlockEntityGPR tg = (BlockEntityGPR)te;
		int dw = ((Double)args[0]).intValue();
		int dh = ((Double)args[1]).intValue();
		/*
		int dh = te.yCoord+((Double)args[1]).intValue();
		int[] xz = tg.getHorizontalInterval();
		int[] yy = tg.getVerticalInterval();
		BlockVector bv = tg.getLookDirection();
		dw += te.xCoord*Math.abs(bv.direction.offsetX);
		dw += te.zCoord*Math.abs(bv.direction.offsetZ);
		if (dw < xz[0] || dw > xz[1] || dh < yy[0] || dh > yy[1]) //out of bounds
			return null;
		 */
		if (Math.abs(dw) > tg.getRange() || dh < 0 || dh > BlockEntityGPR.MAX_HEIGHT)
			return null;
		BlockKey bk = tg.getBlock(dw, dh);
		if (bk == null) //depth 0 is the machine's own layer, which the scan never covers
			return null;
		//1.7.10: Block ID, metadata -> registry name and the block state (the flattened metadata)
		return new Object[]{BuiltInRegistries.BLOCK.getKey(bk.blockID.getBlock()).toString(), bk.blockID.toString()};
	}

	@Override
	public String getDocumentation() {
		return "Returns the block at a given position.\nArgs: horizontal offset, relative depth\nReturns: Block ID, metadata";
	}

	@Override
	public String getArgsAsString() {
		return "int offset, int depth";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.ARRAY;
	}

}
