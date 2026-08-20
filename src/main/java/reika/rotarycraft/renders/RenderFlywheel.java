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
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityFlywheel;
import reika.rotarycraft.models.animated.FlywheelModel;
import reika.rotarycraft.registry.Flywheels;
import reika.rotarycraft.registry.RotaryModelLayers;

import java.util.Locale;

/**
 * 26.2 BER for the flywheel. The whole class was commented out and its registration line with it,
 * so flywheels drew nothing at all — {@code BlockGearbox} (which backs the flywheel blocks too)
 * reports {@code isCustomRendered()}, suppressing the static block model, leaving them invisible.
 */
public class RenderFlywheel extends RotaryTERenderer<BlockEntityFlywheel> {

    private final FlywheelModel flywheelModel;

    public RenderFlywheel(BlockEntityRendererProvider.Context context) {
        flywheelModel = new FlywheelModel(context.bakeLayer(RotaryModelLayers.FLYWHEEL));
    }

    /** One texture per {@link Flywheels} entry, named after the enum constant as in 1.7.10. */
    private static Identifier textureFor(Flywheels f) {
        return Identifier.fromNamespaceAndPath(RotaryCraft.MODID,
                "textures/blockentitytex/transmission/flywheel/" + f.name().toLowerCase(Locale.ENGLISH) + ".png");
    }

    /**
     * 1.7.10 switched on {@code metadata % 4} with the angles 180/0/270/90 for
     * read EAST/WEST/SOUTH/NORTH. The port stores the <em>opposite</em> of the read side in
     * {@code FACING}, so those four cases become WEST/EAST/NORTH/SOUTH — which is
     * {@code toYRot() + 90}.
     *
     * <p>Not the same as {@link RenderGearbox}'s {@code toYRot() + 270}, despite both blocks being
     * {@code BlockGearbox}: the two legacy renderers used different metadata→angle tables, and
     * these are exactly 180° apart.
     */
    private static float getModelYaw(Direction facing) {
        return (facing.toYRot() + 90F) % 360F;
    }

    /**
     * 1.7.10 {@code RotaryTERenderer.setupGL} plus the flywheel's facing rotation: translate to the
     * block's top-centre, flip 180° about X ({@code scale(1,-1,-1)} is {@code Rx(180)}; expressed
     * as a rotation here so the normal matrix stays well-defined), then yaw.
     */
    private static void setupPose(PoseStack stack, BlockEntityFlywheel tile) {
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.XP.rotationDegrees(180));
        if (!tile.isInWorld())
            return;
        BlockState st = tile.getBlockState();
        if (st != null && st.hasProperty(BlockRotaryCraftMachine.FACING)) {
            Direction facing = st.getValue(BlockRotaryCraftMachine.FACING);
            if (!facing.getAxis().isVertical())
                stack.mulPose(Axis.YP.rotationDegrees(getModelYaw(facing)));
        }
    }

    private void renderAt(PoseStack stack, BlockEntityFlywheel tile, VertexConsumer tex, int light) {
        setupPose(stack, tile);
        // The condition list carries `failed`; the model uses it to drop the disc off a shattered
        // flywheel and leave just the hub inside the casing.
        flywheelModel.renderAll(stack, tex, light, tile,
                ReikaJavaLibrary.makeListFrom(tile.failed), -tile.phi, 0);
    }

    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector,
                       CameraRenderState camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(state.blockPos);
        if (!(be instanceof BlockEntityFlywheel tile)) return;
        if (!this.doRenderModel(poseStack, tile)) return;

        RenderType rt = RenderTypes.entityCutout(textureFor(tile.getTypeOrdinal()));
        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());
        int light = state.lightCoords;
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> renderAt(snapped, tile, vc, light));

        if (tile.isInWorld())
            IORenderer.renderIO(poseStack, collector, tile, tile.getBlockPos());
    }
}
