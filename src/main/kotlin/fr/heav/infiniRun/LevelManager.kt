package fr.heav.infiniRun

import net.minestom.server.MinecraftServer
import net.minestom.server.utils.BlockPosition
import net.minestom.server.utils.time.TimeUnit

object LevelManager {
    private val levels = mutableListOf<LevelInstance>()

    fun getLevel(i: Int): LevelInstance {
        if (levels.size > i)
            return levels[i]
        val instance = LevelInstance(
                i.toLong(),
                BlockPosition(i * 1000, 15, 0),
                (i / 2) + 5
        )
        MinecraftServer.getInstanceManager().registerInstance(instance)
        levels.add(i, instance)
        return instance
    }
}