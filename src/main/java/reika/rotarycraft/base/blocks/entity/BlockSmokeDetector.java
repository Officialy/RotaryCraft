package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.BlockEntitySmokeDetector;
import reika.rotarycraft.blockentities.farming.BlockEntityMobHarvester;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * Block class shared by both the smoke detector and the mob-harvester variant. NeoForge 26.1
 * validates the BlockEntityType against the placed block in BlockEntity.&lt;init&gt;, so we must
 * dispatch to the right BE class based on which of the two registered blocks this state belongs
 * to — otherwise placing a mob_harvester throws "Invalid block entity ... state" the same way
 * the old gearbox BE registration did.
 */
public class BlockSmokeDetector extends BlockBasicMachine {
    public BlockSmokeDetector(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.is(RotaryBlocks.MOB_HARVESTER.get())) {
            return new BlockEntityMobHarvester(pos, state);
        }
        return new BlockEntitySmokeDetector(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) return null;
        if (pState.is(RotaryBlocks.MOB_HARVESTER.get())) {
            return (lvl, pos, st, be) -> ((BlockEntityMobHarvester) be).updateEntity(lvl, pos);
        }
        return (lvl, pos, st, be) -> ((BlockEntitySmokeDetector) be).updateEntity(lvl, pos);
    }


    @Override
    protected boolean isCustomRendered() { return true; }
}
