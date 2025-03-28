/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Matrix4f;
import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
import reika.rotarycraft.api.power.ShaftPowerEmitter;
import reika.rotarycraft.api.power.ShaftPowerReceiver;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;
import reika.rotarycraft.blockentities.BlockEntityWinder;
import reika.rotarycraft.blockentities.transmission.BlockEntityDistributionClutch;
import reika.rotarycraft.blockentities.transmission.BlockEntityShaft;
import reika.rotarycraft.blockentities.transmission.BlockEntitySplitter;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.RotaryItems;

public abstract class IORenderer {
    private static final float expand = 1;
    private static final Direction[] dirs = Direction.values();

    public static void renderOut(PoseStack matrixStack, float x, float y, float z, int a) {
        int[] color = {255, 0, 0, a};
        if (ConfigRegistry.COLORBLIND.getState())
            color[0] = 0;
        renderBox(matrixStack, x, y, z, color);
    }

    public static void renderIn(PoseStack matrixStack, float x, float y, float z, int a) {
        int[] color = {0, 255, 0, a};
        if (ConfigRegistry.COLORBLIND.getState()) {
            color[0] = 255;
            color[2] = 255;
        }
        renderBox(matrixStack, x, y, z, color);
    }

    public static void renderIO(PoseStack matrixStack, MultiBufferSource bufferSource, BlockEntity teb, BlockPos pos) {
        renderIO(matrixStack, bufferSource, teb, pos.getX(), pos.getY(), pos.getZ());
    }

    public static void renderIO(PoseStack matrixStack, MultiBufferSource bufferSource, BlockEntity teb, double x, double y, double z) {
        ItemStack is = Minecraft.getInstance().player.getItemBySlot(EquipmentSlot.HEAD);
        boolean flag = is.is(RotaryItems.IO_GOGGLES.get());
        int par4 = 0;
        if (teb instanceof BlockEntityIOMachine te) {
            if (flag)
                te.iotick = 512;
            if (te.iotick <= 0)
                return;
            if (teb instanceof BlockEntitySplitter) {
                BlockEntitySplitter ts = (BlockEntitySplitter) teb;
                if (ts.isSplitting()) { //Splitting
                    float xdiff = ts.getWriteDirection().getStepX();
                    float zdiff = ts.getWriteDirection().getStepZ();
                    renderOut(matrixStack, xdiff, par4, zdiff, ts.iotick);

                    xdiff = ts.getWriteDirection2().getStepX();
                    zdiff = ts.getWriteDirection2().getStepZ();
                    renderOut(matrixStack, xdiff, par4, zdiff, ts.iotick);

                    if (ts.getReadDirection() != null) {
                        xdiff = ts.getReadDirection().getStepX();
                        zdiff = ts.getReadDirection().getStepZ();
                        renderIn(matrixStack, xdiff, par4, zdiff, ts.iotick);
                    }
                } else { //Merging
                    if (ts.getWriteDirection() != null) {
                        float xdiff = ts.getWriteDirection().getStepX();
                        float zdiff = ts.getWriteDirection().getStepZ();
                        renderOut(matrixStack, xdiff, par4, zdiff, ts.iotick);
                    }

                    if (ts.getReadDirection() != null) {
                        float xdiff = ts.getReadDirection().getStepX();
                        float zdiff = ts.getReadDirection().getStepZ();
                        renderIn(matrixStack, xdiff, par4, zdiff, ts.iotick);
                    }

                    if (ts.getReadDirection2() != null) {
                        float xdiff = ts.getReadDirection2().getStepX();
                        float zdiff = ts.getReadDirection2().getStepZ();
                        renderIn(matrixStack, xdiff, par4, zdiff, ts.iotick);
                    }
                }
                return;
            }
            if (teb instanceof BlockEntityShaft && ((BlockEntityShaft) teb).isCross()) { //cross
                BlockEntityShaft ts = (BlockEntityShaft) teb;
                if (ts.getWriteDirection() != null) {
                    float xdiff = ts.getWriteDirection().getStepX();
                    float zdiff = ts.getWriteDirection().getStepZ();
                    renderOut(matrixStack, xdiff, par4, zdiff, ts.iotick);
                }
                if (ts.getWriteDirection2() != null) {
                    float xdiff = ts.getWriteDirection2().getStepX();
                    float zdiff = ts.getWriteDirection2().getStepZ();
                    renderOut(matrixStack, xdiff, par4, zdiff, ts.iotick);
                }
                if (ts.getReadDirection() != null) {
                    float xdiff = ts.getReadDirection().getStepX();
                    float zdiff = ts.getReadDirection().getStepZ();
                    renderIn(matrixStack, xdiff, par4, zdiff, ts.iotick);
                }
                if (ts.getReadDirection2() != null) {
                    float xdiff = ts.getReadDirection2().getStepX();
                    float zdiff = ts.getReadDirection2().getStepZ();
                    renderIn(matrixStack, xdiff, par4, zdiff, ts.iotick);
                }
                return;
            }
            if (teb instanceof BlockEntityDistributionClutch td) {
                Direction read = td.getInputDirection();
                if (read != null) {
                    renderIn(matrixStack, read.getStepX(), read.getStepY(), read.getStepZ(), te.iotick);
                }
                for (int i = 0; i < 4; i++) {
                    Direction dir = Direction.values()[i + 2];
                    if (td.isOutputtingToSide(dir)) {
                        float xdiff = dir.getStepX();
                        float ydiff = dir.getStepY();
                        float zdiff = dir.getStepZ();
                        renderOut(matrixStack, xdiff, ydiff, zdiff, te.iotick);
                    }
                }
                return;
            }
            if (teb instanceof BlockEntityWinder) {
                BlockEntityWinder ts = (BlockEntityWinder) teb;
                if (ts.winding && ts.getReadDirection() != null) {
                    float xdiff = ts.getReadDirection().getStepX();
                    float ydiff = ts.getReadDirection().getStepY();
                    float zdiff = ts.getReadDirection().getStepZ();
                    renderIn(matrixStack, xdiff, ydiff, zdiff, ts.iotick);
                } else if (ts.getWriteDirection() != null) {
                    float xdiff = ts.getWriteDirection().getStepX();
                    float ydiff = ts.getWriteDirection().getStepY();
                    float zdiff = ts.getWriteDirection().getStepZ();
                    renderOut(matrixStack, xdiff, ydiff, zdiff, ts.iotick);
                }
                return;
            }
            if (te.isOmniSided) {
                if (te.getMachine().getMaxY(te) == 1)
                    renderIn(matrixStack, te.getPointingOffsetX(), te.getPointingOffsetY() + 1, te.getPointingOffsetZ(), te.iotick);
                if (te.getMachine().getMinY(te) == 0)
                    renderIn(matrixStack, te.getPointingOffsetX(), te.getPointingOffsetY() - 1, te.getPointingOffsetZ(), te.iotick);
                if (te.getMachine().getMaxX(te) == 1)
                    renderIn(matrixStack, te.getPointingOffsetX() + 1, te.getPointingOffsetY(), te.getPointingOffsetZ(), te.iotick);
                if (te.getMachine().getMinX(te) == 0)
                    renderIn(matrixStack, te.getPointingOffsetX() - 1, te.getPointingOffsetY(), te.getPointingOffsetZ(), te.iotick);
                if (te.getMachine().getMaxZ(te) == 1)
                    renderIn(matrixStack, te.getPointingOffsetX(), te.getPointingOffsetY(), te.getPointingOffsetZ() + 1, te.iotick);
                if (te.getMachine().getMinZ(te) == 0)
                    renderIn(matrixStack, te.getPointingOffsetX(), te.getPointingOffsetY(), te.getPointingOffsetZ() - 1, te.iotick);
                return;
            }
            if (te.getWriteDirection() != null) {
                float xdiff = te.getWriteDirection().getStepX();
                float ydiff = te.getWriteDirection().getStepY();
                float zdiff = te.getWriteDirection().getStepZ();
                renderOut(matrixStack, xdiff, ydiff, zdiff, te.iotick);
            }
            if (te.getWriteDirection2() != null) {
                float xdiff = te.getWriteDirection2().getStepX();
                float ydiff = te.getWriteDirection2().getStepY();
                float zdiff = te.getWriteDirection2().getStepZ();
                renderOut(matrixStack, xdiff, ydiff, zdiff, te.iotick);
            }
            if (te.getReadDirection() != null) {
                float xdiff = te.getReadDirection().getStepX() + te.getPointingOffsetX();
                float ydiff = te.getReadDirection().getStepY() + te.getPointingOffsetY();
                float zdiff = te.getReadDirection().getStepZ() + te.getPointingOffsetZ();
                renderIn(matrixStack, xdiff, ydiff, zdiff, te.iotick);
            }
            if (te.getReadDirection2() != null) {
                float xdiff = te.getReadDirection2().getStepX() + te.getPointingOffsetX();
                float ydiff = te.getReadDirection2().getStepY() + te.getPointingOffsetY();
                float zdiff = te.getReadDirection2().getStepZ() + te.getPointingOffsetZ();
                renderIn(matrixStack, xdiff, ydiff, zdiff, te.iotick);
            }
            if (te.getReadDirection3() != null) {
                float xdiff = te.getReadDirection3().getStepX() + te.getPointingOffsetX();
                float ydiff = te.getReadDirection3().getStepY() + te.getPointingOffsetY();
                float zdiff = te.getReadDirection3().getStepZ() + te.getPointingOffsetZ();
                renderIn(matrixStack, xdiff, ydiff, zdiff, te.iotick);
            }
            if (te.getReadDirection4() != null) {
                float xdiff = te.getReadDirection4().getStepX() + te.getPointingOffsetX();
                float ydiff = te.getReadDirection4().getStepY() + te.getPointingOffsetY();
                float zdiff = te.getReadDirection4().getStepZ() + te.getPointingOffsetZ();
                renderIn(matrixStack, xdiff, ydiff, zdiff, te.iotick);
            }
        } else {
            if (teb instanceof ShaftPowerReceiver sr) {
                int io = sr.getIORenderAlpha();
                if (flag)
                    io = 255;
                if (io <= 0)
                    return;
                for (int i = 0; i < 6; i++) {
                    Direction dir = dirs[i];
//                    int dx = dir.getStepX()+teb.xCoord;
//                    int dy = dir.getStepY()+teb.yCoord;
//                    int dz = dir.getStepZ()+teb.zCoord;
                    if (sr.canReadFrom(dir)) {
                        renderIn(matrixStack, dir.getStepX(), dir.getStepY(), dir.getStepZ(), io);
                    }
                }
            }
            if (teb instanceof ShaftPowerEmitter se) {
                int io = se.getIORenderAlpha();
                if (flag)
                    io = 255;
                if (io <= 0)
                    return;
                for (int i = 0; i < 6; i++) {
                    Direction dir = dirs[i];
                    //int dx = dir.getStepX()+teb.xCoord;
                    //int dy = dir.getStepY()+teb.yCoord;
                    //int dz = dir.getStepZ()+teb.zCoord;
                    if (se.canWriteTo(dir)) {
                        renderOut(matrixStack, dir.getStepX(), dir.getStepY(), dir.getStepZ(), io);
                    }
                }
            }
        }
    }

    private static void renderBox(PoseStack stack, float x, float y, float z, int[] color) {
//        RotaryCraft.LOGGER.info("Rendering box at " + x + ", " + y + ", " + z);
        if (color[3] > 255) color[3] = 255;

        ReikaRenderHelper.prepareGeoDraw(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();

        RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
        Block.box(0, 0, 0, 16, 16, 16).forAllEdges((p_234280_, p_234281_, p_234282_, p_234283_, p_234284_, p_234285_) -> {
            RenderSystem.lineWidth(10);
            ReikaRenderHelper.renderLine(stack, x + p_234280_, y + p_234281_, z + p_234282_, x + p_234283_, y + p_234284_, z + p_234285_, color);
        });

        stack.pushPose();
        stack.translate(0.001, 0.001, 0.001);
        Matrix4f matrix = stack.last().pose();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_NORMAL);
        builder.vertex(matrix, x - 0f * expand, y - 0f * expand, z - 0f * expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x + expand, y - 0f * expand, z - 0f * expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x + expand, y - 0f * expand, z + expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x - 0f * expand, y - 0f * expand, z + expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();

        builder.vertex(matrix, x + expand, y - 0f * expand, z - 0f * expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x + expand, y + expand, z - 0f * expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x + expand, y + expand, z + expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x + expand, y - 0f * expand, z + expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();

        builder.vertex(matrix, x - 0f * expand, y + expand, z - 0f * expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x - 0f * expand, y - 0f * expand, z - 0f * expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x - 0f * expand, y - 0f * expand, z + expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x - 0f * expand, y + expand, z + expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();

        builder.vertex(matrix, x - 0f * expand, y + expand, z + expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x - 0f * expand, y - 0f * expand, z + expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x + expand, y - 0f * expand, z + expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x + expand, y + expand, z + expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();

        builder.vertex(matrix, x - 0f * expand, y - 0f * expand, z - 0f * expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x - 0f * expand, y + expand, z - 0f * expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x + expand, y + expand, z - 0f * expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x + expand, y - 0f * expand, z - 0f * expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();

        builder.vertex(matrix, x + expand, y + expand, z - 0f * expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x - 0f * expand, y + expand, z - 0f * expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x - 0f * expand, y + expand, z + expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        builder.vertex(matrix, x + expand, y + expand, z + expand).color(color[0], color[1], color[2], (int) (color[3] * 0.375)).normal(stack.last().normal(), 1, 1, 1).endVertex();
        tesselator.end();

        RenderSystem.depthMask(true);
        RenderSystem.lineWidth(1.0f);
        ReikaRenderHelper.exitGeoDraw();
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        stack.popPose();
    }
}