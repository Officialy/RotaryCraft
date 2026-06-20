/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
// 26.2: VertexConsumer removed from BER submission path.
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import com.mojang.math.Axis;
import org.joml.Quaternionf;
import reika.dragonapi.ModList;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.transmission.BlockEntityGearbox;
import reika.rotarycraft.models.animated.Gearbox16Model;
import reika.rotarycraft.models.animated.Gearbox4Model;
import reika.rotarycraft.models.animated.Gearbox8Model;
import reika.rotarycraft.models.animated.GearboxModel;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.RotaryModelLayers;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class RenderGearbox extends RotaryTERenderer<BlockEntityGearbox> {

    private final GearboxModel gearboxModel;
    private final Gearbox4Model gearboxModel4;
    private final Gearbox8Model gearboxModel8;
    private final Gearbox16Model gearboxModel16;

    private static Field manaIcon;

    public RenderGearbox(BlockEntityRendererProvider.Context context) {
        gearboxModel = new GearboxModel(context.bakeLayer(RotaryModelLayers.GEARBOX));
        gearboxModel4 = new Gearbox4Model(context.bakeLayer(RotaryModelLayers.GEARBOX_4));
        gearboxModel8 = new Gearbox8Model(context.bakeLayer(RotaryModelLayers.GEARBOX_8));
        gearboxModel16 = new Gearbox16Model(context.bakeLayer(RotaryModelLayers.GEARBOX_16));
    }

    static {
        if (ModList.BOTANIA.isLoaded()) {
            try {
                Class c = Class.forName("vazkii.botania.common.block.mana.BlockPool"); //todo botania compat
                manaIcon = c.getField("manaIcon");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

/*    private static IIcon getManaIcon() {
        IIcon ret = null;
        try {
            ret = (IIcon) manaIcon.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ret == null)
            ret = Blocks.WATER.getIcon(0, 0);
        return ret;
    }*/

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntityGearboxAt(PoseStack stack, BlockEntityGearbox tile, VertexConsumer bufferSource, int light, int overlay) {
//        this.setupGL(stack, tile, par2, par4, par6);

        if (tile.isInWorld()) {
            // 26.1 fix: legacy port left the gearbox at the block's local origin (corner),
            // with no facing rotation and no Z-axis flip, so the model appeared offset and
            // upside-down to the user ("gearboxes look messed up"). Mirror the
            // splitter/shaft mount: translate to top-centre, yaw by -facing-90°, flip Z 180°.
            var blockstate = tile.getLevel() != null ? tile.getBlockState()
                    : RotaryBlocks.HSLA_GEARBOX_2x.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, net.minecraft.core.Direction.SOUTH);
            float f = blockstate.getValue(BlockRotaryCraftMachine.FACING).toYRot();
            stack.translate(0.5F, 1.5F, 0.5F);
            stack.mulPose(Axis.YP.rotationDegrees(-f - 90));
            stack.mulPose(Axis.ZP.rotationDegrees(180));
            ArrayList li = ReikaJavaLibrary.makeListFrom(tile.getBearingTier());

            switch (tile.getRatio()) {
                case 2 -> {
                    VertexConsumer vertexconsumer = bufferSource;
                    gearboxModel.renderAll(stack, vertexconsumer, light, tile, li, -tile.phi);
                }
                case 4 -> {
                    VertexConsumer vertexconsumer = bufferSource;
                    gearboxModel4.renderAll(stack, vertexconsumer, light, tile, li, -tile.phi);
                }
                case 8 -> {
                    VertexConsumer vertexconsumer = bufferSource;
                    gearboxModel8.renderAll(stack, vertexconsumer, light, tile, li, -tile.phi);
                }
                case 16 -> {
                    VertexConsumer vertexconsumer = bufferSource;
                    gearboxModel16.renderAll(stack, vertexconsumer, light, tile, li, -tile.phi);
                }
            }

        } else {
            //ReikaChatHelper.write(this.itemMetadata);
            stack.mulPose(Axis.YP.rotationDegrees(-90));
            switch (tile.getRatio()) {
                case 2 -> {
                    VertexConsumer vertexconsumer = bufferSource;
                    gearboxModel.renderAll(stack, vertexconsumer, light, tile, null);
                }
                case 4 -> {
                    VertexConsumer vertexconsumer = bufferSource;
                    gearboxModel4.renderAll(stack, vertexconsumer, light, tile, null);
                }
                case 8 -> {
                    VertexConsumer vertexconsumer = bufferSource;
                    gearboxModel8.renderAll(stack, vertexconsumer, light, tile, null);
                }
                case 16 -> {
                    VertexConsumer vertexconsumer = bufferSource;
                    gearboxModel16.renderAll(stack, vertexconsumer, light, tile, null);
                }
            }
        }

//        this.closeGL(stack, tile);
    }

    private void renderMode(PoseStack stack, BlockEntityGearbox tile, double par2, double par4, double par6) {
        ItemStack is = Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.HEAD);
        boolean flag = ReikaItemHelper.matchStacks(is, RotaryItems.IO_GOGGLES.get());
        if (flag) {
            int var11 = 0;
     /*todo       switch (tile.getBlockMetadata() & 3) {
                case 0:
                    var11 = 0;
                    break;
                case 1:
                    var11 = 180;
                    break;
                case 2:
                    var11 = 90;
                    break;
                case 3:
                    var11 = 270;
                    break;
            }*/
            stack.translate(par2, par4, par6);
            float sc = 0.1f;
            stack.scale(sc, sc, sc);
            stack.mulPose(Axis.YP.rotationDegrees(var11));
            String s = tile.reduction ? "Torque" : "Speed";
//            Minecraft.getInstance().font.drawInBatch(s, 0, 0, 0xffffff, false, stack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            stack.scale(1 / sc, 1 / sc, 1 / sc);
            stack.translate(-par2, -par4, -par6);
        }
    }

/*    private void renderLiquid(PoseStack stack, BlockEntity tile) {
        stack.pushPose();
//        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        BlockEntityGearbox tr = (BlockEntityGearbox) tile;
        if (tr.getLubricant() > 0) {
            Fluid f = RotaryFluids.LUBRICANT.get();
            ReikaLiquidRenderer.bindFluidTexture(f);
            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
            int c = 0xffffff;
            if (tr.isLiving()) {
                ico = this.getManaIcon();
                float t = tr.getTicksExisted() + ReikaRenderHelper.getPartialTickTime();
                c = ReikaColorAPI.getModifiedHue(0x0000ff, 192 + (int) (32 * Math.sin(t / 16D)));
            }
            float u = ico.getMinU();
            float v = ico.getMinV();
            float du = ico.getMaxU();
            float dv = ico.getMaxV();
            double h = 0.0625 + (4D / 16D * tr.getLubricant() / tr.getMaxLubricant()) * 0.9;
            if (tr.isFlipped) {
                h = 1 - h;
                GL11.glDisable(GL11.GL_CULL_FACE);
            }
            Tesselator tess = Tesselator.getInstance();
            BufferBuilder v5 = tess.getBuilder();
            v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            v5.color(c);
            v5.normal(0, 1, 0);
            v5.vertex(0.0625, h, 0.9375).uv(u, dv);
            v5.vertex(0.9375, h, 0.9375).uv(du, dv);
            v5.vertex(0.9375, h, 0.0625).uv(du, v);
            v5.vertex(0.0625, h, 0.0625).uv(u, v);

            if (tr.isFlipped) {
                ico = Blocks.GLASS.getIcon(0, 0);
                u = ico.getMinU();
                v = ico.getMinV();
                du = ico.getMaxU();
                dv = ico.getMaxV();
                double o = 0.005;
                v5.vertex(0.0625, h - o, 0.9375).uv(u, dv);
                v5.vertex(0.9375, h - o, 0.9375).uv(du, dv);
                v5.vertex(0.9375, h - o, 0.0625).uv(du, v);
                v5.vertex(0.0625, h - o, 0.0625).uv(u, v);
            }
            v5.end();
        }
        //GL11.glPopAttrib();
        stack.popPose();
    }*/

    // 1.21.5: render -> submit; @Override dropped
    public void render(BlockEntityGearbox tile, float p_112308_, PoseStack stack, VertexConsumer bufferSource, int packetLight, int overlay) {
        if (this.doRenderModel(stack, tile))
            this.renderBlockEntityGearboxAt(stack, tile, bufferSource, packetLight, overlay);
        if ((tile).isInWorld()) {//todo && MinecraftForgeClient.getRenderPass() == 1) {
            IORenderer.renderIO(stack, bufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
//todo            this.renderLiquid(stack, tile);
            //this.renderMode((BlockEntityGearbox)tile, par2, par4, par6);
        }
        if (!tile.hasLevel()) {
//         todo   this.renderLiquid(stack, tile);
        }
    }

    /**
     * 26.2 submit hook. Snapshot pose + pass VertexConsumer directly (via tiny adapter for legacy renderBlock...At).
     */
    @Override
    public void submit(net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState state,
                       PoseStack poseStack,
                       net.minecraft.client.renderer.SubmitNodeCollector collector,
                       net.minecraft.client.renderer.state.level.CameraRenderState camera) {
        net.minecraft.world.level.Level level = Minecraft.getInstance().level;
        if (level == null) return;
        net.minecraft.world.level.block.entity.BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntityGearbox tile)) return;
        if (!this.doRenderModel(poseStack, tile)) return;

        // The per-ratio GearboxModel subclasses share the same texture directory; pick the file
        // by material via {@link GearboxTypes#getBaseGearboxTexture}.
        RenderType rt = RenderTypes.entityCutout(textureWithSuffix(GearboxModel.TEXTURE_LOCATION, tile.getGearboxType().getBaseGearboxTexture()));

        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());
        int light = state.lightCoords;
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> {
            renderBlockEntityGearboxAt(snapped, tile, vc, light, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY);
        });
        if (tile.isInWorld()) {
            IORenderer.renderIO(poseStack, collector, tile, tile.getBlockPos());
        }
    }

}

