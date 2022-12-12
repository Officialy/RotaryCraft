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
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//
//import net.minecraft.world.level.block.entity.BlockEntity;
//
//
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.modinterface.model.SteamTurbineModel;
//
//public class RenderSteam extends RotaryTERenderer {
//
//    private SteamTurbineModel SteamModel = new SteamTurbineModel();
//    //private ModelSteamTurbineV SteamModelV = new ModelSteamTurbineV();
//
//    @Override
//    protected String getTextureSubfolder() {
//        return "Converter/";
//    }
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntitySteamAt(TileEntitySteam tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        SteamTurbineModel var14;
//        var14 = SteamModel;
//
//        this.bindTextureByName("/Reika/RotaryCraft/Textures/TileEntityTex/Converter/steamturbtex.png");
//
//        GL11.glPushMatrix();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glTranslatef((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        GL11.glScalef(1.0F, -1.0F, -1.0F);
//        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
//        int var11 = 0;
//        float var13;
//        switch (var9) {
//            case 2 -> var11 = 0;
//            case 0 -> var11 = 180;
//            case 1 -> var11 = 90;
//            case 3 -> var11 = 270;
//        }
//
//        GL11.glRotatef(var11, 0.0F, 1.0F, 0.0F);
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
//            this.renderTileEntitySteamAt((TileEntitySteam) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//        }
//    }
//
//}
