class Tower(val name: String, var weight: Int, var subtowers: List<Tower> = emptyList()) {

    fun subtowers(acc: List<Tower> = emptyList()): List<Tower> {
        if (subtowers.isEmpty()) return acc
        else return subtowers.flatMap { it.subtowers(listOf(it)) }
    }

    fun totalWeight(): Int {
        if (subtowers.isEmpty()) return weight
        else return subtowers.map { it.totalWeight() }.sum() + weight
    }

    fun invalid(): Pair<Tower, Int>? {
        val groupByTotalWeight = subtowers.groupBy { it.totalWeight() }
        val invalid = groupByTotalWeight.filterValues { it.size == 1 }.values
        val validTotalWeight = groupByTotalWeight.filterValues { it.size > 1 }.keys.first()

        if (invalid.isEmpty()) return null
        return Pair(invalid.first().first(), validTotalWeight)
    }

    fun correctWeight(): Int {
        var invalid = invalid()
        var nextInvalid = invalid
        while (nextInvalid != null) {
            invalid = nextInvalid
            nextInvalid = invalid.first.invalid()
        }

        val invalidTower = invalid?.first
        val validTotalWeight = invalid?.second ?:0
        val difference = (invalidTower?.totalWeight() ?: 0) - validTotalWeight
        val correctedWeight = (invalidTower?.weight ?: 0) - difference

        return correctedWeight
    }

    companion object {
        fun parse(s: String): Tower {
            fun parseTower(s: String, towers: MutableList<Tower>) {
                val name = s.substringBefore(" ")
                val weight = s.substringAfter("(").substringBefore(")").toInt()

                val subtowers = mutableListOf<Tower>()
                s.substringAfter("->", "").split(",")
                        .map(String::trim)
                        .filterNot(String::isBlank)
                        .forEach { subprogram ->
                            val subtower = towers.firstOrNull { it.name == subprogram }

                            if (subtower == null) subtowers.add(Tower(subprogram, 0))
                            else {
                                subtowers.add(subtower)
                                towers.remove(subtower)
                            }
                        }

                val tower = towers.flatMap { it.subtowers() }.firstOrNull { it.name == name }
                if (tower == null) {
                    towers.add(Tower(name, weight, subtowers))
                } else {
                    tower.weight = weight
                    tower.subtowers = subtowers
                }
            }

            val towers = mutableListOf<Tower>()
            s.split("\n")
                    .filterNot(String::isBlank)
                    .forEach { parseTower(it, towers) }

            return towers.first()
        }
    }
}
