///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface.conversion;
//
//import net.minecraft.client.gui.components.Button;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import org.lwjgl.opengl.GL11;
//
//import reika.dragonapi.instantiable.io.PacketTarget;
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.dragonapi.libraries.mathsci.ReikaEngLibrary;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.rendering.ReikaColorAPI;
//import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.NonPoweredMachineScreen;
//import reika.rotarycraft.base.blockentity.EnergyToPowerBase;
//import reika.rotarycraft.registry.PacketRegistry;
//
//public class GuiEnergyToPower extends NonPoweredMachineScreen {
//
//	private EnergyToPowerBase engine;
//	private static final int SHIFT = -12;
//	private boolean flexible = false;
//
//	public GuiEnergyToPower(Player pl, EnergyToPowerBase te) {
//		super(new ContainerEnergyToPower(pl, te), te);
//		engine = te;
//		getYSize() = 99;
//		getXSize() = 207;
//		ep = pl;
//	}
//
//	@Override
//	public void initGui() {
//		super.initGui();
//		int j = (width - getXSize()) / 2;
//		int k = (height - getYSize()) / 2;
//		int dx = 4;
//		int inset = 21+dx;
//		if (flexible) {
//			renderables.add(new Button(0, SHIFT+j+inset-1+dx, k+getYSize()-30-48+0, 20, 20, "-"));;
//			renderables.add(new Button(1, SHIFT+j+getXSize()-20-inset-dx, k+getYSize()-30-48+0, 20, 20, "+"));
//		}
//		renderables.add(new Button(2, SHIFT+j+inset-10+dx, k+getYSize()-30-48+25, 20, 20, "-"));;
//		renderables.add(new Button(3, SHIFT+j+getXSize()-20-inset-dx, k+getYSize()-30-48+25, 20, 20, "+"));
//
//		renderables.add(new Button(4, SHIFT+j+getXSize()-20-inset-dx, k+getYSize()-30-48+50, 20, 20, ""));
//
//	}
//
//	@Override
//	protected void actionPerformed(Button b) {
//		super.actionPerformed(b);
//		PacketTarget pt = PacketTarget.server;
//		if (b.id == 4) {
//			ReikaPacketHelper.sendUpdatePacket(RotaryCraft.packetChannel, PacketRegistry.CONVERTERREDSTONE.ordinal(), engine, pt);
//		}
//		else if (b.id < 24000) {
//			ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CONVERTERPOWER.ordinal(), engine, b.id == 3 ? 1 : 0);
//		}
//		this.initGui();
//	}
//
//	@Override
//	protected void drawGuiContainerForegroundLayer(int a, int b)
//	{
//
//		int j = (width - getXSize()) / 2;
//		int k = (height - getYSize()) / 2;
//		super.drawGuiContainerForegroundLayer(a, b);
//
//		int torque = engine.getGenTorque();
//		int omega = engine.getMaxSpeed();
//		long power = (long)torque*(long)omega;
//		int inset = 1;
//		int w = 55;
//		int h = 20;
//		int dy = h+5;
//		int dx = 4;
//
//		for (int i = 0; i < 3; i++) {
//			this.drawRect(SHIFT+getXSize()/2-w-dx, getYSize()-30-48+i*dy, SHIFT+getXSize()/2+w-dx, getYSize()-30-48+h+i*dy, 0xff777777);
//			this.drawRect(SHIFT+getXSize()/2-w+inset-dx, getYSize()-30-48+inset+i*dy, SHIFT+getXSize()/2+w-inset-dx, getYSize()-30-48+h-inset+i*dy, 0xff000000);
//		}
//
//		this.drawCenteredString(font, String.format("Torque: %d Nm", torque), SHIFT+getXSize()/2-dx, getYSize()-30-48+6, 0xffffff);
//
//		this.drawCenteredString(font, String.format("Speed: %d rad/s", omega), SHIFT+getXSize()/2-dx, getYSize()-30-48+6+dy, 0xffffff);
//		this.drawCenteredString(font, String.format("Power: %.3f %sW", ReikaMathLibrary.getThousandBase(power), ReikaEngLibrary.getSIPrefix(power)), SHIFT+getXSize()/2-dx, getYSize()-30-48+6+dy*2, 0xffffff);
//
//		if (ReikaGuiAPI.instance.isMouseInBox(j+171, j+188, k+21, k+90)) {
//			int e = engine.getStoredPower();
//			String sg = String.format("%d/%d %s", e, engine.getMaxStorage(), engine.getUnitDisplay());
//			ReikaGuiAPI.instance.drawTooltipAt(font, sg, ReikaGuiAPI.instance.getMouseRealX()-j+font.width(sg)+24, ReikaGuiAPI.instance.getMouseRealY()-k);
//			//this.drawHoveringText(ReikaJavaLibrary.makeListFrom(sg), ReikaGuiAPI.instance.getMouseRealX()-j, ReikaGuiAPI.instance.getMouseRealY()-k, font);
//		}
//
//		if (ReikaGuiAPI.instance.isMouseInBox(j+192, j+200, k+21, k+90)) {
//			int e = engine.getLubricant();
//			String sg = String.format("%d/%d mB", e, engine.getMaxLubricant());
//			ReikaGuiAPI.instance.drawTooltipAt(font, sg, ReikaGuiAPI.instance.getMouseRealX()-j+font.width(sg)+24, ReikaGuiAPI.instance.getMouseRealY()-k);
//			//this.drawHoveringText(ReikaJavaLibrary.makeListFrom(sg), ReikaGuiAPI.instance.getMouseRealX()-j, ReikaGuiAPI.instance.getMouseRealY()-k, font);
//		}
//
//		if (ReikaGuiAPI.instance.isMouseInBox(-12+j+getXSize()-20-23, -12+j+getXSize()-23, k+getYSize()-30-48+50, k+getYSize()-30-28+50)) {
//			String sg = "Redstone Control";
//			ReikaGuiAPI.instance.drawTooltipAt(font, sg, ReikaGuiAPI.instance.getMouseRealX()-24-font.width(sg), ReikaGuiAPI.instance.getMouseRealY()-k);
//			//this.drawHoveringText(ReikaJavaLibrary.makeListFrom(sg), ReikaGuiAPI.instance.getMouseRealX()-j, ReikaGuiAPI.instance.getMouseRealY()-k, font);
//		}
//
//		int ddy = engine.isRedstoneControlEnabled() ? 0 : 1;
//		api.drawItemStack(itemRender, font, engine.getRedstoneStateIcon(), 148, 71+ddy);
//		GL11.glPushMatrix();
//		double s = 0.75;
//		GL11.glScaled(s, s, s);
//		api.drawItemStack(itemRender, font, engine.hasRedstoneSignal() ? new ItemStack(Blocks.lit_redstone_lamp) : new ItemStack(Blocks.redstone_lamp), (int)(148/s-2/s)-6, (int)(71/s)-6);
//		GL11.glPopMatrix();
//	}
//
//	@Override
//	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
//		int j = (width - getXSize()) / 2;
//		int k = (height - getYSize()) / 2;
//		super.drawGuiContainerBackgroundLayer(par1, par2, par3);
//
//		int px = engine.getEnergyScaled(68);
//		int c = engine.getPowerColor();
//		GL11.glColor3f(ReikaColorAPI.getRed(c)/255F, ReikaColorAPI.getGreen(c)/255F, ReikaColorAPI.getBlue(c)/255F);
//		this.drawTexturedModalRect(j+172, k+90-px, 208, 69-px, 16, px);
//
//		int px2 = engine.getLubricantScaled(68);
//		GL11.glColor3f(1, 1, 1);
//		this.drawTexturedModalRect(j+193, k+90-px2, 244, 69-px2, 7, px2);
//	}
//
//	@Override
//	protected String getGuiTexture() {
//		return "pneugui";
//	}
//
//}
