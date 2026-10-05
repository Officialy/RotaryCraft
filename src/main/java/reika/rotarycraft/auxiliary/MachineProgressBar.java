package reika.rotarycraft.auxiliary;

import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.dragonapi.instantiable.ProgressBar.DurationCallback;

public record MachineProgressBar(float fraction, DiscreteFunction tile) implements DurationCallback {

    public MachineProgressBar(DiscreteFunction te) {
        this(te, 1);
    }

    public MachineProgressBar(DiscreteFunction te, float f) {
        this(f, te);
    }

    @Override
    public int getDuration() {
        return (int) (tile.getOperationTime() * fraction);
    }

}
