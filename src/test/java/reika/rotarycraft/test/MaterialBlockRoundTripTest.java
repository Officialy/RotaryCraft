package reika.rotarycraft.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.junit.jupiter.api.Test;

import reika.rotarycraft.registry.Flywheels;
import reika.rotarycraft.registry.RotaryBlocks;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Material enums must round-trip through their blocks.
 *
 * <p>Both the gearbox and the flywheel derive their tier from the block's registry path, because
 * metadata blocks are gone and each tier is now its own block. That derivation is silent when it
 * breaks, and it broke twice in one week:
 *
 * <ul>
 *   <li>{@code BlockEntityFlywheel.type} was initialised to {@code WOOD} and never assigned, so
 *       every flywheel in the world ran on wood's torque limit, density and tensile strength.</li>
 *   <li>{@code BlockEntityGearbox.calculateRatio} ran every tick and overwrote the block-derived
 *       ratio with 2, so every 4x/8x/16x gearbox silently behaved as a 2x.</li>
 * </ul>
 *
 * <p>The gearbox half is covered in-world by {@code gearbox_reduction_ratios}, which needs a live
 * power chain. This is the cheap static half: the enum, the block set and the naming convention that
 * links them must stay in agreement, so a renamed or forgotten block fails here in seconds rather
 * than as a wrong-tier machine in someone's world.
 */
public class MaterialBlockRoundTripTest {

    /** Path suffix convention: {@code <material>_flywheel}, with DEPLETEDU spelled out. */
    private static String flywheelPath(Flywheels f) {
        return (f == Flywheels.DEPLETEDU ? "depleted_uranium" : f.name().toLowerCase(Locale.ENGLISH))
                + "_flywheel";
    }

    @Test
    void everyFlywheelMaterialHasItsOwnBlock() {
        List<String> problems = new ArrayList<>();
        for (Flywheels f : Flywheels.list) {
            String path = flywheelPath(f);
            Identifier id = Identifier.fromNamespaceAndPath("rotarycraft", path);
            Block block = BuiltInRegistries.BLOCK.getOptional(id).orElse(null);
            if (block == null) {
                problems.add(f + " has no block at rotarycraft:" + path);
                continue;
            }
            // ...and back again: the path must imply exactly the tier we started from.
            Identifier back = BuiltInRegistries.BLOCK.getKey(block);
            if (back == null || !path.equals(back.getPath()))
                problems.add(f + " -> " + path + " -> " + back + " does not round-trip");
        }
        assertTrue(problems.isEmpty(),
                "flywheel material/block mismatch:\n  " + String.join("\n  ", problems));
    }

    /**
     * The seven flywheel blocks must be exactly the seven {@link Flywheels} tiers — no extras. The
     * port previously carried {@code hsla_flywheel} and {@code diamond_flywheel}, which are gearbox
     * materials with no flywheel tier behind them.
     */
    @Test
    void noFlywheelBlocksOutsideTheMaterialSet() {
        List<String> extra = new ArrayList<>();
        List<String> expected = new ArrayList<>();
        for (Flywheels f : Flywheels.list)
            expected.add(flywheelPath(f));

        for (Identifier id : BuiltInRegistries.BLOCK.keySet()) {
            if (!"rotarycraft".equals(id.getNamespace()))
                continue;
            String p = id.getPath();
            if (p.endsWith("_flywheel") && !expected.contains(p))
                extra.add(p);
        }
        assertTrue(extra.isEmpty(),
                "flywheel blocks with no Flywheels tier behind them: " + String.join(", ", extra));
    }

    /** Every gearbox ratio variant a material declares must exist as a registered block. */
    @Test
    void everyGearboxRatioHasItsOwnBlock() {
        String[] materials = {"wood", "stone", "hsla", "tungsten", "diamond", "bedrock"};
        int[] ratios = {2, 4, 8, 16};
        List<String> missing = new ArrayList<>();
        for (String mat : materials) {
            for (int r : ratios) {
                String path = mat + "_gearbox_" + r + "x";
                if (BuiltInRegistries.BLOCK.getOptional(
                        Identifier.fromNamespaceAndPath("rotarycraft", path)).isEmpty())
                    missing.add(path);
            }
        }
        assertTrue(missing.isEmpty(),
                "gearbox ratio blocks missing: " + String.join(", ", missing));
    }

    /** Sanity: the constants the other tests lean on are actually the blocks they claim to be. */
    @Test
    void flywheelBlockConstantsMatchTheirPaths() {
        assertPath(RotaryBlocks.WOOD_FLYWHEEL.get(), "wood_flywheel");
        assertPath(RotaryBlocks.STONE_FLYWHEEL.get(), "stone_flywheel");
        assertPath(RotaryBlocks.IRON_FLYWHEEL.get(), "iron_flywheel");
        assertPath(RotaryBlocks.GOLD_FLYWHEEL.get(), "gold_flywheel");
        assertPath(RotaryBlocks.TUNGSTEN_FLYWHEEL.get(), "tungsten_flywheel");
        assertPath(RotaryBlocks.DEPLETED_URANIUM_FLYWHEEL.get(), "depleted_uranium_flywheel");
        assertPath(RotaryBlocks.BEDROCK_FLYWHEEL.get(), "bedrock_flywheel");
    }

    private static void assertPath(Block block, String expected) {
        Identifier id = BuiltInRegistries.BLOCK.getKey(block);
        assertTrue(id != null && expected.equals(id.getPath()),
                "expected rotarycraft:" + expected + " but the constant is registered as " + id);
    }
}
