package reika.rotarycraft.renders.item;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import reika.dragonapi.instantiable.data.maps.MultiMap;
import reika.dragonapi.instantiable.data.maps.NestedMap;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.RotaryModelBase;
import reika.rotarycraft.models.SolarTowerModel;
import reika.rotarycraft.models.animated.GrinderModel;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final Map<Item, Map.Entry<Model, ResourceLocation>> modelMap = new HashMap<>();

    public MachineItemRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
        super(p_172550_, p_172551_);
        EntityModelSet nonNullEntitySet = Minecraft.getInstance().getEntityModels();
        for (var m : MachineRegistry.values()) {
            if (m != null && m.getBlockState() != null && m.hasModel()) {
                RotaryModelBase model = m.getModel().apply(nonNullEntitySet);
                modelMap.put(m.getBlockState().getBlock().asItem(), Map.entry(model, model.getTexture()));
            }
        }

     /*
        ,(modelSet) -> new JetModel(modelSet.bakeLayer(RotaryModelLayers.JET_ENGINE))
        ,(modelSet) -> new HydroModel(modelSet.bakeLayer(RotaryModelLayers.HYDRO_ENGINE))
        */
    }

    @Override
    public void renderByItem(ItemStack item, ItemDisplayContext type, PoseStack stack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlayIn) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        stack.pushPose();
        if (type == ItemDisplayContext.GROUND) {
            stack.translate(0.5D, 1.1D, 0.5D);
            stack.scale(-0.5f, -0.5f, 0.5f);
            stack.mulPose(Axis.YN.rotationDegrees(90f));
        }
        if (type == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || type == ItemDisplayContext.FIRST_PERSON_LEFT_HAND) {
            stack.translate(0.4D, 1.2D, 0.75D);
            stack.scale(-0.5f, -0.5f, 0.5f);
            stack.mulPose(Axis.YN.rotationDegrees(140f));
        }
        if (type == ItemDisplayContext.GUI) {
            stack.translate(0.5D, 1.1D, 0.5D);
            stack.scale(-0.6f, -0.6f, 0.6f);
            stack.mulPose(Axis.YP.rotationDegrees(135f));
        }
        if (type == ItemDisplayContext.FIXED) {
            stack.translate(0.5D, 1.1D, 0.5D);
            stack.scale(-0.5f, -0.5f, 0.5f);
            stack.mulPose(Axis.YN.rotationDegrees(90f));
        }
        if (type == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || type == ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
            stack.translate(0.5D, 1.1D, 0.5D);
            stack.scale(-0.5f, -0.5f, 0.5f);
            stack.mulPose(Axis.YN.rotationDegrees(90f));
        }
        for (var map : modelMap.entrySet()) {
            if (item.getItem() == map.getKey() && MachineRegistry.isShaft(item.getItem())) {
                map.getValue().getKey().renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid(ResourceLocation.tryParse(ShaftModel.TEXTURE_LOCATION + switch (MaterialRegistry.getMaterialFromShaftItem(item)) {
                    case WOOD -> MaterialRegistry.WOOD.getBaseShaftTexture();
                    case STONE -> MaterialRegistry.STONE.getBaseShaftTexture();
                    case STEEL -> MaterialRegistry.STEEL.getBaseShaftTexture();
                    case DIAMOND -> MaterialRegistry.DIAMOND.getBaseShaftTexture();
                    case TUNGSTEN -> MaterialRegistry.TUNGSTEN.getBaseShaftTexture();
                    case BEDROCK -> MaterialRegistry.BEDROCK.getBaseShaftTexture();
                }))), combinedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1.0F);
            } else if (item.getItem() == map.getKey()) {
                map.getValue().getKey().renderToBuffer(stack, bufferSource.getBuffer(RenderType.entitySolid(map.getValue().getValue())), combinedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            }
        }
        stack.popPose();
        RenderSystem.disableBlend();
    }

}