import java.util.regex.Pattern

class Spreadsheet(val rows: List<Row>) {
    class Row(private val values: List<Int>) {
        fun checksum(fn: (xs: List<Int>) -> Int) = fn(values)
    }

    fun checksum(fn: (xs: List<Int>) -> Int) = rows.map { it.checksum(fn) }.sum()

    companion object {
        private val NUMBER_SEPARATOR = Pattern.compile("\\s")

        fun parse(input: String) =
            Spreadsheet(input.split("\n").filter { it.isNotBlank() }
                    .map { Row(it.split(NUMBER_SEPARATOR).map { it.toInt() }) })
    }
}

fun minMax(xs: List<Int>): Pair<Int, Int> =
        xs.fold(Pair(xs.first(), xs.first())) { acc, n -> replace(acc, n) }

private fun replace(minMax: Pair<Int, Int>, n: Int)= when {
    n < minMax.first -> minMax.copy(first = n)
    n > minMax.second -> minMax.copy(second = n)
    else -> minMax
}

fun diff(xs: List<Int>) = minMax(xs).diff()

fun Pair<Int, Int>.diff() = second - first
