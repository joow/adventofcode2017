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

fun knotHash(lengths: List<Int>, size: Int = 256): KnotHash {
    var hash = KnotHash(size)
    for (length in lengths) hash = hash.hash(length)
    return hash.shift()
}
