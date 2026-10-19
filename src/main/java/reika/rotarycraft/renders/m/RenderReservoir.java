/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders.m;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import reika.dragonapi.client.EntityLighting;
import reika.dragonapi.instantiable.event.client.FluidModelEvent;
import reika.rotarycraft.auxiliary.ReservoirSurface;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.models.ReservoirModel;
import reika.rotarycraft.registry.RotaryModelLayers;

/** Original connected tank walls, glass cover, fluid tint, smoothed corners and emissive light. */
public class RenderReservoir extends RotaryTERenderer<BlockEntityReservoir> {
    private final ReservoirModel model;
    public RenderReservoir(BlockEntityRendererProvider.Context context) {
        model=new ReservoirModel(context.bakeLayer(RotaryModelLayers.RESERVOIR));
    }
    public record UV(float u0,float v0,float u1,float v1) {
        public static UV of(TextureAtlasSprite sprite) { return new UV(sprite.getU0(),sprite.getV0(),sprite.getU1(),sprite.getV1()); }
    }
    public record FluidSurface(ReservoirSurface heights,UV uv,int color,boolean emissive) {}
    public static final class ReservoirRenderState extends BlockEntityRenderState {
        public boolean inWorld;
        public boolean modelAllowed;
        public int walls;
        public UV cover;
        public FluidSurface fluid;
    }
    @Override public ReservoirRenderState createRenderState() { return new ReservoirRenderState(); }

    @Override public void extractRenderState(BlockEntityReservoir tile,BlockEntityRenderState base,float partialTick,
            Vec3 cameraPosition,ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        super.extractRenderState(tile,base,partialTick,cameraPosition,breakProgress);
        var state=(ReservoirRenderState)base;
        state.inWorld=tile.isInWorld();
        state.modelAllowed=!state.inWorld || doRenderModel(new PoseStack(),tile);
        state.walls=0; state.cover=null; state.fluid=null;
        for(var direction:Direction.values()) if(direction!=Direction.UP &&
                (direction==Direction.DOWN || !state.inWorld || !tile.isConnectedOnSide(direction))) state.walls|=1<<direction.ordinal();
        var minecraft=Minecraft.getInstance();
        if(tile.isCovered) state.cover=UV.of(minecraft.getModelManager().getBlockStateModelSet()
                .getParticleMaterial(Blocks.GLASS.defaultBlockState()).sprite());
        var stack=tile.getFluid();
        if(stack.isEmpty()) return;
        var fluid=stack.getFluid(); var fluidState=fluid.defaultFluidState();
        var blockState=fluidState.createLegacyBlock();
        var visual=minecraft.getModelManager().getFluidStateModelSet().get(fluidState);
        var level=tile.getLevel();
        var access=level instanceof BlockAndTintGetter tintLevel?tintLevel:null;
        if(access!=null && fluid.isSame(Fluids.WATER))
            visual=FluidModelEvent.fire(access,tile.getBlockPos(),blockState,fluidState,visual);
        var tint=visual.fluidTintSource();
        int color=tint==null?-1:access==null?tint.color(fluidState):tint.colorInWorld(fluidState,blockState,access,tile.getBlockPos());
        color=tile.getFluidRenderColor(color)|0xff000000;
        var levels=new int[9]; Arrays.fill(levels,-1); levels[4]=stack.getAmount();
        if(level!=null) for(int dz=-1;dz<=1;dz++) for(int dx=-1;dx<=1;dx++) {
            if(dx==0 && dz==0) continue;
            var pos=tile.getBlockPos().offset(dx,0,dz);
            if(level.hasChunkAt(pos) && level.getBlockEntity(pos) instanceof BlockEntityReservoir neighbor
                    && !neighbor.getFluid().isEmpty() && neighbor.getFluid().getFluid()==fluid)
                levels[(dz+1)*3+dx+1]=neighbor.getFluidLevel();
        }
        state.fluid=new FluidSurface(ReservoirSurface.fromLevels(levels,BlockEntityReservoir.CAPACITY),
                UV.of(visual.stillMaterial().sprite()),color,state.inWorld && stack.getFluidType().getLightLevel(stack)>0);
    }
    @Override public void submit(BlockEntityRenderState base,PoseStack poseStack,SubmitNodeCollector collector,CameraRenderState camera) {
        var state=(ReservoirRenderState)base;
        int light=state.lightCoords,walls=state.walls;
        boolean inWorld=state.inWorld;
        var modelPose=new PoseStack(); modelPose.last().set(poseStack.last());
        modelPose.translate(.5,1.5,.5); modelPose.scale(1,-1,-1);
        if(inWorld) modelPose.rotate(Axis.YP.rotationDegrees(90));
        if(state.modelAllowed) collector.submitCustomGeometry(modelPose,RenderTypes.entityCutout(ReservoirModel.TEXTURE_LOCATION),(pose,vertices)->{
            var captured=new PoseStack(); captured.last().set(pose);
            if(!inWorld) model.renderAllParts(captured,vertices,light);
            else {
                for(var direction:Direction.values()) if(direction.getAxis().isHorizontal() && (walls&(1<<direction.ordinal()))!=0)
                    model.renderSide(captured,vertices,light,direction);
                if((walls&(1<<Direction.DOWN.ordinal()))!=0) model.renderSide(captured,vertices,light,Direction.DOWN);
            }
        });
        var cover=state.cover;
        if(state.modelAllowed && cover!=null) collector.submitCustomGeometry(poseStack,RenderTypes.entityTranslucentCull(TextureAtlas.LOCATION_BLOCKS),
                (pose,vertices)->surface(pose,vertices,cover,new ReservoirSurface(.99F,.99F,.99F,.99F),-1,light));
        var fluid=state.fluid;
        if(fluid!=null) collector.submitCustomGeometry(poseStack,
                (fluid.emissive?EntityLighting.FULL_BRIGHT:EntityLighting.LIT).translucent(TextureAtlas.LOCATION_BLOCKS,true),
                (pose,vertices)->surface(pose,vertices,fluid.uv,fluid.heights,fluid.color,fluid.emissive?LightCoordsUtil.FULL_BRIGHT:light));
    }
    private static void surface(PoseStack.Pose pose,VertexConsumer vertices,UV uv,ReservoirSurface heights,int color,int light) {
        vertex(pose,vertices,0,heights.southWest(),1,uv.u0,uv.v1,color,light);
        vertex(pose,vertices,1,heights.southEast(),1,uv.u1,uv.v1,color,light);
        vertex(pose,vertices,1,heights.northEast(),0,uv.u1,uv.v0,color,light);
        vertex(pose,vertices,0,heights.northWest(),0,uv.u0,uv.v0,color,light);
    }
    private static void vertex(PoseStack.Pose pose,VertexConsumer vertices,float x,float y,float z,float u,float v,int color,int light) {
        vertices.addVertex(pose,x,y,z).setColor(color).setUv(u,v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose,0,1,0);
    }
}
