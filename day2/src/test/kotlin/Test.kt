import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {
    private val input = """5 1 9 5
            |7 5 3
            |2 4 6 8""".trimMargin()

    @Test
    fun `it should ignore empty lines when parsing`() {
        val input = """1 2 3
            |""".trimMargin()
        val spreadsheet = Spreadsheet.parse(input)

        assertThat(spreadsheet.rows).hasSize(1)
    }

    @Test
    fun `it should parse input as spreadsheet`() {
        val spreadsheet = Spreadsheet.parse(input)

        assertThat(spreadsheet).isNotNull()
        assertThat(spreadsheet.rows).hasSize(3)
    }

    @Test
    fun `it should return min and max values of a row`() {
        val row = Spreadsheet.Row(listOf(5, 1, 9, 5))

        assertThat(row.minMax()).isEqualTo(Pair(1, 9))
    }

    @Test
    fun `it should return the difference of a row`() {
        val row = Spreadsheet.Row(listOf(5, 1, 9, 5))

        assertThat(row.diff()).isEqualTo(8)
    }

    @Test
    fun `it should compute checksum of a sample spreadsheet`() {
        val spreadsheet = Spreadsheet.parse(input)

        assertThat(spreadsheet.checksum()).isEqualTo(18)
    }

    @Test
    fun `it should compute checksum of the puzzle 1 input`() {
        val input = Tests::class.java.getResource("input").readText()
        val spreadsheet = Spreadsheet.parse(input)

        assertThat(spreadsheet.checksum()).isEqualTo(44887)
    }
}
