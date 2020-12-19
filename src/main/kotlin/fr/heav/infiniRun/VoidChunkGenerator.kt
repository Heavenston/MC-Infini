package fr.heav.infiniRun

import net.minestom.server.instance.ChunkGenerator
import net.minestom.server.instance.ChunkPopulator
import net.minestom.server.instance.batch.ChunkBatch
import net.minestom.server.world.biomes.Biome

class VoidChunkGenerator(val biome: Biome): ChunkGenerator {
    override fun generateChunkData(batch: ChunkBatch, chunkX: Int, chunkZ: Int) {
    }

    override fun fillBiomes(biomes: Array<Biome>, chunkX: Int, chunkZ: Int) {
        biomes.fill(biome)
    }

    override fun getPopulators(): MutableList<ChunkPopulator> {
        return mutableListOf<ChunkPopulator>()
    }
}