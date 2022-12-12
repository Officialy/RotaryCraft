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
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.protocol.Packet;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.Material;
//import reika.dragonapi.instantiable.data.immutable.BlockBounds;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.rotarycraft.RotaryCraft;
//
//import java.util.Random;
//
//public class BlockBedrockSlice extends Block {
//    private final boolean last = false;
//
//    public BlockBedrockSlice() {
//        super(Material.STONE);
//        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
//        this.setBlockUnbreakable();
//        this.setResistance(3600000F);
//    }
//
//    @Override
//    protected boolean isAvailableInCreativeMode() {
//        return false;
//    }
//
//    @Override
//    public boolean canEntityDestroy(BlockGetter world, BlockPos pos, Entity e) {
//        return false;
//    }
//
//    /**
//     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
//     * cleared to be reused)
//     */
//    @Override
//    public AABB getCollisionBoundingBoxFromPool(Level world, BlockPos pos) {
//		/*
//		int var5 = world.getBlockMetadata(x, y, z);
//		if (var5 != 0)
//			return AABB.getBoundingBox(x + minX, y + minY, z + minZ, x + maxX, y + (15-var5)/16F, z + maxZ);
//		else
//			return AABB.getBoundingBox(x + minX, y + minY, z + minZ, x + maxX, y + maxY, z + maxZ);
//		 */
//        BlockEntity te = world.getBlockEntity(pos);
//        return te instanceof BlockEntityBedrockSlice ? ((BlockEntityBedrockSlice) te).getBounds().asAABB(pos) : ReikaAABBHelper.getBlockAABB(pos);
//    }
//
//    /**
//     * Updates the blocks bounds based on its current state. Args: world, x, y, z
//     */
//    @Override
//    public void setBlockBoundsBasedOnState(BlockGetter iba, BlockPos pos) {
//		/*
//		int var5 = iba.getBlockMetadata(x, y, z);
//		float var6 = 1F-(1 * (var5)) / 16.0F; //Get thinner each damageval++
//		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var6, 1.0F);
//		 */
//        ((BlockEntityBedrockSlice) iba.getBlockEntity(pos)).getBounds().copyToBlock(this);
//    }
//
//    @Override
//    public boolean isOpaqueCube() {
//        return false;
//    }
//
//    @Override
//    public boolean renderAsNormalBlock() {
//        return false;
//    }
//
//    @Override
//    public boolean canPlaceBlockAt(Level world, BlockPos pos) {
//        Block var5 = world.getBlockState(pos.below()).getBlock();
//        return var5 != Blocks.AIR && (var5 == Blocks.LEAVES || var5 == Blocks.LEAVES2 || var5.isOpaqueCube()) ? ReikaWorldHelper.getMaterial(world, pos.below()).blocksMotion() : false;
//    }
//
//    @Override
//    public Item getItemDropped(int par1, Random xRandom, int y) {
//        return null;
//    }
//
//    /**
//     * Returns the quantity of items to drop on block destruction.
//     */
//    @Override
//    public int quantityDropped(Random par1Random) {
//        return 0;
//    }
//
//    /**
//     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
//     * coordinates.  Args: blockAccess, x, y, z, side
//     */
//    @Override
//    public boolean shouldSideBeRendered(BlockGetter iba, BlockPos pos, int par5) {
//        return super.shouldSideBeRendered(iba, x, y, z, par5);//par5 == 1 ? true : super.shouldSideBeRendered(iba, x, y, z, par5);
//    }
//
//    @Override
//    public IIcon getIcon(int s) {
//        return icon;
//    }
//
//    @Override
//    public void registerBlockIcons(IIconRegister par1IconRegister) {
//        if (RotaryCraft.getInstance().isLocked())
//            return;
//        icon = par1IconRegister.registerIcon("bedrock");
//    }
//
//    @Override
//    public int getBlockTextureFromSideAndMetadata(int side, int metadata) {
//        return 0;
//    }
//
//    @Override
//    public boolean hasBlockEntity(int meta) {
//        return true;
//    }
//
//    @Override
//    public BlockEntity createBlockEntity(Level world) {
//        return new BlockEntityBedrockSlice();
//    }
//
//    public static class BlockEntityBedrockSlice extends BlockEntity {
//
//        public float dustYield = 1;
//        private Direction machineDirection = Direction.EAST;
//
//        public BlockEntityBedrockSlice(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
//            super(pType, pWorldPosition, pBlockState);
//        }
//
//        @Override
//        public boolean canUpdate() {
//            return false;
//        }
//
//        private BlockBounds getBounds() {
//            return BlockBounds.block().cut(machineDirection, this.getBlockMetadata() / 16D);
//        }
//
//        @Override
//        public void load(CompoundTag nbt) {
//            super.load(nbt);
//            dustYield = nbt.getFloat("yield");
//            machineDirection = Direction.values()[nbt.getInt("dir")];
//        }
//
//        @Override
//        public CompoundTag serializeNBT() {
//            CompoundTag nbt = new CompoundTag();
//            nbt.putFloat("yield", dustYield);
//            nbt.putInt("dir", machineDirection.ordinal());
//            return nbt;
//        }
//
//
//        @Override
//        public boolean shouldRefresh(Block oldBlock, Block newBlock, Level world, BlockPos pos) {
//            return oldBlock != newBlock;
//        }
//
//        public void setDirection(Direction dir) {
//            machineDirection = dir;
//            level.updateNeighborsAt(worldPosition, this.getBlockState().getBlock());
//        }
//
//        /*@Override
//        public Packet getDescriptionPacket() {
//            CompoundTag nbt = new CompoundTag();
//            this.load(nbt);
//            S35PacketUpdateBlockEntity pack = new S35PacketUpdateBlockEntity(getBlockPos(), 0, nbt);
//            return pack;
//        }
//
//        @Override
//        public void onDataPacket(NetworkManager net, S35PacketUpdateBlockEntity p) {
//            this.load(p.field_148860_e);
//            level.markBlockForUpdate(xCoord, yCoord, zCoord);
//        }*/
//
//    }
//}
