package fp.yeyu.mcstructures.biomestructures;

import fp.yeyu.mcstructures.biomestructures.feature.CemeteryFeature;
import fp.yeyu.mcstructures.biomestructures.feature.TombFeature;
import fp.yeyu.mcstructures.biomestructures.generator.CemeteryGenerator;
import fp.yeyu.mcstructures.biomestructures.generator.TombGenerator;
import net.fabricmc.api.ModInitializer;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.EmeraldOreDecorator;
import net.minecraft.world.gen.decorator.NopeDecoratorConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BiomeStructures implements ModInitializer {
    public static final String NAMESPACE = "biomestructures";
    private static final Logger LOGGER = LogManager.getLogger();

    static {
        for (Biome biome : Registry.BIOME) {
            if (biome.getCategory() != Biome.Category.OCEAN && biome.getCategory() != Biome.Category.RIVER) {

                // add cemetery
                biome.addStructureFeature((Structures.CEMETERY.getStructure()).configure(FeatureConfig.DEFAULT));
                biome.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES, Structures.CEMETERY.getFeature().configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.NOPE.configure(new NopeDecoratorConfig())));

                // add tomb
                biome.addStructureFeature((Structures.TOMB.getStructure()).configure(FeatureConfig.DEFAULT));
                biome.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, Structures.TOMB.getFeature().configure(FeatureConfig.DEFAULT).createDecoratedFeature(Decorator.NOPE.configure(new NopeDecoratorConfig())));
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

    public enum Structures {
        CEMETERY("cemetery", new CemeteryFeature(DefaultFeatureConfig::deserialize), CemeteryGenerator.Piece::new),
        TOMB("tomb", new TombFeature(DefaultFeatureConfig::deserialize), TombGenerator.Piece::new);

        private final StructureFeature<DefaultFeatureConfig> feature;
        private final StructureFeature<DefaultFeatureConfig> structure;
        private final StructurePieceType generator;

        Structures(String structureName, StructureFeature<DefaultFeatureConfig> entry, StructurePieceType structurePieceType) {
            final Identifier featureId = constructIdentifier(structureName + "_feature");
            final Identifier structureId = constructIdentifier(structureName + "_structure");
            final Identifier generatorId = constructIdentifier(structureName + "_generator");
            feature = Registry.register(Registry.FEATURE, featureId, entry);
            structure = Registry.register(Registry.STRUCTURE_FEATURE, structureId, entry);
            generator = Registry.register(Registry.STRUCTURE_PIECE, generatorId, structurePieceType);
            Feature.STRUCTURES.put(structureName, entry);
        }

        public StructureFeature<DefaultFeatureConfig> getFeature() {
            return feature;
        }

        public StructureFeature<DefaultFeatureConfig> getStructure() {
            return structure;
        }

        public StructurePieceType getGenerator() {
            return generator;
        }
    }
}
