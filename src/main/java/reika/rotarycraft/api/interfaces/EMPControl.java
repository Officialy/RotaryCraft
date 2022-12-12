package reika.rotarycraft.api.interfaces;

import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Implement this on a BlockEntity to give it custom behavior when hit with a RotaryCraft EMP.
 */
public interface EMPControl {

    /**
     * Called during the actual firing of the EMP. The tile passed in is the EMP itself.
     */
    void onHitWithEMP(BlockEntity te);

}
