package reika.rotarycraft.modinterface.conversion;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.modinterface.model.Dynamo2Model;
import reika.rotarycraft.modinterface.model.DynamoModel;
import reika.rotarycraft.registry.RotaryModelLayers;

/** Original dynamo and flux-upgraded geometry through the 26.2 submit renderer. */
public final class RenderDynamo extends RotaryTERenderer<TileEntityDynamo> {
    private final DynamoModel standard;
    private final Dynamo2Model upgraded;

    public RenderDynamo(BlockEntityRendererProvider.Context context) {
        standard = new DynamoModel(context.bakeLayer(RotaryModelLayers.DYNAMO));
        upgraded = new Dynamo2Model(context.bakeLayer(RotaryModelLayers.DYNAMO_UPGRADED));
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return be instanceof TileEntityDynamo dynamo && dynamo.isUpgraded()
                ? Dynamo2Model.TEXTURE_LOCATION : DynamoModel.TEXTURE_LOCATION;
    }

    @Override
    protected boolean useEntityCutout() { return true; }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer out, int light) {
        if (!(be instanceof TileEntityDynamo dynamo)) return;
        Direction facing = dynamo.getBlockState().getValue(BlockRotaryCraftMachine.FACING);
        stack.pushPose();
        stack.translate(0.5, 1.5, 0.5);
        stack.rotate(Axis.ZP.rotationDegrees(180));
        switch (facing) {
            case UP -> stack.rotate(Axis.XP.rotationDegrees(90));
            case DOWN -> stack.rotate(Axis.XP.rotationDegrees(-90));
            default -> stack.rotate(Axis.YP.rotationDegrees(-facing.toYRot() + 90));
        }
        if (dynamo.isUpgraded()) upgraded.renderAll(stack, out, light, dynamo, null, dynamo.phi, 0);
        else standard.renderAll(stack, out, light, dynamo, null, dynamo.phi, 0);
        stack.popPose();
    }
}
