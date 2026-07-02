package br.com.correios.ppm

import android.app.Application
import android.app.Dialog
import br.com.correios.ppm.concursos.data.ConcursoService
import br.com.correios.ppm.config.AppContext
import br.com.correios.ppm.di.databaseModule
import br.com.correios.ppm.di.sharedModules
import br.com.correios.ppm.login.application.Usuario
import br.com.correios.ppm.login.data.LoginService
import br.com.correios.ppm.unidade.data.UnidadeService
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module


//Essa classe inicia os módulos inicializa o koin, responsável pelas injeção automática de
// dependências da aplicação.
class MobileApp : Application() {

    //Cirando uma função que inicializa o Koin
    override fun onCreate() {
        super.onCreate()
        initKoin()
        AppContext.appContext = applicationContext

    }

    //Inicializando o koin no contexto da aplicação.
    private fun initKoin(){
        val androidModule = module {
            single { LoginService(get(), BuildConfig.BASE_URL) }
            single { UnidadeService(get(), BuildConfig.BASE_URL) }
            single { ConcursoService(get(), BuildConfig.BASE_URL) }
        }
        val modules = sharedModules + databaseModule + androidModule

        startKoin {
            androidContext(this@MobileApp)
            modules(modules)
        }
    }


    companion object {

        const val INTENT_EXTRA_VERSION_APP: String = "extra_version_app"
        var PREF_USERNAME: String = "pref_username"
        const val DYNAMIC_FEATURE_WMS: String = "wms"
        const val URL_PROXY_APP: String = BuildConfig.BASE_URL
        const val PREF_COOKIE: String = "session_cookie"
        const val DOWNLOAD_VERSION_PATH: String = "lojaapp/v1/aplicativos/{app}/versoes/{versao}/file"
        const val PREF_DEFAUL_NAME: String = "pref_app_operacional"
        const val MCU: String = "mcu_cadastrado"
        val REMEMBER_LOGIN: String = "remember_login"
        const val TAG: String = "log"

        @JvmStatic
        var usuario: Usuario? = null

        @JvmStatic
        var dialog: Dialog? = null

        @JvmStatic
        var applicationId: String? = null
    }
}