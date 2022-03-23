import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

val sqlDelightVersion: String by project

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")

//    kotlin("kotlin-android-extensions") version "1.5.10"

    kotlin("plugin.serialization")
    id("com.squareup.sqldelight")
}

version = "1.0"

kotlin {
//    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
//        binaries.withType<org.jetbrains.kotlin.gradle.plugin.mpp.Framework> {
//            isStatic = false
//        }
//    }

    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iosTarget("ios") {}

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
//        ios.deploymentTarget = "14.1"
        ios.deploymentTarget = "14.5"
//        ios.deploymentTarget = "15.0"
        frameworkName = "shared"
        podfile = project.file("../iosApp/Podfile")
    }
    
    sourceSets {
        all {
            languageSettings.useExperimentalAnnotation("kotlin.RequiresOptIn")
            languageSettings.useExperimentalAnnotation("okio.ExperimentalFileSystem")
        }
//        val okioVersion = "3.XXX"

        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-native-mt")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.1")
                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
                implementation("com.squareup.okio:okio-multiplatform:3.0.0-alpha.6")

//                implementation("co.touchlab:stately-common:1.1.4")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
//                implementation("com.squareup.okio:okio-fakefilesystem:3.0.0-alpha.6")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
            }
        }
        val iosTest by getting
    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(16)
        targetSdkVersion(30)
    }
}
dependencies {
    implementation("androidx.appcompat:appcompat:1.3.0")
}

sqldelight {
    database("TestsDatabase") {
        packageName = "com.yoloapps.reactiontimetracker.data.db"
    }
}