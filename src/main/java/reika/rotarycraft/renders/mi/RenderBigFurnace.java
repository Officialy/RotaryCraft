/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders.mi;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.blockentities.processing.BlockEntityLavaSmeltery;

import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.models.BigFurnaceModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderBigFurnace extends RotaryTERenderer<BlockEntityLavaSmeltery> {

    private BigFurnaceModel modelBigFurnace;

    public RenderBigFurnace(BlockEntityRendererProvider.Context ctx) {
        modelBigFurnace = new BigFurnaceModel(ctx.bakeLayer(RotaryModelLayers.BIG_FURNACE));
    }

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntityBigFurnaceAt(BlockEntityLavaSmeltery tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        modelBigFurnace.renderToBuffer(stack, bufferSource.getBuffer(modelBigFurnace.renderType(new ResourceLocation(RotaryCraft.MODID, "/textures/blockentitytex/bigfurnace.png"))), light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.popPose();
    }

    @Override
    public void render(BlockEntityLavaSmeltery tile, float p_112308_, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay) {
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntityBigFurnaceAt( tile, stack, bufferSource, light);
        if (( tile).isInWorld()){// && MinecraftForgeClient.getRenderPass() == 1) {
            IORenderer.renderIO(stack, bufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
        }
//        if (MinecraftForgeClient.getRenderPass() == 0) {
            this.renderLiquid(stack, tile);
//        }
    }

    private void renderLiquid(PoseStack stack, BlockEntityLavaSmeltery tr) {
//        stack.translate(par2, par4, par6);
        if (!tr.isEmpty() && tr.isInWorld()) {
            Fluid f = Fluids.LAVA;
            IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(f.getFluidType());
//            ReikaLiquidRenderer.bindFluidTexture(f);
//            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
            float u = 0;//ico.getMinU();
            float v = 0;//ico.getMinV();
            float du = 1;//ico.getMaxU();
            float dv = 1;//ico.getMaxV();
            double h = 0.0625 + 14D / 16D * tr.getLiquidLevel() / tr.getCapacity();
            Tesselator tess = Tesselator.getInstance();
            BufferBuilder v5 = tess.getBuilder();
            ReikaRenderHelper.disableLighting();
            Minecraft.getInstance().textureManager.bindForSetup(props.getFlowingTexture());
            v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
            v5.vertex(0, h, 1).uv(u, dv).normal(0, 1, 0).color(0, 0, 0, 0).endVertex();
            v5.vertex(1, h, 1).uv(du, dv).normal(0, 1, 0).color(0, 0, 0, 0).endVertex();
            v5.vertex(1, h, 0).uv(du, v).normal(0, 1, 0).color(0, 0, 0, 0).endVertex();
            v5.vertex(0, h, 0).uv(u, v).normal(0, 1, 0).color(0, 0, 0, 0).endVertex();
            v5.end();
            ReikaRenderHelper.enableLighting();
        }
//        stack.translate(-par2, -par4, -par6);
    }

}
