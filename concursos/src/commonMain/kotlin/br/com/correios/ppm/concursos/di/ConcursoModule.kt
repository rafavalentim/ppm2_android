package br.com.correios.ppm.concursos.di

import br.com.correios.ppm.concursos.application.ConcursoUseCase
import br.com.correios.ppm.concursos.data.ConcursoRepository
import br.com.correios.ppm.concursos.presentation.ConcursoViewModel
import br.com.correios.ppm.concursos.presentation.MainConcursosViewModel
import org.koin.dsl.module

val concursoModule = module {
    single { ConcursoRepository(get()) }
    single { ConcursoUseCase(get()) }
    single { ConcursoViewModel(get()) }
    single { MainConcursosViewModel(get(), get()) }
}
