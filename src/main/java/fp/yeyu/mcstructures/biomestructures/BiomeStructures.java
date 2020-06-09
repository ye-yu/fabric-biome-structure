package fp.yeyu.mcstructures.biomestructures;

import fp.yeyu.mcstructures.biomestructures.feature.CemeteryFeature;
import fp.yeyu.mcstructures.biomestructures.generator.CemeteryGenerator;
import net.fabricmc.api.ModInitializer;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BiomeStructures implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String NAMESPACE = "biomestructures";

    public enum Generators {
        CEMETERY(constructIdentifier("cemetery_generator"), CemeteryGenerator.Piece::new);
        private final StructurePieceType generator;

        Generators(Identifier id, StructurePieceType structurePieceType) {
            this.generator = Registry.register(Registry.STRUCTURE_PIECE, id, structurePieceType);
        }

        public StructurePieceType getGenerator() {
            return this.generator;
        }
    }

    public enum Features {
        CEMETERY(constructIdentifier("cemetery_feature"), constructIdentifier("cemetery_structure"), "cemetery", new CemeteryFeature(DefaultFeatureConfig::deserialize));

        private final StructureFeature<DefaultFeatureConfig> feature;
        private final StructureFeature<DefaultFeatureConfig> structure;

        Features(Identifier featureId, Identifier structureId, String structureName, StructureFeature<DefaultFeatureConfig> entry) {
            feature = Registry.register(Registry.FEATURE, featureId, entry);
            structure = Registry.register(Registry.STRUCTURE_FEATURE, structureId, entry);
            Feature.STRUCTURES.put(structureName, entry);
        }

        public StructureFeature<DefaultFeatureConfig> getFeature() {
            return feature;
        }

        public StructureFeature<DefaultFeatureConfig> getStructure() {
            return structure;
        }
    }

    static {
        for(Biome biome : Registry.BIOME) {
            if(biome.getCategory() != Biome.Category.OCEAN && biome.getCategory() != Biome.Category.RIVER) {
                biome.addStructureFeature((Features.CEMETERY.getStructure()).configure(new DefaultFeatureConfig()));
                biome.addFeature(
                        GenerationStep.Feature.SURFACE_STRUCTURES,
                        Features.CEMETERY.getFeature().configure(FeatureConfig.DEFAULT).createDecoratedFeature(
                                Decorator.CHANCE_HEIGHTMAP_DOUBLE.configure(new ChanceDecoratorConfig(0))));
            }
        }
    }


    public static Identifier constructIdentifier(String nameItem) {
        return new Identifier(NAMESPACE, nameItem);
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Mod is loaded.");
    }
}
