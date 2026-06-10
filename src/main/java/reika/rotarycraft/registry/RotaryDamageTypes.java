package reika.rotarycraft.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import reika.rotarycraft.RotaryCraft;

public class RotaryDamageTypes {

    public static final ResourceKey<DamageType> MACHINE = ResourceKey.create(
            Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "machine"));
}
