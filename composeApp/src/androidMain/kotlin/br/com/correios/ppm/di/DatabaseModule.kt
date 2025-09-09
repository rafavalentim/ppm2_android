package br.com.correios.ppm.di

import app.cash.sqldelight.db.SqlDriver
import br.com.correios.ppm.LogDatabase
import br.com.correios.ppm.db.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module{
    single<SqlDriver>{ DatabaseDriverFactory(androidContext()).createDriver()!! }
    single<LogDatabase>{LogDatabase(get())}
}