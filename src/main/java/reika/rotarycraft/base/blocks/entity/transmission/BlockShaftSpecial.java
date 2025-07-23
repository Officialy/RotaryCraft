package reika.rotarycraft.base.blocks.entity.transmission;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.blockentities.transmission.BlockEntityShaft;
import reika.rotarycraft.registry.MaterialRegistry;

public class BlockShaftSpecial extends BlockShaft {

    private final BlockEntityShaft.ShaftType shaftType;

    public BlockShaftSpecial(MaterialRegistry material, Properties properties, BlockEntityShaft.ShaftType type) {
        super(material, properties);
        this.shaftType = type;
        hasVerticalPlacement = false;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        BlockEntityShaft shaft = new BlockEntityShaft(this.material, pos, state);
        shaft.setShaftMode(this.shaftType);
        return shaft;
    }
}