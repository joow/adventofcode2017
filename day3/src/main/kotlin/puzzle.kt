import Square.Companion.ACCESS_PORT
import kotlin.math.abs

fun grid(size: Int) = generateSequence(ACCESS_PORT) { it.next() }.take(size)

data class Position(val x: Int, val y: Int) {
    fun steps(pos: Position) = abs(x - pos.x) + abs(y - pos.y)

    fun isAdjacent(pos: Position) = abs(x - pos.x) <= 1 && abs(y - pos.y) <= 1
}

data class Square(val pos: Position, val advance: Advance) {
    companion object {
        val ACCESS_PORT = Square(Position(0, 0), Advance(Direction.RIGHT, 0, 1))
    }

    fun next() = advance.next(pos)

    fun steps() = pos.steps(ACCESS_PORT.pos)

    fun isAdjacent(square: Square) = pos.isAdjacent(square.pos)
}

enum class Direction {
    RIGHT {
        override fun next() = UP
        override fun turn() = 0
    },
    UP {
        override fun next() = LEFT
        override fun turn() = 1
    },
    LEFT {
        override fun next() = DOWN
        override fun turn() = 0
    },
    DOWN {
        override fun next() = RIGHT
        override fun turn() = 1
    };

    abstract fun next(): Direction
    abstract fun turn(): Int
}

class Advance(val direction: Direction, val curr: Int, val count: Int) {
    fun next(pos: Position): Square {
        val newPos = when (direction) {
            Direction.RIGHT -> pos.copy(x = pos.x + 1)
            Direction.UP -> pos.copy(y = pos.y + 1)
            Direction.LEFT -> pos.copy(x = pos.x - 1)
            Direction.DOWN -> pos.copy(y = pos.y - 1)
        }

        val newAdvance = nextAdvance()

        return Square(newPos, newAdvance)
    }

    private fun nextAdvance() =
            if (curr == count - 1) Advance(direction.next(), 0, count + direction.turn())
            else Advance(direction, curr + 1, count)
}

fun sums(grid: Sequence<Square>): List<Pair<Square, Int>> {
    val sums = mutableListOf<Pair<Square, Int>>()

    for (i in 0 until grid.count()) {
        val square = grid.drop(i).first()
        val sum =
                if (i == 0) 1
                else sums
                        .take(i)
                        .filter { (sq, _) -> sq.isAdjacent(square) }
                        .map { it.second }
                        .sum()
        sums.add(Pair(square, sum))
    }

    return sums
}