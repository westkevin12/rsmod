package org.rsmod.api.drops

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class DropServiceTest {
    private lateinit var mapper: ObjectMapper
    private lateinit var dropService: DropService
    private lateinit var mockRandom: kotlin.random.Random

    @BeforeEach
    fun setUp() {
        mapper = ObjectMapper().registerModule(kotlinModule())
        val dropFile = File("build/resources/test/drops.json")
        val dropTables: Map<Int, NpcDropTable> = mapper.readValue(dropFile)
        mockRandom = mock()
        whenever(mockRandom.nextInt(any())).thenReturn(0)
        whenever(mockRandom.nextInt(any(), any())).thenReturn(0)
        dropService = DropService(dropTables, mockRandom)
    }

    @Test
    fun test_guaranteed_and_main_drops() {
        val mockRandom = mock<kotlin.random.Random>()
        whenever(mockRandom.nextInt(1, 2)).thenReturn(1)
        whenever(mockRandom.nextInt(1)).thenReturn(0)

        val dropTables =
            mapOf(
                1 to
                    NpcDropTable(
                        guaranteed = listOf(Drop(id = 100, min = 1, max = 1)),
                        main = listOf(Drop(id = 200, min = 1, max = 1, weight = 1)),
                    )
            )
        val dropService = DropService(dropTables, mockRandom)
        val drops = dropService.roll(1)
        assertEquals(2, drops.size)
        assertEquals(100, drops[0].id)
        assertEquals(1, drops[0].count)
        assertEquals(200, drops[1].id)
        assertEquals(1, drops[1].count)
    }

    @Test
    fun test_second_npc_drops() {
        val mockRandom = mock<kotlin.random.Random>()
        whenever(mockRandom.nextInt(1)).thenReturn(0)
        whenever(mockRandom.nextInt(1, 2)).thenReturn(1)

        val dropTables =
            mapOf(2 to NpcDropTable(main = listOf(Drop(id = 300, min = 1, max = 1, weight = 1))))
        val dropService = DropService(dropTables, mockRandom)
        val drops = dropService.roll(2)
        assertEquals(1, drops.size)
        assertEquals(300, drops[0].id)
        assertEquals(1, drops[0].count)
    }
}
