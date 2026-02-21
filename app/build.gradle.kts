plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.artemis.volumeswipe"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.artemis.volumeswipe"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "2.00"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Xposed API (local jar – GUARANTEED)
    compileOnly(files("libs/api-82.jar"))
}