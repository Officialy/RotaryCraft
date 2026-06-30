package reika.rotarycraft.base.blocks.entity.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.blockentities.piping.BlockEntityBedrockPipe;
import reika.rotarycraft.registry.MachineRegistry;

public class BlockBedrockPipe extends BlockPipeShell {

    public BlockBedrockPipe(Properties properties) {
        super(properties);
    }

    @Override
    protected MachineRegistry self() {
        return MachineRegistry.BEDPIPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityBedrockPipe(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : ((pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityBedrockPipe) pBlockEntity).updateEntity(pLevel1, pPos);
        });
    }
}
