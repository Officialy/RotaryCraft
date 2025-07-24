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
//import reika.rotarycraft.blockentities.auxiliary.BlockEntityFillingStation;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.renderer.Tesselator;
//import net.minecraft.item.Item;
//import net.minecraft.world.item.ItemStack;
//
//import net.neoforged.client.IItemRenderer;
//import net.neoforged.client.IItemRenderer.ItemRenderType;
//import net.neoforged.client.MinecraftForgeClient;
//import net.neoforged.fluids.Fluid;
//import net.neoforged.fluids.FluidRegistry;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.ModelFillingStation;
//
//public class RenderFillingStation extends RotaryTERenderer {
//
//    private ModelFillingStation FillingStationModel = new ModelFillingStation();
//    //private ModelFillingStationV FillingStationModelV = new ModelFillingStationV();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityFillingStationAt(BlockEntityFillingStation tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelFillingStation var14;
//        var14 = FillingStationModel;
//        //ModelFillingStationV var15;
//        //var14 = this.FillingStationModelV;
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/fillingtex.png");
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
//            stack.mulPose((float) var11 - 90, 0.0F, 1.0F, 0.0F);
//        }
//
//        var14.renderAll(stack, tile, null, -tile.phi);
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
//            this.renderBlockEntityFillingStationAt((BlockEntityFillingStation) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            this.renderLiquid(tile, par2, par4, par6);
//            IORenderer.renderIO(tile, par2, par4, par6);
//        }
//        this.renderItem(tile, par2, par4, par6);
//    }
//
//    private void renderLiquid(BlockEntity tile, double par2, double par4, double par6) {
//        stack.translate(par2, par4, par6);
//        BlockEntityFillingStation tr = (BlockEntityFillingStation) tile;
//        double dx = 0;
//        double dz = 0;
//        double ddx = 0;
//        double ddz = 0;
//        switch (tr.getBlockMetadata()) {
//            case 0:
//                dx = 0.25;
//                break;
//            case 1:
//                ddx = 0.25;
//                break;
//            case 2:
//                dz = 0.25;
//                break;
//            case 3:
//                ddz = 0.25;
//                break;
//        }
//        if (!tr.isEmpty() && tr.isInWorld()) {
//            Fluid f = tr.getFluid();
//            if (!f.equals(FluidRegistry.LAVA)) {
//                RenderSystem.enableBlend();
//            }
//            ReikaLiquidRenderer.bindFluidTexture(f);
//            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//            float u = ico.getMinU();
//            float v = ico.getMinV();
//            float du = ico.getMaxU();
//            float dv = ico.getMaxV();
//            double h = 0.0625 + 14D / 16D * tr.getLevel() / tr.CAPACITY;
//            Tesselator tess = Tesselator.getInstance();
//BufferBuilder v5 = tess.getBuilder();
//            if (f.getLuminosity() > 0)
//                ReikaRenderHelper.disableLighting();
//            v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//            v5.normal(0, 1, 0);
//            int clr = 0xffffffff;
//            if (f.canBePlacedInWorld()) {
//                clr = f.getBlock().colorMultiplier(tr.level, tr.xCoord * 2, tr.yCoord * 2, tr.zCoord * 2);
//            }
//            v5.setColorOpaque_I(clr);
//
//            v5.addVertexWithUV(dx + 0, h, -ddz + 1, u, dv);
//            v5.addVertexWithUV(-ddx + 1, h, -ddz + 1, du, dv);
//            v5.addVertexWithUV(-ddx + 1, h, dz + 0, du, v);
//            v5.addVertexWithUV(dx + 0, h, dz + 0, u, v);
//            v5.end();
//            ReikaRenderHelper.enableLighting();
//        }
//        stack.translate(-par2, -par4, -par6);
//        RenderSystem.disableBlend();
//    }
//
//    private void renderItem(BlockEntity tile, double par2, double par4, double par6) {
//        BlockEntityFillingStation fs = (BlockEntityFillingStation) tile;
//        if (!fs.isInWorld())
//            return;
//        ItemStack is = fs.getItemForRender();
//        if (is == null)
//            return;
//
//        double in = 0.125;
//        double xoff = 0;
//        double zoff = 0;
//
//        float var11 = 0;
//        switch (tile.getBlockMetadata()) {
//            case 0:
//                var11 = 180;
//                break;
//            case 1:
//                var11 = 0;
//                xoff = 1;
//                zoff = -1;
//                break;
//            case 2:
//                var11 = 270;
//                in = -in;
//                break;
//            case 3:
//                var11 = 90;
//                xoff = 1;
//                zoff = 1;
//                in = -in;
//                break;
//        }
//
//        stack.pushPose();
//        stack.translate(par2, par4, par6);
//
//        stack.mulPose(new Quaternion(var11 - 90, 0.0F, 1.0F, 0.0F);
//
//        stack.translate(xoff, 0, zoff);
//        GL11.glDisable(GL11.GL_CULL_FACE);
//
//        Tesselator tess = Tesselator.getInstance();
//BufferBuilder v5 = tess.getBuilder();
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//
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
//            float du = u + 0.0625F;
//            float dv = v + 0.0625F;
//
//            v5.addVertexWithUV(0, 0, in, u, dv);
//            v5.addVertexWithUV(-1, 0, in, du, dv);
//            v5.addVertexWithUV(-1, 1, in, du, v);
//            v5.addVertexWithUV(0, 1, in, u, v);
//        } else if (iir != null) {
//            ;//iir.renderItem(ItemRenderType.INVENTORY, is, new RenderBlocks());
//        } else {
//            if (ReikaItemHelper.isBlock(is))
//                ReikaTextureHelper.bindTerrainTexture();
//            else
//                ReikaTextureHelper.bindItemTexture();
//            IIcon ico = item.getIcon(is, MinecraftForgeClient.getRenderPass());
//            float u = ico.getMinU();
//            float v = ico.getMinV();
//            float du = ico.getMaxU();
//            float dv = ico.getMaxV();
//
//            v5.addVertexWithUV(0, 0, in, u, dv);
//            v5.addVertexWithUV(-1, 0, in, du, dv);
//            v5.addVertexWithUV(-1, 1, in, du, v);
//            v5.addVertexWithUV(0, 1, in, u, v);
//        }
//
//        v5.end();
//        GL11.glEnable(GL11.GL_CULL_FACE);
//        stack.popPose();
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "fillingtex.png";
//    }
//}
