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
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.client.gui.inventory.GuiContainer;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.init.Blocks;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.util.IIcon;
//import net.neoforged.fluids.Fluid;
//import net.neoforged.fluids.FluidRegistry;
//
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.RotaryItems;
//import reika.rotarycraft.gui.machine.inventory.GuiFridge;
//
//import codechicken.nei.PositionedStack;
//import codechicken.nei.recipe.TemplateRecipeHandler;
//
//public class FridgeHandler extends TemplateRecipeHandler {
//
//	public class FridgeRecipe extends CachedRecipe {
//
//
//		public FridgeRecipe() {
//
//		}
//
//		@Override
//		public PositionedStack getResult() {
//			return new PositionedStack(RotaryItems.dryice, 127, 62);
//		}
//
//		@Override
//		public PositionedStack getIngredient()
//		{
//			return new PositionedStack(new ItemStack(Blocks.ice), 94, 48);
//		}
//	}
//
//	@Override
//	public String getRecipeName() {
//		return "Refrigeration Unit";
//	}
//
//	@Override
//	public String getGuiTexture() {
//		return "/Reika/RotaryCraft/Textures/GUI/fridgegui.png";
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
//	public void loadCraftingRecipes(ItemStack result) {
//		if (ReikaItemHelper.matchStacks(result, RotaryItems.dryice))
//			arecipes.add(new FridgeRecipe());
//		if (ReikaItemHelper.matchStacks(result, RotaryItems.nitrogenbucket))
//			arecipes.add(new FridgeRecipe());
//	}
//
//	@Override
//	public void loadUsageRecipes(ItemStack ingredient) {
//		if (ReikaItemHelper.matchStackWithBlock(ingredient, Blocks.ice))
//			arecipes.add(new FridgeRecipe());
//	}
//
//	@Override
//	public Class<? extends GuiContainer> getGuiClass()
//	{
//		return GuiFridge.class;
//	}
//
//	@Override
//	public void drawExtras(int recipe)
//	{
//		this.drawFluids(recipe);
//	}
//
//	private void drawFluids(int recipe) {
//		Fluid f = RotaryFluids.LIQUID_NITROGEN.get();
//		if (f != null) {
//			IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//			float u = ico.getMinU();
//			float v = ico.getMinV();
//			float du = ico.getMaxU();
//			float dv = ico.getMaxV();
//			ReikaTextureHelper.bindTerrainTexture();
//			Tessellator v5 = Tessellator.instance;
//			v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//			int x = 147;
//			for (int i = 0; i < 4; i++) {
//				int y = 7+i*16;
//				v5.addVertexWithUV(x, y, 0, u, v);
//				v5.addVertexWithUV(x, y+16, 0, u, dv);
//				v5.addVertexWithUV(x+16, y+16, 0, du, dv);
//				v5.addVertexWithUV(x+16, y, 0, du, v);
//			}
//			float v2 = v+(dv-v)*5/16F;
//			v5.addVertexWithUV(x, 64, 0, u, v2);
//			v5.addVertexWithUV(x, 69, 0, u, v);
//			v5.addVertexWithUV(x+16, 69, 0, du, v);
//			v5.addVertexWithUV(x+16, 64, 0, du, v2);
//			v5.draw();
//		}
//	}
//
//}
