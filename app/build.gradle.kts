plugins {
    id("com.android.application") version "8.7.3"
    id("org.jetbrains.kotlin.android") version "2.0.21"
}

android {
    namespace = "com.aether.rumblepocket"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.aether.rumblepocket"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.3.0"
    }
}
