///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders.mi;
//
//import net.minecraft.BlockEntity.BlockEntity;
//import net.neoforged.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.auxiliary.HeatRippleRenderer;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.blockentities.auxiliary.BlockEntityHeater;
//import reika.rotarycraft.models.ModelHeater;
//
//public class RenderHeater extends RotaryTERenderer {
//
//    private ModelHeater HeaterModel = new ModelHeater();
//
//    @Override
//    protected String getTextureSubfolder() {
//        return "Heater/";
//    }
//
//    public void renderBlockEntityHeaterAt(BlockEntityHeater tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelHeater var14;
//        var14 = HeaterModel;
//
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/Heater/heatertex.png");
//        if (tile.temperature >= 200)
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/Heater/heatertex200C.png");
//        if (tile.temperature >= 400)
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/Heater/heatertex400C.png");
//        if (tile.temperature >= 600)
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/Heater/heatertex600C.png");
//        if (tile.temperature >= 800)
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/Heater/heatertex800C.png");
//        if (tile.temperature >= 900)
//            this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/Heater/heatertex900C.png");
//
//        stack.pushPose();
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        stack.translate((float) par2, (float) par4 + 2.0F, (float) par6 + 1.0F);
//        stack.scale(1.0F, -1.0F, -1.0F);
//        stack.translate(0.5F, 0.5F, 0.5F);
//        int var11 = 0;     //used to rotate the model about metadata
//
//        var14.renderAll(stack, tile, null);
//
//        if (tile.isInWorld()) {
//            float f = tile.temperature / (float) tile.MAXTEMP;
//            stack.translate(0, 0.675, 0);
//            HeatRippleRenderer.instance.addHeatRippleEffectIfLOS(tile, tile.xCoord + 0.5, tile.yCoord + 0.75, tile.zCoord + 0.5, f, 0.5F, 2F, 0);
//            stack.translate(0, -0.4, 0);
//            HeatRippleRenderer.instance.addHeatRippleEffectIfLOS(tile, tile.xCoord + 0.5, tile.yCoord + 1.75, tile.zCoord + 0.5, f, 0.25F, 2.5F, 0);
//        }
//
//        if (tile.isInWorld())
//            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        stack.popPose();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityHeaterAt((BlockEntityHeater) tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(tile, par2, par4, par6);
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        BlockEntityHeater tile = (BlockEntityHeater) te;
//        String name;
//        if (tile.temperature >= 200)
//            name = "heatertex200C.png";
//        else if (tile.temperature >= 400)
//            name = "heatertex400C.png";
//        else if (tile.temperature >= 600)
//            name = "heatertex600C.png";
//        else if (tile.temperature >= 800)
//            name = "heatertex800C.png";
//        else if (tile.temperature >= 900)
//            name = "heatertex900C.png";
//        else
//            name = "heatertex.png";
//        return name;
//    }
//}
