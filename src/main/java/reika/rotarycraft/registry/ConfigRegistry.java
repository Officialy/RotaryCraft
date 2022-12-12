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

import net.minecraft.util.Mth;
import reika.dragonapi.ModList;
import reika.dragonapi.auxiliary.EnumDifficulty;
import reika.dragonapi.interfaces.configuration.BooleanConfig;
import reika.dragonapi.interfaces.configuration.DecimalConfig;
import reika.dragonapi.interfaces.configuration.IntegerConfig;
import reika.dragonapi.interfaces.configuration.MatchingConfig;
import reika.dragonapi.interfaces.configuration.SegmentedConfigList;
import reika.dragonapi.interfaces.configuration.SelectiveConfig;
import reika.dragonapi.interfaces.configuration.StringConfig;
import reika.dragonapi.interfaces.configuration.UserSpecificConfig;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;


public enum ConfigRegistry implements SegmentedConfigList, SelectiveConfig, IntegerConfig, BooleanConfig, DecimalConfig, StringConfig, MatchingConfig, UserSpecificConfig {

    ENGINEVOLUME("Engine Volume", 1F),
    GPRORES("GPR Renders Ores", true),
    INSTACUT("Instant Woodcutter", true),
    CRAFTABLEBEDROCK("Allow Craftable Bedrock", true),
    LOCKMACHINES("Owner-Only Machine Use", false),
    MACHINEVOLUME("Machine Volume Multiplier", 1.0F),
    FLOODLIGHTRANGE("Max Floodlight Range", 128),
    HEATRAYRANGE("Max Heat Ray Range", 128),
    BRIDGERANGE("Max Bridge Range", 128),
    FANRANGE("Max Fan Range", 128),
    AERORANGE("Max Aerosolizer Range", 128),
    VACUUMRANGE("Max Vacuum Range", 128),
    //SONICRANGE("Max Sonic Weapon Range", 128),
    FORCERANGE("Max Force Field Range", 128),
    SONICBORERRANGE("Sonic Borer Range", 512),
    SPAWNERLIMIT("Spawner Mob Limit", 128),
    DETECTORRANGE("Player Detector Range", 128),
    BREEDERRANGE("Breeder Range", 128),
    BAITRANGE("Bait Box Range", 24),
    LINEBUILDER("Block Ram Range", 512),
    BAITMOBS("Max Bait Box Mob Count", 256),
    CAVEFINDERRANGE("Cave Scanner FOV", 16),
    BANRAIN("Disable Silver Iodide Cannon Rain", false),
    ACHIEVEMENTS("Enable Achievements", true),
    MODORES("Force Inter-Mod Ore Compatibility", true),
    BEDPICKSPAWNERS("Allow Bedrock Pickaxe to Harvest Spawners", true),
    SPAWNERLEAK("Spawn Mobs When Harvesting Spawners By Hand", true),
    BLOCKDAMAGE("Direct Block Damage from Machine Failures", true),
    DIFFICULTY("Difficulty Control", 2),
    ALARM("Machine Warning Alarms", false),
    BIOMEBLOCKS("Terraformer Block Editing", true),
    DYNAMICHANDBOOK("Reload Handbook Data on Open", false),
    TABLEMACHINES("Crafting Table can Make Machines", false),
    EMPLOAD("EMP Charging Speed", 4),
    ROTATEHOSE("Rotate Hose/Pipe/Fuel Line Recipes", false),
    RAILGUNDAMAGE("Railgun Block Damage", true),
    GRAVELPLAYER("Allow Gravel Gun PvP", true),
    CHESTGEN("Chest Generation Tier", 4),
    //HOSTILECRASH("Crash on hostile interference from other mods", true),
    PROJECTORLINES("Render projector lines", true),
    COLORBLIND("Color Blind Mode", false),
    TURRETPLAYERS("Turrets can target players", true),
    HSLADICT("Allow RC steel to be used in other mods", false),
    PREENCHANT("Lock enchants on bedrock tools", true),
    //EXPLODEPACK("Explode jetpack if player is in lava", true),
    SPRINKLER("Sprinkler Particle Density", 4),
    HANDBOOK("Spawn with RC Handbook", true),
    CONSERVEPACK("Conservative Jetpack Firing", true),
    ALLOWBAN("Allow Build Blocking of Some Machines", false),
    LOGBLOCKS("Log Block Placement and Removal", false),
    //PACKETDELAY("Sync Packet Interval in Ticks", 1),
    FLOWSPEED("Fluid Flow Speed", 5),
    ATTACKBLOCKS("Block Damage from Destructive Machines", true),
    VOIDHOLE("Allow Bedrock Breaker to Break Y=0", false),
    JETFUELPACK("Jetpack Requires Jet Fuel", false),
    ALLOWTNTCANNON("Allow TNT Cannon", true),
    ALLOWEMP("Allow EMP", true),
    EXTRAIRON("Iron Ore Density", 1F),
    TEGLASS("Allow Blast Glass to be Used as TE Hardened Glass", false),
    CLEARCHAT("Tools Clear Chat", true),
    KICKFLYING("Jetpack bypasses allow-flight property", true),
    BLOWERSPILL("Item Pump Spills Items If Dumping To Air", true),
    EXTRACTORMAINTAIN("Extractor Drill Wears Down", false),
    HARDGRAVELGUN("Hardmode Gravel Gun", false),
    BORERMAINTAIN("Borer Requires Maintenance", false),
    NOMINERS("Disable Automining Machines", false),
    HARDEU("Hard Mode EU Compatibility", ModList.GREGTECH.isLoaded()), //TypeHelper to Website Generator: boolean
    PIPEHARDNESS("Pipe Block Hardness", 0F),
    FRICTIONXP("Spawn XP from Friction Heater", true),
    SPILLERRANGE("Liquid Spiller Range, Use Zero to Disable", 16),
    POWERCLIENT("Run power transfer code on client", false),  //caused many issues
    //TUTORIAL("Tutorial Mode", false),
    FRAMES("Allow Frames to move Machines (May cause corruption)", false),
    CONVERTERLOSS("Power Converter Loss Percent", 0),
    FAKEBEDROCK("Allow special bedrock tool abilities in automation", true),
    BORERGEN("Borer Chunk Gen Radius", 0),
    ALLOWLIGHTBRIDGE("Enable Light Bridge", true),
    ALLOWITEMCANNON("Enable Item Cannon", true),
    ALLOWCHUNKLOADER("Enable Chunk Loader", true),
    CHUNKLOADERSIZE("Chunk Loader Max Radius in Chunks", 8),
    RECIPEMOD("Allow Nonstandard Recipe Modifications", false),
    STRONGRECIPEMOD("Strong Recipe Editing", false),
    CORERECIPEMOD("Core Recipe Editing", "X"),
    CRAFTERPROFILE("AutoCrafter Lag Profiling And Compensation", true),
    HSLAHARVEST("Increased Harvest Level for HSLA", false),
    LATEDYNAMO("Rotational Dynamo Recipe Difficulty", 0),
    BORERPOW("Borer Power Requirement Factor", 1F),
    BEEYEAST("Use Forestry Bees To Produce Yeast", 0),
    HARDCONVERTERS("Harder Converter Unit Recipes", false),
    OREALUDUST("Allow other mods' aluminum dust to make Silicon", false),
    GATEBLAST("Enable Blast Furnace recipe gating", false),
    GATEWORK("Enable Worktable recipe gating", false),
    VACPOWER("Item Vacuum Power Per Meter", (int) PowerReceivers.VACUUM.getMinPower() / 4), //TypeHelper to Website Generator: int
    HYDROSTREAMFALLMAX("Streams Waterfall Min Height for Max Hydrokinetic Yield", 8),
    TINKERFLAKES("TiC Smeltery Flake Yield Amount In Ingots", 1.5F),
    IC2BLAZECOMPRESS("Increase Blaze Powder To Rod Cost In IC2 Compressor (Exploit Fix)", true),
    FREEWATER("Free Water Production Factor", 1F),
    SNEAKWINGS("Jetpack wings enable with sneak vs disable", false);

    private String label;
    private boolean defaultState;
    private int defaultValue;
    private float defaultFloat;
    private String defaultString;
    private Class type;
    private boolean enforcing = false;

    public static final ConfigRegistry[] optionList = values();

    ConfigRegistry(String l, boolean d) {
        label = l;
        defaultState = d;
        type = boolean.class;
    }

    ConfigRegistry(String l, boolean d, boolean tag) {
        this(l, d);
        enforcing = true;
    }

    ConfigRegistry(String l, int d) {
        label = l;
        defaultValue = d;
        type = int.class;
    }

    ConfigRegistry(String l, float d) {
        label = l;
        defaultFloat = d;
        type = float.class;
    }

    ConfigRegistry(String l, String d) {
        label = l;
        defaultString = d;
        type = String.class;
    }

    public boolean isBoolean() {
        return type == boolean.class;
    }

    public boolean isNumeric() {
        return type == int.class;
    }

    public boolean isDecimal() {
        return type == float.class;
    }

    public Class getPropertyType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean isString() {
        return type == String.class;
    }

    public boolean getState() {
        return (Boolean) RotaryCraft.config.getControl(this.ordinal());
    }

    public int getValue() {
        if (this == DIFFICULTY) {
            return EnumDifficulty.getBoundedDifficulty((Integer) RotaryCraft.config.getControl(this.ordinal()), RotaryConfig.EASIEST, RotaryConfig.HARDEST).ordinal();
        }
        return (Integer) RotaryCraft.config.getControl(this.ordinal());
    }

    public float getFloat() {
        return (Float) RotaryCraft.config.getControl(this.ordinal());
    }

    @Override
    public String getString() {
        return (String) RotaryCraft.config.getControl(this.ordinal());
    }

    @Override
    public String getDefaultString() {
        return defaultString;
    }

    public boolean isDummiedOut() {
        return type == null;
    }

    @Override
    public boolean getDefaultState() {
        return defaultState;
    }

    @Override
    public int getDefaultValue() {
        return defaultValue;
    }

    @Override
    public float getDefaultFloat() {
        return defaultFloat;
    }

    @Override
    public boolean isEnforcingDefaults() {
        return enforcing;
    }

    @Override
    public boolean shouldLoad() {
        return true;
    }

    public static double getConverterEfficiency() {
        return Mth.clamp(1 - CONVERTERLOSS.getValue() / 100D, 0, 1);
    }

    public static boolean enableConverters() {
        return getConverterEfficiency() > 0;
    }

    public static int getRecipeModifyPower() {
        if (RECIPEMOD.getState()) {
            if (STRONGRECIPEMOD.getState()) {
                String s = CORERECIPEMOD.getString();
                if (isValidRecipeModString(s)) {
                    return 3;
                }
                return 2;
            }
            return 1;
        }
        return 0;
    }

    private static boolean isValidRecipeModString(String s) {
        return false;//s.equals("RotaryCraft_RecipeModify@"+RotaryCraft.instance.getModVersion().toString());
    }

    @Override
    public String getCustomConfigFile() {
        return switch (this) {
            case STRONGRECIPEMOD, CORERECIPEMOD -> "*_RecipeModding";
            default -> null;
        };
    }

    @Override
    public boolean isAccessible() {
        return true;
    }

    @Override
    public boolean saveIfUnspecified() {
        return switch (this) {
            case STRONGRECIPEMOD, CORERECIPEMOD -> false;
            default -> true;
        };
    }

    public static float getBorerPowerMult() {
        return Mth.clamp(BORERPOW.getFloat(), 0.5F, 8F);
    }

    public static boolean enableFermenterYeast() {
        return BEEYEAST.getValue() <= 1;
    }

    public static boolean enableBeeYeast() {
        return BEEYEAST.getValue() >= 1;
    }

    public static float getSmelteryFlakeYield() {
        return Mth.clamp(TINKERFLAKES.getFloat(), 0.25F, 2F);
    }

    public static float getFreeWaterProduction() {
        return Mth.clamp(FREEWATER.getFloat(), 0.1F, 2F);
    }

    @Override
    public boolean enforceMatch() {
        return switch (this) {
            case GPRORES, CRAFTABLEBEDROCK, MODORES, DIFFICULTY, TABLEMACHINES, ROTATEHOSE, HSLADICT, PREENCHANT, ALLOWTNTCANNON, ALLOWEMP, NOMINERS, PIPEHARDNESS, CONVERTERLOSS, ALLOWLIGHTBRIDGE, ALLOWITEMCANNON, ALLOWCHUNKLOADER, RECIPEMOD, STRONGRECIPEMOD, CORERECIPEMOD, LATEDYNAMO, BORERPOW, BEEYEAST ->
                //case BLASTGATE: Not a registry config
                    true;
            default -> false;
        };
    }

    @Override
    public boolean isUserSpecific() {
        return switch (this) {
            case ENGINEVOLUME, MACHINEVOLUME, PROJECTORLINES, DYNAMICHANDBOOK, COLORBLIND, SPRINKLER, CLEARCHAT, POWERCLIENT ->
                    true;
            default -> false;
        };
    }
}