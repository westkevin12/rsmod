plugins {
    id("base-conventions")
    id("kotlin-conventions")
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(projects.engine.plugin)
    implementation(projects.engine.game)
    implementation(libs.jakarta.inject)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.module.kotlin)
}
