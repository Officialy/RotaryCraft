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
//import java.util.ArrayList;
//
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.client.gui.inventory.GuiContainer;
//import net.minecraft.world.item.ItemStack;
//
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.gui.machine.inventory.GuiWorktable;
//import reika.rotarycraft.registry.ItemRegistry;
//
//import codechicken.nei.PositionedStack;
//import codechicken.nei.recipe.TemplateRecipeHandler;
//
//public class ToolChargingHandler extends TemplateRecipeHandler {
//
//	public class ChargingRecipe extends CachedRecipe {
//
//		private final ItemStack chargedTool;
//
//		public ChargingRecipe(ItemStack tool, int charge) {
//			chargedTool = new ItemStack(tool.getItem(), 1, charge);
//		}
//
//		@Override
//		public PositionedStack getResult() {
//			return new PositionedStack(chargedTool, 93, 6);
//		}
//
//		@Override
//		public ArrayList<PositionedStack> getIngredients()
//		{
//			ArrayList<PositionedStack> stacks = new ArrayList<PositionedStack>();
//			int dx = 21;
//			int dy = 6;
//			PositionedStack pos = new PositionedStack(new ItemStack(chargedTool.getItem(), 1, 0), dx, dy);
//			dx += 18;
//			PositionedStack pos2 = new PositionedStack(ItemRegistry.SPRING.getStackOfMetadata(chargedTool.getItemDamage()), dx, dy);
//			stacks.add(pos);
//			stacks.add(pos2);
//			return stacks;
//		}
//
//		@Override
//		public ArrayList<PositionedStack> getOtherStacks()
//		{
//			ArrayList<PositionedStack> stacks = new ArrayList<PositionedStack>();
//			PositionedStack stack = new PositionedStack(ItemRegistry.SPRING.get(), 111, 6);
//			stacks.add(stack);
//			return stacks;
//		}
//	}
//
//	@Override
//	public void loadCraftingRecipes(ItemStack result) {
//
//	}
//
//	@Override
//	public void loadUsageRecipes(ItemStack ingredient) {
//		if (ingredient != null && ingredient.getItem() == ItemRegistry.SPRING.get()) {
//			for (int i = 0; i < ItemRegistry.itemList.length; i++) {
//				ItemRegistry ir = ItemRegistry.itemList[i];
//				if (ir.isCharged() && !ir.isDummiedOut()) {
//					arecipes.add(new ChargingRecipe(ir.get(), ingredient.getItemDamage()));
//				}
//			}
//		}
//	}
//
//	@Override
//	public String getRecipeName() {
//		return "Tool Charging";
//	}
//
//	@Override
//	public Class<? extends GuiContainer> getGuiClass() {
//		return GuiWorktable.class;
//	}
//
//	@Override
//	public String getGuiTexture() {
//		return "/Reika/RotaryCraft/Textures/GUI/worktablegui.png";
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
//}
