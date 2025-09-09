package br.com.correios.ppm.di

import org.koin.core.context.startKoin

fun initKoin(){
    val modules = sharedModules + databaseModule

    startKoin {
        modules(modules)
    }
}