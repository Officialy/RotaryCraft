/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.lwjgl.opengl.GL11;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.ModList;
import reika.dragonapi.instantiable.data.maps.MultiMap;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.java.ReikaObfuscationHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.HandbookNotifications;
import reika.rotarycraft.auxiliary.RotaryDescriptions;
import reika.rotarycraft.auxiliary.interfaces.EnchantableMachine;
import reika.rotarycraft.auxiliary.interfaces.HandbookEntry;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.registry.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GuiHandbook extends Screen {


    private int mx;
    private int my;
    protected final int xSize = 256;
    protected final int ySize = 220;
    public Level level;
    private Player player;

    /**
     * One second in nanoseconds.
     */
    public static final long SECOND = 2000000000;

    public static final int PAGES_PER_SCREEN = 8;

    private static final int descX = 8;
    private static final int descY = 88;

    protected int screen = 0;
    protected int page = 0;
    protected int subpage = 0;
    protected int modifier = 0;
    private byte bcg;

    public static long time;
    private long buttontime;
    public static int i = 0;
    private int buttoni = 0;
    protected int buttontimer = 0;

    private static int staticwidth;
    private static int staticheight;

    private int guiTick;

    protected float renderq = 22.5F;
//
//    protected static final RenderBlocks rb = new RenderBlocks();
//    protected static final ItemRenderer ri = Minecraft.getInstance().getItemRenderer();
//
    public GuiHandbook(Player p5ep, Level world, int s, int p) {
        super(Component.translatable("reika.rotarycraft.handbook"));
        player = p5ep;
        level = world;
        staticwidth = xSize;
        staticheight = ySize;

        screen = s;
        page = p;

        /* if (RotaryConfig.DYNAMICHANDBOOK.get() || (DragonAPI.isReikasComputer() && ReikaObfuscationHelper.isDeObfEnvironment()))
            this.reloadXMLData();*/
    }
//
    protected void reloadXMLData() {
        RotaryDescriptions.reload();
    }

//    @Override
//    public final void initGui() {
//        super.initGui();
//        buttonList.clear();
//        guiTick = 0;
//
//        int j = (width - xSize) / 2;
//        int k = (height - ySize) / 2 - 8;
//
//        String file = HandbookRegistry.TOC.getTabImageFile();
//        if (!this.isLimitedView()) {
//            addRenderableWidget(new ImagedButton(10, j - 20, 17 + k + 163, 20, 20, 220, 0, "-", 0, false, file, RotaryCraft.class)); //Prev Page
//            addRenderableWidget(new ImagedButton(11, j - 20, 17 + k + 143, 20, 20, 220, 20, "+", 0, false, file, RotaryCraft.class));    //Next page
//            addRenderableWidget(new ImagedButton(15, j - 20, 17 + k + 183, 20, 20, 220, 20, "<<", 0, false, file, RotaryCraft.class));    //First page
//        }
//        addRenderableWidget(new Button(12, j + xSize - 27, k + 6, 20, 20, "X"));    //Close gui button
//
//        HandbookEntry h = this.getEntry();
//
//        if (h.hasSubpages() || ((h instanceof HandbookRegistry) && ((HandbookRegistry) h).getBonusSubpages() > 0)) {
//            addRenderableWidget(new Button(13, j + xSize - 27, k + 40, 20, 20, ">"));
//            addRenderableWidget(new Button(14, j + xSize - 27, k + 60, 20, 20, "<"));
//        }
//        if (!this.isLimitedView())
//            this.addTabButtons(j, k);
//        this.onInitGui(j, k, h);
//    }
//
//    protected void onInitGui(int j, int k, HandbookEntry h) {
//
//    }
//
//    protected void addTabButtons(int j, int k) {
//        HandbookRegistry.addRelevantButtons(j, k, screen, renderables);
//    }
//
//    @Override
//    public final boolean doesGuiPauseGame() {
//        return true;
//    }
//
//    public int getMaxScreen() {
//        return HandbookRegistry.RESOURCEDESC.getScreen() + HandbookRegistry.RESOURCEDESC.getNumberChildren() / PAGES_PER_SCREEN;
//    }
//
//    public int getMaxPage() {
//        return HandbookRegistry.getEntriesForScreen(screen).size() - 1;
//    }
//
//    public int getMaxSubpage() {
//        HandbookRegistry h = HandbookRegistry.getEntry(screen, page);
//        if (h == HandbookRegistry.TIERS)
//            return HandbookAuxData.getPowerDataSize() - 1;
//        if (h == HandbookRegistry.COMPUTERCRAFT)
//            return MachineRegistry.machineList.length / 36 + 1;
//        if (h == HandbookRegistry.ALERTS)
//            return HandbookNotifications.instance.getNewAlerts().size() / 3;
//        if (h == HandbookRegistry.PACKMODS)
//            return PackModificationTracker.instance.getModifications(RotaryCraft.getInstance()).size() / 3;
//        if (h == HandbookRegistry.ENCHANTING)
//            return MachineRegistry.getEnchantableMachineList().getSize();
//        return h.hasSubpages() ? 1 + h.getBonusSubpages() : h.getBonusSubpages();
//    }
//
//    @Override
//    protected void actionPerformed(Button button) {
//        if (button.id == 12) {
//            minecraft.player.closeScreen();
//            return;
//        }
//        if (buttontimer > 0)
//            return;
//        buttontimer = 20;
//        if (button.id == 15) {
//            screen = 0;
//            page = 0;
//            subpage = 0;
//            renderq = 22.5F;
//            this.initGui();
//            //this.refreshScreen();
//            return;
//        }
//        if (button.id == 10) {
//            this.prevScreen();
//            return;
//        }
//        if (button.id == 11) {
//            this.nextScreen();
//            return;
//        }
//        if (this.isOnTOC()) {
//            screen = this.getNewScreenByTOCButton(button.id + screen * PAGES_PER_SCREEN);
//            this.initGui();
//            page = 0;
//            subpage = 0;
//            renderq = 22.5F;
//            return;
//        }
//        if (button.id == 13) {
//            if (subpage < this.getMaxSubpage())
//                subpage++;
//            this.initGui();
//            return;
//        }
//        if (button.id == 14) {
//            if (subpage > 0)
//                subpage--;
//            this.initGui();
//            return;
//        }
//        time = System.nanoTime();
//        i = 0;
//        page = button.id;
//        subpage = 0;
//        renderq = 22.5F;
//        this.initGui();
//    }
//
//    private void nextScreen() {
//        if (screen < this.getMaxScreen()) {
//            screen++;
//            page = 0;
//            subpage = 0;
//        }
//        renderq = 22.5F;
//        this.initGui();
//        //this.refreshScreen();
//    }
//
//    private void prevScreen() {
//        if (screen > 0) {
//            screen--;
//            page = 0;
//            subpage = 0;
//        }
//        renderq = 22.5F;
//        this.initGui();
//        //this.refreshScreen();
//    }
//
//    private void nextPage() {
//        if (page < this.getMaxPage()) {
//            page++;
//            subpage = 0;
//        } else {
//            this.nextScreen();
//            return;
//        }
//        renderq = 22.5F;
//        this.initGui();
//        //this.refreshScreen();
//    }
//
//    private void prevPage() {
//        if (page > 0) {
//            page--;
//            subpage = 0;
//        } else {
//            this.prevScreen();
//            page = this.getMaxPage();
//            return;
//        }
//        renderq = 22.5F;
//        this.initGui();
//        //this.refreshScreen();
//    }
//
//    protected boolean isOnTOC() {
//        return this.getEntry() == HandbookRegistry.TOC;
//    }
//
//    protected int getNewScreenByTOCButton(int id) {
//        List<HandbookRegistry> li = HandbookRegistry.getCategoryTabs(true);
//        if (id >= li.size()) {
//            ReikaJavaLibrary.pConsole("Could not load screen for #" + id);
//            return 0;
//        }
//        return li.get(id).getScreen();
//    }
//
//    public void refreshScreen() {
//        int lastx = mx;
//        int lasty = my;
//        minecraft.player.closeScreen();
//        Mouse.setCursorPosition(lastx, lasty);
//    }
//
//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//        mx = Mouse.getX();
//        my = Mouse.getY();
//    }
//
//    protected PageType getGuiLayout() {
//        HandbookRegistry h = HandbookRegistry.getEntry(screen, page);
//        if (this.isOnTOC())
//            return PageType.TOC;
//        if (h.isPlainGui())
//            return PageType.PLAIN;
////  todo      if (h == HandbookRegistry.BAITBOX && subpage == 1)
////            return PageType.ANIMALBAIT;
////        if (h == HandbookRegistry.TERRA && subpage == 1)
////            return PageType.TERRAFORMER;
//        if (h == HandbookRegistry.TIERS)
//            return PageType.GREYBOX;
//        if (h == HandbookRegistry.TIMING)
//            return PageType.GREYBOX;
//        if (h == HandbookRegistry.ALERTS)
//            return PageType.BLACKBOX;
//        if (h == HandbookRegistry.PACKMODS)
//            return PageType.BLACKBOX;
//        if (subpage >= 1)
//            return PageType.PLAIN;
//        if (h == HandbookRegistry.STEELINGOT)
//            return PageType.BLASTFURNACE;
//        if (h == HandbookRegistry.EXTRACTS)
//            return PageType.EXTRACTOR;
//        if (h == HandbookRegistry.FLAKES)
//            return PageType.SMELTING;
//        if (h == HandbookRegistry.COMPACTS)
//            return PageType.COMPACTOR;
//        if (h == HandbookRegistry.GLASS)
//            return PageType.SMELTING;
//        if (h == HandbookRegistry.TUNGSTEN)
//            return PageType.SMELTING;
//        if (h == HandbookRegistry.NETHERDUST)
//            return PageType.SMELTING;
//        if (h == HandbookRegistry.YEAST)
//            return PageType.FERMENTER;
//        if (h == HandbookRegistry.ETHANOL)
//            return PageType.SMELTING;
//        if (h == HandbookRegistry.SILVERINGOT)
//            return PageType.SMELTING;
//   /*todo     if (h == HandbookRegistry.BUCKETS) {
//            if ((System.nanoTime() / SECOND) % 2 == 0)
//                return PageType.FRACTIONATOR;
//            else
//                return PageType.GRINDER;
//        }*/
//        if (h == HandbookRegistry.BEDTOOLS || h == HandbookRegistry.BEDARMOR)
//            return PageType.BLASTFURNACE;
//        //if (h == HandbookRegistry.BEDINGOT)
//        //	return PageType.BLASTFURNACE;
//        if (h == HandbookRegistry.ALLOYING)
//            return PageType.BLASTFURNACE;
//        if (h == HandbookRegistry.COKE)
//            return PageType.BLASTFURNACE;
////    todo    if (h == HandbookRegistry.STRONGSPRING)
////            return PageType.BLASTFURNACE;
//
//        if (h.isMachine() || h.isEngine() || h.isTrans() || h.getParent() == HandbookRegistry.CONVERTERDESC) {
//            return PageType.MACHINERENDER;
//        }
//
//        return PageType.CRAFTING;
//    }
//
//    protected static enum PageType {
//        PLAIN("b"),
//        CRAFTING(""),
//        SMELTING("c"),
//        EXTRACTOR("d"),
//        COMPACTOR("e"),
//        FERMENTER("f"),
//        FRACTIONATOR("g"),
//        GRINDER("h"),
//        BLASTFURNACE("j"),
//        ANIMALBAIT("k"),
//        TERRAFORMER("l"),
//        MACHINERENDER("m"),
//        GREYBOX("n"),
//        BLACKBOX("o"),
//        SOLID("p"),
//        TOC("a");
//
//        private final String endString;
//
//        PageType(String s) {
//            endString = s;
//        }
//
//        public String getFileName() {
//            return "handbookgui" + endString;
//        }
//    }
//
//    public final String getBackgroundTexture() {
//        PageType type = this.getGuiLayout();
//        String var4 = "/reika/rotarycraft/textures/gui/handbook/" + type.getFileName() + ".png";
//        return var4;
//    }
//
//    private void drawRecipes() {
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//
//        int posX = (width - xSize) / 2;
//        int posY = (height - ySize) / 2;
//        try {
//            this.drawAuxData(posX, posY);
//        } catch (Exception e) {
//            ReikaChatHelper.write(Arrays.toString(e.getStackTrace()));
//            e.printStackTrace();
//        }
//    }
//
//    protected void drawAuxData(int posX, int posY) {
//        HandbookAuxData.drawPage(font, ri, screen, page, subpage, posX, posY);
//    }
//
//    private void drawTabIcons(PoseStack stack) {
//        int posX = (width - xSize) / 2;
//        int posY = (height - ySize) / 2;
//        List<HandbookEntry> li = this.getAllTabsOnScreen();
//        for (int i = 0; i < li.size(); i++) {
//            HandbookEntry h = li.get(i);
//            ReikaGuiAPI.instance.drawItemStack(stack, ri, font, h.getTabIcon(), posX - 17, posY - 6 + i * 20);
//        }
//    }
//
//    public List<HandbookEntry> getAllTabsOnScreen() {
//        List<HandbookRegistry> li = HandbookRegistry.getEntriesForScreen(screen);
//        return new ArrayList(li);
//    }
//
//    private final void drawGraphics(PoseStack stack, float ptick) {
//        int posX = (width - xSize) / 2 - 2;
//        int posY = (height - ySize) / 2 - 8;
//
//        if (this.getEntry() == HandbookRegistry.ENCHANTING && subpage > 0) {
//            MultiMap<MachineRegistry, Enchantment> map = MachineRegistry.getEnchantableMachineList();
//            ArrayList<MachineRegistry> li = new ArrayList(map.keySet());
//            Collections.sort(li);
//            double x = posX + 167;
//            double y = posY + 44;
//            MachineRegistry m = li.get(subpage - 1);
//            BlockEntity te = m.createTEInstanceForRender(0);
//            ((EnchantableMachine) te).getEnchantmentHandler().setEnchantment(map.get(m).iterator().next(), 1);
//            this.doRenderMachine(x, y, HandbookRegistry.ENCHANTING, m);
//            ((EnchantableMachine) te).getEnchantmentHandler().clear();
//        }
//
//        if (!this.isLimitedView()) {
//            ReikaRenderHelper.disableLighting();
//            int msx = ReikaGuiAPI.instance.getMouseRealX();
//            int msy = ReikaGuiAPI.instance.getMouseRealY();
//            String s = String.format("Page %d/%d", screen, this.getMaxScreen());
//            //ReikaGuiAPI.instance.drawCenteredStringNoShadow(font, s, posX+xSize+23, posY+5, 0xffffff);
//            ReikaGuiAPI.instance.drawTooltipAt(font, s, posX + 24 + xSize + font.width(s), posY + 20);
//            if (ReikaGuiAPI.instance.isMouseInBox(posX - 18, posX + 2, posY + 0, posY + 220)) {
//                String sg = "";
//                List<HandbookEntry> li = this.getAllTabsOnScreen();
//                int idx = (msy - posY) / 20;
//                if (idx >= PAGES_PER_SCREEN) {
//                    int diff = idx - PAGES_PER_SCREEN;
//                    switch (diff) {
//                        case 0 -> sg = "Next";
//                        case 1 -> sg = "Back";
//                        case 2 -> sg = "Return";
//                    }
//                } else if (idx < li.size()) {
//                    HandbookEntry h = li.get(idx);
//                    sg = h.getTitle();
//                }
//                if (!sg.isEmpty())
//                    ReikaGuiAPI.instance.drawTooltipAt(font, sg, msx + font.width(sg) + 30, msy);
//            }
//        }
//
//        if (HandbookNotifications.instance.newAlerts() || PackModificationTracker.instance.modificationsExist(RotaryCraft.getInstance())) {
//            ReikaTextureHelper.bindFinalTexture(DragonAPI.class, "resources/warning.png");
//            RenderSystem.enableBlend();
//            Tesselator tess = Tesselator.getInstance();
//			BufferBuilder v5 = tess.getBuilder();
//            int x = posX + 257;
//            int y = posY + 18;
//            int alpha = (int) (155 + 100 * Math.sin(Math.toRadians(System.currentTimeMillis() / 8 % 360)));
//            v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//            v5.setColorRGBA_I(0xffffff, alpha);
//            v5.addVertexWithUV(x, y + 24, 0, 0, 1);
//            v5.addVertexWithUV(x + 24, y + 24, 0, 1, 1);
//            v5.addVertexWithUV(x + 24, y, 0, 1, 0);
//            v5.addVertexWithUV(x, y, 0, 0, 0);
//            v5.draw();
//            RenderSystem.disableBlend();
//
//            int i = Mouse.getX() * width / mc.displayWidth;
//            int j = height - Mouse.getY() * height / mc.displayHeight - 1;
//            int dx = i - posX;
//            int dy = j - posY;
//            if (ReikaMathLibrary.isValueInsideBoundsIncl(261, 377, dx) && ReikaMathLibrary.isValueInsideBoundsIncl(22, 36, dy)) {
//                if (HandbookNotifications.instance.newAlerts())
//                    ReikaGuiAPI.instance.drawTooltip(font, "Some config settings have been changed.");
//                if (PackModificationTracker.instance.modificationsExist(RotaryCraft.getInstance()))
//                    ReikaGuiAPI.instance.drawTooltip(font, "The modpack has made some changes to the mod.", 0, 10);
//            }
//        }
//
//        this.drawAuxGraphics(stack, posX, posY, ptick);
//    }
//
//    @Override
//    protected final void mouseClicked(int x, int y, int a) {
//        super.mouseClicked(x, y, a);
//        int j = (width - xSize) / 2 - 2;
//        int k = (height - ySize) / 2 - 8;
//        if (a == 0) {
//            int dx = x - j;
//            int dy = y - k;
//            if (HandbookNotifications.instance.newAlerts() || PackModificationTracker.instance.modificationsExist(RotaryCraft.getInstance())) {
//                if (ReikaMathLibrary.isValueInsideBoundsIncl(261, 377, dx) && ReikaMathLibrary.isValueInsideBoundsIncl(22, 36, dy)) {
//                    minecraft.getSoundManager().play(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
//                    int screen = -1;
//                    int page = -1;
//
//                    if (HandbookNotifications.instance.newAlerts()) {
//                        screen = HandbookRegistry.ALERTS.getScreen();
//                        page = HandbookRegistry.ALERTS.getPage();
//                    } else if (PackModificationTracker.instance.modificationsExist(RotaryCraft.getInstance())) {
//                        screen = HandbookRegistry.PACKMODS.getScreen();
//                        page = HandbookRegistry.PACKMODS.getPage();
//                    }
//
//                    if (screen >= 0 && page >= 0) {
//                        this.screen = screen;
//                        this.page = page;
//                        this.initGui();
//                        HandbookNotifications.instance.clearAlert();
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public final void keyTyped(char c, int key) {
//        super.keyTyped(c, key);
//
//        if (ModList.NEI.isLoaded() && key == NEIClientConfig.getKeyBinding("gui.recipe")) {
//            int x = ReikaGuiAPI.instance.getMouseRealX();
//            int y = ReikaGuiAPI.instance.getMouseRealY();
//            int j = (width - xSize) / 2 - 2;
//            int k = (height - ySize) / 2 - 8;
//            if (x >= j && y >= k && x < j + xSize && y < k + ySize) {
//                ItemStack is = ReikaGuiAPI.instance.getItemRenderAt(x, y);
//                if (is != null) {
//                    GuiCraftingRecipe.openRecipeGui("item", is);
//                }
//            }
//        }
//
//        if (key == Keyboard.KEY_LEFT) {
//            this.prevPage();
//        } else if (key == Keyboard.KEY_RIGHT) {
//            this.nextPage();
//        } else if (key == Keyboard.KEY_PRIOR) {
//            this.prevScreen();
//        } else if (key == Keyboard.KEY_NEXT) {
//            this.nextScreen();
//        }
//    }
//
//    protected void drawAuxGraphics(PoseStack stack, int posX, int posY, float ptick) {
//        HandbookAuxData.drawGraphics((HandbookRegistry) this.getEntry(), stack, posX, posY, xSize, ySize, font, ri, subpage);
//    }
//
//    public final int getGuiTick() {
//        return guiTick;
//    }
//
//    @Override
//    public final void drawScreen(PoseStack stack, int x, int y, float f) {
//        if (System.nanoTime() - buttontime > SECOND / 20) {
//            buttoni++;
//            buttontime = System.nanoTime();
//            buttontimer = 0;
//        }
//
//        guiTick++;
//
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        this.bindTexture();
//
//        int posX = (width - xSize) / 2;
//        int posY = (height - ySize) / 2 - 8;
//
//        this.drawTexturedModalRect(posX, posY, 0, 0, xSize, ySize);
//
//        int xo = 0;
//        int yo = 0;
//        HandbookEntry h = this.getEntry();
//        boolean disable = h.isConfigDisabled();
//        String s = h.getTitle() + (disable ? " (Disabled)" : "");
//        font.draw(stack, s, posX + xo + 6, posY + yo + 6, disable ? 0xff0000 : 0x000000);
//        int c = disable ? 0x777777 : 0xffffff;
//        int px = posX + descX;
//        if (this.isOnTOC()) {
//            posY -= 44;
//        }
//        if (subpage == 0 || h.sameTextAllSubpages()) {
//            font.drawSplitString(this.parseHandbookText(h.getData()), px, posY + descY, 242, c);
//        } else {
//            font.drawSplitString(this.parseHandbookText(h.getNotes(subpage)), px, posY + descY, 242, c);
//        }
//        if (disable) {
//            font.drawSplitString("This machine has been disabled by your server admin or modpack creator.", px, posY + descY, 242, 0xffffff);
//            font.drawSplitString("Contact them for further information or to request that they remove this restriction.", px, posY + descY + 27, 242, 0xffffff);
//            font.drawSplitString("If you are the server admin or pack creator, use the configuration files to change this setting.", px, posY + descY + 54, 242, 0xffffff);
//        }
//
//        super.drawScreen(x, y, f);
//
//        if (subpage == 0 && !disable)
//            this.drawRecipes();
//
//        if (!this.isLimitedView()) {
//            this.drawTabIcons();
//        }
//
//        this.drawGraphics(stack, f);
//
//        if (subpage == 0)
//            this.drawMachineRender(posX, posY);
//
//        RenderHelper.disableStandardItemLighting();
//    }
//
//    private String parseHandbookText(String s) {
//        return s;
//    }
//
//    protected HandbookEntry getEntry() {
//        return HandbookRegistry.getEntry(screen, page);
//    }
//
//    protected void bindTexture() {
//        String var4 = this.getBackgroundTexture();
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, var4);
//    }
//
//    public boolean isLimitedView() {
//        return false;
//    }
//
//    protected void doRenderMachine(PoseStack stack, double x, double y, HandbookEntry he) {
//        HandbookRegistry h = (HandbookRegistry) he;
//        MachineRegistry m = h.getMachine();
//        if (m == null)
//            return;
//        this.doRenderMachine(stack, x, y, h, m);
//    }
//
//    protected final void doRenderMachine(PoseStack stack, double x, double y, HandbookRegistry h, MachineRegistry m) {
//        MaterialRegistry[] mats = MaterialRegistry.values();
//        BlockEntity te = m.createTEInstanceForRender(0);
//        int timeStep = (int) ((System.nanoTime() / SECOND) % mats.length);
//        int r = (int) (System.nanoTime() / 20000000) % 360;
//        float variable = 0;
//        if (h.isEngine() && h != HandbookRegistry.SOLAR) {
//            //((BlockEntityEngine)te).type = EngineType.engineList[h.getOffset()];
//            //variable = -1000F*(h.getOffset()+1);
//            EngineType type = EngineType.engineList[h.getOffset()];
//            te = type.getTEInstanceForRender();
//            ((BlockEntityEngine) te).setType(type.getCraftedProduct());
//        }
//        if (h == HandbookRegistry.SHAFT) {
//            variable = -1000F * (timeStep + 1);
//        }
//        if (h == HandbookRegistry.FLYWHEEL) {
//            int tick = (int) ((System.nanoTime() / SECOND) % Flywheels.list.length);
//            variable = 500 - 1000F * (tick + 1);
//        }
//        if (h == HandbookRegistry.GEARBOX) {
//            variable = -1000F * (timeStep + 1);
//        }
//        if (h == HandbookRegistry.WORM) {
//            variable = -1000F;
//        }
//        if (h == HandbookRegistry.CVT) {
//            variable = -2000F;
//        }
//        if (h == HandbookRegistry.COIL) {
//            int tick = (int) ((System.nanoTime() / SECOND) % 2);
//            if (tick == 1)
//                ((BlockEntityAdvancedGear) te).setBedrock(true);
//            variable = -3000F;
//        }
//        double sc = 48;
//        GL11.glPushMatrix();
//        stack.translate(0, 0, 32);
//        if (m.hasModel() && !m.isPipe()) {
//            double dx = x;
//            double dy = y + 21;
//            double dz = 0;
//            stack.translate(dx, dy, dz);
//            GL11.glScaled(sc, -sc, sc);
//            GL11.glRotatef(renderq, 1, 0, 0);
//            GL11.glRotatef(r, 0, 1, 0);
//            BlockEntityRendererDispatcher.instance.renderBlockEntityAt(te, -0.5, 0, -0.5, variable);
//        } else {
//            double dx = x;
//            double dy = y;
//            double dz = 0;
//            stack.translate(dx, dy, dz);
//            GL11.glScaled(sc, -sc, sc);
//            GL11.glRotatef(renderq, 1, 0, 0);
//            GL11.glRotatef(r, 0, 1, 0);
//            ReikaTextureHelper.bindTerrainTexture();
//            rb.renderBlockAsItem(m.getBlock(), m.getBlockMetadata(), 1);
//        }
//        GL11.glPopMatrix();
//    }
//
//    private void drawMachineRender(int posX, int posY) {
////        RenderHelper.enableGUIStandardItemLighting();
//        HandbookEntry h = this.getEntry();
//        double x = posX + 167;
//        double y = posY + 44;
//        //float q = 12.5F + fscale*(float)Math.sin(System.nanoTime()/1000000000D); //wobble
//        //ReikaJavaLibrary.pConsole(y-ReikaGuiAPI.instance.getMouseScreenY(height));
//        int range = 64;
//        boolean rotate = ReikaGuiAPI.instance.isMouseInBox((int) x - range / 2, (int) x + range / 2, (int) y - range, (int) y + range);
//        if (Mouse.isButtonDown(0) && rotate) {
//            int mvy = Mouse.getDY();
//            if (mvy < 0 && renderq < 45) {
//                renderq++;
//            }
//            if (mvy > 0 && renderq > -45) {
//                renderq--;
//            }
//        }
//        y -= 8 * Math.sin(Math.abs(Math.toRadians(renderq)));
//
//        if (h.hasMachineRender()) {
//            RenderSystem.enableBlend();
//            this.doRenderMachine(x, y, h);
//            RenderSystem.disableBlend();
//        }
//    }
}