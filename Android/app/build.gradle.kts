plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.ramobeko.ocsandroidapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ramobeko.ocsandroidapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:5831\"")
        buildConfigField("String", "AES_TRANSFORMATION", "\"AES/GCM/NoPadding\"")
        buildConfigField("String", "KEY_ALIAS", "\"secure_key\"")
        buildConfigField("String", "PREF_NAME", "\"secure\"")
        buildConfigField("String", "TOKEN_KEY", "\"token\"")
        buildConfigField("String", "IV_KEY", "\"iv\"")


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
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.security.state)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}