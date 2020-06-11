package fp.yeyu.mcstructures.biomestructures.generator;

import fp.yeyu.mcstructures.biomestructures.BiomeStructures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;

public class TombGenerator {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final Identifier id = BiomeStructures.constructIdentifier("tomb");

    public static void addParts(StructureManager structureManager, BlockPos blockPos, BlockRotation rotation, List<StructurePiece> list_1, Random random, DefaultFeatureConfig defaultFeatureConfig) {
        list_1.add(new Piece(structureManager, id, blockPos, rotation));
    }

    public static class Piece extends SimpleStructurePiece {
        private final BlockRotation rotation;
        private final Identifier identifier;

        public Piece(StructureManager structureManager_1, CompoundTag compoundTag_1) {
            super(BiomeStructures.Structures.TOMB.getGenerator(), compoundTag_1);

            this.identifier = new Identifier(compoundTag_1.getString("Template"));
            this.rotation = BlockRotation.valueOf(compoundTag_1.getString("Rot"));

            this.setStructureData(structureManager_1);
        }

        public Piece(StructureManager structureManager, Identifier identifier, BlockPos pos, BlockRotation rotation) {
            super(BiomeStructures.Structures.TOMB.getGenerator(), 94);

            this.rotation = rotation;
            this.identifier = identifier;
            this.pos = pos;

            this.setStructureData(structureManager);
        }

        public void setStructureData(StructureManager structureManager) {
            Structure structure_1 = structureManager.getStructureOrBlank(this.identifier);
            StructurePlacementData structurePlacementData_1 = (new StructurePlacementData()).setRotation(this.rotation).setMirrored(BlockMirror.NONE).setPosition(pos).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
            this.setStructureData(structure_1, this.pos, structurePlacementData_1);
        }

        @Override
        public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
            int yHeight = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, this.pos.getX() + 8, this.pos.getZ() + 8);
            this.pos = this.pos.add(0, yHeight - 1, 0);
            LOGGER.info(String.format("Generated tomb at %d %d %d", this.pos.getX(), this.pos.getY(), this.pos.getZ()));
            return  super.generate(world, generator, random, box, pos);
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos pos, IWorld world, Random random, BlockBox boundingBox) {
            LOGGER.info(String.format("Procesing %s", metadata));
        }
    }
}