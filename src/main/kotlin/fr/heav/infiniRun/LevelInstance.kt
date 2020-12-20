package fr.heav.infiniRun

import com.extollit.linalg.mutable.Vec2d
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.BlockPosition
import net.minestom.server.utils.Position
import net.minestom.server.utils.time.TimeUnit
import net.minestom.server.world.DimensionType
import java.util.*
import kotlin.math.absoluteValue

class LevelInstance(seed: Long, start: BlockPosition, size: Int, dimensionType: DimensionType): InstanceContainer(UUID.randomUUID(), dimensionType, null) {
    val start = start.clone()
    val end: BlockPosition

    init {
        this.enableAutoChunkLoad(true)
        this.chunkGenerator = VoidChunkGenerator(lambsBiome)
        this.timeRate = 0
        this.time = 18000
        this.loadChunk(start.toPosition(), null)
        val parkourGenerator = ParkourGenerator(this, start, Vec2d(1.0, 0.0), seed)
        for (i in 0..size) {
            parkourGenerator.step(Block.BONE_BLOCK)
        }
        parkourGenerator.step(Block.GREEN_WOOL)
        end = parkourGenerator.getCurrentPosition()
        this.setBlock(start, Block.RED_WOOL)
    }

    override fun isInVoid(position: Position): Boolean {
        return position.y < 14
    }
}