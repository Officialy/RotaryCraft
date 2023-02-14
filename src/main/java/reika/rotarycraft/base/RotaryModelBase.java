package reika.rotarycraft.base;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.interfaces.TileModel;

import java.util.ArrayList;
import java.util.function.Function;

public abstract class RotaryModelBase extends Model implements TileModel {

    protected final float f5 = 0.0625F;

    public RotaryModelBase(Function<ResourceLocation, RenderType> p_103110_) {
        super(p_103110_);
    }

    public abstract void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta);

    public final void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions) {
        this.renderAll(stack, tex, packedLightIn, te, conditions, 0);
    }

    public final void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi) {
        this.renderAll(stack, tex, packedLightIn, te, conditions, phi, 0);
    }


    // Not always used, but if it is, it's used to render the model in the inventory it gives 0 for phi and theta. The TE IS NULL WHEN USING THIS METHOD, MAKE SURE THE TE IS NOT NEEDED
    @Override
    public void renderToBuffer(PoseStack p_103111_, VertexConsumer p_103112_, int p_103113_, int p_103114_, float p_103115_, float p_103116_, float p_103117_, float p_103118_) {
        this.renderAll(p_103111_, p_103112_, p_103113_, null, null, 0, 0);
    }
}
