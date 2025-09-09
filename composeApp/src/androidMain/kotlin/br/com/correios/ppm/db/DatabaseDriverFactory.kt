package br.com.correios.ppm.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import br.com.correios.ppm.LogDatabase

actual class DatabaseDriverFactory(private val context : Context) {

    actual fun createDriver() : SqlDriver? = AndroidSqliteDriver(
        schema = LogDatabase.Schema,
        context = context,
        name = "Log.Database.db"
    )
}