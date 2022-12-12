///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders.dm;
//
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import com.mojang.blaze3d.vertex.BufferBuilder;
//import com.mojang.blaze3d.vertex.Tesselator;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.renderer.Tesselator;
//import net.minecraft.core.Direction;
//import net.minecraft.init.Blocks;
//
//import net.minecraftforge.client.MinecraftForgeClient;
//import net.minecraftforge.common.util.Direction;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.Animated.ModelBelt;
//import reika.rotarycraft.models.animated.ModelBelt;
//import reika.rotarycraft.tileentities.transmission.BlockEntityBeltHub;
//
//public class RenderBelt extends RotaryTERenderer {
//
//    private ModelBelt BeltModel = new ModelBelt();
//
//    @Override
//    protected String getTextureSubfolder() {
//        return "Transmission/";
//    }
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityBeltAt(BlockEntityBeltHub tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelBelt var14;
//
//        var14 = BeltModel;
//        this.bindTextureByName("/reika/rotarycraft/textures/blockentitytex/transmission/shaft/belttex.png");
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
//            int meta = tile.getBlockMetadata() % 6;
//            switch (meta) {
//                case 0:
//                    var11 = 0;
//                    break;
//                case 1:
//                    var11 = 180;
//                    break;
//                case 2:
//                    var11 = 270;
//                    break;
//                case 3:
//                    var11 = 90;
//                    break;
//                case 4:
//                    var11 = 270;
//                    break;
//                case 5:
//                    var11 = 90;
//                    break;
//            }
//
//            if (meta <= 3)
//                GL11.glRotatef((float) var11 + 90, 0.0F, 1.0F, 0.0F);
//            else {
//                stack.mulPose(new Quaternion(var11, 1F, 0F, 0.0F);
//                stack.translate(0F, -1F, 1F);
//                if (meta == 5)
//                    stack.translate(0F, 0F, -2F);
//            }
//        }
//
//        float var13;
//
//        var14.renderAll(stack, tile, null, tile.phi);
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityBeltAt((BlockEntityBeltHub) tile, par2, par4, par6, par8);
//        if (((BlockEntityBeltHub) tile).hasValidConnection()) {
//            GL11.glDisable(GL11.GL_CULL_FACE);
//            //this.drawBelt((BlockEntityBeltHub)tile, par2, par4, par6, par8);
//            this.drawBelt2((BlockEntityBeltHub) tile, par2, par4, par6, par8);
//            GL11.glEnable(GL11.GL_CULL_FACE);
//        }
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(tile, par2, par4, par6);
//        //ReikaAABBHelper.renderAABB(tile.getRenderBoundingBox(), par2, par4, par6, 0, 0, 0, 127, 255, 255, 0, true);
//    }
//
//    private void drawBelt2(BlockEntityBeltHub tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int meta = tile.getBlockMetadata();
//        boolean vertical = meta == 4 || meta == 5 || meta == 10 || meta == 11;
//        Direction dir = tile.getBeltDirection();
//        int dist = tile.getDistanceToTarget();
//        boolean emit = tile.isEmitting;
//        stack.translate(par2, par4, par6);
//        GL11.glDisable(GL11.GL_TEXTURE_2D);
//        GL11.glDisable(GL11.GL_LIGHTING);
//        Tesselator tess = Tesselator.getInstance();
//        BufferBuilder v5 = tess.getBuilder();
//
//        double rx = 0;
//        double ry = 0;
//        double rz = 0;
//
//        double dx = -0.5;
//        double dy = -0.5;
//        double dz = -0.5;
//
//        switch (dir) {
//            case WEST:
//                if (vertical) {
//                    rx = 90;
//                }
//                ry = 180;
//                break;
//            case EAST:
//                if (vertical) {
//                    rx = 90;
//                }
//                break;
//            case NORTH:
//                if (vertical) {
//                    rz = 90;
//                    rx = -90;
//                } else
//                    ry = 90;
//                break;
//            case SOUTH:
//                if (vertical) {
//                    rz = 90;
//                    rx = 90;
//                } else
//                    ry = -90;
//                break;
//            case UP:
//                if (meta == 0 || meta == 1) {
//                    ry = 90;
//                    rz = 90;
//                } else
//                    rz = 90;
//                break;
//            case DOWN:
//                if (meta == 0 || meta == 1) {
//                    ry = 90;
//                    rz = -90;
//                } else
//                    rz = -90;
//                break;
//            default:
//                break;
//        }
//
//        stack.translate(0.5, 0.5, 0.5);
//        GL11.glRotated(rx, 1, 0, 0);
//        GL11.glRotated(ry, 0, 1, 0);
//        GL11.glRotated(rz, 0, 0, 1);
//        stack.translate(dx, dy, dz);
//
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        int[] color = tile.getBeltColor();
//        v5.setColorOpaque(color[0], color[1], color[2]);
//
//        v5.vertex(0.125, 0.375, 0.375);
//        v5.vertex(0.125, 0.625, 0.375);
//        v5.vertex(0.125, 0.625, 0.625);
//        v5.vertex(0.125, 0.375, 0.625);
//
//        v5.vertex(0.125, 0.625, 0.625);
//        v5.vertex(0.375, 0.875, 0.625);
//        v5.vertex(0.375, 0.875, 0.375);
//        v5.vertex(0.125, 0.625, 0.375);
//
//        v5.vertex(0.125, 0.375, 0.625);
//        v5.vertex(0.375, 0.125, 0.625);
//        v5.vertex(0.375, 0.125, 0.375);
//        v5.vertex(0.125, 0.375, 0.375);
//
//        if (emit) {
//            v5.vertex(0.375, 0.875, 0.375);
//            v5.vertex(0.625 + dist, 0.875, 0.375);
//            v5.vertex(0.625 + dist, 0.875, 0.625);
//            v5.vertex(0.375, 0.875, 0.625);
//
//            v5.vertex(0.375, 0.125, 0.375);
//            v5.vertex(0.625 + dist, 0.125, 0.375);
//            v5.vertex(0.625 + dist, 0.125, 0.625);
//            v5.vertex(0.375, 0.125, 0.625);
//        }
//
//        v5.end();
//
//        stack.translate(-dx, -dy, -dz);
//        GL11.glRotated(-rz, 0, 0, 1);
//        GL11.glRotated(-ry, 0, 1, 0);
//        GL11.glRotated(-rx, 1, 0, 0);
//        stack.translate(-0.5, -0.5, -0.5);
//
//        GL11.glEnable(GL11.GL_LIGHTING);
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        stack.translate(-par2, -par4, -par6);
//    }
//
//    private void drawBelt3(BlockEntityBeltHub tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int meta = tile.getBlockMetadata();
//        boolean vertical = meta == 4 || meta == 5;
//        Direction dir = tile.getBeltDirection();
//        int dist = tile.getDistanceToTarget();
//        stack.translate(par2, par4, par6);
//        //GL11.glDisable(GL11.GL_TEXTURE_2D);
//        ReikaTextureHelper.bindTerrainTexture();
//        GL11.glDisable(GL11.GL_LIGHTING);
//        Tesselator tess = Tesselator.getInstance();
//        BufferBuilder v5 = tess.getBuilder();
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        int[] color = tile.getBeltColor();
//        v5.setColorOpaque(color[0], color[1], color[2]);
//
//        IIcon ico = Blocks.grass.getIcon(1, 0);
//        float u = ico.getMinU();
//        float v = ico.getMinV();
//        float du = ico.getMaxU();
//        float dv = ico.getMaxV();
//
//        v5.addVertexWithUV(0.125, 0.375, 0.375, u, v);
//        v5.addVertexWithUV(0.125, 0.625, 0.375, du, v);
//        v5.addVertexWithUV(0.125, 0.625, 0.625, du, dv);
//        v5.addVertexWithUV(0.125, 0.375, 0.625, u, dv);
//
//        switch (dir) {
//            case EAST:
//                if (meta == 2 || meta == 3) {
//                    v5.addVertexWithUV(0.375, 0.875, 0.375, u, v);
//                    v5.addVertexWithUV(0.625 + dist, 0.875, 0.375, du, v);
//                    v5.addVertexWithUV(0.625 + dist, 0.875, 0.625, du, dv);
//                    v5.addVertexWithUV(0.375, 0.875, 0.625, u, dv);
//
//                    v5.addVertexWithUV(0.375, 0.125, 0.375, u, v);
//                    v5.addVertexWithUV(0.625 + dist, 0.125, 0.375, du, v);
//                    v5.addVertexWithUV(0.625 + dist, 0.125, 0.625, du, dv);
//                    v5.addVertexWithUV(0.375, 0.125, 0.625, u, dv);
//                }
//
//                if (vertical) {
//                    v5.addVertexWithUV(0.375, 0.375, 0.875, u, v);
//                    v5.addVertexWithUV(0.625 + dist, 0.375, 0.875, du, v);
//                    v5.addVertexWithUV(0.625 + dist, 0.625, 0.875, du, dv);
//                    v5.addVertexWithUV(0.375, 0.625, 0.875, u, dv);
//
//                    v5.addVertexWithUV(0.375, 0.375, 0.125, u, v);
//                    v5.addVertexWithUV(0.625 + dist, 0.375, 0.125, du, v);
//                    v5.addVertexWithUV(0.625 + dist, 0.625, 0.125, du, dv);
//                    v5.addVertexWithUV(0.375, 0.625, 0.125, u, dv);
//                }
//                break;
//            case WEST:
//                if (meta == 2 || meta == 3) {
//                    v5.addVertexWithUV(0.375 - dist, 0.875, 0.375, u, v);
//                    v5.addVertexWithUV(0.625, 0.875, 0.375, du, v);
//                    v5.addVertexWithUV(0.625, 0.875, 0.625, du, dv);
//                    v5.addVertexWithUV(0.375 - dist, 0.875, 0.625, u, dv);
//
//                    v5.addVertexWithUV(0.375 - dist, 0.125, 0.375, u, v);
//                    v5.addVertexWithUV(0.625, 0.125, 0.375, du, v);
//                    v5.addVertexWithUV(0.625, 0.125, 0.625, du, dv);
//                    v5.addVertexWithUV(0.375 - dist, 0.125, 0.625, u, dv);
//                }
//                if (vertical) {
//                    v5.addVertexWithUV(0.375 - dist, 0.375, 0.875, u, v);
//                    v5.addVertexWithUV(0.625, 0.375, 0.875, du, v);
//                    v5.addVertexWithUV(0.625, 0.625, 0.875, du, dv);
//                    v5.addVertexWithUV(0.375 - dist, 0.625, 0.875, u, dv);
//
//                    v5.addVertexWithUV(0.375 - dist, 0.375, 0.125, u, v);
//                    v5.addVertexWithUV(0.625, 0.375, 0.125, du, v);
//                    v5.addVertexWithUV(0.625, 0.625, 0.125, du, dv);
//                    v5.addVertexWithUV(0.375 - dist, 0.625, 0.125, u, dv);
//                }
//                break;
//            case NORTH:
//                if (meta == 0 || meta == 1) {
//                    v5.addVertexWithUV(0.375, 0.875, 0.375 - dist, u, v);
//                    v5.addVertexWithUV(0.625, 0.875, 0.375 - dist, du, v);
//                    v5.addVertexWithUV(0.625, 0.875, 0.625, du, dv);
//                    v5.addVertexWithUV(0.375, 0.875, 0.625, u, dv);
//
//                    v5.addVertexWithUV(0.375, 0.125, 0.375 - dist, u, v);
//                    v5.addVertexWithUV(0.625, 0.125, 0.375 - dist, du, v);
//                    v5.addVertexWithUV(0.625, 0.125, 0.625, du, dv);
//                    v5.addVertexWithUV(0.375, 0.125, 0.625, u, dv);
//                }
//                if (vertical) {
//                    v5.addVertexWithUV(0.875, 0.375, 0.375, u, v);
//                    v5.addVertexWithUV(0.875, 0.375, 0.625 - dist, du, v);
//                    v5.addVertexWithUV(0.875, 0.625, 0.625 - dist, du, dv);
//                    v5.addVertexWithUV(0.875, 0.625, 0.375, u, dv);
//
//                    v5.addVertexWithUV(0.125, 0.375, 0.375, u, v);
//                    v5.addVertexWithUV(0.125, 0.375, 0.625 - dist, du, v);
//                    v5.addVertexWithUV(0.125, 0.625, 0.625 - dist, du, dv);
//                    v5.addVertexWithUV(0.125, 0.625, 0.375, u, dv);
//                }
//                break;
//            case SOUTH:
//                if (meta == 0 || meta == 1) {
//                    v5.addVertexWithUV(0.375, 0.875, 0.375 + dist, u, v);
//                    v5.addVertexWithUV(0.625, 0.875, 0.375 + dist, du, v);
//                    v5.addVertexWithUV(0.625, 0.875, 0.625, du, dv);
//                    v5.addVertexWithUV(0.375, 0.875, 0.625, u, dv);
//
//                    v5.addVertexWithUV(0.375, 0.125, 0.375 + dist, u, v);
//                    v5.addVertexWithUV(0.625, 0.125, 0.375 + dist, du, v);
//                    v5.addVertexWithUV(0.625, 0.125, 0.625, du, dv);
//                    v5.addVertexWithUV(0.375, 0.125, 0.625, u, dv);
//                }
//                if (vertical) {
//                    v5.addVertexWithUV(0.875, 0.375, 0.375 + dist, u, v);
//                    v5.addVertexWithUV(0.875, 0.375, 0.625, du, v);
//                    v5.addVertexWithUV(0.875, 0.625, 0.625, du, dv);
//                    v5.addVertexWithUV(0.875, 0.625, 0.375 + dist, u, dv);
//
//                    v5.addVertexWithUV(0.125, 0.375, 0.375 + dist, u, v);
//                    v5.addVertexWithUV(0.125, 0.375, 0.625, du, v);
//                    v5.addVertexWithUV(0.125, 0.625, 0.625, du, dv);
//                    v5.addVertexWithUV(0.125, 0.625, 0.375 + dist, u, dv);
//                }
//                break;
//            case UP:
//                if (meta == 0 || meta == 1) {
//                    v5.addVertexWithUV(0.375, 0.375, 0.125, u, v);
//                    v5.addVertexWithUV(0.375, 0.625 + dist, 0.125, du, v);
//                    v5.addVertexWithUV(0.625, 0.625 + dist, 0.125, du, dv);
//                    v5.addVertexWithUV(0.625, 0.375, 0.125, u, dv);
//
//                    v5.addVertexWithUV(0.375, 0.375, 0.875, u, v);
//                    v5.addVertexWithUV(0.375, 0.625 + dist, 0.875, du, v);
//                    v5.addVertexWithUV(0.625, 0.625 + dist, 0.875, du, dv);
//                    v5.addVertexWithUV(0.625, 0.375, 0.875, u, dv);
//                }
//                if (meta == 2 || meta == 3) {
//                    v5.addVertexWithUV(0.125, 0.375, 0.375, u, v);
//                    v5.addVertexWithUV(0.125, 0.625 + dist, 0.375, du, v);
//                    v5.addVertexWithUV(0.125, 0.625 + dist, 0.625, du, dv);
//                    v5.addVertexWithUV(0.125, 0.375, 0.625, u, dv);
//
//                    v5.addVertexWithUV(0.875, 0.375, 0.375, u, v);
//                    v5.addVertexWithUV(0.875, 0.625 + dist, 0.375, du, v);
//                    v5.addVertexWithUV(0.875, 0.625 + dist, 0.625, du, dv);
//                    v5.addVertexWithUV(0.875, 0.375, 0.625, u, dv);
//                }
//                break;
//            case DOWN:
//                if (meta == 0 || meta == 1) {
//                    v5.addVertexWithUV(0.375, 0.375 - dist, 0.125, u, v);
//                    v5.addVertexWithUV(0.375, 0.625, 0.125, du, v);
//                    v5.addVertexWithUV(0.625, 0.625, 0.125, du, dv);
//                    v5.addVertexWithUV(0.625, 0.375 - dist, 0.125, u, dv);
//
//                    v5.addVertexWithUV(0.375, 0.375 - dist, 0.875, u, v);
//                    v5.addVertexWithUV(0.375, 0.625, 0.875, du, v);
//                    v5.addVertexWithUV(0.625, 0.625, 0.875, du, dv);
//                    v5.addVertexWithUV(0.625, 0.375 - dist, 0.875, u, dv);
//                }
//                if (meta == 2 || meta == 3) {
//                    v5.addVertexWithUV(0.125, 0.375 - dist, 0.375, u, v);
//                    v5.addVertexWithUV(0.125, 0.625, 0.375, du, v);
//                    v5.addVertexWithUV(0.125, 0.625, 0.625, du, dv);
//                    v5.addVertexWithUV(0.125, 0.375 - dist, 0.625, u, dv);
//
//                    v5.addVertexWithUV(0.875, 0.375 - dist, 0.375, u, v);
//                    v5.addVertexWithUV(0.875, 0.625, 0.375, du, v);
//                    v5.addVertexWithUV(0.875, 0.625, 0.625, du, dv);
//                    v5.addVertexWithUV(0.875, 0.375 - dist, 0.625, u, dv);
//                }
//                break;
//            default:
//                break;
//        }
//
//        v5.end();
//        GL11.glEnable(GL11.GL_LIGHTING);
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        stack.translate(-par2, -par4, -par6);
//    }
//
//    private void drawBelt(BlockEntityBeltHub tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int meta = tile.getBlockMetadata();
//        boolean side = meta % 2 == 0;
//        Direction dir = tile.getBeltDirection();
//        int dist = tile.getDistanceToTarget();
//        stack.translate(par2, par4, par6);
//        GL11.glDisable(GL11.GL_TEXTURE_2D);
//        GL11.glDisable(GL11.GL_LIGHTING);
//        Tesselator tess = Tesselator.getInstance();
//        BufferBuilder v5 = tess.getBuilder();
//        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//        v5.setColorOpaque(192, 120, 70);
//
//        //Top
//        if (dir != Direction.UP) {
//            v5.vertex(0.375, 0.875, 0.375);
//            v5.vertex(0.625, 0.875, 0.375);
//            v5.vertex(0.625, 0.875, 0.625);
//            v5.vertex(0.375, 0.875, 0.625);
//        }
//
//        //Bottom
//        if (dir != Direction.DOWN) {
//            v5.vertex(0.375, 0.125, 0.375);
//            v5.vertex(0.625, 0.125, 0.375);
//            v5.vertex(0.625, 0.125, 0.625);
//            v5.vertex(0.375, 0.125, 0.625);
//        }
//
//        //East
//        if (dir != Direction.EAST) {
//            v5.vertex(0.875, 0.375, 0.375);
//            v5.vertex(0.875, 0.625, 0.375);
//            v5.vertex(0.875, 0.625, 0.625);
//            v5.vertex(0.875, 0.375, 0.625);
//        }
//
//        //West
//        if (dir != Direction.WEST) {
//            v5.vertex(0.125, 0.375, 0.375);
//            v5.vertex(0.125, 0.625, 0.375);
//            v5.vertex(0.125, 0.625, 0.625);
//            v5.vertex(0.125, 0.375, 0.625);
//        }
//
//        //North
//        if (dir != Direction.NORTH) {
//            v5.vertex(0.375, 0.375, 0.125);
//            v5.vertex(0.375, 0.625, 0.125);
//            v5.vertex(0.625, 0.625, 0.125);
//            v5.vertex(0.625, 0.375, 0.125);
//        }
//
//        //South
//        if (dir != Direction.SOUTH) {
//            v5.vertex(0.375, 0.375, 0.875);
//            v5.vertex(0.375, 0.625, 0.875);
//            v5.vertex(0.625, 0.625, 0.875);
//            v5.vertex(0.625, 0.375, 0.875);
//        }
//
//        if (dir == Direction.EAST) {
//            v5.vertex(0.875 + dist, 0.375, 0.375);
//            v5.vertex(0.875 + dist, 0.625, 0.375);
//            v5.vertex(0.875 + dist, 0.625, 0.625);
//            v5.vertex(0.875 + dist, 0.375, 0.625);
//
//            if (meta == 2 || meta == 3) {
//                v5.vertex(0.125, 0.625, 0.625);
//                v5.vertex(0.375, 0.875, 0.625);
//                v5.vertex(0.375, 0.875, 0.375);
//                v5.vertex(0.125, 0.625, 0.375);
//
//                v5.vertex(0.125, 0.375, 0.625);
//                v5.vertex(0.375, 0.125, 0.625);
//                v5.vertex(0.375, 0.125, 0.375);
//                v5.vertex(0.125, 0.375, 0.375);
//
//                //Other end
//                v5.vertex(0.875 + dist, 0.625, 0.625);
//                v5.vertex(0.625 + dist, 0.875, 0.625);
//                v5.vertex(0.625 + dist, 0.875, 0.375);
//                v5.vertex(0.875 + dist, 0.625, 0.375);
//
//                v5.vertex(0.875 + dist, 0.375, 0.625);
//                v5.vertex(0.625 + dist, 0.125, 0.625);
//                v5.vertex(0.625 + dist, 0.125, 0.375);
//                v5.vertex(0.875 + dist, 0.375, 0.375);
//            }
//
//            if (meta == 4 || meta == 5) {
//                v5.vertex(0.375, 0.375, 0.875);
//                v5.vertex(0.625 + dist, 0.375, 0.875);
//                v5.vertex(0.625 + dist, 0.625, 0.875);
//                v5.vertex(0.375, 0.625, 0.875);
//
//                v5.vertex(0.375, 0.375, 0.125);
//                v5.vertex(0.625 + dist, 0.375, 0.125);
//                v5.vertex(0.625 + dist, 0.625, 0.125);
//                v5.vertex(0.375, 0.625, 0.125);
//            }
//        }
//        if (dir == Direction.WEST) {
//            v5.vertex(0.125 - dist, 0.375, 0.375);
//            v5.vertex(0.125 - dist, 0.625, 0.375);
//            v5.vertex(0.125 - dist, 0.625, 0.625);
//            v5.vertex(0.125 - dist, 0.375, 0.625);
//
//            if (meta == 2 || meta == 3) {
//                v5.vertex(0.875, 0.625, 0.625);
//                v5.vertex(0.625, 0.875, 0.625);
//                v5.vertex(0.625, 0.875, 0.375);
//                v5.vertex(0.875, 0.625, 0.375);
//
//                v5.vertex(0.875, 0.375, 0.625);
//                v5.vertex(0.625, 0.125, 0.625);
//                v5.vertex(0.625, 0.125, 0.375);
//                v5.vertex(0.875, 0.375, 0.375);
//
//                //Other end
//                v5.vertex(0.125 - dist, 0.625, 0.625);
//                v5.vertex(0.375 - dist, 0.875, 0.625);
//                v5.vertex(0.375 - dist, 0.875, 0.375);
//                v5.vertex(0.125 - dist, 0.625, 0.375);
//
//                v5.vertex(0.125 - dist, 0.375, 0.625);
//                v5.vertex(0.375 - dist, 0.125, 0.625);
//                v5.vertex(0.375 - dist, 0.125, 0.375);
//                v5.vertex(0.125 - dist, 0.375, 0.375);
//            }
//        } else if (dir == Direction.UP) {
//            v5.vertex(0.375, 0.875 + dist, 0.375);
//            v5.vertex(0.625, 0.875 + dist, 0.375);
//            v5.vertex(0.625, 0.875 + dist, 0.625);
//            v5.vertex(0.375, 0.875 + dist, 0.625);
//        } else if (dir == Direction.DOWN) {
//            v5.vertex(0.375, 0.125 - dist, 0.375);
//            v5.vertex(0.625, 0.125 - dist, 0.375);
//            v5.vertex(0.625, 0.125 - dist, 0.625);
//            v5.vertex(0.375, 0.125 - dist, 0.625);
//        } else if (dir == Direction.SOUTH) {
//            v5.vertex(0.375, 0.375, 0.875 + dist);
//            v5.vertex(0.375, 0.625, 0.875 + dist);
//            v5.vertex(0.625, 0.625, 0.875 + dist);
//            v5.vertex(0.625, 0.375, 0.875 + dist);
//        } else if (dir == Direction.NORTH) {
//            v5.vertex(0.375, 0.375, 0.125 - dist);
//            v5.vertex(0.375, 0.625, 0.125 - dist);
//            v5.vertex(0.625, 0.625, 0.125 - dist);
//            v5.vertex(0.625, 0.375, 0.125 - dist);
//        }
//
//        switch (dir) {
//            case EAST:
//                if (meta == 2 || meta == 3) {
//                    v5.vertex(0.375, 0.875, 0.375);
//                    v5.vertex(0.625 + dist, 0.875, 0.375);
//                    v5.vertex(0.625 + dist, 0.875, 0.625);
//                    v5.vertex(0.375, 0.875, 0.625);
//
//                    v5.vertex(0.375, 0.125, 0.375);
//                    v5.vertex(0.625 + dist, 0.125, 0.375);
//                    v5.vertex(0.625 + dist, 0.125, 0.625);
//                    v5.vertex(0.375, 0.125, 0.625);
//                }
//
//                if (meta == 4 || meta == 5) {
//                    v5.vertex(0.375, 0.375, 0.875);
//                    v5.vertex(0.625 + dist, 0.375, 0.875);
//                    v5.vertex(0.625 + dist, 0.625, 0.875);
//                    v5.vertex(0.375, 0.625, 0.875);
//
//                    v5.vertex(0.375, 0.375, 0.125);
//                    v5.vertex(0.625 + dist, 0.375, 0.125);
//                    v5.vertex(0.625 + dist, 0.625, 0.125);
//                    v5.vertex(0.375, 0.625, 0.125);
//                }
//                break;
//            case WEST:
//                if (meta == 2 || meta == 3) {
//                    v5.vertex(0.375 - dist, 0.875, 0.375);
//                    v5.vertex(0.625, 0.875, 0.375);
//                    v5.vertex(0.625, 0.875, 0.625);
//                    v5.vertex(0.375 - dist, 0.875, 0.625);
//
//                    v5.vertex(0.375 - dist, 0.125, 0.375);
//                    v5.vertex(0.625, 0.125, 0.375);
//                    v5.vertex(0.625, 0.125, 0.625);
//                    v5.vertex(0.375 - dist, 0.125, 0.625);
//                }
//                if (meta == 4 || meta == 5) {
//                    v5.vertex(0.375 - dist, 0.375, 0.875);
//                    v5.vertex(0.625, 0.375, 0.875);
//                    v5.vertex(0.625, 0.625, 0.875);
//                    v5.vertex(0.375 - dist, 0.625, 0.875);
//
//                    v5.vertex(0.375 - dist, 0.375, 0.125);
//                    v5.vertex(0.625, 0.375, 0.125);
//                    v5.vertex(0.625, 0.625, 0.125);
//                    v5.vertex(0.375 - dist, 0.625, 0.125);
//                }
//                break;
//            case NORTH:
//                if (meta == 0 || meta == 1) {
//                    v5.vertex(0.375, 0.875, 0.375 - dist);
//                    v5.vertex(0.625, 0.875, 0.375 - dist);
//                    v5.vertex(0.625, 0.875, 0.625);
//                    v5.vertex(0.375, 0.875, 0.625);
//
//                    v5.vertex(0.375, 0.125, 0.375 - dist);
//                    v5.vertex(0.625, 0.125, 0.375 - dist);
//                    v5.vertex(0.625, 0.125, 0.625);
//                    v5.vertex(0.375, 0.125, 0.625);
//                }
//                if (meta == 4 || meta == 5) {
//                    v5.vertex(0.875, 0.375, 0.375);
//                    v5.vertex(0.875, 0.375, 0.625 - dist);
//                    v5.vertex(0.875, 0.625, 0.625 - dist);
//                    v5.vertex(0.875, 0.625, 0.375);
//
//                    v5.vertex(0.125, 0.375, 0.375);
//                    v5.vertex(0.125, 0.375, 0.625 - dist);
//                    v5.vertex(0.125, 0.625, 0.625 - dist);
//                    v5.vertex(0.125, 0.625, 0.375);
//                }
//                break;
//            case SOUTH:
//                if (meta == 0 || meta == 1) {
//                    v5.vertex(0.375, 0.875, 0.375 + dist);
//                    v5.vertex(0.625, 0.875, 0.375 + dist);
//                    v5.vertex(0.625, 0.875, 0.625);
//                    v5.vertex(0.375, 0.875, 0.625);
//
//                    v5.vertex(0.375, 0.125, 0.375 + dist);
//                    v5.vertex(0.625, 0.125, 0.375 + dist);
//                    v5.vertex(0.625, 0.125, 0.625);
//                    v5.vertex(0.375, 0.125, 0.625);
//                }
//                if (meta == 4 || meta == 5) {
//                    v5.vertex(0.875, 0.375, 0.375 + dist);
//                    v5.vertex(0.875, 0.375, 0.625);
//                    v5.vertex(0.875, 0.625, 0.625);
//                    v5.vertex(0.875, 0.625, 0.375 + dist);
//
//                    v5.vertex(0.125, 0.375, 0.375 + dist);
//                    v5.vertex(0.125, 0.375, 0.625);
//                    v5.vertex(0.125, 0.625, 0.625);
//                    v5.vertex(0.125, 0.625, 0.375 + dist);
//                }
//                break;
//            case UP:
//                if (meta == 0 || meta == 1) {
//                    v5.vertex(0.375, 0.375, 0.125);
//                    v5.vertex(0.375, 0.625 + dist, 0.125);
//                    v5.vertex(0.625, 0.625 + dist, 0.125);
//                    v5.vertex(0.625, 0.375, 0.125);
//
//                    v5.vertex(0.375, 0.375, 0.875);
//                    v5.vertex(0.375, 0.625 + dist, 0.875);
//                    v5.vertex(0.625, 0.625 + dist, 0.875);
//                    v5.vertex(0.625, 0.375, 0.875);
//                }
//                if (meta == 2 || meta == 3) {
//                    v5.vertex(0.125, 0.375, 0.375);
//                    v5.vertex(0.125, 0.625 + dist, 0.375);
//                    v5.vertex(0.125, 0.625 + dist, 0.625);
//                    v5.vertex(0.125, 0.375, 0.625);
//
//                    v5.vertex(0.875, 0.375, 0.375);
//                    v5.vertex(0.875, 0.625 + dist, 0.375);
//                    v5.vertex(0.875, 0.625 + dist, 0.625);
//                    v5.vertex(0.875, 0.375, 0.625);
//                }
//                break;
//            case DOWN:
//                if (meta == 0 || meta == 1) {
//                    v5.vertex(0.375, 0.375 - dist, 0.125);
//                    v5.vertex(0.375, 0.625, 0.125);
//                    v5.vertex(0.625, 0.625, 0.125);
//                    v5.vertex(0.625, 0.375 - dist, 0.125);
//
//                    v5.vertex(0.375, 0.375 - dist, 0.875);
//                    v5.vertex(0.375, 0.625, 0.875);
//                    v5.vertex(0.625, 0.625, 0.875);
//                    v5.vertex(0.625, 0.375 - dist, 0.875);
//                }
//                if (meta == 2 || meta == 3) {
//                    v5.vertex(0.125, 0.375 - dist, 0.375);
//                    v5.vertex(0.125, 0.625, 0.375);
//                    v5.vertex(0.125, 0.625, 0.625);
//                    v5.vertex(0.125, 0.375 - dist, 0.625);
//
//                    v5.vertex(0.875, 0.375 - dist, 0.375);
//                    v5.vertex(0.875, 0.625, 0.375);
//                    v5.vertex(0.875, 0.625, 0.625);
//                    v5.vertex(0.875, 0.375 - dist, 0.625);
//                }
//                break;
//            default:
//                break;
//        }
//
//        v5.end();
//        GL11.glEnable(GL11.GL_LIGHTING);
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        stack.translate(-par2, -par4, -par6);
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "belttex.png";
//    }
//}
