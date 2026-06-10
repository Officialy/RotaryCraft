package reika.rotarycraft.base.blocks.entity.transmission;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntitySplitter;

public class BlockSplitter extends BlockBasicMachine {

    public BlockSplitter(Properties properties) {
        super(properties.noOcclusion());
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntitySplitter(pos, state);
    }

    /**
     * 26.1: initialise {@link BlockEntitySplitter#ioside} from FACING immediately on placement
     * so the very first sync packet to the client carries the correct orientation. Without
     * this, the client briefly rendered the default ioside (0 = WEST), saw the BER orient the
     * model at WEST plus IO arrows pointing the wrong way, then snapped to the FACING-derived
     * orientation when the first server tick fired {@code initIosideFromFacing()} — the user
     * saw this as "spazzes out for a second / model rotates around / IO render flickers".
     */
    @Override
    public void setPlacedBy(net.minecraft.world.level.Level world, BlockPos pos, BlockState state,
                            net.minecraft.world.entity.LivingEntity placer, net.minecraft.world.item.ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof BlockEntitySplitter sp) {
            sp.initIosideFromFacingPublic();
            sp.syncAllData(true);
        }
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            // 26.1 fix: server-side splitter's tick increments {@code phi} based on its
            // {@code omega}, but {@code phi} isn't part of the sync packet — too expensive to
            // ship every tick. So mirror the server's animateWithTick on the client by
            // accumulating phi from the locally-synced {@code omega}; that's what makes the
            // cross-shafts visibly spin when the splitter is powered.
            //
            // Also calls {@code getIOSides} to materialise the read/write/read2/write2
            // {@link net.minecraft.core.Direction}s from the locally-synced {@code ioside},
            // because those fields aren't sync'd — server-only {@code updateEntity} normally
            // sets them. Without this the IO renderer (which reads getReadDirection /
            // getWriteDirection) and the splitter renderer (which reads getWriteDirection
            // for model orientation) stayed pointed at whatever ioside the BE was at the
            // moment it loaded, even after the player rotated it with the screwdriver.
            return (lvl, pos, st, be) -> {
                BlockEntitySplitter sp = (BlockEntitySplitter) be;
                sp.getIOSides(lvl, pos, sp.getIoside());
                if (sp.omega > 0) {
                    sp.phi += (float) reika.dragonapi.libraries.mathsci.ReikaMathLibrary
                            .doubpow(reika.dragonapi.libraries.mathsci.ReikaMathLibrary.logbase(sp.omega + 1, 2), 1.05);
                }
            };
        }
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntitySplitter) pBlockEntity).updateEntity(pLevel1, pPos);
        };
    }

    @Override
    protected boolean isCustomRendered() { return true; }
}
