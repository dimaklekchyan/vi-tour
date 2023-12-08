plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "${Config.packageName}.core_network"
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
}

dependencies {

    implementation(project(":core"))

    //Hilt
    implementation(Dependencies.Hilt.hilt)
    kapt(Dependencies.Hilt.compiler)

    //Kotlin
    implementation(Dependencies.Kotlin.coroutines)
    implementation(Dependencies.Kotlin.serialization)

    //AndroidX
    implementation(Dependencies.AndroidX.core)

    //Retrofit
    implementation(Dependencies.Retrofit.retrofit)
    implementation(Dependencies.Retrofit.serializationConverter)

    //OkHttp
    implementation(Dependencies.OkHttp.logging)
}