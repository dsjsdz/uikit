// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin("jvm") version "2.0.10"

    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.dokka) apply false
}

group = "io.github.awish.uikit"
version = "1.0.0"

dependencies {
    implementation(libs.central.publishing.maven.plugin)
}
