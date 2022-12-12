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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShearsItem;

public abstract class ItemRotaryShears extends ShearsItem {

    protected ItemRotaryShears(Item.Properties properties) {
        super(new Item.Properties());
    }

}
