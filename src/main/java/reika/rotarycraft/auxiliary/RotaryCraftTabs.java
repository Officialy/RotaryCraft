package reika.rotarycraft.auxiliary;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.eventbus.api.IEventBus;
import net.neoforged.eventbus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.registries.DeferredRegister;
import net.neoforged.registries.RegistryObject;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;
@Mod.EventBusSubscriber(modid = RotaryCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RotaryCraftTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RotaryCraft.MODID);

    public static final RegistryObject<CreativeModeTab> ROTARYCRAFT = CREATIVE_MODE_TABS.register("rotarycraft", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("tab.rotarycraft"))
                    .icon(() -> new ItemStack(RotaryItems.HSLA_STEEL_GEAR.get()))
                    .build());

    public static final RegistryObject<CreativeModeTab> ROTARYCRAFT_TRANSMISSION = CREATIVE_MODE_TABS.register("transmission", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("tab.rotarycraft.transmission"))
                    .icon(() -> new ItemStack(RotaryItems.DIAMOND_SHAFT.get()))
                    .build());

    public static final RegistryObject<CreativeModeTab> ROTARYCRAFT_TOOLS = CREATIVE_MODE_TABS.register("tools", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("tab.rotarycraft.tools"))
                    .icon(() -> new ItemStack(RotaryItems.BEDROCK_ALLOY_PICK.get()))
                    .build());

    public static final RegistryObject<CreativeModeTab> ROTARY_ORES = CREATIVE_MODE_TABS.register("ores", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("tab.rotarycraft.ores"))
                    .icon(() -> new ItemStack(RotaryItems.TUNGSTEN_FLAKES.get()))
                    .build());

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }

    @SubscribeEvent
    public static void onBuildCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == ROTARYCRAFT.get()) {
            event.accept(RotaryItems.IRON_SCRAP.get());
            event.accept(RotaryItems.ALUMINUM_ALLOY_INGOT.get());
            event.accept(RotaryItems.ALUMINUM_ALLOY_CYLINDER.get());
            event.accept(RotaryItems.WOOD_FLYWHEEL_CORE.get());
            event.accept(RotaryItems.STONE_FLYWHEEL_CORE.get());
            event.accept(RotaryItems.IRON_FLYWHEEL_CORE.get());
            event.accept(RotaryItems.GOLD_FLYWHEEL_CORE.get());
            event.accept(RotaryItems.TUNGSTEN_ALLOY_FLYWHEEL_CORE.get());
            event.accept(RotaryItems.NETHERRACK_DUST.get());
            event.accept(RotaryItems.MOUNT.get());
            event.accept(RotaryItems.HUB.get());
            event.accept(RotaryItems.IGNITION_UNIT.get());
            event.accept(RotaryItems.IMPELLER.get());
            event.accept(RotaryItems.INDUCTIVE_INGOT.get());
            event.accept(RotaryItems.LENS.get());
            event.accept(RotaryItems.LINEAR_INDUCTION_MOTOR.get());
            event.accept(RotaryItems.MIRROR.get());
            event.accept(RotaryItems.MIXER.get());
            event.accept(RotaryItems.PADDLE_PANEL.get());
            event.accept(RotaryItems.POWER_MODULE.get());
            event.accept(RotaryItems.PRESSURE_HEAD.get());
            event.accept(RotaryItems.PROPELLER_BLADE.get());
            event.accept(RotaryItems.RADAR_UNIT.get());
            event.accept(RotaryItems.RADIATOR.get());
            event.accept(RotaryItems.RAILGUN_ACCELERATOR.get());
            event.accept(RotaryItems.SCREEN.get());
            event.accept(RotaryItems.SONAR_UNIT.get());
            event.accept(RotaryItems.TURBINE.get());
            event.accept(RotaryItems.TURRET_AIMING_UNIT.get());
            event.accept(RotaryItems.TURRET_BASE.get());
            event.accept(RotaryItems.TUNGSTEN_ALLOY_INGOT.get());
            event.accept(RotaryItems.TUNGSTEN_INGOT.get());
            event.accept(RotaryItems.WORM_GEAR.get());
            event.accept(RotaryItems.HSLA_STEEL_SPRING.get());
            event.accept(RotaryItems.TUNGSTEN_ALLOY_SPRING.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_SPRING.get());
            event.accept(RotaryItems.HEAT_RAY_BARREL.get());
            event.accept(RotaryItems.HEAT_RAY_CORE.get());
            event.accept(RotaryItems.BELT.get());
            event.accept(RotaryItems.BRAKE_DISC.get());
            event.accept(RotaryItems.CHAIN_LINK.get());
            event.accept(RotaryItems.CIRCUIT_BOARD.get());
            event.accept(RotaryItems.COMBUSTOR.get());
            event.accept(RotaryItems.COMPOUND_COMPRESSOR.get());
            event.accept(RotaryItems.COMPRESSOR.get());
            event.accept(RotaryItems.COMPOUND_TURBINE.get());
            event.accept(RotaryItems.CONDENSER.get());
            event.accept(RotaryItems.CYLINDER.get());
            event.accept(RotaryItems.DIFFUSER.get());
            event.accept(RotaryItems.DRILLHEAD_IRON.get());
            event.accept(RotaryItems.GENERATOR.get());
            event.accept(RotaryItems.GOLD_COIL.get());
            event.accept(RotaryItems.HIGH_TEMPERATURE_COMBUSTOR.get());
            event.accept(RotaryItems.SPRING_STEEL_INGOT.get());
            event.accept(RotaryItems.RAILGUN_AMMO.get());
            event.accept(RotaryItems.TARGET.get());
            event.accept(RotaryItems.SLIDE.get());
            event.accept(RotaryItems.SHELL.get());
            event.accept(RotaryItems.DISK.get());
            event.accept(RotaryItems.SAW.get());
            event.accept(RotaryItems.DRY_ICE.get());
            event.accept(RotaryItems.SILICON.get());
            event.accept(RotaryItems.SILICON_DUST.get());
            event.accept(RotaryItems.INDUCTIVE_BLEND.get());
            event.accept(RotaryItems.SLUDGE.get());
            event.accept(RotaryItems.SAWDUST.get());
            event.accept(RotaryItems.HSLA_STEEL_GEAR.get());
            event.accept(RotaryItems.HSLA_STEEL_GEAR_2x.get());
            event.accept(RotaryItems.HSLA_STEEL_GEAR_4x.get());
            event.accept(RotaryItems.HSLA_STEEL_GEAR_8x.get());
            event.accept(RotaryItems.HSLA_STEEL_GEAR_16x.get());
            event.accept(RotaryItems.HSLA_SHAFT.get());
            event.accept(RotaryItems.HSLA_SHAFT_CORE.get());
            event.accept(RotaryItems.HSLA_STEEL_INGOT.get());
            event.accept(RotaryItems.HSLA_STEEL_NUGGET.get());
            event.accept(RotaryItems.TUNGSTEN_ALLOY_GEAR.get());
            event.accept(RotaryItems.TUNGSTEN_ALLOY_GEAR_2x.get());
            event.accept(RotaryItems.TUNGSTEN_ALLOY_GEAR_4x.get());
            event.accept(RotaryItems.TUNGSTEN_ALLOY_GEAR_8x.get());
            event.accept(RotaryItems.TUNGSTEN_ALLOY_GEAR_16x.get());
            event.accept(RotaryItems.TUNGSTEN_ALLOY_SHAFT.get());
            event.accept(RotaryItems.TUNGSTEN_ALLOY_SHAFT_CORE.get());
            event.accept(RotaryItems.DIAMOND_GEAR.get());
            event.accept(RotaryItems.DIAMOND_GEAR_2x.get());
            event.accept(RotaryItems.DIAMOND_GEAR_4x.get());
            event.accept(RotaryItems.DIAMOND_GEAR_8x.get());
            event.accept(RotaryItems.DIAMOND_GEAR_16x.get());
            event.accept(RotaryItems.DIAMOND_SHAFT.get());
            event.accept(RotaryItems.DIAMOND_SHAFT_CORE.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_GEAR.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_GEAR_2x.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_GEAR_4x.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_GEAR_8x.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_GEAR_16x.get());
            event.accept(RotaryItems.BEDROCK_DUST.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_SHAFT.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_SHAFT_CORE.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_INGOT.get());
            event.accept(RotaryItems.TUNGSTEN_FLAKES.get());
            event.accept(RotaryItems.DIAMOND_FLAKES.get());
            event.accept(RotaryItems.COMPOST.get());
            event.accept(RotaryItems.YEAST.get());
            event.accept(RotaryItems.ETHANOL.get());
            event.accept(RotaryItems.CANOLA_SEEDS.get());
            event.accept(RotaryItems.CANOLA_HUSKS.get());
            event.accept(RotaryItems.DENSE_CANOLA_SEEDS.get());
            event.accept(RotaryItems.TAR.get());
            event.accept(RotaryItems.ALUMINUM_ALLOY_POWDER.get());
            event.accept(RotaryItems.COAL_DUST.get());
            event.accept(RotaryItems.FLOUR.get());
            event.accept(RotaryItems.COKE.get());
            event.accept(RotaryItems.NITRATE.get());
            event.accept(RotaryItems.SALT.get());
            event.accept(RotaryItems.SILVERIODIDE.get());
        }

        if (event.getTab() == ROTARYCRAFT_TRANSMISSION.get()) {
            event.accept(RotaryBlocks.WOOD_FLYWHEEL.get().asItem());
            event.accept(RotaryBlocks.HSLA_FLYWHEEL.get().asItem());
            event.accept(RotaryBlocks.TUNGSTEN_FLYWHEEL.get().asItem());
            event.accept(RotaryBlocks.DIAMOND_FLYWHEEL.get().asItem());
            event.accept(RotaryBlocks.BEDROCK_FLYWHEEL.get().asItem());
            event.accept(RotaryBlocks.HSLA_GEARBOX_2x.get().asItem());
            event.accept(RotaryBlocks.HSLA_GEARBOX_4x.get().asItem());
            event.accept(RotaryBlocks.HSLA_GEARBOX_8x.get().asItem());
            event.accept(RotaryBlocks.HSLA_GEARBOX_16x.get().asItem());
            event.accept(RotaryBlocks.TUNGSTEN_GEARBOX_2x.get().asItem());
            event.accept(RotaryBlocks.TUNGSTEN_GEARBOX_4x.get().asItem());
            event.accept(RotaryBlocks.TUNGSTEN_GEARBOX_8x.get().asItem());
            event.accept(RotaryBlocks.TUNGSTEN_GEARBOX_16x.get().asItem());
            event.accept(RotaryBlocks.DIAMOND_GEARBOX_2x.get().asItem());
            event.accept(RotaryBlocks.DIAMOND_GEARBOX_4x.get().asItem());
            event.accept(RotaryBlocks.DIAMOND_GEARBOX_8x.get().asItem());
            event.accept(RotaryBlocks.DIAMOND_GEARBOX_16x.get().asItem());
            event.accept(RotaryBlocks.BEDROCK_GEARBOX_2x.get().asItem());
            event.accept(RotaryBlocks.BEDROCK_GEARBOX_4x.get().asItem());
            event.accept(RotaryBlocks.BEDROCK_GEARBOX_8x.get().asItem());
            event.accept(RotaryBlocks.BEDROCK_GEARBOX_16x.get().asItem());
            event.accept(RotaryBlocks.WOOD_SHAFT.get().asItem());
            event.accept(RotaryBlocks.STONE_SHAFT.get().asItem());
            event.accept(RotaryBlocks.HSLA_SHAFT.get().asItem());
            event.accept(RotaryBlocks.TUNGSTEN_SHAFT.get().asItem());
            event.accept(RotaryBlocks.DIAMOND_SHAFT.get().asItem());
            event.accept(RotaryBlocks.BEDROCK_SHAFT.get().asItem());
            event.accept(RotaryBlocks.SHAFT_CROSS.get().asItem());
            event.accept(RotaryBlocks.SHAFT_MERGE.get().asItem());
            event.accept(RotaryBlocks.SPLITTER.get().asItem());
            event.accept(RotaryBlocks.DISTRIBUTION_CLUTCH.get().asItem());
            event.accept(RotaryBlocks.CLUTCH.get().asItem());
            event.accept(RotaryBlocks.MULTI_CLUTCH.get().asItem());
            event.accept(RotaryBlocks.COIL.get().asItem());
            event.accept(RotaryBlocks.CVT.get().asItem());
            event.accept(RotaryBlocks.WORMGEAR.get().asItem());
            event.accept(RotaryBlocks.HIGHGEAR.get().asItem());
            event.accept(RotaryBlocks.BEVEL_GEARS.get().asItem());
        }

        if (event.getTab() == ROTARYCRAFT_TOOLS.get()) {
            event.accept(RotaryItems.BEDROCK_ALLOY_PICK.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_AXE.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_SWORD.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_HOE.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_SHEARS.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_SHOVEL.get());
            event.accept(RotaryItems.MOLTEN_HSLA_BUCKET.get());
            event.accept(RotaryItems.NVG.get());
            event.accept(RotaryItems.IO_GOGGLES.get());
            event.accept(RotaryItems.ANGULAR_TRANSDUCER.get());
            event.accept(RotaryItems.ETHANOL_CART.get());
            event.accept(RotaryItems.JETPACK.get());
            event.accept(RotaryItems.JUMP.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_JUMP_BOOTS.get());
            event.accept(RotaryItems.UPGRADE.get());
            event.accept(RotaryItems.RANGE_FINDER.get());
            event.accept(RotaryItems.SPRING_PISTON.get());
            event.accept(RotaryItems.FLAMETHROWER.get());
            event.accept(RotaryItems.TILE_SELECTOR.get());
            event.accept(RotaryItems.HANDHELD_CRAFTING_TABLE.get());
            event.accept(RotaryItems.ULTRASOUND.get());
            event.accept(RotaryItems.SCREWDRIVER.get());
            event.accept(RotaryItems.HANDBOOK.get());
            event.accept(RotaryItems.INTEGRATED_GEARBOX.get());
            event.accept(RotaryItems.HSLA_STEEL_PICKAXE.get());
            event.accept(RotaryItems.HSLA_STEEL_SHOVEL.get());
            event.accept(RotaryItems.HSLA_STEEL_AXE.get());
            event.accept(RotaryItems.HSLA_STEEL_SWORD.get());
            event.accept(RotaryItems.HSLA_STEEL_HOE.get());
            event.accept(RotaryItems.HSLA_STEEL_SHEARS.get());
            event.accept(RotaryItems.HSLA_HELMET.get());
            event.accept(RotaryItems.HSLA_CHESTPLATE.get());
            event.accept(RotaryItems.HSLA_LEGGINGS.get());
            event.accept(RotaryItems.HSLA_BOOTS.get());
            event.accept(RotaryItems.HSLA_DRILL.get());
            event.accept(RotaryItems.HSLA_STEEL_PACK.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_PACK.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_HELMET.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_LEGGINGS.get());
            event.accept(RotaryItems.BEDROCK_ALLOY_BOOTS.get());
            event.accept(RotaryItems.BEDROCK_DRILL.get());
            event.accept(RotaryItems.LUBE_BUCKET.get());
            event.accept(RotaryItems.JET_FUEL_BUCKET.get());
            event.accept(RotaryItems.ETHANOL_BUCKET.get());
            event.accept(RotaryItems.NITROGEN_BUCKET.get());
        }

        if (event.getTab() == ROTARY_ORES.get()) {
            event.accept(RotaryItems.DIAMOND_FLAKES.get());
            event.accept(RotaryItems.TUNGSTEN_FLAKES.get());
        }
    }

}
