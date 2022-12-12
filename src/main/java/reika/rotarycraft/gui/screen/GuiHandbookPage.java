///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.gui.screen;
//
//import net.minecraft.client.gui.components.Button;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//
//public final class GuiHandbookPage extends GuiHandbook {
//
//    public GuiHandbookPage(Player p5ep, Level world, int sc, int pg) {
//        super(p5ep, world, sc, pg);
//    }
//
//    @Override
//    public boolean isLimitedView() {
//        return true;
//    }
//
//    @Override
//    protected void actionPerformed(Button button) {
//        if (button.id == 12) {
//            minecraft.player.closeScreen();
//            return;
//        }
//        if (buttontimer > 0)
//            return;
//        buttontimer = 20;
//        if (button.id == 13) {
//            if (subpage != 1)
//                subpage++;
//            this.initGui();
//            return;
//        }
//        if (button.id == 14) {
//            if (subpage != 0)
//                subpage--;
//            this.initGui();
//            return;
//        }
//        time = System.nanoTime();
//        i = 0;
//        page = (byte) button.id;
//        subpage = 0;
//    }
//}
