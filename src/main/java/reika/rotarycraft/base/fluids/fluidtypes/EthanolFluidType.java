package reika.rotarycraft.base.fluids.fluidtypes;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.neoforged.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.common.SoundActions;
import net.neoforged.fluids.FluidType;
import org.joml.Vector3f;
import reika.rotarycraft.RotaryCraft;

import java.util.function.Consumer;

public class EthanolFluidType extends FluidType {

    public static final ResourceLocation ETHANOL_FLUID_STILL_TEXTURE = new ResourceLocation(RotaryCraft.MODID, "block/fluid/ethanol");
    public static final ResourceLocation ETHANOL_FLUID_FLOWING_TEXTURE = new ResourceLocation(RotaryCraft.MODID, "block/fluid/ethanol_anim");
    public static final ResourceLocation ETHANOL_FLUID_OVERLAY_TEXTURE = ETHANOL_FLUID_STILL_TEXTURE;

    public EthanolFluidType() {
        super(Properties.create()
                .canHydrate(false)
                .canDrown(false)
                .canExtinguish(false)
                .canPushEntity(true)
                .canSwim(true)
                .pathType(BlockPathTypes.WATER)
                .adjacentPathType(BlockPathTypes.WATER)
                .fallDistanceModifier(0.15f)
                .motionScale(0.0115)
                .rarity(Rarity.UNCOMMON)
                .viscosity(6100)
                .density(7000)
                .temperature(1873)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY));
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {

            @Override
            public ResourceLocation getStillTexture() {
                return ETHANOL_FLUID_STILL_TEXTURE;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return ETHANOL_FLUID_FLOWING_TEXTURE;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return ETHANOL_FLUID_OVERLAY_TEXTURE;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return ETHANOL_FLUID_STILL_TEXTURE;
            }

            @Override
            public int getTintColor() {
                return 0xF0B564;
            }

            @Override
            public Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                // Scale the brightness of fog but make sure it is never darker than the dimension's min brightness.
                BlockPos blockpos = new BlockPos((int) camera.getEntity().getX(), (int) camera.getEntity().getY(), (int) camera.getEntity().getZ());
                float brightnessAtEyes = LightTexture.getBrightness(camera.getEntity().level().dimensionType(), camera.getEntity().level().getMaxLocalRawBrightness(blockpos));
                float brightness = (float) Math.max(Math.pow(getDimensionBrightnessAtEyes(camera.getEntity()), 2D), brightnessAtEyes);
                float fogRed = 0.6F * brightness;
                float fogGreen = 0.3F * brightness;
                float fogBlue = 0.0F;
                return new Vector3f(fogRed, fogGreen, fogBlue);
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(0.35f);
                RenderSystem.setShaderFogEnd(4);
            }
        });
    }

    public static float getDimensionBrightnessAtEyes(Entity entity) {
        float lightLevelAtEyes = entity.level().getRawBrightness(new BlockPos((int) entity.getEyePosition().x, (int) entity.getEyePosition().y, (int) entity.getEyePosition().z), 0);
        return lightLevelAtEyes / 15f;
    }
}