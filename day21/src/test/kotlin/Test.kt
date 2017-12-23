import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {

    private val startPattern = ".#...####"

    private val rules = """../.# => ##./#../...
        |.#./..#/### => #..#/..../..../#..#""".trimMargin()

    private val puzzle = Tests::class.java.getResource("input").readText()

    private val puzzleRules: List<Rule> by lazy { parseRules(puzzle) }

    @Test
    fun `it should compute size of image`() {
        val image = Image(startPattern)

        assertThat(image.size).isEqualTo(3)
    }

    @Test
    fun `it should parse rule for 2x2 grid`() {
        val rule = Rule("../.# => ##./#../...")

        assertThat(rule.from).containsExactly("...#", ".#..", "..#.", "#...")
        assertThat(rule.to).isEqualTo("##.#.....")
    }

    @Test
    fun `it should parse rule for 3x3 grid`() {
        val rule = Rule(".#./..#/### => #..#/..../..../#..#")

        assertThat(rule.from).containsExactly(
                ".#...####", "..##.#.##", ".###.#..#", ".#.#..###", "####...#.", "##.#.##..", "#..#.###.", "###..#.#.")
        assertThat(rule.to).isEqualTo("#..#........#..#")
    }

    @Test
    fun `it should not match pattern`() {
        val image = Image(startPattern)
        val rule = Rule("../.# => ##./#../...")

        val match = rule.match(image)

        assertThat(match).isFalse()
    }

    @Test
    fun `it should match pattern`() {
        val image = Image(startPattern)
        val rule = Rule(".#./..#/### => #..#/..../..../#..#")

        val match = rule.match(image)

        assertThat(match).isTrue()
    }

    @Test
    fun `it should match flipping pattern`() {
        val image = Image("#...")
        val rule = Rule("../.# => ##./#../...")

        val match = rule.match(image)

        assertThat(match).isTrue()
    }

    @Test
    fun `it should not split image into grids`() {
        val image = Image(startPattern)

        val grids = image.grids()

        assertThat(grids).hasSize(1)
        assertThat(grids.first().pixels).isEqualTo(startPattern)
    }

    @Test
    fun `it should split image into grids of 2x2`() {
        val image = Image("#..#........#..#")

        val grids = image.grids()

        assertThat(grids).hasSize(4)
        assertThat(grids.map { it.pixels }).containsExactly("#...", ".#..", "..#.", "...#")
    }

    @Test
    fun `it should split image with size multiple of 2 and 3 into grids of 2x2`() {
        val image = Image("##.##.#..#........##.##.#..#........")

        val grids = image.grids()

        assertThat(grids).hasSize(9)
        assertThat(grids.map { it.pixels })
                .containsExactly("###.", ".#.#", "#...", "..##", "...#", "..#.", "#...", ".#..", "....")
    }

    @Test
    fun `it should split image with size multiple of 3 into grids of 3x3`() {
        val image = Image("##....##.....##.....#.##..##.#.#..#..##.#..#..####.##.....#.##..##.#.....####...#")

        val grids = image.grids()

        assertThat(grids).hasSize(9)
        assertThat(grids.map { it.pixels })
                .containsExactly("##......#", "....##.##", "##......#",
                        "#.#.##.##", ".#..#.##.", ".#..#.##.",
                        "....##.##", ".#..#.##.", "##......#")
    }

    @Test
    fun `it should enhance starting image`() {
        val image = Image(startPattern)
        val rule = Rule(".#./..#/### => #..#/..../..../#..#")

        val enhancedImage = image.enhance(listOf(rule))

        assertThat(enhancedImage.pixels).isEqualTo("#..#........#..#")
    }

    @Test
    fun `it should enhance image multiple times`() {
        val image = Image(startPattern)
        val rules = parseRules(rules)

        val enhancedImage = enhance(image, rules, 2)

        assertThat(enhancedImage.pixels).isEqualTo("##.##.#..#........##.##.#..#........")
    }

    @Test
    fun `it should find on pixels`() {
        val image = Image(startPattern)
        val rules = parseRules(rules)

        val enhancedImage = enhance(image, rules, 2)

        assertThat(enhancedImage.pixels.count { it == '#' }).isEqualTo(12)
    }

    @Test
    fun `it should solve puzzle 1`() {
        val image = Image(startPattern)

        val enhancedImage = enhance(image, puzzleRules, 5)

        assertThat(enhancedImage.pixels.count { it == '#' }).isEqualTo(123)
    }

    @Test
    fun `it should solve puzzle 2`() {
        val image = Image(startPattern)

        val enhancedImage = enhance(image, puzzleRules, 18)

        println(enhancedImage.pixels.length)

        assertThat(enhancedImage.pixels.count { it == '#' }).isEqualTo(1984683)
    }
}
