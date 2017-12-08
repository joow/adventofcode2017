fun evaluate(input: String): Pair<Map<String, Int>, Int>  {
    tailrec fun evaluate(instructions: List<Instruction>,
                         registers: MutableMap<String, Int> = mutableMapOf(),
                         max: Int = 0): Pair<Map<String, Int>, Int> {
        if (instructions.isEmpty()) return Pair(registers, max)

        val instruction = instructions.first()
        val registerValue = registers.getOrDefault(instruction.register, 0)
        val registerValueForCondition = registers.getOrDefault(instruction.condition.register, 0)

        if (instruction.condition.accept(registerValueForCondition))
            registers[instruction.register] = instruction.operation.apply(registerValue)

        val newMax = if (max < registers[instruction.register] ?: 0) registers[instruction.register] ?: max else max

        return evaluate(instructions.drop(1), registers, newMax)
    }


    val instructions = input.split("\n").filterNot { it.isBlank() }.map { Instruction.parse(it) }

    return evaluate(instructions)
}

fun max(input: String) = evaluate(input).first.values.max() ?: 0

fun maxHeld(input: String) = evaluate(input).second

class Instruction(val register: String, val operation: Operation, val condition: Condition) {
    companion object {
        fun parse(s: String): Instruction {
            val register = s.substringBefore(' ')
            val operation = Operation.parse(s.substringAfter(' ').substringBefore(" if"))
            val condition = Condition.parse(s.substringAfter("if "))

            return Instruction(register, operation, condition)
        }
    }
}

sealed class Operation(val value: Int) {
    abstract fun apply(n: Int): Int

    companion object {
        fun parse(s: String): Operation {
            val op = s.substringBefore(' ')
            val value = s.substringAfter(' ').toInt()

            return when (op) {
                "inc" -> Inc(value)
                "dec" -> Dec(value)
                else -> throw RuntimeException("unknow op code $op")
            }

        }
    }
}

class Inc(value: Int) : Operation(value) {
    override fun apply(n: Int) = n + value
}

class Dec(value: Int) : Operation(value) {
    override fun apply(n: Int) = n - value
}

sealed class Condition(val register: String, val value: Int) {
    abstract fun accept(n: Int): Boolean

    companion object {
        fun parse(s: String): Condition {
            val parts = s.split(' ')
            val register = parts[0]
            val condition = parts[1]
            val value = parts[2].toInt()

            return when(condition) {
                "==" -> Equals(register, value)
                "!=" -> NotEquals(register, value)
                ">" -> GreaterThan(register, value)
                ">=" -> GreaterThanOrEquals(register, value)
                "<" -> LessThan(register, value)
                "<=" -> LessThanOrEquals(register, value)
                else -> throw RuntimeException("unknown condition code $condition")
            }
        }
    }
}

class Equals(register: String, value: Int) : Condition(register, value) {
    override fun accept(n: Int) = n == value
}

class NotEquals(register: String, value: Int) : Condition(register, value) {
    override fun accept(n: Int) = n != value
}

class GreaterThan(register: String, value: Int) : Condition(register, value) {
    override fun accept(n: Int) = n > value
}

class GreaterThanOrEquals(register: String, value: Int) : Condition(register, value) {
    override fun accept(n: Int) = n >= value
}

class LessThan(register: String, value: Int) : Condition(register, value) {
    override fun accept(n: Int) = n < value
}

class LessThanOrEquals(register: String, value: Int) : Condition(register, value) {
    override fun accept(n: Int) = n <= value
}