package fr.heav.infiniRun

import net.minestom.server.entity.Player
import org.apache.logging.log4j.core.util.Integers

object PlayerLevelStore {

    private fun getPlayerLeveKey(player: Player): String {
        return "${player.username}:level"
    }

    fun store(player: Player, level: Int) {
        val jedis = jedisPool.resource
        jedis.set(getPlayerLeveKey(player), level.toString())
        jedis.close()
    }
    fun load(player: Player): Int {
        val jedis = jedisPool.resource
        if (!jedis.exists(getPlayerLeveKey(player))) {
            return 0
        }
        val level = Integers.parseInt(jedis.get(getPlayerLeveKey(player)))
        jedis.close()
        return level
    }
    fun increment(player: Player): Int {
        val jedis = jedisPool.resource
        val level = jedis.incr(getPlayerLeveKey(player))
        jedis.close()
        return level.toInt()
    }
}
