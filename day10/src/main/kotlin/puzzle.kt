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