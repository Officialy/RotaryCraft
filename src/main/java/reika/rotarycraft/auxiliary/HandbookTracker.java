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

import reika.dragonapi.instantiable.ItemOnSpawnTracker;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import reika.rotarycraft.registry.RotaryItems;

public class HandbookTracker extends ItemOnSpawnTracker {

    @Override
    public ItemStack getItem() {
        return RotaryItems.HANDBOOK.get().getDefaultInstance();
    }

    public String getID() {
        return "RotaryCraft_Handbook";
    }


}
