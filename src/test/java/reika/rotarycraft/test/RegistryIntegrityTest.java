package reika.rotarycraft.test;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.junit.jupiter.api.Test;
import reika.rotarycraft.registry.EngineType;
import reika.rotarycraft.registry.ExtractOres;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryRecipeSerializers;
import reika.rotarycraft.registry.RotaryRecipeTypes;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Headless registry/logic checks. These run with the mod loaded by the NeoForge
 * moddev JUnit harness, so DeferredHolder.get() resolves real registered objects.
 * They guard the regressions this port keeps hitting: enum ordinals saved in NBT,
 * power-receiver wiring, and recipe serializer/type registration.
 */
public class RegistryIntegrityTest {

    /**
     * EngineType ordinals are persisted in BlockEntity NBT ("type"), so reordering
     * silently corrupts placed engines. HYDRO must stay last (it was appended), and the
     * historical order ahead of it must not drift.
     */
    @Test
    void engineTypeOrdinalsAreStable() {
        EngineType[] order = {
                EngineType.DC, EngineType.WIND, EngineType.STEAM, EngineType.GAS,
                EngineType.AC, EngineType.SPORT, EngineType.MICRO, EngineType.JET, EngineType.HYDRO
        };
        for (int i = 0; i < order.length; i++)
            assertEquals(i, order[i].ordinal(), order[i] + " changed ordinal");
        assertEquals(EngineType.values().length - 1, EngineType.HYDRO.ordinal(), "HYDRO must remain the last EngineType");
    }

    @Test
    void engineTypePowerIsTorqueTimesSpeed() {
        for (EngineType e : EngineType.values())
            assertEquals((long) e.getTorque() * e.getSpeed(), e.getPower(), e + " power mismatch");
    }

    /**
     * Every extractor ore must expose a non-null, registered item at all four stages, with no
     * collisions across the whole chain (a duplicate item would make the stage lookup ambiguous).
     * ItemStack-level matching is verified in the in-world game tests, since stack construction
     * needs data-component binding that only a loaded server provides.
     */
    @Test
    void extractOresHaveDistinctStageItems() {
        Set<Item> seen = new HashSet<>();
        for (ExtractOres ore : ExtractOres.oreList) {
            assertNotNull(ore.getOreTag(), ore + " missing ore tag");
            for (int stage = 0; stage < 4; stage++) {
                Item item = ore.getStageItem(stage);
                assertNotNull(item, ore + " missing stage " + stage);
                assertTrue(seen.add(item), "duplicate stage item across chain: " + ore + " stage " + stage);
            }
        }
        // 9 ores × 4 stages
        assertEquals(ExtractOres.oreList.length * 4, seen.size());
    }

    /** Every machine enum entry must have a non-null name and block, and power receivers an entry. */
    @Test
    void machineRegistryEntriesAreWellFormed() {
        for (int i = 0; i < MachineRegistry.machineList.length; i++) {
            MachineRegistry m = MachineRegistry.machineList.get(i);
            assertNotNull(m.getName(), m + " has null name");
            assertNotNull(m.getBlockState(), m + " has null block");
            if (m.isPowerReceiver())
                assertNotNull(m.getPowerReceiverEntry(), m + " is a power receiver but has no PowerReceivers entry");
        }
    }

    /** The fermenter and extractor must be registered and resolve to their blocks. */
    @Test
    void newMachinesArePresent() {
        assertNotNull(MachineRegistry.FERMENTER.getBlockState());
        assertNotNull(MachineRegistry.EXTRACTOR.getBlockState());
        assertNotNull(MachineRegistry.HYDRO_ENGINE.getBlockState());
        assertTrue(MachineRegistry.EXTRACTOR.isPowerReceiver(), "extractor should be a power receiver");
        assertTrue(MachineRegistry.FERMENTER.isPowerReceiver(), "fermenter should be a power receiver");
    }

    @Test
    void recipeSerializersAndTypesRegistered() {
        RecipeSerializer<?> es = RotaryRecipeSerializers.EXTRACTOR.get();
        RecipeSerializer<?> fs = RotaryRecipeSerializers.FERMENTER.get();
        RecipeType<?> et = RotaryRecipeTypes.EXTRACTOR.get();
        RecipeType<?> ft = RotaryRecipeTypes.FERMENTER.get();
        assertNotNull(es);
        assertNotNull(fs);
        assertNotNull(et);
        assertNotNull(ft);
        assertNotEquals(et, ft, "extractor and fermenter recipe types must be distinct");
    }
}
