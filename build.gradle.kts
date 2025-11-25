// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.android.library") version "8.5.2" apply false
}

android {
    namespace = "io.github.awish.uikit"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

group = "io.github.awish.uikit"
version = "1.0.0"

dependencies {
    // import custom jar of libs
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(libs.central.publishing.maven.plugin)

    // 测试依赖
    implementation(libs.guava)
    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.core)
}

kotlin {
    jvmToolchain(17)
}
