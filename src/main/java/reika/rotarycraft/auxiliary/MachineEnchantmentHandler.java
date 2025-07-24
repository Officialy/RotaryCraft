package reika.rotarycraft.auxiliary;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.registries.ForgeRegistries;
import reika.dragonapi.libraries.ReikaEnchantmentHelper;

import java.util.*;

public final class MachineEnchantmentHandler {

    private final HashMap<Enchantment, Integer> data = new HashMap<>();
    private final HashSet<Enchantment> filters = new HashSet<>();

    public MachineEnchantmentHandler addFilter(Enchantment id) {
        filters.add(id);
        return this;
    }

    public boolean hasEnchantment(Enchantment e) {
        return this.getEnchantment(e) > 0;
    }

    public int getEnchantment(Enchantment e) {
        Integer get = data.get(e);
        return get != null ? get : 0;
    }

    public boolean hasEnchantments() {
        return !data.isEmpty();
    }

    public boolean setEnchantment(Enchantment e, int level) {
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
        for (Map.Entry<Enchantment, Integer> e : data.entrySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putString("id", EnchantmentHelper.getEnchantmentId(e.getKey()).toString());
            tag.putInt("lvl", e.getValue());
            li.add(tag);
        }
        return li;
    }

    public void load(ListTag NBT) {
        data.clear();
        for (Object o : NBT) {
            CompoundTag tag = (CompoundTag) o;
            Enchantment e = Enchantment.byId(tag.getInt("id"));
            int lvl = tag.getInt("lvl");
            data.put(e, lvl);
        }
    }

    public boolean isEnchantValid(Enchantment e) {
        return filters.isEmpty() || filters.contains(e);
    }

    public boolean applyEnchants(ItemStack is) {
        boolean flag = false;
        for (Map.Entry<Enchantment, Integer> e : ReikaEnchantmentHelper.getEnchantments(is).entrySet()) {
            Enchantment ec = e.getKey();
            int has = this.getEnchantment(ec);
            if (has < e.getValue())
                flag |= this.setEnchantment(ec, e.getValue());
        }
        return flag;
    }

    public ArrayList<Enchantment> getValidEnchantments() {
        ArrayList<Enchantment> li = new ArrayList<>();
        li.addAll(filters);
        return li;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return Collections.unmodifiableMap(data);
    }

}
