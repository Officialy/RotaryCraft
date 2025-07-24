///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders.mi;
//
//import reika.rotarycraft.blockentities.Weaponry.Turret.BlockEntityRailGun;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.neoforged.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.Turret.ModelRailGun;
//
//public class RenderRailGun extends RotaryTERenderer {
//
//    private ModelRailGun railgunModel = new ModelRailGun();
//
//    public void renderBlockEntityRailGunAt(BlockEntityRailGun tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelRailGun var14;
//        var14 = railgunModel;
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/railguntex.png");
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
//        if (tile == null)
//            return;
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityRailGunAt((BlockEntityRailGun) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(tile, par2, par4, par6);
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "railguntex.png";
//    }
//}
