import kotlin.math.sqrt

class Image(val pixels: String) {

    val size = Math.sqrt(pixels.length.toDouble()).toInt()

    fun enhance(rules: List<Rule>): Image {
        val enhancedGrids = grids()
                .map { img -> Pair(img, rules.first { it.match(img) }) }
                .map { (img, rule) -> rule.enhance(img) }

        return fromGrids(enhancedGrids)
    }

    tailrec fun grids(pixels: String = this.pixels,
                      acc: List<Image> = emptyList(),
                      size: Int = this.size,
                      gridSize: Int = if (size % 2 == 0) 2 else 3): List<Image> {
        if (pixels.length == gridSize * gridSize) return acc.plus(Image(pixels))

        val image = Image(pixels.windowed(gridSize, size).take(gridSize).joinToString(""))

        val remainingPixels =
                if (gridSize == 2) pixels.drop(gridSize).take(size - gridSize).plus(pixels.drop(size + gridSize))
                else pixels.drop(gridSize).take(size - gridSize)
                        .plus(pixels.drop(size + gridSize).take(size - gridSize))
                        .plus(pixels.drop(2 * size + gridSize))

        return grids(remainingPixels, acc.plus(image), if (size == gridSize) this.size else size - gridSize, gridSize)
    }

    private fun fromGrids(grids: List<Image>): Image {
        if (grids.size == 1) return grids.first()

        val n = sqrt(grids.size.toDouble()).toInt()
        val size = grids.first().size
        val buffer = StringBuffer()

        for (i in 0 until grids.size step n) {
            buffer.append(grids.drop(i)
                    .take(n)
                    .flatMap { it.pixels.windowed(size, size).mapIndexed { index, s -> Pair(index, s) } }
                    .groupBy({ it.first}, { it.second })
                    .flatMap { it.value }
                    .joinToString(""))
        }

        return Image(buffer.toString())
    }
}

class Rule(s: String) {
    val from = permutations(s.substringBefore("=>").replace("/", "").trim())
    val to = s.substringAfter("=>").trim().replace("/", "")

    private fun permutations(s: String) : List<String> {
        val p1 = s
        val p2 = symmetric(split(p1))
        val p3 = flip(split(p2))
        val p4 = symmetric(split(p3))
        val p5 = flip(split(p4))
        val p6 = symmetric(split(p5))
        val p7 = flip(split(p6))
        val p8 = symmetric(split(p7))

        return listOf(p1, p2, p3, p4, p5, p6, p7, p8).distinct()
    }

    private fun split(s: String): List<String> {
        val gridSize = sqrt(s.length.toDouble()).toInt()
        return s.windowed(gridSize, gridSize)
    }

    private fun symmetric(rows: List<String>): String {
        val symmetric = (1..rows.size).map { charArrayOf(*("x".repeat(rows.size)).toCharArray()) }

        for (row in 0 until rows.size) {
            for (col in 0 until rows[row].length) {
                symmetric[col][row] = rows[row][col]
            }
        }

        return symmetric.joinToString("") { it.joinToString("") }
    }

    private fun flip(rows: List<String>): String {
        val firstRow = rows.first()
        val lastRow = rows.last()
        val nbRowsToKeep = rows.size - 2

        return listOf(lastRow).plus(rows.drop(1).take(nbRowsToKeep)).plus(listOf(firstRow)).joinToString("")
    }

    fun match(image: Image) = from.any { it == image.pixels }

    fun enhance(image: Image) = if (match(image)) Image(to) else image
}

fun parseRules(s: String) = s.split("\n").filter { it.isNotBlank() }.map { Rule(it) }

tailrec fun enhance(image: Image, rules: List<Rule>, iterations: Int): Image =
    if (iterations == 0) image
    else enhance(image.enhance(rules), rules, iterations - 1)