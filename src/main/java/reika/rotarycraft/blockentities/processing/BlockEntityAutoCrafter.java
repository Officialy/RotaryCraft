///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.processing;
//
//import appeng.api.AEApi;
//import appeng.api.networking.IGridBlock;
//import appeng.api.networking.IGridNode;
//import appeng.api.networking.security.IActionHost;
//import appeng.api.util.AECableType;
//import net.minecraft.block.BlockCompressed;
//import net.minecraft.core.BlockPos;
//import net.minecraft.item.crafting.CraftingManager;
//import net.minecraft.item.crafting.Recipe;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.fml.loading.FMLLoader;
//import net.neoforged.oredict.OreDictionary;
//import reika.dragonapi.modinteract.DeepInteract.AEPatternHandling;
//import reika.dragonapi.modinteract.DeepInteract.MESystemReader;
//import reika.dragonapi.ModList;
//import reika.dragonapi.instantiable.data.Collections.InventoryCache;
//import reika.dragonapi.instantiable.data.maps.CountMap;
//import reika.dragonapi.instantiable.modinteract.MEWorkTracker;
//import reika.dragonapi.instantiable.StepTimer;
//import reika.dragonapi.instantiable.data.KeyedItemStack;
//import reika.dragonapi.instantiable.data.maps.ItemHashMap;
//import reika.dragonapi.libraries.ReikaRecipeHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryBlocks;
//import reika.rotarycraft.registry.RotaryItems;
//
//import java.util.*;
//
//
//@Strippable(value = {"appeng.api.networking.security.IActionHost"/*, "appeng.api.networking.crafting.ICraftingRequester"*/})
//public class BlockEntityAutoCrafter extends InventoriedPowerReceiver implements IActionHost/*, ICraftingRequester*/ {
//
//    public static final int SIZE = 18;
//    private static final String LOGGER_ID = "autocrafter_workflag";
//    private static final int MAX_TICK_DELAY = 100; //5s
//    private static final int OUTPUT_OFFSET = SIZE;
//    private static final int CONTAINER_OFFSET = SIZE * 2;
//    private static final HashMap<KeyedItemStack, CraftingLoopCache> loopCache = new HashMap();
//
//    static {
//        ModularLogger.instance.addLogger(RotaryCraft.INSTANCE, LOGGER_ID);
//    }
//
//    private final InventoryCache ingredients = new InventoryCache();
//    private final StepTimer updateTimer = new StepTimer(50);
//    private final MEWorkTracker hasWork = new MEWorkTracker();
//    public int[] crafting = new int[SIZE];
//    @ModDependent(ModList.APPENG)
//    private MESystemReader network;
//    //private final IdentityHashMap<ICraftingLink, Integer> locks = new IdentityHashMap();
//    //private boolean[] lock = new boolean[SIZE];
//    private Object aeGridBlock;
//    private Object aeGridNode;
//    private int tickTimer = 1;
//    private int tick;
//    private int[] threshold = new int[SIZE];
//    private CraftingMode mode = CraftingMode.REQUEST;
//
//    public BlockEntityAutoCrafter() {
//        if (ModList.APPENG.isLoaded()) {
//            aeGridBlock = new BasicAEInterface(this, this.getMachine().getCraftedProduct());
//            aeGridNode = FMLLoader.getDist() == Dist.DEDICATED_SERVER ? AEApi.instance().createGridNode((IGridBlock) aeGridBlock) : null;
//
//            //for (int i = 0; i < lock.length; i++) {
//            //	lock[i] = new CraftingLock();
//            //}
//        }
//    }
//
//    private void craftMissingItems() {
//        if (ModList.APPENG.isLoaded() && network != null) {
//            for (int i = 0; i < SIZE; i++) {
//                //if (this.canTryMaintaining(i)) {
//                ItemStack is = this.getSlotRecipeOutput(i);
//                if (is != null) {
//                    long thresh = this.getThreshold(i);
//                    int has = (int) network.getItemCount(is, false);
//                    long missing = thresh - has;
//                    if (missing > 0) {
//                        //ReikaJavaLibrary.pConsole("Crafting missing "+is+" x "+missing+" (has "+has+")");
//                        //lock[i] = true;
//                        //network.triggerCrafting(level, is, missing, null, new CraftingLock(i));
//
//                        //int num = Math.min(is.getMaxStackSize(), (int)Math.min(missing, Integer.MAX_VALUE));
//                        this.attemptSlotCrafting(i, 0);
//                    }
//                }
//                //}
//            }
//        }
//    }
//
//    /*
//    private boolean canTryMaintaining(int i) {
//        return !lock[i];
//    }
//     */
//    public int getThreshold(int i) {
//        return threshold[i];
//    }
//
//    public void setThreshold(int i, int amt) {
//        threshold[i] = amt;
//        this.syncAllData(true);
//    }
//
//    public void incrementMode() {
//        mode = mode.next();
//    }
//
//    private void profileCraftingTime(long start) {
//        long duration = System.nanoTime() - start;
//        if (RotaryConfig.COMMON.CRAFTERPROFILE.get() && duration > 1000000 * tickTimer && tickTimer < MAX_TICK_DELAY) {
//            tickTimer += this.getTickIncrement();
//        } else if (tickTimer > 0) {
//            tickTimer--;
//        }
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getSummativeSidedPower();
//        this.tickCraftingDisplay();
//
//        updateTimer.update();
//        if (updateTimer.checkCap() && !world.isClientSide) {
//            this.buildCache();
//        }
//
//        if (ModList.APPENG.isLoaded()) {
//            if (network != null)
//                network.tick();
//            if (aeGridBlock != null && !world.isClientSide) {
//                ((BasicAEInterface) aeGridBlock).setPowerCost(power >= MINPOWER ? 4 : 1);
//            }
//        }
//
//        if (power >= MINPOWER) {
//            tick++;
//            if (!world.isClientSide) {
//                hasWork.tick();
//                if (hasWork.hasWork()) {
//                    //ReikaJavaLibrary.pConsole("Executing tick");
//                    mode.tick(this);
//                    if (ModList.APPENG.isLoaded() && network != null && !network.isEmpty)
//                        hasWork.reset();
//                }
//                this.injectItems();
//            }
//        }
//    }
//
//    private int getTickIncrement() {
//        if (tickTimer < 10)
//            return 1;
//        else if (tickTimer < 20)
//            return 2;
//        else if (tickTimer < 40)
//            return 5;
//        else
//            return 10;
//    }
//
//    @Override
//    protected void onInvalidateOrUnload(Level world, BlockPos pos, boolean invalid) {
//        super.onInvalidateOrUnload(world, pos, invalid);
//        if (ModList.APPENG.isLoaded() && aeGridNode != null)
//            ((IGridNode) aeGridNode).destroy();
//    }
//
//    private void injectItems() {
//        if (ModList.APPENG.isLoaded() && network != null) {
//            for (int i = 0; i < SIZE; i++) {
//                ItemStack in = inv[i + OUTPUT_OFFSET];
//                if (in != null) {
//                    in.getCount() = (int) network.addItem(in, false);
//                    if (in.getCount() <= 0)
//                        inv[i + OUTPUT_OFFSET] = null;
//                }
//
//                in = inv[i + CONTAINER_OFFSET];
//                if (in != null) {
//                    in.getCount() = (int) network.addItem(in, false);
//                    if (in.getCount() <= 0)
//                        inv[i + CONTAINER_OFFSET] = null;
//                }
//            }
//        }
//    }
//
//    private void tickCraftingDisplay() {
//        for (int i = 0; i < SIZE; i++) {
//            crafting[i] = Math.max(crafting[i] - 1, 0);
//        }
//    }
//
//    private void buildCache() {
//        ingredients.clear();
//        BlockEntity te = getAdjacentBlockEntity(Direction.UP);
//        if (te instanceof Container) {
//            ingredients.addInventory((Container) te);
//        }
//
//        if (ModList.APPENG.isLoaded()) {
//            Object oldNode = aeGridNode;
//            if (aeGridNode == null) {
//                aeGridNode = FMLLoader.getDist() == Dist.DEDICATED_SERVER ? AEApi.instance().createGridNode((IGridBlock) aeGridBlock) : null;
//            }
//            if (aeGridNode != null)
//                ((IGridNode) aeGridNode).updateState();
//
//            if (oldNode != aeGridNode || network == null) {
//                if (aeGridNode == null)
//                    network = null;
//                else if (network == null)
//                    network = new MESystemReader((IGridNode) aeGridNode, this);
//                else
//                    network = new MESystemReader((IGridNode) aeGridNode, network);
//
//                this.buildCallbacks();
//            }
//            //network.setRequester(this);
//        }
//    }
//
//    private void buildCallbacks() {
//        if (network != null) {
//            network.clearCallbacks();
//            for (int i = 0; i < SIZE; i++) {
//                ItemStack pattern = itemHandler.getStackInSlot(i);
//                if (this.isItemValidForSlot(i, pattern)) {
//                    ItemStack[] in = this.getIngredients(pattern);
//                    if (in != null) {
//                        for (int k = 0; k < in.length; k++) {
//                            if (in[k] != null)
//                                network.addCallback(in[k], hasWork);
//                        }
//                    }
//                    ItemStack out = this.getSlotRecipeOutput(i);
//                    network.addCallback(out, hasWork);
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void onInventoryChanged(int slot) {
//        if (ModList.APPENG.isLoaded())
//            this.buildCallbacks();
//    }
//
//    public void triggerCraftingCycle(int slot) {
//        if (power >= MINPOWER) {
//            ItemStack out = this.getSlotRecipeOutput(slot);
//            if (out != null)
//                this.attemptSlotCrafting(slot, 0);
//        }
//    }
//
//    public ItemStack getSlotRecipeOutput(int slot) {
//        ItemStack is = inv[slot];
//        if (is == null)
//            return null;
//        return this.getOutput(is);
//    }
//
//	/*
//	public void triggerCrafting(int slot, int amt) {
//		if (power >= MINPOWER) {
//			ItemStack out = this.getSlotRecipeOutput(slot);
//			if (out != null) {
//				int space = inv[slot+OUTPUT_OFFSET].getMaxStackSize()-inv[slot+OUTPUT_OFFSET].getCount();
//				int tocraft = ReikaMathLibrary.multiMin(amt, this.getInventoryStackLimit(), out.getMaxStackSize(), space);
//				int cycles = tocraft/out.getCount();
//				for (int i = 0; i < cycles; i++) {
//					boolean flag = this.attemptSlotCrafting(slot);
//					if (!flag)
//						break;
//				}
//			}
//		}
//	}
//	 */
//
//    private void attemptAllSlotCrafting() {
//        for (int i = 0; i < SIZE; i++) {
//            this.attemptSlotCrafting(i, 0);
//        }
//    }
//
//    private boolean attemptSlotCrafting(int i, int d) {
//        return this.attemptSlotCrafting(i, 1, d);
//    }
//
//    private boolean attemptSlotCrafting(int i, int n, int d) {
//        ItemStack is = itemHandler.getStackInSlot(i);
//        if (is == null)
//            return false;
//        ItemStack[] items = this.getIngredients(is);
//        ItemStack out = this.getOutput(is);
//        //ReikaJavaLibrary.pConsole(is+":"+Arrays.toString(items)+":"+out);
//        if (items != null && out != null) {
//            //ReikaJavaLibrary.pConsole("Crafting "+out+" from "+Arrays.toString(items));
//            boolean flag = false;
//            for (int a = 0; a < n; a++)
//                flag |= this.tryCrafting(i, out, items, d);
//            return flag;
//        }
//        return false;
//    }
//
//    private ItemStack[] getIngredients(ItemStack is) {
//        if (is.getItem() == RotaryItems.CRAFTPATTERN.get() && is.getTag() != null) {
//            return ItemCraftPattern.getItems(is);
//        } else if (ModList.APPENG.isLoaded() && InterfaceCache.AEPATTERN.instanceOf(is.getItem())) {
//            if (!AEPatternHandling.isCraftingRecipe(is, level))
//                return null;
//            return AEPatternHandling.getPatternInput(is, level);
//        } else {
//            return null;
//        }
//    }
//
//    private ItemStack getOutput(ItemStack is) {
//        if (is.getItem() == RotaryItems.CRAFTPATTERN.get() && is.getTag() != null && ItemCraftPattern.getMode(is) == RecipeMode.CRAFTING) {
//            return ItemCraftPattern.getResult(is);
//        } else if (ModList.APPENG.isLoaded() && InterfaceCache.AEPATTERN.instanceOf(is.getItem())) {
//            if (!AEPatternHandling.isCraftingRecipe(is, level))
//                return null;
//            ArrayList<ItemStack> li = AEPatternHandling.getPatternOutputs(is, level);
//            return li == null || li.isEmpty() ? null : li.get(0);
//        } else {
//            return null;
//        }
//    }
//
//    private boolean tryCrafting(int i, ItemStack out, ItemStack[] items, int d) {
//        int slot = i + OUTPUT_OFFSET;
//        int size = inv[slot] != null ? inv[slot].getCount() : 0;
//        if (inv[slot] == null || (ReikaItemHelper.matchStacks(out, inv[slot]) && size + out.getCount() <= out.getMaxStackSize())) {
//            if (inv[i + CONTAINER_OFFSET] == null) {
//                ItemHashMap<Integer> counts = new ItemHashMap(); //ingredient requirements
//                for (int k = 0; k < 9; k++) {
//                    if (items[k] != null) {
//                        Integer req = counts.get(items[k]);
//                        int val = req != null ? req.intValue() : 0;
//                        counts.put(items[k], val + 1); // items[k].getCount() ? no, recipes take 1 per slot
//                    }
//                }
//                for (ItemStack is : counts.keySet()) {
//                    if (!ReikaItemHelper.matchStacks(out, is)) {
//                        int req = counts.get(is);
//                        int has = this.getAvailableIngredients(is);
//                        int missing = req - has;
//                        //ReikaJavaLibrary.pConsole("need "+req+" / have "+has+" '"+is+" ("+is.getDisplayName()+")'; making '"+out+" ("+out.getDisplayName()+")'");
//                        if (missing > 0) {
//                            if (d < 40) {
//                                //ReikaJavaLibrary.pConsole("Going to attempt intermediate crafting. Can? "+this.canCraftIntermediates(out, counts));
//                                //ReikaJavaLibrary.pConsole(options+":"+has+"/"+req);
//                                if (!this.canCraftIntermediates(out, counts))
//                                    return false;
//                                if (!this.tryCraftIntermediates(missing, is, d + 1)) {
//                                    //ReikaJavaLibrary.pConsole("Failed to craft intermediates ("+is+" x "+missing+")");
//                                    //ReikaJavaLibrary.pConsole("missing "+missing+": "+options.get(is)+", needed "+req+", had "+has);
//                                    return false;
//                                }
//                            } else {
//                                //ReikaJavaLibrary.pConsole("Recursed too deep to craft intermediates");
//                                return false;
//                            }
//                        }
//                    }
//                }
//                //ReikaJavaLibrary.pConsole("Performing crafting of "+out);
//                this.craft(slot, size, out, counts);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean canCraftIntermediates(ItemStack out, ItemHashMap<Integer> req) {
//        Block b = Block.getBlockFromItem(out.getItem());
//        if (b == RotaryBlocks.DECO.get())
//            return false;
//        if (b instanceof BlockCompressed)
//            return false;
//        if (this.isLoopable(out, req))
//            return false;
//        //to be safe, since these tend to glitch
//        return !out.getItem().getClass().getName().equals("ItemReactorCondensator");
//    }
//
//    private boolean isLoopable(ItemStack out, ItemHashMap<Integer> req) {
//        KeyedItemStack kout = this.key(out);
//        CraftingLoopCache cache = loopCache.get(kout);
//        if (cache == null) {
//            cache = new CraftingLoopCache(out);
//            loopCache.put(kout, cache);
//        }
//        Collection<ItemStack> c = req.keySet();
//        HashSet<KeyedItemStack> set = new HashSet();
//        for (ItemStack is : c) {
//            set.add(this.key(is));
//        }
//        Boolean seek = cache.loopingSets.get(set);
//        if (seek == null) {
//            seek = this.calculateLoopability(out, c);
//            cache.loopingSets.put(new HashSet(set), seek); //clone set to avoid it being modified
//        }
//        return seek.booleanValue();
//    }
//
//    private KeyedItemStack key(ItemStack is) {
//        return new KeyedItemStack(is).setIgnoreMetadata(false).setIgnoreNBT(true).setSized(false).setSimpleHash(true);
//    }
//
//    private boolean calculateLoopability(ItemStack out, Collection<ItemStack> c) { //recipe contains output, or recipes for inputs contain output
//        if (ReikaItemHelper.collectionContainsItemStack(c, out))
//            return true;
//        for (ItemStack is : c) {
//            List<Recipe> li = ReikaRecipeHelper.getAllRecipesByOutput(CraftingManager.getInstance().getRecipeList(), is);
//            for (Recipe ir : li) {
//                if (ReikaItemHelper.collectionContainsItemStack(ReikaRecipeHelper.getAllItemsInRecipe(ir), out))
//                    return true;
//            }
//        }
//        return false;
//    }
//
//    private int getAvailableIngredients(ItemStack is) { //never needs to return more than 9 (one per slot)
//        int count = 0;
//        //ItemHashMap<Long> map = ModList.APPENG.isLoaded() && network != null ? network.getMESystemContents() : null;
//        //ReikaJavaLibrary.pConsole(map);
//        //for (ItemStack is : li) {
//        //ReikaJavaLibrary.pConsole(is+":"+ingredients.getItemCount(is)+" > "+ingredients);
//        count += ingredients.getItemCount(is);
//        if (ModList.APPENG.isLoaded() && network != null) {
//            count += network.getItemCount(is, false);//network.removeItem(ReikaItemHelper.getSizedItemStack(is, is.getMaxStackSize()), true, false);
//        }
//        //}
//
//        return count;
//    }
//
//    private boolean tryCraftIntermediates(int num, ItemStack is, int d) {
//        int run = 0;
//        //ReikaJavaLibrary.pConsole("Crafting intermediate "+is+" x"+num);
//        CountMap<Integer> ranSlots = new CountMap();
//        //for (ItemStack is : li) {
//        for (int i = 0; i < SIZE && run < num; i++) {
//            ItemStack out = this.getSlotRecipeOutput(i);
//            //ReikaJavaLibrary.pConsole(i+":"+out+" & "+is);
//            if (out != null && ReikaItemHelper.matchStacks(is, out)) {
//                //ReikaJavaLibrary.pConsole("Intermediate crafting: attempting slot "+i+", because "+out+" matches "+is);
//                while (run < num && this.attemptSlotCrafting(i, d)) {
//                    run += out.getCount();
//                    ranSlots.set(i, Math.min(num, ranSlots.get(i) + out.getCount()));
//                }
//            }
//        }
//        //if (run >= num)
//        //	break;
//        //}
//        //ReikaJavaLibrary.pConsole(run+" & "+num);
//        if (run >= num) {
//            for (int slot : ranSlots.keySet()) {
//                inv[slot + OUTPUT_OFFSET].getCount() -= ranSlots.get(slot);
//                if (inv[slot + OUTPUT_OFFSET].getCount() <= 0)
//                    inv[slot + OUTPUT_OFFSET] = null;
//            }
//            //ReikaJavaLibrary.pConsole(ranSlots+"/"+num+" for "+is);
//            return true;
//        }
//        //ReikaJavaLibrary.pConsole("false, "+ranSlots+"/"+num);
//        return false;
//    }
//
//    private void craft(int slot, int size, ItemStack out, ItemHashMap<Integer> counts) {
//        inv[slot] = ReikaItemHelper.getSizedItemStack(out, size + out.getCount());
//        if (out.getTag() != null)
//            inv[slot].getTag() = (CompoundTag) out.getTag().copy();
//        for (ItemStack is : counts.keySet()) {
//            int req = counts.get(is);
//            if (is.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
//                int dec = req;
//                for (int k = 0; k < OreDictionary.WILDCARD_VALUE; k++) {
//                    ItemStack is2 = new ItemStack(is.getItem(), 1, k);
//                    int rem = ingredients.removeXItems(is2, req);
//                    dec -= rem;
//                    if (dec > 0) {
//                        int diff = req - rem;
//                        if (ModList.APPENG.isLoaded()) {
//                            if (diff > 0 && network != null) {
//                                ItemStack is2c = is2.copy();
//                                is2c.getCount() = diff;
//                                dec -= network.removeItem(is2, false, false);//network.removeFromMESystem(is2, diff);
//                            }
//                        }
//                    }
//                    if (dec <= 0)
//                        break;
//                }
//            } else {
//                int rem = ingredients.removeXItems(is, req);
//                int diff = req - rem;
//                if (ModList.APPENG.isLoaded()) {
//                    if (diff > 0 && network != null) {
//                        ItemStack isc = is.copy();
//                        isc.getCount() = diff;
//                        network.removeItem(isc, false, false);//network.removeFromMESystem(is, diff);
//                    }
//                }
//            }
//            this.addContainers(is, req, slot - OUTPUT_OFFSET);
//        }
//        crafting[slot - OUTPUT_OFFSET] = 5;
//        this.markDirty();
//    }
//
//    private void addContainers(ItemStack is, int req, int slot) {
//        ItemStack con = is.getItem().getContainerItem(is);
//        if (con != null)
//            inv[36 + slot] = ReikaItemHelper.getSizedItemStack(con, req);
//    }
//
//    private boolean hasIngredient(ItemStack is) {
//        return ingredients.hasItem(is);
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack is, int j) {
//        return i >= SIZE;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return SIZE * 3;//18+18+18; //18 for patterns, 18 for output, additional 18 for container items; last 18 is hidden
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int i, ItemStack is) {
//        return i < SIZE && RotaryItems.CRAFTPATTERN.matchItem(is) && ItemCraftPattern.getMode(is) == RecipeMode.CRAFTING && ItemCraftPattern.getResult(is) != null;
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.CRAFTER;
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
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        NBT.putInt("mode", mode.ordinal());
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        mode = CraftingMode.list[NBT.getInt("mode")];
//    }
//
//    @Override
//    public void saveAdditional(CompoundTag NBT) {
//        super.saveAdditional(NBT);
//
//        CompoundTag fil = new CompoundTag();
//        for (int i = 0; i < threshold.length; i++) {
//            fil.putInt("thresh_" + i, threshold[i]);
//        }
//
//        NBT.put("filter", fil);
//    }
//
//    @Override
//    public void load(CompoundTag NBT) {
//        super.load(NBT);
//
//        CompoundTag fil = NBT.getCompound("filter");
//
//        threshold = new int[threshold.length];
//        for (int i = 0; i < threshold.length; i++) {
//            String name = "filter_" + i;
//            threshold[i] = fil.getInt("thresh_" + i);
//        }
//    }
//
//    public CraftingMode getMode() {
//        return mode;
//    }
//
//    @Override
//    @ModDependent(ModList.APPENG)
//    public IGridNode getGridNode(Direction dir) {
//        return (IGridNode) aeGridNode;
//    }
//
//    @Override
//    @ModDependent(ModList.APPENG)
//    public AECableType getCableConnectionType(Direction dir) {
//        return AECableType.COVERED;
//    }
//
//    @Override
//    @ModDependent(ModList.APPENG)
//    public void securityBreak() {
//
//    }
//
//    /*
//    @Override
//    @ModDependent(ModList.APPENG)
//    public IGridNode getActionableNode() {
//        return (IGridNode)aeGridNode;
//    }
//
//    @Override
//    @ModDependent(ModList.APPENG)
//    public ImmutableSet<ICraftingLink> getRequestedJobs() {
//        ImmutableSet.Builder<ICraftingLink> b = ImmutableSet.builder();
//        return b.addAll(locks.keySet()).build();
//    }
//
//    @Override
//    @ModDependent(ModList.APPENG)
//    public IAEItemStack injectCraftedItems(ICraftingLink link, IAEItemStack items, Actionable mode) {
//        return items;
//    }
//
//    @Override
//    @ModDependent(ModList.APPENG)
//    public void jobStateChange(ICraftingLink link) {
//        Integer get = locks.get(link);
//        if (get != null) {
//            lock[get.intValue()] = false;
//        }
//    }
//     */
//    @Override
//    @ModDependent(ModList.APPENG)
//    public IGridNode getActionableNode() {
//        return (IGridNode) aeGridNode;
//    }
//
//    /*
//    private class CraftingLock implements CraftCompleteCallback {
//
//        private final int slot;
//        //private boolean isRunning;
//
//        private CraftingLock(int s) {
//            slot = s;
//        }
//
//        @Override
//        @ModDependent(ModList.APPENG)
//        public void onCraftingLinkReturned(ICraftingLink link) {
//            locks.put(link, slot);
//        }
//
//        @Override
//        @ModDependent(ModList.APPENG)
//        public void onCraftingComplete(ICraftingLink link) {
//            //isRunning = false;
//        }
//
//    }
//     */
//    public enum CraftingMode {
//        REQUEST("Request", "Crafts one cycle per request.", 0xff0000, "2"),
//        CONTINUOUS("Continuous", "Crafts continuously as long as there are ingredients", 0x00aaff, "2"),
//        SUSTAIN("Sustain", "Tries to sustain a given number of a certain item", 0xbb22ff, "4");
//
//        private static final CraftingMode[] list = values();
//        public final String label;
//        public final String desc;
//        public final int color;
//        public final String imageSuffix;
//
//        CraftingMode(String l, String d, int c, String img) {
//            label = l;
//            desc = d;
//            color = c;
//            imageSuffix = img;
//        }
//
//        private void tick(BlockEntityAutoCrafter te) {
//            switch (this) {
//                case REQUEST:
//                    //Do nothing tick-based
//                    break;
//                case CONTINUOUS:
//                    if (te.tick >= te.tickTimer) {
//                        te.tick = 0;
//                        long time = System.nanoTime();
//                        te.attemptAllSlotCrafting();
//                        te.profileCraftingTime(time);
//                    }
//                    break;
//                case SUSTAIN:
//                    te.tickTimer = 20;
//                    if (te.tick >= te.tickTimer) {
//                        te.tick = 0;
//                        te.craftMissingItems();
//                    }
//                    break;
//            }
//        }
//
//        public boolean isValid() {
//            switch (this) {
//                case SUSTAIN:
//                    return ModList.APPENG.isLoaded();
//                default:
//                    return true;
//            }
//        }
//
//        public CraftingMode next() {
//            CraftingMode mode = this.calcNext();
//            while (!mode.isValid())
//                mode = mode.calcNext();
//            return mode;
//        }
//
//        private CraftingMode calcNext() {
//            return list[(this.ordinal() + 1) % list.length];
//        }
//    }
//
//    private static class CraftingLoopCache {
//
//        private final ItemStack output;
//        private final HashMap<HashSet<KeyedItemStack>, Boolean> loopingSets = new HashMap();
//
//        private CraftingLoopCache(ItemStack is) {
//            output = is;
//        }
//
//    }
//}
