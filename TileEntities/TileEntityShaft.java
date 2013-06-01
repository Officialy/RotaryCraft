/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2013
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.TileEntities;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import Reika.DragonAPI.Libraries.ReikaEngLibrary;
import Reika.DragonAPI.Libraries.ReikaMathLibrary;
import Reika.RotaryCraft.MachineRegistry;
import Reika.RotaryCraft.Auxiliary.EnumMaterials;
import Reika.RotaryCraft.Auxiliary.ItemStacks;
import Reika.RotaryCraft.Base.RotaryCraftTileEntity;
import Reika.RotaryCraft.Base.RotaryModelBase;
import Reika.RotaryCraft.Base.TileEntity1DTransmitter;
import Reika.RotaryCraft.Models.ModelShaft;
import Reika.RotaryCraft.Models.ModelShaftV;

public class TileEntityShaft extends TileEntity1DTransmitter {
	public int[] readtorque = new int[2];
	public int[] readomega = new int[2];
	public boolean reading2Dir = false; //Is reading a 2-direction block (splitter, cross)

	public float crossphi1 = 0;
	public float crossphi2 = 0;

	public EnumMaterials type;
	public boolean failed = false;

	public void fail(World world, int x, int y, int z, boolean speed) {
		failed = true;
		if (speed) {
			world.createExplosion(null, x+0.5, y+0.5, z+0.5, 1F, true);
			ItemStack item = null;
			switch(type) {
			case WOOD:
				item = new ItemStack(ItemStacks.sawdust.itemID, 1, ItemStacks.sawdust.getItemDamage());
				break;
			case STONE:
				item = new ItemStack(Block.gravel, 1, 0);
				break;
			case STEEL:
				item = new ItemStack(ItemStacks.scrap.itemID, 1, ItemStacks.scrap.getItemDamage());
				break;
			case DIAMOND:
				item = new ItemStack(Item.diamond, 1, 0);
				break;
			case BEDROCK:
				item = new ItemStack(ItemStacks.bedrockdust.itemID, 1, ItemStacks.bedrockdust.getItemDamage());
				break;
			}
			EntityItem ei = new EntityItem(world, x+0.5, y+1.25, z+0.5, item);
			ei.motionY = 0.4F+0.6F*par5Random.nextFloat();
			ei.motionX = par5Random.nextFloat()/5;
			ei.motionZ = par5Random.nextFloat()/5;
			if (world.isRemote)
				return;
			ei.velocityChanged = true;
			world.spawnEntityInWorld(ei);
		}
		else {
			world.playSoundEffect(x+0.5, y+0.5, z+0.5, "random.break", 1F, 1F);
			ItemStack item = null;
			switch(type) {
			case WOOD:
				item = new ItemStack(Item.stick, 1, 0);
				break;
			case STONE:
				item = new ItemStack(Block.cobblestone, 1, 0);
				break;
			case STEEL:
				item = new ItemStack(ItemStacks.shaftitem.itemID, 1, ItemStacks.shaftitem.getItemDamage());
				break;
			case DIAMOND:
				item = new ItemStack(Item.diamond, 1, 0);
				break;
			case BEDROCK:
				item = new ItemStack(ItemStacks.bedrockdust.itemID, 1, ItemStacks.bedrockdust.getItemDamage());
				break;
			}
			EntityItem ei = new EntityItem(world, x+0.5, y+1, z+0.5, item);
			ei.motionY = par5Random.nextFloat()/5;
			ei.motionX = par5Random.nextFloat()/5;
			ei.motionZ = par5Random.nextFloat()/5;
			if (world.isRemote)
				return;
			ei.velocityChanged = true;
			world.spawnEntityInWorld(ei);
		}
	}

	public void repair() {
		failed = false;
	}

	public void testFailure() {
		if (type.isInfiniteStrength())
			return;
		//ReikaChatHelper.write(this.getStrength(false));
		if (ReikaEngLibrary.mat_rotfailure(type.getDensity(), 0.0625, ReikaMathLibrary.doubpow(omega, 1-(0.11D*type.ordinal())), type.getTensileStrength())) {
			this.fail(worldObj, xCoord, yCoord, zCoord, true);
		}
		else if (ReikaEngLibrary.mat_twistfailure(torque, 0.0625, type.getShearStrength())) {
			this.fail(worldObj, xCoord, yCoord, zCoord, false);
		}
	}

	//No read/write y2 since vertical shafts will not have cross equivalent
	//(no way to make them look structurally sound)

	public void crossReadFromCross(TileEntityShaft cross, int dir) {
		reading2Dir = true;
		if (xCoord == cross.writex && zCoord == cross.writez) {/*
    		if (cross.reading2Dir && false) {

    		}
    		else {*/
			readomega[dir] = cross.readomega[0];
			readtorque[dir] = cross.readtorque[0];
			//ReikaChatHelper.writeInt(cross.readomega[0]);
			//}
		}
		else if (xCoord == cross.writex2 && zCoord == cross.writez2) {/*
    		if (cross.reading2Dir && false) {

    		}
    		else {*/
			readomega[dir] = cross.readomega[1];
			readtorque[dir] = cross.readtorque[1];
			//ReikaChatHelper.writeInt(cross.readomega[1]);
			//}
		}
		else
			return; //not its output
	}

	public void readFromCross(TileEntityShaft cross) {
		reading2Dir = true;
		if (xCoord == cross.writex && zCoord == cross.writez) {
			omega = cross.readomega[0];
			torque = cross.readtorque[0];
		}
		else if (xCoord == cross.writex2 && zCoord == cross.writez2) {
			omega = cross.readomega[1];
			torque = cross.readtorque[1];
		}
		else
			return; //not its output
	}

	private void crossReadFromSplitter(TileEntitySplitter spl, int dir) {
		reading2Dir = true;
		int ratio = spl.getRatioFromMode();
		if (ratio == 0)
			return;
		boolean favorbent = false;
		if (ratio < 0) {
			favorbent = true;
			ratio = -ratio;
		}
		if (xCoord == spl.writeinline[0] && zCoord == spl.writeinline[1]) { //We are the inline
			readomega[dir] = spl.omega; //omega always constant
			//ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("INLINE!  %d  %d  FOR %d", spl.omega, spl.torque, ratio));
			if (ratio == 1) { //Even split, favorbent irrelevant
				readtorque[dir] = spl.torque/2;
				//ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d  %d", this.readtorque[dir], this.readomega[dir]));
				return;
			}
			if (favorbent) {
				readtorque[dir] = spl.torque/ratio;
			}
			else {
				readtorque[dir] = (int)(spl.torque*((ratio-1D)/(ratio)));
			}
		}
		else if (xCoord == spl.writebend[0] && zCoord == spl.writebend[1]) { //We are the bend
			readomega[dir] = spl.omega; //omega always constant
			//ModLoader.getMinecraftInstance().thePlayer.addChatMessage("BEND!");
			if (ratio == 1) { //Even split, favorbent irrelevant
				readtorque[dir] = spl.torque/2;
				return;
			}
			if (favorbent) {
				readtorque[dir] = (int)(spl.torque*((ratio-1D)/(ratio)));
			}
			else {
				readtorque[dir] = spl.torque/ratio;
			}
		}
		else //We are not one of its write-to blocks
			return;
	}

	public void readFromSplitter(TileEntitySplitter spl) { //Complex enough to deserve its own function
		reading2Dir = true;
		int ratio = spl.getRatioFromMode();
		if (ratio == 0)
			return;
		boolean favorbent = false;
		if (ratio < 0) {
			favorbent = true;
			ratio = -ratio;
		}
		if (xCoord == spl.writeinline[0] && zCoord == spl.writeinline[1]) { //We are the inline
			omega = spl.omega; //omega always constant
			//ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("INLINE!  %d  %d  FOR %d", spl.omega, spl.torque, ratio));
			if (ratio == 1) { //Even split, favorbent irrelevant
				torque = spl.torque/2;
				//ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d  %d", this.torque, this.omega));
				return;
			}
			if (favorbent) {
				torque = spl.torque/ratio;
			}
			else {
				torque = (int)(spl.torque*((ratio-1D)/(ratio)));
			}
		}
		else if (xCoord == spl.writebend[0] && zCoord == spl.writebend[1]) { //We are the bend
			omega = spl.omega; //omega always constant
			//ModLoader.getMinecraftInstance().thePlayer.addChatMessage("BEND!");
			if (ratio == 1) { //Even split, favorbent irrelevant
				torque = spl.torque/2;
				return;
			}
			if (favorbent) {
				torque = (int)(spl.torque*((ratio-1D)/(ratio)));
			}
			else {
				torque = spl.torque/ratio;
			}
		}
		else  {	//We are not one of its write-to blocks
			omega = torque = 0;
			power = 0;
			return;
		}
	}

	@Override
	public void updateEntity(World world, int x, int y, int z, int meta) {
		super.updateTileEntity();
		if (type == null)
			type = EnumMaterials.STEEL;
		ratio = 1;
		if (failed) {
			torque = 0;
			omega = 0;
			power = 0;
			return;
		}
		this.testFailure();
		power = omega*torque;
		int id = world.getBlockId(x, y, z);

		this.getIOSides(world, x, y, z, meta);
		this.transferPower(world, x, y, z, meta);
		//ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", this.ratio));
	}

	public void getIOSides(World world, int x, int y, int z, int meta) {
		switch(meta) {
		case 0:
			readx = x+1;
			writex = x-1;
			readz = writez = z;
			ready = writey = y;
			break;
		case 1:
			readx = x-1;
			writex = x+1;
			readz = writez = z;
			ready = writey = y;
			break;
		case 2:
			readz = z+1;
			writez = z-1;
			readx = writex = x;
			ready = writey = y;
			break;
		case 3:
			readz = z-1;
			writez = z+1;
			readx = writex = x;
			ready = writey = y;
			break;
		case 4:	//moving up
			readx = writex = x;
			readz = writez = z;
			ready = y-1;
			writey = y+1;
			break;
		case 5:	//moving down
			readx = writex = x;
			readz = writez = z;
			ready = y+1;
			writey = y-1;
			break;
		case 6:	//cross - has 4 states
			readx = x+1;
			writex = x-1;
			readz = z;
			writez = z;
			readx2 = x;
			readz2 = z+1;
			writex2 = x;
			writez2 = z-1;
			ready = writey = y;
			break;
		case 7:	//cross - has 4 states
			readx = x+1;
			writex = x-1;
			readz = z;
			writez = z;
			readx2 = x;
			readz2 = z-1;
			writex2 = x;
			writez2 = z+1;
			ready = writey = y;
			break;
		case 8:	//cross - has 4 states
			readx = x-1;
			writex = x+1;
			readz = z;
			writez = z;
			readx2 = x;
			readz2 = z-1;
			writex2 = x;
			writez2 = z+1;
			ready = writey = y;
			break;
		case 9:	//cross - has 4 states
			readx = x-1;
			writex = x+1;
			readz = z;
			writez = z;
			readx2 = x;
			readz2 = z+1;
			writex2 = x;
			writez2 = z-1;
			ready = writey = y;
			break;
		}
	}

	private void crossTransfer(World world, int ratio) {
		ready = ready2 = ready;
		readomega[0] = 0;
		readomega[1] = 0;
		readtorque[0] = 0;
		readtorque[1] = 0;
		TileEntity te1 = worldObj.getBlockTileEntity(readx, ready, readz);
		TileEntity te2 = worldObj.getBlockTileEntity(readx2, ready2, readz2);
		if (this.isProvider(te1) && this.isIDTEMatch(world, readx, ready, readz)) {
			MachineRegistry m = MachineRegistry.machineList[((RotaryCraftTileEntity)(te1)).getMachineIndex()];
			if (m == MachineRegistry.SHAFT) {
				TileEntityShaft devicein = (TileEntityShaft)world.getBlockTileEntity(readx, yCoord, readz);
				if (devicein.getBlockMetadata() >= 6) {
					this.crossReadFromCross(devicein, 0);
					return;
				}
				else if (devicein.writex == xCoord && devicein.writey == yCoord && devicein.writez == zCoord) {
					readomega[0] = devicein.omega;
					readtorque[0] = devicein.torque;
				}
			}
			if (m == MachineRegistry.ENGINE) {
				TileEntityEngine devicein = (TileEntityEngine)world.getBlockTileEntity(readx, yCoord, readz);
				if ((devicein.writex == xCoord && devicein.writez == zCoord)) {
					readtorque[0] = devicein.torque;
					readomega[0] = devicein.omega;
				}
			}
			if (m == MachineRegistry.WINDER) {
				TileEntityWinder devicein = (TileEntityWinder)world.getBlockTileEntity(readx, yCoord, readz);
				if ((devicein.writex == xCoord && devicein.writez == zCoord)) {
					readtorque[0] = devicein.torque;
					readomega[0] = devicein.omega;
				}
			}
			if (m == MachineRegistry.ADVANCEDGEARS) {
				TileEntityAdvancedGear devicein = (TileEntityAdvancedGear)world.getBlockTileEntity(readx, yCoord, readz);
				if ((devicein.writex == xCoord && devicein.writez == zCoord)) {
					readtorque[0] = devicein.torque;
					readomega[0] = devicein.omega;
				}
			}
			if (m == MachineRegistry.FLYWHEEL) {
				TileEntityFlywheel devicein = (TileEntityFlywheel)world.getBlockTileEntity(readx, yCoord, readz);
				if (devicein.writex == xCoord && devicein.writez == zCoord) {
					readtorque[0] = devicein.torque;
					readomega[0] = devicein.omega;
				}
			}
			if (m == MachineRegistry.CLUTCH) {
				TileEntityClutch devicein = (TileEntityClutch)world.getBlockTileEntity(readx, yCoord, readz);
				if ((devicein.writex == xCoord && devicein.writez == zCoord)) {
					readtorque[0] = devicein.torque;
					readomega[0] = devicein.omega;
				}
			}
			if (m == MachineRegistry.GEARBOX) {
				TileEntityGearbox devicein = (TileEntityGearbox)world.getBlockTileEntity(readx, yCoord, readz);
				if ((devicein.writex == xCoord && devicein.writez == zCoord)) {
					readtorque[0] = devicein.torque;
					readomega[0] = devicein.omega;
				}
			}
			if (m == MachineRegistry.DYNAMOMETER) {
				TileEntityMonitor devicein = (TileEntityMonitor)world.getBlockTileEntity(readx, yCoord, readz);
				if ((devicein.writex == xCoord && devicein.writez == zCoord)) {
					readtorque[0] = devicein.torque;
					readomega[0] = devicein.omega;
				}
			}
			if (m == MachineRegistry.BEVELGEARS) {
				TileEntityGearBevel devicein = (TileEntityGearBevel)world.getBlockTileEntity(readx, yCoord, readz);
				if (devicein.writex == xCoord && devicein.writey == yCoord && devicein.writez == zCoord) {
					readtorque[0] = devicein.torque;
					readomega[0] = devicein.omega;
				}
			}
			if (m == MachineRegistry.SPLITTER) {
				TileEntitySplitter devicein = (TileEntitySplitter)world.getBlockTileEntity(readx, yCoord, readz);
				if (devicein.getBlockMetadata() >= 8) {
					this.crossReadFromSplitter(devicein, 0);
					return;
				}
				else if (devicein.writex == xCoord && devicein.writez == zCoord) {
					readtorque[0] = devicein.torque;
					readomega[0] = devicein.omega;
				}
			}
		}
		if (this.isProvider(te2) && this.isIDTEMatch(world, readx2, ready2, readz2)) {
			MachineRegistry m2 = MachineRegistry.machineList[((RotaryCraftTileEntity)(te2)).getMachineIndex()];
			if (m2 == MachineRegistry.SHAFT) {
				TileEntityShaft devicein2 = (TileEntityShaft)world.getBlockTileEntity(readx2, yCoord, readz2);
				if (devicein2.getBlockMetadata() >= 6) {
					this.crossReadFromCross(devicein2, 1);
					return;
				}
				else if (devicein2.writex == xCoord && devicein2.writey == yCoord && devicein2.writez == zCoord) {
					readomega[1] = devicein2.omega;
					readtorque[1] = devicein2.torque;
				}
			}
			if (m2 == MachineRegistry.WINDER) {
				TileEntityWinder devicein2 = (TileEntityWinder)world.getBlockTileEntity(readx2, yCoord, readz2);
				if ((devicein2.writex == xCoord && devicein2.writez == zCoord)) {
					readtorque[1] = devicein2.torque;
					readomega[1] = devicein2.omega;
				}
			}
			if (m2 == MachineRegistry.ADVANCEDGEARS) {
				TileEntityAdvancedGear devicein2 = (TileEntityAdvancedGear)world.getBlockTileEntity(readx2, yCoord, readz2);
				if ((devicein2.writex == xCoord && devicein2.writez == zCoord)) {
					readtorque[1] = devicein2.torque;
					readomega[1] = devicein2.omega;
				}
			}
			if (m2 == MachineRegistry.ENGINE) {
				TileEntityEngine devicein2 = (TileEntityEngine)world.getBlockTileEntity(readx2, yCoord, readz2);
				if ((devicein2.writex == xCoord && devicein2.writez == zCoord)) {
					readtorque[1] = devicein2.torque;
					readomega[1] = devicein2.omega;
				}
			}
			if (m2 == MachineRegistry.CLUTCH) {
				TileEntityClutch devicein2 = (TileEntityClutch)world.getBlockTileEntity(readx2, yCoord, readz2);
				if ((devicein2.writex == xCoord && devicein2.writez == zCoord)) {
					readtorque[1] = devicein2.torque;
					readomega[1] = devicein2.omega;
				}
			}
			if (m2 == MachineRegistry.FLYWHEEL) {
				TileEntityFlywheel devicein2 = (TileEntityFlywheel)world.getBlockTileEntity(readx, yCoord, readz);
				if (devicein2.writex == xCoord && devicein2.writez == zCoord) {
					readtorque[1] = devicein2.torque;
					readomega[0] = devicein2.omega;
				}
			}
			if (m2 == MachineRegistry.DYNAMOMETER) {
				TileEntityMonitor devicein2 = (TileEntityMonitor)world.getBlockTileEntity(readx2, yCoord, readz2);
				if ((devicein2.writex == xCoord && devicein2.writez == zCoord)) {
					readtorque[1] = devicein2.torque;
					readomega[1] = devicein2.omega;
				}
			}
			if (m2 == MachineRegistry.GEARBOX) {
				TileEntityGearbox devicein2 = (TileEntityGearbox)world.getBlockTileEntity(readx2, yCoord, readz2);
				if ((devicein2.writex == xCoord && devicein2.writez == zCoord)) {
					readtorque[1] = devicein2.torque;
					readomega[1] = devicein2.omega;
				}
			}
			if (m2 == MachineRegistry.BEVELGEARS) {
				TileEntityGearBevel devicein2 = (TileEntityGearBevel)world.getBlockTileEntity(readx2, yCoord, readz2);
				if (devicein2.writex == xCoord && devicein2.writey == yCoord && devicein2.writez == zCoord) {
					readtorque[1] = devicein2.torque;
					readomega[1] = devicein2.omega;
				}
			}
			if (m2 == MachineRegistry.SPLITTER) {
				TileEntitySplitter devicein2 = (TileEntitySplitter)world.getBlockTileEntity(readx2, yCoord, readz2);
				if (devicein2.getBlockMetadata() >= 8) {
					this.crossReadFromSplitter(devicein2, 1);
					return;
				}
				else if (devicein2.writex == xCoord && devicein2.writez == zCoord) {
					readtorque[1] = devicein2.torque;
					readomega[1] = devicein2.omega;
				}
			}
		}
	}

	@Override
	public void transferPower(World world, int x, int y, int z, int meta) {
		reading2Dir = false;
		if (meta >= 6) {
			this.crossTransfer(world, ratio);
			return;
		}
		omegain = torquein = 0;
		TileEntity te = worldObj.getBlockTileEntity(readx, ready, readz);
		if (!this.isProvider(te) || !this.isIDTEMatch(world, readx, ready, readz)) {
			omega = 0;
			torque = 0;
			power = 0;
			return;
		}
		MachineRegistry m = MachineRegistry.machineList[((RotaryCraftTileEntity)(te)).getMachineIndex()];
		if (m == MachineRegistry.SHAFT) {
			TileEntityShaft devicein = (TileEntityShaft)world.getBlockTileEntity(readx, ready, readz);
			if (world.getBlockMetadata(readx, ready, readz) >= 6) {
				this.readFromCross(devicein);
				return;
			}
			else if (devicein.writex == x && devicein.writey == y && devicein.writez == z) {
				torquein = devicein.torque;
				omegain = devicein.omega;
			}
		}
		if (m == MachineRegistry.ENGINE) {
			TileEntityEngine devicein = (TileEntityEngine)world.getBlockTileEntity(readx, ready, readz);
			if ((devicein.writex == x && devicein.writez == z)) {
				torquein = devicein.torque;
				omegain = devicein.omega;
			}
		}
		if (m == MachineRegistry.WINDER) {
			TileEntityWinder devicein = (TileEntityWinder)world.getBlockTileEntity(readx, ready, readz);
			if ((devicein.writex == x && devicein.writez == z)) {
				torquein = devicein.torque;
				omegain = devicein.omega;
			}
		}
		if (m == MachineRegistry.FLYWHEEL) {
			TileEntityFlywheel devicein = (TileEntityFlywheel)world.getBlockTileEntity(readx, y, readz);
			if (devicein.writex == x && devicein.writez == z) {
				torquein = devicein.torque;
				omegain = devicein.omega;
			}
		}
		if (m == MachineRegistry.ADVANCEDGEARS) {
			TileEntityAdvancedGear devicein = (TileEntityAdvancedGear)world.getBlockTileEntity(readx, ready, readz);
			if ((devicein.writex == x && devicein.writez == z)) {
				torquein = devicein.torque;
				omegain = devicein.omega;
			}
		}
		if (m == MachineRegistry.SPLITTER) {
			TileEntitySplitter devicein = (TileEntitySplitter)world.getBlockTileEntity(readx, ready, readz);
			if (devicein.getBlockMetadata() >= 8) {
				this.readFromSplitter(devicein);
				return;
			}
			else if (devicein.writex == x && devicein.writez == z) {
				torquein = devicein.torque;
				omegain = devicein.omega;
			}
		}
		if (m == MachineRegistry.CLUTCH) {
			TileEntityClutch devicein = (TileEntityClutch)world.getBlockTileEntity(readx, ready, readz);
			if (devicein.writex == x && devicein.writez == z) {
				torquein = devicein.torque;
				omegain = devicein.omega;
			}
		}
		if (m == MachineRegistry.DYNAMOMETER) {
			TileEntityMonitor devicein = (TileEntityMonitor)world.getBlockTileEntity(readx, ready, readz);
			if (devicein.writex == x && devicein.writez == z) {
				torquein = devicein.torque;
				omegain = devicein.omega;
			}
		}
		if (m == MachineRegistry.GEARBOX) {
			TileEntityGearbox devicein = (TileEntityGearbox)world.getBlockTileEntity(readx, ready, readz);
			if (devicein.writex == x && devicein.writez == z) {
				torquein = devicein.torque;
				omegain = devicein.omega;
			}
		}
		if (m == MachineRegistry.BEVELGEARS) {
			TileEntityGearBevel devicein = (TileEntityGearBevel)world.getBlockTileEntity(readx, ready, readz);
			if (devicein.writex == x && devicein.writey == y && devicein.writez == z) {
				torquein = devicein.torque;
				omegain = devicein.omega;
			}
		}

		omega = omegain / ratio;
		torque = torquein * ratio;
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound NBT)
	{
		super.writeToNBT(NBT);
		NBT.setBoolean("failed", failed);
		if (type != null)
			NBT.setInteger("type", type.ordinal());
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound NBT)
	{
		super.readFromNBT(NBT);
		failed = NBT.getBoolean("failed");
		type = EnumMaterials.setType(NBT.getInteger("type"));
	}

	@Override
	public boolean hasModelTransparency() {
		return false;
	}

	@Override
	public RotaryModelBase getTEModel(World world, int x, int y, int z) {
		int dmg = world.getBlockMetadata(x, y, z);
		if (dmg < 4)
			return new ModelShaft();
		else
			return new ModelShaftV();
	}

	@Override
	public void animateWithTick(World world, int x, int y, int z) {
		if (!this.isInWorld()) {
			phi = 0;
			return;
		}
		phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega+1, 2), 1.25);

		crossphi1 += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(readomega[0]+1, 2), 1.25);
		crossphi2 += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(readomega[1]+1, 2), 1.25);
	}

	@Override
	public int getMachineIndex() {
		return MachineRegistry.SHAFT.ordinal();
	}

	@Override
	public int getRedstoneOverride() {
		return 0;
	}
}