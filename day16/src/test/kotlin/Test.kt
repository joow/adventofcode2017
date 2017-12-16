import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {
    private val puzzle = Tests::class.java.getResource("input").readText().removeSuffix("\n")

    @Test
    fun `it should make programs spin`() {
        val programs = dance("abcde", "s3")

        assertThat(programs).isEqualTo("cdeab")
    }

    @Test
    fun `it should make programs spin with count 1`() {
        val programs = dance("abcde", "s1")

        assertThat(programs).isEqualTo("eabcd")
    }

    @Test
    fun `it should make programs exchange positions()`() {
        val programs = dance("eabcd", "x3/4")

        assertThat(programs).isEqualTo("eabdc")
    }

    @Test
    fun `it should make programs partner()`() {
        val programs = dance("eabdc", "pe/b")

        assertThat(programs).isEqualTo("baedc")
    }

    @Test
    fun `it should record dance`() {
        val programs = record("abcde", "s1,x3/4,pe/b")

        assertThat(programs).isEqualTo("baedc")
    }

    @Test
    fun `it should solve puzzle 1`() {
        val programs = record("abcdefghijklmnop", puzzle)

        assertThat(programs).isEqualTo("giadhmkpcnbfjelo")
    }

    @Test
    fun `it should record dance as a whole`() {
        val programs = record("abcde", "s1,x3/4,pe/b", 2)

        assertThat(programs).isEqualTo("ceadb")
    }

    @Test
    fun `it should solve puzzle 2`() {
        val programs = record("abcdefghijklmnop", puzzle, 1_000_000_000)

        assertThat(programs).isEqualTo("njfgilbkcoemhpad")
    }
}
