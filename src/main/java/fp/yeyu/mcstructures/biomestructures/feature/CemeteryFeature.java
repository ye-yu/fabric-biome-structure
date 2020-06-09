package fp.yeyu.mcstructures.biomestructures.feature;

import com.mojang.datafixers.Dynamic;
import fp.yeyu.mcstructures.biomestructures.BiomeStructures;
import fp.yeyu.mcstructures.biomestructures.generator.CemeteryGenerator;
import net.minecraft.structure.IglooGenerator;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.VillageStructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.AbstractTempleFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IglooFeature;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;
import java.util.function.Function;

public class CemeteryFeature extends AbstractTempleFeature<DefaultFeatureConfig> {
    public CemeteryFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    protected int getSeedModifier() {
        return 885885221;
    }

    @Override
    public StructureStartFactory getStructureStartFactory() {
        return Start::new;
    }

    @Override
    public String getName() {
        return "Cemetery";
    }

    @Override
    public int getRadius() {
        return 5;
    }

    public static class Start extends StructureStart {

        public Start(StructureFeature<?> structureFeature, int chunkX, int chunkZ, BlockBox blockBox, int reference, long seed) {
            super(structureFeature, chunkX, chunkZ, blockBox, reference, seed);
        }

        @Override
        public void initialize(ChunkGenerator<?> chunkGenerator, StructureManager structureManager, int x, int z, Biome biome) {
            DefaultFeatureConfig defaultFeatureConfig = chunkGenerator.getStructureConfig(biome, BiomeStructures.Features.CEMETERY.getFeature());
            CemeteryGenerator.addPieces(structureManager, x * 16, z * 16, this.children, this.random, defaultFeatureConfig);
            this.setBoundingBoxFromChildren();
        }
    }
}
