/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2017
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.modinterface.lua;

import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.modinteract.lua.LuaMethod;
import reika.rotarycraft.blockentities.transmission.BlockEntitySplitter;

public class LuaSetJunction extends LuaMethod {

	public LuaSetJunction() {
		super("setJunction", BlockEntitySplitter.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		BlockEntitySplitter spl = (BlockEntitySplitter) te;
		int ratio = ((Double)args[0]).intValue();
		int test = Math.abs(ratio);
		if (test != 1 && test != 4 && test != 8 && test != 16 && test != 32)
			throw new IllegalArgumentException("Invalid Ratio!");
		spl.setMode(ratio);
		return null;
	}

	@Override
	public String getDocumentation() {
		return "Sets the junction setting. Use negative numbers to favor bend.\nArgs: Setting Ratio\nReturns: Nothing";
	}

	@Override
	public String getArgsAsString() {
		return "int ratio";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.VOID;
	}

}
