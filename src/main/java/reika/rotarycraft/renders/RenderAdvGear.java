///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders;
//
//import reika.rotarycraft.base.BlockEntity.BlockEntityIOMachine;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.gui.Font;
//import net.minecraftforge.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.auxiliary.RotaryAux;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.Animated.ModelCoil;
//import reika.rotarycraft.models.Animated.ModelHighGear;
//import reika.rotarycraft.models.Animated.ModelWorm;
//import reika.rotarycraft.models.ModelCVT;
//import reika.rotarycraft.tileentities.transmission.BlockEntityAdvancedGear;
//
//public class RenderAdvGear extends RotaryTERenderer {
//
//    private ModelWorm wormModel = new ModelWorm();
//    private ModelCVT cvtModel = new ModelCVT();
//    private ModelCoil coilModel = new ModelCoil();
//    private ModelHighGear highGearModel = new ModelHighGear();
//    private int itemMetadata = 0;
//
//    @Override
//    protected String getTextureSubfolder() {
//        return "Transmission/";
//    }
//
//    public void renderBlockEntityAdvancedGearAt(BlockEntityAdvancedGear tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelWorm var14 = wormModel;
//        ModelCVT var15 = cvtModel;
//        ModelCoil var16 = coilModel;
//        ModelHighGear var17 = highGearModel;
//
//        this.setupGL(tile, par2, par4, par6);
//
//        int var11 = 0;     //used to rotate the model about metadata
//
//        if (tile.isInWorld()) {
//
//            switch (tile.getBlockMetadata() % 4) {
//                case 0:
//                    var11 = 0;
//                    break;
//                case 1:
//                    var11 = 180;
//                    break;
//                case 2:
//                    var11 = 90;
//                    break;
//                case 3:
//                    var11 = 270;
//                    break;
//            }
//
//            GL11.glRotatef((float) var11 + 180, 0.0F, 1.0F, 0.0F);
//
//        } else {
//            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", this.itemMetadata));
//            stack.mulPose(new Quaternion(-90, 0.0F, 1.0F, 0.0F);
//            switch (itemMetadata) {
//                case 1:
//                    this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/shaft/shafttex.png");
//                    var14.renderAll(stack, tile, null);
//                    break;
//                case 2:
//                    this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/cvttex.png");
//                    var15.renderAll(stack, tile, null);
//                    break;
//                case 3:
//                    if (tile.isBedrockCoil())
//                        this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/coiltex_bed.png");
//                    else
//                        this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/coiltex.png");
//                    var16.renderAll(stack, tile, null);
//                    break;
//                case 4:
//                    this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/highgeartex.png");
//                    var17.renderAll(stack, tile, null);
//                    break;
//            }
//            if (tile.isInWorld())
//                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//            stack.popPose();
//            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//            return;
//        }
//
//        float var13;
//        switch (tile.getGearType()) {
//            case WORM:
//                this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/shaft/shafttex.png");
//                var14.renderAll(stack, tile, null, tile.phi);
//                break;
//            case CVT:
//                this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/cvttex.png");
//                var15.renderAll(stack, tile, null, tile.phi);
//                if (tile.isInWorld()) {
//                    GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//                    GL11.glDisable(GL11.GL_LIGHTING);
//                    ReikaRenderHelper.disableEntityLighting();
//                    Font fr = this.getFontRenderer();
//                    float var10 = 0.6666667F * 0.8F;
//                    stack.scale(var10, -var10, -var10);
//                    float var112 = 0.016666668F * var10;
//                    stack.translate(0.0F, 0.61875F * var10, 0.20625F * var10);
//                    GL11.glRotated(-20, 1, 0, 0);
//                    stack.translate(-0.175F, -0.545F, -0.19F); //was 0.1X
//                    stack.scale(var112, -var112, var112);
//                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//                    GL11.glDepthMask(false);
//                    stack.translate(5, -48, 37);
//                    stack.scale(2, 2, 2);
//                    String var15b;
//
//                    if (tile.getTicksExisted() / 80 % 2 == 0) {
//                        var15b = RotaryAux.formatPower(tile.power);
//                        fr.draw(var15b, -18, 70, 0xffffff);
//
//                        var15b = RotaryAux.formatTorque(tile.torque);
//                        fr.draw(var15b, -18, 82, 0xffffff);
//
//                        var15b = RotaryAux.formatSpeed(tile.omega);
//                        fr.draw(var15b, -18, 94, 0xffffff);
//                    } else {
//                        stack.scale(2, 2, 2);
//                        stack.translate(0.075F, 0.25F, 0.1F);
//                        int ratio = tile.getCVTRatio();
//                        if (ratio > 0) {
//                            var15b = "1:" + ratio;
//                        } else {
//                            var15b = -ratio + ":1";
//                        }
//                        while (var15b.length() < 5) {
//                            var15b = " " + var15b;
//                        }
//                        fr.draw(var15b, -10, 39, 0xffffff);
//                    }
//
//                    //GL11.glPopAttrib();
//                }
//                break;
//            case COIL:
//                if (tile.isBedrockCoil())
//                    this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/coiltex_bed.png");
//                else
//                    this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/coiltex.png");
//                var16.renderAll(stack, tile, null, tile.phi);
//                break;
//            case HIGH:
//                this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/highgeartex.png");
//                var17.renderAll(stack, tile, null, tile.phi);
//                break;
//        }
//
//        this.closeGL(stack, tile);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (par8 <= -999F) {
//            itemMetadata = (int) -par8 / 1000;
//            par8 = 0F;
//        }
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityAdvancedGearAt((BlockEntityAdvancedGear) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(tile, par2, par4, par6);
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher t) {
//        if (!(t instanceof RenderFetcher))
//            return "";
//        BlockEntityIOMachine te = (BlockEntityIOMachine) t;
//        if (te.isInWorld()) {
//            if (te.getBlockMetadata() < 4)
//                return "shafttex.png";
//            else if (te.getBlockMetadata() < 8)
//                return "cvttex.png";
//            else if (te.getBlockMetadata() < 12)
//                return "coiltex.png";
//            else
//                return "highgeartex.png";
//        } else {
//            if (itemMetadata == 1)
//                return "shafttex.png";
//            if (itemMetadata == 2)
//                return "cvttex.png";
//            if (itemMetadata == 3)
//                return "coiltex.png";
//            if (itemMetadata == 4)
//                return "highgeartex.png";
//            return "";
//        }
//    }
//}
