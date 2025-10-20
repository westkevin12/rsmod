package org.rsmod.api.drops

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.inject.Inject
import jakarta.inject.Singleton
import java.io.File
import kotlin.random.Random

private data class DropFile(val tables: List<NpcDropTable> = emptyList())

@Singleton
public class DropService @Inject constructor(mapper: ObjectMapper) {
    private val dropTables: Map<Int, NpcDropTable>

    init {
        val tempTables = mutableMapOf<Int, NpcDropTable>()
        val dropFile = File("content/drops/configs/drops.json")
        if (dropFile.exists()) {
            try {
                val drops: DropFile = mapper.readValue(dropFile)
                drops.tables.forEach { table -> tempTables[table.id] = table }
            } catch (e: Exception) {
                // TODO: log this error
                System.err.println("Failed to load drops.json: ${e.message}")
            }
        }
        dropTables = tempTables
    }

    public fun roll(npcId: Int): List<RolledDrop> {
        val table = dropTables[npcId] ?: return emptyList()
        val drops = mutableListOf<RolledDrop>()

        // Add guaranteed drops
        for (drop in table.guaranteed ?: emptyList()) {
            if (drop.id == 0) continue
            val count = if (drop.min >= drop.max) drop.min else Random.nextInt(drop.min, drop.max + 1)
            if (count > 0) {
                drops.add(RolledDrop(drop.id, count))
            }
        }

        // Roll for main drops
        val mainDrops = table.main ?: emptyList()
        val totalWeight = mainDrops.sumOf { it.weight }
        if (totalWeight > 0) {
            var roll = Random.nextInt(totalWeight)
            for (drop in mainDrops) {
                if (roll < drop.weight) {
                    if (drop.id != 0) {
                        val count = if (drop.min >= drop.max) drop.min else Random.nextInt(drop.min, drop.max + 1)
                        if (count > 0) {
                            drops.add(RolledDrop(drop.id, count))
                        }
                    }
                    break
                }
                roll -= drop.weight
            }
        }
        return drops
    }
}
