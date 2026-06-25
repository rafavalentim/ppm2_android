package br.com.correios.ppm

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import br.com.correios.ppm.composeApp.newInstance
import br.com.correios.ppm.composeApp.schema
import kotlin.Unit

public interface LogDatabase : Transacter {
  public companion object {
    public val Schema: SqlSchema<QueryResult.Value<Unit>>
      get() = LogDatabase::class.schema

    public operator fun invoke(driver: SqlDriver): LogDatabase =
        LogDatabase::class.newInstance(driver)
  }
}
