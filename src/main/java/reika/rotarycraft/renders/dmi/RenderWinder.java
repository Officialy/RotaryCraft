///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders.dmi;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Quaternion;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
//import net.minecraft.resources.ResourceLocation;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.IORenderer;
//import reika.rotarycraft.base.RotaryTERenderer;
//import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
//import reika.rotarycraft.blockentities.BlockEntityWinder;
//import reika.rotarycraft.items.ItemCoil;
//import reika.rotarycraft.model.animated.ModelWinder;
//
//public class RenderWinder extends RotaryTERenderer<BlockEntityWinder> {
//
//    private ModelWinder WinderModel = new ModelWinder();
//    //private ModelWinderV WinderModelV = new ModelWinderV();
//
//    public RenderWinder(BlockEntityRendererProvider.Context pContext) {
//
//    }
//
//    /**
//     * Renders the BlockEntity for the position.
//     */
//    public void renderBlockEntityWinderAt(PoseStack stack, BlockEntityWinder tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        ModelWinder var14;
//        var14 = WinderModel;
//        //ModelWinderV var15;
//        //var14 = this.WinderModelV;
////        this.bindTextureByName("/reika/rotarycraft/textures/BlockEntitytex/windertex.png");
//        RenderSystem.setShaderTexture(0, new ResourceLocation(RotaryCraft.MODID, "textures/blockentitytex/windertex.png"));
//        this.setupGL(stack, tile, par2, par4, par6);
//
//        int var11 = 0;     //used to rotate the model about metadata
//
//        if (tile.isInWorld()) {
//
//            switch (tile.getBlockState().getValue(getFacingProperty())) {
//                case SOUTH:
//                    var11 = 180;
//                    break;
//                case NORTH:
//                    var11 = 0;
//                    break;
//                case EAST:
//                    var11 = 270;
//                    break;
//                case WEST:
//                    var11 = 90;
//                    break;
//            }
//
////    todo        if (tile.getBlockMetadata() <= 3)
////                stack.mulPose(new Quaternion((float) var11 - 90, 0.0F, 1.0F, 0.0F));
////            else {
//            stack.mulPose(new Quaternion(var11, 1F, 0F, 0.0F));
//            stack.translate(0F, -1F, 1F);
////          todo      if (tile.getBlockMetadata() == 5)
////                    stack.translate(0F, 0F, -2F);
////            }
//        }
//
//        boolean hasSpring = tile.getStackInSlot(0) != null && tile.getStackInSlot(0).getItem() instanceof ItemCoil;
//        var14.renderAll(stack, tile, ReikaJavaLibrary.makeListFrom(hasSpring), -tile.phi, 0);
//
//        this.closeGL(stack, tile);
//    }
//
//    @Override
//    public void render(BlockEntityWinder tile, float v, PoseStack stack, MultiBufferSource multiBufferSource, int i, int i1) {
//        if (this.doRenderModel(stack, tile)) {
//            this.renderBlockEntityWinderAt(stack, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ(), v);
//        }
//        if (tile.isInWorld())// && MinecraftForgeClient.getRenderPass() == 1)
//            IORenderer.renderIO(stack, bufferSource, tile, tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "windertex.png";
//    }
//}
