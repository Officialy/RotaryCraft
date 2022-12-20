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
//import java.util.List;
//
//import net.minecraft.world.item.ItemStack;
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.inventory.GuiContainer;
//import net.minecraft.init.Blocks;
//import net.minecraft.world.item.ItemStack;
//
//import reika.dragonapi.interfaces.Registry.OreType;
//import reika.dragonapi.interfaces.Registry.OreType.OreRarity;
//import reika.dragonapi.interfaces.registry.OreType;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.libraries.registry.ReikaOreHelper;
//import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
//import reika.dragonapi.modregistry.ModOreList;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.CustomExtractLoader;
//import reika.rotarycraft.auxiliary.CustomExtractLoader.CustomExtractEntry;
//import reika.rotarycraft.auxiliary.RotaryItems;
//import reika.rotarycraft.auxiliary.recipemanagers.ExtractorModOres;
//import reika.rotarycraft.auxiliary.recipemanagers.ExtractorModOres.ExtractorStage;
//import reika.rotarycraft.auxiliary.recipemanagers.RecipesExtractor;
//import reika.rotarycraft.gui.machine.inventory.GuiExtractor;
//import reika.rotarycraft.modinterface.ItemCustomModOre;
//import reika.rotarycraft.registry.ExtractorBonus;
//import reika.rotarycraft.registry.RotaryItems;
//import reika.rotarycraft.blockentities.processing.TileEntityExtractor;
//
//import codechicken.nei.PositionedStack;
//import codechicken.nei.recipe.TemplateRecipeHandler;
//
//public class ExtractorHandler extends TemplateRecipeHandler {
//
//	public class ExtractorRecipe extends CachedRecipe {
//
//		private final ArrayList<ItemStack> oreBlock;
//		public final OreType type;
//
//		public ExtractorRecipe(OreType ore) {
//			type = ore;
//			oreBlock = new ArrayList(ore.getAllOreBlocks());
//		}
//
//		@Override
//		public PositionedStack getResult() {
//			return new PositionedStack(this.getFlakes(), 129, 44);
//		}
//
//		private ItemStack getDust() {
//			if (type instanceof ReikaOreHelper) {
//				return RotaryItems.EXTRACTS.getStackOfMetadata(type.ordinal());
//			}
//			else if (type instanceof ModOreList) {
//				return ExtractorModOres.getDustProduct((ModOreList)type);
//			}
//			else if (type instanceof CustomExtractEntry) {
//				return ItemCustomModOre.getItem(type.ordinal(), ExtractorStage.DUST);
//			}
//			else {
//				return null;
//			}
//		}
//
//		private ItemStack getSlurry() {
//			if (type instanceof ReikaOreHelper) {
//				return RotaryItems.EXTRACTS.getStackOfMetadata(type.ordinal()+8);
//			}
//			else if (type instanceof ModOreList) {
//				return ExtractorModOres.getSlurryProduct((ModOreList)type);
//			}
//			else if (type instanceof CustomExtractEntry) {
//				return ItemCustomModOre.getItem(type.ordinal(), ExtractorStage.SLURRY);
//			}
//			else {
//				return null;
//			}
//		}
//
//		private ItemStack getSolution() {
//			if (type instanceof ReikaOreHelper) {
//				return RotaryItems.EXTRACTS.getStackOfMetadata(type.ordinal()+16);
//			}
//			else if (type instanceof ModOreList) {
//				return ExtractorModOres.getSolutionProduct((ModOreList)type);
//			}
//			else if (type instanceof CustomExtractEntry) {
//				return ItemCustomModOre.getItem(type.ordinal(), ExtractorStage.SOLUTION);
//			}
//			else {
//				return null;
//			}
//		}
//
//		private ItemStack getFlakes() {
//			if (type instanceof ReikaOreHelper) {
//				return RotaryItems.EXTRACTS.getStackOfMetadata(type.ordinal()+24);
//			}
//			else if (type instanceof ModOreList) {
//				return ExtractorModOres.getFlakeProduct((ModOreList)type);
//			}
//			else if (type instanceof CustomExtractEntry) {
//				return ItemCustomModOre.getItem(type.ordinal(), ExtractorStage.FLAKES);
//			}
//			else {
//				return null;
//			}
//		}
//
//		@Override
//		public ArrayList<PositionedStack> getIngredients()
//		{
//			ArrayList<PositionedStack> stacks = new ArrayList<PositionedStack>();
//			int dx = 21;
//			int dy = 2;
//			int vx = 36;
//			int vy = 42;
//
//			for (int i = 0; i < 2; i++) {
//				for (int j = 0; j < 4; j++) {
//					PositionedStack pos = new PositionedStack(this.getItem(j, i), dx+vx*j, dy+vy*i);
//					if (!(i == 1 && j == 3))
//						stacks.add(pos);
//				}
//			}
//
//			if (true) {
//				ExtractorBonus bonus = ExtractorBonus.getBonusForIngredient(this.getSolution());
//				if (bonus != null) {
//					stacks.add(new PositionedStack(bonus.getBonusItem(), 147, 44));
//				}
//			}
//			return stacks;
//		}
//
//		private int getTick() {
//			return (int)((System.currentTimeMillis()/1000000)%oreBlock.size());
//		}
//
//		private Object getItem(int x, int y) {
//			switch(x+y*4) {
//				case 0:
//					return oreBlock.get(this.getTick());
//				case 1:
//				case 4:
//					return this.getDust();
//				case 2:
//				case 5:
//					return this.getSlurry();
//				case 3:
//				case 6:
//					return this.getSolution();
//				default:
//					return new ItemStack(Blocks.FIRE);
//			}
//		}
//
//		private int getSizeAt(int x, int y) {
//			return 1;
//		}
//	}
//
//	@Override
//	public String getRecipeName() {
//		return "Extractor";
//	}
//
//	@Override
//	public String getGuiTexture() {
//		return "/Reika/RotaryCraft/Textures/GUI/extractorgui.png";
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
//	public void loadTransferRects() {
//		transferRects.add(new RecipeTransferRect(new Rectangle(20, 20, 126, 22), "rcextract"));
//	}
//
//	@Override
//	public int recipiesPerPage() {
//		return 1;
//	}
//
//	@Override
//	public void loadCraftingRecipes(String outputId, Object... results) {
//		if (outputId != null && outputId.equals("rcextract")) {
//			for (int i = 0; i < ReikaOreHelper.oreList.length; i++) {
//				ReikaOreHelper ore = ReikaOreHelper.oreList[i];
//				if (ore.existsInGame())
//					arecipes.add(new ExtractorRecipe(ore));
//			}
//			for (int i = 0; i < ModOreList.oreList.length; i++) {
//				ModOreList ore = ModOreList.oreList[i];
//				if (ore.existsInGame())
//					arecipes.add(new ExtractorRecipe(ore));
//			}
//			List<CustomExtractEntry> li = CustomExtractLoader.instance.getEntries();
//			for (CustomExtractEntry e : li) {
//				if (e.existsInGame())
//					arecipes.add(new ExtractorRecipe(e));
//			}
//		}
//		super.loadCraftingRecipes(outputId, results);
//	}
//
//	@Override
//	public void loadUsageRecipes(String inputId, Object... ingredients) {
//		if (inputId != null && inputId.equals("rcextract")) {
//			this.loadCraftingRecipes(inputId, ingredients);
//		}
//		super.loadUsageRecipes(inputId, ingredients);
//	}
//
//	@Override
//	public void loadCraftingRecipes(ItemStack result) {
//		if (RotaryItems.EXTRACTS.matchItem(result)) {
//			arecipes.add(new ExtractorRecipe(RecipesExtractor.getOreFromExtract(result)));
//		}
//		else if (RotaryItems.MODEXTRACTS.matchItem(result)) {
//			ModOreList mod = ModOreList.oreList[result.getItemDamage()/4];
//			if (mod.existsInGame())
//				arecipes.add(new ExtractorRecipe(mod));
//		}
//		else if (RotaryItems.CUSTOMEXTRACT.matchItem(result)) {
//			arecipes.add(new ExtractorRecipe(ItemCustomModOre.getExtractType(result)));
//		}
//		else if (ReikaItemHelper.matchStacks(result, RotaryItems.aluminumpowder)) {
//			arecipes.add(new ExtractorRecipe(ReikaOreHelper.REDSTONE));
//			arecipes.add(new ExtractorRecipe(ReikaOreHelper.LAPIS));
//		}
//	}
//
//	@Override
//	public void loadUsageRecipes(ItemStack ingredient) {
//		int dmg = ingredient.getItemDamage();
//		OreType type = ReikaOreHelper.getEntryByOreDict(ingredient);
//		if (type == null)
//			type = ModOreList.getModOreFromOre(ingredient);
//		if (type == null)
//			type = CustomExtractLoader.instance.getEntryFromOreBlock(ingredient);
//		if (type != null) {
//			arecipes.add(new ExtractorRecipe(type));
//		}
//		else if (RotaryItems.EXTRACTS.matchItem(ingredient) && dmg < ReikaOreHelper.oreList.length*3) {
//			arecipes.add(new ExtractorRecipe(RecipesExtractor.getOreFromExtract(ingredient)));
//		}
//		else if (RotaryItems.MODEXTRACTS.matchItem(ingredient) && dmg%4 != 3) {
//			ModOreList mod = ModOreList.oreList[dmg/4];
//			if (mod.existsInGame())
//				arecipes.add(new ExtractorRecipe(mod));
//		}
//		else if (RotaryItems.CUSTOMEXTRACT.matchItem(ingredient) && dmg%4 != 3) {
//			arecipes.add(new ExtractorRecipe(ItemCustomModOre.getExtractType(ingredient)));
//		}
//	}
//
//	@Override
//	public Class<? extends GuiContainer> getGuiClass()
//	{
//		return GuiExtractor.class;
//	}
//
//	@Override
//	public void drawExtras(int recipe) {
//		int chance = this.getDupeChance(recipe);
//		Minecraft.getMinecraft().fontRenderer.drawString(String.format("%d%s duplication chance per stage", chance, "%"), -2, 65, 0x333333, false);
//		Minecraft.getMinecraft().fontRenderer.drawString(String.format("(Average %.2f units per ore)", Math.pow(1+0.01*this.getDupeChance(recipe), 4)), 9, 76, 0x333333, false);
//
//		ItemStack is = ((ExtractorRecipe)arecipes.get(recipe)).getSolution();
//		ExtractorBonus bon = ExtractorBonus.getBonusForIngredient(is);
//		if (bon != null) {
//			ExtractorBonus bonus = ExtractorBonus.getBonusForIngredient(is);
//			if (bonus != null) {
//				String s = String.format("%.2f%s", bon.getBonusPercent(), "%");
//				ReikaGuiAPI.instance.drawCenteredStringNoShadow(Minecraft.getMinecraft().fontRenderer, s, 157, 34, 0);
//				//Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(s, 146, 34, 0xffffff);
//			}
//		}
//	}
//
//	public int getDupeChance(int recipe) {
//		ExtractorRecipe ir = (ExtractorRecipe)arecipes.get(recipe);
//		if (ir != null) {
//			OreRarity r = ir.type.getRarity();
//			if (r == OreRarity.RARE) {
//				return TileEntityExtractor.oreCopyRare;
//			}
//			else if (ir.type instanceof ModOreList && ((ModOreList)ir.type).isNetherOres()) {
//				return TileEntityExtractor.oreCopyNether;
//			}
//		}
//		return TileEntityExtractor.oreCopy;
//	}
//
//}
