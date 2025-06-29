// dependency versions
object Versions {
    const val RETROFIT = "2.9.0"
    const val GSON_CONVERTER = "2.9.0"
    const val GSON = "2.10"
    const val KOIN = "3.4.0"
    const val KOIN_TEST = "3.1.3"
    const val OKHTTP = "4.11.0"
    const val OK_HTTP_LOGIN_INTERCEPTOR = "4.11.0"
    const val DATA_STORE_CORE = "1.0.0"
    const val DATA_STORE_PREF = "1.1.0-beta02"
    const val COROUTINES = "1.7.1"
    const val MOCK_WEBSERVER = "4.9.0"
    const val COROUTINES_TEST = "1.7.1"
    const val MOCKITO = "5.7.0"
    const val MOCKITO_KOTLIN = "5.2.1"
    const val CORE_TESTING = "2.2.0"
    const val COMPOSE_TESTING = "1.7.0-alpha04"
    const val COMPOSE_UI_TOOLING = "1.7.0-alpha04"
    const val RESOURCE_IDLING = "3.0.2"
    const val LIFECYCLE_COMPOSE = "2.7.0"
    const val WORK_MANAGER = "2.9.0"
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("org.jetbrains.kotlinx.kover")
}

android {

    namespace = "com.sample.currencyconversion"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sample.currencyconversion"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.test.espresso:espresso-idling-resource:3.5.1")
    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    /** dev added dependencies */

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:${Versions.RETROFIT}")
    implementation("com.squareup.retrofit2:converter-gson:${Versions.GSON_CONVERTER}")
    implementation("com.google.code.gson:gson:${Versions.GSON}")

    // Koin
    implementation("io.insert-koin:koin-android:${Versions.KOIN}")
    implementation("io.insert-koin:koin-androidx-compose:3.5.3")
    testImplementation("io.insert-koin:koin-test:${Versions.KOIN_TEST}")

    // ok http
    implementation("com.squareup.okhttp3:okhttp:${Versions.OKHTTP}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Versions.OK_HTTP_LOGIN_INTERCEPTOR}")

    // data store
    implementation("androidx.datastore:datastore-core:${Versions.DATA_STORE_CORE}")
    implementation("androidx.datastore:datastore-preferences:${Versions.DATA_STORE_PREF}")

    // coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}") // Replace with the desired version

    // mockito and coroutine unit test
    implementation("com.squareup.okhttp3:mockwebserver:${Versions.MOCK_WEBSERVER}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES_TEST}")
    testImplementation("org.mockito:mockito-core:${Versions.MOCKITO}")
    testImplementation("org.mockito.kotlin:mockito-kotlin:${Versions.MOCKITO_KOTLIN}")
    testImplementation("androidx.arch.core:core-testing:${Versions.CORE_TESTING}")

    // compose android test
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.COMPOSE_TESTING}")
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.COMPOSE_UI_TOOLING}")

    // resources idling android test
    androidTestImplementation("com.android.support.test.espresso:espresso-idling-resource:${Versions.RESOURCE_IDLING}")

    // compose runtime
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.LIFECYCLE_COMPOSE}")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")

    // work manager runtime
    implementation("androidx.work:work-runtime-ktx:${Versions.WORK_MANAGER}")

}

kover {
    currentProject {
        instrumentation {
            // Exclude JDK internals from instrumentation
            excludedClasses.add("jdk.internal.*")
        }
    }

    reports {
        filters {
            excludes {
                androidGeneratedClasses()
                classes(
                    "okhttp3.*", // Exclude all
                    "com.sample.currencyconversion.core.data.api.apistate.*",
                    "com.sample.currencyconversion.App*",
                    "com.sample.currencyconversion.ComposableSingletons*",
                    "com.sample.currencyconversion.koin.*",
                    "com.sample.currencyconversion.common.theme.*",
                    "com.sample.currencyconversion.common.ui.*",
                    "com.sample.currencyconversion.core.coroutine.*",
                    "com.sample.currencyconversion.core.util.*",
                    "com.sample.currencyconversion.freature.converter.presenter.ui.*",
                    "com.sample.currencyconversion.core.data.local.*",
                    "com.sample.currencyconversion.core.data.service.MyJobService*",
                    "com.sample.currencyconversion.core.data.scheduler.MyScheduler*"
                )
            }
        }

        total {
            xml {
                // Set to true to run koverXmlReport task during the execution of the check task
                onCheck.set(false)
            }

            html {
                // Set to true to run koverMergedHtmlReport task during the execution of the check task
                onCheck.set(false)
            }
        }
    }
}



