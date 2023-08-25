/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import reika.dragonapi.base.BlockTEBase;
import reika.dragonapi.interfaces.block.MachineRegistryBlock;
import reika.dragonapi.interfaces.registry.TileEnum;
import reika.rotarycraft.registry.MachineRegistry;

public abstract class BlockRotaryCraftMachine extends BlockTEBase implements MachineRegistryBlock {

    public boolean hasVerticalPlacement = false;

    /** This is the property that determines the possible directions the block is facing. This does contain UP and DOWN,
    * however they are not enabled unless {@link #hasVerticalPlacement} is set to true.
     * Setting a block that does not have vertical placement to face up or down will cause a crash. This may be fixed in the future.
    */
    public static DirectionProperty FACING = BlockStateProperties.FACING;

    public BlockRotaryCraftMachine(BlockBehaviour.Properties properties) {
        super(properties.strength(4, 15));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        if(hasVerticalPlacement) {
            return this.defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection().getOpposite());
        } else {
            return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
        }
    }

    @Override
    public final TileEnum getMachine(BlockGetter world, BlockPos pos) {
        return MachineRegistry.getMachine((Level) world, pos);
    }
}