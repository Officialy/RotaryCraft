///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.guis.Machine.Inventory;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraftforge.client.gui.ScreenUtils;
//import reika.rotarycraft.base.EngineScreen;
//import reika.rotarycraft.blockentities.engine.BlockEntityGasEngine;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerEthanol;
//
//public class GuiEthanol extends EngineScreen {
//    private final BlockEntityGasEngine engine;
//
//    public GuiEthanol(int id, Inventory p5ep, Component component, BlockEntityGasEngine te) {
//        super(new ContainerEthanol(id, p5ep, te), p5ep, component);
//        engine = te;
//        imageWidth = 176;
//        imageHeight = 166;
//        ep = p5ep;
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(poseStack, par1, par2, par3);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        int i1 = engine.getFuelScaled(54);
//        ScreenUtils.drawTexturedModalRect(poseStack, j + 85, k + 71 - i1, 200, 55 - i1, 5, i1, 0);
//
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "ethanolgui";
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
//}
