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
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

public class CemeteryGenerator implements Generator {
    public static final float CHANCE = 0.4f;
    private static final Logger LOGGER = LogManager.getLogger();

    public static void addPieces(StructureManager manager, int x, int z, List<StructurePiece> pieces, Random random, DefaultFeatureConfig defaultConfig) {
        IntStream.range(0, random.nextInt(7) + 3).forEach(e -> {
            final int row = 10;
            final int distance = 5;
            final int noise = 3;
            if (random.nextFloat() < CHANCE) {
                final Piece cemetery = new Piece(manager, x + distance * (e % row) + random.nextInt(noise), z + distance * (e / row) + random.nextInt(noise), BiomeStructures.constructIdentifier("cemetery"));
                pieces.add(cemetery);
            } else {
                LOGGER.info(String.format("Rejected generation in %d ~ %d", x + distance * (e % row), z + distance * (e / row)));
            }
        });
    }

    public static class Piece extends SimpleStructurePiece {
        private final Identifier id;

        public Piece(StructureManager structureManager, int x, int z, Identifier identifier) {
            super(BiomeStructures.Generators.CEMETERY.getGenerator(), 0);
            this.set2dPosition(x, z);
            this.id = identifier;
            this.setStructureData(structureManager);
        }

        public Piece(StructureManager structureManager, CompoundTag compoundTag) {
            this(structureManager, compoundTag.getInt("PosX"), compoundTag.getInt("PosZ"), new Identifier(compoundTag.getString("Template")));
            this.setStructureData(structureManager);
        }

        public void setStructureData(StructureManager structureManager) {
            Structure structure = structureManager.getStructureOrBlank(this.id);
            StructurePlacementData structurePlacementData = (new StructurePlacementData()).setRotation(BlockRotation.NONE).setMirrored(BlockMirror.NONE).setPosition(pos).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
            this.setStructureData(structure, this.pos, structurePlacementData);
        }

        @Override
        protected void toNbt(CompoundTag tag) {
            super.toNbt(tag);
            tag.putString("Template", this.id.toString());
            tag.putInt("PosX", this.pos.getX());
            tag.putInt("PosZ", this.pos.getZ());
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos pos, IWorld world, Random random, BlockBox boundingBox) {

        }

        @Override
        public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
            int yHeight = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, this.pos.getX(), this.pos.getZ());
            this.pos = this.pos.add(0, yHeight - 1, 0);
            LOGGER.info(String.format("Generated cemetery at %d %d %d", this.pos.getX(), this.pos.getY(), this.pos.getZ()));
            return super.generate(world, generator, random, box, pos);
        }

        public void set2dPosition(int x, int z) {
            this.pos = Objects.isNull(this.pos) ? new BlockPos(x, 0, z) : new BlockPos(x, this.pos.getY(), z);
        }
    }
}
