package br.com.correios.ppm.login.di

import br.com.correios.ppm.login.application.LoginUseCase
import br.com.correios.ppm.login.data.LoginRepository
import br.com.correios.ppm.login.data.LoginService
import br.com.correios.ppm.login.presentation.LoginViewModel
import org.koin.dsl.module

val loginModule = module {
    single <LoginService>{ LoginService(get()) }
    single <LoginUseCase>{ LoginUseCase(get()) }
    single <LoginViewModel>{ LoginViewModel(get()) }
    single <LoginRepository> { LoginRepository(get()) }
}