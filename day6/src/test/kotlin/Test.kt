import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {
    private val initialState = "0\t2\t7\t0"

    private val puzzle = "10\t3\t15\t10\t5\t15\t5\t15\t9\t2\t5\t8\t5\t2\t3\t6"

    @Test
    fun `it should parse input`() {
        val memory = Memory.parse(initialState)

        assertThat(memory.banks).hasSize(4)
        assertThat(memory.banks).containsExactly(0, 2, 7, 0)
    }

    @Test
    fun `it should cycle`() {
        val memory = Memory.parse(initialState)

        val nextMemory = memory.cycle()

        assertThat(nextMemory.banks).containsExactly(2, 4, 1, 2)
    }

    @Test
    fun `it should detect number of cycle for infinite looping`() {
        val memory = Memory.parse(initialState)

        val cycles = cycles(memory)

        assertThat(cycles.first).isEqualTo(5)
    }

    @Test
    fun `it should solve puzzle 1`() {
        val memory = Memory.parse(puzzle)

        val cycles = cycles(memory)

        assertThat(cycles.first).isEqualTo(14029)
    }

    @Test
    fun `it should count loop size`() {
        val memory = Memory.parse("2\t4\t1\t2")

        val cycles = cycles(memory)

        assertThat(cycles.first).isEqualTo(4)
    }

    @Test
    fun `it should count loop size for puzzle`() {
        val memory = Memory.parse(puzzle)

        val cycles = cycles(cycles(memory).second)

        assertThat(cycles.first).isEqualTo(2765)
    }
}
