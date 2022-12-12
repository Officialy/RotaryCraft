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

public class ElecMotorModel extends Model
{
    
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/modinterface/elecmotortex.png");
    
    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape1d;
    private final ModelPart shape1e;
    private final ModelPart shape1f;
    private final ModelPart shape1g;
    private final ModelPart shape1h;
    private final ModelPart shape1u;
    private final ModelPart shape1i;
    private final ModelPart shape1k;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape1l;
    private final ModelPart shape1m;
    private final ModelPart shape1n;
    private final ModelPart shape1o;
    private final ModelPart shape1p;
    private final ModelPart shape1q;
    private final ModelPart shape1r;
    private final ModelPart shape1s;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart c5;
    private final ModelPart c1;
    private final ModelPart c1a;
    private final ModelPart c1b;
    private final ModelPart c1c;
    private final ModelPart c2;
    private final ModelPart c3;
    private final ModelPart c4;
    private final ModelPart c2a;
    private final ModelPart c2b;
    private final ModelPart c2c;
    private final ModelPart c3a;
    private final ModelPart c3b;
    private final ModelPart c3c;
    private final ModelPart c4a;
    private final ModelPart c4b;
    private final ModelPart c4c;
    private final ModelPart c5a;
    private final ModelPart c5b;
    private final ModelPart c5c;
    private final ModelPart shape1ka;
    private final ModelPart shape1kb;
    private final ModelPart shape1kc;
    private final ModelPart shape1kd;
    private final ModelPart shape1ke;
    private final ModelPart shape1kf;
    private final ModelPart shape1kg;
    private final ModelPart shape1kh;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape5b;
    private final ModelPart shape5c;
    private final ModelPart shape5d;
    private final ModelPart shape5e;
    private final ModelPart shape5f;
    private final ModelPart shape5g;
    private final ModelPart root;
    
    public ElecMotorModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;
        
        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape1d = modelPart.getChild("shape1d");
        this.shape1e = modelPart.getChild("shape1e");
        this.shape1f = modelPart.getChild("shape1f");
        this.shape1g = modelPart.getChild("shape1g");
        this.shape1h = modelPart.getChild("shape1h");
        this.shape1u = modelPart.getChild("shape1u");
        this.shape1i = modelPart.getChild("shape1i");
        this.shape1k = modelPart.getChild("shape1k");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape1l = modelPart.getChild("shape1l");
        this.shape1m = modelPart.getChild("shape1m");
        this.shape1n = modelPart.getChild("shape1n");
        this.shape1o = modelPart.getChild("shape1o");
        this.shape1p = modelPart.getChild("shape1p");
        this.shape1q = modelPart.getChild("shape1q");
        this.shape1r = modelPart.getChild("shape1r");
        this.shape1s = modelPart.getChild("shape1s");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.c5 = modelPart.getChild("c5");
        this.c1 = modelPart.getChild("c1");
        this.c1a = modelPart.getChild("c1a");
        this.c1b = modelPart.getChild("c1b");
        this.c1c = modelPart.getChild("c1c");
        this.c2 = modelPart.getChild("c2");
        this.c3 = modelPart.getChild("c3");
        this.c4 = modelPart.getChild("c4");
        this.c2a = modelPart.getChild("c2a");
        this.c2b = modelPart.getChild("c2b");
        this.c2c = modelPart.getChild("c2c");
        this.c3a = modelPart.getChild("c3a");
        this.c3b = modelPart.getChild("c3b");
        this.c3c = modelPart.getChild("c3c");
        this.c4a = modelPart.getChild("c4a");
        this.c4b = modelPart.getChild("c4b");
        this.c4c = modelPart.getChild("c4c");
        this.c5a = modelPart.getChild("c5a");
        this.c5b = modelPart.getChild("c5b");
        this.c5c = modelPart.getChild("c5c");
        this.shape1ka = modelPart.getChild("shape1ka");
        this.shape1kb = modelPart.getChild("shape1kb");
        this.shape1kc = modelPart.getChild("shape1kc");
        this.shape1kd = modelPart.getChild("shape1kd");
        this.shape1ke = modelPart.getChild("shape1ke");
        this.shape1kf = modelPart.getChild("shape1kf");
        this.shape1kg = modelPart.getChild("shape1kg");
        this.shape1kh = modelPart.getChild("shape1kh");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape5d = modelPart.getChild("shape5d");
        this.shape5e = modelPart.getChild("shape5e");
        this.shape5f = modelPart.getChild("shape5f");
        this.shape5g = modelPart.getChild("shape5g");
    }

    // Grab the parts in the constructor if you need them
    
    public static LayerDefinition createLayer() {
        MeshDefinition definition = new MeshDefinition();
        PartDefinition root = definition.getRoot();
        
        root.addOrReplaceChild("shape1",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 20)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, 4, 0, 0, 0.7853982F));
        
        root.addOrReplaceChild("shape1a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 20)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, 4, 0, 0, 1.178097F));
        
        root.addOrReplaceChild("shape1b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 35)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, -7, 0, 0, 0.7853982F));
        
        root.addOrReplaceChild("shape1c",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(65, 8)
                .addBox(-3, -3, 0, 6, 6, 1),
            PartPose.offsetAndRotation(0, 15, -7.9F, 0, 0, 0.7853982F));
        
        root.addOrReplaceChild("shape1d",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 70)
                .addBox(-4, -4, 0, 8, 8, 8),
            PartPose.offsetAndRotation(0, 15, -4, 0, 0, 0.7853982F));
        
        root.addOrReplaceChild("shape1e",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(65, 8)
                .addBox(-3, -3, 0, 6, 6, 1),
            PartPose.offsetAndRotation(0, 15, -7.7F, 0, 0, 1.178097F));
        
        root.addOrReplaceChild("shape1f",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 35)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, -7, 0, 0, 1.178097F));
        
        root.addOrReplaceChild("shape1g",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 70)
                .addBox(-4, -4, 0, 8, 8, 8),
            PartPose.offsetAndRotation(0, 15, -4, 0, 0, 1.178097F));
        
        root.addOrReplaceChild("shape1h",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 35)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, -7, 0, 0, 0));
        
        root.addOrReplaceChild("shape1u",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 35)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, -7, 0, 0, 0.3926991F));
        
        root.addOrReplaceChild("shape1i",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 20)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, 4, 0, 0, 0));
        
        root.addOrReplaceChild("shape1k",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 35)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, -7, 0, 0, 0.1963495F));
        
        root.addOrReplaceChild("shape2",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 12)
                .addBox(-1, -1, 0, 2, 2, 2),
            PartPose.offsetAndRotation(0, 15, 6.5F, 0, 0, 0.7853982F));
        
        root.addOrReplaceChild("shape2a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 12)
                .addBox(-1, -1, 0, 2, 2, 2),
            PartPose.offsetAndRotation(0, 15, 6.5F, 0, 0, 0));
        
        root.addOrReplaceChild("shape1l",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(65, 0)
                .addBox(-3, -3, 0, 6, 6, 1),
            PartPose.offsetAndRotation(0, 15, 6.7F, 0, 0, 0.3926991F));
        
        root.addOrReplaceChild("shape1m",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(65, 8)
                .addBox(-3, -3, 0, 6, 6, 1),
            PartPose.offsetAndRotation(0, 15, -7.8F, 0, 0, 0.3926991F));
        
        root.addOrReplaceChild("shape1n",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 70)
                .addBox(-4, -4, 0, 8, 8, 8),
            PartPose.offsetAndRotation(0, 15, -4, 0, 0, 0));
        
        root.addOrReplaceChild("shape1o",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 70)
                .addBox(-4, -4, 0, 8, 8, 8),
            PartPose.offsetAndRotation(0, 15, -4, 0, 0, 0.3926991F));
        
        root.addOrReplaceChild("shape1p",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(65, 8)
                .addBox(-3, -3, 0, 6, 6, 1),
            PartPose.offsetAndRotation(0, 15, -8, 0, 0, 0));
        
        root.addOrReplaceChild("shape1q",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(65, 0)
                .addBox(-3, -3, 0, 6, 6, 1),
            PartPose.offsetAndRotation(0, 15, 7, 0, 0, 0));
        
        root.addOrReplaceChild("shape1r",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(65, 0)
                .addBox(-3, -3, 0, 6, 6, 1),
            PartPose.offsetAndRotation(0, 15, 6.9F, 0, 0, 0.7853982F));
        
        root.addOrReplaceChild("shape1s",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(65, 0)
                .addBox(-3, -3, 0, 6, 6, 1),
            PartPose.offsetAndRotation(0, 15, 6.8F, 0, 0, 1.178097F));
        
        root.addOrReplaceChild("shape3",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 50)
                .addBox(0, 0, 0, 16, 2, 16),
            PartPose.offsetAndRotation(-8, 22, -8, 0, 0, 0));
        
        root.addOrReplaceChild("shape4",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(41, 6)
                .addBox(0, -2, -2, 5, 2, 2),
            PartPose.offsetAndRotation(7, 22, -4.5F, 0, 0, -2.356194F));
        
        root.addOrReplaceChild("shape4a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(22, 6)
                .addBox(0, 0, -2, 5, 2, 2),
            PartPose.offsetAndRotation(-7, 22, -4.5F, 0, 0, -0.7853982F));
        
        root.addOrReplaceChild("shape4b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(22, 0)
                .addBox(0, 0, -2, 5, 2, 2),
            PartPose.offsetAndRotation(-7, 22, 6.5F, 0, 0, -0.7853982F));
        
        root.addOrReplaceChild("shape4c",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(41, 0)
                .addBox(0, -2, -2, 5, 2, 2),
            PartPose.offsetAndRotation(7, 22, 6.5F, 0, 0, -2.356194F));
        
        root.addOrReplaceChild("c5",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, -3.5F, 0, 0, 0.7853982F));
        
        root.addOrReplaceChild("c1",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, 2.5F, 0, 0, 0));
        
        root.addOrReplaceChild("c1a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, 2.5F, 0, 0, 0.7853982F));
        
        root.addOrReplaceChild("c1b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, 2.5F, 0, 0, 1.178097F));
        
        root.addOrReplaceChild("c1c",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, 2.5F, 0, 0, 0.3926991F));
        
        root.addOrReplaceChild("c2",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, 1, 0, 0, 0.7853982F));
        
        root.addOrReplaceChild("c3",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, -0.5F, 0, 0, 1.178097F));
        
        root.addOrReplaceChild("c4",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, -2, 0, 0, 1.178097F));
        
        root.addOrReplaceChild("c2a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, 1, 0, 0, 0.3926991F));
        
        root.addOrReplaceChild("c2b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, 1, 0, 0, 0));
        
        root.addOrReplaceChild("c2c",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, 1, 0, 0, 1.178097F));
        
        root.addOrReplaceChild("c3a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, -0.5F, 0, 0, 0.3926991F));
        
        root.addOrReplaceChild("c3b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, -0.5F, 0, 0, 0));
        
        root.addOrReplaceChild("c3c",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, -0.5F, 0, 0, 0.7853982F));
        
        root.addOrReplaceChild("c4a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, -2, 0, 0, 0.3926991F));
        
        root.addOrReplaceChild("c4b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, -2, 0, 0, 0));
        
        root.addOrReplaceChild("c4c",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, -2, 0, 0, 0.7853982F));
        
        root.addOrReplaceChild("c5a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, -3.5F, 0, 0, 0.3926991F));
        
        root.addOrReplaceChild("c5b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, -3.5F, 0, 0, 0));
        
        root.addOrReplaceChild("c5c",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 0)
                .addBox(-4.5F, -4.5F, 0, 9, 9, 1),
            PartPose.offsetAndRotation(0, 15, -3.5F, 0, 0, 1.178097F));
        
        root.addOrReplaceChild("shape1ka",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 20)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, 4, 0, 0, 0.3926991F));
        
        root.addOrReplaceChild("shape1kb",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 35)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, -7, 0, 0, 0.5890486F));
        
        root.addOrReplaceChild("shape1kc",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 35)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, -7, 0, 0, 1.374447F));
        
        root.addOrReplaceChild("shape1kd",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 35)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, -7, 0, 0, 0.9817477F));
        
        root.addOrReplaceChild("shape1ke",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 20)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, 4, 0, 0, 0.1963495F));
        
        root.addOrReplaceChild("shape1kf",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 20)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, 4, 0, 0, 1.374447F));
        
        root.addOrReplaceChild("shape1kg",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 20)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, 4, 0, 0, 0.9817477F));
        
        root.addOrReplaceChild("shape1kh",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(0, 20)
                .addBox(-5, -5, 0, 10, 10, 3),
            PartPose.offsetAndRotation(0, 15, 4, 0, 0, 0.5890486F));
        
        root.addOrReplaceChild("shape5",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(58, 70)
                .addBox(-0.5F, -8, 0, 1, 3, 12),
            PartPose.offsetAndRotation(0, 15, -6, 0, 0, -0.2617994F));
        
        root.addOrReplaceChild("shape5a",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(58, 70)
                .addBox(-0.5F, -8, 0, 1, 3, 12),
            PartPose.offsetAndRotation(0, 15, -6, 0, 0, 0.2617994F));
        
        root.addOrReplaceChild("shape5b",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(58, 70)
                .addBox(-0.5F, -8, 0, 1, 3, 12),
            PartPose.offsetAndRotation(0, 15, -6, 0, 0, -0.7853982F));
        
        root.addOrReplaceChild("shape5c",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(58, 70)
                .addBox(-0.5F, -8, 0, 1, 3, 12),
            PartPose.offsetAndRotation(0, 15, -6, 0, 0, 0.7853982F));
        
        root.addOrReplaceChild("shape5d",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(58, 70)
                .addBox(-0.5F, -8, 0, 1, 3, 12),
            PartPose.offsetAndRotation(0, 15, -6, 0, 0, 1.308997F));
        
        root.addOrReplaceChild("shape5e",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(58, 70)
                .addBox(-0.5F, -8, 0, 1, 3, 12),
            PartPose.offsetAndRotation(0, 15, -6, 0, 0, -1.308997F));
        
        root.addOrReplaceChild("shape5f",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(58, 70)
                .addBox(-0.5F, -8, 0, 1, 3, 12),
            PartPose.offsetAndRotation(0, 15, -6, 0, 0, 1.832596F));
        
        root.addOrReplaceChild("shape5g",
            CubeListBuilder.create()
                .mirror(true)
                .texOffs(58, 70)
                .addBox(-0.5F, -8, 0, 1, 3, 12),
            PartPose.offsetAndRotation(0, 15, -6, 0, 0, -1.832596F));
        
        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
    
        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
