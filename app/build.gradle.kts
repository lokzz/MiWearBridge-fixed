import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.batareya16.miWearBridge"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.batareya16.miWearBridge"
        minSdk = 26
        targetSdk = 33
        versionCode = 3
        versionName = "2.1"
    }

    signingConfigs {
        // Pinned keystore committed in this repo => identical signature on every CI run.
        // (An Xposed module signature grants no privileges; this is for install stability.)
        create("pinned") {
            storeFile = file("signing/module.p12")
            storePassword = "modulepass"
            keyAlias = "module"
            keyPassword = "modulepass"
            storeType = "PKCS12"
        }
    }

    buildTypes {
        named("release") {
            // Keep minify off: this is a small Xposed module and R8 can break the hooks
            // (reflective class/method lookups). Re-enable only with proper keep rules.
            isMinifyEnabled = false
            proguardFiles("proguard-rules.pro")
            signingConfig = signingConfigs.getByName("pinned")
        }
        named("debug") {
            versionNameSuffix = "-debug-" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now())
            signingConfig = signingConfigs.getByName("pinned")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    compileOnly("de.robv.android.xposed:api:82")
    implementation("com.github.kyuubiran:EzXHelper:2.2.1")
    implementation("org.luckypray:dexkit:2.0.7")
}