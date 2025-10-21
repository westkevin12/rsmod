package org.rsmod.api.drops

import com.github.michaelbull.logging.InlineLogger
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlin.random.Random

private val logger = InlineLogger()

private data class DropFile(val tables: List<NpcDropTable> = emptyList())

private data class SharedDropFile(val tables: List<SharedDropTable> = emptyList())

private data class SharedDropTable(val name: String, val main: List<Drop> = emptyList())

@Singleton
public class DropService @Inject constructor(private val dropTables: Map<Int, NpcDropTable>) {
    public fun roll(npcId: Int): List<RolledDrop> {
        val table = dropTables[npcId] ?: return emptyList()
        val drops = mutableListOf<RolledDrop>()

        // Add guaranteed drops
        for (drop in table.guaranteed) {
            if (drop.id == 0) continue
            val count =
                if (drop.min >= drop.max) drop.min else Random.nextInt(drop.min, drop.max + 1)
            if (count > 0) {
                drops.add(RolledDrop(drop.id, count))
            }
        }

        // Roll for main drops
        val mainDrops = table.main
        val totalWeight = mainDrops.sumOf { it.weight ?: 0 }
        if (totalWeight > 0) {
            var roll = Random.nextInt(totalWeight)
            for (drop in mainDrops) {
                val weight = drop.weight ?: 0
                if (roll < weight) {
                    if (drop.id != 0) {
                        val count =
                            if (drop.min >= drop.max) drop.min
                            else Random.nextInt(drop.min, drop.max + 1)
                        if (count > 0) {
                            drops.add(RolledDrop(drop.id, count))
                        }
                    }
                    break
                }
                roll -= weight
            }
        }
        return drops
    }
}
