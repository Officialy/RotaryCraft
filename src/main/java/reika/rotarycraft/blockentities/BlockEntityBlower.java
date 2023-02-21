/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.data.immutable.WorldLocation;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;

import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;


public class BlockEntityBlower extends BlockEntityPowerReceiver {

    private static Method getPatterns;
    private static Field dualityField;

//    static {
//        if (ModList.APPENG.isLoaded()) {
//            try {
//                Class c = Class.forName("appeng.helpers.DualityInterface");
//                getPatterns = c.getDeclaredMethod("getPatterns");
//                getPatterns.setAccessible(true);
//
//                c = InterfaceCache.MEINTERFACE.getClassType();
//                dualityField = c.getDeclaredField("duality");
//                dualityField.setAccessible(true);
//            } catch (Exception e) {
//                RotaryCraft.LOGGER.error("Could not add Item Pump AE pattern interfacing!");
//                e.printStackTrace();
//                ReflectiveFailureTracker.instance.logModReflectiveFailure(ModList.APPENG, e);
//            }
//        }
//    }

    public ItemStack[] matchingItems = new ItemStack[18];
    public boolean isWhitelist = false;
    public boolean useOreDict = true;
    public boolean checkNBT = false;
    private Direction facing;

    public BlockEntityBlower(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.BLOWER.get(), pos, state);
    }

//    @ModDependent(ModList.APPENG)
//    public static Container getPatterns(BlockEntity target) throws Exception {
//        return (Container) getPatterns.invoke(dualityField.get(target));
//    }


    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.BLOWER;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    //    @Override
    public void updateEntity(Level world, BlockPos pos) {
        this.getIOSides(world, pos, null);
        this.getSummativeSidedPower();

        if (MINPOWER > power || MINSPEED > omega)
            return;
        if (world.isClientSide)
            return;

        Direction dir = this.getFacingDir();
        Direction from = dir.getOpposite();

        BlockEntity source = getAdjacentBlockEntity(from);

        if (source instanceof IItemHandler) {
            BlockEntity target = getAdjacentBlockEntity(dir);
//            if (target instanceof WorldRift)
//                target = this.getRelayedTarget(target, dir);
            if (target != null) {
                WorldLocation tg = new WorldLocation(target);

                ArrayList<BlockEntity> li = new ArrayList<>();
                while (target instanceof BlockEntityBlower) {
                    BlockEntityBlower te = (BlockEntityBlower) target;
                    dir = te.getFacingDir();
                    target = getAdjacentBlockEntity(dir);
                    tg = tg.move(dir, 1);
//                    if (target instanceof WorldRift) {
//                        target = this.getRelayedTarget(target, dir);
//                        tg = new WorldLocation(target);
//                    }

                    if (li.contains(target))
                        return;
                    else
                        li.add(target);

                    if (this.equals(target)) { //to prevent stackoverflow from loops, because some idiot is going to try
                        return;
                    }
                    if (source.equals(target)) { //to prevent dupe glitch, and because this would be stupid to do
                        return;
                    }
                }

                //this.printTEs(source, target);

                InventoryType src = this.getTypeForInventory(source);

                if (target instanceof IItemHandler) {
                    InventoryType tgt = this.getTypeForInventory(target);
                    if (this.tryPatternInsertion((IItemHandler) source, target)) {

                    } else {
                        //ReikaJavaLibrary.pConsole(map);
                        this.transferItems(source, target, src, tgt);
                    }
                    //ReikaJavaLibrary.pConsole(map, Dist.DEDICATED_SERVER);
                } else if (target == null && ConfigRegistry.BLOWERSPILL.getState() && tg.isEmpty()) {
                    this.dumpItems(source, src, tg, from);
                }
            }
        }
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

//    private BlockEntity getRelayedTarget(BlockEntity te, Direction dir) {
//        WorldRift wr = (WorldRift) te;
//        BlockEntity te2 = wr.getBlockEntityFrom(dir);
//        return te2 != null ? te2 : te;
//    }

    private boolean tryPatternInsertion(IItemHandler source, BlockEntity target) {
//        if (InterfaceCache.MEINTERFACE.instanceOf(target)) {
//            for (int i = 0; i < source.getContainerSize(); i++) {
//                ItemStack is = source.getItem(i);
//                if (is != null && is.getItem() instanceof ICraftingPatternItem) {
//                    try {
//                        Inventory patterns = getPatterns(target);
//                        if (ReikaInventoryHelper.addToIInv(is, patterns))
//                            return true;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return false;
//                    }
//                }
//            }
//        }
        return false;
    }

    private void printTEs(BlockEntity source, BlockEntity target) {
        if (source == null && target == null)
            ReikaJavaLibrary.pConsole("null >> null", Dist.DEDICATED_SERVER);
        else if (source == null)
            ReikaJavaLibrary.pConsole("null >> " + target.getClass().getSimpleName(), Dist.DEDICATED_SERVER);
        else if (target == null)
            ReikaJavaLibrary.pConsole(source.getClass().getSimpleName() + " >> null", Dist.DEDICATED_SERVER);
        else
            ReikaJavaLibrary.pConsole(source.getClass().getSimpleName() + " >> " + target.getClass().getSimpleName(), Dist.DEDICATED_SERVER);
    }

    private int getNumberTransferrableItems() {
        return (int) (power / 1024);
    }

    private void transferItems(BlockEntity source, BlockEntity target, InventoryType src, InventoryType tgt) {
        int max = this.getNumberTransferrableItems();
        if (max <= 0)
            return;

//        if (src == InventoryType.DSU) {
//            IDeepStorageUnit dsu = (IDeepStorageUnit) source;
//            ItemStack is = dsu.getStoredItemType();
//            if (is != null && this.isItemTransferrable(is)) {
//                int has = src.removeItem(source, max, -1, false);
//                int added = tgt.insertItem(target, is, has, dir);
//                src.removeItem(source, added, -1, true);
//            }
//        } else {
        HashMap<Integer, ItemStack> map = src.getMovableSlots(source);
        for (int slot : map.keySet()) {
            ItemStack is = map.get(slot);
            if (max > 0 && this.isItemTransferrable(is)) {
                int has = src.removeItem(source, max, slot, false);
                int added = tgt.insertItem(target, is, has);
                src.removeItem(source, added, slot, true);
                max -= added;
            }
        }
//        }
    }

    private void dumpItems(BlockEntity source, InventoryType src, WorldLocation loc, Direction from) {
        int max = this.getNumberTransferrableItems();
        if (max <= 0)
            return;

//        if (src == InventoryType.DSU) {
//            IDeepStorageUnit dsu = (IDeepStorageUnit) source;
//            ItemStack is = dsu.getStoredItemType();
//            if (is != null && this.isItemTransferrable(is)) {
//                int has = src.removeItem(source, max, -1, true);
//                this.dropItem(is, has, loc);
//            }
//        } else {
        HashMap<Integer, ItemStack> map = src.getMovableSlots(source);
        for (int slot : map.keySet()) {
            ItemStack is = map.get(slot);
            if (max > 0 && this.isItemTransferrable(is)) {
                int has = src.removeItem(source, max, slot, true);
                this.dropItem(is, has, loc);
                max -= has;
            }
        }
//        }
    }

    private void dropItem(ItemStack is, int amt, WorldLocation loc) {
        for (int i = 0; i < amt; i++)
            loc.dropItem(ReikaItemHelper.getSizedItemStack(is, 1), 2 + DragonAPI.rand.nextDouble() * 4);
    }

    private InventoryType getTypeForInventory(BlockEntity te) {
        for (InventoryType t : InventoryType.values()) {
            if (t.isValid(te))
                return t;
        }
        return null;
    }

    public boolean isIntake() {
        return getAdjacentBlockEntity(this.getFacingDir().getOpposite()) instanceof IItemHandler;
    }

    private void getIOSides(Level world, BlockPos pos, Direction dir) {
        switch (dir) {
            case DOWN -> facing = Direction.DOWN;
            case UP -> facing = Direction.UP;
            case NORTH -> facing = Direction.NORTH;
            case EAST -> facing = Direction.EAST;
            case SOUTH -> facing = Direction.SOUTH;
            case WEST -> facing = Direction.WEST;
        }
    }

    /**
     * The side we are emitting items to
     */
    public Direction getFacingDir() {
        return facing != null ? facing : Direction.UP;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putBoolean("ore", useOreDict);
        NBT.putBoolean("cnbt", checkNBT);
        NBT.putBoolean("white", isWhitelist);

        ListTag nbttaglist = new ListTag();

        for (int i = 0; i < matchingItems.length; i++) {
            if (matchingItems[i] != null) {
                CompoundTag tag = new CompoundTag();
                tag.putByte("Slot", (byte) i);
                matchingItems[i].save(tag);
                nbttaglist.add(tag);
            }
        }

        NBT.put("Items", nbttaglist);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        isWhitelist = NBT.getBoolean("white");
        checkNBT = NBT.getBoolean("cnbt");
        useOreDict = NBT.getBoolean("ore");

        ListTag nbttaglist = NBT.getList("Items", Tag.TAG_COMPOUND);
        matchingItems = new ItemStack[18];

        for (int i = 0; i < nbttaglist.size(); i++) {
            CompoundTag tag = nbttaglist.getCompound(i);
            byte byte0 = tag.getByte("Slot");

            if (byte0 >= 0 && byte0 < matchingItems.length) {
                matchingItems[byte0] = ItemStack.of(tag);
            }
        }
    }

    @Override
    protected String getTEName() {
        return null;
    }

    public boolean isItemTransferrable(ItemStack is) {
        boolean match = this.isItemStackMatched(is);
        return isWhitelist == match;
    }

    private boolean isItemStackMatched(ItemStack is) {
        for (int i = 0; i < matchingItems.length; i++) {
            ItemStack is1 = matchingItems[i];
            if (is1 != null) {
                if (this.doStacksMatch(is, is1))
                    return true;
            }
        }
        return false;
    }

    private boolean doStacksMatch(ItemStack is, ItemStack is1) {
//        if (checkMeta && is.getItemDamage() != is1.getItemDamage())
//            return false;
        if (checkNBT && !ItemStack.tagMatches(is, is1))
            return false;
        if (ReikaItemHelper.matchStacks(is, is1))
            return true;
//        if (useOreDict) {
//            int oreID = OreDictionary.getOreID(is);
//            if (oreID > -1 && oreID == OreDictionary.getOreID(is1))
//                return true;
//        }
        return is.getItem() == is1.getItem();
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    public enum InventoryType {
        CHEST(),
        DSU(),
        //PLAYER(),
        ;

        public boolean isValid(BlockEntity te) {
            if (this == InventoryType.CHEST) {
                return te instanceof IItemHandler && !DSU.isValid(te);
//                case DSU:
//                    return InterfaceCache.DSU.instanceOf(te);
            }
            return false;
        }

        public HashMap<Integer, ItemStack> getMovableSlots(BlockEntity te) {
            return switch (this) {
                case CHEST -> ReikaInventoryHelper.getLocatedTransferrables((IItemHandler) te);
                case DSU -> null;//ReikaJavaLibrary.makeMapOf(-1, ((IDeepStorageUnit)te).getStoredItemType());
            };
        }

        public int removeItem(BlockEntity te, int amt, int slot, boolean remove) {
            if (this == InventoryType.CHEST) {
                Container ii = (Container) te;
                int items = 0;
                if (remove) {
                    boolean flag = true;
                    while (flag && amt > 0) {
                        ReikaInventoryHelper.decrStack(slot, ii, 1);
                        amt--;
                        flag = ii.getItem(slot) != null;
                        items++;
                    }
                    return items;
                } else {
                    return ii.getItem(slot) != null ? Math.min(ii.getItem(slot).getCount(), amt) : 0;
                }
//                case DSU:
//                    IDeepStorageUnit dsu = (IDeepStorageUnit) te;
//                    int has = dsu.getStoredItemType().getCount();
//                    int rem = Math.min(amt, has);
//                    if (remove)
//                        dsu.setStoredItemCount(has - rem);
//                    return rem;
            }
            return 0;
        }

        public int insertItem(BlockEntity te, ItemStack is, int amt) {
            if (this == InventoryType.CHEST) {
                ChestBlockEntity ii = (ChestBlockEntity) te;
                int items = 0;
                boolean flag = true;
                is = ReikaItemHelper.getSizedItemStack(is, 1);
                while (flag && amt > 0) {
                    //ReikaJavaLibrary.pConsole("Attempting to add "+is+" to "+target);
                    if (this.addToIInv(is, ii)) {
                        amt--;
                        items++;
                        //ReikaJavaLibrary.pConsole("Success");
                    } else {
                        flag = false;
                        //ReikaJavaLibrary.pConsole("Failure");
                    }
                }
                return items;
//                case DSU:
//                    IDeepStorageUnit dsu = (IDeepStorageUnit) te;
//                    int has = dsu.getStoredItemType().getCount();
//                    int space = dsu.getMaxStoredCount() - has;
//                    int add = Math.min(amt, space);
//                    dsu.setStoredItemCount(has + add);
//                    return add;
            }
            return 0;
        }

        private boolean addToIInv(ItemStack is, Container ii) {
            if (ii instanceof Container) {
                for (int k = 0; k < ii.getContainerSize(); k++) {
                    if (ii.canPlaceItem(k, is)) {
                        if (ReikaInventoryHelper.addToIInv(is, ii)) {
                            return true;
                        }
                    }
                }
            } else {
                return ReikaInventoryHelper.addToIInv(is, ii);
            }
            return false;
        }
    }

}
