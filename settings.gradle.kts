pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("com.android")) {
                useModule("com.android.tools.build:gradle:8.1.0")
            }
            if (requested.id.id.startsWith("org.jetbrains.kotlin")) {
                useVersion("1.9.10")
            }
//            if (requested.id.id.startsWith("com.google.gms")) {
//                useModule("com.google.gms:google-services:4.3.15")
//            }
//            if (requested.id.id.startsWith("com.google.firebase")) {
//                useModule("com.google.firebase:firebase-crashlytics-gradle:2.9.5")
//            }
            if (requested.id.id.startsWith("dagger.hilt.android")) {
                useModule("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
            }
        }
    }
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
        repositories {
            google()
            mavenCentral()
        }
    }
}

rootProject.name = "ViTour"
include(":app")
include(":core")
include(":core-navigation")
include(":core-network")
include(":design-system")
include(":data-video")
include(":feature-video")
include(":feature-start")