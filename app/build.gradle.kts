plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp") // KSP for annotation processing

}

android {
    namespace = "com.example.remindme"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.remindme"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation("com.google.dagger:hilt-android:2.48") // Hilt dependencies
    kapt("com.google.dagger:hilt-compiler:2.48") // Hilt compiler for KAPT
    implementation( libs.androidx.room.runtime.v250) // Room runtime
    ksp (libs.androidx.room.compiler.v250) // Room compiler for KAPT
    implementation( libs.androidx.hilt.navigation.compose.v100) // Hilt for Jetpack Compose

    implementation(libs.androidx.activity.compose.v160) // For ViewModel support in Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)// Compose ViewModel support

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.graphics.shapes)
    implementation(libs.androidx.graphics.core)
    implementation(libs.androidx.graphics.path)
    implementation(libs.androidx.graphics.shapes)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


}
kapt {
    correctErrorTypes = true
}
