package reika.rotarycraft.auxiliary;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import reika.rotarycraft.renders.RotaryRenderPipelines;

/** Source face palette, expanded bounds, per-face alpha and three-block direction rays. */
public final class MachineFaceOverlay {
    private MachineFaceOverlay() {}
    // Ordered DOWN, UP, NORTH, SOUTH, WEST, EAST, as in the original TESR.
    private static final int[] COLORS = {0x00ffff,0x0000ff,0xffff00,0x000000,0xff7800,0xff00ff};
    private static final float L=-.0625F, H=1.0625F;
    private static final float[][] FACES = {
        {L,L,L, H,L,L, H,L,H, L,L,H},
        {L,H,H, H,H,H, H,H,L, L,H,L},
        {L,H,L, H,H,L, H,L,L, L,L,L},
        {L,L,H, H,L,H, H,H,H, L,H,H},
        {L,L,L, L,L,H, L,H,H, L,H,L},
        {H,H,L, H,H,H, H,L,H, H,L,L}
    };
    private static final int[][] RAYS={{0,-3,0},{0,3,0},{0,0,-3},{0,0,3},{-3,0,0},{3,0,0}};
    public static void submit(PoseStack stack, SubmitNodeCollector collector, int alpha) {
        if(alpha<=0) return;
        collector.submitCustomGeometry(stack,RotaryRenderPipelines.IO_BOX_TYPE,(pose,vertices)->{
            for(int face=0;face<6;face++) {
                int rgba=color(COLORS[face],face==3?(int)(alpha/2.4):alpha/3);
                var points=FACES[face];
                for(int i=0;i<12;i+=3) vertices.addVertex(pose,points[i],points[i+1],points[i+2]).setColor(rgba);
            }
        });
        collector.submitCustomGeometry(stack,RotaryRenderPipelines.IO_LINES_TYPE,(pose,vertices)->{
            for(int face=0;face<6;face++) {
                int rgba=color(COLORS[face],alpha); var points=FACES[face];
                for(int i=0;i<12;i+=3) {
                    int next=(i+3)%12;
                    line(pose,vertices,points[i],points[i+1],points[i+2],points[next],points[next+1],points[next+2],rgba);
                }
                var ray=RAYS[face];
                line(pose,vertices,.5F,.5F,.5F,.5F+ray[0],.5F+ray[1],.5F+ray[2],rgba);
            }
        });
    }
    /** Fully preserved dormant compass; its original bevel call remains disabled. */
    public static void submitCompass(PoseStack stack, SubmitNodeCollector collector, int alpha) {
        if(alpha<=0) return;
        int rgba=color(0xffff00,alpha);
        collector.submitCustomGeometry(stack,RotaryRenderPipelines.IO_LINES_TYPE,(pose,v)->{
            line(pose,v,-.5F,1.05F,.5F,1.5F,1.05F,.5F,rgba);
            line(pose,v,.5F,1.05F,-.5F,.5F,1.05F,1.5F,rgba);
            line(pose,v,.35F,1.05F,-.75F,.35F,1.05F,-1.25F,rgba);
            line(pose,v,.35F,1.05F,-1.25F,.65F,1.05F,-.75F,rgba);
            line(pose,v,.65F,1.05F,-.75F,.65F,1.05F,-1.25F,rgba);
        });
    }
    private static int color(int rgb,int alpha) { return (Math.clamp(alpha,0,255)<<24)|rgb; }
    private static void line(PoseStack.Pose pose,VertexConsumer v,float x,float y,float z,float a,float b,float c,int rgba) {
        float nx=a-x,ny=b-y,nz=c-z;
        float length=(float)Math.sqrt(nx*nx+ny*ny+nz*nz); nx/=length; ny/=length; nz/=length;
        v.addVertex(pose,x,y,z).setColor(rgba).setNormal(pose,nx,ny,nz).setLineWidth(1);
        v.addVertex(pose,a,b,c).setColor(rgba).setNormal(pose,nx,ny,nz).setLineWidth(1);
    }
}
