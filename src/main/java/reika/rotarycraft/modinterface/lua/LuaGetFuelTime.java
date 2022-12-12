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
import reika.dragonapi.libraries.mathsci.ReikaDateHelper;
import reika.dragonapi.modinteract.lua.LuaMethod;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;

public class LuaGetFuelTime extends LuaMethod {

	public LuaGetFuelTime() {
		super("getTime", BlockEntityEngine.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		int time = ((BlockEntityEngine)te).getFuelDuration();
		return new Object[]{ReikaDateHelper.getSecondsAsClock(time)};
	}

	@Override
	public String getDocumentation() {
		return "Returns the remaining fuel time in an engine";
	}

	@Override
	public String getArgsAsString() {
		return "";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.STRING;
	}

}
