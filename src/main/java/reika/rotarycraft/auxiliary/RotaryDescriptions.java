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

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.common.NeoForge;
import net.neoforged.fluids.FluidType;
import net.neoforged.fml.loading.FMLLoader;
import reika.dragonapi.instantiable.data.maps.PluralMap;
import reika.dragonapi.instantiable.io.XMLInterface;
import reika.dragonapi.libraries.mathsci.ReikaEngLibrary;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blockentity.EnergyToPowerBase;
import reika.rotarycraft.blockentities.BlockEntityItemCannon;
import reika.rotarycraft.blockentities.BlockEntityItemRefresher;
import reika.rotarycraft.blockentities.BlockEntityPlayerDetector;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityHeater;
import reika.rotarycraft.blockentities.decorative.BlockEntityMusicBox;
import reika.rotarycraft.blockentities.farming.BlockEntityComposter;
import reika.rotarycraft.blockentities.production.BlockEntityObsidianMaker;
import reika.rotarycraft.blockentities.production.BlockEntitySolarTower;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.blockentities.transmission.BlockEntityGearbox;
import reika.rotarycraft.blockentities.weaponry.BlockEntityContainment;
import reika.rotarycraft.blockentities.weaponry.BlockEntityHeatRay;
import reika.rotarycraft.registry.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public final class RotaryDescriptions {

    private static final String DESC_SUFFIX = ":desc";
    private static final String NOTE_SUFFIX = ":note";
    private static final String SUBNOTE_SUFFIX = ":sub";
    private static final HashMap<HandbookRegistry, String> data = new HashMap<>();
    private static final PluralMap<String> notes = new PluralMap<>(2);
    private static final HashMap<MachineRegistry, Object[]> machineData = new HashMap<>();
    private static final PluralMap<Object[]> machineNotes = new PluralMap<>(2);
    private static final HashMap<HandbookRegistry, Object[]> miscData = new HashMap<>();
    private static final HashMap<HandbookRegistry, Integer> lengths = new HashMap<>();
    private static final ArrayList<HandbookRegistry> categories = new ArrayList<>();
    private static String PARENT = getParent(true);
    private static final XMLInterface parents = loadData("categories");
    private static final XMLInterface machines = loadData("machines");
    private static final XMLInterface trans = loadData("trans");
    private static final XMLInterface converter = loadData("converter");
    private static final XMLInterface engines = loadData("engines");
    private static final XMLInterface tools = loadData("tools");
    private static final XMLInterface resources = loadData("resource");
    private static final XMLInterface miscs = loadData("misc");
    private static final XMLInterface infos = loadData("info");

    static {
        loadNumericalData();

        NeoForge.EVENT_BUS.register(new ReloadListener());
    }

    private static XMLInterface loadData(String name) {
        XMLInterface xml = new XMLInterface(RotaryCraft.class, PARENT + name + ".xml", false); // !ReikaObfuscationHelper.isDeObfEnvironment());
        xml.setFallback(getParent(false) + name + ".xml");
        xml.init();
        return xml;
    }

    public static void addCategory(HandbookRegistry h) {
        categories.add(h);
    }

    private static String getParent(boolean locale) {
        return locale && FMLLoader.getDist() == Dist.CLIENT ? getLocalizedParent() : "resources/";
    }

    private static String getLocalizedParent() {
        String language = Minecraft.getInstance().getLanguageManager().getSelected();
//        String lang = language.getCode();
        if (hasLocalizedFor(language) && !"en_us".equals(language))
            return "resources/" + language + "/";
        return "resources/";
    }

    private static boolean hasLocalizedFor(String language) {
//        String lang = language.getCode();
        try (InputStream o = RotaryCraft.class.getResourceAsStream("resources/" + language + "/categories.xml")) {
            return o != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getCategoryCount() {
        return categories.size();
    }

    public static String getTOC() {
        List<HandbookRegistry> toctabs = HandbookRegistry.getTOCTabs();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < toctabs.size(); i++) {
            HandbookRegistry h = toctabs.get(i);
            sb.append("Page ");
            sb.append(h.getScreen());
            sb.append(" - ");
            sb.append(h.getTOCTitle());
            if (i < toctabs.size() - 1)
                sb.append("\n");
        }
        return sb.toString();
    }

    private static void addData(MachineRegistry m, Object... data) {
        machineData.put(m, data);
    }

    private static void addData(HandbookRegistry h, Object... data) {
        miscData.put(h, data);
    }

    private static void addData(HandbookRegistry h, int[] data) {
        Object[] o = new Object[data.length];
        for (int i = 0; i < o.length; i++)
            o[i] = data[i];
        miscData.put(h, o);
    }

    private static void addNotes(MachineRegistry m, Object... data) {
        machineNotes.put(data, m, 0);
    }

    private static void addSubNotes(MachineRegistry m, int subpage, Object... data) {
        machineNotes.put(data, m, subpage);
    }

    /**
     * Call this from the SERVER side!
     */
    public static void reload() {
        PARENT = getParent(true);

        loadNumericalData();

        machines.reread();
        trans.reread();
        engines.reread();
        tools.reread();
        resources.reread();
        miscs.reread();
        infos.reread();

        parents.reread();

        loadData();
    }

    private static void addEntry(HandbookRegistry h, String sg) {
        data.put(h, sg);
    }

    public static void loadData() {
        List<HandbookRegistry> parenttabs = HandbookRegistry.getCategoryTabs(false);

        List<HandbookRegistry> enginetabs = HandbookRegistry.getEngineTabs();
        List<HandbookRegistry> transtabs = HandbookRegistry.getTransTabs();
        List<HandbookRegistry> convertertabs = HandbookRegistry.getConverterTabs();
        List<HandbookRegistry> machinetabs = HandbookRegistry.getMachineTabs();
        List<HandbookRegistry> tooltabs = HandbookRegistry.getToolTabs();
        List<HandbookRegistry> resourcetabs = HandbookRegistry.getResourceTabs();
        List<HandbookRegistry> misctabs = HandbookRegistry.getMiscTabs();
        List<HandbookRegistry> infotabs = HandbookRegistry.getInfoTabs();

        for (HandbookRegistry h : parenttabs) {
            String desc = parents.getValueAtNode("categories:" + h.name().toLowerCase(Locale.ENGLISH).substring(0, h.name().length() - 4));
            addEntry(h, desc);
        }

        for (HandbookRegistry h : machinetabs) {
            loadMachineTab(h, machines);
        }

        for (HandbookRegistry h : convertertabs) {
            loadMachineTab(h, converter);
        }

        for (HandbookRegistry h : transtabs) {
            MachineRegistry m = h.getMachine();
            String desc = trans.getValueAtNode("trans:" + h.name().toLowerCase(Locale.ENGLISH));
            Collection<String> sub = trans.getNodesWithin("trans:" + h.name().toLowerCase(Locale.ENGLISH) + SUBNOTE_SUFFIX);

            if (sub != null && !sub.isEmpty()) {
                int k = 0;
                for (String s : sub) {
                    String val = trans.getValueAtNode(s);
                    if (k == 0) {
                        val = String.format(val, machineData.get(m));
                        val = String.format(val, miscData.get(h));
                        addEntry(h, val);
                    } else {
                        val = String.format(val, machineNotes.get(m, k));
                        notes.put(val, h, k - 1);
                    }
                    k++;
                    lengths.put(h, k);
                }
                continue;
            }

            if (XMLInterface.NULL_VALUE.equals(desc))
                desc = "There is no handbook data for this machine yet.";
            //ReikaJavaLibrary.pConsole(h.name().toLowerCase()+":"+desc);

            if (machineData.containsKey(m))
                desc = String.format(desc, machineData.get(m));
            if (miscData.containsKey(h))
                desc = String.format(desc, miscData.get(h));
            addEntry(h, desc);
        }

        for (HandbookRegistry h : tooltabs) {
            String desc = tools.getValueAtNode("tools:" + h.name().toLowerCase(Locale.ENGLISH));
            Collection<String> sub = tools.getNodesWithin("tools:" + h.name().toLowerCase(Locale.ENGLISH) + SUBNOTE_SUFFIX);

            if (sub != null && !sub.isEmpty()) {
                int k = 0;
                for (String s : sub) {
                    String val = tools.getValueAtNode(s);
                    if (k == 0) {
                        val = String.format(val, miscData.get(h));
                        addEntry(h, val);
                    } else {
                        notes.put(val, h, k - 1);
                    }
                    k++;
                    lengths.put(h, k);
                }
                continue;
            }

            addEntry(h, desc);
        }

        for (HandbookRegistry h : resourcetabs) {
            String desc = resources.getValueAtNode("resource:" + h.name().toLowerCase(Locale.ENGLISH));
            desc = String.format(desc, miscData.get(h));
            addEntry(h, desc);
        }

        for (HandbookRegistry h : misctabs) {
            String desc = miscs.getValueAtNode("misc:" + h.name().toLowerCase(Locale.ENGLISH));
            //ReikaJavaLibrary.pConsole(desc);
            desc = String.format(desc, miscData.get(h));
            addEntry(h, desc);
        }

        for (HandbookRegistry h : infotabs) {
            String desc = infos.getValueAtNode("info:" + h.name().toLowerCase(Locale.ENGLISH));
            desc = String.format(desc, miscData.get(h));
            addEntry(h, desc);
        }

        for (HandbookRegistry h : enginetabs) {
            String desc;
            String aux;
            Collection<String> sub = null;
            EngineType e = h.getEngine();
            if (e != null) {
                desc = engines.getValueAtNode("engines:" + e.name().toLowerCase(Locale.ENGLISH) + DESC_SUFFIX);
                aux = engines.getValueAtNode("engines:" + e.name().toLowerCase(Locale.ENGLISH) + NOTE_SUFFIX);
                sub = engines.getNodesWithin("engines:" + e.name().toLowerCase(Locale.ENGLISH) + NOTE_SUFFIX + SUBNOTE_SUFFIX);

                desc = RotaryAux.formatTorqueSpeedPowerForBook(desc, e.getTorque(), e.getSpeed(), e.getPower());
                aux = RotaryAux.formatTorqueSpeedPowerForBook(aux, e.getTorque(), e.getSpeed(), e.getPower());
            } else {
                desc = engines.getValueAtNode("engines:" + "solar".toLowerCase(Locale.ENGLISH) + DESC_SUFFIX);
                aux = engines.getValueAtNode("engines:" + "solar".toLowerCase(Locale.ENGLISH) + NOTE_SUFFIX);

                desc = String.format(desc, BlockEntitySolarTower.GENOMEGA);
                aux = String.format(aux, BlockEntitySolarTower.GENOMEGA);
            }

            data.put(h, desc);
            notes.put(aux, h, 0);

            if (sub != null) {
                int k = 0;
                for (String s : sub) {
                    String val = engines.getValueAtNode(s);
                    if (k == 0 && e != null) {
                        val = RotaryAux.formatTorqueSpeedPowerForBook(val, e.getTorque(), e.getSpeed(), e.getPower());
                    }
                    notes.put(val, h, k);
                    k++;
                    lengths.put(h, k);
                }
            }
        }
    }

    private static void loadMachineTab(HandbookRegistry h, XMLInterface xml) {
        MachineRegistry m = h.getMachine();
        String desc = xml.getValueAtNode("machines:" + m.name().toLowerCase(Locale.ENGLISH) + DESC_SUFFIX);
        String aux = xml.getValueAtNode("machines:" + m.name().toLowerCase(Locale.ENGLISH) + NOTE_SUFFIX);
        Collection<String> sub = machines.getNodesWithin("machines:" + m.name().toLowerCase(Locale.ENGLISH) + NOTE_SUFFIX + SUBNOTE_SUFFIX);
        desc = RotaryAux.formatValuesForBook(desc, machineData.get(m));
        aux = RotaryAux.formatValuesForBook(aux, machineNotes.get(m, 0));

        if (XMLInterface.NULL_VALUE.equals(desc))
            desc = "There is no handbook data for this machine yet.";

        //ReikaJavaLibrary.pConsole(m.name().toLowerCase()+":"+desc);

//        if (m.isDummiedOut()) {
//            desc += "\nThis machine is currently unavailable.";
//            if (m.getModDependency() != null && !m.getModDependency().isLoaded())
//                desc += "\nThis machine depends on another mod.";
//            aux += "\nNote: Dummied Out";
//        }
//        if (m.hasPrerequisite()) {
//            aux += "\nDependencies: " + m.getPrerequisite();
//        }
//        if (m.isIncomplete()) {
//            desc += "\nThis machine is incomplete. Use at your own risk.";
//        }

        addEntry(h, desc);
        notes.put(aux, h, 0);

        if (sub != null) {
            int k = 0;
            for (String s : sub) {
                String val = xml.getValueAtNode(s);
                val = RotaryAux.formatValuesForBook(val, machineNotes.get(m, k));
                notes.put(val, h, k);
                k++;
                lengths.put(h, k);
            }
        }
    }

    public static String getData(HandbookRegistry h) {
        if (!data.containsKey(h))
            return "";
        return data.get(h);
    }

    public static String getNotes(HandbookRegistry h, int subpage) {
        subpage--;
        if (!notes.containsKeyV(h, subpage))
            return "";
        return notes.get(h, subpage);
    }

    public static int getNotesLength(HandbookRegistry h) {
        return lengths.containsKey(h) ? lengths.get(h) : 1;
    }

    private static void loadNumericalData() {
        addData(HandbookRegistry.MATERIAL,
                ReikaEngLibrary.rhowood,
                ReikaMathLibrary.getThousandBase(ReikaEngLibrary.Twood),
                ReikaEngLibrary.getSIPrefix(ReikaEngLibrary.Twood),
                ReikaMathLibrary.getThousandBase(ReikaEngLibrary.Swood),
                ReikaEngLibrary.getSIPrefix(ReikaEngLibrary.Swood),

                ReikaEngLibrary.rhorock,
                ReikaMathLibrary.getThousandBase(ReikaEngLibrary.Tstone),
                ReikaEngLibrary.getSIPrefix(ReikaEngLibrary.Tstone),
                ReikaMathLibrary.getThousandBase(ReikaEngLibrary.Sstone),
                ReikaEngLibrary.getSIPrefix(ReikaEngLibrary.Sstone),

                ReikaEngLibrary.rhoiron,
                ReikaMathLibrary.getThousandBase(ReikaEngLibrary.Tiron),
                ReikaEngLibrary.getSIPrefix(ReikaEngLibrary.Tiron),
                ReikaMathLibrary.getThousandBase(ReikaEngLibrary.Siron),
                ReikaEngLibrary.getSIPrefix(ReikaEngLibrary.Siron),

                ReikaEngLibrary.rhoiron,
                ReikaMathLibrary.getThousandBase(ReikaEngLibrary.Tsteel),
                ReikaEngLibrary.getSIPrefix(ReikaEngLibrary.Tsteel),
                ReikaMathLibrary.getThousandBase(ReikaEngLibrary.Ssteel),
                ReikaEngLibrary.getSIPrefix(ReikaEngLibrary.Ssteel),

                ReikaEngLibrary.rhogold,
                ReikaMathLibrary.getThousandBase(ReikaEngLibrary.Tgold),
                ReikaEngLibrary.getSIPrefix(ReikaEngLibrary.Tgold),
                ReikaMathLibrary.getThousandBase(ReikaEngLibrary.Sgold),
                ReikaEngLibrary.getSIPrefix(ReikaEngLibrary.Sgold),

                RotaryAux.tungstenDensity,
                ReikaMathLibrary.getThousandBase(ReikaEngLibrary.Ttungsten),
                ReikaEngLibrary.getSIPrefix(ReikaEngLibrary.Ttungsten),
                ReikaMathLibrary.getThousandBase(ReikaEngLibrary.Stungsten),
                ReikaEngLibrary.getSIPrefix(ReikaEngLibrary.Stungsten),

                ReikaEngLibrary.rhodiamond,
                ReikaMathLibrary.getThousandBase(ReikaEngLibrary.Tdiamond),
                ReikaEngLibrary.getSIPrefix(ReikaEngLibrary.Tdiamond),
                ReikaMathLibrary.getThousandBase(ReikaEngLibrary.Sdiamond),
                ReikaEngLibrary.getSIPrefix(ReikaEngLibrary.Sdiamond)
        );

        addData(HandbookRegistry.SHAFTS, MaterialRegistry.getAllLimitLoadsAsInts());
        addData(HandbookRegistry.FLYWHEELS, Flywheels.getLimitsForDisplay());

        addData(HandbookRegistry.MODINTERFACE);
        //ReikaMathLibrary.getThousandBase(ReikaRFHelper.getWattsPerRF()),
        //ReikaEngLibrary.getSIPrefix(ReikaRFHelper.getWattsPerRF()),

        //ReikaMathLibrary.getThousandBase(ReikaBuildCraftHelper.getFuelBucketEnergy()),
        //ReikaEngLibrary.getSIPrefix(ReikaBuildCraftHelper.getFuelBucketEnergy()),

//todo                BlockEntityExtractor.oreCopy,
//                BlockEntityExtractor.oreCopyNether,
//                BlockEntityExtractor.oreCopyRare


//
//        ArrayList<MachineRegistry> li = MachineRegistry.getEnchantableMachineList();
        //        for (int i = 0; i < li.size(); i++) {
//            sb.append(li.get(i).getName());
//            if (i < li.size() - 1)
//                sb.append(", ");
//        }
        addData(HandbookRegistry.ENCHANTING, "");
//
        addData(HandbookRegistry.BEARINGS, (int) (BlockEntityGearbox.BEARINGREDUCTION * 100) + "%", (int) (BlockEntityGearbox.BEARINGINCREASE * 100) + "%");
//
//        addData(MachineRegistry.BORER, BlockEntityBorer.DIGPOWER * 10, BlockEntityBorer.OBSIDIANTORQUE);
//        addData(MachineRegistry.PILEDRIVER, BlockEntityPileDriver.BASEPOWER);
//        addData(MachineRegistry.EXTRACTOR, PowerReceivers.EXTRACTOR.getMinTorque(0), PowerReceivers.EXTRACTOR.getMinSpeed(2));
        addData(MachineRegistry.RESERVOIR, BlockEntityReservoir.CAPACITY / FluidType.BUCKET_VOLUME);
//        addData(MachineRegistry.FAN, PowerReceivers.FAN.getMinPower(), BlockEntityFan.MAXPOWER);
//        addData(MachineRegistry.COMPACTOR, BlockEntityCompactor.REQPRESS, BlockEntityCompactor.REQTEMP);
//        addData(MachineRegistry.BLASTFURNACE, BlockEntityBlastFurnace.SMELTTEMP, BlockEntityBlastFurnace.BEDROCKTEMP);
//        addData(MachineRegistry.SCALECHEST, BlockEntityScaleableChest.MAXSIZE);
//        addData(MachineRegistry.PURIFIER, BlockEntityPurifier.SMELTTEMP);
//        addData(MachineRegistry.GENERATOR, ReikaEUHelper.getWattsPerEU());
//        addData(MachineRegistry.BELT, ((BlockEntityBeltHub) MachineRegistry.BELT.createTEInstanceForRender(0)).getMaxTorque(), ((BlockEntityBeltHub) MachineRegistry.BELT.createTEInstanceForRender(0)).getMaxSmoothSpeed());
//        addData(MachineRegistry.CHAIN, ((BlockEntityBeltHub) MachineRegistry.CHAIN.createTEInstanceForRender(0)).getMaxTorque(), ((BlockEntityBeltHub) MachineRegistry.CHAIN.createTEInstanceForRender(0)).getMaxSmoothSpeed());
//        addData(MachineRegistry.DYNAMO, BlockEntityDynamo.MAXTORQUE, BlockEntityDynamo.MAXTORQUE_UPGRADE, BlockEntityDynamo.MAXOMEGA);
        addData(MachineRegistry.ITEMCANNON, BlockEntityItemCannon.STACKPOWER);
//        addData(MachineRegistry.ADVANCEDGEARS, BlockEntityAdvancedGear.WORMRATIO);
//
//        addNotes(MachineRegistry.BEDROCKBREAKER, PowerReceivers.BEDROCKBREAKER.getMinPower(), PowerReceivers.BEDROCKBREAKER.getMinTorque());
//        addNotes(MachineRegistry.FERMENTER, PowerReceivers.FERMENTER.getMinPower(), PowerReceivers.FERMENTER.getMinSpeed());
//        addNotes(MachineRegistry.GRINDER, PowerReceivers.GRINDER.getMinPower(), PowerReceivers.GRINDER.getMinTorque());
        addNotes(MachineRegistry.FLOODLIGHT, PowerReceivers.FLOODLIGHT.getMinPower());
        addNotes(MachineRegistry.HEATRAY, PowerReceivers.HEATRAY.getMinPower(), PowerReceivers.HEATRAY.getMinPower(), BlockEntityHeatRay.FALLOFF);
//        addNotes(MachineRegistry.PILEDRIVER, BlockEntityPileDriver.BASEPOWER, PowerReceivers.PILEDRIVER.getMinTorque());
        addNotes(MachineRegistry.AEROSOLIZER, PowerReceivers.AEROSOLIZER.getMinPower());
//        addNotes(MachineRegistry.LIGHTBRIDGE, PowerReceivers.LIGHTBRIDGE.getMinPower(), PowerReceivers.LIGHTBRIDGE.getMinPower() / RotaryConfig.COMMON.BRIDGERANGE.getValue());
//        addNotes(MachineRegistry.EXTRACTOR, PowerReceivers.EXTRACTOR.getMinPower(0), PowerReceivers.EXTRACTOR.getMinPower(1), PowerReceivers.EXTRACTOR.getMinPower(2), PowerReceivers.EXTRACTOR.getMinPower(3), PowerReceivers.EXTRACTOR.getMinTorque(0), PowerReceivers.EXTRACTOR.getMinTorque(3), PowerReceivers.EXTRACTOR.getMinSpeed(1), PowerReceivers.EXTRACTOR.getMinSpeed(2));
//        addNotes(MachineRegistry.PULSEJET, PowerReceivers.PULSEJET.getMinSpeed(), BlockEntityPulseFurnace.MAXTEMP);
        addNotes(MachineRegistry.PUMP, PowerReceivers.PUMP.getMinPower(), PowerReceivers.PUMP.getMinTorque());
        addNotes(MachineRegistry.RESERVOIR, BlockEntityReservoir.CAPACITY / 1000);
//        addNotes(MachineRegistry.FAN, PowerReceivers.FAN.getMinPower(), BlockEntityFan.FALLOFF, BlockEntityFan.HARVESTSPEED, BlockEntityFan.FIRESPEED);
//        addNotes(MachineRegistry.COMPACTOR, PowerReceivers.COMPACTOR.getMinPower(), PowerReceivers.COMPACTOR.getMinTorque(), BlockEntityCompactor.REQPRESS, BlockEntityCompactor.REQTEMP, BlockEntityCompactor.MAXPRESSURE, BlockEntityCompactor.MAXTEMP);
//        addNotes(MachineRegistry.AUTOBREEDER, PowerReceivers.AUTOBREEDER.getMinPower(), PowerReceivers.AUTOBREEDER.getMinPower(), BlockEntityAutoBreeder.FALLOFF);
//        addNotes(MachineRegistry.BAITBOX, PowerReceivers.BAITBOX.getMinPower(), PowerReceivers.BAITBOX.getMinPower(), BlockEntityBaitBox.FALLOFF);
//        addNotes(MachineRegistry.FIREWORK, PowerReceivers.FIREWORK.getMinPower(), PowerReceivers.FIREWORK.getMinSpeed());
//        addNotes(MachineRegistry.FRACTIONATOR, PowerReceivers.FRACTIONATOR.getMinPower(), PowerReceivers.FRACTIONATOR.getMinSpeed());
//        addNotes(MachineRegistry.GPR, PowerReceivers.GPR.getMinPower(), PowerReceivers.GPR.getMinPower());
        addNotes(MachineRegistry.HEATER, PowerReceivers.HEATER.getMinPower(), BlockEntityHeater.MAXTEMP);
        addNotes(MachineRegistry.OBSIDIAN, PowerReceivers.OBSIDIAN.getMinPower(), PowerReceivers.OBSIDIAN.getMinSpeed(), BlockEntityObsidianMaker.MAXTEMP, BlockEntityObsidianMaker.CAPACITY);
        addNotes(MachineRegistry.PLAYERDETECTOR, BlockEntityPlayerDetector.FALLOFF, BlockEntityPlayerDetector.BASESPEED, BlockEntityPlayerDetector.SPEEDFACTOR);
//        addNotes(MachineRegistry.SPAWNERCONTROLLER, PowerReceivers.SPAWNERCONTROLLER.getMinPower(), BlockEntitySpawnerController.BASEDELAY);
//        addNotes(MachineRegistry.VACUUM, PowerReceivers.VACUUM.getMinPower(), BlockEntityVacuum.FALLOFF);
//        addNotes(MachineRegistry.WOODCUTTER, PowerReceivers.WOODCUTTER.getMinPower(), PowerReceivers.WOODCUTTER.getMinTorque());
//        addNotes(MachineRegistry.MOBRADAR, PowerReceivers.MOBRADAR.getMinPower(), PowerReceivers.MOBRADAR.getMinPower(), BlockEntityMobRadar.FALLOFF);
        addNotes(MachineRegistry.TNTCANNON, PowerReceivers.TNTCANNON.getMinPower(), PowerReceivers.TNTCANNON.getMinTorque());
//        int fudge = BlockEntitySonicWeapon.fudge;
//        addNotes(MachineRegistry.SONICWEAPON, PowerReceivers.SONICWEAPON.getMinPower(), PowerReceivers.SONICWEAPON.getMinPower(), BlockEntitySonicWeapon.FALLOFF, BlockEntitySonicWeapon.EYEDAMAGE / fudge, BlockEntitySonicWeapon.BRAINDAMAGE / fudge, BlockEntitySonicWeapon.LUNGDAMAGE / fudge, BlockEntitySonicWeapon.LETHALVOLUME / fudge);
//        addNotes(MachineRegistry.FORCEFIELD, PowerReceivers.FORCEFIELD.getMinPower(), PowerReceivers.FORCEFIELD.getMinPower(), BlockEntityForceField.FALLOFF);
        addNotes(MachineRegistry.MUSICBOX, BlockEntityMusicBox.LOOPPOWER);
        addNotes(MachineRegistry.MOBHARVESTER, PowerReceivers.MOBHARVESTER.getMinPower(), PowerReceivers.MOBHARVESTER.getMinPower() * 2);
//        addNotes(MachineRegistry.PROJECTOR, PowerReceivers.PROJECTOR.getMinPower());
//        addNotes(MachineRegistry.RAILGUN, PowerReceivers.RAILGUN.getMinPower());
//        addNotes(MachineRegistry.WEATHERCONTROLLER, PowerReceivers.WEATHERCONTROLLER.getMinPower());
        addNotes(MachineRegistry.REFRESHER, PowerReceivers.REFRESHER.getMinPower(), PowerReceivers.REFRESHER.getMinPower(), BlockEntityItemRefresher.FALLOFF);
        addNotes(MachineRegistry.CAVESCANNER, PowerReceivers.CAVESCANNER.getMinPower());
//        addNotes(MachineRegistry.SCALECHEST, PowerReceivers.SCALECHEST.getMinPower(), PowerReceivers.SCALECHEST.getMinPower(), BlockEntityScaleableChest.FALLOFF, BlockEntityScaleableChest.MAXSIZE);
//        addNotes(MachineRegistry.IGNITER, PowerReceivers.IGNITER.getMinPower());
//        addNotes(MachineRegistry.FREEZEGUN, PowerReceivers.FREEZEGUN.getMinPower(), PowerReceivers.FREEZEGUN.getMinTorque());
//        addNotes(MachineRegistry.MAGNETIZER, PowerReceivers.MAGNETIZER.getMinPower(), PowerReceivers.MAGNETIZER.getMinSpeed());
        addNotes(MachineRegistry.CONTAINMENT, PowerReceivers.CONTAINMENT.getMinPower(), PowerReceivers.CONTAINMENT.getMinPower(), BlockEntityContainment.FALLOFF, BlockEntityContainment.WITHERPOWER, BlockEntityContainment.DRAGONPOWER);
//        addNotes(MachineRegistry.SCREEN, PowerReceivers.SCREEN.getMinPower(), PowerReceivers.SCREEN.getMinTorque());
//        addNotes(MachineRegistry.PURIFIER, PowerReceivers.PURIFIER.getMinPower(), PowerReceivers.PURIFIER.getMinTorque(), BlockEntityPurifier.SMELTTEMP);
//        addNotes(MachineRegistry.LASERGUN, PowerReceivers.LASERGUN.getMinPower());
        addNotes(MachineRegistry.ITEMCANNON, PowerReceivers.ITEMCANNON.getMinPower(), PowerReceivers.ITEMCANNON.getMinTorque());
//        addNotes(MachineRegistry.FRICTION, PowerReceivers.FRICTION.getMinPower(), PowerReceivers.FRICTION.getMinTorque());
        addNotes(MachineRegistry.BUCKETFILLER, PowerReceivers.BUCKETFILLER.getMinPower(), PowerReceivers.BUCKETFILLER.getMinSpeed());
        addNotes(MachineRegistry.BLOCKCANNON, PowerReceivers.BLOCKCANNON.getMinPower());
//        addNotes(MachineRegistry.COMPRESSOR, BlockEntityAirCompressor.MAXPRESSURE);
//        addNotes(MachineRegistry.LAMP, BlockEntityLamp.MAXRANGE);
//        addNotes(MachineRegistry.ECU, BlockEntityEngineController.getSettingsAsString());
//        addNotes(MachineRegistry.BLASTFURNACE, BlockEntityBlastFurnace.SMELT_XP);
//        addNotes(MachineRegistry.FUELENHANCER, PowerReceivers.FUELENHANCER.getMinPower(), PowerReceivers.FUELENHANCER.getMinSpeed());
//        addNotes(MachineRegistry.ARROWGUN, PowerReceivers.ARROWGUN.getMinPower(), PowerReceivers.ARROWGUN.getMinTorque());
//        addNotes(MachineRegistry.STEAMTURBINE, BlockEntitySteam.GEN_OMEGA, BlockEntitySteam.MAX_TORQUE);
//        addNotes(MachineRegistry.FERTILIZER, PowerReceivers.FERTILIZER.getMinPower());
//		addNotes(MachineRegistry.ELECTRICMOTOR,
//				BlockEntityElectricMotor.Tier.LOW.inputVoltage, BlockEntityElectricMotor.Tier.LOW.inputCurrent, BlockEntityElectricMotor.Tier.LOW.outputTorque, BlockEntityElectricMotor.Tier.LOW.outputSpeed, BlockEntityElectricMotor.Tier.LOW.getPowerForDisplay(),
//				BlockEntityElectricMotor.Tier.MEDIUM.inputVoltage, BlockEntityElectricMotor.Tier.MEDIUM.inputCurrent, BlockEntityElectricMotor.Tier.MEDIUM.outputTorque, BlockEntityElectricMotor.Tier.MEDIUM.outputSpeed, BlockEntityElectricMotor.Tier.MEDIUM.getPowerForDisplay(),
//				BlockEntityElectricMotor.Tier.HIGH.inputVoltage, BlockEntityElectricMotor.Tier.HIGH.inputCurrent, BlockEntityElectricMotor.Tier.HIGH.outputTorque, BlockEntityElectricMotor.Tier.HIGH.outputSpeed, BlockEntityElectricMotor.Tier.HIGH.getPowerForDisplay()
//				);
//        addNotes(MachineRegistry.AGGREGATOR, PowerReceivers.AGGREGATOR.getMinPower(), PowerReceivers.AGGREGATOR.getMinSpeed());
//        addNotes(MachineRegistry.FUELENGINE, BlockEntityFuelEngine.GEN_TORQUE, BlockEntityFuelEngine.GEN_OMEGA, BlockEntityFuelEngine.GEN_TORQUE * BlockEntityFuelEngine.GEN_OMEGA);
//        addNotes(MachineRegistry.AIRGUN, PowerReceivers.AIRGUN.getMinPower(), PowerReceivers.AIRGUN.getMinTorque());
//        addNotes(MachineRegistry.SONICBORER, PowerReceivers.SONICBORER.getMinPower(), PowerReceivers.SONICBORER.getMinTorque());
//        addNotes(MachineRegistry.FILLINGSTATION, PowerReceivers.FILLINGSTATION.getMinPower());
//        addNotes(MachineRegistry.SORTING, PowerReceivers.SORTING.getMinPower());
//        addNotes(MachineRegistry.DEFOLIATOR, PowerReceivers.DEFOLIATOR.getMinPower());
        addNotes(MachineRegistry.BIGFURNACE, PowerReceivers.BIGFURNACE.getMinPower());
//        addNotes(MachineRegistry.DISTILLER, BlockEntityDistillery.getValidConversions());
//        addNotes(MachineRegistry.CRYSTALLIZER, PowerReceivers.CRYSTALLIZER.getMinPower(), PowerReceivers.CRYSTALLIZER.getMinSpeed());
        addNotes(MachineRegistry.GRINDSTONE, PowerReceivers.GRINDSTONE.getMinPower(), PowerReceivers.GRINDSTONE.getMinTorque());
        addNotes(MachineRegistry.BLOWER, PowerReceivers.BLOWER.getMinPower(), PowerReceivers.BLOWER.getMinSpeed());
        addNotes(MachineRegistry.REFRIGERATOR, PowerReceivers.REFRIGERATOR.getMinPower(), PowerReceivers.REFRIGERATOR.getMinTorque());
        addNotes(MachineRegistry.COMPOSTER, BlockEntityComposter.MINTEMP, BlockEntityComposter.KILLTEMP);
//        addNotes(MachineRegistry.GASTANK, PowerReceivers.GASTANK.getMinPower(), PowerReceivers.GASTANK.getMinTorque());
//        addNotes(MachineRegistry.CRAFTER, PowerReceivers.CRAFTER.getMinPower());
//        addNotes(MachineRegistry.ANTIAIR, PowerReceivers.ANTIAIR.getMinPower(), PowerReceivers.ANTIAIR.getMinTorque());
//        addNotes(MachineRegistry.PIPEPUMP, PowerReceivers.PIPEPUMP.getMinPower(), PowerReceivers.PIPEPUMP.getMinSpeed());
//        addNotes(MachineRegistry.CENTRIFUGE, PowerReceivers.CENTRIFUGE.getMinPower(), PowerReceivers.CENTRIFUGE.getMinSpeed());
//        addNotes(MachineRegistry.WETTER, PowerReceivers.WETTER.getMinPower(), PowerReceivers.WETTER.getMinSpeed());
//        addNotes(MachineRegistry.CHUNKLOADER, PowerReceivers.CHUNKLOADER.getMinSpeed(), BlockEntityChunkLoader.BASE_RADIUS, PowerReceivers.CHUNKLOADER.getMinSpeed(), BlockEntityChunkLoader.FALLOFF);
//        addNotes(MachineRegistry.DROPS, PowerReceivers.DROPS.getMinPower(), PowerReceivers.DROPS.getMinTorque());
        addNotes(MachineRegistry.SPILLER, PowerReceivers.SPILLER.getMinPower());
        addNotes(MachineRegistry.FILLER, PowerReceivers.FILLER.getMinPower());
//        addNotes(MachineRegistry.GATLING, PowerReceivers.GATLING.getMinPower(), PowerReceivers.GATLING.getMinSpeed());
///*
        addNotes(MachineRegistry.MAGNETIC, EnergyToPowerBase.MAXTEMP);
//        addNotes(MachineRegistry.PNEUENGINE, EnergyToPowerBase.MAXTEMP);
//        addNotes(MachineRegistry.ELECTRICMOTOR, EnergyToPowerBase.MAXTEMP);
//        addNotes(MachineRegistry.STEAMTURBINE, EnergyToPowerBase.MAXTEMP);
//*/
//        addNotes(MachineRegistry.EMP, BlockEntityEMP.BLAST_ENERGY);
///*
        addSubNotes(MachineRegistry.MAGNETIC, 1, EnergyToPowerBase.getTiersAsString());
//        addSubNotes(MachineRegistry.PNEUENGINE, 1, EnergyToPowerBase.getTiersAsString());
//        addSubNotes(MachineRegistry.ELECTRICMOTOR, 1, EnergyToPowerBase.getTiersAsString());
//        addSubNotes(MachineRegistry.STEAMTURBINE, 1, EnergyToPowerBase.getTiersAsString());
//
//        addSubNotes(MachineRegistry.MAGNETIZER, 1, RecipesMagnetizer.getRecipes().getRecipesAsString());
//*/
//        addSubNotes(MachineRegistry.ADVANCEDGEARS, 1, BlockEntityAdvancedGear.getMaxStorageCapacity(false), BlockEntityAdvancedGear.getMaxStorageCapacityFormatted(false), BlockEntityAdvancedGear.getMaxStorageCapacity(true), BlockEntityAdvancedGear.getMaxStorageCapacityFormatted(true));
//        addSubNotes(MachineRegistry.ADVANCEDGEARS, 2, BlockEntityAdvancedGear.getRequiredInputTorque(), BlockEntityAdvancedGear.getRequiredInputPower());
//        addSubNotes(MachineRegistry.ADVANCEDGEARS, 3, BlockEntityAdvancedGear.getOutputCap(false), BlockEntityAdvancedGear.getOutputCap(false), BlockEntityAdvancedGear.getOutputCap(true), BlockEntityAdvancedGear.getOutputCap(true), BlockEntityAdvancedGear.getOutputFunction());
//
//        addData(HandbookRegistry.TUNGSTEN, RecipesFrictionHeater.getRecipes().getRecipeByInput(RotaryItems.TUNGSTEN_FLAKES).requiredTemperature);
    }

    public static String getParentPage() {
        return PARENT;
    }

    public static class ReloadListener implements ResourceManagerReloadListener {
        @Override
        public void onResourceManagerReload(ResourceManager p_10758_) {
            RotaryDescriptions.reload();
        }

        //@SubscribeEvent
        //
        //public void reload(ResourceReloadEvent evt) {
        //}

    }
}
