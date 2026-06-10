/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders.m;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.weaponry.BlockEntityVanDeGraff;

// 1.21.5 STUB: The original renderer relied on the legacy BlockEntityRenderer#render hook,
// RenderSystem.setShaderColor, and other removed APIs. Replace this with a SubmitNodeCollector-based
// implementation against the new BlockEntityRenderer#submit pipeline.
public class RenderVanDeGraff extends RotaryTERenderer<BlockEntityVanDeGraff> {

    public RenderVanDeGraff(BlockEntityRendererProvider.Context context) {
    }

    // RotaryTERenderer already provides final implementations of doRenderModel / getOwnerMod /
    // getModClass / getModID for us.
}
