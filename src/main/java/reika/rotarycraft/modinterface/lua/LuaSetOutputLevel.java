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
import reika.rotarycraft.base.blockentity.EnergyToPowerBase;

public class LuaSetOutputLevel extends LuaMethod {

	public LuaSetOutputLevel() {
		super("setOutputLevel", EnergyToPowerBase.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		EnergyToPowerBase e = (EnergyToPowerBase) te;
		if (!e.setOmega(((Integer)args[0]).intValue()))
			throw new IllegalArgumentException("Invalid power setting out of bounds.");
		return null;
	}

	@Override
	public String getDocumentation() {
		return "Sets the power output level of a converter engine (as the exponent), starting at -1 for no output.\nArgs: Setting\nReturns: Nothing";
	}

	@Override
	public String getArgsAsString() {
		return "int setting";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.VOID;
	}

}
