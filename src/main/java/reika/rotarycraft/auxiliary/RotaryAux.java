/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;
import reika.dragonapi.instantiable.data.collections.OneWayCollections;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaEngLibrary;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.EnvironmentalHeatSource;
import reika.rotarycraft.api.power.ShaftMachine;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;

import reika.rotarycraft.blockentities.transmission.BlockEntitySplitter;
import reika.rotarycraft.registry.*;

import java.awt.*;
import java.util.Set;

public class RotaryAux {

    public static final Color[] sideColors = {Color.CYAN, Color.BLUE, Color.YELLOW, Color.BLACK, new Color(255, 120, 0), Color.MAGENTA};
    public static final String[] sideColorNames = {"CYAN", "BLUE", "YELLOW", "BLACK", "ORANGE", "MAGENTA"};
    public static final boolean getPowerOnClient = ConfigRegistry.POWERCLIENT.getState();// || ReikaObfuscationHelper.isDeObfEnvironment();
    public static final double tungstenDensity = ReikaEngLibrary.rhoiron * 0.8 + 0.2 * ReikaEngLibrary.rhotungsten;
    private static final Set<Class<? extends BlockEntity>> shaftPowerBlacklist = new OneWayCollections.OneWaySet<>();
    public static int blockModel;

    public static boolean isBlacklistedIOMachine(BlockEntity te) {
        return shaftPowerBlacklist.contains(te.getClass());
    }

    private static void addShaftBlacklist(String name) {
        Class cl;
        try {
            cl = Class.forName(name);
            shaftPowerBlacklist.add(cl);
            RotaryCraft.LOGGER.info("Disabling " + name + " for shaft power. Destructive compatibility.");
        } catch (ClassNotFoundException ignored) {

        }
    }

    public static boolean hasGui(Level world, BlockPos pos, Player ep) {
        MachineRegistry m = MachineRegistry.getMachine(world, pos);
        if (m == MachineRegistry.WIND_ENGINE || m == MachineRegistry.STEAM_ENGINE || m == MachineRegistry.PERFORMANCE_ENGINE || m == MachineRegistry.MICRO_TURBINE || m == MachineRegistry.GAS_ENGINE || m == MachineRegistry.DC_ENGINE || m == MachineRegistry.AC_ENGINE /*todo add other engines when added*/) {
            BlockEntityEngine te = (BlockEntityEngine) world.getBlockEntity(pos);
            if (te == null)
                return false;
            if (te.getEngineType() == null)
                return false;
            return te.getEngineType().hasGui();
        }
        /*if (m == MachineRegistry.SPLITTER) {
            BlockEntitySplitter te = (BlockEntitySplitter)world.getBlockEntity(pos);
            return (te.getBlockMetadata() >= 8);
        }*/
//        if (m == MachineRegistry.SCREEN)
//            return !ep.isCrouching();
        return m.hasGui();
    }

    public static boolean canHarvestSteelMachine(Player ep) {
        if (ep.isCreative())
            return false;
        ItemStack eitem = ep.getInventory().getSelected();
        return eitem != null;
//        if (TinkerToolHandler.getInstance().isHammer(eitem))
//            return false;
//        if (TinkerToolHandler.getInstance().isPick(eitem) && TinkerToolHandler.getInstance().isStoneOrBetter(eitem))
//            return true;
//        if (MekToolHandler.getInstance().isPickTypeTool(eitem) && !MekToolHandler.getInstance().isWood(eitem))
//            return true;
//        if (eitem.getItem() == RedstoneArsenalHandler.getInstance().pickID) {
//            return RedstoneArsenalHandler.getInstance().pickLevel > 0;
//        }
//        if (!(eitem.getItem() instanceof PickaxeItem))
//        	return false;
//        return eitem.getItem().canHarvestBlock(Blocks.IRON_ORE, eitem);
//todo canHarvestBlock
    }

    public static boolean shouldSetFlipped(Level world, BlockPos pos) {
        boolean softBelow = ReikaWorldHelper.softBlocks(world, pos.below());
        boolean softAbove = ReikaWorldHelper.softBlocks(world, pos.above());
        return !softAbove && softBelow;
    }

    public static boolean isMuffled(BlockEntity te) {
        return isMuffled(te.getLevel(), te.getBlockPos());
    }

    public static boolean isMuffled(Level world, BlockPos pos) {
        return isMufflingBlock(world, pos.above()) && isMufflingBlock(world, pos.below());
    }

    public static boolean isMufflingBlock(Level world, BlockPos pos) {
        BlockState b = world.getBlockState(pos);
        return b.getMaterial() == Material.WOOL;//  || b == todo Block.getBlockFromName("Rockwool");
    }

    public static boolean isNextToIce(Level world, BlockPos pos) {
        if (ReikaWorldHelper.checkForAdjMaterial(world, pos, Material.ICE) != null)
            return true;
        Block b = world.getBlockState(pos.below()).getBlock();
        if (b instanceof EnvironmentalHeatSource) {
            EnvironmentalHeatSource ehs = (EnvironmentalHeatSource) b;
            return ehs.isActive(world, pos) && ehs.getSourceType(world, pos).isCold();
        }
        return false;
    }

    public static boolean isNextToWater(Level world, BlockPos pos) {
        if (ReikaWorldHelper.checkForAdjMaterial(world, pos, Material.WATER) != null)
            return true;
        for (int i = 1; i <= 2; i++) {
            Block b = world.getBlockState(new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())).getBlock();
            if (b instanceof EnvironmentalHeatSource) {
                EnvironmentalHeatSource ehs = (EnvironmentalHeatSource) b;
                return ehs.isActive(world, new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())) && ehs.getSourceType(world, new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())) == EnvironmentalHeatSource.SourceType.WATER;
            }
        }
        return false;
    }

    public static boolean isNextToFire(Level world, BlockPos pos) {
        if (ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.FIRE) != null)
            return true;
        for (int i = 1; i <= 2; i++) {
            Block b = world.getBlockState(new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())).getBlock();
            if (b instanceof EnvironmentalHeatSource) {
                EnvironmentalHeatSource ehs = (EnvironmentalHeatSource) b;
                return ehs.isActive(world, new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())) && ehs.getSourceType(world, new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())) == EnvironmentalHeatSource.SourceType.FIRE;
            }
        }
        return false;
    }

    public static boolean isNextToLava(Level world, BlockPos pos) {
        if (ReikaWorldHelper.checkForAdjMaterial(world, pos, Material.LAVA) != null)
            return true;
        for (int i = 1; i <= 2; i++) {
            Block b = world.getBlockState(new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())).getBlock();
            if (b instanceof EnvironmentalHeatSource) {
                EnvironmentalHeatSource ehs = (EnvironmentalHeatSource) b;
                return ehs.isActive(world, new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())) && ehs.getSourceType(world, new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())) == EnvironmentalHeatSource.SourceType.LAVA;
            }
        }
        return false;
    }

    public static boolean isAboveFire(Level world, BlockPos pos) {
        Block b = world.getBlockState(pos.below()).getBlock();
        if (b == Blocks.FIRE)
            return true;
        for (int i = 1; i <= 2; i++) {
            b = world.getBlockState(new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())).getBlock();
            if (b instanceof EnvironmentalHeatSource) {
                EnvironmentalHeatSource ehs = (EnvironmentalHeatSource) b;
                return ehs.isActive(world, new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())) && ehs.getSourceType(world, new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())) == EnvironmentalHeatSource.SourceType.FIRE;
            }
        }
        return false;
    }

    public static boolean isAboveLava(Level world, BlockPos pos) {
        Block b = world.getBlockState(pos.below()).getBlock();
        if (b.defaultBlockState().getMaterial() == Material.LAVA)
            return true;
        for (int i = 1; i <= 2; i++) {
            b = world.getBlockState(new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())).getBlock();
            if (b instanceof EnvironmentalHeatSource) {
                EnvironmentalHeatSource ehs = (EnvironmentalHeatSource) b;
                return ehs.isActive(world, new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())) && ehs.getSourceType(world, new BlockPos(pos.getX(), pos.getY() - i, pos.getZ())) == EnvironmentalHeatSource.SourceType.LAVA;
            }
        }
        return false;
    }

    public static String formatTemperature(double temp) {
        String unit = "C";
//        if (config for units in F) {
//            unit = "F";
//            temp = temp * 1.8 + 32;
//        }
        return String.format("%.0f%s", temp, unit);
    }

    public static String formatPressure(double press) {
        String unit = "Pa";
//        if (config for units in psi, and another for bar) {
        unit = "bar";
        press /= 10130;
//            unit = "psi";
//            press *= 0.000145;
//        }
        double val = ReikaMathLibrary.getThousandBase(press);
        String sg = ReikaEngLibrary.getSIPrefix(press);
        return String.format("%.3f%s%s", val, sg, unit);
    }

    public static String formatTorque(double t) {
        String unit = "Nm";
        double val = ReikaMathLibrary.getThousandBase(t);
        String sg = ReikaEngLibrary.getSIPrefix(t);
        return String.format("%.0f %s%s", val, sg, unit);
    }

    public static String formatSpeed(double s) {
        String unit = "rad/s";
//        if (config for units in rpm) {
//            unit = "rpm";
//            s *= 9.55;
//        }
        double val = ReikaMathLibrary.getThousandBase(s);
        String sg = ReikaEngLibrary.getSIPrefix(s);
        return String.format("%.0f %s%s", val, sg, unit);
    }

    public static String formatPower(double p) {
        String unit = "W";
//        if (config for units in hp) {
//            unit = "hp";
//            p /= 745.7;
//        }
        double val = ReikaMathLibrary.getThousandBase(p);
        String sg = ReikaEngLibrary.getSIPrefix(p);
        return String.format("%.3f%s%s", val, sg, unit);
    }

    public static String formatEnergy(double e) {
        String unit = "J";
        double val = ReikaMathLibrary.getThousandBase(e);
        String sg = ReikaEngLibrary.getSIPrefix(e);
        return String.format("%.3f%s%s", val, sg, unit);
    }

    public static String formatPowerIO(BlockEntityIOMachine te) {
        return formatPowerIO(te.omega, te.power);
    }

    public static String formatPowerIO(ShaftMachine te) {
        return formatPowerIO(te.getOmega(), te.getPower());
    }

    public static String formatPowerIO(double speed, double power) {
        String unit1 = "W";
        String unit2 = "rad/s";
//        if (config for units in hp, and another for rpm) {
//            unit1 = "hp";
//            power /= 745.7;
//            unit2 = "rpm";
//            speed *= 9.55;
//        }
        double valp = ReikaMathLibrary.getThousandBase(power);
        String sgp = ReikaEngLibrary.getSIPrefix(power);
        return String.format("%.3f%s%s @ %.0f %s", valp, sgp, unit1, speed, unit2);
    }

    public static String formatTorqueSpeedPowerForBook(String text, double torque, double speed, double power) {
//        boolean old = OldTextureLoader.instance.loadOldTextures();
        String powerunit = /*old ? "hp" :*/ "W";
        String torqueunit = /*old ? "ft-lb" :*/ "Nm";
        String speedunit = /*old ? "rpm" :*/ "rad/s";
//        if (old) {
//            speed *= 9.55;
//            torque *= 0.738;
//            power /= 745.7;
//        } else {
        //speedunit = ReikaEngLibrary.getSIPrefix(speed)+speedunit;
        //torqueunit = ReikaEngLibrary.getSIPrefix(torque)+torqueunit;
        powerunit = ReikaEngLibrary.getSIPrefix(power) + powerunit;
        //speed = ReikaMathLibrary.getThousandBase(speed);
        //torque = ReikaMathLibrary.getThousandBase(torque);
        power = ReikaMathLibrary.getThousandBase(power);
//        }
        text = text.replace("$SPEED_UNIT$", speedunit);
        text = text.replace("$POWER_UNIT$", powerunit);
        text = text.replace("$TORQUE_UNIT$", torqueunit);
        String ret = String.format(text, (int) torque, (int) speed, power);
        return ret;
    }

    public static String formatValuesForBook(String text, Object[] vals) {
        return String.format(text, vals);
    }

    public static String formatDistance(double dist) {
        String unit = "m";
//        if (config for distance in feet, might be dumb to have though, as each block is 1m) {
//            unit = "ft";
//            dist *= 3.2808;
//        }
        double val = ReikaMathLibrary.getThousandBase(dist);
        String sg = ReikaEngLibrary.getSIPrefix(dist);
        return String.format("%.3f%s%s", val, sg, unit);
    }

    public static String formatLiquidAmount(double amt) {
        String unit = "mB";
//        if (OldTextureLoader.instance.loadOldTextures()) {
//            amt *= 0.264;
//            unit = "gal";
//        }
        return String.format("%.0f%s", amt, unit);
    }

    public static String formatLiquidAmountWithSI(double amt) {
        String unit = "B";
        double val = ReikaMathLibrary.getThousandBase(amt);
        String sg = ReikaEngLibrary.getSIPrefix(amt);
        return String.format("%.3f%s%s", val, sg, unit);
    }

    public static String formatLiquidFillFraction(double amt, double capacity) {
        String unit = "mB";
        return String.format("%.0f/%.0f %s", amt, capacity, unit);
    }

    @Deprecated(forRemoval = true)
    //not needed anymore as theres a new registry for each type of shaft
    public static ItemStack getShaftCrossItem() {
        ItemStack is = new ItemStack(RotaryBlocks.SHAFT_CROSS.get());
        //is.getOrCreateTag().putBoolean("cross", true);
        return is;
    }

    public static boolean isHoldingScrewdriver(Player ep) {
        ItemStack is = ep.getUseItem();
//        if (ModList.THAUMCRAFT.isLoaded() && isScrewFocusWand(is))
//            return true;
        return RotaryItems.SCREWDRIVER.get().getDefaultInstance() == is;
    }



}
