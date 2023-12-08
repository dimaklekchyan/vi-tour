plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "${Config.packageName}.app"
    compileSdk = Config.compileSdk

    defaultConfig {
        applicationId = Config.applicationId
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        versionCode = Config.versionCode
        versionName = Config.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = Config.sourceCompatibility
        targetCompatibility = Config.targetCompatibility
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":core-network"))
    implementation(project(":core-navigation"))
    implementation(project(":design-system"))
    implementation(project(":data-video"))
    implementation(project(":feature-video"))
    implementation(project(":feature-start"))

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

    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.activityCompose)
    implementation(Dependencies.Lifecycle.runtime)
}