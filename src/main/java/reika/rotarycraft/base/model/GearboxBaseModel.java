package reika.rotarycraft.base.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;
import java.util.Collection;

import static reika.rotarycraft.RotaryCraft.MODID;

public abstract class GearboxBaseModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/transmission/shaft/gear/geartex.png");

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
    private final ModelPart root;

    private final Collection<ModelPart> base = new ArrayList<>();
    private final Collection<ModelPart> supportColumns = new ArrayList<>();

    public GearboxBaseModel(ModelPart modelPart) {
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

        base.add(shape1);
        base.add(shape2);
        base.add(shape3);
        base.add(shape4);
        base.add(shape5);
        base.add(shape6);
        base.add(shape7);
        base.add(shape8);
        base.add(shape9);
        base.add(shape10);
        base.add(shape11);
    }

    // Grab the parts in the constructor if you need them

    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        PartDefinition root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.ZERO);

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 12, 16),
                PartPose.ZERO);

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 12, 16),
                PartPose.ZERO);

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.ZERO);

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.ZERO);

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.ZERO);

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.ZERO);

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.ZERO);

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.ZERO);

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .texOffs(42, 17)
                        .addBox(0, 0, 0, 1, 3, 10),
                PartPose.ZERO);

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .texOffs(42, 17)
                        .addBox(0, 0, 0, 1, 3, 10),
                PartPose.ZERO);

        return LayerDefinition.create(definition, 128, 128);
    }

    protected final void addSupport(ModelPart p) {
        supportColumns.add(p);
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int light,  BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        root.render(stack, tex, 0, 0, 1, 1, 1, 1);
    }

/*    protected final void renderSupports(PoseStack stack, BlockEntity te, ArrayList li) {
        if (li != null && !li.isEmpty()) {
            GearboxTypes type = (GearboxTypes) li.get(0);
            //GL11.glColor4f(0.35F, 0.82F, 1F, 1F);
            RotaryRenderList.getRenderForMachine(MachineRegistry.GEARBOX).bindTextureByName("/reika/rotaryCraft/textures/blockentitytex/transmission/shaft/gear/" + type.getBaseGearboxTexture());
        }
        for (ModelPart p : supportColumns)
            p.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
//        GL11.glColor4f(1, 1, 1, 1);
    }*/
}
