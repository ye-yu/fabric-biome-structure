package fp.yeyu.mcstructures.biomestructures.generator;

import fp.yeyu.mcstructures.biomestructures.BiomeStructures;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
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

public class TombGenerator {
    public static final Identifier id = BiomeStructures.constructIdentifier("tomb");
    // 3926815997070620247
    // 80 23 -224
    private static final Logger LOGGER = LogManager.getLogger();

    public static void addParts(StructureManager structureManager, BlockPos blockPos, BlockRotation rotation, List<StructurePiece> list_1, Random random, DefaultFeatureConfig defaultFeatureConfig) {
        list_1.add(new Piece(structureManager, id, blockPos, rotation));
    }

    public static class Piece extends SimpleStructurePiece {
        private static final String FLOWER_POT = "flower_pot";
        private static final String CHEST = "chest";
        private static final String SPAWNER = "spawner";
        private static final Block[] FLOWERS = new Block[]{
                Blocks.POTTED_ALLIUM,
                Blocks.POTTED_AZURE_BLUET,
                Blocks.POTTED_BROWN_MUSHROOM,
                Blocks.POTTED_CORNFLOWER,
                Blocks.POTTED_LILY_OF_THE_VALLEY};
        private static final EntityType<?>[] MOB_SPAWNER_ENTITIES = new EntityType[]{
                EntityType.SKELETON,
                EntityType.ZOMBIE,
                EntityType.SPIDER,
                EntityType.WITCH,
                EntityType.ENDERMAN
        };
        private final BlockRotation rotation;
        private final Identifier identifier;
        private Integer spawnerType;

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
            final int structureHeight = this.boundingBox.maxY - this.boundingBox.minY;
            final int offset = 0;

            this.pos = this.pos.add(0, yHeight <= (structureHeight + offset) ? offset : random.nextInt(yHeight - structureHeight - offset) + offset, 0);
            LOGGER.info(String.format("Generated tomb at %d %d %d", this.pos.getX(), this.pos.getY(), this.pos.getZ()));
            if (super.generate(world, generator, random, box, pos)) {
                replaceRightStoneWallsWithAir(world);
                replaceLeftStoneWallsWithAir(world);
                replaceTopStoneRoofsWithAir(world);
                return true;
            }
            return false;
        }

        private void replaceTopStoneRoofsWithAir(IWorld world) {
            final int height = this.boundingBox.maxY - this.boundingBox.minY;
            final int width = this.boundingBox.maxX - this.boundingBox.minX;
            final int breadth = this.boundingBox.maxZ - this.boundingBox.minZ;

            final int y = this.pos.getY() + height;

            for (int x = this.pos.getX(); x <= this.pos.getX() + width; x++) {
                for (int z = this.pos.getZ(); z <= this.pos.getZ() + breadth; z++) {
                    final BlockPos blockPos = new BlockPos(x, y, z);
                    if (!world.getBlockState(blockPos.add(0, 1, 0)).isAir()) continue;
                    world.setBlockState(blockPos, Blocks.CAVE_AIR.getDefaultState(), 3);
                    world.setBlockState(blockPos.add(0, -1, 0), Blocks.CAVE_AIR.getDefaultState(), 3);
                }
            }

        }

        private void replaceLeftStoneWallsWithAir(IWorld world) {
            final int height = this.boundingBox.maxY - this.boundingBox.minY;
            final int width = this.boundingBox.maxX - this.boundingBox.minX;
            final int breadth = this.boundingBox.maxZ - this.boundingBox.minZ;

            final int x = this.pos.getX() + width;

            for (int z = this.pos.getZ(); z <= this.pos.getZ() + breadth; z++) {
                for (int y = this.pos.getY(); y <= this.pos.getY() + height; y++) {
                    final BlockPos blockPos = new BlockPos(x, y, z);
                    if (!world.getBlockState(blockPos.add(1, 0, 0)).isAir()) continue;
                    world.setBlockState(blockPos, Blocks.CAVE_AIR.getDefaultState(), 3);
                }
            }
        }

        private void replaceRightStoneWallsWithAir(IWorld world) {
            final int height = this.boundingBox.maxY - this.boundingBox.minY;
            final int width = this.boundingBox.maxX - this.boundingBox.minX;
            final int breadth = this.boundingBox.maxZ - this.boundingBox.minZ;

            final int x = this.pos.getX();

            for (int z = this.pos.getZ(); z <= this.pos.getZ() + breadth; z++) {
                for (int y = this.pos.getY(); y <= this.pos.getY() + height; y++) {
                    final BlockPos blockPos = new BlockPos(x, y, z);
                    if (!world.getBlockState(blockPos.add(-1, 0, 0)).isAir()) continue;
                    world.setBlockState(blockPos, Blocks.CAVE_AIR.getDefaultState(), 3);
                }
            }
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos pos, IWorld world, Random random, BlockBox boundingBox) {
            if (Objects.isNull(spawnerType)) {
                spawnerType = random.nextInt(FLOWERS.length);
            }
            if (metadata.equalsIgnoreCase(FLOWER_POT)) {
                world.setBlockState(pos, FLOWERS[spawnerType].getDefaultState(), 3);
                return;
            }

            if (metadata.equalsIgnoreCase(CHEST)) {
                world.setBlockState(pos, Blocks.CHEST.getDefaultState(), 3);
                LootableContainerBlockEntity.setLootTable(world, random, pos, BiomeStructures.constructIdentifier("tomb_chest"));
                return;
            }

            if (metadata.equalsIgnoreCase(SPAWNER)) {
                world.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 3);
                final BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof MobSpawnerBlockEntity) {
                    ((MobSpawnerBlockEntity) blockEntity).getLogic().setEntityId(MOB_SPAWNER_ENTITIES[spawnerType]);
                } else {
                    LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", pos.getX(), pos.getY(), pos.getZ());
                }
            }
        }
    }
}