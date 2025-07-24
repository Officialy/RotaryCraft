///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface.conversion;
//
//import com.mojang.blaze3d.shaders.BlendMode;
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.renderer.MultiBufferSource;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.neoforged.fluids.FluidStack;
//
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.modinterface.model.BoilerModel;
//
//public class RenderBoiler extends RotaryTERenderer {
//
//    private BoilerModel boilerModel = new BoilerModel();
//
//    @Override
//    protected String getTextureSubfolder() {
//        return "Converter/";
//    }
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityBoilerAt(TileEntityBoiler tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        BoilerModel var14;
//        var14 = boilerModel;
//
//        this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/Converter/boilertex.png");
//
//        GL11.glPushMatrix();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glTranslatef((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        GL11.glScalef(1.0F, -1.0F, -1.0F);
//        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
//        int var11 = 0;
//        float var13;
//
//
//        var14.renderAll(tile, null, -tile.phi, 0);
//
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        GL11.glPopMatrix();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//    }
//
//    @Override
//    public void render(BlockEntityReservoir tile, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderTileEntityBoilerAt((TileEntityBoiler) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//            this.renderWater((TileEntityBoiler) tile, par2, par4, par6);
//        }
//    }
//
//    private void renderWater(TileEntityBoiler tile, double par2, double par4, double par6) {
//        FluidStack liquid = FluidRegistry.getFluidStack("water", 1);
//        int amount = tile.getWater();
//        if (amount == 0)
//            return;
//        if (amount > tile.CAPACITY)
//            amount = tile.CAPACITY;
//
//        int[] displayList = ReikaLiquidRenderer.getGLLists(liquid, tile.worldObj, false);
//
//        if (displayList == null) {
//            return;
//        }
//
//        GL11.glPushMatrix();
//        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
//        GL11.glEnable(GL11.GL_CULL_FACE);
//        GL11.glDisable(GL11.GL_LIGHTING);
//        GL11.glEnable(GL11.GL_BLEND);
//        BlendMode.DEFAULT.apply();
//
//        ReikaLiquidRenderer.bindFluidTexture(FluidRegistry.WATER);
//        ReikaLiquidRenderer.setFluidColor(liquid);
//
//        stack.translate(par2, par4, par6);
//
//        stack.translate(0, 0.0625, 0);
//
//        stack.translate(0, 0.001, 0);
//        GL11.glScaled(1, 0.95, 1);
//        GL11.glScaled(0.99, 0.95, 0.99);
//
//        GL11.glCallList(displayList[(int) (amount / ((double) tile.CAPACITY) * (ReikaLiquidRenderer.LEVELS - 1))]);
//
//        GL11.glPopAttrib();
//        GL11.glPopMatrix();
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "boilertex.png";
//    }
//
//    @Override
//    public void render(BlockEntity p_112307_, float p_112308_, PoseStack p_112309_, MultiBufferSource p_112310_, int p_112311_, int p_112312_) {
//
//    }
//}
