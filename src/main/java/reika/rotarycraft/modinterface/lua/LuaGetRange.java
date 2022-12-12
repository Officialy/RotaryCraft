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
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;

public class LuaGetRange extends LuaMethod {

	public LuaGetRange() {
		super("getRange", RangedEffect.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		return new Object[]{((RangedEffect)te).getRange(), ((RangedEffect)te).getMaxRange()};
	}

	@Override
	public String getDocumentation() {
		return "Returns the effect range.\nArgs: None\nReturns: [Range, Max Range]";
	}

	@Override
	public String getArgsAsString() {
		return "";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.ARRAY;
	}

}
