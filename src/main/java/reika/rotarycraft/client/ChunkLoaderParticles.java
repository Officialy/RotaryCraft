package reika.rotarycraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/** The original four counter-rotating pairs: rising, full-bright stars and ender pearls. */
public final class ChunkLoaderParticles {
    private ChunkLoaderParticles() {}
    public static void spawn(Level world, BlockPos pos, float phi) {
        if (!(world instanceof ClientLevel level)) return;
        var client = Minecraft.getInstance();
        for (int i = 0; i < 4; i++) {
            double radius = 0.375 - i * 0.0625;
            double height = 0.25 + i * 0.1875;
            for (int side = 0; side < 2; side++) {
                double angle = Math.toRadians((4 - i) * phi + 40 + side * 180);
                var state = new ItemStackRenderState();
                client.getItemModelResolver().updateForTopItem(state, new ItemStack(side == 0 ? Items.NETHER_STAR : Items.ENDER_PEARL), ItemDisplayContext.GROUND, level, null, 0);
                var material = state.pickParticleMaterial(level.getRandom());
                if (material != null) client.particleEngine.add(new Spark(level, pos.getX() + 0.5 + radius * Math.cos(angle),
                        pos.getY() + height, pos.getZ() + 0.5 + radius * Math.sin(angle), material.sprite()));
            }
        }
    }
    private static final class Spark extends SingleQuadParticle {
        Spark(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
            super(level, x, y, z, sprite);
            xd = 0; yd = 0.0625; zd = 0;
            lifetime = 60; gravity = 0; friction = 0.98F; hasPhysics = false;
            quadSize = 0;
        }
        @Override public void tick() {
            super.tick();
            quadSize = 0.0375F * (age <= lifetime / 12 ? age * 12F / lifetime : 1 - age / (float)lifetime);
        }
        @Override protected Layer getLayer() { return Layer.bySprite(sprite); }
        @Override protected int getLightCoords(float partialTick) { return 0xf000f0; }
    }
}
