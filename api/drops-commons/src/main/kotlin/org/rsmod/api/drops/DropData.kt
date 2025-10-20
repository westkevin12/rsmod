package org.rsmod.api.drops

public data class NpcDropTable(
    val id: Int = 0,
    val guaranteed: List<Drop> = emptyList(),
    val main: List<Drop> = emptyList(),
)

public data class Drop(val id: Int = 0, val min: Int = 0, val max: Int = 0, val weight: Int = 0)

public data class RolledDrop(val id: Int, val count: Int)
