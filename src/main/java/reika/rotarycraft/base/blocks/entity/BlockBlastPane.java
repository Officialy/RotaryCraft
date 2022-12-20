///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.base.blocks.ee;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.Explosion;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.StainedGlassPaneBlock;
//import net.minecraft.world.level.material.Material;
//import reika.rotarycraft.RotaryCraft;
//
//public class BlockBlastPane extends StainedGlassPaneBlock {
//
//    public BlockBlastPane() {
//        super(RotaryCraft.getInstance().isLocked() ? "" : "RotaryCraft:obsidiglass", "RotaryCraft:obsidiglass_side", Material.GLASS, true);
//        //there was a 74 here ^^
//        this.setHardness(12.5F);
//        this.setResistance(6000F);
//        this.setLightLevel(0F);
//        this.setStepSound(soundTypeGlass);
//        ////this.requiresSelfNotify[this.blockID] = true;
//        //this.blockIndexInTexture = 74;
//        this.setCreativeTab(RotaryCraft.getInstance().isLocked() ? null : RotaryCraft.tabRotary);
//        this.setHarvestLevel("pickaxe", 3);
//    }
//
//    @Override
//    public boolean canEntityDestroy(BlockGetter world, BlockPos pos, Entity e) {
//        return false;
//    }
//
//    /*
//    public int getRenderType() {
//        return 0;//ClientProxy.BlockSheetTexRenderID;
//    }
//     */
//    @Override
//    public float getExplosionResistance(Entity par1Entity, Level world, BlockPos pos, double explosionX, double explosionY, double explosionZ) {
//        return 6000F;
//    }
//
//    @Override
//    public boolean canDropFromExplosion(Explosion par1Explosion) {
//        return false;
//    }
//
//    @Override
//    public boolean canHarvestBlock(Player ep) {
//        ItemStack item = ep.inventory.getSelected();
//        if (item == null)
//            return false;
//        return item.getItem() == Items.DIAMOND_PICKAXE || item.getItem() == RotaryItems.BEDPICK.get();
//    }
//
//    /**
//     * This block can only be destroyed by the wither explosions - this in effect makes it witherproof
//     */
//    @Override
//    public void onBlockDestroyedByExplosion(Level world, BlockPos pos, Explosion ex) {
//        world.setBlock(pos, this.defaultBlockState(), 0);
//    }
//
//    public String getTextureFile() {
//        return "/Reika/RotaryCraft/Textures/Terrain/textures.png"; //return the block texture where the block texture is saved in
//    }
//
//    @Override
//    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
//        return 74;
//    }
//	/*
//	@Override
//	public Icon getIcon(int s) {
//		return this.icon;
//	}
//
//	@Override
//	public void registerBlockIcons(IconRegister par1IconRegister) {
//		this.icon = par1IconRegister.registerIcon("RotaryCraft:obsidiglass");
//	}*/
//
//}
