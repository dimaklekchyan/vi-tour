object Dependencies {

    object Kotlin {
        const val version = "1.9.10"
        private const val coroutinesVersion = "1.7.0"
        private const val serializationVersion = "1.4.1"
        private const val collectionsVersion = "0.3.5"

        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
        const val collections = "org.jetbrains.kotlinx:kotlinx-collections-immutable:$collectionsVersion"
    }

    object Compose {
        private const val bomVersion = "2023.09.02"
        const val compilerVersion = "1.5.3"
        private const val constraintLayoutVersion = "1.0.0"

        const val bom = "androidx.compose:compose-bom:$bomVersion"
        const val ui = "androidx.compose.ui:ui"
        const val runtime = "androidx.compose.runtime:runtime"
        const val tooling = "androidx.compose.ui:ui-tooling"
        const val preview = "androidx.compose.ui:ui-tooling-preview"
        const val material = "androidx.compose.material:material"
        const val materialIcons = "androidx.compose.material:material-icons-extended"
        const val foundation = "androidx.compose.foundation:foundation"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:$constraintLayoutVersion"
    }

    object Hilt {
        private const val version = "2.48.1"
        private const val navigationVersion = "1.0.0"

        const val hilt = "com.google.dagger:hilt-android:$version"
        const val compiler = "com.google.dagger:hilt-android-compiler:$version"
        const val navigation = "androidx.hilt:hilt-navigation-compose:$navigationVersion"
    }

    object Voyager {
        private const val version = "1.0.0-rc10"

        const val navigator = "cafe.adriel.voyager:voyager-navigator:$version"
        const val tabNavigator = "cafe.adriel.voyager:voyager-tab-navigator:$version"
        const val transitions = "cafe.adriel.voyager:voyager-transitions:$version"
        const val androidViewModel = "cafe.adriel.voyager:voyager-androidx:$version"
        const val hilt = "cafe.adriel.voyager:voyager-hilt:$version"
    }

    object Retrofit {
        const val version = "2.9.0"
        const val serializationVersion = "0.8.0"

        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val serializationConverter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:$serializationVersion"
    }

    object OkHttp {
        private const val version = "4.10.0"

        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val logging = "com.squareup.okhttp3:logging-interceptor:$version"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$version"
    }

    object Lifecycle {
        private const val lifecycleVersion = "2.4.1"
        private const val composeViewModelVersion = "2.4.0-rc01"
        private const val extensionsVersion = "2.2.0"

        const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"
        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-compose:$composeViewModelVersion"
        const val extensions = "androidx.lifecycle:lifecycle-extensions:$extensionsVersion"
    }

    object Accompanist {
        private const val version = "0.32.0"

        const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:$version"
    }

    object Coil {
        private const val version = "2.4.0"

        const val compose = "io.coil-kt:coil-compose:$version"
        const val svg = "io.coil-kt:coil-svg:$version"
    }

    object Room {
        private const val version = "2.6.0-rc01"

        const val runtime = "androidx.room:room-runtime:$version"
        const val compiler = "androidx.room:room-compiler:$version"
        const val ktx = "androidx.room:room-ktx:$version"
    }

    object Firebase {
        private const val bomVersion = "32.0.0"

        const val bom = "com.google.firebase:firebase-bom:$bomVersion"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val remoteConfig = "com.google.firebase:firebase-config-ktx"
        const val messaging = "com.google.firebase:firebase-messaging-ktx"
    }

    object AndroidX {
        private const val coreVersion = "1.10.0"
        private const val appCompatVersion = "1.6.1"
        private const val activityComposeVersion = "1.7.1"
        private const val lifecycleViewmodelVersion = "2.6.1"
        private const val preferenceVersion = "1.2.0"

        const val core = "androidx.core:core-ktx:$coreVersion"
        const val preference = "androidx.preference:preference-ktx:$preferenceVersion"
        const val appCompat = "androidx.appcompat:appcompat:$appCompatVersion"
        const val appCompatResources = "androidx.appcompat:appcompat-resources:$appCompatVersion"
        const val activityCompose = "androidx.activity:activity-compose:$activityComposeVersion"
        const val lifecycleViewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleViewmodelVersion"

    }

    object Camera {
        private const val version = "1.3.0"

        const val core = "androidx.camera:camera-core:$version"
        const val camera2 = "androidx.camera:camera-camera2:$version"
        const val lifecycle = "androidx.camera:camera-lifecycle:$version"
        const val video = "androidx.camera:camera-video:$version"
        const val view = "androidx.camera:camera-view:$version"
        const val extensions = "androidx.camera:camera-extensions:$version"
    }
}