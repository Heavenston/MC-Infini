package fr.heav.infiniRun

import net.minestom.server.entity.Player

object PlayerLevelStore {
    private val playerLevels = mutableMapOf<String, Int>()

    fun store(player: Player, level: Int) {
        playerLevels[player.username] = level
    }
    fun load(player: Player): Int {
        return playerLevels.getOrDefault(player.username, 0)
    }
}
