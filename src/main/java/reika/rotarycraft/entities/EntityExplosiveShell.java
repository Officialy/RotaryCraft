/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
/*package reika.rotarycraft.entities;

import java.util.List;

import reika.dragonapi.libraries.registry.ReikaParticleHelper;
import reika.rotarycraft.registry.init.MachineRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import reika.rotarycraft.api.Interfaces.TargetEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntityExplosiveShell extends EntityRailgunShotBase {

	public static final float EXPLOSION = 8F;

	public EntityExplosiveShell(Level world) {
		super(world);
	}

	public EntityExplosiveShell(Level world, double x, double y, double z, BlockPos vpos, BlockEntityRailGun r) {
		super(world, pos, 0, 0, 0, r, ItemExplosiveShell.ExplosiveRailGunAmmo.instance);
		motionX = vx;
		motionY = vy;
		motionZ = vz;
		//ReikaJavaLibrary.pConsole(vx+" , "+vy+" , "+vz);
		if (!world.isClientSide)
			velocityChanged = true;
	}

	@Override
	public void onHit(HitResult mov) {
		if (mov != null && MachineRegistry.getMachine(level, mov.blockX, mov.blockY, mov.blockZ) == MachineRegistry.RAILGUN) {
			this.kill();
			return;
		}
		if (isAlive())
			return;
		Level world = level;
		double x = getY;
		double y = getY();
		double z = posZ;
		int x0 = (int)x;
		int y0 = (int)y;
		int z0 = (int)z;
		//ReikaChatHelper.writeCoords(world, pos);
		//ReikaChatHelper.writeBlockAtCoords(world, x0, y0, z0);
		world.newExplosion(this, x0, y0, z0, EXPLOSION, true, true);
		AABB box = AABB.getBoundingBox(pos, pos).expand(8, 8, 8);
		for (Entity e : ((List<Entity>)level.getEntities(Entity.class, box))) {
			this.applyAttackEffectsToEntity(world, e);
		}
		this.kill();
		//ent.hurt(DamageSource.outOfWorld, el.getHealth()*(1+el.getTotalArmorValue()));
	}

	@Override
	protected int getAttackDamage() {
		return 0;
	}

	@Override
	protected void applyAttackEffectsToEntity(Level world, Entity el) {
		if (el instanceof TargetEntity) {
			((TargetEntity)el).onRailgunImpact(gun, this.getType());
		}
	}

	@Override
	public void tick() {
		tickCount++;
		boolean hit = false;
		int x = Mth.floor(getY);
		int y = Mth.floor(getY());
		int z = Mth.floor(posZ);
		Block id = level.getBlock(pos);
		MachineRegistry m = MachineRegistry.getMachine(level, getY, getY(), getZ);
		List mobs = level.getEntities(LivingEntity.class, this.getBoundingBox().expand(1, 1, 1));
		//ReikaJavaLibrary.pConsole("ID: "+id+" and "+mobs.size()+" mobs");
		hit = (mobs.size() > 0 || (m != MachineRegistry.RAILGUN && id != Blocks.AIR && !ReikaWorldHelper.softBlocks(level, pos)));
		//ReikaJavaLibrary.pConsole(hit+"   by "+id+"  or mobs "+mobs.size());
		if (hit) {
			//ReikaChatHelper.write("HIT  @  "+tickCount+"  by "+(mobs.size() > 0));
			this.onHit(null);
			this.kill();
			return;
		}

		if (!level.isClientSide && (shootingEntity != null && shootingEntity.isAlive() || !level.blockExists(pos)))
			this.kill();
		else {
			if (tickCount > 80 && !level.isClientSide)
				this.onHit(null);
			this.onEntityUpdate();
			Vec3 var15 = Vec3.createVectorHelper(getY, getY(), getZ);
			Vec3 var2 = Vec3.createVectorHelper(getY + motionX, getY() + motionY, getZ + motionZ);
			HitResult var3 = level.rayTraceBlocks(var15, var2);
			var15 = Vec3.createVectorHelper(getY, getY(), getZ);
			var2 = Vec3.createVectorHelper(getY + motionX, getY() + motionY, getZ + motionZ);
			if (var3 != null)
				var2 = Vec3.createVectorHelper(var3.getLocation().xCoord, var3.getLocation().yCoord, var3.getLocation().zCoord);
			Entity var4 = null;
			List var5 = level.getEntitiesExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
			double var6 = 0.0D;
			for (int var8 = 0; var8 < var5.size(); ++var8) {
				Entity var9 = (Entity)var5.get(var8);
				if (var9.canBeCollidedWith() && (!var9.isEntityEqual(shootingEntity))) {
					float var10 = 0.3F;
					AABB var11 = var9.boundingBox.expand(var10, var10, var10);
					HitResult var12 = var11.calculateIntercept(var15, var2);
					if (var12 != null) {
						double var13 = var15.distanceTo(var12.getLocation());
						if (var13 < var6 || var6 == 0.0D) {
							var4 = var9;
							var6 = var13;
						}
					}
				}
			}
			if (var4 != null)
				var3 = new HitResult(var4);
			if (var3 != null)
				this.onHit(var3);
			getY += motionX;
			getY() += motionY;
			posZ += motionZ;
			if (this.isInWater()) {
				level.explode(this, getY, getY(), getZ, 3F, false);
				for (int var19 = 0; var19 < 4; ++var19) {
					float var18 = 0.25F;
					ReikaParticleHelper.BUBBLE.spawnAt(level, getY - motionX * var18, getY() - motionY * var18, getZ - motionZ * var18, motionX, motionY, motionZ);
				}
			}
			this.setPosition(getY, getY(), getZ);
		}


		//ReikaJavaLibrary.pConsole(motionX);
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {

	}

	@Override
	public void readSpawnData(FriendlyByteBuf additionalData) {

	}
}
*/