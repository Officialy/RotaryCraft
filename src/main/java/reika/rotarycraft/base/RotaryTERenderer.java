package reika.rotarycraft.base;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.auxiliary.trackers.SpecialDayTracker;
import reika.dragonapi.base.BlockEntityBase;
import reika.dragonapi.base.BlockEntityRenderBase;
import reika.dragonapi.base.DragonAPIMod;
import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;

import java.awt.*;

public abstract class RotaryTERenderer<TE extends BlockEntity> extends BlockEntityRenderBase<TE> {

    @Override
    protected Class<?> getModClass() {
        return RotaryCraft.class;
    }

    @Override
    protected String getModID() {
        return RotaryCraft.MODID;
    }

    @Override
    protected boolean loadXmasTextures() {
        return SpecialDayTracker.instance.loadXmasTextures();
    }

/*    protected void renderFaceColors(BlockEntityIOMachine te, double p2, double p4, double p6) {
        double offset = 0.0625;
        int alpha = te.iotick;
        Color[] colors = RotaryAux.sideColors;
        ReikaRenderHelper.prepareGeoDraw(true);
        RenderSystem.defaultBlendFunc();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder v5 = tesselator.getBuilder();

        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 - offset, p6 - offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 - offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 + 1 + offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha).endVertex();
        v5.vertex(p2 - offset, p4 - offset, p6 + 1 + offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha).endVertex();
        tesselator.end();

        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 - offset, p6 - offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 - offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 + 1 + offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 - offset, p4 - offset, p6 + 1 + offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha / 3).endVertex();
        tesselator.end();

        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 - offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 - offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha).endVertex();
        tesselator.end();
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 - offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 - offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha / 3).endVertex();
        tesselator.end();

        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 - offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha).endVertex();
        tesselator.end();
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 - offset, p4 - offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha / 3).endVertex();
        tesselator.end();

        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 - offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), alpha).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), alpha).endVertex();
        tesselator.end();
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 - offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), (int) (alpha / 2.4)).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), (int) (alpha / 2.4)).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), (int) (alpha / 2.4)).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), (int) (alpha / 2.4)).endVertex();
        tesselator.end();

        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 - offset, p6 - offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha).endVertex();
        v5.vertex(p2 - offset, p4 - offset, p6 + 1 + offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 - offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha).endVertex();
        tesselator.end();
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 - offset, p6 - offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 - offset, p4 - offset, p6 + 1 + offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 - offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha / 3).endVertex();
        tesselator.end();

        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 - offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 + 1 + offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 - offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha).endVertex();
        tesselator.end();
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 - offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 + 1 + offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 - offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha / 3).endVertex();
        tesselator.end();

        for (int i = 0; i < 6; i++) {
            int a = 0;
            int b = 0;
            int c = 0;
            switch (i) {
                case 0 -> b = -3;
                case 1 -> b = 3;
                case 2 -> c = -3;
                case 3 -> c = 3;
                case 4 -> a = -3;
                case 5 -> a = 3;
            }
            v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
            v5.vertex(p2 + 0.5, p4 + 0.5, p6 + 0.5).color(colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue(), alpha).endVertex();
            v5.vertex(p2 + 0.5 + a, p4 + 0.5 + b, p6 + 0.5 + c).color(colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue(), alpha).endVertex();
            tesselator.end();
        }

        ReikaRenderHelper.exitGeoDraw();
    }*/

    @Override
    protected final DragonAPIMod getOwnerMod() {
        return RotaryCraft.getInstance();
    }

    @Override
    protected final boolean doRenderModel(PoseStack stack, BlockEntityBase te) {
        return this.isValidMachineRenderPass(te);
    }
}
