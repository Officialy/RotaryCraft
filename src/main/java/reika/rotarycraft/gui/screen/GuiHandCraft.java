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
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.screens.inventory.ContainerScreen;
//import net.minecraft.client.resources.language.I18n;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.client.gui.ScreenUtils;
//import org.lwjgl.opengl.GL11;
//import reika.rotarycraft.gui.container.ContainerHandCraft;
//
//public class GuiHandCraft extends ContainerScreen {
//    private static final ResourceLocation textures = new ResourceLocation("textures/gui/container/crafting_table.png");
//
//    public GuiHandCraft(int id, Player p5ep, Level par2World) {
//        super(new ContainerHandCraft(1, p5ep.getInventory(), par2World), p5ep.getInventory(), null);
//    }
//
//    /**
//     * Draw the foreground layer for the GuiContainer (everything in front of the items)
//     */
//    @Override
//    public void render(PoseStack stack, int p_98419_, int p_98420_, float p_98421_) {
//        font.draw(stack, I18n.get("container.crafting"), 28, 6, 4210752);
//        font.draw(stack, I18n.get("container.inventory"), 8, imageHeight - 96 + 2, 4210752);
//    }
//
//    /**
//     * Draw the background layer for the GuiContainer (everything behind the items)
//     */
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        String var4 = "/gui/crafting.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.setShaderTexture(0, textures);
//        int var5 = (width - imageWidth) / 2;
//        int var6 = (height - imageHeight) / 2;
//        ScreenUtils.drawTexturedModalRect(var5, var6, 0, 0, imageWidth, imageHeight);
//    }
//}
