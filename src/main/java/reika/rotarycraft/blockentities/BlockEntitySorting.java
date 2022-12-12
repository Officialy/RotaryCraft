///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities;
//
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.entity.player.Inventory;
//import org.jetbrains.annotations.NotNull;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//import reika.rotarycraft.registry.MachineRegistry;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.phys.AABB;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BlockEntitySorting extends BlockEntityPowerReceiver {
//
//    public static final int LENGTH = 9;
//
//    private Direction facingDir;
//
//    private ItemStack[] mappings = new ItemStack[LENGTH * 3];
//
//    public static byte getSlot(int col, int side) {
//        return (byte) (side * 3 + col);
//    }
//
//    public static int[] getParams(int slot) {
//        int l = LENGTH;
//        int[] p = new int[2];
//        p[0] = slot % l;
//        p[1] = slot / l;
//        return p;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        this.getIOSides(world, pos);
//        this.getPower(false);
//        if (!world.isClientSide) {
//            if (power >= MINPOWER) {
//                List<ItemCallback> li = this.getItems(world, pos);
//                this.sortItems(world, pos, li);
//            }
//        }
//        //ReikaJavaLibrary.pConsole(this.getSide()+": "+Arrays.deepToString(mappings));
//    }
//
//    private List<ItemCallback> getItems(Level world, BlockPos pos) {
//        BlockEntity te = getAdjacentBlockEntity(Direction.UP);
//        List<ItemCallback> li = new ArrayList<>();
//        if (te instanceof Inventory) {
//            Inventory ii = (Inventory) te;
//            for (int i = 0; i < ii.getContainerSize(); i++) {
//                ItemStack in = ii.getStackInSlot(i);
//                if (in != null) {
//                    li.add(new InventoryItemCallback(ii, i));
//                }
//            }
//        } else {
//            AABB box = this.getBox();
//            List<ItemEntity> items = world.getEntities(ItemEntity.class, box);
//            for (ItemEntity ei : items) {
//                if (!ei.isAlive())
//                    li.add(new EntityItemCallback(ei));
//            }
//        }
//        return li;
//    }
//
//    public void getIOSides(Level world, BlockPos pos) {
//        switch (metadata) {
//            case 0:
//                facingDir = Direction.EAST;
//                break;
//            case 1:
//                facingDir = Direction.WEST;
//                break;
//            case 2:
//                facingDir = Direction.NORTH;
//                break;
//            case 3:
//                facingDir = Direction.SOUTH;
//                break;
//        }
//        read = facingDir;
//    }
//
//    private void sortItems(Level world, BlockPos pos, List<ItemCallback> li) {
//        int n = this.getNumberCyclesPerTick();
//        for (ItemCallback ei : li) {
//            ItemStack is = ei.getStack();
//            for (int i = 0; i < n && is.getCount() > 0; i++) {
//                if (is.getCount() <= 1) {
//                    ei.destroy();
//                }
//                is.setCount(is.getCount() - 1);
//                Direction dir = this.getSideForItem(is);
//                this.dumpItem(world, pos, is, dir);
//            }
//        }
//    }
//
//    private int getNumberCyclesPerTick() {
//        long frac = power / MINPOWER;
//        if (frac == 1) {
//            return 1;
//        } else if (frac >= 16) {
//            return 64;
//        } else if (frac >= 12) {
//            return 32;
//        } else if (frac >= 8) {
//            return 16;
//        } else if (frac >= 4) {
//            return 4;
//        } else if (frac >= 2) {
//            return 2;
//        } else {
//            return 1;
//        }
//    }
//
//    private void dumpItem(Level world, BlockPos pos, ItemStack is, Direction dir) {
//        ItemStack drop = ReikaItemHelper.getSizedItemStack(is, 1);
//        BlockEntity te = getAdjacentBlockEntity(dir);
//        if (te instanceof Container) {
//            if (ReikaInventoryHelper.addToIInv(drop, (Container) te))
//                return;
//        }
//        double dx = x + 0.5 + dir.getStepX() * 0.75;
//        double dy = y + 0.5 + dir.getStepY() * 0.75;
//        double dz = z + 0.5 + dir.getStepZ() * 0.75;
//        ItemEntity e = new ItemEntity(world, dx, dy, dz, drop);
//        double v = 0.1;
//        e.motionX = dir.getStepX() * v;
//        e.motionY = dir.getStepY() * v;
//        e.motionZ = dir.getStepZ() * v;
//        world.addFreshEntity(e);
//    }
//
//    private Direction getSideForItem(ItemStack is) {
//        for (int k = 0; k < mappings.length; k++) {
//            ItemStack map = mappings[k];
//            if (map != null) {
//                Item item = is.getItem();
//                Item item2 = map.getItem();
//                if (item.getHasSubtypes() || item2.getHasSubtypes()) {
//                    if (ReikaItemHelper.matchStacks(map, is))
//                        return this.getDirection(k);
//                } else {
//                    if (is.getItem() == map.getItem())
//                        return this.getDirection(k);
//                }
//            }
//        }
//        return Direction.DOWN;
//    }
//
//    private Direction getDirection(int index) {
//        index /= LENGTH;
//        List<Direction> li = new ArrayList<>();
//        for (int i = 2; i < 6; i++)
//            li.add(dirs[i]);
//        li.remove(facingDir);
//        return li.get(index);
//    }
//
//    private Direction getFacingDir() {
//        switch (meta) {
//            case 0:
//                return Direction.EAST;
//            case 1:
//                return Direction.WEST;
//            case 2:
//                return Direction.NORTH;
//            case 3:
//                return Direction.SOUTH;
//        }
//        return Direction.DOWN;
//    }
//
//    private AABB getBox() {
//        return AABB.getBoundingBox(worldPosition.above(), xCoord + 1, yCoord + 1.25, zCoord + 1);
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.SORTING;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    public boolean setMapping(int index, ItemStack item) {
//        if (item == null) {
//            mappings[index] = null;
//            return true;
//        } else {
//            if (this.isValidForSlot(index, item)) {
//                Item i = item.getItem();
//                if (i.getHasSubtypes())
//                    mappings[index] = new ItemStack(item.getItem(), 1, item.getItemDamage());
//                else
//                    mappings[index] = new ItemStack(item.getItem(), 1, 0);
//                return true;
//            } else
//                return false;
//        }
//    }
//
//    public ItemStack getMapping(int index) {
//        return mappings[index];
//    }
//
//    private boolean isValidForSlot(int index, ItemStack item) {
//        for (int i = 0; i < mappings.length; i++) {
//            ItemStack is = mappings[i];
//            //ReikaJavaLibrary.pConsole(is);
//            if (is != null) {
//                if (ReikaItemHelper.matchStacks(item, is))
//                    return false;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        ListTag nbttaglist = NBT.getTagList("Items", NBTTypes.COMPOUND.ID);
//        mappings = new ItemStack[LENGTH * 3];
//
//        for (int i = 0; i < nbttaglist.size(); i++) {
//            CompoundTag CompoundTag = nbttaglist.getCompound(i);
//            byte byte0 = CompoundTag.getByte("Slot");
//
//            if (byte0 >= 0 && byte0 < mappings.length) {
//                mappings[byte0] = ItemStack.of(CompoundTag);
//            }
//        }
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        ListTag nbttaglist = new ListTag();
//
//        for (int i = 0; i < mappings.length; i++) {
//            if (mappings[i] != null) {
//                CompoundTag CompoundTag = new CompoundTag();
//                CompoundTag.setByte("Slot", (byte) i);
//                mappings[i].saveAdditional(CompoundTag);
//                nbttaglist.add(CompoundTag);
//            }
//        }
//
//        NBT.put("Items", nbttaglist);
//
//    }
//
//    @Override
//    public TextureFetcher getRenderer() {
//        return null;
//    }
//
//    @Override
//    public BlockPos getWritePos(BlockPos pos) {
//        return null;
//    }
//
//    @Override
//    public BlockPos getWritePos2(BlockPos pos) {
//        return null;
//    }
//
//    @Override
//    public String getName() {
//        return null;
//    }
//
//    @Override
//    public boolean hasAnInventory() {
//        return false;
//    }
//
//    @Override
//    public boolean hasATank() {
//        return false;
//    }
//
//    @Override
//    public Level getWorld() {
//        return null;
//    }
//
//    @Override
//    public BlockPos getPos() {
//        return null;
//    }
//
//    @Override
//    public int getSlots() {
//        return 0;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack getStackInSlot(int slot) {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack extractItem(int slot, int amount, boolean simulate) {
//        return null;
//    }
//
//    @Override
//    public int getSlotLimit(int slot) {
//        return 0;
//    }
//
//    @Override
//    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
//        return false;
//    }
//
//    private interface ItemCallback {
//
//        void destroy();
//
//        ItemStack getStack();
//
//    }
//
//    private static class EntityItemCallback implements ItemCallback {
//
//        private final ItemEntity item;
//
//        private EntityItemCallback(ItemEntity ei) {
//            item = ei;
//        }
//
//        @Override
//        public void destroy() {
//            item.kill();
//        }
//		/*
//		@Override
//		public int getSize() {
//			return item.getEntityItem().getCount();
//		}
//
//		@Override
//		public void incrementSize(int amt) {
//			item.getEntityItem().getCount() += amt;
//		}
//		 */
//
//        @Override
//        public ItemStack getStack() {
//            return item.getEntityItem();
//        }
//    }
//
//    private static class InventoryItemCallback implements ItemCallback {
//
//        private final InventorySlot slot;
//
//        private InventoryItemCallback(Container ii, int slot) {
//            this.slot = new InventorySlot(slot, ii);
//        }
//
//        @Override
//        public void destroy() {
//            slot.setSlot(null);
//        }
//		/*
//		@Override
//		public int getSize() {
//			return slot.getStackSize();
//		}
//
//		@Override
//		public void incrementSize(int amt) {
//			slot.increment(amt);
//		}
//		 */
//
//        @Override
//        public ItemStack getStack() {
//            return slot.getStack();
//        }
//    }
//
//}
