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

import net.minecraft.advancements.Advancement;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;

import java.util.Locale;

public enum RotaryAdvancements {

    RCUSEBOOK(1, 1, RotaryItems.HANDBOOK.get(), null, false),
    DUMBEXTRACTOR(1, -1, EngineType.DC.getCraftedProduct(), null, false),
    MAKESTEEL(0, 0, RotaryItems.HSLA_STEEL_INGOT.get(), null, false),
    FAILSTEEL(1, 2, RotaryBlocks.HSLA_STEEL_BLOCK.get(), MAKESTEEL, false),
    //    WORKTABLE(-2, 1, MachineRegistry.WORKTABLE, MAKESTEEL, false),
    MAKEYEAST(2, -2, RotaryItems.YEAST.get(), MAKESTEEL, false),
    //    EXTRACTOR(2, 0, RotaryItems.GOLDOREFLAKES, MAKESTEEL, false),
//    PCB(0, 4, RotaryItems.PCB, MAKESTEEL, false),
    PUMP(-6, 0, MachineRegistry.PUMP, MAKESTEEL, false),
    //    GPR(-2, 4, MachineRegistry.GPR, PCB, false),
//    BORER(2, 6, MachineRegistry.BORER, PCB, false),
    JETFUEL(4, -4, RotaryItems.JET_FUEL_BUCKET.get(), MAKEYEAST, false), //make
    RECYCLE(4, -8, RotaryItems.HSLA_STEEL_SCRAP.get(), JETFUEL, false),
    //    JETENGINE(6, -4, EngineType.JET.getCraftedProduct(), JETFUEL, true),
//    MAKERAILGUN(0, 8, MachineRegistry.RAILGUN, PCB, true),
//    SUCKEDINTOJET(6, -8, Items.ROTTEN_FLESH, JETENGINE, false),
    BEDROCKBREAKER(-4, 2, RotaryItems.BEDROCK_DUST.get(), MAKESTEEL, false), //break bedrock with
    STEAMENGINE(-8, 0, EngineType.STEAM.getCraftedProduct(), PUMP, false), //turn on
    STEELSHAFT(-2, -2, RotaryItems.HSLA_SHAFT.get(), MAKESTEEL, false), //make
    //    CVT(-2, -4, MachineRegistry.ADVANCEDGEARS.getCraftedMetadataProduct(1), STEELSHAFT, false), //make
    BEDROCKSHAFT(-4, 6, RotaryItems.BEDROCK_ALLOY_SHAFT.get(), BEDROCKBREAKER, false), //make
    //    BEDROCKTOOLS(-6, 2, RotaryItems.BEDPICK, BEDROCKBREAKER, false), //make
//    JETCHICKEN(8, -4, Items.FEATHER, JETENGINE, false), //suck 50 chickens into jet engine
//    JETFAIL(8, -2, Blocks.FIRE, JETENGINE, false), //cause violent failure
//    LIGHTFALL(8, -6, MachineRegistry.LIGHTBRIDGE, JETENGINE, false), //light bridge turns off, drops you to death
//    SPRINKLER(-6, -2, MachineRegistry.SPRINKLER, PUMP, false), //turn on
    FLOODLIGHT(-1, -1, MachineRegistry.FLOODLIGHT, MAKESTEEL, false), //turn on at Light 15
    //    DAMAGEGEARS(-4, -2, RotaryItems.GEARUNIT, STEELSHAFT, false),
//    DIAMONDGEARS(-4, -4, GearboxTypes.DIAMOND.getGearboxItem(8), DAMAGEGEARS, false), //make
//    MRADS32(2, -6, RotaryItems.ANGULAR_TRANSDUCER.get(), JETFUEL, true), //transmit power at 32Mrad/s
//    GIGAWATT(6, 0, Blocks.REDSTONE_BLOCK, JETENGINE, true), //transmit 1GW of power in one shaft w/o breaking
//    RAILDRAGON(2, 8, Blocks.DRAGON_EGG, MAKERAILGUN, true), //kill dragon with railgun
//    RAILKILLED(0, 10, new ItemStack(Items.SKELETON_SKULL, 1), MAKERAILGUN, false), //kill self with railgun
//    GRAVELGUN(0, -4, RotaryItems.GRAVEL_GUN, MAKESTEEL, false), //one hit kill with
    LANDMINE(2, 3, MachineRegistry.LANDMINE, MAKESTEEL, false), //step on
    //    NETHERHEATRAY(4, -2, MachineRegistry.HEATRAY, JETFUEL, true), //dig 500m with heat ray in nether
//    CUTKNOT(4, 6, RotaryItems.DRILL, BORER, true),
    //RAREEXTRACT(4, 0, ExtractorModOres.getFlakeProduct(ModOreList.PLATINUM), EXTRACTOR, true),
//    MASSIVEHIT(0, -8, Items.FLINT, GRAVELGUN, true),
    OVERPRESSURE(-8, 2, MachineRegistry.COOLINGFIN, STEAMENGINE, false),
//    DOUBLEKILL(-2, -6, Items.ARROW, GRAVELGUN, true),
//    INSANITY(2, 2, MachineRegistry.EXTRACTOR, EXTRACTOR, true),
//    INSTANTBED(-6, 4, MachineRegistry.BEDROCKBREAKER, BEDROCKBREAKER, true),
//    PULSEFIRE(5, -5, MachineRegistry.PULSEJET, JETFUEL, false),
    ;
    public static final RotaryAdvancements[] list = values();

    public final RotaryAdvancements dependency;
    public final int xPosition;
    public final int yPosition;
    public final boolean isSpecial;
    private final ItemStack iconItem;

    RotaryAdvancements(int x, int y, Item icon, RotaryAdvancements preReq, boolean special) {
        this(x, y, new ItemStack(icon), preReq, special);
    }

    RotaryAdvancements(int x, int y, Block icon, RotaryAdvancements preReq, boolean special) {
        this(x, y, new ItemStack(icon), preReq, special);
    }

    RotaryAdvancements(int x, int y, MachineRegistry icon, RotaryAdvancements preReq, boolean special) {
        this(x, y, icon.getBlockState().getBlock(), preReq, special);
    }

    RotaryAdvancements(int x, int y, ItemStack icon, RotaryAdvancements preReq, boolean special) {
        xPosition = x;
        yPosition = y;
        dependency = preReq;
        iconItem = icon;
        isSpecial = special;
    }

    public static void registerAchievements() {
        //ReikaJavaLibrary.pConsole(Arrays.toString(RotaryCraft.config.achievementIDs));
      /*todo  for (int i = 0; i < list.length; i++) {
            RotaryAdvancements a = list[i];
            int id = RotaryCraft.config.getAchievementID(i);
            Advancement dep = a.hasDependency() ? a.dependency.get() : null;
            Advancement ach = new Advancement(new ResourceLocation(a.name().toLowerCase(Locale.ENGLISH)), a.name().toLowerCase(Locale.ENGLISH), a.xPosition, a.yPosition, a.iconItem, dep);
            //ReikaJavaLibrary.pConsole(a+":"+id+":"+StatList.getOneShotStat(id));
            //if (StatList.getOneShotStat(id) != null)
            //	throw new IDConflictException(RotaryCraft.getInstance(), "The mod's achievement IDs are conflicting with another at ID "+id+" ("+a+" is trying to overwrite "+StatList.getOneShotStat(id).statName+").\nCheck the config file and change them.");
            if (a.isSpecial)
                ach.setSpecial();
            RotaryCraft.achievements[i] = ach;
            ach.registerStat();
            RotaryCraft.LOGGER.info("Registering achievement " + a + " with ID " + id + " and ingame name \"" + a + "\" (slot " + i + ").");
        }
                AdvancementsScreen.registerAchievementPage(new RCAchievementPage("RotaryCraft", RotaryCraft.achievements));*/
    }

    public Advancement get() {
        return RotaryCraft.achievements[this.ordinal()];
    }

    public void triggerAchievement(Player ep) {
        if (!ConfigRegistry.ACHIEVEMENTS.getState())
            return;
        if (ep == null) {
            if (FMLLoader.getDist() == Dist.DEDICATED_SERVER) {
                //ReikaChatHelper.write("Player does not exist to receive their achievement \""+this+"\"!");
                //ReikaJavaLibrary.pConsole("Player does not exist to receive their achievement \""+this+"\"!");
                RotaryCraft.LOGGER.debug("Player does not exist to receive their achievement \"" + this + "\"!");
            }
        } //else {
//         todo   ep.triggerAchievement(this.get());
//        }
    }

    public boolean hasDependency() {
        return dependency != null;
    }

}
