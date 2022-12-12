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
//import reika.rotarycraft.blockentities.processing.BlockEntityBigFurnace;
//import com.mojang.blaze3d.vertex.BufferBuilder;
//import com.mojang.blaze3d.vertex.Tesselator;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.renderer.Tesselator;
//
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.client.MinecraftForgeClient;
//import net.minecraftforge.fluids.Fluid;
//import net.minecraftforge.fluids.FluidRegistry;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.blockentities.processing.BlockEntityBigFurnace;
//import reika.rotarycraft.models.ModelBigFurnace;
//
//@SideOnly(Dist.CLIENT)
//public class RenderBigFurnace extends RotaryTERenderer {
//
//    private ModelBigFurnace BigFurnaceModel = new ModelBigFurnace();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityBigFurnaceAt(BlockEntityBigFurnace tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelBigFurnace var14;
//        var14 = BigFurnaceModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/bigfurnace.png");
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
//            this.renderBlockEntityBigFurnaceAt((BlockEntityBigFurnace) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//        }
//        if (MinecraftForgeClient.getRenderPass() == 0) {
//            this.renderLiquid((BlockEntityBigFurnace) tile, par2, par4, par6);
//        }
//    }
//
//    private void renderLiquid(BlockEntityBigFurnace tr, double par2, double par4, double par6) {
//        stack.translate(par2, par4, par6);
//        if (!tr.isEmpty() && tr.isInWorld()) {
//            Fluid f = FluidRegistry.LAVA;
//            ReikaLiquidRenderer.bindFluidTexture(f);
//            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//            float u = ico.getMinU();
//            float v = ico.getMinV();
//            float du = ico.getMaxU();
//            float dv = ico.getMaxV();
//            double h = 0.0625 + 14D / 16D * tr.getLevel() / tr.getCapacity();
//            Tesselator tess = Tesselator.getInstance();
//            BufferBuilder v5 = tess.getBuilder();
//            ReikaRenderHelper.disableLighting();
//            v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//            v5.normal(0, 1, 0);
//            v5.addVertexWithUV(0, h, 1, u, dv);
//            v5.addVertexWithUV(1, h, 1, du, dv);
//            v5.addVertexWithUV(1, h, 0, du, v);
//            v5.addVertexWithUV(0, h, 0, u, v);
//            v5.end();
//            ReikaRenderHelper.enableLighting();
//        }
//        stack.translate(-par2, -par4, -par6);
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "bigfurnace.png";
//    }
//}
