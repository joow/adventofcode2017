import kotlin.math.abs

fun parse(s: String) =
        s.split("\n").filter { it.isNotBlank() }.mapIndexed { index, p -> Particle.parse(p, index) }

fun tick(particle: Particle, ticks: Int = 1) = generateSequence(particle) { it.tick() }.drop(1).take(ticks).last()

fun closest(s: String, ticks: Int = 10) =
    parse(s).map { tick(it, ticks) }.sortedBy { it.distance() }.first().order

fun collide(s: String, ticks: Int = 10): List<Particle> {
    var articles = parse(s)

    for (i in 1..ticks) {
        articles = articles.map { it.tick() }.groupBy { it.position }.filterValues { it.size == 1 }.map { it.value.first() }
    }

    return articles
}

class Particle(val position: Vector, val velocity: Vector, val acceleration: Vector, val order: Int) {

    fun tick(): Particle {
        val newVelocity = Vector(velocity.x + acceleration.x, velocity.y + acceleration.y, velocity.z + acceleration.z)
        val newPosition = Vector(position.x + newVelocity.x, position.y + newVelocity.y, position.z + newVelocity.z)

        return Particle(newPosition, newVelocity, acceleration, order)
    }

    fun distance(): Long = position.run { abs(x) + abs(y) + abs(z) }

    companion object {
        fun parse(s: String, order: Int = 0): Particle {
            val position = parseCoordinate(
                    Regex("p=<(.*)>").find(s)?.groups?.first()?.value ?: throw RuntimeException("no position found"))
            val velocity = parseCoordinate(
                    Regex("v=<(.*)>").find(s)?.groups?.first()?.value ?: throw RuntimeException("no position found"))
            val acceleration = parseCoordinate(
                    Regex("a=<(.*)>").find(s)?.groups?.first()?.value ?: throw RuntimeException("no position found"))

            return Particle(position, velocity, acceleration, order)
        }

        private fun parseCoordinate(s: String): Vector {
            val coordinate = s.substringAfter("<").substringBefore(">").split(",")
            return Vector(coordinate[0].trim().toLong(), coordinate[1].trim().toLong(), coordinate[2].trim().toLong())
        }
    }
}

data class Vector(val x: Long, val y: Long, val z: Long)