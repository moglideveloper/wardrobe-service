package com.mindera
package wardrobe.core

import db._
import wardrobe.adts.Clothing
import doobie.implicits._
import scala.util.Try

object DbClothingOps extends DbContext {

  def fetchClothingByName(db: Db, name: String): SEither[Seq[Clothing]] = {

    val query = {
      val selectFragment = sql"SELECT * FROM CLOTHING WHERE "
      val whereFragment = fr"NAME ILIKE ${"%" + name + "%"}"
      val sqlQuery = selectFragment ++ whereFragment
      println(s"$name : " + sqlQuery)
      sql"$sqlQuery".query[Clothing]
    }

    val tryQueryExecution = Try {
      val data = query.to[List].transact(xa(db)).unsafeRunSync()
      data
    }

    tryQueryExecution.sEither(ServiceCodes.INTERNAL_ERROR_MSG)
  }

  def addClothing(db: Db, clothing: Clothing): SEither[Clothing] = {
    val query = {
      val sql = sql"INSERT INTO CLOTHING (NAME, CATEGORY) VALUES (${clothing.name}, ${clothing.category})"
      sql.update
    }

    val tryQueryExecution = Try {
      val recordInserted = query.run.transact(xa(db)).unsafeRunSync()
      clothing
    }

    tryQueryExecution.sEither(ServiceCodes.INTERNAL_ERROR_MSG)
  }
}
