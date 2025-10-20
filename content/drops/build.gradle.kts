plugins {
    id("base-conventions")
    id("kotlin-conventions")
}

dependencies {
    implementation(projects.engine.plugin)
    implementation(projects.engine.game)
    implementation(projects.api.dropsCommons)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.module.kotlin)
}
