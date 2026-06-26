package br.com.correios.ppm.di

import br.com.correios.ppm.login.di.loginModule
import br.com.correios.ppm.main.di.mainModule
import br.com.correios.ppm.splash.di.splashScreenModule
import br.com.correios.ppm.unidade.di.unidadeModule

val sharedModules = listOf(
    loginModule,
    splashScreenModule,
    networkModule,
    mainModule,
    unidadeModule
)