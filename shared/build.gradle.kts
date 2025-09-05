import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqlDelight)
    alias (libs.plugins.composeMultiplatform)
    id("org.jetbrains.kotlin.plugin.serialization") version libs.versions.kotlin
    id("org.jetbrains.kotlin.plugin.compose") version libs.versions.kotlin

}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    
    jvm()

    js(IR){
        binaries.executable()
        browser()
    }

    // aplica o -lsqlite3 a todos os binários nativos (inclui iOS)
    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.all {
            linkerOpts("-lsqlite3")
        }
    }
    
    sourceSets {
        commonMain.dependencies {

            //ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            //coroutines
            implementation(libs.kotlinx.datetime)
            implementation(libs.sql.coroutines.extensions)
            implementation(libs.kotlinx.coroutines.core)

            //koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            //Compose multiplatform
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.material)
            implementation(compose.runtime)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)

            //Kamel (Questões de AsyncImages)
            implementation(libs.kamel.image)

            //Voyager (Navegação entre telas)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }


        //Adicionando depenências específicas para o módulo Android
        androidMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodel.ktx)
            implementation(libs.ktor.client.android)
            implementation(libs.sql.android.driver)
        }

        //Adicionando dependências específicas para o módulo IOS
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sql.native.driver)
        }

        val jvmMain by getting{
            dependencies {
                implementation(libs.ktor.client.cio)
                implementation(libs.sql.desktop.driver)
            }
        }

        val jsMain by getting{
            dependencies{
                implementation(libs.ktor.client.js)

            }
        }
    }
}

android {
    namespace = "br.com.correios.ppm.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

sqldelight {
    databases {
        create(name = "LogDatabase") {
            packageName.set("br.com.correios.ppm.db")
        }
    }
}
