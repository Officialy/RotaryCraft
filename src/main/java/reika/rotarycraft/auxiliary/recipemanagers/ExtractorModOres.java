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
//import java.util.List;
//
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import reika.dragonapi.interfaces.registry.OreType;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.modregistry.ModOreList;
//import reika.rotarycraft.auxiliary.CustomExtractLoader;
//import reika.rotarycraft.auxiliary.CustomExtractLoader.CustomExtractEntry;
//import reika.rotarycraft.base.AutoOreItem;
//import reika.rotarycraft.registry.RotaryItems;
//
//public class ExtractorModOres {
//
//	public static void registerRCIngots() {
//		for (int i = 0; i < ModOreList.oreList.length; i++) {
//			OreDictionary.registerOre(ModOreList.oreList[i].getProductOreDictName(), RotaryItems.MODINGOTS.getStackOfMetadata(i));
//		}
//
//		OreDictionary.registerOre("ingotHeeEndium", RotaryItems.MODINGOTS.getStackOfMetadata(ModOreList.ENDIUM.ordinal()));
//
//		List<CustomExtractEntry> li = CustomExtractLoader.instance.getEntries();
//		for (int i = 0; i < li.size(); i++) {
//			CustomExtractEntry e = li.get(i);
//			OreType nat = e.nativeOre;
//			if (nat == null) {
//				ItemStack out = ItemCustomModOre.getSmeltedItem(i);
//				OreDictionary.registerOre(e.productName, out);
//			}
//		}
//	}
//
//	public static void addSmelting() {
//		for (int i = 0; i < ModOreList.oreList.length; i++) {
//			ModOreList ore = ModOreList.oreList[i];
//			ItemStack in = RotaryItems.MODEXTRACTS.getStackOfMetadata(getFlakesIndex(ore));
//			ItemStack out = ReikaItemHelper.getSizedItemStack(getSmeltedIngot(ore), ore.getDropCount());
//			ReikaRecipeHelper.addSmelting(in, out, ore.rarity == OreType.OreRarity.RARE ? 1 : ore.rarity == OreType.OreRarity.EVERYWHERE ? 0.5F : 0.7F);
//		}
//	}
//
//	public static void addCustomSmelting() {
//		List<CustomExtractEntry> li = CustomExtractLoader.instance.getEntries();
//		for (int i = 0; i < li.size(); i++) {
//			CustomExtractEntry e = li.get(i);
//			ItemStack in = ItemCustomModOre.getItem(i, ExtractorStage.FLAKES);
//			OreType nat = e.nativeOre;
//			ItemStack out = ItemCustomModOre.getSmeltedItem(i);
//			out.setCount(e.numberSmelted);
//			if (nat instanceof ReikaOreHelper) {
//				out = ((ReikaOreHelper)nat).getDrop();
//				out.setCount(((ReikaOreHelper)nat).blockDrops);
//			}
//			else if (nat instanceof ModOreList) {
//				out = getSmeltedIngot((ModOreList)nat);
//				out.setCount(((ModOreList)nat).dropCount);
//			}
//			ReikaRecipeHelper.addSmelting(in, out, e.rarity == OreType.OreRarity.RARE ? 1 : e.rarity == OreType.OreRarity.EVERYWHERE ? 0.5F : 0.7F);
//		}
//	}
//
//	public static int getSpritesheet(ModOreList ore) {
//		return ore.ordinal()/64;
//	}
//
//	public static boolean isModOreIngredient(ItemStack is) {
//		if (is == null)
//			return false;
//		if (!RotaryItems.MODEXTRACTS.matchItem(is))
//			return false;
//		return ModOreList.getEntryFromDamage(is.getItemDamage()/4) != null;
//	}
//
//	public static ExtractorStage getStageFromMetadata(ItemStack is) {
//		return ExtractorStage.list[is.getItemDamage()%4];
//	}
//
//	public static int getIndexOffsetForIngot(ItemStack is) {
//		ModOreList ore = ModOreList.getEntryFromDamage(is.getItemDamage());
//		if (ore.isIngotType())
//			return 3;
//		if (ore.isDustType())
//			return 1;
//		if (ore.isGemType())
//			return 2;
//		return 0;
//	}
//
//	public static int getDustIndex(ModOreList ore) {
//		return ore.ordinal()*4;
//	}
//
//	public static int getSlurryIndex(ModOreList ore) {
//		return getDustIndex(ore)+1;
//	}
//
//	public static int getSolutionIndex(ModOreList ore) {
//		return getDustIndex(ore)+2;
//	}
//
//	public static int getFlakesIndex(ModOreList ore) {
//		return getDustIndex(ore)+3;
//	}
//
//	public static boolean isDust(ModOreList ore, int index) {
//		return index == getDustIndex(ore);
//	}
//
//	public static boolean isSlurry(ModOreList ore, int index) {
//		return index == getSlurryIndex(ore);
//	}
//
//	public static boolean isSolution(ModOreList ore, int index) {
//		return index == getSolutionIndex(ore);
//	}
//
//	public static boolean isFlakes(ModOreList ore, int index) {
//		return index == getFlakesIndex(ore);
//	}
//
//	public static ItemStack getDustProduct(ModOreList ore) {
//		return RotaryItems.MODEXTRACTS.getStackOfMetadata(getDustIndex(ore));
//	}
//
//	public static ItemStack getSlurryProduct(ModOreList ore) {
//		return RotaryItems.MODEXTRACTS.getStackOfMetadata(getSlurryIndex(ore));
//	}
//
//	public static ItemStack getSolutionProduct(ModOreList ore) {
//		return RotaryItems.MODEXTRACTS.getStackOfMetadata(getSolutionIndex(ore));
//	}
//
//	public static ItemStack getFlakeProduct(ModOreList ore) {
//		return RotaryItems.MODEXTRACTS.getStackOfMetadata(getFlakesIndex(ore));
//	}
//
//	public static boolean isOreFlake(ItemStack is) {
//		if (RotaryItems.EXTRACTS.matchItem(is)) {
//			return ReikaMathLibrary.isValueInsideBoundsIncl(RotaryItems.coaloreflakes.getItemDamage(), RotaryItems.tungstenflakes.getItemDamage(), is.getItemDamage());
//		}
//		else if (RotaryItems.MODEXTRACTS.matchItem(is)) {
//			return is.getItemDamage()%4 == 3;
//		}
//		else if (RotaryItems.CUSTOMEXTRACT.matchItem(is)) {
//			return is.getItemDamage()%4 == 3;
//		}
//		return false;
//	}
//
//	public static OreType getOreFromExtract(ItemStack is) {
//		if (is.getItem() instanceof AutoOreItem)
//			return ((AutoOreItem)is.getItem()).getOreType(is);
//		else if (RotaryItems.EXTRACTS.matchItem(is)) {
//			return ReikaOreHelper.oreList[is.getItemDamage()%ReikaOreHelper.oreList.length];
//		}
//		else if (RotaryItems.MODEXTRACTS.matchItem(is)) {
//			return ModOreList.oreList[(is.getItemDamage()/4)];
//		}
//		return null;
//	}
//
//	public static ItemStack getSmeltedIngot(ModOreList ore) {
//		return switch (ore) {
//			case NETHERCOAL -> new ItemStack(Items.COAL);
//			case NETHERCOPPER -> new ItemStack(Items.COPPER_INGOT);
//			case NETHERDIAMOND -> new ItemStack(Items.DIAMOND);
//			case NETHEREMERALD -> new ItemStack(Items.EMERALD);
//			case NETHERGOLD -> new ItemStack(Items.GOLD_INGOT);
//			case NETHERIRON -> new ItemStack(Items.IRON_INGOT);
//			case NETHERLAPIS -> Items.LAPIS_LAZULI.getDefaultInstance();
//			case NETHERLEAD -> RotaryItems.MODINGOTS.getStackOfMetadata(ModOreList.LEAD.ordinal());
//			case NETHERNICKEL -> RotaryItems.MODINGOTS.getStackOfMetadata(ModOreList.NICKEL.ordinal());
//			case NETHERNIKOLITE -> RotaryItems.MODINGOTS.getStackOfMetadata(ModOreList.NIKOLITE.ordinal());
//			case NETHERREDSTONE -> new ItemStack(Items.REDSTONE);
//			case NETHERSILVER -> RotaryItems.MODINGOTS.getStackOfMetadata(ModOreList.SILVER.ordinal());
//			case NETHERTIN -> RotaryItems.MODINGOTS.getStackOfMetadata(ModOreList.TIN.ordinal());
//			case NETHERPLATINUM -> RotaryItems.MODINGOTS.getStackOfMetadata(ModOreList.PLATINUM.ordinal());
//			case NETHERURANIUM -> RotaryItems.MODINGOTS.getStackOfMetadata(ModOreList.URANIUM.ordinal());
//			case NETHERIRIDIUM -> RotaryItems.MODINGOTS.getStackOfMetadata(ModOreList.IRIDIUM.ordinal());
//			case NETHERSULFUR -> RotaryItems.MODINGOTS.getStackOfMetadata(ModOreList.SULFUR.ordinal());
//			case NETHERTITANIUM -> RotaryItems.MODINGOTS.getStackOfMetadata(ModOreList.TITANIUM.ordinal());
//			case NETHEROSMIUM -> RotaryItems.MODINGOTS.getStackOfMetadata(ModOreList.OSMIUM.ordinal());
//			case NETHERSALTPETER -> RotaryItems.MODINGOTS.getStackOfMetadata(ModOreList.SALTPETER.ordinal());
//			default -> RotaryItems.MODINGOTS.getStackOfMetadata(ore.ordinal());
//		};
//	}
//
//	public enum ExtractorStage {
//		DUST(),
//		SLURRY(),
//		SOLUTION(),
//		FLAKES();
//
//		private static final ExtractorStage[] list = values();
//
//		public String getDisplayName(OreType ore) {
//			return switch (this) {
//				case DUST -> "Powdered " + ore.getDisplayName();
//				case SLURRY -> ore.getDisplayName() + " Slurry";
//				case SOLUTION -> ore.getDisplayName() + " Solution";
//				case FLAKES -> ore.getDisplayName() + " Flakes";
//			};
//		}
//	}
//}
