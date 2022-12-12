///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders.dmi;
//
//import reika.dragonapi.interfaces.Item.IndexedItemSprites;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.base.BlockItemPlacer;
//import reika.rotarycraft.blockentities.BlockEntityGrindstone;
//import com.mojang.blaze3d.vertex.BufferBuilder;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.blaze3d.vertex.Tesselator;
//import com.mojang.math.Quaternion;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.renderer.ItemRenderer;
//import net.minecraft.client.renderer.Tesselator;
//import net.minecraft.item.Item;
//import net.minecraft.world.item.ItemStack;
//
//import net.minecraftforge.client.IItemRenderer;
//import net.minecraftforge.client.IItemRenderer.ItemRenderType;
//import net.minecraftforge.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.blockentities.BlockEntityGrindstone;
//import reika.rotarycraft.models.Animated.ModelGrindstone;
//import reika.rotarycraft.models.animated.ModelGrindstone;
//
//public class RenderGrindstone extends RotaryTERenderer {
//
//    private ModelGrindstone GrindstoneModel = new ModelGrindstone();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityGrindstoneAt(PoseStack stack, BlockEntityGrindstone tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelGrindstone var14;
//        var14 = GrindstoneModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/grindstonetex.png");
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
//                    var11 = 0;
//                    break;
//                case 1:
//                    var11 = 90;
//                    break;
//            }
//            stack.mulPose(new Quaternion(var11, 0.0F, 1.0F, 0.0F));
//        }
//
//        float var13;
//
//        var14.renderAll(stack, tile, null, tile.phi);
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
//            this.renderBlockEntityGrindstoneAt((BlockEntityGrindstone) tile, par2, par4, par6, par8);
//        this.renderTool((BlockEntityGrindstone) tile, par2, par4, par6);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(tile, par2, par4, par6);
//    }
//
//    private void renderTool(BlockEntityGrindstone tile, double par2, double par4, double par6) {
//        ItemStack is = tile.getStackInSlot(0);
//        if (is == null)
//            return;
//        if (ReikaItemHelper.isBlock(is) || is.getItem() instanceof BlockItemPlacer)
//            return;
//
//        double in = 0.125;
//        double off = 0;
//        float ang = 12;
//
//        float var11 = 0;
//        switch (tile.getBlockMetadata()) {
//            case 0:
//                var11 = 0;
//                ang = -12;
//                break;
//            case 1:
//                var11 = 90;
//                off = 0.0625;
//                break;
//        }
//
//
//        stack.translate(par2, par4, par6);
//        stack.translate(0, 0, off);
//
//        stack.mulPose(new Quaternion(var11 - 90, 0.0F, 1.0F, 0.0F);
//
//        Tesselator tess = Tesselator.getInstance();
//        BufferBuilder v5 = tess.getBuilder();
//        float thick = 0.0625F;
//        Item item = is.getItem();
//        IItemRenderer iir = MinecraftForgeClient.getItemRenderer(is, ItemRenderType.INVENTORY);
//        if (item instanceof IndexedItemSprites && !(item instanceof BlockItemPlacer)) {
//            IndexedItemSprites iis = (IndexedItemSprites) item;
//            ReikaTextureHelper.bindTexture(iis.getTextureReferenceClass(), iis.getTexture(is));
//            int index = iis.getItemSpriteIndex(is);
//            int row = index / 16;
//            int col = index % 16;
//
//            float u = col / 16F;
//            float v = row / 16F;
//
//            double b = 0.25;
//            double dy = 0.5;
//            GL11.glRotated(ang, 1, 0, 0);
//            stack.translate(0, dy, 0);
//            stack.translate(0, b, 0);
//            stack.mulPose(new Quaternion(-45, 0, 0, 1);
//            stack.translate(0, -b, 0);
//            ItemRenderer.renderItemIn2D(v5, 0.0625F + 0.0625F * col, 0.0625F * row, 0.0625F * col, 0.0625F + 0.0625F * row, 256, 256, thick);
//            stack.translate(0, b, 0);
//            stack.mulPose(new Quaternion(45, 0, 0, 1);
//            stack.translate(0, -b, 0);
//            stack.translate(0, -dy, 0);
//            GL11.glRotated(-ang, 1, 0, 0);
//        } else if (iir != null) {
//            ;//iir.renderItem(ItemRenderType.INVENTORY, is, new RenderBlocks());
//        } else {
//            if (ReikaItemHelper.isBlock(is))
//                ReikaTextureHelper.bindTerrainTexture();
//            else
//                ReikaTextureHelper.bindItemTexture();
//            IIcon ico = item.getIcon(is, MinecraftForgeClient.getRenderPass());
//            if (ico == null)
//                return;
//            float u = ico.getMinU();
//            float v = ico.getMinV();
//            float du = ico.getMaxU();
//            float dv = ico.getMaxV();
//
//            double b = 0.65;
//            double dy = -0.25;
//            GL11.glRotated(ang, 1, 0, 0);
//            stack.translate(0, dy, 0);
//            stack.translate(0, b, 0);
//            stack.mulPose(new Quaternion(45, 0, 0, 1);
//            stack.translate(0, -b, 0);
//            ItemRenderer.renderItemIn2D(v5, u, v, du, dv, 256, 256, thick);
//            stack.translate(0, b, 0);
//            stack.mulPose(new Quaternion(-45, 0, 0, 1);
//            stack.translate(0, -b, 0);
//            stack.translate(0, -dy, 0);
//            GL11.glRotated(-ang, 1, 0, 0);
//        }
//
//        stack.mulPose(new Quaternion(-var11 + 90, 0.0F, 1.0F, 0.0F);
//
//        stack.translate(0, 0, -off);
//        stack.translate(-par2, -par4, -par6);
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "grindstonetex.png";
//    }
//}
