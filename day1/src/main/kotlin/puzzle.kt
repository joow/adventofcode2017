fun sum(s: String, strategy: LookupStrategy): Int {
    var sum = 0
    val jump = strategy.jump(s.length)

    for (i in 0 until s.length) {
        val c1 = s[i]
        val c2 = circular(s, i, jump)
        sum += value(c1.toDigit(), c2.toDigit())
    }

    return sum
}

fun circular(s: String, from: Int, jump: Int): Char {
    val idx = (from + jump) % s.length
    return s[idx]
}

fun value(x: Int, y: Int) = if (x == y) x else 0

fun Char.toDigit() = toString().toInt()

enum class LookupStrategy {

    NEXT {
        override fun jump(length: Int) = 1
    },

    HALF {
        override fun jump(length: Int) = length / 2
    };

    abstract fun jump(length: Int): Int
}
