fun parse(s: String) = s.split("\n").filter { it.isNotBlank() }
        .map { it.split("/") }
        .map { (port1, port2) -> Component(port1.toInt(), port2.toInt())}

fun bridges(components: List<Component>, pins: Int = 0): List<Bridge> {
    val matchingComponents = components.filter { it.hasPort(pins) }
    val bridges = mutableListOf<Bridge>()

    for (component in matchingComponents) {
        val subBridges = bridges(components.filterNot { it == component }, component.otherPort(pins))
        bridges.add(Bridge(listOf(component)))
        bridges.addAll(subBridges.map { Bridge(listOf(component).plus(it.components)) })
    }

    return bridges
}

fun strength(s: String) =
        bridges(parse(s)).maxBy { it.strength }?.strength ?: 0

fun longest(s: String) =
        bridges(parse(s))
                .sortedWith(compareByDescending<Bridge> { it.components.size }.thenByDescending { it.strength })
                .first()

data class Component(private val port1: Int, private val port2: Int) {
    val strength = port1 + port2

    fun hasPort(pins: Int) = port1 == pins || port2 == pins
    fun otherPort(pins: Int) = if (port1 == pins) port2 else port1
}

class Bridge(val components: List<Component>) {
    val strength = components.sumBy { it.strength }
}