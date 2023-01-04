///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.gui.screen.machine;
//
//import java.awt.Color;
//
//import net.minecraft.client.gui.components.Button;
//import net.minecraft.client.gui.components.ImageButton;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.item.ItemStack;
//import org.lwjgl.opengl.GL11;
//
//import reika.dragonapi.base.CoreMenu;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.RotaryAux;
//import reika.rotarycraft.base.GuiNonPoweredMachine;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.blockentities.transmission.BlockEntityMultiClutch;
//
//public class GuiMultiClutch extends GuiNonPoweredMachine {
//
//    private final BlockEntityMultiClutch multi;
//
//    public GuiMultiClutch(Inventory ep, BlockEntityMultiClutch te) {
//        super(new CoreMenu(ep, te), te);
//        this.ep = ep;
//        imageWidth = 176;
//        imageHeight = 148;
//        multi = te;
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        String file = "/Reika/RotaryCraft/Textures/GUI/buttons.png";
//
//        for (int i = 0; i < 16; i++) {
//            addRenderableWidget(new ImageButton(i, j + 18 + 70 * (i / 8) + 15, k + 20 + 16 * (i % 8) - 1, 35, 9, 256 - 18, 256 - 62, file, RotaryCraft.class));
//        }
//    }
//
//    @Override
//    protected void actionPerformed(Button b) {
//        super.actionPerformed(b);
//        if (b.id < 16) {
//            int side = multi.getNextSideForState(b.id);
//            multi.setSideOfState(b.id, side);
//            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.MULTISIDE.ordinal(), multi, b.id, side);
//        }
//        this.initGui();
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        int pwr = multi.level.getBlockPowerInput(multi.xCoord, multi.yCoord, multi.zCoord);
//        for (int i = 0; i < 16; i++) {
//            int x = 3 + 70 * (i / 8);
//            int y = 15 + 16 * (i % 8);
//            api.drawItemStack(itemRender, font, new ItemStack(i == pwr ? Items.glowstone_dust : Items.redstone), x, y);
//        }
//
//        GL11.glDisable(GL11.GL_LIGHTING);
//        GL11.glColor4f(1, 1, 1, 1);
//        for (int i = 0; i < 16; i++) {
//            font.draw(String.format("%d", i), 18 + 70 * (i / 8), 20 + 16 * (i % 8), 0);
//            int idx = multi.getSideOfState(i);
//            Color color = RotaryAux.sideColors[idx];
//            int border = 0xff000000;
//            if (color == Color.BLACK)
//                border = 0xffffffff;
//            this.drawRect(18 + 70 * (i / 8) + 14, 20 + 16 * (i % 8) - 2, 18 + 70 * (i / 8) + 51, 20 + 16 * (i % 8) + 9, border);
//            this.drawRect(18 + 70 * (i / 8) + 15, 20 + 16 * (i % 8) - 1, 18 + 70 * (i / 8) + 50, 20 + 16 * (i % 8) + 8, 0xff000000 + color.getRGB());
//            String s = ReikaStringParser.capFirstChar(Direction.values()[idx].name());
//            font.draw(s, 18 + 70 * (i / 8) + 16, 20 + 16 * (i % 8), idx == 0 || idx == 2 ? 0x000000 : 0xffffff);
//        }
//        GL11.glEnable(GL11.GL_LIGHTING);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "multigui";
//    }
//
//}
