package reika.rotarycraft.base.blocks.entity.transmission;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
// 1.21.5: net.neoforged.common.property.Properties removed; use vanilla BlockStateProperties instead.
import reika.dragonapi.libraries.registry.ReikaItemHelper;
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
        if (pLevel.isClientSide()) {
            @SuppressWarnings("unchecked")
            BlockEntityTicker<T> t = (BlockEntityTicker<T>) clientPhiTicker(BlockEntityAdvancedGear.class);
            return t;
        }
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityAdvancedGear) pBlockEntity).updateEntity(pLevel1, pPos);
        };
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        BlockEntityAdvancedGear adv = null;//todo (BlockEntityAdvancedGear) builder.getLevel().getBlockEntity(pos);
        ItemStack is = null;//todo RotaryItems.ADVGEAR.getStackOfMetadata(adv.getBlockMetadata() / 4);
        if (adv.getGearType().storesEnergy()) {
            if (is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag() == null)
                is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            ReikaItemHelper.updateStackTag(is, __T__ -> __T__.putLong("energy", adv.getEnergy()));
            ReikaItemHelper.updateStackTag(is, __T__ -> __T__.putBoolean("bedrock", adv.isBedrockCoil()));
        }
        ret.add(is);
        return ret;
    }

    @Override
    protected boolean isCustomRendered() { return true; }
}