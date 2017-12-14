class Group(val programs: MutableSet<Int> = mutableSetOf())

fun parse(input: String): List<Group> {
    val groups = mutableListOf<Group>()

    val lines = input.split("\n").filterNot { it.isBlank() }
    lines.forEach { line ->
        val programs = line.replace("<->", ",").split(",")
                .map { it.trim() }
                .map { it.toInt() }
                .distinct()
                .toSet()

        val group = groups.firstOrNull { it.programs.intersect(programs).isNotEmpty() }

        if (group == null) groups.add(Group(programs.toMutableSet()))
        else group.programs.addAll(programs.subtract(group.programs))
    }

    return regroup(regroup(groups))
}

private tailrec fun regroup(groups: List<Group>, acc: MutableList<Group> = mutableListOf()): List<Group> {
    if (groups.isEmpty()) return acc

    val group = groups.first()
    var mergingGroup = acc.firstOrNull { group.programs.intersect(it.programs).isNotEmpty() }

    if (mergingGroup == null) {
        mergingGroup = Group()
        acc.add(mergingGroup)
    }

    mergingGroup.programs.addAll(group.programs.subtract(mergingGroup.programs))

    return regroup(groups.drop(1), acc)
}
