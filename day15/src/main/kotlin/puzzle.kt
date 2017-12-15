class Generator(seed: Long, private val factor: Long, private inline val criteria: (Long) -> Boolean = { true }) {

    private var previous: Long = seed

    tailrec fun next(): Long {
        previous = previous * factor % Int.MAX_VALUE
        return if (criteria(previous)) previous
        else next()
    }
}

fun lowestBits(n: Long): String {
    val s = n.toString(2).padStart(16, '0')
    return s.substring(s.length - 16)
}

fun matches(generatorA: Generator, generatorB: Generator, rounds: Int = 40_000_000): Int {
    var matches = 0
    for (i in 0..rounds) {
        val a = generatorA.next()
        val b = generatorB.next()
        if (lowestBits(a) == lowestBits(b)) matches++
    }

    return matches
}