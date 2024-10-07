plugins {
    id("com.android.application")
    kotlin("android") version "1.9.10" // Update Kotlin version here
}

android {
    namespace = "com.example.tehtava2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tehtava2"
        minSdk = 26  // Minimum SDK for adaptive icons
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3" // Keep this version
    }

    packaging {
        resources {
            excludes.add("/META-INF/AL2.0")
            excludes.add("/META-INF/LGPL2.1")
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.compose.ui:ui:1.5.3")
    implementation("androidx.compose.material:material:1.5.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.3")
    implementation("androidx.navigation:navigation-compose:2.7.0")
    implementation("androidx.activity:activity-compose:1.7.2")
    // Add any other dependencies you may need here
}
