plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.navigation.safeargs)
}

android {
    namespace = "com.misw.appvynills"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.misw.appvynills"
        minSdk = 21
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.espresso.contrib)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Retrofit
    //implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    //implementation 'com.google.code.gson:gson:2.8.2'
    //implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
    //implementation 'com.squareup.retrofit2:converter-scalars:2.9.0'

    //Volley APIS REST
    //implementation 'com.android.volley:volley:1.2.1'
    implementation(libs.android.volley)
    implementation(libs.picasso)
    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.converter)

    // Mockito core para pruebas unitarias
    testImplementation(libs.mockito.core)

    // Mockito-Kotlin (opcional, para mejorar la integración con Kotlin)
    testImplementation(libs.mockito.kotlin)

    // Biblioteca de pruebas de corrutinas si usas funciones suspend
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

}