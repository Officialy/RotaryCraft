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


import reika.dragonapi.instantiable.CustomStringDamageSource;
import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;
import reika.rotarycraft.registry.RotaryDamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TurretDamage extends CustomStringDamageSource {

    private final Player player;

    public TurretDamage(BlockEntityAimedCannon te) {
        this(te, te.getPlacer());
    }

    public TurretDamage(BlockEntityAimedCannon te, Player ep) {
        this(te.getLevel(), ep);
    }

    public TurretDamage(net.minecraft.world.level.Level level, Player ep) {
        super(CustomStringDamageSource.resolve(level, RotaryDamageTypes.TURRET), "found themselves in high-powered crosshairs");
        player = ep;
    }

    // Projectile and unblockable behavior is encoded in the generated native damage-type tags.

    @Override
    public Entity getEntity() {
        return player;
    }

}
