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
//import reika.dragonapi.libraries.ReikaAABBHelper;
//import reika.rotarycraft.base.BlockEntity.BlockEntityIOMachine;
//import reika.rotarycraft.blockentities.Farming.BlockEntityMobHarvester;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraftforge.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.auxiliary.EnchantmentRenderer;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.blockentities.farming.BlockEntityMobHarvester;
//import reika.rotarycraft.models.ModelHarvester;
//
//@SideOnly(Dist.CLIENT)
//public class RenderHarvester extends RotaryTERenderer {
//
//    private ModelHarvester HarvesterModel = new ModelHarvester();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityMobHarvesterAt(BlockEntityMobHarvester tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelHarvester var14;
//        var14 = HarvesterModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/harvestertex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 0;     //used to rotate the model about metadata
//
//        float var13;
//
//        var14.renderAll(stack, tile, null);
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
//            this.renderBlockEntityMobHarvesterAt((BlockEntityMobHarvester) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(tile, par2, par4, par6);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            ReikaAABBHelper.renderAABB(((BlockEntityMobHarvester) tile).getBox(), par2, par4, par6, tile.xCoord, tile.yCoord, tile.zCoord, ((BlockEntityIOMachine) tile).iotick, 255, 127, 0, true);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            this.renderLaser((BlockEntityMobHarvester) tile, par2, par4, par6);
//            if (((BlockEntityMobHarvester) tile).getEnchantmentHandler().hasEnchantments())
//                //EnchantmentRenderer.renderShine(0, 0, 0, par2, par4, par6);
//                EnchantmentRenderer.renderGlint(tile, HarvesterModel, null, par2, par4, par6);
//        } else if (!tile.hasLevel()) {
//            if (((BlockEntityMobHarvester) tile).getEnchantmentHandler().hasEnchantments())
//                EnchantmentRenderer.renderGlint(tile, HarvesterModel, null, par2, par4, par6);
//        }
//    }
//
//    private void renderLaser(BlockEntityMobHarvester harv, double par2, double par4, double par6) {
//        ReikaRenderHelper.prepareGeoDraw(true);
//        if (harv.laser) {
//            ReikaAABBHelper.renderAABB(harv.getLaser(), par2, par4, par6, harv.xCoord, harv.yCoord, harv.zCoord, -960, 255, 0, 0, false);
//            ReikaAABBHelper.renderAABB(harv.getLaser().expand(0.125, 0.001, 0.125), par2, par4, par6, harv.xCoord, harv.yCoord, harv.zCoord, -192, 255, 128, 128, false);
//        }
//        ReikaRenderHelper.exitGeoDraw();
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "harvestertex.png";
//    }
//}
