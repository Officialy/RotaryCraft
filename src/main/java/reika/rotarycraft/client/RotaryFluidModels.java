package reika.rotarycraft.client;

import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterFluidModelsEvent;
import net.neoforged.neoforge.client.fluid.FluidTintSources;
import net.neoforged.neoforge.registries.DeferredHolder;

import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.RotaryFluids;

/**
 * Binds a renderable {@link FluidModel.Unbaked} to every RotaryCraft fluid. 26.2 dropped the
 * texture/tint accessors from {@code IClientFluidTypeExtensions} — without registering one of
 * these through {@link RegisterFluidModelsEvent}, NeoForge has no block-atlas sprite for the
 * fluid at all (not "wrong texture" — no model, so the fluid never gets stitched into the atlas
 * in the first place, regardless of what path a renderer's hand-picked sprite lookup uses).
 *
 * <p>RotaryCraft has real per-fluid art under {@code textures/block/fluid/} for HSLA, jet fuel,
 * ethanol, lubricant and liquid nitrogen (nitrogen only has an animated/flow frame — reused for
 * both slots); the rest reuse vanilla water's still/flow sprites with a constant tint, the same
 * approach {@link reika.reactorcraft.client.ReactorFluidModels} uses in ReactorCraft.</p>
 */
@EventBusSubscriber(modid = RotaryCraft.MODID, value = Dist.CLIENT)
public final class RotaryFluidModels {

    private static Material mat(String path) {
        return new Material(Identifier.fromNamespaceAndPath(RotaryCraft.MODID, path));
    }

    private static final Material WATER_STILL = new Material(Identifier.withDefaultNamespace("block/water_still"));
    private static final Material WATER_FLOW = new Material(Identifier.withDefaultNamespace("block/water_flow"));

    private static final Material HSLA_STILL = mat("block/fluid/hsla_still");
    private static final Material HSLA_FLOW = mat("block/fluid/hsla_flow");
    private static final Material JETFUEL_STILL = mat("block/fluid/jetfuel");
    private static final Material JETFUEL_FLOW = mat("block/fluid/jetfuel_anim");
    private static final Material ETHANOL_STILL = mat("block/fluid/ethanol");
    private static final Material ETHANOL_FLOW = mat("block/fluid/ethanol_anim");
    private static final Material LUBRICANT_STILL = mat("block/fluid/lubricant");
    private static final Material LUBRICANT_FLOW = mat("block/fluid/lubricant_anim");
    // No dedicated still frame was authored for nitrogen — only the animated sprite exists.
    private static final Material NITROGEN_STILL = mat("block/fluid/nitrogen_anim");
    private static final Material NITROGEN_FLOW = mat("block/fluid/nitrogen_anim");

    private RotaryFluidModels() {}

    @SubscribeEvent
    public static void registerFluidModels(RegisterFluidModelsEvent event) {
        register(event, RotaryFluids.HSLA_FLUID, HSLA_STILL, HSLA_FLOW, 0xFFFFFFFF);
        register(event, RotaryFluids.HSLA_FLUID_FLOWING, HSLA_STILL, HSLA_FLOW, 0xFFFFFFFF);
        register(event, RotaryFluids.JET_FUEL, JETFUEL_STILL, JETFUEL_FLOW, 0xFFFFFFFF);
        register(event, RotaryFluids.ETHANOL, ETHANOL_STILL, ETHANOL_FLOW, 0xFFFFFFFF);
        register(event, RotaryFluids.LUBRICANT, LUBRICANT_STILL, LUBRICANT_FLOW, 0xFFFFFFFF);
        register(event, RotaryFluids.LIQUID_NITROGEN, NITROGEN_STILL, NITROGEN_FLOW, 0xFFFFFFFF);

        // No dedicated art -- vanilla water sprite, tinted to match each fluid's identity
        // (same palette PipeRenderer/RenderReservoir already use for their BER tint).
        register(event, RotaryFluids.POISON, WATER_STILL, WATER_FLOW, 0xFFA030F0);
        register(event, RotaryFluids.STEAM, WATER_STILL, WATER_FLOW, 0xC0E0E0E0);
        register(event, RotaryFluids.SODIUM, WATER_STILL, WATER_FLOW, 0xFFD0D050);
        register(event, RotaryFluids.CHLORINE, WATER_STILL, WATER_FLOW, 0xFFC0E060);
        register(event, RotaryFluids.OXYGEN, WATER_STILL, WATER_FLOW, 0xFFE0F0FF);
        register(event, RotaryFluids.LIQUID_AMMONIA, WATER_STILL, WATER_FLOW, 0xFFD0F0A0);
        register(event, RotaryFluids.AMMONIA, WATER_STILL, WATER_FLOW, 0xFFCFE8FF);
        register(event, RotaryFluids.HEAVY_WATER, WATER_STILL, WATER_FLOW, 0xFFA0C0E0);
    }

    private static void register(RegisterFluidModelsEvent event, DeferredHolder<net.minecraft.world.level.material.Fluid, ? extends net.minecraft.world.level.material.Fluid> fluid,
                                  Material still, Material flow, int tint) {
        FluidModel.Unbaked model = new FluidModel.Unbaked(still, flow, null, FluidTintSources.constant(tint));
        event.register(model, fluid.get());
    }
}
