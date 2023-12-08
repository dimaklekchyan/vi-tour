plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "${Config.packageName}.core_navigation"
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
    //Voyager
    implementation(Dependencies.Voyager.navigator)
    implementation(Dependencies.Voyager.tabNavigator)
    implementation(Dependencies.Voyager.transitions)
    implementation(Dependencies.Voyager.androidViewModel)
    implementation(Dependencies.Voyager.hilt)

    //Compose
    implementation(platform(Dependencies.Compose.bom))
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.material)

    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.activityCompose)
}