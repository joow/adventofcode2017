import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {

    private val input = """set a 1
    |add a 2
    |mul a a
    |mod a 5
    |snd a
    |set a 0
    |rcv a
    |jgz a -1
    |set a 1
    |jgz a -2""".trimMargin()

    private val puzzle = Tests::class.java.getResource("input").readText()

    @Test
    fun `it should set register value`() {
        val soundCard = SoundCard()

        soundCard.evaluate(listOf("set a 1"))

        assertThat(soundCard.registers["a"]).isEqualTo(1)
    }

    @Test
    fun `it should add value to register`() {
        val soundCard = SoundCard()

        soundCard.evaluate(listOf("set a 1", "add a 2"))

        assertThat(soundCard.registers["a"]).isEqualTo(3)
    }

    @Test
    fun `it should multiply register`() {
        val soundCard = SoundCard()

        soundCard.evaluate(listOf("set a 1", "add a 2", "mul a a"))

        assertThat(soundCard.registers["a"]).isEqualTo(9)
    }

    @Test
    fun `it should store modulo of register`() {
        val soundCard = SoundCard()

        soundCard.evaluate(listOf("set a 1", "add a 2", "mul a a", "mod a 5"))

        assertThat(soundCard.registers["a"]).isEqualTo(4)
    }

    @Test
    fun `it should play sound`() {
        val soundCard = SoundCard()

        soundCard.evaluate(listOf("set a 1", "add a 2", "mul a a", "mod a 5", "snd a"))

        assertThat(soundCard.played).containsExactly(4)
    }

    @Test
    fun `it should recover last played frequency`() {
        val soundCard = SoundCard()

        soundCard.evaluate(listOf("set a 1", "add a 2", "mul a a", "mod a 5", "snd a", "rcv a"))

        assertThat(soundCard.recovered).containsExactly(4)
    }

    @Test
    fun `it should not recover last played frequency if register is equal to 0`() {
        val soundCard = SoundCard()

        soundCard.evaluate(listOf("set a 1", "add a 2", "mul a a", "mod a 5", "snd a", "set a 0", "rcv a"))

        assertThat(soundCard.recovered).isEmpty()
    }

    @Test
    fun `it should jump to instruction`() {
        val soundCard = SoundCard()

        soundCard.evaluate(listOf("set a 1", "jgz a 2", "set a 0", "set a, 3", "mul a 3"))

        assertThat(soundCard.registers["a"]).isEqualTo(3)
    }

    @Test
    fun `it should not jump to instruction when register is equal to 0`() {
        val soundCard = SoundCard()

        soundCard.evaluate(listOf("set a 1", "set a 0", "jgz a 2", "mul a 3"))

        assertThat(soundCard.registers["a"]).isEqualTo(0)
    }

    @Test
    fun `it should find first recovered frequency`() {
        val soundCard = SoundCard()

        soundCard.evaluate(input.split("\n").filterNot { it.isBlank() })

        assertThat(soundCard.recovered.first()).isEqualTo(4)
    }

    @Test
    fun `it should solve puzzle 1`() {
        val soundCard = SoundCard()

        soundCard.evaluate(puzzle.split("\n").filterNot { it.isBlank() })

        assertThat(soundCard.recovered.first()).isEqualTo(4)
    }
}
