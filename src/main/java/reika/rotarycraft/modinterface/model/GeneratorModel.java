package reika.rotarycraft.modinterface.model;
    
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

public class GeneratorModel extends Model
{
    
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/converter/elecmotortex.png");
    
    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape1a;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape4a;
    private final ModelPart shape5a;
    private final ModelPart shape5b;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape6b;
    private final ModelPart shape6c;
    private final ModelPart shape6d;
    private final ModelPart shape6e;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape7b;
    private final ModelPart shape8;
    private final ModelPart shape9;
    private final ModelPart shape9a;
    private final ModelPart shape9b;
    private final ModelPart shape9c;
    private final ModelPart root;
    
    public GeneratorModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;
        
        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6b = modelPart.getChild("shape6b");
        this.shape6c = modelPart.getChild("shape6c");
        this.shape6d = modelPart.getChild("shape6d");
        this.shape6e = modelPart.getChild("shape6e");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape7b = modelPart.getChild("shape7b");
        this.shape8 = modelPart.getChild("shape8");
        this.shape9 = modelPart.getChild("shape9");
        this.shape9a = modelPart.getChild("shape9a");
        this.shape9b = modelPart.getChild("shape9b");
        this.shape9c = modelPart.getChild("shape9c");
    }

    // Grab the parts in the constructor if you need them
    
    public static LayerDefinition createLayer() {
        MeshDefinition definition = new MeshDefinition();
        PartDefinition root = definition.getRoot();
        
        root.addOrReplaceChild("shape1",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 42)
                .addBox(-3, -3, 0, 6, 6, 9),
            PartPose.offsetAndRotation(0, 17, -7.5F, 0, 0, 1.178097F));
        
        root.addOrReplaceChild("shape2",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 22)
                .addBox(-4, -4, 0, 8, 8, 6),
            PartPose.offsetAndRotation(0, 17, 2, 0, 0, 1.178097F));
        
        root.addOrReplaceChild("shape1a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 42)
                .addBox(-3, -3, 0, 6, 6, 9),
            PartPose.offsetAndRotation(0, 17, -7.5F, 0, 0, 0));
        
        root.addOrReplaceChild("shape2a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 22)
                .addBox(-4, -4, 0, 8, 8, 6),
            PartPose.offsetAndRotation(0, 17, 2, 0, 0, 0));
        
        root.addOrReplaceChild("shape3",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-2.5F, -2.5F, 0, 5, 5, 1),
            PartPose.offsetAndRotation(0, 17, 1, 0, 0, 0.7853982F));
        
        root.addOrReplaceChild("shape3a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-2.5F, -2.5F, 0, 5, 5, 1),
            PartPose.offsetAndRotation(0, 17, 1, 0, 0, 1.178097F));
        
        root.addOrReplaceChild("shape2b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 22)
                .addBox(-4, -4, 0, 8, 8, 6),
            PartPose.offsetAndRotation(0, 17, 2, 0, 0, 0.7853982F));
        
        root.addOrReplaceChild("shape2c",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 22)
                .addBox(-4, -4, 0, 8, 8, 6),
            PartPose.offsetAndRotation(0, 17, 2, 0, 0, 0.3926991F));
        
        root.addOrReplaceChild("shape1b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 42)
                .addBox(-3, -3, 0, 6, 6, 9),
            PartPose.offsetAndRotation(0, 17, -7.5F, 0, 0, 0.7853982F));
        
        root.addOrReplaceChild("shape1c",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 42)
                .addBox(-3, -3, 0, 6, 6, 9),
            PartPose.offsetAndRotation(0, 17, -7.5F, 0, 0, 0.3926991F));
        
        root.addOrReplaceChild("shape3b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-2.5F, -2.5F, 0, 5, 5, 1),
            PartPose.offsetAndRotation(0, 17, 1, 0, 0, 0));
        
        root.addOrReplaceChild("shape3c",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-2.5F, -2.5F, 0, 5, 5, 1),
            PartPose.offsetAndRotation(0, 17, 1, 0, 0, 0.3926991F));
        
        root.addOrReplaceChild("shape4",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 78)
                .addBox(-2, 0, 0, 4, 1, 9),
            PartPose.offsetAndRotation(0, 12, -7.5F, 0, 0, 0));
        
        root.addOrReplaceChild("shape5",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 12)
                .addBox(-4, 0, 0, 8, 1, 6),
            PartPose.offsetAndRotation(0, 8, 1.5F, 0, 0, 0));
        
        root.addOrReplaceChild("shape4a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 117)
                .addBox(-3, 0, 0, 6, 1, 9),
            PartPose.offsetAndRotation(0, 13, -7.5F, 0, 0, 0));
        
        root.addOrReplaceChild("shape5a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 58)
                .addBox(-5, 0, 0, 10, 4, 7),
            PartPose.offsetAndRotation(0, 8.5F, 0.9F, 0, 0, 0));
        
        root.addOrReplaceChild("shape5b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 70)
                .addBox(-4, 0, 0, 8, 1, 6),
            PartPose.offsetAndRotation(0, 12, 1.8F, 0, 0, 0));
        
        root.addOrReplaceChild("shape6",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 89)
                .addBox(0, 0, 0, 1, 1, 8),
            PartPose.offsetAndRotation(-1.5F, 9, -5.5F, 0, 0, 0));
        
        root.addOrReplaceChild("shape6a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(15, 99)
                .addBox(0, 0, 0, 1, 2, 1),
            PartPose.offsetAndRotation(-1.5F, 9, -6.5F, 0, 0, 0));
        
        root.addOrReplaceChild("shape6b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(15, 99)
                .addBox(0, 0, 0, 1, 2, 1),
            PartPose.offsetAndRotation(0.5F, 9, -6.5F, 0, 0, 0));
        
        root.addOrReplaceChild("shape6c",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 99)
                .addBox(0, 0, 0, 1, 1, 6),
            PartPose.offsetAndRotation(-1.5F, 11, -6.5F, 0, 0, 0));
        
        root.addOrReplaceChild("shape6d",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 89)
                .addBox(0, 0, 0, 1, 1, 8),
            PartPose.offsetAndRotation(0.5F, 9, -5.5F, 0, 0, 0));
        
        root.addOrReplaceChild("shape6e",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 99)
                .addBox(0, 0, 0, 1, 1, 6),
            PartPose.offsetAndRotation(0.5F, 11, -6.5F, 0, 0, 0));
        
        root.addOrReplaceChild("shape7",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 113)
                .addBox(0, 0, 0, 1, 2, 1),
            PartPose.offsetAndRotation(-1.5F, 13, -8, 0, 0, 0));
        
        root.addOrReplaceChild("shape7a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 107)
                .addBox(-2, -2, 0, 4, 4, 1),
            PartPose.offsetAndRotation(0, 17, -8, 0, 0, 0));
        
        root.addOrReplaceChild("shape7b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 113)
                .addBox(0, 0, 0, 1, 2, 1),
            PartPose.offsetAndRotation(0.5F, 13, -8, 0, 0, 0));
        
        root.addOrReplaceChild("shape8",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(36, 0)
                .addBox(0, 0, 0, 16, 1, 16),
            PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));
        
        root.addOrReplaceChild("shape9",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(101, 0)
                .addBox(-1, 0, 0, 2, 8, 3),
            PartPose.offsetAndRotation(0, 17, -7, 0, 0, -0.6108652F));
        
        root.addOrReplaceChild("shape9a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(101, 0)
                .addBox(-1, 0, 0, 2, 8, 3),
            PartPose.offsetAndRotation(0, 17, 4, 0, 0, -0.6108652F));
        
        root.addOrReplaceChild("shape9b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(101, 0)
                .addBox(-1, 0, 0, 2, 8, 3),
            PartPose.offsetAndRotation(0, 17, -7, 0, 0, 0.6108652F));
        
        root.addOrReplaceChild("shape9c",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(101, 0)
                .addBox(-1, 0, 0, 2, 8, 3),
            PartPose.offsetAndRotation(0, 17, 4, 0, 0, 0.6108652F));
        
        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
    
        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
