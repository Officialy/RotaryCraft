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
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.Container;
//import net.minecraftforge.client.gui.ScreenUtils;
//import org.lwjgl.input.Mouse;
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.client.gui.GuiTextField;
//import net.minecraft.entity.player.Player;
//import net.minecraft.inventory.Container;
//import net.minecraft.util.I18n;
//
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiMachine;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerHeater;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.blockentities.auxiliary.BlockEntityHeater;
//
//public class GuiHeater extends GuiMachine {
//    private final Container upperHeaterInventory;
//    private final BlockEntityHeater heater;
//    public int temperature;
//    int x;
//    int y;
//    private GuiTextField input;
//    private int inventoryRows = 0;
//
//    public GuiHeater(Player p5ep, Container par2IInventory, BlockEntityHeater te) {
//        super(new ContainerHeater(p5ep, te), te);
//        ep = p5ep;
//        upperHeaterInventory = ep.inventory;
//        allowUserInput = false;
//        short var3 = 256;
//        int var4 = var3 - 108;
//        inventoryRows = par2IInventory.getContainerSize() / 9;
//        imageHeight = var4 + inventoryRows * 18;
//        heater = te;
//        temperature = te.setTemperature;
//        imageHeight = 167;
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        int j = (width - imageWidth) / 2 + 8;
//        int k = (height - imageHeight) / 2 - 12;
//        input = new GuiTextField(font, j + imageWidth / 2 + 40, k + 67, 32, 16);
//        input.setFocused(false);
//        input.setMaxStringLength(4);
//    }
//
//    @Override
//    protected void keyTyped(char c, int i) {
//        super.keyTyped(c, i);
//        input.textboxKeyTyped(c, i);
//    }
//
//    @Override
//    protected void mouseClicked(int i, int j, int k) {
//        super.mouseClicked(i, j, k);
//        input.mouseClicked(i, j, k);
//    }
//
//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//        x = Mouse.getX();
//        y = Mouse.getY();
//        if (input.getText().isEmpty()) {
//            return;
//        }
//        if (!(input.getText().matches("^[0-9 ]+$"))) {
//            temperature = 0;
//            input.deleteFromCursor(-1);
//            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.HEATER.ordinal(), heater, temperature);
//            return;
//        }
//        temperature = ReikaJavaLibrary.safeIntParse(input.getText());
//        if (temperature >= 0)
//            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.HEATER.ordinal(), heater, temperature);
//        heater.setTemperature = temperature;
//
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        api.drawCenteredStringNoShadow(font, tile.getMultiValuedName(), imageWidth / 2, 5, 4210752);
//        if (tile instanceof Container)
//            font.draw(I18n.get("container.inventory"), 8, (imageHeight - 96) + 3, 4210752);
//
//        font.draw("Temperature Control:", 26, 59, 4210752);
//        if (!input.isFocused()) {
//            font.draw(String.format("%d", heater.setTemperature), 140, 59, 0xffffffff);
//        }
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(poseStack, par1, par2, par3);
//        input.drawTextBox();
//    }
//
//    @Override
//    protected void drawPowerTab(PoseStack poseStack, int var5, int var6) {
//        String var4 = "/Reika/RotaryCraft/Textures/GUI/powertab.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//
////        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//
//        ScreenUtils.drawTexturedModalRect(poseStack,imageWidth + var5, var6 + 4, 0, 4, 42, imageHeight - 20,0);
//        ScreenUtils.drawTexturedModalRect(poseStack,imageWidth + var5, var6 + 4 + imageHeight - 20, 0, 157, 42, 6,0);
//
//        long frac = (heater.power * 29L) / heater.MINPOWER;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(poseStack,imageWidth + var5 + 5, imageHeight + var6 - 145, 0, 0, (int) frac, 4,0);
//
//        frac = heater.omega * 29L / heater.MINSPEED;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(poseStack,imageWidth + var5 + 5, imageHeight + var6 - 85, 0, 0, (int) frac, 4,0);
//
//        frac = heater.torque * 29L / heater.MINTORQUE;
//        if (frac > 29)
//            frac = 29;
//        ScreenUtils.drawTexturedModalRect(poseStack,imageWidth + var5 + 5, imageHeight + var6 - 25, 0, 0, (int) frac, 4,0);
//
//        api.drawCenteredStringNoShadow(poseStack, font, "Power:", imageWidth + var5 + 20, var6 + 9, 0xff000000);
//        api.drawCenteredStringNoShadow(poseStack, font, "Speed:", imageWidth + var5 + 20, var6 + 69, 0xff000000);
//        api.drawCenteredStringNoShadow(poseStack, font, "Torque:", imageWidth + var5 + 20, var6 + 129, 0xff000000);
//        //this.drawCenteredStringNoShadow(font, String.format("%d/%d", heater.power, heater.MINPOWER), imageWidth+var5+16, var6+16, 0xff000000);
//    }
//
//    @Override
//    protected ResourceLocation getGuiTexture() {
//        return ResourceLocation.parse("heatergui");
//    }
//}
