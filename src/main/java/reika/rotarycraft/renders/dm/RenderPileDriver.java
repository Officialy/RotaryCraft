/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders.dm;
import java.util.ArrayList;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.level.BlockEntityPileDriver;
import reika.rotarycraft.models.animated.PileDriverModel;
import reika.rotarycraft.registry.RotaryModelLayers;
public final class RenderPileDriver extends RotaryTERenderer<BlockEntityPileDriver> {
    private final PileDriverModel model;
    public RenderPileDriver(BlockEntityRendererProvider.Context context) { model = new PileDriverModel(context.bakeLayer(RotaryModelLayers.PILE_DRIVER)); }
    @Override protected Identifier getSubmitTexture(BlockEntity tile) { return PileDriverModel.TEXTURE_LOCATION; }
    @Override protected boolean useEntityCutout() { return true; }
    @Override protected void renderModel(PoseStack stack, BlockEntity tile, VertexConsumer vertices, int light) {
        var driver = (BlockEntityPileDriver)tile;
        stack.pushPose(); stack.translate(.5, 1.5, .5); stack.scale(1, -1, -1);
        if (driver.getBlockState().getValue(BlockRotaryCraftMachine.FACING).getAxis() == net.minecraft.core.Direction.Axis.Z) stack.rotate(Axis.YP.rotationDegrees(90));
        model.renderAll(stack, vertices, light, tile, new ArrayList<>(java.util.List.of(driver.power > 0)), driver.phi, 0); stack.popPose();
    }
}
