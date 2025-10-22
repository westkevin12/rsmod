import org.rsmod.gradle.PackDrops

plugins {
    id("base-conventions")
    id("kotlin-conventions")
}

val packDrops = tasks.register("packDrops", PackDrops::class.java) {
    inputFile = project(":content:drops").file("src/main/resources/drops.toml")
    outputFile = layout.buildDirectory.file("resources/main/drops.json").get().asFile
}

val packTestDrops = tasks.register("packTestDrops", PackDrops::class.java) {
    inputFile = project(":content:drops").file("src/main/resources/drops.toml")
    outputFile = layout.buildDirectory.file("resources/test/drops.json").get().asFile
    outputs.upToDateWhen { false }
}

tasks.named("jar") {
    dependsOn(packDrops)
}

tasks.named("test") {
    dependsOn(packTestDrops)
}

dependencies {
    implementation(projects.engine.plugin)
    implementation(projects.engine.game)
    implementation(libs.bundles.logging)
    implementation(libs.jakarta.inject)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.guice)

    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
}

