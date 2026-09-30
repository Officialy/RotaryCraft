package reika.rotarycraft.api.interfaces;

import net.minecraft.core.BlockPos;

/** Optional area-marker bridge. Bounds are inclusive; the Terraformer adopts the X/Z area on its first tick. */
public interface TerraformerAreaProvider {
    BlockPos minimum();
    BlockPos maximum();
    void removeAreaMarker();
}
