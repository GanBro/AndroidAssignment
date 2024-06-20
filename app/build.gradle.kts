plugins {
    id("com.android.application") version "8.3.1"
    id("org.jetbrains.kotlin.android") version "1.8.0"
    id("org.jetbrains.kotlin.kapt") version "1.8.0"
}

android {
    namespace = "com.ganbro.shopmaster"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.ganbro.shopmaster"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        // 指定使用的 Java 版本
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }

        // 指定使用的 Kotlin 版本
        kotlinOptions {
            jvmTarget = "17"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0")
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.room:room-runtime:2.5.1")
    kapt("androidx.room:room-compiler:2.5.1")
    implementation("com.squareup.picasso:picasso:2.8")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}