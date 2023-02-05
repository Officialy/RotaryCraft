package reika.rotarycraft.renders.item;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import reika.rotarycraft.models.animated.shaftonly.CrossModel;
import reika.rotarycraft.models.animated.shaftonly.ShaftModel;
import reika.rotarycraft.models.animated.shaftonly.ShaftVModel;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryModelLayers;
import reika.rotarycraft.renders.RenderShaft;

public class ShaftItemRenderer extends BlockEntityWithoutLevelRenderer {

    private final ShaftModel shaftModel;
    private final ShaftVModel VShaftModelt;
    private final CrossModel crossModel;

    public ShaftItemRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
        super(p_172550_, p_172551_);
        EntityModelSet nonNullEntitySet = Minecraft.getInstance().getEntityModels();
        shaftModel = new ShaftModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.SHAFT));
        VShaftModelt = new ShaftVModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.SHAFT_VERTICAL));
        crossModel = new CrossModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.SHAFT_CROSS));
    }

    @Override
    public void renderByItem(ItemStack item, ItemTransforms.TransformType transformType, PoseStack stack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlayIn) {
        stack.pushPose();
        stack.translate(0.5D, 1.5D, 0.5D);
        stack.scale(-0.9999F, -0.9999F, 0.9999F);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        if (item.getItem() == RotaryBlocks.WOOD_SHAFT.get().asItem()) {
            shaftModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySmoothCutout(ResourceLocation.tryParse(ShaftModel.TEXTURE_LOCATION + RenderShaft.getImageFileName(RotaryBlockEntities.WOOD_SHAFT.get().getBlockEntity(null, null))))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }

        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
        stack.popPose();
    }
}