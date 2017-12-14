fun binary(c: Char) = Integer.parseInt(c.toString(), 16).toString(2).padStart(4, '0')

fun binary(s: String) = s.map { binary(it) }.joinToString("")

fun inputs(input: String) = (0..127).map { "$input-$it" }

fun hashes(input: String) = inputs(input).map { hash(it) }

fun hash(input: String): String {
    val lengths = lengths(input)
    val denseHash = denseHash(lengths)
    return hex(denseHash)
}

fun count(input: String) = hashes(input).joinToString("") { binary(it) }.count { it == '1' }

fun squares(input: String) = hashes(input).map { binary(it) }.map { split(it) }

private fun split(row: String) = row.map { Square(it) }

fun regions(input: String): Int {

    fun regions(squares: List<List<Square>>, region: Int, row: Int = 0, column: Int = 0): Int {
        if (row == -1 || column == -1) return region
        if (row == squares.size || column == squares[row].size) return region

        val square = squares[row][column]

        if (!square.isUsed) return region

        // assign the current region to the square if no region set yet
        if (square.isUsed && square.region == 0) square.region = region
        else return region

        // try to advance to the next square in the row
        regions(squares, region,row + 1, column)

        // try to advance to the previous square in the row
        regions(squares, region, row - 1, column)

        // try to advance to the next square in the column
        regions(squares, region, row, column + 1)

        // try to advance to the previous square in the row
        regions(squares, region, row, column - 1)

        return region + 1
    }

    val squares = squares(input)
    var region = 1

    for (row in 0 until squares.size) {
        for (column in 0 until squares[row].size) {
            region = regions(squares, region, row, column)
        }
    }

    return region - 1
}

class Square(used: Char) {
    val isUsed = used == '1'
    var region: Int = 0
}

class KnotHash(size: Int = 256,
               val state: List<Int> = List(size) { it },
               val skipSize: Int = 0,
               val shift: Int = 0) {

    fun hash(length: Int): KnotHash {
        val reversed = state.take(length).reversed().plus(state.drop(length))
        val position = (length + skipSize) % reversed.size
        val newState = if (position == 0) reversed else reversed.drop(position).plus(reversed.take(position))

        return KnotHash(state = newState, skipSize = skipSize + 1, shift = shift + position)
    }

    fun shift(): KnotHash {
        val fromIndex = state.size - (shift % state.size)
        return if (fromIndex == 0) this else KnotHash(state = state.drop(fromIndex).plus(state.take(fromIndex)))
    }
}

fun knotHash(lengths: List<Int>, size: Int = 256, rounds: Int = 1): KnotHash {
    var hash = KnotHash(size)
    repeat(rounds) { for (length in lengths) hash = hash.hash(length) }
    return hash.shift()
}

fun denseHash(lengths: List<Int>, size: Int = 256): List<Int> {
    val hash = knotHash(lengths, size, 64)

    return hash.state.windowed(16, 16).map { xor(it) }
}

fun xor(xs: List<Int>): Int = xs.fold(0) { acc, n -> acc xor n}

fun ascii(s: String) = s.map { it.toInt() }

val lengthsSuffix = listOf(17, 31, 73, 47, 23)

fun lengths(s: String) = ascii(s).plus(lengthsSuffix)

fun hex(xs: List<Int>) = xs.joinToString("") { it.toHexString() }

private fun Int.toHexString(): String {
    val hex = toString(16)
    return if (hex.length == 2) hex else "0$hex"
}