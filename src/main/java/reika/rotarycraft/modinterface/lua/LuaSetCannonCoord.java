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

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.instantiable.data.immutable.WorldLocation;
import reika.dragonapi.modinteract.lua.LuaMethod;
import reika.rotarycraft.auxiliary.interfaces.LocationTarget;

public class LuaSetCannonCoord extends LuaMethod {

	public LuaSetCannonCoord() {
		super("setCannonCoord", LocationTarget.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		LocationTarget can = (LocationTarget) te;
		int x = ((Double)args[0]).intValue();
		int y = ((Double)args[1]).intValue();
		int z = ((Double)args[2]).intValue();
		int dim = ((Double)args[3]).intValue();
		WorldLocation loc = new WorldLocation(null/*todo dim as ID*/, new BlockPos(x, y, z));
		can.setTarget(loc);
		return null;
	}

	@Override
	public String getDocumentation() {
		return "Sets the cannon target.\nArgs: X, Y, Z, Dimension\nReturns: Nothing";
	}

	@Override
	public String getArgsAsString() {
		return "int x, int y, int z, int dim";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.VOID;
	}

}
