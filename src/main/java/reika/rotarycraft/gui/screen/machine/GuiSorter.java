///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.guis.Machine;
//
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.item.ItemStack;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//
//public class GuiSorter extends GuiPowerOnlyMachine {
//
//    private final BlockEntitySorting tile;
//
//    public GuiSorter(Inventory player, BlockEntitySorting te) {
//        super(new ContainerSorter(player, te), te);
//        tile = te;
//        imageWidth = 176;
//        imageHeight = 180;
//        ep = player;
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//        int dy = 22;
//        int x = 8;
//        int y = 18;
//        int l = BlockEntitySorting.LENGTH;
//        for (int k = 0; k < l * 3; k++) {
//            ItemStack is = tile.getMapping(k);
//            if (is != null) {
//                api.drawItemStack(itemRender, font, is, x + k % l * 18, y + k / l * dy);
//            }
//        }
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "sortergui";
//    }
//
//}
