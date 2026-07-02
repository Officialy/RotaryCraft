/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools.steel;

import reika.rotarycraft.RotaryCraft;
import net.minecraft.world.item.*;
import reika.rotarycraft.registry.RotaryItems;

public class ItemSteelHoe extends HoeItem {


    public ItemSteelHoe() {
        super(ToolMaterial.IRON, -3, 0.0F, RotaryItems.itemProperties().stacksTo(1).stacksTo(600));
    }
}