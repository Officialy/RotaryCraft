/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items;

import reika.rotarycraft.registry.RotaryBlocks;
import net.minecraft.world.item.BlockItem;

public class BlockItemDeco extends BlockItem {

    public BlockItemDeco() {
        super(RotaryBlocks.DECOTANK.get(), new Properties());
    }

}
