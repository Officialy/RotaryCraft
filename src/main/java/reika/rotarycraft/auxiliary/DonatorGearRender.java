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

import reika.dragonapi.interfaces.PlayerRenderObj;

public class DonatorGearRender implements PlayerRenderObj {

    public static final DonatorGearRender instance = new DonatorGearRender();

    private DonatorGearRender() {

    }

/* todo   @Override
    public void render(PoseStack stack, Player ep, float ptick) {
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
        matrixStack.mulPose(new Quaternion(angle, 0, 0, 1));
        matrixStack.translate(0, -d, 0);
        matrixStack.mulPose(new Quaternion(90, 0, 1, 0));
        float s = 0.5f;
        matrixStack.scale(s, s, s);
        //GL11.glRotated(45-ep.rotationPitch+90, 1, 0, 0);
        //GL11.glRotated(RenderManager.instance.playerViewY-ep.rotationYawHead-45, 0, 1, 0);
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        MultiBufferSource.BufferSource renderTypeBufferImpl = Minecraft.getInstance().renderBuffers().bufferSource();
        itemRenderer.render(new ItemStack(RotaryItems.HSLA_STEEL_GEAR.get()), ItemTransforms.TransformType.HEAD, false, matrixStack, renderTypeBufferImpl, 1, 1, null);
        matrixStack.popPose();
    }*/

    @Override
    public int getRenderPriority() {
        return 0;
    }

}
