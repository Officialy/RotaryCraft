package reika.rotarycraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.MEStorage;
import appeng.blockentity.storage.DriveBlockEntity;
import reika.rotarycraft.blockentities.BlockEntityItemFilter;
import reika.rotarycraft.blockentities.processing.BlockEntityAutoCrafter;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * GameTests for the Applied Energistics 2 integration, run against a real ME network (creative energy cell + drive
 * with a 1k cell). Only loaded and registered when AE2 is present; the normal GameTest runtime has no AE2, so run
 * these with the AE2 and GuideME jars dropped into {@code RotaryCraft/run-gametest/mods}.
 */
final class RotaryAETests {
    private RotaryAETests() {}

    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        RotaryGameTests.register(event, env, "ae2_autocrafter_uses_me_network", 240, RotaryAETests::autoCrafterUsesNetwork);
        RotaryGameTests.register(event, env, "ae2_item_filter_pulls_from_me_network", 400, RotaryAETests::itemFilterPullsFromNetwork);
    }

    private static Block ae(String id) {
        return BuiltInRegistries.BLOCK.getValue(Identifier.fromNamespaceAndPath("ae2", id));
    }

    /** Drive north of the machine (its front faces away) with a 1k cell, creative energy cell east of the drive. */
    private static void network(GameTestHelper helper, int x, int z) {
        BlockPos drivePos = RotaryPowerTests.at(x, z - 1);
        helper.setBlock(drivePos, ae("drive"));
        helper.setBlock(RotaryPowerTests.at(x + 1, z - 1), ae("creative_energy_cell"));
        DriveBlockEntity drive = helper.getBlockEntity(drivePos, DriveBlockEntity.class);
        drive.getInternalInventory().setItemDirect(0, new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath("ae2", "item_storage_cell_1k"))));
    }

    /** The drive's cell storage; only exists once the drive has readied (a few ticks after placement). */
    private static MEStorage cell(GameTestHelper helper, int x, int z) {
        MEStorage c = helper.getBlockEntity(RotaryPowerTests.at(x, z - 1), DriveBlockEntity.class).getCellInventory(0);
        helper.assertTrue(c != null, "the drive must expose its cell");
        return c;
    }

    /** V33a AutoCrafter with AE: ingredients come out of the ME network and the output goes back into it. */
    static void autoCrafterUsesNetwork(GameTestHelper helper) {
        int z = 4;
        RotaryPowerTests.coil(helper, 2, z, 128, 1 << 20);
        RotaryPowerTests.place(helper, 3, z, RotaryBlocks.CRAFTER.get(), Direction.EAST);
        network(helper, 3, z);
        var crafter = helper.getBlockEntity(RotaryPowerTests.at(3, z), BlockEntityAutoCrafter.class);
        crafter.itemHandler.setStackInSlot(0, RotaryProcessingTests.programmedPattern(helper,
                null, Items.OAK_PLANKS, null, null, Items.OAK_PLANKS, null, null, null, null));
        helper.runAfterDelay(10, () -> cell(helper, 3, z).insert(AEItemKey.of(Items.OAK_PLANKS), 3, Actionable.MODULATE, IActionSource.empty()));
        helper.runAfterDelay(120, () -> { //grid boot + the 50-tick reader refresh
            crafter.triggerCraftingCycle(0);
            helper.runAfterDelay(5, () -> {
                MEStorage c = cell(helper, 3, z);
                long planks = c.getAvailableStacks().get(AEItemKey.of(Items.OAK_PLANKS));
                long sticks = c.getAvailableStacks().get(AEItemKey.of(Items.STICK));
                helper.assertTrue(planks == 1, "two planks must come out of the ME network, left " + planks);
                helper.assertTrue(sticks == 4, "the four sticks must be injected into the ME network, found " + sticks
                        + " (output slot " + crafter.itemHandler.getStackInSlot(BlockEntityAutoCrafter.SIZE) + ")");
                helper.succeed();
            });
        });
    }

    /** V33a Item Filter with AE: it pulls template matches out of the ME network into its filtered slot. */
    static void itemFilterPullsFromNetwork(GameTestHelper helper) {
        int z = 4;
        RotaryPowerTests.coil(helper, 2, z, 128, 1 << 20);
        RotaryPowerTests.place(helper, 3, z, RotaryBlocks.ITEMFILTER.get(), Direction.EAST);
        network(helper, 3, z);
        helper.runAfterDelay(10, () -> {
            cell(helper, 3, z).insert(AEItemKey.of(Items.IRON_INGOT), 10, Actionable.MODULATE, IActionSource.empty());
            cell(helper, 3, z).insert(AEItemKey.of(Items.GOLD_INGOT), 10, Actionable.MODULATE, IActionSource.empty());
        });
        var filter = helper.getBlockEntity(RotaryPowerTests.at(3, z), BlockEntityItemFilter.class);
        filter.itemHandler.setStackInSlot(0, new ItemStack(Items.IRON_INGOT));
        helper.runAfterDelay(300, () -> { //grid boot + the 200-tick ME scan
            ItemStack got = filter.itemHandler.getStackInSlot(1);
            helper.assertTrue(got.is(Items.IRON_INGOT) && got.getCount() == 10, "the filter must pull the ten iron out of the network, has " + got);
            MEStorage c = cell(helper, 3, z);
            helper.assertTrue(c.getAvailableStacks().get(AEItemKey.of(Items.GOLD_INGOT)) == 10, "non-matching gold must stay in the network");
            helper.succeed();
        });
    }
}
