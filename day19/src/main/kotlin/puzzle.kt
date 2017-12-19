fun collect(s: String): Pair<String, Int> {
    val routingDiagram = RoutingDiagram(s)
    var position = routingDiagram.start()
    val letters = mutableListOf<Char>()
    var steps = 0
    while (!position.isEnd) {
        position = routingDiagram.next(position)
        if (position.letter != null) letters.add(position.letter!!)
        steps++
    }

    return Pair(letters.joinToString(""), steps)
}

class RoutingDiagram(s: String) {

    val diagram: List<String> = s.split("\n").filterNot { it.isBlank() }

    fun start() = Point(0, diagram[0].indexOfFirst { it == '|' }, Direction.DOWN)

    fun next(from: Point): Point {
        var next = move(from)

        if (charAt(next).isWhitespace() && from.direction != Direction.DOWN)
            next = move(from.copy(direction = Direction.UP))

        if (charAt(next).isWhitespace() && from.direction != Direction.UP)
            next = move(from.copy(direction = Direction.DOWN))

        if (charAt(next).isWhitespace() && from.direction != Direction.LEFT)
            next = move(from.copy(direction = Direction.RIGHT))

        if (charAt(next).isWhitespace() && from.direction != Direction.RIGHT)
            next = move(from.copy(direction = Direction.LEFT))

        if (charAt(next).isLetter()) next = next.copy(letter = charAt(next)) else next = next.copy(letter = null)

        return next.copy(isEnd = charAt(next).isWhitespace())
    }

    private fun move(from: Point) = when (from.direction) {
        Direction.UP -> from.copy(row = from.row - 1)
        Direction.DOWN -> from.copy(row = from.row + 1)
        Direction.LEFT -> from.copy(column = from.column - 1)
        Direction.RIGHT -> from.copy(column = from.column + 1)
    }

    private fun charAt(point: Point) =
            if (point.row == -1 || point.row >= diagram.size
                    || point.column == -1 || point.column >= diagram[point.row].length) ' '
            else diagram[point.row][point.column]
}

enum class Direction { UP, DOWN, LEFT, RIGHT }

data class Point(val row: Int,
                 val column: Int,
                 val direction: Direction,
                 val isEnd: Boolean = false,
                 val letter: Char? = null)