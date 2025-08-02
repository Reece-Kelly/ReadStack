plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.assignmentapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.assignmentapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose BOM (central versioning)
    implementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.junit.junit)
    testImplementation(libs.junit.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // Compose UI
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.material.icons.extended)

    // Material 3
    implementation(libs.androidx.compose.material3.material32) // If this is a different version

    // Navigation
    implementation(libs.compose.navigation)
    implementation(libs.androidx.navigation.compose)

    // Lifecycle & Activity
    implementation(libs.compose.lifecycle)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.preference.ktx)

    // Coil for image loading
    implementation(libs.coil.compose)

    // Networking
    implementation(libs.bundles.networking)
    implementation(libs.converter.gson)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)

    // Dependency Injection (Koin)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // Room Database
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Experimental XR Compose (optional)
    implementation("androidx.xr.compose:compose:1.0.0-alpha04")

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.androidx.junit.v115)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
