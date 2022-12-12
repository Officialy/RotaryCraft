///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.guis.Machine;
//
//import net.minecraft.client.gui.components.Button;
//import net.minecraft.client.gui.components.ImageButton;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.item.ItemStack;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.containers.Machine.ContainerBlower;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.blockentities.BlockEntityBlower;
//
//public class GuiBlower extends GuiPowerOnlyMachine {
//
//    private final BlockEntityBlower tile;
//    private final boolean[] controls;
//
//    public GuiBlower(Inventory player, BlockEntityBlower te) {
//        super(new ContainerBlower(player, te), te);
//        tile = te;
//        imageWidth = 176;
//        imageHeight = 192;
//        ep = player;
//
//        controls = new boolean[4];
//        controls[0] = te.isWhitelist;
//        controls[1] = te.checkMeta;
//        controls[2] = te.checkNBT;
//        controls[3] = !te.useOreDict;
//    }
//
//    @Override
//    public void init() {
//        super.init();
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        String s = "Textures/GUI/blowergui.png";
//
//        for (int i = 0; i < 4; i++) {
//            int u = 176;
//            if (controls[i])
//                u += 18;
//            renderables.add(new ImageButton(i, j + 25 + 36 * i, k + 64, 18, 18, u, 54 + 18 * i, s, RotaryCraft.class));
//        }
//    }
//
//    @Override
//    protected void actionPerformed(Button b) {
//        super.actionPerformed(b);
//
//        if (b.id < 24000) {
//            PacketRegistry id = null;
//            switch (b.id) {
//                case 0:
//                    id = PacketRegistry.BLOWERWHITELIST;
//                    break;
//                case 1:
//                    id = PacketRegistry.BLOWERMETA;
//                    break;
//                case 2:
//                    id = PacketRegistry.BLOWERNBT;
//                    break;
//                case 3:
//                    id = PacketRegistry.BLOWEROREDICT;
//                    break;
//            }
//            if (id != null)
//                ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, id.ordinal(), tile);
//            controls[b.id] = !controls[b.id];
//        }
//
//        this.init();
//    }
//
//    //    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
////        super.drawGuiContainerForegroundLayer(a, b);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        int dy = 18;
//        int x = 8;
//        int y = 21;
//        for (int i = 0; i < tile.matchingItems.length; i++) {
//            ItemStack is = tile.matchingItems[i];
//            if (is != null) {
//                api.drawItemStack(itemRender, font, is, x + i % 9 * 18, y + i / 9 * dy);
//            }
//        }
//
//        if (api.isMouseInBox(j + 25, j + 43, k + 64, k + 82)) {
//            api.drawTooltipAt(font, controls[0] ? "Whitelist" : "Blacklist", api.getMouseRealX() - j + 50, api.getMouseRealY() - k);
//        }
//        if (api.isMouseInBox(j + 25 + 36, j + 43 + 36, k + 64, k + 82)) {
//            api.drawTooltipAt(font, controls[1] ? "Use Metadata" : "Ignore Metadata", api.getMouseRealX() - j + 80, api.getMouseRealY() - k);
//        }
//        if (api.isMouseInBox(j + 25 + 36 * 2, j + 43 + 36 * 2, k + 64, k + 82)) {
//            api.drawTooltipAt(font, controls[2] ? "Use NBT" : "Ignore NBT", api.getMouseRealX() - j, api.getMouseRealY() - k);
//        }
//        if (api.isMouseInBox(j + 25 + 36 * 3, j + 43 + 36 * 3, k + 64, k + 82)) {
//            api.drawTooltipAt(font, controls[3] ? "Match Exact" : "Use Ore Dictionary", api.getMouseRealX() - j, api.getMouseRealY() - k);
//        }
//    }
//
//    @Override
//    protected ResourceLocation getGuiTexture() {
//        return new ResourceLocation("blowergui");
//    }
//
//}
