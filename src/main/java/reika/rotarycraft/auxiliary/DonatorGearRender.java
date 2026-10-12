/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import reika.dragonapi.auxiliary.trackers.PlayerSpecificRenderer;
import reika.dragonapi.interfaces.PlayerRenderObj;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.RotaryItems;

/** Original spinning HSLA steel gear, using the 26.3 native dropped-item model. */
@EventBusSubscriber(modid = RotaryCraft.MODID, value = Dist.CLIENT)
public final class DonatorGearRender implements PlayerRenderObj {
    public static final DonatorGearRender instance = new DonatorGearRender();
    private DonatorGearRender() {}

    @SubscribeEvent public static void load(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> PlayerSpecificRenderer.instance.registerDonatorRenderer(instance));
    }

    @Override public void extractRenderState(PoseStack stack, Player player, float partialTick,
            PlayerSpecificRenderer.PlayerRotationData rotation, SubmitNodeCollector collector) {
        var item = new ItemStackRenderState();
        Minecraft.getInstance().getItemModelResolver().updateForNonLiving(item,
                new ItemStack(RotaryItems.HSLA_STEEL_GEAR.get()), ItemDisplayContext.GROUND, player);
        stack.pushPose();
        try {
            stack.translate(0, 2.1875, 0);
            stack.translate(0, 0.0625, 0);
            stack.rotate(Axis.ZP.rotationDegrees((System.currentTimeMillis() / 10L) % 360));
            stack.translate(0, -0.0625, 0);
            stack.rotate(Axis.YP.rotationDegrees(90));
            stack.scale(0.5F, 0.5F, 0.5F);
            item.submit(stack, collector, LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0);
        } finally { stack.popPose(); }
    }
    @Override public int getRenderPriority() { return 0; }
}
