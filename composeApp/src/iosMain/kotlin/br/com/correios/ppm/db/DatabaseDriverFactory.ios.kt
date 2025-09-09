package br.com.correios.ppm.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import br.com.correios.ppm.LogDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver? = NativeSqliteDriver (
        schema = LogDatabase.Schema,
        name = "LogDatabase.Database.db"
        )
}