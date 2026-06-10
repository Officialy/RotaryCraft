package reika.rotarycraft.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.renders.item.MachineItemRenderer;

/**
 * 1.21.5 client-side extension registrations for RotaryCraft.
 * <p>
 * Replaces the legacy {@code IClientItemExtensions.getCustomRenderer()} hookup that
 * bound a {@code BlockEntityWithoutLevelRenderer} to each machine item — the new
 * pipeline routes custom item rendering through {@link RegisterSpecialModelRendererEvent}
 * and per-item model JSONs that reference the registered renderer ID
 * ({@code rotarycraft:machine}).
 */
@EventBusSubscriber(modid = RotaryCraft.MODID, value = Dist.CLIENT)
public final class RotaryClientExtensions {

    private RotaryClientExtensions() {}

    @SubscribeEvent
    public static void registerSpecialModelRenderers(RegisterSpecialModelRendererEvent event) {
        // The "machine" codec is consumed by item model JSONs of the form:
        //   "model": { "type": "minecraft:special",
        //              "base":  "minecraft:item/generated",
        //              "model": { "type": "rotarycraft:machine", "machine": "<enum_name>" } }
        // where <enum_name> is the MachineRegistry value (case-insensitive).
        event.register(MachineItemRenderer.ID, MachineItemRenderer.Unbaked.MAP_CODEC);
    }
}
