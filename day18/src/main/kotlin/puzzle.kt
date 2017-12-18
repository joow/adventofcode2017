class SoundCard {
    val registers = mutableMapOf<String, Long>()
    val played: MutableList<Long> = mutableListOf()
    val recovered: MutableList<Long> = mutableListOf()

    private var line = 0

    fun evaluate(instructions: List<String>) {
        while (line < instructions.size) {
            val instruction = instructions[line]
            line += evaluate(instruction)
        }
    }

    private fun evaluate(instruction: String): Int =
        when (instruction.take(3)) {
            "set" -> set(instruction.drop(4))
            "add" -> add(instruction.drop(4))
            "mul" -> mul(instruction.drop(4))
            "mod" -> mod(instruction.drop(4))
            "snd" -> snd(instruction.drop(4))
            "rcv" -> rcv(instruction.drop(4))
            "jgz" -> jgz(instruction.drop(4))
            else -> throw RuntimeException("unknown instruction $instruction")
        }

    private fun getRegisterNameAndValue(instruction: String): Pair<String, Long> {
        val (registerName, term) = instruction.split(" ").take(2)
        val value = if (term.first().isLetter()) registers.getOrDefault(term, 0L)
                         else term.toLong()

        return Pair(registerName, value)
    }

    private fun set(instruction: String): Int {
        val (registerName, value) = getRegisterNameAndValue(instruction)
        registers[registerName] = value
        return 1
    }

    private fun add(instruction: String): Int {
        val (registerName, value) = getRegisterNameAndValue(instruction)
        val registerValue = registers.getOrDefault(registerName, 0)
        registers[registerName] = registerValue + value
        return 1
    }

    private fun mul(instruction: String): Int {
        val (registerName, value) = getRegisterNameAndValue(instruction)
        val registerValue = registers.getOrDefault(registerName, 0)
        registers[registerName] = registerValue * value
        return 1
    }

    private fun mod(instruction: String): Int {
        val (registerName, value) = getRegisterNameAndValue(instruction)
        val registerValue = registers.getOrDefault(registerName, 0)
        registers[registerName] = registerValue % value
        return 1
    }

    private fun snd(instruction: String): Int {
        played.add(registers.getOrDefault(instruction, 0))
        return 1
    }

    private fun rcv(instruction: String): Int {
        val registerValue = registers.getOrDefault(instruction, 0)
        if (registerValue != 0L) {
            recovered.add(played.last())
            return 100
        }

        return 1
    }

    private fun jgz(instruction: String): Int {
        val (registerName, value) = getRegisterNameAndValue(instruction)

        val registerValue = if (registerName.first().isLetter()) registers.getOrDefault(registerName, 0)
        else registerName.toLong()

        return if (registerValue > 0L) value.toInt() else 1
    }
}