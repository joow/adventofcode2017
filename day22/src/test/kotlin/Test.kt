import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test

class Tests {
    private val input = """..#
        |#..
        |...""".trimMargin()

    private val puzzle = Tests::class.java.getResource("input").readText()

    @Test
    fun `it should parse map`() {
        val map = Map(input)

        assertThat(map.nodes).hasSize(9)
    }

    @Test
    fun `it should find center of map`() {
        val map = Map(input)

        assertThat(map.center()).isEqualTo(Position(1, 1))
    }

    @Test
    fun `it should place carrier at the center of the map`() {
        val map = Map(input)
        val carrier = Carrier(map)

        assertThat(carrier.position).isEqualTo(map.center())
    }

    @Test
    fun `it should move the carrier on the left when current position is clean`() {
        val map = Map(input)
        val carrier = Carrier(map)

        carrier.move()

        assertThat(carrier.position).isEqualTo(Position(0, 1))
    }

    @Test
    fun `it should count infected nodes`() {
        val map = Map(input)
        val carrier = Carrier(map)

        repeat(10_000) { carrier.move() }

        assertThat(carrier.infected).isEqualTo(5587)
    }

    @Test
    fun `it should solve puzzle 1`() {
        val map = Map(puzzle)
        val carrier = Carrier(map)

        repeat(10_000) { carrier.move() }

        assertThat(carrier.infected).isEqualTo(5570)
    }

    @Test
    fun `it should weakens a clean node`() {
        val map = Map(input)
        val carrier = ResistantCarrier(map)

        carrier.move()

        assertThat(map.nodes[map.center()]).isEqualTo('W')
    }

    @Test
    fun `it should flag an infected node`() {
        val map = Map(input)
        val carrier = ResistantCarrier(map)

        carrier.move()
        carrier.move()

        assertThat(map.nodes[Position(0, 1)]).isEqualTo('F')
    }

    @Test
    fun `it should weakens multiple clean nodes`() {
        val map = Map(input)
        val carrier = ResistantCarrier(map)

        carrier.move()
        carrier.move()
        carrier.move()
        carrier.move()
        carrier.move()

        assertThat(map.nodes[Position(0, 0)]).isEqualTo('W')
        assertThat(map.nodes[Position(-1, 0)]).isEqualTo('W')
        assertThat(map.nodes[Position(-1, 1)]).isEqualTo('W')
    }

    @Test
    fun `it should cleans flagged node`() {
        val map = Map(input)
        val carrier = ResistantCarrier(map)

        carrier.move()
        carrier.move()
        carrier.move()
        carrier.move()
        carrier.move()
        carrier.move()

        assertThat(map.nodes[Position(0, 1)]).isEqualTo('.')
    }

    @Test
    fun `it should infect weakened node`() {
        val map = Map(input)
        val carrier = ResistantCarrier(map)

        carrier.move()
        carrier.move()
        carrier.move()
        carrier.move()
        carrier.move()
        carrier.move()
        carrier.move()

        assertThat(map.nodes[Position(-1, 1)]).isEqualTo('#')
    }

    @Test
    fun `it should count infected nodes after 100 bursts`() {
        val map = Map(input)
        val carrier = ResistantCarrier(map)

        repeat(100) { carrier.move() }

        assertThat(carrier.infected).isEqualTo(26)
    }

    @Test
    fun `it should count infected nodes after 10000000 bursts`() {
        val map = Map(input)
        val carrier = ResistantCarrier(map)

        repeat(10_000_000) { carrier.move() }

        assertThat(carrier.infected).isEqualTo(2511944)
    }

    @Test
    fun `it should solve puzzle 2`() {
        val map = Map(puzzle)
        val carrier = ResistantCarrier(map)

        repeat(10_000_000) { carrier.move() }

        assertThat(carrier.infected).isEqualTo(2512022)
    }
}
