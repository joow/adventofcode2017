class Passphrase(val words: List<String>) {
    fun isValid() = words.groupBy { it }.count() == words.count()

    companion object {
        fun parse(s: String) = Passphrase(s.split(" "))
    }
}