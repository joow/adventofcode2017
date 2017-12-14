import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test

class Tests {

    val input = """0 <-> 2
        |1 <-> 1
        |2 <-> 0, 3, 4
        |3 <-> 2, 4
        |4 <-> 2, 3, 6
        |5 <-> 6
        |6 <-> 4, 5""".trimMargin()

    val puzzleInput = Tests::class.java.getResource("input").readText()

    @Test
    fun `it should parse one line`() {
        val groups = parse("0 <-> 2")

        assertThat(groups).hasSize(1)
        assertThat(groups.first().programs).hasSize(2)
    }

    @Test
    fun `it should parse all program lines`() {
        val groups = parse(input)

        assertThat(groups).hasSize(2)
    }

    @Test
    fun `it should group all linked programs`() {
        val input = """1 <-> 2, 3
            |4 <-> 5
            |3 <-> 6
            |8 <-> 9
            |9 <-> 1""".trimMargin()
        val groups = parse(input)

        assertThat(groups).hasSize(2)
    }

    @Test
    fun `it should count programs in the group of program ID 0`() {
        val groups = parse(input)

        val group0 = groups.first { it.programs.contains(0) }

        assertThat(group0.programs).hasSize(6)
    }

    @Test
    fun `it should solve puzzle 1`() {
        val groups = parse(puzzleInput)

        val group0 = groups.first { it.programs.contains(0) }

        assertThat(group0.programs).hasSize(115)
    }

    @Test
    fun `it should solve puzzle 2`() {
        val groups = parse(puzzleInput)

        assertThat(groups).hasSize(221)
    }
}
