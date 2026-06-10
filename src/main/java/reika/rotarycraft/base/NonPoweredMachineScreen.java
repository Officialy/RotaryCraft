package reika.rotarycraft.base;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import reika.dragonapi.base.CoreContainer;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;

public abstract class NonPoweredMachineScreen<E extends RotaryCraftBlockEntity, T extends CoreContainer<E>> extends MachineScreen<E, T> {

    public NonPoweredMachineScreen(T container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    public NonPoweredMachineScreen(T container, Inventory inv, Component name, int imageWidth, int imageHeight) {
        super(container, inv, name, imageWidth, imageHeight);
    }

    @Override
    protected void drawPowerTab(GuiGraphicsExtractor stack, int j, int k) {

    }
}
