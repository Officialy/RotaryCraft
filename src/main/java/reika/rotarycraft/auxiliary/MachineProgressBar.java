package reika.rotarycraft.auxiliary;

import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.dragonapi.instantiable.ProgressBar.DurationCallback;

public class MachineProgressBar implements DurationCallback {

    public final float fraction;
    public final DiscreteFunction tile;

    public MachineProgressBar(DiscreteFunction te) {
        this(te, 1);
    }

    public MachineProgressBar(DiscreteFunction te, float f) {
        tile = te;
        fraction = f;
    }

    @Override
    public int getDuration() {
        return (int) (tile.getOperationTime() * fraction);
    }

}
