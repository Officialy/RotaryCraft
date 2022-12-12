/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools.bedrock;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import reika.rotarycraft.base.ItemRotaryTool;

//@Strippable(value = {"codechicken.microblock.Saw"})
public class ItemBedrockSaw extends ItemRotaryTool {//implements Saw {


    public ItemBedrockSaw(Properties properties) {
        super(properties);
    }

    /**
     * Called when item is crafted/smelted. Used only by maps so far.
     *
     * @param pStack
     * @param pLevel
     * @param pPlayer
     */
    @Override
    public void onCraftedBy(ItemStack pStack, Level pLevel, Player pPlayer) {
        //RotaryAdvancements.BEDROCKTOOLS.triggerAchievement(ep);

    }

//    @Override
//    public int getMaxCuttingStrength() {
//        return 5;
//    }
//
//    public int getCuttingStrength() {
//        return this.getMaxCuttingStrength();
//    }
//
//    @Override
//    public int getCuttingStrength(ItemStack stack) {
//        return this.getMaxCuttingStrength();
//    }
//
//    @Override
//    public boolean doesContainerItemLeaveCraftingGrid(ItemStack is) {
//        return false;
//    }

}
