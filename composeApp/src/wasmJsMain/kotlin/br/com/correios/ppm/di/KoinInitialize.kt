package br.com.correios.ppm.di

import br.com.correios.ppm.concursos.data.GestaoLogisticaService
import br.com.correios.ppm.login.data.LoginService
import br.com.correios.ppm.unidade.data.UnidadeService
import org.koin.core.context.startKoin
import org.koin.dsl.module

// Vazia (relativa ao próprio localhost:8080): em dev, o webpack-dev-server faz proxy
// de /v1, /lojaapp, /aplicativos e /rest para o host real, evitando bloqueio de CORS no browser.
private const val WASM_BASE_URL = ""
private const val WASM_BASE_URL_CONCURSOS = "/rest/gestaologistica/v1"

fun initKoin() = startKoin {
    val wasmModule = module {
        single { LoginService(get(), WASM_BASE_URL) }
        single { UnidadeService(get(), WASM_BASE_URL) }
        single { GestaoLogisticaService(get(), WASM_BASE_URL, WASM_BASE_URL_CONCURSOS) }
    }
    modules(sharedModules + databaseModule + wasmModule)
}.koin
