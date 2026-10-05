package reika.rotarycraft.items.tools;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.base.ItemRotaryTool;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.RotaryRecipeTypes;

import java.util.List;
import java.util.function.Consumer;

public class ItemCraftPattern  extends ItemRotaryTool {// implements SpriteRenderCallback {

    public ItemCraftPattern(Properties properties) {
        super(properties);
    }

    //right click to open programming gui

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (player.isCrouching()) {
            // V33a: sneak-use wipes the programmed recipe off the held pattern.
            held.remove(DataComponents.CUSTOM_DATA);
        }
        else if (!level.isClientSide()) {
            player.openMenu(new net.minecraft.world.SimpleMenuProvider(
                    (id, inv, ep) -> new reika.rotarycraft.gui.container.ContainerCraftingPattern(id, inv, ep),
                    held.getHoverName()));
        }
        return InteractionResult.SUCCESS;
    }

    // 1.21.5: Item.appendHoverText now has 5 args including TooltipDisplay and Consumer<Component>.
    @Override
    public void appendHoverText(ItemStack is, Item.TooltipContext ctx, TooltipDisplay display, Consumer<Component> li, TooltipFlag flag) {
        if (!is.has(DataComponents.CUSTOM_DATA)) {
            li.accept(Component.literal("No Crafting Pattern."));
        }
        else {
            ItemStack item = getResult(is, ctx.registries());
            if (!item.isEmpty()) {
                li.accept(Component.literal("Crafts "+item.getCount()+" ").append(item.getHoverName()));
            }
            else {
                li.accept(Component.literal("Items, No Output."));
            }
        }
        li.accept(Component.literal("Recipe Mode: "+getMode(is).displayName));
    }

    private static CompoundTag tag(ItemStack is) {
        return is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    /** The registries of the running server, for decoding outside a level context; null on a bare client. */
    @javax.annotation.Nullable
    private static HolderLookup.Provider currentRegistries() {
        net.minecraft.server.MinecraftServer server = net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer();
        return server != null ? server.registryAccess() : null;
    }

    private static ItemStack decode(CompoundTag tag, @javax.annotation.Nullable HolderLookup.Provider provider) {
        var ops = provider != null ? provider.createSerializationContext(NbtOps.INSTANCE) : NbtOps.INSTANCE;
        return ItemStack.CODEC.parse(ops, tag).result().orElse(ItemStack.EMPTY);
    }

    /** V33a getRecipeOutput: the programmed recipe's output, or EMPTY if none. */
    public static ItemStack getResult(ItemStack is) {
        return getResult(is, currentRegistries());
    }

    public static ItemStack getResult(ItemStack is, @javax.annotation.Nullable HolderLookup.Provider provider) {
        CompoundTag nbt = tag(is);
        if (!nbt.contains("output"))
            return ItemStack.EMPTY;
        return decode(nbt.getCompoundOrEmpty("output"), provider).copy();
    }

    public static ItemStack[] getItems(ItemStack is) {
        return getItems(is, currentRegistries());
    }

    /**
     * V33a: the nine grid inputs (null where empty). If a stored input no longer decodes (its item was removed), the
     * pattern is cleared and null is returned, as V33a did.
     */
    public static ItemStack[] getItems(ItemStack is, @javax.annotation.Nullable HolderLookup.Provider provider) {
        ItemStack[] items = new ItemStack[9];
        CompoundTag nbt = tag(is);
        if (nbt.contains("recipe")) {
            CompoundTag recipe = nbt.getCompoundOrEmpty("recipe");
            for (int i = 0; i < 9; i++) {
                String s = "slot"+i;
                if (recipe.contains(s)) {
                    CompoundTag t = recipe.getCompoundOrEmpty(s);
                    ItemStack in = decode(t, provider);
                    if (in.isEmpty() && !t.isEmpty()) { //item no longer exists, clear the pattern
                        is.remove(DataComponents.CUSTOM_DATA);
                        return null;
                    }
                    items[i] = in.isEmpty() ? null : in;
                }
            }
        }
        return items;
    }

    public static int getStackInputLimit(ItemStack is) {
        int amt = tag(is).getIntOr("stacklimit", 0);
        return amt > 0 ? amt : 64;
    }

    private static void resetNBT(ItemStack is) {
        if (is.has(DataComponents.CUSTOM_DATA)) {
            ReikaItemHelper.updateStackTag(is, __T__ -> __T__.remove("output"));
            ReikaItemHelper.updateStackTag(is, __T__ -> __T__.remove("recipe"));
        }
    }

    public static void setRecipe(ItemStack is, CraftingContainer ic, Level world) {
        if (world.isClientSide())
            ;//return;
        RecipeMode mode = getMode(is);
        resetNBT(is);
        setMode(is, mode);
        ItemStack out = mode.getRecipe(ic, world);
        boolean valid = out != null && !out.isEmpty();
        CompoundTag recipe = new CompoundTag();
        // 1.21.5: ItemStack.save(CompoundTag) was removed; serialisation now goes through
        // ItemStack.save(HolderLookup.Provider, Tag). Persist via ItemStack.CODEC encode
        // until we plumb a provider end-to-end.
        HolderLookup.Provider provider = world.registryAccess();
        for (int i = 0; i < 9; i++) {
            ItemStack in = ic.getItem(i);
            if (in != null && !in.isEmpty()) {
                Tag encoded = ItemStack.CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), in).result().orElse(null);
                if (encoded instanceof CompoundTag ct)
                    recipe.put("slot"+i, ct);
            }
        }
        ReikaItemHelper.updateStackTag(is, __T__ -> __T__.put("recipe", recipe));
        ReikaItemHelper.updateStackTag(is, __T__ -> __T__.putBoolean("valid", valid));
        if (valid) {
            Tag encoded = ItemStack.CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), out).result().orElse(null);
            if (encoded instanceof CompoundTag outt)
                ReikaItemHelper.updateStackTag(is, __T__ -> __T__.put("output", outt));
        }
    }

/*    @Override
    public boolean onRender(RenderItem ri, ItemStack is, ItemRenderType type) {
        if (type == ItemRenderType.INVENTORY && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            ItemStack out = this.getResult(is);
            if (out != null) {
                double s = 0.0625;
                GL11.glScaled(s, -s, s);
                ReikaGuiAPI.instance.drawItemStack(ri, out, 0, -16);
                return true;
            }
        }
        return false;
    }*/

    public static RecipeMode getMode(ItemStack is) {
        return RecipeMode.list[Mth.clamp(tag(is).getIntOr("mode", 0), 0, RecipeMode.list.length-1)];
    }

    public static void setMode(ItemStack is, RecipeMode md) {
        if (!is.is(RotaryItems.CRAFT_PATTERN.get()))
            return;
        ReikaItemHelper.updateStackTag(is, __T__ -> __T__.putInt("mode", md.ordinal()));
    }

    public static void changeStackLimit(ItemStack is, int change) {
        if (!is.is(RotaryItems.CRAFT_PATTERN.get()))
            return;
        int limit = getStackInputLimit(is);
        ReikaItemHelper.updateStackTag(is, __T__ -> __T__.putInt("stacklimit", Mth.clamp(limit+change, 1, 64)));
    }

    public enum RecipeMode {
        CRAFTING("Crafting Recipe", new ItemStack(Blocks.CRAFTING_TABLE)),
        WORKTABLE("Worktable Recipe", MachineRegistry.WORKTABLE.getBlockState().getBlock().asItem().getDefaultInstance()),
        BLASTFURN("Blast Furnace Crafting", MachineRegistry.BLASTFURNACE.getBlockState().getBlock().asItem().getDefaultInstance());

        private final ItemStack item;
        public final String displayName;

        public static final RecipeMode[] list = values();

        RecipeMode(String s, ItemStack is) {
            item = is;
            displayName = s;
        }

        public ItemStack getIcon() {
            return item.copy();
        }

        public RecipeMode next() {
            return this.ordinal() == list.length-1 ? list[0] : list[this.ordinal()+1];
        }

        public ItemStack getRecipe(CraftingContainer ic, Level world) {
            // 1.21.5: Level.getRecipeManager() removed; recipe lookup now via the server's
            // recipe manager (when available). Returns null on the client until we wire
            // ItemCraftPattern through a server-side handler.
            if (world.isClientSide() || world.getServer() == null) return null;
            RecipeManager rm = world.getServer().getRecipeManager();
            CraftingInput input = ic.asCraftInput();
            switch (this) {
                case CRAFTING -> {
                    return rm.getRecipeFor(RecipeType.CRAFTING, input, world)
                            .map(holder -> holder.value().assemble(input))
                            .orElse(null);
                }
                case BLASTFURN -> {
                    // BlastFurnaceRecipe uses our own RecipeInput; skip until that path is rewired.
                    return null;
                }
                /*case WORKTABLE -> {
//                    WorktableRecipes.WorktableRecipe wr = WorktableRecipes.getInstance().findMatchingRecipe(ic, null);
                    Collection<Recipe<?>> li = world.getRecipeManager().getRecipes().stream()
                            .filter(r -> r.getType() == RotaryRecipeTypes.WORKTABLE.getRecipeType())
                            .toList();

                    return wr != null ? wr.getOutput() : null;
                }*/
            }
            return null;
        }
    }

    public static boolean checkPatternForMatch(Container te, RecipeMode type, int invslot, int patternslot, ItemStack is, ItemStack p) {
        ItemStack in = te.getItem(invslot);
        return getMode(p) == type && checkItemAndSize(patternslot, is, p, in != null ? in.getCount() : 0);
    }

    private static boolean checkItemAndSize(int slot, ItemStack is, ItemStack p, int current) {
        return ReikaItemHelper.matchStacks(is, getItems(p)[slot]) && current+is.getCount() <= Math.min(is.getMaxStackSize(), getStackInputLimit(p));
    }
}
