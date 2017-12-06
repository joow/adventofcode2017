class Memory(val banks: List<Int>) {
    fun cycle(): Memory {
        val max = banks.max() ?: throw RuntimeException("memory should have banks")
        val index = banks.indexOfFirst { it == max }
        val updatedBanks = banks.toMutableList()

        var nbRemainingBlocks = max
        updatedBanks[index] = 0
        var i = index

        while (nbRemainingBlocks > 0) {
            i += 1
            if (i == updatedBanks.size) i = 0
            updatedBanks[i] += 1
            nbRemainingBlocks -= 1
        }

        return Memory(updatedBanks)
    }

    companion object {
        fun parse(input: String) = Memory(input.split("\t").map { it.toInt() })
    }
}

fun cycles(memory: Memory): Pair<Int, Memory> {
    val cycles = mutableSetOf(memory.banks)
    var nbCycles = 0
    var nextMemory = memory

    while (true) {
        nbCycles++
        nextMemory = nextMemory.cycle()
        if (!cycles.add(nextMemory.banks)) break
    }

    return Pair(nbCycles, nextMemory)
}