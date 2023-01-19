/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools;

import net.minecraft.client.Minecraft;
import reika.dragonapi.auxiliary.PopupWriter;
import reika.rotarycraft.base.ItemRotaryTool;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import reika.rotarycraft.gui.screen.GuiCalculator;

public class ItemCalculator extends ItemRotaryTool {

    public ItemCalculator(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new GuiCalculator(player, level));
        return super.use(level, player, hand);
    }
}