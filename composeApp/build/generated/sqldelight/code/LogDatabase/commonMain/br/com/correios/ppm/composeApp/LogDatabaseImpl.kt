package br.com.correios.ppm.composeApp

import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import br.com.correios.ppm.LogDatabase
import kotlin.Long
import kotlin.Unit
import kotlin.reflect.KClass

internal val KClass<LogDatabase>.schema: SqlSchema<QueryResult.Value<Unit>>
  get() = LogDatabaseImpl.Schema

internal fun KClass<LogDatabase>.newInstance(driver: SqlDriver): LogDatabase =
    LogDatabaseImpl(driver)

private class LogDatabaseImpl(
  driver: SqlDriver,
) : TransacterImpl(driver),
    LogDatabase {
  public object Schema : SqlSchema<QueryResult.Value<Unit>> {
    override val version: Long
      get() = 1

    override fun create(driver: SqlDriver): QueryResult.Value<Unit> = QueryResult.Unit

    override fun migrate(
      driver: SqlDriver,
      oldVersion: Long,
      newVersion: Long,
      vararg callbacks: AfterVersion,
    ): QueryResult.Value<Unit> = QueryResult.Unit
  }
}
