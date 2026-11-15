plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    // Type-safe navigation routes and arguments.
    // Version follows `kotlin` in libs.versions.toml.
    alias(libs.plugins.kotlin.serialization)

    // Kotlin Symbol Processing, used by the Room compiler.
    // Version is declared outside this file (root build.gradle.kts or settings.gradle.kts).
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.dylanbeebe.fuelbuddy"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.dylanbeebe.fuelbuddy"
        minSdk = 34
        targetSdk = 36
        versionCode = 4
        versionName = "1.3.1"

        // Required for src/androidTest (connectedDebugAndroidTest).
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Code shrinking disabled: unused code and icons remain in the release APK.
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    signingConfigs {
        create("release") {
            storeFile = System.getenv("KEYSTORE_PATH")?.let(::file)
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {
    // ---- Compose ----
    // The BOM supplies the version of every androidx.compose.* artifact below.
    // Do not add versions to these entries in libs.versions.toml.
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3)
    // Includes material-icons-core. Large without code shrinking.
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // ---- AndroidX core and lifecycle ----
    // All lifecycle artifacts share the single `lifecycle` version.
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // ViewModel integration for Compose.
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)

    // ---- Navigation ----
    implementation(libs.androidx.navigation.compose)
    // Serialization for type-safe routes. Requires the serialization plugin above.
    implementation(libs.kotlinx.serialization.json)

    // ---- Persistence (Room) ----
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // ---- Images ----
    implementation(libs.coil.compose)
    // Jetpack photo picker. Alpha artifact: API may change.
    implementation(libs.androidx.photopicker.compose)

    // ---- Local unit tests (src/test) ----
    testImplementation(libs.junit)

    // ---- Instrumented tests (src/androidTest) ----
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    // Navigation testing (TestNavHostController).
    androidTestImplementation(libs.androidx.navigation.testing)

    // ---- Debug builds only ----
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}