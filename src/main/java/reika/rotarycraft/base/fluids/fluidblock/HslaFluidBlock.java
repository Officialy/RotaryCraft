package reika.rotarycraft.base.fluids.fluidblock;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import reika.rotarycraft.registry.RotaryBlocks;

import java.util.function.Supplier;

public class HslaFluidBlock extends LiquidBlock {
    public HslaFluidBlock(Supplier<? extends FlowingFluid> baseFluid) {
        super(baseFluid.get(), RotaryBlocks.blockProperties().mapColor(MapColor.FIRE).noCollision().strength(100.0F, 100.0F).speedFactor(0.95F));
    }

}
