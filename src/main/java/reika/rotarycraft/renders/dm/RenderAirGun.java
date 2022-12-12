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
//import reika.rotarycraft.blockentities.Weaponry.BlockEntityAirGun;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraftforge.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.ModelAirGun;
//
//public class RenderAirGun extends RotaryTERenderer {
//
//    private ModelAirGun AirGunModel = new ModelAirGun();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityAirGunAt(BlockEntityAirGun tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelAirGun var14;
//        var14 = AirGunModel;
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/airguntex.png");
//
//        this.setupGL(tile, par2, par4, par6);
//
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
//                    var11 = 270;
//                    break;
//                case 3:
//                    var11 = 90;
//                    break;
//            }
//
//            if (tile.getBlockMetadata() <= 3)
//                GL11.glRotatef((float) var11 - 90, 0.0F, 1.0F, 0.0F);
//            else {
//                stack.mulPose(new Quaternion(var11, 1F, 0F, 0.0F);
//                stack.translate(0F, -1F, 1F);
//                if (tile.getBlockMetadata() == 5)
//                    stack.translate(0F, 0F, -2F);
//            }
//        }
//
//        var14.renderAll(stack, tile, null, -tile.phi);
//
//        this.closeGL(stack, tile);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityAirGunAt((BlockEntityAirGun) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(tile, par2, par4, par6);
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "airguntex.png";
//    }
//}
