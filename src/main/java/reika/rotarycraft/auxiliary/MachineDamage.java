package reika.rotarycraft.auxiliary;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Level;
import reika.dragonapi.instantiable.CustomStringDamageSource;
import reika.rotarycraft.registry.RotaryDamageTypes;

public class MachineDamage {

    private final String message;

    public MachineDamage(String msg) {
        message = msg;
    }

    public MachineDamage setArmorBlocking(float f, int mult, int... types) {
        return this;
    }

    public DamageSource get(Level level) {
        return new CustomStringDamageSource(CustomStringDamageSource.resolve(level, RotaryDamageTypes.MACHINE), message);
    }

}
