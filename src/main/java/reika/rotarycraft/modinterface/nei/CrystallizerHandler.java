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
//import java.util.Collection;
//
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.Font;
//import net.minecraft.client.gui.inventory.GuiContainer;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.util.IIcon;
//import net.neoforged.fluids.Fluid;
//import net.neoforged.fluids.FluidContainerRegistry;
//import net.neoforged.fluids.FluidStack;
//
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.recipemanagers.RecipesCrystallizer;
//import reika.rotarycraft.auxiliary.recipemanagers.RecipesCrystallizer.CrystallizerRecipe;
//import reika.rotarycraft.gui.machine.inventory.GuiCrystallizer;
//import reika.rotarycraft.blockentities.processing.TileEntityCrystallizer;
//
//import codechicken.nei.PositionedStack;
//import codechicken.nei.recipe.TemplateRecipeHandler;
//
//public class CrystallizerHandler extends TemplateRecipeHandler {
//
//	public class CrystallizerNEIRecipe extends CachedRecipe {
//
//		private final CrystallizerRecipe input;
//
//		public CrystallizerNEIRecipe(CrystallizerRecipe in) {
//			input = in;
//		}
//
//		@Override
//		public PositionedStack getResult() {
//			if (input != null) {
//				return new PositionedStack(input.getOutput(), 75, 25);
//			}
//			return null;
//		}
//
//		@Override
//		public PositionedStack getIngredient()
//		{
//			return null;//new PositionedStack(this.getEntry(), 120, 5);
//		}
//	}
//
//	@Override
//	public String getRecipeName() {
//		return "Fluid Crystallizer";
//	}
//
//	@Override
//	public String getGuiTexture() {
//		return "/Reika/RotaryCraft/Textures/GUI/crystalgui.png";
//	}
//
//	@Override
//	public void drawBackground(int recipe)
//	{
//		GL11.glColor4f(1, 1, 1, 1);
//		ReikaTextureHelper.bindTexture(RotaryCraft.class, this.getGuiTexture());
//		GL11.glDisable(GL11.GL_DEPTH_TEST);
//		ReikaGuiAPI.instance.drawTexturedModalRectWithDepth(0, 1, 5, 11, 166, 70, ReikaGuiAPI.NEI_DEPTH);
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
//	public void loadTransferRects() {
//		transferRects.add(new RecipeTransferRect(new Rectangle(23, 23, 46, 18), "rccrystall"));
//	}
//
//	@Override
//	public void loadCraftingRecipes(String outputId, Object... results) {
//		if (outputId != null && outputId.equals("rccrystall")) {
//			Collection<CrystallizerRecipe> li = RecipesCrystallizer.getRecipes().getAllRecipes();
//			for (CrystallizerRecipe f : li)
//				arecipes.add(new CrystallizerNEIRecipe(f));
//		}
//		super.loadCraftingRecipes(outputId, results);
//	}
//
//	@Override
//	public void loadUsageRecipes(String inputId, Object... ingredients) {
//		if (inputId != null && inputId.equals("rccrystall")) {
//			this.loadCraftingRecipes(inputId, ingredients);
//		}
//		super.loadUsageRecipes(inputId, ingredients);
//	}
//
//	@Override
//	public void loadCraftingRecipes(ItemStack result) {
//		CrystallizerRecipe f = RecipesCrystallizer.getRecipes().getRecipe(result);
//		if (f != null) {
//			arecipes.add(new CrystallizerNEIRecipe(f));
//		}
//	}
//
//	@Override
//	public void loadUsageRecipes(ItemStack ingredient) {
//		FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(ingredient);
//		if (fs != null) {
//			ItemStack is = RecipesCrystallizer.getRecipes().getFreezingResult(fs);
//			if (is != null) {
//				arecipes.add(new CrystallizerNEIRecipe(RecipesCrystallizer.getRecipes().getRecipe(is)));
//			}
//		}
//	}
//
//	@Override
//	public Class<? extends GuiContainer> getGuiClass()
//	{
//		return GuiCrystallizer.class;
//	}
//
//	@Override
//	public void drawExtras(int recipe)
//	{
//		this.drawFluids(recipe);
//		this.drawTemperatures(recipe);
//	}
//
//	private void drawTemperatures(int recipe) {
//		CrystallizerNEIRecipe r = (CrystallizerNEIRecipe)arecipes.get(recipe);
//		FluidStack fs = r.input.getFluid();
//		if (fs != null) {
//			int freeze = TileEntityCrystallizer.getFreezingPoint(fs);
//			Font f = Minecraft.getMinecraft().fontRenderer;
//			String s = String.format("%dC", freeze);
//			ReikaGuiAPI.instance.drawCenteredStringNoShadow(f, s, 45, 20, 0);
//		}
//	}
//
//	private void drawFluids(int recipe) {
//		CrystallizerNEIRecipe r = (CrystallizerNEIRecipe)arecipes.get(recipe);
//		FluidStack fs = r.input.getFluid();
//		if (fs != null) {
//			Fluid f = fs.getFluid();
//			IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//			float u = ico.getMinU();
//			float v = ico.getMinV();
//			float du = ico.getMaxU();
//			float dv = ico.getMaxV();
//			ReikaTextureHelper.bindTerrainTexture();
//			Tessellator v5 = Tessellator.instance;
//			v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//			for (int i = 0; i < 4; i++) {
//				int y = 2+i*16;
//				v5.addVertexWithUV(3, y, 0, u, v);
//				v5.addVertexWithUV(3, y+16, 0, u, dv);
//				v5.addVertexWithUV(19, y+16, 0, du, dv);
//				v5.addVertexWithUV(19, y, 0, du, v);
//			}
//			float v2 = v+(dv-v)*5/16F;
//			v5.addVertexWithUV(3, 64, 0, u, v2);
//			v5.addVertexWithUV(3, 69, 0, u, v);
//			v5.addVertexWithUV(19, 69, 0, du, v);
//			v5.addVertexWithUV(19, 64, 0, du, v2);
//			v5.draw();
//
//			//if (ReikaGuiAPI.instance.isMouseInBox(3, 19, 2, 69)) {
//			//	ReikaGuiAPI.instance.drawTooltip(Minecraft.getMinecraft().fontRenderer, f.getLocalizedName());
//			//}
//			Font fr = Minecraft.getMinecraft().fontRenderer;
//			String s = f.getLocalizedName()+" ("+fs.amount+" mB)";
//			fr.drawString(s, 22, 56, 0);
//		}
//	}
//
//}
