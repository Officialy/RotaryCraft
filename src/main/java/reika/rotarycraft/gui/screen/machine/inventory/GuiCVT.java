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
//import net.minecraft.client.gui.GuiTextField;
//import net.minecraft.entity.player.Player;
//import net.minecraft.init.Blocks;
//import net.minecraft.init.Items;
//import net.minecraft.world.item.ItemStack;
//
//import net.neoforged.fluids.Fluid;
//import net.neoforged.fluids.Fluids;
//
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.RotaryItems;
//import reika.rotarycraft.base.GuiNonPoweredMachine;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerCVT;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;
//import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear.CVTMode;
//
//public class GuiCVT extends GuiNonPoweredMachine {
//    private final BlockEntityAdvancedGear cvt;
//    public int ratio;
//    private boolean reduction;
//    private CVTMode mode;
//    private int buttontimer = 0;
//    //private Level level = ModLoader.getMinecraftInstance().theWorld;
//    private GuiTextField input;
//
//    //Make gui look cool (like connecting spindles with belts)
//
//    public GuiCVT(Player p5ep, BlockEntityAdvancedGear AdvancedGear) {
//        super(new ContainerCVT(p5ep, AdvancedGear), AdvancedGear);
//        cvt = AdvancedGear;
//        imageHeight = 237;
//        imageWidth = 240;
//        ep = p5ep;
//        ratio = cvt.getRatio();
//        if (ratio > cvt.getMaxRatio())
//            ratio = cvt.getMaxRatio();
//        reduction = ratio < 0;
//        mode = cvt.getMode();
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", this.ratio));
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        int j = (width - imageWidth) / 2 + 8;
//        int k = (height - imageHeight) / 2 - 12;
//
//        input = null;
//
//        switch (mode) {
//            case AUTO:
//                input = new GuiTextField(font, j + imageWidth / 2 + 24, k + 48, 76, 16);
//                input.setFocused(false);
//                input.setMaxStringLength(9);
//                break;
//            case MANUAL:
//                if (ratio > 0)
//                    addRenderableWidget(new Button(1, j + imageWidth / 2 - 6, -1 + k + 64, 80, 20, "Speed"));
//                else
//                    addRenderableWidget(new Button(1, j + imageWidth / 2 - 6, -1 + k + 64, 80, 20, "Torque"));
//                input = new GuiTextField(font, j + imageWidth / 2 + 24, k + 39, 26, 16);
//                input.setFocused(false);
//                input.setMaxStringLength(3);
//                break;
//            case REDSTONE:
//                addRenderableWidget(new Button(1, j + imageWidth / 2 + 25, -1 + k + 44, 71, 20, ""));
//                addRenderableWidget(new Button(2, j + imageWidth / 2 + 25, -1 + k + 67, 71, 20, ""));
//                break;
//        }
//
//        addRenderableWidget(new Button(0, j + imageWidth / 2 + 84, -1 + k + 19, 20, 20, ""));
//    }
//
//    @Override
//    protected void keyTyped(char c, int i) {
//        super.keyTyped(c, i);
//        if (input != null)
//            input.textboxKeyTyped(c, i);
//    }
//
//    @Override
//    protected void mouseClicked(int i, int j, int k) {
//        super.mouseClicked(i, j, k);
//        if (input != null)
//            input.mouseClicked(i, j, k);
//    }
//
//    @Override
//    protected void actionPerformed(Button button) {
//        super.actionPerformed(button);
//        if (buttontimer > 0)
//            return;
//        else
//            buttontimer = 8;
//        if (button.id == 0) {
//            mode = mode.next();
//            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CVTMODE.ordinal(), cvt);
//        }
//
//        switch (mode) {
//            case AUTO:
//                break;
//            case MANUAL:
//                if (button.id == 1) {
//                    if (ratio > cvt.getMaxRatio())
//                        ratio = cvt.getMaxRatio();
//                    ratio = -ratio;
//                    reduction = ratio < 0;
//                    ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CVTRATIO.ordinal(), cvt, ratio);
//                }
//                break;
//            case REDSTONE:
//                if (button.id == 1) {
//                    cvt.incrementCVTState(true);
//                    ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CVTREDSTONESTATE.ordinal(), cvt, 1);
//                }
//                if (button.id == 2) {
//                    cvt.incrementCVTState(false);
//                    ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CVTREDSTONESTATE.ordinal(), cvt, 0);
//                }
//                break;
//        }
//
//        super.updateScreen();
//        this.initGui();
//    }
//
//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//        if (input != null) {
//            if (input.getText().isEmpty())
//                return;
//            PacketRegistry p = mode == CVTMode.AUTO ? PacketRegistry.CVTTARGET : PacketRegistry.CVTRATIO;
//            int val = 1;
//            if (!(input.getText().matches("^[0-9 ]+$"))) {
//                input.deleteFromCursor(-1);
//                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, p.ordinal(), cvt, val);
//                return;
//            }
//            val = ReikaJavaLibrary.safeIntParse(input.getText());
//            if (mode == CVTMode.MANUAL && reduction)
//                val = -val;
//            if (val != 0)
//                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, p.ordinal(), cvt, val);
//        }
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        if (buttontimer > 0)
//            buttontimer--;
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        if (cvt.hasLubricant()) {
//            Fluid f = RotaryFluids.LUBRICANT.get();
//            IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(f);
//            ReikaLiquidRenderer.bindFluidTexture(f);
//            GL11.glColor4f(1, 1, 1, 1);
//            this.drawTexturedModelRectFromIcon(186, 89, ico, 16, 48);
//        }
//
//        if (api.isMouseInBox(j + 185, j + 202, k + 88, k + 149)) {
//            String s = "Lubricant";
//            api.drawTooltipAt(font, s, -j + api.getMouseRealX() + 55 - font.width(s), -k + api.getMouseRealY());
//        }
//
//        switch (mode) {
//            case REDSTONE:
//                api.drawItemStack(itemRender, font, new ItemStack(Blocks.redstone_torch), 129, 31);
//                api.drawItemStack(itemRender, font, new ItemStack(Blocks.unlit_redstone_torch), 129, 54);
//
//                this.drawCenteredString(font, cvt.getCVTString(true), 188, 37, 0xffffff);
//                this.drawCenteredString(font, cvt.getCVTString(false), 188, 60, 0xffffff);
//
//                int dy = 17;
//                int dx = -14;
//                font.draw("Belt Ratio:", imageWidth / 2 - 32 + dx, 31 + dy, 4210752);
//
//                api.drawItemStack(itemRender, font, new ItemStack(Items.redstone), imageWidth / 2 + 94, 7);
//                break;
//            case MANUAL:
//                if (!input.isFocused()) {
//                    font.draw(String.format("%d", Math.abs(cvt.getRatio())), imageWidth / 2 + 36, 31, 0xffffffff);
//                }
//                font.draw("Belt Ratio:", imageWidth / 2 - 32, 31, 4210752);
//                this.drawCenteredString(font, "M", imageWidth / 2 + 102, 12, 0xffffffff);
//                break;
//            case AUTO:
//                if (!input.isFocused()) {
//                    font.draw(String.format("%d", Math.abs(cvt.getTargetTorque())), imageWidth / 2 + 36, 40, 0xffffffff);
//                }
//                font.draw("Target Torque:", imageWidth / 2 - 48, 40, 4210752);
//                font.draw(String.format("Current Input: %d Nm", cvt.getTorqueIn()), imageWidth / 2 - 30, 60, 4210752);
//                int r = cvt.getRatio();
//                font.draw(String.format("Current Ratio: %dx (%s)", Math.abs(r), r < 0 ? "Torque" : "Speed"), imageWidth / 2 - 30, 72, 4210752);
//                api.drawItemStack(itemRender, font, RotaryItems.pcb, imageWidth / 2 + 94, 8);
//                break;
//        }
//
//        if (api.isMouseInBox(j + imageWidth / 2 + 92, j + imageWidth / 2 + 112, -1 + k + 7, -1 + k + 27)) {
//            String s = "Control Mode";
//            api.drawTooltipAt(font, s, api.getMouseRealX() - 5 - font.width(s), api.getMouseRealY());
//        }
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        if (input != null)
//            input.drawTextBox();
//
//        switch (mode) {
//            case MANUAL:
//                if (ratio > cvt.getMaxRatio())
//                    api.drawCenteredStringNoShadow(font, String.format("(%d)", cvt.getMaxRatio()), j + imageWidth / 2 + 88, k + 31, 0xff0000);
//                else if (ratio == 0)
//                    api.drawCenteredStringNoShadow(font, "(1)", j + imageWidth / 2 + 88, k + 31, 0xff0000);
//                else
//                    api.drawCenteredStringNoShadow(font, String.format("(%d)", Math.abs(cvt.getRatio())), j + imageWidth / 2 + 88, k + 31, 4210752);
//                break;
//            case AUTO:
//                break;
//            case REDSTONE:
//                break;
//        }
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return mode == CVTMode.REDSTONE ? "cvtgui2" : "cvtgui";
//    }
//
//}
