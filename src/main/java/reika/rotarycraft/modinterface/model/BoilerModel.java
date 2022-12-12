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

public class BoilerModel extends Model
{
    
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/modinterface/");
    
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape1d;
    private final ModelPart shape1e;
    private final ModelPart shape1f;
    private final ModelPart shape1g;
    private final ModelPart shape1h;
    private final ModelPart shape1i;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart p;
    private final ModelPart p5;
    private final ModelPart p3;
    private final ModelPart p6;
    private final ModelPart shape1;
    private final ModelPart root;
    
    public BoilerModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;
        
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape1d = modelPart.getChild("shape1d");
        this.shape1e = modelPart.getChild("shape1e");
        this.shape1f = modelPart.getChild("shape1f");
        this.shape1g = modelPart.getChild("shape1g");
        this.shape1h = modelPart.getChild("shape1h");
        this.shape1i = modelPart.getChild("shape1i");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.p = modelPart.getChild("p");
        this.p5 = modelPart.getChild("p5");
        this.p3 = modelPart.getChild("p3");
        this.p6 = modelPart.getChild("p6");
        this.shape1 = modelPart.getChild("shape1");
    }

    // Grab the parts in the constructor if you need them
    
    public static LayerDefinition createLayer() {
        MeshDefinition definition = new MeshDefinition();
        PartDefinition root = definition.getRoot();
        
        root.addOrReplaceChild("shape1b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(0, 0, 0, 16, 1, 16),
            PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));
        
        root.addOrReplaceChild("shape1c",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 54)
                .addBox(0, 0, 0, 16, 1, 16),
            PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));
        
        root.addOrReplaceChild("shape1d",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(65, 0)
                .addBox(-0.5F, 0, -6, 1, 1, 12),
            PartPose.offsetAndRotation(0, 21, 0, 0, 1.047198F, 0));
        
        root.addOrReplaceChild("shape1e",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(65, 0)
                .addBox(-0.5F, 0, -6, 1, 1, 12),
            PartPose.offsetAndRotation(0, 21, 0, 0, 0.5235988F, 0));
        
        root.addOrReplaceChild("shape1f",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(65, 0)
                .addBox(-0.5F, 0, -6, 1, 1, 12),
            PartPose.offsetAndRotation(0, 21, 0, 0, -0.5235988F, 0));
        
        root.addOrReplaceChild("shape1g",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(65, 0)
                .addBox(-0.5F, 0, -6, 1, 1, 12),
            PartPose.offsetAndRotation(0, 21, 0, 0, 1.570796F, 0));
        
        root.addOrReplaceChild("shape1h",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(65, 0)
                .addBox(-0.5F, 0, -6, 1, 1, 12),
            PartPose.offsetAndRotation(0, 21, 0, 0, -1.047198F, 0));
        
        root.addOrReplaceChild("shape1i",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(65, 0)
                .addBox(-0.5F, 0, -6, 1, 1, 12),
            PartPose.offsetAndRotation(0, 21, 0, 0, 0, 0));
        
        root.addOrReplaceChild("shape3",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 75)
                .addBox(0, 0, 0, 1, 14, 1),
            PartPose.offsetAndRotation(7, 9, 7, 0, 0, 0));
        
        root.addOrReplaceChild("shape3a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 75)
                .addBox(0, 0, 0, 1, 14, 1),
            PartPose.offsetAndRotation(-8, 9, 7, 0, 0, 0));
        
        root.addOrReplaceChild("shape3b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 75)
                .addBox(0, 0, 0, 1, 14, 1),
            PartPose.offsetAndRotation(7, 9, -8, 0, 0, 0));
        
        root.addOrReplaceChild("shape3c",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 75)
                .addBox(0, 0, 0, 1, 14, 1),
            PartPose.offsetAndRotation(-8, 9, -8, 0, 0, 0));
        
        root.addOrReplaceChild("p",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 96)
                .addBox(0, 0, 0, 14, 14, 1),
            PartPose.offsetAndRotation(-7, 9, 7, 0, 0, 0));
        
        root.addOrReplaceChild("p5",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(33, 75)
                .addBox(0, 0, 0, 1, 14, 14),
            PartPose.offsetAndRotation(7, 9, -7, 0, 0, 0));
        
        root.addOrReplaceChild("p3",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 96)
                .addBox(0, 0, 0, 14, 14, 1),
            PartPose.offsetAndRotation(-7, 9, -8, 0, 0, 0));
        
        root.addOrReplaceChild("p6",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(33, 75)
                .addBox(0, 0, 0, 1, 14, 14),
            PartPose.offsetAndRotation(-8, 9, -7, 0, 0, 0));
        
        root.addOrReplaceChild("shape1",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 20)
                .addBox(0, 0, 0, 12, 1, 12),
            PartPose.offsetAndRotation(-6, 22, -6, 0, 0, 0));
        
        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
    
        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
