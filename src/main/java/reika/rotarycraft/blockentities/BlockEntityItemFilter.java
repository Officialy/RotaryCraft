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

import java.util.*;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import reika.dragonapi.ModList;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.instantiable.data.KeyedItemStack;
import reika.dragonapi.instantiable.modinteract.BasicAEInterface;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.instantiable.storage.SlotFilteredItemHandler;
import reika.dragonapi.interfaces.blockentity.HasItemHandler;
import reika.dragonapi.interfaces.blockentity.MEGridHost;
import reika.dragonapi.libraries.io.NBTCompat;
import reika.dragonapi.libraries.java.ReikaStringParser;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.dragonapi.modinteract.deepinteract.MESystemReader;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.gui.container.machine.inventory.ContainerItemFilter;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * V33a {@code TileEntityItemFilter}: a template item in slot 0 defines a match (item, damage, mod, ore names,
 * class hierarchy and every NBT key, each set to Match / Mismatch / Ignore, the whole inverted by redstone); only
 * matching items can be inserted into slot 1, which is the only slot automation can empty. Sixteen blacklist slots
 * veto exact stacks. With Applied Energistics 2 it pulls matching items out of its ME network into slot 1 and emits a
 * redstone signal once none are left.
 *
 * <p>26.3 port: NBT is now data components, so the "NBT" the filter matches key-by-key is the template's component
 * patch encoded to NBT (damage excluded, since that is the separate damage setting); ore dictionary names are the
 * item's {@code c:} tags. The AE grid node is a DragonAPI {@link BasicAEInterface} behind the AE-free
 * {@link MEGridHost}.
 */
public class BlockEntityItemFilter extends InventoriedPowerReceiver implements MEGridHost, HasItemHandler, MenuProvider {

	private static final Random rand = new Random();

	public static final int BLACKLIST_SLOTS = 16;

	private MatchData data;

	private final Object aeGridBlock;
	private MESystemReader network;

	private final StepTimer updateTimer = new StepTimer(200);

	private final ArrayList<ItemStack> MEStacks = new ArrayList<>();

	private final HashSet<KeyedItemStack> blacklist = new HashSet<>();
	private int blacklistSignature;

	private final SlotFilteredItemHandler automationHandler = new SlotFilteredItemHandler(
			new DelegatingResourceHandler<>(() -> itemHandler),
			(slot, res) -> slot == 1 && this.isItemValidForSlot(slot, res.toStack()), slot -> slot == 1);

	public BlockEntityItemFilter(BlockPos pos, BlockState state) {
		super(RotaryBlockEntities.ITEMFILTER.get(), pos, state);
		aeGridBlock = ModList.APPENG.isLoaded() ? this.createAEInterface() : null;
	}

	private Object createAEInterface() {
		return new BasicAEInterface(this, new ItemStack(RotaryBlocks.ITEMFILTER.get())).setCoveredCable();
	}

	public boolean canExtractItem(int slot, ItemStack is, int side) {
		return slot == 1;
	}

	@Override
	public int getContainerSize() {
		return 2 + BLACKLIST_SLOTS;
	}

	public boolean isItemValidForSlot(int slot, ItemStack is) {
		return slot == 1 && power >= MINPOWER && this.matchItem(is);
	}

	private boolean matchItem(ItemStack is) {
		return data != null && !blacklist.contains(new KeyedItemStack(is).setSimpleHash(true)) && data.match(is, this.getRegistries()) != this.hasRedstoneSignal();
	}

	private HolderLookup.Provider getRegistries() {
		return level != null ? level.registryAccess() : null;
	}

	@Override
	protected void animateWithTick(Level world, BlockPos pos) {

	}

	/** V33a onInventoryChanged; called by the menu after every click, and from the tick when the slots change. */
	public void onSlotsChanged() {
		this.reloadData();
		this.rebuildBlacklist();
	}

	private void rebuildBlacklist() {
		blacklist.clear();
		for (int i = 2; i < this.getContainerSize(); i++) {
			ItemStack is = this.getStackInSlot(i);
			if (!is.isEmpty())
				blacklist.add(new KeyedItemStack(is).setSimpleHash(true).setIgnoreNBT(false).setSized(false));
		}
	}

	public void reloadData() {
		ItemStack template = this.getStackInSlot(0);
		if (template.isEmpty())
			this.setData(null);
		else if (data == null || !data.isFor(template, this.getRegistries()))
			this.setData(new MatchData(template, this.getRegistries()).loadFrom(data));
	}

	@Override
	public MachineRegistry getMachine() {
		return MachineRegistry.ITEMFILTER;
	}

	@Override
	public Block getBlockEntityBlockID() {
		return RotaryBlocks.ITEMFILTER.get();
	}

	@Override
	protected String getTEName() {
		return "Item Filter";
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

	public void updateEntity(Level world, BlockPos pos) {
		super.updateBlockEntity();
		this.getSummativeSidedPower();

		if (ModList.APPENG.isLoaded() && power >= MINPOWER) {
			updateTimer.update();
			if (updateTimer.checkCap() && !world.isClientSide()) {
				this.buildCache();
			}

			if (network != null)
				network.tick();
			if (aeGridBlock != null && !world.isClientSide()) {
				((BasicAEInterface)aeGridBlock).setPowerCost(power >= MINPOWER ? 2 : 1);
			}

			if (!world.isClientSide() && network != null && data != null && this.getStackInSlot(1).isEmpty() && !MEStacks.isEmpty()) {
				int idx = rand.nextInt(MEStacks.size());
				ItemStack is = MEStacks.get(idx);
				is = is.copyWithCount(is.getMaxStackSize());
				int ret = (int)network.removeItem(is, false, true);
				if (ret > 0) {
					itemHandler.setStackInSlot(1, is.copyWithCount(ret));
					MEStacks.remove(idx);
				}
			}
		}

		int sig = this.computeSlotSignature();
		if (sig != blacklistSignature || (data == null && !this.getStackInSlot(0).isEmpty())) {
			blacklistSignature = sig;
			this.onSlotsChanged();
		}
	}

	private int computeSlotSignature() {
		int sig = 1;
		sig = sig * 31 + ItemStack.hashItemAndComponents(this.getStackInSlot(0));
		for (int i = 2; i < this.getContainerSize(); i++)
			sig = sig * 31 + ItemStack.hashItemAndComponents(this.getStackInSlot(i));
		return sig;
	}

	private void buildCache() {
		if (ModList.APPENG.isLoaded() && aeGridBlock != null) {
			network = ((BasicAEInterface)aeGridBlock).updateReader(network);

			if (network != null && data != null) {
				MEStacks.clear();
				for (ItemStack is : network.getRawMESystemContents()) {
					if (this.matchItem(is)) {
						MEStacks.add(is.copyWithCount(1));
					}
				}
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

	public MatchData getData() {
		return data;
	}

	public void setData(MatchData dat) {
		data = dat;
		MEStacks.clear();
		updateTimer.setTick(updateTimer.getCap() + 2);
		this.setChanged();
	}

	/** Server side of the FILTERSETTING packet: the client's edited settings, applied over the current template. */
	public void setDataFromClient(CompoundTag tag) {
		MatchData dat = MatchData.createFromNBT(tag);
		if (dat != null) {
			this.setData(dat);
			this.syncAllData(true);
		}
	}

	@Override
	public int getRedstoneOverride() {
		return MEStacks.isEmpty() ? 15 : 0;
	}

	@Override
	public ManagedItemHandler getItemHandler() {
		return itemHandler;
	}

	@Override
	public ResourceHandler<ItemResource> getAutomationItemHandler() {
		return automationHandler;
	}

	@Override
	protected void writeSyncTag(CompoundTag NBT) {
		super.writeSyncTag(NBT);
		if (data != null)
			NBT.put("data", data.writeToNBT());
	}

	@Override
	protected void readSyncTag(CompoundTag NBT) {
		super.readSyncTag(NBT);
		if (NBT.contains("data")) {
			MatchData dat = MatchData.createFromNBT(NBTCompat.getCompound(NBT, "data"));
			if (dat != null)
				data = dat;
		}
	}

	@Override
	public void load(CompoundTag NBT) {
		super.load(NBT);
		this.rebuildBlacklist();
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
		return new ContainerItemFilter(id, inv, this);
	}

	/** The template's component patch as NBT (V33a stackTagCompound), damage excluded; null if it has none. */
	@Nullable
	private static CompoundTag getComponentNBT(ItemStack is, @Nullable HolderLookup.Provider provider) {
		DataComponentPatch patch = is.getComponentsPatch().forget(t -> t == DataComponents.DAMAGE);
		if (patch.isEmpty())
			return null;
		var ops = provider != null ? provider.createSerializationContext(NbtOps.INSTANCE) : NbtOps.INSTANCE;
		Tag t = DataComponentPatch.CODEC.encodeStart(ops, patch).result().orElse(null);
		return t instanceof CompoundTag ct && !ct.isEmpty() ? ct : null;
	}

	/** V33a ore dictionary names: the item's common ({@code c:}) tags. */
	private static String[] getOreNames(ItemStack is) {
		return is.getItem().builtInRegistryHolder().tags().filter(t -> "c".equals(t.location().getNamespace()))
				.map(t -> t.location().toString()).sorted().toArray(String[]::new);
	}

	private static boolean isInOreTag(ItemStack is, String tag) {
		return is.is(TagKey.create(net.minecraft.core.registries.Registries.ITEM, Identifier.parse(tag)));
	}

	private static String getRegistrantMod(ItemStack is) {
		return BuiltInRegistries.ITEM.getKey(is.getItem()).getNamespace();
	}

	public static class MatchData {

		private MatchType matchID = MatchType.MATCH;
		private MatchType matchMetadata = MatchType.MATCH;
		private MatchType matchMod = MatchType.MATCH;

		private MatchType doCheckNBT = MatchType.MATCH; //true if any of the matches in matchNBT are not 'ignore'
		private MatchType doCheckOre = MatchType.MATCH;

		private final MatchType[] matchOre;
		private final CompoundTag matchNBT; //compound and list matches if has tag, not tag.equals(); everything else equals
		private final HashMap<String, MatchType> matchClass;

		public final Item itemID;
		public final int metadata;
		public final String modID;
		private final String[] oreDict;
		private final CompoundTag nbt;
		private final Collection<String> classList;

		private MatchData(ItemStack is, @Nullable HolderLookup.Provider provider) {
			itemID = is.getItem();
			metadata = is.getDamageValue();
			modID = getRegistrantMod(is);
			oreDict = getOreNames(is);
			nbt = getComponentNBT(is, provider);
			matchOre = new MatchType[oreDict.length];
			Arrays.fill(matchOre, MatchType.MATCH);
			if (nbt != null) {
				matchNBT = nbt.copy();
				this.parseNBT(matchNBT);
			}
			else {
				matchNBT = null;
			}
			classList = this.calcClasses(is.getItem());
			matchClass = new HashMap<>();
			for (String s : classList) {
				matchClass.put(s, MatchType.MATCH);
			}
		}

		/** Whether this data was built from (an equivalent of) this template. */
		private boolean isFor(ItemStack is, @Nullable HolderLookup.Provider provider) {
			if (is.getItem() != itemID || is.getDamageValue() != metadata)
				return false;
			CompoundTag n = getComponentNBT(is, provider);
			return Objects.equals(n, nbt);
		}

		private Collection<String> calcClasses(Item item) {
			ArrayList<String> li = new ArrayList<>();
			Class<?> c = item.getClass();
			int n = 0;
			do {
				String s = ReikaStringParser.getNOf(">", n);
				li.add(s + c.getSimpleName());
				Class<?>[] ints = c.getInterfaces();
				for (Class<?> anInterface : ints) {
					li.add(s + "%" + anInterface.getSimpleName());
				}
				c = c.getSuperclass();
				n++;
			} while (c != null && c != Item.class); //no point in showing Item (or Object)
			return li;
		}

		private MatchData(Item itemID, int metadata, String modID, CompoundTag nbt, String[] oreDict, Collection<String> classList, MatchType matchID, MatchType matchMetadata, MatchType matchMod, MatchType doCheckNBT, MatchType doCheckOre, MatchType[] matchOre, CompoundTag matchNBT, HashMap<String, MatchType> matchClass) {
			if (matchNBT != null && matchNBT.isEmpty())
				matchNBT = null;
			if (nbt != null && nbt.isEmpty())
				nbt = null;
			this.itemID = itemID;
			this.metadata = metadata;
			this.modID = modID;
			this.nbt = nbt;
			this.oreDict = oreDict;
			this.classList = classList;
			this.matchID = matchID;
			this.matchMetadata = matchMetadata;
			this.matchMod = matchMod;
			this.doCheckNBT = doCheckNBT;
			this.doCheckOre = doCheckOre;
			this.matchOre = matchOre;
			this.matchNBT = matchNBT;
			this.matchClass = matchClass;
		}

		public MatchData loadFrom(MatchData data) {
			if (data != null) {
				matchID = data.matchID;
				matchMetadata = data.matchMetadata;
				matchMod = data.matchMod;
				doCheckNBT = data.doCheckNBT;
				doCheckOre = data.doCheckOre;
				matchClass.clear();
				matchClass.putAll(data.matchClass);
			}
			return this;
		}

		public CompoundTag writeToNBT() {
			CompoundTag tag = new CompoundTag();

			CompoundTag settings = new CompoundTag();
			settings.putInt("item", matchID.ordinal());
			settings.putInt("dmg", matchMetadata.ordinal());
			settings.putInt("mod", matchMod.ordinal());
			settings.putInt("checknbt", doCheckNBT.ordinal());
			settings.putInt("checkore", doCheckOre.ordinal());
			CompoundTag ore2 = new CompoundTag();
			for (int i = 0; i < oreDict.length; i++) {
				ore2.putInt(oreDict[i], matchOre[i].ordinal());
			}
			settings.put("ores", ore2);
			if (nbt != null)
				settings.put("nbt", matchNBT);

			CompoundTag classes = new CompoundTag();
			for (String s : matchClass.keySet()) {
				MatchType m = matchClass.get(s);
				classes.putInt(s, m.ordinal());
			}
			settings.put("classes", classes);

			tag.put("settings", settings);

			CompoundTag val = new CompoundTag();
			val.putString("item", BuiltInRegistries.ITEM.getKey(itemID).toString());
			val.putInt("dmg", metadata);
			val.putString("mod", modID);
			if (nbt != null)
				val.put("nbt", nbt);
			tag.put("value", val);
			ListTag ore = new ListTag();
			for (String s : oreDict) {
				ore.add(StringTag.valueOf(s));
			}
			val.put("ores", ore);
			ListTag classTypes = new ListTag();
			for (String s : classList) {
				classTypes.add(StringTag.valueOf(s));
			}
			val.put("classes", classTypes);

			return tag;
		}

		@Nullable
		public static MatchData createFromNBT(CompoundTag tag) {
			CompoundTag val = NBTCompat.getCompound(tag, "value");
			Item itemID = BuiltInRegistries.ITEM.getValue(Identifier.parse(NBTCompat.getString(val, "item", "minecraft:air")));
			if (itemID == null || itemID == Items.AIR)
				return null;
			int metadata = NBTCompat.getInt(val, "dmg", 0);
			String modID = NBTCompat.getString(val, "mod", "");
			CompoundTag nbt = NBTCompat.getCompound(val, "nbt");
			ListTag ore = val.getListOrEmpty("ores");
			ArrayList<String> li = new ArrayList<>();
			for (int i = 0; i < ore.size(); i++) {
				li.add(ore.getStringOr(i, ""));
			}
			String[] oreDict = li.toArray(new String[0]);
			ListTag classTypes = val.getListOrEmpty("classes");
			ArrayList<String> classList = new ArrayList<>();
			for (int i = 0; i < classTypes.size(); i++) {
				classList.add(classTypes.getStringOr(i, ""));
			}

			CompoundTag settings = NBTCompat.getCompound(tag, "settings");
			MatchType matchID = MatchType.get(NBTCompat.getInt(settings, "item", 0));
			MatchType matchMetadata = MatchType.get(NBTCompat.getInt(settings, "dmg", 0));
			MatchType matchMod = MatchType.get(NBTCompat.getInt(settings, "mod", 0));
			MatchType doCheckNBT = MatchType.get(NBTCompat.getInt(settings, "checknbt", 0));
			MatchType doCheckOre = MatchType.get(NBTCompat.getInt(settings, "checkore", 0));
			CompoundTag ore2 = NBTCompat.getCompound(settings, "ores");
			MatchType[] matchOre = new MatchType[oreDict.length];
			for (int i = 0; i < oreDict.length; i++) {
				matchOre[i] = MatchType.get(NBTCompat.getInt(ore2, oreDict[i], 0));
			}
			CompoundTag matchNBT = NBTCompat.getCompound(settings, "nbt");
			CompoundTag classes = NBTCompat.getCompound(settings, "classes");
			HashMap<String, MatchType> matchClasses = new HashMap<>();
			for (String s : classes.keySet()) {
				MatchType m = MatchType.get(NBTCompat.getInt(classes, s, 0));
				matchClasses.put(s, m);
			}
			return new MatchData(itemID, metadata, modID, nbt, oreDict, classList, matchID, matchMetadata, matchMod, doCheckNBT, doCheckOre, matchOre, matchNBT, matchClasses);
		}

		private void incrementSetting(MatchDisplay m) {
			switch (m.type) {
				case BASIC:
					switch (m.displayName) {
						case "Item ID" -> matchID = matchID.getNext();
						case "Metadata" -> matchMetadata = matchMetadata.getNext();
						case "Mod ID" -> matchMod = matchMod.getNext();
						case "NBT Overall" -> doCheckNBT = doCheckNBT.getNext();
						case "OreDict Overall" -> doCheckOre = doCheckOre.getNext();
					}
					break;
				case NBT:
					CompoundTag b = matchNBT;
					if (m.tags != null) {
						for (String s : m.tags) {
							b = NBTCompat.getCompound(b, s);
						}
					}
					CompoundTag tag = NBTCompat.getCompound(b, m.displayName);
					MatchType match = MatchType.get(NBTCompat.getInt(tag, "type", 0));
					tag.putInt("type", match.getNext().ordinal());
					break;
				case ORE:
					int i = Integer.parseInt(m.displayName);
					matchOre[i] = matchOre[i].getNext();
					break;
				case CLASS:
					matchClass.put(m.internalID, matchClass.get(m.internalID).getNext());
					break;
			}
		}

		public ArrayList<MatchDisplay> getMainDisplay() {
			ArrayList<MatchDisplay> li = new ArrayList<>();
			li.add(new MatchDisplay(this, SettingType.BASIC, "Item ID", BuiltInRegistries.ITEM.getKey(itemID).toString(), "id", matchID));
			li.add(new MatchDisplay(this, SettingType.BASIC, "Metadata", String.valueOf(metadata), "meta", matchMetadata));
			li.add(new MatchDisplay(this, SettingType.BASIC, "Mod ID", modID, "mod", matchMod));
			li.add(new MatchDisplay(this, SettingType.BASIC, "NBT Overall", "", "nbt", doCheckNBT));
			li.add(new MatchDisplay(this, SettingType.BASIC, "OreDict Overall", Arrays.toString(oreDict), "ore", doCheckOre));
			return li;
		}

		public ArrayList<MatchDisplay> getOreDisplay() {
			ArrayList<MatchDisplay> li = new ArrayList<>();
			for (int i = 0; i < oreDict.length; i++) {
				li.add(new MatchDisplay(this, SettingType.ORE, String.valueOf(i), oreDict[i], oreDict[i], matchOre[i]));
			}
			return li;
		}

		public ArrayList<MatchDisplay> getClassDisplay() {
			ArrayList<MatchDisplay> li = new ArrayList<>();
			for (String s : classList) {
				String orig = s;
				String id = "Item Class";
				if (s.startsWith(">")) {
					int n = 0;
					while (s.startsWith(">")) {
						s = s.substring(1);
						n++;
					}
					id = "Parent Class x" + n;
				}
				if (s.startsWith("%")) {
					s = s.substring(1);
					id = "Interface";
				}
				li.add(new MatchDisplay(this, SettingType.CLASS, id, s, orig, matchClass.getOrDefault(orig, MatchType.MATCH)));
			}
			return li;
		}

		public ArrayList<MatchDisplay> getNBTDisplay() {
			if (nbt == null || nbt.isEmpty())
				return new ArrayList<>();
			return this.getNBTDisplay(nbt, matchNBT, new LinkedList<>());
		}

		private ArrayList<MatchDisplay> getNBTDisplay(CompoundTag tag, CompoundTag matchRef, LinkedList<String> tags) {
			ArrayList<MatchDisplay> li = new ArrayList<>();
			for (String s : tag.keySet()) {
				Tag b = tag.get(s);
				CompoundTag match = !tags.isEmpty() && tags.getLast().equals("tag") ? NBTCompat.getCompound(NBTCompat.getCompound(matchRef, "tag"), s) : NBTCompat.getCompound(matchRef, s);
				MatchType m = MatchType.get(NBTCompat.getInt(match, "type", 0));
				if (b instanceof ListTag lt) {
					MatchDisplay md = new MatchDisplay(this, SettingType.NBT, s, "", "", m);
					md.tags = new LinkedList<>(tags);
					li.add(md);
					tags.add(s);
					li.addAll(this.getNBTDisplay(lt, match, tags));
				}
				else if (b instanceof CompoundTag ct) {
					MatchDisplay md = new MatchDisplay(this, SettingType.NBT, s, "", "", m);
					md.tags = new LinkedList<>(tags);
					li.add(md);
					tags.add(s);
					li.addAll(this.getNBTDisplay(ct, match, tags));
				}
				else {
					li.add(new MatchDisplay(this, SettingType.NBT, s, b, tags, "", m));
				}
			}
			if (!tags.isEmpty()) {
				if (tags.getLast().equals("tag"))
					tags.removeLast();
				if (!tags.isEmpty())
					tags.removeLast();
			}
			return li;
		}

		private ArrayList<MatchDisplay> getNBTDisplay(ListTag tag, CompoundTag matchRef, LinkedList<String> tags) {
			ArrayList<MatchDisplay> li = new ArrayList<>();
			for (int i = 0; i < tag.size(); i++) {
				String s = "#" + i;
				Tag b = tag.get(i);
				CompoundTag match = NBTCompat.getCompound(matchRef, s);
				MatchType m = MatchType.get(NBTCompat.getInt(match, "type", 0));
				if (b instanceof ListTag lt) {
					MatchDisplay md = new MatchDisplay(this, SettingType.NBT, s, "", "", m);
					md.tags = new LinkedList<>(tags);
					li.add(md);
					tags.add(s);
					tags.add("tag");
					li.addAll(this.getNBTDisplay(lt, match, tags));
				}
				else if (b instanceof CompoundTag ct) {
					MatchDisplay md = new MatchDisplay(this, SettingType.NBT, s, "", "", m);
					md.tags = new LinkedList<>(tags);
					li.add(md);
					tags.add(s);
					tags.add("tag");
					li.addAll(this.getNBTDisplay(ct, match, tags));
				}
				else {
					li.add(new MatchDisplay(this, SettingType.NBT, s, b, tags, "", m));
				}
			}
			if (!tags.isEmpty()) {
				if (tags.getLast().equals("tag"))
					tags.removeLast();
				if (!tags.isEmpty())
					tags.removeLast();
			}
			return li;
		}

		public boolean match(ItemStack is, @Nullable HolderLookup.Provider provider) {
			if (!matchID.check(is.getItem() == itemID))
				return false;
			if (!matchMetadata.check(is.getDamageValue() == metadata))
				return false;
			if (!matchMod.check(getRegistrantMod(is).equals(modID)))
				return false;
			if (doCheckOre != MatchType.IGNORE) {
				for (int i = 0; i < matchOre.length; i++) {
					String s = oreDict[i];
					if (!matchOre[i].check(isInOreTag(is, s)))
						return false;
				}
			}
			Item item = is.getItem();
			Class<?> c1 = itemID.getClass();
			Class<?> c2 = item.getClass();
			int n = 0;
			do {
				String s = ReikaStringParser.getNOf(">", n);
				MatchType m = matchClass.getOrDefault(s + c1.getSimpleName(), MatchType.MATCH);
				if (!m.check(c1 == c2))
					return false;
				HashSet<Class<?>> ints2 = new HashSet<>(Arrays.asList(c2.getInterfaces()));
				for (Class<?> c : c1.getInterfaces()) {
					m = matchClass.getOrDefault(s + "%" + c.getSimpleName(), MatchType.MATCH);
					if (!m.check(ints2.contains(c)))
						return false;
				}
				c1 = c1.getSuperclass();
				c2 = c2.getSuperclass();
				n++;
			} while (c1 != null && c1 != Item.class && c2 != null && c2 != Item.class);
			if (doCheckNBT != MatchType.IGNORE) {
				CompoundTag isNBT = getComponentNBT(is, provider);
				if (nbt == null && isNBT == null) { //V33a nbt == is.stackTagCompound: both absent
					if (doCheckNBT.check(true))
						return true;
					if (doCheckNBT == MatchType.MISMATCH)
						return false;
				}
				if (nbt == null && isNBT != null) {
					if (!doCheckNBT.check(false))
						return false;
					if (doCheckNBT == MatchType.MISMATCH)
						return true;
				}
				if (nbt != null && isNBT == null) {
					if (!doCheckNBT.check(false))
						return false;
					if (doCheckNBT == MatchType.MISMATCH)
						return true;
				}
                return nbt == null || isNBT == null || this.tryMatchNBT(isNBT, nbt, matchNBT);
			}
			return true;
		}

		private boolean tryMatchNBT(CompoundTag NBT, CompoundTag parentRef, CompoundTag matchRef) {
			if (NBT == null || matchRef == null)
				return false;
			for (String s : matchRef.keySet()) {
				Tag b2 = NBT.get(s);
				CompoundTag match = NBTCompat.getCompound(matchRef, s);
				MatchType m = MatchType.get(NBTCompat.getInt(match, "type", 0));
				if (m == MatchType.IGNORE)
					continue;
				Tag val = match.get("tag");
				if (val != null && b2 != null && val.getClass() != b2.getClass()) {
					return m.check(false);
				}
				if (val instanceof CompoundTag cv) {
					if (!this.tryMatchNBT((CompoundTag)b2, cv, match)) {
						if (!m.check(false))
							return false;
					}
				}
				else if (val instanceof ListTag lv) {
					if (!this.tryMatchNBT((ListTag)b2, lv, match)) {
						if (!m.check(false))
							return false;
					}
				}
				else {
					if (val == b2) {
						return m.check(true);
					}
					if (val == null || b2 == null) {
						return m.check(false);
					}
					if (!m.check(val.equals(b2))) {
						return false;
					}
				}
			}
			return true;
		}

		private boolean tryMatchNBT(ListTag NBT, ListTag parentRef, CompoundTag matchRef) {
			for (int i = 0; i < parentRef.size(); i++) {
				String s = "#" + i;
				Tag b2 = parentRef.get(i);
				CompoundTag match = NBTCompat.getCompound(matchRef, s);
				MatchType m = MatchType.get(NBTCompat.getInt(match, "type", 0));
				if (m == MatchType.IGNORE)
					continue;
				Tag val = match.get("tag");
				if (val == b2)
					return m.check(true);
				if (val == null || b2 == null)
					return m.check(false);
				if (val.getClass() != b2.getClass())
					return m.check(false);
				if (val instanceof CompoundTag cv) {
					if (!this.tryMatchNBT((CompoundTag)b2, cv, match)) {
						return false;
					}
				}
				else if (val instanceof ListTag lv) {
					if (!this.tryMatchNBT((ListTag)b2, lv, match)) {
						if (!m.check(false))
							return false;
					}
				}
				else {
					if (!m.check(val.equals(b2))) {
						if (!m.check(false))
							return false;
					}
				}
			}
			return true;
		}

		/** Turns every leaf into a {name, tag, type} match record, as V33a's parseNBT did (lists become #i compounds). */
		private void parseNBT(CompoundTag NBT) {
			for (String s : new ArrayList<>(NBT.keySet())) {
				Tag b = NBT.get(s);
				if (b instanceof ListTag lt) {
					this.parseNBT(NBT, s, lt);
				}
				else if (b instanceof CompoundTag ct) {
					this.parseNBT(ct);
				}
				else {
					NBT.put(s, NBTMatch.asTag(s, b));
				}
			}
		}

		private void parseNBT(CompoundTag parent, String parentName, ListTag NBT) {
			ListTag copy = NBT.copy();
			CompoundTag repl = new CompoundTag();
			for (int i = 0; i < copy.size(); i++) {
				String s = "#" + i;
				Tag b = copy.get(i);
				if (b instanceof CompoundTag ct) {
					CompoundTag c2 = ct.copy();
					this.parseNBT(c2);
					repl.put(s, combine(c2, NBTMatch.asTag(s, b)));
				}
				else if (b instanceof ListTag lt) {
					CompoundTag holder = new CompoundTag();
					this.parseNBT(holder, s, lt);
					repl.put(s, holder.get(s));
				}
				else {
					repl.put(s, NBTMatch.asTag(s, b));
				}
			}
			parent.put(parentName, combine(repl, NBTMatch.asTag(parentName, NBT)));
		}

		/** V33a ReikaNBTHelper.combineNBT: copy every key of {@code b} into {@code a} that it lacks. */
		private static CompoundTag combine(CompoundTag a, CompoundTag b) {
			for (String s : b.keySet()) {
				if (!a.contains(s))
					a.put(s, b.get(s).copy());
			}
			return a;
		}

		@Override
		public String toString() {
			return matchID + "/" + matchMetadata + "/" + matchMod + "/" + doCheckOre + "/" + doCheckNBT + " = " + itemID + " / " + metadata + " / " + modID + " / " + Arrays.toString(oreDict) + " / " + Arrays.toString(matchOre) + " / " + matchNBT;
		}
	}

	private static class NBTMatch {

		public static CompoundTag asTag(String s, Tag b) {
			CompoundTag tag = new CompoundTag();
			tag.putString("name", s);
			tag.put("tag", b.copy());
			tag.putInt("type", MatchType.MATCH.ordinal());
			return tag;
		}

	}

	public static class MatchDisplay {

		private final MatchData source;
		private final SettingType type;

		private LinkedList<String> tags;

		private MatchType setting;
		public final String displayName;
		public final String value;

		private final String internalID;

		private MatchDisplay(MatchData src, SettingType type, String s, String val, String id, MatchType m) {
			setting = m;
			displayName = s;
			value = val;
			source = src;
			this.type = type;
			tags = null;
			internalID = id;
		}

		private MatchDisplay(MatchData src, SettingType type, String s, Tag b, LinkedList<String> li, String id, MatchType m) {
			this(src, type, s, b.toString(), id, m);
			tags = new LinkedList<>(li);
		}

		public MatchType getSetting() {
			return setting;
		}

		public void increment() {
			setting = setting.getNext();
			source.incrementSetting(this);
		}

		@Override
		public String toString() {
			return displayName + ": " + setting.toString() + " & " + type.toString() + " % " + tags;
		}

	}

	public enum MatchType {
		MATCH(0x008000, "Match"),
		MISMATCH(0xa00000, "Mismatch"),
		IGNORE(0xffd000, "Ignore");

		private static final MatchType[] list = values();
		public final String name;
		public final int color;

		MatchType(int c, String n) {
			name = n;
			color = c;
		}

		private static MatchType get(int i) {
			return list[Math.max(0, Math.min(i, list.length - 1))];
		}

		public MatchType getNext() {
			return this.ordinal() == list.length - 1 ? list[0] : list[this.ordinal() + 1];
		}

		public boolean check(boolean match) {
			return switch (this) {
				case IGNORE -> true;
				case MATCH -> match;
				case MISMATCH -> !match;
			};
		}
	}

	public enum SettingType {
		BASIC(),
		ORE(),
		NBT(),
		CLASS();

		private static final SettingType[] list = values();

		public SettingType previous() {
			return this.ordinal() == 0 ? this : list[this.ordinal() - 1];
		}

		public SettingType next() {
			return this.ordinal() == list.length - 1 ? this : list[this.ordinal() + 1];
		}
	}

}
