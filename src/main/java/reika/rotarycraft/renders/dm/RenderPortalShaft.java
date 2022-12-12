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
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraftforge.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.Animated.ShaftOnly.ModelShaft;
//import reika.rotarycraft.models.Animated.ShaftOnly.ModelShaftV;
//import reika.rotarycraft.renders.RenderShaft;
//import reika.rotarycraft.tileentities.transmission.BlockEntityPortalShaft;
//
//public class RenderPortalShaft extends RenderShaft {
//
//    public void renderBlockEntityPortalShaftAt(BlockEntityPortalShaft tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelShaft var14 = ShaftModel;
//        ModelShaftV var15 = VShaftModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/" + this.getImageFileName(tile));
//
//        this.setupGL(tile, par2, par4, par6);
//
//        int var11 = 0;
//
//        int meta;
//        boolean failed = false;
//        meta = tile.getBlockMetadata();
//        if (tile.isInWorld()) {
//            switch (meta) {
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
//            stack.mulPose(new Quaternion(var11, 0.0F, 1.0F, 0.0F);
//        }
//        float var13;
//
//        int dir = 1;
//        if (meta == 5)
//            dir = -1;
//        double s = tile.isInWorld() && (tile.isEnteringPortal() || tile.isExitingPortal()) ? 1.5 : 1;
//        double d = tile.isInWorld() ? tile.isEnteringPortal() ? 0.25 : tile.isExitingPortal() ? -0.25 : 0 : 0;
//        double ss = 0.5;
//        if (meta <= 3) {
//            var14.renderMount(tile);
//            stack.translate(-d, 0, 0);
//            stack.scale(s, 1, 1);
//            var14.renderShaft(tile, -tile.phi);
//            stack.scale(1 / s, 1, 1);
//            stack.translate(d, 0, 0);
//        } else if (meta <= 5) {
//            d += 0.5 * dir;
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/" + this.getImageFileName(tile));
//            var15.renderMount(tile);
//            stack.translate(0, -d * dir, 0);
//            stack.scale(1, s, 1);
//            var15.renderShaft(tile, -tile.phi * dir);
//            stack.scale(1, 1 / s, 1);
//            stack.translate(0, d * dir, 0);
//        }
//
//        GL11.glEnable(GL11.GL_LIGHTING);
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityPortalShaftAt((BlockEntityPortalShaft) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(tile, par2, par4, par6);
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        if (te == null)
//            return null;
//        String name;
//        BlockEntityPortalShaft tile = (BlockEntityPortalShaft) te;
//        return tile.getShaftType().getBaseShaftTexture();
//    }
//}
