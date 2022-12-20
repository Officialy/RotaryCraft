package reika.rotarycraft.base;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.client.gui.ScreenUtils;
import reika.dragonapi.base.CoreContainer;
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

    }

    protected abstract String getGuiTexture();

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
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void init() {
        super.init();
        renderables.clear();
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        ResourceLocation file = new ResourceLocation(RotaryCraft.MODID, "textures/screen/buttons.png");
        //24000, 24001
//       todo renderables.add(new ImageButton(j - 17, k + 4, 18, imageHeight - 12, 72, 0, file, 10, 10, pButton -> RotaryCraft.LOGGER.info("Button 1 pressed"), Component.translatable("Info")));// 0xffffff
//        todo renderables.add(new ImageButton(j - 17, k + imageHeight - 8, 18, 4, 72, 252, file, 10, 10, pButton -> RotaryCraft.LOGGER.info("Button 2 pressed"), Component.translatable("Info")));//button 0xffffff
    }

    //    @Override
    protected void actionPerformedd(Button b) {
//        todo if (b.id == 24000 || b.id == 24001) {
//            ep.player.closeContainer();
//            if (ReikaInventoryHelper.checkForItem(RotaryItems.HANDBOOK.get(), ep.player.getInventory()))
//                ep.openMenu(RotaryCraft.getInstance(), GuiRegistry.LOADEDHANDBOOK.ordinal(), tile.getLevel(), tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
//            else
//                ep.openMenu(RotaryCraft.getInstance(), GuiRegistry.HANDBOOKPAGE.ordinal(), tile.getLevel(), tile.getBlockPos().getX(), tile.getBlockPos().getY(), tile.getBlockPos().getZ());
//        }
    }

    protected final boolean isClickTooSoon() {
        boolean flag = System.currentTimeMillis() - lastClick < 250;
        if (!flag)
            lastClick = System.currentTimeMillis();
        return flag;
    }

    @Override
    protected void renderLabels(PoseStack stack, int pMouseX, int pMouseY) {

        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
//        if (tile instanceof BlockEntityProjector)
//            font.draw(tile.getMultiValuedName(), 6, 6, 4210752);
//        else if (tile instanceof BlockEntityScaleableChest)
//            font.draw(tile.getMultiValuedName(), 8, 6, 4210752);
//        else
        ReikaGuiAPI.instance.drawCenteredStringNoShadow(stack, minecraft.font, tile.getName(), imageWidth / 2, 5, 4210752);

        if (tile instanceof Container && this.labelInventory()) {
            int dx = this.inventoryLabelLeft() ? 8 : imageWidth - 58;
            Minecraft.getInstance().font.draw(stack, I18n.get("container.inventory"), dx, (imageHeight - 96) + 3, 4210752);
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
        //int lastx = x;
        //int lasty = y;
        //mc.thePlayer.closeScreen();
        //ModLoader.openGUI(player, new GuiReservoir(player, Reservoir));
        //Mouse.setCursorPosition(lastx, lasty);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pX, int pY) {
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, new ResourceLocation(RotaryCraft.MODID, "textures/screen/" + getGuiTexture() + ".png"));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        ScreenUtils.drawTexturedModalRect(pPoseStack, j, k, 0, 0, imageWidth, imageHeight, 0);
        if (tile instanceof BlockEntityPowerReceiver)
            this.drawPowerTab(pPoseStack, j, k);

        if (inventory == null && !(this instanceof GuiOneSlotScreen))
            RotaryCraft.LOGGER.error("Gui for " + tile.getName() + " did not set player entity!");
    }

    protected abstract void drawPowerTab(PoseStack poseStack, int j, int k);

    public void drawHelpTab(int j, int k) {
        Minecraft.getInstance().font.draw(new PoseStack(), "?", -10, imageHeight / 2 - 4, 0xffffff);
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
