///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2018
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.renders;
//
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.block.Block;
//
//
//@Deprecated
//public class MachineISBRH extends ISBRH {
//
//    protected MachineISBRH(int id) {
//        super(id);
//    }
//
//    @Override
//    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
//
//    }
//
//    @Override
//    public boolean renderWorldBlock(BlockGetter world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
//        BlockEntitySpecialRenderer tesr = BlockEntityRendererDispatcher.instance.getSpecialRenderer(world.getBlockEntity(x, y, z));
//        if (tesr instanceof StaticModelRenderer) {
//            Model rmb = ((StaticModelRenderer) tesr);
//            //ISBRHModel model = new ISBRHModel();
//        }
//        return true;
//    }
//
//    @Override
//    public boolean shouldRender3DInInventory(int modelId) {
//        return false;
//    }
//
//    public static interface StaticModelRenderer {
//
//        public Model getModel();
//
//    }
//
//}
