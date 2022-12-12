package reika.rotarycraft.api.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public interface GPRReactive {

    /**
     * This will be fired every tick the GPR scans the block, not just once!
     */
    void onScanned(Level world, BlockPos pos, Block b);

}
