import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

private const val INPUT = 344

class Tests {
    @Test
    fun `it should insert a value for a buffer of size 1`() {
        val buffer = InsertingBuffer(3)

        buffer.insert(1)

        assertThat(buffer.values()).containsExactly(0, 1)
    }

    @Test
    fun `it should insert two values`() {
        val buffer = InsertingBuffer(3)

        buffer.insert(1)
        buffer.insert(2)

        assertThat(buffer.values()).containsExactly(0, 2, 1)
    }

    @Test
    fun `it should insert three values`() {
        val buffer = InsertingBuffer(3)

        buffer.insert(1)
        buffer.insert(2)
        buffer.insert(3)

        assertThat(buffer.values()).containsExactly(0, 2, 3, 1)
    }

    @Test
    fun `it should generate a buffer with ten values`() {
        val buffer = generate(InsertingBuffer(3), 9)

        assertThat(buffer).containsExactly(0, 9, 5, 7, 2, 4, 3, 8, 6, 1)
    }

    @Test
    fun `it should find value after 2017`() {
        val buffer = generate(InsertingBuffer(3), 2017)
        val value = valueAfter(buffer, 2017)

        assertThat(value).isEqualTo(638)
    }

    @Test
    fun `it should solve puzzle 1`() {
        val values = generate(InsertingBuffer(INPUT), 2017)
        val value = valueAfter(values, 2017)

        assertThat(value).isEqualTo(996)
    }

    @Test
    fun `it should solve puzzle 2`() {
        val values = generate(NoInsertingBuffer(INPUT), 50_000_000)
        val valueAfter0 = values.last()

        assertThat(valueAfter0).isEqualTo(1898341)
    }
}
