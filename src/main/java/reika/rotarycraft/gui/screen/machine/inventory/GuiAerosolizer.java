///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.guis.Machine.Inventory;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.entity.player.Inventory;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.blockentities.level.BlockEntityAerosolizer;
//import reika.rotarycraft.containers.Machine.Inventory.ContainerAerosolizer;
//
//public class GuiAerosolizer extends GuiPowerOnlyMachine {
//    BlockEntityAerosolizer aero;
//
//    public GuiAerosolizer(int id, Inventory p5ep, BlockEntityAerosolizer aerosolizer, Component component) {
//        super(new ContainerAerosolizer(id, p5ep, aerosolizer), p5ep, component);
//        aero = aerosolizer;
//        ep = p5ep;
//    }
//
//    /**
//     * Draw the background layer for the GuiContainer (everything behind the items)
//     */
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        int var5 = (width - imageWidth) / 2;
//        int var6 = (height - imageHeight) / 2;
//
//        super.renderBg(poseStack, par1, par2, par3);
//
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                int amount = aero.getPotionLevel(3 * i + j) / 4;
//                //ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.format("%d %d %d %d %d %d %d %d %d", aero.potionLevel[0], aero.potionLevel[1], aero.potionLevel[2], aero.potionLevel[3], aero.potionLevel[4], aero.potionLevel[5], aero.potionLevel[6], aero.potionLevel[7], aero.potionLevel[8]));
//                api.fillBar(poseStack, var5 + 62 + 18 * j, var6 + 17 + 18 * i, 16, var6 + 17 + 18 * i + 16, aero.getPotionColor(3 * i + j), amount, 16, true);
//                api.drawCenteredStringNoShadow(poseStack, font, String.format("%d", aero.getPotionLevel(3 * i + j)), var5 + 70 + 18 * j, var6 + 22 + 18 * i, 0x000000);
//            }
//        }
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "aerosolizergui";
//    }
//}
