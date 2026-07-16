package reika.rotarycraft.base.blocks.entity.transmission;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
// 1.21.5: net.neoforged.common.property.Properties removed; use vanilla BlockStateProperties instead.
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;

import javax.annotation.Nullable;

public class BlockCoil extends BlockBasicMachine {
    public BlockCoil(Properties properties) {
        super(properties.noOcclusion());
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityAdvancedGear(BlockEntityAdvancedGear.GearType.COIL, pos, state);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            @SuppressWarnings("unchecked")
            BlockEntityTicker<T> t = (BlockEntityTicker<T>) clientPhiTicker(BlockEntityAdvancedGear.class);
            return t;
        }
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityAdvancedGear) pBlockEntity).updateEntity(pLevel1, pPos);
        };
    }

    // No getDrops override: the generated coil loot table already drops the block's own item, which is
    // the whole drop. 1.7.10 needed the override because every advanced gear shared one block and one
    // ADVGEAR item, so the drop had to rebuild itself from the tile's metadata; here each gear type is
    // its own block, so there is nothing left to reconstruct. The only thing 1.7.10 carried on the
    // stack was the bedrock-coil flag, and bedrock coils do not exist yet in this port (setBedrock is
    // never called and the flag is never saved), so there is no state to preserve. Restore this
    // override when bedrock coils land.

    @Override
    protected boolean isCustomRendered() { return true; }
}