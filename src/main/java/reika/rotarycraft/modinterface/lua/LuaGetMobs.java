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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enderman;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.libraries.ReikaEntityHelper;
import reika.dragonapi.modinteract.lua.LuaMethod;
import reika.rotarycraft.blockentities.surveying.BlockEntityMobRadar;

public class LuaGetMobs extends LuaMethod {

	public LuaGetMobs() {
		super("getMobs", BlockEntityMobRadar.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		List<LivingEntity> li = ((BlockEntityMobRadar)te).getEntities();
		ArrayList<Object[]> entities = new ArrayList<>();
		for (LivingEntity e : li) {
			entities.add(this.encodeEntity(e));
		}
		return entities.toArray(new Object[entities.size()]);
	}

	private Object[] encodeEntity(LivingEntity e) {
		ArrayList<Object> params = new ArrayList<>();
		//1.7.10 class names all carried the "Entity" prefix ("EntityCreeper", "EntityPlayer"); kept so scripts still match
		params.add(e instanceof Player ? "EntityPlayer" : "Entity"+e.getClass().getSimpleName());
		params.add(e.getX());
		params.add(e.getY());
		params.add(e.getZ());
		//1.7.10 returned the UUID object, which the computer could not represent; its string form carries the same data
		params.add(e.getStringUUID());
		params.add(e.getHealth()*100F/e.getMaxHealth());
		if (e instanceof Creeper c) {
			params.add(ReikaEntityHelper.getCreeperFuse(c));
			params.add(ReikaEntityHelper.isCreeperCharged(c));
		}
		if (e instanceof AbstractSkeleton) {
			//1.7.10 getSkeletonType(): 0 normal, 1 wither (now its own entity)
			params.add(e instanceof WitherSkeleton ? 1 : 0);
		}
		if (e instanceof AbstractCubeMob slime) {
			params.add(slime.getSize());
		}
		if (e instanceof ZombifiedPiglin pz) {
			params.add(ReikaEntityHelper.isPigZombieAngry(pz));
		}
		if (e instanceof Villager v) {
			//1.7.10 getProfession() was a numeric id; professions are registry entries now
			params.add(v.getVillagerData().profession().getRegisteredName());
		}
		if (e instanceof Enderman em) {
			BlockState carried = em.getCarriedBlock();
			//1.7.10: carried block name, then its metadata (block state properties since the flattening)
			params.add(carried != null ? BuiltInRegistries.BLOCK.getKey(carried.getBlock()).toString() : "minecraft:air");
			params.add(carried != null ? carried.toString() : "");
		}
		return params.toArray(new Object[params.size()]);
	}

	@Override
	public String getDocumentation() {
		return "Returns the list of mobs in range around a radar.";
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
