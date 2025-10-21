package org.rsmod.api.drops

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.michaelbull.logging.InlineLogger
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton
import java.io.File

private val logger = InlineLogger()

public class DropModule : AbstractModule() {
    @Provides
    @Singleton
    public fun provideDropTables(mapper: ObjectMapper): Map<Int, NpcDropTable> {
        val resource = this::class.java.classLoader.getResourceAsStream("drops.json")
        if (resource == null) {
            logger.warn { "Drop file not found in classpath: drops.json" }
            return emptyMap()
        }
        return try {
            resource.use { mapper.readValue(it) }
        } catch (e: Exception) {
            logger.error(e) { "Failed to load drops.json: ${e.message}" }
            emptyMap()
        }
    }
}
