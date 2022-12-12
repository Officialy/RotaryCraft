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
//import net.minecraft.client.resources.language.I18n;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import org.lwjgl.opengl.GL11;
//
//import reika.rotarycraft.RotaryCraft;
//
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//public class GuiCraftingPattern extends ContainerScreen {
//
//    private final Player player;
//
//    public GuiCraftingPattern(Player p5ep, Level par2World) {
//        super(new ContainerCraftingPattern(p5ep, par2World));
//        player = p5ep;
//    }
//
//    private ItemStack getItem() {
//        return player.getCurrentEquippedItem();
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        renderables.clear();
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        renderables.add(new Button(0, j + 6, k + 6, 20, 20, ""));
//
//        String file = "/Reika/RotaryCraft/Textures/GUI/buttons.png";
//        renderables.add(new ImageButton(1, j + 4, k + 42, 24, 8, 18, 110, file, RotaryCraft.class));
//        renderables.add(new ImageButton(2, j + 4, k + 61, 24, 8, 42, 110, file, RotaryCraft.class));
//    }
//
//    @Override
//    public void actionPerformed(Button b) {
//        if (b.id == 0) {
//            if (RotaryItems.CRAFTPATTERN.matchItem(this.getItem())) {
//                RecipeMode next = ItemCraftPattern.getMode(this.getItem()).next();
//                ItemCraftPattern.setMode(this.getItem(), next);
//                ((ContainerCraftingPattern) player.openContainer).clearRecipe();
//                ReikaPacketHelper.sendDataPacket(RotaryCraft.packetChannel, PacketRegistry.CRAFTPATTERNMODE.ordinal(), PacketTarget.server, next.ordinal());
//            }
//        } else if (b.id == 1 || b.id == 2) {
//            int amt = GuiScreen.isCrouching() ? 64 : GuiScreen.isCtrlKeyDown() ? 16 : 1;
//            if (b.id == 2)
//                amt = -amt;
//            if (amt > 1 && ItemCraftPattern.getStackInputLimit(this.getItem()) == 1)
//                amt--;
//            ItemCraftPattern.changeStackLimit(this.getItem(), amt);
//            ReikaPacketHelper.sendDataPacket(RotaryCraft.packetChannel, PacketRegistry.CRAFTPATTERNLIMIT.ordinal(), PacketTarget.server, amt);
//        }
//        this.initGui();
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
//        String inv = I18n.get("container.inventory");
//        font.draw(inv, imageWidth - font.width(inv) - 8, imageHeight - 96 + 2, 4210752);
//        if (RotaryItems.CRAFTPATTERN.matchItem(this.getItem())) {
//            ReikaGuiAPI.instance.drawCenteredStringNoShadow(font, ItemCraftPattern.getMode(this.getItem()).displayName, imageWidth / 2, 6, 4210752);
//            int lim = ItemCraftPattern.getStackInputLimit(this.getItem());
//            ReikaGuiAPI.instance.drawCenteredStringNoShadow(font, lim == 64 ? "\u221E" : String.valueOf(lim), 16, 40 + 12, 4210752);
//            //ReikaGuiAPI.instance.drawCenteredStringNoShadow(font, "Limit", 16, 60, 4210752);
//            font.draw("Input Limit", 6, 72, 4210752);
//            ReikaGuiAPI.instance.drawItemStack(itemRender, ItemCraftPattern.getMode(this.getItem()).getIcon(), 8, 8);
//        }
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack,float par1, int par2, int par3) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/patterngui.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        int var5 = (width - imageWidth) / 2;
//        int var6 = (height - imageHeight) / 2;
//        ScreenUtils.drawTexturedModalRect(var5, var6, 0, 0, imageWidth, imageHeight);
//    }
//}
