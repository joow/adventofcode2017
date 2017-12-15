fun generator(seed: Long, factor: Long, criteria: (Long) -> Boolean = { true }) =
        generateSequence(seed) { previous -> previous * factor % Int.MAX_VALUE }.drop(1).filter(criteria).iterator()

fun lowestBits(n: Long): Long = n and 0b1111111111111111

fun matches(generatorA: Iterator<Long>, generatorB: Iterator<Long>, rounds: Int = 40_000_000) =
        generateSequence { Pair(generatorA.next(), generatorB.next()) }
                .take(rounds)
                .count { pair -> lowestBits(pair.first) == lowestBits(pair.second) }