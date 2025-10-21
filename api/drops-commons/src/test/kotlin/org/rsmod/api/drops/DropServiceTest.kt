package org.rsmod.api.drops

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class DropServiceTest {
    private lateinit var mapper: ObjectMapper
    private lateinit var dropService: DropService

    @BeforeEach
    fun setUp() {
        mapper = ObjectMapper().registerModule(kotlinModule())
        val dropFile = File("build/resources/test/drops.json")
        val dropTables: Map<Int, NpcDropTable> = mapper.readValue(dropFile)
        dropService = DropService(dropTables)
    }

    @Test
    fun `test guaranteed and main drops`() {
        val drops = dropService.roll(1)
        assertEquals(2, drops.size)
        assertTrue(drops.any { it.id == 100 && it.count == 1 })
        assertTrue(drops.any { it.id == 200 && it.count == 1 })
    }

    @Test
    fun `test second npc drops`() {
        val drops = dropService.roll(2)
        assertEquals(2, drops.size)
        assertTrue(drops.any { it.id == 101 && it.count == 2 })
        assertTrue(drops.any { it.id == 300 && it.count == 1 })
    }
}
