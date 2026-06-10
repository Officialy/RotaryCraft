package reika.rotarycraft.base.fluids.fluidtypes;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import reika.rotarycraft.RotaryCraft;

public class HslaFluidType extends FluidType {

    public static final Identifier HSLA_FLUID_STILL_TEXTURE = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "block/fluid/hsla_still");
    public static final Identifier HSLA_FLUID_FLOWING_TEXTURE = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "block/fluid/hsla_flow");
    public static final Identifier HSLA_FLUID_OVERLAY_TEXTURE = HSLA_FLUID_STILL_TEXTURE;
    public static final int TINT_COLOR = 0xF0B564;

    public HslaFluidType() {
        super(FluidType.Properties.create()
                .canHydrate(false)
                .canDrown(false)
                .canExtinguish(true)
                .canPushEntity(true)
                .canSwim(true)
                .pathType(PathType.LAVA)
                .adjacentPathType(PathType.FIRE)
                .fallDistanceModifier(0.15f)
                .motionScale(0.0115)
                .rarity(Rarity.UNCOMMON)
                .viscosity(6100)
                .density(7000)
                .temperature(1873)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH));
    }

    // 1.21.5: still/flow/overlay textures and tint live in the resource pack
    // (assets/rotarycraft/blockstates/molten_hsla*.json + fluid model JSON) — they are no longer
    // a Java responsibility. IClientFluidTypeExtensions only carries camera-overlay + fog hooks
    // now; this fluid needs neither, so no registration is required. The TEXTURE / TINT_COLOR
    // constants above are kept as the canonical source of truth for the resource authors.
}
