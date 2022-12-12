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
import reika.rotarycraft.blockentities.decorative.BlockEntityMusicBox;

public class LuaClearChannel extends LuaMethod {

	public LuaClearChannel() {
		super("clearChannel", BlockEntityMusicBox.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		BlockEntityMusicBox mus = (BlockEntityMusicBox) te;
		int channel = ((Double)args[0]).intValue();
		mus.clearChannel(channel);
		return null;
	}

	@Override
	public String getDocumentation() {
		return "Clears the music box channel.\nArgs: Channel (0-15)\nReturns: Nothing";
	}

	@Override
	public String getArgsAsString() {
		return "int channel";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.VOID;
	}

}
