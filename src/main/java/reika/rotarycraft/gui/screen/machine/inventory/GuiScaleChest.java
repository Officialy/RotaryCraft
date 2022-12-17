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
//import net.minecraft.client.gui.Button;
//import net.minecraft.entity.player.Player;
//import net.minecraft.inventory.Container;
//
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerScaleChest;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.blockentities.storage.BlockEntityScaleableChest;
//
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.OnlyIn;
//
//
//public class GuiScaleChest extends GuiPowerOnlyMachine {
//    private final Container upperScaleChestInventory;
//    private final BlockEntityScaleableChest scale;
//    int x;
//    int y;
//    private int numrows;
//    private int page;
//    private int invsize = 0;
//
//    public GuiScaleChest(Player p5ep, BlockEntityScaleableChest te, int page) {
//        super(new ContainerScaleChest(p5ep, te, page), te);
//        upperScaleChestInventory = p5ep.inventory;
//        allowUserInput = false;
//        short var3 = 222;
//        int var4 = var3 - 108;
//        invsize = te.getNumberSlots();
//        scale = te;
//        imageHeight = var4 + 18 * BlockEntityScaleableChest.MAXROWS;//ReikaMathLibrary.extrema(numrows, scale.MAXROWS, "min")*18;
//        ep = p5ep;
//        this.setValues();
//        this.page = page;
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        int j = (width - imageWidth) / 2 + 8;
//        int k = (height - imageHeight) / 2 - 12;
//        renderables.add(new Button(0, j + imageWidth - 9, k + 45, 40, 20, "Next"));
//        renderables.add(new Button(1, j + imageWidth - 9, k + 65, 40, 20, "Back"));
//    }
//
//    private void setValues() {
//        int oldpage = page;
//        numrows = (int) Math.ceil(invsize / 9D);
//        if (numrows > BlockEntityScaleableChest.MAXROWS) {
//            numrows = BlockEntityScaleableChest.MAXROWS;
//        }
//        if (page == scale.getMaxPage()) {
//            numrows = (int) Math.ceil((invsize - (page * 9 * BlockEntityScaleableChest.MAXROWS)) / 9D);
//        }
//        if (page != oldpage) {
//
//        }
//    }
//
//    @Override
//    protected void actionPerformed(Button button) {
//        super.actionPerformed(button);
//        int oldpage = page;
//        if (button.id == 0 && page < scale.getMaxPage() - 1)
//            page++;
//        if (button.id == 1 && page > 0)
//            page--;
//        if (page == oldpage)
//            return;
//        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CHEST.ordinal(), scale, page);
//        //ep.closeScreen();
//        //this.refresh();
//        //this.setValues();
//    }
//
//    /**
//     * Draw the foreground layer for the GuiContainer (everything in front of the items)
//     */
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        this.setValues();
//        super.drawGuiContainerForegroundLayer(a, b);
//        font.draw("Page " + page, imageWidth - 48, 6, 4210752);
//        int var3 = 0;
//        int pageinv = invsize - page * 9 * BlockEntityScaleableChest.MAXROWS;
//        int pagerows = numrows;
//        if (pagerows > BlockEntityScaleableChest.MAXROWS)
//            pagerows = BlockEntityScaleableChest.MAXROWS;
//        int var4 = 7 + pageinv * 18 - (numrows - 1) * 18 * 9;
//        int var5 = -1 + pagerows * 18;
//        int diff = pagerows * 9 - pageinv;
//        if (page < scale.getMaxPage())
//            diff = 0;
//        int color1 = 0xffeeeeee;
//        int color2 = 0xff939393;
//        int color3 = 0xffc6c6c6;
//
//        ReikaGuiAPI.drawRect(var4, var5, var4 + 18 * diff, var5 + 18, color3);
//        if (pagerows < BlockEntityScaleableChest.MAXROWS) {
//            var4 = 7;
//            var5 += 18;
//            diff = BlockEntityScaleableChest.MAXROWS - pagerows;
//            ReikaGuiAPI.drawRect(var4, var5, var4 + 18 * 9, var5 + 18 * diff, color3);
//        }
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        if (scale.power < scale.MINPOWER) {
//            return;
//        }
//        String var4 = this.getGuiTexture();
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//        int var5 = (width - imageWidth) / 2;
//        int var6 = (height - imageHeight) / 2;
//        this.drawTexturedModalRect(var5, var6, 0, 0, imageWidth, BlockEntityScaleableChest.MAXROWS * 18 + 17);
//        this.drawTexturedModalRect(var5, var6 + BlockEntityScaleableChest.MAXROWS * 18 + 17, 0, 126, imageWidth, 96);
//
//        this.drawPowerTab(var5, var6);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "/Reika/RotaryCraft/Textures/GUI/basicstorage.png";
//    }
//}
