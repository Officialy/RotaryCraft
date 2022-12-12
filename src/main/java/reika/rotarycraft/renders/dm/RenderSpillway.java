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
//import reika.dragonapi.instantiable.Effects.EntityFluidFX;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.java.ReikaGLHelper.BlendMode;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.rotarycraft.blockentities.Production.BlockEntitySpillway;
//import com.mojang.blaze3d.vertex.BufferBuilder;
//import com.mojang.blaze3d.vertex.Tesselator;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.Tesselator;
//import net.minecraft.core.Direction;
//
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.level.material.Fluids;
//import net.minecraftforge.client.MinecraftForgeClient;
//import net.minecraftforge.common.util.Direction;
//import net.minecraftforge.fluids.Fluid;
//import net.minecraftforge.fluids.FluidRegistry;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.blockentities.production.BlockEntitySpillway;
//import reika.rotarycraft.models.ModelSpillway;
//
//public class RenderSpillway extends RotaryTERenderer {
//
//    private ModelSpillway SpillwayModel = new ModelSpillway();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntitySpillwayAt(BlockEntitySpillway tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelSpillway var14;
//
//        var14 = SpillwayModel;
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/spillwaytex.png");
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
//
//            switch (tile.getBlockMetadata()) {
//                case 0:
//                    var11 = 180;
//                    break;
//                case 1:
//                    var11 = 0;
//                    break;
//                case 2:
//                    var11 = 90;
//                    break;
//                case 3:
//                    var11 = 270;
//                    break;
//            }
//
//            stack.mulPose(new Quaternion(var11, 0.0F, 1.0F, 0.0F);
//
//        }
//
//        float var13;
//
//        var14.renderAll(stack, tile, null);
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntitySpillwayAt((BlockEntitySpillway) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            stack.pushPose();
//            stack.translate(par2, par4, par6);
//            this.renderLiquid((BlockEntitySpillway) tile, par8);
//            if (((BlockEntitySpillway) tile).isActive())
//                this.renderParticles((BlockEntitySpillway) tile, par8);
//            stack.popPose();
//        }
//    }
//
//    private void renderParticles(BlockEntitySpillway te, float par8) {
//        if (!Minecraft.getInstance().isGamePaused() && te.getRandom().nextInt(2) == 0) {
//            Direction dir = te.getDrainSide();
//            double v = 0.03125;
//            double px = te.xCoord + 0.5 + dir.offsetX * 0.5;
//            double pz = te.zCoord + 0.5 + dir.offsetZ * 0.5;
//            if (dir.offsetX != 0)
//                pz = ReikaRandomHelper.getRandomPlusMinus(pz, 0.4);
//            if (dir.offsetZ != 0)
//                px = ReikaRandomHelper.getRandomPlusMinus(px, 0.4);
//            EntityFluidFX fx = new EntityFluidFX(te.level, px, te.yCoord + 1, pz, -v * dir.offsetX, 0, -v * dir.offsetZ, FluidRegistry.WATER);
//            fx.setGravity(0.0625F).setScale(0.5F).setLife(30 - te.getLevel() * 15 / 8000);
//            Minecraft.getInstance().effectRenderer.addEffect(fx);
//        }
//        Tesselator tess = Tesselator.getInstance();
//        BufferBuilder v5 = tess.getBuilder();
//        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//        RenderSystem.enableBlend();
//        GL11.glDisable(GL11.GL_CULL_FACE);
//        RenderSystem.defaultBlendFunc();
//        boolean flip = false;
//
//        stack.pushPose();
//        int r = 0;
//        switch (te.getBlockMetadata()) {
//            case 0:
//                r = 180;
//                stack.translate(1, 0, 0);
//                break;
//            case 1:
//                r = 0;
//                stack.translate(0, 0, 1);
//                break;
//            case 2:
//                r = 90;
//                stack.translate(1, 0, 1);
//                flip = true;
//                break;
//            case 3:
//                r = 270;
//                flip = true;
//                break;
//        }
//        stack.mulPose(new Quaternion(r + 90, 0.0F, 1.0F, 0.0F);
//
//        ReikaTextureHelper.bindTerrainTexture();
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.normal(0, 1, 0);
//        v5.setColorOpaque_I(0xffffff);
//        IIcon ico = FluidRegistry.WATER.getFlowingIcon();
//        float u = ico.getMinU();
//        float v = ico.getMinV();
//        float du = ico.getMaxU();
//        double z0 = 0.125;
//        double z1 = 6.2 / 16D;
//        float v0 = ico.getInterpolatedV(z0 * 16);
//        float v1 = ico.getInterpolatedV(z1 * 16);
//        float dv = ico.getMaxV();
//        double h0 = 1 - 0.03125 + 0.005;
//        double h1 = 0.78 + 0.005;
//        double h2 = 0.0625 + 0.005;
//        if (flip) {
//            v5.addVertexWithUV(0, h0, 1 - z0, u, v0);
//            v5.addVertexWithUV(1, h0, 1 - z0, du, v0);
//            v5.addVertexWithUV(1, h0, 1, du, v);
//            v5.addVertexWithUV(0, h0, 1, u, v);
//
//            v5.addVertexWithUV(0, h1, 1 - z1, u, v1);
//            v5.addVertexWithUV(1, h1, 1 - z1, du, v1);
//            v5.addVertexWithUV(1, h0, 1 - z0, du, v0);
//            v5.addVertexWithUV(0, h0, 1 - z0, u, v0);
//
//            v5.addVertexWithUV(0, h1, 1 - z1, u, v1);
//            v5.addVertexWithUV(1, h1, 1 - z1, du, v1);
//            v5.addVertexWithUV(1, h2, 1 - z1, du, dv);
//            v5.addVertexWithUV(0, h2, 1 - z1, u, dv);
//        } else {
//            v5.addVertexWithUV(0, h0, z0, u, v0);
//            v5.addVertexWithUV(1, h0, z0, du, v0);
//            v5.addVertexWithUV(1, h0, 0, du, v);
//            v5.addVertexWithUV(0, h0, 0, u, v);
//
//            v5.addVertexWithUV(0, h1, z1, u, v1);
//            v5.addVertexWithUV(1, h1, z1, du, v1);
//            v5.addVertexWithUV(1, h0, z0, du, v0);
//            v5.addVertexWithUV(0, h0, z0, u, v0);
//
//            v5.addVertexWithUV(0, h1, z1, u, v1);
//            v5.addVertexWithUV(1, h1, z1, du, v1);
//            v5.addVertexWithUV(1, h2, z1, du, dv);
//            v5.addVertexWithUV(0, h2, z1, u, dv);
//        }
//        v5.end();
//
//        //GL11.glPopAttrib();
//        stack.popPose();
//    }
//
//    private void renderLiquid(BlockEntitySpillway te, float par8) {
//        int amt = te.getLevel();
//        if (amt <= 0)
//            return;
//        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        double h = 0.03125 + 11.5 / 16D * amt / te.CAPACITY;
//        Fluid f = Fluids.WATER;
//
//        ReikaLiquidRenderer.bindFluidTexture(f);
//        IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//        float u = ico.getMinU();
//        float v = ico.getMinV();
//        float du = ico.getMaxU();
//        float dv = ico.getMaxV();
//        Tesselator tess = Tesselator.getInstance();
//        BufferBuilder v5 = tess.getBuilder();
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.normal(0, 1, 0);
//        v5.setColorOpaque_I(0xffffff);
//        v5.addVertexWithUV(0, h, 1, u, dv);
//        v5.addVertexWithUV(1, h, 1, du, dv);
//        v5.addVertexWithUV(1, h, 0, du, v);
//        v5.addVertexWithUV(0, h, 0, u, v);
//        v5.end();
//        //GL11.glPopAttrib();
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "spillwaytex.png";
//    }
//}
