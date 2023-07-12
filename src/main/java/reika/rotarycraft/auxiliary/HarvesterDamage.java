/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.enchantment.Enchantments;
import reika.rotarycraft.blockentities.farming.BlockEntityMobHarvester;

public class HarvesterDamage extends /*Entity*/DamageSource {

    private final BlockEntityMobHarvester tile;

    public HarvesterDamage(BlockEntityMobHarvester te) {
        super(Holder.direct(new DamageType("player", DamageScaling.ALWAYS, 0.1F, DamageEffects.HURT)));//"player", te.getPlacer());
        tile = te;
    }

    public boolean isTileEqual(BlockEntityMobHarvester te) {
        return te != null && te.equals(tile);
    }

    public int getLootingLevel() {
        return tile.getEnchantmentHandler().getEnchantment(Enchantments.MOB_LOOTING);
    }

    public boolean hasInfinity() {
        return tile.getEnchantmentHandler().hasEnchantment(Enchantments.INFINITY_ARROWS);
    }

}
