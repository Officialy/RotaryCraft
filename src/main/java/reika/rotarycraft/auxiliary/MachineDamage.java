package reika.rotarycraft.auxiliary;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.instantiable.CustomStringDamageSource;
import reika.dragonapi.libraries.ReikaEntityHelper;

import java.util.HashSet;

public class MachineDamage extends CustomStringDamageSource {

    private final HashSet<Integer> armorTypes = new HashSet<>();
    public BlockEntity lastMachine;
    private float armorFactor = 1;
    private int armorDamage = 0;

    public MachineDamage(String msg) {
        super(msg);
    }

    public MachineDamage setArmorBlocking(float f, int mult, int... types) { //0 is feet, 3 is helmet
        armorFactor = f;
        armorDamage = mult;
        for (int type : types) {
            armorTypes.add(type);
        }
        return this;
    }

    public float onDamageDealt(float base, LivingEntity e) {
        for (int slot : armorTypes) {
            ItemStack armor = e.getItemBySlot(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, slot));
            if (armor != null && armor.getItem() instanceof ArmorItem) {

//				float prot = ((ArmorItem)armor.getItem()).getArmorMaterial().getDamageReductionAmount(4-slot);
//				if (armor.getItem() instanceof ISpecialArmor) {
//					((ISpecialArmor)armor.getItem()).getProperties(e, armor, this, base, slot);
//				}
                ReikaEntityHelper.damageArmor(e, armorDamage, slot);
                base *= armorFactor;
            }
        }
        return base;
    }

}