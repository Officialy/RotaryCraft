package reika.rotarycraft.renders.mi;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.level.BlockEntityDefoliator;
import reika.rotarycraft.models.animated.DefoliatorModel;
import reika.rotarycraft.registry.RotaryModelLayers;
public final class RenderDefoliator extends RotaryTERenderer<BlockEntityDefoliator> {
    private final DefoliatorModel model;
    public RenderDefoliator(BlockEntityRendererProvider.Context context) { model = new DefoliatorModel(context.bakeLayer(RotaryModelLayers.DEFOLIATOR)); }
    @Override protected Identifier getSubmitTexture(BlockEntity tile) { return DefoliatorModel.TEXTURE_LOCATION; }
    @Override protected boolean useEntityCutout() { return true; }
    @Override protected void renderModel(PoseStack stack, BlockEntity tile, VertexConsumer vertices, int light) {
        stack.pushPose(); stack.translate(.5, 1.5, .5); stack.scale(1, -1, -1);
        model.renderAll(stack, vertices, light, tile, null, -((BlockEntityDefoliator)tile).phi, 0); stack.popPose();
    }
}
