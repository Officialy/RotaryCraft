package reika.rotarycraft.renders;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jspecify.annotations.Nullable;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.ArrayList;

/**
 * Picture-in-picture state for drawing a rotating machine model inside a GUI
 * (the handbook's machine pages). Carries everything {@link GuiMachineRenderer}
 * needs to bake and pose the {@link reika.rotarycraft.base.RotaryModelBase}.
 */
public record GuiMachineRenderState(
        MachineRegistry machine,
        @Nullable BlockEntity blockEntity,
        @Nullable ArrayList<?> conditions,
        float phi,
        float pitch,
        float yaw,
        int x0,
        int y0,
        int x1,
        int y1,
        float scale,
        @Nullable ScreenRectangle scissorArea,
        @Nullable ScreenRectangle bounds
) implements PictureInPictureRenderState {

    public GuiMachineRenderState(MachineRegistry machine, @Nullable BlockEntity blockEntity, @Nullable ArrayList<?> conditions,
                                 float phi, float pitch, float yaw,
                                 int x0, int y0, int x1, int y1, float scale, @Nullable ScreenRectangle scissorArea) {
        this(machine, blockEntity, conditions, phi, pitch, yaw, x0, y0, x1, y1, scale, scissorArea,
                PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
    }
}
