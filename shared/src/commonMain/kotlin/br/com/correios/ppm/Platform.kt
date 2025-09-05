package br.com.correios.ppm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform