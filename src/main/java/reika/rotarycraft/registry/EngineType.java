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

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import reika.dragonapi.exception.RegistrationException;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.blockentities.engine.*;

public enum EngineType {
    DC(65536, 1024, EngineClass.ELECTRIC, BlockEntityDCEngine.class),
    WIND(1024, 8, EngineClass.KINETIC, BlockEntityWindEngine.class),
    STEAM(512, 32, EngineClass.THERMAL, BlockEntitySteamEngine.class),
    GAS(512, 128, EngineClass.PISTON, BlockEntityGasEngine.class),
    AC(256, 512, EngineClass.ELECTRIC, BlockEntityACEngine.class),
    SPORT(1024, 256, EngineClass.PISTON, BlockEntityPerformanceEngine.class),
    //HYDRO(32, 16384, EngineClass.KINETIC, BlockEntityHydroEngine.class), //double speed, add new lava engine as 524kW? no
    MICRO(131072, 16, EngineClass.TURBINE, BlockEntityMicroturbine.class),
    ;
//  JET(65536, 1024, EngineClass.TURBINE, BlockEntityJetEngine.class);

    public static final EngineType[] engineList = values();
    public final Class<? extends BlockEntityEngine> engineClass;
    public final EngineClass type;
    /**
     * Standard Motor TorqueSpeeds:
     * DC Engine = 1-4Nm @ 1600-2400 rpm (168 - 251 rad/s) 			-> 0.672kW - 1.004kW
     * Steam Engine 40-50Nm @ 5000 rpm (524 rad/s)					-> 20.96kW - 26.2kW (best)
     * Standard Combustion = 100Nm @ 5500-7000 rpm (576 - 733 rad/s)-> 57.6kW - 73.3kW (Standard car)
     * AC Engine = ~300Nm @ max 3600 rpm (377 rad/s)				-> 113.1 kW
     * Sport Combustion = 200Nm @ 9400-10600 rpm (984 - 1110 rad/s) -> 196.8kW - 222kW (sports car)
     * Microturbine 5Nm @ 200 krpm (52356 rad/s)					-> 1MW
     * Gas Turbine 700-1400 Nm @ 50-100 krpm (5236 - 10471 rad/s)	-> 70 MW (Boeing 767 engines)
     *
     * @author Reika
     */

    private final int torque;
    private final int omega;

    EngineType(int rpm, int torque, EngineClass type, Class<? extends BlockEntityEngine> c) {
        omega = rpm;
        this.torque = torque;
        this.type = type;
        engineClass = c;
    }

    public static EngineType setType(int type) {
        return EngineType.values()[type];
    }

    public int getSpeed() {
        return omega;
    }

    public int getTorque() {
        return torque;
    }

    public long getPower() {
        return (long) torque * (long) omega;
    }


    public double getPowerKW() {
        return this.getPower() / 1000D;
    }

    public double getPowerMW() {
        return this.getPower() / 1000000D;
    }

    public String getStringPowerMW() {
        return String.format("%.3f", (torque * omega) / 1000000D);
    }

    public double getPowerForDisplay() {
        if (this.getPower() < 1000)
            return this.getPower();
        else if (this.getPower() < 1000000)
            return this.getPowerKW();
        return this.getPowerMW();
    }

    public boolean isJetFueled() {
        return /*this == JET || */this == MICRO;
    }

    public boolean isEthanolFueled() {
        return this == GAS || this == SPORT;
    }

    public boolean isWaterPiped() {
        return this == STEAM || this == SPORT;
    }

    public boolean hasGui() {
        return this == STEAM || this == GAS ||/* this == AC ||*/ this == SPORT || this == MICRO;// || this == JET;
    }

    public boolean burnsFuel() {
        return this == STEAM || this == GAS || this == SPORT || this == MICRO; //|| this == JET;
    }

    public int getSoundLength() {
        if (this.carNoise()) {
            return 88;
        }
        if (this.electricNoise()) {
            return 74;
        }
        if (this.steamNoise()) {
            return 49;
        }
//        if (this.waterNoise()) {
//            return 59;
//        }
        if (this.windNoise()) {
            return 105;
        }
//        if (this.jetNoise()) {
//            return 79;
//        }
        if (this.turbineNoise()) {
            return 20;
        }
        return 0;
    }

    public boolean isCooled() {
        return this == STEAM || this == SPORT;
    }

    public boolean isAirBreathing() {
        return this == GAS || this == SPORT || this == MICRO;// || this == JET;
    }

    public boolean electricNoise() {
        return type == EngineClass.ELECTRIC;
    }

    public boolean carNoise() {
        return this == GAS || this == SPORT;
    }

//    public boolean waterNoise() {
//        return this == HYDRO;
//    }

    public boolean steamNoise() {
        return this == STEAM;
    }

//    public boolean jetNoise() {
//        return this == JET;
//    }

    public boolean turbineNoise() {
        return /*this == JET || */this == MICRO;
    }

    public boolean windNoise() {
        return this == WIND;
    }

    public boolean canHurtPlayer() {
//        if (this == JET)
//            return true;
        if (this == SPORT)
            return true;
        return this == WIND;// this == HYDRO;
    }

    public boolean isValidFuel(ItemStack is) {
        if (this == STEAM)
            return is.getItem() == Items.WATER_BUCKET;
        if (this == GAS)
            return is.getItem() == RotaryItems.ETHANOL.get();
        if (this == SPORT)
            return is.getItem() == RotaryItems.ETHANOL.get() || this.isAdditive(is);
        //if (this == AC)
        //	return ReikaItemHelper.matchStacks(is, RotaryItems.HSLA_SHAFT_CORE) || ReikaItemHelper.matchStacks(is, RotaryItems.tungstenshaftcore);
        return false;
    }

    public boolean isAdditive(ItemStack is) {
        Item i = is.getItem();
        if (this == SPORT)
            return i == Items.REDSTONE || i == Items.GUNPOWDER || i == Items.BLAZE_POWDER;
        return false;
    }

    /**
     * Returns ticks
     */
    public int getFuelUnitDuration() {
        return switch (this) {
            case STEAM -> 18;
            case GAS -> 12;
            //case AC -> 600;
            case SPORT -> 6;
            case MICRO -> 48;
            //case JET -> 2;
            default -> 0;
        };
    }

    public ItemStack getCraftedProduct() {
        switch (this) {
            case DC -> RotaryBlocks.DC_ENGINE.get().asItem();
            case WIND -> RotaryBlocks.WIND_ENGINE.get().asItem();
            case STEAM -> RotaryBlocks.STEAM_ENGINE.get().asItem();
            case GAS -> RotaryBlocks.GAS_ENGINE.get().asItem();
            case AC -> RotaryBlocks.AC_ENGINE.get().asItem();
            case SPORT -> RotaryBlocks.PERFORMANCE_ENGINE.get().asItem();
//          case HYDRO -> RotaryBlocks.HYDROKINETIC_ENGINE.get().asItem();
            case MICRO -> RotaryBlocks.MICRO_TURBINE.get().asItem();
//          case JET -> RotaryBlocks.GAS_TURBINE.get().asItem();
        }
        return ItemStack.EMPTY;
    }

    public boolean isEMPImmune() {
        return /*this == HYDRO ||*/ this == WIND;
    }

    public boolean isECUControllable() {
        return type.isECUControllable();
    }


    public boolean canReceiveFluid(Fluid fluid) {
        switch (this) {
            case STEAM -> {
                if (fluid.equals(Fluids.WATER))
                    return true;
            }
            case GAS -> {
                if (fluid.equals(RotaryFluids.ETHANOL.get()))
                    return true;
            }
            case SPORT -> {
                if (fluid.equals(Fluids.WATER))
                    return true;
                if (fluid.equals(RotaryFluids.ETHANOL.get()))
                    return true;
            }
               /* case HYDRO -> {
                if (fluid.equals(RotaryFluids.LUBRICANT.get()))
                    return true;
                }*/
            case MICRO/*, JET*/ -> {
                if (fluid.equals(RotaryFluids.JET_FUEL.get()))
                    return true;
            }
            default -> {
                return false;
            }
        }
        return false;
    }

    public Fluid getFuelType() {
        return switch (this) {
            case GAS, SPORT -> RotaryFluids.ETHANOL.get();
            case MICRO/*, JET*/ -> RotaryFluids.JET_FUEL.get();
            default -> null;
        };
    }

    public boolean usesFluids() {
        return this.burnsFuel() || this.isWaterPiped();// || this.requiresLubricant();
    }

    public boolean requiresLubricant() {
        return false;// this == HYDRO;
    }

    public int getContainerSize() {
        switch (this) {
            case STEAM:
            case GAS:
                //case AC:
                //    return 1;
            case SPORT:
                return 2;
            default:
                return 0;
        }
    }

    public boolean allowInventoryStacking() {
        switch (this) {
            case GAS:
            case SPORT:
                return true;
            default:
                return false;
        }
    }

    public boolean usesAdditives() {
        return this == SPORT;
    }

    public boolean hasInventory() {
        return this.getContainerSize() > 0;
    }

    public boolean needsWater() {
        return this == STEAM || this == SPORT;
    }

    public BlockEntityEngine newBlockEntity() {
        try {
            return engineClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RegistrationException(RotaryCraft.getInstance(), "Engine type " + this + " has a noninstantiable class!");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RegistrationException(RotaryCraft.getInstance(), "Engine type " + this + " has an inaccessible class!");
        }
    }

    public enum EngineClass {
        KINETIC(),
        THERMAL(),
        ELECTRIC(),
        PISTON(),
        TURBINE();

        public boolean isECUControllable() {
            return this == PISTON || this == TURBINE;
        }
    }
}
