package reika.rotarycraft.auxiliary;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;


@Deprecated
public class SolarSkyCache {

    //public final int xPosition;
    //public final int zPosition;

    //private boolean needsCalculation;
	/*
	public SolarSkyCache(int x, int z) {
		xPosition = x;
		zPosition = z;
	}*/

    private final HashMap<BlockPos, Integer> data = new HashMap();

    public void markDirty(int x, int z) {
        //needsCalculation = true;
        data.remove(new BlockPos(x, 0, z));
    }

    public boolean canSeeTheSky(Level world, BlockPos pos) {
        BlockPos key = new BlockPos(pos);//.to2D();
        Integer cache = data.get(key);
        if (cache != null) {
            return pos.getY() >= cache.intValue();
        }
        int ref = this.calculateSkyAccess(world, pos);
        data.put(key, ref);
        return pos.getY() >= ref;
    }

    private int calculateSkyAccess(Level world, BlockPos pos) {
        int dy = 256;
        while (dy > 0 && this.isTransparent(world, new BlockPos(pos.getX(), dy - 1, pos.getZ()))) {
            dy--;
        }
        return dy;
    }

    private boolean isTransparent(Level world, BlockPos pos) {
		/*
		Block b = world.getBlock(pos);
		if (b == Blocks.AIR || b.isAir(world, pos))
			return true;
		if (MachineRegistry.getMachine(world, pos) == MachineRegistry.MIRROR)
			;//return false;
		 */
        return false;
    }

}
