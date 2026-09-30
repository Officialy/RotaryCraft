/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.MenuProvider;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import reika.dragonapi.ModList;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.instantiable.data.KeyedItemStack;
import reika.dragonapi.instantiable.data.maps.CountMap;
import reika.dragonapi.instantiable.modinteract.BasicAEInterface;
import reika.dragonapi.instantiable.modinteract.MEWorkTracker;
import reika.dragonapi.instantiable.storage.SlotFilteredItemHandler;
import reika.dragonapi.interfaces.blockentity.HasItemHandler;
import reika.dragonapi.interfaces.blockentity.MEGridHost;
import reika.dragonapi.libraries.ReikaRecipeHelper;
import reika.dragonapi.libraries.io.NBTCompat;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.dragonapi.modinteract.deepinteract.AEPatternHandling;
import reika.dragonapi.modinteract.deepinteract.MESystemReader;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.gui.container.machine.inventory.ContainerAutoCrafter;
import reika.rotarycraft.items.tools.ItemCraftPattern;
import reika.rotarycraft.items.tools.ItemCraftPattern.RecipeMode;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

/**
 * V33a {@code TileEntityAutoCrafter}: eighteen Craft Pattern slots, each with an output and a container-item slot.
 * Ingredients come from the inventory above and, with Applied Energistics 2, from the ME network the crafter is
 * connected to (which also receives every output). Modes: <b>Request</b> (one cycle per GUI click),
 * <b>Continuous</b> (every slot, every tick it can) and <b>Sustain</b> (AE only: keep each output stocked in the
 * network up to a threshold). Missing ingredients are crafted recursively from the crafter's other patterns.
 *
 * <p>26.3 port: the AE grid node is a DragonAPI {@link BasicAEInterface} (the machine implements the AE-free
 * {@link MEGridHost}); the inventory above is read through the item capability rather than V33a's
 * {@code InventoryCache}; damage-wildcard ingredients have no modern equivalent.
 */
public class BlockEntityAutoCrafter extends InventoriedPowerReceiver implements MEGridHost, HasItemHandler, MenuProvider {

	public static final int SIZE = 18;

	private static final int OUTPUT_OFFSET = SIZE;
	private static final int CONTAINER_OFFSET = SIZE * 2;

	private static final int MAX_TICK_DELAY = 100; //5s

	private static final HashMap<KeyedItemStack, CraftingLoopCache> loopCache = new HashMap<>();

	public final int[] crafting = new int[SIZE];

	/** {@code BasicAEInterface} when AE2 is loaded; typed Object so AE-less installs never resolve it. */
	private final Object aeGridBlock;
	private MESystemReader network;
	private int patternSignature;

	@Nullable
	private ResourceHandler<ItemResource> ingredients;

	private final StepTimer updateTimer = new StepTimer(50);

	private int tickTimer = 1;
	private int tick;

	private int[] threshold = new int[SIZE];

	private CraftingMode mode = CraftingMode.REQUEST;

	private final MEWorkTracker hasWork = ModList.APPENG.isLoaded() ? new MEWorkTracker() : null;

	private final SlotFilteredItemHandler automationHandler = new SlotFilteredItemHandler(this.getItemHandlerDelegate(), i -> i < SIZE, i -> i >= SIZE);

	private final ContainerData craftingData = new ContainerData() {
		@Override
		public int get(int index) {
			return crafting[index];
		}

		@Override
		public void set(int index, int value) {
			crafting[index] = value;
		}

		@Override
		public int getCount() {
			return SIZE;
		}
	};

	public BlockEntityAutoCrafter(BlockPos pos, BlockState state) {
		super(RotaryBlockEntities.CRAFTER.get(), pos, state);
		aeGridBlock = ModList.APPENG.isLoaded() ? this.createAEInterface() : null;
	}

	private Object createAEInterface() {
		return new BasicAEInterface(this, new ItemStack(RotaryBlocks.CRAFTER.get())).setCoveredCable();
	}

	private ResourceHandler<ItemResource> getItemHandlerDelegate() {
		//the handler is replaced on load, so the automation view must always read the live one
		return new net.neoforged.neoforge.transfer.DelegatingResourceHandler<>(() -> itemHandler);
	}

	public enum CraftingMode {
		REQUEST("Request", "Crafts one cycle per request.", 0xff0000, "2"),
		CONTINUOUS("Continuous", "Crafts continuously as long as there are ingredients", 0x00aaff, "2"),
		SUSTAIN("Sustain", "Tries to sustain a given number of a certain item", 0xbb22ff, "4");

		public final String label;
		public final String desc;
		public final int color;
		public final String imageSuffix;

		private static final CraftingMode[] list = values();

		CraftingMode(String l, String d, int c, String img) {
			label = l;
			desc = d;
			color = c;
			imageSuffix = img;
		}

		private void tick(BlockEntityAutoCrafter te) {
			switch (this) {
				case REQUEST:
					//Do nothing tick-based
					break;
				case CONTINUOUS:
					if (te.tick >= te.tickTimer) {
						te.tick = 0;
						long time = System.nanoTime();
						te.attemptAllSlotCrafting();
						te.profileCraftingTime(time);
					}
					break;
				case SUSTAIN:
					te.tickTimer = 8; //TODO revisit this
					if (te.tick >= te.tickTimer) {
						te.tick = 0;
						te.craftMissingItems();
					}
					break;
			}
		}

		public boolean isValid() {
			return this != SUSTAIN || ModList.APPENG.isLoaded();
		}

		public CraftingMode next() {
			CraftingMode mode = this.calcNext();
			while (!mode.isValid())
				mode = mode.calcNext();
			return mode;
		}

		private CraftingMode calcNext() {
			return list[(this.ordinal() + 1) % list.length];
		}
	}

	private void craftMissingItems() {
		if (ModList.APPENG.isLoaded() && network != null) {
			for (int i = 0; i < SIZE; i++) {
				ItemStack is = this.getSlotRecipeOutput(i);
				if (is != null) {
					long thresh = this.getThreshold(i);
					long has = network.getItemCount(is, false);
					long missing = thresh - has;
					if (missing > 0) {
						this.attemptSlotCrafting(i, 0);
					}
				}
			}
		}
	}

	public int getThreshold(int i) {
		return threshold[i];
	}

	public void setThreshold(int i, int amt) {
		threshold[i] = amt;
		this.syncAllData(true);
	}

	public void incrementMode() {
		mode = mode.next();
		this.syncAllData(true);
	}

	public CraftingMode getMode() {
		return mode;
	}

	private void profileCraftingTime(long start) {
		long duration = System.nanoTime() - start;
		if (ConfigRegistry.CRAFTERPROFILE.getState() && duration > 1000000L * tickTimer && tickTimer < MAX_TICK_DELAY) {
			tickTimer += this.getTickIncrement();
		}
		else if (tickTimer > 0) {
			tickTimer--;
		}
	}

	private int getTickIncrement() {
		if (tickTimer < 10)
			return 1;
		else if (tickTimer < 20)
			return 2;
		else if (tickTimer < 40)
			return 5;
		else
			return 10;
	}

	public void updateEntity(Level world, BlockPos pos) {
		super.updateBlockEntity();
		this.getSummativeSidedPower();
		this.tickCraftingDisplay();

		updateTimer.update();
		if (updateTimer.checkCap() && !world.isClientSide()) {
			this.buildCache();
		}

		if (ModList.APPENG.isLoaded()) {
			if (network != null)
				network.tick();
			if (aeGridBlock != null && !world.isClientSide()) {
				((BasicAEInterface)aeGridBlock).setPowerCost(power >= MINPOWER ? 4 : 1);
			}
		}

		if (power >= MINPOWER) {
			tick++;
			if (!world.isClientSide()) {
				if (hasWork != null) {
					hasWork.tick();
					if (hasWork.hasWork()) {
						mode.tick(this);
						if (network != null && !network.isEmpty)
							hasWork.reset();
					}
				}
				else {
					mode.tick(this);
				}
				this.injectItems();
			}
		}
	}

	@Override
	public void clearRemoved() {
		super.clearRemoved();
		if (aeGridBlock != null)
			((BasicAEInterface)aeGridBlock).onReady();
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		if (aeGridBlock != null)
			((BasicAEInterface)aeGridBlock).destroy();
	}

	@Override
	public void onChunkUnloaded() {
		super.onChunkUnloaded();
		if (aeGridBlock != null)
			((BasicAEInterface)aeGridBlock).destroy();
	}

	@Nullable
	@Override
	public Object getAEInterface() {
		return aeGridBlock;
	}

	private void injectItems() {
		if (ModList.APPENG.isLoaded() && network != null) {
			for (int i = 0; i < SIZE; i++) {
				this.injectSlot(i + OUTPUT_OFFSET);
				this.injectSlot(i + CONTAINER_OFFSET);
			}
		}
	}

	private void injectSlot(int slot) {
		ItemStack in = this.getStackInSlot(slot);
		if (!in.isEmpty()) {
			int left = (int)network.addItem(in, false);
			if (left != in.getCount())
				itemHandler.setStackInSlot(slot, left <= 0 ? ItemStack.EMPTY : in.copyWithCount(left));
		}
	}

	private void tickCraftingDisplay() {
		for (int i = 0; i < SIZE; i++) {
			crafting[i] = Math.max(crafting[i] - 1, 0);
		}
	}

	private void buildCache() {
		ingredients = level.getCapability(Capabilities.Item.BLOCK, worldPosition.above(), Direction.DOWN);

		if (ModList.APPENG.isLoaded() && aeGridBlock != null) {
			MESystemReader was = network;
			network = ((BasicAEInterface)aeGridBlock).updateReader(network);
			if (network != was)
				patternSignature = 0;
			int sig = this.computePatternSignature();
			if (network != null && sig != patternSignature) {
				patternSignature = sig;
				this.buildCallbacks();
			}
		}
	}

	/** V33a rebuilt the watch list on every inventory change; the pattern slots are compared here instead. */
	private int computePatternSignature() {
		int sig = 1;
		for (int i = 0; i < SIZE; i++)
			sig = sig * 31 + ItemStack.hashItemAndComponents(this.getStackInSlot(i));
		return sig == 0 ? 1 : sig;
	}

	private void buildCallbacks() {
		if (network != null) {
			network.clearCallbacks();
			for (int i = 0; i < SIZE; i++) {
				ItemStack pattern = this.getStackInSlot(i);
				if (this.isItemValidForSlot(i, pattern)) {
					ItemStack[] in = this.getIngredients(pattern);
					if (in != null) {
						for (ItemStack itemStack : in) {
							if (itemStack != null)
								network.addCallback(itemStack, hasWork);
						}
					}
					ItemStack out = this.getSlotRecipeOutput(i);
					if (out != null)
						network.addCallback(out, hasWork);
				}
			}
		}
	}

	public void triggerCraftingCycle(int slot) {
		if (power >= MINPOWER) {
			ItemStack out = this.getSlotRecipeOutput(slot);
			if (out != null)
				this.attemptSlotCrafting(slot, 0);
		}
	}

	@Nullable
	public ItemStack getSlotRecipeOutput(int slot) {
		ItemStack is = this.getStackInSlot(slot);
		if (is.isEmpty())
			return null;
		return this.getOutput(is);
	}

	private void attemptAllSlotCrafting() {
		for (int i = 0; i < SIZE; i++) {
			this.attemptSlotCrafting(i, 0);
		}
	}

	private boolean attemptSlotCrafting(int i, int d) {
		return this.attemptSlotCrafting(i, 1, d);
	}

	private boolean attemptSlotCrafting(int i, int n, int d) {
		ItemStack is = this.getStackInSlot(i);
		if (is.isEmpty())
			return false;
		ItemStack[] items = this.getIngredients(is);
		ItemStack out = this.getOutput(is);
		if (items != null && out != null) {
			boolean flag = false;
			for (int a = 0; a < n; a++)
				flag |= this.tryCrafting(i, out, items, d);
			return flag;
		}
		return false;
	}

	@Nullable
	private ItemStack[] getIngredients(ItemStack is) {
		if (is.is(RotaryItems.CRAFT_PATTERN.get()) && is.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA)) {
			return ItemCraftPattern.getItems(is, level.registryAccess());
		}
		else if (ModList.APPENG.isLoaded() && AEPatternHandling.isPattern(is)) {
			if (!AEPatternHandling.isCraftingRecipe(is, level))
				return null;
			return AEPatternHandling.getPatternInput(is, level);
		}
		else {
			return null;
		}
	}

	@Nullable
	private ItemStack getOutput(ItemStack is) {
		if (is.is(RotaryItems.CRAFT_PATTERN.get()) && is.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA) && ItemCraftPattern.getMode(is) == RecipeMode.CRAFTING) {
			ItemStack out = ItemCraftPattern.getResult(is, level.registryAccess());
			return out.isEmpty() ? null : out;
		}
		else if (ModList.APPENG.isLoaded() && AEPatternHandling.isPattern(is)) {
			if (!AEPatternHandling.isCraftingRecipe(is, level))
				return null;
			ArrayList<ItemStack> li = AEPatternHandling.getPatternOutputs(is, level);
			return li == null || li.isEmpty() ? null : li.get(0);
		}
		else {
			return null;
		}
	}

	private boolean tryCrafting(int i, ItemStack out, ItemStack[] items, int d) {
		int slot = i + OUTPUT_OFFSET;
		ItemStack inSlot = this.getStackInSlot(slot);
		int size = inSlot.getCount();
		if (inSlot.isEmpty() || (ReikaItemHelper.matchStacks(out, inSlot) && size + out.getCount() <= out.getMaxStackSize())) {
			if (this.getStackInSlot(i + CONTAINER_OFFSET).isEmpty()) {
				ArrayList<ItemStack> keys = new ArrayList<>(); //ingredient requirements (V33a ItemHashMap, NBT-sensitive)
				ArrayList<Integer> counts = new ArrayList<>();
				for (int k = 0; k < 9; k++) {
					if (items[k] != null && !items[k].isEmpty()) {
						int idx = indexOf(keys, items[k]);
						if (idx < 0) {
							keys.add(items[k]);
							counts.add(1); // recipes take 1 per slot
						}
						else {
							counts.set(idx, counts.get(idx) + 1);
						}
					}
				}
				for (int k = 0; k < keys.size(); k++) {
					ItemStack is = keys.get(k);
					if (!ReikaItemHelper.matchStacks(out, is)) {
						int req = counts.get(k);
						int has = this.getAvailableIngredients(is);
						int missing = req - has;
						if (missing > 0) {
							if (d < 40) {
								if (!this.canCraftIntermediates(out, keys))
									return false;
								if (!this.tryCraftIntermediates(missing, is, d + 1)) {
									return false;
								}
							}
							else {
								return false;
							}
						}
					}
				}
				this.craft(slot, size, out, keys, counts);
				return true;
			}
		}
		return false;
	}

	private static int indexOf(List<ItemStack> li, ItemStack is) {
		for (int i = 0; i < li.size(); i++) {
			if (ItemStack.isSameItemSameComponents(li.get(i), is))
				return i;
		}
		return -1;
	}

	private boolean canCraftIntermediates(ItemStack out, Collection<ItemStack> req) {
		Block b = out.getItem() instanceof BlockItem bi ? bi.getBlock() : null;
		if (b == RotaryBlocks.DECO.get())
			return false;
		if (b != null && b.getClass().getSimpleName().equals("BlockCompressed")) //V33a BlockCompressed (compressed resource blocks)
			return false;
		if (this.isLoopable(out, req))
			return false;
		if (out.getItem().getClass().getName().equals("ItemReactorCondensator")) //to be safe, since these tend to glitch
			return false;
		return true;
	}

	private boolean isLoopable(ItemStack out, Collection<ItemStack> req) {
		KeyedItemStack kout = this.key(out);
		CraftingLoopCache cache = loopCache.get(kout);
		if (cache == null) {
			cache = new CraftingLoopCache(out);
			loopCache.put(kout, cache);
		}
		HashSet<KeyedItemStack> set = new HashSet<>();
		for (ItemStack is : req) {
			set.add(this.key(is));
		}
		Boolean seek = cache.loopingSets.get(set);
		if (seek == null) {
			seek = this.calculateLoopability(out, req);
			cache.loopingSets.put(new HashSet<>(set), seek); //clone set to avoid it being modified
		}
		return seek;
	}

	private KeyedItemStack key(ItemStack is) {
		return new KeyedItemStack(is).setIgnoreNBT(true).setSized(false).setSimpleHash(true);
	}

	/** Recipe contains output, or recipes for inputs contain output. */
	private boolean calculateLoopability(ItemStack out, Collection<ItemStack> c) {
		if (ReikaItemHelper.collectionContainsItemStack(c, out))
			return true;
		if (level.getServer() == null)
			return false;
		List<Recipe<?>> all = new ArrayList<>();
		for (RecipeHolder<CraftingRecipe> h : level.getServer().getRecipeManager().recipeMap().byType(RecipeType.CRAFTING))
			all.add(h.value());
		for (ItemStack is : c) {
			List<Recipe<?>> li = ReikaRecipeHelper.getAllRecipesByOutput(all, is);
			for (Recipe<?> ir : li) {
				if (ReikaItemHelper.collectionContainsItemStack(ReikaRecipeHelper.getAllItemsInRecipe(ir), out))
					return true;
			}
		}
		return false;
	}

	/** Never needs to return more than 9 (one per slot). */
	private int getAvailableIngredients(ItemStack is) {
		int count = 0;
		count += this.countInSource(is);
		if (ModList.APPENG.isLoaded() && network != null) {
			count += (int)Math.min(Integer.MAX_VALUE, network.getItemCount(is, false));
		}
		return count;
	}

	private int countInSource(ItemStack is) {
		if (ingredients == null)
			return 0;
		int count = 0;
		for (int i = 0; i < ingredients.size(); i++) {
			ItemResource r = ingredients.getResource(i);
			if (!r.isEmpty() && r.matches(is))
				count += ingredients.getAmountAsInt(i);
		}
		return count;
	}

	/** V33a {@code InventoryCache.removeXItems}: returns how many were removed. */
	private int removeFromSource(ItemStack is, int amt) {
		if (ingredients == null || amt <= 0)
			return 0;
		try (Transaction tx = Transaction.openRoot()) {
			int got = ingredients.extract(ItemResource.of(is), amt, tx);
			tx.commit();
			return got;
		}
	}

	private boolean tryCraftIntermediates(int num, ItemStack is, int d) {
		int run = 0;
		CountMap<Integer> ranSlots = new CountMap<>();
		for (int i = 0; i < SIZE && run < num; i++) {
			ItemStack out = this.getSlotRecipeOutput(i);
			if (out != null && ReikaItemHelper.matchStacks(is, out)) {
				while (run < num && this.attemptSlotCrafting(i, d)) {
					run += out.getCount();
					ranSlots.set(i, Math.min(num, ranSlots.get(i) + out.getCount()));
				}
			}
		}
		if (run >= num) {
			for (int slot : ranSlots.keySet()) {
				ItemStack o = this.getStackInSlot(slot + OUTPUT_OFFSET);
				int left = o.getCount() - ranSlots.get(slot);
				itemHandler.setStackInSlot(slot + OUTPUT_OFFSET, left <= 0 ? ItemStack.EMPTY : o.copyWithCount(left));
			}
			return true;
		}
		return false;
	}

	private void craft(int slot, int size, ItemStack out, List<ItemStack> keys, List<Integer> counts) {
		itemHandler.setStackInSlot(slot, out.copyWithCount(size + out.getCount()));
		for (int k = 0; k < keys.size(); k++) {
			ItemStack is = keys.get(k);
			int req = counts.get(k);
			int rem = this.removeFromSource(is, req);
			int diff = req - rem;
			if (ModList.APPENG.isLoaded()) {
				if (diff > 0 && network != null) {
					network.removeItem(is.copyWithCount(diff), false, false);
				}
			}
			this.addContainers(is, req, slot - OUTPUT_OFFSET);
		}
		crafting[slot - OUTPUT_OFFSET] = 5;
		this.setChanged();
	}

	private void addContainers(ItemStack is, int req, int slot) {
		ItemStackTemplate con = is.getItem().getCraftingRemainder();
		if (con != null)
			itemHandler.setStackInSlot(CONTAINER_OFFSET + slot, con.create().copyWithCount(req));
	}

	public boolean canExtractItem(int i, ItemStack is, int j) {
		return i >= SIZE;
	}

	@Override
	public int getContainerSize() {
		return SIZE * 3; //18 for patterns, 18 for output, additional 18 for container items
	}

	public boolean isItemValidForSlot(int i, ItemStack is) {
		return i < SIZE && is.is(RotaryItems.CRAFT_PATTERN.get()) && ItemCraftPattern.getMode(is) == RecipeMode.CRAFTING && !ItemCraftPattern.getResult(is).isEmpty();
	}

	@Override
	public reika.dragonapi.instantiable.storage.ManagedItemHandler getItemHandler() {
		return itemHandler;
	}

	@Override
	public ResourceHandler<ItemResource> getAutomationItemHandler() {
		return automationHandler;
	}

	public ContainerData getCraftingData() {
		return craftingData;
	}

	@Override
	protected void animateWithTick(Level world, BlockPos pos) {

	}

	@Override
	public MachineRegistry getMachine() {
		return MachineRegistry.CRAFTER;
	}

	@Override
	public Block getBlockEntityBlockID() {
		return RotaryBlocks.CRAFTER.get();
	}

	@Override
	protected String getTEName() {
		return "Automatic Crafter";
	}

	@Override
	public boolean hasAnInventory() {
		return true;
	}

	@Override
	public boolean hasATank() {
		return false;
	}

	@Override
	public boolean hasModelTransparency() {
		return false;
	}

	@Override
	public int getRedstoneOverride() {
		return 0;
	}

	@Override
	protected void writeSyncTag(CompoundTag NBT) {
		super.writeSyncTag(NBT);

		NBT.putInt("mode", mode.ordinal());
		NBT.putIntArray("thresh", threshold);
	}

	@Override
	protected void readSyncTag(CompoundTag NBT) {
		super.readSyncTag(NBT);

		mode = CraftingMode.list[Math.min(NBTCompat.getInt(NBT, "mode", 0), CraftingMode.list.length - 1)];
		int[] t = NBT.getIntArray("thresh").orElse(null);
		if (t != null && t.length == SIZE)
			threshold = t;
	}

	@Override
	public void saveAdditional(CompoundTag NBT) {
		super.saveAdditional(NBT);

		CompoundTag fil = new CompoundTag();
		for (int i = 0; i < threshold.length; i++) {
			fil.putInt("thresh_" + i, threshold[i]);
		}

		NBT.put("filter", fil);
	}

	@Override
	public void load(CompoundTag NBT) {
		super.load(NBT);

		CompoundTag fil = NBTCompat.getCompound(NBT, "filter");

		threshold = new int[SIZE];
		for (int i = 0; i < threshold.length; i++) {
			threshold[i] = NBTCompat.getInt(fil, "thresh_" + i, 0);
		}
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		super.saveAdditional(output);
		if (aeGridBlock != null)
			((BasicAEInterface)aeGridBlock).saveNode(output);
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		super.loadAdditional(input);
		if (aeGridBlock != null)
			((BasicAEInterface)aeGridBlock).loadNode(input);
	}

	@Override
	public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
		return new ContainerAutoCrafter(id, inv, this);
	}

	private static class CraftingLoopCache {

		private final ItemStack output;
		private final HashMap<HashSet<KeyedItemStack>, Boolean> loopingSets = new HashMap<>();

		private CraftingLoopCache(ItemStack is) {
			output = is;
		}

	}
}
