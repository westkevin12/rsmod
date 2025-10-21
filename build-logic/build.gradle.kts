repositories {
    gradlePluginPortal()
}

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.jmh.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.spotless.gradle.plugin)
    implementation(libs.jackson.dataformat.toml)
    implementation(libs.jackson.module.kotlin)
}
