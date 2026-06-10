package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityBevelGear;

public class BlockBevelGears extends BlockBasicMachine {

    public BlockBevelGears(Properties properties) {
        super(properties.noOcclusion());
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityBevelGear(pPos, pState);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            // 26.1 fix: animate phi locally from the synced omega so the bevel gear visibly
            // spins on the client; phi itself is not part of the sync packet.
            return (lvl, pos, st, be) -> {
                BlockEntityBevelGear bg = (BlockEntityBevelGear) be;
                if (bg.omega > 0) {
                    bg.phi += (float) reika.dragonapi.libraries.mathsci.ReikaMathLibrary
                            .doubpow(reika.dragonapi.libraries.mathsci.ReikaMathLibrary.logbase(bg.omega + 1, 2), 1.05);
                }
            };
        }
        return (pLevel1, pPos, pState1, pBlockEntity) -> ((BlockEntityBevelGear) pBlockEntity).updateEntity(pLevel1, pPos);
    }

    @Override
    protected boolean isCustomRendered() { return true; }
}