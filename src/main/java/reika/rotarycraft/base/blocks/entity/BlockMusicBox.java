package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.decorative.BlockEntityMusicBox;
import reika.rotarycraft.blockentities.transmission.BlockEntityBevelGear;

public class BlockMusicBox extends BlockBasicMachine {

    public BlockMusicBox(Properties properties) {
        super(properties);
    }

    @Override
    public  <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : ((pLevel1, pPos, pState1, pBlockEntity) -> ((BlockEntityMusicBox) pBlockEntity).updateEntity(pLevel1, pPos));
    }

    @Override
    public  BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityMusicBox(pPos, pState);
    }

}
