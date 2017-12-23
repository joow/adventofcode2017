class Map(s: String) {
    val nodes = mutableMapOf<Position, Char>()

    init {
        val rows = s.split("\n")
        for (y in 0 until rows.size) {
            for (x in 0 until rows[y].length) {
                nodes.put(Position(x, y), rows[y][x])
            }
        }
    }

    fun center(): Position {
        val max = nodes.keys.maxBy { it.x + it.y } ?: throw RuntimeException("no center found")
        return Position(max.x / 2, max.y / 2)
    }
}

data class Position(val x: Int, val y: Int)

class Carrier(private val map: Map) {
    var position = map.center()
    var direction = Direction.UP
    var infected = 0

    fun move() {
        val currentState = map.nodes.getOrDefault(position, '.')
        val nextState = when (currentState) {
            '.' -> '#'
            '#' -> '.'
            else -> throw RuntimeException("unknown state $currentState")
        }

        direction = when (currentState) {
            '.' -> direction.left()
            '#' -> direction.right()
            else -> throw RuntimeException("unknown state $currentState")
        }

        map.nodes[position] = nextState

        if (nextState == '#') infected++

        position = when (direction) {
            Direction.UP -> position.copy(y = position.y - 1)
            Direction.DOWN -> position.copy(y = position.y + 1)
            Direction.LEFT -> position.copy(x = position.x - 1)
            Direction.RIGHT -> position.copy(x = position.x + 1)
        }
    }
}

class ResistantCarrier(private val map: Map) {
    var position = map.center()
    var direction = Direction.UP
    var infected = 0

    fun move() {
        val currentState = map.nodes.getOrDefault(position, '.')
        val nextState = when (currentState) {
            '.' -> 'W'
            'W' -> '#'
            '#' -> 'F'
            'F' -> '.'
            else -> throw RuntimeException("unknown state $currentState")
        }

        direction = when (currentState) {
            '.' -> direction.left()
            'W' -> direction
            '#' -> direction.right()
            'F' -> direction.reverse()
            else -> throw RuntimeException("unknown state $currentState")
        }

        map.nodes[position] = nextState

        if (nextState == '#') infected++

        position = when (direction) {
            Direction.UP -> position.copy(y = position.y - 1)
            Direction.DOWN -> position.copy(y = position.y + 1)
            Direction.LEFT -> position.copy(x = position.x - 1)
            Direction.RIGHT -> position.copy(x = position.x + 1)
        }
    }
}

enum class Direction {
    UP {
        override fun right() = RIGHT
        override fun left() = LEFT
    },
    DOWN {
        override fun right() = LEFT
        override fun left() = RIGHT
    },
    RIGHT {
        override fun right() = DOWN
        override fun left() = UP
    },
    LEFT {
        override fun right() = UP
        override fun left() = DOWN
    };

    abstract fun right(): Direction
    abstract fun left(): Direction
    fun reverse() = right().right()
}