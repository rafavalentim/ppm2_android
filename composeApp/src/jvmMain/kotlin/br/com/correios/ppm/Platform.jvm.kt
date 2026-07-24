package br.com.correios.ppm

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override val type: PlatformType = PlatformType.JVM
}

actual fun getPlatform(): Platform = JVMPlatform()