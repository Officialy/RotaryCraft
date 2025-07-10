package reika.rotarycraft.base.blocks.entity.transmission;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraftforge.common.property.Properties;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
        return pLevel.isClientSide() ? null : ((pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityAdvancedGear) pBlockEntity).updateEntity(pLevel1, pPos);
        });
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        BlockEntityAdvancedGear adv = null;//todo (BlockEntityAdvancedGear) builder.getLevel().getBlockEntity(pos);
        ItemStack is = null;//todo RotaryItems.ADVGEAR.getStackOfMetadata(adv.getBlockMetadata() / 4);
        if (adv.getGearType().storesEnergy()) {
            if (is.getTag() == null)
                is.getOrCreateTag();
            is.getTag().putLong("energy", adv.getEnergy());
            is.getTag().putBoolean("bedrock", adv.isBedrockCoil());
        }
        ret.add(is);
        return ret;
    }
}