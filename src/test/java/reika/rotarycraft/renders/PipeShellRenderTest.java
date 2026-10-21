package reika.rotarycraft.renders;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.Direction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PipeShellRenderTest {
    @Test
    void allConnectionMasksHaveExactlyOneSurfaceAtEveryExposedPixel() {
        for (int mask = 0; mask < 64; mask++) {
            var geometry = PipeShellRenderer.geometry(mask);
            List<PipeShellRenderer.Rect> all = new ArrayList<>(geometry.frame());
            all.addAll(geometry.glass());
            for (Direction face : Direction.values()) {
                for (int a = 0; a < 16; a++) {
                    for (int b = 0; b < 16; b++) {
                        float[] point = point(face, a + .5F, b + .5F);
                        boolean inside = occupied(mask, point[0] - face.getStepX() * .25F,
                                point[1] - face.getStepY() * .25F, point[2] - face.getStepZ() * .25F);
                        boolean outside = occupied(mask, point[0] + face.getStepX() * .25F,
                                point[1] + face.getStepY() * .25F, point[2] + face.getStepZ() * .25F);
                        long count = all.stream().filter(r -> covers(r, face, point)).count();
                        assertEquals(inside && !outside ? 1L : 0L, count,
                                "mask=" + mask + ", face=" + face + ", pixel=" + a + "," + b);
                    }
                }
            }
        }
    }

    @Test
    void straightRunsHaveContinuousGlassWithoutTransverseFrameBars() {
        for (Direction run : List.of(Direction.EAST, Direction.UP, Direction.SOUTH)) {
            int mask = 1 << run.ordinal() | 1 << run.getOpposite().ordinal();
            var geometry = PipeShellRenderer.geometry(mask);
            for (Direction face : Direction.values()) {
                if (face.getAxis() == run.getAxis()) continue;
                for (int i = 0; i < 16; i++) {
                    float[] p = point(face, 8, 8);
                    p[run.getAxis() == Direction.Axis.X ? 0 : run.getAxis() == Direction.Axis.Y ? 1 : 2] = i + .5F;
                    assertEquals(1L, geometry.glass().stream().filter(r -> covers(r, face, p)).count());
                    assertEquals(0L, geometry.frame().stream().filter(r -> covers(r, face, p)).count());
                }
            }
        }
    }

    private static float[] point(Direction face, float a, float b) {
        float plane = face.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 14 : 2;
        return switch (face.getAxis()) {
            case X -> new float[]{plane, b, a};
            case Y -> new float[]{a, plane, b};
            case Z -> new float[]{a, b, plane};
        };
    }

    private static boolean covers(PipeShellRenderer.Rect r, Direction face, float[] p) {
        return r.face() == face && p[0] >= r.x1() && p[0] <= r.x2()
                && p[1] >= r.y1() && p[1] <= r.y2() && p[2] >= r.z1() && p[2] <= r.z2();
    }

    /** Independent solid-volume oracle: a 12px core plus full-width 2px connection arms. */
    private static boolean occupied(int mask, float x, float y, float z) {
        if (core(x) && core(y) && core(z)) return true;
        for (Direction d : Direction.values()) {
            if ((mask & (1 << d.ordinal())) == 0) continue;
            float run = d.getAxis() == Direction.Axis.X ? x : d.getAxis() == Direction.Axis.Y ? y : z;
            boolean cross = switch (d.getAxis()) {
                case X -> core(y) && core(z);
                case Y -> core(x) && core(z);
                case Z -> core(x) && core(y);
            };
            if (cross && (d.getAxisDirection() == Direction.AxisDirection.POSITIVE
                    ? run >= 14 && run <= 16 : run >= 0 && run <= 2)) return true;
        }
        return false;
    }

    private static boolean core(float p) {
        return p >= 2 && p <= 14;
    }
}
