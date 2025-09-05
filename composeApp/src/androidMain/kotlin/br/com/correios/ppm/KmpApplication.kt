package br.com.correios.ppm

import android.app.Application

class KmpApplication : Application() {

    //Inicializando o Koin para injeção automática de dependência
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    //Inicializando o koin no contexto da aplicação:
    private fun initKoin(){

    }



}