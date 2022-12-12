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
//import net.minecraftforge.common.util.Direction;
//
//import reika.dragonapi.instantiable.gui.ImageButton;
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiNonPoweredMachine;
//import reika.rotarycraft.containers.Machine.Inventory.ContainerPowerBus;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.blockentities.transmission.BlockEntityPowerBus;
//
//public class GuiPowerBus extends GuiNonPoweredMachine {
//
//    private final BlockEntityPowerBus tile;
//
//    public GuiPowerBus(Player ep, BlockEntityPowerBus te) {
//        super(new ContainerPowerBus(ep, te), te);
//        this.ep = ep;
//        tile = te;
//
//        imageWidth = 220;
//        imageHeight = 220;
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        int j = (width - imageWidth) / 2 - 2;
//        int k = (height - imageHeight) / 2 - 12;
//
//        String file = "/Reika/RotaryCraft/Textures/GUI/buttons.png";
//
//        int[] dx = {j + 103, j + 103, j + 49, j + 139 + 18};
//        int[] dy = {k + 26, k + 134, k + 66 + 18 - 4, k + 66 + 18 - 4};
//
//        for (int i = 0; i < 4; i++) {
//            Direction dir = Direction.values()[i + 2];
//            int u = tile.isSideSpeedMode(dir) ? 54 : 36;
//            if (tile.canHaveItemInSlot(dir))
//                renderables.add(new ImageButton(i, dx[i], dy[i], 18, 18, u, 36, file, RotaryCraft.class));
//        }
//    }
//
//    @Override
//    protected void actionPerformed(Button button) {
//        super.actionPerformed(button);
//        if (button.id < 24000) {
//            Direction dir = Direction.values()[button.id + 2];
//            tile.setMode(dir, !tile.isSideSpeedMode(dir));
//            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.POWERBUS.ordinal(), tile, button.id);
//        }
//        this.initGui();
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        int[] x = {101, 101, 65, 137};
//        int[] y = {32, 104, 68, 68};
//        for (int i = 0; i < 4; i++) {
//            Direction dir = Direction.values()[i + 2];
//            int dx = x[i];
//            int dy = y[i];
//            if (tile.canHaveItemInSlot(dir)) {
//                this.drawRect(dx, dy, dx + 18, dy + 18, this.getColorForSide(dir));
//            } else {
//                String = "/Reika/RotaryCraft/Textures/GUI/" + this.getGuiTexture() + ".png";
//                ReikaTextureHelper.bindTexture(RotaryCraft.class, );
//                GL11.glColor3f(1, 1, 1);
//                ScreenUtils.drawTexturedModalRect(dx, dy, 8, 8, 18, 18);
//            }
//        }
//    }
//
//    private int getColorForSide(Direction dir) {
//        switch (dir) {
//            case EAST:
//                return 0x44ffff00;
//            case NORTH:
//                return 0x440000ff;
//            case SOUTH:
//                return 0x44ff0000;
//            case WEST:
//                return 0x4400ff00;
//            default:
//                return 0x44ffffff;
//        }
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "bus";
//    }
//
//}
