/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders.mi;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.blockentities.processing.BlockEntityLavaSmeltery;

import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.models.BigFurnaceModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderBigFurnace extends RotaryTERenderer<BlockEntityLavaSmeltery> {

    private final BigFurnaceModel modelBigFurnace;

    public RenderBigFurnace(BlockEntityRendererProvider.Context ctx) {
        modelBigFurnace = new BigFurnaceModel(ctx.bakeLayer(RotaryModelLayers.BIG_FURNACE));
    }

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntityBigFurnaceAt(BlockEntityLavaSmeltery tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
        stack.pushPose();
        stack.translate(0.5F, 1.5F, 0.5F);
        stack.mulPose(Axis.ZP.rotationDegrees(180.0F));

        modelBigFurnace.renderToBuffer(stack, bufferSource.getBuffer(modelBigFurnace.renderType(BigFurnaceModel.TEXTURE_LOCATION)), light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        stack.popPose();
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return BigFurnaceModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, MultiBufferSource mbs, int light) {
        if (be instanceof BlockEntityLavaSmeltery smeltery)
            renderBlockEntityBigFurnaceAt(smeltery, stack, mbs, light);
    }
}
