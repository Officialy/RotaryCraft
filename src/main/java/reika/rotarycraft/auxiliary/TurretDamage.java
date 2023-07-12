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


import net.minecraft.world.damagesource.DamageType;
import reika.dragonapi.instantiable.CustomStringDamageSource;
import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TurretDamage extends CustomStringDamageSource {

    private final Player player;
    private final BlockEntity tile;

    public TurretDamage(BlockEntityAimedCannon te) {
//        this(te, te.getPlacer());
        this(te, null);
    }

    public TurretDamage(BlockEntityAimedCannon te, Player ep) {
        super("found themselves in high-powered crosshairs");
        player = ep;
        tile = te;
    }

  /*  @Override
    public boolean isProjectile() {
        return true;
    }*/

    @Override
    public Entity getEntity() {
        return player;
    }

//    @Override
    public boolean isUnblockable() {
        return true;
    }

}
