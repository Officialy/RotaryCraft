package reika.rotarycraft.models.animated;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;

/** V33a's opposite bend; geometry, shaft animation and failure rendering share the first model. */
public class SplitterModel2 extends SplitterModel {

    public SplitterModel2(ModelPart modelPart) {
        super(modelPart, true);
    }

    public static LayerDefinition createLayer() {
        return createLayer(true);
    }
}
