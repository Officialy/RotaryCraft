//package reika.rotarycraft.auxiliary;
//
//import net.minecraft.world.level.block.entity.BlockEntity;
//import reika.rotarycraft.blockentities.Weaponry.BlockEntityEMP;
//
//public class EMPTileWatcher implements TileUpdateWatcher {
//
//    public static final EMPTileWatcher instance = new EMPTileWatcher();
//
//    private boolean isRegistered;
//
//    private EMPTileWatcher() {
//
//    }
//
//    @Override
//    public int watcherSortIndex() {
//        return Integer.MIN_VALUE;
//    }
//
//    @Override
//    public boolean interceptTileUpdate(BlockEntity te) {
//        return BlockEntityEMP.isShutdown(te);
//    }
//
//    public void registerTileWatcher() {
//        if (isRegistered)
//            return;
//        isRegistered = true;
//        TileUpdateEvent.addWatcher(this);
//    }
//
//    public void unregisterTileWatcher() {
//        if (!isRegistered)
//            return;
//        isRegistered = false;
//        TileUpdateEvent.removeWatcher(this);
//    }
//
//}
