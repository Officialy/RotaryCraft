package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityEngineController;

public class BlockEngineController extends BlockBasicMachine {

    public BlockEngineController(Properties properties) {
        super(properties.noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityEngineController(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide())
            return null;
        return (pLevel1, pPos, pState1, pBlockEntity) ->
                ((BlockEntityEngineController) pBlockEntity).updateEntity(pLevel1, pPos);
    }

    // Legacy interaction: right-click cycles the throttle setting.
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof BlockEntityEngineController ecu) {
            if (!level.isClientSide()) {
                ecu.increment();
                player.sendOverlayMessage(Component.literal("ECU setting: " + ecu.getSettingName()));
                ecu.setChanged();
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
