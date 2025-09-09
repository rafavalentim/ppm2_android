package br.com.correios.ppm

import android.app.Application
import br.com.correios.ppm.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


//Essa classe inicia os módulos inicializa o koin, responsável pelas injeção automática de
// dependências da aplicação.
class MobileApp : Application() {

    //Cirando uma função que inicializa o Koin
    override fun onCreate() {
        super.onCreate()
        initKoin()

    }

    //Inicializando o koin no contexto da aplicação.
    private fun initKoin(){
        val modules = databaseModule

        startKoin {
            androidContext(this@MobileApp)
            modules(modules)
        }
    }

}