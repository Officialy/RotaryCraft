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

import org.joml.Quaternionf;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
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
    public void render(GuiGraphics stack, Player ep, float ptick, PlayerSpecificRenderer.PlayerRotationData dat) {
        Tesselator tesselator = Tesselator.getInstance();
        PoseStack matrixStack = new PoseStack();

        matrixStack.pushPose();
        matrixStack.translate(0, 2.1875, 0);
        //GL11.glRotated(-dat.getRenderYaw(), 0, 1, 0);
        //GL11.glRotated(45, 1, 0, 0);
        //GL11.glRotated(dat.getRenderPitch(), 1, 0, 0);
        double d = 0.0625;
        float angle = (System.currentTimeMillis() / 10f) % 360;
        matrixStack.translate(0, d, 0);
        matrixStack.mulPose(new Quaternionf(angle, 0, 0, 1));
        matrixStack.translate(0, -d, 0);
        matrixStack.mulPose(new Quaternionf(90, 0, 1, 0));
        float s = 0.5f;
        matrixStack.scale(s, s, s);
        //GL11.glRotated(45-ep.rotationPitch+90, 1, 0, 0);
        //GL11.glRotated(RenderManager.instance.playerViewY-ep.rotationYawHead-45, 0, 1, 0);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        MultiBufferSource.BufferSource renderTypeBufferImpl = Minecraft.getInstance().renderBuffers().bufferSource();
        itemRenderer.render(new ItemStack(RotaryItems.HSLA_STEEL_GEAR.get()), ItemDisplayContext.HEAD, false, matrixStack, renderTypeBufferImpl, 1, 1, null);
        matrixStack.popPose();
    }

    @Override
    public int getRenderPriority() {
        return 0;
    }

}
