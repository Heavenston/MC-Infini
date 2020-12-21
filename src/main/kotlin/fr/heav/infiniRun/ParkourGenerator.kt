package fr.heav.infiniRun

import com.extollit.linalg.mutable.Vec2d
import net.minestom.server.instance.BlockModifier
import net.minestom.server.instance.Instance
import net.minestom.server.instance.batch.BlockBatch
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.BlockPosition
import kotlin.math.*
import kotlin.random.Random

private fun rotate2DVec(vec: Vec2d, angle: Double) {
    val ca = cos(angle)
    val sa = sin(angle)
    val x = vec.x
    val y = vec.y
    vec.x = ca * x - sa * y
    vec.y = sa * x + ca * y
}

data class ParkourGeneratorConfig (
    val seed: Long,
    val startPosition: BlockPosition,
    val direction: Vec2d,
    val heightChange: Boolean,
    val fiveBlockDistanceProbability: Double = 0.0,
)

class ParkourGenerator(val blockModifier: BlockModifier, val config: ParkourGeneratorConfig) {
    private val currentPosition: BlockPosition = BlockPosition(
        config.startPosition.x,
        config.startPosition.y,
        config.startPosition.z,
    )
    private val direction: Vec2d = Vec2d(config.direction)
    private var currentRotation: Double = 0.0
    private val random = Random(config.seed)

    fun step(block: Block) {
        currentRotation += random.nextDouble(-PI / 10, PI / 10)
        currentRotation = min(currentRotation, PI / 2.5)
        currentRotation = max(currentRotation, -PI / 2.5)

        val move = Vec2d(direction)
        rotate2DVec(move, currentRotation)
        move.normalize()

        val heightChange = Random.nextInt(0, if (config.heightChange) 2 else 1)
        val fiveBlockDistance = Random.nextFloat() < config.fiveBlockDistanceProbability
        val gapSize = if (heightChange == 1) {
            random.nextDouble(
                    3.0,
                    if (fiveBlockDistance) 4.5 else 4.0
            )
        }
        else {
            random.nextDouble(
                    3.0,
                    if (fiveBlockDistance) 6.0 else 5.0
            )
        }
        move.mul(gapSize)
        currentPosition.x += move.x.toInt()
        currentPosition.z += move.y.toInt()
        currentPosition.y += heightChange

        blockModifier.setBlock(currentPosition, block)
    }

    fun getCurrentPosition(): BlockPosition {
        return this.currentPosition
    }
}