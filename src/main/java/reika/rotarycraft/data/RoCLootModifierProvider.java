package reika.rotarycraft.data;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

public final class RoCLootModifierProvider extends GlobalLootModifierProvider {
    public RoCLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, "rotarycraft");
    }

    @Override
    protected void start() {
        for (RoCChestLoot.Location location : RoCChestLoot.Location.values())
            add("chest_loot_" + location.name().toLowerCase(java.util.Locale.ROOT), new AddTableLootModifier(
                    Optional.of(Holder.direct(LootTableIdCondition.builder(location.target.identifier()).build())),
                    0, location.subtable));
    }
}
