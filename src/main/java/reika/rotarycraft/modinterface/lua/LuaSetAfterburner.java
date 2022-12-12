///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface.lua;
//
//import net.minecraft.world.level.block.entity.BlockEntity;
//import reika.dragonapi.modinteract.lua.LuaMethod;
//
//
//public class LuaSetAfterburner extends LuaMethod {
//
//	public LuaSetAfterburner() {
//		super("setAfterburner", BlockEntityJetEngine.class);
//	}
//
//	@Override
//	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
//		if (((BlockEntityJetEngine)te).canAfterBurn()) {
//			((BlockEntityJetEngine)te).setBurnerActive((Boolean)args[1]);
//		}
//		else {
//			throw new LuaMethodException("This engine ("+te+") does not have an afterburner!");
//		}
//		return null;
//	}
//
//	@Override
//	public String getDocumentation() {
//		return "Allows for control of the jet engine afterburner.";
//	}
//
//	@Override
//	public String getArgsAsString() {
//		return "boolean active";
//	}
//
//	@Override
//	public ReturnType getReturnType() {
//		return ReturnType.VOID;
//	}
//
//}
