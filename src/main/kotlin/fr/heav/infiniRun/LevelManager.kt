package fr.heav.infiniRun

import com.extollit.linalg.mutable.Vec2d
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.BlockPosition
import net.minestom.server.utils.time.TimeUnit
import kotlin.math.max

object LevelManager {
    private val levels = mutableListOf<LevelInstance>()

    fun getLevel(i: Int): LevelInstance {
        if (levels.size > i)
            return levels[i]

        val parkourConfig = ParkourGeneratorConfig(
            seed = i.toLong(),
            startPosition = BlockPosition(1000 * i, 15, 0),
            direction = Vec2d(1.0, 0.0),
            heightChange = i >= 5,
            fiveBlockDistanceProbability = (max(i.toDouble() - 5.0, 0.0) / 10.0)
        )

        val instance = LevelInstance(
            parkourConfig,
            (i / 2) + 5,
            lambsDimensionType[i % lambsDimensionType.size]
        )
        MinecraftServer.getInstanceManager().registerInstance(instance)
        levels.add(i, instance)
        return instance
    }
}