package reika.rotarycraft.models;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import reika.rotarycraft.RotaryCraft;

@OnlyIn(Dist.CLIENT)
public class LampModel extends Model {
    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(RotaryCraft.MODID, "textures/blockentitytex/lamptex.png");
    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart shape7;
    private final ModelPart shape8;
    private final ModelPart shape9;
    private final ModelPart shape10;
    private final ModelPart shape11;
    private final ModelPart shape12;
    private final ModelPart shape13;
    private final ModelPart shape14;
    private final ModelPart shape15;
    private final ModelPart shape16;
    private final ModelPart root;
    public LampModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6 = modelPart.getChild("shape6");
        this.shape7 = modelPart.getChild("shape7");
        this.shape8 = modelPart.getChild("shape8");
        this.shape9 = modelPart.getChild("shape9");
        this.shape10 = modelPart.getChild("shape10");
        this.shape11 = modelPart.getChild("shape11");
        this.shape12 = modelPart.getChild("shape12");
        this.shape13 = modelPart.getChild("shape13");
        this.shape14 = modelPart.getChild("shape14");
        this.shape15 = modelPart.getChild("shape15");
        this.shape16 = modelPart.getChild("shape16");

        this.shape1.setPos(-8F, 23F, -8F);
        this.shape2.setPos(-7F, 10F, -2F);
        this.shape3.setPos(-6F, 11F, -6F);
        this.shape4.setPos(4F, 11F, -6F);
        this.shape5.setPos(-4F, 11F, -6F);
        this.shape6.setPos(-4F, 21F, -6F);
        this.shape7.setPos(4F, 12F, -8F);
        this.shape8.setPos(-5F, 12F, -8F);
        this.shape9.setPos(-4F, 12F, -8F);
        this.shape10.setPos(-4F, 21F, -8F);
        this.shape11.setPos(-6F, 11F, 6F);
        this.shape12.setPos(-4F, 20F, -6F);
        this.shape13.setPos(-4F, 13F, -6F);
        this.shape14.setPos(-4F, 14F, -6F);
        this.shape15.setPos(3F, 14F, -6F);
        this.shape16.setPos(-3F, 14F, -4F);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("shape1", CubeListBuilder.create().texOffs(0, 0)
                .addBox(0F, 0F, 0F, 16, 1, 16).mirror(), PartPose.offset(0, 0, 0));

//        shape1.setPos(-8F, 23F, -8F);
//        shape1

        partdefinition.addOrReplaceChild("shape2", CubeListBuilder.create().texOffs(64, 27)
                .addBox(0F, 0F, 0F, 14, 14, 8).mirror(), PartPose.offset(0, 0, 0));

//        Shape2 = new ModelPart(this, 64, 27);
//        Shape2.setPos(-7F, 10F, -2F);
//        Shape2
        partdefinition.addOrReplaceChild("shape3", CubeListBuilder.create().texOffs(0, 58)
                .addBox(0F, 0F, 0F, 2, 12, 4).mirror(), PartPose.offset(0, 0, 0));

//        Shape3 = new ModelPart(this, 0, 58);
//        Shape3.setPos(-6F, 11F, -6F);
//        Shape3

        partdefinition.addOrReplaceChild("shape4", CubeListBuilder.create().texOffs(0, 58)
                .addBox(0F, 0F, 0F, 2, 12, 4).mirror(), PartPose.offset(0, 0, 0));
//        Shape4 = new ModelPart(this, 0, 58);
//        Shape4.setPos(4F, 11F, -6F);
//        Shape4

        partdefinition.addOrReplaceChild("shape5", CubeListBuilder.create().texOffs(0, 52)
                .addBox(0F, 0F, 0F, 8, 2, 4).mirror(), PartPose.offset(0, 0, 0));
//        Shape5 = new ModelPart(this, 0, 52);
//        Shape5.setPos(-4F, 11F, -6F);
//        Shape5

        partdefinition.addOrReplaceChild("shape6", CubeListBuilder.create().texOffs(0, 52)
                .addBox(0F, 0F, 0F, 8, 2, 4).mirror(), PartPose.offset(0, 0, 0));
//        Shape6 = new ModelPart(this, 0, 52);
//        Shape6.setPos(-4F, 21F, -6F);
//        Shape6

        partdefinition.addOrReplaceChild("shape7", CubeListBuilder.create().texOffs(12, 58)
                .addBox(0F, 0F, 0F, 1, 10, 2).mirror(), PartPose.offset(0, 0, 0));
//        Shape7 = new ModelPart(this, 12, 58);
//        Shape7.setPos(4F, 12F, -8F);
//        Shape7

        partdefinition.addOrReplaceChild("shape8", CubeListBuilder.create().texOffs(12, 58)
                .addBox(0F, 0F, 0F, 1, 10, 2).mirror(), PartPose.offset(0, 0, 0));
//        Shape8 = new ModelPart(this, 12, 58);
//        Shape8.setPos(-5F, 12F, -8F);
//        Shape8

        partdefinition.addOrReplaceChild("shape9", CubeListBuilder.create().texOffs(32, 54)
                .addBox(0F, 0F, 0F, 8, 1, 2).mirror(), PartPose.offset(0, 0, 0));
//        Shape9 = new ModelPart(this, 32, 54);
//        Shape9.setPos(-4F, 12F, -8F);
//        Shape9

        partdefinition.addOrReplaceChild("shape10", CubeListBuilder.create().texOffs(32, 54)
                .addBox(0F, 0F, 0F, 8, 1, 2).mirror(), PartPose.offset(0, 0, 0));
//        Shape10 = new ModelPart(this, 32, 54);
//        Shape10.setPos(-4F, 21F, -8F);
//        Shape10

        partdefinition.addOrReplaceChild("shape11", CubeListBuilder.create().texOffs(64, 49)
                .addBox(0F, 0F, 0F, 12, 12, 2).mirror(), PartPose.offset(0, 0, 0));
//        Shape11 = new ModelPart(this, 64, 49);
//        Shape11.setPos(-6F, 11F, 6F);
//        Shape11

        partdefinition.addOrReplaceChild("shape12", CubeListBuilder.create().texOffs(32, 57)
                .addBox(0F, 0F, 0F, 8, 1, 2).mirror(), PartPose.offset(0, 0, 0));
//        Shape12 = new ModelPart(this, 32, 57);
//        Shape12.setPos(-4F, 20F, -6F);
//        Shape12

        partdefinition.addOrReplaceChild("shape13", CubeListBuilder.create().texOffs(32, 57)
                .addBox(0F, 0F, 0F, 8, 1, 2).mirror(), PartPose.offset(0, 0, 0));
//        Shape13 = new ModelPart(this, 32, 57);
//        Shape13.setPos(-4F, 13F, -6F);
//        Shape13

        partdefinition.addOrReplaceChild("shape14", CubeListBuilder.create().texOffs(20, 24)
                .addBox(0F, 0F, 0F, 1, 6, 2).mirror(), PartPose.offset(0, 0, 0));
//        Shape14 = new ModelPart(this, 20, 24);
//        Shape14.setPos(-4F, 14F, -6F);
//        Shape14

        partdefinition.addOrReplaceChild("shape15", CubeListBuilder.create().texOffs(20, 24)
                .addBox(0F, 0F, 0F, 1, 6, 2).mirror(), PartPose.offset(0, 0, 0));
//        Shape15 = new ModelPart(this, 20, 24);
//        Shape15.setPos(3F, 14F, -6F);
//        Shape15

        partdefinition.addOrReplaceChild("shape16", CubeListBuilder.create().texOffs(-1, 45)
                .addBox(0F, 0F, 0F, 6, 6, 1).mirror(), PartPose.offset(0, 0, 0));

//        Shape16 = new ModelPart(this, -1, 45);
//        Shape16.setPos(-3F, 14F, -4F);
//        Shape16
        return LayerDefinition.create(meshdefinition, 128, 128);
    }


    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
