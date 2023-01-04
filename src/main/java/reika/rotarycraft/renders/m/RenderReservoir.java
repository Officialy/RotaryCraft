/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders.m;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import reika.dragonapi.instantiable.data.DynamicAverage;
import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.models.ReservoirModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderReservoir extends RotaryTERenderer<BlockEntityReservoir> {

    private static final DynamicAverage average = new DynamicAverage();
    private final ReservoirModel reservoirModel;

    public RenderReservoir(BlockEntityRendererProvider.Context context) {
        reservoirModel = new ReservoirModel(context.bakeLayer(RotaryModelLayers.RESERVOIR));
    }

    public void renderBlockEntityReservoirAt(PoseStack stack, BlockEntityReservoir tile, MultiBufferSource bufferSource, int pPackedLight) {
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        stack.mulPose(Axis.YN.rotationDegrees(90.0F));
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(ReservoirModel.TEXTURE_LOCATION));
        if (tile.isInWorld()) {
            for (int i = 2; i < 6; i++) {
                if (!tile.isConnectedOnSide(dirs[i])) {
                    reservoirModel.renderSide(stack, vertexconsumer, pPackedLight, dirs[i]);
                }
            }
            reservoirModel.renderSide(stack, vertexconsumer, pPackedLight, Direction.DOWN);
        } else {
            reservoirModel.renderAll(stack, vertexconsumer, pPackedLight, tile, null);
        }

//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void render(BlockEntityReservoir tile, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
//        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        if (this.doRenderModel(pPoseStack, tile)) {
            this.renderBlockEntityReservoirAt(pPoseStack, tile, pBufferSource, pPackedLight);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            if (tile.isCovered) {
                this.renderCover(pPoseStack, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
            }
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
//        if (MinecraftForgeClient.getRenderPass() == 1 || !(tile).isInWorld()) {
        this.renderLiquid(pPoseStack, tile, pBufferSource, pPackedLight);
//        }
        //GL11.glPopAttrib();
    }

    private void renderCover(PoseStack stack, BlockEntityReservoir tr, double par2, double par4, double par6) {
        Matrix4f m = stack.last().pose();
        float u = 0;//todo ico.getMinU();
        float v = 0;//todo ico.getMinV();
        float du = 1;//todo  ico.getMaxU();
        float dv = 1;//todo  ico.getMaxV();
        float h = 0.99F;
        float dd = 0;//.03125F;
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder v5 = tess.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, new ResourceLocation("textures/block/glass.png"));
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
        v5.vertex(m, dd, h, 1 - dd).uv(u, dv).color(255).normal(0, 1, 0).endVertex();
        v5.vertex(m, 1 - dd, h, 1 - dd).uv(du, dv).color(255).normal(0, 1, 0).endVertex();
        v5.vertex(m, 1 - dd, h, dd).uv(du, v).color(255).normal(0, 1, 0).endVertex();
        v5.vertex(m, dd, h, dd).uv(u, v).color(255).normal(0, 1, 0).endVertex();
        tess.end();
        RenderSystem.disableDepthTest();
    }

    private void renderLiquid(PoseStack stack, BlockEntity tile, MultiBufferSource bufferSource, int pPackedLight) {
        Matrix4f m = stack.last().pose();
        BlockEntityReservoir tr = (BlockEntityReservoir) tile;
        Fluid f = tr.getFluid().getFluid();
        if (f != null) {
            RenderSystem.enableDepthTest();
            RenderSystem.defaultBlendFunc();
            if (!f.equals(Fluids.LAVA)) {
                RenderSystem.enableBlend();
            }
//            ReikaLiquidRenderer.bindFluidTexture(f);
//            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//            if (f == Fluids.WATER.getFlowing() && tile.getLevel() != null) {
//                ico = LiquidBlockIconEvent.fire(Blocks.WATER, tile.getLevel(), tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), 1);
//            }
            double h = this.getFillAmount(tr);
            if (f.getFluidType().getLightLevel() > 0 && tile.hasLevel())
                ReikaRenderHelper.disableLighting();


            BlockEntityReservoir tr1 = null;
            BlockEntityReservoir tr2 = null;
            BlockEntityReservoir tr3 = null;
            BlockEntityReservoir tr4 = null;
            BlockEntityReservoir tr6 = null;
            BlockEntityReservoir tr7 = null;
            BlockEntityReservoir tr8 = null;
            BlockEntityReservoir tr9 = null;

            if (tr.hasNearbyReservoir(1)) {
                BlockEntity teb = tr.getLevel().getBlockEntity(new BlockPos(tr.getBlockPos().getX() - 1, tr.getBlockPos().getY(), tr.getBlockPos().getZ() + 1));
                if (teb instanceof BlockEntityReservoir) {
                    tr1 = (BlockEntityReservoir) teb;
                    if (tr1.getFluid().getFluid() != f)
                        tr1 = null;
                }
            }
            if (tr.hasNearbyReservoir(2)) {
                BlockEntity teb = tr.getLevel().getBlockEntity(new BlockPos(tr.getBlockPos().getX(), tr.getBlockPos().getY(), tr.getBlockPos().getZ() + 1));
                if (teb instanceof BlockEntityReservoir) {
                    tr2 = (BlockEntityReservoir) teb;
                    if (tr2.getFluid().getFluid() != f)
                        tr2 = null;
                }
            }
            if (tr.hasNearbyReservoir(3)) {
                BlockEntity teb = tr.getLevel().getBlockEntity(new BlockPos(tr.getBlockPos().getX() + 1, tr.getBlockPos().getY(), tr.getBlockPos().getZ() + 1));
                if (teb instanceof BlockEntityReservoir) {
                    tr3 = (BlockEntityReservoir) teb;
                    if (tr3.getFluid().getFluid() != f)
                        tr3 = null;
                }
            }
            if (tr.hasNearbyReservoir(4)) {
                BlockEntity teb = tr.getLevel().getBlockEntity(new BlockPos(tr.getBlockPos().getX() - 1, tr.getBlockPos().getY(), tr.getBlockPos().getZ()));
                if (teb instanceof BlockEntityReservoir) {
                    tr4 = (BlockEntityReservoir) teb;
                    if (tr4.getFluid().getFluid() != f)
                        tr4 = null;
                }
            }
            if (tr.hasNearbyReservoir(6)) {
                BlockEntity teb = tr.getLevel().getBlockEntity(new BlockPos(tr.getBlockPos().getX() + 1, tr.getBlockPos().getY(), tr.getBlockPos().getZ()));
                if (teb instanceof BlockEntityReservoir) {
                    tr6 = (BlockEntityReservoir) teb;
                    if (tr6.getFluid().getFluid() != f)
                        tr6 = null;
                }
            }
            if (tr.hasNearbyReservoir(7)) {
                BlockEntity teb = tr.getLevel().getBlockEntity(new BlockPos(tr.getBlockPos().getX() - 1, tr.getBlockPos().getY(), tr.getBlockPos().getZ() - 1));
                if (teb instanceof BlockEntityReservoir) {
                    tr7 = (BlockEntityReservoir) teb;
                    if (tr7.getFluid().getFluid() != f)
                        tr7 = null;
                }
            }
            if (tr.hasNearbyReservoir(8)) {
                BlockEntity teb = tr.getLevel().getBlockEntity(new BlockPos(tr.getBlockPos().getX(), tr.getBlockPos().getY(), tr.getBlockPos().getZ() - 1));
                if (teb instanceof BlockEntityReservoir) {
                    tr8 = (BlockEntityReservoir) teb;
                    if (tr8.getFluid().getFluid() != f)
                        tr8 = null;
                }
            }
            if (tr.hasNearbyReservoir(9)) {
                BlockEntity teb = tr.getLevel().getBlockEntity(new BlockPos(tr.getBlockPos().getX() + 1, tr.getBlockPos().getY(), tr.getBlockPos().getZ() - 1));
                if (teb instanceof BlockEntityReservoir) {
                    tr9 = (BlockEntityReservoir) teb;
                    if (tr9.getFluid().getFluid() != f)
                        tr9 = null;
                }
            }

            average.clear();
            average.add(h);
            if (tr1 != null)
                average.add(this.getFillAmount(tr1));
            if (tr2 != null)
                average.add(this.getFillAmount(tr2));
            if (tr4 != null)
                average.add(this.getFillAmount(tr4));
            float hmp = (float) average.getAverage();

            average.clear();
            average.add(h);
            if (tr3 != null)
                average.add(this.getFillAmount(tr3));
            if (tr2 != null)
                average.add(this.getFillAmount(tr2));
            if (tr6 != null)
                average.add(this.getFillAmount(tr6));
            float hpp = (float) average.getAverage();

            average.clear();
            average.add(h);
            if (tr8 != null)
                average.add(this.getFillAmount(tr8));
            if (tr9 != null)
                average.add(this.getFillAmount(tr9));
            if (tr6 != null)
                average.add(this.getFillAmount(tr6));
            float hpm = (float) average.getAverage();

            average.clear();
            average.add(h);
            if (tr7 != null)
                average.add(this.getFillAmount(tr7));
            if (tr8 != null)
                average.add(this.getFillAmount(tr8));
            if (tr4 != null)
                average.add(this.getFillAmount(tr4));
            float hmm = (float) average.getAverage();
            IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(f.getFluidType());

            if (props.getStillTexture() != null) {
                float u = 0;
                float v = 0;
                float du = 1f;
                float dv = 0.05f;

                Tesselator tess = Tesselator.getInstance();
                BufferBuilder v5 = tess.getBuilder();
                RenderSystem.setShader(GameRenderer::getPositionTexColorNormalShader);
                RenderSystem.setShaderTexture(0, new ResourceLocation(props.getStillTexture().getNamespace(), "textures/" + props.getStillTexture().getPath() + ".png"));
                v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
                v5.vertex(m,0, hmp, 1).uv(u, dv).color(tr.getFluidRenderColor()).normal(0, 1, 0).endVertex();
                v5.vertex(m,1, hpp, 1).uv(du, dv).color(tr.getFluidRenderColor()).normal(0, 1, 0).endVertex();
                v5.vertex(m,1, hpm, 0).uv(du, v).color(tr.getFluidRenderColor()).normal(0, 1, 0).endVertex();
                v5.vertex(m,0, hmm, 0).uv(u, v).color(tr.getFluidRenderColor()).normal(0, 1, 0).endVertex();
                tess.end();
                if (tile.hasLevel())
                    ReikaRenderHelper.enableLighting();
            }
        }
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

    private double getFillAmount(BlockEntityReservoir tr) {
        return 0.0625 + 14D / 16D * tr.getFluidLevel() / BlockEntityReservoir.CAPACITY;
    }
}
