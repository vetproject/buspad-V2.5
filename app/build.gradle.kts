plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.project.buspad_25"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.project.buspad_25"
        minSdk = 26
        // noinspection OldTargetApi
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("com.jcraft:jsch:0.1.55")
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("com.google.android.exoplayer:exoplayer:2.18.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.recyclerview)
}