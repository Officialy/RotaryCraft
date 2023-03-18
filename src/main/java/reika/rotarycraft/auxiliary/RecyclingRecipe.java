package reika.rotarycraft.auxiliary;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import reika.dragonapi.interfaces.CustomToStringRecipe;
import reika.dragonapi.interfaces.IReikaRecipe;
import reika.dragonapi.libraries.java.ReikaArrayHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.registry.RotaryItems;

import java.util.ArrayList;

public class RecyclingRecipe implements IReikaRecipe, CustomToStringRecipe {

    private final IReikaRecipe input;
    public final int scrapValue;

    public RecyclingRecipe(int value, ItemStack... recipe) {
        this(ReikaRecipeHelper.getShapelessRecipeFor(ReikaItemHelper.getSizedItemStack(RotaryItems.IRON_SCRAP.get().getDefaultInstance(), value), recipe), value);
    }

    public RecyclingRecipe(ItemStack in, int value) {
        this(value, in);
    }

    public RecyclingRecipe(int value, ItemStack in, int amt) {
        this(value, ReikaArrayHelper.getArrayOf(in, amt));
    }

    public RecyclingRecipe(IReikaRecipe is, int value) {
        input = is;
        scrapValue = value;
    }

    @Override
    public ItemStack getCraftingResult(Container ics) {
        return ReikaItemHelper.getSizedItemStack(RotaryItems.IRON_SCRAP.get().getDefaultInstance(), scrapValue);//RotaryItems.getScrap(scrapValue);
    }

    @Override
    public boolean matches(Container container, Level level) {
        return input.matches(container, level);
    }

    @Override
    public ItemStack getResult() {
        return ReikaItemHelper.getSizedItemStack(RotaryItems.IRON_SCRAP.get().getDefaultInstance(), scrapValue);//RotaryItems.scrap;
    }

    @Override
    public int getRecipeGridSize() {
        return 1;
    }

    public ArrayList<ItemStack> getSplitOutput() {
        ArrayList<ItemStack> li = new ArrayList<>();
        int val = scrapValue;
        while (val > 0) {
            int num = Math.min(val, RotaryItems.IRON_SCRAP.get().getMaxStackSize());
            li.add(ReikaItemHelper.getSizedItemStack(RotaryItems.IRON_SCRAP.get().getDefaultInstance(), num));
            val -= num;
        }
        return li;
    }

    @Override
    public String toDisplayString() {
        return scrapValue + " X scrap = ";// todo + ReikaRecipeHelper.toString(input);
    }

    @Override
    public String toDeterministicString() {
        return scrapValue + " X scrap = ";//todo +ReikaRecipeHelper.toDeterministicString(input);
    }

}
