package br.com.correios.ppm.di

import br.com.correios.ppm.concursos.data.GestaoLogisticaService
import br.com.correios.ppm.login.data.LoginService
import br.com.correios.ppm.unidade.data.UnidadeService
import org.koin.core.context.startKoin
import org.koin.dsl.module

private const val IOS_BASE_URL = "https://applogisticahom.correios.com.br"
private const val IOS_BASE_URL_CONCURSOS = "/rest/gestaologistica/v1"

fun initKoin() {
    val iosModule = module {
        single { LoginService(get(), IOS_BASE_URL) }
        single { UnidadeService(get(), IOS_BASE_URL) }
        single { GestaoLogisticaService(get(), IOS_BASE_URL, IOS_BASE_URL_CONCURSOS) }
    }
    val modules = sharedModules + databaseModule + iosModule

    startKoin {
        modules(modules)
    }
}