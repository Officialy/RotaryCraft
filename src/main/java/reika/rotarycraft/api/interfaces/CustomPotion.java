/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.api.interfaces;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;

/**
 * Implement this on item classes that represent custom potion bottles in order to allow the Aerosolizer to use them.
 * This is not necessary if you extend PotionItem.
 */
public interface CustomPotion {

    Potion getPotion(ItemStack is);

    int getAmplifier(ItemStack is);

    boolean isExtended(ItemStack is);

}
