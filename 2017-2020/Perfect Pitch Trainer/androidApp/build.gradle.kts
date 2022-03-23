plugins {
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")

    implementation("androidx.compose.ui:ui:1.0.0-rc01")
    // Tooling support (Previews, etc.)
//    implementation("androidx.compose.ui:ui-tooling:1.0.0-rc01")
    implementation("androidx.compose.ui:ui-tooling:1.0.0-beta09")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:1.0.0-rc01")
    // Material Design
    implementation("androidx.compose.material:material:1.0.0-rc01")
    // Material design icons
    implementation("androidx.compose.material:material-icons-core:1.0.0-rc01")
    implementation("androidx.compose.material:material-icons-extended:1.0.0-rc01")
    // Integration with observables
    implementation("androidx.compose.runtime:runtime-livedata:1.0.0-rc01")
    implementation("androidx.compose.runtime:runtime-rxjava2:1.0.0-rc01")

    // UI Tests
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.0-rc01")

//    implementation("androidx.ui:ui-framework:0.1.0-dev10")
    implementation("androidx.activity:activity-compose:1.3.0-rc01")

//    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
}

android {
    compileSdk = 30
    defaultConfig {
        applicationId = "com.yoloapps.pitchtrainer.android"
//        minSdk = 16
        minSdk = 21
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    // Set both the Java and Kotlin compilers to target Java 8.

    buildFeatures {
        compose = true
    }
//    kotlinOptions {
//        jvmTarget = "1.8"
////        useIR = true
//    }
    composeOptions {
//        kotlinCompilerVersion = "1.5.21"
//        kotlinCompilerExtensionVersion = "1.3.0-rc01"
//        kotlinCompilerExtensionVersion = "1.0.0-rc01"
//        kotlinCompilerExtensionVersion = "1.0.0-rc02"
        kotlinCompilerExtensionVersion = "+"
//        kotlinCompilerExtensionVersion = "0.1.0-dev10"
//        kotlinCompilerExtensionVersion = "0.1.0-dev06" // THIS ONE is important
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}