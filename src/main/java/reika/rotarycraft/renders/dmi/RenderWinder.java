/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders.dmi;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Quaternionf;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.BlockEntityWinder;
import reika.rotarycraft.items.ItemCoil;
import reika.rotarycraft.models.animated.WinderModel;
import reika.rotarycraft.registry.RotaryModelLayers;

public class RenderWinder extends RotaryTERenderer<BlockEntityWinder> {

    private final WinderModel winderModel;

    public RenderWinder(BlockEntityRendererProvider.Context pContext) {
        winderModel = new WinderModel(pContext.bakeLayer(RotaryModelLayers.WINDER));
    }

    public void renderBlockEntityWinderAt(PoseStack stack, BlockEntityWinder tile, VertexConsumer bufferSource, int light) {
        VertexConsumer tex = bufferSource;
        boolean hasSpring = !tile.getStackInSlot(0).isEmpty() && tile.getStackInSlot(0).getItem() instanceof ItemCoil;
        winderModel.renderAll(stack, tex, light, tile, ReikaJavaLibrary.makeListFrom(hasSpring), -tile.phi, 0);
    }

    @Override
    protected Identifier getSubmitTexture(BlockEntity be) {
        return WinderModel.TEXTURE_LOCATION;
    }

    @Override
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer mbs, int light) {
        if (be instanceof BlockEntityWinder winder)
            renderBlockEntityWinderAt(stack, winder, mbs, light);
    }
}

