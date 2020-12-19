package fr.heav.infiniRun

import com.extollit.linalg.mutable.Vec2d
import net.minestom.server.MinecraftServer
import net.minestom.server.chat.ChatColor
import net.minestom.server.chat.ColoredText
import net.minestom.server.entity.GameMode
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
import java.util.*
import kotlin.math.*

class JumpLevel(val n: Int): InstanceContainer(UUID.randomUUID(), DimensionType.OVERWORLD, null) {
    val start = BlockPosition(0, 15, 0)
    val end: BlockPosition

    init {
        this.enableAutoChunkLoad(true)
        this.chunkGenerator = VoidChunkGenerator(lambsBiome)
        this.timeRate = 0
        this.time = 18000

        val parkourGenerator = ParkourGenerator(this, start, Vec2d(1.0, 0.0), n.toLong())
        for (i in 0..10) {
            parkourGenerator.step(Block.BONE_BLOCK)
        }
        parkourGenerator.step(Block.GREEN_WOOL)
        end = parkourGenerator.getCurrentPosition()
        this.setBlock(start, Block.GREEN_WOOL)
    }

    override fun isInVoid(position: Position): Boolean {
        return position.y < 14
    }
}

fun main() {
    val playerLevels = hashMapOf<UUID, Int>()

    // Initialization
    val minecraftServer = MinecraftServer.init()

    val biomeManager = MinecraftServer.getBiomeManager();
    biomeManager.addBiome(lambsBiome)

    val schedulerManager = MinecraftServer.getSchedulerManager()

    val levels = mutableListOf<JumpLevel>()
    fun getLevel(n: Int): JumpLevel {
        if (levels.size <= n) {
            val instance = JumpLevel(n)
            MinecraftServer.getInstanceManager().registerInstance(instance)
            levels.add(instance)
        }
        return levels[n]
    }

    val globalEventHandler = MinecraftServer.getGlobalEventHandler()
    globalEventHandler.addEventCallback(
            PlayerLoginEvent::class.java
    ) { event: PlayerLoginEvent ->
        val player = event.player
        val currentLevel = playerLevels.getOrDefault(player.uuid, 0)
        event.setSpawningInstance(getLevel(currentLevel))
        val p = getLevel(currentLevel).start.toPosition()
        p.y += 1
        player.respawnPoint = p

        player.gameMode = GameMode.ADVENTURE
        //player.isFlying = true
        //player.isAllowFlying = true
        //player.flyingSpeed = 0.5F
    }

    globalEventHandler.addEventCallback(PlayerSpawnEvent::class.java) { event ->
        if (event.isFirstSpawn) {
            val player = event.player
            var currentLevel = playerLevels.getOrDefault(player.uuid, 0)
            val sidebar = Sidebar("Infini Run")
            sidebar.createLine(Sidebar.ScoreboardLine("level", ColoredText.of(ChatColor.CYAN, "Level ").append(ChatColor.WHITE, "${currentLevel + 1}"), 1))
            sidebar.createLine(Sidebar.ScoreboardLine("distanceToEnd", ColoredText.of("Distance from finish line: 0"), 0))
            sidebar.addViewer(player)
            val task = schedulerManager.buildTask {
                val ppos = player.position.toBlockPosition()
                val targetPos = getLevel(currentLevel).end
                if (ppos.x == targetPos.x && ppos.z == targetPos.z) {
                    currentLevel += 1
                    val level = getLevel(currentLevel)
                    player.sendActionBarMessage(ColoredText.of(
                            ChatColor.BRIGHT_GREEN,
                            "Congrats on passing level ${level.n} ! Welcome to level ${level.n + 1}",
                    ))
                    sidebar.updateLineContent("level", ColoredText.of(ChatColor.CYAN, "Level ").append(ChatColor.WHITE, "${currentLevel + 1}"))
                    player.setInstance(level, level.start.toPosition().clone().add(0f, 1f, 0f))
                    schedulerManager.buildTask {
                        player.respawn()
                    }.delay(2, TimeUnit.TICK).schedule()
                    playerLevels[player.uuid] = currentLevel
                }
                else {
                    val level = getLevel(currentLevel)
                    sidebar.updateLineContent(
                        "distanceToEnd",
                        ColoredText.of(ChatColor.CYAN, "Distance from finish line: ")
                                .append(ChatColor.WHITE, "${level.end.toPosition().getDistance(player.position).roundToInt()}")
                    )
                }
            }.repeat(5, TimeUnit.TICK).schedule()
            globalEventHandler.addEventCallback(PlayerDisconnectEvent::class.java) { event ->
                task.cancel()
            }
        }

    }

    minecraftServer.start("0.0.0.0", 25565)
}