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
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.material.Fluid;
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.inventory.GuiContainer;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.util.IIcon;
//import net.neoforged.fluids.Fluid;
//import net.neoforged.fluids.FluidRegistry;
//
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.api.Interfaces.Fillable;
//import reika.rotarycraft.api.interfaces.Fillable;
//import reika.rotarycraft.gui.machine.inventory.GuiFillingStation;
//
//import codechicken.nei.PositionedStack;
//import codechicken.nei.recipe.TemplateRecipeHandler;
//
//public class FillingStationHandler extends TemplateRecipeHandler {
//
//	public class FillingStationRecipe extends CachedRecipe {
//
//		private final Fluid fluid;
//		private final ItemStack item;
//
//		public FillingStationRecipe(ItemStack is, Fluid f) {
//			fluid = f;
//			item = is;
//		}
//
//		@Override
//		public PositionedStack getResult() {
//			return new PositionedStack(item, 101, 60);
//		}
//
//		@Override
//		public PositionedStack getIngredient()
//		{
//			return null;
//		}
//	}
//
//	@Override
//	public String getRecipeName() {
//		return "Filling Station";
//	}
//
//	@Override
//	public String getGuiTexture() {
//		return "/Reika/RotaryCraft/Textures/GUI/fillingstationgui.png";
//	}
//
//	@Override
//	public void drawBackground(int recipe)
//	{
//		GL11.glColor4f(1, 1, 1, 1);
//		ReikaTextureHelper.bindTexture(RotaryCraft.class, this.getGuiTexture());
//		GL11.glDisable(GL11.GL_DEPTH_TEST);
//		ReikaGuiAPI.instance.drawTexturedModalRectWithDepth(0, 0, 5, 11, 166, 70, ReikaGuiAPI.NEI_DEPTH);
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
//	public void loadCraftingRecipes(ItemStack result) {
//		if (result.getItem() instanceof Fillable) {
//			Fillable item = (Fillable)result.getItem();
//			Fluid f = item.getCurrentFluid(result);
//			if (f != null)
//				arecipes.add(new FillingStationRecipe(result, f));
//		}
//	}
//
//	@Override
//	public void loadUsageRecipes(ItemStack ingredient) {
//		if (ingredient.getItem() instanceof Fillable) {
//			Fillable item = (Fillable)ingredient.getItem();
//			Fluid f = item.getCurrentFluid(ingredient);
//			if (f == null) {
//				for (String name : FluidRegistry.getRegisteredFluids().keySet()) {
//					Fluid fluid = FluidRegistry.getFluid(name);
//					if (fluid != null && item.isValidFluid(fluid, ingredient)) {
//						ItemStack is = ingredient.copy();
//						arecipes.add(new FillingStationRecipe(is, fluid));
//					}
//				}
//			}
//		}
//	}
//
//	@Override
//	public int recipiesPerPage()
//	{
//		return 1;
//	}
//
//	@Override
//	public Class<? extends GuiContainer> getGuiClass()
//	{
//		return GuiFillingStation.class;
//	}
//
//	@Override
//	public void drawExtras(int recipe)
//	{
//		ReikaGuiAPI.instance.drawTexturedModalRect(134, 7, 177, 45, 6, 50);
//		this.drawFluids(recipe);
//	}
//
//	private void drawFluids(int recipe) {
//		FillingStationRecipe r = (FillingStationRecipe)arecipes.get(recipe);
//		Fluid f = r.fluid;
//		if (f != null) {
//			GL11.glColor4f(1, 1, 1, 1);
//			GL11.glDisable(GL11.GL_BLEND);
//			IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//			float u = ico.getMinU();
//			float v = ico.getMinV();
//			float du = ico.getMaxU();
//			float dv = ico.getMaxV();
//			ReikaTextureHelper.bindTerrainTexture();
//			Tessellator v5 = Tessellator.instance;
//			v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//			for (int i = 0; i < 4; i++) {
//				int y = 10+i*16;
//				v5.addVertexWithUV(77, y, 0, u, v);
//				v5.addVertexWithUV(77, y+16, 0, u, dv);
//				v5.addVertexWithUV(89, y+16, 0, du, dv);
//				v5.addVertexWithUV(89, y, 0, du, v);
//			}
//			v5.draw();
//			int ox = 130;
//			int oy = 53;
//			if (ReikaGuiAPI.instance.isMouseInBox(ox+76, ox+90, oy+9, oy+75)) {
//				int mx = ReikaGuiAPI.instance.getMouseRealX();
//				int my = ReikaGuiAPI.instance.getMouseRealY();
//				ReikaGuiAPI.instance.drawTooltipAt(Minecraft.getMinecraft().fontRenderer, f.getLocalizedName(), mx-ox, my-oy);
//			}
//			GL11.glEnable(GL11.GL_BLEND);
//		}
//	}
//
//}
