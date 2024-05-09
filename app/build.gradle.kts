plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.socialmediaapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.socialmediaapp"
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
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))

    //Storage
    implementation("com.google.firebase:firebase-storage")

    //Firebase Authentication
    implementation("com.google.firebase:firebase-auth")

    //Firestore
    implementation("com.google.firebase:firebase-firestore")

    //Firestore UI
    implementation("com.firebaseui:firebase-ui-firestore:8.0.2")

    //ImagePicker
    implementation("com.github.dhaval2404:imagepicker:2.1")

    //DateTimeUtils
    implementation("com.github.thunder413:DateTimeUtils:3.0")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //CircleImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("com.google.android.gms:play-services-cast-framework:21.4.0")
    implementation("androidx.activity:activity:1.8.2")
    implementation("androidx.activity:activity-ktx:1.8.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}



