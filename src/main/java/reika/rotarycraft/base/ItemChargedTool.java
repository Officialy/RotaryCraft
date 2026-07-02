/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base;

import reika.dragonapi.libraries.io.ReikaChatHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.RotaryItems;


public abstract class ItemChargedTool extends ItemRotaryTool {

    public ItemChargedTool() {
        super(RotaryItems.itemProperties());
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return super.use(level, player, hand);
    }

    protected final void noCharge() {
        if (ConfigRegistry.CLEARCHAT.getState()) {
            ReikaChatHelper.clearChat();
        }
        ReikaChatHelper.write("Tool charge is depleted!");
    }

    protected final void warnCharge(ItemStack is) {
        if (ConfigRegistry.CLEARCHAT.getState())
        	ReikaChatHelper.clearChat();
    }

    public final String getItemStackDisplayName(ItemStack is) {
        return super.getName(is).toString();
    }

}
