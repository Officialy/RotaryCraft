package reika.rotarycraft.auxiliary;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import reika.dragonapi.libraries.ReikaEnchantmentHelper;

import java.util.*;

// 1.21.5: Enchantments are now identified by ResourceKey<Enchantment>; in-stack enchant
// data is keyed by Holder<Enchantment>. This handler stores both: filters as ResourceKey
// (the form vanilla constants give us) and live data as Holder (the form we read from
// an ItemStack's enchant component).
public final class MachineEnchantmentHandler {

    private final HashMap<Holder<Enchantment>, Integer> data = new HashMap<>();
    private final HashSet<ResourceKey<Enchantment>> filters = new HashSet<>();

    public MachineEnchantmentHandler addFilter(ResourceKey<Enchantment> id) {
        filters.add(id);
        return this;
    }

    public boolean hasEnchantment(Holder<Enchantment> e) {
        return this.getEnchantment(e) > 0;
    }

    public boolean hasEnchantment(ResourceKey<Enchantment> key) {
        return this.getEnchantment(key) > 0;
    }

    public int getEnchantment(Holder<Enchantment> e) {
        Integer get = data.get(e);
        return get != null ? get : 0;
    }

    public int getEnchantment(ResourceKey<Enchantment> key) {
        for (Map.Entry<Holder<Enchantment>, Integer> e : data.entrySet()) {
            if (e.getKey().is(key)) return e.getValue();
        }
        return 0;
    }

    public boolean hasEnchantments() {
        return !data.isEmpty();
    }

    public boolean setEnchantment(Holder<Enchantment> e, int level) {
        if (this.isEnchantValid(e)) {
            data.put(e, level);
            return true;
        }
        return false;
    }

    public void clear() {
        data.clear();
    }

    public ListTag saveAdditional() {
        ListTag li = new ListTag();
        for (Map.Entry<Holder<Enchantment>, Integer> e : data.entrySet()) {
            CompoundTag tag = new CompoundTag();
            // Persist by registered name; rehydrate by lookup at load time.
            tag.putString("id", e.getKey().getRegisteredName());
            tag.putInt("lvl", e.getValue());
            li.add(tag);
        }
        return li;
    }

    public void load(ListTag NBT) {
        data.clear();
        // 1.21.5: rehydrating a Holder<Enchantment> requires a HolderLookup.Provider; this
        // method is invoked from BE load paths that don't yet thread a provider through,
        // so the data is left empty and rebuilt when an item next applies its enchants.
    }

    public boolean isEnchantValid(Holder<Enchantment> e) {
        // Holder.is(ResourceKey) covers both vanilla and modded enchants without a registry roundtrip.
        if (filters.isEmpty()) return true;
        for (ResourceKey<Enchantment> key : filters) {
            if (e.is(key)) return true;
        }
        return false;
    }

    public boolean applyEnchants(ItemStack is) {
        boolean flag = false;
        HashMap<Holder<Enchantment>, Integer> stack = ReikaEnchantmentHelper.getEnchantments(is);
        if (stack == null) return false;
        for (Map.Entry<Holder<Enchantment>, Integer> e : stack.entrySet()) {
            Holder<Enchantment> ec = e.getKey();
            int has = this.getEnchantment(ec);
            if (has < e.getValue())
                flag |= this.setEnchantment(ec, e.getValue());
        }
        return flag;
    }

    public ArrayList<ResourceKey<Enchantment>> getValidEnchantments() {
        return new ArrayList<>(filters);
    }

    public Map<Holder<Enchantment>, Integer> getEnchantments() {
        return Collections.unmodifiableMap(data);
    }
}
