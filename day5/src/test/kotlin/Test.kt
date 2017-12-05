import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {
    @Test
    fun `it should jump to next instruction`() {
        val instructions = Instructions(listOf(0, 3))

        val newInstructions = instructions.jump()

        assertThat(newInstructions.jumps).containsExactly(1, 3)
        assertThat(newInstructions.curr).isEqualTo(0)
        assertThat(newInstructions.steps).isEqualTo(1)
    }

    @Test
    fun `it should jump to next negative instruction`() {
        val instructions = Instructions(listOf(0, 3, 0, 1, -3), 4, 0)

        val newInstructions = instructions.jump()

        assertThat(newInstructions.jumps).containsExactly(0, 3, 0, 1, -2)
    }

    @Test
    fun `it should count steps to exit instructions`() {
        val instructions = Instructions(listOf(0, 3, 0, 1, -3))

        val steps = steps(instructions)

        assertThat(steps).isEqualTo(5)
    }

    @Test
    fun `it should count steps for puzzle 1`() {
        val input = Tests::class.java.getResource("input").readText()
        val instructions = Instructions(input.split("\n").filterNot { it.isBlank() }.map { it.toInt() })

        val steps = steps(instructions)

        assertThat(steps).isEqualTo(381680)
    }
}
