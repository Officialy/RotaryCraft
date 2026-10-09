package reika.rotarycraft.renders.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import reika.rotarycraft.items.ItemRailGunAmmo;

/** Reads both native tier data and the old port's damage metadata for the four authored icons. */
public record RailgunPowerProperty() implements RangeSelectItemModelProperty {
    public static final MapCodec<RailgunPowerProperty> CODEC = MapCodec.unit(new RailgunPowerProperty());
    @Override public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        return ItemRailGunAmmo.power(stack);
    }
    @Override public MapCodec<RailgunPowerProperty> type() { return CODEC; }
}
