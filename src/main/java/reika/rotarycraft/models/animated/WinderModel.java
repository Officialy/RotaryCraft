package reika.rotarycraft.models.animated;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import java.util.ArrayList;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class WinderModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/windertex.png");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape6b;
    private final ModelPart shape6c;
    private final ModelPart shape6d;
    private final ModelPart shape6e;
    private final ModelPart shape6f;
    private final ModelPart shape6g;
    private final ModelPart shape6h;
    private final ModelPart shape6i;
    private final ModelPart shape6j;
    private final ModelPart shape6k;
    private final ModelPart root;

    public WinderModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6b = modelPart.getChild("shape6b");
        this.shape6c = modelPart.getChild("shape6c");
        this.shape6d = modelPart.getChild("shape6d");
        this.shape6e = modelPart.getChild("shape6e");
        this.shape6f = modelPart.getChild("shape6f");
        this.shape6g = modelPart.getChild("shape6g");
        this.shape6h = modelPart.getChild("shape6h");
        this.shape6i = modelPart.getChild("shape6i");
        this.shape6j = modelPart.getChild("shape6j");
        this.shape6k = modelPart.getChild("shape6k");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(48, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 37)
                        .addBox(0, 0, 0, 12, 1, 7),
                PartPose.offsetAndRotation(-6, 11, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(48, 18)
                        .addBox(0, 0, 0, 12, 1, 12),
                PartPose.offsetAndRotation(-6, 22, -5, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(0, 0, 0, 1, 10, 7),
                PartPose.offsetAndRotation(5, 12, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(0, 0, 0, 1, 10, 7),
                PartPose.offsetAndRotation(-6, 12, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 46)
                        .addBox(0, 0, 0, 10, 10, 5),
                PartPose.offsetAndRotation(-5, 12, 2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-1, -1, 0, 2, 2, 8),
                PartPose.offsetAndRotation(0, 17, 0.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-1, -1, 0, 2, 2, 8),
                PartPose.offsetAndRotation(0, 17, 0.5F, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -2, 0, 1, 2, 1),
                PartPose.offsetAndRotation(-2.9F, 18.2F, 1, 0, 0, 2.303835F));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(1.2F, 20, 1, 0, 0, 0.8726646F));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(2.5F, 16.5F, 1, 0, 0, -0.6806784F));

        root.addOrReplaceChild("shape6c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -4, 0, 1, 4, 1),
                PartPose.offsetAndRotation(-2.2F, 14.4F, 1, 0, 0, -3.01942F));

        root.addOrReplaceChild("shape6d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, 0, 1, 1, 1),
                PartPose.offsetAndRotation(1, 17, 1, 0, 0, 0));

        root.addOrReplaceChild("shape6e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(0.7F, 14.2F, 1, 0, 0, -1.692969F));

        root.addOrReplaceChild("shape6f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -2, 0, 1, 2, 1),
                PartPose.offsetAndRotation(1, 16, 1, 0, 0, -1.22173F));

        root.addOrReplaceChild("shape6g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(-0.8F, 15.2F, 1, 0, 0, -2.86234F));

        root.addOrReplaceChild("shape6h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(-1.6F, 19.4F, 1, 0, 0, 1.745329F));

        root.addOrReplaceChild("shape6i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(-1.7F, 18, 1, 0, 0, 1.867502F));

        root.addOrReplaceChild("shape6j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(3.4F, 18.2F, 1, 0, 0, -0.0698132F));

        root.addOrReplaceChild("shape6k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(1, 19, 1, 0, 0, 0.5061455F));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        /*boolean has = (Boolean)conditions.get(0);
        shape1.render(te, f5);
        shape2.render(te, f5);
        shape2a.render(te, f5);
        shape3.render(te, f5);
        shape3a.render(te, f5);
        shape4.render(te, f5);
        stack.translate(0, 1.0625, 0);
        stack.glRotatef(phi, 0, 0, 1);
        stack.translate(0, -1.0625, 0);
        Shape5.render(te, f5);
        Shape5a.render(te, f5);
        stack.translate(0, 1.0625, 0);
        stack.mulPose(-phi, 0, 0, 1);
        stack.translate(0, -1.0625, 0);
        if (!has)
            return;
        GL11.glScaled(-1, 1, 1);
        GL11.glFrontFace(GL11.GL_CW);
        Shape6.render(te, f5);
        Shape6a.render(te, f5);
        Shape6b.render(te, f5);
        Shape6c.render(te, f5);
        Shape6d.render(te, f5);
        Shape6e.render(te, f5);
        Shape6f.render(te, f5);
        Shape6g.render(te, f5);
        Shape6h.render(te, f5);
        Shape6i.render(te, f5);
        Shape6j.render(te, f5);
        Shape6k.render(te, f5);
        GL11.glScaled(-1, 1, 1);
        GL11.glFrontFace(GL11.GL_CCW);*/
    }
    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }
}
