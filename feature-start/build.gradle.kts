plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "${Config.packageName}.feature_start"
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
    implementation(project(":core-navigation"))
    implementation(project(":design-system"))

    //Hilt
    implementation(Dependencies.Hilt.hilt)
    kapt(Dependencies.Hilt.compiler)
    implementation(Dependencies.Hilt.navigation)

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