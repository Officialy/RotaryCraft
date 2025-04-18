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
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.gui.components.Button;
//import net.minecraft.client.gui.components.ImageButton;
//import net.minecraft.world.entity.player.Player;
//import net.minecraftforge.client.gui.ScreenUtils;
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.rotarycraft.RotaryCraft;
//
//import reika.rotarycraft.base.EngineScreen;
//import reika.rotarycraft.base.blockentity.BlockEntityEngine;
//import reika.rotarycraft.gui.container.machine.ContainerJet;
//import reika.rotarycraft.registry.PacketRegistry;
//
//
//public class GuiJet extends EngineScreen<BlockEntityEngine, ContainerJet> {
//    private final BlockEntityJetEngine jet;
//    private boolean burn;
//
//    public GuiJet(Player p5ep, BlockEntityEngine te) {
//        super();
//        imageWidth = 176;
//        imageHeight = 166;
//        jet = eng instanceof BlockEntityJetEngine ? (BlockEntityJetEngine) eng : null;
//        burn = jet != null && jet.canAfterBurn() && jet.burnerActive();
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        if (jet != null) {
//            int u = jet.burnerActive() ? 36 : 0;
//            int v = jet.canAfterBurn() ? 72 : 90;
//            addRenderableWidget(new ImageButton(0, j + 32, k + 36, 36, 18, u, v, "Textures/GUI/buttons.png", RotaryCraft.class));
//        }
//    }
//
//    @Override
//    protected void actionPerformed(Button b) {
//        super.actionPerformed(b);
//
//        if (b.id == 0 && jet != null && jet.canAfterBurn()) {
//            burn = !burn;
//            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.AFTERBURN.ordinal(), eng, burn ? 1 : 0);
//            jet.setBurnerActive(burn);
//            this.initGui();
//        }
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        int x = api.getMouseRealX();
//        int y = api.getMouseRealY();
//
//        if (jet != null) {
//            if (api.isMouseInBox(j + 32, j + 68, k + 36, k + 54)) {
//                api.drawTooltipAt(font, "Afterburner", x - j, y - k);
//            }
//        }
//    }
//
//    @Override
//    protected int getFuelBarXPos() {
//        return 84;
//    }
//
//    @Override
//    protected int getFuelBarYPos() {
//        return 16;
//    }
//
//    @Override
//    protected int getFuelBarXSize() {
//        return 6;
//    }
//
//    @Override
//    protected int getFuelBarYSize() {
//        return 55;
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(poseStack, par1, par2, par3);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        int i2 = engine.getFuelScaled(54);
//        ScreenUtils.drawTexturedModalRect(poseStack, + 85, k + 71 - i2, 207, 55 - i2, 5, i2, 0);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "jetgui";
//    }
//}
