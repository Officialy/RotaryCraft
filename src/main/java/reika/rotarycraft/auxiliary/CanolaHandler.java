/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import net.minecraft.world.level.block.CropBlock;
import reika.rotarycraft.base.blocks.CanolaBlock;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;
import reika.dragonapi.ModList;
import reika.dragonapi.interfaces.CustomCropHandler;
import reika.dragonapi.interfaces.registry.ModEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.List;

public final class CanolaHandler implements CustomCropHandler {

    @Override
    public boolean isCrop(Block id) {
        return ModList.ROTARYCRAFT.isLoaded() && id == RotaryBlocks.CANOLA.get();
    }

    @Override
    public boolean isRipeCrop(Level world, BlockPos pos) {
        return ModList.ROTARYCRAFT.isLoaded() && this.isCrop(world.getBlockState(pos).getBlock()) && world.getBlockState(pos).getValue(CanolaBlock.AGE) == CanolaBlock.MAX_AGE;
    }

    @Override
    public void makeRipe(Level world, BlockPos pos) {
        if (ModList.ROTARYCRAFT.isLoaded())
            world.setBlock(pos, RotaryBlocks.CANOLA.get().defaultBlockState().setValue(CropBlock.AGE, 7), 1);
    }

    @Override
    public int getGrowthState(Level level, BlockPos blockPos) {
        return 0;
    }

    @Override
    public boolean isSeedItem(ItemStack is) {
        return ModList.ROTARYCRAFT.isLoaded() && is.getItem() == RotaryItems.CANOLA_SEEDS.get();
    }

    @Override
    public List<ItemStack> getDropsOverride(Level level, BlockPos blockPos, Block block, int i) {
        return null;
    }

    @Override
    public List<ItemStack> getAdditionalDrops(Level world, BlockPos pos, Block id, int fortune) {
        return Collections.emptyList();
    }

    @Override
    public void editTileDataForHarvest(Level world, BlockPos pos) {

    }

    @Override
    public boolean initializedProperly() {
        if (!ModList.ROTARYCRAFT.isLoaded()) return false;
        RotaryBlocks.CANOLA.get();
        return true;
    }

    @Override
    public ModEntry getMod() {
        return ModList.ROTARYCRAFT;
    }

    @Override
    public int getColor() {
        return 0x00cc00;
    }

    @Override
    public String getEnumEntryName() {
        return "CANOLA";
    }

    @Override
    public boolean isBlockEntity() {
        return false;
    }

    @Override
    public boolean neverDropsSecondSeed() {
        return false;
    }
	/*
	@Override
	public CropFormat getShape() {
		return CropFormat.PLANT;
	}*/

}
