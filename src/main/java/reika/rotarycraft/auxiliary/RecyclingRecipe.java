//package reika.rotarycraft.auxiliary;
//
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.Container;
//import net.minecraft.world.inventory.CraftingContainer;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.item.crafting.RecipeSerializer;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.level.Level;
//import reika.dragonapi.interfaces.CustomToStringRecipe;
//import reika.dragonapi.libraries.ReikaRecipeHelper;
//import reika.dragonapi.libraries.java.ReikaArrayHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.auxiliary.recipemanagers.RotaryRecipeTypes;
//import reika.rotarycraft.registry.RotaryItems;
//
//import java.util.ArrayList;
//
//public class RecyclingRecipe implements Recipe, CustomToStringRecipe {
//
//    private final Recipe input;
//    public final int scrapValue;
//
//    public RecyclingRecipe(int value, ItemStack... recipe) {
//        this(ReikaRecipeHelper.getShapelessRecipeFor(ReikaItemHelper.getSizedItemStack(RotaryItems.IRON_SCRAP.get().getDefaultInstance(), value), recipe), value);
//    }
//
//    public RecyclingRecipe(ItemStack in, int value) {
//        this(value, in);
//    }
//
//    public RecyclingRecipe(int value, ItemStack in, int amt) {
//        this(value, ReikaArrayHelper.getArrayOf(in, amt));
//    }
//
//    public RecyclingRecipe(Recipe is, int value) {
//        input = is;
//        scrapValue = value;
//    }
//
//    public ItemStack getCraftingResult(CraftingContainer ics) {
//        return ReikaItemHelper.getSizedItemStack(RotaryItems.IRON_SCRAP.get().getDefaultInstance(), scrapValue);//ItemStacks.getScrap(scrapValue);
//    }
//
//    @Override
//    public boolean matches(Container container, Level level) {
//        return input.matches(container, level);
//    }
//
//    @Override
//    public ItemStack assemble(Container p_44001_) {
//        return null;
//    }
//
//    @Override
//    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
//        return false;
//    }
//
//    @Override
//    public ItemStack getResultItem() {
//        return ReikaItemHelper.getSizedItemStack(RotaryItems.IRON_SCRAP.get().getDefaultInstance(), scrapValue);//ItemStacks.scrap;
//    }
//
//    @Override
//    public ResourceLocation getId() {
//        return null;
//    }
//
//    @Override
//    public RecipeSerializer<?> getSerializer() {
//        return null;
//    }
//
//    @Override
//    public RecipeType<?> getType() {
//        return RotaryRecipeTypes.RECYCLING;
//    }
//
//    public ArrayList<ItemStack> getSplitOutput() {
//        ArrayList<ItemStack> li = new ArrayList<>();
//        int val = scrapValue;
//        while (val > 0) {
//            int num = Math.min(val, RotaryItems.IRON_SCRAP.get().getMaxStackSize());
//            li.add(ReikaItemHelper.getSizedItemStack(RotaryItems.IRON_SCRAP.get().getDefaultInstance(), num));
//            val -= num;
//        }
//        return li;
//    }
//
//    @Override
//    public String toDisplayString() {
//        return scrapValue+" X scrap = "+ ReikaRecipeHelper.toString(input);
//    }
//
//    @Override
//    public String toDeterministicString() {
//        return scrapValue+" X scrap = "+ReikaRecipeHelper.toDeterministicString(input);
//    }
//
//}
