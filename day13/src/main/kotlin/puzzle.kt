fun parse(s: String): Firewall {
    val lines = s.split("\n").filterNot { it.isBlank() }
    val layers = mutableListOf<Layer>()

    var currentDepth = 0
    for (line in lines) {
        val depth = line.substringBefore(':').trim().toInt()
        val range = line.substringAfter(':').trim().toInt()

        while (currentDepth < depth) {
            layers.add(Layer())
            currentDepth++
        }

        layers.add(Layer(range))
        currentDepth++
    }

    return Firewall(layers)
}

tailrec fun delay(firewall: Firewall, delay: Int = 0): Int {
    if (delay > 0) firewall.scan()

    val caught = firewall.copyOf().caught()

    return if (caught) delay(firewall, delay + 1) else delay
}

class Firewall(val layers: List<Layer>) {
    fun scan() = layers.forEach { it.scan() }

    fun traverse(): Int {
        var severity = 0

        for (depth in 0 until layers.size) {
            val layer = layers[depth]
            if (layer.scannerPosition == 0) {
                severity += (depth * layer.range)
            }
            scan()
        }

        return severity
    }

    fun caught(): Boolean {
        for (depth in 0 until layers.size) {
            val layer = layers[depth]
            if (layer.scannerPosition == 0) {
                return true
            }
            scan()
        }

        return false
    }

    fun copyOf() = Firewall(layers.map { it.copyOf() })
}

class Layer(val range: Int = 0) {

    var scannerPosition: Int = 0
    get() = if (range == 0) -1 else field

    var scannerAdvance: Int = 1

    fun scan() {
        if (range > 0) {
            scannerPosition += scannerAdvance

            if (scannerPosition == range - 1) scannerAdvance = -1
            else if (scannerPosition ==0) scannerAdvance = 1
        }
    }

    fun copyOf(): Layer {
        val copy = Layer(range)
        copy.scannerPosition = scannerPosition
        copy.scannerAdvance = scannerAdvance

        return copy
    }
}