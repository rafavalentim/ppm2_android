package br.com.correios.ppm

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
    override val type: PlatformType = PlatformType.WASM
}

actual fun getPlatform(): Platform = WasmPlatform()