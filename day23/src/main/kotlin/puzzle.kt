import kotlin.math.sqrt

class Coprocessor(private val registers: MutableMap<String, Int> = mutableMapOf()) {
    private var line = 0
    private var invocations = 0
    var mulInvocations = 0

    fun evaluate(vararg instructions: String): MutableMap<String, Int> {
        while (line in 0 until instructions.size) {
            val (instruction, op1, op2) = instructions[line].split(" ")

            when (instruction) {
                "set" -> set(op1, op2)
                "sub" -> sub(op1, op2)
                "mul" -> mul(op1, op2)
                "jnz" -> jnz(op1, op2)
            }

            invocations++
            if (invocations % 10_000_000 == 0) println("invocations: $invocations, ${registers}")
        }

        return registers
    }

    private fun getValue(s: String) =
            if (s.first().isLetter()) registers.getOrDefault(s, 0) else s.toInt()

    private fun set(op1: String, op2: String) {
        registers[op1] = getValue(op2)
        line++
    }

    private fun sub(op1: String, op2: String) {
        registers[op1] = getValue(op1) - getValue(op2)
        line++
    }

    private fun mul(op1: String, op2: String) {
        registers[op1] = getValue(op1) * getValue(op2)
        mulInvocations++
        line++
    }

    private fun jnz(op1: String, op2: String) {
        if (getValue(op1) == 0) line++ else line += getValue(op2)
    }
}

fun isPrime(n: Int) = n > 1 && (2..sqrt(n.toDouble()).toInt()).none { n % it == 0 }