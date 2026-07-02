package br.com.correios.ppm.di

import br.com.correios.ppm.concursos.data.ConcursoService
import br.com.correios.ppm.login.data.LoginService
import br.com.correios.ppm.unidade.data.UnidadeService
import org.koin.core.context.startKoin
import org.koin.dsl.module

private const val WASM_BASE_URL = "https://applogisticahom.correios.com.br"

fun initKoin() = startKoin {
    val wasmModule = module {
        single { LoginService(get(), WASM_BASE_URL) }
        single { UnidadeService(get(), WASM_BASE_URL) }
        single { ConcursoService(get(), WASM_BASE_URL) }
    }
    modules(sharedModules + databaseModule + wasmModule)
}.koin
