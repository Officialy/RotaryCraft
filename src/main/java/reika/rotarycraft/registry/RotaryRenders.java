package reika.rotarycraft.registry;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

public class RotaryRenders {

    public static void registerBlockColors() {

    }

    public static void registerRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(RotaryBlocks.CANOLA.get(), RenderType.cutout());
    }

}