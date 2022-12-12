//package reika.rotarycraft.auxiliary;
//
//import java.util.HashMap;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.block.entity.BlockEntity;
//
//public class HeatRippleRenderer implements ShaderHook {
//
//    public static final HeatRippleRenderer instance = new HeatRippleRenderer();
//
//    private final RayTracerWithCache LOS = RayTracer.getVisualLOSForRenderCulling();
//
//    private HeatRippleRenderer() {
//        ClientProxy.getHeatRippleShader().setHook(this);
//    }
//
//    public void onPreRender(ShaderProgram s) {
//
//    }
//
//    public void onPostRender(ShaderProgram s) {
//        //s.setEnabled(s.hasOngoingFoci());
//    }
//
//    public void updateEnabled(ShaderProgram s) {
//
//    }
//
//    public boolean addHeatRippleEffectIfLOS(BlockEntity tile, double x, double y, double z, float f, float fac, float scale, float centerFade) {
//        Player ep = Minecraft.getInstance().player;
//        double dist = ep.getDistance(pos);
//        return this.addHeatRippleEffectIfLOS(tile, pos, ep, dist, f, fac, scale, centerFade);
//    }
//
//    public boolean addHeatRippleEffectIfLOS(BlockEntity tile, double x, double y, double z, Player ep, double dist, float f, float fac, float scale, float centerFade) {
//        //LOS.update(tile);
//        LOS.setOrigins(pos, ep.getY(), ep.getY(), ep.getZ())
//        if (LOS.isClearLineOfSight(tile)) {
//            HeatRippleRenderer.instance.addHeatRippleEffect(tile, dist, f, fac, scale, centerFade);
//            return true;
//        }
//        return false;
//    }
//
//    public void addHeatRippleEffect(BlockEntity tile, double dist, float f, float fac, float scale, float centerFade) {
//        HashMap<String, Object> map = new HashMap();
//        map.put("distance", dist * dist);
//        map.put("factor", fac);
//        map.put("scale", scale);
//        map.put("fade", centerFade);
//        ClientProxy.getHeatRippleShader().addFocus(tile);
//        ClientProxy.getHeatRippleShader().modifyLastCompoundFocus(f, map);
//
//        ClientProxy.getHeatRippleShader().setEnabled(true);
//        ClientProxy.getHeatRippleShader().putIntensity(1);
//    }
//}
