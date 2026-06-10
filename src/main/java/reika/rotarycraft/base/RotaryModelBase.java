package reika.rotarycraft.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.interfaces.TileModel;

import java.util.ArrayList;
import java.util.function.Function;

// 1.21.5: Model became Model<S> with a (ModelPart root, Function<Identifier, RenderType>)
// constructor; renderToBuffer is now final. We bind to Unit (no animation state) and
// expose a phi/theta-aware renderAll for our existing call sites.
public abstract class RotaryModelBase extends Model<Unit> implements TileModel {

    protected final float f5 = 0.0625F;

    public RotaryModelBase(ModelPart root, Function<Identifier, RenderType> renderType) {
        super(root, renderType);
    }

    public abstract void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta);

    public final void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions) {
        this.renderAll(stack, tex, packedLightIn, te, conditions, 0);
    }

    public final void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi) {
        this.renderAll(stack, tex, packedLightIn, te, conditions, phi, 0);
    }

    public abstract Identifier getTexture();
}
