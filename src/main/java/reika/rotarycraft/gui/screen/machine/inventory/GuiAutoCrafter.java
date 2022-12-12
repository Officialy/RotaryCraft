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
//import java.util.List;
//
//import org.lwjgl.opengl.GL11;
//
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.registry.PacketRegistry;
//
//public class GuiAutoCrafter extends GuiPowerOnlyMachine {
//    private final BlockEntityAutoCrafter crafter;
//
//    private int selectedSlot = -1;
//    private GuiTextField text;
//
//    public GuiAutoCrafter(Player p5ep, BlockEntityAutoCrafter te) {
//        super(new ContainerAutoCrafter(p5ep, te), te);
//        imageHeight = 222;
//        crafter = te;
//        ep = p5ep;
//    }
//
//    @Override
//    public void init() {
//        super.init();
//        int var5 = (width - imageWidth) / 2;
//        int var6 = (height - imageHeight) / 2;
//        //renderables.add(new Button(0, var5+imageWidth-1, var6+32, 43, 20, "Get XP"));
//        if (crafter.getMode() == CraftingMode.REQUEST) {
//            for (int i = 0; i < BlockEntityAutoCrafter.SIZE; i++) {
//                int dx = var5 + (i % 9) * BlockEntityAutoCrafter.SIZE + 7;
//                int dy = i < 9 ? var6 + 13 : var6 + 75;
//                renderables.add(new ImageButton(i, dx, dy, 18, 4, 176, 6, this.getGuiTexture(), RotaryCraft.class));
//            }
//        }
//
//        if (crafter.getMode() == CraftingMode.SUSTAIN) {
//            text = new GuiTextField(font, var5 + 8, var6 + 119, 52, 15);
//            text.setFocused(false);
//            text.setMaxStringLength(7);
//        } else {
//            text = null;
//        }
//
//        //renderables.add(new ImageButton(-1, var5+imageWidth-25, var6+4, 18, 9, 176, 25, this.getGuiTexture(), RotaryCraft.class));
//        if (crafter.getMode() == CraftingMode.SUSTAIN) {
//            for (int i = 0; i < BlockEntityAutoCrafter.SIZE; i++) {
//                int dx = var5 + 8 + 18 * (i % 9);
//                int dy = var6 + 37 + 62 * (i / 9);
//                renderables.add(new ImageButton(40 + i, dx, dy, 16, 16, 195, 41, this.getGuiTexture(), RotaryCraft.class));
//            }
//        }
//    }
//
//    @Override
//    protected void actionPerformed(Button button) {
//        super.actionPerformed(button);
//        if (button.id >= 0 && button.id < BlockEntityAutoCrafter.SIZE)
//            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CRAFTERCRAFT.ordinal(), crafter, button.id, 0);
//        else if (button.id == -1) {
//            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CRAFTERMODE.ordinal(), crafter, button.id, 0);
//        } else if (button.id >= 40 && button.id < 40 + BlockEntityAutoCrafter.SIZE) {
//            selectedSlot = button.id - 40;
//            text.setFocused(false);
//            text.setText("");
//        }
//    }
//
//    @Override
//    protected void mouseClicked(int x, int y, int b) {
//        super.mouseClicked(x, y, b);
//        if (text != null) {
//            //boolean flag = text.isFocused();
//            text.mouseClicked(x, y, b);
//            //flag |= text.isFocused();
//            //if (!flag)
//            //	selectedSlot = this.getSlotClickPosition(x, y);
//        }
//    }
//
//    @Override
//    protected void keyTyped(char c, int k) {
//        super.keyTyped(c, k);
//        if (text != null) {
//            //if (k == Keyboard.KEY_SPACE) {
//            //	selectedSlot = this.getSlotClickPosition(api.getMouseRealX(), api.getMouseRealY());
//            //}
//            if (text.isFocused()) {
//                text.textboxKeyTyped(c, k);
//                if (selectedSlot >= 0) {
//                    this.dispatch(selectedSlot, Math.max(0, ReikaJavaLibrary.safeIntParse(text.getText())));
//                }
//            }
//        }
//    }
//
//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//    }
//
//    private int getSlotClickPosition(int x, int y) {
//        int var5 = (width - imageWidth) / 2;
//        int var6 = (height - imageHeight) / 2;
//        x -= var5;
//        y -= var6;
//        for (Slot s : ((List<Slot>) inventorySlots.inventorySlots)) {
//            if (s.getSlotIndex() < BlockEntityAutoCrafter.SIZE && s.inventory == crafter) {
//                int sx = s.xDisplayPosition;
//                int sy = s.yDisplayPosition;
//                if (x >= sx && x <= sx + 18 && y >= sy && y <= sy + 18) {
//                    return s.getSlotIndex();
//                }
//            }
//        }
//        return -1;
//    }
//
//    private void dispatch(int slot, int thresh) {
//        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CRAFTERTHRESH.ordinal(), crafter, selectedSlot, thresh);
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        RenderSystem.enableBlend();
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, this.getGuiTexture());
//        int x = 7;
//        int y = 36;
//        for (int i = 0; i < BlockEntityAutoCrafter.SIZE; i++) {
//            int dx = x + (i % 9) * BlockEntityAutoCrafter.SIZE;
//            int dy = i >= 9 ? y + 62 : y;
//            if (crafter.crafting[i] > 0) {
//                float alpha = crafter.crafting[i] / 2F;
//                GL11.glColor4f(1, 1, 1, alpha);
//                ScreenUtils.drawTexturedModalRect(dx, dy, 176, 11, 18, 9);
//                //ReikaJavaLibrary.pConsole(i);
//            }
//
//            if (i == selectedSlot) {
//                this.drawRect(dx + 1, dy - 17, dx + 17, dy - 1, 0x663388ff);
//            }
//        }
//        RenderSystem.disableBlend();
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        String var4 = "/gui/container.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        //mc.renderEngine.bindTexture(GuiContainer.field_110408_a);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, this.getGuiTexture());
//        int var5 = (width - imageWidth) / 2;
//        int var6 = (height - imageHeight) / 2;
//        ScreenUtils.drawTexturedModalRect(var5, var6, 0, 0, imageWidth, imageHeight);
//        this.drawPowerTab(var5, var6);
//        if (text != null) {
//            text.drawTextBox();
//            if (selectedSlot >= 0) {
//                ItemStack out = ReikaItemHelper.getSizedItemStack(crafter.getSlotRecipeOutput(selectedSlot), 1);
//                if (out != null) {
//                    if (!text.isFocused()) {
//                        font.draw(String.valueOf(crafter.getThreshold(selectedSlot)), var5 + 12, var6 + 122, 0xffffff);
//                    }
//                    font.draw("of", var5 + 65, var6 + 122, 4210752);
//                    api.drawItemStack(itemRender, out, var5 + 80, var6 + 118);
//                    //font.draw("("+out.getDisplayName()+")", var5+90, var6+67, 4210752);
//                }
//            }
//        }
//
//        GL11.glDisable(GL11.GL_LIGHTING);
//        this.drawRect(var5 + 5, var6 + 5, var5 + 16, var6 + 16, 0xff000000 | crafter.getMode().color);
//        this.drawRect(var5 + 5, var6 + 5, var5 + 6, var6 + 15, 0xff000000 | ReikaColorAPI.mixColors(crafter.getMode().color, 0xffffff, 0.5F));
//        this.drawRect(var5 + 5, var6 + 5, var5 + 15, var6 + 6, 0xff000000 | ReikaColorAPI.mixColors(crafter.getMode().color, 0xffffff, 0.5F));
//        this.drawRect(var5 + 6, var6 + 15, var5 + 16, var6 + 16, 0xff000000 | ReikaColorAPI.mixColors(crafter.getMode().color, 0x000000, 0.5F));
//        this.drawRect(var5 + 15, var6 + 6, var5 + 16, var6 + 16, 0xff000000 | ReikaColorAPI.mixColors(crafter.getMode().color, 0x000000, 0.5F));
//        if (api.isMouseInBox(var5 + 5, var5 + 16, var6 + 5, var6 + 16)) {
//            String s = crafter.getMode().label;
//            api.drawTooltipAt(font, s, api.getMouseRealX() + font.width(s) + 28, api.getMouseRealY() + 20);
//        }
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "/Reika/RotaryCraft/Textures/GUI/craftergui" + crafter.getMode().imageSuffix + ".png";
//    }
//}
