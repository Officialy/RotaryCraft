package reika.rotarycraft.models;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import net.minecraft.client.renderer.texture.OverlayTexture;
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

public class ItemCannonModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape2d;
    private final ModelPart shape2e;
    private final ModelPart shape2f;
    private final ModelPart shape2g;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape3d;
    private final ModelPart shape3e;
    private final ModelPart shape3f;
    private final ModelPart shape2av;
    private final ModelPart shape2gf;
    private final ModelPart shape3ddf;
    private final ModelPart shape3;
    private final ModelPart root;

    public ItemCannonModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2e = modelPart.getChild("shape2e");
        this.shape2f = modelPart.getChild("shape2f");
        this.shape2g = modelPart.getChild("shape2g");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape3e = modelPart.getChild("shape3e");
        this.shape3f = modelPart.getChild("shape3f");
        this.shape2av = modelPart.getChild("shape2av");
        this.shape2gf = modelPart.getChild("shape2gf");
        this.shape3ddf = modelPart.getChild("shape3ddf");
        this.shape3 = modelPart.getChild("shape3");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 2, 16),
                PartPose.offsetAndRotation(-8, 22, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 86)
                        .addBox(0, 0, 0, 16, 2, 3),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 14)
                        .addBox(0, 0, 0, 16, 6, 6),
                PartPose.offsetAndRotation(-8, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(21, 19)
                        .addBox(0, 0, 0, 5, 4, 5),
                PartPose.offsetAndRotation(-8, 12, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 35)
                        .addBox(0, 0, 0, 16, 2, 4),
                PartPose.offsetAndRotation(-8, 10, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(0, 0, 0, 16, 6, 6),
                PartPose.offsetAndRotation(-8, 16, 2, 0, 0, 0));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(42, 19)
                        .addBox(0, 0, 0, 5, 4, 5),
                PartPose.offsetAndRotation(3, 12, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(21, 33)
                        .addBox(0, 0, 0, 16, 2, 4),
                PartPose.offsetAndRotation(-8, 10, 4, 0, 0, 0));

        root.addOrReplaceChild("shape2g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 79)
                        .addBox(0, 0, 0, 16, 2, 3),
                PartPose.offsetAndRotation(-8, 8, 5, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 51)
                        .addBox(0, 0, 0, 3, 2, 10),
                PartPose.offsetAndRotation(5, 8, -5, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 27)
                        .addBox(0, 0, 0, 5, 2, 4),
                PartPose.offsetAndRotation(3, 16, -2, 0, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(26, 40)
                        .addBox(0, 0, 0, 4, 2, 8),
                PartPose.offsetAndRotation(4, 10, -4, 0, 0, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(104, 27)
                        .addBox(0, 0, 0, 6, 4, 4),
                PartPose.offsetAndRotation(-8, 18, -2, 0, 0, 0));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 40)
                        .addBox(0, 0, 0, 4, 2, 8),
                PartPose.offsetAndRotation(-8, 10, -4, 0, 0, 0));

        root.addOrReplaceChild("shape3f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 65)
                        .addBox(0, 0, 0, 3, 2, 10),
                PartPose.offsetAndRotation(-8, 8, -5, 0, 0, 0));

        root.addOrReplaceChild("shape2av",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 5, 4, 5),
                PartPose.offsetAndRotation(-8, 12, 3, 0, 0, 0));

        root.addOrReplaceChild("shape2gf",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 30)
                        .addBox(0, 0, 0, 5, 4, 5),
                PartPose.offsetAndRotation(3, 12, 3, 0, 0, 0));

        root.addOrReplaceChild("shape3ddf",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(104, 37)
                        .addBox(0, 0, 0, 6, 4, 4),
                PartPose.offsetAndRotation(2, 18, -2, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(85, 27)
                        .addBox(0, 0, 0, 5, 2, 4),
                PartPose.offsetAndRotation(-8, 16, -2, 0, 0, 0));

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
