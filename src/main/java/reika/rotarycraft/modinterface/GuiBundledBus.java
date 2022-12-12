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
//import net.minecraft.entity.player.Player;
//import net.minecraft.inventory.Container;
//import net.minecraft.util.StatCollector;
//
//import net.minecraft.world.Container;
//import net.minecraft.world.entity.player.Player;
//import net.minecraftforge.api.distmarker.Dist;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//
//@SideOnly(Dist.CLIENT)
//public class GuiBundledBus extends GuiPowerOnlyMachine
//{
//	private Container upperBundledBusInventory;
//	private BlockEntityBundledBus med;
//
//	public GuiBundledBus(Player p5ep, BlockEntityBundledBus te)
//	{
//		super(new ContainerBundledBus(p5ep, te), te);
//		upperBundledBusInventory = p5ep.inventory;
//		med = te;
//		ep = p5ep;
//
//		xSize = 176;
//		ySize = 192;
//	}
//
//	@Override
//	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
//		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
//	}
//
//	@Override
//	public boolean labelInventory() {
//		return true;
//	}
//
//	@Override
//	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
//		super.drawGuiContainerForegroundLayer(par1, par2);
//
//		font.drawString(I18n.get("container.inventory"), xSize-58, (ySize - 97) + 4, 0xffffff);
//		int j = (width - xSize) / 2;
//		int k = (height - ySize) / 2;
//
//		for (int i = 0; i < med.NSLOTS; i++) {
//			int dx = 50+(i%4)*20;
//			int dy = 19+(i/4)*20;
//			api.drawItemStack(itemRender, font, med.getMapping(i), dx, dy);
//		}
//	}
//
//	@Override
//	public String getGuiTexture() {
//		return "bundledbusgui";
//	}
//}
