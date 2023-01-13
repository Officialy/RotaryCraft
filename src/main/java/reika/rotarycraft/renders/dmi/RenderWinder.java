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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.BlockEntityWinder;
import reika.rotarycraft.items.ItemCoil;
import reika.rotarycraft.models.animated.WinderModel;

public class RenderWinder extends RotaryTERenderer<BlockEntityWinder> {

    private WinderModel winderModel;
    //private WinderModelV WinderModelV = new WinderModelV();

    public RenderWinder(BlockEntityRendererProvider.Context pContext) {

    }

    /**
     * Renders the BlockEntity for the position.
     */
    public void renderBlockEntityWinderAt(PoseStack stack, BlockEntityWinder tile, MultiBufferSource bufferSource, int light) {
        VertexConsumer tex = bufferSource.getBuffer(RenderType.entitySolid(new ResourceLocation(RotaryCraft.MODID, "textures/blockentitytex/windertex.png")));
        boolean hasSpring = tile.getStackInSlot(0) != null && tile.getStackInSlot(0).getItem() instanceof ItemCoil;
        winderModel.renderAll(stack, tex, light, tile, ReikaJavaLibrary.makeListFrom(hasSpring), -tile.phi, 0);
    }

    @Override
    public void render(BlockEntityWinder tile, float v, PoseStack stack, MultiBufferSource multiBufferSource, int i, int i1) {
        if (this.doRenderModel(stack, tile)) {
            this.renderBlockEntityWinderAt(stack, tile, multiBufferSource, i);
        }
        if (tile.isInWorld())// && MinecraftForgeClient.getRenderPass() == 1)
            IORenderer.renderIO(stack, multiBufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
    }

}
