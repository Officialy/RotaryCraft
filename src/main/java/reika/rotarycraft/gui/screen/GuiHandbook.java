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

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.gui.ImagedGuiButton;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.HandbookAuxData;
import reika.rotarycraft.auxiliary.HandbookNotifications;
import reika.rotarycraft.auxiliary.RotaryDescriptions;
import reika.rotarycraft.auxiliary.interfaces.HandbookEntry;
import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;
import reika.rotarycraft.registry.HandbookRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.renders.GuiMachineRenderState;

import java.util.ArrayList;
import java.util.List;

public class GuiHandbook extends Screen {

    public static final int PAGES_PER_SCREEN = 8;

    protected final int xSize = 256;
    protected final int ySize = 220;

    private static final int descX = 8;
    private static final int descY = 88;

    private static final Identifier TAB_TEXTURE = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/handbook/tabs_toc.png");
    private static final Identifier WARNING_TEXTURE = Identifier.fromNamespaceAndPath(DragonAPI.MODID, "textures/gui/warning.png");

    public Level level;
    private final Player player;

    protected int screen;
    protected int page;
    protected int subpage = 0;

    protected float renderq = 22.5F;
    private long lastButtonClick;
    private int guiTick;

    public GuiHandbook(Player p5ep, Level world, int s, int p) {
        super(Component.translatable("reika.rotarycraft.handbook"));
        player = p5ep;
        level = world;
        screen = s;
        page = p;
    }

    protected void reloadXMLData() {
        RotaryDescriptions.reload();
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        guiTick = 0;

        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2 - 8;

        if (!this.isLimitedView()) {
            addRenderableWidget(new ImagedGuiButton(11, j - 20, 17 + k + 143, 20, 20, 220, 20, "+", 0, false, TAB_TEXTURE, b -> this.nextScreen()));
            addRenderableWidget(new ImagedGuiButton(10, j - 20, 17 + k + 163, 20, 20, 220, 0, "-", 0, false, TAB_TEXTURE, b -> this.prevScreen()));
            addRenderableWidget(new ImagedGuiButton(15, j - 20, 17 + k + 183, 20, 20, 220, 20, "<<", 0, false, TAB_TEXTURE, b -> this.goToTOC()));
        }
        addRenderableWidget(Button.builder(Component.literal("X"), b -> this.onClose()).bounds(j + xSize - 27, k + 6, 20, 20).build());

        HandbookEntry h = this.getEntry();

        if (h.hasSubpages() || ((h instanceof HandbookRegistry) && ((HandbookRegistry) h).getBonusSubpages() > 0)) {
            addRenderableWidget(Button.builder(Component.literal(">"), b -> this.nextSubpage()).bounds(j + xSize - 27, k + 40, 20, 20).build());
            addRenderableWidget(Button.builder(Component.literal("<"), b -> this.prevSubpage()).bounds(j + xSize - 27, k + 60, 20, 20).build());
        }
        if (!this.isLimitedView())
            this.addTabButtons(j, k);
        this.onInitGui(j, k, h);
    }

    protected void onInitGui(int j, int k, HandbookEntry h) {

    }

    protected void addTabButtons(int j, int k) {
        List<HandbookRegistry> tabs = HandbookRegistry.getEntriesForScreen(screen);
        for (HandbookRegistry h : tabs) {
            addRenderableWidget(new ImagedGuiButton(h.getPage(), j - 20, k + h.getRelativeTabPosn() * 20, 20, 20,
                    h.getTabColumn(), h.getTabRow(), TAB_TEXTURE, b -> this.onTabClicked(h)));
        }
    }

    private void onTabClicked(HandbookRegistry h) {
        if (this.isClickThrottled())
            return;
        if (this.isOnTOC()) {
            screen = this.getNewScreenByTOCButton(h.getPage() + screen * PAGES_PER_SCREEN);
            page = 0;
        } else {
            page = h.getPage();
        }
        subpage = 0;
        renderq = 22.5F;
        this.rebuildWidgets();
    }

    private boolean isClickThrottled() {
        long time = System.currentTimeMillis();
        if (time - lastButtonClick < 250)
            return true;
        lastButtonClick = time;
        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    public int getMaxScreen() {
        return HandbookRegistry.RESOURCEDESC.getScreen() + HandbookRegistry.RESOURCEDESC.getNumberChildren() / PAGES_PER_SCREEN;
    }

    public int getMaxPage() {
        return HandbookRegistry.getEntriesForScreen(screen).size() - 1;
    }

    public int getMaxSubpage() {
        HandbookRegistry h = HandbookRegistry.getEntry(screen, page);
        if (h == HandbookRegistry.TIERS)
            return HandbookAuxData.getPowerDataSize() - 1;
        if (h == HandbookRegistry.COMPUTERCRAFT)
            return MachineRegistry.machineList.length / 36 + 1;
        if (h == HandbookRegistry.ALERTS)
            return HandbookNotifications.instance.getNewAlerts().size() / 3;
        return h.hasSubpages() ? 1 + h.getBonusSubpages() : h.getBonusSubpages();
    }

    private void goToTOC() {
        if (this.isClickThrottled())
            return;
        screen = 0;
        page = 0;
        subpage = 0;
        renderq = 22.5F;
        this.rebuildWidgets();
    }

    private void nextSubpage() {
        if (this.isClickThrottled())
            return;
        if (subpage < this.getMaxSubpage())
            subpage++;
        this.rebuildWidgets();
    }

    private void prevSubpage() {
        if (this.isClickThrottled())
            return;
        if (subpage > 0)
            subpage--;
        this.rebuildWidgets();
    }

    private void nextScreen() {
        if (this.isClickThrottled())
            return;
        if (screen < this.getMaxScreen()) {
            screen++;
            page = 0;
            subpage = 0;
        }
        renderq = 22.5F;
        this.rebuildWidgets();
    }

    private void prevScreen() {
        if (this.isClickThrottled())
            return;
        if (screen > 0) {
            screen--;
            page = 0;
            subpage = 0;
        }
        renderq = 22.5F;
        this.rebuildWidgets();
    }

    private void nextPage() {
        if (page < this.getMaxPage()) {
            page++;
            subpage = 0;
            renderq = 22.5F;
            this.rebuildWidgets();
        } else {
            lastButtonClick = 0;
            this.nextScreen();
        }
    }

    private void prevPage() {
        if (page > 0) {
            page--;
            subpage = 0;
            renderq = 22.5F;
            this.rebuildWidgets();
        } else {
            lastButtonClick = 0;
            this.prevScreen();
            page = Math.max(0, this.getMaxPage());
        }
    }

    protected boolean isOnTOC() {
        return this.getEntry() == HandbookRegistry.TOC;
    }

    protected int getNewScreenByTOCButton(int id) {
        List<HandbookRegistry> li = HandbookRegistry.getCategoryTabs(true);
        if (id >= li.size()) {
            ReikaJavaLibrary.pConsole("Could not load screen for #" + id);
            return 0;
        }
        return li.get(id).getScreen();
    }

    protected PageType getGuiLayout() {
        HandbookRegistry h = HandbookRegistry.getEntry(screen, page);
        if (this.isOnTOC())
            return PageType.TOC;
        if (h.isPlainGui())
            return PageType.PLAIN;
        if (h == HandbookRegistry.TIERS)
            return PageType.GREYBOX;
        if (h == HandbookRegistry.TIMING)
            return PageType.GREYBOX;
        if (h == HandbookRegistry.ALERTS)
            return PageType.BLACKBOX;
        if (h == HandbookRegistry.PACKMODS)
            return PageType.BLACKBOX;
        if (subpage >= 1)
            return PageType.PLAIN;
        if (h == HandbookRegistry.STEELINGOT)
            return PageType.BLASTFURNACE;
        if (h == HandbookRegistry.EXTRACTS)
            return PageType.EXTRACTOR;
        if (h == HandbookRegistry.FLAKES)
            return PageType.SMELTING;
        if (h == HandbookRegistry.COMPACTS)
            return PageType.COMPACTOR;
        if (h == HandbookRegistry.GLASS)
            return PageType.SMELTING;
        if (h == HandbookRegistry.TUNGSTEN)
            return PageType.SMELTING;
        if (h == HandbookRegistry.NETHERDUST)
            return PageType.SMELTING;
        if (h == HandbookRegistry.YEAST)
            return PageType.FERMENTER;
        if (h == HandbookRegistry.ETHANOL)
            return PageType.SMELTING;
        if (h == HandbookRegistry.SILVERINGOT)
            return PageType.SMELTING;
        if (h == HandbookRegistry.BEDTOOLS || h == HandbookRegistry.BEDARMOR)
            return PageType.BLASTFURNACE;
        if (h == HandbookRegistry.ALLOYING)
            return PageType.BLASTFURNACE;
        if (h == HandbookRegistry.COKE)
            return PageType.BLASTFURNACE;

        if (h.isMachine() || h.isEngine() || h.isTrans() || h.getParent() == HandbookRegistry.CONVERTERDESC) {
            return PageType.MACHINERENDER;
        }

        return PageType.CRAFTING;
    }

    protected enum PageType {
        PLAIN("b"),
        CRAFTING(""),
        SMELTING("c"),
        EXTRACTOR("d"),
        COMPACTOR("e"),
        FERMENTER("f"),
        FRACTIONATOR("g"),
        GRINDER("h"),
        BLASTFURNACE("j"),
        ANIMALBAIT("k"),
        TERRAFORMER("l"),
        MACHINERENDER("m"),
        GREYBOX("n"),
        BLACKBOX("o"),
        SOLID("p"),
        TOC("a");

        private final String endString;

        PageType(String s) {
            endString = s;
        }

        public String getFileName() {
            return "handbookgui" + endString;
        }
    }

    public final Identifier getBackgroundTexture() {
        PageType type = this.getGuiLayout();
        return Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/handbook/" + type.getFileName() + ".png");
    }

    private void drawRecipes(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int posX = (width - xSize) / 2;
        int posY = (height - ySize) / 2;
        try {
            this.drawAuxData(graphics, posX, posY, mouseX, mouseY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void drawAuxData(GuiGraphicsExtractor graphics, int posX, int posY, int mouseX, int mouseY) {
        HandbookAuxData.drawPage(new PoseStack(), font, graphics, screen, page, subpage, posX, posY, mouseX, mouseY);
    }

    private void drawTabIcons(GuiGraphicsExtractor graphics) {
        int posX = (width - xSize) / 2;
        int posY = (height - ySize) / 2;
        List<HandbookEntry> li = this.getAllTabsOnScreen();
        for (int i = 0; i < li.size(); i++) {
            HandbookEntry h = li.get(i);
            ItemStack icon = h.getTabIcon();
            if (icon != null && !icon.isEmpty())
                ReikaGuiAPI.instance.drawItemStack(graphics, font, icon, posX - 17, posY - 6 + i * 20);
        }
    }

    public List<HandbookEntry> getAllTabsOnScreen() {
        return new ArrayList<>(HandbookRegistry.getEntriesForScreen(screen));
    }

    private void drawGraphics(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float ptick) {
        int posX = (width - xSize) / 2 - 2;
        int posY = (height - ySize) / 2 - 8;

        if (!this.isLimitedView()) {
            String s = String.format("Page %d/%d", screen, this.getMaxScreen());
            ReikaGuiAPI.instance.drawTooltipAt(graphics, font, s, posX + 24 + xSize + font.width(s), posY + 20);
            if (ReikaGuiAPI.instance.isMouseInBox(posX - 18, posX + 2, posY, posY + 220, mouseX, mouseY)) {
                String sg = "";
                List<HandbookEntry> li = this.getAllTabsOnScreen();
                int idx = (mouseY - posY) / 20;
                if (idx >= PAGES_PER_SCREEN) {
                    int diff = idx - PAGES_PER_SCREEN;
                    switch (diff) {
                        case 0 -> sg = "Next";
                        case 1 -> sg = "Back";
                        case 2 -> sg = "Return";
                    }
                } else if (idx >= 0 && idx < li.size()) {
                    HandbookEntry h = li.get(idx);
                    sg = h.getTitle();
                }
                if (sg != null && !sg.isEmpty())
                    ReikaGuiAPI.instance.drawTooltipAt(graphics, font, sg, mouseX + font.width(sg) + 30, mouseY);
            }
        }

        if (HandbookNotifications.instance.newAlerts()) {
            int x = posX + 257;
            int y = posY + 18;
            graphics.blit(RenderPipelines.GUI_TEXTURED, WARNING_TEXTURE, x, y, 0, 0, 24, 24, 24, 24);

            int dx = mouseX - posX;
            int dy = mouseY - posY;
            if (ReikaMathLibrary.isValueInsideBoundsIncl(257, 281, dx) && ReikaMathLibrary.isValueInsideBoundsIncl(18, 42, dy)) {
                ReikaGuiAPI.instance.drawTooltip(graphics, font, "Some config settings have been changed.", mouseX, mouseY);
            }
        }

        this.drawAuxGraphics(graphics, posX, posY, mouseX, mouseY, ptick);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (super.mouseClicked(event, doubleClick))
            return true;
        int j = (width - xSize) / 2 - 2;
        int k = (height - ySize) / 2 - 8;
        if (event.button() == 0) {
            int dx = (int) event.x() - j;
            int dy = (int) event.y() - k;
            if (HandbookNotifications.instance.newAlerts()) {
                if (ReikaMathLibrary.isValueInsideBoundsIncl(257, 281, dx) && ReikaMathLibrary.isValueInsideBoundsIncl(18, 42, dy)) {
                    screen = HandbookRegistry.ALERTS.getScreen();
                    page = HandbookRegistry.ALERTS.getPage();
                    subpage = 0;
                    HandbookNotifications.instance.clearAlert();
                    this.rebuildWidgets();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (super.mouseDragged(event, dx, dy))
            return true;
        if (event.button() == 0 && this.getEntry().hasMachineRender() && subpage == 0) {
            int posX = (width - xSize) / 2;
            int posY = (height - ySize) / 2 - 8;
            int x = posX + 167;
            int y = posY + 44;
            int range = 64;
            if (ReikaGuiAPI.instance.isMouseInBox(x - range / 2, x + range / 2, y - range, y + range, event.x(), event.y())) {
                if (dy > 0 && renderq < 45) {
                    renderq++;
                }
                if (dy < 0 && renderq > -45) {
                    renderq--;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (super.keyPressed(event))
            return true;
        if (this.isLimitedView())
            return false;
        switch (event.key()) {
            case InputConstants.KEY_LEFT -> {
                this.prevPage();
                return true;
            }
            case InputConstants.KEY_RIGHT -> {
                this.nextPage();
                return true;
            }
            case InputConstants.KEY_PAGEUP -> {
                lastButtonClick = 0;
                this.prevScreen();
                return true;
            }
            case InputConstants.KEY_PAGEDOWN -> {
                lastButtonClick = 0;
                this.nextScreen();
                return true;
            }
        }
        return false;
    }

    protected void drawAuxGraphics(GuiGraphicsExtractor graphics, int posX, int posY, int mouseX, int mouseY, float ptick) {
        HandbookAuxData.drawGraphics(new PoseStack(), font, graphics, screen, page, subpage, posX, posY, mouseX, mouseY);
    }

    public final int getGuiTick() {
        return guiTick;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float f) {
        guiTick++;

        int posX = (width - xSize) / 2;
        int posY = (height - ySize) / 2 - 8;

        graphics.blit(RenderPipelines.GUI_TEXTURED, this.getBackgroundTexture(), posX, posY, 0, 0, xSize, ySize, 256, 256);

        HandbookEntry h = this.getEntry();
        boolean disable = h.isConfigDisabled();
        String s = h.getTitle() + (disable ? " (Disabled)" : "");
        graphics.text(font, s, posX + 6, posY + 6, 0xFF000000 | (disable ? 0xff0000 : 0x000000), false);
        int c = 0xFF000000 | (disable ? 0x777777 : 0xffffff);
        int px = posX + descX;
        int textY = posY + descY;
        if (this.isOnTOC()) {
            textY -= 44;
        }
        if (!disable) {
            String text = subpage == 0 || h.sameTextAllSubpages() ? h.getData() : h.getNotes(subpage);
            if (text != null && !text.isEmpty())
                graphics.textWithWordWrap(font, FormattedText.of(this.parseHandbookText(text)), px, textY, 242, c, false);
        } else {
            graphics.textWithWordWrap(font, FormattedText.of("This machine has been disabled by your server admin or modpack creator."), px, textY, 242, 0xFFFFFFFF, false);
            graphics.textWithWordWrap(font, FormattedText.of("Contact them for further information or to request that they remove this restriction."), px, textY + 27, 242, 0xFFFFFFFF, false);
            graphics.textWithWordWrap(font, FormattedText.of("If you are the server admin or pack creator, use the configuration files to change this setting."), px, textY + 54, 242, 0xFFFFFFFF, false);
        }

        super.extractRenderState(graphics, mouseX, mouseY, f);

        if (subpage == 0 && !disable)
            this.drawRecipes(graphics, mouseX, mouseY);

        if (!this.isLimitedView()) {
            this.drawTabIcons(graphics);
        }

        this.drawGraphics(graphics, mouseX, mouseY, f);

        if (subpage == 0)
            this.drawMachineRender(graphics, mouseX, mouseY, posX, posY);
    }

    private String parseHandbookText(String s) {
        return s;
    }

    protected HandbookEntry getEntry() {
        return HandbookRegistry.getEntry(screen, page);
    }

    public boolean isLimitedView() {
        return false;
    }

    protected void doRenderMachine(GuiGraphicsExtractor graphics, int x, int y, HandbookEntry he) {
        HandbookRegistry h = (HandbookRegistry) he;
        MachineRegistry m = h.getMachine();
        if (m == null)
            return;
        this.doRenderMachine(graphics, x, y, h, m);
    }

    protected final void doRenderMachine(GuiGraphicsExtractor graphics, int x, int y, HandbookRegistry h, MachineRegistry m) {
        if (!m.hasModel() || m.isPipe()) {
            ItemStack is = m.getBlockState().getBlock().asItem().getDefaultInstance();
            if (!is.isEmpty()) {
                ReikaGuiAPI.instance.drawItemStack(graphics, font, is, x - 8, y - 8);
            }
            return;
        }

        long SECOND = 1000000000L;
        int timeStep = (int) ((System.nanoTime() / SECOND) % reika.rotarycraft.registry.MaterialRegistry.values().length);
        float yaw = (int) (System.nanoTime() / 20000000) % 360;
        float variable = 0;

        BlockEntity te = null;
        if (m.getBlockState().getBlock() instanceof EntityBlock eb)
            te = eb.newBlockEntity(net.minecraft.core.BlockPos.ZERO, m.getBlockState());

        ArrayList<?> conditions = null;
        if (h == HandbookRegistry.SHAFT) {
            variable = -1000F * (timeStep + 1);
        }
        if (h == HandbookRegistry.FLYWHEEL) {
            int tick = (int) ((System.nanoTime() / SECOND) % reika.rotarycraft.registry.Flywheels.list.length);
            variable = 500 - 1000F * (tick + 1);
        }
        if (h == HandbookRegistry.GEARBOX) {
            variable = -1000F * (timeStep + 1);
        }
        if (h == HandbookRegistry.WORM) {
            variable = -1000F;
        }
        if (h == HandbookRegistry.CVT) {
            variable = -2000F;
        }
        if (h == HandbookRegistry.COIL) {
            int tick = (int) ((System.nanoTime() / SECOND) % 2);
            if (tick == 1 && te instanceof BlockEntityAdvancedGear gear)
                gear.setBedrock(true);
            variable = -3000F;
        }
        if (h == HandbookRegistry.HYDROENGINE) {
            conditions = ReikaJavaLibrary.makeListFrom(false, false);
        }

        int half = 40;
        graphics.submitPictureInPictureRenderState(new GuiMachineRenderState(m, te, conditions, variable, renderq, yaw,
                x - half, y - 30, x + half, y + 50, 48, null));
    }

    private void drawMachineRender(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int posX, int posY) {
        HandbookEntry h = this.getEntry();
        int x = posX + 167;
        int y = posY + 44;
        y -= (int) (8 * Math.sin(Math.abs(Math.toRadians(renderq))));

        if (h.hasMachineRender()) {
            this.doRenderMachine(graphics, x, y, h);
        }
    }
}
