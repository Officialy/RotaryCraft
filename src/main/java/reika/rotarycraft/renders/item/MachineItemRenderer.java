package reika.rotarycraft.renders.item;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import reika.rotarycraft.models.SolarTowerModel;
import reika.rotarycraft.models.animated.shaftonly.CrossModel;
import reika.rotarycraft.models.animated.shaftonly.ShaftModel;
import reika.rotarycraft.models.engine.*;
import reika.rotarycraft.modinterface.model.MagneticModel;
import reika.rotarycraft.modinterface.model.SteamTurbineModel;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.MaterialRegistry;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryModelLayers;
import reika.rotarycraft.renders.RenderShaft;

public class MachineItemRenderer extends BlockEntityWithoutLevelRenderer {

    private final ShaftModel shaftModel;
    private final CrossModel crossModel;
    private final DCModel dcModel;
    private SteamModel steamModel;
    private CombustionModel combModel;
    private ACModel acModel;
    private PerformanceModel perfModel;
    private MicroTurbineModel microModel;
    private JetModel jetModel;
    private HydroModel hydroModel;
    private WindModel windModel;
    private SolarTowerModel solarModel;
    private SteamTurbineModel steamTurbineModel;
    private MagneticModel magneticModel;
    public MachineItemRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
        super(p_172550_, p_172551_);
        EntityModelSet nonNullEntitySet = Minecraft.getInstance().getEntityModels();
        shaftModel = new ShaftModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.SHAFT));
        crossModel = new CrossModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.SHAFT_CROSS));
        dcModel = new DCModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.DC_ENGINE));
        steamModel = new SteamModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.STEAM_ENGINE));
        combModel = new CombustionModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.COMBUSTION_ENGINE));
        acModel = new ACModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.AC_ENGINE));
        perfModel = new PerformanceModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.PERFORMANCE_ENGINE));
        microModel = new MicroTurbineModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.MICRO_ENGINE));
        jetModel = new JetModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.JET_ENGINE));
        hydroModel = new HydroModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.HYDRO_ENGINE));
        windModel = new WindModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.WIND_ENGINE));
        solarModel = new SolarTowerModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.SOLAR_TOWER));
//        steamTurbineModel = new SteamTurbineModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.STEAM_TURBINE));
        magneticModel = new MagneticModel(nonNullEntitySet.bakeLayer(RotaryModelLayers.MAGNETIC));
    }

    @Override
    public void renderByItem(ItemStack item, ItemTransforms.TransformType type, PoseStack stack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlayIn) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        //Shafts
        stack.pushPose();
        if (type == ItemTransforms.TransformType.GROUND|| type == ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND || type == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND) {
            stack.translate(0.5, 1, 0.5);
            stack.scale(-0.5f, -0.5f, 0.5f);
        }
        if (type == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || type == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
            stack.translate(0.4D, 1.1D, 0.75D);
            stack.scale(-0.45f, -0.45f, 0.45f);
            stack.mulPose(Axis.YP.rotationDegrees(135f));
            stack.mulPose(Axis.ZN.rotationDegrees(15f));
        }
        if (type == ItemTransforms.TransformType.GUI) {
            stack.translate(0.5D, 1.1D, 0.5D);
            stack.scale(-0.6f, -0.6f, 0.6f);
            stack.mulPose(Axis.YN.rotationDegrees(135f));
        }
        if (type == ItemTransforms.TransformType.FIXED) {
            stack.translate(0.5, 1, 0.5);
            stack.scale(-0.5f, -0.5f, 0.5f);
        }
        if (type == ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND || type == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND) {
            stack.translate(0.5, 1, 0.5);
            stack.scale(-0.5f, -0.5f, 0.5f);
        }
        if (MachineRegistry.isShaft(item.getItem())) {
            shaftModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid(ResourceLocation.tryParse(ShaftModel.TEXTURE_LOCATION + switch (MaterialRegistry.getMaterialFromShaftItem(item)){
                case WOOD -> MaterialRegistry.WOOD.getBaseShaftTexture();
                case STONE -> MaterialRegistry.STONE.getBaseShaftTexture();
                case STEEL -> MaterialRegistry.STEEL.getBaseShaftTexture();
                case DIAMOND -> MaterialRegistry.DIAMOND.getBaseShaftTexture();
                case TUNGSTEN -> MaterialRegistry.TUNGSTEN.getBaseShaftTexture();
                case BEDROCK -> MaterialRegistry.BEDROCK.getBaseShaftTexture();
            }))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        if (item == RotaryBlocks.SHAFT_CROSS.get().asItem().getDefaultInstance()){
            crossModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid(CrossModel.TEXTURE_LOCATION)), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        stack.popPose();
        //DC Engine
        stack.pushPose();
        if (type == ItemTransforms.TransformType.GROUND) {
            stack.translate(0.5D, 1.1D, 0.5D);
            stack.scale(-0.5f, -0.5f, 0.5f);
            stack.mulPose(Axis.YN.rotationDegrees(90f));
        }
        if (type == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || type == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
            stack.translate(0.4D, 1.2D, 0.75D);
            stack.scale(-0.5f, -0.5f, 0.5f);
            stack.mulPose(Axis.YN.rotationDegrees(135f));
        }
        if (type == ItemTransforms.TransformType.GUI) {
            stack.translate(0.5D, 1.1D, 0.5D);
            stack.scale(-0.6f, -0.6f, 0.6f);
            stack.mulPose(Axis.YP.rotationDegrees(135f));
        }
        if (type == ItemTransforms.TransformType.FIXED) {
            stack.translate(0.5D, 1.1D, 0.5D);
            stack.scale(-0.5f, -0.5f, 0.5f);
            stack.mulPose(Axis.YN.rotationDegrees(90f));
        }
        if (type == ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND || type == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND){
            stack.translate(0.5D, 1.1D, 0.5D);
            stack.scale(-0.5f, -0.5f, 0.5f);
            stack.mulPose(Axis.YN.rotationDegrees(90f));
        }
        if (item.getItem() == RotaryBlocks.DC_ENGINE.get().asItem()) {
            dcModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid((DCModel.TEXTURE_LOCATION))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        stack.popPose();
        stack.pushPose();
        if (type == ItemTransforms.TransformType.GROUND) {
            stack.translate(0.5D, 1.1D, 0.5D);
            stack.scale(-0.5f, -0.5f, 0.5f);
            stack.mulPose(Axis.YN.rotationDegrees(90f));
        }
        if (type == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || type == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND) {
            stack.translate(0.4D, 1.2D, 0.75D);
            stack.scale(-0.5f, -0.5f, 0.5f);
            stack.mulPose(Axis.YN.rotationDegrees(140f));
        }
        if (type == ItemTransforms.TransformType.GUI) {
            stack.translate(0.5D, 1.1D, 0.5D);
            stack.scale(-0.6f, -0.6f, 0.6f);
            stack.mulPose(Axis.YP.rotationDegrees(135f));
        }
        if (type == ItemTransforms.TransformType.FIXED) {
            stack.translate(0.5D, 1.1D, 0.5D);
            stack.scale(-0.5f, -0.5f, 0.5f);
            stack.mulPose(Axis.YN.rotationDegrees(90f));
        }
        if (type == ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND || type == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND){
            stack.translate(0.5D, 1.1D, 0.5D);
            stack.scale(-0.5f, -0.5f, 0.5f);
            stack.mulPose(Axis.YN.rotationDegrees(90f));
        }
        if (item.getItem() == RotaryBlocks.MICRO_TURBINE.get().asItem()) {
            microModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid((MicroTurbineModel.TEXTURE_LOCATION))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        if (item.getItem() == RotaryBlocks.STEAM_TURBINE.get().asItem()) {
            steamModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid((SteamModel.TEXTURE_LOCATION))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        if (item.getItem() == RotaryBlocks.GAS_ENGINE.get().asItem()) {
            combModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid((CombustionModel.TEXTURE_LOCATION))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        if (item.getItem() == RotaryBlocks.SOLAR_TOWER.get().asItem()) {
            solarModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid((SolarTowerModel.TEXTURE_LOCATION))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        if (item.getItem() == RotaryBlocks.WIND_ENGINE.get().asItem()) {
            windModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid((WindModel.TEXTURE_LOCATION))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        if (item.getItem() == RotaryBlocks.PERFORMANCE_ENGINE.get().asItem()) {
            perfModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid((PerformanceModel.TEXTURE_LOCATION))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        if (item.getItem() == RotaryBlocks.AC_ENGINE.get().asItem()) {
            acModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid((ACModel.TEXTURE_LOCATION))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
        if (item.getItem() == RotaryBlocks.MAGNETOSTATIC_ENGINE.get().asItem()) {
            magneticModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid((MagneticModel.TEXTURE_LOCATION))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
        }
//        if (item.getItem() == RotaryBlocks.JET_ENGINE.get().asItem()) {
//            jetModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid((JetModel.TEXTURE_LOCATION))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
//        }
//        if (item.getItem() == RotaryBlocks.HYDRO_ENGINE.get().asItem()) {
//            hydroModel.renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid((HydroModel.TEXTURE_LOCATION))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
//        }

        stack.popPose();
        RenderSystem.disableBlend();
    }

}