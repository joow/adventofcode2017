class Passphrase(val words: List<String>) {
    fun isValid() = words.groupBy { it }.count() == words.count()

    companion object {
        fun parse(s: String, transform: (s: String) -> Iterable<String> = ::identity) =
            Passphrase(s.split(" ").flatMap { transform(it) })
    }
}

fun identity(s: String) = listOf(s)

fun anagrams(s: String) = permutations(s.toList()).map { String(it.toCharArray()) }.groupBy { it }.keys

fun <T> permutations(input: List<T>): List<List<T>> {
    if (input.size == 1) return listOf(input)
    val permutations = mutableListOf<List<T>>()
    val toInsert = input[0]
    for (perm in permutations(input.drop(1))) {
        for (i in 0..perm.size) {
            val newPermutation = perm.toMutableList()
            newPermutation.add(i, toInsert)
            permutations.add(newPermutation)
        }
    }
    return permutations
}
