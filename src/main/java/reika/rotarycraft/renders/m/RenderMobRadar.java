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
//import reika.dragonapi.libraries.ReikaEntityHelper;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.rotarycraft.registry.RotaryItems;
//import reika.rotarycraft.blockentities.surveying.BlockEntityMobRadar;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.Tesselator;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.Player;
//import net.neoforged.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaColorAPI;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.Animated.ModelRadar;
//
//import java.util.List;
//
//@SideOnly(Dist.CLIENT)
//public class RenderMobRadar extends RotaryTERenderer {
//
//    private ModelRadar RadarModel = new ModelRadar();
//    //private ModelMobRadarV MobRadarModelV = new ModelMobRadarV();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityMobRadarAt(BlockEntityMobRadar tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//        ModelRadar var14;
//        var14 = RadarModel;
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/radartex.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 0;     //used to rotate the model about metadata
//
//        var14.renderAll(stack, tile, null, -tile.phi);
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityMobRadarAt((BlockEntityMobRadar) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//            this.renderHUD((BlockEntityMobRadar) tile, par2, par4, par6);
//        }
//    }
//
//    private void renderHUD(BlockEntityMobRadar te, double par2, double par4, double par6) {
//        if (te == null)
//            return;
//        if (!te.isInWorld())
//            return;
//        List<LivingEntity> li = te.getEntities();
//        if (li == null)
//            return;
//        Minecraft mc = Minecraft.getInstance();
//        Player ep = te.getPlacer();
//        if (ep == null)
//            return;
//        if (!ReikaInventoryHelper.checkForItem(RotaryItems.MOTION.get(), ep.inventory.mainInventory))
//            return;
//        if (mc.thePlayer.getCommandSenderName() != ep.getCommandSenderName())
//            return;
//        ReikaRenderHelper.disableLighting();
//        stack.pushPose();
//        //stack.translate((float)par2, (float)par4 + 2.0F, (float)par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glDisable(GL11.GL_TEXTURE_2D);
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glEnable(GL11.GL_CULL_FACE);
//        GL11.glDisable(GL11.GL_DEPTH_TEST);
//        Tesselator tess = Tesselator.getInstance();
//        BufferBuilder v5 = tess.getBuilder();
//        for (LivingEntity ent : li) {
//            if (ent != null && ent != mc.thePlayer) {
//                float v = ent.height;
//                v = 2;
//                v5.startDrawing(GL11.GL_LINE_LOOP);
//                int color = ReikaEntityHelper.mobToColor(ent);
//                int[] rgb = ReikaColorAPI.HexToRGB(color);
//                v5.setColorOpaque(rgb[0], rgb[1], rgb[2]);
//                v5.vertex(par2 + ent.posX - te.xCoord, par4 + ent.posY - te.yCoord, par6 + ent.posZ - te.zCoord);
//                v5.vertex(par2 + ent.posX - te.xCoord, par4 + ent.posY + v - te.yCoord, par6 + ent.posZ - te.zCoord);
//                //v5.vertex(ent.posX, ent.posY+ent.height, ent.posZ);
//                v5.end();
//            }
//        }
//        GL11.glEnable(GL11.GL_DEPTH_TEST);
//        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        ReikaRenderHelper.enableLighting();
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "radartex.png";
//    }
//}
