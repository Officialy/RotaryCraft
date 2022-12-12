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

import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.item.enchantment.Enchantments;
import reika.rotarycraft.blockentities.farming.BlockEntityMobHarvester;

public class HarvesterDamage extends EntityDamageSource {

    private final BlockEntityMobHarvester tile;

    public HarvesterDamage(BlockEntityMobHarvester te) {
        super("player", te.getPlacer());
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
