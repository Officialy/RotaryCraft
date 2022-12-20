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
//import java.util.ArrayList;
//
//import net.minecraft.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.World;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.oredict.OreDictionary;
//
//import reika.dragonapi.libraries.ReikaNBTHelper;
//import reika.dragonapi.libraries.ReikaNBTHelper;
//import reika.rotarycraft.registry.RotaryItems;
//
//import reika.rotarycraft.registry.RotaryItems;
//import thaumcraft.api.ThaumcraftApiHelper;
//import thaumcraft.api.aspects.AspectList;
//import thaumcraft.api.crafting.InfusionRecipe;
//
//
//public final class BedrockRevealingInfusion extends InfusionRecipe {
//
//	private ItemStack cachedCentralInput;
//
//	public BedrockRevealingInfusion(int inst, AspectList al, ItemStack[] recipe) {
//		super("BEDREVEAL", RotaryItems.BEDREVEAL.getEnchantedStack(), inst, al, RotaryItems.BEDHELM.getEnchantedStack(), recipe);
//		cachedCentralInput = this.getRecipeInput().copy();
//	}
//
//	@Override
//	public boolean matches(ArrayList<ItemStack> input, ItemStack main, Level world, Player player) {
//		cachedCentralInput = main.copy();
//
//		if (this.getRecipeInput() == null)
//			return false;
//
//		if (!research.isEmpty() && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), research)) {
//			return false;
//		}
//
//		ItemStack in = main.copy();
//		if (this.getRecipeInput().getItemDamage() == OreDictionary.WILDCARD_VALUE) {
//			in.setItemDamage(OreDictionary.WILDCARD_VALUE);
//		}
//
//		if (in.getItem() != this.getRecipeInput().getItem()) //instead of the original equality test, since that was NBT sensitive
//			return false;
//
//		ArrayList<ItemStack> li = new ArrayList<ItemStack>();
//		for (ItemStack is : input) {
//			li.add(is.copy());
//		}
//
//		for (ItemStack is : this.getComponents()) {
//			boolean flag = false;
//			for (int a = 0; a < li.size(); a++) {
//				in = li.get(a).copy();
//				if (is.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
//					in.setItemDamage(OreDictionary.WILDCARD_VALUE);
//				}
//				if (areItemStacksEqual(in, is, true)) {
//					li.remove(a);
//					flag = true;
//					break;
//				}
//			}
//			if (!flag)
//				return false;
//		}
//		return li.isEmpty() ? true : false;
//	}
//
//	@Override
//	public Object getRecipeOutput(ItemStack input) {
//		ItemStack is = RotaryItems.BEDREVEAL.getEnchantedStack();
//		ReikaNBTHelper.combineNBT(is.stackTagCompound, cachedCentralInput.stackTagCompound);
//		return is;
//	}
//
//}
