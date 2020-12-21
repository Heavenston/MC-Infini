package fr.heav.infiniRun

import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.BlockPosition
import net.minestom.server.utils.Position
import net.minestom.server.world.DimensionType
import java.util.*

class LevelInstance(parkourConfig: ParkourGeneratorConfig, size: Int, dimensionType: DimensionType): InstanceContainer(UUID.randomUUID(), dimensionType, null) {
    val start = parkourConfig.startPosition.clone()
    val end: BlockPosition

    init {
        this.enableAutoChunkLoad(true)
        this.chunkGenerator = VoidChunkGenerator(lambsBiome)
        this.timeRate = 0
        this.time = 18000
        this.loadChunk(start.toPosition(), null)

        val parkourGenerator = ParkourGenerator(this, parkourConfig)
        for (i in 0..size) {
            parkourGenerator.step(Block.BONE_BLOCK)
        }
        parkourGenerator.step(Block.GREEN_WOOL)

        end = parkourGenerator.getCurrentPosition()
        this.setBlock(start, Block.RED_WOOL)
    }

    override fun isInVoid(position: Position): Boolean {
        return false
    }
}