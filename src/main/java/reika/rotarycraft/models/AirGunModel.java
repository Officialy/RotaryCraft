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

public class AirGunModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape1a;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape7b;
    private final ModelPart shape7c;
    private final ModelPart shape7d;
    private final ModelPart shape7e;
    private final ModelPart shape7f;
    private final ModelPart shape8;
    private final ModelPart shape9;
    private final ModelPart shape9a;
    private final ModelPart shape3a;
    private final ModelPart shape10;
    private final ModelPart shape10a;
    private final ModelPart shape10b;
    private final ModelPart shape10c;
    private final ModelPart shape10d;
    private final ModelPart shape10e;
    private final ModelPart shape10f;
    private final ModelPart shape10g;
    private final ModelPart shape7g;
    private final ModelPart shape7h;
    private final ModelPart shape7i;
    private final ModelPart shape7j;
    private final ModelPart shape7k;
    private final ModelPart shape7l;
    private final ModelPart shape7m;
    private final ModelPart shape7n;
    private final ModelPart shape7o;
    private final ModelPart shape11;
    private final ModelPart shape11a;
    private final ModelPart shape12;
    private final ModelPart shape12a;
    private final ModelPart shape13;
    private final ModelPart root;

    public AirGunModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape7b = modelPart.getChild("shape7b");
        this.shape7c = modelPart.getChild("shape7c");
        this.shape7d = modelPart.getChild("shape7d");
        this.shape7e = modelPart.getChild("shape7e");
        this.shape7f = modelPart.getChild("shape7f");
        this.shape8 = modelPart.getChild("shape8");
        this.shape9 = modelPart.getChild("shape9");
        this.shape9a = modelPart.getChild("shape9a");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape10 = modelPart.getChild("shape10");
        this.shape10a = modelPart.getChild("shape10a");
        this.shape10b = modelPart.getChild("shape10b");
        this.shape10c = modelPart.getChild("shape10c");
        this.shape10d = modelPart.getChild("shape10d");
        this.shape10e = modelPart.getChild("shape10e");
        this.shape10f = modelPart.getChild("shape10f");
        this.shape10g = modelPart.getChild("shape10g");
        this.shape7g = modelPart.getChild("shape7g");
        this.shape7h = modelPart.getChild("shape7h");
        this.shape7i = modelPart.getChild("shape7i");
        this.shape7j = modelPart.getChild("shape7j");
        this.shape7k = modelPart.getChild("shape7k");
        this.shape7l = modelPart.getChild("shape7l");
        this.shape7m = modelPart.getChild("shape7m");
        this.shape7n = modelPart.getChild("shape7n");
        this.shape7o = modelPart.getChild("shape7o");
        this.shape11 = modelPart.getChild("shape11");
        this.shape11a = modelPart.getChild("shape11a");
        this.shape12 = modelPart.getChild("shape12");
        this.shape12a = modelPart.getChild("shape12a");
        this.shape13 = modelPart.getChild("shape13");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 10)
                        .addBox(-6, 0, -1, 12, 7, 1),
                PartPose.offsetAndRotation(0, 15, -3, -2.356194F, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(55, 0)
                        .addBox(0, 0, 0, 16, 3, 13),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(12, 19)
                        .addBox(-6, -6, 0, 7, 12, 1),
                PartPose.offsetAndRotation(0, 15, -3, 0, -0.7853982F, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, 0, 12, 7, 1),
                PartPose.offsetAndRotation(0, 15, -3, -0.7853982F, 0, 0));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(29, 19)
                        .addBox(-1, -6, 0, 7, 12, 1),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(55, 20)
                        .addBox(0, 0, 0, 16, 3, 13),
                PartPose.offsetAndRotation(-8, 21, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(-1, -1, 0, 2, 2, 3),
                PartPose.offsetAndRotation(0, 15, -4.9F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 3, 10, 13),
                PartPose.offsetAndRotation(5, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 3, 10, 13),
                PartPose.offsetAndRotation(-8, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(36, 38)
                        .addBox(0, 0, 0, 10, 10, 1),
                PartPose.offsetAndRotation(-5, 11, -2, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 86)
                        .addBox(-1, -1, 0, 2, 2, 10),
                PartPose.offsetAndRotation(0, 15, -1.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 86)
                        .addBox(-1, -1, 0, 2, 2, 10),
                PartPose.offsetAndRotation(0, 15, -1.5F, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 6, 0, 0, 0.0872665F));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 6, 0, 0, 0.8726646F));

        root.addOrReplaceChild("shape7b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 6, 0, 0, 0.4799655F));

        root.addOrReplaceChild("shape7c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 6, 0, 0, 1.265364F));

        root.addOrReplaceChild("shape7d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 6, 0, 0, 1.658063F));

        root.addOrReplaceChild("shape7e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 6, 0, 0, 2.443461F));

        root.addOrReplaceChild("shape7f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 6, 0, 0, 2.050762F));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 101)
                        .addBox(0, 0, 0, 10, 2, 1),
                PartPose.offsetAndRotation(-5, 19, -8, 0, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(34, 0)
                        .addBox(0, 0, 0, 1, 8, 1),
                PartPose.offsetAndRotation(4, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape9a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(28, 0)
                        .addBox(0, 0, 0, 1, 8, 1),
                PartPose.offsetAndRotation(-5, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 26)
                        .addBox(-1, -1, 0, 2, 2, 3),
                PartPose.offsetAndRotation(0, 15, -5, 0, 0, 0));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(22, 116)
                        .addBox(0, 0, 0, 2, 1, 7),
                PartPose.offsetAndRotation(-4, 20, -2, 0, 0, 0));

        root.addOrReplaceChild("shape10a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 106)
                        .addBox(0, 0, 0, 2, 1, 7),
                PartPose.offsetAndRotation(-4, 11, -2, 0, 0, 0));

        root.addOrReplaceChild("shape10b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 116)
                        .addBox(0, 0, 0, 1, 3, 7),
                PartPose.offsetAndRotation(4, 18, -2, 0, 0, 0));

        root.addOrReplaceChild("shape10c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(22, 106)
                        .addBox(0, 0, 0, 2, 1, 7),
                PartPose.offsetAndRotation(2, 20, -2, 0, 0, 0));

        root.addOrReplaceChild("shape10d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 116)
                        .addBox(0, 0, 0, 2, 1, 7),
                PartPose.offsetAndRotation(2, 11, -2, 0, 0, 0));

        root.addOrReplaceChild("shape10e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(43, 116)
                        .addBox(0, 0, 0, 1, 3, 7),
                PartPose.offsetAndRotation(4, 11, -2, 0, 0, 0));

        root.addOrReplaceChild("shape10f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(43, 104)
                        .addBox(0, 0, 0, 1, 3, 7),
                PartPose.offsetAndRotation(-5, 11, -2, 0, 0, 0));

        root.addOrReplaceChild("shape10g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 104)
                        .addBox(0, 0, 0, 1, 3, 7),
                PartPose.offsetAndRotation(-5, 18, -2, 0, 0, 0));

        root.addOrReplaceChild("shape7g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 5, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape7h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 5, 0, 0, 1.570796F));

        root.addOrReplaceChild("shape7i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 5, 0, 0, 1.963495F));

        root.addOrReplaceChild("shape7j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 5, 0, 0, 2.356194F));

        root.addOrReplaceChild("shape7k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 5, 0, 0, 2.748893F));

        root.addOrReplaceChild("shape7l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 5, 0, 0, 0));

        root.addOrReplaceChild("shape7m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 6, 0, 0, 2.83616F));

        root.addOrReplaceChild("shape7n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 5, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape7o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, 5, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 16)
                        .addBox(0, 0, 0, 1, 16, 2),
                PartPose.offsetAndRotation(7, 8, 5, 0, 0, 0));

        root.addOrReplaceChild("shape11a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 16)
                        .addBox(0, 0, 0, 1, 16, 2),
                PartPose.offsetAndRotation(-8, 8, 5, 0, 0, 0));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(60, 38)
                        .addBox(0, 0, 0, 14, 1, 2),
                PartPose.offsetAndRotation(-7, 23, 5, 0, 0, 0));

        root.addOrReplaceChild("shape12a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(60, 38)
                        .addBox(0, 0, 0, 14, 1, 2),
                PartPose.offsetAndRotation(-7, 8, 5, 0, 0, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(34, 51)
                        .addBox(0, 0, 0, 16, 16, 1),
                PartPose.offsetAndRotation(-8, 8, 7, 0, 0, 0));

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
