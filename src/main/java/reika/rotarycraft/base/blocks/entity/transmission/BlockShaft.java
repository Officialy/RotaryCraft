package reika.rotarycraft.base.blocks.entity.transmission;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.client.extensions.common.IClientBlockExtensions;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityShaft;
import reika.rotarycraft.registry.MaterialRegistry;

import java.util.function.Consumer;

public class BlockShaft extends BlockBasicMachine {

    public final MaterialRegistry material;

    public BlockShaft(MaterialRegistry mat, Properties properties) {
        super(properties.noOcclusion());
        material = mat;
        hasVerticalPlacement = true;
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityShaft(material, pPos, pState);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : ((pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityShaft) pBlockEntity).updateEntity(pLevel1, pPos);
        });
    }

}
