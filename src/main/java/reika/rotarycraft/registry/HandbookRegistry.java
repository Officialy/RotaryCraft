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

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.RegistryObject;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.HandbookNotifications;
import reika.rotarycraft.auxiliary.RotaryDescriptions;
import reika.rotarycraft.auxiliary.interfaces.HandbookEntry;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
import reika.rotarycraft.gui.screen.GuiHandbook;

import java.util.*;

public enum HandbookRegistry implements HandbookEntry {

    //---------------------TOC--------------------//
    TOC("Table of Contents", "TOC"),
    INFO("Information"),
    ENGINES("Engines"),
    TRANS("Transmission"),
    CONVERTER("Converters"),
    PRODMACHINES("Production"),
    PROCMACHINES("Processing"),
    FARMMACHINES("Farming"),
    PIPEMACHINES("Piping"),
    ACCMACHINES("Accessory"),
    WEPMACHINES("Weaponry"),
    SURVMACHINES("Surveying"),
    COSMACHINES("Cosmetic"),
    UTILMACHINES("Utility"),
    TOOLS("Tools"),
    RESOURCE("Resources"),

    //---------------------INFO--------------------//
    TERMS("Basic Terms", "Terms and Physics Explanations"),
    PHYSICS("Relevant Physics"),
    MATERIAL("Material Properties"),
    SHAFTS("Shaft Load Limits"),
    FLYWHEELS("Flywheel Load Limits"),
    TRANSFER("Basics of Power Transfer"),
    TIERS("Machine Tiers"),
    TIMING("Duration Time"),
    MUFFLING("Sounds and Muffling"),
    INTERDIM("Interdimensional Power Transport"),
    ALERTS("Config Alerts"),
    PACKMODS("Modpack Changes"),

    //---------------------MISC--------------------//
    INFODESC("Important Notes", "Important Notes"),
    LUBE("Lubricant"),
    BEARINGS("Gearbox Bearings"),
    CANOLA("Canola", RotaryItems.CANOLA_SEEDS),
    METER("Angular Transducer", RotaryItems.ANGULAR_TRANSDUCER),
    SCREW("Screwdriver", RotaryItems.SCREWDRIVER),
    ENCHANTING("Enchanting Machines"),
    MODINTERFACE("Inter-Mod Interactions"),
    COMPUTERCRAFT("ComputerCraft"),

    //---------------------ENGINES--------------------//
    ENGINEDESC("Power Supply", "Engines"),
    DCENGINE(MachineRegistry.DC_ENGINE, EngineType.DC),
    WINDENGINE(MachineRegistry.WIND_ENGINE, EngineType.WIND),
    STEAMENGINE(MachineRegistry.STEAM_ENGINE, EngineType.STEAM),
    GASENGINE(MachineRegistry.GAS_ENGINE, EngineType.GAS),
    ACENGINE(MachineRegistry.AC_ENGINE, EngineType.AC),
    PERFENGINE(MachineRegistry.PERFORMANCE_ENGINE, EngineType.SPORT),
    //        HYDROENGINE(MachineRegistry.ENGINE, EngineType.HYDRO),
    MICROTURB(MachineRegistry.MICRO_TURBINE, EngineType.MICRO),
    //    JETENGINE(MachineRegistry.ENGINE, EngineType.JET),
    SOLAR(MachineRegistry.SOLARTOWER),

    //---------------------TRANSMISSION--------------------//
    TRANSDESC("Power Transfer", "Transmission"),
    SHAFT(MachineRegistry.SHAFT),
    GEARBOX(MachineRegistry.GEARBOX),
    BEVEL(MachineRegistry.BEVELGEARS),
    SPLITTER(MachineRegistry.SPLITTER),
    CLUTCH(MachineRegistry.CLUTCH),
    DYNA(MachineRegistry.DYNAMOMETER),
    FLYWHEEL(MachineRegistry.FLYWHEEL),
    WORM(MachineRegistry.ADVANCEDGEARS, 0),
    CVT(MachineRegistry.ADVANCEDGEARS, 1),
    COIL(MachineRegistry.ADVANCEDGEARS, 2),
    HIGEAR(MachineRegistry.ADVANCEDGEARS, 3),
    MULTI(MachineRegistry.MULTICLUTCH),
    //    BELT(MachineRegistry.BELT),
//    BUSCONTROLLER(MachineRegistry.BUSCONTROLLER),
//    BUS(MachineRegistry.POWERBUS),
//    CHAIN(MachineRegistry.CHAIN),
    DISTRIBCLUTCH(MachineRegistry.DISTRIBCLUTCH),

    //---------------------CONVERTER--------------------//
    CONVERTERDESC("Conversion Engines", "Converters"),
    /*STATIC(MachineRegistry.DYNAMO), //Rotational Dynamo
    GENERATOR(MachineRegistry.GENERATOR),
    BOILER(MachineRegistry.BOILER),
    AIRCOMPRESSOR(MachineRegistry.COMPRESSOR),
    DYNAMO(MachineRegistry.MAGNETIC), //Magnetostatic
    MOTOR(MachineRegistry.ELECTRICMOTOR),
    STEAMTURB(MachineRegistry.STEAMTURBINE),
    PNEUMATIC(MachineRegistry.PNEUENGINE),
    FUELENGINE(MachineRegistry.FUELENGINE),
*/
    //---------------------MACHINES--------------------//
    PRODMACHINEDESC("Production Machines", "Production"),
    //    BLAST(MachineRegistry.BLASTFURNACE),
//    WORKTABLE(MachineRegistry.WORKTABLE),
//    FERMENTER(MachineRegistry.FERMENTER),
//    FRACTION(MachineRegistry.FRACTIONATOR),
//    BEDROCK(MachineRegistry.BEDROCKBREAKER),
//    BORER(MachineRegistry.BORER),
    PUMP(MachineRegistry.PUMP),
    OBSIDIAN(MachineRegistry.OBSIDIAN),
    //    LAVAMAKER(MachineRegistry.LAVAMAKER),
//    AGGREGATOR(MachineRegistry.AGGREGATOR),
    FRIDGE(MachineRegistry.REFRIGERATOR),

    PROCMACHINEDESC("Processing Machines", "Processing"),
    //    GRINDER(MachineRegistry.GRINDER),
//    EXTRACTOR(MachineRegistry.EXTRACTOR),
//    PULSEJET(MachineRegistry.PULSEJET),
//    COMPACTOR(MachineRegistry.COMPACTOR),
//    PURIFIER(MachineRegistry.PURIFIER),
//    ENHANCER(MachineRegistry.FUELENHANCER),
//    MAGNET(MachineRegistry.MAGNETIZER),
//    DISTILER(MachineRegistry.DISTILLER),
    FURNACE(MachineRegistry.BIGFURNACE),
    //    CRYSTAL(MachineRegistry.CRYSTALLIZER),
    COMPOST(MachineRegistry.COMPOSTER),
//    CENTRIFUGE(MachineRegistry.CENTRIFUGE),
//    DRYING(MachineRegistry.DRYING),
//    WETTER(MachineRegistry.WETTER),
//    DROPS(MachineRegistry.DROPS),

    FARMMACHINEDESC("Farming Machines", "Farming"),
    //    FAN(MachineRegistry.FAN),
//    BREEDER(MachineRegistry.AUTOBREEDER),
//    BAITBOX(MachineRegistry.BAITBOX),
//    SPAWNER(MachineRegistry.SPAWNERCONTROLLER),
//    SPRINKLER(MachineRegistry.SPRINKLER),
//    WOODCUTTER(MachineRegistry.WOODCUTTER),
    HARVESTER(MachineRegistry.MOBHARVESTER),
//    FERTILIZER(MachineRegistry.FERTILIZER),
//    LAWNSPRINKLER(MachineRegistry.LAWNSPRINKLER),
//    HYDRATOR(MachineRegistry.HYDRATOR),

    PIPEMACHINEDESC("Piping", "Piping"),
    HOSE(MachineRegistry.HOSE.getName(), MachineRegistry.HOSE),
    PIPE(MachineRegistry.PIPE.getName(), MachineRegistry.PIPE),
    FUELLINE(MachineRegistry.FUELLINE.getName(), MachineRegistry.FUELLINE),
    VALVE(MachineRegistry.VALVE.getName(), MachineRegistry.VALVE),
    BYPASS(MachineRegistry.BYPASS.getName(), MachineRegistry.BYPASS),
    SEPARATOR(MachineRegistry.SEPARATION.getName(), MachineRegistry.SEPARATION),
    SUCTION(MachineRegistry.SUCTION.getName(), MachineRegistry.SUCTION),
    BEDPIPE(MachineRegistry.BEDPIPE.getName(), MachineRegistry.BEDPIPE),

    ACCMACHINEDESC("Accessory Machines", "Aux Machines"),
    //    FURNACEHEATER(MachineRegistry.FRICTION),
    HEATER(MachineRegistry.HEATER),
    //    VACUUM(MachineRegistry.VACUUM),
//    ECU(MachineRegistry.ECU),
    WINDER(MachineRegistry.WINDER),
    REFRESHER(MachineRegistry.REFRESHER),
    //    CCTVSCREEN(MachineRegistry.SCREEN),
    MIRROR(MachineRegistry.MIRROR),
    COOLINGFIN(MachineRegistry.COOLINGFIN),
//    SORTING(MachineRegistry.SORTING),
//    FILLING(MachineRegistry.FILLINGSTATION),
//    PIPEPUMP(MachineRegistry.PIPEPUMP),
//    ITEMFILTER(MachineRegistry.ITEMFILTER),

    WEPMACHINEDESC("Defence/Offence Machines", "Defence/Offense"),
    //    ARROWGUN(MachineRegistry.ARROWGUN),
    HEATRAY(MachineRegistry.HEATRAY),
    TNT(MachineRegistry.TNTCANNON),
    //    SONIC(MachineRegistry.SONICWEAPON),
//    FORCE(MachineRegistry.FORCEFIELD),
//    ANTIAIR(MachineRegistry.ANTIAIR),
//    RAILGUN(MachineRegistry.RAILGUN),
//    FREEZE(MachineRegistry.FREEZEGUN),
    CONTAIN(MachineRegistry.CONTAINMENT),
    //    LASERGUN(MachineRegistry.LASERGUN),
    LANDMINE(MachineRegistry.LANDMINE),
    BLOCKCANNON(MachineRegistry.BLOCKCANNON),
    SELFDESTRUCT(MachineRegistry.SELFDESTRUCT),
    //    EMP(MachineRegistry.EMP),
//    AIRGUN(MachineRegistry.AIRGUN),
    VDG(MachineRegistry.VANDEGRAFF),
//    GATLING(MachineRegistry.GATLING),
//    FLAMETURRET(MachineRegistry.FLAMETURRET),

    SURVMACHINEDESC("Surveying Machines", "Surveying"),
    //    GPR(MachineRegistry.GPR),
//    RADAR(MachineRegistry.MOBRADAR),
    SCANNER(MachineRegistry.CAVESCANNER),
//    CCTV(MachineRegistry.CCTV),
//    SPYCAM(MachineRegistry.SPYCAM),

    COSMACHINEDESC("Cosmetic Machines", "Cosmetic"),
    //    FIREWORK(MachineRegistry.FIREWORK),
    MUSIC(MachineRegistry.MUSICBOX),
    //    PROJECTOR(MachineRegistry.PROJECTOR),
//    WEATHER(MachineRegistry.WEATHERCONTROLLER),
//    DISPLAY(MachineRegistry.DISPLAY),
    PARTICLE(MachineRegistry.PARTICLE),

    UTILMACHINEDESC("Utility Machines", "Utility"),
    FLOODLIGHT(MachineRegistry.FLOODLIGHT),
    //    PILEDRIVER(MachineRegistry.PILEDRIVER),
    AEROSOL(MachineRegistry.AEROSOLIZER),
    //    LIGHTBRID(MachineRegistry.LIGHTBRIDGE),
    RESERVOIR(MachineRegistry.RESERVOIR),
    DETECTOR(MachineRegistry.PLAYERDETECTOR),
    //    CHEST(MachineRegistry.SCALECHEST),
    //SPILLER(MachineRegistry.SPILLER),
    SMOKE(MachineRegistry.SMOKEDETECTOR),
    //    FIRESTARTER(MachineRegistry.IGNITER),
    ITEMCANNON(MachineRegistry.ITEMCANNON),
    BUCKETFILLER(MachineRegistry.BUCKETFILLER),
    //    LAMP(MachineRegistry.LAMP),
//    TERRA(MachineRegistry.TERRAFORMER),
    LINE(MachineRegistry.LINEBUILDER),
    BEAMMIRROR(MachineRegistry.BEAMMIRROR),
    //    SONICBORER(MachineRegistry.SONICBORER),
//    DEFOLIATOR(MachineRegistry.DEFOLIATOR),
    GRINDSTONE(MachineRegistry.GRINDSTONE),
    BLOWER(MachineRegistry.BLOWER),
    //    GASTANK(MachineRegistry.GASTANK),
//    CRAFTER(MachineRegistry.CRAFTER),
//    CHUNKLOADER(MachineRegistry.CHUNKLOADER),
    FILLER(MachineRegistry.FILLER),
    SPILLWAY(MachineRegistry.SPILLWAY),
//    BUNDLEDBUS(MachineRegistry.BUNDLEDBUS),

    //---------------------TOOLS--------------------//
    TOOLDESC("Tool Items", "Tools"),
    //    SPRING(RotaryItems.SPRING),
//    STRONGSPRING(RotaryItems.STRONGCOIL),
    ULTRA(RotaryItems.ULTRASOUND),
    //    MOTION(RotaryItems.MOTION),
//    VAC(RotaryItems.VACUUM),
//    STUN(RotaryItems.STUNGUN),
//    GRAVEL(RotaryItems.GRAVELGUN),
//    FIREBALL(RotaryItems.FIREBALL),
//    HANDCRAFT(RotaryItems.HANDCRAFT),
    NVG(RotaryItems.NVG),
    //    BUCKETS(RotaryItems.BUCKET),
    BEDTOOLS("Bedrock Tools"),
    BEDARMOR("Bedrock Armor"),
    TARGETER(RotaryItems.TARGET),
    //    IOGOGGLES(RotaryItems.IOGOGGLES),
    //CKEY(RotaryItems.KEY),
//    MINECART(RotaryItems.MINECART),
//    TILESELECT(RotaryItems.TILESELECTOR),
    JETPACK("Jetpacks"),
    STEELTOOLS("Steel Tools"),
    STEELARMOR("Steel Armor"),
    //    ITEMPUMP(RotaryItems.PUMP),
    JUMPBOOTS("Spring Boots"),
    //    TANKS(RotaryItems.FUEL),
    DISK(RotaryItems.DISK),

    //---------------------RESOURCE--------------------//
    RESOURCEDESC("Resource Items", "Resource Items"),
    STEELINGOT("Steel Ingot"),
    COKE("Coal Coke"),
    NETHERDUST("Netherrack Dust and Tar"),
    SAWDUST("Sawdust"),
    BEDDUST("Bedrock Dust"),
    EXTRACTS("Dust/Slurry/Solution"),
    FLAKES("Ore Flakes"),
    COMPACTS("Anthracite/Prismane/Lonsdaleite"),
    DECOBLOCKS("Decorative Blocks"),
    GLASS("Blast Glass"),
    SPAWNERS("Monster Spawners"),
    YEAST("Yeast"),
    ETHANOL("Ethanol"),
    BEDINGOT("Bedrock Alloy Ingot"),
    ALLOYING("Alloying"),
    SILVERINGOT("Silver Ingot"),
    SALT("Salt"),
    AMMONIUM("Ammonium Nitrate"),
    SILVERIODIDE("Silver Iodide"),
    ALUMINUM("Aluminum Powder"),
    RAILGUNAMMO("Railgun Ammunition"),
    SLIDES("Projector Slides"),
    EXPLOSIVES("Explosive Shells"),
    TUNGSTEN("Tungsten");

    public static final HandbookRegistry[] tabList = HandbookRegistry.values();
    private static final Comparator<HandbookRegistry> tabSorter = new Comparator<HandbookRegistry>() {

        @Override
        public int compare(HandbookRegistry o1, HandbookRegistry o2) {
            if (o1.isParent != o2.isParent) {
                if (o1.isParent) {
                    return Integer.MIN_VALUE;
                } else {
                    return Integer.MAX_VALUE;
                }
            }
            if (o1.machine != null && o2.machine != null) {
                return o1.machine.compareTo(o2.machine);
            } else if (o1.machine != null) {
                return Integer.MIN_VALUE;
            } else if (o2.machine != null) {
                return Integer.MAX_VALUE;
            } else {
                return o1.compareTo(o2);
            }
        }

    };
    private final int offset;
    private final boolean isParent;
    private MachineRegistry machine;
    private RegistryObject<Item> item;
    private int parentindex;
    private int basescreen;
    private String title;
    private String name;
    private EngineType engine;

    HandbookRegistry(MachineRegistry m, int o) {
        machine = m;
        offset = o;
        isParent = false;
        item = null;
    }

    HandbookRegistry(MachineRegistry m, EngineType o) {
        machine = m;
        engine = o;
        offset = o != null ? o.ordinal() : -1;
        isParent = false;
        item = null;
    }

    HandbookRegistry(MachineRegistry m) {
        machine = m;
        offset = -1;
        isParent = false;
        item = null;
    }

    HandbookRegistry(RegistryObject<Item> i) {
        offset = -1;
        isParent = false;
        item = i;
    }

    HandbookRegistry(String s) {
        machine = null;
        offset = -1;
        isParent = false;
        item = null;
        title = s;
    }

    HandbookRegistry(ItemStack is) {
        machine = null;
        offset = -1;
        isParent = false;
    }

    HandbookRegistry(String s, MachineRegistry m) {
        machine = m;
        offset = -1;
        isParent = false;
        item = null;
        title = s;

    }

    HandbookRegistry(String s, RegistryObject<Item> m) {
        machine = null;
        offset = -1;
        isParent = false;
        item = m;
        title = s;
    }

    HandbookRegistry() {
        machine = null;
        offset = -1;
        isParent = false;
        item = null;
    }

    HandbookRegistry(String s, String toc) {
        machine = null;
        offset = -1;
        parentindex = RotaryDescriptions.getCategoryCount();
        isParent = true;
        item = null;
        title = s;
        name = toc;
        RotaryDescriptions.addCategory(this);
    }

    public static int getScreen(MachineRegistry m, BlockEntity te) {
        if (m == MachineRegistry.WIND_ENGINE || m == MachineRegistry.STEAM_ENGINE || m == MachineRegistry.PERFORMANCE_ENGINE || m == MachineRegistry.MICRO_TURBINE || m == MachineRegistry.GAS_ENGINE || m == MachineRegistry.DC_ENGINE || m == MachineRegistry.AC_ENGINE)
            return getEngineScreen(te);
        if (m == MachineRegistry.ADVANCEDGEARS)
            return TRANSDESC.getBaseScreen() + 1;
        for (int i = ENGINEDESC.ordinal(); i < TOOLDESC.ordinal(); i++) {
            if (tabList[i].machine == m)
                return tabList[i].getScreen();
        }
        return -1;
    }

    public static List<HandbookRegistry> getEngineTabs() {
        ArrayList<HandbookRegistry> li = getSubList(ENGINEDESC);
        Collections.sort(li, tabSorter);
        return li;
    }

    public static List<HandbookRegistry> getTransTabs() {
        ArrayList<HandbookRegistry> li = getSubList(TRANSDESC);
        li.sort(tabSorter);
        return li;
    }

    public static List<HandbookRegistry> getConverterTabs() {
        ArrayList<HandbookRegistry> li = getSubList(CONVERTERDESC);
        li.sort(tabSorter);
        return li;
    }

    public static List<HandbookRegistry> getMachineTabs() {
        List<HandbookRegistry> tabs = new ArrayList<HandbookRegistry>();
        for (HandbookRegistry h : tabList) {
            if (h.isMachine() && !h.isParent)
                tabs.add(h);
        }
        return tabs;
    }

    public static List<HandbookRegistry> getToolTabs() {
        ArrayList<HandbookRegistry> li = getSubList(TOOLDESC);
        return li;
    }

    public static List<HandbookRegistry> getResourceTabs() {
        ArrayList<HandbookRegistry> li = getSubList(RESOURCEDESC);
        return li;
    }

    public static List<HandbookRegistry> getMiscTabs() {
        ArrayList<HandbookRegistry> li = getSubList(INFODESC);
        return li;
    }

    public static List<HandbookRegistry> getInfoTabs() {
        ArrayList<HandbookRegistry> li = getSubList(TERMS);
        li.add(0, TERMS);
        return li;
    }

    private static ArrayList<HandbookRegistry> getSubList(HandbookRegistry parent) {
        ArrayList<HandbookRegistry> li = new ArrayList<>();
        for (int i = parent.ordinal(); i < tabList.length; i++) {
            HandbookRegistry h = tabList[i];
            if (h != parent && h.isParent)
                break;
            if (!h.isParent)
                li.add(h);
        }
        return li;
    }

    public static List<HandbookRegistry> getCategoryTabs(boolean info) {
        ArrayList<HandbookRegistry> li = new ArrayList<>();
        for (HandbookRegistry handbookRegistry : tabList) {
            if (handbookRegistry.isParent && handbookRegistry != TOC && (info || handbookRegistry != TERMS))
                li.add(handbookRegistry);
        }
        return li;
    }

    public static List<HandbookRegistry> getTOCTabs() {
        ArrayList<HandbookRegistry> li = new ArrayList<>();
        for (HandbookRegistry handbookRegistry : tabList) {
            if (handbookRegistry.isParent && handbookRegistry != TOC)
                li.add(handbookRegistry);
        }
        return li;
    }

    public static int getPage(MachineRegistry m, BlockEntity te) {
        if (m == MachineRegistry.WIND_ENGINE || m == MachineRegistry.STEAM_ENGINE || m == MachineRegistry.PERFORMANCE_ENGINE || m == MachineRegistry.MICRO_TURBINE || m == MachineRegistry.GAS_ENGINE || m == MachineRegistry.DC_ENGINE || m == MachineRegistry.AC_ENGINE)
            return getEnginePage(te);
        if (m == MachineRegistry.ADVANCEDGEARS)
            return getAdvGearPage(te);
        for (HandbookRegistry handbookRegistry : tabList) {
            if (handbookRegistry.machine == m)
                return handbookRegistry.getPage();
        }
        return -1;
    }

    private static int getAdvGearPage(BlockEntity te) {
        return 1;
//  todo      return ((RotaryCraftBlockEntity) te).getBlockMetadata() / 4;
    }

    private static int getEnginePage(BlockEntity te) {
        EngineType e = ((BlockEntityEngine) te).getEngineType();
        return 1 + e.ordinal() - (getEngineScreen(te) - ENGINEDESC.getBaseScreen()) * GuiHandbook.PAGES_PER_SCREEN;
    }

    private static int getEngineScreen(BlockEntity te) {
        EngineType e = ((BlockEntityEngine) te).getEngineType();
        int ei = (1 + e.ordinal()) / GuiHandbook.PAGES_PER_SCREEN;
        return ENGINEDESC.getBaseScreen() + ei;
    }

    public static void addRelevantButtons(int j, int k, int screen, List<AbstractWidget> li) {
        int id = 0;
        for (HandbookRegistry handbookRegistry : tabList) {
            if (handbookRegistry.getScreen() == screen/* && !tabList[i].isDummiedOut()*/) {
                li.add(new ImageButton(id, j - 20, k + handbookRegistry.getRelativeTabPosn() * 20, 20, 20, 0, 0, new ResourceLocation(RotaryCraft.MODID, handbookRegistry.getTabImageFile()), null)); //todo onpress
                //ReikaJavaLibrary.pConsole("Adding "+tabList[i]+" with ID "+id+" to screen "+screen);
                id++;
            }
        }
    }

    public static HandbookRegistry getEntry(int screen, int page) {
        //ReikaJavaLibrary.pConsole(screen+"   "+page);
        if (screen < TERMS.getScreen())
            return TOC;
        HandbookRegistry h = null;//todo HandbookAuxData.getMapping(screen, page);
        return h != null ? h : TOC;
        //throw new RuntimeException("Handbook screen "+screen+" and page "+page+" do not correspond to an entry!");
    }

    public static List<HandbookRegistry> getEntriesForScreen(int screen) {
        //ReikaJavaLibrary.pConsole(screen);
        List<HandbookRegistry> li = new ArrayList<>();
        for (int i = 0; i < tabList.length; i++) {
            if (tabList[i].getScreen() == screen/* && !tabList[i].isDummiedOut()*/) {
                li.add(tabList[i]);
            }
        }
        return li;
    }

    public String getTOCTitle() {
        return name;
    }

    public EngineType getEngine() {
        return engine;
    }

    public boolean isMachine() {
        if (this.getParent() == PROCMACHINEDESC)
            return true;
        if (this.getParent() == UTILMACHINEDESC)
            return true;
        if (this.getParent() == WEPMACHINEDESC)
            return true;
        if (this.getParent() == SURVMACHINEDESC)
            return true;
        if (this.getParent() == PRODMACHINEDESC)
            return true;
        if (this.getParent() == FARMMACHINEDESC)
            return true;
        if (this.getParent() == COSMACHINEDESC)
            return true;
        if (this.getParent() == ACCMACHINEDESC)
            return true;
        return this.getParent() == PIPEMACHINEDESC;
        //if (this.getParent() == CONVERTERDESC)
        //	return true;
    }

    public boolean isEngine() {
        return (this.getParent() == ENGINEDESC);
    }

    public boolean isTrans() {
        return (this.getParent() == TRANSDESC);
    }

    public MachineRegistry getMachine() {
        return machine;
    }

    public Item getItem() {
        return item.get();
    }

    public int getBaseScreen() {
        int sc = 0;
        for (int i = 0; i < this.ordinal(); i++) {
            HandbookRegistry h = tabList[i];
            if (h.isParent) {
                sc += h.getNumberChildren() / GuiHandbook.PAGES_PER_SCREEN + 1;
            }
        }
        return sc;
    }

    public int getNumberChildren() {
        if (!isParent)
            return 0;
        int ch = 0;
        for (int i = this.ordinal() + 1; i < tabList.length; i++) {
            HandbookRegistry h = tabList[i];
            if (h.isParent) {
                return ch;
            } else {
                ch++;
            }
        }
        return ch;
    }

    public int getScreen() {
        return this.getParent().getBaseScreen() + this.getRelativeScreen();
    }

    public int getPage() {
        return (this.ordinal() - this.getParent().ordinal()) % GuiHandbook.PAGES_PER_SCREEN;
    }

    public boolean hasOffset() {
        return offset > -1;
    }

    public int getOffset() {
        return offset;
    }

    public int getShiftedOrdinal() {
        return this.getParent().getBaseScreen() + this.ordinal() - this.getParent().ordinal();
    }

    public String getTabImageFile() {
        //return "/Reika/RotaryCraft/Textures/GUI/Handbook/tabs_"+this.getParent().name().toLowerCase()+".png";
        return "/screen/handbook/tabs_" + TOC.getParent().name().toLowerCase(Locale.ENGLISH) + ".png";
    }

    public int getTabRow() {
        return (this.getRelativePage() / 12) * 20;
    }

    public int getTabColumn() {
        return (this.getRelativePage() % 12) * 20;
    }

    public int getRelativePage() {
        int offset = this.ordinal() - this.getParent().ordinal();
        return offset;
    }

    public int getRelativeTabPosn() {
        int offset = this.ordinal() - this.getParent().ordinal();
        return offset - this.getRelativeScreen() * GuiHandbook.PAGES_PER_SCREEN;
    }

    public int getRelativeScreen() {
        int offset = this.ordinal() - this.getParent().ordinal();
        return offset / GuiHandbook.PAGES_PER_SCREEN;
    }

    public HandbookRegistry getParent() {
        HandbookRegistry parent = null;
        for (HandbookRegistry handbookRegistry : tabList) {
            if (handbookRegistry.isParent) {
                if (this.ordinal() >= handbookRegistry.ordinal()) {
                    parent = handbookRegistry;
                }
            }
        }
        //ReikaJavaLibrary.pConsole("Setting parent for "+this+" to "+parent);
        return parent;
    }

    public boolean isPlainGui() {
        //ReikaJavaLibrary.pConsole(this);
        if (this == TOC)
            return true;
        if (this == TIERS)
            return false;
        if (this == TIMING)
            return false;
        if (this == ALERTS)
            return false;
        if (this == PACKMODS)
            return false;
        if (this.getParent() == TERMS)
            return true;
        if (isParent)
            return true;
        if (this == MODINTERFACE)
            return true;
        if (this == ENCHANTING)
            return true;
        if (this == BEDDUST)
            return true;
        if (this == SPAWNERS)
            return true;
        if (this == LUBE)
            return true;
        if (this == BEARINGS)
            return true;
        if (this == CANOLA)
            return true;
        if (this == ALUMINUM)
            return true;
        return this == COMPUTERCRAFT;
    }

    public String getTitle() {
        if (this == TOC)
            return "Table Of Contents";
        if (isParent)
            return title;
        if (this.getParent() == ENGINEDESC) {
            if (this == SOLAR)
                return MachineRegistry.SOLARTOWER.getName();
//            todo else
//                return RotaryNames.getEngineName(offset);
        }
        if (this.isMachine() || this.getParent() == CONVERTERDESC)
            return machine.getName();
//     todo   if (machine == MachineRegistry.ADVANCEDGEARS)
//            return RotaryNames.getAdvGearName(offset);
        if (this.getParent() == TRANSDESC)
            return machine.getName();
        if (this.getParent() == TOOLDESC && item != null)
            return item.toString(); //todo itemname
        return title;
    }

    public boolean isCrafting() {
        if (isParent)
            return false;
        if (this.isSmelting())
            return false;
        if (this.getParent() == TOC || this.getParent() == TERMS)
            return false;
        if (this == MODINTERFACE)
            return false;
        if (this == COMPUTERCRAFT)
            return false;
        if (this == ENCHANTING)
            return false;
        if (this == LUBE)
            return false;
        if (this == BEARINGS)
            return false;
        if (this == STEELINGOT)
            return false;
        if (this == NETHERDUST)
            return false;
        if (this == GLASS)
            return false;
        if (this == EXTRACTS)
            return false;
        if (this == COMPACTS)
            return false;
        if (this == BEDDUST)
            return false;
        if (this == SAWDUST)
            return false;
        if (this == SPAWNERS)
            return false;
        if (this == YEAST)
            return false;
        if (this == ALUMINUM)
            return false;
        if (this == RAILGUNAMMO)
            return false;
        if (this == CANOLA)
            return false;
        if (this == FLAKES)
            return false;
        if (this == SILVERINGOT)
            return false;
        if (this == JETPACK)
            return false;
        if (this == JUMPBOOTS)
            return false;
        if (this == BEDTOOLS)
            return false;
        if (this == BEDARMOR)
            return false;
//        if (this == STRONGSPRING)
//            return false;
//        if (this == BEDINGOT)
//        	return false;
        if (this == ALLOYING)
            return false;
        return this != COKE;
    }

    public List<ItemStack> getCrafting() {
        if (!this.isCrafting())
            return null;
        if (this == SHAFT) {
            List<ItemStack> li = new ArrayList<ItemStack>();
//            for (int i = 0; i < MaterialRegistry.values().length; i++) {
//                li.add(MachineRegistry.SHAFT.getCraftedMetadataProduct(i));
//            }
            li.add(RotaryBlocks.WOOD_SHAFT.get().asItem().getDefaultInstance());
            li.add(RotaryBlocks.HSLA_SHAFT.get().asItem().getDefaultInstance());
            li.add(RotaryBlocks.TUNGSTEN_SHAFT.get().asItem().getDefaultInstance());
            li.add(RotaryBlocks.DIAMOND_SHAFT.get().asItem().getDefaultInstance());
            li.add(RotaryBlocks.BEDROCK_SHAFT.get().asItem().getDefaultInstance());

            return li;
        }
        if (this == GEARBOX) {
            List<ItemStack> li = new ArrayList<ItemStack>();
//            for (int i = 0; i < MaterialRegistry.values().length; i++) {
//                li.add(MachineRegistry.GEARBOX.getCraftedMetadataProduct(i));
//            }
            li.add(RotaryBlocks.HSLA_GEARBOX_2x.get().asItem().getDefaultInstance());
            li.add(RotaryBlocks.HSLA_GEARBOX_4x.get().asItem().getDefaultInstance());
            li.add(RotaryBlocks.HSLA_GEARBOX_8x.get().asItem().getDefaultInstance());
            li.add(RotaryBlocks.HSLA_GEARBOX_16x.get().asItem().getDefaultInstance());
            li.add(RotaryBlocks.DIAMOND_GEARBOX_2x.get().asItem().getDefaultInstance());
            li.add(RotaryBlocks.DIAMOND_GEARBOX_4x.get().asItem().getDefaultInstance());
            li.add(RotaryBlocks.DIAMOND_GEARBOX_8x.get().asItem().getDefaultInstance());
            li.add(RotaryBlocks.DIAMOND_GEARBOX_16x.get().asItem().getDefaultInstance());
            li.add(RotaryBlocks.BEDROCK_GEARBOX_2x.get().asItem().getDefaultInstance());
            li.add(RotaryBlocks.BEDROCK_GEARBOX_4x.get().asItem().getDefaultInstance());
            li.add(RotaryBlocks.BEDROCK_GEARBOX_8x.get().asItem().getDefaultInstance());
            li.add(RotaryBlocks.BEDROCK_GEARBOX_16x.get().asItem().getDefaultInstance());

            return li;
        }
        /*todo if (this == COIL) {
            List<ItemStack> li = new ArrayList<ItemStack>();
            ItemStack is = MachineRegistry.ADVANCEDGEARS.getCraftedMetadataProduct(2);
            li.add(is);
            is = is.copy();
            is.save(new CompoundTag());
            is.getTag().putBoolean("bedrock", true);
            li.add(is);
            return li;
        }
        if (this == FLYWHEEL) {
            List<ItemStack> li = new ArrayList<ItemStack>();
            for (int i = 0; i < 4; i++) {
                li.add(MachineRegistry.FLYWHEEL.getCraftedMetadataProduct(i));
            }
            return li;
        }
        if (crafted != null)
            return ReikaJavaLibrary.makeListFrom(crafted);
        if (machine != null && machine.isPipe())
            return ReikaJavaLibrary.makeListFrom(machine.getCraftedProduct());*/
        if (this == BEDTOOLS) {
            List<ItemStack> li = new ArrayList<ItemStack>();
            li.add(RotaryItems.BEDROCK_ALLOY_PICK.get().getDefaultInstance());
            li.add(RotaryItems.BEDROCK_ALLOY_SHOVEL.get().getDefaultInstance());
            li.add(RotaryItems.BEDROCK_ALLOY_AXE.get().getDefaultInstance());
            li.add(RotaryItems.BEDROCK_ALLOY_SWORD.get().getDefaultInstance());
            li.add(RotaryItems.BEDROCK_ALLOY_HOE.get().getDefaultInstance());
            li.add(RotaryItems.BEDROCK_ALLOY_SHEARS.get().getDefaultInstance());
//            li.add(RotaryItems.BEDROCK_SICKLE.get().getDefaultInstance());
            return li;
        }
        if (this == BEDARMOR) {
            List<ItemStack> li = new ArrayList<ItemStack>();
            li.add(RotaryItems.BEDROCK_ALLOY_HELMET.get().getDefaultInstance());
            li.add(RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance());
            li.add(RotaryItems.BEDROCK_ALLOY_LEGGINGS.get().getDefaultInstance());
            li.add(RotaryItems.BEDROCK_ALLOY_BOOTS.get().getDefaultInstance());
            return li;
        }
        if (this == STEELTOOLS) {
            List<ItemStack> li = new ArrayList<ItemStack>();
            li.add(RotaryItems.HSLA_STEEL_PICKAXE.get().getDefaultInstance());
            li.add(RotaryItems.HSLA_STEEL_SHOVEL.get().getDefaultInstance());
            li.add(RotaryItems.HSLA_STEEL_AXE.get().getDefaultInstance());
            li.add(RotaryItems.HSLA_STEEL_SWORD.get().getDefaultInstance());
            li.add(RotaryItems.HSLA_STEEL_HOE.get().getDefaultInstance());
//        todo    li.add(RotaryItems.HSLA_STEEL_SHEARS.get().getDefaultInstance());
//            li.add(RotaryItems.HSLA_SICKLE.get().getDefaultInstance());
            return li;
        }
        if (this == STEELARMOR) {
            List<ItemStack> li = new ArrayList<ItemStack>();
            li.add(RotaryItems.HSLA_HELMET.get().getDefaultInstance());
            li.add(RotaryItems.HSLA_CHESTPLATE.get().getDefaultInstance());
            li.add(RotaryItems.HSLA_LEGGINGS.get().getDefaultInstance());
            li.add(RotaryItems.HSLA_BOOTS.get().getDefaultInstance());
            return li;
        }
        if (this == SOLAR)
            return ReikaJavaLibrary.makeListFrom(MachineRegistry.SOLARTOWER.getBlockState().getBlock().asItem().getDefaultInstance());
        if (this.getParent() == ENGINEDESC)
            return ReikaJavaLibrary.makeListFrom(EngineType.engineList[offset].getCraftedProduct());
/*  todo    if (machine == MachineRegistry.ADVANCEDGEARS)
            return ReikaJavaLibrary.makeListFrom(MachineRegistry.ADVANCEDGEARS.getCraftedMetadataProduct(offset));
        if (this.getParent() == TRANSDESC || this.isMachine()) {
            if (machine.hasCustomPlacerItem())
                return ReikaJavaLibrary.makeListFrom(machine.getCraftedMetadataProduct(0));
            return ReikaJavaLibrary.makeListFrom(machine.getCraftedProduct());
        }*/
        if (this == DECOBLOCKS) {
            List<ItemStack> li = new ArrayList<>();
            li.add(RotaryBlocks.HSLA_STEEL_BLOCK.get().asItem().getDefaultInstance());
//   todo         li.add(RotaryItems.ANTHRABLOCK);
//            li.add(RotaryItems.LONSBLOCK);
            return li;
        }
    /*   todo if (this == SLIDES) {
            List<ItemStack> li = new ArrayList<>();
            for (int i = 0; i < RotaryItems.SLIDE.getNumberMetadatas(); i++)
                li.add(RotaryItems.SLIDE.getStackOfMetadata(i));
            return li;
        }
        //if (this == BEDINGOT)
        //	return ReikaJavaLibrary.makeListFrom(RotaryItems.BEDROCK_ALLOY_INGOT);
        if (this == ALLOYING) {
            ArrayList<ItemStack> is = new ArrayList<>();
            ArrayList<BlastFurnacePattern> li = RecipesBlastFurnace.getRecipes().getAllAlloyingRecipes();
            for (BlastFurnacePattern p : li) {
                is.add(p.outputItem());
            }
            return is;
        }*/
        if (this == COKE)
            return ReikaJavaLibrary.makeListFrom(RotaryItems.COKE.get().getDefaultInstance());
        if (this == AMMONIUM)
            return ReikaJavaLibrary.makeListFrom(RotaryItems.NITRATE.get().getDefaultInstance());
        if (this == SALT)
            return ReikaJavaLibrary.makeListFrom(RotaryItems.SALT.get().getDefaultInstance());
        if (this == SILVERIODIDE)
            return ReikaJavaLibrary.makeListFrom(RotaryItems.SILVERIODIDE.get().getDefaultInstance());
        if (this == EXPLOSIVES)
            return ReikaJavaLibrary.makeListFrom(RotaryItems.SHELL.get().getDefaultInstance());
//    todo    if (this == MINECART)
//            return ReikaJavaLibrary.makeListFrom(RotaryItems.MINECART.get().getDefaultInstance());
        return ReikaJavaLibrary.makeListFrom(RotaryItems.HSLA_PLATE.get().getDefaultInstance());
    }

    public boolean isCustomRecipe() {
        if (this.getParent() == ENGINEDESC)
            return true;
        if (this.getParent() == TRANSDESC)
            return true;
        if (this.getParent() == CONVERTERDESC)
            return true;
        if (this.isMachine())
            return true;
        return machine != null && machine.isPipe();
    }

    public int getTabIconIndex() {
        if (this == SHAFT)
            return MaterialRegistry.STEEL.ordinal();
        if (this == GEARBOX)
            return MaterialRegistry.STEEL.ordinal();
        if (this == FLYWHEEL)
            return 3;
        if (this == DECOBLOCKS)
            return 1;
        return 0;
    }

    public boolean isSmelting() {
        if (this == ETHANOL)
            return true;
        return this == TUNGSTEN;
    }

    public ItemStack getSmelting() {
        if (!this.isSmelting())
            return null;
        if (this == ETHANOL)
            return RotaryItems.ETHANOL.get().getDefaultInstance();
        if (this == TUNGSTEN)
            return RotaryItems.TUNGSTEN_INGOT.get().getDefaultInstance();
//todo        return RotaryItems.BARREL;
        return ItemStack.EMPTY;
    }

    public ItemStack getTabIcon() {
        if (this == TOC)
            return RotaryItems.HANDBOOK.get().getDefaultInstance();
        if (this == TERMS)
            return RotaryItems.HANDBOOK.get().getDefaultInstance();
        if (this == PHYSICS)
            return new ItemStack(Items.BOOK);
        if (this == MATERIAL)
            return RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance();
        if (this == SHAFTS)
            return RotaryBlocks.DIAMOND_SHAFT.get().asItem().getDefaultInstance();
//        if (this == FLYWHEELS)
//            return MachineRegistry.FLYWHEEL.getCraftedMetadataProduct(0);
//        if (this == TIERS)
//            return MachineRegistry.EMP.getCraftedProduct();
        if (this == LUBE)
            return RotaryItems.LUBE_BUCKET.get().getDefaultInstance();
        if (this == BEARINGS)
            return GearboxTypes.TUNGSTEN.getPart(GearboxTypes.GearPart.BEARING);
//        if (this == MODINTERFACE)
//            return MachineRegistry.COMPRESSOR.getCraftedProduct();
        if (this == ENCHANTING)
            return new ItemStack(Items.ENCHANTED_BOOK);
        if (this == TIMING)
            return new ItemStack(Items.CLOCK);
//        if (this == MUFFLING)
//            return ReikaItemHelper.whiteWool.asItemStack();
        if (this == INTERDIM)
            return new ItemStack(Blocks.NETHER_PORTAL);
//        if (this == COMPUTERCRAFT)
//            return RotaryItems.PCB;
//        if (this == TRANSFER)
//            return RotaryItems.GEARUNIT;
        if (this == ENGINES)
            return EngineType.AC.getCraftedProduct();
        if (this == INFO)
            return RotaryItems.SCREWDRIVER.get().getDefaultInstance();
        if (this == TRANS)
            return GearboxTypes.STEEL.getGearboxItem(8);
        if (this == CONVERTER)
            return MachineRegistry.MAGNETIC.getBlockState().getBlock().asItem().getDefaultInstance();
//        if (this == PRODMACHINES)
//            return MachineRegistry.BEDROCKBREAKER.getCraftedProduct();
//        if (this == PROCMACHINES)
//            return MachineRegistry.EXTRACTOR.getCraftedProduct();
//        if (this == FARMMACHINES)
//            return MachineRegistry.FAN.getCraftedProduct();
        if (this == PIPEMACHINES)
            return MachineRegistry.FUELLINE.getBlockState().getBlock().asItem().getDefaultInstance();
//        if (this == ACCMACHINES)
//            return MachineRegistry.FRICTION.getCraftedProduct();
//        if (this == WEPMACHINES)
//            return MachineRegistry.RAILGUN.getCraftedProduct();
        if (this == COSMACHINES)
            return MachineRegistry.MUSICBOX.getBlockState().getBlock().asItem().getDefaultInstance();
//        if (this == SURVMACHINES)
//            return MachineRegistry.GPR.getCraftedProduct();
        if (this == UTILMACHINES)
            return MachineRegistry.FLOODLIGHT.getBlockState().getBlock().asItem().getDefaultInstance();
//        if (this == TOOLS)
//            return RotaryItems.MOTION.get().getDefaultInstance();
        if (this == RESOURCE)
            return RotaryItems.BEDROCK_DUST.get().getDefaultInstance();
//        if (this == FLAKES)
//            return RotaryItems.GOLDOREFLAKES;
       /* todo if (this == BEDTOOLS)
            return RotaryItems.BEDROCK_ALLOY_PICK.getEnchantedStack();
        if (this == BEDARMOR)
            return RotaryItems.BEDROCK_ALLOY_HELMET.getEnchantedStack();
        if (this == STEELTOOLS)
            return RotaryItems.HSLA_STEEL_PICKAXE.getEnchantedStack();
        if (this == STEELARMOR)
            return RotaryItems.HSLA_HELMET.getEnchantedStack();
        if (this == JETPACK)
            return RotaryItems.BEDROCK_ALLOY_CHESTPLATE.getEnchantedStack();*/
        if (this == JUMPBOOTS)
            return RotaryItems.JUMP.get().getDefaultInstance();
        if (this.isCrafting())
            return this.getCrafting().get(this.getTabIconIndex());
        if (this.isSmelting())
            return this.getSmelting();
        if (this == STEELINGOT)
            return RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance();
        if (this == NETHERDUST)
            return RotaryItems.NETHERRACK_DUST.get().getDefaultInstance();
        if (this == SAWDUST)
            return RotaryItems.SAWDUST.get().getDefaultInstance();
        if (this == BEDDUST)
            return RotaryItems.BEDROCK_DUST.get().getDefaultInstance();
//        todo if (this == EXTRACTS)
//            return RotaryItems.GOLDOREDUST;
//        if (this == COMPACTS)
//            return RotaryItems.PRISMANE;
        if (this == GLASS)
            return RotaryBlocks.BLASTGLASS.get().asItem().getDefaultInstance();
        if (this == YEAST)
            return RotaryItems.SLUDGE.get().getDefaultInstance();
        if (this == SALT)
            return RotaryItems.SALT.get().getDefaultInstance();
        if (this == ALUMINUM)
            return RotaryItems.ALUMINUM_ALLOY_POWDER.get().getDefaultInstance();
//        if (this == RAILGUNAMMO)
//            return RotaryItems.RAILGUNAMMO.get().getDefaultInstance();
            if (this == CANOLA)
                return RotaryItems.CANOLA_SEEDS.get().getDefaultInstance();
//        if (this == SILVERINGOT)
//            return RotaryItems.SILVERINGOT;
//        if (this == STRONGSPRING)
//            return RotaryItems.STRONGCOIL.get().getDefaultInstance();
        if (this == BEDINGOT)
        	return RotaryItems.BEDROCK_ALLOY_INGOT.get().getDefaultInstance();
        if (this == ALLOYING)
            return RotaryItems.BEDROCK_ALLOY_INGOT.get().getDefaultInstance();
        if (this == COKE)
            return RotaryItems.COKE.get().getDefaultInstance();
        if (this == ALERTS || this == PACKMODS)
            return RotaryItems.HSLA_STEEL_GEAR.get().getDefaultInstance();
        return null;
    }

    public String getData() {
        if (this == TOC)
            return RotaryDescriptions.getTOC();
        if (this == ALERTS || this == PACKMODS)
            return "";
        return RotaryDescriptions.getData(this);
    }

    public String getNotes(int subpage) {
        return RotaryDescriptions.getNotes(this, subpage);
    }

    public boolean hasSubpages() {
        if (isParent)
            return false;
        if (this.getParent() == ENGINEDESC)
            return true;
        if (this.getParent() == CONVERTERDESC)
            return true;
        if (this.isMachine())
            return true;
        if (this == TIERS)
            return true;
        if (this == COMPUTERCRAFT)
            return true;
        if (this == ALERTS)
            return !HandbookNotifications.instance.getNewAlerts().isEmpty();
//        if (this == PACKMODS)
//            return PackModificationTracker.instance.modificationsExist(RotaryCraft.getInstance());
        return false;
    }

    public boolean sameTextAllSubpages() {
        return this == TIERS;
    }

    @Override
    public boolean hasMachineRender() {
        return this.isEngine() || this.isTrans() || this.isMachine() || this.getParent() == CONVERTERDESC;
    }

    @Override
    public boolean isConfigDisabled() {
        return machine != null && machine.isConfigDisabled();
    }

    /*
    public boolean isDummiedOut() {
        if (this == ALERTS)
            return HandbookNotifications.instance.getNewAlerts().isEmpty();
        if (this == PACKMODS)
            return !PackModificationTracker.instance.modificationsExist(RotaryCraft.getInstance());
        return false;
    }
     */
    public int getBonusSubpages() {
        return RotaryDescriptions.getNotesLength(this) - 1;
    }

}
