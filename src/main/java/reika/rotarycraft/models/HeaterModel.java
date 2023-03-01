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

public class HeaterModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape5b;
    private final ModelPart shape5c;
    private final ModelPart shape5d;
    private final ModelPart shape5e;
    private final ModelPart shape5f;
    private final ModelPart shape5g;
    private final ModelPart shape5h;
    private final ModelPart shape5i;
    private final ModelPart shape5j;
    private final ModelPart shape5k;
    private final ModelPart shape5l;
    private final ModelPart shape5m;
    private final ModelPart shape5n;
    private final ModelPart shape5o;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape7b;
    private final ModelPart shape7c;
    private final ModelPart shape7d;
    private final ModelPart shape7e;
    private final ModelPart shape7f;
    private final ModelPart shape7g;
    private final ModelPart shape7h;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape6b;
    private final ModelPart root;

    public HeaterModel(ModelPart modelPart) {
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
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape5d = modelPart.getChild("shape5d");
        this.shape5e = modelPart.getChild("shape5e");
        this.shape5f = modelPart.getChild("shape5f");
        this.shape5g = modelPart.getChild("shape5g");
        this.shape5h = modelPart.getChild("shape5h");
        this.shape5i = modelPart.getChild("shape5i");
        this.shape5j = modelPart.getChild("shape5j");
        this.shape5k = modelPart.getChild("shape5k");
        this.shape5l = modelPart.getChild("shape5l");
        this.shape5m = modelPart.getChild("shape5m");
        this.shape5n = modelPart.getChild("shape5n");
        this.shape5o = modelPart.getChild("shape5o");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape7b = modelPart.getChild("shape7b");
        this.shape7c = modelPart.getChild("shape7c");
        this.shape7d = modelPart.getChild("shape7d");
        this.shape7e = modelPart.getChild("shape7e");
        this.shape7f = modelPart.getChild("shape7f");
        this.shape7g = modelPart.getChild("shape7g");
        this.shape7h = modelPart.getChild("shape7h");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6b = modelPart.getChild("shape6b");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(30, 25)
                        .addBox(0, 0, 0, 16, 7, 1),
                PartPose.offsetAndRotation(-8, 16, 7, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 16, 7, 1),
                PartPose.offsetAndRotation(-8, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 1, 7, 14),
                PartPose.offsetAndRotation(-8, 16, -7, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 38)
                        .addBox(0, 0, 0, 1, 7, 14),
                PartPose.offsetAndRotation(7, 16, -7, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 14, 1, 14),
                PartPose.offsetAndRotation(-7, 22, -7, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-5.5F, 18, -2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-5.5F, 18, 3.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-2.5F, 18, -2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-5.5F, 18, 0.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(3.5F, 18, -2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(0.5F, 18, -2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-5.5F, 18, -5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(3.5F, 18, 3.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(3.5F, 18, 0.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(3.5F, 18, -5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(0.5F, 18, 3.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(0.5F, 18, 0.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(0.5F, 18, -5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-2.5F, 18, 3.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-2.5F, 18, 0.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-2.5F, 18, -5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 65)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(4, 16.2F, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 65)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(-3.5F, 16.2F, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 65)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(1, 16.2F, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 65)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(-5, 16.2F, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 65)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(-6.5F, 16.2F, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 65)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(5.5F, 16.2F, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 65)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(2.5F, 16.2F, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 65)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(-2, 16.2F, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 65)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(-0.5F, 16.2F, -7, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 80)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 16.3F, -4, 0, 0, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 80)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 16.3F, -0.5F, 0, 0, 0));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 80)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 16.3F, 3, 0, 0, 0));

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
