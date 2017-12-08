import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {
    @Test
    fun `it should parse register name`() {
        val input = "b inc 5 if a > 1"

        val instruction = Instruction.parse(input)

        assertThat(instruction.register).isEqualTo("b")
    }

    @Test
    fun `it should parse operation`() {
        val input = "inc 5"

        val operation = Operation.parse(input)

        assertThat(operation).isInstanceOf(Inc::class.java)
        assertThat(operation.value).isEqualTo(5)
    }

    @Test
    fun `it should parse condition`() {
        val input = "a > 1"

        val condition = Condition.parse(input)

        assertThat(condition).isInstanceOf(GreaterThan::class.java)
        assertThat(condition.register).isEqualTo("a")
        assertThat(condition.value).isEqualTo(1)
    }

    @Test
    fun `it should parse instruction`() {
        val input = "b inc 5 if a > 1"

        val instruction = Instruction.parse(input)

        assertThat(instruction.register).isEqualTo("b")
    }

    @Test
    fun `it should evaluation instruction with failing condition`() {
        val input = "b inc 5 if a > 1"

        val registers = evaluate(input)

        assertThat(registers.first).isEmpty()
    }

    @Test
    fun `it should evaluation instruction with succeeding condition`() {
        val input = "a inc 1 if b < 5"

        val registers = evaluate(input)

        assertThat(registers.first).hasSize(1)
        assertThat(registers.first["a"]).isEqualTo(1)
    }

    @Test
    fun `it should evaluation multiple instructions`() {
        val input = """b inc 5 if a > 1
            |a inc 1 if b < 5
            |c dec -10 if a >= 1
            |c inc -20 if c == 10""".trimMargin()

        val registers = evaluate(input)

        assertThat(registers.first).hasSize(2)
        assertThat(registers.first["a"]).isEqualTo(1)
        assertThat(registers.first["c"]).isEqualTo(-10)
    }

    @Test
    fun `it should find largest value in registers`() {
        val input = """b inc 5 if a > 1
            |a inc 1 if b < 5
            |c dec -10 if a >= 1
            |c inc -20 if c == 10""".trimMargin()

        val max = max(input)

        assertThat(max).isEqualTo(1)
    }

    @Test
    fun `it should find largest value for puzzle 1`() {
        val input = Tests::class.java.getResource("input").readText()

        val max = max(input)

        assertThat(max).isEqualTo(7296)
    }

    @Test
    fun `it should find max held`() {
        val input = """b inc 5 if a > 1
            |a inc 1 if b < 5
            |c dec -10 if a >= 1
            |c inc -20 if c == 10""".trimMargin()

        val maxHeld = maxHeld(input)

        assertThat(maxHeld).isEqualTo(10)
    }

    @Test
    fun `it should find max held for puzzle 2`() {
        val input = Tests::class.java.getResource("input").readText()

        val maxHeld = maxHeld(input)

        assertThat(maxHeld).isEqualTo(7296)
    }
}
