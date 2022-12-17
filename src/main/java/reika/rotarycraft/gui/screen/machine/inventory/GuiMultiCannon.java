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
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.entity.player.Player;
//import net.minecraft.inventory.Slot;
//
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiMachine;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerMultiCannon;
//import reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityMultiCannon;
//
//
//public class GuiMultiCannon extends GuiMachine {
//
//    private final BlockEntityMultiCannon tile;
//
//    public GuiMultiCannon(Player ep, BlockEntityMultiCannon te) {
//        super(new ContainerMultiCannon(ep, te), te);
//        this.ep = ep;
//        tile = te;
//        imageHeight = 204;
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "gatlinggui";
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//
//        if (tile.isReloading()) {
//            int j = (width - imageWidth) / 2;
//            int k = (height - imageHeight) / 2;
//
//            String i = "/Reika/RotaryCraft/Textures/GUI/" + this.getGuiTexture() + ".png";
//            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//            ReikaTextureHelper.bindTexture(RotaryCraft.class, i);
//
//
//            ScreenUtils.drawTexturedModalRect(j + 12, k + 31, 178, 2, 16, 5);
//        }
//    }
//
//    @Override
//    protected void drawPowerTab(int j, int k) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/powertab.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        ScreenUtils.drawTexturedModalRect(imageWidth + j, k + 4, 0, 4, 42, imageHeight - 4 - 40);
//
//        long frac = (tile.power * 29L) / tile.MINPOWER;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(imageWidth + j + 5, imageHeight + k - 182, 0, 0, (int) frac, 4);
//
//        frac = (int) (tile.omega * 29L) / tile.MINSPEED;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + j + 5, imageHeight + k - 122, 0, 0, (int) frac, 4);
//
//        frac = (int) (tile.torque * 29L) / tile.MINTORQUE;
//        if (frac > 29)
//            frac = 29;
//        this.drawTexturedModalRect(imageWidth + j + 5, imageHeight + k - 62, 0, 0, (int) frac, 4);
//
//        api.drawCenteredStringNoShadow(font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
//        api.drawCenteredStringNoShadow(font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
//        //this.drawCenteredStringNoShadow(font, String.format("%d/%d", tile.power, tile.MINPOWER), imageWidth+j+16, k+16, 0xff000000);
//    }
//
//    @Override
//    public boolean isMouseOverSlot(Slot p_146981_1_, int p_146981_2_, int p_146981_3_) {
//        return this.func_146978_c(p_146981_1_.xDisplayPosition, p_146981_1_.yDisplayPosition, 15, 15, p_146981_2_, p_146981_3_); //reduced slot size
//    }
//
//}
