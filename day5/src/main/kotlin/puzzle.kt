class Instructions(val jumps: List<Int>, val curr: Int = 0, val steps: Int = 0) {
    fun jump(): Instructions {
        val newCurr = curr + jumps[curr]
        return Instructions(transform(), newCurr, steps + 1)
    }

    private fun transform() = jumps.take(curr).plus(jumps[curr] + 1).plus(jumps.drop(curr + 1))

    fun isOut() = curr >= jumps.size

    override fun toString() =
            jumps.mapIndexed { index, n -> if (index == curr) "($n)" else "$n" }.joinToString()
}

fun steps(instructions: Instructions) =
        generateSequence(instructions) { it.jump() }.first { it.isOut() }.steps
