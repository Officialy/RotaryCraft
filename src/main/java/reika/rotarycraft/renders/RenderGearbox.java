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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
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
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.RotaryModelLayers;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class RenderGearbox extends RotaryTERenderer<BlockEntityGearbox> {

    private GearboxModel gearboxModel;
    private Gearbox4Model gearboxModel4;
    private Gearbox8Model gearboxModel8;
    private Gearbox16Model gearboxModel16;

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
    public void renderBlockEntityGearboxAt(PoseStack stack, BlockEntityGearbox tile, MultiBufferSource bufferSource, int light, int overlay) {
//        this.setupGL(stack, tile, par2, par4, par6);

        int var11 = 0;     //used to rotate the model about metadata
        if (tile.isInWorld()) {
  /*todo          switch (tile.getBlockMetadata() & 3) {
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
            stack.mulPose(new Quaternionf(var11, 0.0F, 1.0F, 0.0F));
            ArrayList li = ReikaJavaLibrary.makeListFrom(tile.getBearingTier());

            switch (tile.getRatio()) {
                case 2 -> {
                    VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.tryParse(GearboxModel.TEXTURE_LOCATION + tile.getGearboxType().getBaseGearboxTexture())));
                    gearboxModel.renderAll(stack, vertexconsumer, light, tile, li, -tile.phi);
                }
                case 4 -> {
                    VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.tryParse(Gearbox4Model.TEXTURE_LOCATION + tile.getGearboxType().getBaseGearboxTexture())));
                    gearboxModel4.renderAll(stack, vertexconsumer, light, tile, li, -tile.phi);
                }
                case 8 -> {
                    VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.tryParse(Gearbox8Model.TEXTURE_LOCATION + tile.getGearboxType().getBaseGearboxTexture())));
                    gearboxModel8.renderAll(stack, vertexconsumer, light, tile, li, -tile.phi);
                }
                case 16 -> {
                    VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.tryParse(Gearbox16Model.TEXTURE_LOCATION + tile.getGearboxType().getBaseGearboxTexture())));
                    gearboxModel16.renderAll(stack, vertexconsumer, light, tile, li, -tile.phi);
                }
            }

        } else {
            //ReikaChatHelper.write(this.itemMetadata);
            stack.mulPose(new Quaternionf(-90, 0.0F, 1.0F, 0.0F));
            switch (tile.getRatio()) {
                case 2 -> {
                    VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.tryParse(GearboxModel.TEXTURE_LOCATION + tile.getGearboxType().getBaseGearboxTexture())));
                    gearboxModel.renderAll(stack, vertexconsumer, light, tile, null);
                }
                case 4 -> {
                    VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.tryParse(Gearbox4Model.TEXTURE_LOCATION + tile.getGearboxType().getBaseGearboxTexture())));
                    gearboxModel4.renderAll(stack, vertexconsumer, light, tile, null);
                }
                case 8 -> {
                    VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.tryParse(Gearbox8Model.TEXTURE_LOCATION + tile.getGearboxType().getBaseGearboxTexture())));
                    gearboxModel8.renderAll(stack, vertexconsumer, light, tile, null);
                }
                case 16 -> {
                    VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutout(ResourceLocation.tryParse(Gearbox16Model.TEXTURE_LOCATION + tile.getGearboxType().getBaseGearboxTexture())));
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
            stack.mulPose(new Quaternionf(var11, 0.0F, 1.0F, 0.0F));
            String s = tile.reduction ? "Torque" : "Speed";
            Minecraft.getInstance().font.draw(stack, s, 0, 0, 0xffffff);
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
            RenderSystem.enableBlend();
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

    @Override
    public void render(BlockEntityGearbox tile, float p_112308_, PoseStack stack, MultiBufferSource bufferSource, int packetLight, int overlay) {
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
}
