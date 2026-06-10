package reika.rotarycraft.registry;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.fluids.*;
import reika.rotarycraft.base.fluids.fluidblock.HslaFluidBlock;
import reika.rotarycraft.base.fluids.fluidtypes.*;

public class RotaryFluids {

    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, RotaryCraft.MODID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.FLUID_TYPES, RotaryCraft.MODID);

    //Source
    public static final DeferredHolder<Fluid, FlowingFluid> HSLA_FLUID = FLUIDS.register("molten_hsla_still", () -> new HslaFluid.Source(RotaryFluids.HSLA_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> JET_FUEL = FLUIDS.register("jet_fuel_still", () -> new JetFuelFluid.Source(RotaryFluids.JET_FUEL_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> ETHANOL = FLUIDS.register("ethanol_still", () -> new EthanolFluid.Source(RotaryFluids.ETHANOL_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> LUBRICANT = FLUIDS.register("lubricant_still", () -> new LubricantFluid.Source(RotaryFluids.LUBRICANT_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> LIQUID_NITROGEN = FLUIDS.register("liquid_nitrogen_still", () -> new LiquidNitrogenFluid.Source(RotaryFluids.LIQUID_NITROGEN_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> POISON = FLUIDS.register("poison_still", () -> new HslaFluid.Source(RotaryFluids.POISON_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> STEAM = FLUIDS.register("steam_still", () -> new HslaFluid.Source(RotaryFluids.STEAM_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> SODIUM = FLUIDS.register("sodium_still", () -> new HslaFluid.Source(RotaryFluids.SODIUM_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> CHLORINE = FLUIDS.register("chlorine_still", () -> new HslaFluid.Source(RotaryFluids.CHLORINE_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> OXYGEN = FLUIDS.register("oxygen_still", () -> new HslaFluid.Source(RotaryFluids.OXYGEN_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> LIQUID_AMMONIA = FLUIDS.register("liquid_ammonia_still", () -> new HslaFluid.Source(RotaryFluids.LIQUID_AMMONIA_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> AMMONIA = FLUIDS.register("ammonia_still", () -> new HslaFluid.Source(RotaryFluids.AMMONIA_FLUID_PROPERTIES));
    public static final DeferredHolder<Fluid, FlowingFluid> HEAVY_WATER = FLUIDS.register("heavy_water_still", () -> new HslaFluid.Source(RotaryFluids.HEAVY_WATER_FLUID_PROPERTIES));
    //Flowing
    public static final DeferredHolder<Fluid, FlowingFluid> HSLA_FLUID_FLOWING = FLUIDS.register("molten_hsla_flowing", () -> new HslaFluid.Flowing(RotaryFluids.HSLA_FLUID_PROPERTIES));

    //Fluidtypes
    private static final DeferredHolder<FluidType, FluidType> HSLA_FLUID_TYPE = FLUID_TYPES.register("molten_hsla", HslaFluidType::new);
    private static final DeferredHolder<FluidType, FluidType> JET_FUEL_FLUID_TYPE = FLUID_TYPES.register("jet_fuel_fluid_type", JetFuelFluidType::new);
    private static final DeferredHolder<FluidType, FluidType> ETHANOL_FLUID_TYPE = FLUID_TYPES.register("ethanol_fluid_type", EthanolFluidType::new);
    private static final DeferredHolder<FluidType, FluidType> LUBRICANT_FLUID_TYPE = FLUID_TYPES.register("lubricant_fluid_type", LubricantFluidType::new);
    private static final DeferredHolder<FluidType, FluidType> LIQUID_NITROGEN_FLUID_TYPE = FLUID_TYPES.register("liquid_nitrogen_fluid_type", LiquidNitrogenFluidType::new);
    private static final DeferredHolder<FluidType, FluidType> POISON_FLUID_TYPE = FLUID_TYPES.register("poison_fluid_type", HslaFluidType::new);
    private static final DeferredHolder<FluidType, FluidType> STEAM_FLUID_TYPE = FLUID_TYPES.register("steam_fluid_type", HslaFluidType::new);
    private static final DeferredHolder<FluidType, FluidType> SODIUM_FLUID_TYPE = FLUID_TYPES.register("sodium_fluid_type", HslaFluidType::new);
    private static final DeferredHolder<FluidType, FluidType> CHLORINE_FLUID_TYPE = FLUID_TYPES.register("chlorine_fluid_type", HslaFluidType::new);
    private static final DeferredHolder<FluidType, FluidType> OXYGEN_FLUID_TYPE = FLUID_TYPES.register("oxygen_fluid_type", HslaFluidType::new);
    private static final DeferredHolder<FluidType, FluidType> LIQUID_AMMONIA_FLUID_TYPE = FLUID_TYPES.register("liquid_ammonia_fluid_type", HslaFluidType::new);
    private static final DeferredHolder<FluidType, FluidType> AMMONIA_FLUID_TYPE = FLUID_TYPES.register("ammonia_fluid_type", HslaFluidType::new);
    private static final DeferredHolder<FluidType, FluidType> HEAVY_WATER_FLUID_TYPE = FLUID_TYPES.register("heavy_water_fluid_type", HslaFluidType::new);

    // 1.21.5: route via RotaryBlocks.registerBlockOnly so the ResourceKey threadlocal is set
    // before HslaFluidBlock's constructor builds its Properties (no BlockItem is generated —
    // the bucket item handles in-inventory representation).
    public static final DeferredBlock<LiquidBlock> HSLA_FLUID_BLOCK = RotaryBlocks.registerBlockOnly("hsla_fluid_block", () -> new HslaFluidBlock(HSLA_FLUID));

    //Fluid Properties
    //todo some buckets are missing
    public static final BaseFlowingFluid.Properties HSLA_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(HSLA_FLUID_TYPE, HSLA_FLUID, HSLA_FLUID_FLOWING).bucket(RotaryItems.MOLTEN_HSLA_BUCKET);
    // 26.1 fix: legacy port passed {@code null} for the flowing-fluid arg, but
    // {@link BaseFlowingFluid#isSame(Fluid)} unconditionally calls {@code this.flowing.get()}
    // — so any FluidStack.isEmpty() check against one of these fluids NPEs (the user's bucket
    // crash happened the moment vanilla's BucketItem.use → FluidUtil.getStack ran isSame). We
    // don't actually have separate flowing variants for these — pump fluids around through
    // tanks/pipes — so wire the source supplier in for both slots; isSame then treats the
    // source as its own flowing for the purpose of the identity check, which is correct here.
    public static final BaseFlowingFluid.Properties JET_FUEL_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(JET_FUEL_FLUID_TYPE, JET_FUEL, JET_FUEL).bucket(RotaryItems.JET_FUEL_BUCKET);
    public static final BaseFlowingFluid.Properties ETHANOL_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(ETHANOL_FLUID_TYPE, ETHANOL, ETHANOL).bucket(RotaryItems.ETHANOL_BUCKET);
    public static final BaseFlowingFluid.Properties LUBRICANT_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(LUBRICANT_FLUID_TYPE, LUBRICANT, LUBRICANT).bucket(RotaryItems.LUBE_BUCKET);
    public static final BaseFlowingFluid.Properties LIQUID_NITROGEN_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(LIQUID_NITROGEN_FLUID_TYPE, LIQUID_NITROGEN, LIQUID_NITROGEN).bucket(RotaryItems.NITROGEN_BUCKET);
    public static final BaseFlowingFluid.Properties POISON_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(POISON_FLUID_TYPE, POISON, POISON);
    public static final BaseFlowingFluid.Properties STEAM_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(STEAM_FLUID_TYPE, STEAM, STEAM);
    public static final BaseFlowingFluid.Properties SODIUM_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(SODIUM_FLUID_TYPE, SODIUM, SODIUM);
    public static final BaseFlowingFluid.Properties CHLORINE_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(CHLORINE_FLUID_TYPE, CHLORINE, CHLORINE);
    public static final BaseFlowingFluid.Properties OXYGEN_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(OXYGEN_FLUID_TYPE, OXYGEN, OXYGEN);
    public static final BaseFlowingFluid.Properties LIQUID_AMMONIA_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(LIQUID_AMMONIA_FLUID_TYPE, LIQUID_AMMONIA, LIQUID_AMMONIA);
    public static final BaseFlowingFluid.Properties AMMONIA_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(AMMONIA_FLUID_TYPE, AMMONIA, AMMONIA);
    public static final BaseFlowingFluid.Properties HEAVY_WATER_FLUID_PROPERTIES = new BaseFlowingFluid.Properties(HEAVY_WATER_FLUID_TYPE, HEAVY_WATER, HEAVY_WATER);

}