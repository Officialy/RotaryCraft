package reika.rotarycraft.registry;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.gui.container.ContainerHandCraft;
import reika.rotarycraft.gui.container.machine.*;
import reika.rotarycraft.gui.container.machine.inventory.*;

import java.util.function.Supplier;

public interface RotaryMenus {

    DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, RotaryCraft.MODID);

    static <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String id, IContainerFactory<T> factory) {
        return REGISTRY.register(id, () -> new MenuType<>(factory));
    }

    Supplier<MenuType<WinderContainer>> WINDER = register("winder", WinderContainer::new);

    Supplier<MenuType<GearboxContainer>> GEARBOX = register("gearbox", GearboxContainer::new);

    Supplier<MenuType<ReservoirContainer>> RESERVOIR = register("reservoir", ReservoirContainer::new);


    Supplier<MenuType<LandmineContainer>> LANDMINE = register("landmine", LandmineContainer::new);

    Supplier<MenuType<BlowerContainer>> BLOWER = register("blower", BlowerContainer::new);

//    Supplier<MenuType<ContainerEthanol>> GAS_ENGINE = register("gas_engine",      () -> IForgeMenuType.create(new ContainerEthanol.Factory()));

//    Supplier<MenuType<ContainerHeater>> HEATER = register("heater",     () -> IForgeMenuType.create(new ContainerHeater.Factory()));

    Supplier<MenuType<ContainerHandCraft>> HAND_CRAFT = register("hand_craft", ContainerHandCraft::new);


    Supplier<MenuType<SteamContainer>> STEAM_ENGINE = register("steam_engine", SteamContainer::new);
    Supplier<MenuType<BevelContainer>> BEVEL = register("bevel", BevelContainer::new);
    Supplier<MenuType<MusicContainer>> MUSIC = register("music", MusicContainer::new);

    Supplier<MenuType<ContainerGrinder>> GRINDER = register("grinder", ContainerGrinder::new);

//    Supplier<MenuType<BlastFurnaceMenu>> BLAST_FURNACE = register("blast_furnace",   () -> IForgeMenuType.create(new BlastFurnaceMenu.Factory()));
//
//    Supplier<MenuType<GrinderMenu>> GRINDER = register("grinder", () -> IForgeMenuType.create(new GrinderMenu.Factory()));
//
//    Supplier<MenuType<CompactorMenu>> COMPACTOR = register("compactor", () -> IForgeMenuType.create(new CompactorMenu.Factory()));

//    Supplier<MenuType<ContainerExtractor>> EXTRACTOR = register("extractor", () -> IForgeMenuType.create(new ContainerExtractor.Factory()));

    Supplier<MenuType<PerformanceContainer>> PERFORMANCE_ENGINE = register("performance_engine", PerformanceContainer::new);

//    Supplier<MenuType<ContainerItemCannon>> ITEM_CANNON = register("item_cannon",     () -> IForgeMenuType.create(new ContainerItemCannon.Factory()));

//    Supplier<MenuType<ContainerObsidian>> OBSIDIAN_MAKER = register("obsidian_maker",  () -> IForgeMenuType.create(new ContainerObsidian.Factory()));

//    Supplier<MenuType<ContainerCannon>> CANNON = register("cannon", () -> IForgeMenuType.create(new ContainerCannon.Factory()));

    Supplier<MenuType<ContainerAerosolizer>> AEROSOLIZER = register("aerosolizer", ContainerAerosolizer::new);
//    Supplier<MenuType<ContainerWorktable>> WORKTABLE = register("worktable", ContainerWorktable::new);

    Supplier<MenuType<ContainerSorter>> SORTER = register("sorter", ContainerSorter::new);
    Supplier<MenuType<ContainerBigFurnace>> BIG_FURNACE = register("big_furnace", ContainerBigFurnace::new);
    Supplier<MenuType<ContainerCVT>> CVT = register("cvt", ContainerCVT::new);
//    Supplier<MenuType<ContainerFillingStation>> FILLING_STATION = register("filling_station", ContainerFillingStation::new);

}
