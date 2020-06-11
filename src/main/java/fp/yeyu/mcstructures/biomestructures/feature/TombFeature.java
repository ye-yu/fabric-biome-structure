package fp.yeyu.mcstructures.biomestructures.feature;

import com.mojang.datafixers.Dynamic;
import fp.yeyu.mcstructures.biomestructures.BiomeStructures;
import fp.yeyu.mcstructures.biomestructures.generator.TombGenerator;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.AbstractTempleFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.function.Function;

public class TombFeature extends AbstractTempleFeature<DefaultFeatureConfig> {
    public TombFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    protected int getSeedModifier() {
        return 1;
    }

    @Override
    public StructureStartFactory getStructureStartFactory() {
        return Start::new;
    }

    @Override
    public String getName() {
        return "Tomb";
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
            DefaultFeatureConfig defaultFeatureConfig = chunkGenerator.getStructureConfig(biome, BiomeStructures.Structures.CEMETERY.getFeature());
            TombGenerator.addParts(structureManager, new BlockPos(x * 16, 0, z * 16), BlockRotation.NONE, this.children, this.random, defaultFeatureConfig);
            this.setBoundingBoxFromChildren();
        }
    }
}
