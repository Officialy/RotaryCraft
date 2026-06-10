package reika.rotarycraft.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import reika.dragonapi.base.CoreContainer;
import reika.dragonapi.instantiable.gui.ImagedGuiButton;
import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;

public abstract class MachineScreen<E extends RotaryCraftBlockEntity, T extends CoreContainer<E>> extends AbstractContainerScreen<T> {

    protected static final ReikaGuiAPI api = ReikaGuiAPI.instance;
    protected final RotaryCraftBlockEntity tile = menu.tile;
    protected BlockEntityPowerReceiver recv;
    protected Inventory inventory;
    protected long lastClick = -1;

    public MachineScreen(T container, Inventory inv, Component title) {
        super(container, inv, title);
        if (tile instanceof BlockEntityPowerReceiver)
            recv = (BlockEntityPowerReceiver) tile;
        inventory = inv;
    }

    // 26.1: imageWidth/imageHeight are final and must be set via the super constructor (the
    // legacy xSize/ySize assignments in subclass constructors are no longer permitted).
    public MachineScreen(T container, Inventory inv, Component title, int imageWidth, int imageHeight) {
        super(container, inv, title, imageWidth, imageHeight);
        if (tile instanceof BlockEntityPowerReceiver)
            recv = (BlockEntityPowerReceiver) tile;
        inventory = inv;
    }

    protected abstract String getGuiTexture();

    protected Identifier getTextureIdentifier() {
        return Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/" + getGuiTexture() + ".png");
    }

    public final int getXSize() {
        return imageWidth;
    }

    public final int getYSize() {
        return imageHeight;
    }

    public int getGuiLeft() {
        return leftPos;
    }

    public int getGuiTop() {
        return topPos;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.extractRenderState(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    @Override
    protected void init() {
        super.init();
        clearWidgets();
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        Identifier file = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/buttons.png");
        // Legacy GuiMachine: two ImagedGuiButtons (ids 24000/24001) along the left edge that
        // open the handbook. UVs 72,0 (tall body) and 72,252 (thin footer) on buttons.png.
        addRenderableWidget(new ImagedGuiButton(24000, j - 17, k + 4, 18, imageHeight - 12, 72, 0, file, "Info", 0xffffff, false, b -> actionPerformed(b, 24000)));
        addRenderableWidget(new ImagedGuiButton(24001, j - 17, k + imageHeight - 8, 18, 4, 72, 252, file, "Info", 0xffffff, false, b -> actionPerformed(b, 24001)));
    }

    protected void actionPerformed(Button b, int id) {
        if (id == 24000 || id == 24001) {
            inventory.player.closeContainer();
            // Handbook navigation depends on the not-yet-ported GuiRegistry / menu-provider
            // network path; closing the container preserves the legacy click behaviour for now.
            // todoif (ReikaInventoryHelper.checkForItem(RotaryItems.HANDBOOK.get(), inventory))
            // todo    inventory.player.openMenu(RotaryCraft.getInstance(), GuiRegistry.LOADEDHANDBOOK.ordinal(), tile.getLevel(), tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
            // todoelse
            // todo    inventory.player.openMenu(RotaryCraft.getInstance(), GuiRegistry.HANDBOOKPAGE.ordinal(), tile.getLevel(), tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
        }
    }

    protected final boolean isClickTooSoon() {
        boolean flag = System.currentTimeMillis() - lastClick < 250;
        if (!flag)
            lastClick = System.currentTimeMillis();
        return flag;
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor stack, int pMouseX, int pMouseY) {
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaleHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int j = (scaledWidth - imageWidth) / 2;
        int k = (scaleHeight - imageHeight) / 2;
//        if (tile instanceof BlockEntityProjector)
//            font.draw(tile.getMultiValuedName(), 6, 6, 4210752);
//        else if (tile instanceof BlockEntityScaleableChest)
//            font.draw(tile.getMultiValuedName(), 8, 6, 4210752);
//        else
        ReikaGuiAPI.instance.drawCenteredStringNoShadow(stack, minecraft.font, tile.getName(), imageWidth / 2, 5, 4210752);

        if (tile instanceof Container && this.labelInventory()) {
            int dx = this.inventoryLabelLeft() ? 8 : imageWidth - 58;
            stack.text(Minecraft.getInstance().font, I18n.get("container.inventory"), dx, (imageHeight - 96) + 3, 4210752);
        }

        this.drawHelpTab(j, k);
    }

    protected boolean inventoryLabelLeft() {
        return false;
    }

    public boolean labelInventory() {
        return !this.getGuiTexture().equals("targetgui");
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public final void refreshScreen() {
        int lastx = this.getXSize();
        int lasty = this.getYSize();
        minecraft.player.closeContainer();
//        ModLoader.openGUI(minecraft.player, new ReservoirScreen(minecraft.player, Reservoir));
//        Mouse.setCursorPosition(lastx, lasty);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int pX, int pY, float pPartialTick) {
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/" + getGuiTexture() + ".png"), j, k, 0, 0, imageWidth, imageHeight, 256, 256);
        if (tile instanceof BlockEntityPowerReceiver)
            this.drawPowerTab(graphics, j, k);

        if (inventory == null && !(this instanceof GuiOneSlotScreen))
            RotaryCraft.LOGGER.error("The Gui" + tile.getName() + "'s Player Inventory is null!");

        // 1.21.5: do NOT call extractLabels here. AbstractContainerScreen#extractContents
        // already calls extractLabels INSIDE a pose stack translated by (leftPos, topPos),
        // so subclass tooltip code (e.g. EngineScreen's fluid-bar tooltip drawn at
        // mouseX-j, mouseY-k) lands at the cursor. Calling it again from extractBackground
        // — which runs BEFORE that translate — would draw a second, untranslated copy of
        // every label/tooltip at the screen's top-left.
    }

    protected abstract void drawPowerTab(GuiGraphicsExtractor stack, int j, int k);

    public void drawHelpTab(int j, int k) {
//      todo  Minecraft.getInstance().font.draw(new PoseStack(), "?", -10, imageHeight / 2 - 4, 0xffffff);
    }

//    @Override
//    protected final void func_146977_a(Slot slot) {
//        if (this.renderSlot(slot)) {
//            super.func_146977_a(slot);
//        }
//        if (Keyboard.isKeyDown(DragonOptions.CLIENT.DEBUGKEY.get()) && DragonOptions.COMMON.TABNBT.getState()) {
//            ReikaTextureHelper.bindFontTexture();
//            font.draw(String.format("%d", slot.getSlotIndex()), slot.xDisplayPosition + 1, slot.yDisplayPosition + 1, 0x888888);
//        }
//    }

    protected boolean renderSlot(Slot slot) {
        return true;
    }
}
