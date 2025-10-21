package org.rsmod.gradle

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

private data class DropTableFile(val table: List<DropTable>)

private data class DropTable(val ids: List<Int>, val guaranteed: List<Drop>, val main: List<Drop> = emptyList())

private data class PackedDropTable(val guaranteed: List<Drop>, val main: List<Drop>)

private data class Drop(val id: Int, val min: Int, val max: Int, val weight: Int? = null)

public open class PackDrops : DefaultTask() {
    @get:InputFile
    public lateinit var inputFile: File

    @get:OutputFile
    public lateinit var outputFile: File

    @TaskAction
    public fun pack() {
        val tomlMapper = TomlMapper().registerModule(kotlinModule())
        val jsonMapper = ObjectMapper().registerModule(kotlinModule())

        val tableFile: DropTableFile = tomlMapper.readValue(inputFile)
        val packed = tableFile.table.flatMap { table ->
            table.ids.map { id ->
                id to PackedDropTable(table.guaranteed, table.main)
            }
        }.toMap()

        outputFile.parentFile.mkdirs()
        outputFile.writeText(jsonMapper.writeValueAsString(packed))
    }
}