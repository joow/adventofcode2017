import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

const val PUZZLE_INPUT = "106,118,236,1,130,0,235,254,59,205,2,87,129,25,255,118"

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
        val lengths = PUZZLE_INPUT.split(",").map { it.toInt() }

        val hash = knotHash(lengths)

        assertThat(hash.state[0] * hash.state[1]).isEqualTo(6909)
    }

    @Test
    fun `it should convert input to ASCII codes`() {
        val lengths = ascii("1,2,3")

        assertThat(lengths).containsExactly(49, 44, 50, 44, 51)
    }

    @Test
    fun `it should compute lengths from input`() {
        val lengths = lengths("1,2,3")

        assertThat(lengths).containsExactly(49, 44, 50, 44, 51, 17, 31, 73, 47, 23)
    }

    @Test
    fun `it should xor values`() {
        val xor = xor(listOf(65, 27, 9, 1, 4, 3, 40, 50, 91, 7, 6, 0, 2, 5, 68, 22))

        assertThat(xor).isEqualTo(64)
    }

    @Test
    fun `it should display hex value of list of ints`() {
        val hex = hex(listOf(64, 7, 255))

        assertThat(hex).isEqualTo("4007ff")
    }

    @Test
    fun `it should solve puzzle 2`() {
        val lengths = lengths(PUZZLE_INPUT)
        val denseHash = denseHash(lengths)
        val hex = hex(denseHash)

        assertThat(hex).isEqualTo("9d5f4561367d379cfbf04f8c471c0095")
    }
}
