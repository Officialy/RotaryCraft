package reika.rotarycraft.registry;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.fluids.*;
import reika.rotarycraft.base.fluids.fluidblock.HslaFluidBlock;
import reika.rotarycraft.base.fluids.fluidtypes.*;

public class RotaryFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, RotaryCraft.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, RotaryCraft.MODID);

    //Source
    public static final RegistryObject<FlowingFluid> HSLA_FLUID = FLUIDS.register("molten_hsla_still", () -> new HslaFluid.Source(RotaryFluids.HSLA_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> JET_FUEL = FLUIDS.register("jet_fuel_still", () -> new JetFuelFluid.Source(RotaryFluids.JET_FUEL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> ETHANOL = FLUIDS.register("ethanol_still", () -> new EthanolFluid.Source(RotaryFluids.ETHANOL_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> LUBRICANT = FLUIDS.register("lubricant_still", () -> new LubricantFluid.Source(RotaryFluids.LUBRICANT_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> LIQUID_NITROGEN = FLUIDS.register("liquid_nitrogen_still", () -> new LiquidNitrogenFluid.Source(RotaryFluids.LIQUID_NITROGEN_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> POISON = FLUIDS.register("poison_still", () -> new HslaFluid.Source(RotaryFluids.POISON_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> STEAM = FLUIDS.register("steam_still", () -> new HslaFluid.Source(RotaryFluids.STEAM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> SODIUM = FLUIDS.register("sodium_still", () -> new HslaFluid.Source(RotaryFluids.SODIUM_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> CHLORINE = FLUIDS.register("chlorine_still", () -> new HslaFluid.Source(RotaryFluids.CHLORINE_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> OXYGEN = FLUIDS.register("oxygen_still", () -> new HslaFluid.Source(RotaryFluids.OXYGEN_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> LIQUID_AMMONIA = FLUIDS.register("liquid_ammonia_still", () -> new HslaFluid.Source(RotaryFluids.LIQUID_AMMONIA_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> AMMONIA = FLUIDS.register("ammonia_still", () -> new HslaFluid.Source(RotaryFluids.AMMONIA_FLUID_PROPERTIES));
    public static final RegistryObject<FlowingFluid> HEAVY_WATER = FLUIDS.register("heavy_water_still", () -> new HslaFluid.Source(RotaryFluids.HEAVY_WATER_FLUID_PROPERTIES));
    //Flowing
    public static final RegistryObject<FlowingFluid> HSLA_FLUID_FLOWING = FLUIDS.register("molten_hsla_flowing", () -> new HslaFluid.Flowing(RotaryFluids.HSLA_FLUID_PROPERTIES));

    //Fluidtypes
    private static final RegistryObject<FluidType> HSLA_FLUID_TYPE = FLUID_TYPES.register("molten_hsla", HslaFluidType::new);
    private static final RegistryObject<FluidType> JET_FUEL_FLUID_TYPE = FLUID_TYPES.register("jet_fuel_fluid_type", JetFuelFluidType::new);
    private static final RegistryObject<FluidType> ETHANOL_FLUID_TYPE = FLUID_TYPES.register("ethanol_fluid_type", EthanolFluidType::new);
    private static final RegistryObject<FluidType> LUBRICANT_FLUID_TYPE = FLUID_TYPES.register("lubricant_fluid_type", LubricantFluidType::new);
    private static final RegistryObject<FluidType> LIQUID_NITROGEN_FLUID_TYPE = FLUID_TYPES.register("liquid_nitrogen_fluid_type", LiquidNitrogenFluidType::new);
    private static final RegistryObject<FluidType> POISON_FLUID_TYPE = FLUID_TYPES.register("poison_fluid_type", HslaFluidType::new);
    private static final RegistryObject<FluidType> STEAM_FLUID_TYPE = FLUID_TYPES.register("steam_fluid_type", HslaFluidType::new);
    private static final RegistryObject<FluidType> SODIUM_FLUID_TYPE = FLUID_TYPES.register("sodium_fluid_type", HslaFluidType::new);
    private static final RegistryObject<FluidType> CHLORINE_FLUID_TYPE = FLUID_TYPES.register("chlorine_fluid_type", HslaFluidType::new);
    private static final RegistryObject<FluidType> OXYGEN_FLUID_TYPE = FLUID_TYPES.register("oxygen_fluid_type", HslaFluidType::new);
    private static final RegistryObject<FluidType> LIQUID_AMMONIA_FLUID_TYPE = FLUID_TYPES.register("liquid_ammonia_fluid_type", HslaFluidType::new);
    private static final RegistryObject<FluidType> AMMONIA_FLUID_TYPE = FLUID_TYPES.register("ammonia_fluid_type", HslaFluidType::new);
    private static final RegistryObject<FluidType> HEAVY_WATER_FLUID_TYPE = FLUID_TYPES.register("heavy_water_fluid_type", HslaFluidType::new);

    public static final RegistryObject<LiquidBlock> HSLA_FLUID_BLOCK = RotaryBlocks.BLOCKS.register("hsla_fluid_block", () -> new HslaFluidBlock(HSLA_FLUID));

    //Fluid Properties
    //todo some buckets are missing
    public static final ForgeFlowingFluid.Properties HSLA_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(HSLA_FLUID_TYPE, HSLA_FLUID, HSLA_FLUID_FLOWING).bucket(RotaryItems.MOLTEN_HSLA_BUCKET);
    public static final ForgeFlowingFluid.Properties JET_FUEL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(JET_FUEL_FLUID_TYPE, JET_FUEL, null).bucket(RotaryItems.JET_FUEL_BUCKET);
    public static final ForgeFlowingFluid.Properties ETHANOL_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(ETHANOL_FLUID_TYPE, ETHANOL, null).bucket(RotaryItems.ETHANOL_BUCKET);
    public static final ForgeFlowingFluid.Properties LUBRICANT_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(LUBRICANT_FLUID_TYPE, LUBRICANT, null).bucket(RotaryItems.LUBE_BUCKET);
    public static final ForgeFlowingFluid.Properties LIQUID_NITROGEN_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(LIQUID_NITROGEN_FLUID_TYPE, LIQUID_NITROGEN, null).bucket(RotaryItems.NITROGEN_BUCKET);
    public static final ForgeFlowingFluid.Properties POISON_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(POISON_FLUID_TYPE, POISON, null);
    public static final ForgeFlowingFluid.Properties STEAM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(STEAM_FLUID_TYPE, STEAM, null);
    public static final ForgeFlowingFluid.Properties SODIUM_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(SODIUM_FLUID_TYPE, SODIUM, null);
    public static final ForgeFlowingFluid.Properties CHLORINE_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(CHLORINE_FLUID_TYPE, CHLORINE, null);
    public static final ForgeFlowingFluid.Properties OXYGEN_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(OXYGEN_FLUID_TYPE, OXYGEN, null);
    public static final ForgeFlowingFluid.Properties LIQUID_AMMONIA_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(LIQUID_AMMONIA_FLUID_TYPE, LIQUID_AMMONIA, null);
    public static final ForgeFlowingFluid.Properties AMMONIA_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(AMMONIA_FLUID_TYPE, AMMONIA, null);
    public static final ForgeFlowingFluid.Properties HEAVY_WATER_FLUID_PROPERTIES = new ForgeFlowingFluid.Properties(HEAVY_WATER_FLUID_TYPE, HEAVY_WATER, null);

}