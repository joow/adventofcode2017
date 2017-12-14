import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

const val INPUT = "flqrgnkx"

const val PUZZLE = "vbqugkhl"

class Tests {

    @Test
    fun `it should convert hex char to binary string`() {
        assertThat(binary('0')).isEqualTo("0000")
        assertThat(binary('1')).isEqualTo("0001")
        assertThat(binary('e')).isEqualTo("1110")
        assertThat(binary('f')).isEqualTo("1111")
    }

    @Test
    fun `it should convert hex string to binary string`() {
        assertThat(binary("a0c2017")).isEqualTo("1010000011000010000000010111")
    }

    @Test
    fun `it should generate input sequences`() {
        val inputs = inputs(INPUT)

        assertThat(inputs).hasSize(128)
        assertThat(inputs.first()).isEqualTo("$INPUT-0")
        assertThat(inputs.last()).isEqualTo("$INPUT-127")
    }

    @Test
    fun `it should compute hashes of inputs`() {
        val hashes = hashes(INPUT)

        assertThat(hashes).hasSize(128)
        assertThat(hashes.first()).isEqualTo("d4f76bdcbf838f8416ccfa8bc6d1f9e6")
    }

    @Test
    fun `it should count used squares`() {
        assertThat(count(INPUT)).isEqualTo(8108)
    }

    @Test
    fun `it should solve puzzle 1`() {
        assertThat(count(PUZZLE)).isEqualTo(8148)
    }

    @Test
    fun `it should generate squares`() {
        val squares = squares(INPUT)

        assertThat(squares).hasSize(128)
        assertThat(squares.first()).hasSize(128)
    }

    @Test
    fun `it should count regions`() {
        val regions = regions(INPUT)

        assertThat(regions).isEqualTo(1242)
    }

    @Test
    fun `it should solve puzzle 2`() {
        val regions = regions(PUZZLE)

        assertThat(regions).isEqualTo(1180)
    }
}