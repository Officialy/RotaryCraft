package reika.rotarycraft.test;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import reika.rotarycraft.auxiliary.ReservoirSurface;
import static org.junit.jupiter.api.Assertions.*;

class ReservoirSurfaceTest {
    @Test void originalCornerMeansKeepTheirCompassPositions() {
        var heights=ReservoirSurface.fromLevels(new int[]{0,10,20,30,50,60,70,80,90},100);
        assertEquals(.565625F,heights.southWest(),1e-6);
        assertEquals(.675F,heights.southEast(),1e-6);
        assertEquals(.36875F,heights.northEast(),1e-6);
        assertEquals(.259375F,heights.northWest(),1e-6);
    }
    @Test void missingOrDifferentFluidNeighboursDoNotDiluteHeights() {
        var levels=new int[9]; Arrays.fill(levels,-1); levels[4]=50;
        var alone=ReservoirSurface.fromLevels(levels,100);
        assertEquals(new ReservoirSurface(.5F,.5F,.5F,.5F),alone);
        levels[5]=100;
        var eastern=ReservoirSurface.fromLevels(levels,100);
        assertEquals(.5F,eastern.southWest()); assertEquals(.5F,eastern.northWest());
        assertEquals(.71875F,eastern.southEast()); assertEquals(.71875F,eastern.northEast());
        Arrays.fill(levels,-1);
        assertEquals(.71875F,eastern.southEast(),"computed heights must not retain the mutable level array");
    }
    @Test void originalBottomAndFullInsetsAreRetained() {
        var empty=new int[9]; Arrays.fill(empty,-1); empty[4]=0;
        assertEquals(.0625F,ReservoirSurface.fromLevels(empty,64000).northWest());
        empty[4]=64000;
        assertEquals(.9375F,ReservoirSurface.fromLevels(empty,64000).northWest());
    }
    @Test void malformedGridCannotProduceInventedGeometry() {
        assertThrows(IllegalArgumentException.class,()->ReservoirSurface.fromLevels(new int[8],100));
        assertThrows(IllegalArgumentException.class,()->ReservoirSurface.fromLevels(new int[9],0));
        var absent=new int[9]; absent[4]=-1;
        assertThrows(IllegalArgumentException.class,()->ReservoirSurface.fromLevels(absent,100));
    }
}
