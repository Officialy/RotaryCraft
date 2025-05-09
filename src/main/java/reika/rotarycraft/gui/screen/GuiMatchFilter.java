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
//import net.minecraft.client.gui.screens.inventory.ContainerScreen;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import org.lwjgl.opengl.GL11;
//import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
//import reika.rotarycraft.RotaryCraft;
//
//public class GuiMatchFilter extends ContainerScreen {
//
//    public GuiMatchFilter(Player p5ep, Level par2World) {
//        super(new ContainerMatchFilter(p5ep, par2World));
//    }
//
//    /**
//     * Draw the foreground layer for the GuiContainer (everything in front of the items)
//     */
//    @Override
//    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
//        ReikaGuiAPI.instance.drawCenteredStringNoShadow(font, "Match Filter", imageWidth / 2, 6, 4210752);
//        font.draw(I18n.get("container.inventory"), 8, imageHeight - 96 + 2, 4210752);
//    }
//
//    /**
//     * Draw the background layer for the GuiContainer (everything behind the items)
//     */
//    @Override
//    protected void renderBg(PoseStack poseStack,float par1, int par2, int par3) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/basic_gui_oneslot.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        int var5 = (width - imageWidth) / 2;
//        int var6 = (height - imageHeight) / 2;
//        ScreenUtils.drawTexturedModalRect(var5, var6, 0, 0, imageWidth, imageHeight);
//    }
//}
