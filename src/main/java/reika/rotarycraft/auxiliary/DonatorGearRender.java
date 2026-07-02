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

import com.mojang.math.Axis;
import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import reika.dragonapi.auxiliary.trackers.PlayerSpecificRenderer;
import reika.dragonapi.interfaces.PlayerRenderObj;
import reika.rotarycraft.registry.RotaryItems;

public class DonatorGearRender implements PlayerRenderObj {

    public static final DonatorGearRender instance = new DonatorGearRender();

    private DonatorGearRender() {

    }

    @Override
    public void extractRenderState(PoseStack matrixStack, Player ep, float ptick, PlayerSpecificRenderer.PlayerRotationData dat, SubmitNodeCollector collector) {
        // 1.21.5 TODO: ItemRenderer.render moved to ItemModelResolver/ItemStackRenderState. This
        // donator gear rendering is stubbed pending a port to the new item rendering pipeline.
        matrixStack.pushPose();
        matrixStack.translate(0, 2.1875, 0);
        double d = 0.0625;
        float angle = (System.currentTimeMillis() / 10f) % 360;
        matrixStack.translate(0, d, 0);
        // 1.21.5 fix: legacy ported {@code new Quaternionf(a,x,y,z)} but Quaternionf takes
        // (x,y,z,w) — yielding a non-normalised quaternion. Use Axis rotations instead.
        matrixStack.mulPose(Axis.ZP.rotationDegrees(angle));
        matrixStack.translate(0, -d, 0);
        matrixStack.mulPose(Axis.YP.rotationDegrees(90));
        float s = 0.5f;
        matrixStack.scale(s, s, s);
        matrixStack.popPose();
    }

    @Override
    public int getRenderPriority() {
        return 0;
    }

}
