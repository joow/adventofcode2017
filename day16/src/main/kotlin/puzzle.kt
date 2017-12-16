fun record(programs: String, input: String, repeat: Int = 1): String {
    val moves = input.split(",")
    var dancingPrograms = programs
    var turn = 1

    while (turn <= repeat) {
        for (move in moves) {
            dancingPrograms = dance(dancingPrograms, move)
        }

        if (dancingPrograms == programs) {
            print("Cycle detected after $turn turns")
            val jump = repeat / turn - 1
            turn += turn * jump + 1
            println(", jumping to turn $turn...")
        } else turn++
    }

    return dancingPrograms
}

fun dance(programs: String, move: String) = when {
    move.startsWith('s') -> spin(programs, move)
    move.startsWith('x') -> exchange(programs, move)
    move.startsWith('p') -> partner(programs, move)
    else -> throw RuntimeException("unknown move: $move")
}

private fun spin(programs: String, move: String): String {
    val from = programs.length - move.drop(1).toInt()
    return programs.substring(from).plus(programs.take(from))
}

private fun exchange(programs: String, move: String): String {
    val from = move.drop(1).substringBefore('/').toInt()
    val to = move.drop(1).substringAfter('/').toInt()

    return programs.replaceRange(from..from, "${programs[to]}").replaceRange(to..to, "${programs[from]}")
}

private fun partner(programs: String, move: String): String {
    val p1 = move.drop(1).substringBefore("/")
    val p2 = move.drop(1).substringAfter("/")
    val pos1 = programs.indexOf(p1)
    val pos2 = programs.indexOf(p2)

    return if (pos1 < pos2) programs.replaceFirst(p2, p1).replaceFirst(p1, p2)
    else programs.replaceFirst(p1, p2).replaceFirst(p2, p1)
}