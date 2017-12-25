import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test

class Tests {
    @Test
    fun `it should handle state A when current value is 0`() {
        val turing = Turing(mutableMapOf(), 0, StateTestA()).step()

        assertThat(turing.position).isEqualTo(1)
        assertThat(turing.values.values).containsExactly(1)
        assertThat(turing.state).isInstanceOf(StateTestB::class.java)
    }

    @Test
    fun `it should handle state B when current value is 0`() {
        val turing = Turing(mutableMapOf(0 to 1), 1, StateTestB()).step()

        assertThat(turing.position).isEqualTo(0)
        assertThat(turing.values.values).containsExactly(1, 1)
        assertThat(turing.state).isInstanceOf(StateTestA::class.java)
    }

    @Test
    fun `it should handle state A when current value is 1`() {
        val turing = Turing(mutableMapOf(0 to 1, 1 to 1), 0, StateTestA()).step()

        assertThat(turing.position).isEqualTo(-1)
        assertThat(turing.values.values).containsExactly(0, 1)
        assertThat(turing.state).isInstanceOf(StateTestB::class.java)
    }

    @Test
    fun `it should compute checksum after 6 steps`() {
        var turing = Turing(mutableMapOf(), 0, StateTestA())

        repeat(6) { turing = turing.step() }

        assertThat(turing.values.values.count { it == 1 }).isEqualTo(3)
    }

    @Test
    fun `it should solve puzzle 1`() {
        var turing = Turing()

        repeat(12459852) { turing = turing.step() }

        assertThat(turing.values.values.count { it == 1 }).isEqualTo(4217)
    }
}

private class StateTestA : State() {
    override fun parts(currentValue: Int) =
            if (currentValue == 0) Triple(1, RIGHT, StateTestB())
            else Triple(0, LEFT, StateTestB())
}

private class StateTestB : State() {
    override fun parts(currentValue: Int) =
            if (currentValue == 0) Triple(1, LEFT, StateTestA())
            else Triple(1, RIGHT, StateTestA())
}