/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import reika.dragonapi.libraries.level.LegacyOreVeins;
import reika.rotarycraft.registry.ConfigRegistry;

/** Original optional extra-iron scatter; vanilla continues supplying the base distribution. */
public record ExtraIronGenerator(int baseCount, int size, int minY, int maxY) implements Feature {
    public static final MapCodec<ExtraIronGenerator> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.intRange(1,1024).fieldOf("base_count").forGetter(ExtraIronGenerator::baseCount),
            Codec.intRange(1,128).fieldOf("size").forGetter(ExtraIronGenerator::size),
            Codec.INT.fieldOf("min_y").forGetter(ExtraIronGenerator::minY),
            Codec.INT.fieldOf("max_y").forGetter(ExtraIronGenerator::maxY)
    ).apply(i,ExtraIronGenerator::new));
    @Override public MapCodec<ExtraIronGenerator> codec() { return CODEC; }
    public int passes(float factor) { return factor>1 ? (int)(baseCount*(factor-1)) : 0; }
    @Override public boolean place(WorldGenLevel world, ChunkGenerator generator, RandomSource random, BlockPos origin) {
        int passes=passes(ConfigRegistry.EXTRAIRON.getFloat()); boolean placed=false;
        for (int i=0;i<passes;i++) {
            int x=origin.getX()+random.nextInt(16),z=origin.getZ()+random.nextInt(16),y=minY+random.nextInt(maxY-minY+1);
            placed |= LegacyOreVeins.place(world,random,Blocks.IRON_ORE.defaultBlockState(),size,0,x,y,z);
        }
        return placed;
    }
}
