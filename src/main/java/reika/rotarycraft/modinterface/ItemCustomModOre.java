///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface;
//
//import java.util.List;
//
//import net.minecraft.creativetab.CreativeTabs;
//import net.minecraft.item.Item;
//import net.minecraft.world.item.CreativeModeTab;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//
//import reika.dragonapi.interfaces.Item.GradientBlend;
//import reika.dragonapi.interfaces.item.GradientBlend;
//import reika.dragonapi.libraries.Java.ReikaStringParser;
//import reika.dragonapi.libraries.java.ReikaStringParser;
//import reika.rotarycraft.auxiliary.CustomExtractLoader;
//import reika.rotarycraft.auxiliary.CustomExtractLoader.CustomExtractEntry;
//import reika.rotarycraft.auxiliary.recipemanagers.ExtractorModOres;
//import reika.rotarycraft.auxiliary.recipemanagers.ExtractorModOres.ExtractorStage;
//import reika.rotarycraft.base.AutoOreItem;
//import reika.rotarycraft.registry.ItemRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//public class ItemCustomModOre extends AutoOreItem implements GradientBlend {
//
//	public ItemCustomModOre(int idx) {
//		super(idx);
//	}
//
//	@Override
//	public void getSubItems(Item ID, CreativeModeTab tab, List list)
//	{
//		boolean extract = this == RotaryItems.CUSTOMEXTRACT.get();
//		int i = 0;
//		for (CustomExtractEntry ore : CustomExtractLoader.instance.getEntries()) {
//			if (extract) {
//				list.add(new ItemStack(this, 1, i));
//				list.add(new ItemStack(this, 1, i+1));
//				list.add(new ItemStack(this, 1, i+2));
//				list.add(new ItemStack(this, 1, i+3));
//				i += 4;
//			}
//			else {
//				list.add(new ItemStack(this, 1, i));
//				i++;
//			}
//		}
//	}
//
//	@Override
//	public int getItemSpriteIndex(ItemStack item) {
//		if (CustomExtractLoader.instance.getEntries().isEmpty())
//			return 0;
//		int base = this.getRootIndex();
//		return this == RotaryItems.CUSTOMEXTRACT.get() ? base+item.getItemDamage()%4 : base+this.getOreType(item).type.ordinal();
//	}
//
//	@Override
//	public int getColorOne(ItemStack is) {
//		if (CustomExtractLoader.instance.getEntries().isEmpty())
//			return 0xffffff;
//		return this.getOreType(is).color2;
//	}
//
//	@Override
//	public int getColorTwo(ItemStack is) {
//		if (CustomExtractLoader.instance.getEntries().isEmpty())
//			return 0xffffff;
//		return this.getOreType(is).color1;
//	}
//
//	@Override
//	public int getColorThree(ItemStack is) {
//		if (CustomExtractLoader.instance.getEntries().isEmpty())
//			return 0xffffff;
//		return this.getOreType(is).color1;
//	}
//
//	@Override
//	public int getColorFour(ItemStack is) {
//		if (CustomExtractLoader.instance.getEntries().isEmpty())
//			return 0xffffff;
//		return this.getOreType(is).color1;
//	}
//
//	@Override
//	public String getItemStackDisplayName(ItemStack is) {
//		CustomExtractEntry ore = this.getOreType(is);
//		if (ore == null)
//			return "Null Ore Item";
//		if (RotaryItems.CUSTOMEXTRACT.matchItem(is)) {
//			ExtractorStage s = ExtractorModOres.getStageFromMetadata(is);
//			return s != null ? ore.displayName+" "+ ReikaStringParser.capFirstChar(s.name()) : "null";
//		}
//		else {
//			return ore.displayName+" "+ore.type.displayName;
//		}
//	}
//
//	@Override
//	public CustomExtractEntry getOreType(ItemStack item) {
//		return this.getExtractType(item);
//	}
//
//	public static CustomExtractEntry getExtractType(ItemStack is) {
//		if (CustomExtractLoader.instance.getEntries().isEmpty())
//			return null;
//		int idx = getEntryIndex(is);
//		return CustomExtractLoader.instance.getEntries().get(idx);
//	}
//
//	public static int getEntryIndex(ItemStack is) {
//		return is.getItem() == RotaryItems.CUSTOMEXTRACT.get() ? is.getItemDamage()/4 : is.getItemDamage();
//	}
//
//	public static ItemStack getItem(int idx, ExtractorStage s) {
//		return RotaryItems.CUSTOMEXTRACT.getStackOfMetadata(idx*4+s.ordinal());
//	}
//
//	public static ItemStack getSmeltedItem(int idx) {
//		return RotaryItems.CUSTOMINGOT.getStackOfMetadata(idx);
//	}
//}
