///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders.dm;
//
//import reika.rotarycraft.blockentities.Farming.BlockEntityWoodcutter;
//import net.minecraft.BlockEntity.BlockEntity;
//import net.minecraftforge.client.MinecraftForgeClient;
//import org.lwjgl.opengl.GL11;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.rendering.ReikaColorAPI;
//import reika.rotarycraft.auxiliary.EnchantmentRenderer;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.models.Animated.ModelWoodcutter;
//
//public class RenderWoodcutter extends RotaryTERenderer {
//
//    private ModelWoodcutter WoodcutterModel = new ModelWoodcutter();
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityWoodcutterAt(BlockEntityWoodcutter tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        int var9;
//
//        if (!tile.isInWorld())
//            var9 = 0;
//        else
//            var9 = tile.getBlockMetadata();
//
//        ModelWoodcutter var14;
//        var14 = WoodcutterModel;
//        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/woodcuttertex.png");
//
//        this.setupGL(tile, par2, par4, par6);
//
//        int var11 = 0;     //used to rotate the model about metadata
//
//        if (tile.isInWorld()) {
//            switch (tile.getBlockMetadata()) {
//                case 0:
//                    var11 = 180;
//                    break;
//                case 1:
//                    var11 = 0;
//                    break;
//                case 2:
//                    var11 = 270;
//                    break;
//                case 3:
//                    var11 = 90;
//                    break;
//            }
//
//            stack.mulPose((float) var11 - 90, 0.0F, 1.0F, 0.0F);
//
//        }
//        float var13;
//
//        if (tile.isInWorld()) {
//            int c = tile.getJamColor();
//            if (c != -1)
//                GL11.glColor4f(ReikaColorAPI.getRed(c) / 255F, ReikaColorAPI.getGreen(c) / 255F, ReikaColorAPI.getBlue(c) / 255F, 1);
//        }
//
//        var14.renderAll(stack, tile, null, tile.phi);
//
//        this.closeGL(stack, tile);
//    }
//
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        if (this.doRenderModel((RotaryCraftBlockEntity) tile))
//            this.renderBlockEntityWoodcutterAt((BlockEntityWoodcutter) tile, par2, par4, par6, par8);
//        if (MinecraftForgeClient.getRenderPass() != 0 && tile.hasLevel()) {
//            IORenderer.renderIO(tile, par2, par4, par6);
//            if (((BlockEntityWoodcutter) tile).getEnchantmentHandler().hasEnchantments())
//                EnchantmentRenderer.renderGlint(tile, WoodcutterModel, null, par2, par4, par6);
//        } else if (!tile.hasLevel()) {
//            int var11 = 0;
//            switch (tile.getBlockMetadata()) {
//                case 0:
//                    var11 = 180;
//                    break;
//                case 1:
//                    var11 = 0;
//                    break;
//                case 2:
//                    var11 = 270;
//                    break;
//                case 3:
//                    var11 = 90;
//                    break;
//            }
//            stack.mulPose((float) var11 - 90, 0.0F, 1.0F, 0.0F);
//            if (((BlockEntityWoodcutter) tile).getEnchantmentHandler().hasEnchantments())
//                EnchantmentRenderer.renderGlint(tile, WoodcutterModel, null, par2, par4, par6);
//        }
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "woodcuttertex.png";
//    }
//}
