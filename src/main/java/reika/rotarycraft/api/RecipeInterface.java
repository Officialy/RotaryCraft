package reika.rotarycraft.api;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;

public class RecipeInterface {

    public static CompactorManager compactor;
    public static GrinderManager grinder;
    public static CentrifugeManager centrifuge;
    public static PulseFurnaceManager pulsefurn;
    public static CrystallizerManager crystallizer;
    public static RockMelterManager rockmelt;
    public static WetterManager wetter;
    public static DryingBedManager dryingbed;
    public static FrictionHeaterManager friction;
    public static BlastFurnaceManager blastfurn;
    public static MagnetizerManager magnetizer;
    public static WorktableManager worktable;


    public interface CompactorManager extends RecipeManager {

        void addAPIRecipe(ItemStack in, ItemStack out, int pressure, int temperature);

    }

    public interface GrinderManager extends RecipeManager {

        void addAPIRecipe(ItemStack in, ItemStack out);

    }

    public interface CentrifugeManager extends RecipeManager {

        void addAPIRecipe(ItemStack in, FluidStack fout, float chanceout, Object... items);

    }

    public interface PulseFurnaceManager extends RecipeManager {

        void addAPISmelting(ItemStack in, ItemStack out);

    }

    public interface CrystallizerManager extends RecipeManager {

        void addAPIRecipe(Fluid f, int amount, ItemStack out);

    }

    public interface RockMelterManager extends RecipeManager {

        void addAPIRecipe(ItemStack in, FluidStack out, int temperature, long energy);

    }

    public interface WetterManager extends RecipeManager {

         void addAPIRecipe(ItemStack in, Fluid f, int amount, ItemStack out, int time);

    }

    public interface DryingBedManager extends RecipeManager {

         void addAPIRecipe(Fluid f, int amount, ItemStack out);

    }

    public interface BlastFurnaceManager extends RecipeManager {

        /**
         * The first nine arguments are for the 3 slots on the left - three groups of ItemStack, chance-to-consume, and number-to-consume.
         * To ignore a slot, supply an empty collection, NOT null.
         *
         * The other arguments control the rest of the recipe. 'Main' is the main grid item (like iron for HSLA), 'req' is how many of it are required
         * (supply -1 for "any"), 'bonus' is whether and how much bonus output amounts can be given (>0 to give any, with the actual value as a multiplier).
         */
        void addAPIAlloying(Collection<ItemStack> in1, float c1, int decr1, Collection<ItemStack> in2, float c2, int decr2, Collection<ItemStack> in3, float c3, int decr3, ItemStack main, ItemStack out, int req, float bonus, float xp, int temp);

        /** For adding 3x3 crafting recipes like bedrock tool crafting. 'Speed' works inversely; higher values mean slower recipes. */
        void addAPIRecipe(ItemStack out, int temperature, Recipe in, int speed, float xp);

    }

    public interface FrictionHeaterManager extends RecipeManager {

        void addAPIRecipe(ItemStack in, ItemStack out, int temperature, int time);

    }

    public interface MagnetizerManager extends RecipeManager {

        /** Null is acceptable for an action, in which case it will use the native NBT "magnet" behavior. */
        void addAPIRecipe(ItemStack in, int minSpeed, int reqSpeedPerMicroTesla, int timeFactor, boolean allowStacks, MagnetizationAction a);

        interface MagnetizationAction {

            void step(int omega, ItemStack is);

        }

    }

    public interface WorktableManager extends RecipeManager {

        void addAPIRecipe(Recipe ir);

    }

    private interface RecipeManager {

    }

    private static boolean isValid(ItemStack out) {
        return !out.getItem().getClass().getName().startsWith("reika.rotarycraft.items");
    }

}
