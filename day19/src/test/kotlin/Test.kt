import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {

    private val input = Tests::class.java.getResource("test").readText()

    private val puzzle = Tests::class.java.getResource("input").readText()

    @Test
    fun `it should parse routing diagram`() {
        val routingDiagram = RoutingDiagram(input)

        assertThat(routingDiagram.diagram).hasSize(6)
    }

    @Test
    fun `it should find starting point`() {
        val routingDiagram = RoutingDiagram(input)

        assertThat(routingDiagram.start()).isEqualTo(Point(0, 5, Direction.DOWN))
    }

    @Test
    fun `it should find next point in same direction`() {
        val routingDiagram = RoutingDiagram(input)

        val next = routingDiagram.next(Point(0, 5, Direction.DOWN))

        assertThat(next).isEqualTo(Point(1, 5, Direction.DOWN))
    }

    @Test
    fun `it should find letter`() {
        val routingDiagram = RoutingDiagram(input)

        val next = routingDiagram.next(Point(1, 5, Direction.DOWN))

        assertThat(next).isEqualTo(Point(2, 5, Direction.DOWN, false, 'A'))
    }

    @Test
    fun `it should turn left`() {
        val routingDiagram = RoutingDiagram(input)

        val next = routingDiagram.next(Point(3, 14, Direction.UP))

        assertThat(next).isEqualTo(Point(3, 13, Direction.LEFT))
    }

    @Test
    fun `it should turn right`() {
        val routingDiagram = RoutingDiagram(input)

        val next = routingDiagram.next(Point(5, 5, Direction.DOWN))

        assertThat(next).isEqualTo(Point(5, 6, Direction.RIGHT, false, 'B'))
    }

    @Test
    fun `it should turn up`() {
        val routingDiagram = RoutingDiagram(input)

        val next = routingDiagram.next(Point(5, 8, Direction.RIGHT))

        assertThat(next).isEqualTo(Point(4, 8, Direction.UP))
    }

    @Test
    fun `it should turn down`() {
        val routingDiagram = RoutingDiagram(input)

        val next = routingDiagram.next(Point(1, 11, Direction.RIGHT))

        assertThat(next).isEqualTo(Point(2, 11, Direction.DOWN, false, 'C'))
    }

    @Test
    fun `it should stop at the end`() {
        val routingDiagram = RoutingDiagram(input)

        val next = routingDiagram.next(Point(3, 1, Direction.LEFT))

        assertThat(next.isEnd).isTrue()
    }

    @Test
    fun `it should collect letters`() {
        val (letters, _) = collect(input)

        assertThat(letters).isEqualTo("ABCDEF")
    }

    @Test
    fun `it should solve puzzle 1`() {
        val (letters, _) = collect(puzzle)

        assertThat(letters).isEqualTo("DTOUFARJQ")
    }

    @Test
    fun `it should count steps`() {
        val (_, steps) = collect(input)

        assertThat(steps).isEqualTo(38)
    }

    @Test
    fun `it should solve puzzle 2`() {
        val (_, steps) = collect(puzzle)

        assertThat(steps).isEqualTo(16642)
    }
}
