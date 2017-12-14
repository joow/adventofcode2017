fun parse(s: String): Firewall {
    val lines = s.split("\n").filterNot { it.isBlank() }
    val layers = mutableListOf<Layer>()

    var currentDepth = 0
    for (line in lines) {
        val depth = line.substringBefore(':').trim().toInt()
        val range = line.substringAfter(':').trim().toInt()

        while (currentDepth < depth) {
            layers.add(NoScannerLayer())
            currentDepth++
        }

        layers.add(ScannerLayer(range))
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
            if (layer.isAtTop()) {
                severity += (depth * layer.range())
            }
            scan()
        }

        return severity
    }

    fun caught(): Boolean {
        for (depth in 0 until layers.size) {
            val layer = layers[depth]
            if (layer.isAtTop()) {
                return true
            }
            scan()
        }

        return false
    }

    fun copyOf() = Firewall(layers.map { it.copyOf() })
}

sealed class Layer {
    abstract fun scan()
    abstract fun range(): Int
    abstract fun isAtTop(): Boolean
    abstract fun copyOf(): Layer
}

class ScannerLayer(val range: Int) : Layer() {

    private var scannerPosition: Int = 0

    private val moves = (range - 1) * 2

    override fun scan() {
        scannerPosition++
    }

    override fun range() = range

    override fun isAtTop() = scannerPosition % moves == 0

    override fun copyOf(): Layer {
        val copy = ScannerLayer(range)
        copy.scannerPosition = scannerPosition

        return copy
    }
}

class NoScannerLayer : Layer() {
    override fun scan() {}
    override fun range() = 0
    override fun isAtTop() = false
    override fun copyOf() = this
}