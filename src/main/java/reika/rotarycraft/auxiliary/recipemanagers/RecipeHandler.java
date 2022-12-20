package reika.rotarycraft.auxiliary.recipemanagers;

import com.google.common.collect.HashBiMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import reika.dragonapi.exception.RegistrationException;
import reika.dragonapi.instantiable.data.KeyedItemStack;
import reika.dragonapi.instantiable.data.maps.MultiMap;
import reika.dragonapi.instantiable.io.CustomRecipeList;
import reika.dragonapi.instantiable.io.LuaBlock;
import reika.dragonapi.instantiable.recipe.FlexibleIngredient;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.java.ReikaStringParser;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

public abstract class RecipeHandler implements FlexibleIngredient.IngredientIDHandler {

    private static final boolean enableRegistries = ConfigRegistry.RECIPEMOD.getState();

    private final MultiMap<RecipeLevel, String> recipesByLevel = new MultiMap(MultiMap.CollectionType.HASHSET);
    private final HashMap<String, RecipeLevel> recipeLevels = new HashMap();

    private final HashBiMap<MachineRecipe, String> recipeKeys = HashBiMap.create();

    public final MachineRegistry machine;

    protected RecipeHandler(MachineRegistry m) {
        machine = m;
    }

    protected final void onAddRecipe(MachineRecipe recipe, RecipeLevel rl) {
        if (enableRegistries) {
            String s = recipeKeys.get(recipe);
            if (s == null) {
                s = this.generateKey(recipe);
            }
            if (s == null)
                return;
            recipesByLevel.addValue(rl, s);
            recipeLevels.put(s, rl);
        }
    }

    private String generateKey(MachineRecipe recipe) {
        String s = machine.name()+"$"+recipe.getClass().getSimpleName()+"#("+recipe.getUniqueID();
//  todo      if (RotaryCraft.LOGGER.shouldDebug())
            ReikaJavaLibrary.pConsole("Recipe Loaded: "+recipe+"="+s);
        if (recipeKeys.containsValue(s)) {
            MachineRecipe pre = recipeKeys.inverse().get(s);
            if (pre == null || pre.equals(recipe)) {
                return null; //do nothing
            }
            else {
                RotaryCraft.LOGGER.error("Found duplicate recipe key when adding recipe "+recipe.getAllInfo()+" in place of "+pre.getAllInfo());
                RotaryCraft.LOGGER.info("Original Recipe Items:");
                for (ItemStack is : pre.getAllUsedItems()) {
//                    RotaryCraft.LOGGER.info(is+" from mod '"+ReikaItemHelper.getRegistrantMod(is)+"', NBT="+is.getTag());
                }
                RotaryCraft.LOGGER.info("New Recipe Items:");
                for (ItemStack is : recipe.getAllUsedItems()) {
//                    RotaryCraft.LOGGER.info(is+" from mod '"+ReikaItemHelper.getRegistrantMod(is)+"', NBT="+is.getTag());
                }
                throw new RegistrationException(RotaryCraft.getInstance(), "Two recipes have the same key: '"+s+"'");
            }
        }
        recipeKeys.put(recipe, s);
        return s;
    }

    public final String fullIDForItems(Collection<KeyedItemStack> c) {
        return fullIDKeys(c);
    }

    protected static String fullIDKeys(Collection<KeyedItemStack> c) {
        StringBuilder sb = new StringBuilder();
        for (KeyedItemStack is : c) {
            sb.append(CustomRecipeList.fullID(is.getItemStack()));
            sb.append("|");
        }
        return sb.toString();
    }

    protected static String fullID(Collection<ItemStack> c) {
        StringBuilder sb = new StringBuilder();
        for (ItemStack is : c) {
            sb.append(CustomRecipeList.fullID(is));
            sb.append("|");
        }
        return sb.toString();
    }

    protected static String fullID(ItemStack is) {
        return CustomRecipeList.fullID(is);
    }

    protected final Collection<?> getRecipes(RecipeLevel rl) {
        return recipesByLevel.get(rl);
    }

    public final RecipeLevel getRecipeLevel(String rec) {
        return recipeLevels.get(rec);
    }

    public final boolean removeRecipe(String rec) {
        RecipeLevel rl = this.getRecipeLevel(rec);
        RecipeModificationPower power = getModificationPower();
        if (power.canRemove(rl)) {
            MachineRecipe recipe = recipeKeys.inverse().get(rec);
            if (rec == null) {
                RotaryCraft.LOGGER.info("Recipe removal of '"+rec+"' from "+machine+" not possible; No such recipe with that key.");
                return false;
            }
            try {
                if (this.removeRecipe(recipe)) {
                    recipesByLevel.remove(rl, rec);
                    recipeLevels.remove(rec);
                    recipeKeys.remove(recipe);
                    return true;
                }
                else {
                    RotaryCraft.LOGGER.info("Recipe removal of '"+rec+"' from "+machine+" failed; Potential code error.");
                    return false;
                }
            }
            catch (Exception e) {
                RotaryCraft.LOGGER.info("Recipe removal of '"+rec+"' from "+machine+" failed and threw an exception; Potential code error.");
                e.printStackTrace();
                return false;
            }
        }
        else {
            RotaryCraft.LOGGER.info("Recipe removal of '"+rec+"' from "+machine+" rejected; This is a '"+rl+"' recipe and cannot be modified with '"+power+"' modify power.");
            return false;
        }
    }

    public abstract void addPostLoadRecipes();

    protected abstract boolean removeRecipe(MachineRecipe recipe);
    //protected abstract boolean addCustomRecipe(String key);

/*    private static RecipeModificationPower getRequiredPowerForOutput(ItemStack is) {
        if (!ReikaItemHelper.getNamespace(is.getItem()).contains("RotaryCraft"))
            return RecipeModificationPower.DEFAULT;
        if (!ReikaItemHelper.getNamespace(is.getItem()).contains("ReactorCraft"))
            return RecipeModificationPower.DEFAULT;
        if (!ReikaItemHelper.getNamespace(is.getItem()).contains("ElectriCraft"))
            return RecipeModificationPower.DEFAULT;
        return SensitiveItemRegistry.instance.contains(is) ? RecipeModificationPower.FULL : RecipeModificationPower.NORMAL;
    }

    private static RecipeModificationPower getRequiredPowerForOutput(Fluid f) {
        return SensitiveFluidRegistry.instance.contains(f) ? RecipeModificationPower.FULL : RecipeModificationPower.DEFAULT;
    }

    public static boolean isOutputPermitted(ItemStack is) {
        return getModificationPower().ordinal() <= getRequiredPowerForOutput(is).ordinal();
    }

    public static boolean isOutputPermitted(Fluid f) {
        return getModificationPower().ordinal() <= getRequiredPowerForOutput(f).ordinal();
    }*/

    public static enum RecipeLevel {

        CORE(), //Core native recipesByLevel
        PROTECTED(), //Non-core but native and fairly important
        PERIPHERAL(), //Non-core but native
        MODINTERACT(), //Ones for mod interaction; also native
        API(), //API-level
        CUSTOM(); //Minetweaker

        private static final RecipeLevel[] list = values();

    }

    private enum RecipeModificationPower {
        FULL(RecipeLevel.CORE),
        STRONG(RecipeLevel.PROTECTED),
        NORMAL(RecipeLevel.PERIPHERAL),
        DEFAULT(RecipeLevel.CUSTOM);

        private final RecipeLevel maxLevel;

        private static final RecipeModificationPower[] list = values();

        RecipeModificationPower(RecipeLevel rl) {
            maxLevel = rl;
        }

        public final boolean canRemove(RecipeLevel rl) {
            return rl.ordinal() >= maxLevel.ordinal();
        }
    }

    private static RecipeModificationPower getModificationPower() {
        int get = Math.min(RecipeModificationPower.DEFAULT.ordinal(), Math.max(0, 5));//ConfigRegistry.getRecipeModifyPower()));
        return RecipeModificationPower.list[RecipeModificationPower.DEFAULT.ordinal()-get];
    }

    protected interface MachineRecipe {
        String getUniqueID();
        Collection<ItemStack> getAllUsedItems();
        String getAllInfo();

    }

    public final void loadCustomRecipeFiles() {
        CustomRecipeList crl = new CustomRecipeList(RotaryCraft.getInstance(), machine.name().toLowerCase(Locale.ENGLISH));
        if (crl.load()) {
            for (LuaBlock lb : crl.getEntries()) {
                Exception e = null;
                boolean flag = false;
                String n = lb.getString("type");
                try {
                    if (LuaBlock.isErrorCode(n))
                        throw new IllegalArgumentException("Custom recipes require a specified name!");
                    if (!ReikaStringParser.isValidVariableName(n))
                        throw new IllegalArgumentException("Name must be a valid field name in Java syntax! '"+n+"' is not valid!");
                    flag = this.addCustomRecipe(n, lb, crl);
                }
                catch (Exception ex) {
                    e = ex;
                    flag = false;
                }
                if (flag) {
                    RotaryCraft.LOGGER.info("Loaded custom recipe '"+n+"' for "+machine.name()+"");
                }
                else {
                    RotaryCraft.LOGGER.error("Could not load custom recipe '"+n+"' for "+machine.name()+"");
                    if (e != null)
                        e.printStackTrace();
                }
            }
        }
        else {/*
			crl.createFolders();
			for (Collection c : this.getRecipes(RecipeLevel.CORE))
				crl.addToExample(createLuaBlock(c));
			crl.createExampleFile();*/
        }
    }

    protected abstract boolean addCustomRecipe(String n, LuaBlock lb, CustomRecipeList crl) throws Exception;

    protected final void verifyOutputItem(ItemStack is) {
        /*if (is.getItem() instanceof ItemBlockPlacer || is.getItem() == RotaryItems.ETHANOL.get())
            throw new IllegalArgumentException("This item is not permitted as an output (it is a gating item).");
        if (ReikaItemHelper.matchStacks(is, RotaryItems.tungstenflakes) || ReikaItemHelper.matchStacks(is, RotaryItems.TUNGSTEN_ALLOY_INGOT))
            throw new IllegalArgumentException("This item is not permitted as an output (it is a gating item).");
        if (ReikaItemHelper.matchStacks(is, RotaryItems.SILICONDUST) || ReikaItemHelper.matchStacks(is, RotaryItems.silicon))
            throw new IllegalArgumentException("This item is not permitted as an output (it is a gating item).");
        if (ReikaItemHelper.matchStacks(is, RotaryItems.REDGOLDINGOT) || ReikaItemHelper.matchStacks(is, RotaryItems.silumin))
            throw new IllegalArgumentException("This item is not permitted as an output (it is a gating item).");
        if (ReikaItemHelper.matchStacks(is, RotaryItems.BEDROCKDUST) || ReikaItemHelper.matchStacks(is, RotaryItems.BEDROCK_ALLOY_INGOT))
            throw new IllegalArgumentException("This item is not permitted as an output (it is a gating item).");
        if (ReikaItemHelper.matchStacks(is, RotaryItems.SPRINGINGOT) || ReikaItemHelper.matchStacks(is, RotaryItems.HSLA_STEEL_INGOT))
            throw new IllegalArgumentException("This item is not permitted as an output (it is a gating item).");*/
    }

    protected final void verifyOutputFluid(Fluid f) {
//    todo    if (f.getName().equalsIgnoreCase("rc jet fuel") || f.getName().equalsIgnoreCase("rc ethanol") || f.getName().equalsIgnoreCase("rc lubricant"))
//            throw new IllegalArgumentException("This fluid is not permitted as an output (it is a gating item).");
    }

}