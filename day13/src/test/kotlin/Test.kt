import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {

    val input = """0: 3
        |1: 2
        |4: 4
        |6: 4""".trimMargin()

    val puzzle = Tests::class.java.getResource("input").readText()

    @Test
    fun `it should parse firewall`() {
        val firewall = parse(input)

        assertThat(firewall.layers).hasSize(7)
    }

    @Test
    fun `it should not scan if range is 0`() {
        val layer = Layer(0)

        layer.scan()

        assertThat(layer.scannerPosition).isEqualTo(-1)
    }

    @Test
    fun `it should scan forward`() {
        val layer = Layer(3)

        layer.scan()

        assertThat(layer.scannerPosition).isEqualTo(1)
    }

    @Test
    fun `it should scan backward`() {
        val layer = Layer(3)

        repeat(3) { layer.scan() }

        assertThat(layer.scannerPosition).isEqualTo(1)
    }

    @Test
    fun `it should scan forward and backward`() {
        val layer = Layer(4)

        repeat(8) { layer.scan() }

        assertThat(layer.scannerPosition).isEqualTo(2)
    }

    @Test
    fun `it should scan firewall`() {
        val firewall = parse(input)

        firewall.scan()

        assertThat(firewall.layers.first().scannerPosition).isEqualTo(1)
    }

    @Test
    fun `it should traverse firewall`() {
        val firewall = parse(input)

        val severity = firewall.traverse()

        assertThat(severity).isEqualTo(24)
    }

    @Test
    fun `it should solve puzzle 1`() {
        val firewall = parse(puzzle)

        val severity = firewall.traverse()

        assertThat(severity).isEqualTo(1316)
    }

    @Test
    fun `it should find best delay`() {
        val firewall = parse(input)

        val delay = delay(firewall)

        assertThat(delay).isEqualTo(10)
    }

    @Test
    fun `it should solve puzzle 2`() {
        val firewall = parse(puzzle)

        val delay = delay(firewall)

        assertThat(delay).isEqualTo(3840052)
    }
}
