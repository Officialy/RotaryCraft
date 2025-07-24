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
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.rotarycraft.blockentities.Farming.BlockEntityAutoBreeder;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.block.Block;
//import net.minecraft.init.Items;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.block.Block;
//import net.neoforged.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.ModelBreeder;
//
//public class RenderBreeder extends RotaryTERenderer {
//
//    private ModelBreeder AutoBreederModel = new ModelBreeder();
//
//    //private ModelAutoBreederV AutoBreederModelV = new ModelAutoBreederV();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityAutoBreederAt(BlockEntityAutoBreeder te, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!te.isInWorld())
//            var9 = 0;
//        else {
//            Block var10 = te.getBlockType();
//            var9 = te.getBlockMetadata();
//        }
//
//        ModelBreeder var14;
//        var14 = AutoBreederModel;
//        //ModelAutoBreederV var15;
//        //var14 = this.AutoBreederModelV;
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/emptybreedertex.png");
//        if (ReikaInventoryHelper.checkForItem(Items.wheat, te))
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/breedertex.png");
//        stack.pushPose();
//
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 0;     //used to rotate the model about metadata
//
//        //float var12 = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * par8;
//        float var13;/*
//
//            var12 = 1.0F - var12;
//            var12 = 1.0F - var12 * var12 * var12;*/
//        // if (tile.getBlockMetadata() < 4)
//        var14.renderAll(te, ReikaJavaLibrary.makeListFromArray(this.getConditions(te)), 0, 0);
//
//        if (te.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityAutoBreederAt((BlockEntityAutoBreeder) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(tile, par2, par4, par6);
//    }
//
//    private Object[] getConditions(BlockEntityAutoBreeder te) {
//        Object[] vals = new Object[5];
//        vals[0] = true;
//        vals[1] = ReikaInventoryHelper.checkForItem(Items.CARROT, te);
//        vals[2] = ReikaInventoryHelper.checkForItem(Items.PORKCHOP, te);
//        vals[3] = ReikaInventoryHelper.checkForItem(Items.FISH, te);
//        vals[4] = ReikaInventoryHelper.checkForItem(Items.WHEAT_SEEDS, te);
//        return vals;
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        BlockEntityAutoBreeder tb = (BlockEntityAutoBreeder) te;
//        if (ReikaInventoryHelper.checkForItem(Items.wheat, tb))
//            return "breedertex.png";
//        return "emptybreedertex.png";
//    }
//}
