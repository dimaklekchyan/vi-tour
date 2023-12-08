plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "${Config.packageName}.design_system"
    compileSdk = Config.targetSdk

    defaultConfig {
        minSdk = Config.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = Config.jvmTarget
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.Compose.compilerVersion
    }
}

dependencies {

    implementation(project(":core"))

    //Compose
    implementation(platform(Dependencies.Compose.bom))
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.materialIcons)

    //Kotlin
    implementation(Dependencies.Kotlin.coroutines)
    implementation(Dependencies.Kotlin.serialization)

    //AndroidX
    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.activityCompose)

    //Accompanist
    implementation(Dependencies.Accompanist.systemUiController)
}