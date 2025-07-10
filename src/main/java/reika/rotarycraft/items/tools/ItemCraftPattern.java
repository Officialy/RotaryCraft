//package reika.rotarycraft.items.tools;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.util.Mth;
//import net.minecraft.world.Container;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResultHolder;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.CraftingContainer;
//import net.minecraft.world.inventory.CraftingMenu;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.TooltipFlag;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Blocks;
//
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.ItemRotaryTool;
//import reika.rotarycraft.registry.GuiRegistry;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//import java.util.List;
//
//public class ItemCraftPattern  extends ItemRotaryTool {// implements SpriteRenderCallback {
//
//    public ItemCraftPattern(Properties properties) {
//        super(properties);
//    }
//
//    //right click to open programming gui
//
//    @Override
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//        if (player.isCrouching()) {
//            this.getDefaultInstance().setTag(null);
//        }
//        else {
////         todo   player.openMenu(RotaryCraft.getInstance(), GuiRegistry.PATTERN.ordinal(), level, 0, 0, 0);
//        }
//        return InteractionResultHolder.pass(this.getDefaultInstance());
//    }
//
//    @Override
//    public void appendHoverText(ItemStack is,  Level p_41422_, List<Component> li, TooltipFlag p_41424_) {
//        //FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
//        if (is.getTag() == null) {
//            li.add(Component.literal("No Crafting Pattern."));
//        }
//        else {
//            ItemStack item = this.getResult(is);
//            if (item != null) {
//                li.add(Component.literal("Crafts "+item.getCount()+" "+item.getDisplayName()));//+" with:");
//            }
//            else {
//                li.add(Component.literal("Items, No Output."));
//            }
//        }
//        li.add(Component.literal("Recipe Mode: "+this.getMode(is).displayName));
//    }
//
//    public static ItemStack getResult(ItemStack is) {
//        ItemStack item = is.getTag() != null ? ItemStack.of(is.getTag().getCompound("output")) : null;
//        return item != null ? item.copy() : null;
//    }
//
//    public static ItemStack[] getItems(ItemStack is) {
//        ItemStack[] items = new ItemStack[9];
//        if (is.getTag() != null) {
//            CompoundTag recipe = is.getTag().getCompound("recipe");
//            for (int i = 0; i < 9; i++) {
//                String s = "slot"+i;
//                if (recipe.contains(s)) {
//                    CompoundTag tag = recipe.getCompound(s);
//                    ItemStack in = ItemStack.of(tag);
//                    if (in == null && tag != null && !tag.isEmpty()) { //item no longer exists, clear the pattern
//                        is.setTag(null);
//                        return null;
//                    }
//                    items[i] = in;
//                }
//            }
//        }
//        return items;
//    }
//
//    public static int getStackInputLimit(ItemStack is) {
//        if (is.getTag() != null) {
//            int amt = is.getTag().getInt("stacklimit");
//            return amt > 0 ? amt : 64;
//        }
//        return 64;
//    }
//
//    private static void resetNBT(ItemStack is) {
//        if (is.getTag() != null) {
//            is.getTag().remove("output");
//            is.getTag().remove("recipe");
//        }
//    }
//
//    public static void setRecipe(ItemStack is, CraftingContainer ic, Level world) {
//        if (world.isClientSide())
//            ;//return;
//        RecipeMode mode = getMode(is);
//        resetNBT(is);
//        setMode(is, mode);
//        if (is.getTag() == null)
//            is.setTag(new CompoundTag());
//        ItemStack out = mode.getRecipe(ic, world);
//        boolean valid = out != null;
//        CompoundTag recipe = new CompoundTag();
//        for (int i = 0; i < 9; i++) {
//            ItemStack in = ic.getItem(i);
//            if (in != null) {
//                CompoundTag tag = new CompoundTag();
//                in.save(tag);
//                recipe.put("slot"+i, tag);
//            }
//        }
//        is.getTag().put("recipe", recipe);
//        is.getTag().putBoolean("valid", valid);
//        if (valid) {
//            CompoundTag outt = new CompoundTag();
//            out.save(outt);
//            is.getTag().put("output", outt);
//        }
//        //ReikaJavaLibrary.pConsole(Arrays.toString(items)+" -> "+out);
//    }
//
///*    @Override
//    public boolean onRender(RenderItem ri, ItemStack is, ItemRenderType type) {
//        if (type == ItemRenderType.INVENTORY && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
//            ItemStack out = this.getResult(is);
//            if (out != null) {
//                double s = 0.0625;
//                GL11.glScaled(s, -s, s);
//                ReikaGuiAPI.instance.drawItemStack(ri, out, 0, -16);
//                return true;
//            }
//        }
//        return false;
//    }*/
//
//    public static RecipeMode getMode(ItemStack is) {
//        return is.getTag() != null ? RecipeMode.list[is.getOrCreateTag().getInt("mode")] : RecipeMode.CRAFTING;
//    }
//
//    public static void setMode(ItemStack is, RecipeMode md) {
//        if (!RotaryItems.CRAFTPATTERN.get().equals(is)) //todo check equals
//            return;
//        is.getOrCreateTag().putInt("mode", md.ordinal());
//    }
//
//    public static void changeStackLimit(ItemStack is, int change) {
//        if (!RotaryItems.CRAFTPATTERN.get().equals(is)) //todo check equals
//            return;
//        int limit = getStackInputLimit(is);
//        is.getOrCreateTag().putInt("stacklimit", Mth.clamp(limit+change, 1, 64));
//    }
//
//    public static enum RecipeMode {
//        CRAFTING("Crafting Recipe", new ItemStack(Blocks.CRAFTING_TABLE)),
//        WORKTABLE("Worktable Recipe", MachineRegistry.WORKTABLE.getBlockState().getBlock().asItem().getDefaultInstance()),;
////todo        BLASTFURN("Blast Furnace Crafting", MachineRegistry.BLASTFURNACE.getCraftedProduct());
//
//        private final ItemStack item;
//        public final String displayName;
//
//        public static final RecipeMode[] list = values();
//
//        RecipeMode(String s, ItemStack is) {
//            item = is;
//            displayName = s;
//        }
//
//        public ItemStack getIcon() {
//            return item.copy();
//        }
//
//        public RecipeMode next() {
//            return this.ordinal() == list.length-1 ? list[0] : list[this.ordinal()+1];
//        }
//
//        public ItemStack getRecipe(CraftingContainer ic, Level world) {
//            switch (this) {
//                case CRAFTING -> {
//                    List<Recipe> li = null;//todo CraftingManager.getInstance().getRecipeList();
//                    for (Recipe ir : li) {
//                        if (ir.matches(ic, null)) {
//                            return ir.getResultItem();
//                        }
//                    }
//                    return null;
//                }
//                /*case BLASTFURN -> {
//                    for (BlastCrafting ir : RecipesBlastFurnace.getRecipes().getAllCraftingRecipes())  {
//                        if (ir.matches(ic, Integer.MAX_VALUE)) {
//                            return ir.outputItem();
//                        }
//                    }
//                    return null;*/
//                case WORKTABLE -> {
//                    WorktableRecipes.WorktableRecipe wr = WorktableRecipes.getInstance().findMatchingRecipe(ic, null);
//                    return wr != null ? wr.getOutput() : null;
//                }
//            }
//            return null;
//        }
//    }
//
//    public static boolean checkPatternForMatch(Container te, RecipeMode type, int invslot, int patternslot, ItemStack is, ItemStack p) {
//        ItemStack in = te.getItem(invslot);
//        return getMode(p) == type && checkItemAndSize(patternslot, is, p, in != null ? in.getCount() : 0);
//    }
//
//    private static boolean checkItemAndSize(int slot, ItemStack is, ItemStack p, int current) {
//        return ReikaItemHelper.matchStacks(is, getItems(p)[slot]) && current+is.getCount() <= Math.min(is.getMaxStackSize(), getStackInputLimit(p));
//    }
//}
