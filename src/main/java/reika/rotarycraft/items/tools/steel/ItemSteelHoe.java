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

public class ItemSteelHoe extends Item {


    public ItemSteelHoe() {
        super(RotaryItems.itemProperties().hoe(ToolMaterial.IRON, -3, 0.0F)
                .durability(600).delayedComponent(net.minecraft.core.component.DataComponents.REPAIRABLE,
                        context -> new net.minecraft.world.item.enchantment.Repairable(net.minecraft.core.HolderSet.direct(
                                context.getOrThrow(net.minecraft.resources.ResourceKey.create(
                                        net.minecraft.core.registries.Registries.ITEM,
                                        net.minecraft.resources.Identifier.fromNamespaceAndPath("rotarycraft", "hsla_steel_ingot")))))));
    }
}
