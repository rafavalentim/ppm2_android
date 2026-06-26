package br.com.correios.ppm.unidade.di

import br.com.correios.ppm.unidade.application.UnidadeUseCase
import br.com.correios.ppm.unidade.data.UnidadeRepository
import br.com.correios.ppm.unidade.presentation.UnidadeViewModel
import org.koin.dsl.module

val unidadeModule = module {
    single { UnidadeRepository(get()) }
    single { UnidadeUseCase(get()) }
    single { UnidadeViewModel(get()) }
}
