import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {

    private val puzzle= Tests::class.java.getResource("input").readText().split("\n").filter { it.isNotBlank() }

    @Test
    fun `it should set register value`() {
        val registers = Coprocessor().evaluate("set a 1")

        assertThat(registers["a"]).isEqualTo(1)
    }

    @Test
    fun `it should set register value to other register value`() {
        val registers = Coprocessor(mutableMapOf("b" to 1)).evaluate("set a b")

        assertThat(registers["a"]).isEqualTo(1)
    }

    @Test
    fun `it should substract value from register`() {
        val registers = Coprocessor().evaluate("sub a 1")

        assertThat(registers["a"]).isEqualTo(-1)
    }

    @Test
    fun `it should substract register value from register`() {
        val registers = Coprocessor(mutableMapOf("b" to 1)).evaluate("sub a b")

        assertThat(registers["a"]).isEqualTo(-1)
    }

    @Test
    fun `it should multiply register by value`() {
        val registers = Coprocessor(mutableMapOf("a" to 1)).evaluate("mul a 2")

        assertThat(registers["a"]).isEqualTo(2)
    }

    @Test
    fun `it should multiply register by register value`() {
        val registers = Coprocessor(mutableMapOf("a" to 1, "b" to 2)).evaluate("mul a b")

        assertThat(registers["a"]).isEqualTo(2)
    }

    @Test
    fun `it should not skip instruction when value is 0`() {
        val registers = Coprocessor().evaluate("jnz a 2", "set a 1")

        assertThat(registers["a"]).isEqualTo(1)
    }

    @Test
    fun `it should skip instruction when value is not 0`() {
        val registers = Coprocessor(mutableMapOf("b" to 1)).evaluate("jnz b 2", "set a 1")

        assertThat(registers["a"]).isNull()
    }

    @Test
    fun `it should count mul invocations`() {
        val coprocessor = Coprocessor()
        val registers = coprocessor
                .evaluate("set a 1", "mul a 2", "jnz a 2", "mul a 3", "sub a 2", "jnz a 2", "mul a 4")

        assertThat(registers["a"]).isEqualTo(0)
        assertThat(coprocessor.mulInvocations).isEqualTo(2)
    }

    @Test
    fun `it should solve puzzle 1`() {
        val coprocessor = Coprocessor()

        coprocessor.evaluate(*puzzle.toTypedArray())

        assertThat(coprocessor.mulInvocations).isEqualTo(3969)
    }

    @Test
    fun `it should find first 10 prime numbers`() {
        val primes = (1..100).filter { isPrime(it) }.take(10)

        assertThat(primes).containsExactly(2, 3, 5, 7, 11, 13, 17, 19, 23, 29)
    }

    @Test
    fun `it should solve puzzle 2`() {
        val nbNotPrimes = (106500..123500 step 17).filterNot { isPrime(it) }.count()

        assertThat(nbNotPrimes).isEqualTo(917)
    }
}
