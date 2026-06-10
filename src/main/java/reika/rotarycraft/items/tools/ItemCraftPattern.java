package reika.rotarycraft.items.tools;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.base.ItemRotaryTool;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.RotaryRecipeTypes;

import java.util.List;

public class ItemCraftPattern  extends ItemRotaryTool {// implements SpriteRenderCallback {

    public ItemCraftPattern(Properties properties) {
        super(properties);
    }

    //right click to open programming gui

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.isCrouching()) {
            reika.dragonapi.libraries.registry.ReikaItemHelper.setStackTag(this.getDefaultInstance(), null);
        }
        else {
//         todo   player.openMenu(RotaryCraft.getInstance(), GuiRegistry.PATTERN.ordinal(), level, 0, 0, 0);
        }
        return InteractionResult.PASS;
    }

    // 1.21.5: Item.appendHoverText now has 5 args including TooltipDisplay and Consumer<Component>.
    @Override
    public void appendHoverText(ItemStack is, net.minecraft.world.item.Item.TooltipContext ctx, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> li, TooltipFlag flag) {
        if (is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag() == null) {
            li.accept(Component.literal("No Crafting Pattern."));
        }
        else {
            ItemStack item = this.getResult(is);
            if (item != null) {
                li.accept(Component.literal("Crafts "+item.getCount()+" "+item.getDisplayName()));
            }
            else {
                li.accept(Component.literal("Items, No Output."));
            }
        }
        li.accept(Component.literal("Recipe Mode: "+this.getMode(is).displayName));
    }

    public static ItemStack getResult(ItemStack is) {
        // 1.21.5: ItemStack.of(CompoundTag) was removed; deserialisation now goes through
        // ItemStack.parse(HolderLookup.Provider, Tag). Without ready access to the registry
        // provider here, return empty until we plumb a provider through.
        return ItemStack.EMPTY;
    }

    public static ItemStack[] getItems(ItemStack is) {
        // 1.21.5: ItemStack.of(CompoundTag) was removed; recipe-pattern decode stubbed
        // until a HolderLookup.Provider is plumbed through to call ItemStack.parse.
        return new ItemStack[9];
    }

    public static int getStackInputLimit(ItemStack is) {
        if (is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag() != null) {
            int amt = is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getIntOr("stacklimit", 0);
            return amt > 0 ? amt : 64;
        }
        return 64;
    }

    private static void resetNBT(ItemStack is) {
        if (is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag() != null) {
            reika.dragonapi.libraries.registry.ReikaItemHelper.updateStackTag(is, __T__ -> __T__.remove("output"));
            reika.dragonapi.libraries.registry.ReikaItemHelper.updateStackTag(is, __T__ -> __T__.remove("recipe"));
        }
    }

    public static void setRecipe(ItemStack is, CraftingContainer ic, Level world) {
        if (world.isClientSide())
            ;//return;
        RecipeMode mode = getMode(is);
        resetNBT(is);
        setMode(is, mode);
        if (is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag() == null)
            reika.dragonapi.libraries.registry.ReikaItemHelper.setStackTag(is, new CompoundTag());
        ItemStack out = mode.getRecipe(ic, world);
        boolean valid = out != null;
        CompoundTag recipe = new CompoundTag();
        // 1.21.5: ItemStack.save(CompoundTag) was removed; serialisation now goes through
        // ItemStack.save(HolderLookup.Provider, Tag). Persist via ItemStack.CODEC encode
        // until we plumb a provider end-to-end.
        net.minecraft.core.HolderLookup.Provider provider = world.registryAccess();
        for (int i = 0; i < 9; i++) {
            ItemStack in = ic.getItem(i);
            if (in != null && !in.isEmpty()) {
                net.minecraft.nbt.Tag encoded = ItemStack.CODEC.encodeStart(provider.createSerializationContext(net.minecraft.nbt.NbtOps.INSTANCE), in).result().orElse(null);
                if (encoded instanceof CompoundTag ct)
                    recipe.put("slot"+i, ct);
            }
        }
        reika.dragonapi.libraries.registry.ReikaItemHelper.updateStackTag(is, __T__ -> __T__.put("recipe", recipe));
        reika.dragonapi.libraries.registry.ReikaItemHelper.updateStackTag(is, __T__ -> __T__.putBoolean("valid", valid));
        if (valid) {
            net.minecraft.nbt.Tag encoded = ItemStack.CODEC.encodeStart(provider.createSerializationContext(net.minecraft.nbt.NbtOps.INSTANCE), out).result().orElse(null);
            if (encoded instanceof CompoundTag outt)
                reika.dragonapi.libraries.registry.ReikaItemHelper.updateStackTag(is, __T__ -> __T__.put("output", outt));
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
        return is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag() != null ? RecipeMode.list[is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getIntOr("mode", 0)] : RecipeMode.CRAFTING;
    }

    public static void setMode(ItemStack is, RecipeMode md) {
        if (!RotaryItems.CRAFT_PATTERN.get().equals(is)) //todo check equals
            return;
        reika.dragonapi.libraries.registry.ReikaItemHelper.updateStackTag(is, __T__ -> __T__.putInt("mode", md.ordinal()));
    }

    public static void changeStackLimit(ItemStack is, int change) {
        if (!RotaryItems.CRAFT_PATTERN.get().equals(is)) //todo check equals
            return;
        int limit = getStackInputLimit(is);
        reika.dragonapi.libraries.registry.ReikaItemHelper.updateStackTag(is, __T__ -> __T__.putInt("stacklimit", Mth.clamp(limit+change, 1, 64)));
    }

    public static enum RecipeMode {
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
            net.minecraft.world.item.crafting.RecipeManager rm = world.getServer().getRecipeManager();
            net.minecraft.world.item.crafting.CraftingInput input = ic.asCraftInput();
            switch (this) {
                case CRAFTING -> {
                    return rm.getRecipeFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING, input, world)
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
