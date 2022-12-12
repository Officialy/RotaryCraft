/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2017
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary.interfaces;


import net.minecraft.world.item.ItemStack;

public interface HandbookEntry {

	ItemStack getTabIcon();

	String getData();

	String getNotes(int subpage);

	boolean sameTextAllSubpages();

	String getTitle();

	boolean hasMachineRender();

	boolean hasSubpages();

	int getScreen();

	int getPage();

	boolean isConfigDisabled();

}
