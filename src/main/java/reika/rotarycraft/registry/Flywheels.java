package reika.rotarycraft.registry;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import reika.dragonapi.libraries.mathsci.ReikaEngLibrary;
import reika.rotarycraft.auxiliary.RotaryAux;

import java.util.Locale;

public enum Flywheels {
    WOOD(16, 2, ReikaEngLibrary.rhowood, ReikaEngLibrary.Twood),        // rho 0.8	-> 1	-> 1
    STONE(128, 5, ReikaEngLibrary.rhorock, 0.9 * ReikaEngLibrary.Tstone),        // rho 3	-> 4	-> 8
    IRON(512, 15, ReikaEngLibrary.rhoiron, 5 * ReikaEngLibrary.Tiron),        // rho 8	-> 8	-> 32
    GOLD(4096, 40, ReikaEngLibrary.rhogold, ReikaEngLibrary.Tgold),    // rho 19.3	-> 32	-> 256
    TUNGSTEN(8192, 25, RotaryAux.tungstenDensity, 8 * ReikaEngLibrary.Ttungsten),
    DEPLETEDU(4096, 40, ReikaEngLibrary.rhouranium, 6 * ReikaEngLibrary.Turanium),
    BEDROCK(Integer.MAX_VALUE, 200, 1.75 * ReikaEngLibrary.rhoiron, Double.POSITIVE_INFINITY);

    public static final Flywheels[] list = values();
    public final int maxTorque;
    public final int maxSpeed;
    public final int decayTime;
    public final double density;
    public final double tensileStrength;

    Flywheels(int t, int dec, double rho, double str) {
        maxTorque = t;
        tensileStrength = str;
        decayTime = dec;
        density = rho;
        maxSpeed = this.getLimitLoad();
    }

    public static int[] getSpeedLimits() {
        int[] ret = new int[list.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = list[i].maxSpeed;
        }
        return ret;
    }

    public static String getLimitsForDisplay() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.length; i++) {
            Flywheels set = list[i];
            sb.append(String.format("%s: %d rad/s", set.getName(), set.maxSpeed));
            if (i < list.length - 1)
                sb.append("\n");
        }
        return sb.toString();
    }

//    public static Flywheels getMaterialFromFlywheelItem(ItemStack is) {
//        int idx = is.getItemDamage();
//        if (idx < 0 || idx >= list.length) { //completely invalid
//            is.setItemDamage(0);
//            is.func_150996_a(Item.byBlock(Blocks.STONE));
//            return WOOD;
//        }
//        return list[idx];
//    }

    private int getLimitLoad() {
        double r = 0.75;
        double s = 100 * tensileStrength;
        double frac = 2 * s / (density * r * r);
        double base = Math.sqrt(frac);
        return (int) base;
    }

    public int getMinTorque() {
        //return this == BEDROCK ? 16384 : maxTorque / BlockEntityFlywheel.MINTORQUERATIO;
        return this == BEDROCK ? 16384 : maxTorque / 4; //todo fix this bitch
    }

    public ItemStack getFlywheelItem() {
        //return MachineRegistry.FLYWHEEL.getBlock().getBlock().asItem().getDefaultInstance();
        return RotaryItems.IRON_FLYWHEEL_CORE.get().getDefaultInstance();
    }

    public String getName() {
        return "flywheel." + this.name().toLowerCase(Locale.ROOT);
    }

    public Object getRawMaterial() {
        return switch (this) {
            case GOLD -> Items.GOLD_INGOT;
            case IRON -> Items.IRON_INGOT;
            case STONE -> Blocks.STONE;
            case TUNGSTEN -> RotaryItems.TUNGSTEN_ALLOY_SPRING;
            case WOOD -> "plankWood";
            case BEDROCK -> RotaryItems.BEDROCK_ALLOY_INGOT;
            case DEPLETEDU -> "depletedUranium";
        };
    }
}
