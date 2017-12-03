import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {
    @Test
    fun `it should compute next square from access port`() {
        val nextSquare = Square.ACCESS_PORT.next()

        assertThat(nextSquare.pos).isEqualTo(Position(1, 0))
    }

    @Test
    fun `it should build a grid with one square`() {
        val grid = grid(1)

        assertThat(grid.count()).isEqualTo(1)
        val firstSquare = grid.first()
        assertThat(firstSquare.pos).isEqualTo(Position(0, 0))
    }

    @Test
    fun `it should build a grid with two squares`() {
        val grid = grid(2)

        assertThat(grid.count()).isEqualTo(2)
        val secondSquare = grid.drop(1).first()
        assertThat(secondSquare.pos).isEqualTo(Position(1, 0))
    }

    @Test
    fun `it should build a complex grid`() {
        val grid = grid(12)

        val twelfthSquare = grid.drop(11).first()
        assertThat(twelfthSquare.pos).isEqualTo(Position(2, 1))
    }

    @Test
    fun `it should compute steps`() {
        val grid = grid(1024)

        val square1 = grid.first()
        assertThat(square1.steps()).isZero()

        val square12 = grid.drop(11).first()
        assertThat(square12.steps()).isEqualTo(3)

        val square23 = grid.drop(22).first()
        assertThat(square23.steps()).isEqualTo(2)

        val square1024 = grid.drop(1023).first()
        assertThat(square1024.steps()).isEqualTo(31)
    }

    @Test
    fun `it should compute steps for puzzle 1`() {
        val grid = grid(361527)

        val square = grid.last()
        assertThat(square.steps()).isEqualTo(326)
    }

    @Test
    fun `it should check adjacent position()`() {
        val pos = Position(0, 0)
        val horizontalAdjacentPos = Position(1, 0)
        val verticalAdjacentPos = Position(0, 1)
        val diagnonalAdjacentPos = Position(1, 1)
        val nonAdjacentPos = Position(2, 0)

        assertThat(horizontalAdjacentPos.isAdjacent(pos)).isTrue()
        assertThat(verticalAdjacentPos.isAdjacent(pos)).isTrue()
        assertThat(diagnonalAdjacentPos.isAdjacent(pos)).isTrue()
        assertThat(nonAdjacentPos.isAdjacent(pos)).isFalse()
    }

    @Test
    fun `it should compute value of each square`() {
        val grid = grid(23)
        val sums = sums(grid)

        assertThat(sums.last().second).isEqualTo(806)
    }

    @Test
    fun `it should resolve puzzle 2`() {
        val grid = grid(1024)
        val sums = sums(grid)

        val answer = sums.first { (_, sum) -> sum > 361527 }.second

        assertThat(answer).isEqualTo(363010)
    }
}
