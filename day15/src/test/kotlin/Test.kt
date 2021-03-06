import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

private fun isMult4(n: Long) = n and 3 == 0L
private fun isMult8(n: Long) = n and 7 == 0L

class Tests {
    @Test
    fun `it should generate next values for generator A`() {
        val generator = generator(65, 16807)

        assertThat(generator.take(5).toList()).containsExactly(1092455, 1181022009, 245556042, 1744312007, 1352636452)
    }

    @Test
    fun `it should generate next values for generator B`() {
        val generator = generator(8921, 48271)

        assertThat(generator.take(5).toList()).containsExactly(430625591, 1233683848, 1431495498, 137874439, 285222916)
    }

    @Test
    fun `it should get lowest bits of a Long value`() {
        val lowestBits = lowestBits(1092455)

        assertThat(lowestBits).isEqualTo(43879L)
    }

    @Test
    fun `it should get lowest bits of a small Long value`() {
        val lowestBits = lowestBits(16)

        assertThat(lowestBits).isEqualTo(16)
    }

    @Test
    fun `it should count matching pairs for a few rounds`() {
        val generatorA = generator(65, 16807)
        val generatorB = generator(8921, 48271)

        val matches = matches(generatorA, generatorB, 5)

        assertThat(matches).isEqualTo(1)
    }

    @Test
    fun `it should count matching pairs`() {
        val generatorA = generator(65, 16807)
        val generatorB = generator(8921, 48271)

        val matches = matches(generatorA, generatorB)

        assertThat(matches).isEqualTo(588)
    }

    @Test
    fun `it should solve puzzle 1`() {
        val generatorA = generator(703, 16807)
        val generatorB = generator(516, 48271)

        val matches = matches(generatorA, generatorB)

        assertThat(matches).isEqualTo(594)
    }

    @Test
    fun `it should generate values matching criteria for generator A`() {
        val generator = generator(65, 16807) { isMult4(it) }

        assertThat(generator.take(5).toList()).containsExactly(1352636452, 1992081072, 530830436, 1980017072, 740335192)
    }

    @Test
    fun `it should generate values matching criteria for generator B`() {
        val generator = generator(8921, 48271) { isMult8(it) }

        assertThat(generator.take(5).toList()).containsExactly(1233683848, 862516352, 1159784568, 1616057672, 412269392)
    }

    @Test
    fun `it should count matching pairs for generators with criteria`() {
        val generatorA = generator(65, 16807) { isMult4(it) }
        val generatorB = generator(8921, 48271) { isMult8(it) }

        val matches = matches(generatorA, generatorB, 5_000_000)

        assertThat(matches).isEqualTo(309)
    }

    @Test
    fun `it should solve puzzle 2`() {
        val generatorA = generator(703, 16807) { isMult4(it) }
        val generatorB = generator(516, 48271) { isMult8(it) }

        val matches = matches(generatorA, generatorB, 5_000_000)

        assertThat(matches).isEqualTo(328)
    }
}
