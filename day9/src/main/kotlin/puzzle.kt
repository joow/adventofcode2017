fun score(group: Group): Int = group.score + group.groups.map { score(it) }.sum()

data class Group(val groups: List<Group>, val garbages: List<Garbage>, val score: Int)

/**
 * Result of parsing a group, returns the parsed group and the number of consumed characters.
 */
typealias GroupParsing = Pair<Group, Int>

class GroupParser {
    companion object {
        fun parse(s: String, score: Int = 1): GroupParsing {
            if (s.isEmpty()) throw RuntimeException("invalid group, should not be empty")
            if (s.first() != '{') throw RuntimeException("invalid group start, expected '{' instead of '${s.first()}'")

            val groups = mutableListOf<Group>()
            val garbages = mutableListOf<Garbage>()

            var i = 1
            while (i < s.length) {
                val c = s[i]
                when (c) {
                    '}' -> return GroupParsing(Group(groups, garbages, score), i + 1)
                    '{' -> {
                        val parsing = GroupParser.parse(s.drop(i), score + 1)
                        groups.add(parsing.first)
                        i += parsing.second
                    }
                    '<' -> {
                        val parsing = GarbageParser.parse(s.drop(i))
                        garbages.add(parsing.first)
                        i += parsing.second
                    }
                    else -> i += 1
                }
            }

            throw RuntimeException("invalid group end, expected '}'")
        }
    }
}

class Garbage

/**
 * Result of parsing a garbage, returns the parsed garbage and the number of consumed characters.
 */
typealias GarbageParsing = Pair<Garbage, Int>

class GarbageParser {
    companion object {
        fun parse(s: String): GarbageParsing {
            var i = 1
            while (i < s.length) {
                val c = s[i]
                when (c) {
                    '>' -> return GarbageParsing(Garbage(), i + 1)
                    '!' -> i += 2
                    else -> i += 1
                }
            }

            throw RuntimeException("invalid group end, expected '}'")
        }
    }
}