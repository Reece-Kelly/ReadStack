plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.serialization) // ✅ Use this only
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
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.material.icons.extended)

    implementation(libs.bundles.networking)

    implementation(libs.koin.core)
    // Koin for Android
    implementation(libs.koin.android)
    // Koin for Jetpack Compose (if you're using Koin to inject into Composables directly)
    implementation(libs.koin.androidx.compose)
    // Koin for AndroidX ViewModel (if you're injecting ViewModels)
//    implementation(libs.koin.androidx.viewmodel)

    implementation(libs.retrofit) // Check for the latest version

    implementation(libs.converter.gson) // For Gson

    implementation(libs.kotlinx.serialization.json) // latest as of mid-2025

    implementation(libs.retrofit2.kotlinx.serialization.converter)

    implementation(libs.okhttp) // latest as of mid-2025


    implementation(libs.coil.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
