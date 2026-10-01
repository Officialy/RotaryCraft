/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.modinterface;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.modinterface.model.FuelEngineModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public final class RenderFuelEngine extends RotaryTERenderer<TileEntityFuelEngine> {
    private final FuelEngineModel model;
    public RenderFuelEngine(BlockEntityRendererProvider.Context context) { model = new FuelEngineModel(context.bakeLayer(RotaryModelLayers.FUEL_ENGINE)); }
    @Override protected Identifier getSubmitTexture(BlockEntity tile) { return FuelEngineModel.TEXTURE_LOCATION; }
    @Override protected boolean useEntityCutout() { return true; }
    @Override protected void renderModel(PoseStack stack, BlockEntity tile, VertexConsumer out, int light) {
        if (!(tile instanceof TileEntityFuelEngine engine)) return;
        stack.pushPose(); stack.translate(.5, 1.5, .5); stack.scale(1, -1, -1);
        float rotation = switch (engine.getBlockState().getValue(BlockRotaryCraftMachine.FACING)) {
            case WEST -> 270; case EAST -> 90; case NORTH -> 0; default -> 180;
        };
        if (engine.isFlipped) {
            stack.rotate(Axis.XP.rotationDegrees(180)); stack.translate(0, -2, 0);
            if (engine.getBlockState().getValue(BlockRotaryCraftMachine.FACING).getAxis() == net.minecraft.core.Direction.Axis.Z)
                stack.rotate(Axis.YP.rotationDegrees(180));
        }
        stack.rotate(Axis.YP.rotationDegrees(rotation));
        model.renderAll(stack, out, light, engine, null, engine.phi, 0); stack.popPose();
    }
}
