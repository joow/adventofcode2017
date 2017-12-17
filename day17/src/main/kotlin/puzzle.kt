sealed class Buffer(val steps: Int) {
    abstract fun insert(value: Int)
    abstract fun values(): List<Int>
}

class InsertingBuffer(steps: Int) : Buffer(steps) {

    private val buffer = mutableListOf(0)
    private var position = 0

    override fun insert(value: Int) {
        position = (position + steps) % buffer.size + 1
        buffer.add(position, value)
    }

    override fun values() = buffer.toList()
}

fun generate(buffer: Buffer, max: Int): List<Int> {
    for (n in 1..max) buffer.insert(n)

    return buffer.values()
}

fun valueAfter(xs: List<Int>, x: Int) = xs.dropWhile { it != x }.drop(1).first()

class NoInsertingBuffer(steps: Int) : Buffer(steps) {

    private var size = 1
    private var position = 0
    private var positionOf0 = 0
    private var valueAfter0 = -1

    override fun insert(value: Int) {
        position = (position + steps) % size + 1
        size++
        if (position < positionOf0) positionOf0++
        if (position == positionOf0 + 1) valueAfter0 = value
    }

    override fun values() = listOf(0, valueAfter0)
}