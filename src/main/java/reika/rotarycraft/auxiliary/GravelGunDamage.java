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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class GravelGunDamage extends CustomStringDamageSource {

    private final Player player;

    public GravelGunDamage(Player ep) {
        super("was hit by supersonic flint");
        player = ep;
    }

    @Override
    public Entity getEntity() {
        return player;
    }

}
