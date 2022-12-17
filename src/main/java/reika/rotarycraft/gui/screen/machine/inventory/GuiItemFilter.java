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
//import java.util.ArrayList;
//
//import net.minecraft.client.gui.Button;
//import net.minecraft.entity.player.Player;
//import net.minecraft.nbt.CompoundTag;
//
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerItemFilter;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.blockentities.BlockEntityItemFilter;
//import reika.rotarycraft.blockentities.BlockEntityItemFilter.MatchData;
//import reika.rotarycraft.blockentities.BlockEntityItemFilter.MatchDisplay;
//import reika.rotarycraft.blockentities.BlockEntityItemFilter.MatchType;
//import reika.rotarycraft.blockentities.BlockEntityItemFilter.SettingType;
//
//public class GuiItemFilter extends GuiPowerOnlyMachine {
//    private static final int LINES = 5;
//    private final BlockEntityItemFilter filter;
//    private final MatchData data;
//    private SettingType page = SettingType.BASIC;
//    private ArrayList<MatchDisplay> display;
//    private int nbtListPos = 0;
//
//    public GuiItemFilter(Player p5ep, BlockEntityItemFilter te) {
//        super(new ContainerItemFilter(p5ep, te), te);
//        imageWidth = 256;
//        imageHeight = 217;
//        ep = p5ep;
//        filter = te;
//        data = filter.getData();
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        if (data != null) {
//            display = null;
//            switch (page) {
//                case BASIC:
//                    display = data.getMainDisplay();
//                    break;
//                case NBT:
//                    display = data.getNBTDisplay();
//                    break;
//                case ORE:
//                    display = data.getOreDisplay();
//                    break;
//                case CLASS:
//                    display = data.getClassDisplay();
//                    break;
//            }
//            if (display != null) {
//                int d = page == SettingType.NBT ? 1 : 0;
//                int max = Math.min(nbtListPos + display.size(), nbtListPos + 5 - d);
//                for (int i = nbtListPos; i < max && i < display.size() + d; i++) {
//                    int i2 = i - nbtListPos + d;
//                    MatchDisplay m = display.get(i);
//                    int u = m.getSetting() == MatchType.MATCH ? 0 : 9;
//                    int v = m.getSetting() == MatchType.MISMATCH ? 54 : 63;
//                    renderables.add(new ImageButton(i, j + 30, k + 18 + i2 * 16, 9, 9, u, v, "Textures/GUI/buttons.png", RotaryCraft.class));
//                }
//            }
//        }
//        renderables.add(new Button(-1, j + 30, k + 100, 20, 20, "<"));
//        renderables.add(new Button(-2, j + 50, k + 100, 20, 20, ">"));
//        if (page == SettingType.NBT) {
//            if (!display.isEmpty()) {
//                for (int i = 0; i < 3; i++) {
//                    int u = i == 0 ? 0 : 9;
//                    int v = i == 1 ? 54 : 63;
//                    renderables.add(new ImageButton(-5 - i, j + 30 + i * 10, k + 18 + 0, 9, 9, u, v, "Textures/GUI/buttons.png", RotaryCraft.class));
//                }
//            }
//            if (display != null && display.size() > LINES) {
//                renderables.add(new Button(-3, j + 70, k + 100, 20, 20, "+"));
//                renderables.add(new Button(-4, j + 90, k + 100, 20, 20, "-"));
//            }
//        }
//    }
//
//    @Override
//    protected void actionPerformed(Button b) {
//        super.actionPerformed(b);
//        if (this.isClickTooSoon())
//            return;
//
//        if (b.id == -1) {
//            page = page.previous();
//        } else if (b.id == -2) {
//            page = page.next();
//        } else if (b.id == -3) {
//            int d = page == SettingType.NBT ? 1 : 0;
//            if (display != null && nbtListPos < display.size() - LINES + d)
//                nbtListPos++;
//        } else if (b.id == -4) {
//            if (nbtListPos > 0)
//                nbtListPos--;
//        } else if (b.id == -5 || b.id == -6 || b.id == -7) {
//            for (MatchDisplay m : display) {
//                while (m.getSetting().ordinal() != (-b.id) - 5)
//                    m.increment();
//            }
//            this.sendData();
//        } else if (b.id >= 0 && b.id != 24000) {
//            MatchDisplay m = display.get(b.id);
//            m.increment();
//            this.sendData();
//        }
//        this.initGui();
//    }
//
//    private void sendData() {
//        //ReikaJavaLibrary.pConsole(filter.getData());
//        CompoundTag nbt = data.saveAdditional();
//        //ReikaJavaLibrary.pConsole(nbt);
//        nbt.putInt("getY", tile.xCoord);
//        nbt.putInt("getY()", tile.yCoord);
//        nbt.putInt("posZ", tile.zCoord);
//        ReikaPacketHelper.sendNBTPacket(RotaryCraft.packetChannel, PacketRegistry.FILTERSETTING.ordinal(), nbt, PacketTarget.server);
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        int dx = this.inventoryLabelLeft() ? 176 : imageWidth - 50;
//        font.draw("Blacklist", dx, (imageHeight - 96) + 3, 4210752);
//
//        int x = api.getMouseRealX();
//        int y = api.getMouseRealY();
//
//        if (data != null) {
//			/*
//			int tx = 44;
//			int ty = 16;
//			Topology<String> t = match.getTopology();
//			Map<String, Integer> map = t.getDepthMap();
//			if (!map.isEmpty()) {
//				font.draw("NBT Data:", tx, ty, 0x000000);
//				ty += font.FONT_HEIGHT+2;
//				for (String s : map.keySet()) {
//					if (!s.isEmpty()) {
//						int lvl = map.get(s);
//						s = s+": "+match.getTagString(s);
//						for (int i = 0; i < lvl; i++) {
//							s = "  "+s;
//						}
//						font.draw(s, tx, ty, 0x000000);
//						ty += font.FONT_HEIGHT+2;
//					}
//				}
//			}
//			 */
//
//            if (display != null) {
//                if (display.isEmpty()) {
//                    font.draw("[No Values]", 42, 19, 0x000000);
//                }
//
//                int d = page == SettingType.NBT ? 1 : 0;
//                int max = Math.min(nbtListPos + display.size(), nbtListPos + LINES - d);
//                for (int i = nbtListPos; i < max && i < display.size() + d; i++) {
//                    int i2 = i - nbtListPos + d;
//                    MatchDisplay m = display.get(i);
//                    int tx = 42;
//                    int ty = 19 + i2 * 16;
//                    String s = m.displayName + " (" + m.value + "): ";
//                    font.draw(s, tx, ty, 0x000000);
//                    font.draw(m.getSetting().name, tx + font.width(s), ty, m.getSetting().color);
//                }
//
//                if (page == SettingType.NBT) {
//                    if (!display.isEmpty()) {
//                        int tx = 42;
//                        int ty = 19 + 0 * 16;
//                        font.draw("Toggle All", tx + 22, ty, 0x000000);
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    protected boolean inventoryLabelLeft() {
//        return true;
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "filtergui2";
//    }
//}
