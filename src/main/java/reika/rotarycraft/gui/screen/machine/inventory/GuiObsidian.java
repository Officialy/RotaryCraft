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
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Inventory;
//import net.neoforged.client.gui.ScreenUtils;
//import org.lwjgl.opengl.GL11;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiMachine;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerObsidian;
//import reika.rotarycraft.blockentities.production.BlockEntityObsidianMaker;
//
//public class GuiObsidian extends GuiMachine {
//    private final BlockEntityObsidianMaker obs;
//
//    public GuiObsidian(int id, Inventory p5ep, Component component, BlockEntityObsidianMaker obsidian) {
//        super(new ContainerObsidian(id, p5ep, obsidian), p5ep, obsidian, component);
//        obs = obsidian;
//        ep = p5ep;
//    }
//
//    //    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
////        super.drawGuiContainerForegroundLayer(a, b);
//
//        if (api.isMouseInBox(j + 47, j + 55, k + 16, k + 71)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("Water: %dmB", obs.getWater()), mx - j, my - k);
//        }
//        if (api.isMouseInBox(j + 119, j + 127, k + 16, k + 71)) {
//            int mx = api.getMouseRealX();
//            int my = api.getMouseRealY();
//            api.drawTooltipAt(font, String.format("Lava: %dmB", obs.getLava()), mx - j, my - k);
//        }
//    }
//
//    @Override
//    protected void renderBg(PoseStack stack, float par1, int par2, int par3) {
//        super.renderBg(stack, par1, par2, par3);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        int i1 = obs.getWaterScaled(54);
//        int i2 = obs.getLavaScaled(54);
//        ScreenUtils.drawTexturedModalRect(stack, j + 48, k + 71 - i1, 193, 55 - i1, 7, i1, 0);
//        ScreenUtils.drawTexturedModalRect(stack, j + 120, k + 71 - i2, 202, 55 - i2, 7, i2, 0);
//    }
//
//    @Override
//    protected void drawPowerTab(PoseStack stack, int var5, int var6) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/powertab.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        ScreenUtils.drawTexturedModalRect(stack, imageWidth + var5, var6 + 4, 0, 4, 42, imageHeight - 4);
//
//        long frac = (obs.power * 29L) / obs.MINPOWER;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(stack, imageWidth + var5 + 5, imageHeight + var6 - 144, 0, 0, (int) frac, 4);
//
//        frac = (int) (obs.omega * 29L) / obs.MINSPEED;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(stack, imageWidth + var5 + 5, imageHeight + var6 - 84, 0, 0, (int) frac, 4);
//
//        frac = (int) (obs.torque * 29L) / obs.MINTORQUE;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(stack, imageWidth + var5 + 5, imageHeight + var6 - 24, 0, 0, (int) frac, 4);
//
//        api.drawCenteredStringNoShadow(stack, font, "Power:", imageWidth + var5 + 20, var6 + 9, 0xff000000);
//        api.drawCenteredStringNoShadow(stack, font, "Speed:", imageWidth + var5 + 20, var6 + 69, 0xff000000);
//        api.drawCenteredStringNoShadow(stack, font, "Torque:", imageWidth + var5 + 20, var6 + 129, 0xff000000);
//        //this.drawCenteredStringNoShadow(font, String.format("%d/%d", obs.power, obs.MINPOWER), imageWidth+var5+16, var6+16, 0xff000000);
//    }
//
//    @Override
//    protected ResourceLocation getGuiTexture() {
//        return "obsidiangui";
//    }
//}
