package br.com.correios.ppm.main.di

import br.com.correios.ppm.main.presentation.MainViewModel
import org.koin.dsl.module

val mainModule = module {
    single<MainViewModel> { MainViewModel() }
}