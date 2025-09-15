package br.com.correios.ppm.splash.di

import br.com.correios.ppm.splash.presentation.SplashScreenViewModel
import org.koin.dsl.module

val splashScreenModule = module {
    single <SplashScreenViewModel>{ SplashScreenViewModel(get()) }

}
