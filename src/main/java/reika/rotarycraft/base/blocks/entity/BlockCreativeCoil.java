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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.blockentities.BlockEntityCreativeCoil;
import reika.rotarycraft.registry.RotaryBlockEntities;

/**
 * Creative Coil -- an "infinite Industrial Coil": a full-cube block whose BE emits a large, constant
 * shaft-power output to every adjacent power receiver, for free, so machines (notably the ReactorCraft
 * solenoid magnet) can be powered for testing without a real drivetrain. A plain {@link EntityBlock}
 * rather than a full RotaryCraft machine -- it has no facing, GUI, fuel or heat.
 */
public class BlockCreativeCoil extends Block implements EntityBlock {

	public BlockCreativeCoil(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BlockEntityCreativeCoil(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide() || type != RotaryBlockEntities.CREATIVE_COIL.get())
			return null;
		return (lvl, pos, st, be) -> ((BlockEntityCreativeCoil) be).serverTick();
	}
}
