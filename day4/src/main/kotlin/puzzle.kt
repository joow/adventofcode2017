class Passphrase(val words: List<String>) {
    fun isValid() = words.groupBy { it }.count() == words.count()

    companion object {
        fun parse(s: String, transform: (s: String) -> List<String> = ::identity) =
            Passphrase(s.split(" ").flatMap { transform(it) })
    }
}

fun identity(s: String) = listOf(s)
