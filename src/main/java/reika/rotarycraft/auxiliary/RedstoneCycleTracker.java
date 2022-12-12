package reika.rotarycraft.auxiliary;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public class RedstoneCycleTracker {

    private final boolean[] lastPower;
    private boolean integratedRedstone;

    private boolean alternating;

    private boolean lastSound;

    public RedstoneCycleTracker(int l) {
        lastPower = new boolean[l];
    }

    public void update(Level world, BlockPos pos) {
        alternating = (this.hasIntegrated() && !world.hasNeighborSignal(pos));// || ReikaRedstoneHelper.isGettingACRedstone(world, pos, lastPower);
        if (this.hasIntegrated() && alternating) {
            if (world.getGameTime() % 6 == 0) {
                float f = RotaryAux.isMuffled(world, pos) ? 0.05F : 0.25F;
                world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.COMPARATOR_CLICK/*"DragonAPI.rand.click"*/, SoundSource.BLOCKS, f, 0.66F, false);
            }
        }
    }

    public boolean isAlternating() {
        return alternating;
    }

    public void addIntegrated() {
        integratedRedstone = true;
    }

    public boolean hasIntegrated() {
        return integratedRedstone;
    }

    public void reset() {
        integratedRedstone = false;
    }

}
