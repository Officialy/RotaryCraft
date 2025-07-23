package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import reika.rotarycraft.blockentities.piping.BlockEntityPipe;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.List;

public class BlockPiping extends Block implements EntityBlock {

    public BlockPiping(Properties properties) {
        super(properties.strength(Mth.clamp(ConfigRegistry.PIPEHARDNESS.getValue(), 0, 1)));
//        this.setResistance(1F);
//        this.setLightLevel(0F);
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return MachineRegistry.getMachine((Level) world, pos) == MachineRegistry.HOSE ? 45 : 0;
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        return null;
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter world, BlockPos pos, Player player) {
        return true;
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityPipe(pos, state);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : ((pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityPipe) pBlockEntity).updateEntity(pLevel1, pPos);
        });
    }
}
