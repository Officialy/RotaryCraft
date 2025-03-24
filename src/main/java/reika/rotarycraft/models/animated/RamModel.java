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

import static reika.rotarycraft.RotaryCraft.MODID;

public class RamModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart a;
    private final ModelPart b;
    private final ModelPart c;
    private final ModelPart d;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart shape4d;
    private final ModelPart shape4e;
    private final ModelPart shape4f;
    private final ModelPart shape4g;
    private final ModelPart shape2;
    private final ModelPart root;

    public RamModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.a = modelPart.getChild("a");
        this.b = modelPart.getChild("b");
        this.c = modelPart.getChild("c");
        this.d = modelPart.getChild("d");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape4d = modelPart.getChild("shape4d");
        this.shape4e = modelPart.getChild("shape4e");
        this.shape4f = modelPart.getChild("shape4f");
        this.shape4g = modelPart.getChild("shape4g");
        this.shape2 = modelPart.getChild("shape2");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 16, 6),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 50)
                        .addBox(0, 0, 0, 14, 3, 3),
                PartPose.offsetAndRotation(-7, 20, 0, 0, 0, 0));

        root.addOrReplaceChild("b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 42)
                        .addBox(0, 0, 0, 14, 3, 3),
                PartPose.offsetAndRotation(-7, 9, 0, 0, 0, 0));

        root.addOrReplaceChild("c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 58)
                        .addBox(0, 0, 0, 3, 8, 3),
                PartPose.offsetAndRotation(4, 12, 0, 0, 0, 0));

        root.addOrReplaceChild("d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(14, 58)
                        .addBox(0, 0, 0, 3, 8, 3),
                PartPose.offsetAndRotation(-7, 12, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(95, 26)
                        .addBox(0, 0, 0, 2, 2, 10),
                PartPose.offsetAndRotation(-8, 22, -2, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(45, 26)
                        .addBox(0, 0, 0, 2, 2, 10),
                PartPose.offsetAndRotation(6, 22, -2, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(95, 0)
                        .addBox(0, 0, 0, 2, 2, 10),
                PartPose.offsetAndRotation(-8, 8, -2, 0, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(70, 26)
                        .addBox(0, 0, 0, 2, 2, 10),
                PartPose.offsetAndRotation(-1, 22, -2, 0, 0, 0));

        root.addOrReplaceChild("shape4d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(95, 13)
                        .addBox(0, 0, 0, 2, 2, 10),
                PartPose.offsetAndRotation(-8, 15, -2, 0, 0, 0));

        root.addOrReplaceChild("shape4e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(70, 0)
                        .addBox(0, 0, 0, 2, 2, 10),
                PartPose.offsetAndRotation(-1, 8, -2, 0, 0, 0));

        root.addOrReplaceChild("shape4f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(45, 0)
                        .addBox(0, 0, 0, 2, 2, 10),
                PartPose.offsetAndRotation(6, 8, -2, 0, 0, 0));

        root.addOrReplaceChild("shape4g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(45, 13)
                        .addBox(0, 0, 0, 2, 2, 10),
                PartPose.offsetAndRotation(6, 15, -2, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 23)
                        .addBox(0, 0, 0, 14, 14, 2),
                PartPose.offsetAndRotation(-7, 9, -2, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

        @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        root.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }
}
