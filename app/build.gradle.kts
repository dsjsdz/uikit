plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
}

android {
    namespace = "io.github.awish.uikit"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }
}

dependencies {
    // import custom jar of libs
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.core.ktx)

    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
}

kotlin {
    jvmToolchain(17)
}

