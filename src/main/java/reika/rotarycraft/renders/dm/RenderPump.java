///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders.dm;
//
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import com.mojang.blaze3d.vertex.BufferBuilder;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.Tesselator;
//import com.mojang.math.Quaternion;
//import net.minecraft.client.renderer.MultiBufferSource;
//
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.client.MinecraftForgeClient;
//import net.minecraftforge.fluids.Fluid;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.blockentities.production.BlockEntityPump;
//import reika.rotarycraft.models.animated.ModelPump;
//
//public class RenderPump extends RotaryTERenderer {
//
//    private ModelPump PumpModel = new ModelPump();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityPumpAt(PoseStack stack, BlockEntityPump tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelPump var14;
//        var14 = PumpModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/pumptex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 0;     //used to rotate the model about metadata
//
//        if (tile.isInWorld()) {
//            switch (tile.getBlockMetadata()) {
//                case 0:
//                    var11 = 90;
//                    break;
//                case 1:
//                    var11 = 0;
//                    break;
//            }
//
//            stack.mulPose(new Quaternion(var11, 0.0F, 1.0F, 0.0F));
//
//        }
//
//        float var13;
//        Object[] pars = new Object[3];
//        pars[0] = tile.getLevel() > 0 && MinecraftForgeClient.getRenderPass() == 1;
//        pars[1] = (tile.shouldRenderInPass(0) && MinecraftForgeClient.getRenderPass() == 0) || !tile.isInWorld();
//        pars[2] = tile.isBroken();
//        var14.renderAll(stack, tile, ReikaJavaLibrary.makeListFromArray(pars), -tile.phi, 0);
//
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void render(BlockEntity tile, float v, PoseStack stack, MultiBufferSource multiBufferSource, int i, int i1) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile)) {
//            this.renderBlockEntityPumpAt(stack, (BlockEntityPump) tile, par2, par4, par6, par8);
//        }
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//        }
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            this.renderLiquid(stack, tile, par2, par4, par6);
//        }
//    }
//
//    private void renderLiquid(PoseStack stack, BlockEntity tile, double par2, double par4, double par6) {
//        stack.pushPose();
//        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//        RenderSystem.enableBlend();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor3f(1, 1, 1);
//        stack.translate(par2, par4, par6);
//        BlockEntityPump tr = (BlockEntityPump) tile;
//        if (tr.getLevel() > 0 && tr.isInWorld()) {
//            Fluid f = tr.getLiquid();
//            ReikaLiquidRenderer.bindFluidTexture(f);
//            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//            float u = ico.getMinU();
//            float v = ico.getMinV();
//            float du = ico.getMaxU();
//            float dv = ico.getMaxV();
//            double inset = 0.125;
//            double offset = inset * (du - u);
//            u += offset;
//            v += offset;
//            du -= offset;
//            dv -= offset;
//            double h = 0.3125 + 0.375 * tr.getLevel() / tr.CAPACITY;
//            Tesselator tess = Tesselator.getInstance();
//            BufferBuilder v5 = tess.getBuilder();
//            if (f.getLuminosity() > 0)
//                ReikaRenderHelper.disableLighting();
//            v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//            v5.normal(0, 1, 0);
//            v5.addVertexWithUV(inset, h, 1 - inset, u, dv);
//            v5.addVertexWithUV(1 - inset, h, 1 - inset, du, dv);
//            v5.addVertexWithUV(1 - inset, h, inset, du, v);
//            v5.addVertexWithUV(inset, h, inset, u, v);
//            v5.end();
//            ReikaRenderHelper.enableLighting();
//        }
//        //GL11.glPopAttrib();
//        stack.popPose();
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "pumptex.png";
//    }
//}
