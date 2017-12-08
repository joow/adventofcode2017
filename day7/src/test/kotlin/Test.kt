import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test

class Tests {
    private val INPUT = """pbga (66)
        |xhth (57)
        |ebii (61)
        |havc (66)
        |ktlj (57)
        |fwft (72) -> ktlj, cntj, xhth
        |qoyq (66)
        |padx (45) -> pbga, havc, qoyq
        |tknk (41) -> ugml, padx, fwft
        |jptl (61)
        |ugml (68) -> gyxo, ebii, jptl
        |gyxo (61)
        |cntj (57)""".trimMargin()

    @Test
    fun `it should parse tower without subtowers`() {
        val input = "pbga (66)"

        val tower = Tower.parse(input)

        assertThat(tower.name).isEqualTo("pbga")
        assertThat(tower.weight).isEqualTo(66)
        assertThat(tower.subtowers).isEmpty()
    }

    @Test
    fun `it should parse tower with subtowers`() {
        val input = "fwft (72) -> ktlj, cntj, xhth"

        val tower = Tower.parse(input)

        assertThat(tower.name).isEqualTo("fwft")
        assertThat(tower.weight).isEqualTo(72)
        assertThat(tower.subtowers).hasSize(3)

    }

    @Test
    fun `it should find the bottom of the tower`() {
        val tower = Tower.parse(INPUT)

        assertThat(tower.name).isEqualTo("tknk")
    }

    @Test
    fun `it should solve puzzle 1`() {
        val tower = Tower.parse(Tests::class.java.getResource("input").readText())

        assertThat(tower.name).isEqualTo("dgoocsw")
    }

    @Test
    fun `it should compute weights`() {
        val tower = Tower.parse(INPUT)

        assertThat(tower.subtowers.map { it.totalWeight() }).containsExactly(251, 243, 243)
    }

    @Test
    fun `it should find invalid weighted tower`() {
        val tower = Tower.parse(INPUT)
        val invalid = tower.invalid()
        val invalidTower = invalid?.first
        val validTotalWeight = invalid?.second

        assertThat(invalidTower?.name).isEqualTo("ugml")
        assertThat(invalidTower?.totalWeight()).isEqualTo(251)
        assertThat(invalidTower?.totalWeight()).isEqualTo(251)
        assertThat(validTotalWeight).isEqualTo(243)
    }

    @Test
    fun `it should compute correct weight`() {
        val tower = Tower.parse(INPUT)

        val correctedWeight = tower.correctWeight()

        assertThat(correctedWeight).isEqualTo(60)
    }

    @Test
    fun `it should solve puzzle 2`() {
        val tower = Tower.parse(Tests::class.java.getResource("input").readText())

        val correctedWeight = tower.correctWeight()

        assertThat(correctedWeight).isEqualTo(1275)
    }
}
