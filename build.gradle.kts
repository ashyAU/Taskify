
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin("jvm") version "1.9.10" apply false // or the latest stable version
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false


}
