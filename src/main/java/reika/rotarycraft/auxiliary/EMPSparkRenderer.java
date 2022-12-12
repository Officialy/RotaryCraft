/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import org.lwjgl.opengl.GL11;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.Interpolation;
import reika.dragonapi.instantiable.data.immutable.DecimalPosition;
import reika.dragonapi.instantiable.data.immutable.WorldLocation;
import reika.dragonapi.libraries.java.ReikaRandomHelper;
import reika.dragonapi.libraries.rendering.ReikaColorAPI;

import java.util.*;


public class EMPSparkRenderer {

    public static final EMPSparkRenderer instance = new EMPSparkRenderer();

    private static final int SPARK_COUNT = 4;

    private static final Random rand = DragonAPI.rand;

    private final ArrayList<EMPSpark> sparks = new ArrayList<>();
    private final HashSet<WorldLocation> locations = new HashSet();

    private final ArrayList<WorldLocation> selectableLocations = new ArrayList<>();

    private EMPSparkRenderer() {

    }

    public void addSparkingLocation(WorldLocation loc) {
        if (!selectableLocations.contains(loc))
            selectableLocations.add(loc);
    }

    public void removeSparkingLocation(WorldLocation loc) {
        selectableLocations.remove(loc);
    }

    public void tick() {
        Iterator<EMPSpark> it = sparks.iterator();
        while (it.hasNext()) {
            EMPSpark sp = it.next();
            if (sp.tick()) {
                it.remove();
                locations.remove(sp.location);
            }
        }
        if (!selectableLocations.isEmpty() && sparks.size() < SPARK_COUNT && DragonAPI.rand.nextInt(4) == 0) {
            WorldLocation loc = selectableLocations.get(DragonAPI.rand.nextInt(selectableLocations.size()));
            if (loc != null) {
                this.doAddLocation(loc);
            }
        }
    }
	/*
	public void addLocation(BlockEntity te) {
		if (sparks.size() < SPARK_COUNT) {
			WorldLocation loc = new WorldLocation(te);
			this.doAddLocation(loc);
		}
	}*/

    private void doAddLocation(WorldLocation loc) {
        if (loc != null && !locations.contains(loc)) {
            sparks.add(new EMPSpark(loc));
            locations.add(loc);
        }
    }

    public void render(PoseStack stack, BufferBuilder v5, float ptick) {
//        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        //ReikaRenderHelper.prepareGeoDraw(true);
        //ReikaRenderHelper.disableEntityLighting();
        stack.pushPose();
        //stack.translate(-RenderManager.renderPosX, -RenderManager.renderPosY, -RenderManager.renderPosZ);
        for (EMPSpark sp : sparks) {
            if (sp.location.getDimension() == Minecraft.getInstance().level.dimension()) {
                sp.render(v5, ptick);
            }
        }
        //GL11.glPopAttrib();
        stack.popPose();
    }

    private static class EMPSpark {

        private final WorldLocation location;

        private final int LIFESPAN;
        private final Collection<LightningBolt> bolts = new ArrayList<>();
        private final Interpolation brightnessCurve = new Interpolation(false);

        private int age = 0;

        private EMPSpark(WorldLocation loc) {
            location = loc;

            int n = 1 + DragonAPI.rand.nextInt(4);
            for (int i = 0; i < n; i++) {
                bolts.add(this.createBolt());
            }

            LIFESPAN = 8 + DragonAPI.rand.nextInt(40);

            int a = 0;
            double f = 0.5 + 0.5 * DragonAPI.rand.nextDouble();
            brightnessCurve.addPoint(a, f);
            while (a < LIFESPAN) {
                a += Math.min(LIFESPAN - a, ReikaRandomHelper.getRandomBetween(4, 10));
                f = DragonAPI.rand.nextDouble();
                brightnessCurve.addPoint(a, f);
            }
        }

        private LightningBolt createBolt() {
            double o = 0.0625;
            double r = 0.5 + o;
            double x1 = -o + DragonAPI.rand.nextDouble() * (1 + o * 2);
            double y1 = -o + DragonAPI.rand.nextDouble() * (1 + o * 2);
            double z1 = -o + DragonAPI.rand.nextDouble() * (1 + o * 2);
            double x2 = -o + DragonAPI.rand.nextDouble() * (1 + o * 2);
            double y2 = -o + DragonAPI.rand.nextDouble() * (1 + o * 2);
            double z2 = -o + DragonAPI.rand.nextDouble() * (1 + o * 2);
            switch (Direction.values()[DragonAPI.rand.nextInt(6)]) {
                case DOWN:
                    y1 = y2 = -o;
                    break;
                case UP:
                    y1 = y2 = o;
                    break;
                case EAST:
                    x1 = x2 = o;
                    break;
                case WEST:
                    x1 = x2 = -o;
                    break;
                case NORTH:
                    z1 = z2 = -o;
                    break;
                case SOUTH:
                    z1 = z2 = o;
                    break;
                default:
                    break;
            }

            x1 += location.pos.getX();
            x2 += location.pos.getX();
            y1 += location.pos.getY();
            y2 += location.pos.getY();
            z1 += location.pos.getZ();
            z2 += location.pos.getZ();

            //LightningBolt bolt = new LightningBolt(x1, y1, z1, x2, y2, z2, 6);
            LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, location.getWorld());
            //bolt.scaleVelocity(1 / 64D);
            //bolt.scaleVariance(1 / 12D);

            return bolt;
        }

        public void render(BufferBuilder v5, float ptick) {
            double f = brightnessCurve.getValue(age);
            for (LightningBolt bolt : bolts) {
                float w = GL11.glGetFloat(GL11.GL_LINE_WIDTH);
                GL11.glLineWidth(4);
                this.drawBolt(bolt, v5, 0x7000ffff, f);
                GL11.glLineWidth(w);
                this.drawBolt(bolt, v5, 0xffffffff, f);
                bolt.tick();
            }
        }

        private void drawBolt(LightningBolt bolt, BufferBuilder v5, int color, double brightness) {
            v5.begin(VertexFormat.Mode.LINE_STRIP, DefaultVertexFormat.POSITION);
            v5.color(color & 0xffffff, color, color, (int) (brightness * ReikaColorAPI.getAlpha(color))); //todo fix color
            for (int i = 0; i < bolt.maxUpStep; i++) { //todo figure out wtf nsteps is
                DecimalPosition p = new DecimalPosition(bolt.getPosition(i));
                v5.vertex(p.xCoord, p.yCoord, p.zCoord);
            }
            v5.end();
        }

        private boolean tick() {
            age++;
            return age >= LIFESPAN;
        }

    }

}
