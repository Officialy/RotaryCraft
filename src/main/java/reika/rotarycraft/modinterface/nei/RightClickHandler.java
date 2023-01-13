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
//import net.minecraft.block.Block;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.RenderBlocks;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
//import net.minecraft.init.Blocks;
//import net.minecraft.init.Items;
//import net.minecraft.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import reika.dragonapi.ModList;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
//import reika.rotarycraft.ClientProxy;
//import reika.rotarycraft.ItemMachineRenderer;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.RotaryItems;
//import reika.rotarycraft.registry.EngineType;
//import reika.rotarycraft.registry.GearboxTypes;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.MaterialRegistry;
//
//import codechicken.nei.PositionedStack;
//import codechicken.nei.recipe.TemplateRecipeHandler;
//import cpw.mods.fml.common.registry.GameRegistry;
//
//public class RightClickHandler extends TemplateRecipeHandler {
//
//	private static final RenderBlocks rb = new RenderBlocks();
//
//	public class RightClickRecipe extends CachedRecipe {
//
//		private final ItemStack item;
//		private final ItemStack machineItem;
//		public final MachineRegistry machine;
//		public final String description;
//
//		public RightClickRecipe(MachineRegistry machine, ItemStack machineitem, ItemStack is, String desc) {
//			this.machine = machine;
//			machineItem = machineitem;
//			item = is;
//			description = desc;
//		}
//
//		@Override
//		public PositionedStack getResult() {
//			return new PositionedStack(item, 112, 32);
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
//		return "Right-Click";
//	}
//
//	@Override
//	public String getGuiTexture() {
//		return "/Reika/RotaryCraft/Textures/GUI/rightclickgui.png";
//	}
//
//	@Override
//	public void drawBackground(int recipe)
//	{
//		GL11.glColor4f(1, 1, 1, 1);
//		//ReikaTextureHelper.bindTexture(RotaryCraft.class, this.getGuiTexture());
//		//ReikaGuiAPI.instance.drawTexturedModalRectWithDepth(0, 0, 5, 11, 166, 70, -300);
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
//		for (int i = 0; i < Actions.values().length; i++) {
//			Actions a = Actions.values()[i];
//			if (!a.entry.isDummiedOut() && ReikaItemHelper.matchStacks(result, a.getMachine())) {
//				arecipes.add(new RightClickRecipe(a.entry, a.getMachine(), a.getItem(), a.desc));
//			}
//		}
//	}
//
//	@Override
//	public void loadUsageRecipes(ItemStack ingredient) {
//		for (int i = 0; i < Actions.values().length; i++) {
//			Actions a = Actions.values()[i];
//			if (!a.entry.isDummiedOut() && ReikaItemHelper.matchStacks(ingredient, a.getItem())) {
//				arecipes.add(new RightClickRecipe(a.entry, a.getMachine(), a.getItem(), a.desc));
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
//	public void drawExtras(int recipe)
//	{
//		ReikaGuiAPI.instance.drawTexturedModalRect(134, 7, 177, 45, 6, 50);
//		this.renderMachine((RightClickRecipe)arecipes.get(recipe));
//		this.drawText((RightClickRecipe)arecipes.get(recipe));
//	}
//
//	private void drawText(RightClickRecipe rec) {
//		ReikaTextureHelper.bindFontTexture();
//		ReikaGuiAPI.instance.drawCenteredStringNoShadow(Minecraft.getMinecraft().fontRenderer, rec.description, 120, 160, 0);
//	}
//
//	private void renderMachine(RightClickRecipe rec) {
//		MachineRegistry m = rec.machine;
//		double sc = 32;
//		double x = 48;
//		double y = 32;
//		double z = 25;
//		int variable = rec.machineItem.getItemDamage();
//		float r = (System.currentTimeMillis()/20)%360;
//		float renderq = 22.5F;
//		RenderHelper.enableGUIStandardItemLighting();
//		GL11.glEnable(GL11.GL_DEPTH_TEST);
//		ItemMachineRenderer ir = ClientProxy.machineItems;
//		BlockEntity te = ir.getRenderingInstance(m, variable);
//		if (m.hasModel() && !m.isPipe()) {
//			double dx = x;
//			double dy = y+21;
//			double dz = z;
//			stack.translate(dx, dy, dz);
//			GL11.glScaled(sc, -sc, sc);
//			stack.mulPose(renderq, 1, 0, 0);
//			stack.mulPose(r, 0, 1, 0);
//			TileEntityRendererDispatcher.instance.renderTileEntityAt(te, -0.5, 0, -0.5, variable);
//			stack.mulPose(-r, 0, 1, 0);
//			stack.mulPose(-renderq, 1, 0, 0);
//			stack.translate(-dx, -dy, -dz);
//			GL11.glScaled(1D/sc, -1D/sc, 1D/sc);
//		}
//		else {
//			double dx = x;
//			double dy = y;
//			double dz = z;
//			stack.translate(dx, dy, dz);
//			GL11.glScaled(sc, -sc, sc);
//			stack.mulPose(renderq, 1, 0, 0);
//			stack.mulPose(r, 0, 1, 0);
//			ReikaTextureHelper.bindTerrainTexture();
//			rb.renderBlockAsItem(m.getBlock(), m.getBlockMetadata(), 1);
//			stack.mulPose(-r, 0, 1, 0);
//			stack.mulPose(-renderq, 1, 0, 0);
//			GL11.glScaled(1D/sc, -1D/sc, 1D/sc);
//			stack.translate(-dx, -dy, -dz);
//		}
//		GL11.glDisable(GL11.GL_DEPTH_TEST);
//	}
//
//	private static enum Actions {
//		RESERVOIR(MachineRegistry.RESERVOIR, Blocks.glass_pane, "Covers Reservoir"),
//		ADDCOIL(MachineRegistry.ELECTRICMOTOR, RotaryItems.GOLD_COIL, "Adds Coils"),
//		DISPLAYCOLOR(MachineRegistry.DISPLAY, Items.dye, "Changes Color"),
//		DISPLAYGLOW(MachineRegistry.DISPLAY, Items.glowstone_dust, "Makes Display Glow"),
//		DISPLAYSET(MachineRegistry.DISPLAY, Items.written_book, "Sets Text"),
//		REPAIRMIRROR(MachineRegistry.MIRROR, RotaryItems.mirror, "Repairs Mirror"),
//		REPAIRSHAFT(MachineRegistry.SHAFT, MaterialRegistry.STEEL.getShaftItem(), RotaryItems.shaftitem, "Repairs Shaft"),
//		REPAIRGEAR(MachineRegistry.GEARBOX, GearboxTypes.STEEL.getGearboxItem(4), RotaryItems.gearunit, "Repairs Gearbox"),
//		REPAIRJET(MachineRegistry.ENGINE, EngineType.JET.getCraftedProduct(), RotaryItems.compoundturb, "Repairs Engine"),
//		PNEU1(MachineRegistry.PNEUENGINE, RotaryItems.impeller, "Upgrades To Tier 1"),
//		PNEU2(MachineRegistry.PNEUENGINE, RotaryItems.turbine, "Upgrades To Tier 2"),
//		PNEU3(MachineRegistry.PNEUENGINE, RotaryItems.compoundturb, "Upgrades To Tier 3"),
//		MAGNETO1(MachineRegistry.MAGNETIC, GameRegistry.findItemStack(ModList.THERMALEXPANSION.modid, "powerCoilGold", 1), "Upgrades To Tier 1"),
//		MAGNETO2(MachineRegistry.MAGNETIC, GameRegistry.findItemStack(ModList.THERMALEXPANSION.modid, "conduitEnergyReinforced", 1), "Upgrades To Tier 2"),
//		MAGNETO3(MachineRegistry.MAGNETIC, RotaryItems.generator, "Upgrades To Tier 3"),
//		MAGNETO4(MachineRegistry.MAGNETIC, GameRegistry.findItemStack(ModList.THERMALEXPANSION.modid, "cellReinforced", 1), "Upgrades To Tier 4"),
//		MAGNETO5(MachineRegistry.MAGNETIC, GameRegistry.findItemStack(ModList.THERMALEXPANSION.modid, "cellResonant", 1), "Upgrades To Tier 5");
//
//		private final ItemStack item;
//		private final ItemStack machine;
//		public final MachineRegistry entry;
//		public final String desc;
//
//		private Actions(MachineRegistry m, ItemStack mis, ItemStack is, String d) {
//			machine = mis;
//			item = is;
//			entry = m;
//			desc = d;
//		}
//
//		private Actions(MachineRegistry m, ItemStack is, String d) {
//			this(m, m.getCraftedProduct(), is, d);
//		}
//
//		private Actions(MachineRegistry m, Item i, String d) {
//			this(m, new ItemStack(i), d);
//		}
//
//		private Actions(MachineRegistry m, Block b, String d) {
//			this(m, new ItemStack(b), d);
//		}
//
//		public ItemStack getMachine() {
//			return machine.copy();
//		}
//
//		public ItemStack getItem() {
//			return item.copy();
//		}
//	}
//
//}
