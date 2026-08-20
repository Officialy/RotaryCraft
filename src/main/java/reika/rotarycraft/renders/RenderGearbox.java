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
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
// 26.2: VertexConsumer removed from BER submission path.
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.ModList;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.base.model.GearboxBaseModel;
import reika.rotarycraft.blockentities.transmission.BlockEntityGearbox;
import reika.rotarycraft.models.animated.Gearbox16Model;
import reika.rotarycraft.models.animated.Gearbox4Model;
import reika.rotarycraft.models.animated.Gearbox8Model;
import reika.rotarycraft.models.animated.GearboxModel;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.RotaryModelLayers;

import java.lang.reflect.Field;

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

    private GearboxBaseModel getModel(BlockEntityGearbox tile) {
        return switch (tile.getRatio()) {
            case 4 -> gearboxModel4;
            case 8 -> gearboxModel8;
            case 16 -> gearboxModel16;
            default -> gearboxModel;
        };
    }

    /**
     * The yaw the 1.7.10 renderer applied for each gearbox orientation. It switched on
     * {@code metadata & 3} (0/1/2/3 = read EAST/WEST/SOUTH/NORTH, per
     * {@code TileEntity1DTransmitter.getIOSides}) with the angles 0/180/90/270; the port stores the
     * <em>opposite</em> of the read side in {@code FACING}, so the same four cases come out as
     * WEST/EAST/NORTH/SOUTH — which is exactly {@code toYRot() + 270}.
     */
    private static float getModelYaw(Direction facing) {
        return (facing.toYRot() + 270F) % 360F;
    }

    /**
     * 1.7.10 {@code RotaryTERenderer.setupGL} plus the gearbox's own facing rotation.
     *
     * <p>{@code setupGL} did {@code translate(x,y,z); scale(1,-1,-1); translate(0.5,0.5,0.5);
     * translate(0,-2,-1)}, i.e. a translation to the block's top-centre followed by a 180° flip
     * about X ({@code scale(1,-1,-1)} <em>is</em> {@code Rx(180)}); the flip is expressed as a
     * rotation here so the normal matrix stays well-defined.
     */
    private static void setupPose(PoseStack stack, BlockEntityGearbox tile) {
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.XP.rotationDegrees(180));

        if (!tile.isInWorld()) {
            stack.mulPose(Axis.YP.rotationDegrees(-90));
            return;
        }

        Direction facing = tile.getBlockState().getValue(BlockRotaryCraftMachine.FACING);
        if (tile.isFlipped) {
            // setupGL's ceiling-mount branch: flip the machine over and drop it back into its block.
            stack.mulPose(Axis.XP.rotationDegrees(180));
            stack.translate(0, -2, 0);
            // Legacy `metadata > 1`, i.e. the two Z-axis orientations, also needed a half turn.
            if (facing.getAxis() == Direction.Axis.Z)
                stack.mulPose(Axis.YP.rotationDegrees(180));
        }
        stack.mulPose(Axis.YP.rotationDegrees(getModelYaw(facing)));
    }

    private static PoseStack snapshot(PoseStack from) {
        PoseStack snapped = new PoseStack();
        snapped.last().set(from.last());
        return snapped;
    }

    /** IO-goggles overlay naming the gearbox's current mode. Not wired up yet (see the todos). */
    @SuppressWarnings("unused")
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

    /**
     * 26.2 submit hook.
     *
     * <p>Two submissions, because the original bound two textures: the housing and gear train use
     * the gearbox material's texture, while {@code ModelGearboxBase.renderSupports} rebound the
     * <em>bearing tier</em>'s before drawing the support columns. Each submission gets its own pose
     * snapshot — the lambdas run deferred, so they must not share mutable state.
     */
    @Override
    public void submit(BlockEntityRenderState state,
                       PoseStack poseStack,
                       SubmitNodeCollector collector,
                       CameraRenderState camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntityGearbox tile)) return;
        if (!this.doRenderModel(poseStack, tile)) return;

        GearboxBaseModel model = this.getModel(tile);
        int light = state.lightCoords;
        float phi = -tile.phi;

        // The per-ratio GearboxModel subclasses share the same texture directory; pick the file
        // by material via {@link GearboxTypes#getBaseGearboxTexture}.
        RenderType body = RenderTypes.entityCutout(
                textureWithSuffix(GearboxModel.TEXTURE_LOCATION, tile.getGearboxType().getBaseGearboxTexture()));
        RenderType bearings = RenderTypes.entityCutout(
                textureWithSuffix(GearboxModel.TEXTURE_LOCATION, tile.getBearingTier().getBaseGearboxTexture()));

        PoseStack bodyPose = snapshot(poseStack);
        setupPose(bodyPose, tile);
        collector.submitCustomGeometry(poseStack, body, (pose, vc) -> model.renderMain(bodyPose, vc, light, phi));

        PoseStack bearingPose = snapshot(poseStack);
        setupPose(bearingPose, tile);
        collector.submitCustomGeometry(poseStack, bearings, (pose, vc) -> model.renderSupports(bearingPose, vc, light));

        if (tile.isInWorld()) {
            IORenderer.renderIO(poseStack, collector, tile, tile.getBlockPos());
//todo            this.renderLiquid(stack, tile);
        }
    }

}
