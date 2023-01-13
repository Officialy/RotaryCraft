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
//import java.util.Collection;
//import java.util.List;
//
//import net.minecraft.block.Block;
//import net.minecraft.init.Blocks;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//
//import reika.dragonapi.instantiable.data.maps.ItemHashMap;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.libraries.registry.ReikaOreHelper;
//import reika.dragonapi.ModRegistry.ModOreList;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.CustomExtractLoader;
//import reika.rotarycraft.auxiliary.CustomExtractLoader.CustomExtractEntry;
//import reika.rotarycraft.auxiliary.RotaryItems;
//import reika.rotarycraft.auxiliary.recipemanagers.ExtractorModOres.ExtractorStage;
//import reika.rotarycraft.ModInterface.ItemCustomModOre;
//import reika.rotarycraft.registry.RotaryItems;
//
//public class RecipesExtractor
//{
//	private static final RecipesExtractor instance = new RecipesExtractor();
//
//	private final ItemHashMap<ItemStack> recipeList = new ItemHashMap().setOneWay();
//
//	public static final RecipesExtractor getRecipes()
//	{
//		return instance;
//	}
//
//	private RecipesExtractor()
//	{
//		for (int i = 0; i < 24; i++)
//			this.addRecipe(RotaryItems.EXTRACTS.getStackOfMetadata(i), RotaryItems.EXTRACTS.getStackOfMetadata(i+8));
//
//		this.addRecipe(Blocks.coal_ore, 0, RotaryItems.getDust(ReikaOreHelper.COAL));
//		this.addRecipe(Blocks.iron_ore, 0, RotaryItems.getDust(ReikaOreHelper.IRON));
//		this.addRecipe(Blocks.gold_ore, 0, RotaryItems.getDust(ReikaOreHelper.GOLD));
//		this.addRecipe(Blocks.redstone_ore, 0, RotaryItems.getDust(ReikaOreHelper.REDSTONE));
//		this.addRecipe(Blocks.lapis_ore, 0, RotaryItems.getDust(ReikaOreHelper.LAPIS));
//		this.addRecipe(Blocks.diamond_ore, 0, RotaryItems.getDust(ReikaOreHelper.DIAMOND));
//		this.addRecipe(Blocks.emerald_ore, 0, RotaryItems.getDust(ReikaOreHelper.EMERALD));
//		this.addRecipe(Blocks.quartz_ore, 0, RotaryItems.getDust(ReikaOreHelper.QUARTZ));
//
//		this.addModRecipes();
//	}
//
//	private void addModRecipes() {
//		for (int i = 0; i < ModOreList.oreList.length; i++) {
//			ModOreList ore = ModOreList.oreList[i];
//			Collection<ItemStack> c = ore.getAllOreBlocks();
//			for (ItemStack is : c) {
//				if (recipeList.containsKey(is)) {
//					ModOreList mod = (ModOreList)ExtractorModOres.getOreFromExtract(recipeList.get(is));
//					RotaryCraft.LOGGER.info("Ore "+is.getDisplayName()+" is being skipped for Extractor registration as "+ore+" as it is already registered to "+mod);
//				}
//				else {
//					this.addRecipe(is, ExtractorModOres.getDustProduct(ore));
//				}
//			}
//			this.addRecipe(ExtractorModOres.getDustProduct(ore), ExtractorModOres.getSlurryProduct(ore));
//			this.addRecipe(ExtractorModOres.getSlurryProduct(ore), ExtractorModOres.getSolutionProduct(ore));
//			this.addRecipe(ExtractorModOres.getSolutionProduct(ore), ExtractorModOres.getFlakeProduct(ore));
//		}
//
//		List<CustomExtractEntry> li = CustomExtractLoader.instance.getEntries();
//		for (int i = 0; i < li.size(); i++) {
//			CustomExtractEntry e = li.get(i);
//			Collection<ItemStack> c = e.getAllOreBlocks();
//			for (ItemStack is : c) {
//				if (recipeList.containsKey(is)) {
//					RotaryCraft.LOGGER.info("Ore "+is.getDisplayName()+" is being skipped for Extractor registration as "+e+" as it is already registered to "+recipeList.get(is));
//				}
//				else {
//					this.addRecipe(is, ItemCustomModOre.getItem(i, ExtractorStage.DUST));
//				}
//			}
//			this.addRecipe(ItemCustomModOre.getItem(i, ExtractorStage.DUST), ItemCustomModOre.getItem(i, ExtractorStage.SLURRY));
//			this.addRecipe(ItemCustomModOre.getItem(i, ExtractorStage.SLURRY), ItemCustomModOre.getItem(i, ExtractorStage.SOLUTION));
//			this.addRecipe(ItemCustomModOre.getItem(i, ExtractorStage.SOLUTION), ItemCustomModOre.getItem(i, ExtractorStage.FLAKES));
//		}
//	}
//
//	private void addRecipe(Block in, ItemStack out)
//	{
//		this.addRecipe(new ItemStack(in), out);
//	}
//
//	private void addRecipe(Block in, int meta, ItemStack out)
//	{
//		this.addRecipe(new ItemStack(in, 1, meta), out);
//	}
//
//	private void addRecipe(Item in, ItemStack out)
//	{
//		this.addRecipe(new ItemStack(in), out);
//	}
//
//	private void addRecipe(Item in, int dmg, ItemStack out)
//	{
//		this.addRecipe(new ItemStack(in, 1, dmg), out);
//	}
//
//	private void addRecipe(ItemStack in, ItemStack out)
//	{
//		recipeList.put(in, out);
//	}
//
//	public ItemStack getExtractionResult(ItemStack item)
//	{
//		if (item == null)
//			return null;
//		ReikaOreHelper ore = ReikaOreHelper.getEntryByOreDict(item);
//		if (ore != null) {
//			item = ore.getOreBlock();
//		}
//		ItemStack ret = recipeList.get(item);
//		return ret != null ? ret.copy() : null;
//	}
//
//	public static boolean isDust(ItemStack is) {
//		if (!RotaryItems.EXTRACTS.matchItem(is))
//			return false;
//		int dmg = is.getItemDamage();
//		return dmg < ReikaOreHelper.oreList.length;
//	}
//
//	public static boolean isSlurry(ItemStack is) {
//		if (!RotaryItems.EXTRACTS.matchItem(is))
//			return false;
//		int dmg = is.getItemDamage();
//		return dmg < ReikaOreHelper.oreList.length*2 && dmg >= ReikaOreHelper.oreList.length;
//	}
//
//	public static boolean isSolution(ItemStack is) {
//		if (!RotaryItems.EXTRACTS.matchItem(is))
//			return false;
//		int dmg = is.getItemDamage();
//		return dmg < ReikaOreHelper.oreList.length*3 && dmg >= ReikaOreHelper.oreList.length*2;
//	}
//
//	public static boolean isFlakes(ItemStack is) {
//		if (!RotaryItems.EXTRACTS.matchItem(is))
//			return false;
//		if (ReikaItemHelper.matchStacks(is, RotaryItems.silverflakes) || ReikaItemHelper.matchStacks(is, RotaryItems.tungstenflakes))
//			return true;
//		int dmg = is.getItemDamage();
//		return dmg < ReikaOreHelper.oreList.length*4 && dmg >= ReikaOreHelper.oreList.length*3;
//	}
//
//	public static ReikaOreHelper getOreFromExtract(ItemStack item) {
//		return ReikaOreHelper.oreList[item.getItemDamage()%ReikaOreHelper.oreList.length];
//	}
//}
