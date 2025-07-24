///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders.m;
//
//import reika.dragonapi.libraries.java.ReikaGLHelper.BlendMode;
//import reika.rotarycraft.blockentities.processing.BlockEntityDistillery;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.neoforged.client.MinecraftForgeClient;
//import net.neoforged.fluids.Fluid;
//import net.neoforged.fluids.FluidStack;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.Animated.ModelDistillery;
//
//public class RenderDistillery extends RotaryTERenderer {
//
//    private ModelDistillery DistilleryModel = new ModelDistillery();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityDistilleryAt(BlockEntityDistillery tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        ModelDistillery var14;
//        var14 = DistilleryModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/distillertex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 0;
//        float var13;
//
//
//        var14.renderAll(stack, tile, null, -tile.phi);
//
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityDistilleryAt((BlockEntityDistillery) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//            this.renderFuels((BlockEntityDistillery) tile, par2, par4, par6, ((BlockEntityDistillery) tile).getFluidInInput(), false);
//            this.renderFuels((BlockEntityDistillery) tile, par2, par4, par6, ((BlockEntityDistillery) tile).getFluidInOutput(), true);
//        }
//    }
//
//    private void renderFuels(BlockEntityDistillery tile, double par2, double par4, double par6, Fluid liq, boolean output) {
//        if (liq == null)
//            return;
//        FluidStack liquid = new FluidStack(liq, 1);
//
//        int amount = output ? tile.getOutputLevel() : tile.getInputLevel();
//        if (amount == 0)
//            return;
//        if (amount > tile.getCapacity())
//            amount = tile.getCapacity();
//
//        int[] displayList = ReikaLiquidRenderer.getGLLists(liquid, tile.level, false);
//
//        if (displayList == null) {
//            return;
//        }
//
//        stack.pushPose();
//        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
//        GL11.glEnable(GL11.GL_CULL_FACE);
//        GL11.glDisable(GL11.GL_LIGHTING);
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//
//        ReikaLiquidRenderer.bindFluidTexture(liq);
//        ReikaLiquidRenderer.setFluidColor(liquid);
//
//        stack.translate(par2, par4, par6);
//
//        stack.translate(0, output ? 10 / 16D : 1 / 16D, 0);
//
//        stack.translate(0, 0.001, 0);
//        stack.scale(1, 1 / 3D, 1);
//        stack.scale(0.99, 0.95, 0.99);
//
//        GL11.glCallList(displayList[(int) (amount / ((double) tile.getCapacity()) * (ReikaLiquidRenderer.LEVELS - 1))]);
//
//        //GL11.glPopAttrib();
//        stack.popPose();
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "distillertex.png";
//    }
//}
