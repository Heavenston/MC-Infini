package fr.heav.infiniRun

import com.extollit.linalg.mutable.Vec2d
import net.minestom.server.MinecraftServer
import net.minestom.server.chat.ChatColor
import net.minestom.server.chat.ColoredText
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.block.Block
import net.minestom.server.scoreboard.Sidebar
import net.minestom.server.utils.BlockPosition
import net.minestom.server.utils.Position
import net.minestom.server.utils.time.TimeUnit
import net.minestom.server.world.DimensionType
import net.minestom.server.world.biomes.Biome
import java.util.*
import kotlin.math.*

object PlayerLevelStore {
    private val playerLevels = mutableMapOf<UUID, Int>()

    fun store(player: Player, level: Int) {
        playerLevels[player.uuid] = level
    }
    fun load(player: Player): Int {
        return playerLevels.getOrDefault(player.uuid, 0)
    }
}

fun main() {
    val minecraftServer = MinecraftServer.init()

    val biomeManager = MinecraftServer.getBiomeManager();
    biomeManager.addBiome(lambsBiome)

    val transitionInstance = MinecraftServer.getInstanceManager().createInstanceContainer()
    transitionInstance.chunkGenerator = VoidChunkGenerator(Biome.PLAINS)
    transitionInstance.enableAutoChunkLoad(true)

    val globalEventHandler = MinecraftServer.getGlobalEventHandler()
    globalEventHandler.addEventCallback(
            PlayerLoginEvent::class.java
    ) { event: PlayerLoginEvent ->
        val player = event.player
        var levelIndex = PlayerLevelStore.load(player)

        event.setSpawningInstance(LevelManager.getLevel(levelIndex))
        player.respawnPoint = LevelManager.getLevel(levelIndex).start.toPosition().add(0f, 1f, 0f)
        player.gameMode = GameMode.ADVENTURE

        MinecraftServer.getSchedulerManager().buildTask {
            val position = player.position.toBlockPosition()
            val level = LevelManager.getLevel(levelIndex)
            if (position.x == level.end.x && (position.y-level.end.y).absoluteValue < 3 && position.z == level.end.z) {
                levelIndex += 1
                PlayerLevelStore.store(player, levelIndex)
                val newLevel = LevelManager.getLevel(levelIndex)
                newLevel.loadChunk(player.position, null)
                player.respawnPoint = newLevel.start.toPosition().add(0f, 1f, 0f)
                val newPos = player.respawnPoint.clone()
                newPos.yaw = player.position.yaw
                newPos.pitch = player.position.pitch
                newPos.x += player.position.x - floor(player.position.x)
                newPos.z += player.position.z - floor(player.position.z)
                player.setInstance(newLevel, newPos)
            }
        }.repeat(3, TimeUnit.TICK).schedule()
    }

    minecraftServer.start("0.0.0.0", 25565)
}