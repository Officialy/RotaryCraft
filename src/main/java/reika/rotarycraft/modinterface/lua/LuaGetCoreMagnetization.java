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
import reika.rotarycraft.auxiliary.interfaces.MagnetizationCore;

public class LuaGetCoreMagnetization extends LuaMethod {

	public LuaGetCoreMagnetization() {
		super("getMagnetization", MagnetizationCore.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		int core = ((MagnetizationCore)te).getCoreMagnetization();
		return new Object[]{core};
	}

	@Override
	public String getDocumentation() {
		return "Returns the magnetization of the shaft core";
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
