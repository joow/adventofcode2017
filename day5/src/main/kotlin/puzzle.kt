class Instructions(val jumps: MutableList<Int>, val curr: Int = 0, val steps: Int = 0) {
    fun jump(inc: (offset: Int) -> Int): Instructions {
        val jump = jumps[curr]
        return Instructions(transform(inc), curr + jump, steps + 1)
    }

    private fun transform(inc: (offset: Int) -> Int): MutableList<Int> {
        jumps[curr] = inc(jumps[curr])
        return jumps
    }


    fun isOut() = curr >= jumps.size

    override fun toString() =
            jumps.mapIndexed { index, n -> if (index == curr) "($n)" else "$n" }.joinToString()
}

fun steps(instructions: Instructions, inc: (offset: Int) -> Int = { it + 1 }) =
        generateSequence(instructions) { it.jump(inc) }.first { it.isOut() }.steps
