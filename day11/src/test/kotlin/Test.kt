import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {
    @Test
    fun `it should compute coordinates of path`() {
        val steps = arrayOf(
                Pair("ne,ne,ne", 3),
                Pair("ne,ne,sw,sw", 0),
                Pair("ne,ne,s,s", 2),
                Pair("se,sw,se,sw,sw", 3))

        steps.forEach {
            val actualSteps = steps(it.first)
            assertThat(actualSteps).isEqualTo(it.second)
        }
    }

    @Test
    fun `it should solve puzzle 1`() {
        val path = Tests::class.java.getResource("input").readText().replace("\n", "")

        val steps = steps(path)

        assertThat(steps).isEqualTo(722)
    }

    @Test
    fun `it should solve puzzle 2`() {
        val path = Tests::class.java.getResource("input").readText().replace("\n", "")

        val furthest = furthest(path)

        assertThat(furthest).isEqualTo(1551)
    }
}
