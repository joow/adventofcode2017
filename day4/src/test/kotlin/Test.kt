import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
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

    private fun readLines(path: String) =
        Tests::class.java.getResource(path).readText().split("\n").filterNot { it.isBlank() }

    @Test
    fun `it should count valid passphrases for puzzle 1`() {
        val lines = readLines("input")

        val passphrases = lines.map { Passphrase.parse(it) }
        val validPassphrases = passphrases.count { it.isValid() }

        assertThat(validPassphrases).isEqualTo(337)
    }

    @Test
    fun `it should get all anagrams of a word`() {
        val word = "abc"
        val anagrams = anagrams(word)

        assertThat(anagrams).hasSize(6)
        assertThat(anagrams).contains("abc", "acb", "bac", "bca", "cab", "cba")
    }

    @Test
    fun `it should detect valid anagrams passphrase`() {
        assertThat(Passphrase.parse("abcde fghij", ::anagrams).isValid()).isTrue()
        assertThat(Passphrase.parse("a ab abc abd abf abj", ::anagrams).isValid()).isTrue()
        assertThat(Passphrase.parse("iiii oiii ooii oooi oooo", ::anagrams).isValid()).isTrue()
    }

    @Test
    fun `it should detect invalid anagrams passphrase`() {
        assertThat(Passphrase.parse("abcde xyz ecdab", ::anagrams).isValid()).isFalse()
        assertThat(Passphrase.parse("oiii ioii iioi iiio", ::anagrams).isValid()).isFalse()
    }

    @Test
    fun `it should count valid passphrases for puzzle 2`() {
        val lines = readLines("input")

        val passphrases = lines.map { Passphrase.parse(it, ::anagrams) }
        val validPassphrases = passphrases.count { it.isValid() }

        assertThat(validPassphrases).isEqualTo(231)
    }
}
