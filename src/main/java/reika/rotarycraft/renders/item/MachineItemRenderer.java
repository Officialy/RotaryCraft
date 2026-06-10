package reika.rotarycraft.renders.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.resources.Identifier;
import org.joml.Vector3fc;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.RotaryModelBase;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.function.Consumer;

/**
 * 1.21.5: Special-model renderer for machine items.
 * <p>
 * Replaces the old {@code BlockEntityWithoutLevelRenderer} hookup
 * ({@code IClientItemExtensions.getCustomRenderer()}, gone in NeoForge 26.x). Each machine
 * item's model JSON should declare:
 * <pre>{@code
 * "model": {
 *   "type": "minecraft:special",
 *   "base": "minecraft:item/generated",
 *   "model": { "type": "rotarycraft:machine", "machine": "wood_flywheel" }
 * }
 * }</pre>
 * The {@code machine} field is the {@link MachineRegistry} enum name in lowercase. Baking
 * resolves the corresponding {@link RotaryModelBase} via {@link MachineRegistry#getModel()}.
 * Registration of the codec lives in {@link reika.rotarycraft.client.RotaryClientExtensions}.
 */
public class MachineItemRenderer implements NoDataSpecialModelRenderer {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "machine");

    private final RotaryModelBase model;
    private final Identifier texture;

    public MachineItemRenderer(RotaryModelBase model, Identifier texture) {
        this.model = model;
        this.texture = texture;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        poseStack.pushPose();
        // Center + flip to match the legacy item-render orientation (was ItemTransforms.TransformType.GUI etc).
        // 2x scale: the legacy BE model was authored at full block size in world coordinates, but
        // vanilla's item-display transform fits it into roughly a half-block, so machine items
        // end up looking tiny in the inventory / hand. Doubling the scale lifts them back into
        // the visual size of a normal block item.
        // 26.1: user reported all machine items still sat too low in the hotbar / in-hand —
        // the bottom of every model was visibly clipped against the frame. Bumped from 1.3 to
        // 1.5 so the model centre sits roughly half a block higher, matching the visual size
        // of a normal block item in the slot.
        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YN.rotationDegrees(90F));
        // Submit the model using the renderType derived from the model's texture.
        collector.submitModel(model, net.minecraft.util.Unit.INSTANCE, poseStack, texture, lightCoords, overlayCoords, outlineColor, null);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        if (model != null) {
            PoseStack ps = new PoseStack();
            model.root().getExtentsForGui(ps, output);
        }
    }

    /** Codec-backed unbaked form. Resolved at bake() time against the live {@link MachineRegistry}. */
    public record Unbaked(String machine) implements NoDataSpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                com.mojang.serialization.Codec.STRING.fieldOf("machine").forGetter(Unbaked::machine)
        ).apply(i, Unbaked::new));

        @Override
        public MapCodec<? extends NoDataSpecialModelRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public MachineItemRenderer bake(SpecialModelRenderer.BakingContext context) {
            MachineRegistry m;
            try {
                m = MachineRegistry.valueOf(machine.toUpperCase(java.util.Locale.ROOT));
            } catch (IllegalArgumentException e) {
                RotaryCraft.LOGGER.warn("Unknown machine '{}' in special model renderer", machine);
                return null;
            }
            if (!m.hasModel() || m.getModel() == null) {
                RotaryCraft.LOGGER.warn("MachineRegistry.{} has no model registered; skipping special renderer", m);
                return null;
            }
            RotaryModelBase modelInstance = m.getModel().apply(context.entityModelSet());
            return new MachineItemRenderer(modelInstance, resolveTextureForMachine(m, modelInstance));
        }

        /**
         * Picks the right texture for the item-in-hand rendering of {@code m}.
         *
         * <p>Most machines have a single fixed texture that ships with their model — those use
         * {@code modelInstance.getTexture()} directly, which the model class hard-codes to its
         * sole asset. Shafts are the exception: every shaft tier (WOOD, STONE, STEEL/HSLA,
         * TUNGSTEN, DIAMOND, BEDROCK) shares the same {@link reika.rotarycraft.models.animated.shaftonly.ShaftModel}
         * but renders against a tier-specific PNG inside
         * {@code textures/blockentitytex/transmission/shaft/}. The in-world BER picks the right
         * one via {@link reika.rotarycraft.renders.RenderShaft#getImageFileName} reading the
         * BE's {@code shaftType}; for the in-hand path there's no BE, so we derive the tier
         * from the {@link MachineRegistry} enum name and compose the texture path the same way
         * {@link reika.rotarycraft.registry.MaterialRegistry#getBaseShaftTexture} does.
         *
         * <p>Before this fix, every shaft item used {@code ShaftModel.getTexture()} which
         * returned the unsuffixed {@code shafttex.png} — the HSLA-tier texture — so a bedrock
         * shaft in hand showed up wearing the HSLA texture.
         */
        private static net.minecraft.resources.Identifier resolveTextureForMachine(MachineRegistry m, RotaryModelBase modelInstance) {
            String tierSuffix = shaftTierSuffix(m);
            if (tierSuffix != null) {
                return net.minecraft.resources.Identifier.fromNamespaceAndPath(
                        RotaryCraft.MODID,
                        "textures/blockentitytex/transmission/shaft/shafttex" + tierSuffix + ".png");
            }
            return modelInstance.getTexture();
        }

        /**
         * Maps the {@link MachineRegistry} enum to the texture suffix used by
         * {@link reika.rotarycraft.registry.MaterialRegistry#getBaseShaftTexture}.
         * Returns {@code null} for non-shaft machines (let the model's default texture stand).
         * The suffix for STEEL (HSLA) is the empty string — that's the unsuffixed default.
         */
        private static String shaftTierSuffix(MachineRegistry m) {
            return switch (m) {
                case WOOD_SHAFT     -> "w";
                case STONE_SHAFT    -> "s";
                case HSLA_SHAFT     -> "";   // default unsuffixed texture is HSLA/steel
                case TUNGSTEN_SHAFT -> "t";
                case DIAMOND_SHAFT  -> "d";
                case BEDROCK_SHAFT  -> "b";
                default             -> null;
            };
        }
    }
}
