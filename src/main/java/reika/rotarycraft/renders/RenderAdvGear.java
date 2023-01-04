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
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.VertexConsumer;
//import net.minecraft.client.gui.Font;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import org.lwjgl.opengl.GL11;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.auxiliary.RotaryAux;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;
//import reika.rotarycraft.models.CVTModel;
//import reika.rotarycraft.models.animated.CoilModel;
//import reika.rotarycraft.models.animated.HighGearModel;
//import reika.rotarycraft.models.animated.WormModel;
//import reika.rotarycraft.registry.RotaryModelLayers;
//
//public class RenderAdvGear extends RotaryTERenderer<BlockEntityAdvancedGear> {
//
//    private WormModel wormModel;
//    private CVTModel cvtModel;
//    private CoilModel coilModel;
//    private HighGearModel highGearModel;
//
//    public RenderAdvGear(BlockEntityRendererProvider.Context context) {
//        wormModel = new WormModel(context.bakeLayer(RotaryModelLayers.WORMMODEL));
//        cvtModel = new CVTModel(context.bakeLayer(RotaryModelLayers.CVTMODEL));
//        coilModel = new CoilModel(context.bakeLayer(RotaryModelLayers.COILMODEL));
//        highGearModel = new HighGearModel(context.bakeLayer(RotaryModelLayers.HIGHGEARMODEL));
//    }
//    public void renderBlockEntityAdvancedGearAt(BlockEntityAdvancedGear tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//       /* switch (itemMetadata) {
//            case 1 -> {
//                this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/shaft/shafttex.png");
//                wormModel.renderAll(stack, tile, null);
//            }
//            case 2 -> {
//                this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/cvttex.png");
//                cvtModel.renderAll(stack, tile, null);
//            }
//            case 3 -> {
//                if (tile.isBedrockCoil())
//                    this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/coiltex_bed.png");
//                else
//                    this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/coiltex.png");
//                coilModel.renderAll(stack, tile, null);
//            }
//            case 4 -> {
//                this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/highgeartex.png");
//                highGearModel.renderAll(stack, tile, null);
//            }
//        }*/
//
//        switch (tile.getGearType()) {
//            case WORM:
//                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout((new ResourceLocation(RotaryCraft.MODID"/textures/blockentitytex/transmission/shaft/shaft/shafttex.png"))));
//                wormModel.renderAll(stack, tile, null, tile.phi);
//                break;
//            case CVT:
//                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout((new ResourceLocation(RotaryCraft.MODID,"/textures/blockentitytex/transmission/shaft/cvttex.png"))));
//                cvtModel.renderAll(stack, tile, null, tile.phi);
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
//                        fr.draw(stack, var15b, -18, 70, 0xffffff);
//
//                        var15b = RotaryAux.formatTorque(tile.torque);
//                        fr.draw(stack, var15b, -18, 82, 0xffffff);
//
//                        var15b = RotaryAux.formatSpeed(tile.omega);
//                        fr.draw(stack, var15b, -18, 94, 0xffffff);
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
//                        fr.draw(stack, var15b, -10, 39, 0xffffff);
//                    }
//
//                    //GL11.glPopAttrib();
//                }
//                break;
//            case COIL:
//                if (tile.isBedrockCoil())
//                    VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout((new ResourceLocation(RotaryCraft.MODID,"/textures/blockentitytex/transmission/shaft/coiltex_bed.png"))));
//                else
//                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout((new ResourceLocation(RotaryCraft.MODID,"/textures/blockentitytex/transmission/shaft/coiltex.png"))));
//                coilModel.renderAll(stack, tile, null, tile.phi);
//                break;
//            case HIGH:
//                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout((new ResourceLocation(RotaryCraft.MODID,"/textures/blockentitytex/transmission/shaft/highgeartex.png"))));
//                highGearModel.renderAll(stack, tile, null, tile.phi);
//                break;
//        }
//    }
//
//    @Override
//    public void render(BlockEntityAdvancedGear tile, float e, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay) {
//      /*  if (par8 <= -999F) {
//            itemMetadata = (int) -par8 / 1000;
//            par8 = 0F;
//        }*/
//        if (this.doRenderModel(stack, tile))
//            this.renderBlockEntityAdvancedGearAt(tile, stack, bufferSource, light);
//        if (tile.isInWorld())// && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(stack, bufferSource, tile, tile.getBlockPos());
//    }
//
//}
