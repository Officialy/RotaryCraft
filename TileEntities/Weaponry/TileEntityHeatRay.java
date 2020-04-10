/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.TileEntities.Weaponry;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import Reika.DragonAPI.Interfaces.Block.SemiTransparent;
import Reika.RotaryCraft.API.Event.HeatRayNetherDetonationEvent;
import Reika.RotaryCraft.API.Interfaces.Laserable;
import Reika.RotaryCraft.Auxiliary.Interfaces.RangedEffect;
import Reika.RotaryCraft.Base.TileEntity.TileEntityBeamMachine;
import Reika.RotaryCraft.Registry.ConfigRegistry;
import Reika.RotaryCraft.Registry.MachineRegistry;
import Reika.RotaryCraft.Registry.RotaryAchievements;

public class TileEntityHeatRay extends TileEntityBeamMachine implements RangedEffect {

	/** Rate of conversion - one power++ = 1/falloff ++ blocks range */
	public static final int FALLOFF = 256;

	@Override
	public void updateEntity(World world, int x, int y, int z, int meta) {
		super.updateTileEntity();
		tickcount++;
		power = (long)omega*(long)torque;
		this.getIOSides(world, x, y, z, meta);
		this.getPower(false);
		//if ((world.getTotalWorldTime()&2) == 2) //halves load
		this.makeBeam(world, x, y, z, meta);
	}

	@Override
	protected void makeBeam(World world, int x, int y, int z, int metadata) {
		boolean blocked = false;
		int step;
		if (power >= MINPOWER) { //2MW+ (real military laser)
			int maxdist = this.getRange();
			for (step = 1; step < maxdist && (step < this.getMaxRange() || this.getMaxRange() == -1) && !blocked; step++) {
				int dx = x+step*facing.offsetX;
				int dy = y+step*facing.offsetY;
				int dz = z+step*facing.offsetZ;
				Block id = world.getBlock(dx, dy, dz);
				int meta2 = world.getBlockMetadata(dx, dy, dz);
				if (id != Blocks.air && id.isFlammable(world, dx, dy, dz, ForgeDirection.UP))
					this.ignite(world, dx, dy, dz, metadata, step);
				//ReikaJavaLibrary.pConsole(Blocks.blocksList[id]);
				if (this.affectBlock(world, dx, dy, dz, step, id, meta2, maxdist)) {
					blocked = true;
					tickcount = 0;
				}
				if (id != world.getBlock(dx, dy, dz) || meta2 != world.getBlockMetadata(dx, dy, dz))
					world.markBlockForUpdate(dx, dy, dz);
				if (id instanceof SemiTransparent) {
					SemiTransparent st = (SemiTransparent)id;
					if (st.isOpaque(meta2))
						blocked = true;
				}
				else if (id.isOpaqueCube())
					blocked = true; //break loop
			}
			AxisAlignedBB zone = this.getBurnZone(metadata, step);
			List<Entity> inzone = worldObj.getEntitiesWithinAABB(Entity.class, zone);
			for (Entity caught : inzone) {
				if (!(caught instanceof EntityItem)) //Do not burn drops
					caught.setFire(this.getBurnTime());	// 1 Hearts worth of fire at min power, +1 heart for every 65kW extra
				if (caught instanceof EntityTNTPrimed)
					world.spawnParticle("lava", caught.posX+rand.nextFloat(), caught.posY+rand.nextFloat(), caught.posZ+rand.nextFloat(), 0, 0, 0);
				if (caught instanceof Laserable) {
					((Laserable)caught).whenInBeam(world, MathHelper.floor_double(caught.posX), MathHelper.floor_double(caught.posY), MathHelper.floor_double(caught.posZ), power, step);
				}
			}
		}
	}

	public int getBurnTime() {
		return 2+(int)(16L*power/MINPOWER);
	}

	public int getRange() {
		int r = (int)(8L+(power-MINPOWER)/FALLOFF);
		if (r > this.getMaxRange())
			return this.getMaxRange();
		return r;
	}

	private AxisAlignedBB getBurnZone(int meta, int step) {
		int minx = 0;
		int miny = 0;
		int minz = 0;
		int maxx = 0;
		int maxy = 0;
		int maxz = 0;

		switch (meta) {
			case 0:
				minx = xCoord-step;
				maxx = xCoord-1;
				miny = yCoord;
				maxy = yCoord;
				minz = zCoord;
				maxz = zCoord;
				break;
			case 1:
				minx = xCoord+1;
				maxx = xCoord+step;
				miny = yCoord;
				maxy = yCoord+1;
				minz = zCoord;
				maxz = zCoord+1;
				break;
			case 2:
				maxz = zCoord+step;
				minz = zCoord+1;
				miny = yCoord;
				maxy = yCoord+1;
				minx = xCoord;
				maxx = xCoord+1;
				break;
			case 3:
				maxz = zCoord-1;
				minz = zCoord-step;
				miny = yCoord;
				maxy = yCoord+1;
				minx = xCoord;
				maxx = xCoord+1;
				break;
			case 4:
				miny = yCoord;
				maxz = zCoord+1;
				miny = yCoord+1;
				maxy = yCoord+step;
				minx = xCoord;
				maxx = xCoord+1;
				break;
			case 5:
				minz = zCoord;
				maxz = zCoord+1;
				miny = yCoord-1;
				maxy = yCoord-step-1;
				minx = xCoord;
				maxx = xCoord+1;
				break;
		}
		/*ReikaWorldHelper.legacySetBlockWithNotify(this.worldObj, minx, miny, minz, 20);
    	ReikaWorldHelper.legacySetBlockWithNotify(this.worldObj, minx, maxy, minz, 20);
    	ReikaWorldHelper.legacySetBlockWithNotify(this.worldObj, maxx, maxy, maxz, 20);
    	ReikaWorldHelper.legacySetBlockWithNotify(this.worldObj, maxx, miny, maxz, 20);*/
		return AxisAlignedBB.getBoundingBox(minx, miny, minz, maxx, maxy, maxz);//.expand(0.25D, 0.25D, 0.25D);
	}

	private void ignite(World world, int x, int y, int z, int metadata, int step) {
		if (world.getBlock(x+1, y, z) == Blocks.air)
			world.setBlock(x+1, y, z, Blocks.fire);
		if (world.getBlock(x-1, y, z) == Blocks.air)
			world.setBlock(x-1, y, z, Blocks.fire);
		if (world.getBlock(x, y+1, z) == Blocks.air)
			world.setBlock(x, y+1, z, Blocks.fire);
		if (world.getBlock(x, y-1, z) == Blocks.air)
			world.setBlock(x, y-1, z, Blocks.fire);
		if (world.getBlock(x, y, z+1) == Blocks.air)
			world.setBlock(x, y, z+1, Blocks.fire);
		if (world.getBlock(x, y, z-1) == Blocks.air)
			world.setBlock(x, y, z-1, Blocks.fire);
	}

	private boolean affectBlock(World world, int dx, int dy, int dz, int step, Block id, int metadata, int maxdist) {
		boolean value = false;
		if (id == Blocks.air)
			return false;
		if (id.hasTileEntity(metadata)) {
			TileEntity te = world.getTileEntity(dx, dy, dz);
			if (te instanceof Laserable) {
				((Laserable)te).whenInBeam(world, dx, dy, dz, power, step);
				if (((Laserable)te).blockBeam(world, dx, dy, dz, power))
					return true;
			}
		}
		if (ConfigRegistry.ATTACKBLOCKS.getState()) {
			if (id == Blocks.stone || id == Blocks.cobblestone || id == Blocks.stonebrick || id == Blocks.sandstone) {
				int chance = (int)((power-MINPOWER)/(1024 * step * 2));
				chance = Math.max(chance, 1);
				if (rand.nextInt(chance) != 0)
					if (rand.nextInt(step) == 0)
						world.setBlock(dx, dy, dz, Blocks.flowing_lava);
				world.spawnParticle("lava", dx+rand.nextFloat(), dy+rand.nextFloat(), dz+rand.nextFloat(), 0, 0, 0);
			}
			if (id == Blocks.sand) {
				int chance = (int)((power-MINPOWER)/(1024 * step * 1));
				chance = Math.max(chance, 1);
				if (rand.nextInt(chance) != 0)
					if (rand.nextInt(step) == 0)
						world.setBlock(dx, dy, dz, Blocks.glass);
			}
			if (id == Blocks.gravel) {
				int chance = (int)((power-MINPOWER)/(1024 * step * 1));
				chance = Math.max(chance, 1);
				if (rand.nextInt(chance) != 0)
					if (rand.nextInt(step) == 0)
						world.setBlock(dx, dy, dz, Blocks.cobblestone);
			}/*
    	if (id == Blocks.netherrack) {
    		if (world.getBlock(dx, 1+dy, dz) == 0) {
    			world.setBlock(dx, 1+dy, dz, Blocks.fire);
    		}
    	}*/
			if (id == Blocks.netherrack && tickcount >= 6) {
				world.newExplosion(null, dx+0.5, dy+0.5, dz+0.5, 3F, true, true);
				MinecraftForge.EVENT_BUS.post(new HeatRayNetherDetonationEvent(world, dx, dy, dz));
				if (world.provider.dimensionId == -1 && step >= 500)
					RotaryAchievements.NETHERHEATRAY.triggerAchievement(this.getPlacer());
				step = maxdist;
				value = true;
			}
			if (id == Blocks.dirt || id == Blocks.farmland) {
				int chance = (int)((power-MINPOWER)/(1024 * step * 1));
				chance = Math.max(chance, 1);
				if (rand.nextInt(chance) != 0)
					if (rand.nextInt(step) == 0)
						world.setBlock(dx, dy, dz, Blocks.sand);
			}
			if (id == Blocks.grass || id == Blocks.mycelium) {
				int chance = (int)((power-MINPOWER)/(1024 * step * 2));
				chance = Math.max(chance, 1);
				if (rand.nextInt(chance) != 0)
					if (rand.nextInt(step) == 0)
						world.setBlock(dx, dy, dz, Blocks.dirt);
			}
			if (id == Blocks.ice || id == Blocks.snow) {
				int chance = (int)((power-MINPOWER)/(1024 * step / 4));
				chance = Math.max(chance, 1);
				if (rand.nextInt(chance) != 0)
					if (rand.nextInt(step) == 0)
						world.setBlock(dx, dy, dz, Blocks.flowing_water);
			}
			if (id == Blocks.tallgrass || id == Blocks.web || id == Blocks.yellow_flower || id == Blocks.snow ||
					id == Blocks.red_flower || id == Blocks.red_mushroom || id == Blocks.brown_mushroom ||
					id == Blocks.deadbush || id == Blocks.wheat || id == Blocks.carrots || id == Blocks.potatoes || id == Blocks.vine ||
					id == Blocks.melon_stem || id == Blocks.pumpkin_stem || id == Blocks.waterlily) {
				int chance = (int)((power-MINPOWER)/(1024 * step / 8));
				chance = Math.max(chance, 1);
				if (rand.nextInt(chance) != 0)
					if (rand.nextInt(step) == 0) {
						world.setBlockToAir(dx, dy, dz);
						if (id == Blocks.snow)
							world.playSoundEffect(dx + 0.5D, dy + 0.5D, dz + 0.5D, "random.fizz", 0.5F, 2.6F + (rand.nextFloat() - rand.nextFloat()) * 0.8F);
					}
			}
			if (id == Blocks.flowing_water || id == Blocks.water) {
				//ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", id));
				int chance = (int)((power-MINPOWER)/(1024 * step / 8));
				chance = Math.max(chance, 1);
				if (rand.nextInt(chance) != 0)
					if (rand.nextInt(step) == 0) {
						world.setBlockToAir(dx, dy, dz);
						world.playSoundEffect(dx + 0.5D, dy + 0.5D, dz + 0.5D, "random.fizz", 0.5F, 2.6F + (rand.nextFloat() - rand.nextFloat()) * 0.8F);
					}
			}
			if (id == Blocks.tnt) {
				world.setBlockToAir(dx, dy, dz);
				EntityTNTPrimed var6 = new EntityTNTPrimed(world, dx+0.5D, dy+0.5D, dz+0.5D, null);
				if (!world.isRemote)
					world.spawnEntityInWorld(var6);
				world.playSoundAtEntity(var6, "random.fuse", 1.0F, 1.0F);
				world.spawnParticle("lava", dx+rand.nextFloat(), dy+rand.nextFloat(), dz+rand.nextFloat(), 0, 0, 0);
			}
			if (id instanceof Laserable) {
				((Laserable)id).whenInBeam(world, dx, dy, dz, power, step);
				if (((Laserable)id).blockBeam(world, dx, dy, dz, power))
					return true;
			}

		}
		return value;
	}

	@Override
	public boolean hasModelTransparency() {
		return false;
	}

	@Override
	protected void animateWithTick(World world, int x, int y, int z) {

	}

	@Override
	public MachineRegistry getMachine() {
		return MachineRegistry.HEATRAY;
	}

	@Override
	public int getMaxRange() {
		return Math.max(64, ConfigRegistry.HEATRAYRANGE.getValue());
	}

	@Override
	public int getRedstoneOverride() {
		return 0;
	}
}
