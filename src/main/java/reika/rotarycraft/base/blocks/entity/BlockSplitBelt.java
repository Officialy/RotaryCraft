package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.blockentities.transmission.BlockEntitySplitBelt;

public class BlockSplitBelt extends BlockBeltHub {

    public BlockSplitBelt(Properties properties) {
        super(false, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntitySplitBelt(pPos, pState);
    }
}
