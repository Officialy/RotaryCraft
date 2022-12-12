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
//import java.util.ArrayList;
//import java.util.List;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.monster.*;
//import net.minecraft.world.entity.npc.Villager;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import reika.dragonapi.libraries.ReikaEntityHelper;
//import reika.dragonapi.modinteract.lua.LuaMethod;
//
//public class LuaGetMobs extends LuaMethod {
//
//	public LuaGetMobs() {
//		super("getMobs", BlockEntityMobRadar.class);
//	}
//
//	@Override
//	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
//		List<LivingEntity> li = ((BlockEntityMobRadar)te).getEntities();
//		ArrayList<Object[]> entities = new ArrayList<>();
//		for (LivingEntity e : li) {
//			entities.add(this.encodeEntity(e));
//		}
//		return entities.toArray(new Object[entities.size()]);
//	}
//
//	private Object[] encodeEntity(LivingEntity e) {
//		ArrayList<Object> params = new ArrayList<>();
//		params.add(e instanceof Player ? "Player" : e.getClass().getSimpleName());
//		params.add(e.getX());
//		params.add(e.getY());
//		params.add(e.getZ());
//		params.add(e.getUUID());
//		params.add(e.getHealth()*100F/e.getMaxHealth());
//		if (e instanceof Creeper) {
//			params.add(ReikaEntityHelper.getCreeperFuse((Creeper)e));
//			params.add(ReikaEntityHelper.isCreeperCharged((Creeper)e));
//		}
//		if (e instanceof Skeleton) {
//			params.add(((Skeleton)e).getSkeletonType());
//		}
//		if (e instanceof Slime) {
//			params.add(((Slime)e).getSlimeSize());
//		}
//		if (e instanceof ZombifiedPiglin) {
//			params.add(ReikaEntityHelper.isPigZombieAngry((ZombifiedPiglin)e));
//		}
//		if (e instanceof Villager) {
//			params.add(((Villager)e).getProfession());
//		}
//		if (e instanceof EnderMan) {
//			params.add(Block.blockRegistry.getNameForObject(((EnderMan)e).func_146080_bZ()));
//			params.add(((EnderMan)e).getCarriedBlock());
//		}
//		return params.toArray(new Object[params.size()]);
//	}
//
//	@Override
//	public String getDocumentation() {
//		return "Returns the list of mobs in range around a radar.";
//	}
//
//	@Override
//	public String getArgsAsString() {
//		return "";
//	}
//
//	@Override
//	public ReturnType getReturnType() {
//		return ReturnType.ARRAY;
//	}
//
//}
