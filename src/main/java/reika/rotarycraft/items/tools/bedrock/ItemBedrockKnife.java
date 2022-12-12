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

public class ItemBedrockKnife extends ItemRotaryTool {

    public ItemBedrockKnife(Properties properties) {
        super(properties);
    }

    @Override
    public void onCraftedBy(ItemStack p_41447_, Level p_41448_, Player p_41449_) {
        //  RotaryAdvancements.BEDROCKTOOLS.triggerAchievement(ep);

    }
}
