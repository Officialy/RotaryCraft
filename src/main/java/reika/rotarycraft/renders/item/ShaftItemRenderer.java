package reika.rotarycraft.renders.item;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.blockentities.transmission.BlockEntityShaft;
import reika.rotarycraft.models.animated.shaftonly.CrossModel;
import reika.rotarycraft.models.animated.shaftonly.ShaftModel;
import reika.rotarycraft.models.animated.shaftonly.ShaftVModel;
import reika.rotarycraft.registry.MaterialRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryModelLayers;
import reika.rotarycraft.renders.RenderShaft;

public class ShaftItemRenderer extends BlockEntityWithoutLevelRenderer {

    private final ShaftModel shaftModel;
    private final CrossModel crossModel;

    public ShaftItemRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
        super(p_172550_, p_172551_);
        EntityModelSet nonNullEntitySet = Minecraft.getInstance().getEntityModels();
        shaftModel = new ShaftModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.SHAFT));
        crossModel = new CrossModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.SHAFT_CROSS));
    }

    @Override
    public void renderByItem(ItemStack item, ItemTransforms.TransformType transformType, PoseStack stack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlayIn) {
        stack.pushPose();
        stack.translate(0.4D, 1.1D, 0.75D);
        stack.scale(-0.5f, -0.5f, 0.5f);
        stack.mulPose(Axis.YP.rotationDegrees(135f));
//        stack.mulPose(Axis.ZN.rotationDegrees(36f));
//        stack.mulPose(Axis.ZP.rotation(0.5f));
//        stack.scale(0.5F, 0.5F, 0.5F);

        if (item.getItem() == RotaryBlocks.WOOD_SHAFT.get().asItem()) {
            shaftModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid(ResourceLocation.tryParse(ShaftModel.TEXTURE_LOCATION + MaterialRegistry.WOOD.getBaseShaftTexture()))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        if (item.getItem() == RotaryBlocks.STONE_SHAFT.get().asItem()) {
            shaftModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid(ResourceLocation.tryParse(ShaftModel.TEXTURE_LOCATION + MaterialRegistry.STONE.getBaseShaftTexture()))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        if (item.getItem() == RotaryBlocks.HSLA_SHAFT.get().asItem()) {
            shaftModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid(ResourceLocation.tryParse(ShaftModel.TEXTURE_LOCATION + MaterialRegistry.STEEL.getBaseShaftTexture()))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        if (item.getItem() == RotaryBlocks.DIAMOND_SHAFT.get().asItem()) {
            shaftModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid(ResourceLocation.tryParse(ShaftModel.TEXTURE_LOCATION + MaterialRegistry.DIAMOND.getBaseShaftTexture()))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        if (item.getItem() == RotaryBlocks.TUNGSTEN_SHAFT.get().asItem()) {
            shaftModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid(ResourceLocation.tryParse(ShaftModel.TEXTURE_LOCATION + MaterialRegistry.TUNGSTEN.getBaseShaftTexture()))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        if (item.getItem() == RotaryBlocks.DIAMOND_SHAFT.get().asItem()) {
            shaftModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid(ResourceLocation.tryParse(ShaftModel.TEXTURE_LOCATION + MaterialRegistry.BEDROCK.getBaseShaftTexture()))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        if (item.getItem() == RotaryBlocks.SHAFT_CROSS.get().asItem()) {
            shaftModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid(ResourceLocation.tryParse(ShaftModel.TEXTURE_LOCATION + MaterialRegistry.BEDROCK.getBaseShaftTexture()))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        if (item.getItem() == RotaryBlocks.SHAFT_MERGE.get().asItem()) {
            shaftModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid(ResourceLocation.tryParse(ShaftModel.TEXTURE_LOCATION + MaterialRegistry.BEDROCK.getBaseShaftTexture()))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }


        stack.popPose();
    }
}