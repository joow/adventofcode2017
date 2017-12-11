import kotlin.math.abs

fun steps(path: String): Int {
    tailrec fun follow(hex: Hex, directions: List<String>): Hex {
        if (directions.isEmpty()) return hex
        return follow(hex.move(directions.first()), directions.drop(1))
    }

    val directions = path.split(",")
    val destination = follow(Hex.ORIGIN, directions)

    return destination.distance(Hex.ORIGIN)
}

fun furthest(path: String): Int {
    val directions = path.split(",")

    var max = 0
    var currentPosition = Hex.ORIGIN
    for (direction in directions) {
        currentPosition = currentPosition.move(direction)
        val steps = currentPosition.distance(Hex.ORIGIN)
        if (steps > max) max = steps
    }

    return max
}

data class Hex(val x: Int, val y: Int) {
    val z = -(x + y)

    fun move(direction: String) = when(direction) {
        "n" -> copy(x, y + 1)
        "ne" -> copy(x + 1, y)
        "se" -> copy(x + 1, y - 1)
        "s" -> copy(x, y - 1)
        "sw" -> copy(x - 1, y)
        "nw" -> copy(x - 1, y + 1)
        else -> throw RuntimeException("unknown direction [$direction]")
    }

    fun distance(hex: Hex) = (abs(x - hex.x) + abs(y - hex.y) + abs(z - hex.z)) / 2

    companion object {
        val ORIGIN = Hex(0, 0)
    }
}