///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface.nei;
//
//import java.awt.Rectangle;
//import java.util.ArrayList;
//import java.util.Collection;
//
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.client.gui.inventory.GuiContainer;
//import net.minecraft.init.Blocks;
//import net.minecraft.init.Items;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.fluids.FluidRegistry;
//
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.RotaryItems;
//import reika.rotarycraft.auxiliary.recipemanagers.MulchMaterials;
//import reika.rotarycraft.gui.machine.inventory.GuiFermenter;
//import reika.rotarycraft.registry.ConfigRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//import codechicken.nei.PositionedStack;
//import codechicken.nei.recipe.TemplateRecipeHandler;
//
//public class FermenterHandler extends TemplateRecipeHandler {
//
//	public class FermenterRecipe extends CachedRecipe {
//
//		private ItemStack output;
//		private ItemStack input;
//
//		private FermenterRecipe(ItemStack in, ItemStack out) {
//			output = out;
//			input = ReikaItemHelper.getSizedItemStack(in, 1);
//		}
//
//		@Override
//		public PositionedStack getResult() {
//			int amt = input != null ? MulchMaterials.instance.getPlantValue(input) : 1;
//			ItemStack is = output.getItem() == RotaryItems.YEAST.get() ? output : ReikaItemHelper.getSizedItemStack(output, amt);
//			return new PositionedStack(is, 111, 36);
//		}
//
//		@Override
//		public ArrayList<PositionedStack> getIngredients()
//		{
//			ArrayList<PositionedStack> stacks = new ArrayList<PositionedStack>();
//			stacks.add(new PositionedStack(this.getTopSlot(), 50, 18));
//			stacks.add(new PositionedStack(this.getBottomSlot(), 50, 54));
//			return stacks;
//		}
//
//		private ItemStack getTopSlot() {
//			return output.getItem() == RotaryItems.YEAST.get() ? new ItemStack(Items.sugar) : RotaryItems.YEAST.get();
//		}
//
//		private ItemStack getBottomSlot() {
//			return output.getItem() == RotaryItems.YEAST.get() ? new ItemStack(Blocks.dirt) : input;
//		}
//	}
//
//	@Override
//	public String getRecipeName() {
//		return "Fermenter";
//	}
//
//	@Override
//	public String getGuiTexture() {
//		return "/Reika/RotaryCraft/Textures/GUI/fermentergui.png";
//	}
//
//	@Override
//	public void drawBackground(int recipe)
//	{
//		GL11.glColor4f(1, 1, 1, 1);
//		ReikaTextureHelper.bindTexture(RotaryCraft.class, this.getGuiTexture());
//		GL11.glDisable(GL11.GL_DEPTH_TEST);
//		ReikaGuiAPI.instance.drawTexturedModalRectWithDepth(0, 6, 5, 5, 166, 76, ReikaGuiAPI.NEI_DEPTH);
//	}
//
//	@Override
//	public void drawForeground(int recipe)
//	{
//		GL11.glColor4f(1, 1, 1, 1);
//		GL11.glDisable(GL11.GL_LIGHTING);
//		ReikaTextureHelper.bindTexture(RotaryCraft.class, this.getGuiTexture());
//		this.drawExtras(recipe);
//		ReikaLiquidRenderer.bindFluidTexture(FluidRegistry.WATER);
//		//ReikaGuiAPI.instance.drawTexturedModalRect(0, 0, (int)(ico.getMinU()*16), (int)(ico.getMinV()*16), 16, 16);
//	}
//
//	@Override
//	public void loadTransferRects() {
//		transferRects.add(new RecipeTransferRect(new Rectangle(74, 35, 23, 17), "rcferment"));
//	}
//
//	@Override
//	public void loadCraftingRecipes(String outputId, Object... results) {
//		if (outputId != null && outputId.equals("rcferment")) {
//			if (ConfigRegistry.enableFermenterYeast())
//				arecipes.add(new FermenterRecipe(null, RotaryItems.YEAST.get()));
//			Collection<ItemStack> li = MulchMaterials.instance.getAllValidPlants();
//			for (ItemStack is : li)
//				arecipes.add(new FermenterRecipe(is, ReikaItemHelper.getSizedItemStack(RotaryItems.sludge, MulchMaterials.instance.getPlantValue(is))));
//		}
//		super.loadCraftingRecipes(outputId, results);
//	}
//
//	@Override
//	public void loadUsageRecipes(String inputId, Object... ingredients) {
//		if (inputId != null && inputId.equals("rcferment")) {
//			this.loadCraftingRecipes(inputId, ingredients);
//		}
//		super.loadUsageRecipes(inputId, ingredients);
//	}
//
//	@Override
//	public void loadCraftingRecipes(ItemStack result) {
//		if (ReikaItemHelper.matchStacks(result, RotaryItems.sludge) || ReikaItemHelper.matchStacks(result, RotaryItems.ethanolbucket)) {
//			Collection<ItemStack> li = MulchMaterials.instance.getAllValidPlants();
//			for (ItemStack is : li) {
//				arecipes.add(new FermenterRecipe(is, RotaryItems.sludge.copy()));
//			}
//		}
//		else if (result.getItem() == RotaryItems.YEAST.get()) {
//			if (ConfigRegistry.enableFermenterYeast())
//				arecipes.add(new FermenterRecipe(null, result));
//		}
//	}
//
//	public boolean isEthanolIngredient(ItemStack is) {
//		if (is.getItem() == RotaryItems.YEAST.get())
//			return true;
//		if (is.getItem() == Items.water_bucket)
//			return true;
//		return MulchMaterials.instance.getPlantValue(is) > 0;
//	}
//
//	public boolean isYeastIngredient(ItemStack is) {
//		if (ReikaItemHelper.matchStackWithBlock(is, Blocks.dirt))
//			return true;
//		if (is.getItem() == Items.sugar)
//			return true;
//		if (is.getItem() == Items.water_bucket)
//			return true;
//		return false;
//	}
//
//	@Override
//	public void loadUsageRecipes(ItemStack ingredient) {
//		if (RotaryItems.YEAST.matchItem(ingredient)) {
//			Collection<ItemStack> li = MulchMaterials.instance.getAllValidPlants();
//			for (ItemStack is : li) {
//				arecipes.add(new FermenterRecipe(is, RotaryItems.sludge.copy()));
//			}
//		}
//		else if (this.isEthanolIngredient(ingredient) || this.isYeastIngredient(ingredient)) {
//			if (this.isYeastIngredient(ingredient)) {
//				if (ConfigRegistry.enableFermenterYeast())
//					arecipes.add(new FermenterRecipe(ingredient, RotaryItems.YEAST.get()));
//			}
//			else
//				arecipes.add(new FermenterRecipe(ingredient, RotaryItems.sludge.copy()));
//		}
//	}
//
//	@Override
//	public Class<? extends GuiContainer> getGuiClass()
//	{
//		return GuiFermenter.class;
//	}
//
//	@Override
//	public void drawExtras(int recipe)
//	{
//		int l = 27;
//		ReikaGuiAPI.instance.drawTexturedModalRect(18, 16+l, 176, 31+l, 11, 56-l);
//	}
//
//}
