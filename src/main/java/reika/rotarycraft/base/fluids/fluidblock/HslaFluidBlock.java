package reika.rotarycraft.base.fluids.fluidblock;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public class HslaFluidBlock extends LiquidBlock {

    public HslaFluidBlock(Supplier<? extends FlowingFluid> baseFluid) {
        super(baseFluid, BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).noCollission().strength(100.0F, 100.0F)/*.noLootTable()*/.speedFactor(0.95F));
    }

}
