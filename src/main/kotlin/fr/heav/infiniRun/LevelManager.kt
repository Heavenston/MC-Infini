package fr.heav.infiniRun

import net.minestom.server.MinecraftServer
import net.minestom.server.utils.BlockPosition
import net.minestom.server.utils.time.TimeUnit

object LevelManager {
    private val levels = mutableListOf<LevelInstance>()

    fun getLevel(i: Int): LevelInstance {
        if (levels.size > i)
            return levels[i]
        val start: BlockPosition = if (i == 0) {
            BlockPosition(0, 15, 0)
        }
        else {
            getLevel(i - 1).end.clone()
        }
        val instance = LevelInstance(
                i.toLong(),
                start,
                (i / 2) + 3
        )
        MinecraftServer.getInstanceManager().registerInstance(instance)
        levels.add(i, instance)
        return instance
    }
}