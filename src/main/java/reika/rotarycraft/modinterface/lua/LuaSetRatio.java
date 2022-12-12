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
//import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;
//import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear.GearType;
//
//public class LuaSetRatio extends LuaMethod {
//
//	public LuaSetRatio() {
//		super("setRatio", BlockEntityAdvancedGear.class);
//	}
//
//	@Override
//	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
//		BlockEntityAdvancedGear adv = (BlockEntityAdvancedGear) te;
//		if (adv.getGearType() == GearType.CVT) {
//			int ratio = ((Double)args[0]).intValue();
//			adv.setRatio(ratio);
//		}
//		return null;
//	}
//
//	@Override
//	public String getDocumentation() {
//		return "Sets the CVT ratio.\nArgs: Ratio\nReturns: Nothing";
//	}
//
//	@Override
//	public String getArgsAsString() {
//		return "int ratio";
//	}
//
//	@Override
//	public ReturnType getReturnType() {
//		return ReturnType.VOID;
//	}
//
//}
