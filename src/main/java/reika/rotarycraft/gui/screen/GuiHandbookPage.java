/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.screen;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Single-page handbook view shown from a machine's "Info" tab when the player
 * does not carry the handbook: no tab strip, no screen navigation, and only the
 * machine's own description/notes subpage.
 */
public final class GuiHandbookPage extends GuiHandbook {

    public GuiHandbookPage(Player p5ep, Level world, int sc, int pg) {
        super(p5ep, world, sc, pg);
    }

    @Override
    public boolean isLimitedView() {
        return true;
    }

    @Override
    public int getMaxSubpage() {
        return Math.min(1, super.getMaxSubpage());
    }
}
