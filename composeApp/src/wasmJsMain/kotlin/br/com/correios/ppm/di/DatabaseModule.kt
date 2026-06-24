package br.com.correios.ppm.di

import app.cash.sqldelight.db.SqlDriver
import br.com.correios.ppm.LogDatabase
import br.com.correios.ppm.db.DatabaseDriverFactory
import org.koin.dsl.module

val databaseModule = module {
    single<SqlDriver?> { DatabaseDriverFactory().createDriver() }
    single<LogDatabase?> {
        val driver: SqlDriver? = get()
        driver?.let { LogDatabase(it) }
    }
}