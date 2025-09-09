package br.com.correios.ppm.di

import org.koin.core.context.startKoin

fun initKoin() = startKoin {
    modules(
        sharedModules + databaseModule
    )
}.koin