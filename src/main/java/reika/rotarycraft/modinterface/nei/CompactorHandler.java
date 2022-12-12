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
//import java.util.List;
//
//import net.minecraft.world.item.ItemStack;
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.client.Minecraft;
//
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.recipemanagers.RecipesCompactor;
//import reika.rotarycraft.gui.machine.inventory.GuiCompactor;
//
//import codechicken.nei.PositionedStack;
//import codechicken.nei.recipe.TemplateRecipeHandler;
//
//public class CompactorHandler extends TemplateRecipeHandler {
//
//	public class CompactorRecipe extends CachedRecipe {
//
//		private ItemStack input;
//		private ItemStack output;
//
//		private CompactorRecipe(ItemStack in) {
//			input = in;
//			output = RecipesCompactor.getRecipes().getCompactingResult(in);
//		}
//
//		@Override
//		public PositionedStack getResult() {
//			return new PositionedStack(output, 75, 35);
//		}
//
//		@Override
//		public PositionedStack getIngredient()
//		{
//			return new PositionedStack(ReikaItemHelper.getSizedItemStack(input, 1), 71, 24);
//		}
//
//		@Override
//		public List<PositionedStack> getIngredients()
//		{
//			ArrayList<PositionedStack> stacks = new ArrayList<PositionedStack>();
//			PositionedStack stack = this.getIngredient();
//			if(stack != null) {
//				for (int i = 0; i < 4; i++)
//					stacks.add(new PositionedStack(stack.item, 21, 8+18*i));
//			}
//			return stacks;
//		}
//	}
//
//	@Override
//	public String getRecipeName() {
//		return "Compactor";
//	}
//
//	@Override
//	public String getGuiTexture() {
//		return "/Reika/RotaryCraft/Textures/GUI/compactorgui2.png";
//	}
//
//	@Override
//	public void drawBackground(int recipe)
//	{
//		GL11.glColor4f(1, 1, 1, 1);
//		ReikaTextureHelper.bindTexture(RotaryCraft.class, this.getGuiTexture());
//		int dy = 4;
//		GL11.glDisable(GL11.GL_DEPTH_TEST);
//		ReikaGuiAPI.instance.drawTexturedModalRectWithDepth(0, dy, 5, dy, 166, 75, ReikaGuiAPI.NEI_DEPTH);
//	}
//
//	@Override
//	public void drawForeground(int recipe)
//	{
//		GL11.glColor4f(1, 1, 1, 1);
//		GL11.glDisable(GL11.GL_LIGHTING);
//		ReikaTextureHelper.bindTexture(RotaryCraft.class, this.getGuiTexture());
//		this.drawExtras(recipe);
//	}
//
//	@Override
//	public int recipiesPerPage() {
//		return 1;
//	}
//
//	@Override
//	public void loadTransferRects() {
//		transferRects.add(new RecipeTransferRect(new Rectangle(40, 13, 33, 60), "rccompactor"));
//	}
//
//	@Override
//	public void loadCraftingRecipes(String outputId, Object... results) {
//		if (outputId != null && outputId.equals("rccompactor")) {
//			Collection<ItemStack> li = RecipesCompactor.getRecipes().getAllCompactables();
//			for (ItemStack is : li)
//				arecipes.add(new CompactorRecipe(is));
//		}
//		super.loadCraftingRecipes(outputId, results);
//	}
//
//	@Override
//	public void loadUsageRecipes(String inputId, Object... ingredients) {
//		if (inputId != null && inputId.equals("rccompactor")) {
//			this.loadCraftingRecipes(inputId, ingredients);
//		}
//		super.loadUsageRecipes(inputId, ingredients);
//	}
//
//	@Override
//	public void loadCraftingRecipes(ItemStack result) {
//		ItemStack is = RecipesCompactor.getRecipes().getSource(result);
//		if (is != null)
//			arecipes.add(new CompactorRecipe(is));
//	}
//
//	@Override
//	public void loadUsageRecipes(ItemStack ingredient) {
//		if (RecipesCompactor.getRecipes().isCompactable(ingredient)) {
//			arecipes.add(new CompactorRecipe(ingredient));
//		}
//	}
//
//	@Override
//	public Class<? extends GuiContainer> getGuiClass()
//	{
//		return GuiCompactor.class;
//	}
//
//	@Override
//	public void drawExtras(int recipe)
//	{
//		ReikaGuiAPI.instance.drawTexturedModalRect(142, 26, 176, 37, 4, 44);
//		ReikaGuiAPI.instance.drawTexturedModalRect(112, 25, 181, 41, 11, 46);
//
//		int pressure = this.getPressure(recipe);
//		int temperature = this.getTemperature(recipe);
//		Minecraft.getMinecraft().fontRenderer.drawString(String.format("Required Pressure: %d kPa", pressure), 0, 85, 0x333333, false);
//		Minecraft.getMinecraft().fontRenderer.drawString(String.format("Required Temperature: %dC", temperature), 0, 95, 0x333333, false);
//	}
//
//	private int getPressure(int recipe) {
//		CompactorRecipe comp = (CompactorRecipe)arecipes.get(recipe);
//		return RecipesCompactor.getRecipes().getReqPressure(comp.input);
//	}
//
//	private int getTemperature(int recipe) {
//		CompactorRecipe comp = (CompactorRecipe)arecipes.get(recipe);
//		return RecipesCompactor.getRecipes().getReqTemperature(comp.input);
//	}
//
//}
