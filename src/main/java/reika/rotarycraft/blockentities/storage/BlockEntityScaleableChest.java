///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.storage;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.phys.AABB;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.interfaces.blockentity.MultiPageInventory;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerScaleChest;
//
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//
//public class BlockEntityScaleableChest extends InventoriedPowerReceiver implements MultiPageInventory {
//
//    public static final int FALLOFF = 128;
//    public static final int MAXROWS = 6;
//    public static final int MAXSIZE = 972;
//
//    public static final int POWERCHANGEAGE = 20; //1s
//    private final ArrayList<Integer> powerchanges = new ArrayList<Integer>();
//    private final int resetTick = 0;
//    /**
//     * The current angle of the lid (between 0 and 1)
//     */
//    public float lidAngle;
//    /**
//     * The angle of the lid last tick
//     */
//    public float prevLidAngle;
//    public int numUsingPlayers;
//    public int page;
//    private int numchanges;
//    private boolean lastpower;
//
//    public static int getMaxPages() {
//        int size = MAXSIZE;
//        size /= 9;
//        size /= MAXROWS;
//        return size;
//    }
//
//    public boolean dropsInventoryOnBroken() {
//        return false;
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return true;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return MAXSIZE;
//    }
//
//    public int getNumberSlots() {
//        int size;
//        if (power < MINPOWER)
//            size = 9;
//        else
//            size = 9 + (int) (power - MINPOWER) / FALLOFF;
//        if (size >= itemHandler.getSlots())
//            size = itemHandler.getSlots();
//        return size;
//    }
//
//    public int getNumPowerChanges() {
//        return numchanges;
//    }
//
//    public int getMaxPage() {
//        int size = this.getNumberSlots();
//        size /= 9D;
//        size /= MAXROWS;
//        return Math.min(getMaxPages(), (int) Math.ceil(size));
//    }
//
//    @Override
//    public int getNumberPages() {
//        return this.getMaxPage();
//    }
//
//    @Override
//    public int getSlotsOnPage(int page) {
//        int max = this.getMaxPage();
//        if (page == max)
//            return this.getNumberSlots() - max * 9 * MAXROWS;
//        else if (page < max)
//            return 9 * MAXROWS;
//        else
//            return 0;
//    }
//
//    public int getCurrentPage() {
//        return page;
//    }
//
//    private boolean testInconsistentPower() {
//        for (int i = 0; i < powerchanges.size(); i++) {
//            int b = powerchanges.get(i);
//            b++;
//            powerchanges.set(i, b);
//        }
//        for (Iterator itr = powerchanges.iterator(); itr.hasNext(); ) {
//            int c = (Integer) itr.next();
//            if (c > POWERCHANGEAGE)
//                itr.remove();
//        }
//        boolean pw = (power >= MINPOWER);
//        if (pw != lastpower) {
//            int a = 0;
//            powerchanges.add(a);
//        }
//        numchanges = powerchanges.size();
//        lastpower = pw;
//        if (numchanges > 10 && !level.isClientSide) {
//            this.getBlockEntityBlockID().dropBlockAsItem(level, xCoord, yCoord, zCoord, this.getMachineIndex(), 0);
//            level.setBlockToAir(xCoord, yCoord, zCoord);
//            level.explode(null, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 4F, RotaryConfig.COMMON.BLOCKDAMAGE.getState());
//        } else if (numchanges > 8) {
//            for (int i = 0; i < numchanges / 3; i++)
//                level.addParticle("smoke", xCoord + DragonAPI.rand.nextFloat(), yCoord + DragonAPI.rand.nextFloat(), zCoord + DragonAPI.rand.nextFloat(), 0, 0, 0);
//            if (DragonAPI.rand.nextInt(19 - numchanges) == 0)
//                level.explode(null, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 0F, false);
//        } else if (numchanges > 3) {
//            for (int i = 0; i < numchanges / 3; i++)
//                level.addParticle("smoke", xCoord + DragonAPI.rand.nextFloat(), yCoord + DragonAPI.rand.nextFloat(), zCoord + DragonAPI.rand.nextFloat(), 0, 0, 0);
//            if (DragonAPI.rand.nextInt(11 - numchanges) == 0)
//                level.playLocalSound(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, "DragonAPI.rand.fizz", 1F, 1F);
//        } else
//            return false;
//        return true;
//    }
//
//    @Override
//    public boolean isUseableByPlayer(Player ep) {
//        if (numchanges > 0)
//            return false;
//        if (power < MINPOWER)
//            return false;
//        return super.isPlayerAccessible(ep);
//    }
//
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getIOSides(world, pos, dir);
//        this.getPower(false);
//        if (this.testInconsistentPower())
//            return;
//        if (power < MINPOWER) {
//            lidAngle = 0;
//            prevLidAngle = 0;
//            return;
//        }
//        float f;
//        if (!level.isClientSide && numUsingPlayers != 0) {
//            numUsingPlayers = 0;
//            f = 5.0F;
//            List list = level.getEntities(Player.class, AABB.getBoundingBox(xCoord - f, yCoord - f, zCoord - f, xCoord + 1 + f, yCoord + 1 + f, zCoord + 1 + f));
//            Iterator iterator = list.iterator();
//            while (iterator.hasNext()) {
//                Player Player = (Player) iterator.next();
//                if (Player.openContainer instanceof ContainerScaleChest) {
//                    Container iinventory = ((ContainerScaleChest) Player.openContainer).getLowerScaleChestInventory();
//                    if (iinventory == this)
//                        ++numUsingPlayers;
//                }
//            }
//        }
//        prevLidAngle = lidAngle;
//        f = 0.1F;
//        double d0;
//        if (numUsingPlayers > 0 && lidAngle == 0.0F) {
//            double d1 = xCoord + 0.5D;
//            d0 = zCoord + 0.5D;
//            level.playLocalSound(d1, yCoord + 0.5D, d0, "DragonAPI.rand.chestopen", 0.5F, level.DragonAPI.rand.nextFloat() * 0.1F + 0.9F);
//        }
//        if (numUsingPlayers == 0 && lidAngle > 0.0F || numUsingPlayers > 0 && lidAngle < 1.0F) {
//            float f1 = lidAngle;
//            if (numUsingPlayers > 0)
//                lidAngle += f;
//            else
//                lidAngle -= f;
//            if (lidAngle > 1.0F)
//                lidAngle = 1.0F;
//            float f2 = 0.5F;
//            if (lidAngle < f2 && f1 >= f2) {
//                d0 = xCoord + 0.5D;
//                double d2 = zCoord + 0.5D;
//                level.playLocalSound(d0, yCoord + 0.5D, d2, "DragonAPI.rand.chestclosed", 0.5F, level.DragonAPI.rand.nextFloat() * 0.1F + 0.9F);
//            }
//            if (lidAngle < 0.0F)
//                lidAngle = 0.0F;
//        }
//    }
//
//    public void getIOSides(Level world, BlockPos pos, int metadata) {
//        switch (metadata) {
//            case 0:
//                read = Direction.EAST;
//                break;
//            case 1:
//                read = Direction.WEST;
//                break;
//            case 3:
//                read = Direction.SOUTH;
//                break;
//            case 2:
//                read = Direction.NORTH;
//                break;
//        }
//    }
//
//    @Override
//    public boolean receiveClientEvent(int par1, int par2) {
//        if (par1 == 1) {
//            numUsingPlayers = par2;
//            return true;
//        } else
//            return super.receiveClientEvent(par1, par2);
//    }
//
//    @Override
//    public void openInventory() {
//        if (power < MINPOWER)
//            return;
//        if (numUsingPlayers < 0)
//            numUsingPlayers = 0;
//        ++numUsingPlayers;
//        level.addBlockEvent(xCoord, yCoord, zCoord, this.getBlockEntityBlockID(), 1, numUsingPlayers);
//        level.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, this.getBlockEntityBlockID());
//        level.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, this.getBlockEntityBlockID());
//    }
//
//    @Override
//    public void closeInventory() {
//        --numUsingPlayers;
//        level.addBlockEvent(xCoord, yCoord, zCoord, this.getBlockEntityBlockID(), 1, numUsingPlayers);
//        level.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, this.getBlockEntityBlockID());
//        level.notifyBlocksOfNeighborChange(xCoord, yCoord - 1, zCoord, this.getBlockEntityBlockID());
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        NBT.putInt("chng", numchanges);
//        NBT.putInt("player", numUsingPlayers);
//
//        NBT.putInt("pg", page);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        numchanges = NBT.getInt("chng");
//        numUsingPlayers = NBT.getInt("player");
//
//        page = NBT.getInt("pg");
//    }
//
//    public void writeInventoryToItem(ItemStack is) {
//        is.put(new CompoundTag());
//
//        ListTag nbttaglist = new ListTag();
//
//        for (int i = 0; i < itemHandler.getSlots(); i++) {
//            if (itemHandler.getStackInSlot(i).isEmpty()) {
//                CompoundTag CompoundTag = new CompoundTag();
//                CompoundTag.putShort("Slot", (short) i);
//                itemHandler.getStackInSlot(i).saveAdditional(CompoundTag);
//                nbttaglist.add(CompoundTag);
//                //ReikaJavaLibrary.pConsole(i+":"+itemHandler.getStackInSlot(i));
//            }
//        }
//
//        is.getTag().put("Items", nbttaglist);
//    }
//
//    public void readInventoryFromItem(ItemStack is) {
//        if (is.getTag() != null) {
//            ListTag nbttaglist = is.getTag().getTagList("Items", NBTTypes.COMPOUND.ID);
//            inv = new ItemStack[this.getContainerSize()];
//
//            for (int i = 0; i < nbttaglist.size(); i++) {
//                CompoundTag CompoundTag = nbttaglist.getCompound(i);
//                short byte0 = CompoundTag.getShort("Slot");
//
//                if (byte0 >= 0 && byte0 < itemHandler.getSlots()) {
//                    inv[byte0] = ItemStack.of(CompoundTag);
//                } else {
//                    RotaryCraft.LOGGER.error(this + " tried to load an inventory slot " + byte0 + " from NBT!");
//                }
//            }
//        }
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.SCALECHEST;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return slot < this.getNumberSlots();
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 15 * this.getContainerSize() / MAXSIZE;
//    }
//}
