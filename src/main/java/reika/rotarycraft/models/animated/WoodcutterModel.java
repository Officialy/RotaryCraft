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

public class WoodcutterModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape5a;
    private final ModelPart shape5b;
    private final ModelPart shape5d;
    private final ModelPart shape5e;
    private final ModelPart shape5f;
    private final ModelPart shape5;
    private final ModelPart shape5c;
    private final ModelPart shape5g;
    private final ModelPart shape6;
    private final ModelPart shape7;
    private final ModelPart shape8;
    private final ModelPart shape8a;
    private final ModelPart shape8b;
    private final ModelPart shape8c;
    private final ModelPart shape8d;
    private final ModelPart shape8e;
    private final ModelPart shape8f;
    private final ModelPart shape8g;
    private final ModelPart shape8h;
    private final ModelPart shape8j;
    private final ModelPart shape8i;
    private final ModelPart shape8k;
    private final ModelPart shape8l;
    private final ModelPart shape8m;
    private final ModelPart shape8n;
    private final ModelPart shape8o;
    private final ModelPart shape9;
    private final ModelPart shape9a;
    private final ModelPart shape5fa;
    private final ModelPart shape5aa;
    private final ModelPart shape5ca;
    private final ModelPart shape51;
    private final ModelPart shape5ga;
    private final ModelPart shape5ba;
    private final ModelPart shape5ea;
    private final ModelPart shape5da;
    private final ModelPart shape5fab;
    private final ModelPart shape5fa1;
    private final ModelPart shape8p;
    private final ModelPart shape8q;
    private final ModelPart shape8r;
    private final ModelPart shape8s;
    private final ModelPart shape8t;
    private final ModelPart shape8u;
    private final ModelPart shape8v;
    private final ModelPart shape8w;
    private final ModelPart shape8x;
    private final ModelPart shape8y;
    private final ModelPart shape8z;
    private final ModelPart shape8aa;
    private final ModelPart shape8ab;
    private final ModelPart shape8ac;
    private final ModelPart shape8ad;
    private final ModelPart shape8ae;
    private final ModelPart shape8af;
    private final ModelPart shape8ag;
    private final ModelPart shape8ah;
    private final ModelPart shape8ai;
    private final ModelPart shape8aj;
    private final ModelPart shape8ak;
    private final ModelPart shape8al;
    private final ModelPart shape8am;
    private final ModelPart shape8an;
    private final ModelPart shape8ao;
    private final ModelPart shape8ap;
    private final ModelPart shape8aq;
    private final ModelPart shape8ar;
    private final ModelPart shape8as;
    private final ModelPart shape8at;
    private final ModelPart shape8au;
    private final ModelPart shape8av;
    private final ModelPart shape8aw;
    private final ModelPart shape8ax;
    private final ModelPart shape8ay;
    private final ModelPart shape8az;
    private final ModelPart shape8ba;
    private final ModelPart shape8bb;
    private final ModelPart shape8bc;
    private final ModelPart shape8bd;
    private final ModelPart shape8be;
    private final ModelPart shape8bf;
    private final ModelPart shape8bg;
    private final ModelPart shape8bh;
    private final ModelPart shape8bi;
    private final ModelPart shape8bj;
    private final ModelPart shape8bk;
    private final ModelPart shape9u;
    private final ModelPart shape9v;
    private final ModelPart shape9w;
    private final ModelPart shape9y;
    private final ModelPart shape9x;
    private final ModelPart shape9z;
    private final ModelPart shape10;
    private final ModelPart shape10a;
    private final ModelPart shape10b;
    private final ModelPart shape10c;
    private final ModelPart shape11;
    private final ModelPart shape11a;
    private final ModelPart root;

    public WoodcutterModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5d = modelPart.getChild("shape5d");
        this.shape5e = modelPart.getChild("shape5e");
        this.shape5f = modelPart.getChild("shape5f");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape5g = modelPart.getChild("shape5g");
        this.shape6 = modelPart.getChild("shape6");
        this.shape7 = modelPart.getChild("shape7");
        this.shape8 = modelPart.getChild("shape8");
        this.shape8a = modelPart.getChild("shape8a");
        this.shape8b = modelPart.getChild("shape8b");
        this.shape8c = modelPart.getChild("shape8c");
        this.shape8d = modelPart.getChild("shape8d");
        this.shape8e = modelPart.getChild("shape8e");
        this.shape8f = modelPart.getChild("shape8f");
        this.shape8g = modelPart.getChild("shape8g");
        this.shape8h = modelPart.getChild("shape8h");
        this.shape8j = modelPart.getChild("shape8j");
        this.shape8i = modelPart.getChild("shape8i");
        this.shape8k = modelPart.getChild("shape8k");
        this.shape8l = modelPart.getChild("shape8l");
        this.shape8m = modelPart.getChild("shape8m");
        this.shape8n = modelPart.getChild("shape8n");
        this.shape8o = modelPart.getChild("shape8o");
        this.shape9 = modelPart.getChild("shape9");
        this.shape9a = modelPart.getChild("shape9a");
        this.shape5fa = modelPart.getChild("shape5fa");
        this.shape5aa = modelPart.getChild("shape5aa");
        this.shape5ca = modelPart.getChild("shape5ca");
        this.shape51 = modelPart.getChild("shape51");
        this.shape5ga = modelPart.getChild("shape5ga");
        this.shape5ba = modelPart.getChild("shape5ba");
        this.shape5ea = modelPart.getChild("shape5ea");
        this.shape5da = modelPart.getChild("shape5da");
        this.shape5fab = modelPart.getChild("shape5fab");
        this.shape5fa1 = modelPart.getChild("shape5fa1");
        this.shape8p = modelPart.getChild("shape8p");
        this.shape8q = modelPart.getChild("shape8q");
        this.shape8r = modelPart.getChild("shape8r");
        this.shape8s = modelPart.getChild("shape8s");
        this.shape8t = modelPart.getChild("shape8t");
        this.shape8u = modelPart.getChild("shape8u");
        this.shape8v = modelPart.getChild("shape8v");
        this.shape8w = modelPart.getChild("shape8w");
        this.shape8x = modelPart.getChild("shape8x");
        this.shape8y = modelPart.getChild("shape8y");
        this.shape8z = modelPart.getChild("shape8z");
        this.shape8aa = modelPart.getChild("shape8aa");
        this.shape8ab = modelPart.getChild("shape8ab");
        this.shape8ac = modelPart.getChild("shape8ac");
        this.shape8ad = modelPart.getChild("shape8ad");
        this.shape8ae = modelPart.getChild("shape8ae");
        this.shape8af = modelPart.getChild("shape8af");
        this.shape8ag = modelPart.getChild("shape8ag");
        this.shape8ah = modelPart.getChild("shape8ah");
        this.shape8ai = modelPart.getChild("shape8ai");
        this.shape8aj = modelPart.getChild("shape8aj");
        this.shape8ak = modelPart.getChild("shape8ak");
        this.shape8al = modelPart.getChild("shape8al");
        this.shape8am = modelPart.getChild("shape8am");
        this.shape8an = modelPart.getChild("shape8an");
        this.shape8ao = modelPart.getChild("shape8ao");
        this.shape8ap = modelPart.getChild("shape8ap");
        this.shape8aq = modelPart.getChild("shape8aq");
        this.shape8ar = modelPart.getChild("shape8ar");
        this.shape8as = modelPart.getChild("shape8as");
        this.shape8at = modelPart.getChild("shape8at");
        this.shape8au = modelPart.getChild("shape8au");
        this.shape8av = modelPart.getChild("shape8av");
        this.shape8aw = modelPart.getChild("shape8aw");
        this.shape8ax = modelPart.getChild("shape8ax");
        this.shape8ay = modelPart.getChild("shape8ay");
        this.shape8az = modelPart.getChild("shape8az");
        this.shape8ba = modelPart.getChild("shape8ba");
        this.shape8bb = modelPart.getChild("shape8bb");
        this.shape8bc = modelPart.getChild("shape8bc");
        this.shape8bd = modelPart.getChild("shape8bd");
        this.shape8be = modelPart.getChild("shape8be");
        this.shape8bf = modelPart.getChild("shape8bf");
        this.shape8bg = modelPart.getChild("shape8bg");
        this.shape8bh = modelPart.getChild("shape8bh");
        this.shape8bi = modelPart.getChild("shape8bi");
        this.shape8bj = modelPart.getChild("shape8bj");
        this.shape8bk = modelPart.getChild("shape8bk");
        this.shape9u = modelPart.getChild("shape9u");
        this.shape9v = modelPart.getChild("shape9v");
        this.shape9w = modelPart.getChild("shape9w");
        this.shape9y = modelPart.getChild("shape9y");
        this.shape9x = modelPart.getChild("shape9x");
        this.shape9z = modelPart.getChild("shape9z");
        this.shape10 = modelPart.getChild("shape10");
        this.shape10a = modelPart.getChild("shape10a");
        this.shape10b = modelPart.getChild("shape10b");
        this.shape10c = modelPart.getChild("shape10c");
        this.shape11 = modelPart.getChild("shape11");
        this.shape11a = modelPart.getChild("shape11a");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(42, 0)
                        .addBox(0, 0, 0, 5, 1, 16),
                PartPose.offsetAndRotation(3, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(84, 0)
                        .addBox(0, 0, 0, 6, 1, 5),
                PartPose.offsetAndRotation(-3, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(42, 0)
                        .addBox(0, 0, 0, 5, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(84, 0)
                        .addBox(0, 0, 0, 6, 1, 5),
                PartPose.offsetAndRotation(-3, 23, 3, 0, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(84, 6)
                        .addBox(0, 0, 0, 1, 2, 16),
                PartPose.offsetAndRotation(7, 21, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(101, 38)
                        .addBox(0, 0, 0, 1, 1, 12),
                PartPose.offsetAndRotation(7, 16, -4, 0, 0, 0));

        root.addOrReplaceChild("shape5d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(57, 34)
                        .addBox(0, 0, 0, 1, 1, 9),
                PartPose.offsetAndRotation(7, 14, -1, 0, 0, 0));

        root.addOrReplaceChild("shape5e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(77, 38)
                        .addBox(0, 0, 0, 1, 1, 11),
                PartPose.offsetAndRotation(7, 15, -3, 0, 0, 0));

        root.addOrReplaceChild("shape5f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(112, 24)
                        .addBox(0, 0, 0, 1, 1, 4),
                PartPose.offsetAndRotation(7, 12, 4, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(22, 17)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(7, 18, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(52, 17)
                        .addBox(0, 0, 0, 1, 2, 15),
                PartPose.offsetAndRotation(7, 19, -7, 0, 0, 0));

        root.addOrReplaceChild("shape5g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(84, 24)
                        .addBox(0, 0, 0, 1, 1, 13),
                PartPose.offsetAndRotation(7, 17, -5, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, 0, 0, 14, 12, 1),
                PartPose.offsetAndRotation(-7, 11, 7, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(0, 0, 0, 8, 1, 1),
                PartPose.offsetAndRotation(-4, 10, 7, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(1, 16, -1, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape8a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(1, 16, -1, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape8b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(1, 16, -1, 1.963495F, 0, 0));

        root.addOrReplaceChild("shape8c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-2, 16, -1, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape8d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(1, 16, -1, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape8e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(1, 16, -1, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape8f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(1, 16, -1, 2.748893F, 0, 0));

        root.addOrReplaceChild("shape8g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(1, 16, -1, 0, 0, 0));

        root.addOrReplaceChild("shape8h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-2, 16, -1, 1.963495F, 0, 0));

        root.addOrReplaceChild("shape8j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(1, 16, -1, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape8i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(1, 16, -1, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape8k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(1, 16, -1, 2.748893F, 0, 0));

        root.addOrReplaceChild("shape8l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(1, 16, -1, 0, 0, 0));

        root.addOrReplaceChild("shape8m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(1, 16, -1, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape8n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(1, 16, -1, 1.963495F, 0, 0));

        root.addOrReplaceChild("shape8o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(4, 16, -1, 0, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 32)
                        .addBox(0, -3, -3, 1, 6, 6),
                PartPose.offsetAndRotation(-7, 16, -1, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape9a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 46)
                        .addBox(0, -1, -1, 15, 2, 2),
                PartPose.offsetAndRotation(-7.5F, 16, -1, 0.0174533F, 0, 0));

        root.addOrReplaceChild("shape5fa",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(61, 44)
                        .addBox(0, 0, 0, 1, 1, 7),
                PartPose.offsetAndRotation(7, 13, 1, 0, 0, 0));

        root.addOrReplaceChild("shape5aa",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(84, 6)
                        .addBox(0, 0, 0, 1, 2, 16),
                PartPose.offsetAndRotation(-8, 21, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5ca",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(52, 17)
                        .addBox(0, 0, 0, 1, 2, 15),
                PartPose.offsetAndRotation(-8, 19, -7, 0, 0, 0));

        root.addOrReplaceChild("shape51",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(22, 17)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(-8, 18, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5ga",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(84, 24)
                        .addBox(0, 0, 0, 1, 1, 13),
                PartPose.offsetAndRotation(-8, 17, -5, 0, 0, 0));

        root.addOrReplaceChild("shape5ba",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(101, 38)
                        .addBox(0, 0, 0, 1, 1, 12),
                PartPose.offsetAndRotation(-8, 16, -4, 0, 0, 0));

        root.addOrReplaceChild("shape5ea",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(77, 38)
                        .addBox(0, 0, 0, 1, 1, 11),
                PartPose.offsetAndRotation(-8, 15, -3, 0, 0, 0));

        root.addOrReplaceChild("shape5da",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(57, 34)
                        .addBox(0, 0, 0, 1, 1, 9),
                PartPose.offsetAndRotation(-8, 14, -1, 0, 0, 0));

        root.addOrReplaceChild("shape5fab",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(61, 44)
                        .addBox(0, 0, 0, 1, 1, 7),
                PartPose.offsetAndRotation(-8, 13, 1, 0, 0, 0));

        root.addOrReplaceChild("shape5fa1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(112, 24)
                        .addBox(0, 0, 0, 1, 1, 4),
                PartPose.offsetAndRotation(-8, 12, 4, 0, 0, 0));

        root.addOrReplaceChild("shape8p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(4, 16, -1, 0, 0, 0));

        root.addOrReplaceChild("shape8q",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-2, 16, -1, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape8r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(1, 16, -1, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape8s",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-2, 16, -1, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape8t",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-2, 16, -1, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape8u",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-2, 16, -1, 2.748893F, 0, 0));

        root.addOrReplaceChild("shape8v",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-2, 16, -1, 0, 0, 0));

        root.addOrReplaceChild("shape8w",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-5, 16, -1, 0, 0, 0));

        root.addOrReplaceChild("shape8x",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-2, 16, -1, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape8y",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-5, 16, -1, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape8z",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-5, 16, -1, 1.963495F, 0, 0));

        root.addOrReplaceChild("shape8aa",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-5, 16, -1, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape8ab",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-5, 16, -1, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape8ac",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-5, 16, -1, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape8ad",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-5, 16, -1, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape8ae",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(-5, 16, -1, 2.748893F, 0, 0));

        root.addOrReplaceChild("shape8af",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(1, 16, -1, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape8ag",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(4, 16, -1, 1.963495F, 0, 0));

        root.addOrReplaceChild("shape8ah",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(4, 16, -1, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape8ai",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(4, 16, -1, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape8aj",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(4, 16, -1, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape8ak",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(4, 16, -1, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape8al",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(4, 16, -1, 2.748893F, 0, 0));

        root.addOrReplaceChild("shape8am",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -6, 1, 2, 12),
                PartPose.offsetAndRotation(4, 16, -1, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape8an",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-5, 16, -1, 0, 0, 0));

        root.addOrReplaceChild("shape8ao",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-2, 16, -1, 0, 0, 0));

        root.addOrReplaceChild("shape8ap",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(1, 16, -1, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape8aq",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-5, 16, -1, 1.963495F, 0, 0));

        root.addOrReplaceChild("shape8ar",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-5, 16, -1, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape8as",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-5, 16, -1, 2.748893F, 0, 0));

        root.addOrReplaceChild("shape8at",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-5, 16, -1, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape8au",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-5, 16, -1, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape8av",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-5, 16, -1, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape8aw",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-5, 16, -1, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape8ax",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-2, 16, -1, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape8ay",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-2, 16, -1, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape8az",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-2, 16, -1, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape8ba",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-2, 16, -1, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape8bb",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-2, 16, -1, 1.963495F, 0, 0));

        root.addOrReplaceChild("shape8bc",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-2, 16, -1, 2.748893F, 0, 0));

        root.addOrReplaceChild("shape8bd",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(-2, 16, -1, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape8be",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(4, 16, -1, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape8bf",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(4, 16, -1, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape8bg",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(4, 16, -1, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape8bh",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(4, 16, -1, 1.963495F, 0, 0));

        root.addOrReplaceChild("shape8bi",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(4, 16, -1, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape8bj",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(4, 16, -1, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape8bk",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -0.5F, -7, 1, 1, 14),
                PartPose.offsetAndRotation(4, 16, -1, 2.748893F, 0, 0));

        root.addOrReplaceChild("shape9u",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 46)
                        .addBox(0, -1, -1, 15, 2, 2),
                PartPose.offsetAndRotation(-7.5F, 16, -1, 0.8028515F, 0, 0));

        root.addOrReplaceChild("shape9v",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 32)
                        .addBox(0, -3, -3, 1, 6, 6),
                PartPose.offsetAndRotation(-7, 16, -1, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape9w",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 32)
                        .addBox(0, -3, -3, 1, 6, 6),
                PartPose.offsetAndRotation(-7, 16, -1, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape9y",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 32)
                        .addBox(0, -3, -3, 1, 6, 6),
                PartPose.offsetAndRotation(-7, 16, -1, 0, 0, 0));

        root.addOrReplaceChild("shape9x",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(119, 0)
                        .addBox(0, -1.5F, -1.5F, 1, 3, 3),
                PartPose.offsetAndRotation(0, 0, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape9z",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(119, 0)
                        .addBox(0, -1.5F, -1.5F, 1, 3, 3),
                PartPose.ZERO);

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 23)
                        .addBox(-2, -2, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 16, 6, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape10a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(-1.5F, -1.5F, 0, 3, 3, 1),
                PartPose.offsetAndRotation(0, 0, 0, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape10b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 23)
                        .addBox(-2, -2, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 16, 6, 0, 0, 0));

        root.addOrReplaceChild("shape10c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(-1.5F, -1.5F, 0, 3, 3, 1),
                PartPose.offsetAndRotation(0, 0, 0, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 51)
                        .addBox(-1, -1, 0, 2, 2, 2),
                PartPose.offsetAndRotation(0, 16, 6.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape11a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 51)
                        .addBox(-1, -1, 0, 2, 2, 2),
                PartPose.offsetAndRotation(0, 16, 6.5F, 0, 0, 0));

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
