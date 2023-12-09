plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    kotlin("plugin.serialization")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "${Config.packageName}.data_video"
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
    implementation(project(":core-network"))

    //Hilt
    implementation(Dependencies.Hilt.hilt)
    kapt(Dependencies.Hilt.compiler)

    //Compose
    implementation(platform(Dependencies.Compose.bom))
    implementation(Dependencies.Compose.runtime)

    //Kotlin
    implementation(Dependencies.Kotlin.coroutines)
    implementation(Dependencies.Kotlin.serialization)

    //Retrofit
    implementation(Dependencies.Retrofit.retrofit)

    //AndroidX
    implementation(Dependencies.AndroidX.preference)
    implementation(Dependencies.AndroidX.core)

    //Room
    api(Dependencies.Room.runtime)
    kapt(Dependencies.Room.compiler)
    implementation(Dependencies.Room.ktx)
}