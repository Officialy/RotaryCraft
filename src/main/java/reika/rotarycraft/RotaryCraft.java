/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft;

import net.minecraft.advancements.Advancement;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.ModList;
import reika.dragonapi.auxiliary.trackers.CommandableUpdateChecker;
import reika.dragonapi.auxiliary.trackers.DonatorController;
import reika.dragonapi.auxiliary.trackers.PlayerFirstTimeTracker;
import reika.dragonapi.auxiliary.trackers.PlayerHandler;
import reika.dragonapi.base.DragonAPIMod;
import reika.dragonapi.instantiable.io.SoundLoader;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.rotarycraft.auxiliary.*;
import reika.rotarycraft.gui.screen.GuiHandCraft;
import reika.rotarycraft.gui.screen.machine.*;
import reika.rotarycraft.gui.screen.machine.inventory.*;
import reika.rotarycraft.registry.*;

import java.io.File;
import java.net.URL;

@Mod(RotaryCraft.MODID)
public class RotaryCraft extends DragonAPIMod {

    public static final String MODID = "rotarycraft";
    public static final String packetChannel = "RotaryCraftData";

    public static final Logger LOGGER = LogManager.getLogger();

    protected static final SoundLoader sounds = new SoundLoader(SoundRegistry.class);
    public static final MachineDamage jetingest = new MachineDamage("was sucked into a jet engine").setArmorBlocking(1F, 90, 1, 2, 3, 4);
    public static final MachineDamage hydrokinetic = new MachineDamage("was paddled to death").setArmorBlocking(0.5F, 2, 4);
    public static final MachineDamage shock = new MachineDamage("was electrified");//todo .bypassArmor();
    public static final MachineDamage grind = new MachineDamage("was ground to a pulp").setArmorBlocking(0.5F, 6, 1);
    public static final MachineDamage freezeDamage = new MachineDamage("froze");
    public static final MachineDamage heatDamage = new MachineDamage("burned up");//todo .setIsFire();

    public static FreezePotion freeze = new FreezePotion(MobEffectCategory.HARMFUL, 0x289EFF);
    private static RotaryCraft instance;
    public static Advancement[] achievements;
    public static RotaryConfig config;

    @Override
    public URL getDocumentationSite() {
        return DragonAPI.getReikaForumPage();
    }

    @Override
    public URL getBugSite() {
        return null;
    }

    @Override
    public File getConfigFolder() {
        return config.getConfigFolder();
    }

    public RotaryCraft() {
        this.startTiming(LoadProfiler.LoadPhase.PRELOAD);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        instance = this;

        this.basicSetup();
        config = new RotaryConfig(instance, ConfigRegistry.optionList, null);
        config.loadSubfolderedConfigFile();
        config.initProps();

        modEventBus.addListener(this::commonSetup);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            // Client setup
            modEventBus.addListener(this::clientSetup);
            RotaryModelLayers.init(modEventBus);
        });
        LOGGER.info("RotaryCraft:" + " Creating Blocks!");
        RotaryBlocks.BLOCKS.register(modEventBus);

        LOGGER.info("RotaryCraft:" + " Creating Block Entities!");
        RotaryBlockEntities.BLOCK_ENTITIES.register(modEventBus);

        LOGGER.info("RotaryCraft:" + " Creating Entities!");
        RotaryEntities.ENTITIES.register(modEventBus);

        LOGGER.info("RotaryCraft:" + " Creating Items!");
        RotaryItems.ITEMS.register(modEventBus);
        RotaryBlocks.ITEMS.register(modEventBus);

        LOGGER.info("RotaryCraft:" + " Creating Fluids!");
        RotaryFluids.FLUID_TYPES.register(modEventBus);
        RotaryFluids.FLUIDS.register(modEventBus);

        LOGGER.info("RotaryCraft:" + " Creating Containers!");
        RotaryMenus.REGISTRY.register(modEventBus);

        LOGGER.info("RotaryCraft:" + " Creating Sound Events!");
        SoundRegistry.SOUND_EVENTS.register(modEventBus);

        LOGGER.info("RotaryCraft:" + " Creating Test items!");
        Tests.ITEMS.register(modEventBus);
        RotaryCraftTabs.CREATIVE_MODE_TABS.register(modEventBus);
        DonatorController.instance.registerMod(this, DonatorController.reikaURL);
        ReikaPacketHelper.registerPacketHandler(instance, packetChannel, new PacketHandlerCore());

        RotaryRecipeTypes.RECIPE_TYPES.register(modEventBus);
        RotaryRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);

//   todo     ReikaJavaLibrary.initClass(HandbookRegistry.class);
        ReikaJavaLibrary.initClass(SoundRegistry.class);
        this.finishTiming();
    }

    @Override
    public String getUpdateCheckURL() {
        return CommandableUpdateChecker.reikaURL;
    }

    @Override
    public String getModId() {
        return MODID;
    }

    public static RotaryCraft getInstance() {
        return instance;
    }


    public void commonSetup(final FMLCommonSetupEvent evt) {
        if (ConfigRegistry.HANDBOOK.getState())
            PlayerFirstTimeTracker.addTracker(new HandbookTracker());
        PlayerHandler.instance.registerTracker(HandbookNotifications.HandbookConfigVerifier.instance);

        for (int i = 0; i < MachineRegistry.machineList.length; i++) {
            MachineRegistry m = MachineRegistry.machineList.get(i);
            if (!m.allowsAcceleration()) {
                if (ModList.CHROMATICRAFT.isLoaded()) {
//                    AcceleratorBlacklist.addBlacklist(m.getTEClass(), m.getName(), BlacklistReason.EXPLOIT);
                }
//                TimeTorchHelper.blacklistBlockEntity(m.getTEClass());
            }
        }
        for (int i = 0; i < EngineType.engineList.length; i++) {
            EngineType type = EngineType.engineList[i];
            if (ModList.CHROMATICRAFT.isLoaded()) {
//                AcceleratorBlacklist.addBlacklist(type.engineClass, type.name(), BlacklistReason.EXPLOIT);
            }
//            TimeTorchHelper.blacklistBlockEntity(type.engineClass);
        }
//        RotaryRecipes.loadMachineRecipeHandlers();
//        RotaryRecipes.addPostLoadRecipes();

      /*todo  if (RotaryConfig.Common.ACHIEVEMENTS.get()) {
            achievements = new Advancement[RotaryAdvancements.list.length];
            RotaryAdvancements.registerAchievements();
        }*/
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        sounds.register();
        LOGGER.info(SoundRegistry.PULSEJET.getPath());
        MenuScreens.register(RotaryMenus.STEAM_ENGINE.get(), SteamScreen::new);
        MenuScreens.register(RotaryMenus.GEARBOX.get(), GearboxScreen::new);
        MenuScreens.register(RotaryMenus.RESERVOIR.get(), ReservoirScreen::new);
        MenuScreens.register(RotaryMenus.BEVEL.get(), GuiBevel::new);
        MenuScreens.register(RotaryMenus.SORTER.get(), GuiSorter::new);
        MenuScreens.register(RotaryMenus.BLOWER.get(), GuiBlower::new);
        MenuScreens.register(RotaryMenus.LANDMINE.get(), GuiLandmine::new);
        MenuScreens.register(RotaryMenus.PERFORMANCE_ENGINE.get(), GuiPerformance::new);
        MenuScreens.register(RotaryMenus.WINDER.get(), WinderScreen::new);
        MenuScreens.register(RotaryMenus.BIG_FURNACE.get(), GuiBigFurnace::new);
        MenuScreens.register(RotaryMenus.HAND_CRAFT.get(), GuiHandCraft::new);
        MenuScreens.register(RotaryMenus.MUSIC.get(), GuiMusic::new);
        MenuScreens.register(RotaryMenus.GRINDER.get(), GuiGrinder::new);
        MenuScreens.register(RotaryMenus.GAS_ENGINE.get(), GuiEthanol::new);
        MenuScreens.register(RotaryMenus.BLAST_FURNACE.get(), GuiBlastFurnace::new);

        RotaryRenders.registerBlockColors();
        event.enqueueWork(RotaryRenders::registerRenderLayers);
    }

    @Override
    public String getDisplayName() {
        return "RotaryCraft";
    }

    @Override
    public String getModAuthorName() {
        return "Reika";
    }


}