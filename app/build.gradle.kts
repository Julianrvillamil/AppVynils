plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.navigation.safeargs)
    id("kotlin-kapt")
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
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
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

    // Pruebas unitarias y de integración
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    // Pruebas instrumentadas
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)




    // Mockito-Kotlin (opcional, para mejorar la integración con Kotlin)
    testImplementation(libs.mockito.kotlin)

    // Biblioteca de pruebas de corrutinas si usas funciones suspend
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    implementation(libs.glide)
    implementation(libs.glide.okhttp)
    kapt(libs.glide.compiler)
    implementation(libs.okhttp)

    //coroutines
    implementation(libs.coroutines)

    //room
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    androidTestImplementation("androidx.room:room-testing:2.6.1")


}