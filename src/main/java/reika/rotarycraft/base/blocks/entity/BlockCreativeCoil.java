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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import reika.rotarycraft.blockentities.BlockEntityCreativeCoil;
import reika.rotarycraft.registry.RotaryBlockEntities;

/**
 * Creative Coil -- an "infinite Industrial Coil": a full-cube block whose BE emits a large, constant
 * shaft-power output to the single adjacent machine on its {@link #FACING} side, for free, so machines
 * (notably the ReactorCraft solenoid magnet) can be powered for testing without a real drivetrain.
 * A plain {@link EntityBlock} rather than a full RotaryCraft machine -- no GUI, fuel or heat.
 *
 * <p>FACING is set like every RotaryCraft machine ({@code getNearestLookingDirection().getOpposite()}):
 * placed on a floor it points UP, so putting one under the solenoid (which reads power from below)
 * just works.</p>
 */
public class BlockCreativeCoil extends Block implements EntityBlock {

	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

	public BlockCreativeCoil(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getNearestLookingDirection().getOpposite());
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
