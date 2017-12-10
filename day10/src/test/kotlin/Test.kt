import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {

    @Test
    fun `it should hash first length`() {
        val hash = KnotHash(5).hash(3)

        assertThat(hash.state).containsExactly(3, 4, 2, 1, 0)
        assertThat(hash.skipSize).isEqualTo(1)
        assertThat(hash.shift).isEqualTo(3)
    }

    @Test
    fun `it should hash second length`() {
        val hash = KnotHash(state = listOf(3, 4, 2, 1, 0), skipSize = 1).hash(4)

        assertThat(hash.state).containsExactly(1, 2, 4, 3, 0)
        assertThat(hash.skipSize).isEqualTo(2)
        assertThat(hash.shift).isEqualTo(0)
    }

    @Test
    fun `it should hash third length`() {
        val hash = KnotHash(state = listOf(1, 2, 4, 3, 0), skipSize = 2).hash(1)

        assertThat(hash.state).containsExactly(3, 0, 1, 2, 4)
        assertThat(hash.skipSize).isEqualTo(3)
        assertThat(hash.shift).isEqualTo(3)
    }

    @Test
    fun `it should hash fourth length`() {
        val hash = KnotHash(state = listOf(3, 0, 1, 2, 4), skipSize = 3).hash(5)

        assertThat(hash.state).containsExactly(0, 3, 4, 2, 1)
        assertThat(hash.skipSize).isEqualTo(4)
        assertThat(hash.shift).isEqualTo(3)
    }

    @Test
    fun `it should compute the complete hash`() {
        val hash = knotHash(listOf(3, 4, 1, 5), 5)

        assertThat(hash.state).containsExactly(3, 4, 2, 1, 0)
    }

    @Test
    fun `it should solve puzzle 1`() {
        val input = "106,118,236,1,130,0,235,254,59,205,2,87,129,25,255,118"
        val lengths = input.split(",").map { it.toInt() }

        val hash = knotHash(lengths)

        assertThat(hash.state[0] * hash.state[1]).isEqualTo(100)
    }
}
