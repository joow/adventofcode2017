import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {
    @Test
    fun `it should parse passphrase`() {
        val input = "aa bb cc dd ee"

        val passphrase = Passphrase.parse(input)

        assertThat(passphrase.words).hasSize(5)
    }

    @Test
    fun `it should detect valid passphrase`() {
        val input = "aa bb cc dd aaa"

        val passphrase = Passphrase.parse(input)

        assertThat(passphrase.isValid()).isTrue()
    }

    @Test
    fun `it should detect invalid passphrase`() {
        val input = "aa bb cc dd aa"

        val passphrase = Passphrase.parse(input)

        assertThat(passphrase.isValid()).isFalse()
    }

    @Test
    fun `it should count valid passphrases for puzzle 1`() {
        val input = Tests::class.java.getResource("input").readText()
        val lines = input.split("\n").filterNot { it.isBlank() }

        val passphrases = lines.map { Passphrase.parse(it) }
        val validPassphrases = passphrases.count { it.isValid() }

        assertThat(validPassphrases).isEqualTo(337)
    }
}
