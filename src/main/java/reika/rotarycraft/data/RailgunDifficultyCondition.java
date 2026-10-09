package reika.rotarycraft.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import reika.rotarycraft.registry.DifficultyEffects;

/** Selects the original crafting yield at datapack load, rather than baking the datagen host's setting. */
public record RailgunDifficultyCondition(int difficulty) implements ICondition {
    public static final MapCodec<RailgunDifficultyCondition> CODEC = Codec.intRange(1,3).fieldOf("difficulty")
            .xmap(RailgunDifficultyCondition::new,RailgunDifficultyCondition::difficulty);
    public static final DeferredRegister<MapCodec<? extends ICondition>> TYPES =
            DeferredRegister.create(NeoForgeRegistries.CONDITION_SERIALIZERS, "rotarycraft");
    static { TYPES.register("railgun_difficulty", () -> CODEC); }
    @Override public boolean test(IContext context) {
        int mode = switch (DifficultyEffects.getDifficulty()) {case EASY -> 1;case HARD -> 3;default -> 2;};
        return mode == difficulty;
    }
    @Override public MapCodec<RailgunDifficultyCondition> codec() {return CODEC;}
}
