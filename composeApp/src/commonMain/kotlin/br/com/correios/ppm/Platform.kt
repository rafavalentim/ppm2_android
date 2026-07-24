package br.com.correios.ppm

enum class PlatformType {
    ANDROID, IOS, JVM, WASM
}

interface Platform {
    val name: String
    val type: PlatformType
}

expect fun getPlatform(): Platform