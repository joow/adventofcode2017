import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Tests {

    @Test
    fun `it should not parse empty string`() {
        try {
            GroupParser.parse("")
        } catch (e: RuntimeException) {
            assertThat(e.message).isEqualTo("invalid group, should not be empty")
        }
    }

    @Test
    fun `it should not parse string with invalid starting character`() {
        try {
            GroupParser.parse("not a valid group")
        } catch (e: RuntimeException) {
            assertThat(e.message).isEqualTo("invalid group start, expected '{' instead of 'n'")
        }
    }

    @Test
    fun `it should parse empty group`() {
        val input = "{}"

        val parsing = GroupParser.parse(input)

        assertThat(parsing.first).isNotNull()
        assertThat(parsing.first.groups).isEmpty()
    }

    @Test
    fun `it should parse three empty groups`() {
        val input = "{{{}}}"

        val parsing = GroupParser.parse(input)

        assertThat(parsing.first.groups).hasSize(1)
        assertThat(parsing.first.groups.first().groups).hasSize(1)
    }

    @Test
    fun `it should parse comma separated empty groups`() {
        val input = "{{},{}}"

        val parsing = GroupParser.parse(input)

        assertThat(parsing.first.groups).hasSize(2)
    }

    @Test
    fun `it should parse multiple nested empty groups`() {
        val input = "{{{},{},{{}}}}"

        val parsing = GroupParser.parse(input)

        assertThat(parsing.first.groups).hasSize(1)
    }

    @Test
    fun `it should parse group containing garbage`() {
        val input = "{<{},{},{{}}>}"

        val parsing = GroupParser.parse(input)

        assertThat(parsing.first.groups).isEmpty()
        assertThat(parsing.first.garbages).hasSize(1)
    }

    @Test
    fun `it should parse group containing multiple garbages`() {
        val input = "{<a>,<a>,<a>,<a>}"

        val parsing = GroupParser.parse(input)

        assertThat(parsing.first.groups).isEmpty()
        assertThat(parsing.first.garbages).hasSize(4)
    }

    @Test
    fun `it should parse group containing groups containing garbages`() {
        val input = "{{<a>},{<a>},{<a>},{<a>}}"

        val parsing = GroupParser.parse(input)

        assertThat(parsing.first.groups).hasSize(4)
        assertThat(parsing.first.garbages).isEmpty()
    }

    @Test
    fun `it should parse group containing garbage with canceling character`() {
        val input = "{{<!>},{<!>},{<!>},{<a>}}"

        val parsing = GroupParser.parse(input)

        assertThat(parsing.first.groups).hasSize(1)
    }

    @Test
    fun `it should compute score of an empty group`() {
        val input = "{}"

        val parsing = GroupParser.parse(input)

        assertThat(score(parsing.first)).isEqualTo(1)
    }

    @Test
    fun `it should compute score of a group containing nested groups`() {
        val input = "{{{}}}"

        val parsing = GroupParser.parse(input)

        assertThat(score(parsing.first)).isEqualTo(6)
    }

    @Test
    fun `it should compute score of a group containing two groups`() {
        val input = "{{},{}}"

        val parsing = GroupParser.parse(input)

        assertThat(score(parsing.first)).isEqualTo(5)
    }

    @Test
    fun `it should compute score of a group containing groups containing groups`() {
        val input = "{{{},{},{{}}}}"

        val parsing = GroupParser.parse(input)

        assertThat(score(parsing.first)).isEqualTo(16)
    }

    @Test
    fun `it should compute score of a group containing only garbage`() {
        val input = "{<a>,<a>,<a>,<a>}"

        val parsing = GroupParser.parse(input)

        assertThat(score(parsing.first)).isEqualTo(1)
    }

    @Test
    fun `it should compute score of a group containing groups containing garbage`() {
        val input = "{{<ab>},{<ab>},{<ab>},{<ab>}}"

        val parsing = GroupParser.parse(input)

        assertThat(score(parsing.first)).isEqualTo(9)
    }

    @Test
    fun `it should compute score of a group containing groups containing garbage with canceling character`() {
        val input = "{{<!!>},{<!!>},{<!!>},{<!!>}}"

        val parsing = GroupParser.parse(input)

        assertThat(score(parsing.first)).isEqualTo(9)
    }

    @Test
    fun `it should compute score of a group containing groups containing garbage and canceling character`() {
        val input = "{{<a!>},{<a!>},{<a!>},{<ab>}}"

        val parsing = GroupParser.parse(input)

        assertThat(score(parsing.first)).isEqualTo(3)
    }

    @Test
    fun `it should compute score for puzzle input`() {
        val input = Tests::class.java.getResource("input").readText()

        val parsing = GroupParser.parse(input)

        assertThat(score(parsing.first)).isEqualTo(13154)
    }

    @Test
    fun `it should count canceled characters for empty group`() {
        val input = "{}"

        val parsing = GroupParser.parse(input)

        assertThat(nonCanceled(parsing.first)).isEqualTo(0)
    }

    @Test
    fun `it should count non-canceled characters for garbage`() {
        val garbage = "<random characters>"

        val parsing = GarbageParser.parse(garbage)

        assertThat(parsing.first.nonCanceled).isEqualTo(17)
    }

    @Test
    fun `it should count non-canceled characters for nested garbages`() {
        val garbage = "<<<<>"

        val parsing = GarbageParser.parse(garbage)

        assertThat(parsing.first.nonCanceled).isEqualTo(3)
    }

    @Test
    fun `it should count non-canceled characters for garbage with canceling characted`() {
        val garbage = "<{!>}>"

        val parsing = GarbageParser.parse(garbage)

        assertThat(parsing.first.nonCanceled).isEqualTo(2)
    }

    @Test
    fun `it should count non-canceled characters for garbage with only canceling character`() {
        val garbage = "<!!>"

        val parsing = GarbageParser.parse(garbage)

        assertThat(parsing.first.nonCanceled).isEqualTo(0)
    }

    @Test
    fun `it should count non-canceled characters for garbage with multiple canceling characters`() {
        val garbage = "<!!!>>"

        val parsing = GarbageParser.parse(garbage)

        assertThat(parsing.first.nonCanceled).isEqualTo(0)
    }

    @Test
    fun `it should count non-canceled characters for garbage with random characters mixed`() {
        val garbage = "<{o\"i!a,<{i<a>"

        val parsing = GarbageParser.parse(garbage)

        assertThat(parsing.first.nonCanceled).isEqualTo(10)
    }

    @Test
    fun `it should count non-canceled characters for puzzle input`() {
        val garbage = Tests::class.java.getResource("input").readText()

        val parsing = GroupParser.parse(garbage)

        assertThat(nonCanceled(parsing.first)).isEqualTo(6369)
    }
 }
