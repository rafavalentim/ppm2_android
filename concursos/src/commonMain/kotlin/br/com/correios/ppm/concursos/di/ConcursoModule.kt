package br.com.correios.ppm.concursos.di

import br.com.correios.ppm.concursos.application.AtualizaStatusObjetoUseCase
import br.com.correios.ppm.concursos.application.ConcursoUseCase
import br.com.correios.ppm.concursos.application.EdicaoUseCase
import br.com.correios.ppm.concursos.data.AtualizaStatusObjetoRepository
import br.com.correios.ppm.concursos.data.ConcursoRepository
import br.com.correios.ppm.concursos.data.EdicaoRepository
import br.com.correios.ppm.concursos.presentation.AtualizaStatusObjetosViewModel
import br.com.correios.ppm.concursos.presentation.CadastroEdicaoViewModel
import br.com.correios.ppm.concursos.presentation.ConcursoViewModel
import br.com.correios.ppm.concursos.presentation.MainConcursosViewModel
import org.koin.dsl.module

val concursoModule = module {
    single { ConcursoRepository(get()) }
    single { ConcursoUseCase(get()) }
    single { ConcursoViewModel(get()) }
    single { MainConcursosViewModel(get(), get()) }
    single { EdicaoRepository(get()) }
    single { EdicaoUseCase(get()) }
    single { AtualizaStatusObjetoRepository(get()) }
    single { AtualizaStatusObjetoUseCase(get()) }
    single { CadastroEdicaoViewModel(get(), get(), get()) }
    single { AtualizaStatusObjetosViewModel(get(), get()) }
}
