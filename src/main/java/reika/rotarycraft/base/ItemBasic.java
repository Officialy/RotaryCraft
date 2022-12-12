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

import net.minecraft.world.item.Item;

import java.util.Random;

public class ItemBasic extends Item {

    protected Random par5Random = new Random();

    public ItemBasic(Properties properties, int max) {
        super(properties.stacksTo(max));
    }

}