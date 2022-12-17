package reika.rotarycraft.registry;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import reika.dragonapi.base.BlockEntityBase;
import reika.dragonapi.base.CoreMenu;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.blockentities.transmission.BlockEntityBevelGear;
import reika.rotarycraft.gui.container.ContainerHandCraft;
import reika.rotarycraft.gui.container.machine.*;
import reika.rotarycraft.gui.container.machine.inventory.*;

import java.util.function.Supplier;

public interface RotaryMenus {

    DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, RotaryCraft.MODID);

    static <T extends AbstractContainerMenu> Supplier<MenuType<T>> register(String id, IContainerFactory<T> factory) {
        return REGISTRY.register(id, () -> new MenuType<>(factory));
    }

    Supplier<MenuType<WinderMenu>> WINDER = register("winder", WinderMenu::new);

    Supplier<MenuType<GearboxMenu>> GEARBOX = register("gearbox", GearboxMenu::new);

    Supplier<MenuType<ReservoirContainer>> RESERVOIR = register("reservoir", ReservoirContainer::new);

    Supplier<MenuType<LandmineMenu>> LANDMINE = register("landmine", LandmineMenu::new);

    Supplier<MenuType<BlowerMenu>> BLOWER = register("blower", BlowerMenu::new);

//    Supplier<MenuType<ContainerEthanol>> GAS_ENGINE = register("gas_engine",      () -> IForgeMenuType.create(new ContainerEthanol.Factory()));

//    Supplier<MenuType<ContainerHeater>> HEATER = register("heater",     () -> IForgeMenuType.create(new ContainerHeater.Factory()));

    Supplier<MenuType<ContainerHandCraft>> HAND_CRAFT = register("hand_craft", ContainerHandCraft::new);


    Supplier<MenuType<SteamMenu>> STEAM_ENGINE = register("steam_engine", SteamMenu::new);
    Supplier<MenuType<BevelMenu>> BEVEL = register("bevel", BevelMenu::new);


//    Supplier<MenuType<ContainerGrinder>> GRINDER = register("grinder", GrinderMenu::new);

//    Supplier<MenuType<BlastFurnaceMenu>> BLAST_FURNACE = register("blast_furnace",   () -> IForgeMenuType.create(new BlastFurnaceMenu.Factory()));
//
//    Supplier<MenuType<GrinderMenu>> GRINDER = register("grinder", () -> IForgeMenuType.create(new GrinderMenu.Factory()));
//
//    Supplier<MenuType<CompactorMenu>> COMPACTOR = register("compactor", () -> IForgeMenuType.create(new CompactorMenu.Factory()));

//    Supplier<MenuType<ContainerExtractor>> EXTRACTOR = register("extractor", () -> IForgeMenuType.create(new ContainerExtractor.Factory()));

    Supplier<MenuType<PerformanceMenu>> PERFORMANCE_ENGINE = register("performance_engine", PerformanceMenu::new);

//    Supplier<MenuType<ContainerItemCannon>> ITEM_CANNON = register("item_cannon",     () -> IForgeMenuType.create(new ContainerItemCannon.Factory()));

//    Supplier<MenuType<ContainerObsidian>> OBSIDIAN_MAKER = register("obsidian_maker",  () -> IForgeMenuType.create(new ContainerObsidian.Factory()));

//    Supplier<MenuType<ContainerCannon>> CANNON = register("cannon", () -> IForgeMenuType.create(new ContainerCannon.Factory()));

    Supplier<MenuType<ContainerAerosolizer>> AEROSOLIZER = register("aerosolizer", ContainerAerosolizer::new);

    Supplier<MenuType<ContainerSorter>> SORTER = register("sorter", ContainerSorter::new);
    Supplier<MenuType<ContainerBigFurnace>> BIG_FURNACE = register("aerosolizer", ContainerBigFurnace::new);

}
