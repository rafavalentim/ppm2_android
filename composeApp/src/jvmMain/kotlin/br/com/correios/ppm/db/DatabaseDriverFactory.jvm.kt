package br.com.correios.ppm.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import br.com.correios.ppm.LogDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver? {
         return JdbcSqliteDriver(
             url = "jdbc:sqlite:LogDatabase.db",
             schema = LogDatabase.Schema
         )
    }
}