///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders.m;
//
//import reika.rotarycraft.blockentities.Weaponry.Turret.BlockEntityLaserGun;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.renderer.Tesselator;
//import net.neoforged.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.Turret.ModelLaserGun;
//
//public class RenderLaserGun extends RotaryTERenderer {
//
//    private ModelLaserGun railgunModel = new ModelLaserGun();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityLaserGunAt(BlockEntityLaserGun tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelLaserGun var14;
//        var14 = railgunModel;
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/lasertex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 1;     //used to rotate the model about metadata
//        int var12 = 0;
//        if (tile.isInWorld()) {
//            if (tile.getBlockMetadata() == 1) {
//                var11 = -1;
//                var12 = 2;
//                GL11.glFrontFace(GL11.GL_CW);
//            }
//        }
//        stack.translate(0, var12, 0);
//        stack.scale(1, var11, 1);
//        int a = tile.getBlockMetadata() == 0 ? -1 : 1;
//        var14.renderAll(stack, tile, null, -tile.phi, a * tile.theta);
//        stack.scale(1, var11, 1);
//        stack.translate(0, -var12, 0);
//        GL11.glFrontFace(GL11.GL_CCW);
//
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityLaserGunAt((BlockEntityLaserGun) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//            this.renderLaser((BlockEntityLaserGun) tile, par2, par4, par6);
//        }
//    }
//
//    private void renderLaser(BlockEntityLaserGun tile, double par2, double par4, double par6) {
//        if (tile == null)
//            return;
//        if (!tile.isInWorld())
//            return;
//        int r = tile.getRange();
//        if (r <= 0)
//            return;
//        double voff = 0.75;
//        double h = 0.0625;
//        GL11.glDisable(GL11.GL_CULL_FACE);
//        double dx = r * Math.cos(Math.toRadians(tile.theta)) * Math.cos(Math.toRadians(-tile.phi + 90));
//        double dy = r * Math.sin(Math.toRadians(tile.theta));
//        double dz = r * Math.cos(Math.toRadians(tile.theta)) * Math.sin(Math.toRadians(-tile.phi + 90));
//
//        double dd0 = 0.0625 * Math.cos(Math.toRadians(-tile.phi));
//        double dd1 = 0.0625 * Math.sin(Math.toRadians(-tile.phi));
//
//        if (tile.getBlockMetadata() == 1) {
//            voff = 0.25;
//            h = -h;
//        }
//
//        ReikaRenderHelper.prepareGeoDraw(false);
//        int[] rgb = {255, 0, 0};
//        Tesselator tess = Tesselator.getInstance();
//        BufferBuilder v5 = tess.getBuilder();
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.setColorOpaque(rgb[0], rgb[1], rgb[2]);
//        v5.vertex(par2 + 0.5 - dd0, par4 + voff, par6 + 0.5 - dd1);
//        v5.vertex(par2 + 0.5 + dd0, par4 + voff, par6 + 0.5 + dd1);
//        v5.vertex(par2 + dx - dd0, par4 + dy, par6 + dz - dd1);
//        v5.vertex(par2 + dx + dd0, par4 + dy, par6 + dz + dd1);
//        v5.end();
//
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.setColorOpaque(rgb[0], rgb[1], rgb[2]);
//        v5.vertex(par2 + 0.5 - dd0, par4 + voff + h, par6 + 0.5 - dd1);
//        v5.vertex(par2 + 0.5 + dd0, par4 + voff + h, par6 + 0.5 + dd1);
//        v5.vertex(par2 + dx - dd0, par4 + dy, par6 + dz - dd1);
//        v5.vertex(par2 + dx + dd0, par4 + dy, par6 + dz + dd1);
//        v5.end();
//
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.setColorOpaque(rgb[0], rgb[1], rgb[2]);
//        v5.vertex(par2 + 0.5 - dd0, par4 + voff, par6 + 0.5 - dd1);
//        v5.vertex(par2 + 0.5 + dd0, par4 + voff + h, par6 + 0.5 + dd1);
//        v5.vertex(par2 + dx - dd0, par4 + dy, par6 + dz - dd1);
//        v5.vertex(par2 + dx + dd0, par4 + dy, par6 + dz + dd1);
//        v5.end();
//
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.setColorOpaque(rgb[0], rgb[1], rgb[2]);
//        v5.vertex(par2 + 0.5 - dd0, par4 + voff + h, par6 + 0.5 - dd1);
//        v5.vertex(par2 + 0.5 + dd0, par4 + voff, par6 + 0.5 + dd1);
//        v5.vertex(par2 + dx - dd0, par4 + dy, par6 + dz - dd1);
//        v5.vertex(par2 + dx + dd0, par4 + dy, par6 + dz + dd1);
//        v5.end();
//
//        GL11.glEnable(GL11.GL_CULL_FACE);
//        ReikaRenderHelper.exitGeoDraw();
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "lasertex.png";
//    }
//}
