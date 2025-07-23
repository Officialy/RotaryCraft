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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
import reika.rotarycraft.auxiliary.interfaces.RenderableDuct;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;

public class PipeRenderer extends RotaryTERenderer<BlockEntityPiping> {

    public PipeRenderer(BlockEntityRendererProvider.Context context) {

    }

    protected void renderLiquid(PoseStack stack, RenderableDuct tile, double par2, double par4, double par6, Direction dir) {
        Fluid f = tile.getAttributes();
        if (f == null)
            return;

        float size = 0.75F / 2F;
        float window = 0.5F / 2F;
        float dl = size - window;
        float dd = 0.5F - size;
        float in = 0.5f + size - 0.01f;
        float in2 = 0.5f - size + 0.01f;
        float dd2 = in - in2;

//        IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(tile.getFluidType());
//        ReikaLiquidRenderer.bindFluidTexture(f);
//    todo    if (f.getLuminosity() > 0)
//            ReikaRenderHelper.disableLighting();
        float u = 1;//ico.getMinU();
        float v = 1;//ico.getMinV();
        float u2 = 1;// ico.getMaxU();
        float v2 = 1;// ico.getMaxV();
        float du = (float) (dd2 * (u2 - u) / 4D);

        stack.translate(par2, par4, par6);
        RenderSystem.enableBlend();
        RenderSystem.enableCull();
//    todo    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        //    todo     GL11.glColor3f(1, 1, 1);

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder v5 = tess.getBuilder();

        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        v5.normal(dir.getStepX(), dir.getStepY(), dir.getStepZ());
        //this.faceBrightness(Direction.DOWN, v5);
        if (!tile.isConnectionValidForSide(dir)) {
            switch (dir) {
                case UP:
                    v5.vertex(in2, in, in).uv(u, v2);
                    v5.vertex(in, in, in).uv(u2, v2);
                    v5.vertex(in, in, in2).uv(u2, v);
                    v5.vertex(in2, in, in2).uv(u, v);
                    break;
                case DOWN:
                    v5.vertex(in2, in2, in2).uv(u, v);
                    v5.vertex(in, in2, in2).uv(u2, v);
                    v5.vertex(in, in2, in).uv(u2, v2);
                    v5.vertex(in2, in2, in).uv(u, v2);
                    break;
                case SOUTH:
                    v5.vertex(in, in, in).uv(u, v);
                    v5.vertex(in2, in, in).uv(u2, v);
                    v5.vertex(in2, in2, in).uv(u2, v2);
                    v5.vertex(in, in2, in).uv(u, v2);
                    break;
                case NORTH:
                    v5.vertex(in, in2, in2).uv(u, v2);
                    v5.vertex(in2, in2, in2).uv(u2, v2);
                    v5.vertex(in2, in, in2).uv(u2, v);
                    v5.vertex(in, in, in2).uv(u, v);
                    break;
                case EAST:
                    v5.vertex(in, in2, in).uv(u, v2);
                    v5.vertex(in, in2, in2).uv(u2, v2);
                    v5.vertex(in, in, in2).uv(u2, v);
                    v5.vertex(in, in, in).uv(u, v);
                    break;
                case WEST:
                    v5.vertex(in2, in, in).uv(u, v);
                    v5.vertex(in2, in, in2).uv(u2, v);
                    v5.vertex(in2, in2, in2).uv(u2, v2);
                    v5.vertex(in2, in2, in).uv(u, v2);
                default:
                    break;
            }
        } else { //is connected on side
            switch (dir) {
                case DOWN -> {
                    v5.normal(-1, 0, 0);
                    v5.vertex(in2, in2, in).uv(u, v);
                    v5.vertex(in2, in2, in2).uv(u2, v);
                    v5.vertex(in2, 0, in2).uv(u2, v + du);
                    v5.vertex(in2, 0, in).uv(u, v + du);
                    v5.normal(1, 0, 0);
                    v5.vertex(in, 0, in).uv(u, v + du);
                    v5.vertex(in, 0, in2).uv(u2, v + du);
                    v5.vertex(in, in2, in2).uv(u2, v);
                    v5.vertex(in, in2, in).uv(u, v);
                    v5.normal(0, 0, -1);
                    v5.vertex(in, 0, in2).uv(u, v + du);
                    v5.vertex(in2, 0, in2).uv(u2, v + du);
                    v5.vertex(in2, in2, in2).uv(u2, v);
                    v5.vertex(in, in2, in2).uv(u, v);
                    v5.normal(0, 0, 1);
                    v5.vertex(in, in2, in).uv(u, v);
                    v5.vertex(in2, in2, in).uv(u2, v);
                    v5.vertex(in2, 0, in).uv(u2, v + du);
                    v5.vertex(in, 0, in).uv(u, v + du);
                }
                case UP -> {
                    v5.normal(-1, 0, 0);
                    v5.vertex(in2, 1, in).uv(u, v + du);
                    v5.vertex(in2, 1, in2).uv(u2, v + du);
                    v5.vertex(in2, in, in2).uv(u2, v);
                    v5.vertex(in2, in, in).uv(u, v);
                    v5.normal(1, 0, 0);
                    v5.vertex(in, in, in).uv(u, v);
                    v5.vertex(in, in, in2).uv(u2, v);
                    v5.vertex(in, 1, in2).uv(u2, v + du);
                    v5.vertex(in, 1, in).uv(u, v + du);
                    v5.normal(0, 0, -1);
                    v5.vertex(in, in, in2).uv(u, v);
                    v5.vertex(in2, in, in2).uv(u2, v);
                    v5.vertex(in2, 1, in2).uv(u2, v + du);
                    v5.vertex(in, 1, in2).uv(u, v + du);
                    v5.normal(0, 0, 1);
                    v5.vertex(in, 1, in).uv(u, v + du);
                    v5.vertex(in2, 1, in).uv(u2, v + du);
                    v5.vertex(in2, in, in).uv(u2, v);
                    v5.vertex(in, in, in).uv(u, v);
                }
                case NORTH -> {
                    v5.normal(-1, 0, 0);
                    v5.vertex(in2, in2, 0).uv(u, v2);
                    v5.vertex(in2, in2, in2).uv(u + du, v2);
                    v5.vertex(in2, in, in2).uv(u + du, v);
                    v5.vertex(in2, in, 0).uv(u, v);
                    v5.normal(1, 0, 0);
                    v5.vertex(in, in, 0).uv(u, v);
                    v5.vertex(in, in, in2).uv(u + du, v);
                    v5.vertex(in, in2, in2).uv(u + du, v2);
                    v5.vertex(in, in2, 0).uv(u, v2);
                    v5.normal(0, 1, 0);
                    v5.vertex(in2, in, 0).uv(u, v2);
                    v5.vertex(in2, in, in2).uv(u + du, v2);
                    v5.vertex(in, in, in2).uv(u + du, v);
                    v5.vertex(in, in, 0).uv(u, v);
                    v5.normal(0, -1, 0);
                    v5.vertex(in, in2, 0).uv(u, v);
                    v5.vertex(in, in2, in2).uv(u + du, v);
                    v5.vertex(in2, in2, in2).uv(u + du, v2);
                    v5.vertex(in2, in2, 0).uv(u, v2);
                }
                case SOUTH -> {
                    v5.normal(-1, 0, 0);
                    v5.vertex(in2, in, 1).uv(u, v);
                    v5.vertex(in2, in, in).uv(u + du, v);
                    v5.vertex(in2, in2, in).uv(u + du, v2);
                    v5.vertex(in2, in2, 1).uv(u, v2);
                    v5.normal(1, 0, 0);
                    v5.vertex(in, in2, 1).uv(u, v2);
                    v5.vertex(in, in2, in).uv(u + du, v2);
                    v5.vertex(in, in, in).uv(u + du, v);
                    v5.vertex(in, in, 1).uv(u, v);
                    v5.normal(0, 1, 0);
                    v5.vertex(in, in, 1).uv(u, v);
                    v5.vertex(in, in, in).uv(u + du, v);
                    v5.vertex(in2, in, in).uv(u + du, v2);
                    v5.vertex(in2, in, 1).uv(u, v2);
                    v5.normal(0, -1, 0);
                    v5.vertex(in2, in2, 1).uv(u, v2);
                    v5.vertex(in2, in2, in).uv(u + du, v2);
                    v5.vertex(in, in2, in).uv(u + du, v);
                    v5.vertex(in, in2, 1).uv(u, v);
                }
                case EAST -> {
                    v5.normal(0, 0, 1);
                    v5.vertex(1, in, in).uv(u, v);
                    v5.vertex(in, in, in).uv(u + du, v);
                    v5.vertex(in, in2, in).uv(u + du, v2);
                    v5.vertex(1, in2, in).uv(u, v2);
                    v5.normal(0, 0, -1);
                    v5.vertex(1, in2, in2).uv(u, v2);
                    v5.vertex(in, in2, in2).uv(u + du, v2);
                    v5.vertex(in, in, in2).uv(u + du, v);
                    v5.vertex(1, in, in2).uv(u, v);
                    v5.normal(0, 1, 0);
                    v5.vertex(1, in, in2).uv(u, v2);
                    v5.vertex(in, in, in2).uv(u + du, v2);
                    v5.vertex(in, in, in).uv(u + du, v);
                    v5.vertex(1, in, in).uv(u, v);
                    v5.normal(0, -1, 0);
                    v5.vertex(1, in2, in).uv(u, v);
                    v5.vertex(in, in2, in).uv(u + du, v);
                    v5.vertex(in, in2, in2).uv(u + du, v2);
                    v5.vertex(1, in2, in2).uv(u, v2);
                }
                case WEST -> {
                    v5.normal(0, 0, 1);
                    v5.vertex(0, in2, in).uv(u, v2);
                    v5.vertex(in2, in2, in).uv(u + du, v2);
                    v5.vertex(in2, in, in).uv(u + du, v);
                    v5.vertex(0, in, in).uv(u, v);
                    v5.normal(0, 0, -1);
                    v5.vertex(0, in, in2).uv(u, v);
                    v5.vertex(in2, in, in2).uv(u + du, v);
                    v5.vertex(in2, in2, in2).uv(u + du, v2);
                    v5.vertex(0, in2, in2).uv(u, v2);
                    v5.normal(0, 1, 0);
                    v5.vertex(0, in, in).uv(u, v);
                    v5.vertex(in2, in, in).uv(u + du, v);
                    v5.vertex(in2, in, in2).uv(u + du, v2);
                    v5.vertex(0, in, in2).uv(u, v2);
                    v5.normal(0, -1, 0);
                    v5.vertex(0, in2, in2).uv(u, v2);
                    v5.vertex(in2, in2, in2).uv(u + du, v2);
                    v5.vertex(in2, in2, in).uv(u + du, v);
                    v5.vertex(0, in2, in).uv(u, v);
                }
                default -> {
                }
            }

        }
        if (tile.isConnectedToNonSelf(dir)) {
            v5.normal(dir.getStepX(), dir.getStepY(), dir.getStepZ());
            switch (dir) {
                case UP:
                    v5.vertex(in2, 0.99, in).uv(u, v2);
                    v5.vertex(in, 0.99, in).uv(u2, v2);
                    v5.vertex(in, 0.99, in2).uv(u2, v);
                    v5.vertex(in2, 0.99, in2).uv(u, v);
                    break;
                case DOWN:
                    v5.vertex(in2, 0.01, in2).uv(u, v);
                    v5.vertex(in, 0.01, in2).uv(u2, v);
                    v5.vertex(in, 0.01, in).uv(u2, v2);
                    v5.vertex(in2, 0.01, in).uv(u, v2);
                    break;
                case SOUTH:
                    v5.vertex(in, in, 0.99).uv(u, v);
                    v5.vertex(in2, in, 0.99).uv(u2, v);
                    v5.vertex(in2, in2, 0.99).uv(u2, v2);
                    v5.vertex(in, in2, 0.99).uv(u, v2);
                    break;
                case NORTH:
                    v5.vertex(in, in2, 0.01).uv(u, v2);
                    v5.vertex(in2, in2, 0.01).uv(u2, v2);
                    v5.vertex(in2, in, 0.01).uv(u2, v);
                    v5.vertex(in, in, 0.01).uv(u, v);
                    break;
                case EAST:
                    v5.vertex(0.99, in2, in).uv(u, v2);
                    v5.vertex(0.99, in2, in2).uv(u2, v2);
                    v5.vertex(0.99, in, in2).uv(u2, v);
                    v5.vertex(0.99, in, in).uv(u, v);
                    break;
                case WEST:
                    v5.vertex(0.01, in, in).uv(u, v);
                    v5.vertex(0.01, in, in2).uv(u2, v);
                    v5.vertex(0.01, in2, in2).uv(u2, v2);
                    v5.vertex(0.01, in2, in).uv(u, v2);
                default:
                    break;
            }
        }
        tess.end();
//        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        ReikaRenderHelper.enableLighting();
        stack.translate(-par2, -par4, -par6);
        RenderSystem.disableBlend();
    }

    @Override
    public void render(BlockEntityPiping tile, float p_112308_, PoseStack stack, MultiBufferSource bufferSource, int p_112311_, int p_112312_) {
        RenderableDuct te = tile;
        /*todo if (!tile.hasLevel()) {
            ReikaTextureHelper.bindTerrainTexture();
            double s = 1;
            double sy = 1.05;
            stack.scale((float) s, (float) sy, (float) s);
            //this.renderBlock(te, par2, par4, par6);
            ClientProxy.pipe.renderBlockInInventory(te, par2, par4, par6);
            stack.scale((float) (1 / s), (float) (1 / sy), (float) (1 / s));
        }*/
        renderBlock(stack, te, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
    }

    private void renderBlock(PoseStack stack, RenderableDuct te, double par2, double par4, double par6) {
//        IIcon ico = te.getBlockIcon();
        float u = 0;//todo ico.getMinU();
        float v = 1;//todo ico.getMinV();
        float du = 0;//todo  ico.getMaxU();
        float dv = 1;//todo  ico.getMaxV();
        stack.translate(par2, par4, par6);
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder v5 = tess.getBuilder();

        float f = 0.6F;
//   todo     GL11.glColor4f(f, f, f, 1);
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        v5.normal(0, 1, 0);
        v5.vertex(0, 0, 1).uv(u, v);
        v5.vertex(1, 0, 1).uv(du, v);
        v5.vertex(1, 1, 1).uv(du, dv);
        v5.vertex(0, 1, 1).uv(u, dv);
        tess.end();
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        v5.normal(0, 1, 0);
        v5.vertex(0, 1, 0).uv(u, dv);
        v5.vertex(1, 1, 0).uv(du, dv);
        v5.vertex(1, 0, 0).uv(du, v);
        v5.vertex(0, 0, 0).uv(u, v);
        tess.end();
        f = 0.4F;
//  todo      GL11.glColor4f(f, f, f, 1);
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        v5.normal(0, 1, 0);
        v5.vertex(1, 1, 0).uv(u, dv);
        v5.vertex(1, 1, 1).uv(du, dv);
        v5.vertex(1, 0, 1).uv(du, v);
        v5.vertex(1, 0, 0).uv(u, v);
        tess.end();
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        v5.normal(0, 1, 0);
        v5.vertex(0, 0, 0).uv(u, v);
        v5.vertex(0, 0, 1).uv(du, v);
        v5.vertex(0, 1, 1).uv(du, dv);
        v5.vertex(0, 1, 0).uv(u, dv);
        tess.end();

        f = 1F;
// todo       GL11.glColor4f(f, f, f, 1);
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        v5.normal(0, 1, 0);
        v5.vertex(0, 1, 1).uv(u, dv);
        v5.vertex(1, 1, 1).uv(du, dv);
        v5.vertex(1, 1, 0).uv(du, v);
        v5.vertex(0, 1, 0).uv(u, v);
        tess.end();

        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        v5.normal(0, 1, 0);
        v5.vertex(0, 0, 0).uv(u, v);
        v5.vertex(1, 0, 0).uv(du, v);
        v5.vertex(1, 0, 1).uv(du, dv);
        v5.vertex(0, 0, 1).uv(u, dv);
        tess.end();

        //-----------------------------------

        double g = 0.35;
        double g1 = g / 2;
        double g2 = 1 - g / 2;

//        ico = Blocks.WOOL.getIcon(0, ReikaDyeHelper.BLACK.getWoolStack());
        u = 0;//ico.getMinU();
        v = 1;//ico.getMinV();
        du = 0;// ico.getMaxU();
        dv = 1;// ico.getMaxV();
        float uu = du - u;
        float vv = dv - v;
        u += g1 * uu;
        du -= g1 * uu;
        v += g1 * vv;
        dv -= g1 * vv;

        f = 0.6F;
// todo       GL11.glColor4f(f, f, f, 1);
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        v5.normal(0, 1, 0);
        v5.vertex(g1, g1, 1.001).uv(u, v);
        v5.vertex(g2, g1, 1.001).uv(du, v);
        v5.vertex(g2, g2, 1.001).uv(du, dv);
        v5.vertex(g1, g2, 1.001).uv(u, dv);
        tess.end();

        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        v5.normal(0, 1, 0);
        v5.vertex(g1, g2, -0.001).uv(u, dv);
        v5.vertex(g2, g2, -0.001).uv(du, dv);
        v5.vertex(g2, g1, -0.001).uv(du, v);
        v5.vertex(g1, g1, -0.001).uv(u, v);
        tess.end();

        f = 0.4F;
//    todo    GL11.glColor4f(f, f, f, 1);
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        v5.normal(0, 1, 0);
        v5.vertex(1.001, g2, g1).uv(u, dv);
        v5.vertex(1.001, g2, g2).uv(du, dv);
        v5.vertex(1.001, g1, g2).uv(du, v);
        v5.vertex(1.001, g1, g1).uv(u, v);
        tess.end();

        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        v5.normal(0, 1, 0);
        v5.vertex(-0.001, g1, g1).uv(u, v);
        v5.vertex(-0.001, g1, g2).uv(du, v);
        v5.vertex(-0.001, g2, g2).uv(du, dv);
        v5.vertex(-0.001, g2, g1).uv(u, dv);
        tess.end();

        f = 1F;
        //    todo        GL11.glColor4f(f, f, f, 1);
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        v5.normal(0, 1, 0);
        v5.vertex(g1, 1.001, g2).uv(u, dv);
        v5.vertex(g2, 1.001, g2).uv(du, dv);
        v5.vertex(g2, 1.001, g1).uv(du, v);
        v5.vertex(g1, 1.001, g1).uv(u, v);
        tess.end();

        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        v5.normal(0, 1, 0);
        v5.vertex(g1, -0.001, g1).uv(u, v);
        v5.vertex(g2, -0.001, g1).uv(du, v);
        v5.vertex(g2, -0.001, g2).uv(du, dv);
        v5.vertex(g1, -0.001, g2).uv(u, dv);
        tess.end();
        stack.translate(-par2, -par4, -par6);
    }

    private void renderFace(RenderableDuct tile, double x, double y, double z, Direction dir) {
        float size = 0.75F / 2F;
        float window = 0.5F / 2F;
        float dl = size - window;
        float dd = 0.5F - size;

//        IIcon ico = tile.getBlockIcon();
        float u = 0;//todo ico.getMinU();
        float v = 1;//todo ico.getMinV();
        float u2 = 0;//todo  ico.getMaxU();
        float v2 = 1;//todo  ico.getMaxV();

        float ddu = u2 - u;
        float ddv = v2 - v;
        float uo = u;
        float vo = v;
        float u2o = u2;
        float v2o = v2;

        u += ddu * (1 - size) / 5;
        v += ddv * (1 - size) / 5;
        u2 -= ddu * (1 - size) / 5;
        v2 -= ddv * (1 - size) / 5;

        float du = ddu * dd;
        float dv = ddv * dd;

        float lx = dd + dl;
        float ly = dd + dl;
        float mx = 1 - dd - dl;
        float my = 1 - dd - dl;

//        IIcon gico = Blocks.GLASS.getIcon(0, 0);
        float gu = 1;// todo gico.getMinU();
        float gv = 1;// todo gico.getMinV();
        float gu2 = 1;// todo  gico.getMaxU();
        float gv2 = 1;// todo  gico.getMaxV();
        float dgu = gu2 - gu;
        float dgv = gv2 - gv;

        float guu = gu + dgu * dl;
        float gvv = gv + dgv * dl;

        gu += dgu / 8;
        gv += dgv / 8;
        gu2 -= dgu / 8;
        gv2 -= dgv / 8;

        Tesselator tess = Tesselator.getInstance();
        BufferBuilder v5 = tess.getBuilder();
        //stack.translate(x, y, z);

        this.faceBrightness(dir, tess);
        switch (dir) {
            case DOWN -> {
                v5.vertex(dd, 1 - dd, 1 - dd).uv(u, v);
                v5.vertex(dd + dl, 1 - dd, 1 - dd).uv(u + du, v);
                v5.vertex(dd + dl, 1 - dd, dd).uv(u + du, v2);
                v5.vertex(dd, 1 - dd, dd).uv(u, v2);
                v5.vertex(1 - dd - dl, 1 - dd, 1 - dd).uv(u2 - du, v);
                v5.vertex(1 - dd, 1 - dd, 1 - dd).uv(u2, v);
                v5.vertex(1 - dd, 1 - dd, dd).uv(u2, v2);
                v5.vertex(1 - dd - dl, 1 - dd, dd).uv(u2 - du, v2);
                v5.vertex(dd, 1 - dd, dd + dl).uv(u, v2 - dv);
                v5.vertex(1 - dd, 1 - dd, dd + dl).uv(u2, v2 - dv);
                v5.vertex(1 - dd, 1 - dd, dd).uv(u2, v2);
                v5.vertex(dd, 1 - dd, dd).uv(u, v2);
                v5.vertex(dd, 1 - dd, 1 - dd).uv(u, v);
                v5.vertex(1 - dd, 1 - dd, 1 - dd).uv(u2, v);
                v5.vertex(1 - dd, 1 - dd, 1 - dd - dl).uv(u2, v + dv);
                v5.vertex(dd, 1 - dd, 1 - dd - dl).uv(u, v + dv);
                v5.vertex(mx, 1 - dd, ly).uv(gu2, gv);
                v5.vertex(lx, 1 - dd, ly).uv(gu, gv);
                v5.vertex(lx, 1 - dd, my).uv(gu, gv2);
                v5.vertex(mx, 1 - dd, my).uv(gu2, gv2);
            }
            case NORTH -> {
                v5.vertex(dd, dd, 1 - dd).uv(u, v2);
                v5.vertex(dd + dl, dd, 1 - dd).uv(u + du, v2);
                v5.vertex(dd + dl, 1 - dd, 1 - dd).uv(u + du, v);
                v5.vertex(dd, 1 - dd, 1 - dd).uv(u, v);
                v5.vertex(1 - dd - dl, dd, 1 - dd).uv(u2 - du, v2);
                v5.vertex(1 - dd, dd, 1 - dd).uv(u2, v2);
                v5.vertex(1 - dd, 1 - dd, 1 - dd).uv(u2, v);
                v5.vertex(1 - dd - dl, 1 - dd, 1 - dd).uv(u2 - du, v);
                v5.vertex(dd, dd, 1 - dd).uv(u, v2);
                v5.vertex(1 - dd, dd, 1 - dd).uv(u2, v2);
                v5.vertex(1 - dd, dd + dl, 1 - dd).uv(u2, v2 - dv);
                v5.vertex(dd, dd + dl, 1 - dd).uv(u, v2 - dv);
                v5.vertex(dd, 1 - dd - dl, 1 - dd).uv(u, v + dv);
                v5.vertex(1 - dd, 1 - dd - dl, 1 - dd).uv(u2, v + dv);
                v5.vertex(1 - dd, 1 - dd, 1 - dd).uv(u2, v);
                v5.vertex(dd, 1 - dd, 1 - dd).uv(u, v);
                v5.vertex(mx, my, 1 - dd).uv(gu2, gv2);
                v5.vertex(lx, my, 1 - dd).uv(gu, gv2);
                v5.vertex(lx, ly, 1 - dd).uv(gu, gv);
                v5.vertex(mx, ly, 1 - dd).uv(gu2, gv);
            }
            case EAST -> {
                v5.vertex(1 - dd, 1 - dd, dd).uv(u, v);
                v5.vertex(1 - dd, 1 - dd, dd + dl).uv(u + du, v);
                v5.vertex(1 - dd, dd, dd + dl).uv(u + du, v2);
                v5.vertex(1 - dd, dd, dd).uv(u, v2);
                v5.vertex(1 - dd, 1 - dd, 1 - dd - dl).uv(u2 - du, v);
                v5.vertex(1 - dd, 1 - dd, 1 - dd).uv(u2, v);
                v5.vertex(1 - dd, dd, 1 - dd).uv(u2, v2);
                v5.vertex(1 - dd, dd, 1 - dd - dl).uv(u2 - du, v2);
                v5.vertex(1 - dd, dd + dl, dd).uv(u, v2 - dv);
                v5.vertex(1 - dd, dd + dl, 1 - dd).uv(u2, v2 - dv);
                v5.vertex(1 - dd, dd, 1 - dd).uv(u2, v2);
                v5.vertex(1 - dd, dd, dd).uv(u, v2);
                v5.vertex(1 - dd, 1 - dd, dd).uv(u, v);
                v5.vertex(1 - dd, 1 - dd, 1 - dd).uv(u2, v);
                v5.vertex(1 - dd, 1 - dd - dl, 1 - dd).uv(u2, v + dv);
                v5.vertex(1 - dd, 1 - dd - dl, dd).uv(u, v + dv);
                v5.vertex(1 - dd, ly, mx).uv(gu2, gv);
                v5.vertex(1 - dd, ly, lx).uv(gu, gv);
                v5.vertex(1 - dd, my, lx).uv(gu, gv2);
                v5.vertex(1 - dd, my, mx).uv(gu2, gv2);
            }
            case WEST -> {
                v5.vertex(dd, dd, dd).uv(u, v2);
                v5.vertex(dd, dd, dd + dl).uv(u + du, v2);
                v5.vertex(dd, 1 - dd, dd + dl).uv(u + du, v);
                v5.vertex(dd, 1 - dd, dd).uv(u, v);
                v5.vertex(dd, dd, 1 - dd - dl).uv(u2 - du, v2);
                v5.vertex(dd, dd, 1 - dd).uv(u2, v2);
                v5.vertex(dd, 1 - dd, 1 - dd).uv(u2, v);
                v5.vertex(dd, 1 - dd, 1 - dd - dl).uv(u2 - du, v);
                v5.vertex(dd, dd, dd).uv(u, v2);
                v5.vertex(dd, dd, 1 - dd).uv(u2, v2);
                v5.vertex(dd, dd + dl, 1 - dd).uv(u2, v2 - dv);
                v5.vertex(dd, dd + dl, dd).uv(u, v2 - dv);
                v5.vertex(dd, 1 - dd - dl, dd).uv(u, v + dv);
                v5.vertex(dd, 1 - dd - dl, 1 - dd).uv(u2, v + dv);
                v5.vertex(dd, 1 - dd, 1 - dd).uv(u2, v);
                v5.vertex(dd, 1 - dd, dd).uv(u, v);
                v5.vertex(dd, my, mx).uv(gu2, gv2);
                v5.vertex(dd, my, lx).uv(gu, gv2);
                v5.vertex(dd, ly, lx).uv(gu, gv);
                v5.vertex(dd, ly, mx).uv(gu2, gv);
            }
            case UP -> {
                v5.vertex(dd, dd, dd).uv(u, v2);
                v5.vertex(dd + dl, dd, dd).uv(u + du, v2);
                v5.vertex(dd + dl, dd, 1 - dd).uv(u + du, v);
                v5.vertex(dd, dd, 1 - dd).uv(u, v);
                v5.vertex(1 - dd - dl, dd, dd).uv(u2 - du, v2);
                v5.vertex(1 - dd, dd, dd).uv(u2, v2);
                v5.vertex(1 - dd, dd, 1 - dd).uv(u2, v);
                v5.vertex(1 - dd - dl, dd, 1 - dd).uv(u2 - du, v);
                v5.vertex(dd, dd, dd).uv(u, v2);
                v5.vertex(1 - dd, dd, dd).uv(u2, v2);
                v5.vertex(1 - dd, dd, dd + dl).uv(u2, v2 - dv);
                v5.vertex(dd, dd, dd + dl).uv(u, v2 - dv);
                v5.vertex(dd, dd, 1 - dd - dl).uv(u, v + dv);
                v5.vertex(1 - dd, dd, 1 - dd - dl).uv(u2, v + dv);
                v5.vertex(1 - dd, dd, 1 - dd).uv(u2, v);
                v5.vertex(dd, dd, 1 - dd).uv(u, v);
                v5.vertex(mx, dd, my).uv(gu2, gv2);
                v5.vertex(lx, dd, my).uv(gu, gv2);
                v5.vertex(lx, dd, ly).uv(gu, gv);
                v5.vertex(mx, dd, ly).uv(gu2, gv);
            }
            case SOUTH -> {
                v5.vertex(dd, 1 - dd, dd).uv(u, v);
                v5.vertex(dd + dl, 1 - dd, dd).uv(u + du, v);
                v5.vertex(dd + dl, dd, dd).uv(u + du, v2);
                v5.vertex(dd, dd, dd).uv(u, v2);
                v5.vertex(1 - dd - dl, 1 - dd, dd).uv(u2 - du, v);
                v5.vertex(1 - dd, 1 - dd, dd).uv(u2, v);
                v5.vertex(1 - dd, dd, dd).uv(u2, v2);
                v5.vertex(1 - dd - dl, dd, dd).uv(u2 - du, v2);
                v5.vertex(dd, dd + dl, dd).uv(u, v2 - dv);
                v5.vertex(1 - dd, dd + dl, dd).uv(u2, v2 - dv);
                v5.vertex(1 - dd, dd, dd).uv(u2, v2);
                v5.vertex(dd, dd, dd).uv(u, v2);
                v5.vertex(dd, 1 - dd, dd).uv(u, v);
                v5.vertex(1 - dd, 1 - dd, dd).uv(u2, v);
                v5.vertex(1 - dd, 1 - dd - dl, dd).uv(u2, v + dv);
                v5.vertex(dd, 1 - dd - dl, dd).uv(u, v + dv);
                v5.vertex(mx, ly, dd).uv(gu2, gv);
                v5.vertex(lx, ly, dd).uv(gu, gv);
                v5.vertex(lx, my, dd).uv(gu, gv2);
                v5.vertex(mx, my, dd).uv(gu2, gv2);
            }
            default -> {
            }
        }

        //stack.translate(-x, -y, -z);
    }

    private void faceBrightness(Direction dir, Tesselator v5) {
        float f = 1;
        switch (dir.getOpposite()) {
            case DOWN -> f = 0.4F;
            case EAST, WEST -> f = 0.5F;
            case NORTH, SOUTH -> f = 0.65F;
            case UP -> f = 1F;
            default -> {
            }
        }
//        todo v5.setColorOpaque_F(f, f, f);
    }
}
