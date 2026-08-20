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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.ModList;
import reika.dragonapi.libraries.rendering.ReikaColorAPI;
import reika.rotarycraft.RotaryCraft;
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

    /** Block-atlas sprite lookup for the lubricant surface; only obtainable at construction. */
    private final SpriteGetter sprites;

    public RenderGearbox(BlockEntityRendererProvider.Context context) {
        sprites = context.sprites();
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

/**
     * The lubricant surface inside the casing, faithful to {@code RenderGearbox.renderLiquid}: a
     * single horizontal quad inset 1px from each wall, its height scaling with the tank, textured
     * with the lubricant's still sprite. A living (botania) gearbox shows mana instead — the
     * original fell back to water's sprite tinted with a hue that cycles over ~16 ticks.
     *
     * <p>Drawn in unrotated block-local space: 1.7.10 called this straight off the TE's render
     * coordinates, outside the model's flip/yaw, so the surface stays level regardless of facing.
     */
    private void renderLiquid(PoseStack poseStack, SubmitNodeCollector collector, BlockEntityGearbox tile, int light) {
        int max = tile.getMaxLubricant();
        int lube = tile.getLubricant();
        if (max <= 0 || lube <= 0)
            return;

        boolean living = tile.isLiving();
        // Beware the transcription: 1.7.10 reads `4D/16D * lube / max`, i.e. (0.25 * lube) / max
        // evaluated in doubles. Writing it as 0.25 * (lube / max) is integer division and pins the
        // surface at the 1px floor forever.
        double h = 0.0625 + (0.25D * lube / max) * 0.9;
        boolean flipped = tile.isFlipped;
        if (flipped)
            h = 1 - h;

        int colour = 0xFFFFFFFF;
        Identifier tex;
        if (living) {
            // Original: mana pool icon if Botania exposed one, else water; tinted with a hue that
            // sweeps 192..224 over 16 ticks.
            float t = tile.getTicksExisted() + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
            colour = 0xFF000000 | ReikaColorAPI.getModifiedHue(0x0000ff, 192 + (int) (32 * Math.sin(t / 16D)));
            tex = Identifier.withDefaultNamespace("block/water_still");
        } else {
            tex = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "block/fluid/lubricant");
        }

        TextureAtlasSprite sprite = sprites.get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, tex));
        final float u = sprite.getU0(), v = sprite.getV0(), du = sprite.getU1(), dv = sprite.getV1();
        final float y = (float) h;
        final int rgba = colour;

        PoseStack snapped = snapshot(poseStack);
        collector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS), (pose, vc) -> {
            var pp = snapped.last();
            vc.addVertex(pp, 0.0625F, y, 0.9375F).setColor(rgba).setUv(u,  dv).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0F, 1F, 0F);
            vc.addVertex(pp, 0.9375F, y, 0.9375F).setColor(rgba).setUv(du, dv).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0F, 1F, 0F);
            vc.addVertex(pp, 0.9375F, y, 0.0625F).setColor(rgba).setUv(du, v ).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0F, 1F, 0F);
            vc.addVertex(pp, 0.0625F, y, 0.0625F).setColor(rgba).setUv(u,  v ).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0F, 1F, 0F);
        });

        if (flipped) {
            // Ceiling-mounted: the original laid a glass sheet just under the surface so the
            // lubricant reads as held in rather than hanging.
            TextureAtlasSprite glass = sprites.get(new SpriteId(TextureAtlas.LOCATION_BLOCKS,
                    Identifier.withDefaultNamespace("block/glass")));
            final float gu = glass.getU0(), gv = glass.getV0(), gdu = glass.getU1(), gdv = glass.getV1();
            final float gy = (float) (h - 0.005);
            PoseStack snappedGlass = snapshot(poseStack);
            collector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS), (pose, vc) -> {
                var pp = snappedGlass.last();
                vc.addVertex(pp, 0.0625F, gy, 0.9375F).setColor(-1).setUv(gu,  gdv).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0F, 1F, 0F);
                vc.addVertex(pp, 0.9375F, gy, 0.9375F).setColor(-1).setUv(gdu, gdv).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0F, 1F, 0F);
                vc.addVertex(pp, 0.9375F, gy, 0.0625F).setColor(-1).setUv(gdu, gv ).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0F, 1F, 0F);
                vc.addVertex(pp, 0.0625F, gy, 0.0625F).setColor(-1).setUv(gu,  gv ).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(0F, 1F, 0F);
            });
        }
    }

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
            this.renderLiquid(poseStack, collector, tile, light);
        }
    }

}
