/*******************************************************************************
 * @author Reika Kalseki
 * Copyright 2017
 * All rights reserved. Distribution requires the owner's explicit prior permission.
 ******************************************************************************/
package reika.rotarycraft.renders.m;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.weaponry.BlockEntitySonicWeapon;
import reika.rotarycraft.models.SonicWeaponModel;
import reika.rotarycraft.registry.RotaryModelLayers;
public final class RenderSonic extends RotaryTERenderer<BlockEntitySonicWeapon> {
    private final SonicWeaponModel model;
    public RenderSonic(BlockEntityRendererProvider.Context context) { model = new SonicWeaponModel(context.bakeLayer(RotaryModelLayers.SONIC_WEAPON)); }
    @Override protected Identifier getSubmitTexture(BlockEntity tile) { return SonicWeaponModel.TEXTURE_LOCATION; }
    @Override protected boolean useEntityCutout() { return true; }
    @Override protected void renderModel(PoseStack stack, BlockEntity tile, VertexConsumer vertices, int light) {
        stack.pushPose(); stack.translate(.5, 1.5, .5); stack.scale(1, -1, -1);
        model.renderAll(stack, vertices, light, tile, null, 0, 0); stack.popPose();
    }
}
