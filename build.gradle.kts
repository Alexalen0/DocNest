plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    // Remove KSP from root build.gradle.kts entirely
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}