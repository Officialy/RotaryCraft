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
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;

public class LuaGetTemperature extends LuaMethod {

	public LuaGetTemperature() {
		super("getTemperature", TemperatureTE.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		return new Object[]{((TemperatureTE)te).getTemperature()};
	}

	@Override
	public String getDocumentation() {
		return "Returns the machine temperature.\nArgs: None\nReturns: Temperature";
	}

	@Override
	public String getArgsAsString() {
		return "";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.INTEGER;
	}

}
