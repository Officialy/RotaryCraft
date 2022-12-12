/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base;

import reika.rotarycraft.RotaryCraft;
import reika.dragonapi.interfaces.registry.OreType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public abstract class AutoOreItem extends ItemBasic {

    public AutoOreItem() {
        super(new Properties(), 64);
    }

    @Override
    public Component getName(ItemStack is) {
        return super.getName(is);

    }

    public abstract OreType getOreType(ItemStack item);

}
