import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {
    @Test
    fun `it should generate next values for generator A`() {
        val generator = generator(65, 16807)

        assertThat(generator.next()).isEqualTo(1092455)
        assertThat(generator.next()).isEqualTo(1181022009)
        assertThat(generator.next()).isEqualTo(245556042)
        assertThat(generator.next()).isEqualTo(1744312007)
        assertThat(generator.next()).isEqualTo(1352636452)
    }

    @Test
    fun `it should generate next values for generator B`() {
        val generator = generator(8921, 48271)

        assertThat(generator.next()).isEqualTo(430625591)
        assertThat(generator.next()).isEqualTo(1233683848)
        assertThat(generator.next()).isEqualTo(1431495498)
        assertThat(generator.next()).isEqualTo(137874439)
        assertThat(generator.next()).isEqualTo(285222916)
    }

    @Test
    fun `it should get lowest bits of a Long value`() {
        val lowestBits = lowestBits(1092455)

        assertThat(lowestBits).isEqualTo("1010101101100111")
    }

    @Test
    fun `it should get lowest bits of a small Long value`() {
        val lowestBits = lowestBits(16)

        assertThat(lowestBits).isEqualTo("0000000000010000")
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
        val generator = generator(65, 16807) { it % 4 == 0L }

        assertThat(generator.next()).isEqualTo(1352636452)
        assertThat(generator.next()).isEqualTo(1992081072)
        assertThat(generator.next()).isEqualTo(530830436)
        assertThat(generator.next()).isEqualTo(1980017072)
        assertThat(generator.next()).isEqualTo(740335192)
    }

    @Test
    fun `it should generate values matching criteria for generator B`() {
        val generator = generator(8921, 48271) { it % 8 == 0L }

        assertThat(generator.next()).isEqualTo(1233683848)
        assertThat(generator.next()).isEqualTo(862516352)
        assertThat(generator.next()).isEqualTo(1159784568)
        assertThat(generator.next()).isEqualTo(1616057672)
        assertThat(generator.next()).isEqualTo(412269392)
    }

    @Test
    fun `it should count matching pairs for generators with criteria`() {
        val generatorA = generator(65, 16807) { it % 4 == 0L }
        val generatorB = generator(8921, 48271) { it % 8 == 0L }

        val matches = matches(generatorA, generatorB, 5_000_000)

        assertThat(matches).isEqualTo(309)
    }

    @Test
    fun `it should solve puzzle 2`() {
        val generatorA = generator(703, 16807) { it % 4 == 0L }
        val generatorB = generator(516, 48271) { it % 8 == 0L }

        val matches = matches(generatorA, generatorB, 5_000_000)

        assertThat(matches).isEqualTo(328)
    }
}
