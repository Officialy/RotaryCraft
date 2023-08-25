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
//import reika.rotarycraft.RotaryCraft;
//import reika.dragonapi.libraries.ReikaNBTHelper;
//import reika.rotarycraft.items.BlockItemDecoTank;
//import reika.rotarycraft.blockentities.BlockEntityDecoTank;
//import reika.rotarycraft.blockentities.BlockEntityDecoTank.TankFlags;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.material.Fluid;
//
//
//import java.util.ArrayList;
//import java.util.HashSet;
//
//public class BlockDecoTank extends Block {
//
//    private final ArrayList<Integer> allDirs = new ArrayList<>();
//
//    public BlockDecoTank() {
//        super(Material.GLASS, "decotank", 1);
//        this.setResistance(2);
//        this.setHardness(0.35F);
//        this.setLightOpacity(0);
//        this.setCreativeTab(RotaryCraft.ROTARY);
//
//        for (int i = 1; i < 10; i++) {
//            allDirs.add(i);
//        }
//    }
//
//    @Override
//    public float getBlockHardness(Level world, BlockPos pos) {
//        float h = super.getBlockHardness(world, pos);
//        return TankFlags.RESISTANT.apply(world, pos) ? h * 2 : h;
//    }
//
//    @Override
//    public float getExplosionResistance(Entity e, Level world, BlockPos pos, double eX, double eY, double eZ) {
//        float r = super.getExplosionResistance(e, world, pos, eX, eY, eZ);
//        return TankFlags.RESISTANT.apply(world, pos) ? 600000 : r;
//    }
//
//    @Override
//    public void onBlockAdded(Level world, BlockPos pos) {
//        super.onBlockAdded(world, pos);
//        world.blockUpdated(pos, this.getBlockState().getBlock();
//    }
//
//    @Override
//    public void breakBlock(Level world, BlockPos pos, Block id) {
//        super.breakBlock(world, pos, id);
//        world.blockUpdated(pos, this.getBlockState().getBlock();
//    }
//
//    @Override
//    public ArrayList<ItemStack> getDrops(Level world, BlockPos pos, int fortune) {
//        ArrayList<ItemStack> li = super.getDrops(world, pos, fortune);
//        BlockEntityDecoTank te = (BlockEntityDecoTank) world.getBlockEntity(pos);
//        if (te != null) {
//            Fluid f = te.getFluid();
//
//            ItemStack is = li.get(0);
//
//            if (f != null) {
//                is.put(new CompoundTag());
//                ReikaNBTHelper.writeFluidToNBT(is.getTag(), f);
//                is.getTag().putInt("level", BlockItemDecoTank.FILL);
//            }
//
//            int dropmeta = 0;
//            for (int k = 0; k < TankFlags.list.length; k++) {
//                TankFlags fg = TankFlags.list[k];
//                if (te.getFlag(fg)) {
//                    dropmeta = dropmeta | fg.getItemMetadataBit();
//                }
//            }
//
//            is.setItemDamage(dropmeta);
//        } else
//            li.clear();
//        return li;
//    }
//
//    @Override
//    public boolean removedByPlayer(Level world, Player player, BlockPos pos, boolean harv) {
//        if (!player.isCreative())
//            this.harvestBlock(world, player, pos, world.getBlockMetadata(pos));
//        return world.setBlockToAir(pos);
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
//    public int getRenderType() {
//        return RotaryCraft.proxy.connectedRender;
//    }
//
//    @Override
//    public int getRenderBlockPass() {
//        return 1;
//    }
//
//    @Override
//    public boolean renderCentralTextureForItem(int meta) {
//        return !TankFlags.CLEAR.applyItem(meta);
//    }
//
//    public HashSet<Integer> getEdgesForFace(BlockGetter world, BlockPos pos, Direction face) {
//        HashSet<Integer> li = new HashSet();
//        li.addAll(allDirs);
//
//        if (TankFlags.CLEAR.apply(world, pos))
//            li.remove(5); //glass tex
//
//        if (face.offsetX != 0) { //test YZ
//            //sides; removed if have adjacent on side
//            if (world.getBlockState(pos + 1).getBlock() == this)
//                li.remove(2);
//            if (world.getBlockState(pos - 1).getBlock() == this)
//                li.remove(8);
//            if (world.getBlockState(x, y + 1, z).getBlock() == this)
//                li.remove(4);
//            if (world.getBlockState(x, y - 1, z).getBlock() == this)
//                li.remove(6);
//
//            //Corners; only removed if have adjacent on side AND corner
//            if (world.getBlockState(x, y + 1, z + 1) == this && !li.contains(4) && !li.contains(2))
//                li.remove(1);
//            if (world.getBlockState(x, y - 1, z - 1) == this && !li.contains(6) && !li.contains(8))
//                li.remove(9);
//            if (world.getBlockState(x, y + 1, z - 1) == this && !li.contains(4) && !li.contains(8))
//                li.remove(7);
//            if (world.getBlockState(x, y - 1, z + 1) == this && !li.contains(2) && !li.contains(6))
//                li.remove(3);
//        }
//        if (face.offsetY != 0) { //test XZ
//            //sides; removed if have adjacent on side
//            if (world.getBlockState(pos.above()).getBlockState() == this)
//                li.remove(2);
//            if (world.getBlocks(pos - 1) == this)
//                li.remove(8);
//            if (world.getBlockState(x + 1, pos.getY(), z) == this)
//                li.remove(4);
//            if (world.getBlockState(x - 1, pos.getY(), z) == this)
//                li.remove(6);
//
//            //Corners; only removed if have adjacent on side AND corner
//            if (world.getBlockState(x + 1, pos.getY(), pos.getZ() + 1) == this && !li.contains(4) && !li.contains(2))
//                li.remove(1);
//            if (world.getBlockState(x - 1, pos.getY(), pos.getZ() - 1) == this && !li.contains(6) && !li.contains(8))
//                li.remove(9);
//            if (world.getBlockState(x + 1, pos.getY(), pos.getZ() - 1) == this && !li.contains(4) && !li.contains(8))
//                li.remove(7);
//            if (world.getBlockState(x - 1, pos.getY(), pos.getZ() + 1) == this && !li.contains(2) && !li.contains(6))
//                li.remove(3);
//        }
//        if (face.offsetZ != 0) { //test XY
//            //sides; removed if have adjacent on side
//            if (world.getBlockState(x, pos.getY() + 1, z) == this)
//                li.remove(4);
//            if (world.getBlockState(x, pos.getY() - 1, z) == this)
//                li.remove(6);
//            if (world.getBlockState(x + 1, pos.getY(), z) == this)
//                li.remove(2);
//            if (world.getBlockState(x - 1, pos.getY(), z) == this)
//                li.remove(8);
//
//            //Corners; only removed if have adjacent on side AND corner
//            if (world.getBlockState(new BlockPos(pos.getX() + 1, pos.getY() + 1, pos.getZ())).getBlockState() == this && !li.contains(2) && !li.contains(4))
//                li.remove(1);
//            if (world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY() - 1, z)) == this && !li.contains(8) && !li.contains(6))
//                li.remove(9);
//            if (world.getBlockState(new BlockPos(pos.getX() + 1, pos.getY() - 1, z)) == this && !li.contains(2) && !li.contains(6))
//                li.remove(3);
//            if (world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY() + 1, z)) == this && !li.contains(4) && !li.contains(8))
//                li.remove(7);
//        }
//        return li;
//    }
//
//    @Override
//    public boolean shouldSideBeRendered(BlockGetter iba, BlockPos pos, int side) {
//        Direction dir = Direction.values()[side];
//        return iba.getBlockState(new BlockPos(pos.getX() + dir.getStepX(), pos.getY() + dir.getStepY(), pos.getZ() + dir.getStepZ())).getBlockState() != this;
//    }
//
//
//    public Fluid getFluid(BlockGetter world, BlockPos pos) {
//        Block id = world.getBlockState(pos).getBlockState();
//        if (id == this) {
//            BlockEntity te = world.getBlockEntity(pos);
//            if (te instanceof BlockEntityDecoTank) {
//                return ((BlockEntityDecoTank) te).getFluid();
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public int getLightValue(BlockGetter world, BlockPos pos) {
//        if (TankFlags.LIGHTED.apply(world, pos))
//            return 15;
//        Fluid f = this.getFluid(world, pos);
//        return f != null ? f.getLuminosity() : 0;
//    }
//
//    @Override
//    public ItemStack getPickBlock(BlockHitResult target, Level world, BlockPos pos) {
//        BlockEntityDecoTank te = (BlockEntityDecoTank) world.getBlockEntity(pos);
//        ItemStack is = getDrops(world, pos, world.getBlockMetadata(pos), 0).get(0);
//        return is;
//    }
//
//}
