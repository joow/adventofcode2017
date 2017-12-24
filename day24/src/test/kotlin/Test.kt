import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {

    val input = """0/2
        |2/2
        |2/3
        |3/4
        |3/5
        |0/1
        |10/1
        |9/10""".trimMargin()

    val puzzle = Tests::class.java.getResource("input").readText()

    @Test
    fun `it should parse components`() {
        val components = parse(input)

        assertThat(components).containsExactly(Component(0, 2), Component(2, 2), Component(2, 3),
                Component(3, 4), Component(3, 5), Component(0, 1), Component(10, 1), Component(9, 10))
    }

    @Test
    fun `it should generate valid bridges for simple case`() {
        val components = parse("0/2\n2/2")
        val bridges = bridges(components)

        assertThat(bridges).hasSize(2)
        assertThat(bridges.first().components).containsExactly(Component(0, 2))
        assertThat(bridges.last().components).containsExactly(Component(0, 2), Component(2, 2))
    }

    @Test
    fun `it should generate all valid bridges`() {
        val components = parse(input)
        val bridges = bridges(components)

        assertThat(bridges).hasSize(11)
    }

    @Test
    fun `it should find strength of the strongest bridge`() {
        val strength = strength(input)

        assertThat(strength).isEqualTo(31)
    }

    @Test
    fun `it should solve puzzle 1`() {
        val strength = strength(puzzle)

        assertThat(strength).isEqualTo(1695)
    }

    @Test
    fun `it should find longest bridge`() {
        val longest = longest(input)
        val strength = longest.strength

        assertThat(strength).isEqualTo(19)
    }

    @Test
    fun `it should solve puzzle 2`() {
        val longest = longest(puzzle)
        val strength = longest.strength

        assertThat(strength).isEqualTo(1673)
    }
}
