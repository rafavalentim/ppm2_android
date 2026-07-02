import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.sqlDelight)
    id("org.jetbrains.kotlin.plugin.serialization") version libs.versions.kotlin
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
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    // aplica o -lsqlite3 a todos os binários nativos (inclui iOS)
    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.all {
            linkerOpts("-lsqlite3")
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(compose.uiTooling)
            implementation(libs.androidx.activity.compose)

            implementation(libs.androidx.lifecycle.viewmodel.ktx)
            implementation(libs.ktor.client.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sql.android.driver)
        }
        commonMain.dependencies {

            // Módulos internos
            implementation(project(":core"))
            implementation(project(":concursos"))

            //Compose
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)

            //AndroidX
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            //ktor (Acesso às api's em substituição ao Retrofit)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)

            //coroutines
            implementation(libs.kotlinx.datetime)
            implementation(libs.sql.coroutines.extensions)
            implementation(libs.kotlinx.coroutines.core)

            //koin (Injeção de dependência alternativa ao Hilt que não funciona no kmp)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            //Kamel (Questões de AsyncImages)
            implementation(libs.kamel.image)

            //Voyager (Navegação entre telas)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)

            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.sql.desktop.driver)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sql.native.driver)
        }

        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)

            implementation(libs.runtime.wasm.js)
            implementation(libs.web.worker.driver.wasm.js)
            implementation(libs.kotlinx.browser) // verifique versão mais nova


            // NPM do worker SQL.js (usado pelo driver)
            implementation(devNpm("@cashapp/sqldelight-sqljs-worker", "2.1.0"))
            // Se o seu setup usar webpack, a doc recomenda também:
            implementation(devNpm("copy-webpack-plugin", "9.1.0"))

        }
    }
}

android {
    namespace = "br.com.correios.ppm"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "br.com.correios.ppm"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            buildConfigField("String", "BASE_URL", "\"https://applogisticades.correios.com.br\"")
        }
        create("hom") {
            dimension = "environment"
            applicationIdSuffix = ".hom"
            buildConfigField("String", "BASE_URL", "\"https://applogisticahom.correios.com.br\"")
        }
        create("prod") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"https://applogistica.correios.com.br\"")
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

sqldelight {
    databases {
        create(name = "LogDatabase") {
            packageName.set("br.com.correios.ppm")
        }
    }
}

compose.desktop {
    application {
        mainClass = "br.com.correios.ppm.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "br.com.correios.ppm"
            packageVersion = "1.0.0"
        }
    }
}