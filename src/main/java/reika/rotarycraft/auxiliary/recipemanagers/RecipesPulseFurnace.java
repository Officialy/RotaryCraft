///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.auxiliary.recipemanagers;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//
//import net.minecraft.util.Mth;
//import net.minecraft.world.item.*;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//
//import reika.dragonapi.ModList;
//import reika.dragonapi.auxiliary.trackers.ItemMaterialController;
//import reika.dragonapi.instantiable.ItemMaterial;
//import reika.dragonapi.instantiable.data.maps.ItemHashMap;
//import reika.dragonapi.instantiable.io.CustomRecipeList;
//import reika.dragonapi.instantiable.io.LuaBlock;
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.api.RecipeInterface;
//import reika.rotarycraft.api.RecipeInterface.PulseFurnaceManager;
//import reika.rotarycraft.blockentities.processing.BlockEntityPulseFurnace;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryBlocks;
//import reika.rotarycraft.registry.RotaryItems;
//
//public class RecipesPulseFurnace extends RecipeHandler implements PulseFurnaceManager {
//
//	private static final RecipesPulseFurnace PulseFurnaceBase = new RecipesPulseFurnace();
//
//	private ItemHashMap<PulseJetRecipe> recipes = new ItemHashMap<>();
//
//	public static RecipesPulseFurnace getRecipes()
//	{
//		return PulseFurnaceBase;
//	}
//
//	private RecipesPulseFurnace() {
//		super(MachineRegistry.PULSEJET);
//		RecipeInterface.pulsefurn = this;
//
//		this.addSmelting(Blocks.OBSIDIAN.asItem().getDefaultInstance(), RotaryBlocks.BLASTGLASS.get().asItem().getDefaultInstance(), 850, RecipeLevel.CORE);
//		this.addSmelting(Items.IRON_INGOT.getDefaultInstance(), ReikaItemHelper.getSizedItemStack(RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance(), 2), 900, RecipeLevel.CORE);
//
//		this.addRecycling();
//
//		this.addSmelting(RotaryItems.INDUCTIVE_BLEND.get().getDefaultInstance(), RotaryItems.INDUCTIVE_INGOT.get().getDefaultInstance(), RecipeLevel.CORE);
//
//		//addSmelting(RotaryCraft.shaftcraft, 10, new ItemStack(Items.iron_ingot, 1));	//scrap
//		//addSmelting(RotaryCraft.shaftcraft, 9, new ItemStack(RotaryCraft.shaftcraft, 1, 1));	//Iron scrap
//	}
//
//	public static class PulseJetRecipe implements MachineRecipe {
//
//		private final ItemStack input;
//		private final ItemStack output;
//
//		public final int requiredTemperature;
//
//		private PulseJetRecipe(ItemStack in, ItemStack out) {
//			this(in, out, getDefaultMeltingTemp(in));
//		}
//
//		private PulseJetRecipe(ItemStack in, ItemStack out, int temp) {
//			input = in;
//			output = out;
//
//			requiredTemperature = temp;
//		}
//
//		public ItemStack getInput() {
//			return input.copy();
//		}
//
//		public ItemStack getOutput() {
//			return output.copy();
//		}
//
//		public boolean makesItem(ItemStack is) {
//			return ReikaItemHelper.matchStacks(is, output);
//		}
//
//		@Override
//		public String getUniqueID() {
//			return fullID(input)+">"+fullID(output);
//		}
//
//		@Override
//		public String getAllInfo() {
//			return "Smelting "+fullID(input)+" into "+fullID(output);
//		}
//
//		@Override
//		public Collection<ItemStack> getAllUsedItems() {
//			return ReikaJavaLibrary.makeListFrom(input, output);
//		}
//
//	}
//
//	private void addRecycling() {
//		this.addRecycling(Items.CHAINMAIL_HELMET, new ItemStack(Items.IRON_INGOT, 3), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.CHAINMAIL_BOOTS, new ItemStack(Items.IRON_INGOT, 2), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.CHAINMAIL_LEGGINGS, new ItemStack(Items.IRON_INGOT, 4), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.CHAINMAIL_CHESTPLATE, new ItemStack(Items.IRON_INGOT, 5), RecipeLevel.PROTECTED);
//
//		this.addRecycling(Items.IRON_HELMET, new ItemStack(Items.IRON_INGOT, 5), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.IRON_CHESTPLATE, new ItemStack(Items.IRON_INGOT, 8), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.IRON_LEGGINGS, new ItemStack(Items.IRON_INGOT, 7), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.IRON_BOOTS, new ItemStack(Items.IRON_INGOT, 4), RecipeLevel.PROTECTED);
//
//		this.addRecycling(Items.IRON_HOE, new ItemStack(Items.IRON_INGOT, 2), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.IRON_SHOVEL, new ItemStack(Items.IRON_INGOT, 1), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.IRON_AXE, new ItemStack(Items.IRON_INGOT, 3), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.IRON_PICKAXE, new ItemStack(Items.IRON_INGOT, 3), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.IRON_SWORD, new ItemStack(Items.IRON_INGOT, 2), RecipeLevel.PROTECTED);
//
//		this.addRecycling(Items.GOLDEN_HELMET, new ItemStack(Items.GOLD_INGOT, 5), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.GOLDEN_CHESTPLATE, new ItemStack(Items.GOLD_INGOT, 8), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.GOLDEN_LEGGINGS, new ItemStack(Items.GOLD_INGOT, 7), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.GOLDEN_BOOTS, new ItemStack(Items.GOLD_INGOT, 4), RecipeLevel.PROTECTED);
//
//		this.addRecycling(Items.GOLDEN_AXE, new ItemStack(Items.GOLD_INGOT, 3), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.GOLDEN_SWORD, new ItemStack(Items.GOLD_INGOT, 2), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.GOLDEN_SHOVEL, new ItemStack(Items.GOLD_INGOT, 1), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.GOLDEN_PICKAXE, new ItemStack(Items.GOLD_INGOT, 3), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.GOLDEN_HOE, new ItemStack(Items.GOLD_INGOT, 2), RecipeLevel.PROTECTED);
//
//		this.addRecycling(Items.DIAMOND_HELMET, new ItemStack(Items.DIAMOND, 5), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.DIAMOND_CHESTPLATE, new ItemStack(Items.DIAMOND, 8), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.DIAMOND_LEGGINGS, new ItemStack(Items.DIAMOND, 7), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.DIAMOND_BOOTS, new ItemStack(Items.DIAMOND, 4), RecipeLevel.PROTECTED);
//
//		this.addRecycling(Items.DIAMOND_AXE, new ItemStack(Items.DIAMOND, 3), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.DIAMOND_SWORD, new ItemStack(Items.DIAMOND, 2), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.DIAMOND_SHOVEL, new ItemStack(Items.DIAMOND, 1), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.DIAMOND_PICKAXE, new ItemStack(Items.DIAMOND, 3), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.DIAMOND_HOE, new ItemStack(Items.DIAMOND, 2), RecipeLevel.PROTECTED);
//
//		this.addRecycling(Items.IRON_HORSE_ARMOR, new ItemStack(Items.IRON_INGOT, 7), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.DIAMOND_HORSE_ARMOR, new ItemStack(Items.DIAMOND, 7), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.GOLDEN_HORSE_ARMOR, new ItemStack(Items.GOLD_INGOT, 7), RecipeLevel.PROTECTED);
//
//		this.addRecycling(Items.FLINT_AND_STEEL, new ItemStack(Items.IRON_INGOT, 1), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.BUCKET, new ItemStack(Items.IRON_INGOT, 3), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.WATER_BUCKET, new ItemStack(Items.IRON_INGOT, 3), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.LAVA_BUCKET, new ItemStack(Items.IRON_INGOT, 3), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.MILK_BUCKET, new ItemStack(Items.IRON_INGOT, 3), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.MINECART, new ItemStack(Items.IRON_INGOT, 5), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.IRON_DOOR, new ItemStack(Items.IRON_INGOT, 6), RecipeLevel.PROTECTED);
//		this.addRecycling(Items.CAULDRON, new ItemStack(Items.IRON_INGOT, 7), RecipeLevel.PROTECTED);
////todo		if (!Loader.isModLoaded("DragonRealmCore"))
////			this.addRecycling(Items.HOPPER_MINECART, new ItemStack(Items.IRON_INGOT, 10), RecipeLevel.PROTECTED);
//
//		this.addRecycling(RotaryItems.HSLA_HELMET.get(), this.getSizedSteel(5), RecipeLevel.PROTECTED);
//		this.addRecycling(RotaryItems.HSLA_BOOTS.get(), this.getSizedSteel(4), RecipeLevel.PROTECTED);
//		this.addRecycling(RotaryItems.HSLA_CHESTPLATE.get(), this.getSizedSteel(8), RecipeLevel.PROTECTED);
//		this.addRecycling(RotaryItems.HSLA_LEGGINGS.get(), this.getSizedSteel(7), RecipeLevel.PROTECTED);
//		this.addRecycling(RotaryItems.HSLA_STEEL_AXE.get(), this.getSizedSteel(3), RecipeLevel.PROTECTED);
//		this.addRecycling(RotaryItems.HSLA_STEEL_PICKAXE.get(), this.getSizedSteel(3), RecipeLevel.PROTECTED);
//		this.addRecycling(RotaryItems.HSLA_STEEL_SHOVEL.get(), this.getSizedSteel(1), RecipeLevel.PROTECTED);
//		this.addRecycling(RotaryItems.HSLA_STEEL_HOE.get(), this.getSizedSteel(2), RecipeLevel.PROTECTED);
//		this.addRecycling(RotaryItems.HSLA_STEEL_SHEARS.get(), this.getSizedSteel(2), RecipeLevel.PROTECTED);
////		this.addRecycling(RotaryItems.HSLA_STEEL_SICKLE.get(), this.getSizedSteel(3), RecipeLevel.PROTECTED);
//		this.addRecycling(RotaryItems.HSLA_STEEL_SWORD.get(), this.getSizedSteel(2), RecipeLevel.PROTECTED);
//
//		if (!ModList.RAILCRAFT.isLoaded()) { //exploit fix; he changes recipes
//			this.addRecycling(Blocks.DETECTOR_RAIL, new ItemStack(Items.IRON_INGOT, 1), RecipeLevel.PERIPHERAL);	//1 ingot per block of rail
//			this.addRecycling(Blocks.POWERED_RAIL, new ItemStack(Items.GOLD_INGOT, 1), RecipeLevel.PERIPHERAL);
//		}
//	}
//
//	private void addRecycling(Block in, ItemStack out, RecipeLevel rl) {
//		this.addRecycling(new ItemStack(in), out, rl);
//	}
//
//	private void addRecycling(Item in, ItemStack out, RecipeLevel rl) {
//		this.addRecycling(new ItemStack(in), out, rl);
//	}
//
//	private void addRecycling(ItemStack in, ItemStack out, RecipeLevel rl) {
//		if (in.getItem() instanceof SwordItem || in.getItem() instanceof DiggerItem || in.getItem() instanceof ArmorItem) {
//			in = in.copy();
////			in.setItemDamage(OreDictionary.WILDCARD_VALUE);
//		}
//	/* todo	List<IRecipe> li = ReikaRecipeHelper.getAllRecipesByOutput(CraftingManager.getInstance().getRecipeList(), in);
//		if (li != null && li.size() > 1) {
//			RotaryCraft.LOGGER.info("Skipping recycling of "+this.fullID(in)+" to "+this.fullID(out)+" due to multiple production recipes");
//			return;
//		}*/
//		PulseJetRecipe rec = this.addSmelting(in, out, rl);
//	}
//
//	private ItemStack getSizedSteel(int size) {
//		return ReikaItemHelper.getSizedItemStack(RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance(), size);
//	}
//
//	public void addAPISmelting(ItemStack in, ItemStack itemstack) {
//		this.addSmelting(in, itemstack, RecipeLevel.API);
//	}
//
//	public void addSmelting(ItemStack in, ItemStack itemstack) {
//		this.addSmelting(in, itemstack, RecipeLevel.CORE);
//	}
//
//	private void addSmelting(ItemStack in, ItemStack itemstack, int temp, RecipeLevel rl) {
//		PulseJetRecipe rec = new PulseJetRecipe(in, itemstack, temp);
//		this.createRecipe(rec, rl);
//	}
//
//	private PulseJetRecipe addSmelting(ItemStack in, ItemStack itemstack, RecipeLevel rl) {
//		PulseJetRecipe rec = new PulseJetRecipe(in, itemstack);
//		this.createRecipe(rec, rl);
//		return rec;
//	}
//
//	private void createRecipe(PulseJetRecipe rec, RecipeLevel rl) {
//		recipes.put(rec.input, rec);
//		this.onAddRecipe(rec, rl);
//	}
//
///*	private void addSmelting(Block b, ItemStack itemstack, RecipeLevel rl) {
//		this.addSmelting(new ItemStack(b, 1, OreDictionary.WILDCARD_VALUE), itemstack, rl);
//	}
//
//	private void addSmelting(Block b, ItemStack itemstack, int temp, RecipeLevel rl) {
//		this.addSmelting(new ItemStack(b, 1, OreDictionary.WILDCARD_VALUE), itemstack, temp, rl);
//	}
//
//	private void addSmelting(Item i, ItemStack itemstack, RecipeLevel rl) {
//		this.addSmelting(new ItemStack(i, 1, OreDictionary.WILDCARD_VALUE), itemstack, rl);
//	}
//
//	private void addSmelting(Item i, ItemStack itemstack, int temp, RecipeLevel rl) {
//		this.addSmelting(new ItemStack(i, 1, OreDictionary.WILDCARD_VALUE), itemstack, temp, rl);
//	}*/
//
//	public void addCustomRecipe(ItemStack in, ItemStack output, int temp) {
//		if (temp < 0)
//			temp = getDefaultMeltingTemp(in);
//		this.addSmelting(in, output, temp, RecipeLevel.CUSTOM);
//	}
//
//	public PulseJetRecipe getSmeltingResult(ItemStack item) {
//		PulseJetRecipe ret = item != null ? recipes.get(item) : null;
//		return ret;
//	}
//
//	public List<PulseJetRecipe> getSources(ItemStack result) {
//		List<PulseJetRecipe> li = new ArrayList();
//		for (ItemStack in : recipes.keySet()) {
//			PulseJetRecipe out = this.getSmeltingResult(in);
//			if (out != null && ReikaItemHelper.matchStacks(result, out.output))
//				li.add(out);
//		}
//		return li;
//	}
//
//	public boolean isProduct(ItemStack item) {
//		for (PulseJetRecipe pjr : recipes.values()) {
//			if (pjr.makesItem(item))
//				return true;
//		}
//		return false;
//	}
//
//	public boolean isSmeltable(ItemStack ingredient) {
//		return this.getSmeltingResult(ingredient) != null;
//	}
//
//	public Collection<ItemStack> getAllSmeltables() {
//		return Collections.unmodifiableCollection(recipes.keySet());
//	}
//
//	@Override
//	public void addPostLoadRecipes() {
//	/*	if (ModList.THERMALFOUNDATION.isLoaded()) {
//			ItemStack enderdust = GameRegistry.findItemStack(ModList.THERMALFOUNDATION.modLabel, "dustEnderium", 1);
//			ItemStack enderingot = GameRegistry.findItemStack(ModList.THERMALFOUNDATION.modLabel, "ingotEnderium", 1);
//			if (enderdust == null || enderingot == null)
//				RotaryCraft.LOGGER.error("No item found for TE3 enderium crafting!");
//			else
//				this.addSmelting(enderdust, enderingot, RecipeLevel.MODINTERACT);
//		}
//
//		if (ModList.ARSENAL.isLoaded()) {
//			ItemStack fluxdust = RedstoneArsenalHandler.getInstance().getFluxDust();
//			ItemStack fluxingot = RedstoneArsenalHandler.getInstance().getFluxIngot();
//			if (fluxdust == null || fluxingot == null)
//				RotaryCraft.LOGGER.error("No item found for Redstone Arsenal fluxed ingot crafting!");
//			else
//				this.addSmelting(fluxdust, fluxingot, RecipeLevel.MODINTERACT);
//		}
//
//		if (ModList.IC2.isLoaded()) {
//			ItemStack[] items = {
//					IC2Handler.IC2Stacks.BRONZEAXE.getItem(),
//					IC2Handler.IC2Stacks.BRONZEPICK.getItem(),
//					IC2Handler.IC2Stacks.BRONZEHOE.getItem(),
//					IC2Handler.IC2Stacks.BRONZESWORD.getItem(),
//					IC2Handler.IC2Stacks.BRONZESHOVEL.getItem(),
//					IC2Handler.IC2Stacks.BRONZEHELMET.getItem(),
//					IC2Handler.IC2Stacks.BRONZECHESTPLATE.getItem(),
//					IC2Handler.IC2Stacks.BRONZELEGGINGS.getItem(),
//					IC2Handler.IC2Stacks.BRONZEBOOTS.getItem(),
//			};
//
//			int[] n = {
//					3, 3, 2, 2, 1, 5, 8, 7, 4
//			};
//
//			ItemStack out = OreDictionary.getOres("ingotBronze").get(0);
//			for (int i = 0; i < items.length; i++) {
//				if (items[i] != null) {
//					this.addRecycling(items[i].getItem(), ReikaItemHelper.getSizedItemStack(out, n[i]), RecipeLevel.MODINTERACT);
//				}
//			}
//		}
//
//		if (ModList.RAILCRAFT.isLoaded()) {
//			Object[] items = {
//					"tool.steel.pickaxe",
//					"tool.steel.axe",
//					"tool.steel.sword",
//					"tool.steel.hoe",
//					"tool.steel.shovel",
//					"armor.steel.helmet",
//					"armor.steel.plate",
//					"armor.steel.legs",
//					"armor.steel.boots",
//					"tool.steel.shears",
//					"tool.crowbar",
//					"tool.crowbar.reinforced"
//			};
//
//			int[] n = {
//					3, 3, 2, 2, 1, 5, 8, 7, 4, 2, 3, 3
//			};
//
//			for (int i = 0; i < items.length; i++) {
//				items[i] = ReikaItemHelper.lookupItem(ModList.RAILCRAFT, (String)items[i], 0);
//			}
//
//			ItemStack out = ReikaItemHelper.lookupItem(ModList.RAILCRAFT, "ingot", 0);
//			if (out == null) {
//				List<ItemStack> li = OreDictionary.getOres("ingotSteel");
//				out = li.isEmpty() ? RotaryItems.steelingot.copy() : li.get(0);
//			}
//			ItemStack[] outarr = ReikaArrayHelper.getArrayOf(out, items.length);
//			outarr[outarr.length-2] = new ItemStack(Items.iron_ingot);
//
//			for (int i = 0; i < items.length; i++) {
//				if (items[i] != null) {
//					ItemStack outb = outarr[i];
//					this.addRecycling(((ItemStack)items[i]).getItem(), ReikaItemHelper.getSizedItemStack(outb, n[i]), RecipeLevel.MODINTERACT);
//				}
//			}
//		}
//
//		if (ModList.ENDERIO.isLoaded()) {
//			Object[] items = {
//					"item.darkSteel_pickaxe",
//					"item.darkSteel_axe",
//					"item.darkSteel_sword",
//					"item.darkSteel_helmet",
//					"item.darkSteel_chestplate",
//					"item.darkSteel_leggings",
//					"item.darkSteel_boots",
//					"item.darkSteel_shears",
//			};
//
//			int[] n = {
//					3, 3, 2, 5, 8, 7, 4, 2
//			};
//
//			for (int i = 0; i < items.length; i++) {
//				items[i] = ReikaItemHelper.lookupItem(ModList.ENDERIO, (String)items[i], 0);
//			}
//
//			ItemStack out = ReikaItemHelper.lookupItem(ModList.ENDERIO, "itemAlloy", 6);
//			for (int i = 0; i < items.length; i++) {
//				if (items[i] != null) {
//					this.addRecycling(((ItemStack)items[i]).getItem(), ReikaItemHelper.getSizedItemStack(out, n[i]), RecipeLevel.MODINTERACT);
//				}
//			}
//		}
//
//		if (ModList.THAUMCRAFT.isLoaded()) {
//			Object[] items = {
//					"ItemPickThaumium",
//					"ItemAxeThaumium",
//					"ItemSwordThaumium",
//					"ItemHoeThaumium",
//					"ItemShovelThaumium",
//					"ItemHelmetThaumium",
//					"ItemChestplateThaumium",
//					"ItemLeggingsThaumium"
//			};
//
//			int[] n = {
//					3, 3, 2, 2, 1, 5, 8, 7
//			};
//
//			for (int i = 0; i < items.length; i++) {
//				items[i] = ReikaItemHelper.lookupItem(ModList.THAUMCRAFT, (String)items[i], 0);
//			}
//
//			ItemStack out = ThaumItemHelper.ItemEntry.THAUMIUM.getItem();
//			for (int i = 0; i < items.length; i++) {
//				if (items[i] != null) {
//					this.addRecycling(((ItemStack)items[i]).getItem(), ReikaItemHelper.getSizedItemStack(out, n[i]), RecipeLevel.MODINTERACT);
//				}
//				else {
//					RotaryCraft.logger.logError("Could not find thaumium tool #"+i);
//				}
//			}
//		}
//
//		if (ModList.MEKTOOLS.isLoaded()) {
//			for (Materials m : Materials.values()) {
//				ItemStack out = m.getRawMaterial();
//				for (Tools t : Tools.values()) {
//					Item i = m.getItem(t);
//					if (i != null) {
//						this.addRecycling(i, ReikaItemHelper.getSizedItemStack(out, t.getNumberIngots(m)), RecipeLevel.MODINTERACT);
//					}
//				}
//			}
//		}*/
//	}
//
//	@Override
//	protected boolean removeRecipe(MachineRecipe recipe) {
//		return recipes.removeValue((PulseJetRecipe)recipe);
//	}
//
//	@Override
//	protected boolean addCustomRecipe(String n, LuaBlock lb, CustomRecipeList crl) throws Exception {
//		ItemStack in = crl.parseItemString(lb.getString("input"), lb.getChild("input_nbt"), false);
//		ItemStack out = crl.parseItemString(lb.getString("output"), lb.getChild("output_nbt"), false);
//		int temp = getDefaultMeltingTemp(in);
//		if (lb.containsKey("required_temperature")) {
//			temp = lb.getInt("required_temperature");
//			if (temp <= 200) {
//				throw new IllegalArgumentException("Temperature is too low");
//			}
//			else if (temp >= BlockEntityPulseFurnace.MAXTEMP) {
//				throw new IllegalArgumentException("Recipe is impossible - temperature exceeds maximum");
//			}
//		}
//		this.verifyOutputItem(out);
//		this.addCustomRecipe(in, out, temp);
//		return true;
//	}
//
//	private static int getDefaultMeltingTemp(ItemStack is) {
//		if (ItemMaterialController.instance.getMaterial(is) == ItemMaterial.OBSIDIAN)
//			return ItemMaterialController.instance.getMeltingPoint(is);
//		return Mth.clamp(ItemMaterialController.instance.getMeltingPoint(is)/2, 400, 850);
//	}
//}
