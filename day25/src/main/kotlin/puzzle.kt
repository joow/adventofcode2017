const val LEFT = -1
const val RIGHT = 1

class Turing(val values: MutableMap<Int, Int> = mutableMapOf(), val position: Int = 0, val state: State = StateA()) {
    val currentValue = values.getOrDefault(position, 0)

    fun step() = state.apply(this)
}

abstract class State {
    fun apply(turing: Turing): Turing {
        val (value, move, nextState) = parts(turing.currentValue)
        val values = turing.values
        values[turing.position] = value

        return Turing(values, turing.position + move, nextState)
    }

    abstract fun parts(currentValue: Int): Triple<Int, Int, State>
}

class StateA : State() {
    override fun parts(currentValue: Int) =
            if (currentValue == 0) Triple(1, RIGHT, StateB())
            else Triple(1, LEFT, StateE())
}

class StateB : State() {
    override fun parts(currentValue: Int) =
            if (currentValue == 0) Triple(1, RIGHT, StateC())
            else Triple(1, RIGHT, StateF())
}

class StateC : State() {
    override fun parts(currentValue: Int) =
            if (currentValue == 0) Triple(1, LEFT, StateD())
            else Triple(0, RIGHT, StateB())
}

class StateD : State() {
    override fun parts(currentValue: Int) =
            if (currentValue == 0) Triple(1, RIGHT, StateE())
            else Triple(0, LEFT, StateC())
}

class StateE : State() {
    override fun parts(currentValue: Int) =
            if (currentValue == 0) Triple(1, LEFT, StateA())
            else Triple(0, RIGHT, StateD())
}

class StateF : State() {
    override fun parts(currentValue: Int) =
            if (currentValue == 0) Triple(1, RIGHT, StateA())
            else Triple(1, RIGHT, StateC())
}