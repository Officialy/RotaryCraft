package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.farming.BlockEntitySprinkler;

public class BlockSprinkler extends BlockBasicMachine {

    public BlockSprinkler(Properties properties) {
        super(properties.noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntitySprinkler(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        // Ticks BOTH sides: the base tick routes performEffects to hydration on the server and
        // spray particles on the client (exactly the legacy updateEntity behaviour).
        return (pLevel1, pPos, pState1, pBlockEntity) ->
                ((BlockEntitySprinkler) pBlockEntity).tick(pLevel1, pPos);
    }

    @Override
    protected boolean isCustomRendered() {
        return true;
    }
}
