package reika.rotarycraft.base;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import reika.dragonapi.base.CoreContainer;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;

public abstract class GuiNonPoweredMachine<E extends RotaryCraftBlockEntity, T extends CoreContainer<E>> extends MachineScreen<E, T> {

    public GuiNonPoweredMachine(T par1Container, Inventory inv, Component title) {
        super(par1Container, inv, title);
    }

    @Override
    protected final void drawPowerTab(PoseStack poseStack, int j, int k) {
    }

}
