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

import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;

public interface NBTMachine {

	/** Do not return null. */
	CompoundTag getTagsToWriteToStack();

	/** May supply null. */
	void setDataFromItemStackTag(CompoundTag NBT);

	/** Do not return null. */
	ArrayList<CompoundTag> getCreativeModeVariants();

	/** Will never supply null. */
	ArrayList<String> getDisplayTags(CompoundTag NBT);
}
