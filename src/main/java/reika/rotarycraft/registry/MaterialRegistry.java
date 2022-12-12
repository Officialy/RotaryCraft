/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.registry;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import reika.dragonapi.libraries.mathsci.ReikaEngLibrary;
import reika.rotarycraft.auxiliary.RotaryAux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public enum MaterialRegistry {

    WOOD(-1, ReikaEngLibrary.Ewood, ReikaEngLibrary.Gwood, ReikaEngLibrary.Twood, ReikaEngLibrary.Swood, ReikaEngLibrary.rhowood),
    STONE(0, ReikaEngLibrary.Estone, ReikaEngLibrary.Gstone, ReikaEngLibrary.Tstone, ReikaEngLibrary.Sstone, ReikaEngLibrary.rhorock),
    STEEL(1, ReikaEngLibrary.Esteel, ReikaEngLibrary.Gsteel, ReikaEngLibrary.Tsteel, ReikaEngLibrary.Ssteel, ReikaEngLibrary.rhoiron),
    TUNGSTEN(1, ReikaEngLibrary.Etungsten, ReikaEngLibrary.Gtungsten, ReikaEngLibrary.Ttungsten, ReikaEngLibrary.Stungsten, RotaryAux.tungstenDensity),
    DIAMOND(2, ReikaEngLibrary.Ediamond, ReikaEngLibrary.Gdiamond, ReikaEngLibrary.Tdiamond, ReikaEngLibrary.Sdiamond, ReikaEngLibrary.rhodiamond),
    BEDROCK(3, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, ReikaEngLibrary.rhorock);

    public static final MaterialRegistry[] matList = values();
    public final int harvestLevel;
    private final double Emod;
    private final double Smod;
    private final double tensile;
    private final double shear;
    private final double rho;
    private LoadData limits;

    MaterialRegistry(int h, double E, double G, double TS, double S, double den) {
        Emod = E;
        Smod = G;
        tensile = TS;
        shear = S;
        rho = den;
        harvestLevel = h;
    }

    public static int[] getAllLimitLoadsAsInts() {
        ArrayList<LoadData> li = getAllLimitLoads();
        int[] ret = new int[li.size() * 2];
        for (int i = 0; i < li.size(); i++) {
            LoadData l = li.get(i);
            ret[i * 2] = (int) l.maxTorque;
            ret[i * 2 + 1] = (int) l.maxSpeed;
        }
        return ret;
    }

    public static ArrayList<LoadData> getAllLimitLoads() {
        ArrayList<LoadData> li = new ArrayList<>();
        for (MaterialRegistry m : MaterialRegistry.matList) {
            if (m.isInfiniteStrength())
                continue;
            li.add(m.getLimits());
        }
        Collections.sort(li);
        return li;
    }

    public static MaterialRegistry getMaterialFromShaftItem(ItemStack is) {
        if (is.getTag() != null && is.getTag().contains("material"))
            return MaterialRegistry.valueOf(is.getTag().getString("material"));
        return MaterialRegistry.matList[is.getDamageValue()];
    }

    public double getElasticModulus() {
        return Emod;
    }

    public double getShearModulus() {
        return Smod;
    }

    public double getTensileStrength() {
        return tensile;
    }

    public double getShearStrength() {
        return shear;
    }

    public double getDensity() {
        return rho;
    }

    public boolean isInfiniteStrength() {
        return this == BEDROCK;
    }

    public boolean isFlammable() {
        return this == WOOD;
    }

    public SoundEvent getDamageNoise() {
        if (this == WOOD)
            return SoundEvents.WOOD_BREAK;
        if (this == STONE)
            return SoundEvents.STONE_BREAK;
        return SoundEvents.BLAZE_HURT;
    }


    public String getBaseShaftTexture() {
        String tex = "shafttex";
        switch (this) {
            case BEDROCK -> tex = tex + "b";
            case DIAMOND -> tex = tex + "d";
            case STONE -> tex = tex + "s";
            case TUNGSTEN -> tex = tex + "t";
            case WOOD -> tex = tex + "w";
            default -> {
            }
        }
        return tex + ".png";
    }

    public boolean isHarvestablePickaxe(ItemStack tool) {
        if (harvestLevel < 0)
            return true;
        if (tool == null)
            return false;
        Item item = tool.getItem();
        //if (item == RotaryItems.BEDPICK.get())
        //    return true;
        if (item == RotaryItems.HSLA_STEEL_PICKAXE.get())
            return this != BEDROCK;
        /*if (item instanceof PickaxeItem) {
            switch (this) {
                case STONE:
                    return true;
                case STEEL:
                    return item.canHarvestBlock(Blocks.IRON_ORE, tool);
                case TUNGSTEN:
                case DIAMOND:
                    return item.canHarvestBlock(Blocks.DIAMOND_ORE, tool);
                case BEDROCK:
                    return item.canHarvestBlock(Blocks.OBSIDIAN, tool);
                default:
                    return false;
            }
        }
        //if (item == RedstoneArsenalHandler.getInstance().pickID) {
        //    return RedstoneArsenalHandler.getInstance().pickLevel >= harvestLevel;
        //}
        switch (this) {
            case STONE:
                return item.canHarvestBlock(Blocks.STONE, tool);
            case STEEL:
                return item.canHarvestBlock(Blocks.IRON_ORE, tool);
            case TUNGSTEN:
            case DIAMOND:
                return item.canHarvestBlock(Blocks.DIAMOND_ORE, tool);
            case BEDROCK:
                return item.canHarvestBlock(Blocks.OBSIDIAN, tool);
            default:
                break;
        }*/
        return false;
    }

    public String getName() {
        return I18n.get("material." + this.name().toLowerCase(Locale.ENGLISH));
    }

    private double getMaxShaftTorque() {
        if (this.isInfiniteStrength())
            return Double.POSITIVE_INFINITY;
        double r = 0.0625;
        double tau = this.getShearStrength();
        return 0.5 * Math.PI * r * r * r * tau / 16D;
    }

    private double getMaxShaftSpeed() {
        if (this.isInfiniteStrength())
            return Double.POSITIVE_INFINITY;
        double f = 1D / this.getSpeedForceExponent();
        double rho = this.getDensity();
        double r = 0.0625;
        double sigma = this.getTensileStrength();
        double base = Math.sqrt(2 * sigma / (rho * r * r));
        return Math.pow(base, f);
    }

    public LoadData getLimits() {
        if (limits == null)
            limits = new LoadData();
        return limits;
    }
/*
    public ItemStack getShaftItem() {
        //return MachineRegistry.SHAFT.getCraftedMetadataProduct(this.INDEX());
        ItemStack is = new ItemStack(MachineRegistry.SHAFT.getBlock().getBlock());
        is.getOrCreateTag().putString("material", this.name());
        return is;
    }*/

    public double getSpeedForceExponent() {
        switch (this) {
            case WOOD:
            case STONE:
            case STEEL:
                return 1 - (0.11D * this.ordinal());
            //case TUNGSTEN:
            //    return 0.70;
            case DIAMOND:
                return 0.67;
            default:
                return 1;
        }
    }

    public ItemStack getShaftUnitItem() {
        if (this == WOOD)
            return new ItemStack(Items.STICK);
        //return RotaryItems.GEARCRAFT.get().getDefaultInstance(); //.get(GearboxTypes.getFromMaterial(this));
        return RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance();
    }

    public Object getMountItem() {
        //return GearboxTypes.getFromMaterial(this).getMountItem();
        return null;
    }

    public String getShaftUnlocName() {
        return "material." + this.name().toLowerCase(Locale.ENGLISH);
    }

    public class LoadData implements Comparable<LoadData> {

        public final double maxSpeed;
        public final double maxTorque;

        private LoadData() {
            maxSpeed = this.getOwner().getMaxShaftSpeed();
            maxTorque = this.getOwner().getMaxShaftTorque();
        }

        @Override
        public int compareTo(LoadData o) {
            return this.getOwner().compareTo(o.getOwner());
        }

        private MaterialRegistry getOwner() {
            return MaterialRegistry.this;
        }

    }
}
