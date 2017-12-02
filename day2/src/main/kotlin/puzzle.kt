import java.util.regex.Pattern

class Spreadsheet(val rows: List<Row>) {
    class Row(private val values: List<Int>) {
        fun minMax(): Pair<Int, Int> =
            values.fold(Pair(values.first(), values.first())) { acc, n -> replace(acc, n) }

        private fun replace(minMax: Pair<Int, Int>, n: Int)= when {
            n < minMax.first -> minMax.copy(first = n)
            n > minMax.second -> minMax.copy(second = n)
            else -> minMax
        }

        fun diff() = minMax().diff()
    }

    fun checksum() = rows.map { it.diff() }.sum()

    companion object {
        private val NUMBER_SEPARATOR = Pattern.compile("\\s")

        fun parse(input: String) =
            Spreadsheet(input.split("\n").filter { it.isNotBlank() }
                    .map { Row(it.split(NUMBER_SEPARATOR).map { it.toInt() }) })
    }
}

fun Pair<Int, Int>.diff() = second - first
