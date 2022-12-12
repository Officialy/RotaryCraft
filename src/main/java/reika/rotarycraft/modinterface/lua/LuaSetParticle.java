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
import reika.dragonapi.libraries.registry.ReikaParticleHelper;
import reika.dragonapi.modinteract.lua.LuaMethod;
import reika.rotarycraft.blockentities.decorative.BlockEntityParticleEmitter;

public class LuaSetParticle extends LuaMethod {

	public LuaSetParticle() {
		super("setParticle", BlockEntityParticleEmitter.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		BlockEntityParticleEmitter part = (BlockEntityParticleEmitter) te;
		if (args[0] instanceof String) {
			part.particleType = ReikaParticleHelper.getByString((String)args[0]);
		}
		else {
			int index = ((Double)args[0]).intValue();
			part.particleType = ReikaParticleHelper.particleList[index];
		}
		return null;
	}

	@Override
	public String getDocumentation() {
		return "Sets the particle setting.\nArgs: Setting ordinal or name\nReturns: Nothing";
	}

	@Override
	public String getArgsAsString() {
		return "int settingOrdinal OR String particleName";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.VOID;
	}

}
