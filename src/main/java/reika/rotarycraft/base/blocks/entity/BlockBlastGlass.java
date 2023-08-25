/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.registry.RotaryItems;

import java.util.ArrayList;

public class BlockBlastGlass extends Block {

    private final ArrayList<Integer> allDirs = new ArrayList<>();

    public BlockBlastGlass() {
        super(Properties.copy(Blocks.GLASS).strength(10, 6000F));
        //this.setCreativeTab(RotaryCraft.ROTARY);
        //this.setHarvestLevel("pickaxe", 3);
        //this.blockIndexInTexture = 74;

        for (int i = 1; i < 10; i++) {
            allDirs.add(i);
        }
    }

    /**
     * Determines if this block is can be destroyed by the specified entities normal behavior.
     *
     * @param state  The current state
     * @param world  The current world
     * @param pos    Block position in world
     * @param entity
     * @return True to allow the ender dragon to destroy this block
     */
    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter world, BlockPos pos, Entity entity) {
        return false;
    }

    //@Override
    public boolean shouldSideBeRendered(BlockGetter iba, BlockPos pos, int side) {
        Direction dir = Direction.values()[side];
        return iba.getBlockState(new BlockPos(pos.getX() + dir.getStepX(), pos.getY() + dir.getStepY(), pos.getZ() + dir.getStepZ())).getBlock() != this;
    }

    /**
     * @param pExplosion
     * @return whether this block should drop its drops when destroyed by the given explosion
     */
    @Override
    public boolean dropFromExplosion(Explosion pExplosion) {
        return false;
    }

    /**
     * Determines if the player can harvest this block, obtaining it's drops when the block is destroyed.
     *
     * @param state
     * @param world  The current world
     * @param pos    The block's current position
     * @param player The player damaging the block
     * @return True to spawn the drops
     */
    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        ItemStack item = player.getInventory().getSelected();
        if (item == null)
            return false;
        if (item.getItem() == Items.DIAMOND_PICKAXE || item.getItem() == RotaryItems.BEDROCK_ALLOY_PICK.get())
            return true;
        return item.getItem().canAttackBlock(Blocks.OBSIDIAN.defaultBlockState(), player.level(), pos, player);
    }

    /**
     * This block can only be destroyed by the wither explosions - this in effect makes it witherproof
     */
    //@Override
    public void onBlockDestroyedByExplosion(Level world, BlockPos pos, Explosion ex) {
        world.setBlock(pos, this.defaultBlockState(), 1);
    }

}
