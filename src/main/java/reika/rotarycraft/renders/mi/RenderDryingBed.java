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
//import reika.dragonapi.interfaces.Item.AnimatedSpritesheet;
//import reika.dragonapi.interfaces.Item.IndexedItemSprites;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.blockentities.processing.BlockEntityDryingBed;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.ItemRenderer;
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
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.models.ModelDryingBed;
//
//public class RenderDryingBed extends RotaryTERenderer {
//
//    private final ModelDryingBed model = new ModelDryingBed();
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "dryingbedtex.png";
//    }
//
//    public void renderBlockEntityDryingBedAt(BlockEntityDryingBed tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        ModelDryingBed var14;
//        var14 = model;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/dryingbedtex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 0;
//        float var13;
//
//        var14.renderAll(stack, tile, null);
//
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        BlockEntityDryingBed te = (BlockEntityDryingBed) tile;
//        if (this.doRenderModel(te))
//            this.renderBlockEntityDryingBedAt(te, par2, par4, par6, par8);
//        stack.pushPose();
//        stack.translate(par2, par4, par6);
//        if (te.isInWorld()) {
//            this.renderItem(te);
//        }
//        if (te.isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            this.renderFluid(te);
//        }
//        stack.popPose();
//    }
//
//    private void renderItem(BlockEntityDryingBed te) {
//        ItemStack is = te.getStackInSlot(0);
//        if (is != null) {
//            float thick = 0.0625F;
//            Tesselator tess = Tesselator.getInstance();
//BufferBuilder v5 = tess.getBuilder();
//            //int col = 0;
//            //int row = 0;
//            //ItemRenderer.renderItemIn2D(v5, 0.0625F+0.0625F*col, 0.0625F*row, 0.0625F*col, 0.0625F+0.0625F*row, 256, 256, thick);
//            RenderSystem.enableBlend();
//            //GL11.glDisable(GL11.GL_ALPHA_TEST);
//            //GL11.glDisable(GL11.GL_CULL_FACE);
//            GL11.glColor4f(1, 1, 1, 1.2F * te.progress / 400F);
//            GL11.glColor4f(1, 1, 1, 1);
//
//            IItemRenderer iir = MinecraftForgeClient.getItemRenderer(is, ItemRenderType.INVENTORY);
//            Item item = is.getItem();
//            if (item instanceof IndexedItemSprites) {
//                IndexedItemSprites iis = (IndexedItemSprites) item;
//                ReikaTextureHelper.bindTexture(iis.getTextureReferenceClass(), iis.getTexture(is));
//                int index = iis.getItemSpriteIndex(is);
//                int row = index / 16;
//                int col = index % 16;
//
//                if (item instanceof AnimatedSpritesheet) {
//                    AnimatedSpritesheet a = (AnimatedSpritesheet) item;
//                    if (a.useAnimatedRender(is)) {
//                        col = a.getColumn(is);
//                        int offset = (int) ((System.currentTimeMillis() / 32 / a.getFrameSpeed(is)) % a.getFrameCount(is));
//                        row = a.getBaseRow(is) + offset;
//                    }
//                }
//
//                float u = col / 16F;
//                float v = row / 16F;
//
//                double b = 0.25;
//                double dx = 0.125;
//                double dz = 0.905;
//                double dy = 0.965 - te.progress * 0.00005;
//                double s = 0.8;
//                stack.pushPose();
//                //GL11.glRotated(ang, 1, 0, 0);
//                stack.translate(dx, dy, dz);
//                //stack.translate(0, b, 0);
//                stack.mulPose(new Quaternion(-90, 1, 0, 0);
//                //stack.translate(0, -b, 0);
//                stack.scale(s, s, s);
//                if (Minecraft.getInstance().gameSettings.fancyGraphics)
//                    ItemRenderer.renderItemIn2D(v5, 0.0625F * col, 0.0625F * row, 0.0625F + 0.0625F * col, 0.0625F + 0.0625F * row, 256, 256, thick);
//                else {
//                    v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//                    v5.addVertexWithUV(0, 0, 0, 0.0625F * col, 0.0625F + 0.0625F * row);
//                    v5.addVertexWithUV(1, 0, 0, 0.0625F + 0.0625F * col, 0.0625F + 0.0625F * row);
//                    v5.addVertexWithUV(1, 1, 0, 0.0625F + 0.0625F * col, 0.0625F * row);
//                    v5.addVertexWithUV(0, 1, 0, 0.0625F * col, 0.0625F * row);
//                    v5.end();
//                }
//                stack.popPose();
//            } else if (iir != null) {
//                ;//iir.renderItem(ItemRenderType.INVENTORY, is, new RenderBlocks());
//            } else {
//                if (ReikaItemHelper.isBlock(is))
//                    ReikaTextureHelper.bindTerrainTexture();
//                else
//                    ReikaTextureHelper.bindItemTexture();
//                IIcon ico = item.getIcon(is, MinecraftForgeClient.getRenderPass());
//                if (ico == null)
//                    return;
//                float u = ico.getMinU();
//                float v = ico.getMinV();
//                float du = ico.getMaxU();
//                float dv = ico.getMaxV();
//
//                double b = 0.65;
//                double dx = 0.1;
//                double dz = 0.125;
//                double dy = 0.925 - te.progress * 0.00005;
//                double s = 0.8;
//                stack.pushPose();
//                //GL11.glRotated(ang, 1, 0, 0);
//                stack.translate(dx, dy, dz);
//                //stack.translate(0, b, 0);
//                stack.mulPose(new Quaternion(90, 1, 0, 0);
//                //stack.translate(0, -b, 0);
//                stack.scale(s, s, s);
//                if (Minecraft.getInstance().gameSettings.fancyGraphics)
//                    ItemRenderer.renderItemIn2D(v5, u, v, du, dv, 256, 256, thick);
//                else {
//                    v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//                    v5.addVertexWithUV(0, 1, 0, u, v);
//                    v5.addVertexWithUV(1, 1, 0, du, v);
//                    v5.addVertexWithUV(1, 0, 0, du, dv);
//                    v5.addVertexWithUV(0, 0, 0, u, dv);
//                    v5.end();
//                }
//                stack.popPose();
//            }
//            RenderSystem.disableBlend();
//            //GL11.glEnable(GL11.GL_CULL_FACE);
//            //GL11.glEnable(GL11.GL_ALPHA_TEST);
//
//        }
//    }
//
//    private void renderFluid(BlockEntityDryingBed tile) {
//        Fluid f = tile.getFluid();
//        if (f != null) {
//            ReikaTextureHelper.bindTerrainTexture();
//            Tesselator tess = Tesselator.getInstance();
//BufferBuilder v5 = tess.getBuilder();
//            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//            float u = ico.getMinU();
//            float v = ico.getMinV();
//            float du = ico.getMaxU();
//            float dv = ico.getMaxV();
//            int l = tile.getLevel();
//            if (!f.equals(FluidRegistry.LAVA)) {
//                RenderSystem.enableBlend();
//            }
//            double h = l > 0 ? 0.8125 + l * 0.125 / tile.getCapacity() : 0.5;
//            if (f.getLuminosity() > 0 && tile.hasLevel())
//                ReikaRenderHelper.disableLighting();
//            v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//            v5.setColorOpaque_I(0xffffff);
//            v5.addVertexWithUV(0 + 0.0625, h, 1 - 0.0625, u, v);
//            v5.addVertexWithUV(1 - 0.0625, h, 1 - 0.0625, du, v);
//            v5.addVertexWithUV(1 - 0.0625, h, 0 + 0.0625, du, dv);
//            v5.addVertexWithUV(0 + 0.0625, h, 0 + 0.0625, u, dv);
//            v5.end();
//            ReikaRenderHelper.enableLighting();
//            RenderSystem.disableBlend();
//        }
//    }
//
//}
