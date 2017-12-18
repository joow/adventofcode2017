import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.delay

class SoundCard(val id: Long = 0) {
    val registers = mutableMapOf("p" to id)
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

    private fun getValue(term: String) =
            if (term.first().isLetter()) registers.getOrDefault(term, id) else term.toLong()

    private fun getRegisterNameAndValue(instruction: String): Pair<String, Long> {
        val (registerName, term) = instruction.split(" ").take(2)
        val value = getValue(term)

        return Pair(registerName, value)
    }

    private fun set(instruction: String): Int {
        val (registerName, value) = getRegisterNameAndValue(instruction)
        registers[registerName] = value
        return 1
    }

    private fun add(instruction: String): Int {
        val (registerName, value) = getRegisterNameAndValue(instruction)
        val registerValue = getValue(registerName)
        registers[registerName] = registerValue + value
        return 1
    }

    private fun mul(instruction: String): Int {
        val (registerName, value) = getRegisterNameAndValue(instruction)
        val registerValue = getValue(registerName)
        registers[registerName] = registerValue * value
        return 1
    }

    private fun mod(instruction: String): Int {
        val (registerName, value) = getRegisterNameAndValue(instruction)
        val registerValue = getValue(registerName)
        registers[registerName] = registerValue % value
        return 1
    }

    private fun snd(instruction: String): Int {
        played.add(getValue(instruction))
        return 1
    }

    private fun rcv(instruction: String): Int {
        val registerValue = getValue(instruction)
        if (registerValue != 0L) {
            recovered.add(played.last())
            return 100
        }

        return 1
    }

    private fun jgz(instruction: String): Int {
        val (registerName, value) = getRegisterNameAndValue(instruction)
        val registerValue = getValue(registerName)

        return if (registerValue > 0L) value.toInt() else 1
    }
}

class Program(val id: Long = 0, private val chan: Channel<Long>, private val queue: ReceiveChannel<Long>) {

    val registers = mutableMapOf("p" to id)
    var sent = 0

    private var line = 0

    suspend fun evaluate(instructions: List<String>) {
        while (line < instructions.size) {
            val instruction = instructions[line]
            line += evaluate(instruction)
        }
    }

    suspend private fun evaluate(instruction: String): Int =
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

    private fun getValue(term: String) =
            if (term.first().isLetter()) registers.getOrDefault(term, id) else term.toLong()

    private fun getRegisterNameAndValue(instruction: String): Pair<String, Long> {
        val (registerName, term) = instruction.split(" ").take(2)
        val value = getValue(term)

        return Pair(registerName, value)
    }

    private fun set(instruction: String): Int {
        val (registerName, value) = getRegisterNameAndValue(instruction)
        registers[registerName] = value
        return 1
    }

    private fun add(instruction: String): Int {
        val (registerName, value) = getRegisterNameAndValue(instruction)
        val registerValue = getValue(registerName)
        registers[registerName] = registerValue + value
        return 1
    }

    private fun mul(instruction: String): Int {
        val (registerName, value) = getRegisterNameAndValue(instruction)
        val registerValue = getValue(registerName)
        registers[registerName] = registerValue * value
        return 1
    }

    private fun mod(instruction: String): Int {
        val (registerName, value) = getRegisterNameAndValue(instruction)
        val registerValue = getValue(registerName)
        registers[registerName] = registerValue % value
        return 1
    }

    suspend private fun snd(instruction: String): Int {
        val value = getValue(instruction)
        sent++
        chan.send(value)
        return 1
    }

    suspend private fun rcv(instruction: String): Int {
        registers[instruction] = queue.receive()

        return 1
    }

    private fun jgz(instruction: String): Int {
        val (registerName, value) = getRegisterNameAndValue(instruction)
        val registerValue = getValue(registerName)

        return if (registerValue > 0L) value.toInt() else 1
    }
}

suspend fun evaluate(instructions: List<String>): Int {
    val chan0 = Channel<Long>(50_000)
    val chan1 = Channel<Long>(50_000)
    val program0 = Program(0, chan0, chan1)
    val program1 = Program(1, chan1, chan0)

    async { program0.evaluate(instructions) }
    async { program1.evaluate(instructions) }

    delay(3_000)

    return program1.sent
}