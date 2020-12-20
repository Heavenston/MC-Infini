package fr.heav.infiniRun

import com.extollit.linalg.mutable.Vec2d
import net.minestom.server.instance.Instance
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

class ParkourGenerator(val instance: Instance, startPosition: BlockPosition, direction: Vec2d = Vec2d(1.0, 0.0), seed: Long = Random.nextLong()) {
    private val currentPosition: BlockPosition = BlockPosition(
            startPosition.x,
            startPosition.y,
            startPosition.z,
    )
    private val direction: Vec2d = Vec2d(1.0, 0.0)
    private var currentRotation: Double = 0.0
    private val random = Random(seed)

    fun step(block: Block) {
        currentRotation += random.nextDouble(-PI / 10, PI / 10)
        currentRotation = min(currentRotation, PI / 2.5)
        currentRotation = max(currentRotation, -PI / 2.5)

        val move = Vec2d(direction)
        rotate2DVec(move, currentRotation)
        move.normalize()
        move.mul(random.nextDouble(3.0, 5.0))
        currentPosition.x += move.x.toInt()
        currentPosition.z += move.y.toInt()

        instance.setBlock(currentPosition, block)
    }

    fun getCurrentPosition(): BlockPosition {
        return this.currentPosition
    }
}