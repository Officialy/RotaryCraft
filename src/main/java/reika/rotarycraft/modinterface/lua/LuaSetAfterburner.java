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

import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.modinteract.lua.LuaMethod;
import reika.rotarycraft.blockentities.engine.BlockEntityJetEngine;


public class LuaSetAfterburner extends LuaMethod {

	public LuaSetAfterburner() {
		super("setAfterburner", BlockEntityJetEngine.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		if (((BlockEntityJetEngine)te).canAfterBurn()) {
			//1.7.10 read args[1] although the signature is (boolean active), so a one-argument call failed; the last
			//argument serves both the documented call and scripts that padded it to two
			if (args.length == 0)
				throw new LuaMethodException("Expected a boolean");
			((BlockEntityJetEngine)te).setBurnerActive((Boolean)args[args.length-1]);
		}
		else {
			throw new LuaMethodException("This engine ("+te+") does not have an afterburner!");
		}
		return null;
	}

	@Override
	public String getDocumentation() {
		return "Allows for control of the jet engine afterburner.";
	}

	@Override
	public String getArgsAsString() {
		return "boolean active";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.VOID;
	}

}
