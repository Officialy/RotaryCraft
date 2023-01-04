///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.gui.screen.machine;
//
//import net.minecraft.client.gui.components.Button;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.inventory.MenuType;
//import reika.rotarycraft.base.NonPoweredMachineScreen;
//import reika.rotarycraft.blockentities.transmission.BlockEntitySplitter;
//
//public class SplitterScreen extends NonPoweredMachineScreen<SplitterMenu> {
//    private final BlockEntitySplitter splitter;
//    public int mode;
//    int x;
//    int y;
//
//    public SplitterScreen(SplitterContainer container,  int id, Inventory inv, BlockEntitySplitter splitter) {
//        super(container, inv, Component.literal("Splitter"));
//        this.splitter = splitter;
//        imageHeight = 140;
//        ep = inv;
//        mode = this.splitter.getRatioFromMode();
//    }
//
//    @Override
//    public void init() {
//        super.init();
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        //                                  x,            y,            widthIn,       heightIn,          buttonText
//        this.addRenderableWidget(new Button(j + 8, -1 + k + 32, 72, 20, Component.literal("31:1 Inline"), this::actionPerformed)); //id used to be 32
//        this.addRenderableWidget(new Button(j + 8, -1 + k + 52, 72, 20, Component.literal("15:1 Inline"), this::actionPerformed));
//        this.addRenderableWidget(new Button(j + 8, -1 + k + 72, 72, 20, Component.literal("7:1 Inline"), this::actionPerformed));
//        this.addRenderableWidget(new Button(j + 8, -1 + k + 92, 72, 20, Component.literal("3:1 Inline"), this::actionPerformed));
//
//        this.addRenderableWidget(new Button(j + 52, -1 + k + 114, 72, 20, Component.literal("1:1 Even"), this::actionPerformed));
//
//        this.addRenderableWidget(new Button(j + 96, -1 + k + 32, 72, 20, Component.literal("1:31 Bend"), this::actionPerformed));
//        this.addRenderableWidget(new Button(j + 96, -1 + k + 52, 72, 20, Component.literal("1:15 Bend"), this::actionPerformed));
//        this.addRenderableWidget(new Button(j + 96, -1 + k + 72, 72, 20, Component.literal("1:7 Bend"), this::actionPerformed));
//        this.addRenderableWidget(new Button(j + 96, -1 + k + 92, 72, 20, Component.literal("1:3 Bend"), this::actionPerformed));
//    }
//
//    public void updateMode(int md) {
//        splitter.setMode(md);
//    }
//
//    @Override
//    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
//        return super.mouseClicked(pMouseX, pMouseY, pButton);
//    }
//
//    protected void actionPerformed(Button button) {
//        //this.updateMode(button.id);
//        mode = button.getMessage().getContents().contains("31:1 Inline") ? 32 : button.getMessage().getContents().contains("15:1 Inline") ? 16 :
//                button.getMessage().getContents().contains("7:1 Inline") ? 8 : button.getMessage().getContents().contains("3:1 Inline") ? 4 : 1;
//
//        mode = button.getMessage().getContents().contains("1:31 Bend") ? -32 : button.getMessage().getContents().contains("1:15 Bend") ? -16 :
//                button.getMessage().getContents().contains("1:7 Bend") ? -8 : button.getMessage().getContents().contains("1:3 Bend") ? -4 : 1;
//
////        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.SPLITTERMODE.ordinal(), splitter, mode);
//
////        this.updateScreen();
//        this.updateMode(mode);
//
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "splittergui";
//    }
//}
