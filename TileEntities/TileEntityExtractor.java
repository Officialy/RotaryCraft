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

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import Reika.DragonAPI.Libraries.ReikaBlockHelper;
import Reika.DragonAPI.Libraries.ReikaInventoryHelper;
import Reika.DragonAPI.Libraries.ReikaMathLibrary;
import Reika.RotaryCraft.MachineRegistry;
import Reika.RotaryCraft.RotaryCraft;
import Reika.RotaryCraft.Auxiliary.ItemStacks;
import Reika.RotaryCraft.Auxiliary.RecipesExtractor;
import Reika.RotaryCraft.Base.RotaryModelBase;
import Reika.RotaryCraft.Base.TileEntityInventoriedPowerReceiver;
import Reika.RotaryCraft.Models.ModelExtractor;

public class TileEntityExtractor extends TileEntityInventoriedPowerReceiver
{
	private ItemStack inv[] = new ItemStack[9];

	/** The number of ticks that the current item has been cooking for */
	public int[] extractorCookTime = new int[4];

	public int waterLevel = 0;
	public static final int CAPACITY = 16;

	public boolean idle = false;

	public void testIdle() {
		for (int i = 0; i < 4; i++)
			if (power < machine.getMinPower(i))
				return;
		boolean works = false;
		for (int i = 0; i < 4; i++) {
			if (this.canSmelt(i))
				works = true;
		}
		idle = !works;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return i == 7 || i == 8;
	}

	public int getSmeltNumber(int num) {
		if (num == 63)
			return 1;
		return (1+par5Random.nextInt(2));
	}

	public void throughPut() {
		for (int i = 1; i < 4; i++) {
			if (inv[i+3] != null) {
				if (inv[i] == null) {
					inv[i] = inv[i+3];
					inv[i+3] = null;
				}
				else if (inv[i].stackSize < inv[i].getMaxStackSize()) {
					if (inv[i].itemID == inv[i+3].itemID && inv[i].getItemDamage() == inv[i+3].getItemDamage()) {
						inv[i].stackSize++;
						ReikaInventoryHelper.decrStack(i+3, inv);
					}
				}
			}
		}
	}

	public void getLiq(World world, int x, int y, int z, int metadata) {
		int oldLevel = 0;
		if (waterLevel < CAPACITY) {
			if (MachineRegistry.getMachine(world, x+1, y, z) == MachineRegistry.PIPE) {
				TileEntityPipe tile = (TileEntityPipe)world.getBlockTileEntity(x+1, y, z);
				if (tile != null && (tile.liquidID == 8 || tile.liquidID == 9) && tile.liquidLevel > 0) {
					oldLevel = tile.liquidLevel;
					tile.liquidLevel = ReikaMathLibrary.extrema(tile.liquidLevel-tile.liquidLevel/4-1, 0, "max");
					waterLevel = ReikaMathLibrary.extrema(waterLevel+oldLevel/4+1, 0, "max");
				}
			}
			if (MachineRegistry.getMachine(world, x-1, y, z) == MachineRegistry.PIPE) {
				TileEntityPipe tile = (TileEntityPipe)world.getBlockTileEntity(x-1, y, z);
				if (tile != null && (tile.liquidID == 8 || tile.liquidID == 9) && tile.liquidLevel > 0) {
					oldLevel = tile.liquidLevel;
					tile.liquidLevel = ReikaMathLibrary.extrema(tile.liquidLevel-tile.liquidLevel/4-1, 0, "max");
					waterLevel = ReikaMathLibrary.extrema(waterLevel+oldLevel/4+1, 0, "max");
				}
			}
			if (MachineRegistry.getMachine(world, x, y+1, z) == MachineRegistry.PIPE) {
				TileEntityPipe tile = (TileEntityPipe)world.getBlockTileEntity(x, y+1, z);
				if (tile != null && (tile.liquidID == 8 || tile.liquidID == 9) && tile.liquidLevel > 0) {
					oldLevel = tile.liquidLevel;
					tile.liquidLevel = ReikaMathLibrary.extrema(tile.liquidLevel-tile.liquidLevel/4-1, 0, "max");
					waterLevel = ReikaMathLibrary.extrema(waterLevel+oldLevel/4+1, 0, "max");
				}
			}
			if (MachineRegistry.getMachine(world, x, y-1, z) == MachineRegistry.PIPE) {
				TileEntityPipe tile = (TileEntityPipe)world.getBlockTileEntity(x, y-1, z);
				if (tile != null && (tile.liquidID == 8 || tile.liquidID == 9) && tile.liquidLevel > 0) {
					oldLevel = tile.liquidLevel;
					tile.liquidLevel = ReikaMathLibrary.extrema(tile.liquidLevel-tile.liquidLevel/4-1, 0, "max");
					waterLevel = ReikaMathLibrary.extrema(waterLevel+oldLevel/4+1, 0, "max");
				}
			}
			if (MachineRegistry.getMachine(world, x, y, z+1) == MachineRegistry.PIPE) {
				TileEntityPipe tile = (TileEntityPipe)world.getBlockTileEntity(x, y, z+1);
				if (tile != null && (tile.liquidID == 8 || tile.liquidID == 9) && tile.liquidLevel > 0) {
					oldLevel = tile.liquidLevel;
					tile.liquidLevel = ReikaMathLibrary.extrema(tile.liquidLevel-tile.liquidLevel/4-1, 0, "max");
					waterLevel = ReikaMathLibrary.extrema(waterLevel+oldLevel/4+1, 0, "max");
				}
			}
			if (MachineRegistry.getMachine(world, x, y, z-1) == MachineRegistry.PIPE) {
				TileEntityPipe tile = (TileEntityPipe)world.getBlockTileEntity(x, y, z-1);
				if (tile != null && (tile.liquidID == 8 || tile.liquidID == 9) && tile.liquidLevel > 0) {
					oldLevel = tile.liquidLevel;
					tile.liquidLevel = ReikaMathLibrary.extrema(tile.liquidLevel-tile.liquidLevel/4-1, 0, "max");
					waterLevel = ReikaMathLibrary.extrema(waterLevel+oldLevel/4+1, 0, "max");
				}
			}
		}
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	public int getSizeInventory()
	{
		return inv.length;
	}

	public static boolean func_52005_b(ItemStack par0ItemStack)
	{
		return true;
	}

	/**
	 * Returns the stack in slot i
	 */
	public ItemStack getStackInSlot(int par1)
	{
		return inv[par1];
	}

	/**
	 * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
	 * stack.
	 */
	public ItemStack decrStackSize(int par1, int par2)
	{
		if (inv[par1] != null)
		{
			if (inv[par1].stackSize <= par2)
			{
				ItemStack itemstack = inv[par1];
				inv[par1] = null;
				return itemstack;
			}

			ItemStack itemstack1 = inv[par1].splitStack(par2);

			if (inv[par1].stackSize <= 0)
			{
				inv[par1] = null;
			}

			return itemstack1;
		}
		else
		{
			return null;
		}
	}

	/**
	 *
	 *
	 */
	public ItemStack getStackInSlotOnClosing(int par1)
	{
		if (inv[par1] != null)
		{
			ItemStack itemstack = inv[par1];
			inv[par1] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	/**
	 *
	 */
	public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
	{
		inv[par1] = par2ItemStack;

		if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
		{
			par2ItemStack.stackSize = this.getInventoryStackLimit();
		}
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound NBT)
	{
		super.readFromNBT(NBT);
		NBTTagList nbttaglist = NBT.getTagList("Items");
		inv = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < nbttaglist.tagCount(); i++)
		{
			NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.tagAt(i);
			byte byte0 = nbttagcompound.getByte("Slot");

			if (byte0 >= 0 && byte0 < inv.length)
			{
				inv[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound);
			}
		}

		extractorCookTime = NBT.getIntArray("CookTime");
		waterLevel = NBT.getInteger("water");
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound NBT)
	{
		super.writeToNBT(NBT);
		NBT.setIntArray("CookTime", extractorCookTime);
		NBT.setInteger("water", waterLevel);
		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < inv.length; i++)
		{
			if (inv[i] != null)
			{
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte)i);
				inv[i].writeToNBT(nbttagcompound);
				nbttaglist.appendTag(nbttagcompound);
			}
		}

		NBT.setTag("Items", nbttaglist);
	}

	/**
	 * Returns an integer between 0 and the passed value representing how close the current item is to being completely
	 * cooked
	 */
	public int getCookProgressScaled(int par1, int i)
	{
		int j = i+1;
		int time = -1;
		switch (j) {
		case 1:
			time = 30*(30-(int)(2*ReikaMathLibrary.logbase(omega, 2)));
			break;
		case 2:
			time = (800-(int)(40*ReikaMathLibrary.logbase(omega, 2)))/2;
			break;
		case 3:
			time = 600-(int)(30*ReikaMathLibrary.logbase(omega, 2));
			break;
		case 4:
			time = 1200-(int)(80*ReikaMathLibrary.logbase(omega, 2));
			break;
		}
		if (time == -1)
			return 0;
		if (time <= 0)
			time = 1;
		return (extractorCookTime[i] * par1)/2 / time;
	}

	/**
	 * Allows the entity to update its state. Overridden in most subclasses, e.g. the mob spawner uses this to count
	 * ticks and creates a new spawn inside its implementation.
	 */
	@Override
	public void updateEntity(World world, int x, int y, int z, int meta)
	{
		super.updateTileEntity();
		//this.getPower(false);
		//this.getReceptor(world, x, y, z, meta);
		this.getPowerBelow();
		this.testIdle();
		this.throughPut();
		this.getLiq(world, x, y, z, meta);
		for (int i = 0; i < 4; i++) {
			boolean flag1 = false;
			//ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.valueOf(canSmelt(i)));
			if (!worldObj.isRemote) {
				if (this.canSmelt(i)) {
					flag1 = true;/*
	                if (inv[i] != null) {
	                    if (inv[i].getItem().func_46056_k())
	                        inv[i] = new ItemStack(inv[i].getItem().setFull3D());
	                    else
	                        inv[i].stackSize--;
	                    if (inv[i].stackSize == 0)
	                        inv[i] = null;
	                }*/
				}
				if (this.canSmelt(i)) {
					extractorCookTime[i]++;
					//ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.format("%d", ReikaMathLibrary.extrema(2, 1200-this.omega, "max")));
					if (this.operationComplete(extractorCookTime[i], i+1)) {
						extractorCookTime[i] = 0;
						this.smeltItem(i);
						flag1 = true;
					}
				}
				else
					extractorCookTime[i] = 0;
			}
			if (flag1)
				this.onInventoryChanged();
		}
		//ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.valueOf(canSmelt(0))+"  "+String.valueOf(canSmelt(1))+"  "+String.valueOf(canSmelt(2))+"  "+String.valueOf(canSmelt(3)));
	}

	/**
	 * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
	 */
	private boolean canSmelt(int i)
	{
		//readPower();
		//ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d  %d  %d  %d", this.power, this.torque, this.omega, i));

		if (power < machine.getMinPower(i) || omega < machine.getMinSpeed(i) || torque < machine.getMinTorque(i))
			return false;
		//ModLoader.getMinecraftInstance().thePlayer.addChatMessage("75");
		if ((i == 1 || i == 2) && waterLevel < 1)
			return false;
		//ModLoader.getMinecraftInstance().thePlayer.addChatMessage("45");

		//ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("DSFFD");

		if (inv[i] == null)
			return false;
		ItemStack itemstack = RecipesExtractor.smelting().getSmeltingResult(inv[i]);
		//ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.format("%d", itemstack.itemID));
		if (itemstack == null) {
			//ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("!!!!@#");
			return false;
		}
		//ModLoader.getMinecraftInstance().ingameGUI.addChatMessage("45656");
		if (inv[i+4] == null)
			return true;
		if (!inv[i+4].isItemEqual(itemstack))
			return false;
		if (inv[i+4].stackSize < this.getInventoryStackLimit() && inv[i+4].stackSize < inv[i+4].getMaxStackSize())
			return true;
		return inv[i+4].stackSize < itemstack.getMaxStackSize();
	}

	/**
	 * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
	 */
	public void smeltItem(int i)
	{
		if (!this.canSmelt(i))
			return;
		ItemStack itemstack = RecipesExtractor.smelting().getSmeltingResult(inv[i]);
		if (inv[i+4] == null) {
			inv[i+4] = itemstack.copy();
			inv[i+4].stackSize *= this.getSmeltNumber(0);
		}
		else if (inv[i+4].itemID == itemstack.itemID)
			inv[i+4].stackSize += this.getSmeltNumber(inv[i+4].stackSize);

		if (i == 3)
			this.bonusItems(inv[i]);

		// if (inv[i].getItem().func_46056_k())
		//   inv[i] = new ItemStack(inv[i].getItem().setFull3D());
		// else
		inv[i].stackSize--;
		if (par5Random.nextInt(8) == 0)
			if (i == 1 || i == 2)
				waterLevel--;

		if (inv[i].stackSize <= 0)
			inv[i] = null;/*
        if (i == 3) {
        	int xp = 0;
        	switch(inv[i+4].getItemDamage()) {

        	}
        	ReikaWorldHelper.splitAndSpawnXP(worldObj, xCoord, yCoord, zCoord, xp);
        }*/
	}

	private void bonusItems(ItemStack is) {
		if (is == null)
			return;
		if (is.itemID == RotaryCraft.extracts.itemID) {
			if (is.getItemDamage() == ItemStacks.goldoresolution.getItemDamage()) {
				if (par5Random.nextInt(8) == 0)
					ReikaInventoryHelper.addOrSetStack(ItemStacks.silverflakes.itemID, 1, ItemStacks.silverflakes.getItemDamage(), inv, 8);
			}
			if (is.getItemDamage() == ItemStacks.ironoresolution.getItemDamage()) {
				if (par5Random.nextInt(8) == 0)
					ReikaInventoryHelper.addOrSetStack(ItemStacks.aluminumpowder.itemID, 1, ItemStacks.aluminumpowder.getItemDamage(), inv, 8);
			}
		}
	}

	private boolean isValidModOre() {
		return false;
	}

	private void processModOre(ItemStack is) {
		if (is == null || is.itemID != RotaryCraft.modextracts.itemID)
			return;

	}

	@Override
	public boolean hasModelTransparency() {
		return true;
	}

	@Override
	public RotaryModelBase getTEModel(World world, int x, int y, int z) {
		return new ModelExtractor();
	}

	@Override
	public void animateWithTick(World world, int x, int y, int z) {

	}

	@Override
	public int getMachineIndex() {
		return MachineRegistry.EXTRACTOR.ordinal();
	}

	@Override
	public boolean isStackValidForSlot(int slot, ItemStack is) {
		return ReikaBlockHelper.isOre(is.itemID) && slot == 0;
	}

	@Override
	public int getRedstoneOverride() {
		if (!this.canSmelt(0) && !this.canSmelt(1) && !this.canSmelt(2) && !this.canSmelt(3))
			return 15;
		return 0;
	}
}