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
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryModelBase;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;
import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear.GearType;
import reika.rotarycraft.models.CVTModel;
import reika.rotarycraft.models.animated.CoilModel;
import reika.rotarycraft.models.animated.HighGearModel;
import reika.rotarycraft.models.animated.WormModel;
import reika.rotarycraft.registry.RotaryModelLayers;

/**
 * 26.1 BER for the advanced-gear family (worm gear is the only block currently wired through
 * here; high-gear / CVT / coil use their own renderers). Was a no-op stub — the worm gear
 * never drew anything in-world. Mirrors the orient-by-FACING + animate-by-phi pattern from
 * {@link RenderShaft} / {@link reika.rotarycraft.renders.dm.RenderPump}: the WormModel's
 * {@code renderAll} already applies the X/Z-axis crank rotations from {@code phi}, so all the
 * BER needs to do is set up the right block-origin transform and submit.
 */
public class RenderAdvGear extends RotaryTERenderer<BlockEntityAdvancedGear> {

    private final WormModel wormModel;
    private final CVTModel cvtModel;
    private final CoilModel coilModel;
    private final HighGearModel highGearModel;

    public RenderAdvGear(BlockEntityRendererProvider.Context context) {
        wormModel = new WormModel(context.bakeLayer(RotaryModelLayers.WORM));
        cvtModel = new CVTModel(context.bakeLayer(RotaryModelLayers.CVT));
        coilModel = new CoilModel(context.bakeLayer(RotaryModelLayers.COIL));
        highGearModel = new HighGearModel(context.bakeLayer(RotaryModelLayers.HIGHGEAR));
    }

    /**
     * The advanced-gear family (worm / CVT / high / coil) shares one BER and one BE class; the
     * drawn model is chosen per {@link GearType}. Previously every type drew the worm model, so a
     * CVT (and COIL / the creative coil, which shares COIL's GearType) in-world showed worm-gear
     * geometry. HIGH still falls back to the worm model until its model is wired through here.
     */
    private RotaryModelBase selectModel(BlockEntityAdvancedGear tile) {
        return switch (tile.getGearType()) {
            case CVT -> cvtModel;
            case COIL -> coilModel;
            case HIGH -> highGearModel;
            default -> wormModel;
        };
    }

    /**
     * Per-type texture. Everything but the coil uses its model's own; a bedrock-cored coil gets
     * {@code coiltex_bed.png}, which 1.7.10 bound on {@code isBedrockCoil()} and the port never
     * referenced.
     */
    private static Identifier textureFor(BlockEntityAdvancedGear tile, RotaryModelBase model) {
        if (tile.getGearType() == GearType.COIL && tile.isBedrockCoil())
            return CoilModel.BEDROCK_TEXTURE;
        return model.getTexture();
    }

    /**
     * 1.7.10 switched on {@code metadata % 4} with angles 0/180/90/270 for read
     * EAST/WEST/SOUTH/NORTH and then added 180. The port stores the <em>opposite</em> of the read
     * side in {@code FACING}, so the four cases become WEST/EAST/NORTH/SOUTH — which works out to
     * {@code toYRot() + 90}.
     */
    private static float getModelYaw(Direction facing) {
        return (facing.toYRot() + 90F) % 360F;
    }

    /**
     * 1.7.10 {@code setupGL} then the facing yaw: translate to the block's top-centre, flip 180°
     * about X ({@code scale(1,-1,-1)} is {@code Rx(180)}), then yaw.
     *
     * <p>The port previously applied {@code Rz(180)} <em>before</em> the yaw. Since
     * {@code T·Rz(180)·Ry(b) == T·Rx(180)·Ry(b+180)}, its effective yaw was {@code -toYRot+270} —
     * right at EAST/WEST but 180° out at NORTH/SOUTH.
     */
    private void renderAt(PoseStack stack, BlockEntityAdvancedGear tile, VertexConsumer tex, int light, RotaryModelBase model) {
        stack.pushPose();
        stack.translate(0.5, 1.5, 0.5);
        stack.mulPose(Axis.XP.rotationDegrees(180));
        if (tile.isInWorld()) {
            BlockState st = tile.getBlockState();
            if (st != null && st.hasProperty(BlockRotaryCraftMachine.FACING)) {
                Direction facing = st.getValue(BlockRotaryCraftMachine.FACING);
                if (!facing.getAxis().isVertical())
                    stack.mulPose(Axis.YP.rotationDegrees(getModelYaw(facing)));
            }
        }
        // phi is NOT negated here: unlike RenderShaft/RenderGearbox, the legacy RenderAdvGear
        // passed `tile.phi` straight through, so the port's `-tile.phi` spun these backwards.
        model.renderAll(stack, tex, light, tile, ReikaJavaLibrary.makeListFrom(false), tile.phi, 0);
        stack.popPose();
    }

    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntityAdvancedGear tile)) return;
        if (!this.doRenderModel(poseStack, tile)) return;

        RotaryModelBase model = this.selectModel(tile);
        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());
        // The render type must be bound to the same texture the model draws with, or the CVT would
        // be drawn into the worm's shaft-texture layer.
        RenderType rt = RenderTypes.entityCutout(textureFor(tile, model));
        int light = state.lightCoords;
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> {
            renderAt(snapped, tile, vc, light, model);
        });
        if (tile.isInWorld()) {
            IORenderer.renderIO(poseStack, collector, tile, tile.getBlockPos());
        }
    }
}
