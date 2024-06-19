plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
}

android {
    namespace = "com.chhorvorn.material"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.chhorvorn.material"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    android {
        buildFeatures {
            viewBinding = true
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    val camerax_version = "1.2.2"
    //noinspection GradleDependency
    implementation("androidx.camera:camera-core:${camerax_version}")
    //noinspection GradleDependency
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    //noinspection GradleDependency
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    //noinspection GradleDependency
    implementation("androidx.camera:camera-video:${camerax_version}")

    //noinspection GradleDependency
    implementation("androidx.camera:camera-view:${camerax_version}")
    //noinspection GradleDependency
    implementation("androidx.camera:camera-extensions:${camerax_version}")

    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    implementation("com.google.android.material:material:1.4.0")
    implementation("com.google.firebase:firebase-auth")
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-analytics")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}