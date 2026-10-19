package reika.rotarycraft.auxiliary;

/** Original per-corner mean heights; rows are north-to-south, columns west-to-east. */
public record ReservoirSurface(float southWest,float southEast,float northEast,float northWest) {
    /** Missing/different-fluid neighbours are -1; the center at index four must be present. */
    public static ReservoirSurface fromLevels(int[] levels,int capacity) {
        if(levels.length!=9 || levels[4]<0 || capacity<=0) throw new IllegalArgumentException("A 3x3 level grid, present center and positive capacity are required");
        return new ReservoirSurface(corner(levels,capacity,6,7,3),corner(levels,capacity,8,7,5),
                corner(levels,capacity,2,1,5),corner(levels,capacity,0,1,3));
    }
    private static float corner(int[] levels,int capacity,int diagonal,int vertical,int horizontal) {
        double sum=height(levels[4],capacity); int count=1;
        for(int index:new int[]{diagonal,vertical,horizontal}) if(levels[index]>=0) { sum+=height(levels[index],capacity); count++; }
        return (float)(sum/count);
    }
    private static double height(int amount,int capacity) { return .0625+.875*amount/capacity; }
}
