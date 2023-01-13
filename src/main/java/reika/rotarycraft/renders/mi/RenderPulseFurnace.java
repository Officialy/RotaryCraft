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
//import reika.rotarycraft.blockentities.processing.BlockEntityPulseFurnace;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraftforge.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.ModelPulseFurnace;
//
//public class RenderPulseFurnace extends RotaryTERenderer {
//
//    private ModelPulseFurnace PulseFurnaceModel = new ModelPulseFurnace();
//
//    @Override
//    protected String getTextureSubfolder() {
//        return "PulseJet/";
//    }
//
//    public void renderBlockEntityPulseFurnaceAt(BlockEntityPulseFurnace tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelPulseFurnace var14;
//        var14 = PulseFurnaceModel;
//        int temp = tile.temperature;
//        int maxtemp = tile.MAXTEMP;
//
//        if (temp < maxtemp / 10)
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/PulseJet/pulsetex.png");
//        else if (temp < maxtemp / 5)
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/PulseJet/pulsetexhot-1.png");
//        else if (temp < maxtemp / 2)
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/PulseJet/pulsetexhot0.png");
//        else if (temp < maxtemp / 1.2)
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/PulseJet/pulsetexhot2.png");
//        else
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/PulseJet/pulsetexhot3.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 0;
//
//        if (tile.isInWorld()) {
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
//                stack.mulPose((float) var11 + 0, 0.0F, 1.0F, 0.0F);
//            else {
//                stack.mulPose(new Quaternion(var11, 1F, 0F, 0.0F);
//                stack.translate(0F, -1F, 1F);
//                if (tile.getBlockMetadata() == 5)
//                    stack.translate(0F, 0F, -2F);
//            }
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
//            this.renderBlockEntityPulseFurnaceAt((BlockEntityPulseFurnace) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(tile, par2, par4, par6);
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        BlockEntityPulseFurnace tp = (BlockEntityPulseFurnace) te;
//        int temp = tp.temperature;
//        int maxtemp = tp.MAXTEMP;
//        if (temp < maxtemp / 10)
//            return "pulsetex.png";
//        else if (temp < maxtemp / 5)
//            return "pulsetexhot-1.png";
//        else if (temp < maxtemp / 2)
//            return "pulsetexhot0.png";
//        else if (temp < maxtemp / 1.2)
//            return "pulsetexhot2.png";
//        else
//            return "pulsetexhot3.png";
//    }
//}
