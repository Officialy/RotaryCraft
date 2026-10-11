package reika.rotarycraft.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.blockentities.storage.ScaleChestContents;

/** RotaryCraft item data components. */
public final class RotaryDataComponents {

    public static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, RotaryCraft.MODID);

    /** Persistent family identity for datapack-defined custom extract stacks and ingredients. */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<net.minecraft.resources.Identifier>> EXTRACT_FAMILY =
            COMPONENTS.register("extract_family", () -> reika.dragonapi.instantiable.data.ItemKeyComponents.variant(
                    DataComponentType.<net.minecraft.resources.Identifier>builder()
                    .persistent(net.minecraft.resources.Identifier.CODEC)
                    .networkSynchronized(net.minecraft.resources.Identifier.STREAM_CODEC).build(), null));

    /**
     * Inventory of a harvested Scaleable Chest. Network-synced as well as persistent: creative
     * inventory clicks hand the client's copy of a stack back to the server, so an unsynced
     * component would be wiped from any chest item moved there.
     */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ScaleChestContents>> SCALE_CHEST_CONTENTS =
            COMPONENTS.registerComponentType("scale_chest_contents", b -> b
                    .persistent(ScaleChestContents.CODEC)
                    .networkSynchronized(ScaleChestContents.STREAM_CODEC));

    /** Original railgun metadata 0..15; persistence and tracking use the same tier identity. */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> RAILGUN_POWER =
            COMPONENTS.register("railgun_power", () -> reika.dragonapi.instantiable.data.ItemKeyComponents.variant(
                    DataComponentType.<Integer>builder()
                    .persistent(com.mojang.serialization.Codec.intRange(0, 15))
                    .networkSynchronized(net.minecraft.network.codec.ByteBufCodecs.VAR_INT).build(), 0));

    private RotaryDataComponents() {}
}
