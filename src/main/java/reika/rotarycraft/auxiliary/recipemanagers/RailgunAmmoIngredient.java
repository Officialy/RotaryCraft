package reika.rotarycraft.auxiliary.recipemanagers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.Codec;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import reika.rotarycraft.items.ItemRailGunAmmo;
import reika.rotarycraft.registry.RotaryItems;

/** Exact previous tier, including legacy damage metadata; the recipe viewer sees the actual mass. */
public record RailgunAmmoIngredient(int tier) implements ICustomIngredient {
    public static final MapCodec<RailgunAmmoIngredient> CODEC = Codec.intRange(0, 15).fieldOf("tier")
            .xmap(RailgunAmmoIngredient::new, RailgunAmmoIngredient::tier);
    public static final DeferredRegister<IngredientType<?>> TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.INGREDIENT_TYPES, "rotarycraft");
    public static final DeferredHolder<IngredientType<?>, IngredientType<RailgunAmmoIngredient>> TYPE =
            TYPES.register("railgun_ammo", () -> new IngredientType<>(CODEC));
    public RailgunAmmoIngredient { if (tier < 0 || tier > 15) throw new IllegalArgumentException("Invalid railgun tier: " + tier); }
    @Override public boolean test(ItemStack stack) { return stack.is(RotaryItems.RAILGUN_AMMO.get()) && ItemRailGunAmmo.power(stack) == tier; }
    @Override public Stream<Holder<Item>> items() { return Stream.of(RotaryItems.RAILGUN_AMMO.get().builtInRegistryHolder()); }
    @Override public boolean isSimple() { return false; }
    @Override public IngredientType<?> getType() { return TYPE.get(); }
    @Override public SlotDisplay display() { return new SlotDisplay.ItemStackSlotDisplay(net.minecraft.world.item.ItemStackTemplate.fromNonEmptyStack(ItemRailGunAmmo.stack(tier))); }
}
