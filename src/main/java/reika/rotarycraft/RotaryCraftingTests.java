package reika.rotarycraft;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import reika.rotarycraft.auxiliary.recipemanagers.BulkShapedRecipe;
import reika.rotarycraft.registry.RotaryItems;

/** Original crafting yields, real vanilla result-slot actions and synchronized recipe data. */
final class RotaryCraftingTests {
    private RotaryCraftingTests() {}

    private static ServerPlayer connectedPlayer(GameTestHelper helper) {
        var player = (ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        // Vanilla crafting updates send result-slot and recipe-award packets even in headless tests.
        var connection = new Connection(PacketFlow.SERVERBOUND);
        new EmbeddedChannel(connection);
        new ServerGamePacketListenerImpl(helper.getLevel().getServer(), connection, player,
                CommonListenerCookie.createInitial(player.getGameProfile(), false));
        return player;
    }

    private static CraftingMenu menu(GameTestHelper helper, Player player, boolean disc, int batches) {
        BlockPos table = new BlockPos(4, 1, 4);
        helper.setBlock(table, Blocks.CRAFTING_TABLE);
        var menu = new CraftingMenu(1, player.getInventory(), ContainerLevelAccess.create(helper.getLevel(), helper.absolutePos(table)));
        Item steel = RotaryItems.HSLA_STEEL_INGOT.get();
        Item[] ingredients = disc
                ? new Item[] {Items.WOOL.black(), Items.REDSTONE, Items.WOOL.black(), Items.REDSTONE, steel,
                    Items.REDSTONE, Items.WOOL.black(), Items.REDSTONE, Items.WOOL.black()}
                : new Item[] {Items.AIR, steel, Items.AIR, Items.AIR, RotaryItems.HSLA_PLATE.get(), Items.AIR, Items.AIR, steel, Items.AIR};
        for (int i = 0; i < ingredients.length; i++)
            if (ingredients[i] != Items.AIR) menu.getSlot(i + 1).set(new ItemStack(ingredients[i], batches));
        helper.assertTrue(menu.getResultSlot().getItem().getCount() == 4, "one original ingredient layout must produce four items");
        return menu;
    }

    private static int inventoryCount(GameTestHelper helper, Player player, Item result) {
        int count = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            helper.assertTrue(stack.isEmpty() || stack.getCount() <= stack.getMaxStackSize(), "inventory must contain no oversized stacks");
            if (stack.is(result)) {
                if (result == RotaryItems.DISK.get() || result == RotaryItems.CRAFT_PATTERN.get())
                    helper.assertTrue(stack.getMaxStackSize() == 1, "crafted items must retain individual storage limits");
                count += stack.getCount();
            }
        }
        return count;
    }

    static void discShiftCraft(GameTestHelper helper) { shiftCraft(helper, true); }
    static void patternShiftCraft(GameTestHelper helper) { shiftCraft(helper, false); }

    private static void shiftCraft(GameTestHelper helper, boolean disc) {
        var player = connectedPlayer(helper);
        var menu = menu(helper, player, disc, 2);
        menu.clicked(0, 0, ContainerInput.QUICK_MOVE, player);
        Item result = disc ? RotaryItems.DISK.get() : RotaryItems.CRAFT_PATTERN.get();
        helper.assertTrue(inventoryCount(helper, player, result) == 8, "two batches must produce eight individually stored items");
        for (var slot : menu.getInputGridSlots()) helper.assertTrue(slot.getItem().isEmpty(), "ingredients must be consumed exactly twice");
        helper.assertTrue(menu.getResultSlot().getItem().isEmpty(), "empty grid must have no residual result");
        helper.succeed();
    }

    static void discNormalPickup(GameTestHelper helper) {
        var player = connectedPlayer(helper);
        var menu = menu(helper, player, true, 1);
        menu.clicked(0, 0, ContainerInput.PICKUP, player);
        helper.assertTrue(menu.getCarried().getCount() == 4, "ordinary result pickup must retain all four discs");
        for (int slot = 37; slot < 41; slot++) menu.clicked(slot, 0, ContainerInput.PICKUP, player);
        helper.assertTrue(menu.getCarried().isEmpty() && inventoryCount(helper, player, RotaryItems.DISK.get()) == 4,
                "placing the carried batch must split into four inventory slots");
        for (var slot : menu.getInputGridSlots()) helper.assertTrue(slot.getItem().isEmpty(), "one batch must consume each ingredient once");
        helper.succeed();
    }

    static void discFullInventory(GameTestHelper helper) {
        var player = connectedPlayer(helper);
        var menu = menu(helper, player, true, 1);
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++)
            player.getInventory().setItem(slot, new ItemStack(Items.DIAMOND, 64));
        menu.clicked(0, 0, ContainerInput.QUICK_MOVE, player);
        helper.assertTrue(menu.getResultSlot().getItem().getCount() == 4, "a full inventory must preserve the pending result");
        for (var slot : menu.getInputGridSlots())
            if (!slot.getItem().isEmpty()) helper.assertTrue(slot.getItem().getCount() == 1, "failed pickup must preserve ingredients");
        player.getInventory().setItem(0, ItemStack.EMPTY);
        player.getInventory().setItem(1, ItemStack.EMPTY);
        player.getInventory().setItem(2, ItemStack.EMPTY);
        player.getInventory().setItem(3, ItemStack.EMPTY);
        menu.clicked(0, 0, ContainerInput.QUICK_MOVE, player);
        helper.assertTrue(inventoryCount(helper, player, RotaryItems.DISK.get()) == 4, "restoring four slots must permit exactly one full batch");
        helper.succeed();
    }

    static void discPartialInventory(GameTestHelper helper) {
        var player = connectedPlayer(helper);
        var menu = menu(helper, player, true, 1);
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++)
            player.getInventory().setItem(slot, new ItemStack(Items.DIAMOND, 64));
        for (int slot = 0; slot < 3; slot++) player.getInventory().setItem(slot, ItemStack.EMPTY);
        menu.clicked(0, 0, ContainerInput.QUICK_MOVE, player);
        helper.assertTrue(menu.getResultSlot().getItem().getCount() == 4 && inventoryCount(helper, player, RotaryItems.DISK.get()) == 0,
                "three free slots must preserve the entire four-disc batch");
        for (var slot : menu.getInputGridSlots()) helper.assertTrue(slot.getItem().getCount() == 1, "partial capacity must preserve all ingredients");
        player.getInventory().setItem(3, ItemStack.EMPTY);
        menu.clicked(0, 0, ContainerInput.QUICK_MOVE, player);
        helper.assertTrue(inventoryCount(helper, player, RotaryItems.DISK.get()) == 4, "four free slots must accept the whole batch");
        helper.succeed();
    }

    static void vanillaShiftCraft(GameTestHelper helper) {
        var player = connectedPlayer(helper);
        BlockPos table = new BlockPos(4, 1, 4);
        helper.setBlock(table, Blocks.CRAFTING_TABLE);
        var menu = new CraftingMenu(1, player.getInventory(), ContainerLevelAccess.create(helper.getLevel(), helper.absolutePos(table)));
        menu.getSlot(1).set(new ItemStack(Items.OAK_LOG, 2));
        menu.clicked(0, 0, ContainerInput.QUICK_MOVE, player);
        helper.assertTrue(inventoryCount(helper, player, Items.OAK_PLANKS) == 8, "ordinary vanilla crafting must retain normal stacked transfer");
        helper.assertTrue(menu.getInputGridSlots().stream().allMatch(slot -> slot.getItem().isEmpty()), "vanilla crafting must consume both logs");
        helper.succeed();
    }

    static void bulkRecipeNetwork(GameTestHelper helper) {
        for (boolean disc : new boolean[] {true, false}) {
            var player = connectedPlayer(helper);
            var menu = menu(helper, player, disc, 1);
            CraftingInput input = CraftingInput.of(3, 3, menu.getInputGridSlots().stream().map(slot -> slot.getItem().copy()).toList());
            var recipe = helper.getLevel().getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel()).orElseThrow().value();
            helper.assertTrue(recipe instanceof BulkShapedRecipe, "bulk outputs must load through their registered datapack serializer");
            var buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), helper.getLevel().registryAccess());
            try {
                BulkShapedRecipe.STREAM_CODEC.encode(buffer, (BulkShapedRecipe) recipe);
                var decoded = BulkShapedRecipe.STREAM_CODEC.decode(buffer);
                ItemStack result = decoded.assemble(input);
                helper.assertTrue(decoded.matches(input, helper.getLevel()) && result.getCount() == 4 && result.getMaxStackSize() == 1,
                        "recipe synchronization must retain matching, yield and the real stack limit");
                var display = (ShapedCraftingRecipeDisplay) decoded.display().getFirst();
                ItemStack icon = ((SlotDisplay.ItemStackSlotDisplay) display.result()).stack().create();
                helper.assertTrue(!icon.isEmpty() && icon.getCount() == 4 && ItemStack.validateStrict(icon).isSuccess(),
                        "recipe-book display must show four without an invalid template");
                ItemStack.STREAM_CODEC.encode(buffer, result);
                ItemStack networkResult = ItemStack.STREAM_CODEC.decode(buffer);
                helper.assertTrue(networkResult.getCount() == 4 && networkResult.getMaxStackSize() == 1 && !buffer.isReadable(),
                        "result-slot synchronization must preserve the transient four-item batch");
            } finally {
                buffer.release();
            }
        }
        helper.succeed();
    }
}
