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

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import reika.rotarycraft.base.ItemRotaryTool;
import reika.rotarycraft.gui.screen.GuiHandbook;

public class ItemHandBook extends ItemRotaryTool {

    public ItemHandBook() {
        super(reika.rotarycraft.registry.RotaryItems.itemProperties());
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide() && hand.equals(InteractionHand.MAIN_HAND)) {
            Minecraft.getInstance().gui.setScreen(new GuiHandbook(player, level, 0, 0));
        }
        return super.use(level, player, hand);
    }
}
