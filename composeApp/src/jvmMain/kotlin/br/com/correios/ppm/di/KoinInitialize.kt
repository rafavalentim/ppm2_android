package br.com.correios.ppm.di

import br.com.correios.ppm.concursos.data.GestaoLogisticaService
import br.com.correios.ppm.login.data.LoginService
import br.com.correios.ppm.unidade.data.UnidadeService
import org.koin.core.context.startKoin
import org.koin.dsl.module

private const val JVM_BASE_URL = "https://applogisticahom.correios.com.br"
private const val JVM_BASE_URL_CONCURSOS = "/rest/gestaologistica/v1"

fun initKoin() = startKoin {
    val jvmModule = module {
        single { LoginService(get(), JVM_BASE_URL) }
        single { UnidadeService(get(), JVM_BASE_URL) }
        single { GestaoLogisticaService(get(), JVM_BASE_URL, JVM_BASE_URL_CONCURSOS) }
    }
    modules(sharedModules + databaseModule + jvmModule)
}.koin