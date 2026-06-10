package reika.rotarycraft.base.fluids.fluidtypes;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import reika.rotarycraft.RotaryCraft;

public class JetFuelFluidType extends FluidType {

    public static final Identifier JETFUEL_FLUID_STILL_TEXTURE = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "block/fluid/jetfuel");
    public static final Identifier JETFUEL_FLUID_FLOWING_TEXTURE = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "block/fluid/jetfuel_anim");
    public static final Identifier JETFUEL_FLUID_OVERLAY_TEXTURE = JETFUEL_FLUID_STILL_TEXTURE;
    public static final int TINT_COLOR = 0xF0B564;

    public JetFuelFluidType() {
        super(Properties.create()
                .canHydrate(false)
                .canDrown(false)
                .canExtinguish(false)
                .canPushEntity(true)
                .canSwim(true)
                .pathType(PathType.LAVA)
                .adjacentPathType(PathType.LAVA)
                .fallDistanceModifier(0.15f)
                .motionScale(0.0115)
                .rarity(Rarity.UNCOMMON)
                .viscosity(6100)
                .density(7000)
                .temperature(1873)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY));
    }

    // 1.21.5: fluid textures + tint are resource-pack driven (assets/rotarycraft/blockstates/jet_fuel*.json).
    // IClientFluidTypeExtensions only carries camera-overlay + fog hooks now; no Java registration needed.
}
