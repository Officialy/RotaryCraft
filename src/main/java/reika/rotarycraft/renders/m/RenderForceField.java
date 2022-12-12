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
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.client.MinecraftForgeClient;
//import reika.dragonapi.interfaces.blockentity.RenderFetcher;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//
//public class RenderForceField extends RenderProtectionDome {
//    @Override
//    public void renderBlockEntityAt(BlockEntity tile, PoseStack stack, MultiBufferSource bufferSource, int light) {
//        super.renderBlockEntityAt(tile, par2, par4, par6, par8);
//        if (((RotaryCraftBlockEntity) tile).isInWorld() && MinecraftForgeClient.getRenderPass() == 1) {
//            if (((BlockEntityForceField) tile).getEnchantmentHandler().hasEnchantments())
//                EnchantmentRenderer.renderGlint(tile, model, null, par2, par4, par6);
//        } else if (!tile.hasLevel()) {
//            if (((BlockEntityForceField) tile).getEnchantmentHandler().hasEnchantments())
//                EnchantmentRenderer.renderGlint(tile, model, null, par2, par4, par6);
//        }
//    }
//
//    @Override
//    public String getImageFileName(RenderFetcher te) {
//        return "forcetex2.png";
//    }
//}
