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
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.joml.Matrix4f;
import reika.dragonapi.instantiable.data.DynamicAverage;
import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.models.ReservoirModel;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderReservoir extends RotaryTERenderer<BlockEntityReservoir> {

    private static final DynamicAverage average = new DynamicAverage();
    private final ReservoirModel reservoirModel;
    /** Block-atlas sprite lookup, needed to draw the fluid surface with its actual still texture
     *  instead of a flat colour quad. Captured at construction because {@link BlockEntityRendererProvider.Context#sprites()}
     *  is the only way to get a {@link SpriteGetter}. */
    private final SpriteGetter sprites;

    public RenderReservoir(BlockEntityRendererProvider.Context context) {
        reservoirModel = new ReservoirModel(context.bakeLayer(RotaryModelLayers.RESERVOIR));
        this.sprites = context.sprites();
    }

    public void renderBlockEntityReservoirAt(PoseStack stack, BlockEntityReservoir tile, VertexConsumer bufferSource, int pPackedLight) {
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        stack.mulPose(Axis.YN.rotationDegrees(90.0F));
        VertexConsumer vertexconsumer = bufferSource;
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

    // Legacy 1.7 signature retained as dead code; vanilla calls {@link #submit} instead.
    public void render(BlockEntityReservoir tile, float pPartialTick, PoseStack pPoseStack, VertexConsumer pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (this.doRenderModel(pPoseStack, tile)) {
            this.renderBlockEntityReservoirAt(pPoseStack, tile, pBufferSource, pPackedLight);
            if (tile.isCovered) this.renderCover(pPoseStack, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
        }
        this.renderLiquid(pPoseStack, tile, pBufferSource, pPackedLight);
    }

    @Override
    public void submit(BlockEntityRenderState state,
                       PoseStack poseStack,
                       SubmitNodeCollector collector,
                       CameraRenderState camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntityReservoir tile)) return;
        if (!this.doRenderModel(poseStack, tile)) return;

        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());
        RenderType rt = RenderTypes.entityCutout(ReservoirModel.TEXTURE_LOCATION);
        int light = state.lightCoords;
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> {
            renderBlockEntityReservoirAt(snapped, tile, vc, light);
        });

        // 26.1: proper fluid surface using the block-atlas still sprite.
        // Switched from a flat-colour quad to a UV-textured quad with sprite UVs from
        // {@link net.minecraft.client.resources.model.sprite.SpriteGetter#get}, matching the
        // {@link reika.rotarycraft.renders.PipeRenderer} pattern. The colour tint stays
        // because some fluids (water, custom mod fluids) need the texture multiplied by a
        // colour to look right — the tint goes to {@code 0xFFRRGGBB} (full alpha) so vanilla
        // sprites render at full opacity; the translucent RenderType handles blending.
        FluidStack fs = tile.getFluid();
        if (!fs.isEmpty()) {
            Fluid fluid = fs.getFluid();
            int rgba = fluidTint(fluid) | 0xFF000000; // force full alpha; RT does the blend
            // Fill height: 1/16 (bottom inset) + 14/16 * (level / capacity). Matches the
            // visible interior dimensions of the reservoir model (~0.0625 .. ~0.9375 in Y).
            double fillFrac = tile.getFluidLevel() / (double) BlockEntityReservoir.CAPACITY;
            final float y = (float) (0.0625 + (14.0 / 16.0) * fillFrac);

            // Resolve the fluid's still sprite from the block atlas. Vanilla water/lava have
            // well-known atlas IDs; for RotaryCraft fluids we map to the project's PNGs.
            TextureAtlasSprite sprite = stillSpriteFor(fluid);
            final float u  = sprite.getU0();
            final float v  = sprite.getV0();
            final float u2 = sprite.getU1();
            final float v2 = sprite.getV1();

            PoseStack snappedLiq = new PoseStack();
            snappedLiq.last().set(poseStack.last());
            final int lightCoords = light;
            RenderType fluidRT =
                    RenderTypes.entityTranslucent(
                            TextureAtlas.LOCATION_BLOCKS);
            collector.submitCustomGeometry(poseStack, fluidRT, (pose, vc2) -> {
                var p = snappedLiq.last();
                // Top face (+Y), counter-clockwise from below = clockwise from above so the
                // visible normal points up. Tile UVs roughly 1:1 with the world quad — atlas
                // sprite is 16x16 px, and the quad is 14/16 wide, so UVs span ~87.5 % of the
                // sprite which is acceptable for a continuous fluid surface.
                vc2.addVertex(p, 0.0625F, y, 0.0625F).setColor(rgba).setUv(u,  v ).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(0F, 1F, 0F);
                vc2.addVertex(p, 0.0625F, y, 0.9375F).setColor(rgba).setUv(u,  v2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(0F, 1F, 0F);
                vc2.addVertex(p, 0.9375F, y, 0.9375F).setColor(rgba).setUv(u2, v2).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(0F, 1F, 0F);
                vc2.addVertex(p, 0.9375F, y, 0.0625F).setColor(rgba).setUv(u2, v ).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(0F, 1F, 0F);
            });
        }
        // Cover overlay still TODO.
    }

    /**
     * Maps a {@link Fluid} to its still-texture sprite on the {@code minecraft:blocks} atlas.
     * Vanilla water and lava have well-known atlas IDs; RotaryCraft fluids ship their own PNGs
     * under {@code assets/rotarycraft/textures/block/}. Unknown fluids fall back to {@code water_still}
     * or {@code lava_still} based on the fluid type's temperature.
     */
    private TextureAtlasSprite stillSpriteFor(Fluid fluid) {
        Identifier id = stillTextureId(fluid);
        return sprites.get(new SpriteId(
                TextureAtlas.LOCATION_BLOCKS, id));
    }

    private static Identifier stillTextureId(Fluid fluid) {
        if (fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER)
            return Identifier.withDefaultNamespace("block/water_still");
        if (fluid == Fluids.LAVA || fluid == Fluids.FLOWING_LAVA)
            return Identifier.withDefaultNamespace("block/lava_still");
        if (fluid == RotaryFluids.JET_FUEL.get())
            return Identifier.fromNamespaceAndPath("rotarycraft", "block/fluid/jetfuel");
        if (fluid == RotaryFluids.ETHANOL.get())
            return Identifier.fromNamespaceAndPath("rotarycraft", "block/fluid/ethanol");
        if (fluid == RotaryFluids.LUBRICANT.get())
            return Identifier.fromNamespaceAndPath("rotarycraft", "block/fluid/lubricant");
        if (fluid == RotaryFluids.HSLA_FLUID.get())
            return Identifier.fromNamespaceAndPath("rotarycraft", "block/fluid/hsla_still");
        return fluid.getFluidType().getTemperature() > 500
                ? Identifier.withDefaultNamespace("block/lava_still")
                : Identifier.withDefaultNamespace("block/water_still");
    }

    /**
     * Approximate ARGB tint per fluid. Covers vanilla water/lava plus the RotaryCraft fluid set
     * (jet fuel, ethanol, lubricant, molten HSLA, etc.) with hand-picked colours that visually
     * match the legacy 1.7 still-textures. Unknown fluids fall back to a neutral cyan so the
     * surface is still visible.
     */
    private static int fluidTint(Fluid f) {
        if (f == Fluids.WATER || f == Fluids.FLOWING_WATER)
            return 0xC03050E0; // translucent blue
        if (f == Fluids.LAVA || f == Fluids.FLOWING_LAVA)
            return 0xE0E04010; // translucent orange-red
        if (f == RotaryFluids.JET_FUEL.get())
            return 0xE060A000; // dark amber
        if (f == RotaryFluids.ETHANOL.get())
            return 0xC0F0F050; // translucent yellow
        if (f == RotaryFluids.LUBRICANT.get())
            return 0xD0C08020; // viscous brown-orange
        if (f == RotaryFluids.HSLA_FLUID.get())
            return 0xF0E04010; // molten steel — closer to lava but darker
        if (f == RotaryFluids.LIQUID_NITROGEN.get())
            return 0xC0A0E0F0; // pale blue
        if (f == RotaryFluids.POISON.get())
            return 0xC0A030F0; // sickly purple
        if (f == RotaryFluids.STEAM.get())
            return 0x80E0E0E0; // misty white
        if (f == RotaryFluids.SODIUM.get())
            return 0xD0D0D050; // metallic yellow
        if (f == RotaryFluids.CHLORINE.get())
            return 0xC0C0E060; // chlorine green
        if (f == RotaryFluids.OXYGEN.get())
            return 0xA0E0F0FF; // pale icy blue
        if (f == RotaryFluids.LIQUID_AMMONIA.get()
         || f == RotaryFluids.AMMONIA.get())
            return 0xC0E0E0F0; // washed-out blue-white
        if (f == RotaryFluids.HEAVY_WATER.get())
            return 0xC02060B0; // darker blue than water
        return 0xC020A0C0;     // generic cyan fallback
    }

    private void renderCover(PoseStack stack, BlockEntityReservoir tr, double par2, double par4, double par6) {
        // TODO: Port to 26.1 rendering API (setShader + Tesselator.getBuilder() + begin() + vertex().endVertex() + end() all removed)
    }

    private void renderLiquid(PoseStack stack, BlockEntity tile, VertexConsumer bufferSource, int pPackedLight) {
        Matrix4f m = stack.last().pose();
        BlockEntityReservoir tr = (BlockEntityReservoir) tile;
        Fluid f = tr.getFluid().getFluid();
        if (f != null) {
            if (!f.equals(Fluids.LAVA)) {
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

            if (false) { // TODO 1.21.5: IClientFluidTypeExtensions.getStillTexture() removed
                float u = 0;
                float v = 0;
                float du = 1f;
                float dv = 0.05f;

                // TODO: Port fluid surface rendering to 26.1 API (setShader + Tesselator.getBuilder() + begin() + vertex().endVertex() + end() all removed)
                if (tile.hasLevel())
                    ReikaRenderHelper.enableLighting();
            }
        }
    }

    private double getFillAmount(BlockEntityReservoir tr) {
        return 0.0625 + 14D / 16D * tr.getFluidLevel() / BlockEntityReservoir.CAPACITY;
    }
}

