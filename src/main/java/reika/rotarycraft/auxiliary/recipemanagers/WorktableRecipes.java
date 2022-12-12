//package reika.rotarycraft.auxiliary.recipemanagers;
//
//import net.minecraft.core.NonNullList;
//import net.minecraft.world.inventory.CraftingContainer;
//import net.minecraft.world.item.BlockItem;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.item.crafting.ShapedRecipe;
//import net.minecraft.world.item.crafting.ShapelessRecipe;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import reika.dragonapi.ModList;
//import reika.dragonapi.instantiable.data.immutable.BlockKey;
//import reika.dragonapi.instantiable.io.CustomRecipeList;
//import reika.dragonapi.instantiable.io.LuaBlock;
//import reika.dragonapi.libraries.ReikaRecipeHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.api.RecipeInterface;
//import reika.rotarycraft.api.RecipeInterface.WorktableManager;
//import reika.rotarycraft.auxiliary.RecyclingRecipe;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//public class WorktableRecipes extends RecipeHandler implements WorktableManager {
//
//    private static final WorktableRecipes instance = new WorktableRecipes();
//
//    private ArrayList<WorktableRecipe> recipes = new ArrayList();
//    private ArrayList<Recipe> display = new ArrayList();
//
//    private final RecipeSorter sorter = new RecipeSorter();
//
//    public static final WorktableRecipes getInstance() {
//        return instance;
//    }
//
//    @Override
//    public void addAPIRecipe(Recipe recipe) {
//        this.addRecipe(recipe, RecipeLevel.API);
//    }
//
//    public void addRecyclingRecipe(RecyclingRecipe recipe) {
//        this.addRecipe(recipe, RecipeLevel.PERIPHERAL);
//    }
//
//    public void addRecipe(Recipe recipe, RecipeLevel rl) {
//        WorktableRecipe wr = new WorktableRecipe(recipe);
//        recipes.add(wr);
//        display.add(recipe);
//
//        super.onAddRecipe(wr, rl);
//    }
//
//    private WorktableRecipes() {
//        super(MachineRegistry.WORKTABLE);
//        RecipeInterface.worktable = this;
//        //Collections.sort(recipes, sorter);
//    }
//
//    public ShapedRecipe addAPIRecipe(ItemStack output, Object... items) {
//        return this.addRecipe(output, RecipeLevel.API, items);
//    }
//
//    public ShapedRecipe addRecipe(ItemStack output, RecipeLevel rl, Object... items) {
//        String s = "";
//        int i = 0;
//        int j = 0;
//        int k = 0;
//        //ReikaJavaLibrary.spamConsole(Arrays.toString(par2ArrayOfObj));
//        if (items[i] == null || ((items[i] instanceof ItemStack && ((ItemStack)items[i]).getItem() == null))) {
//            throw new IllegalArgumentException("Null item in recipe! Possible mod conflict?");
//        }
//        else if (items[i] instanceof String[]) {
//            String[] astring = ((String[])items[i++]);
//
//            for (String s1 : astring) {
//                ++k;
//                j = s1.length();
//                s = s + s1;
//            }
//        }
//        else {
//            while (items[i] instanceof String) {
//                String s2 = (String)items[i++];
//                ++k;
//                j = s2.length();
//                s = s+s2;
//            }
//        }
//
//        HashMap hashmap;
//
//        for (hashmap = new HashMap(); i < items.length; i += 2) {
//            Character character = (Character)items[i];
//            ItemStack input = null;
//
//            if (items[i+1] == null || items[i+1] == Blocks.AIR) {
//                input = null;
//            }
//            else if (items[i+1] instanceof Item) {
//                input = new ItemStack((Item)items[i+1]);
//            }
//            else if (items[i+1] instanceof Block) {
//                if (Item.BY_BLOCK.get((Block)items[i+1]) == null)
//                    throw new IllegalArgumentException("Tried to create a recipe with a block with no item!");
//                input = new ItemStack((Block)items[i+1], 1); //todo meta was 32767? idfk
//            }
//            else if (items[i+1] instanceof ItemStack) {
//                input = (ItemStack)items[i+1];
//            }
//            else if (items[i+1] instanceof BlockKey) {
//                input = ((BlockKey)items[i+1]).asItemStack();
//            }
//            else if (items[i+1] == null || ((items[i+1] instanceof ItemStack && ((ItemStack)items[i+1]).getItem() == null))) {
//                throw new IllegalArgumentException("Null item in recipe! Possible mod conflict?");
//            }
//
//            //ReikaJavaLibrary.pConsole(character+" -> "+itemstack1);
//            if (input != null)
//                hashmap.put(character, input);
//        }
//
//
//        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(j * k, Ingredient.EMPTY);
//
//        for (int i1 = 0; i1 < j * k; ++i1) {
//            char c0 = s.charAt(i1);
//
//            if (hashmap.containsKey(c0)) {
//                //ReikaJavaLibrary.spamConsole(c0+":   "+(hashmap.get(Character.valueOf(c0))));
//                nonnulllist.set(i1, Ingredient.of(((ItemStack) hashmap.get(Character.valueOf(c0))).copy()));
//            }
//            else {
//                nonnulllist.set(i1, null); //todo likely crash if null
//            }
//        }
//
//        ShapedRecipe shapedrecipes = new ShapedRecipe(null, null,j, k, nonnulllist, output);
//        this.addRecipe(shapedrecipes, rl);
//        return shapedrecipes;
//    }
//
//    public void addShapelessAPIRecipe(ItemStack output, Object... items) {
//        this.addShapelessRecipe(output, RecipeLevel.API, items);
//    }
//
//    public void addShapelessRecipe(ItemStack output, RecipeLevel rl, Object... items) {
//        ArrayList<Ingredient> li = new ArrayList<>();
//        Object[] aobject = items;
//        int i = items.length;
//        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i, Ingredient.EMPTY);
//
//        for (int j = 0; j < i; ++j) {
//            Object object1 = aobject[j];
//
//            if (object1 == null)
//                throw new IllegalArgumentException("Null item in recipe! Possible mod conflict?");
//            else if (object1 instanceof ItemStack) {
//                if (((ItemStack)object1).getItem() == null)
//                    throw new IllegalArgumentException("Null item in recipe! Possible mod conflict?");
//                nonnulllist.add(Ingredient.of(((ItemStack)object1).copy()));
//            }
//            else if (object1 instanceof Item) {
//                nonnulllist.add(Ingredient.of(new ItemStack((Item)object1)));
//            }
//            else {
//                if (!(object1 instanceof Block)) {
//                    throw new RuntimeException("Invalid shapeless recipe!");
//                }
//
//                nonnulllist.add(Ingredient.of(new ItemStack((Block)object1)));
//            }
//        }
//
//
//        this.addRecipe(new ShapelessRecipe(null, null, output, nonnulllist), rl);
//    }
//
//    public WorktableRecipe findMatchingRecipe(CraftingContainer ic, Level world) {
//        int i = 0;
//        ItemStack is = null;
//        ItemStack is1 = null;
//        int j;
//
//        for (j = 0; j < ic.getContainerSize(); ++j) {
//            ItemStack is2 = ic.getItem(j);
//
//            if (is2 != null) {
//                if (i == 0) {
//                    is = is2;
//                }
//
//                if (i == 1) {
//                    is1 = is2;
//                }
//
//                ++i;
//            }
//        }
//
//        for (WorktableRecipe wr : recipes) {
//            Recipe ir = wr.recipe;
//
//            if (ir.matches(ic, world))
//                return wr;//ir.getCraftingResult(ic);
//        }
//
//        return null;
//    }
//
//    public List<WorktableRecipe> getRecipeListCopy() {
//        return Collections.unmodifiableList(recipes);
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public List<Recipe> getDisplayList()
//    {
//        return Collections.unmodifiableList(display);
//    }
//
//    public Recipe getInputRecipe(ItemStack is) {
//        is = is.copy();
//        if (is.getTag() != null) {
//            is.getTag().remove("lubricant");
//            is.getTag().remove("lube");
//            is.getTag().remove("energy");
//            is.getTag().remove("bearing");
//            if (!is.getTag().getBoolean("living"))
//                is.getTag().remove("living");
//        }
//        for (WorktableRecipe wr : recipes) {
//            Recipe ir = wr.recipe;
//            ItemStack is2 = ir.getResultItem();
//            if (ReikaItemHelper.matchStacks(is, is2) && is.getCount() >= is2.getCount()) {
//                if (is.getTag() == null || ItemStack.isSameItemSameTags(is, is2))
//                    return ir;
//            }
//        }
//        return null;
//    }
//
//    public static final class WorktableRecipe implements MachineRecipe {
//
//        private final Recipe recipe;
//        private final ItemStack output;
//
//        private WorktableRecipe(Recipe ir) {
//            if (ir == null)
//                throw new IllegalArgumentException("Invalid worktable recipe: Null!");
//            if (ir.getResultItem() == null)
//                throw new IllegalArgumentException("Invalid worktable recipe: No output!");
//            recipe = ir;
//            output = ir.getResultItem();
//        }
//
//        public ItemStack getOutput() {
//            return output.copy();
//        }
//
//        public boolean containsItem(ItemStack is) {
//            return ReikaRecipeHelper.recipeContains(recipe, is);
//        }
//
//        public ItemStack[] getDisplayArray() {
//            return ReikaRecipeHelper.getPermutedRecipeArray(recipe);
//        }
//
//    /*    @ModDependent(ModList.CHROMATICRAFT)
//        public ElementTagCompound getElements() {
//            return ItemElementCalculator.instance.getIRecipeTotal(recipe);
//        }*/
//
//        @Override
//        public String getUniqueID() {
//            return "WORKTABLE/"+recipe.getClass().getName()+"^"+ ReikaRecipeHelper.toDeterministicString(recipe)+">"+fullID(output)+"?"+(output.getItem() instanceof BlockItem);
//        }
//
//        @Override
//        public String getAllInfo() {
//            return "Crafting "+fullID(output)+" from "+ReikaRecipeHelper.toString(recipe);
//        }
//
//        @Override
//        public Collection<ItemStack> getAllUsedItems() {
//            ArrayList<ItemStack> li = new ArrayList(ReikaRecipeHelper.getAllItemsInRecipe(recipe));
//            li.add(output);
//            return li;
//        }
//
//        public boolean isRecycling() {
//            return recipe instanceof RecyclingRecipe;
//        }
//
//        public RecyclingRecipe getRecycling() {
//            return this.isRecycling() ? (RecyclingRecipe)recipe : null;
//        }
//
//        @Override
//        public int hashCode() {
//            return this.getUniqueID().hashCode();
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            return o instanceof WorktableRecipe && ((WorktableRecipe)o).getUniqueID().equals(this.getUniqueID());
//        }
//
//    }
//
//    private static class RecipeSorter implements Comparator<WorktableRecipe> {
//
//        private RecipeSorter()
//        {
//
//        }
//
//        public int compare(WorktableRecipe ir, WorktableRecipe ir2)
//        {
//            return ir.recipe instanceof ShapelessRecipe && ir2.recipe instanceof ShapedRecipe ? 1 : (ir2.recipe instanceof ShapelessRecipe && ir.recipe instanceof ShapedRecipe ? -1 : Integer.compare(ir2.recipe.getIngredients().size(), ir.recipe.getIngredients().size()));
//        }
//    }
//
//    @Override
//    public void addPostLoadRecipes() {
//
//    }
//
//    @Override
//    protected boolean removeRecipe(MachineRecipe recipe) {
//        return recipes.remove(recipe) && display.remove(((WorktableRecipe)recipe).recipe);
//    }
//
//    @Override
//    protected boolean addCustomRecipe(String n, LuaBlock lb, CustomRecipeList crl) throws Exception {
//        ItemStack out = crl.parseItemString(lb.getString("output"), lb.getChild("output_nbt"), false);
//        this.verifyOutputItem(out);
//        Recipe ir = crl.parseCraftingRecipe(lb, out);
//        this.addRecipe(ir, RecipeLevel.CUSTOM);
//        return true;
//    }
//}
